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
package dk.silverbullet.kihdb.interceptors

import dk.silverbullet.kihdb.constants.Constants
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor
import org.apache.cxf.helpers.IOUtils
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.io.CachedOutputStream
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class XdsInInterceptor extends AbstractSoapInterceptor {

    private static Logger log = LoggerFactory.getLogger(XdsInInterceptor.class)
    static transactional = true


    XdsInInterceptor() {
        super(Phase.RECEIVE)
    }

    @Override
    void handleMessage(SoapMessage message) throws Fault {
        // Dont block to retrieve the WSDL
        String queryString = (String) message.get(Message.QUERY_STRING)
        if (queryString?.equals("wsdl") || queryString?.contains("wsdl=")) {
            log.info "WSDL Query detected - stopping interceptor"
            return // Step out
        }

        log.debug "*** Extract information from chain ***"

        InputStream is = message.getContent(InputStream.class)
        if (is != null) {
            CachedOutputStream bos = new CachedOutputStream()
            try {
                IOUtils.copy(is, bos)

                bos.flush()
                is.close()
                message.setContent(InputStream.class, bos.getInputStream())
                bos.close()
                String soapMessage = new String(bos.getBytes())
                // TODO Dette må kunne gøres mere smart
                String body = soapMessage.substring(soapMessage.indexOf("Body>") + 5, soapMessage.lastIndexOf("Body>")).trim()
                body = body.substring(0, body.lastIndexOf("</")).trim()
                // log.debug 'soapBody...:' + body

                message.getExchange().put(Constants.XDS_BODY_MESSAGE, body)

            } catch (IOException e) {
                log.error 'Got Exception :(', e // TODO Remove this
                throw new Fault(e)
            }
        }

        log.debug 'Bye...'
    }
}
