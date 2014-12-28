//package dk.silverbullet.kihdb.sosi
//
///**
// * The MIT License
// *
// * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
// * (http://www.nsi.dk)
// *
// * Copyright (C) 2012,2013 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of
// * this software and associated documentation files (the "Software"), to deal in
// * the Software without restriction, including without limitation the rights to
// * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// * of the Software, and to permit persons to whom the Software is furnished to do
// * so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//
//import java.io.File;
//import java.security.cert.X509Certificate;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Properties;
//
//import org.apache.log4j.Logger;
//import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.Security;
//import org.w3c.dom.Document;
//
//import dk.medcom.dgws._2006._04.dgws_1_0.Header;
//import dk.nsi.util.ConfigException;
//import dk.nsi.util.ConfigInterface;
//import dk.sosi.seal.SOSIFactory;
//import dk.sosi.seal.model.AuthenticationLevel;
//import dk.sosi.seal.model.CareProvider;
//import dk.sosi.seal.model.IDCard;
//import dk.sosi.seal.model.Request;
//import dk.sosi.seal.model.SecurityTokenRequest;
//import dk.sosi.seal.model.SecurityTokenResponse;
//import dk.sosi.seal.model.SignatureUtil;
//import dk.sosi.seal.model.SystemIDCard;
//import dk.sosi.seal.pki.Federation;
//import dk.sosi.seal.pki.SOSIFederation;
//import dk.sosi.seal.pki.SOSITestFederation;
//import dk.sosi.seal.vault.CredentialPair;
//import dk.sosi.seal.vault.CredentialVault;
//import dk.sosi.seal.vault.CredentialVaultException;
//import dk.sosi.seal.vault.FileBasedCredentialVault;
//
///**
// * Class for SOSI security interaction such as acquiring STS tokens and exchanging DGWS and Security headers.
// * If you are not generating your ws-client classes you should use SosiService instead.
// *
// * @see SosiService
// */
//public class DGWSConsumer extends TokenRequester {
//    private Logger logger = Logger.getLogger(DGWSConsumer.class);
//    private ConfigInterface config;
//    private SOSIFactory factory;
//    private long expiresAt = 0;
//    private IDCard cachedIdCard;
//
//    public DGWSConsumer(ConfigInterface config) {
//        this.config = config;
//        factory = createFactory();
//    }
//
//    private SOSIFactory createFactory() {
//        Properties properties = SignatureUtil.setupCryptoProviderForJVM();
//        Federation federation = null;
//        if (Boolean.parseBoolean(config.getProperty(STS_TEST_MODE))) {
//            federation = new SOSITestFederation(properties);
//        } else {
//            federation = new SOSIFederation(properties);
//        }
//        File file = config.getRelativeFile(STS_KEYSTORE);
//        if (!file.canRead()) {
//            throw new ConfigException("Unable to read keystore: " + file.getAbsolutePath());
//        }
//        CredentialVault vault = new FileBasedCredentialVault(properties, file, config.getProperty(STS_KEYSTORE_PW));
//        return new SOSIFactory(federation, vault, properties);
//    }
//
//    /***
//     * Obtains valid security headers by engaging the STS server if no IDCard is at hand
//     *
//     * @param flowId Session specific id for tracking calls between webservices
//     * @return A valid set of DGWS headers
//     * @throws STSException if communication with the STS servers fails for some reason
//     */
//    public DGWSHeaderWrapper getValidDGWSHeaders(String flowId) throws STSException {
//        if (isTokenExpiredOrNearExpiration()) {
//            try {
//                cachedIdCard = requestSecurityToken(flowId);
//                expiresAt = cachedIdCard.getExpiryDate().getTime();
//            } catch (Exception e) {
//                throw new STSException(e.getMessage(), e);
//            }
//        }
//        return createHeaders(cachedIdCard, flowId);
//    }
//
//    /**
//     * Initializes the consumer and issues warning log entry if system certificate is about to expire
//     */
//    public void initialise() {
//        warnOnCertificateExpiryNear(factory);
//    }
//
//    private void warnOnCertificateExpiryNear(SOSIFactory factory) {
//        expiryEarlyWarning(getCertificate());
//    }
//
//    private void expiryEarlyWarning(X509Certificate cert) {
//        Date notAfter = cert.getNotAfter();
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(config.getProperty(CERT_EXPIRES_IN_WARNING)));
//        if (notAfter.before(c.getTime())) {
//            logEarlyWarning(notAfter, cert.getSubjectDN().toString());
//        }
//    }
//
//    protected void logEarlyWarning(Date notAfter, String subjectDN) {
//        logger.warn("Certificate expires on " + notAfter + ", SubjectDN: " + subjectDN);
//    }
//
//    IDCard requestSecurityToken() throws Exception {
//        return requestSecurityToken(null);
//    }
//
//    IDCard requestSecurityToken(String flowId) throws Exception {
//        String systemName = config.getProperty(IDCARD_SYSTEM_NAME);
//        String careProviderIdType = config.getProperty(IDCARD_CAREPROVIDER_ID_TYPE);
//        String careProviderId = config.getProperty(IDCARD_CAREPROVIDER_ID);
//        String careProviderName = config.getProperty(IDCARD_CAREPROVIDER_NAME);
//        AuthenticationLevel level = AuthenticationLevel.getEnumeratedValue(Integer.parseInt(config.getProperty(IDCARD_LEVEL)));
//        CareProvider careProvider = new CareProvider(careProviderIdType, careProviderId, careProviderName);
//        try {
//            SystemIDCard systemIDCard = factory.createNewSystemIDCard(
//                    systemName,
//                    careProvider,
//                    level,
//                    null,
//                    null,
//                    getCertificate(),
//                    null);
//            SecurityTokenRequest securityTokenRequest = factory.createNewSecurityTokenRequest();
//            securityTokenRequest.setIDCard(systemIDCard);
//            SecurityTokenResponse response = requestToken(securityTokenRequest);
//            if (response.isFault()) {
//                throw new STSException(response.getFaultString());
//            }
//            return response.getIDCard();
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    private DGWSHeaderWrapper createHeaders(IDCard idCard, String flowId) throws STSException {
//        try {
//            Request request = factory.createNewRequest(false /* do not request non-repudiation */, flowId);
//            request.setIDCard(idCard);
//            request.setDGWSVersion("1.0.1");
//            Document doc = request.serialize2DOMDocument();
//            return new DGWSHeaderWrapper(
//                    HeaderConverter.SECURITY.documentToJaxb(Security.class, doc),
//                    HeaderConverter.MEDCOM.documentToJaxb(Header.class, doc));
//        } catch (HeaderConverterException e) {
//            throw new STSException("Creation of DGWS headers failed", e);
//        }
//    }
//
//    protected X509Certificate getCertificate() throws CredentialVaultException {
//        CredentialPair cp = factory.getCredentialVault().getSystemCredentialPair();
//        if (cp == null) {
//            throw new CredentialVaultException("No system credentials in keystore");
//        }
//        return cp.getCertificate();
//    }
//
//    public boolean isTokenExpiredOrNearExpiration() {
//        return System.currentTimeMillis() + getTokenExpiryThreshold() > expiresAt;
//    }
//
//    @Override
//    protected String getSTSServerEndpoint() {
//        return config.getProperty(STS_ENDPOINT);
//    }
//
//    @Override
//    protected SOSIFactory getFactory() {
//        return factory;
//    }
//
//    private int getTokenExpiryThreshold() {
//        return Integer.parseInt(config.getProperty(STS_TOKEN_EXPIRY_THRESHOLD)) * 1000;
//    }
//}
