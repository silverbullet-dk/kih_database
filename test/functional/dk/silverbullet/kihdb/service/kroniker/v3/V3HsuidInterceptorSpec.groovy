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

import dk.nsi.hsuid.*
import dk.nsi.hsuid._2013._01.hsuid_1_1.HsuidHeader
import dk.silverbullet.kihdb.util.HsuidUtil
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import geb.spock.GebReportingSpec
import oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetRequestMessage
import oio.medcom.monitoringdataset._1_0.GetMonitoringDatasetResponseMessage
import oio.medcom.monitoringdataset._1_0.ObjectFactory
import oio.medcom.monitoringdataset._1_0_2.MonitoringDatasetPortType
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Element

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.ws.Service
import javax.xml.ws.soap.SOAPFaultException

class V3HsuidInterceptorSpec extends GebReportingSpec {


    Logger log = LoggerFactory.getLogger(V3HsuidInterceptorSpec.class)

    private final static String TEST_SSN = "2512484916"
    private final
    static String URL = 'http://localhost:' + System.getProperty("server.port", "8080") + '/kih_database/services/v3/monitoringDataset?wsdl'
//    private final static String URL = 'http://localhost:8080/kih_database/services/monitoringDataset'

    private final static QName hsuidQname = new QName("http://www.nsi.dk/hsuid/2013/01/hsuid-1.1.xsd", "HsuidHeader")

    private final static String v3monitoringDatasetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2"
    private final static String v3monintoringDatasetServiceName = "MonitoringDatasetService"

    private final static String HSUID_ISSUER = "my-issuer"
    private final static String HSUID_VENDOR = "MEDCOM"
    private final static String HSUID_SYSTEM_NAME = "KIHDB"
    private final static String HSUID_SYSTEM_VERSION = "1.2.1"
    private final static String HSUID_OPERATION_ORG = "SB"


    private final static String HSUID_RESPONSIBLE_USER = "1404444444"
    private final static String HSUID_RESPONSIBLE_USER_AUTHORIZATION_CODE = "12345"
    private final static String HSUID_ORG_USING_ID = "440081000016006"


    private MonitoringDatasetPortType servicePort = null

    SosiTestDataUtil stdu = new SosiTestDataUtil()
    ObjectFactory objectFactory = new ObjectFactory()


    private List<Header> appendHsuidHeader(List<Header> headersList, String issuer, String ssn, HsuidUtil.UserType type, String vendorName, String systemName, String systemVersion, String operationsOrganisation) {
        appendHsuidHeader(headersList, issuer, ssn, type, vendorName, systemName, systemVersion, operationsOrganisation, null, null, null)
    }


    private List<Header> appendHsuidHeader(List<Header> headersList, String issuer, String ssn, HsuidUtil.UserType type,
                                           String vendorName, String systemName, String systemVersion, String operationsOrganisation,
                                           String responsibleUserCivilRegistrationNumber, String responsibleUserAuthorizationCode,
                                           String organisationId) {
        if (!headersList) {
            headersList = new ArrayList<Header>()
        }

        Element hsuidElement = generateHsuidHeaders(issuer, ssn, type, vendorName, systemName, systemVersion, operationsOrganisation, responsibleUserCivilRegistrationNumber, responsibleUserAuthorizationCode, organisationId)
        SoapHeader hsuidHeader = new SoapHeader(hsuidQname, hsuidElement)
        headersList.add(hsuidHeader)
        return headersList
    }

    private Element generateHsuidHeaders(String issuer, String ssn, HsuidUtil.UserType type, String vendorName, String systemName, String systemVersion, String operationsOrganisation,
                                         String responsibleUserCivilRegistrationNumber, String responsibleUserAuthorizationCode, String organisationId) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>()

        if (ssn) {
            ActingUserCivilRegistrationNumberAttribute actingUserCivilRegistrationNumberAttribute
            actingUserCivilRegistrationNumberAttribute = new ActingUserCivilRegistrationNumberAttribute(ssn)
            attributes.add(actingUserCivilRegistrationNumberAttribute)
        }

        if (type) {
            UserTypeAttribute userTypeAttribute
            switch (type) {
                case HsuidUtil.UserType.CITIZEN:
                    userTypeAttribute = new UserTypeAttribute(UserTypeAttribute.LegalValues.CITIZEN.value)
                    break
                case HsuidUtil.UserType.HEALTHCARE_PROFFESIONAL:
                    userTypeAttribute = new UserTypeAttribute(UserTypeAttribute.LegalValues.HEALTHCAREPROFESSIONAL.value)
                    break
            }
            attributes.add(userTypeAttribute)
        }

        if (vendorName) {
            SystemVendorNameAttribute systemVendorNameAttribute = new SystemVendorNameAttribute(vendorName)
            attributes.add(systemVendorNameAttribute)
        }

        if (systemName) {
            SystemNameAttribute systemNameAttribute = new SystemNameAttribute(systemName)
            attributes.add(systemNameAttribute)
        }

        if (systemVersion) {
            SystemVersionAttribute systemVersionAttribute = new SystemVersionAttribute(systemVersion)
            attributes.add(systemVersionAttribute)
        }

        if (operationsOrganisation) {
            OperationsOrganisationNameAttribute operationsOrganisationNameAttribute = new OperationsOrganisationNameAttribute(operationsOrganisation)
            attributes.add(operationsOrganisationNameAttribute)
        }

        if (responsibleUserCivilRegistrationNumber) {
            ResponsibleUserCivilRegistrationNumberAttribute responsibleUserCivilRegistrationNumberAttribute = new ResponsibleUserCivilRegistrationNumberAttribute(responsibleUserCivilRegistrationNumber)
            attributes.add(responsibleUserCivilRegistrationNumberAttribute)
        }

        if (responsibleUserAuthorizationCode) {
            ResponsibleUserAuthorizationCodeAttribute responsibleUserAuthorizationCodeAttribute = new ResponsibleUserAuthorizationCodeAttribute(responsibleUserCivilRegistrationNumber)
            attributes.add(responsibleUserAuthorizationCodeAttribute)
        }

        if (organisationId) {
            OrganisationIdentifierAttribute organisationIdentifierAttribute = new OrganisationIdentifierAttribute(organisationId, OrganisationIdentifierAttribute.LegalFormatNames.SOR)
            attributes.add(organisationIdentifierAttribute)
        }

        final HsuidHeader header = HealthcareServiceUserIdentificationHeaderUtil.createHealthcareServiceUserIdentificationHeader(issuer, attributes)
        def headerAsElement = HealthcareServiceUserIdentificationHeaderDOMUtil.elementFromHeader(header)
        return headerAsElement
    }

    def createService() {
        QName qname = new QName(v3monitoringDatasetNamespace, v3monintoringDatasetServiceName)
        Service service = Service.create(new URL(URL), qname)
        servicePort = service.getPort(oio.medcom.monitoringdataset._1_0_2.MonitoringDatasetPortType)
    }

    def "Test valid citizen HSUID header"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.CITIZEN, HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response = servicePort.getMonitoringDataset(request)

        then:
        null != response
    }

    def "Test valid health care proffessional HSUID header"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.HEALTHCARE_PROFFESIONAL,
                HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG, HSUID_RESPONSIBLE_USER,
                HSUID_RESPONSIBLE_USER_AUTHORIZATION_CODE, HSUID_ORG_USING_ID)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response = servicePort.getMonitoringDataset(request)

        then:
        null != response
    }


    def "Test invalid health care proffessional HSUID header"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.HEALTHCARE_PROFFESIONAL,
                HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG, null, null, null)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)
        boolean caughtException = false
        String faultString

        try {
            GetMonitoringDatasetResponseMessage response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()


        }

        then:
        true == caughtException
        "The following attributes are missing from the header: [NSI_RESPONSIBLE_USER_CIVIL_REGISTRATION_NUMBER, NSI_RESPONSIBLE_USER_AUTHORIZATION_CODE, NSI_ORG_USING_ID]" == faultString

    }


    def "Test missing basic hsuid headers - UserType"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, null, HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "UserType missing" == faultString

    }


    def "Test missing hsuid header"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "HSUID Header is missing" == faultString
    }


    def "Test missing basic hsuid headers - Vendor Name"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.CITIZEN, null, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "The following attributes are missing from the header: [NSI_SYSTEM_OWNER_NAME]" == faultString

    }


    def "Test missing basic hsuid headers - System Name"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.CITIZEN, HSUID_VENDOR, null, HSUID_SYSTEM_VERSION, HSUID_OPERATION_ORG)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "The following attributes are missing from the header: [NSI_SYSTEM_NAME]" == faultString

    }

    def "Test missing basic hsuid headers - System Version"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.CITIZEN, HSUID_VENDOR, HSUID_SYSTEM_NAME, null, HSUID_OPERATION_ORG)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "The following attributes are missing from the header: [NSI_SYSTEM_VERSION]" == faultString

    }

    def "Test missing basic hsuid headers - Operation Organisation"() {
        given:
        createService()

        when:
        List<Header> headerList = stdu.exchangeAndSignIDCard()
        headerList = appendHsuidHeader(headerList, HSUID_ISSUER, TEST_SSN, HsuidUtil.UserType.CITIZEN, HSUID_VENDOR, HSUID_SYSTEM_NAME, HSUID_SYSTEM_VERSION, null)

        GetMonitoringDatasetRequestMessage request = objectFactory.createGetMonitoringDatasetRequestMessage()
        request.personCivilRegistrationIdentifier = TEST_SSN

        ((BindingProvider) servicePort).getRequestContext().put(Header.HEADER_LIST, headerList)

        GetMonitoringDatasetResponseMessage response

        boolean caughtException = false
        String faultString

        try {
            response = servicePort.getMonitoringDataset(request)
        } catch (SOAPFaultException e) {
            caughtException = true
            faultString = e.getMessage()
        } catch (Exception e) {
            log.debug "Caught exception : " + e
        }

        then:
        true == caughtException
        "The following attributes are missing from the header: [NSI_ORG_RESPONSIBLE_NAME]" == faultString

    }

//    def "Test missing HSUID header"() {
//        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)
//
//        when:
////        // Created the request
////        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
////        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
////        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
////
////        requestMsg.personCivilRegistrationIdentifier = TEST_SSN
////
////        log.debug "Message: " + requestMsg
////
////        Request request = factory.createNewRequest(false,"flow")
////        String soapRequest = tdu.generateRequest(reqWrap, request)
////        log.debug "REquest: " + soapRequest
////
////        def expectedSize = SelfMonitoringSample.findAllByCitizen(citizen).size()
////
////        def caughtException = false
////        def lResponse
////        try {
////            response = cxfClient.send(soapRequest)
////            lResponse = response
////            log.debug "Re:" + response
////            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
////        } catch (SOAPFaultException e) {
////            log.error "Caught exception: " + e
////            caughtException = true
//        }
//
//        then:
//        cxfClient != null
//        true == caughtException
//    }
//
//    def "Test different CPR HSUID header"() {
//        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)
//
//        when:
//        // Created the request
//        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
//        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
//        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
//
//        requestMsg.personCivilRegistrationIdentifier = "1212121212"
//
//        log.debug "Message: " + requestMsg
//
//        Request request = factory.createNewRequest(false,"flow")
//        request.addNonSOSIHeader(tdu.generateHsuidHeaders(TEST_SSN))
//        String soapRequest = tdu.generateRequest(reqWrap, request)
//        log.debug "REquest: " + soapRequest
//
//        def expectedSize = SelfMonitoringSample.findAllByCitizen(citizen).size()
//
//        def caughtException = false
//        def lResponse
//        try {
//            response = cxfClient.send(soapRequest)
//            lResponse = response
//            log.debug "Re:" + response
//            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//            caughtException = true
//        }
//
//        then:
//        cxfClient != null
//        true == caughtException
//    }
//
//    def "Test empty CPR HSUID header"() {
//        given:
//        def citizen = Citizen.findBySsn(TEST_SSN)
//
//        when:
//        // Created the request
//        GetMonitoringDataset reqWrap = new GetMonitoringDataset()
//        GetMonitoringDatasetRequestMessage requestMsg = objectFactory.createGetMonitoringDatasetRequestMessage()
//        reqWrap.getMonitoringDatasetRequestMessage = requestMsg
//
//        requestMsg.personCivilRegistrationIdentifier = "1212121212"
//
//        log.debug "Message: " + requestMsg
//
//        Request request = factory.createNewRequest(false,"flow")
//        String soapRequest = tdu.generateRequest(reqWrap, request)
//        log.debug "REquest: " + soapRequest
//
//        def expectedSize = SelfMonitoringSample.findAllByCitizen(citizen).size()
//
//        def caughtException = false
//        def lResponse
//        try {
//            response = cxfClient.send(soapRequest)
//            lResponse = response
//            log.debug "Re:" + response
//            resultSet = response.GetMonitoringDatasetResponse?.GetMonitoringDatasetResponseMessage?.CitizenMonitoringDataset?.SelfMonitoredSampleCollection?.SelfMonitoredSample.list()
//        } catch (SOAPFaultException e) {
//            log.error "Caught exception: " + e
//            caughtException = true
//        }
//
//        then:
//        cxfClient != null
//        true == caughtException
//    }


}
