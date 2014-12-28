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
package dk.silverbullet.kihdb.util.version1_0_1

import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.CreateMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.DeleteMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.GetMonitoringDataset
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.XmlConverterUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.AuthenticationLevel
import dk.sosi.seal.model.CareProvider
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues
import dk.sosi.seal.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import java.text.SimpleDateFormat

class TestDataUtil {
    private static Logger log = LoggerFactory.getLogger(TestDataUtil.class)
    private static SosiTestDataUtil stdu = new SosiTestDataUtil()

    private static Set<UUID> laboratoryUuidSet = new HashSet<UUID>()

    public String generateRequest(DeleteMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(DeleteMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }


    public String generateRequest(GetMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(GetMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }

    public String generateRequest(CreateMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(CreateMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
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

    /**
     * Loads Test data from file
     * @return
     */
    def loadTestdata(def objectFactory) {
        def retVal = []

        boolean isV101 = false
        boolean isV102 = false
        boolean isV3 = false
        def v101Objectfactory
        def v102Objectfactory


        if (objectFactory instanceof dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.ObjectFactory) {
            log.debug "V101 detected"
            isV101 = true
        }
        if (objectFactory instanceof dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.ObjectFactory) {
            log.debug "V102 detected"
            isV102 = true
        }

        if (objectFactory instanceof oio.medcom.chronicdataset._1_0.ObjectFactory) {
            log.debug "V3 detected"
            isV3 = true
        }


        File f = new File("test/data/MedComKD testeksempel 1 version 1.0.1.xml")

        def sb = new StringBuffer()
        f.eachLine { line ->
            sb.append(line)
        }

        log.debug("Loading measurements XML")

        def sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")


        def elements = new XmlParser().parseText(sb.toString())
        def size = elements.SelfMonitoredSample.size()
        def basicNodes = elements.SelfMonitoredSample
        basicNodes.each { node ->


            def t = objectFactory.createSelfMonitoredSampleType()

            def createdBy = node.CreatedByText

            t.createdByText = createdBy[0]?.value()?.getAt(0)
            t.laboratoryReportExtendedCollection = objectFactory.createLaboratoryReportExtendedCollectionType()

            def reports = node.LaboratoryReportExtendedCollection.LaboratoryReportExtended

            reports.each { report ->
                def r = objectFactory.createLaboratoryReportExtendedType()

                def uuid = UUID.randomUUID()
                r.uuidIdentifier = uuid.toString()
                r.createdDateTime = XmlConverterUtil.getDateAsXml(sdf.parse(report.CreatedDateTime[0]?.value()?.getAt(0)))
                r.analysisText = report.AnalysisText[0]?.value()?.getAt(0)
                r.resultText = report.ResultText[0]?.value()?.getAt(0)


                def encId
                def resEnc = report.ResultEncodingIdentifier[0]?.value()?.getAt(0)
                if (isV101) {
                    encId = dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.EncodingIdentifierType.fromValue(resEnc)
                }
                if (isV102) {
                    encId = dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.EncodingIdentifierType.fromValue(resEnc)
                }


                r.resultEncodingIdentifier = encId
                r.resultUnitText = report.ResultUnitText[0]?.value()?.getAt(0)
                r.nationalSampleIdentifier = report.NationalSampleIdentifier[0]?.value()?.getAt(0)
                r.iupacIdentifier = report.IupacIdentifier[0]?.value()?.getAt(0)
                r.resultTypeOfInterval = report.ResultTypeOfInterval[0]?.value()?.getAt(0)

                r.producerOfLabResult = objectFactory.createProducerOfLabResultType()
                def labProducerIdentifier = report.ProducerOfLabResult[0]?.Identifier[0]?.value()?.getAt(0)
                def labProducerIdentifierCode = report.ProducerOfLabResult[0]?.IdentifierCode[0]?.value()?.getAt(0)

                r.producerOfLabResult.identifier = labProducerIdentifier
                r.producerOfLabResult.identifierCode = labProducerIdentifierCode

                r.instrument = objectFactory.createInstrumentType()

                r.instrument.medComID = report.Instrument[0]?.MedComID[0]?.value()?.getAt(0)
                r.instrument.manufacturer = report.Instrument[0]?.Manufacturer[0]?.value()?.getAt(0)
                r.instrument.model = report.Instrument[0]?.Model[0]?.value()?.getAt(0)
                r.instrument.productType = report.Instrument[0]?.ProductType[0]?.value()?.getAt(0)
                r.instrument.softwareVersion = report.Instrument[0]?.SoftwareVersion[0]?.value()?.getAt(0)

                // Currently not handled - thus commented out
                r.healthCareProfessionalComment = report?.HealthCareProfessionalComment[0].value()
                r.measuringCircumstances = report?.MeasuringCircumstances[0].value()

                t.laboratoryReportExtendedCollection.getLaboratoryReportExtended().add(r)
            }
            retVal.add(t)
        }

        return retVal
    }

}
