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

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.*
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues
import dk.sosi.seal.pki.Federation
import dk.sosi.seal.pki.SOSITestFederation
import dk.sosi.seal.vault.ClasspathCredentialVault
import dk.sosi.seal.vault.CredentialVault
import dk.sosi.seal.vault.GenericCredentialVault
import dk.sosi.seal.xml.XmlUtil
import grails.util.Holders
import org.apache.cxf.binding.soap.SoapHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.namespace.QName
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class SosiTestDataUtil {
    private static final Logger log = LoggerFactory.getLogger(SosiTestDataUtil.class)

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


    def careProvider
    def authLevel
    def factory

    private static boolean isSosiOverridden = false

    private static final String VAULT_PWD = "zse4rfv214"

    public SosiTestDataUtil() {
        careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, "30808460", "SOSITEST")
        authLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM
        factory = createSOSIFactory()

        def isDisabledValue = Holders?.grailsApplication?.config?.seal?.check?.disable

        isSosiOverridden = (isDisabledValue ? Boolean.valueOf(isDisabledValue) : false)

        log.debug "Override SOSI checks: " + isSosiOverridden
    }


    public IDCard getSignedIDCard() {
        IDCard card = createNewSystemIDCard()

        def signedIdCard

        if (!isSosiOverridden) {
            SecurityTokenRequest securityTokenRequest = factory.createNewSecurityTokenRequest();
            securityTokenRequest.setIDCard(card);

            log.debug "Calling IDP service"
            SecurityTokenResponse response = callIdpService(factory, securityTokenRequest.serialize2DOMDocument())
            signedIdCard = response.IDCard
        } else {
            log.debug "Overriding SOSI"
            signedIdCard = card
        }
        return signedIdCard
    }

    public Request createRequest() {
        Request sosiRequest = factory.createNewRequest(false, UUID.randomUUID().toString())
        IDCard signedIdcard = getSignedIDCard()
        sosiRequest.setIDCard(signedIdcard)
        sosiRequest
    }

    public IDCard createNewSystemIDCard() {
        IDCard card = factory.createNewSystemIDCard(
                "SOSITEST", careProvider, authLevel,
                factory.getCredentialVault().getSystemCredentialPair().getCertificate(), "SOSITEST"
        );

        return card
    }

    public GenericCredentialVault getCredentialVault(Properties properties) {
        // Init credentialvault with system certificate
        // Use serialized JKS rather than serialized PKCS12, thus not needing
        // bouncycastle

        CredentialVault vault = new ClasspathCredentialVault(properties, "resources/SealKeystore.jks", VAULT_PWD)
        return vault
    }

    private SOSIFactory createSOSIFactory() {

        Properties properties = new Properties();
        //properties.setProperty("sosi:issuer", "TESTSTS");
        properties.setProperty("medcom:ITSystemName", "SOSITEST")
        properties.setProperty("medcom:UserCivilRegistrationNumber", "1111111118")// eller "1111111134"
        properties.setProperty("medcom:CareProviderID", "30808460") //(og NameFormat skal være "medcom:cvrnumber")


        Federation testFederation = new SOSITestFederation(properties);

        CredentialVault cv = getCredentialVault(properties)
        SOSIFactory factory = new SOSIFactory(testFederation, cv, properties);

        return factory

    }

    public SecurityTokenResponse callIdpService(SOSIFactory factory, Document doc) throws Exception {
        // Setup WebService
        final String SERVER = System.getProperty("sts.url",
                "http://test2.ekstern-test.nspop.dk:8080/sts/services/SecurityTokenService");
        final String SOAP_ACTION = "";

        try {
            URL u = new URL(SERVER);
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


            SecurityTokenResponse resp = factory.deserializeSecurityTokenResponse(response);
            return resp

//            if (FlowStatusValues.FLOW_FINALIZED_SUCCESFULLY.equals(resp.getgetFlowStatus()) {
//                IDCard idCard = resp.getIDCard();
//            } else {
//            }
//
//

//            Document returnDoc = XmlUtil.readXml(factory.getProperties(), response, true);
//
//
//            boolean isFault = returnDoc.getElementsByTagNameNS(NameSpaces.SOAP_SCHEMA, "Fault").getLength() > 0;
//            if (!isFault) {
//                int authlvl = -1;
//                org.w3c.dom.NodeList attributes=  returnDoc.getElementsByTagNameNS(NameSpaces.SAML2ASSERTION_SCHEMA, "Attribute");
//                for (int i = 0; i < attributes.getLength(); i++) {
//                    org.w3c.dom.Node elmAttr = attributes.item(i);
//                    if("sosi:AuthenticationLevel".equals(elmAttr.getAttributes().item(0).getNodeValue())) {
//                        authlvl = Integer.parseInt(elmAttr.getFirstChild().getFirstChild().getNodeValue());
//                        break;
//                    }
//                }
//
//                if(AuthenticationLevel.MOCES_TRUSTED_USER.getLevel() == authlvl ||
//                        AuthenticationLevel.VOCES_TRUSTED_SYSTEM.getLevel() == authlvl) {
//
//                    org.w3c.dom.Node elmSignature = returnDoc.getElementsByTagNameNS(NameSpaces.DSIG_SCHEMA, "Signature").item(0);
//                    SignatureUtil.
//                    if (!SignatureUtil.validate(elmSignature, factory.getFederation(), factory.getCredentialVault(),true))  {
//                        log.info "Validation failed"
//                    }
//                } else {
//                    log.info "No validation"
//                }

//        }

            // Build reply
            XmlUtil.readXml(factory.getProperties(), total, true);
            return factory.deserializeSecurityTokenResponse(total);

        } catch (IOException e) {
            log.error "Error : " + e.printStackTrace();
            return null
        }
    }

/**
 * utility methods which generates and signs SOSI IDCard to be used in connection to registry
 */
    public List<org.apache.cxf.headers.Header> exchangeAndSignIDCard() {
        List<org.apache.cxf.headers.Header> headersList = new ArrayList<org.apache.cxf.headers.Header>();
        if (!isSosiOverridden) {
            Request sosiRequest = createRequest() // Gets the signed id card

            // Mapping between formats ..
            Document doc = sosiRequest.serialize2DOMDocument()

            Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
            Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

            SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
            SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)


            headersList.add(securitySoapHeader)
            headersList.add(medcomSoapHeader)
        }
        return headersList
    }
}
