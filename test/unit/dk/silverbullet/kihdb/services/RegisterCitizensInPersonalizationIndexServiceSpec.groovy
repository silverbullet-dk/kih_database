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
package dk.silverbullet.kihdb.services

import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.services.sundhed.PersonaliseringsindeksSoapService
import dk.silverbullet.kihdb.services.sundhed.RegisterCitizensInPersonalizationIndexService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import java.text.SimpleDateFormat

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RegisterCitizensInPersonalizationIndexService)
@Mock(Citizen)
class RegisterCitizensInPersonalizationIndexServiceSpec extends Specification {

    def registerCitizensInPersonalizationIndexService
    PersonaliseringsindeksSoapService personaliseringsIndeksSoapService

    def setup() {
        registerCitizensInPersonalizationIndexService = new RegisterCitizensInPersonalizationIndexService()
        def mockService = Mock(PersonaliseringsindeksSoapService)
        mockService.updatePersonaliseringsindeks(_) >> true // Returns true no matter what.
        mockService.isEnabled() >> true // Returns true no matter what.
        personaliseringsIndeksSoapService = mockService
        registerCitizensInPersonalizationIndexService.personaliseringsindeksSoapService = personaliseringsIndeksSoapService

    }

    def cleanup() {
    }

    @Unroll("returns #success and #numberOfCitizens")
    void "test register citizen"() {
        given:

        for (int i = 1; i < numberOfCitizens + 1; i++) {

            // Create nancy
            def nancy = new Citizen(firstName: 'Nancy Ann ',
                    lastName: 'Berggren',
                    ssn: 2512484910 + i + 1,
                    dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                    sex: Sex.FEMALE,
                    streetName: 'Åbogade 15',
                    zipCode: '8200',
                    city: 'Aarhus N',
                    country: 'Denmark',
                    mobilePhone: "12345678",
                    contactTelephone: "12345690",
                    email: "email@example.com")

            nancy.save(failOnError: true)
        }

        def a, b
        (a, b) = registerCitizensInPersonalizationIndexService.registerCitizens()
        def c = Citizen.findAllByInPersonalizationIndex(true).size()

        expect:
        success == a
        noOfCitizens == b
        matchedCitizens == c

        where:
        numberOfCitizens | noOfCitizens | success | matchedCitizens
        0                | 0            | true    | 0
        1                | 1            | true    | 1
        9                | 9            | true    | 9
    }


}
