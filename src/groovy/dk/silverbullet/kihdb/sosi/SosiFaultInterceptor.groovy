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
package dk.silverbullet.kihdb.sosi

import dk.silverbullet.kihdb.constants.Constants
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.Reply
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor
import org.apache.cxf.headers.Header
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.phase.Phase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document

import javax.xml.namespace.QName

class SosiFaultInterceptor extends AbstractSoapInterceptor {
    private static Logger log = LoggerFactory.getLogger(SosiFaultInterceptor.class)

    private SOSIFactory sosiFactory

    SosiFaultInterceptor() {
        super(Phase.PRE_PROTOCOL)
        sosiFactory = SosiUtil.createSOSIFactory()
    }


    @Override
    void handleMessage(SoapMessage message) throws Fault {
        log.debug "Handling message: " + message

        def dgwsVersion = message.getExchange().get(Constants.SOSI_DGWS_VERSION)
        def sosiMessageId = message.getExchange().get(Constants.SOSI_MESSAGE_ID)
        def sosiFlowId = message.getExchange().get(Constants.SOSI_FLOW_ID)
        def sosiFlowStatus = message.getExchange().get(Constants.SOSI_FLOW_VALUE)
        def faultCode = message.getExchange().get(Constants.SOSI_FAULT_CODE)
        def faultString = message.getExchange().get(Constants.SOSI_FAULT_STRING)

        log.debug "Fault code: " + faultCode + " reason: " + faultString

        def secXsd = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
        def secTag = "Security"
        def qname = new QName(secXsd, secTag)

        if (!dgwsVersion) dgwsVersion = "1.0.1"
        if (!sosiMessageId) sosiMessageId = "erorrmessage"
        if (!sosiFlowId) sosiFlowId = "flow id"
        Reply reply = sosiFactory.createNewErrorReply(dgwsVersion, sosiMessageId, sosiFlowId, faultCode.toString(), faultString)

        Document doc = reply.serialize2DOMDocument()

        def soapEnvelope = doc.getFirstChild()
        def soapHeader = soapEnvelope.getFirstChild()
        def secHeader = soapHeader.getFirstChild()

        log.debug "GOt header: " + secHeader

        def soapSecHeadr = new SoapHeader(qname, secHeader)
        List<Header> headers = message.getHeaders()

        headers.add(soapSecHeadr)
        f
    }
}
