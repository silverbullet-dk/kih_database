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
package dk.silverbullet.kihdb.model.xds

import dk.silverbullet.kihdb.model.PermissionName
import grails.converters.XML
import grails.plugins.springsecurity.Secured
import groovy.xml.XmlUtil

@Secured(PermissionName.NONE)
class XDSDocumentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.XDS_DOCUMENT_READ)
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [XDSDocumentInstanceList: XDSDocument.list(params), XDSDocumentInstanceTotal: XDSDocument.count()]
    }

    @Secured(PermissionName.XDS_DOCUMENT_READ)
    def show(Long id) {
        def XDSDocumentInstance = XDSDocument.get(id)
        if (!XDSDocumentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'XDSDocument.label', default: 'XDSDocument'), id])
            redirect(action: "list")
            return
        }

        String xmlDocument = new String(XDSDocumentInstance.document.decodeBase64())

        try {
            xmlDocument = XmlUtil.serialize(xmlDocument)
        } catch (Exception e) {
        }

        withFormat {
            html { [XDSDocumentInstance: XDSDocumentInstance, prettyPrint: xmlDocument] }
            xml { render xmlDocument as XML }
        }
    }

    @Secured(PermissionName.XDS_DOCUMENT_READ)
    def showUuid(String uuid) {

        def XDSDocumentInstance = XDSDocument.findByUuid(uuid)
        redirect(action: "show", id: XDSDocumentInstance.id)
    }


    @Secured(PermissionName.XDS_DOCUMENT_READ)
    def download() {

        // get from the parameters passed from the link in the GSP page
        def uuid = params.uuid

        def doc = XDSDocument.findByUuid(uuid)
        String prettyPrint

        try {
            byte[] byteArr
            if ("text/xml".equals(doc.mimeType)) {
                // for pretty printing the xml file
                def stringWriter = new StringWriter()
                def node = new XmlParser().parseText(new String(doc.document))
                def printer = new XmlNodePrinter(new PrintWriter(stringWriter))

                printer.preserveWhitespace = true
                printer.print(node)

                def newXml = stringWriter.toString().trim()
                byteArr = newXml.getBytes()
            } else {
                byteArr = doc.document
            }

            // The actual file download. This approach actually calls the save dialog of the browser.

            String filename = "kih-cda-" + uuid + ".xml"

            response.setHeader("Content-disposition", "attachment; filename=${filename}")
            response.setHeader("Content-Length", "${byteArr.length}")
            response.contentType = doc.mimeType
            response.outputStream << byteArr
        } catch (Exception e) {
        }
    }
}
