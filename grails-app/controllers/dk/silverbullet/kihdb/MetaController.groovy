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
package dk.silverbullet.kihdb

import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import dk.silverbullet.kihdb.model.User
import grails.converters.JSON
import grails.util.Environment

@SkipAuditLog
class MetaController {

    def statisticService

    def index() {}

    def currentServerVersion() {
        def version = grailsApplication.metadata['app.version']
        def minClientVersion = grailsApplication.metadata['app.requiredClientVersion']
        render(contentType: 'text/json') {
            [
                    'version'                     : version,
                    'minimumRequiredClientVersion': minClientVersion,
                    'serverEnvironment'           : Environment.getCurrent()?.getName()
            ]
        }
    }

    def isAlive() {
        def info = buildIsAliveInformation()
        render(view: "/meta", model: [info: info])
    }

    def isAliveJSON() {
        def info = buildIsAliveInformation()
        render text: "jsonCallback(${info as JSON});", contentType: 'text/javascript'
    }

    def noAccess() {
    }

    private Map<String, String> buildIsAliveInformation() {
        def info = [:]
        info['responseText'] = "I'm alive!"
        info['runningVersion'] = grailsApplication.metadata['app.version']
        info['environment'] = Environment.getCurrent()?.getName()

        def dbAlive = ''
        try {
            User.executeQuery("select 1 from User").size() > 0
            dbAlive = 'Yes.'
        } catch (Exception e) {
            dbAlive = 'No. ' + e.getMessage()
        }

        info['isDatabaseAlive'] = dbAlive

        statisticService.getCreators().each {
            info[it + ':Borgere'] = statisticService.getCitizens(it)
            info[it + ':Målingsgrupper'] = statisticService.getMeasurementGroups(it)
            info[it + ':Målinger'] = statisticService.getMeasurements(it)
        }

        return info
    }
}
