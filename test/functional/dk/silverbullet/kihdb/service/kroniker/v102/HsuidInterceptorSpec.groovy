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

import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.GetMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.GetMonitoringDatasetRequestMessage
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.ObjectFactory
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.Request
import geb.spock.GebReportingSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException

class HsuidInterceptorSpec extends GebReportingSpec {
    Logger log = LoggerFactory.getLogger(HsuidInterceptorSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/v2/monitoringDataset'
//    private final static String URL = 'http://localhost:8080/kih_database/services/monitoringDataset'

    V102TestDataUtil tdu = new V102TestDataUtil()
    SosiTestDataUtil stdu = new SosiTestDataUtil()
    ObjectFactory objectFactory = new ObjectFactory()
    SOSIFactory factory = stdu.createSOSIFactory()

    def cxfClient = new SOAPClient(URL)
    def response
    def resultSet


    def "Test valid HSUID header"() {
        given:

        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = TEST_SSN

        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(tdu.generateHsuidHeaders(TEST_SSN))
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
        resultSet.size() > 0
    }

    def "Test missing HSUID header"() {
        given:

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


        def caughtException = false
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
        cxfClient != null
        true == caughtException
    }

    def "Test different CPR HSUID header"() {
        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)

        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = "1212121212"

        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        request.addNonSOSIHeader(tdu.generateHsuidHeaders(TEST_SSN))
        String soapRequest = tdu.generateRequest(reqWrap, request)
        log.debug "REquest: " + soapRequest

//        def expectedSize = SelfMonitoringSample.findAllByCitizen(citizen).size()

        def caughtException = false
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
        cxfClient != null
        true == caughtException
    }

    def "Test empty CPR HSUID header"() {
        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)

        when:
        // Created the request
        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
        reqWrap.getMonitoringDatasetRequestMessage = requestMsg

        requestMsg.personCivilRegistrationIdentifier = "1212121212"

        log.debug "Message: " + requestMsg

        Request request = factory.createNewRequest(false, "flow")
        String soapRequest = tdu.generateRequest(reqWrap, request)
        log.debug "REquest: " + soapRequest

//        def expectedSize = SelfMonitoringSample.findAllByCitizen(citizen).size()

        def caughtException = false
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
        cxfClient != null
        true == caughtException
    }


}
