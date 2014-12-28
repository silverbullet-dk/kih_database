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
package dk.silverbullet.kihdb.model

import dk.silverbullet.kihdb.util.ControllerUtil
import grails.plugins.springsecurity.Secured

@Secured(PermissionName.NONE)
class SelfMonitoringSampleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ_ALL)
    def index() {
        ControllerUtil.setPatientSession(session, null)
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ_ALL)
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        ControllerUtil.setPatientSession(session, null)
        [selfMonitoringSampleInstanceList: SelfMonitoringSample.list(params), selfMonitoringSampleInstanceTotal: SelfMonitoringSample.count()]
    }

    @Secured(PermissionName.SELF_MONITORING_SAMPLE_READ)
    def show(Long id) {
        def selfMonitoringSampleInstance = SelfMonitoringSample.get(id)

        ControllerUtil.setPatientSession(session, selfMonitoringSampleInstance?.citizen)

        if (!selfMonitoringSampleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample'), id])
            redirect(action: "list")
            return
        }

        [selfMonitoringSampleInstance: selfMonitoringSampleInstance]
    }
}
