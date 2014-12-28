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

import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.UnauthorizedException
import grails.util.Holders
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Element

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.namespace.QName

class HsuidOutInterceptor extends AbstractSoapInterceptor {
    private static Logger log = LoggerFactory.getLogger(HsuidOutInterceptor.class)

    // KDB-68 - we need special handling on how to interpret userType and dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute
    private static String[] namespacesForFullHsuidCheck = Arrays.asList("urn:oio:medcom:monitoringdataset:1.0.2")

    private JAXBContext jc = null
    private Unmarshaller u = null

    private boolean disableHsuidCheck = false

    HsuidOutInterceptor() {
        super(Phase.PRE_PROTOCOL)
        jc = JAXBContext.newInstance(HsuidHeader.class);
        u = jc.createUnmarshaller();

        def isDisabledValue = Holders?.grailsApplication?.config?.hsuid?.check?.disable

        disableHsuidCheck = (isDisabledValue ? Boolean.valueOf(isDisabledValue) : false)
    }

    @Override
    void handleMessage(SoapMessage message) throws Fault {
        log.debug "Extract information from chain"

        def qname = new QName("http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd", "HsuidHeader")
        SoapMessage inMessage = message.getExchange().getInMessage()
        def header = inMessage.getHeader(qname)

        QName operation = message.getExchange().getInMessage().get(Message.WSDL_OPERATION)

        def actingCitizenCpr = message.getExchange().get(Constants.HSUID_ACTING_USER_SSN)
        def requestCitizenCpr = message.getExchange().get(Constants.AUDIT_CPR)

        log.debug "Acting User: " + actingCitizenCpr + " requested user: " + requestCitizenCpr + " operation: " + operation.getLocalPart()
        log.debug "Enabled hsuid operations: " + Constants.HSUID_ENABLED_OPERATIONS.toString()

        if (!disableHsuidCheck) {
            if (Constants.HSUID_ENABLED_OPERATIONS.contains(operation.getLocalPart())) {
                if (namespacesForFullHsuidCheck.contains(operation.getNamespaceURI())) {
                    if (header && header.object) {
                        HsuidHeader h = (HsuidHeader) u.unmarshal((Element) header.object)
                        def validatedAttributes = HealthcareServiceUserIdentificationHeaderUtil.getValidatedHsuidAttributes(h)

                        log.debug "Performing check on hsuid"
                        if (validatedAttributes.isCitizen()) {
                            if (!actingCitizenCpr.equals(requestCitizenCpr)) {
                                log.debug "Throwing fault."
                                throw new Fault(new UnauthorizedException(Constants.ERROR_CODE_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA, Constants.ERROR_TEXT_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA))
                            }
                        } // ELse nothing - healthcare professionals access is just logged
                        log.debug "Access authorized ... or check not enabled  "
                    } else {
                        log.debug "Throwing fault - no HSUID header found in request."
                        throw new Fault(new UnauthorizedException(Constants.ERROR_CODE_HSUID_HEADER_MISSING, Constants.ERROR_TEXT_HSUID_HEADER_MISSING))
                    }
                } else {
                    log.debug "Performing HSUID check for acting SSN and requested SSN - using old check"
                    if (!(actingCitizenCpr != null && requestCitizenCpr != null && actingCitizenCpr.equals(requestCitizenCpr))) {
                        log.debug "Throwing fault."
                        throw new Fault(new UnauthorizedException(Constants.ERROR_CODE_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA, Constants.ERROR_TEXT_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA))
                    } else {
                        log.debug "Requested ok - proceeding"
                    }
                }
            }

        } else {
            log.debug "HSUID override engaged"
        }
    }
}
