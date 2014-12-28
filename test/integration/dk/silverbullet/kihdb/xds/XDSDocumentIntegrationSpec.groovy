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
package dk.silverbullet.kihdb.xds

import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import grails.test.spock.IntegrationSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class XDSDocumentIntegrationSpec extends IntegrationSpec {
    private Logger log = LoggerFactory.getLogger(XDSDocumentIntegrationSpec.class)


    void "Saving XDSdokument to the database"() {
        given: "A new xds document"
        String document = "bla.bla."
        def mimeType = null
        def xdsMetadata = null
        def xdsDocument = new XDSDocument(uuid: UUID.randomUUID() as String, document: document, mimeType: mimeType, metadata: xdsMetadata)

        when: "doc is saved"
        xdsDocument.save()

        then: "it saved successfully and can be found in the database"
        xdsDocument.errors.errorCount == 0
        xdsDocument.id != null
        XDSDocument.get(xdsDocument.id).uuid == xdsDocument.uuid
    }

    void "Saving XDSdokument with metadata to the database"() {
        given: "A new xds document"
        log.debug "start..."
        log.error 'error...'
        String document = "bla.bla."
        def mimeType = null
        def xdsMetadata = new XDSMetadata(data: "some data")
        def xdsDocument = new XDSDocument(uuid: UUID.randomUUID() as String, document: document, mimeType: mimeType, metadata: xdsMetadata)

        when: "doc is saved"
        xdsDocument.metadata.save()
        xdsDocument.save()

        then: "it saved successfully and can be found in the database"
        xdsDocument.errors.errorCount == 0
        xdsDocument.id != null
        XDSDocument.get(xdsDocument.id).uuid == xdsDocument.uuid

        xdsDocument.metadata.id != null
        xdsDocument.metadata.data == xdsMetadata.data

    }
}