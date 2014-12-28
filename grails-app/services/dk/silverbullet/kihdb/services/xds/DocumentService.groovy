/**
 * Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.silverbullet.kihdb.services.xds

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.ServiceStorageException
import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import dk.silverbullet.kihdb.xds.EbXmlBuilder
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType
import oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import org.apache.commons.io.IOUtils
import org.springframework.transaction.annotation.Transactional

@Transactional
class DocumentService {

    def documentRegistryService

    private ObjectFactory objectFactory

    public DocumentService() {
        log.debug "In constructor"
        objectFactory = new ObjectFactory()
    }


    public RegistryResponseType persistDocuments(ProvideAndRegisterDocumentSetRequestType request) throws ServiceStorageException {

        log.debug "Document Repository Provide and Register Document Set B - " + request
        def submitObjectsRequest = request.submitObjectsRequest
        String submitObjectsRequestString = new EbXmlBuilder().convertSubmitObjectsRequestToString(submitObjectsRequest)

        // Persists documents
        XDSMetadata xdsMetadata = new XDSMetadata(data: submitObjectsRequestString)

        if (!xdsMetadata.save()) {
            log.error "Error saving metadata: " + xdsMetadata.errors
            throw new ServiceStorageException("Error storing XDS metadata", xdsMetadata.errors)
        }
        log.debug "Stored metadata OK"

        // Content of save to base
        for (def doc in request.document) {
            XDSDocument d = new XDSDocument()
            d.metadata = xdsMetadata
            String id = doc.getId()
            d.uuid = id

            log.debug 'id..:' + doc.id

            final InputStream inputStream = doc.value.getInputStream()
            d.document = IOUtils.toByteArray(inputStream)

            if (!d.save(flush: true)) {
                throw new ServiceStorageException(Constants.ERROR_TEXT_DOCUMENT_SERVICE_STORAGE_ERROR, d.errors)
            }

            xdsMetadata.addToDocuments(d)
        }

        if (!xdsMetadata.save(flush: true)) {
            throw new ServiceStorageException(Constants.ERROR_TEXT_DOCUMENT_SERVICE_STORAGE_ERROR, xdsMetadata.errors)
        }

        // register documents
        RegistryResponseType successFullyRegistered = documentRegistryService.updateRegistry(submitObjectsRequest)

        if (Constants.XDS_REGISTRY_SUCCESSFULLY_REGISTERED.equals(successFullyRegistered?.status)) {
            xdsMetadata = xdsMetadata.refresh()
            xdsMetadata.registered = true
            if (!xdsMetadata.save()) {
                log.error "Not registerered success registry registration. Errors ": xdsMetadata.errors
            }
        }

        if (Constants.XDS_REGISTRY_FAILED_REGISTERED.equals(successFullyRegistered?.status)) {
            log.error "Not registered successfully. Errors were: "
            successFullyRegistered?.registryErrorList?.each {
                it.registryError?.each {
                    log.error "Code: ${it?.errorCode} - context: ${it?.codeContext} - location: ${it?.location}"
                }
            }
        }

        if (successFullyRegistered == null) {
            log.warn "Registry not updated - it's disabled. "
            successFullyRegistered = objectFactory.createRegistryResponseType()
            successFullyRegistered.status = Constants.XDS_REGISTRY_SUCCESSFULLY_REGISTERED
        }

        log.debug "Registered document in registry: " + successFullyRegistered

        return successFullyRegistered
    }


    def persistDocuments(def xdsDocuments, boolean base64Encoded, XDSMetadata metadata) {
        def res = []
        for (XDSDocument doc in xdsDocuments) {
            doc.metadata = metadata
            res.add(persistDocument(doc, base64Encoded))
        }


        return res
    }

    def persistDocument(XDSDocument xdsDocument, boolean base64Encoded) {
        if (!xdsDocument)
            return

        if (!xdsDocument.uuid)
            xdsDocument.uuid = UUID.randomUUID() as String

        if (xdsDocument.metadata && xdsDocument.metadata?.id == null) {
            if (!xdsDocument.metadata.save(flush: true)) {
                log.error "Error saving metadata: " + xdsDocument.metadata.errors
                throw new ServiceStorageException("Error storing XDS document", xdsDocument.metadata.errors)
            }
        }

        if (!xdsDocument.save(flush: true)) {
            log.error "Error saving document: " + xdsDocument.errors
            throw new ServiceStorageException("Error storing XDS document", xdsDocument.errors)
        }

        log.debug "Stored document with UUID: " + xdsDocument.uuid

        return xdsDocument
    }
}
