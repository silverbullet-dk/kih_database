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
package dk.silverbullet.kihdb.util.version1

import dk.silverbullet.kihdb.client.dgks.chronicdataset.version1.GetChronicDatasetRequestMessage
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.GetMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.monitoring.version1.*
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.XmlConverterUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.*
import dk.sosi.seal.model.constants.NameSpaces
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues
import dk.sosi.seal.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import java.text.SimpleDateFormat

class TestDataUtil {
    private static Logger log = LoggerFactory.getLogger(TestDataUtil.class)
    private static Set<UUID> laboratoryUuidSet = new HashSet<UUID>()

    private static SosiTestDataUtil stdu = new SosiTestDataUtil()


    private static boolean isSosiOverridden = "true".equals(System.getProperty(Constants.SOSI_OVERRIDE))

    private static final String VAULT_PWD = "zse4rfv214"


    public String generateRequest(GetChronicDatasetRequestMessage cxfRequest) {
        Document doc = generateDomElement(cxfRequest)

        def soapRequest = XmlUtil.node2String(doc, false, true)
        return soapRequest
    }


    public String generateRequest(GetChronicDatasetRequestMessage cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Document doc = generateDomElement(cxfRequest)

//        return XmlUtil.node2String(doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }

    public String generateRequest(GetMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Document doc = generateDomElement(cxfRequest)

//        return XmlUtil.node2String(doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }

    public String generateRequest(DeleteMonitoringRequestMessage cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Document doc = createDocument()
        Marshaller m = createMarshaller(DeleteMonitoringRequestMessage.class)
        m.marshal(cxfRequest, doc)

        return documentToString(sosiRequest, signedIdCard, doc)
    }

    public String generateRequest(CreateMonitoringRequestMessage cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Document doc = generateDomElement(cxfRequest)

//        return XmlUtil.node2String(doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }


    private Document generateDomElement(GetChronicDatasetRequestMessage reqWrap) {
        Document doc = createDocument()
        Marshaller m = createMarshaller(GetChronicDatasetRequestMessage.class)

        m.marshal(reqWrap, doc)
        return doc
    }

    private Document generateDomElement(CreateMonitoringRequestMessage reqWrap) {
        Document doc = createDocument()
        Marshaller m = createMarshaller(CreateMonitoringRequestMessage.class)

        m.marshal(reqWrap, doc)
        return doc
    }

    private Marshaller createMarshaller(Class clz) {
        JAXBContext context = JAXBContext.newInstance(clz);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return m
    }

    private Document createDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        return doc
    }

    private String documentToString(Request sosiRequest, IDCard signedIdCard, Document doc) {
        sosiRequest.setIDCard(signedIdCard)
        sosiRequest.body = doc.getDocumentElement()
        def soapRequest = XmlUtil.node2String(sosiRequest.serialize2DOMDocument(), false, true)
        return soapRequest
    }


    protected IDCard createIDCard(SOSIFactory factory) {
        String systemName = "DemoCPR";
        String orgCVR = "12345678";
        String orgName = "Fyns Amt";
        CareProvider careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, orgCVR, orgName);
        AuthenticationLevel authenticationLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM;
        IDCard idCard = factory.createNewSystemIDCard(systemName, careProvider, authenticationLevel, null,
                null, factory.getCredentialVault().getSystemCredentialPair().getCertificate(), null);
        return idCard;
    }

    protected Request createRequest(SOSIFactory factory) {
        String flowID = null; // only necessary when multiple calls to service (session)
        return factory.createNewRequest(false, flowID);
    }


    public SecurityTokenResponse callIdpService(SOSIFactory factory, Document doc) throws Exception {
        // Setup WebService
        final String SERVER = System.getProperty("sts.url",
                "http://pan.certifikat.dk/sts/services/SecurityTokenService");
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


            Document returnDoc = XmlUtil.readXml(factory.getProperties(), response, true);

            boolean isFault = returnDoc.getElementsByTagNameNS(NameSpaces.SOAP_SCHEMA, "Fault").getLength() > 0;
            if (!isFault) {
                int authlvl = -1;
                NodeList attributes = returnDoc.getElementsByTagNameNS(NameSpaces.SAML2ASSERTION_SCHEMA, "Attribute");
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node elmAttr = attributes.item(i);
                    if ("sosi:AuthenticationLevel".equals(elmAttr.getAttributes().item(0).getNodeValue())) {
                        authlvl = Integer.parseInt(elmAttr.getFirstChild().getFirstChild().getNodeValue());
                        break;
                    }
                }

                if (AuthenticationLevel.MOCES_TRUSTED_USER.getLevel() == authlvl ||
                        AuthenticationLevel.VOCES_TRUSTED_SYSTEM.getLevel() == authlvl) {

                    Node elmSignature = returnDoc.getElementsByTagNameNS(NameSpaces.DSIG_SCHEMA, "Signature").item(0);
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

    /**
     * Loads Test data from file
     * @return
     */
    def loadTestdata(ObjectFactory objectFactory) {
        List<SelfMonitoredSampleType> retVal = new ArrayList<SelfMonitoredSampleType>()

        File f = new File("test/data/MedComKD testeksempel 1 version 1.0.xml")

        def sb = new StringBuffer()
        f.eachLine { line ->
            sb.append(line)
        }

        log.debug("Loading measurements XML")

        def elements = new XmlParser().parseText(sb.toString())
        def size = elements.SelfMonitoredSampleCollection.SelfMonitoredSample.size()
        def basicNodes = elements.SelfMonitoredSampleCollection.SelfMonitoredSample
        basicNodes.each { node ->
            SelfMonitoredSampleType t = objectFactory.createSelfMonitoredSampleType()

            def createdBy = node.CreatedByText
            def createDateTime = node.CreatedDateTime
            def sampleCategory = node.SampleCategoryIdentifier

            t.createdByText = createdBy[0]?.value()?.getAt(0)
            def sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            def v = createDateTime[0]?.value()?.getAt(0)
            Date d = sdf.parse(createDateTime[0]?.value()?.getAt(0))// 2006-05-04T18:13:51.0Z
            t.createdDateTime = dk.silverbullet.kihdb.util.XmlConverterUtil.getDateAsXml(d)
            t.sampleCategoryIdentifier = sampleCategory[0]?.value()?.getAt(0)
            t.uuidIdentifier = UUID.randomUUID().toString()

            t.laboratoryReportCollection = objectFactory.createLaboratoryReportCollectionType()


            def reports = node.LaboratoryReportCollection.LaboratoryReport

            reports.each { report ->
                LaboratoryReportType r = new LaboratoryReportType()

                def uuid = UUID.randomUUID()
                r.uuidIdentifier = uuid.toString()
                r.createdDateTime = XmlConverterUtil.getDateAsXml(sdf.parse(report.CreatedDateTime[0]?.value()?.getAt(0)))
                r.analysisText = report.AnalysisText[0]?.value()?.getAt(0)
                r.resultText = report.ResultText[0]?.value()?.getAt(0)
                r.resultEncodingIdentifier = EncodingIdentifierType.fromValue(report.ResultEncodingIdentifier[0]?.value()?.getAt(0))
                r.resultUnitText = report.ResultUnitText[0]?.value()?.getAt(0)
                r.nationalSampleIdentifier = report.NationalSampleIdentifier[0]?.value()?.getAt(0)
                r.iupacIdentifier = report.IupacIdentifier[0]?.value()?.getAt(0)

                r.producerOfLabResult = objectFactory.createProducerOfLabResultType()
                def labProducerIdentifier = report.ProducerOfLabResult[0]?.Identifier[0]?.value()?.getAt(0)
                def labProducerIdentifierCode = report.ProducerOfLabResult[0]?.IdentifierCode[0]?.value()?.getAt(0)

                r.producerOfLabResult.identifier = labProducerIdentifier
                r.producerOfLabResult.identifierCode = labProducerIdentifierCode

                t.laboratoryReportCollection.getLaboratoryReport().add(r)
            }
            retVal.add(t)
        }

        return retVal
    }

}
