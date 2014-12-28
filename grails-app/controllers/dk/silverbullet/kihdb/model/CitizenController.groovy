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
class CitizenController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.CITIZEN_READ_ALL)
    def index() {
        ControllerUtil.setPatientSession(session, null)
        redirect(action: "search", params: params)
    }

    @Secured(PermissionName.CITIZEN_READ_ALL)
    def list(Integer max) {
        ControllerUtil.setPatientSession(session, null)
        params.max = Math.min(max ?: 10, 100)
        [citizenInstanceList: Citizen.list(params), citizenInstanceTotal: Citizen.count()]
    }


    @Secured(PermissionName.CITIZEN_READ)
    def show(Long id) {
        def citizenInstance = Citizen.get(id)

        ControllerUtil.setPatientSession(session, citizenInstance)

        if (!citizenInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'citizen.label', default: 'Citizen'), id])
            redirect(action: "list")
            return
        }

        //Get all measurementBase objects excluding 'aggregated' objects (e.g. spirometri), order by date - i.e. the objects where analysisid IS set, and thus NOT NULL
        def max = params.max ? params.max : 10
        def offset = params.offset ? params.offset : 0
        def total = SelfMonitoringSample.findAllByCitizen(citizenInstance).size()
        def selfMonitoringSampleList = SelfMonitoringSample.findAllByCitizen(citizenInstance, [sort: "createdDateTime", order: "desc", max: max, offset: offset])
        [citizenInstance: citizenInstance, selfMonitoringSampleList: selfMonitoringSampleList, selfMonitoringSampleListTotal: total]
    }


    @Secured(PermissionName.CITIZEN_READ_SEARCH)
    def search() {
        def citizenList
        def firstName = params.firstName
        def lastName = params.lastName
        def ssn = params.ssn

        ControllerUtil.setPatientSession(session, null)

        if (request.method == "POST") {
            //Normalize cpr
            if (ssn.contains('-')) {
                def ssnParts = ssn.split('-')
                ssn = ssnParts[0] + "" + ssnParts[1]
            }

            //TODO: For Demo purpose
            if (ssn.equals('*')) {
                citizenList = Citizen.findAll("from Citizen as c order by c.firstName, c.lastName")
            } else {
                def query = "from Citizen as c where c.ssn=(:ssn)"
                citizenList = Citizen.findAll(query, [ssn: ssn])
            }
        } else {
            //Don't show anything per default due to audit logging
            //citizenList = Citizen.findAll()
        }

        [
                citizens : citizenList,
                cpr      : ssn,
                firstName: firstName,
                lastName : lastName
        ]
    }

}
