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

import dk.silverbullet.kih.api.auditlog.util.AuditLogUtil
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.UnauthorizedException
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.ModelException
import dk.sosi.seal.model.Request
import dk.sosi.seal.model.constants.FlowStatusValues
import dk.sosi.seal.modelbuilders.ModelBuildException
import dk.sosi.seal.xml.XmlUtil
import dk.sosi.seal.xml.XmlUtilException
import grails.util.Holders
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor
import org.apache.cxf.binding.soap.interceptor.EndpointSelectionInterceptor
import org.apache.cxf.binding.soap.interceptor.ReadHeadersInterceptor
import org.apache.cxf.headers.Header
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document

import javax.servlet.http.HttpServletRequest
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.namespace.QName

public class SosiInInterceptor extends AbstractSoapInterceptor {
    private static Logger log = LoggerFactory.getLogger(SosiInInterceptor.class)

    private QName securityQname = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security")
    private QName medcomQname = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Header")

    private DGWSProvider dgwsProvider

    private boolean disableSosiCheck = false

    private SOSIFactory factory

    public SosiInInterceptor() {
        super(Phase.READ);
        addAfter(ReadHeadersInterceptor.class.getName());
        addAfter(EndpointSelectionInterceptor.class.getName());

        this.factory = SosiUtil.createSOSIFactory()
        dgwsProvider = new DGWSProvider(this.factory)

        def isDisabledValue = Holders?.grailsApplication?.config?.seal?.check?.disable

        disableSosiCheck = (isDisabledValue ? Boolean.valueOf(isDisabledValue) : false)

    }


    private <T> T toJaxb(Class<T> jaxbClass, org.w3c.dom.Node node) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(jaxbClass);
            JAXBElement<T> element = context.createUnmarshaller().unmarshal(node, jaxbClass);
            return element.getValue();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @SuppressWarnings("rawtypes")
    private Marshaller getMarshaller(Class clazz) throws JAXBException {
        Marshaller m = JAXBContext.newInstance(clazz).createMarshaller();
//        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new SealNamespacePrefixMapper());
        return m;
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        message.getExchange().put(Constants.AUDIT_START_TIME, System.currentTimeMillis())
        message.getExchange().put(Constants.AUDIT_URL, message.get("org.apache.cxf.request.url"))

        HttpServletRequest req = (HttpServletRequest) message.get("HTTP.REQUEST");
        message.getExchange().put(Constants.AUDIT_IP, AuditLogUtil.extractIpFromRequest(req))

        log.info "Request: " + message.get(Message.REQUEST_URI)

        // Dont block to retrieve the WSDL
        String queryString = (String) message.get(Message.QUERY_STRING)
        if (queryString?.equals("wsdl") || queryString?.contains("wsdl=")) {
            log.info "WSDL Query detected - stopping interceptor"
            return // Step out
        } else {
            log.debug "Checking certificates"
        }

        def isAuthorized = true
        def rejectReason = ""



        String soapRequest
        def sosiDgwsVersion
        def sosiMessageId
        def sosiFlowId
        def sosiFlowValue


        if (!disableSosiCheck) {
            Header secHeader = message.getHeader(securityQname)
            Header medcomHeader = message.getHeader(medcomQname)
            log.debug "Headers - security: " + secHeader + " medcom: " + medcomHeader

            if (secHeader && medcomHeader) {

                org.w3c.dom.Node sn = ((org.w3c.dom.Node) secHeader.getObject())
                org.w3c.dom.Node mn = ((org.w3c.dom.Node) medcomHeader.getObject())

                Document docReq = sn.getOwnerDocument()

                soapRequest = XmlUtil.node2String(docReq, false, true)

                String soap12Url = "http://www.w3.org/2003/05/soap-envelope"
                String soap11url = "http://schemas.xmlsoap.org/soap/envelope/"
                // WARNING: Gigatum hack to handle soap 1.2 UGLY UGLY
                if (soapRequest.contains(soap12Url)) {
                    // Replace with 11
                    log.debug "Modifying: \n" + soapRequest
                    log.info "SOAP 1.2 Hack engaged"
                    soapRequest = soapRequest.replaceAll(soap12Url, soap11url)
                }
                log.debug "Handling request:\n" + soapRequest

                (isAuthorized, rejectReason, sosiDgwsVersion, sosiMessageId, sosiFlowId) = validateMessage(soapRequest, message)
                log.debug "Valid id card? " + isAuthorized
            } else {
                isAuthorized = false
                rejectReason = "No security header found in request"
            }

        }

        // Added manuel override
        if (disableSosiCheck) {
            log.warn "SOSI Override engaged - ignored validity of SOSI headers"
            isAuthorized = true

            message.getExchange().put(Constants.AUDIT_IDCARD, null)
            message.getExchange().put(Constants.AUDIT_ISSUER, null)

            message.getExchange().put(Constants.SOSI_DGWS_VERSION, "1.0.1")
            message.getExchange().put(Constants.SOSI_MESSAGE_ID, "overridden")
            message.getExchange().put(Constants.SOSI_FLOW_ID, "overridden")


        }

        if (isAuthorized) { // Override
            log.debug "Request is authorized: "
            return
        } else {
            log.error "Requested is not authorized, aborting filter chain. Reason: " + rejectReason

            message.getExchange().put(Constants.AUDIT_IDCARD, null)
            message.getExchange().put(Constants.AUDIT_ISSUER, null)

            message.getExchange().put(Constants.SOSI_DGWS_VERSION, sosiDgwsVersion)
            message.getExchange().put(Constants.SOSI_MESSAGE_ID, sosiMessageId)
            message.getExchange().put(Constants.SOSI_FLOW_ID, sosiFlowId)
            message.getExchange().put(Constants.SOSI_FAULT_CODE, Constants.SOSI_FAULT_UNAUTHORIZED_VALUE)
            message.getExchange().put(Constants.SOSI_FAULT_STRING, rejectReason)
            throw new Fault(new UnauthorizedException(rejectReason.toString()))
        }
    }

    private List validateMessage(String soapRequest, SoapMessage message) {
        def sosiFlowId, sosiFlowValue, rejectReason, sosiDgwsVersion, isAuthorized, sosiMessageId
        try {
            Request requestHeader = factory.deserializeRequest(soapRequest);
            IDCard idCard = requestHeader.getIDCard();


            if (!idCard.validInTime) {
                isAuthorized = false
                rejectReason = "Card has expired"
            }

            isAuthorized = true

            // Create the reply header
            sosiDgwsVersion = requestHeader.getDGWSVersion()
            sosiMessageId = requestHeader.getMessageID()
            sosiFlowId = requestHeader.getFlowID()
            sosiFlowValue = FlowStatusValues.FLOW_FINALIZED_SUCCESFULLY

            String callingOrganisation = "Issued: " + idCard.issuer + " for " + idCard.getAlternativeIdentifier()

            log.info "Request - " + callingOrganisation

            message.getExchange().put(Constants.AUDIT_ISSUER, callingOrganisation)
            message.getExchange().put(Constants.AUDIT_IDCARD, idCard.IDCardID)
        } catch (XmlUtilException e) {
            log.error("Caught exception while parsing header", e)
            isAuthorized = false
            rejectReason = "Exception parsing header"

        } catch (ModelBuildException e) {
            log.error("Caught exception while parsing header", e)
            isAuthorized = false
            rejectReason = "Exception parsing header"
        } catch (ModelException e) {
            log.error("Model error", e)
            isAuthorized = false
            rejectReason = e.getMessage()

        } catch (Exception e) {
            log.error("Caught exception", e)
            isAuthorized = false
            rejectReason = "Exception parsing header"
        } finally {
            message.getExchange().put(Constants.SOSI_DGWS_VERSION, sosiDgwsVersion)
            message.getExchange().put(Constants.SOSI_MESSAGE_ID, sosiMessageId)
            message.getExchange().put(Constants.SOSI_FLOW_ID, sosiFlowId)
            message.getExchange().put(Constants.SOSI_FLOW_VALUE, sosiFlowValue)
        }
        return [isAuthorized, rejectReason, sosiDgwsVersion, sosiMessageId, sosiFlowId]
    }
}
