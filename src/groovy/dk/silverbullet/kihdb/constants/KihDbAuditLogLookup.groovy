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
package dk.silverbullet.kihdb.constants

import dk.silverbullet.kih.api.auditlog.AuditLogLookup
import org.slf4j.LoggerFactory

/**
 * Class used to hook into KIH audit log plugin to provide more humanreadable names for controller / action mappings
 */
class KihDbAuditLogLookup implements AuditLogLookup {

    def log = LoggerFactory.getLogger(KihDbAuditLogLookup.class)
    Map lookup = [:]

    def addCitizenText() {
        return [
                "search": "Søg patient"
        ]
    }

    KihDbAuditLogLookup() {
        // Setup controllers ....

        lookup["home"] = [
                "index": "Vis patientsøgning"
        ]

        lookup["logout"] = [
                "logout": "Log ud",
                "index" : "Vis log ind",
                "list"  : "Log ud list",
        ]
        lookup["login"] = [
                "auth"    : "Log ind",
                "authFail": "Fejlet login",
                "denied"  : "Log ind afvist",

        ]

        lookup["citizen"] = addStandardTexts("patient", "patienter")
        lookup["citizen"] << addCitizenText()

        lookup["measurementBase"] = addStandardTexts("måling", "målinger")
        lookup["selfMonitoringSample"] = addStandardTexts("måling", "målinger")
        lookup["laboratoryReport"] = addStandardTexts("målepunkt", "målepunkter")
        lookup["labProducer"] = addStandardTexts("måleproducent", "måleproducenter")

        //lookup["iupaccode"] = null

    }

    def addStandardTexts(String singular, String plural) {
        return [
                "create": "Opret ${singular}",
                "delete": "Slet ${singular}",
                "edit"  : "Rediger ${singular}",
                "index" : "Liste af ${plural}",
                "list"  : "Liste af ${plural}",
                "save"  : "Gem ${singular}",
                "show"  : "Vis ${singular}",
                "update": "Opdater ${singular}"
        ]
    }


    @Override
    Map retrieve() {
        return lookup
    }
}
