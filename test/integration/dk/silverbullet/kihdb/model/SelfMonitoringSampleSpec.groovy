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

import grails.test.spock.IntegrationSpec


class SelfMonitoringSampleSpec extends IntegrationSpec {

    def "Finding samples with no uuid in the database"() {
        given: "a new sample in the database"
        def citizen = new Citizen(ssn: '1212801234', dateOfBirth: new Date()).save()
        def sample = new SelfMonitoringSample(citizen: citizen, createdBy: "test", createdByText: 'test')

        when: "the sample is saved"
        sample.save()

        then: "it saved successfully and can be found in the database"
        sample.errors.errorCount == 0
        sample.id != null
        sample in SelfMonitoringSample.findAllByXdsDocumentUuidIsNull() == true
    }
}