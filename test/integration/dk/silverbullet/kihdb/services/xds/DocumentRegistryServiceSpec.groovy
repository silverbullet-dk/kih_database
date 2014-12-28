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
package dk.silverbullet.kihdb.services.xds

import dk.nsi.hsuid.*
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.xds.RegistryException
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.phmr.PHMRBuilder
import dk.silverbullet.kihdb.services.SosiService
import dk.silverbullet.kihdb.util.TestDataUtil
import dk.silverbullet.kihdb.xds.EbXmlBuilder
import dk.silverbullet.kihdb.xds.SubmitPHMRObjectsRequestHelper
import dk.sosi.seal.model.Request
import grails.test.spock.IntegrationSpec
import grails.util.Holders
import ihe.iti.xds_b._2007.DocumentRegistryPortType
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.w3c.dom.Document
import org.w3c.dom.Element
import spock.lang.Ignore

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.namespace.QName
import javax.xml.transform.stream.StreamSource
import javax.xml.ws.BindingProvider
import javax.xml.ws.Service
import javax.xml.ws.soap.AddressingFeature
import javax.xml.ws.soap.SOAPFaultException
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import java.text.SimpleDateFormat

class DocumentRegistryServiceSpec extends IntegrationSpec {

    private URL documentRegistryURL
    private String serviceName = "DocumentRegistryEndpointService"
    private static final String documentRegistryNamespace = "urn:ihe:iti:xds-b:2007"


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


    private boolean enabled = true
    private boolean updateRegistry = true


    private DocumentRegistryPortType documentRegistry

    def kihDocumentRegistryService = new DocumentRegistryService()
    def sosiService = new SosiService()

    Citizen nancy

    def testDataUtil = new TestDataUtil()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    def idsToDeprecated = []

    private List<String> extactIupacCode(SelfMonitoringSample sample) {
        List<String> uipacCodes = []
        for (report in sample.reports) {
            uipacCodes << report.iupacCode.code
        }

        return uipacCodes

    }


    private SubmitObjectsRequest prepareForRegisterDeprecatedDocument(SelfMonitoringSample sample, String documentId) throws JAXBException {

        SubmitPHMRObjectsRequestHelper requestHelper = new SubmitPHMRObjectsRequestHelper();


        SubmitObjectsRequest request = requestHelper.create();
        ExtrinsicObjectType metadata = requestHelper.addStableDocumentMetadataEntry(request, documentId, Constants.KIHDB_DOCUMENTREPOSITORY_ID);

        requestHelper.addDocumentMetadataAttributes(metadata, sample.createdByText, sample.citizen);

        List<String> uipacCodes = extactIupacCode(sample)

        requestHelper.setStatusDeprecated(metadata)

        requestHelper.addDocumentMetadataDefaultAttributes(metadata, uipacCodes);

        RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
        requestHelper.addSubmissionSetAttributes(submissionSet, sample.citizen);
        requestHelper.addSubmissionSetDefaultAttributes(submissionSet);
        requestHelper.addAssociation(request, metadata, submissionSet);

        return request;
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


        kihDocumentRegistryService.sosiService = sosiService

    }


    @Ignore
    def "Test update registry"() {
        setup:
        def ebBuilder = new EbXmlBuilder()
        // Create nancy

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
        def documentUUID = "691ebe2e-7ef3-4bd5-bdbc-1ad1610577a4"
        def cda = phmrBuilder.create(nancy, s, documentUUID)
        def metadata = phmrBuilder.buildRegistryEntry(s)
        def associatedUUID = "Document01"

        def registryPackageId = UUID.randomUUID().toString()
        def extrinsicObjectId = UUID.randomUUID().toString()

        metadata.setRegistryPackageId(registryPackageId)
        metadata.setExtrinsicObjectId(extrinsicObjectId)
        metadata.setUniqueID(documentUUID)
        metadata.setAssociatedID(associatedUUID)
        metadata.submissionSetID = "KIH_submission_01"

        SubmitObjectsRequest request = prepareForRegisterStableDocument(s, documentUUID)

        when:

        RegistryResponseType response
        boolean ignoreException = false
        try {
            response = kihDocumentRegistryService.updateRegistry(request)
            println "Got: " + response?.status
        } catch (RegistryException e) {
            log.error "Caught exception: " + e.message
            ignoreException = true
        } catch (Exception e) {
            log.error "Problem submitting to registry: " + e.getMessage()
        }

        if (response?.status?.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")) {
            idsToDeprecated << [documentUUID, s]
        }

        then:
        response?.status == "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"

    }


    public void cleanup() {
        log.info "Cleaning"

        idsToDeprecated.each { u, s ->
            String uuid = (String) u
            SelfMonitoringSample sample = (SelfMonitoringSample) s

            SubmitObjectsRequest request = prepareForRegisterDeprecatedDocument(sample, uuid)
            RegistryResponseType response
            try {
                response = kihDocumentRegistryService.updateRegistry(request)
                log.debug "Response: " + response.status
            } catch (RegistryException e) {
                log.error "Caught exception: " + e.message
            } catch (Exception e) {
                log.error "Problem submitting to registry: " + e.getMessage()
            }


        }

    }

    /*
	 * Deprecate all documents for patient
	 *
	 */
    //TODO valid deprecate function for new version of Healthshare with more validation, some of the testhelper functions mix registered values and
    // and introduce validation errors when doing replace

    public void depricateAllDocumentsForPatient() throws Exception {

        String authorInstitution = "deprecateByRegisterOnDemandDocumentEntry^^^^^&2.16.208&ISO^^^^Yder=278467";

        System.out.println("Invoking deprecate on all documents for " + nancy.ssn);

        AdhocQueryResponse responseStoredQuery = storedQuery(nancy);

        List<ExtrinsicObjectType> onDemandDocuments = findDocuments(responseStoredQuery, Constants.XDS_ONDEMANDDOCUMENT_OBJ_TYPE);
        List<ExtrinsicObjectType> stableDocuments = findDocuments(responseStoredQuery, Constants.XDS_STABLEDOCUMENT_OBJ_TYPE);

        System.out.println("BEFORE: Number of ondemand/stable documents: " + onDemandDocuments.size() + "/" + stableDocuments.size());

        List<String[]> onDemandDocumentsEntriesToBeDeprecated = getIdsForQuery(onDemandDocuments);

        for (String[] values : onDemandDocumentsEntriesToBeDeprecated) {
            //System.out.println("Values: " + values[1] + " " + values [2] + " " + values[3] + " " + values[4] );
            SubmitPHMRObjectsRequestHelper requestHelper = new SubmitPHMRObjectsRequestHelper();
            SubmitObjectsRequest request = requestHelper.create();
            ExtrinsicObjectType metadata = requestHelper.addOnDemandDocumentMetadataEntry(
                    request,
                    values[1],
                    values[2]);
            requestHelper.setStatusDeprecated(metadata);
            requestHelper.addDocumentMetadataAttributes(metadata, values[3], nancy.ssn + "^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO");
            RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
            requestHelper.addSubmissionSetAttributes(submissionSet, nancy.ssn + "^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO");
            requestHelper.addAssociation(request, metadata, submissionSet);
            List<String> docEntriesToBeReplaced = new LinkedList<String>();
            docEntriesToBeReplaced.add(values[0]);
            requestHelper.addAssociationsForReplacements(request, metadata, docEntriesToBeReplaced);

            RegistryResponseType response = kihDocumentRegistryService.updateRegistry(request);

            printErrors(response.getRegistryErrorList());
        }

        //Deprecate stable documents
        List<String[]> stableDocumentsEntriesToBeDeprecated = getIdsForQuery(stableDocuments);

        for (String[] values : stableDocumentsEntriesToBeDeprecated) {
            SubmitPHMRObjectsRequestHelper requestHelper = new SubmitPHMRObjectsRequestHelper();
            SubmitObjectsRequest request = requestHelper.create();
            ExtrinsicObjectType metadata = requestHelper.addStableDocumentMetadataEntry(
                    request,
                    values[1],
                    values[2]);
            requestHelper.setStatusDeprecated(metadata);
            requestHelper.addDocumentMetadataAttributes(metadata, values[3], PATIENT_10_ID + "^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO");
            RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
            requestHelper.addSubmissionSetAttributes(submissionSet, PATIENT_10_ID + "^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO");
            requestHelper.addAssociation(request, metadata, submissionSet);
            List<String> docEntriesToBeReplaced = new LinkedList<String>();
            docEntriesToBeReplaced.add(values[0]);
            requestHelper.addAssociationsForReplacements(request, metadata, docEntriesToBeReplaced);
            RegistryResponseType response = kihDocumentRegistryService.updateRegistry(request);

            printErrors(response.getRegistryErrorList());
        }

        responseStoredQuery = storedQuery(nancy);

        onDemandDocuments = findDocuments(responseStoredQuery, Constants.XDS_ONDEMANDDOCUMENT_OBJ_TYPE);
        stableDocuments = findDocuments(responseStoredQuery, Constants.XDS_STABLEDOCUMENT_OBJ_TYPE);

        System.out.println("AFTER: Number of ondemand/stable documents: " + onDemandDocuments.size() + "/" + stableDocuments.size());
    }

    /**
     * Easy access to Proxy object. Handles the scenario where the proxy could not be established etc.
     * @return The proxy object to use
     * @throws RegistryException thrown in case of errors
     */


    DocumentRegistryPortType getDocumentRegistryProxy() throws RegistryException {

        if (Holders.grailsApplication.config.xds.documentregistry.updateRegistry) {
            enabled = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.updateRegistry)
            log.debug "Updates are enabled: " + enabled
        }

        if (Holders.grailsApplication.config.xds.documentregistry.url) {
            documentRegistryURL = new URL(Holders.grailsApplication.config.xds.documentregistry.url)
            log.debug "URL: " + documentRegistryURL.toString()
        } else {
            log.info "URL is not specified. Disabling updates. Please specify: 'xds.documentregistry.url' "
            enabled = false
        }

        if (Holders.grailsApplication.config.xds.documentregistry.serviceName) {
            serviceName = Holders.grailsApplication.config.xds.documentregistry.serviceName
        }


        log.info "Reading settings from config"
        this.updateRegistry = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.updateRegistry)
        log.debug "Update Registry: " + this.updateRegistry



        if (!documentRegistry) {
            try {
                QName qname = new QName(documentRegistryNamespace, serviceName)
                Service service = Service.create(documentRegistryURL, qname)
                documentRegistry = service.getPort(DocumentRegistryPortType.class, new AddressingFeature())
            } catch (Exception e) {
                log.error "Caught exception while setting up document registry endpoint. Disabling. Message: " + e.message
                enabled = false

                throw new RegistryException(Constants.ERROR_CODE_XDS_REGISTRY_INITIALIZATION, Constants.ERROR_TEXT_XDS_REGISTRY_INITIALIZATION + " " + e.getMessage())
            }

        }

        return documentRegistry
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    private void exchangeAndSignIDCard() {
        Request sosiRequest = sosiService.createRequest() // Gets the signed id card

        // Mapping between formats ..
        Document doc = sosiRequest.serialize2DOMDocument()

        Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
        Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

        SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
        SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)

        Element hsuidHeader = generateHsuidHeaders(nancy.ssn)
        QName hsuidQname = new QName("hsuid:HsuidHeader", "http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd")

        SoapHeader hsuidSoapHeader = new SoapHeader(hsuidQname, hsuidHeader)


        List<Header> headersList = new ArrayList<Header>();

        headersList.add(securitySoapHeader)
        headersList.add(medcomSoapHeader)
        headersList.add(hsuidSoapHeader)

        // Adding the relevant headers to the request
        ((BindingProvider) getDocumentRegistryProxy()).getRequestContext().put(Header.HEADER_LIST, headersList)
    }


    def executeAdhocQueryResponse(AdhocQueryRequest request, Citizen c) throws RegistryException {

        boolean caughtException = false
        // make sure the registry is initialized
        try {
            getDocumentRegistryProxy()
        } catch (Exception e) {
            log.error "Registry is not availabe. Reason: " + e.getMessage()
        }




        AdhocQueryResponse response
        if (enabled) {

            exchangeAndSignIDCard()

            try {
                response = getDocumentRegistryProxy().documentRegistryRegistryStoredQuery(request)

//                if (response.status
                log.debug "Got Response: " + response

            } catch (SOAPFaultException e) {
                enabled = false
                log.error "Caught SoapFaultException while invokikng registry. " + e.toString()
                caughtException = true

            } catch (ConnectException e) {
                log.error "Caught ConnectException while connecting to registry. " + e.toString()
                enabled = false
                caughtException = true

            } catch (Exception e) {
                log.error "Caught Exception while connecting to registry. " + e.toString()
                enabled = false

                caughtException = true
            }


        } else {
            log.info "Registry update is disabled - not updating"
        }


        return response


    }


    private Element generateHsuidHeaders(String ssn) {
        final HsuidHeader header = HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(
                "KIHDB",
                new ArrayList<Attribute>(Arrays.asList(new UserTypeAttribute(false),
                        new ActingUserCivilRegistrationNumberAttribute(ssn),
                        new ResponsibleUserCivilRegistrationNumberAttribute("0101584160"),
                        new ResponsibleUserAuthorizationCodeAttribute("SRTTQ"),
                        new OrganisationIdentifierAttribute("293591000016003", OrganisationIdentifierAttribute.LegalFormatNames.SOR),
                        new SystemVendorNameAttribute("Silverbullet / Mecom"),
                        new SystemNameAttribute("Silverbullet"),
                        new SystemVersionAttribute("1.0.7"),
                        new OperationsOrganisationNameAttribute("MedCom")
                )))
        HealthcareServiceUserIdentificationHeaderDOMUtil.elementFromHeader(header)
    }

    /// Query Helpers
    private AdhocQueryResponse storedQuery(Citizen c) throws Exception {
        //Retrieving the list of registered documents
        AdhocQueryHelper requestHelper = new AdhocQueryHelper();
        AdhocQueryRequest storedQueryRequest = requestHelper.createRequestStoredQueryFindDocumentsApprovedOnly(nancy.ssn + "^^^&1.3.6.1.4.1.21367.2010.1.2.300&ISO");


        AdhocQueryResponse responseStoredQuery = executeAdhocQueryResponse(storedQueryRequest, c)

        return responseStoredQuery;
    }

    private void printErrors(RegistryErrorList registryErrorList) {
        if (registryErrorList != null) {
            for (RegistryError error : registryErrorList.getRegistryError()) {
                System.out.println("Error: " + error.getCodeContext());
            }
        }
    }

    private String getAuthorInstitution(ExtrinsicObjectType doc) {
        List<ClassificationType> lis = doc.getClassification();
        for (ClassificationType classificationType : lis) {
            if (classificationType.getClassificationScheme().equals(XDSConstantsHelper.XDS_CLASSIFICATION_AUTHOR_IDENTIFICATION_SCHEME)) {
                return getSlotValue(classificationType.getSlot(), "authorInstitution");
            }
        }
        return null;
    }

    private String getRepositoryId(ExtrinsicObjectType doc) {
        return getSlotValue(doc.getSlot(), "repositoryUniqueId");
    }

    private String getSlotValue(List<SlotType1> list, String name) {
        for (SlotType1 slotType1 : list) {
            if (slotType1.getName().equals(name)) {
                return slotType1.getValueList().getValue().get(0);
            }

        }
        return null;
    }

    private List<String[]> getIdsForQuery(List<ExtrinsicObjectType> documents) {
        List<String[]> result = new ArrayList<String[]>();
        for (ExtrinsicObjectType doc : documents) {
            String[] ids = new String[4];
            ids[0] = doc.getId();
            System.out.println("ID: " + ids[0]);
            ids[1] = getDocumentId(doc);
            System.out.println("docID: " + ids[1]);
            ids[2] = getRepositoryId(doc);
            System.out.println("repID: " + ids[2]);
            ids[3] = getAuthorInstitution(doc);
            System.out.println("authorInstID: " + ids[3]);
            result.add(ids);
        }
        return result;
    }

    private String getDocumentId(ExtrinsicObjectType doc) {
        for (ExternalIdentifierType type : doc.getExternalIdentifier()) {
            if (type.getIdentificationScheme().equals(XDSConstantsHelper.XDS_EXTERNAL_IDENTIFIER_UNIQUE_ID_IDENTIFICATION_SCHEME)) {
                return type.getValue();
            }
        }
        return null;
    }

    private List<ExtrinsicObjectType> findDocuments(AdhocQueryResponse responseStoredQuery, String xdsOndemanddocumentObjType) {
        List<ExtrinsicObjectType> result = new LinkedList<ExtrinsicObjectType>();
        for (JAXBElement<? extends IdentifiableType> jaxbElement : responseStoredQuery?.getRegistryObjectList()?.getIdentifiable()) {
            if (jaxbElement.getValue() instanceof ExtrinsicObjectType) {
                ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) jaxbElement.getValue();
                if (extrinsicObjectType.getObjectType().equals(xdsOndemanddocumentObjType)) {

                    result.add(extrinsicObjectType);
                }
            }
        }
        return result;
    }

    /// Helpers


    private SubmitObjectsRequest prepareForRegister(SubmitObjectsRequest request) throws JAXBException {
        List<String> ids = extractListOfIdsToMakeUnique(request);
        String requestString = convertSubmitObjectsRequestToString(request);
        for (String id : ids) {
            requestString = requestString.replace(id, "urn:uuid:" + UUID.randomUUID());
        }
        return convertStringToSubmitObjectsRequest(requestString);
    }

    private List<String> extractListOfIdsToMakeUnique(SubmitObjectsRequest request) {
        List<String> ids = new ArrayList<String>();
        for (JAXBElement<? extends IdentifiableType> identifiable : request.getRegistryObjectList().getIdentifiable()) {
            ids.add(identifiable.getValue().getId());
        }
        return ids;
    }

    private String convertSubmitObjectsRequestToString(SubmitObjectsRequest request) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(request.getClass());
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter strWriter = new StringWriter();
        marshaller.marshal(request, strWriter);
        return strWriter.toString();
    }

    private SubmitObjectsRequest convertStringToSubmitObjectsRequest(String submitObjectsRequestString) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SubmitObjectsRequest.class);
        Object request = context.createUnmarshaller().unmarshal(new StreamSource(new StringReader(submitObjectsRequestString)));
        return (SubmitObjectsRequest) request;
    }


}
