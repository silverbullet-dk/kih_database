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

import dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil
import dk.nsi.hsuid.UserTypeAttribute
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.constants.Constants
import org.apache.cxf.binding.soap.SoapMessage
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor
import org.apache.cxf.headers.Header
import org.apache.cxf.interceptor.Fault
import org.apache.cxf.jaxws.interceptors.HolderInInterceptor
import org.apache.cxf.message.Message
import org.apache.cxf.phase.Phase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Element

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.namespace.QName

class HsuidInInterceptor extends AbstractSoapInterceptor {
    private static Logger log = LoggerFactory.getLogger(HsuidInInterceptor.class)


    public HsuidInInterceptor() {
        super(Phase.PRE_INVOKE);
        addAfter(HolderInInterceptor.class.getName());
    }


    @Override
    void handleMessage(SoapMessage message) throws Fault {
        def qname = new QName("http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd", "HsuidHeader")
        def hsuidHeader = message.getHeader(qname)

        String actingUserCivilRegistrationNumber
        String userType

        QName operation = message.get(Message.WSDL_OPERATION)


        actingUserCivilRegistrationNumber = extractInformationFromHeader(hsuidHeader)
        message.getExchange().put(Constants.HSUID_ACTING_USER_SSN, actingUserCivilRegistrationNumber)
    }

    /**
     * extract required information from provided HSUID header
     * @param header
     * @return UserType and the actingUserCivilRegistrationNumber
     */
    private String extractInformationFromHeader(Header header) {
        String actingUserCivilRegistrationNumber = null
        String userType = null

        if (header) {
            Element e
            if (header.object instanceof Element) {
                e = (Element) header.object
            }
            if (e) {
                JAXBContext jc = JAXBContext.newInstance(HsuidHeader.class);
                Unmarshaller u = jc.createUnmarshaller();
                HsuidHeader h = (HsuidHeader) u.unmarshal(e)

                final attributes = HealthcareServiceUserIdentificationHeaderUtil.getHealthcareServiceUserIdentificationHeaderAttributes(h)

                /*  - hsuid header looks as follows
                <ns2:Assertion IssueInstant="24-06-2014 08:45:31" Version="2.0" id="HSUID">
					<ns2:Issuer>KIHService Test</ns2:Issuer>
					<ns2:AttributeStatement id="HSUIDdata">
						<ns2:Attribute Name="nsi:UserType" NameFormat="">
							<ns2:AttributeValue>nsi:HealthcareProfessional</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:ActingUserCivilRegistrationNumber" NameFormat="">
							<ns2:AttributeValue>xxxxxxxx</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:SystemOwnerName" NameFormat="">
							<ns2:AttributeValue>YYYYYYY YY</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:SystemName" NameFormat="">
							<ns2:AttributeValue>XXXXXXXXXXX</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:SystemVersion" NameFormat="">
							<ns2:AttributeValue>1.0</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:OrgResponsibleName" NameFormat="">
							<ns2:AttributeValue>Test Testerne</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:OrgUsingID" NameFormat="nsi:sor">
							<ns2:AttributeValue>12345678</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:ResponsibleUserCivilRegistrationNumber" NameFormat="">
							<ns2:AttributeValue>yyyyyyyyyy</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:ResponsibleUserAuthorizationCode" NameFormat="">
							<ns2:AttributeValue>zzzzzzz</ns2:AttributeValue>
						</ns2:Attribute>
						<ns2:Attribute Name="nsi:ConsentOverride" NameFormat="">
							<ns2:AttributeValue>true</ns2:AttributeValue>
						</ns2:Attribute>
					</ns2:AttributeStatement>
				</ns2:Assertion>
                */

                attributes.each { attribute ->
                    if (UserTypeAttribute.attributeName.toString().equals(attribute.name.toString())) {
                        log.debug "Found usertype: " + attribute.value
                        userType = attribute.value
                    }
                    if (ActingUserCivilRegistrationNumberAttribute.attributeName.toString().equals(attribute.name.toString())) {
                        log.debug "Found acting user CPR: " + attribute.value
                        actingUserCivilRegistrationNumber = attribute.value
                    }
                }
            }
        }

        return actingUserCivilRegistrationNumber
    }
}
