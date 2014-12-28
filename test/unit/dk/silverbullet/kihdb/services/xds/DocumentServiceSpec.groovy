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

import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(DocumentService)
@Mock([XDSDocument, XDSMetadata])
class DocumentServiceSpec extends Specification {

    def "Valid xdsdocuments get saved"() {

        given: "a new document"
        String document = "bla.bla."
        XDSMetadata meta = new XDSMetadata(data: "som meta")

        when: "the document is saved by the service"
        def xdsDocument = service.persistDocument(new XDSDocument(document: document.bytes, metadata: meta), false)

        then: "the document returned and added to the database"
        xdsDocument.document == document.bytes
        XDSDocument.findAllByUuid(xdsDocument.uuid).size() == 1

        xdsDocument.metadata.id != null
        xdsDocument.metadata.data == meta.data
    }

    def "Valid xdsmetadata get saved"() {

        given: "a set of documents"
        XDSMetadata metadata = new XDSMetadata()
        metadata.registered = false
        metadata.data = "new meta"
        def documents = [new XDSDocument(document: "doc.1"), new XDSDocument(document: "doc.2")]

        when: "the documents is saved by the service"
        def xdsDocument = service.persistDocuments(documents, false, metadata)


        then: "the document returned and added to the database"
        xdsDocument.size() == 2

        xdsDocument[0].metadata.documents.size() == 2
        xdsDocument[0].document == documents[0].document

        xdsDocument[1].metadata.documents.size() == 2
        xdsDocument[1].document == documents[1].document
    }
}
