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
package dk.silverbullet.kihdb.services.sundhed

import dk.silverbullet.kihdb.model.Citizen
import grails.util.Holders

class RegisterCitizensInPersonalizationIndexService {

    def personaliseringsindeksSoapService

    private Integer applicationId = 532
    private String shortName = "KIHDB"


    RegisterCitizensInPersonalizationIndexService() {
        log.info "Initiating service"

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.application?.id) {
            applicationId = Integer.valueOf(Holders?.grailsApplication?.config?.sundhed?.dk?.application?.id)
        }
        if (Holders?.grailsApplication?.config?.sundhed?.dk?.shortName) {
            shortName = Holders?.grailsApplication?.config?.sundhed?.dk?.shortName
        }
    }

    def registerCitizens() {

        def unregisteredCitizens = Citizen.findAllByInPersonalizationIndex(false)
        log.debug "Starting registration run for " + unregisteredCitizens.size() + " citizens"
        def errors = false

        if (unregisteredCitizens.size() > 0 && personaliseringsindeksSoapService.isEnabled()) {
            for (citizen in unregisteredCitizens) {
                def registeredSuccesfully = personaliseringsindeksSoapService.updatePersonaliseringsindeks(citizen.ssn)
                log.info("Registered citizen.id: ${citizen.id} registered: ${registeredSuccesfully}")

                if (registeredSuccesfully) {
                    citizen.refresh()
                    citizen.inPersonalizationIndex = true

                    if (!citizen.save()) {
                        log.error "Error updating citizen with id: " + citizen.id + " errors: " + citizen.errors
                        errors = true
                    }
                }
            }

            [!errors, unregisteredCitizens.size()]
        } else {
            // Nothing to do.
            [true, 0]
        }

    }
}
