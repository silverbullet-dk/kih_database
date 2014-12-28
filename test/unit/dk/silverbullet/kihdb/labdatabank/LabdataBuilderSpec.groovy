package dk.silverbullet.kihdb.labdatabank

import dk.dsdn.laboratoriemedicinbank.GemAnalysesvarResponse
import dk.dsdn.laboratoriemedicinbank.IVirtuelLaboratoriebankService
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.LabProducerController
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.xml.namespace.QName
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.ws.Dispatch
import javax.xml.ws.Service

@TestFor(LabProducerController)
@Mock([SelfMonitoringSample, LaboratoryReport])
class LabdataBuilderSpec extends Specification {

    def log = LoggerFactory.getLogger(LabdataBuilderSpec.class.name)

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'

        params['identifier'] = "En test værdi"
        params['identifierCode'] = "TST"
        params['id'] = 100099L
    }

    def "Test"() {
        given:
        def a = 1

        when:
        def builder = new LabdataBuilder()

        populateValidParams(params)
        LaboratoryReport laboratoryReport = new LaboratoryReport(params)

        Citizen citizen = new Citizen()
        citizen.ssn = '1211111111'
        citizen.firstName = 'Fred'
        citizen.lastName = 'Flintstone'

        SelfMonitoringSample selfMonitoringSample = new SelfMonitoringSample(params)
        selfMonitoringSample.id = 19900L
        selfMonitoringSample.createdDate = new Date()
        selfMonitoringSample.createdByText = 'TeleCare Nord'
        selfMonitoringSample.addToReports(laboratoryReport)
        selfMonitoringSample.citizen = citizen

        def emessage = LabdataBuilder.getEmessage(selfMonitoringSample)
        log.debug "Got: \n" + LabdataBuilder.convertToString(emessage) + '\n'

        then:
        a == 1
    }
}
