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

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.*
import dk.sosi.seal.model.constants.NameSpaces
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues
import dk.sosi.seal.pki.Federation
import dk.sosi.seal.pki.SOSIFederation
import dk.sosi.seal.pki.SOSITestFederation
import dk.sosi.seal.vault.*
import dk.sosi.seal.xml.XmlUtil
import grails.util.Environment
import grails.util.Holders
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class SosiUtil {

    private QName securityQname = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse")
    private QName medcomQname = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Header", "medcom")

    // Setting up for XPath
    private XPath xPath = XPathFactory.newInstance().newXPath();

    // Define the parts
    private String soapEnvelopeXpath = "*[local-name()='Envelope'and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']"
    private String soapHeaderXpath = "*[local-name()='Header' and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']"
    private String securityXpath = "*[local-name()='${securityQname.localPart}' and namespace-uri()='${securityQname.namespaceURI}']"
    private String medcomLocalXpath = "*[local-name()='${medcomQname.localPart}' and namespace-uri()='${medcomQname.namespaceURI}']"

    // Define the expressions
    private String wsseXpath = "/${soapEnvelopeXpath}/${soapHeaderXpath}/${securityXpath}"
    private String medcomXpath = "/${soapEnvelopeXpath}/${soapHeaderXpath}/${medcomLocalXpath}"

    private static Logger log = LoggerFactory.getLogger(SosiUtil.class.name)


    static public SOSIFactory createSOSIFactory() {

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

    public SosiUtil() {

    }

    CareProvider careProvider
    AuthenticationLevel authLevel
    SOSIFactory factory

    private String credentialVaultPassword
    private String credentialVaultPath
    private String credentialVaultType
    private String sealFederationType
    private String cvr
    private String systemName
    private String stsUrl

    private boolean overrideSosi = false

    public SosiUtil(ConfigObject config) {
        log.debug "Starting SOSI Util creation"
        this.cvr = config.seal.cvr
        this.systemName = config.seal.systemName
        this.credentialVaultType = (config.seal.vault.type ? config.seal.vault.type : "classpath")
        this.sealFederationType = (config.seal.federation.type ? config.seal.federation.type : "test")
        this.credentialVaultPath = config.seal.vault.path
        this.credentialVaultPassword = config.seal.vault.password
        this.stsUrl = config.seal.sts.url

        if (!sealFederationType) {
            log.info "No seal.federation.type configured - setting to test"
            this.sealFederationType = "test"
        }

        careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, systemName)
        authLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM
        log.debug "Creating factory"
//        factory = constructSOSIFactory()

        def isDisabledValue = Holders?.grailsApplication?.config?.seal?.check?.disable

        overrideSosi = (isDisabledValue ? Boolean.valueOf(isDisabledValue) : false)

        log.debug "Override SOSI checks: " + overrideSosi
    }


    public SOSIFactory getSOSIFactory() {
        if (!factory) {
            factory = constructSOSIFactory()
        }

        return factory
    }


    public SosiUtil(String cvr, String systemName, String credentialVaultType, String credentialVaultPath, String credentialVaultPassword, String stsUrl) {

        this.credentialVaultPassword = credentialVaultPassword
        this.credentialVaultPath = credentialVaultPath
        this.credentialVaultType = (credentialVaultType ? credentialVaultType : "classpath")
        // Set to classpath if nothing is set.
        this.sealFederationType = (config.seal.federation.type ? config.seal.federation.type : "test")

        this.systemName = systemName
        this.cvr = cvr
        this.stsUrl = stsUrl

        careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, systemName)
        authLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM
        factory = constructSOSIFactory()

        log.debug "Override SOSI checks: " + overrideSosi
    }

    public IDCard getSignedIDCard() {
        IDCard card = createNewSystemIDCard()

        def signedIdCard

        if (!overrideSosi) {

            SecurityTokenRequest securityTokenRequest = factory.createNewSecurityTokenRequest();
            securityTokenRequest.setIDCard(card);

            log.debug "Calling IDP service"

            SecurityTokenResponse response = callIdpService(factory, securityTokenRequest.serialize2DOMDocument())
            if (response.fault) {
                log.info 'response.faultCode.....:' + response.faultCode
                log.info 'response.faultString...:' + response.faultString
            }
            signedIdCard = response?.IDCard

        } else {
            log.debug "Overriding SOSI"
            signedIdCard = card
        }

        return signedIdCard
    }

    public IDCard createNewSystemIDCard() {

        CredentialVault vault = factory?.getCredentialVault()
        def systemPair = vault?.systemCredentialPair
        def certificate = systemPair?.getCertificate()

        IDCard card = factory.createNewSystemIDCard(
                systemName, careProvider, authLevel, certificate, systemName);

        return card
    }

    public GenericCredentialVault getCredentialVault(Properties properties) {
        // Init credentialvault with system certificate
        // Use serialized JKS rather than serialized PKCS12, thus not needing
        // bouncycastle


        CredentialVault vault = null
        if ("classpath".equals(credentialVaultType)) {
            log.info "Using classpath based vault based on setting seal.vault.type: " + credentialVaultType
            vault = new ClasspathCredentialVault(properties, credentialVaultPath, credentialVaultPassword)
        }

        if ("file".equals(credentialVaultType)) {
            log.info "Using file based vault based on setting seal.vault.type: " + credentialVaultType
            vault = new FileBasedCredentialVault(properties, new File(credentialVaultPath), credentialVaultPassword)
        }

        return vault
    }

    private SOSIFactory constructSOSIFactory() {

        Properties properties = new Properties();
        properties.setProperty("medcom:ITSystemName", systemName)
        properties.setProperty("medcom:CareProviderID", cvr) //(og NameFormat skal være "medcom:cvrnumber")

        Federation federation = getFederation(properties)

        CredentialVault credentialVault = getCredentialVault(properties)
        SOSIFactory factory = new SOSIFactory(federation, (CredentialVault) credentialVault, properties);

        return factory

    }

    private Federation getFederation(Properties properties) {
        Federation federation

        if ("production".equals(sealFederationType)) {
            log.info "Setting SOSI federation to production"
            federation = new SOSIFederation(properties);

        } else {
            log.info "Setting SOSI federation to test"
            federation = new SOSITestFederation(properties)
        }

        return federation
    }


    public SecurityTokenResponse callIdpService(SOSIFactory factory, Document doc) throws Exception {

        // Setup WebService
        final String SOAP_ACTION = "";

        try {
            URL u = new URL(stsUrl);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("SOAPAction", SOAP_ACTION);

            OutputStream out = connection.getOutputStream();
            Writer wout = new OutputStreamWriter(out);

            String xml = XmlUtil.node2String(doc, false, true);
            wout.write(xml);
            wout.flush();
            wout.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            def total = "";
            while ((line = reader.readLine()) != null) {
                total += line;
            }
            inputStream.close();

            def response = total;

            Document returnDoc = XmlUtil.readXml(factory.getProperties(), response, true);

            boolean isFault = returnDoc.getElementsByTagNameNS(NameSpaces.SOAP_SCHEMA, "Fault").getLength() > 0;
            if (!isFault) {
                int authlvl = -1;
                org.w3c.dom.NodeList attributes = returnDoc.getElementsByTagNameNS(NameSpaces.SAML2ASSERTION_SCHEMA, "Attribute");
                for (int i = 0; i < attributes.getLength(); i++) {
                    org.w3c.dom.Node elmAttr = attributes.item(i);

                    if ("sosi:AuthenticationLevel".equals(elmAttr.getAttributes().item(0).getNodeValue())) {
                        authlvl = Integer.parseInt(elmAttr.getFirstChild().getFirstChild().getNodeValue());
                        break;
                    }
                }

                if (AuthenticationLevel.MOCES_TRUSTED_USER.getLevel() == authlvl ||
                        AuthenticationLevel.VOCES_TRUSTED_SYSTEM.getLevel() == authlvl) {

                    org.w3c.dom.Node elmSignature = returnDoc.getElementsByTagNameNS(NameSpaces.DSIG_SCHEMA, "Signature").item(0);
                    if (!SignatureUtil.validate(elmSignature, factory.getFederation(), factory.getCredentialVault(), true)) {
                        log.info "Validation failed"
                    }
                } else {
                    log.info "No validation"
                }
            }

            // Build reply
            XmlUtil.readXml(factory.getProperties(), total, true);
            return factory.deserializeSecurityTokenResponse(total);

        } catch (IOException e) {
            log.error "Error : " + e.printStackTrace();
            return null
        }
    }

    public Request createRequest() {
        SOSIFactory sosiFactory = getSOSIFactory()
        Request sosiRequest = sosiFactory.createNewRequest(false, "flow")
        IDCard signedIdCard = getSignedIDCard()
        sosiRequest.setIDCard(signedIdCard)
//      setIDCard(sosiRequest)
        return sosiRequest
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    public void exchangeAndSignIDCard(BindingProvider provider) {
        Request sosiRequest = createRequest() // Gets the signed id card

        // Mapping between formats ..
        Document doc = sosiRequest.serialize2DOMDocument()

        Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
        Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

        SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
        SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)

        List<Header> headersList = new ArrayList<Header>();

        headersList.add(securitySoapHeader)
        headersList.add(medcomSoapHeader)

        // Adding the relevant headers to the request
        provider.getRequestContext().put(Header.HEADER_LIST, headersList)
    }

}
