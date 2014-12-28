package dk.silverbullet.kihdb.service.xds

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.ServiceStorageException
import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import dk.silverbullet.kihdb.phmr.XdsMarshaller
import dk.silverbullet.kihdb.xds.EbXmlBuilder
import ihe.iti.xds_b._2007.DocumentRepositoryPortType
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType
import oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import org.apache.cxf.jaxws.context.WrappedMessageContext
import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import org.springframework.transaction.annotation.Transactional

import javax.activation.DataHandler
import javax.annotation.Resource
import javax.jws.WebParam
import javax.jws.WebService
import javax.mail.util.ByteArrayDataSource
import javax.xml.ws.WebServiceContext
import javax.xml.ws.soap.Addressing
import javax.xml.ws.soap.MTOM

@MTOM(enabled = true)
@Addressing(enabled = true, required = false)
@GrailsCxfEndpoint(soap12 = true, inInterceptors = ["sosiInInterceptor"], outInterceptors = ["sosiOutInterceptor"])
@WebService(targetNamespace = "urn:ihe:iti:xds-b:2007", name = "DocumentRepository_PortType", serviceName="DocumentRepository")
class KIHDocumentRepositoryEndpoint implements DocumentRepositoryPortType {

    // TODO: Make sure we audit log document UUID.
    static expose = EndpointType.JAX_WS
    static wsdl = ""

    static transactional = true
    static soap12 = true

//    static expose = ['cxfjax']

    @Resource
    WebServiceContext wsContext

    def sessionFactory
    def documentService
    private static ObjectFactory objectFactory


    public KIHDocumentRepositoryEndpoint() {
        log.debug "In constructor"
        objectFactory = new ObjectFactory()
    }

    @Transactional
    @Override
    RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(
            @WebParam(partName = "body", name = "RetrieveDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007") RetrieveDocumentSetRequestType body) {

        ihe.iti.xds_b._2007.ObjectFactory of = new ihe.iti.xds_b._2007.ObjectFactory()
        RetrieveDocumentSetResponseType retVal = of.createRetrieveDocumentSetResponseType()




        RegistryResponseType responseType = new RegistryResponseType()
        RetrieveDocumentSetRequestType document = body
        boolean errorOccurred = false
        for (RetrieveDocumentSetRequestType.DocumentRequest request in document.getDocumentRequest()) {
            log.debug "R: " + request.documentUniqueId + " " + request.homeCommunityId + " " + request.repositoryUniqueId

            RetrieveDocumentSetResponseType.DocumentResponse documentResponse = of.createRetrieveDocumentSetResponseTypeDocumentResponse()


            XDSDocument xdsDocument = XDSDocument.findByUuid(request.getDocumentUniqueId())
            log.debug 'Got xdsDocument..:' + xdsDocument
            if (xdsDocument) {
                documentResponse.setDocumentUniqueId(request.getDocumentUniqueId())
                documentResponse.setRepositoryUniqueId(request.getRepositoryUniqueId())
                documentResponse.setDocument(new DataHandler(new ByteArrayDataSource(xdsDocument.document, null)))
                documentResponse.setMimeType(xdsDocument.getMimeType())

                retVal.getDocumentResponse().add(documentResponse)
            } else {
                // TODO ERROR handling
                errorOccurred = true
                log.error 'Could not find XDSDocument with uuid..:' + request.getDocumentUniqueId()
            }
        }


        if (errorOccurred) {
            responseType.setStatus(Constants.XDS_REGISTRY_FAILED_REGISTERED)
        } else {
            responseType.setStatus(Constants.XDS_REGISTRY_SUCCESSFULLY_REGISTERED)
        }

        retVal.setRegistryResponse(responseType)

        return retVal
    }

    @Transactional
    @Override
    RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            @WebParam(partName = "body", name = "ProvideAndRegisterDocumentSetRequest", targetNamespace = "urn:ihe:iti:xds-b:2007") ProvideAndRegisterDocumentSetRequestType body) {

        log.debug "Document Repository Provide and Register Document Set B - " + body
        RegistryResponseType responseType
        try {
            log.debug "Invoking documentService"
            responseType = documentService.persistDocuments(body)
            log.debug "Done invoking documentService"
        } catch (ServiceStorageException e) {
            responseType = objectFactory.createRegistryResponseType()
            responseType.status = Constants.XDS_REGISTRY_FAILED_REGISTERED
            responseType.registryErrorList = objectFactory.createRegistryErrorList()
            RegistryError error = objectFactory.createRegistryError()
            error.value = e.message
            error.errorCode = Constants.ERROR_CODE_DOCUMENT_SERVICE_STORAGE_ERROR
            responseType.registryErrorList.getRegistryError().add(error)
        }


        log.debug "Registered document in registry: " + responseType?.status
        return responseType
    }

}
