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
package dk.silverbullet.kihdb.util

import dk.nsi.hsuid.*
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.w3c.dom.Element

import javax.xml.namespace.QName

/**
 * Various convenience methods with regards to the HSUID header
 */
class HsuidUtil {
    private final static QName hsuidQname = new QName("http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd", "HsuidHeader")

    public enum UserType {
        CITIZEN, HEALTHCARE_PROFFESIONAL
    }


    public
    static List<Header> appendHsuidHeader(List<Header> headersList, String issuer, String ssn, HsuidUtil.UserType type, String vendorName, String systemName, String systemVersion, String operationsOrganisation) {
        appendHsuidHeader(headersList, issuer, ssn, type, vendorName, systemName, systemVersion, operationsOrganisation, null, null, null)
    }


    public
    static List<Header> appendHsuidHeader(List<Header> headersList, String issuer, String ssn, HsuidUtil.UserType type,
                                          String vendorName, String systemName, String systemVersion, String operationsOrganisation,
                                          String responsibleUserCivilRegistrationNumber, String responsibleUserAuthorizationCode,
                                          String organisationId) {
        if (!headersList) {
            headersList = new ArrayList<Header>()
        }

        Element hsuidElement = generateHsuidHeaders(issuer, ssn, type, vendorName, systemName, systemVersion, operationsOrganisation, responsibleUserCivilRegistrationNumber, responsibleUserAuthorizationCode, organisationId)
        SoapHeader hsuidHeader = new SoapHeader(hsuidQname, hsuidElement)
        headersList.add(hsuidHeader)
        return headersList
    }

    private static Element generateHsuidHeaders(String issuer, String ssn) {

        return generateHsuidHeaders(issuer, ssn, null, null, null, null, null,
                null, null, null)


    }

    private
    static Element generateHsuidHeaders(String issuer, String ssn, HsuidUtil.UserType type, String vendorName, String systemName, String systemVersion, String operationsOrganisation,
                                        String responsibleUserCivilRegistrationNumber, String responsibleUserAuthorizationCode, String organisationId) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>()

        if (ssn) {
            ActingUserCivilRegistrationNumberAttribute actingUserCivilRegistrationNumberAttribute
            actingUserCivilRegistrationNumberAttribute = new ActingUserCivilRegistrationNumberAttribute(ssn)
            attributes.add(actingUserCivilRegistrationNumberAttribute)
        }

        if (type) {
            UserTypeAttribute userTypeAttribute
            switch (type) {
                case HsuidUtil.UserType.CITIZEN:
                    userTypeAttribute = new UserTypeAttribute(UserTypeAttribute.LegalValues.CITIZEN.value)
                    break
                case HsuidUtil.UserType.HEALTHCARE_PROFFESIONAL:
                    userTypeAttribute = new UserTypeAttribute(UserTypeAttribute.LegalValues.HEALTHCAREPROFESSIONAL.value)
                    break
            }
            attributes.add(userTypeAttribute)
        }

        if (vendorName) {
            SystemVendorNameAttribute systemVendorNameAttribute = new SystemVendorNameAttribute(vendorName)
            attributes.add(systemVendorNameAttribute)
        }

        if (systemName) {
            SystemNameAttribute systemNameAttribute = new SystemNameAttribute(systemName)
            attributes.add(systemNameAttribute)
        }

        if (systemVersion) {
            SystemVersionAttribute systemVersionAttribute = new SystemVersionAttribute(systemVersion)
            attributes.add(systemVersionAttribute)
        }

        if (operationsOrganisation) {
            OperationsOrganisationNameAttribute operationsOrganisationNameAttribute = new OperationsOrganisationNameAttribute(operationsOrganisation)
            attributes.add(operationsOrganisationNameAttribute)
        }

        if (responsibleUserCivilRegistrationNumber) {
            ResponsibleUserCivilRegistrationNumberAttribute responsibleUserCivilRegistrationNumberAttribute = new ResponsibleUserCivilRegistrationNumberAttribute(responsibleUserCivilRegistrationNumber)
            attributes.add(responsibleUserCivilRegistrationNumberAttribute)
        }

        if (responsibleUserAuthorizationCode) {
            ResponsibleUserAuthorizationCodeAttribute responsibleUserAuthorizationCodeAttribute = new ResponsibleUserAuthorizationCodeAttribute(responsibleUserCivilRegistrationNumber)
            attributes.add(responsibleUserAuthorizationCodeAttribute)
        }

        if (organisationId) {
            OrganisationIdentifierAttribute organisationIdentifierAttribute = new OrganisationIdentifierAttribute(organisationId, OrganisationIdentifierAttribute.LegalFormatNames.SOR)
            attributes.add(organisationIdentifierAttribute)
        }

        final HsuidHeader header = HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes)
        def headerAsElement = HealthcareServiceUserIdentificationHeaderDOMUtil.elementFromHeader(header)
        return headerAsElement
    }
}
