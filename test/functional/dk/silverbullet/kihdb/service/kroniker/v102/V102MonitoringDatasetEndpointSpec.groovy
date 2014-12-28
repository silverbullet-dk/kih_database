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
package dk.silverbullet.kihdb.service.kroniker.v102

import dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute
import dk.nsi.hsuid.Attribute
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderDOMUtil
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.*
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.XmlConverterUtil
import dk.silverbullet.kihdb.util.version1_0_1.TestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import spock.lang.Specification
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import java.text.SimpleDateFormat

class V102MonitoringDatasetEndpointSpec extends Specification {
    Logger log = LoggerFactory.getLogger(V102MonitoringDatasetEndpointSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final static String ALTERNATE_SSN = "2512504916"

    private final static String K26_SSN = "2105669996"

    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/v2/monitoringDataset'
//    private final static String URL = 'http://localhost:8080/kih_database/services/monitoringDataset'

    TestDataUtil tdu = new TestDataUtil()
    SosiTestDataUtil stdu = new SosiTestDataUtil()
    ObjectFactory objectFactory = new ObjectFactory()
    SOSIFactory factory = stdu.createSOSIFactory()
    V102TestDataUtil v102TestDataUtil = new V102TestDataUtil()

    def cxfClient = new SOAPClient(URL)
    def response
    def resultSet

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

    private Element generateHsuidHeaders() {
        return generateHsuidHeaders(TEST_SSN)
    }

    private Element generateHsuidHeaders(String ssn) {
        final HsuidHeader header = HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(
                "my-issuer",
                new ArrayList<Attribute>(Arrays.asList(new ActingUserCivilRegistrationNumberAttribute(ssn))));
        return HealthcareServiceUserIdentificationHeaderDOMUtil.elementFromHeader(header)
    }

    private Citizen nancy
    private Citizen k26

    def setup() {
        nancy = new Citizen(firstName: 'Nancy', middleName: 'Ann', lastName: 'Berggren',
                ssn: 2512484916,
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                sex: Sex.FEMALE,
                streetName: 'Gammel Kongevej 62',
                zipCode: '1200',
                city: 'København',
                country: 'Denmark',
                mobilePhone: "12345678",
                contactTelephone: "12345690",
                email: "email@example.com"
        )

        k26 = new Citizen(firstName: "FornavnK26",
                lastName: "EfternavnK26",
                ssn: "2105669996",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1966-05-21"),
                sex: Sex.FEMALE,
                streetName: 'AdresseK26 26',
                zipCode: '9998',
                city: 'ByK26',
                country: 'Denmark',
                mobilePhone: null,
                contactTelephone: "99999999",
                email: "MailK26@mail.dk")
    }


    def "Test retrieve data"() {
        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)

        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN


        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())
        String soapRequest = generateRequest(reqWrap, request)
        log.debug "REquest: " + soapRequest

//        def expectedSize = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false).size()


        def lResponse
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            log.debug "Re:" + response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        0 < resultSet?.size()
    }

    def "Test citizen data from CPR"() {
        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)

        when:
        // Created the request
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN



        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        def lResponse
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null

        String resStreet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetName.toString() +
                " " + response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetBuildingIdentifier.toString()

        nancy.ssn == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonCivilRegistrationIdentifier.toString()
        nancy.streetName.equals(resStreet.trim())
        nancy.zipCode == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.PostCodeIdentifier.toString()
        nancy.firstName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonGivenName.toString()
        nancy.middleName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonMiddleName.toString()
        nancy.lastName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonSurnameName.toString()
        nancy.email == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.EmailAddressIdentifier.toString()
        nancy.contactTelephone == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PhoneNumberIdentifier.toString()
    }


    def "Test citizen data from failed CPR lookup"() {
        given:
//        def citizen = Citizen.findBySsn(K26_SSN)

        when:
        // Created the request
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = K26_SSN



        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders(K26_SSN))

        String soapRequest = generateRequest(reqWrap, request)

        def lResponse
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null

        String resStreet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetName.toString() +
                " " + response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetBuildingIdentifier.toString()

        k26.ssn == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonCivilRegistrationIdentifier.toString()
        k26.streetName.trim().equals(resStreet.trim())
        k26.zipCode == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.PostCodeIdentifier.toString()
        k26.firstName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure?.PersonGivenName?.toString()
//        citizen.middleName.toString() == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure?.PersonMiddleName?.text()
        k26.lastName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure?.PersonSurnameName?.toString()
        k26.email == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.EmailAddressIdentifier?.toString()
        k26.contactTelephone == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PhoneNumberIdentifier?.toString()
    }


    def "Test HealthCareComment and MeasuringCircumstances"() {
        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)

        when:
        // Created the request
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN



        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        def lResponse
        def foundMeasuringCircumstances = false
        def foundHealthProfessionalComment = false
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()

            for (sample in resultSet) {
                for (report in sample.LaboratoryReportExtendedCollection?.LaboratoryReportExtended.list()) {
                    if (report.HealthCareProfessionalComment.toString() && report.HealthCareProfessionalComment.toString().length() > 0) {
                        foundHealthProfessionalComment = true
                    }
                    if (report.MeasuringCircumstances.toString() && report.MeasuringCircumstances.toString().length() > 0) {
                        foundMeasuringCircumstances = true
                    }

                }
            }
            // Find
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }




        then:
        cxfClient != null
        String resStreet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetName.toString() +
                " " + response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetBuildingIdentifier.toString()

        nancy.ssn == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonCivilRegistrationIdentifier.toString()
        nancy.streetName.equals(resStreet.trim())
        nancy.zipCode == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.PostCodeIdentifier.toString()
        nancy.firstName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonGivenName.toString()
        nancy.middleName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonMiddleName.toString()
        nancy.lastName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonSurnameName.toString()
        nancy.email == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.EmailAddressIdentifier.toString()
        nancy.contactTelephone == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PhoneNumberIdentifier.toString()
        foundHealthProfessionalComment == true
        foundMeasuringCircumstances == true


    }


    def "Test number of results"() {
        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
        requestMsg.maximumReturnedMonitorering = BigInteger.ONE


        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        println "Sending: " + soapRequest

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
        BigInteger.ONE.intValue() == resultSet?.size()
    }

    def "Test data since date"() {
        when:

        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        def cal = new GregorianCalendar()
        cal.add(Calendar.YEAR, 1) // Add one year

        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(cal.time) // Now now should be after boot.

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
        0 == resultSet?.size()
    }

    def "Test data since from date till to date"() {
        given:
//        def c = Citizen.findBySsn(TEST_SSN)

        def cal = new GregorianCalendar()
        cal.add(Calendar.YEAR, 1) // Add one year
        def toDate = cal.time

        cal.add(Calendar.YEAR, -2)
        def fromDate = cal.time

//        def samples = SelfMonitoringSample.findAllByCitizen(c)
//        def reports = LaboratoryReport.findAllBySampleInListAndCreatedDateTimeBetween(samples,fromDate,toDate)
//
//        def results = []
//        for (report in reports) {
//            if (!results.contains(report.sample)) {
//                results << report.sample
//            }
//        }
//

        when:

        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN


        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(fromDate) // Now now should be after boot.
        requestMsg.toDate = XmlConverterUtil.getDateAsXml(toDate) // Now now should be after boot.

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
        0 == resultSet?.size()
    }

    def "Test data since and number of items "() {
        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        requestMsg.maximumReturnedMonitorering = BigInteger.TEN

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Copenhagen"))
        cal.set(2013, Calendar.AUGUST, 1) // 2013-08-01 in order to match test date

        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(cal.time) // Now now should be after boot.

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)
        log.debug "Sending: \n" + soapRequest

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
        BigInteger.TEN == resultSet?.size()

        when:
        cal.add(Calendar.YEAR, 2) // Add one year

        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(cal.time) // Now now should be after boot.
        request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
        BigInteger.ZERO == resultSet?.size()

    }

    def "Test no data on SSN"() {
        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
        requestMsg.personCivilRegistrationIdentifier = ALTERNATE_SSN

        requestMsg.maximumReturnedMonitorering = BigInteger.TEN

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(v102TestDataUtil.generateHsuidHeaders(ALTERNATE_SSN))

        String soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        200 == response.httpResponse.statusCode
    }


    def "Test empty SSN"() {
        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String soapRequest = generateRequest(reqWrap, request)

        def caughtException = false
        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            caughtException = true

        }

        then:
        cxfClient != null
        true == caughtException

    }

    def "Test upload of measurements"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        def collection = requestMsg.getMonitoringDatasetCollection()

        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)

        collection.add(monitoringItem)

        def expectedResultSize = 0

        for (sample in monitoringItem.selfMonitoredSample) {
            expectedResultSize += sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
        }

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        log.debug "Sending: \n" + soapRequest

        def lresponse

        try {
            response = cxfClient.send(soapRequest)
            lresponse = response
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.MonitoringDatasetCollectionResponse.UuidIdentifier.list()
            // Extract the result set.
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        expectedResultSize == resultSet.size()
    }

    def "Test no SSN"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()


        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list()
            // Extract the result set.
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        BigInteger.ZERO == resultSet.size()
    }

    def "Test no data"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()

        def collection = requestMsg.getMonitoringDatasetCollection()
        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        collection.add(monitoringItem)

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list()
            // Extract the result set.
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        BigInteger.ZERO == resultSet.size()
    }

    def "Test empty UUID"() {
        when:

        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()

        def collection = requestMsg.getMonitoringDatasetCollection()
        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)

        def expectedResultSize = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        println "Sending: " + soapRequest

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.MonitoringDatasetCollectionResponse.UuidIdentifier.list()
            // Extract the result set.
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        expectedResultSize == resultSet.size()
    }

    def "Test error On same LaboratoryReportUUID"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        def collection = requestMsg.getMonitoringDatasetCollection()
        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        collection.add(monitoringItem)


        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)


        def expectedResultSize = monitoringItem.selfMonitoredSample.size()

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg2 = objectFactory.createCreateMonitoringDatasetRequestMessage()
        requestMsg2.monitoringDatasetCollection = collection

        Request request2 = factory.createNewRequest(false, "flow")
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg2
        String soapRequest2 = generateRequest(reqWrap, request2)

        def booleanExceptionCaught = false
        try {
            log.info "Sending once!"
            response = cxfClient.send(soapRequest)
            log.info "Sending twice!"
            response = cxfClient.send(soapRequest2)
            log.debug "Response: " + response
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }
        then:
        cxfClient != null
        booleanExceptionCaught == true
        200 == response.httpResponse.statusCode
    }


    def "Test deleting laboratory report "() {
        given:
//        def c = Citizen.findBySsn(TEST_SSN)
//        def startNoOfSamples = SelfMonitoringSample.findAllByCitizen(c).reports.collect { it.size() }.sum()

        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        def collection = requestMsg.getMonitoringDatasetCollection()
        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)

        def uuid = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        def booleanExceptionCaught = false
        def noOfSamples = 0
        try {
            log.info "Sending once!"
            response = cxfClient.send(soapRequest)
            log.info "Resp: " + response
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

//        def requestedSamples = SelfMonitoringSample.findAllByCitizen(c)
//        noOfSamples = SelfMonitoringSample.findAllByCitizen(c).reports.collect { it.size() }.sum()

        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size() == 4

        when:
        DeleteMonitoringDataset dreqWrap = new DeleteMonitoringDataset()
        DeleteMonitoringDatasetRequestMessage deleteMonitoringRequestMessage = objectFactory.createDeleteMonitoringDatasetRequestMessage()
        deleteMonitoringRequestMessage.personCivilRegistrationIdentifier = TEST_SSN
        deleteMonitoringRequestMessage.uuidIdentifier = new ArrayList<String>()
        deleteMonitoringRequestMessage.uuidIdentifier.add(uuid)

        dreqWrap.deleteMonitoringDatasetRequestMessage = deleteMonitoringRequestMessage

        request = factory.createNewRequest(false, "flow2")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String deleteSoapRequest = generateRequest(dreqWrap, request)

        def endNoOfSamples
        try {
            log.info "Delete it again"
            response = cxfClient.send(deleteSoapRequest)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

//        endNoOfSamples = SelfMonitoringSample.findAllByCitizen(c).reports.collect { it.size() }.sum()

        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size() == 4
    }

    def "Test deleting sample"() {
        given:
//        def c = Citizen.findBySsn(TEST_SSN)
//        def startNoOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()

        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        def collection = requestMsg.getMonitoringDatasetCollection()
        def monitoringItem = objectFactory.createMonitoringDatasetCollectionType()
        monitoringItem.citizen = objectFactory.createCitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)

        def uuids = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.collect { it.uuidIdentifier }


        monitoringItem.selfMonitoredSample.clear()
        monitoringItem.selfMonitoredSample.add(sample)

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(generateHsuidHeaders())

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = generateRequest(reqWrap, request)

        def booleanExceptionCaught = false
        def noOfSamples = 0
        try {
            log.info "Sending once!"
            response = cxfClient.send(soapRequest)
            log.info "Resp: " + response
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

//        noOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()

        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode
        monitoringItem.selfMonitoredSample.size() == 1

        when:
        DeleteMonitoringDataset dreqWrap = new DeleteMonitoringDataset()
        DeleteMonitoringDatasetRequestMessage deleteMonitoringRequestMessage = objectFactory.createDeleteMonitoringDatasetRequestMessage()
        deleteMonitoringRequestMessage.personCivilRegistrationIdentifier = TEST_SSN
        deleteMonitoringRequestMessage.uuidIdentifier = new ArrayList<String>()
        deleteMonitoringRequestMessage.uuidIdentifier.addAll(uuids)

        dreqWrap.deleteMonitoringDatasetRequestMessage = deleteMonitoringRequestMessage

        request = factory.createNewRequest(false, "flow2")
        request.addNonSOSIHeader(generateHsuidHeaders())

        String deleteSoapRequest = generateRequest(dreqWrap, request)

        def endNoOfSamples
        try {
            log.info "Delete it again"
            response = cxfClient.send(deleteSoapRequest)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

//        endNoOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()
        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode

    }
}
