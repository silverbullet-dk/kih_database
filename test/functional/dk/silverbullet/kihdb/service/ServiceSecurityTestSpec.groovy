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
package dk.silverbullet.kihdb.service

import dk.nsi.hsuid.ActingUserCivilRegistrationNumberAttribute
import dk.nsi.hsuid.Attribute
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderDOMUtil
import dk.nsi.hsuid.HealthcareServiceUserIdentificationHeaderUtil
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.GetMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.GetMonitoringDatasetRequestMessage
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.ObjectFactory
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.version1.TestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import geb.spock.GebReportingSpec
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.io.IOUtils
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

class ServiceSecurityTestSpec extends GebReportingSpec {

    Logger log = LoggerFactory.getLogger(ServiceSecurityTestSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final static String PORT_NUMBER = System.getProperty("server.port", "8080")

    TestDataUtil tdu = new TestDataUtil()
    SosiTestDataUtil stdu = new SosiTestDataUtil()

    ObjectFactory objectFactory = new ObjectFactory()

    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/v2/monitoringDataset'
//    private final static String URL = 'http://localhost:8080/kih_database/services/monitoringDataset'

    SOSIFactory factory = stdu.createSOSIFactory()

    def cxfClient = new SOAPClient(URL)
    def response
    def resultSet


    public String generateRequest(GetMonitoringDataset cxfRequest, Request sosiRequest) {
        return generateRequest(cxfRequest, sosiRequest, true)
    }

    public String generateRequest(GetMonitoringDataset cxfRequest, Request sosiRequest, boolean validIdCard) {
        if (validIdCard) {
            IDCard signedIdCard = stdu.getSignedIDCard()
            sosiRequest.setIDCard(signedIdCard)

        } else {
            sosiRequest.setIDCard(stdu.createNewSystemIDCard())
        }
        Marshaller m = createMarshaller(GetMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, doc)
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

    private String documentToString(Request sosiRequest, Document doc) {
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


    def "test WSDL Queyry "() {
        given:
        def httpClient = new HttpClient()
        def getMethod = new GetMethod('http://localhost:' + PORT_NUMBER + '/kih_database/services/v2/monitoringDataset?wsdl')

        when:

        def response
        def status
        XmlSlurper slurper
        String bodyText
        try {
//            sb.toString()
            response = httpClient.executeMethod(getMethod)
            byte[] body = getMethod.getResponseBody()
            bodyText = IOUtils.toString(body, "UTF-8")
            slurper = new XmlSlurper()
            slurper.parseText(bodyText)
        } catch (Exception e) {
            log.error "Caught exception: " + e
            status = e.getMessage()
        }

        then:
        response == 200
        slurper != null
        bodyText.contains("definitions")
        bodyText.contains('<wsdl:binding name="V102MonitoringDatasetEndpointServiceSoapBinding"')

    }

    def "test valid certificate security"() {
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


    def "test invalid sosi headers"() {

        when:
        // Create a signed id card for calling service
        tdu = new TestDataUtil()
        SOSIFactory factory = stdu.createSOSIFactory()
        objectFactory = new ObjectFactory()

        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN


        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = generateRequest(reqWrap, request, false)
        log.debug "REquest: " + soapRequest

        boolean caughtException = false


        def lResponse
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            log.debug "Re:" + response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            caughtException = true

            log.error "Caught exception: " + e
        }

        then:
        caughtException == true
    }


    def "test missing hsuid headers"() {
        when:
        // Create a signed id card for calling service
        tdu = new TestDataUtil()
        SOSIFactory factory = stdu.createSOSIFactory()
        objectFactory = new ObjectFactory()

        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN


        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = generateRequest(reqWrap, request, true)
        log.debug "REquest: " + soapRequest

        boolean caughtException = false
        def lResponse
        try {
            response = cxfClient.send(soapRequest)
            lResponse = response
            log.debug "Re:" + response
            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
        } catch (SOAPFaultException e) {
            log.error "Caught exception: " + e

            caughtException = true
        }

        then:
        caughtException == true
    }


}
