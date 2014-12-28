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
package dk.silverbullet.kihdb.service.xds.documentrepository

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.cpr.StamdataLookupSoapService
import dk.silverbullet.kihdb.enums.*
import dk.silverbullet.kihdb.model.*
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.phmr.PHMRBuilder
import dk.silverbullet.kihdb.services.SosiService
import dk.silverbullet.kihdb.sosi.SosiUtil
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.TestDataUtil
import dk.silverbullet.kihdb.xds.EbXmlBuilder
import dk.silverbullet.kihdb.xds.SubmitPHMRObjectsRequestHelper
import dk.sosi.seal.model.Request
import ihe.iti.xds_b._2007.DocumentRepositoryPortType
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import spock.lang.Specification

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.ws.Service
import javax.xml.ws.soap.SOAPBinding
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import java.text.SimpleDateFormat

class KIHDocumentRepositoryEndpointSpec extends Specification {
    Logger log = LoggerFactory.getLogger(KIHDocumentRepositoryEndpointSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/KIHDocumentRepository'

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

    def documentTransformationService

    Citizen nancy
    SelfMonitoringSample sample

    BindingProvider bp
    BootStrapUtil bootStrapUtil

    def testDataUtil = new TestDataUtil()

    SosiTestDataUtil stdu = new SosiTestDataUtil()

    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    private ProvideAndRegisterDocumentSetRequestType readObjectFromFile(String file) {
        File f = new File(file)

        JAXBContext jaxbContext = JAXBContext.newInstance(ProvideAndRegisterDocumentSetRequestType.class)
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        JAXBElement<ProvideAndRegisterDocumentSetRequestType> retVal = unmarshaller.unmarshal(new FileInputStream(file))

        log.info "Unmarshalled: " + retVal.value

        return retVal.value
    }

    private List<String> extactIupacCode(SelfMonitoringSample sample) {
        List<String> uipacCodes = []
        for (report in sample.reports) {
            uipacCodes << report.iupacCode.code
        }

        return uipacCodes
    }

    private SubmitObjectsRequest prepareForRegisterStableDocument(SelfMonitoringSample sample, String documentId) throws JAXBException {
        SubmitPHMRObjectsRequestHelper requestHelper = new SubmitPHMRObjectsRequestHelper();

        SubmitObjectsRequest request = requestHelper.create();
        ExtrinsicObjectType metadata = requestHelper.addStableDocumentMetadataEntry(request, documentId, Constants.KIHDB_DOCUMENTREPOSITORY_ID);

        requestHelper.addDocumentMetadataAttributes(metadata, sample.createdByText, sample.citizen);

        List<String> uipacCodes = extactIupacCode(sample)

        requestHelper.addDocumentMetadataDefaultAttributes(metadata, uipacCodes);

        RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
        requestHelper.addSubmissionSetAttributes(submissionSet, sample.citizen);
        requestHelper.addSubmissionSetDefaultAttributes(submissionSet);
        requestHelper.addAssociation(request, metadata, submissionSet);

        return request;
    }


    public LaboratoryReport createLaboratoryReport(String laboratoryReportUUID,
                                                   Date createdDateTime,
                                                   String analysisText,
                                                   String resultText,
                                                   String resultEncodingIdentifier,
                                                   String resultOperatorIdentifier,
                                                   String resultUnitText,
                                                   String resultAbnormalIdentifier,
                                                   String resultMinimumText,
                                                   String resultMaximumText,
                                                   String resultTypeOfInterval,
                                                   String nationalSampleIdentifier,
                                                   IUPACCode iupacCode,
                                                   LabProducer producerOfLabResult,
                                                   Instrument instrument,
                                                   String measurementTransferredBy,
                                                   String measurementLocation,
                                                   String measuringDataClassification,
                                                   String measurementDuration,
                                                   String measurementScheduled,
                                                   String healthCareProfessionalComment,
                                                   String measuringCircumstances, boolean saveLaboratoryReport) {
        def l = new LaboratoryReport()

        if (resultTypeOfInterval) l.resultTypeOfInterval = resultTypeOfInterval
        if (producerOfLabResult) l.producerOfLabResult = producerOfLabResult
        if (iupacCode) l.iupacCode = iupacCode
        if (instrument) l.instrument = instrument
        if (nationalSampleIdentifier) l.nationalSampleIdentifier = nationalSampleIdentifier
        if (analysisText) l.analysisText = analysisText
        if (createdDateTime) l.createdDateTime = createdDateTime
        if (resultMaximumText) l.resultMaximumText = resultMaximumText
        if (resultMinimumText) l.resultMinimumText = resultMinimumText

        if (measurementDuration) {
            l.measurementDuration = measurementDuration
        }

        if (measurementScheduled) {
            l.measurementScheduled = MeasurementScheduled.fromValue(measurementScheduled)
        }

        if (measuringCircumstances) {
            l.measuringCircumstances = measuringCircumstances
        }

        if (healthCareProfessionalComment) {
            l.healthCareProfessionalComment = healthCareProfessionalComment
        }

        if (measurementTransferredBy) {
            l.measurementTransferredBy = MeasurementTransferredBy.fromString(measurementTransferredBy)
        }

        if (measurementLocation) {
            l.measurementLocation = MeasurementLocation.fromValue(measurementLocation)
        }


        if (measuringDataClassification) {
            l.measuringDataClassification = MeasuringDataClassification.fromValue(measuringDataClassification)
        }

        l.laboratoryReportUUID = laboratoryReportUUID

        if (resultText) l.resultText = resultText
        if (resultUnitText) {
            l.resultUnitText = resultUnitText
        } else {
            l.resultUnitText = " " // Handle PROTEINURI
        }

        if (resultAbnormalIdentifier) {
            l.resultAbnormalIdentifier = AbnormalIdentifier.fromString(resultAbnormalIdentifier)
        }

        if (resultEncodingIdentifier) {
            l.resultEncodingIdentifier = EncodingIdentifier.fromString(resultEncodingIdentifier)
        }

//        if (resultOperatorIdentifier) {
//            l.resultOperatorIdentifier = OperatorIdentifier.fromString(resultOperatorIdentifier)
//        }

    }


    private setup() {
        nancy = new Citizen(firstName: 'Nancy Ann ',
                lastName: 'Berggren',
                ssn: 2512484916,
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                sex: Sex.FEMALE,
                streetName: 'Åbogade 15',
                zipCode: '8200',
                city: 'Aarhus N',
                country: 'Denmark',
                mobilePhone: "12345678",
                contactTelephone: "12345690",
                email: "email@example.com")


        def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        bootStrapUtil = new BootStrapUtil()


        this.sample = new SelfMonitoringSample()
        sample.citizen = nancy
        sample.createdByText = "Helbredsprofilen"

        // Create FEV1
        def dates = [
                ["2013-08-20T18:13:51Z", "3.3"],
                ["2013-08-21T18:13:51Z", "3.7"],
                ["2013-08-22T18:13:51Z", "3.2"],
                ["2013-08-23T18:13:51Z", "3.6"],
                ["2013-08-24T18:13:51Z", "3.1"]
        ]

        def ResultEncodingIdentifier = "numeric"
        def MeasurementTransferredBy = "automatic"
        def MeasurementLocation = "home"
        def MeasuringDataClassification = "clinical"
        def MeasurementDuration = "120 sek"
        def MeasurementScheduled = "scheduled"
        def HealthCareProfessionalComment = "Pt. havde meget svært ved at puste igennem idag"
        def MeasuringCircumstances = "Udblæsningen blev foretaget siddende på en stol lige efter morgenmaden"
        def ResultTypeOfInterval = "therapeutic"

        def ResultOperatorIdentifier = "less_than"
        def ResultAbnormalIdentifier = "to_high"
        def ResultMinimumText = "1"
        def ResultMaximumText = "3"

        def AnalysisText = "FEV1"
        def ResultUnitText = "Liter"
        def IupacIdentifier = "MCS88015"
        def NationalSampleIdentifier = "9999999999"
        def iupacCode = IupacIdentifier

        def fevIupacCode = new IUPACCode(code: iupacCode, name: iupacCode, description: iupacCode, unit: iupacCode)
        def LabProducer_Identifier = "Patient målt"
        def LabProducer_Identifier_Code = "POT"

        def Instrument_MedComID = "EPQ12265"
        def Instrument_Manufacturer = "Siemens"
        def Instrument_ProductType = "Urine Analyzer"
        def Instrument_Model = "Clinitek Status+"
        def Instrument_SoftwareVersion = "SUA-2014r74-4"


        def labproducer = new LabProducer(identifier: LabProducer_Identifier, identifierCode: LabProducer_Identifier_Code)
        def instrument = new Instrument(medComID: Instrument_MedComID, model: Instrument_Model, manufacturer: Instrument_Manufacturer,
                productType: Instrument_ProductType, softwareVersion: Instrument_SoftwareVersion)

        sample.reports = new HashSet<LaboratoryReport>()
        for (d in dates) {
            def l = createLaboratoryReport(UUID.randomUUID().toString(), dateFormatter.parse(d[0]), AnalysisText, d[1], ResultEncodingIdentifier, ResultOperatorIdentifier,
                    ResultUnitText, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval, NationalSampleIdentifier, fevIupacCode,
                    labproducer, instrument, MeasurementTransferredBy, MeasurementLocation, MeasuringDataClassification, MeasurementDuration, MeasurementScheduled, HealthCareProfessionalComment,
                    MeasuringCircumstances, false)

            sample.reports.add(l)
        }


    }

    def "Simple test of ProvideAndRetrieveDocumentSetRequest"() {
        given:

        when:
        URL url = new URL(URL + "?wsdl")
        QName qname = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository")
        Service myRepository = Service.create(url, qname)

        DocumentRepositoryPortType repoServer = myRepository.getPort(DocumentRepositoryPortType.class)
        bp = (BindingProvider) repoServer

        SOAPBinding binding = (SOAPBinding) bp.getBinding()
        binding.setMTOMEnabled(false)

        exchangeAndSignIDCard()

        def filePath = "test/data/xds/RetrieveDocumentSetRequest_SOAP-body.xml"
        File ff2 = new File(filePath)

        dk.silverbullet.kihdb.phmr.XdsMarshaller xdsMarshaller = new dk.silverbullet.kihdb.phmr.XdsMarshaller()
        RetrieveDocumentSetRequestType documentSetRequestType = xdsMarshaller.getRetrieveDocumentSetRequestType(new FileInputStream(ff2))
        log.debug 'documentSetRequestType..:' + documentSetRequestType
        def response = repoServer.documentRepositoryRetrieveDocumentSet(documentSetRequestType)

        then:
        log.debug "Got respose : " + response.getRegistryResponse().getStatus()
        response != null

        response.getRegistryResponse().getStatus().equals(Constants.XDS_B_STATUS_SUCCESS)
        response.documentResponse.size() == 1
    }

    def "Test of retrieving generated document"() {

        when:
        URL url = new URL(URL + "?wsdl")
        QName qname = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository")
        Service myRepository = Service.create(url, qname)

        DocumentRepositoryPortType repoServer = myRepository.getPort(DocumentRepositoryPortType.class)
        bp = (BindingProvider) repoServer

        SOAPBinding binding = (SOAPBinding) bp.getBinding()
        binding.setMTOMEnabled(false)

        exchangeAndSignIDCard()

        RetrieveDocumentSetRequestType documentSetRequestType = new RetrieveDocumentSetRequestType()
        def request = new RetrieveDocumentSetRequestType.DocumentRequest()
        request.documentUniqueId = Constants.TEST_NANCY_PHMR_UUID
        documentSetRequestType.getDocumentRequest().add(request)

        log.debug 'documentSetRequestType..:' + documentSetRequestType
        RetrieveDocumentSetResponseType response = repoServer.documentRepositoryRetrieveDocumentSet(documentSetRequestType)

        then:
        log.debug "Got respose : " + response.getRegistryResponse().getStatus()
        response != null

        response.getRegistryResponse().getStatus().equals(Constants.XDS_B_STATUS_SUCCESS)
        response.documentResponse.size() == BigInteger.ONE
        response.documentResponse.get(0).documentUniqueId == Constants.TEST_NANCY_PHMR_UUID
        def t = new String(response.documentResponse.get(0).getDocument().getInputStream().bytes)
        t != null
    }

    private StamdataLookupSoapService setupStamdataService() {
        StamdataLookupSoapService service = new StamdataLookupSoapService()
        service.sosiService = new SosiService()
        service.sosiService.sosiUtil = new SosiUtil()

        return service
    }


    def "Simple test of ProvideAndRegisterDocumentSetRequest"() {
        given:

        when:
        URL url = new URL(URL + "?wsdl")
        QName qname = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository")
        Service myRepository = Service.create(url, qname)

        DocumentRepositoryPortType repoServer = myRepository.getPort(DocumentRepositoryPortType.class)
        bp = (BindingProvider) repoServer

        SOAPBinding binding = (SOAPBinding) bp.getBinding()
        binding.setMTOMEnabled(false)

        exchangeAndSignIDCard()

        def filePath = "test/data/xds/ProvideAndRegisterDocumentSet-test-body.xml"
        File ff2 = new File(filePath)

        dk.silverbullet.kihdb.phmr.XdsMarshaller xdsMarshaller = new dk.silverbullet.kihdb.phmr.XdsMarshaller()
        def response = repoServer.documentRepositoryProvideAndRegisterDocumentSetB(xdsMarshaller.getProvideAndRegisterDocumentSetRequest(new FileInputStream(ff2)))

        then:
        log.debug "Got respose : " + response
        response != null
        response.status.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")
    }

    def "test mtom upload"() {
        given:
        URL url = new URL(URL + "?wsdl")
        QName qname = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository")
        Service myRepository = Service.create(url, qname)

        DocumentRepositoryPortType repoServer = myRepository.getPort(DocumentRepositoryPortType.class)
        bp = (BindingProvider) repoServer

        SOAPBinding binding = (SOAPBinding) bp.getBinding()

        binding.setMTOMEnabled(true)

        def s = new SelfMonitoringSample()
        s.citizen = nancy
        s.createdByText = "Helbredsprofilen"

        s.reports = new HashSet<LaboratoryReport>()

        // Puls
        def dates = [["2013-05-24 13:06:29 CEST", "66.0"], ["2013-05-22 14:18:44 CEST", "57.0"],
                     ["2013-05-21 10:17:18 CEST", "55.0"], ["2013-05-20 16:15:38 CEST", "58.0"],
                     ["2013-05-18 14:13:37 CEST", "56.0"], ["2013-05-15 15:13:01 CEST", "57.0"],
                     ["2013-05-15 13:56:45 CEST", "58.0"], ["2013-05-11 10:50:00 CEST", "54.0"],
                     ["2013-05-12 10:50:00 CEST", "61.0"], ["2013-05-13 11:01:00 CEST", "58.0"]]
        for (d in dates) {
            def l = testDataUtil.createPulseMeasurement(sdf.parse(d[0]), d[1])
            s.reports.add(l)
            l = testDataUtil.createCalProtein(sdf.parse(d[0]), d[1])
            s.reports.add(l)

        }

        s.createdDateTime = sdf.parse(dates[0][0])

        def ebxmlBuilder = new EbXmlBuilder()
        def phmrBuilder = new PHMRBuilder()
        def metadata = phmrBuilder.buildRegistryEntry(s)
        def documentUUID = "691ebe2e-7ef3-4bd5-bdbc-1ad1610577a4"
        def cda = phmrBuilder.create(nancy, s, documentUUID)
        def associatedUUID = "Document01"

        def registryPackageId = UUID.randomUUID().toString()
        def extrinsicObjectId = UUID.randomUUID().toString()

        metadata.setRegistryPackageId(registryPackageId)
        metadata.setExtrinsicObjectId(extrinsicObjectId)
        metadata.setUniqueID(documentUUID)
        metadata.setAssociatedID(associatedUUID)
        metadata.submissionSetID = "KIH_submission_01"

        SubmitObjectsRequest request = prepareForRegisterStableDocument(s, documentUUID)

        ProvideAndRegisterDocumentSetRequestType input = new ProvideAndRegisterDocumentSetRequestType()
        input.submitObjectsRequest = request

        ProvideAndRegisterDocumentSetRequestType.Document d = new ProvideAndRegisterDocumentSetRequestType.Document()

        File logoFile = new File("web-app/images/logo.png")
        if (logoFile.exists()) {
            DataSource logoFileDataSourc = new FileDataSource(logoFile)
            d.setValue(new DataHandler(logoFileDataSourc))
            d.id = documentUUID
            input.getDocument().add(d)
            println "Added file to document"
        }

        log.info 'xml..: ' + new EbXmlBuilder().convertSubmitObjectsRequestToString(request)

        when:
        exchangeAndSignIDCard()

        def response = repoServer.documentRepositoryProvideAndRegisterDocumentSetB(input)
        log.debug "response: " + response?.status

        then:
        response != null
        response.status.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    private void exchangeAndSignIDCard() {
        Request sosiRequest = stdu.createRequest() // Gets the signed id card

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
        bp.getRequestContext().put(Header.HEADER_LIST, headersList)
    }
}
