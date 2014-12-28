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

import dk.medcom.dgws._2006._04.dgws_1_0.*
import dk.silverbullet.kihdb.util.DateUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.RequestHeader
import dk.sosi.seal.modelbuilders.ModelBuildException
import dk.sosi.seal.modelbuilders.SignatureInvalidModelBuildException
import dk.sosi.seal.pki.Federation
import dk.sosi.seal.pki.PKIException
import dk.sosi.seal.pki.SOSIFederation
import dk.sosi.seal.pki.SOSITestFederation
import dk.sosi.seal.vault.EmptyCredentialVault
import dk.sosi.seal.xml.XmlUtil
import grails.util.Environment
import oasis.names.tc.saml._2_0.assertion.AssertionType
import oasis.names.tc.saml._2_0.assertion.Attribute
import oasis.names.tc.saml._2_0.assertion.AttributeStatement
import oasis.names.tc.saml._2_0.assertion.Conditions
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.Security
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Node

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.namespace.QName
import javax.xml.soap.MessageFactory
import javax.xml.ws.Holder

/**
 *  Provides methods for web services to validate incoming headers <br/>
 *  Note: Protected and package scope methods are used by unit tests.
 */
class DGWSProvider {
    private Logger log = LoggerFactory.getLogger(DGWSProvider.class)

    // From DGWS 1.0
    public static final String FAULT_CODE_SYNTAX_ERROR = "syntax_error";
    public static final String FAULT_CODE_MISSING_REQUIRED_HEADER = "missing_required_header";
    public static final String FAULT_CODE_SECURITY_LEVEL_FAILED = "security_level_failed";
    public static final String FAULT_CODE_INVALID_USERNAME_PASSWORD = "invalid_username_password";
    public static final String FAULT_CODE_INVALID_SIGNATURE = "invalid_signature";
    public static final String FAULT_CODE_INVALID_IDCARD = "invalid_idcard";
    public static final String FAULT_CODE_INVALID_CERTIFICATE = "invalid_certificate";
    public static final String FAULT_CODE_EXPIRED_IDCARD = "expired_idcard";
    public static final String FAULT_CODE_NOT_AUTHORIZED = "not_authorized";
    public static final String FAULT_CODE_ILLEGAL_HTTP_METHOD = "illegal_http_method";
    public static final String FAULT_CODE_NONREPUDIATION_NOT_SUPPORTED = "nonrepudiation_not_supported";
    // Zulu time required, from DGWS 1.0.1
    public static final String FAULT_CODE_INVALID_DATE_FORMAT = "invalid_date_timezone";

    public static final String ID_CARD_VERSION = "1.0.1"

    private SOSIFactory factory;
    private Calendar now = Calendar.getInstance();
    public static final String ID_CARD_MIN_SEC_LEVEL = "3"
    public static final String ID_CARD_MAX_AGE_HOURS = "24"

    public DGWSProvider() {
        factory = createSOSIFactory();
    }

    public DGWSProvider(SOSIFactory factory) {
        this.factory = factory
    }

    /**
     * Validates the contents of the headers.
     * <p>
     * Validation includes:
     * <ul>
     *  <li>missing headers</li>
     *  <li>SAML assertion valid dates</li>
     *  <li>non repudiation</li>
     *  <li>header signatures</li>
     *  <li>id card version</li>
     *  <li>security level</li>
     *  <li>id card expiration</li>
     *  <li>id card max age</li>
     *  <li>time zones</li>
     *  <li>whitelist</li>
     * </ul>
     *
     * @param wsseHeader The {@link Security} header
     * @param medcomHeader The medcom {@link Header}
     * @throws DGWSFault If a validation error occurs
     */
    public RequestHeader validate(Security wsseHeader, Header medcomHeader) throws DGWSFault {

        assertion(wsseHeader == null, "Security header is missing", FAULT_CODE_MISSING_REQUIRED_HEADER);
        // noinspection ConstantConditions
        assertion(wsseHeader.getAssertion() == null, "Assertion is missing in security header",
                FAULT_CODE_MISSING_REQUIRED_HEADER);
        assertion(medcomHeader == null, "Medcom header is missing", FAULT_CODE_MISSING_REQUIRED_HEADER);
        validateConditions(wsseHeader.getAssertion().getConditions());
        validateNonRepudiation(medcomHeader);
        RequestHeader requestHeader = validateSignature(wsseHeader, medcomHeader);
        IDCard idCard = requestHeader.getIDCard();
        validateIDCardVersion(idCard);
        validateSecurityLevel(idCard, medcomHeader);
        validateIDCardExpiration(idCard);
        validateIDCardMaxAge(idCard);
        validateDateTimeZones(wsseHeader);
//        validateWhitelist(wsseHeader);

        return requestHeader
    }

    /**
     * Adds an attribute to the header indicating the maximum number of hours (since start of
     * the ID Card's validity period) in which the service will accept the ID Card.
     * @param medcomHeader
     */
    public static void addServiceNotAfter(Header medcomHeader) {
        try {
            medcomHeader.getOtherAttributes().put(new QName("urn:dk.nsi.service", "maxAge"), "2");
        } catch (Exception e) {
            ; // Not essential
        }
    }

    /**
     * Replaces the Medcom header kept in the holder medcomHeader with one appropriate for
     * a response.
     * @param medcomHeader
     */
    public static void setMedcomHeaderToOutgoing(Holder<Header> medcomHeader) {
        Header medcom = new Header();
        medcom.setFlowStatus(FlowStatus.flow_finalized_succesfully);
        final Linking linking = new Linking();
        linking.setFlowID(medcomHeader.value.getLinking().getFlowID());
        linking.setInResponseToMessageID(medcomHeader.value.getLinking().getMessageID());
        linking.setMessageID(UUID.randomUUID().toString());
        medcom.setLinking(linking);
        addServiceNotAfter(medcom);
        medcomHeader.value = medcom;
    }

    /**
     * Gets the CareProviderID captured in the security header
     * @param wsseHeader
     * @return CareProviderID
     */
    public static String getInvokerId(Security wsseHeader) {
        return findAttribute(wsseHeader, "SystemLog", "medcom:CareProviderID");
    }

    /**
     * Validates that the CVR in the {@link Security ] header is in the whitelist for this service
     */
    void validateWhitelist(Security wsseHeader) throws DGWSFault {
        String id = getInvokerId(wsseHeader);
        if (id == null || !isAllowed(id)) {
            throw new DGWSFault("CVR " + id + " is not in whitelist", FAULT_CODE_NOT_AUTHORIZED);
        }
    }

    /**
     * Validates that the medcom {@link Header} does not require non repudiation <br/>
     * Note: Currently none of our services support nonrepudiation
     */
    void validateNonRepudiation(Header medcomHeader) throws DGWSFault {
        if (YesNo.yes == medcomHeader.getRequireNonRepudiationReceipt()) {
            throw new DGWSFault("Service does not support non-repudiation", FAULT_CODE_NONREPUDIATION_NOT_SUPPORTED);
        }
    }

    private RequestHeader validateSignature(Security wsseHeader, Header medcomHeader) throws DGWSFault {
        try {
            Node soapHeader = MessageFactory.newInstance().createMessage().getSOAPHeader();
            getMarshaller(Security.class).marshal(wsseHeader, soapHeader);
//            getMarshaller(Header.class).marshal(medcomHeader, soapHeader);
            String xmlString = XmlUtil.node2String(soapHeader);
            return factory.deserializeRequestHeader(xmlString);
        } catch (SignatureInvalidModelBuildException si) {
            log.error("Error marshalling DGWS headers. SignatureInvalidModelBuildException: ", si.getMessage());
            assertion(true, si.getMessage(), FAULT_CODE_INVALID_SIGNATURE);
        } catch (ModelBuildException mbe) {
            log.error("Error marshalling DGWS headers. ModelBuildException: ", mbe.getMessage());
            assertion(true, mbe.getMessage(), FAULT_CODE_INVALID_SIGNATURE);
        } catch (PKIException pki) {
            log.error("Error marshalling DGWS headers. PKIException", pki.getMessage());
            assertion(true, pki.getMessage(), FAULT_CODE_INVALID_CERTIFICATE);
        } catch (Exception e) {
            log.error("Error marshalling DGWS headers. Exception", e.getMessage());
            assertion(true, e.getMessage(), "unknown error");
        }
        return null;
    }

    /**
     * Validates the security level of the {@link IDCard} and the medcom {@link Header}
     */
    void validateSecurityLevel(IDCard idCard, Header medcomHeader) throws DGWSFault {
        int idCardLevel = idCard.getAuthenticationLevel().getLevel();
        int level = Integer.parseInt(ID_CARD_MIN_SEC_LEVEL)
        assertion(idCardLevel < level, "Service requires security level " + level + " or higher",
                FAULT_CODE_SECURITY_LEVEL_FAILED);
        if (medcomHeader.getSecurityLevel() != null) { // Optional field
            int medcomLevel = medcomHeader.getSecurityLevel().toInt();
            assertion(idCardLevel != medcomLevel, "Security level in medcom header and security header should be the same",
                    FAULT_CODE_SECURITY_LEVEL_FAILED);
        }
    }

    private void validateIDCardVersion(IDCard idCard) throws DGWSFault {
        String version = ID_CARD_VERSION
        assertion(!version.equals(idCard.getVersion()), "Wrong IDCard version. Version required is " + version,
                FAULT_CODE_INVALID_IDCARD);
    }

    private void validateIDCardExpiration(IDCard idCard) throws DGWSFault {
        assertion(now.before(idCard.getCreatedDate()), "IDCard is not yet valid", FAULT_CODE_EXPIRED_IDCARD);
        assertion(now.after(idCard.getExpiryDate()), "IDCard has expired", FAULT_CODE_EXPIRED_IDCARD);
    }

    /**
     * Validates that the age of the supplied {@link IDCard} does not exceed 24 hours
     */
    void validateIDCardMaxAge(IDCard idCard) throws DGWSFault {
        Calendar cal = Calendar.getInstance();
        cal.setTime(idCard.getCreatedDate());
        int maxAge = Integer.parseInt(ID_CARD_MAX_AGE_HOURS)
        if (maxAge > 24) {
            throw new RuntimeException(ID_CARD_MAX_AGE_HOURS + " can not be more than 24 hours");
        }
        cal.add(Calendar.HOUR, maxAge);
        Calendar now = Calendar.getInstance();
        assertion(cal.before(now), "Service requires IDCard to be less than 24 hours old", FAULT_CODE_EXPIRED_IDCARD);
    }

    /**
     * Validates that all date times are supplied in Zulu time format
     */
    private void validateDateTimeZones(Security wsseHeader) throws DGWSFault {
        AssertionType assertion = wsseHeader.getAssertion();
        assertTimeZone(assertion.getIssueInstant(), "Assertion.IssueInstant");
        Conditions conditions = assertion.getConditions();
        assertTimeZone(conditions.getNotBefore(), "Assertion.Conditions.NotBefore");
        assertTimeZone(conditions.getNotOnOrAfter(), "Assertion.Conditions.NotOnOrAfter");
        assertTimeZone(wsseHeader.getTimestamp().getCreated(), "TimeStamp");
    }

    private void assertTimeZone(XMLGregorianCalendar calendar, String headerParameter) throws DGWSFault {
        assertion(!DateUtil.isXmlGregorianCalendarInZulu(calendar), "The header parameter " + headerParameter
                + " was not supplied in Zulu format as required by DGWS 1.0.1", FAULT_CODE_INVALID_DATE_FORMAT);
    }

    /**
     * Validates that the SAML assertion is valid at this time
     */
    void validateConditions(Conditions conditions) throws DGWSFault {
        assertion(now.before(conditions.getNotBefore().toGregorianCalendar()), "SAML Assertion is not yet valid",
                FAULT_CODE_EXPIRED_IDCARD);
        assertion(now.after(conditions.getNotOnOrAfter().toGregorianCalendar()), "SAML Assertion has expired",
                FAULT_CODE_EXPIRED_IDCARD);
    }

    private static String findAttribute(Security security, String statementId, String attributeId) {
        for (AttributeStatement attributeStatement : security.getAssertion().getAttributeStatement()) {
            if (statementId.equals(attributeStatement.getId())) {
                for (Attribute attribute : attributeStatement.getAttribute()) {
                    if (attributeId.equals(attribute.getName()))
                        return attribute.getAttributeValue();
                }
            }
        }
        return null;
    }

    private SOSIFactory createSOSIFactory() {

        Properties properties = System.getProperties()

        //properties.setProperty("sosi:issuer", "KIH Database");

        Federation federation

        if (Environment.current == Environment.PRODUCTION || Environment.currentEnvironment.name.equals("rnprod")) {
            log.info "Creating SOSI Factory for PRODUCTION environment"
            federation = new SOSIFederation(properties)
        } else {
            log.info "Creating SOSI Factory for NON-PRODUCTION environments"
            federation = new SOSITestFederation(properties)
        }


        log.debug "Created federation: " + federation

        SOSIFactory sosiFactory = new SOSIFactory(federation, new EmptyCredentialVault(), properties);

        return sosiFactory
    }

    @SuppressWarnings("rawtypes")
    private Marshaller getMarshaller(Class clazz) throws JAXBException {
        Marshaller m = JAXBContext.newInstance(clazz).createMarshaller();
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new SealNamespacePrefixMapper());
        return m;
    }

    private void assertion(boolean error, String msg, String code) throws DGWSFault {
        if (error) {
            throw new DGWSFault(msg, code);
        }
    }
}
