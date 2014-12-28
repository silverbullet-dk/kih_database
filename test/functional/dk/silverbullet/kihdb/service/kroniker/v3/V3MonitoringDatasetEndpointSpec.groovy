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
package dk.silverbullet.kihdb.service.kroniker.v3

import com.sun.xml.internal.ws.client.BindingProviderProperties
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.util.DateUtil
import dk.silverbullet.kihdb.util.HsuidUtil
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.version1_0_1.TestDataUtil
import oio.medcom.chronicdataset._1_0.CitizenType
import oio.medcom.monitoringdataset._1_0.CreateMonitoringDatasetRequestMessage
import oio.medcom.monitoringdataset._1_0.DeleteMonitoringDatasetRequestMessage
import oio.medcom.monitoringdataset._1_0.MonitoringDatasetCollectionType
import org.apache.cxf.headers.Header
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.ws.Service
import javax.xml.ws.soap.SOAPFaultException
import java.text.SimpleDateFormat

class V3MonitoringDatasetEndpointSpec extends Specification {
    Logger log = LoggerFactory.getLogger(V3MonitoringDatasetEndpointSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final static String ALTERNATE_SSN = "2512504916"

    private final static String K26_SSN = "2105669996"

    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/v3/monitoringDataset?wsdl'

    private final static QName hsuidQname = new QName("http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd", "HsuidHeader")

    private final static String v3monitoringDatasetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2"
    private final static String v3monintoringDatasetServiceName = "MonitoringDatasetService"

    private final static String HSUID_ISSUER = "my-issuer"
    private final static String HSUID_VENDOR = "MEDCOM"
    private final static String HSUID_SYSTEM_NAME = "KIHDB"
    private final static String HSUID_SYSTEM_VERSION = "1.2.1"
    private final static String HSUID_OPERATION_ORG = "SB"

    private oio.medcom.monitoringdataset._1_0_2.MonitoringDatasetPortType servicePort = null

    SosiTestDataUtil stdu = new SosiTestDataUtil()
    oio.medcom.monitoringdataset._1_0.ObjectFactory objectFactory = new oio.medcom.monitoringdataset._1_0.ObjectFactory()

    javax.sql.DataSource dataSource

    TestDataUtil tdu = new TestDataUtil()

    def retrieveSamples(String ssn) {
        retrieveSamples(ssn, null, null, null)
    }

    def retrieveSamples(String ssn, Date from, Date to, BigInteger maximumNumberOfSamples) {
        setupSosiAndHsuid(ssn)

        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        if (ssn) {
            request.personCivilRegistrationIdentifier = ssn
        }

        if (from) {
            request.fromDate = DateUtil.dateToXmlGregorianCalendar(from)
        }

        if (to) {
            request.toDate = DateUtil.dateToXmlGregorianCalendar(to)
        }

        if (maximumNumberOfSamples) {
            request.maximumReturnedMonitorering = maximumNumberOfSamples
        }

        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = servicePort.getMonitoringDataset(request)
        return response
    }

    private void setupSosiAndHsuid(String ssn) {
        List<Header> headers = stdu.exchangeAndSignIDCard()
        HsuidUtil.appendHsuidHeader(headers, HSUID_ISSUER, ssn, HsuidUtil.UserType.CITIZEN, HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG,)

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headers)
    }


    def createService() {
        QName qname = new QName(v3monitoringDatasetNamespace, v3monintoringDatasetServiceName)
        Service service = Service.create(new URL(URL), qname)
        servicePort = service.getPort(oio.medcom.monitoringdataset._1_0_2.MonitoringDatasetPortType)


        Map<String, Object> requestContext = ((BindingProvider) servicePort).getRequestContext();
        requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 300000); // Timeout in millis
        requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 100000); // Timeout in millis

    }


    private Citizen nancy
    private Citizen k26


    def setup() {
        log.info "Setting up service endpoints"
        createService()

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

        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN)

//        def expectedSize = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false).size()

        then:
        null != response
        0 < response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()
    }

    def "Test citizen data from CPR"() {
        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN)

        then:
        String resStreet = response?.citizenMonitoringDataset?.citizen?.addressPostal?.streetName.toString() + " " + response?.citizenMonitoringDataset?.citizen?.addressPostal?.streetBuildingIdentifier.toString()
        nancy?.ssn == response?.citizenMonitoringDataset?.citizen?.personCivilRegistrationIdentifier
        nancy?.streetName?.trim()?.equals(resStreet?.trim())
        nancy?.zipCode == response?.citizenMonitoringDataset?.citizen?.addressPostal?.postCodeIdentifier
        nancy?.firstName == response?.citizenMonitoringDataset?.citizen?.personNameStructure?.personGivenName
        nancy?.middleName == response?.citizenMonitoringDataset?.citizen?.personNameStructure?.getPersonMiddleName()
        nancy?.lastName == response?.citizenMonitoringDataset?.citizen?.personNameStructure?.personSurnameName
        nancy?.email == response?.citizenMonitoringDataset?.citizen?.emailAddress?.emailAddressIdentifier
        nancy?.contactTelephone == response?.citizenMonitoringDataset?.citizen?.phoneNumberSubscriber?.phoneNumberIdentifier
    }


    def "Test citizen data from failed CPR lookup"() {
        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(K26_SSN)

        then:
        response != null
        String resStreet = response?.citizenMonitoringDataset?.citizen?.addressPostal?.streetName

        k26?.ssn == response?.citizenMonitoringDataset?.citizen?.personCivilRegistrationIdentifier
        k26?.streetName?.trim()?.equals(resStreet?.trim())
        k26?.zipCode == response?.citizenMonitoringDataset?.citizen?.addressPostal?.postCodeIdentifier
        k26?.firstName == response?.citizenMonitoringDataset?.citizen?.personNameStructure?.personGivenName
        k26?.lastName == response?.citizenMonitoringDataset?.citizen?.personNameStructure?.personSurnameName
        k26?.email == response?.citizenMonitoringDataset?.citizen?.emailAddress?.emailAddressIdentifier
        k26?.contactTelephone == response?.citizenMonitoringDataset?.citizen?.phoneNumberSubscriber?.phoneNumberIdentifier
    }
//
//
//    def "Test HealthCareComment and MeasuringCircumstances"() {
//        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)
//
//        when:
//        // Created the request
//        // Created the request
//        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
//        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
//        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
//
//        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
//
//
//
//        Request request = factory.createNewRequest(false,"flow")
//        request.addNonSOSIHeader(generateHsuidHeaders())
//
//        String soapRequest = generateRequest(reqWrap, request)
//
//        def lResponse
//        def foundMeasuringCircumstances = false
//        def foundHealthProfessionalComment = false
//        try {
//            response = cxfClient.send(soapRequest)
//            lResponse = response
//            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
//
//            for (sample in resultSet) {
//                for (report in sample.LaboratoryReportExtendedCollection?.LaboratoryReportExtended.list()) {
//                    if (report.HealthCareProfessionalComment.toString() && report.HealthCareProfessionalComment.toString().length() > 0) {
//                        foundHealthProfessionalComment = true
//                    }
//                    if (report.MeasuringCircumstances.toString() && report.MeasuringCircumstances.toString().length() > 0) {
//                        foundMeasuringCircumstances = true
//                    }
//
//                }
//            }
//            // Find
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//        }
//
//
//
//
//        then:
//        cxfClient != null
//        String resStreet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetName.toString() +
//                " " + response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.StreetBuildingIdentifier.toString()
//
//        citizen.ssn == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonCivilRegistrationIdentifier.toString()
//        citizen.streetName.equals(resStreet)
//        citizen.zipCode == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.AddressPostal?.PostCodeIdentifier.toString()
//        citizen.firstName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonGivenName.toString()
//        citizen.middleName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonMiddleName.toString()
//        citizen.lastName == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PersonNameStructure.PersonSurnameName.toString()
//        citizen.email == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.EmailAddressIdentifier.toString()
//        citizen.contactTelephone == response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.Citizen?.PhoneNumberIdentifier.toString()
//        foundHealthProfessionalComment == true
//        foundMeasuringCircumstances == true
//
//
//    }
//
//
    def "Test number of results"() {
        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN, null, null, BigInteger.ONE)

        then:
        BigInteger.ONE.intValue() == response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()
    }

    def "Test data since date"() {
        when:

        def cal = new GregorianCalendar()
        cal.add(Calendar.YEAR, 1) // Add one year
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN, cal.time, null, null)

        then:
        0 == response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()
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

//        def results = []
//        for (report in reports) {
//            if (!results.contains(report.sample)) {
//                results << report.sample
//            }
//        }

        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN, fromDate, toDate, null)

        then:
        0 == response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()
    }


    def "Test data since and number of items "() {
        given:
        def cal = new GregorianCalendar()
        cal.set(2013, Calendar.AUGUST, 1) // Set date to 2013-08-01

        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN, cal.time, null, BigInteger.TEN)

        then:
        BigInteger.TEN == response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()

        when:
        cal.add(Calendar.YEAR, 2) // Add one year
        response = retrieveSamples(TEST_SSN, cal.time, null, null)

        then:
        BigInteger.ZERO == response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample.size()
    }


    def "Test no data on SSN"() {
        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(ALTERNATE_SSN, null, null, BigInteger.TEN)
        log.debug "response: " + response

        then:
        null == response.citizenMonitoringDataset.selfMonitoredSampleCollection
        null == response.citizenMonitoringDataset.citizen
        0 == response.citizenMonitoringDataset.author.size()
        null == response.citizenMonitoringDataset.legalAuthenticator
    }


    def "Test empty SSN"() {
        when:
        def caughtException = false
        try {
            oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(null, null, null, BigInteger.TEN)

        } catch (SOAPFaultException e) {
            caughtException = true
        }

        then:
        true == caughtException
    }
//
    def "Test upload of measurements"() {
        when:

        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        def expectedResultSize = 0

        for (sample in monitoringItem.selfMonitoredSample) {
            expectedResultSize += sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
        }

        setupSosiAndHsuid(TEST_SSN)

        def response = servicePort.createMonitoringDataset(requestMessage)


        then:
        1 == response.monitoringDatasetCollectionResponse.size()
        expectedResultSize == response.monitoringDatasetCollectionResponse.get(0).uuidIdentifier.size()
    }

    def "Test no SSN"() {
        when:
        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        setupSosiAndHsuid(TEST_SSN)

        def response = servicePort.createMonitoringDataset(requestMessage)

        then:
        BigInteger.ZERO == response.monitoringDatasetCollectionResponse.size()
    }

    def "Test no data"() {
        when:

        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
//        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        setupSosiAndHsuid(TEST_SSN)

        def response = servicePort.createMonitoringDataset(requestMessage)

        then:
        BigInteger.ZERO == response.monitoringDatasetCollectionResponse?.get(0)?.getUuidIdentifier()?.size()
    }

    def "Test empty UUID"() {
        when:
        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)

        def expectedResultSize = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()

        setupSosiAndHsuid(TEST_SSN)
        def response = servicePort.createMonitoringDataset(requestMessage)

        then:
        expectedResultSize == response.monitoringDatasetCollectionResponse.get(0).uuidIdentifier.size()
    }

    def "Test error On same LaboratoryReportUUID"() {
        when:
        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)

        def expectedResultSize = monitoringItem.selfMonitoredSample.size()


        CreateMonitoringDatasetRequestMessage requestMessage2 = new CreateMonitoringDatasetRequestMessage()
        requestMessage2.monitoringDatasetCollection = collection

        log.info "Sending once!"
        setupSosiAndHsuid(TEST_SSN)

        def response = servicePort.createMonitoringDataset(requestMessage)

        def booleanExceptionCaught = false
        String errorMessage
        try {
            response = servicePort.createMonitoringDataset(requestMessage2)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
            errorMessage = e.message
        }
        then:
        booleanExceptionCaught == true
        "Found 1 LaboratoryReports with the same UUID. Aborting." == errorMessage
    }


    def "Test deleting laboratory report "() {
        given:
//        def c = Citizen.findBySsn(TEST_SSN)
//        def startNoOfSamples = SelfMonitoringSample.findAllByCitizen(c).reports.collect { it.size() }.sum()

        when:
        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        monitoringItem.selfMonitoredSample.clear() // Clear it
        monitoringItem.selfMonitoredSample.add(sample)

        def uuid = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier
        setupSosiAndHsuid(TEST_SSN)

        def booleanExceptionCaught = false
        def noOfSamples = 0

        def response

        try {
            response = servicePort.createMonitoringDataset(requestMessage)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true

        }

//        def requestedSamples = SelfMonitoringSample.findAllByCitizen(c)
//        noOfSamples = SelfMonitoringSample.findAllByCitizen(c).reports.collect { it.size() }.sum()

        then:
        booleanExceptionCaught == false
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size() == 4

        when:
        DeleteMonitoringDatasetRequestMessage deleteRequest = new DeleteMonitoringDatasetRequestMessage()
        deleteRequest.personCivilRegistrationIdentifier = TEST_SSN
        deleteRequest.getUuidIdentifier().add(uuid)

        setupSosiAndHsuid(TEST_SSN)

        def endNoOfSamples
        try {
            log.info "Delete it again"
            response = servicePort.deleteMonitoringDataset(deleteRequest)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

        endNoOfSamples = 0

        then:
        booleanExceptionCaught == false
        0 + sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size() == 4
    }

    def "Test deleting sample"() {
        given:
//        def c = Citizen.findBySsn(TEST_SSN)
//        def startNoOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()

        when:
        CreateMonitoringDatasetRequestMessage requestMessage = new CreateMonitoringDatasetRequestMessage()
        def collection = requestMessage.getMonitoringDatasetCollection()

        def monitoringItem = new MonitoringDatasetCollectionType()
        monitoringItem.citizen = new CitizenType()
        monitoringItem.citizen.personCivilRegistrationIdentifier = TEST_SSN
        monitoringItem.selfMonitoredSample = tdu.loadTestdata(new oio.medcom.chronicdataset._1_0.ObjectFactory())

        collection.add(monitoringItem)

        def sample = monitoringItem.selfMonitoredSample.get(0)

        def uuids = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.collect { it.uuidIdentifier }

        monitoringItem.selfMonitoredSample.clear()
        monitoringItem.selfMonitoredSample.add(sample)

        def booleanExceptionCaught = false
        def noOfSamples = 0

        def response

        setupSosiAndHsuid(TEST_SSN)


        try {
            response = servicePort.createMonitoringDataset(requestMessage)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true

        }

//        def requestedSamples = SelfMonitoringSample.findAllByCitizen(c)
//        noOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()


        then:
        booleanExceptionCaught == false
        monitoringItem.selfMonitoredSample.size() == 1

        when:
        DeleteMonitoringDatasetRequestMessage deleteRequest = new DeleteMonitoringDatasetRequestMessage()
        deleteRequest.personCivilRegistrationIdentifier = TEST_SSN
        deleteRequest.getUuidIdentifier().addAll(uuids)
        setupSosiAndHsuid(TEST_SSN)


        def endNoOfSamples
        try {
            log.info "Delete it again"
            response = servicePort.deleteMonitoringDataset(deleteRequest)
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
            booleanExceptionCaught = true
        }

//        endNoOfSamples = SelfMonitoringSample.findAllByCitizenAndDeleted(c,false).size()

        then:
        booleanExceptionCaught == false
//        startNoOfSamples == endNoOfSamples
    }

    // Todo complete
    def "test author, custodian and legalauthenticator"() {
        given:

        when:
        oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage response = retrieveSamples(TEST_SSN)

//        def expectedSize = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false).size()

        then:
        null != response
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.id[0].identifier == '88878685'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.id[0].identifierCode == 'SOR'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getCountryIdentificationCode().getValue() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getDistrictName() == 'Svendborg'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getDistrictSubdivisionIdentifier() == '?'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getFloorIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getMailDeliverySublocationIdentifier() == 'Hjertemedicinsk afdeling B'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getMunicipalityName() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getPostCodeIdentifier() == '5700'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getPostOfficeBoxIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetBuildingIdentifier() == '53'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetName() == 'Valdemarsgade'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetNameForAddressingName() == '?'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getSuiteIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personGivenName == 'Anders'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personMiddleName == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personSurnameName == 'Andersen'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.emailAddress[0].emailAddressIdentifier == 'hjma@ouh-svendborg.dk'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.emailAddress[0].emailAddressUse.value() == 'WP'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.phoneNumberSubscriber[0].phoneNumberIdentifier == '65223344'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.phoneNumberSubscriber[0].phoneNumberUse.value() == 'WP'

//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getCountryIdentificationCode().getValue() == '?'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getDistrictName() == 'Svendborg'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getDistrictSubdivisionIdentifier() == '?'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getFloorIdentifier() == '?'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getMailDeliverySublocationIdentifier() == 'Hjertemedicinsk afdeling B'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getMunicipalityName() == '?'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getPostCodeIdentifier() == '5700'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getPostOfficeBoxIdentifier() == '?'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getStreetBuildingIdentifier() == '53'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getStreetName() == 'Valdemarsgade'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getStreetNameForAddressingName() == '?'
//        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.getAddress().getSuiteIdentifier() == '?'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.name == 'Odense Universitetshospital - Svendborg Sygehus'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberIdentifier == '65223344'
        response.citizenMonitoringDataset.custodian.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberUse.value() == 'WP'

        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.id[0].identifier == '88878685'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.id[0].identifierCode == 'SOR'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getCountryIdentificationCode().getValue() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getDistrictName() == 'Svendborg'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getDistrictSubdivisionIdentifier() == '?'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getFloorIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getMailDeliverySublocationIdentifier() == 'Hjertemedicinsk afdeling B'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getMunicipalityName() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getPostCodeIdentifier() == '5700'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getPostOfficeBoxIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetBuildingIdentifier() == '53'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetName() == 'Valdemarsgade'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getStreetNameForAddressingName() == '?'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAddress().getSuiteIdentifier() == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personGivenName == 'Anders'
//        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personMiddleName == '?'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.getAssignedPerson().personSurnameName == 'Andersen'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.emailAddress[0].emailAddressIdentifier == 'hjma@ouh-svendborg.dk'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.emailAddress[0].emailAddressUse.value() == 'WP'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.phoneNumberSubscriber[0].phoneNumberIdentifier == '65223344'
        response.citizenMonitoringDataset.legalAuthenticator.assignedEntity.phoneNumberSubscriber[0].phoneNumberUse.value() == 'WP'
    }

}
