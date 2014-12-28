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
package dk.silverbullet.kihdb.util

import dk.silverbullet.kihdb.model.Citizen
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Methods used in controllers.
 */
class ControllerUtil {
    private static Logger log = LoggerFactory.getLogger(ControllerUtil.class)

    /**
     * Addeds a patients CPR number to session in order for it to be picked up by the audit log plugin
     * @param session
     * @param citizen The citizen to extract the SSN from. If null, clear the session
     * @return
     */
    public static setPatientSession(javax.servlet.http.HttpSession session, Citizen citizen) {
        log.debug "Setting patient: " + citizen

        if (citizen) {
            session.cpr = citizen?.ssn[0..5] + "-" + citizen?.ssn[6..9]
        } else {
            session.cpr = null
        }
    }
}
