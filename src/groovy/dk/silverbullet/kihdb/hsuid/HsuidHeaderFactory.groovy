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
package dk.silverbullet.kihdb.hsuid

import dk.nsi.hsuid.*
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader

class HsuidHeaderFactory {

    private static String DEFAULT_ISSUER = "issuer";
    private static String DEFAULT_SYSTEM_OWNER = "System owner";
    private static String DEFAULT_SYSTEM_NAME = "System name";
    private static String DEFAULT_VERSION = "1.0";
    private static String DEFAULT_RESP_ORG = "Org responsible for consuming service";
    private static String DEFAULT_HEALTHCARE_PROF_USINGORG = "12345678";
    private static String DEFAULT_RESPONSIBLE_USER = null;
    private static String DEFAULT_RESPONSIBLE_USER_AUTHCODE = "-";

    private HsuidHeaderFactory() {}

    public static HsuidHeader getCitizenHsuidHeader(String cpr) {
        return getValidCitizenHsuidHeader(DEFAULT_ISSUER, cpr, DEFAULT_SYSTEM_OWNER, DEFAULT_SYSTEM_NAME, DEFAULT_VERSION, DEFAULT_RESP_ORG);
    }

    public static HsuidHeader getCitizenHsuidHeaderForRetrieve(String callerCpr, String patientCpr) {
        return getValidCitizenHsuidHeaderForRetrieve(DEFAULT_ISSUER, callerCpr, DEFAULT_SYSTEM_OWNER, DEFAULT_SYSTEM_NAME, DEFAULT_VERSION, DEFAULT_RESP_ORG, patientCpr);
    }

    public
    static HsuidHeader getValidCitizenHsuidHeader(String issuer, String cpr, String systemOwner, String systemName, String systemVersion,
                                                  String responsibleOrg) {
        List<Attribute> attributes = new ArrayList<Attribute>(6);
        addBasicAttributes(true, cpr, systemOwner, systemName, systemVersion, responsibleOrg, attributes);
        return HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes);
    }

    public
    static HsuidHeader getValidCitizenHsuidHeaderForRetrieve(String issuer, String callerCpr, String systemOwner, String systemName, String systemVersion,
                                                             String responsibleOrg, String patientCpr) {
        List<Attribute> attributes = new ArrayList<Attribute>(6);
        addBasicAttributes(true, callerCpr, systemOwner, systemName, systemVersion, responsibleOrg, attributes);
        addCitizenCivilRegistrationNumber(patientCpr, attributes);
        return HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes);
    }

    public static HsuidHeader getHealthcareProfessionalHsuidHeader(String cpr) {
        return getValidHealthcareProfessionalHsuidHeader(DEFAULT_ISSUER, cpr, DEFAULT_SYSTEM_OWNER, DEFAULT_SYSTEM_NAME, DEFAULT_VERSION, DEFAULT_RESP_ORG,
                new OrganisationIdentifier(DEFAULT_HEALTHCARE_PROF_USINGORG), null, cpr, DEFAULT_RESPONSIBLE_USER_AUTHCODE, false);
    }

    public
    static HsuidHeader getHealthcareProfessionalHsuidHeader(String actingUserId, String responsibleUserId, String responsibleUserAuthCode, OrganisationIdentifier usingOrgId) {
        return getHealthcareProfessionalHsuidHeader(actingUserId, responsibleUserId, responsibleUserAuthCode, usingOrgId, false);
    }

    public static HsuidHeader getHealthcareProfessionalHsuidHeader(
            String actingUserId,
            String responsibleUserId,
            String responsibleUserAuthCode,
            OrganisationIdentifier usingOrgId,
            boolean doConsentOVerride) {
        return getValidHealthcareProfessionalHsuidHeader(DEFAULT_ISSUER, actingUserId, DEFAULT_SYSTEM_OWNER, DEFAULT_SYSTEM_NAME, DEFAULT_VERSION, DEFAULT_RESP_ORG,
                usingOrgId, null, responsibleUserId, responsibleUserAuthCode, doConsentOVerride);
    }

    public
    static HsuidHeader getValidHealthcareProfessionalHsuidHeader(String issuer, String cpr, String systemOwner, String systemName,
                                                                 String systemVersion, String responsibleOrg, OrganisationIdentifier orgUsingId1, OrganisationIdentifier orgUsingId2,
                                                                 String responsibleUser, String responsibleUserAuthorization, boolean doConsentOverride) {
        List<Attribute> attributes;
        if (orgUsingId2 == null) {
            attributes = new ArrayList<Attribute>(9);
        } else {
            attributes = new ArrayList<Attribute>(10);
        }
        addBasicAttributes(false, cpr, systemOwner, systemName, systemVersion, responsibleOrg, attributes);
        attributes.add(getOrganisationIdentifierAttribute(orgUsingId1));
        if (orgUsingId2 != null) {
            attributes.add(getOrganisationIdentifierAttribute(orgUsingId2));
        }
        attributes.add(new ResponsibleUserCivilRegistrationNumberAttribute(responsibleUser));
        attributes.add(new ResponsibleUserAuthorizationCodeAttribute(responsibleUserAuthorization));
        attributes.add(new ConsentOverrideAttribute(doConsentOverride));
        return HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes);
    }


    public static HsuidHeader getHealthcareProfessionalHsuidHeaderForRetrieve(
            String actingUserId, String responsibleUserId,
            String responsibleUserAuthCode, OrganisationIdentifier orgUsingId,
            String patientUserId, boolean doConsentOverride) {
        return getHealthcareProfessionalHsuidHeaderForRetrieve(DEFAULT_ISSUER, actingUserId, DEFAULT_SYSTEM_OWNER, DEFAULT_SYSTEM_NAME, DEFAULT_VERSION, DEFAULT_RESP_ORG,
                orgUsingId, null, responsibleUserId, responsibleUserAuthCode, patientUserId, doConsentOverride);
    }

    public
    static HsuidHeader getHealthcareProfessionalHsuidHeaderForRetrieve(String issuer, String callerCpr, String systemOwner, String systemName,
                                                                       String systemVersion, String responsibleOrg, OrganisationIdentifier orgUsingId1, OrganisationIdentifier orgUsingId2,
                                                                       String responsibleUser, String responsibleUserAuthorization, String patientCpr, boolean doConsentOverride) {
        List<Attribute> attributes;
        if (orgUsingId2 == null) {
            attributes = new ArrayList<Attribute>(9);
        } else {
            attributes = new ArrayList<Attribute>(10);
        }
        addBasicAttributes(false, callerCpr, systemOwner, systemName, systemVersion, responsibleOrg, attributes);
        addCitizenCivilRegistrationNumber(patientCpr, attributes);
        attributes.add(getOrganisationIdentifierAttribute(orgUsingId1));
        if (orgUsingId2 != null) {
            attributes.add(getOrganisationIdentifierAttribute(orgUsingId2));
        }
        attributes.add(new ResponsibleUserCivilRegistrationNumberAttribute(responsibleUser));
        attributes.add(new ResponsibleUserAuthorizationCodeAttribute(responsibleUserAuthorization));
        attributes.add(new ConsentOverrideAttribute(doConsentOverride));
        return HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes);
    }

    private static OrganisationIdentifierAttribute getOrganisationIdentifierAttribute(OrganisationIdentifier org) {
        OrganisationIdentifierAttribute.LegalFormatNames type;
        OrganisationIdentifierType identifierType = org.getIdentifierType();
        if (identifierType.equals(OrganisationIdentifierType.SOR_CODE)) {
            type = OrganisationIdentifierAttribute.LegalFormatNames.SOR;
        } else if (identifierType.equals(OrganisationIdentifierType.SHAK_CODE)) {
            type = OrganisationIdentifierAttribute.LegalFormatNames.SHAK;
        } else if (identifierType.equals(OrganisationIdentifierType.DEA_NUMBER)) {
            type = OrganisationIdentifierAttribute.LegalFormatNames.GP_NUMBER;
        } else {
            throw new IllegalArgumentException("The organisation is not valid: " + org);
        }
        return new OrganisationIdentifierAttribute(org.getIdentifier(), type);
    }

    private
    static void addBasicAttributes(boolean isCitizen, String cpr, String systemOwner, String systemName, String systemVersion, String responsibleOrg,
                                   List<Attribute> attributes) {
        attributes.add(new UserTypeAttribute(isCitizen));
        attributes.add(new ActingUserCivilRegistrationNumberAttribute(cpr));
        attributes.add(new SystemVendorNameAttribute(systemOwner));
        attributes.add(new SystemNameAttribute(systemName));
        attributes.add(new SystemVersionAttribute(systemVersion));
        attributes.add(new OperationsOrganisationNameAttribute(responsibleOrg));
    }

    private static void addCitizenCivilRegistrationNumber(String cpr, List<Attribute> attributes) {
        attributes.add(new CitizenCivilRegistrationNumberAttribute(cpr));
    }

}
