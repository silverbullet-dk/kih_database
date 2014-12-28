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

import grails.plugins.springsecurity.Secured

@Secured(PermissionName.NONE)
class LabProducerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.LAB_PRODUCER_READ)
    def show(Long id) {
        def labProducerInstance = LabProducer.get(id)
        if (!labProducerInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'labProducer.label', default: 'LabProducer'), id])
            redirect(action: "list")
            return
        }

        [labProducerInstance: labProducerInstance]
    }

}
