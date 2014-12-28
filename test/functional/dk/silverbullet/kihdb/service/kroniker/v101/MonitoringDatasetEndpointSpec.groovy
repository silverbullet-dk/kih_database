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
package dk.silverbullet.kihdb.service.kroniker.v101

import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1.*
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.XmlConverterUtil
import dk.silverbullet.kihdb.util.version1_0_1.TestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.Request
import geb.spock.GebReportingSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException

import java.text.SimpleDateFormat

class MonitoringDatasetEndpointSpec extends GebReportingSpec {
    Logger log = LoggerFactory.getLogger(MonitoringDatasetEndpointSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/monitoringDataset'
//    private final static String URL = 'http://localhost:8080/kih_database/services/monitoringDataset'

    TestDataUtil tdu = new TestDataUtil()
    SosiTestDataUtil stdu = new SosiTestDataUtil()
    ObjectFactory objectFactory = new ObjectFactory()
    SOSIFactory factory = stdu.createSOSIFactory()

    def cxfClient = new SOAPClient(URL)
    def response
    def resultSet

    private Citizen nancy


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
        String soapRequest = tdu.generateRequest(reqWrap, request)
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

    def "Test citizen data"() {
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
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        String soapRequest = tdu.generateRequest(reqWrap, request)

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


    def "Test data since to and from date"() {
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

        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        // Add one year
        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(fromDate) // Now now should be after boot.

        requestMsg.toDate = XmlConverterUtil.getDateAsXml(toDate) // Now now should be after boot.

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = tdu.generateRequest(reqWrap, request)

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

        def cal = new GregorianCalendar()
        cal.set(2013, Calendar.AUGUST, 1)

        requestMsg.fromDate = XmlConverterUtil.getDateAsXml(cal.time) // Now now should be after boot.

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        soapRequest = tdu.generateRequest(reqWrap, request)

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

    def "Test empty SSN"() {
        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = tdu.generateRequest(reqWrap, request)

        try {
            response = cxfClient.send(soapRequest)
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        BigInteger.ZERO.intValue() == resultSet?.size()
    }

    def "Test upload of measurements"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        requestMsg.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        def expectedResultSize = 0

        for (sample in requestMsg.selfMonitoredSample) {
            expectedResultSize += sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
        }

        Request request = factory.createNewRequest(false, "flow")

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

        def lresponse

        try {
            response = cxfClient.send(soapRequest)
            lresponse = response
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list()
            // Extract the result set.
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e
        }

        then:
        cxfClient != null
        200 == response.httpResponse.statusCode
        expectedResultSize == resultSet.size()
    }

    def "Test no resultUnitText"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        requestMsg.selfMonitoredSample = tdu.loadTestdata(objectFactory)
        def expectedResultSize = 0

        for (sample in requestMsg.selfMonitoredSample) {
            for (report in sample.laboratoryReportExtendedCollection.laboratoryReportExtended) {
                report.resultUnitText = null
            }
            expectedResultSize += sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()
        }

        Request request = factory.createNewRequest(false, "flow")

        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

        def lresponse

        try {
            response = cxfClient.send(soapRequest)
            lresponse = response
            resultSet = response.CreateMonitoringDatasetResponse.CreateMonitoringDatasetResponseMessage.UuidIdentifier.list()
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
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        Request request = factory.createNewRequest(false, "flow")
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        def samples = tdu.loadTestdata(objectFactory)
        def sample = samples.get(0)
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier = null

        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
        requestMsg.selfMonitoredSample.add(sample)

        def expectedResultSize = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size()

        Request request = factory.createNewRequest(false, "flow")
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

        println "Sending: " + soapRequest

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
        expectedResultSize == resultSet.size()
    }

    def "Test error On same LaboratoryReportUUID"() {
        when:
        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg = objectFactory.createCreateMonitoringDatasetRequestMessage()
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        def samples = tdu.loadTestdata(objectFactory)
        def sample = samples.get(0)

        log.debug "Sample: " + sample
        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
        requestMsg.selfMonitoredSample.add(sample)

        def expectedResultSize = requestMsg.selfMonitoredSample.size()

        Request request = factory.createNewRequest(false, "flow")
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

        // Created the request
        CreateMonitoringDatasetRequestMessage requestMsg2 = objectFactory.createCreateMonitoringDatasetRequestMessage()
        requestMsg2.personCivilRegistrationIdentifier = TEST_SSN


        def samples2 = tdu.loadTestdata(objectFactory)
        def sample2 = samples.get(0)

        requestMsg2.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
        requestMsg2.selfMonitoredSample.add(sample2)

        Request request2 = factory.createNewRequest(false, "flow")
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg2
        String soapRequest2 = tdu.generateRequest(reqWrap, request2)

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
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        def samples = tdu.loadTestdata(objectFactory)
        def sample = samples.get(0)

        def uuid = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.get(0).uuidIdentifier

        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
        requestMsg.selfMonitoredSample.add(sample)

        Request request = factory.createNewRequest(false, "flow")
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        String deleteSoapRequest = tdu.generateRequest(dreqWrap, request)

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
        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        def samples = tdu.loadTestdata(objectFactory)
        def sample = samples.get(0)

        def uuids = sample.laboratoryReportExtendedCollection.laboratoryReportExtended.collect { it.uuidIdentifier }

        requestMsg.selfMonitoredSample = new ArrayList<SelfMonitoredSampleType>()
        requestMsg.selfMonitoredSample.add(sample)

        Request request = factory.createNewRequest(false, "flow")
        CreateMonitoringDataset reqWrap = new CreateMonitoringDataset()
        reqWrap.createMonitoringDatasetRequestMessage = requestMsg
        String soapRequest = tdu.generateRequest(reqWrap, request)

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
        requestMsg.selfMonitoredSample.size() == 1

        when:
        DeleteMonitoringDataset dreqWrap = new DeleteMonitoringDataset()
        DeleteMonitoringDatasetRequestMessage deleteMonitoringRequestMessage = objectFactory.createDeleteMonitoringDatasetRequestMessage()
        deleteMonitoringRequestMessage.personCivilRegistrationIdentifier = TEST_SSN
        deleteMonitoringRequestMessage.uuidIdentifier = new ArrayList<String>()
        deleteMonitoringRequestMessage.uuidIdentifier.addAll(uuids)

        dreqWrap.deleteMonitoringDatasetRequestMessage = deleteMonitoringRequestMessage

        request = factory.createNewRequest(false, "flow2")
        String deleteSoapRequest = tdu.generateRequest(dreqWrap, request)

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
