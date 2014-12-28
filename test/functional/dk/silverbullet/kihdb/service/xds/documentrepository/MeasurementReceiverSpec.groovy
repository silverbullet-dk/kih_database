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

import dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute
import dk.nsi.hsuid.Attribute
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderDOMUtil
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.*
import dk.silverbullet.kihdb.service.kroniker.v102.V102TestDataUtil
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.version1_0_1.TestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import geb.spock.GebReportingSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class MeasurementReceiverSpec extends GebReportingSpec {
    Logger log = LoggerFactory.getLogger(MeasurementReceiverSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final static String ALTERNATE_SSN = "2512504916"
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
        final HsuidHeader header = HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(
                "my-issuer",
                new ArrayList<Attribute>(Arrays.asList(new ActingUserCivilRegistrationNumberAttribute(TEST_SSN))));
        HealthcareServiceUserIdentificationHeaderDOMUtil.elementFromHeader(header)
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


    def "Test deleting laboratory report "() {
        given:

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


        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode

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


        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode
        sample.laboratoryReportExtendedCollection.laboratoryReportExtended.size() > 0
    }

    def "Test deleting sample"() {
        given:

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

        then:
        cxfClient != null
        booleanExceptionCaught == false
        200 == response.httpResponse.statusCode
    }


}
