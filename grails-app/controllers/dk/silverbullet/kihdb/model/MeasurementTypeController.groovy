package dk.silverbullet.kihdb.model

import dk.silverbullet.kihdb.util.ControllerUtil
import grails.plugins.springsecurity.Secured

@Secured(PermissionName.NONE)
class MeasurementTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ_ALL)
    def index() {
        ControllerUtil.setPatientSession(session, null)
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ_ALL)
    def list(Integer max) {
        log.debug '**** list ****'
        params.max = Math.min(max ?: 10, 100)
        ControllerUtil.setPatientSession(session, null)
        [measurementTypeList: MeasurementType.list(params), measurementTypeTotal: MeasurementType.count()]
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ)
    def edit(Long id) {
        def measurementType = MeasurementType.get(id)

        if (!measurementType) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), id])
            redirect(action: "list")
            return
        }

        [measurementTypeInstance: measurementType]
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ)
    def update() {
        def measurementType = MeasurementType.get(params.id)

        if (!measurementType) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), params.id])
            redirect(action: "list")
            return
        }
        measurementType.properties = params

        if (!measurementType.save(flush: true)) {
            render(view: "edit", model: [measurementTypeInstance: measurementType])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), params.code])

        redirect(action: "list")
        return
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ)
    def delete() {
        log.debug 'delete....'
        def measurementType = MeasurementType.get(params.id)

        if (!measurementType) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), params.id])
            redirect(action: "list")
            return
        }

        measurementType.delete(flush: true)
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), params.code])

        redirect(action: "list")
        return
    }
}
