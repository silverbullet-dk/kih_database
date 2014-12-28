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

import dk.silverbullet.kihdb.model.types.Sex
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class CitizenSpec extends Specification {


    @Unroll("GetGendercode based on #ssn or #sex returns #genderCode")
    void "test getGenderCode"() {
        given:
        Citizen citizen = new Citizen(ssn: ssn, sex: sex)

        expect:
        genderCode == citizen.getGenderCode()

        where:
        ssn          | sex        | genderCode
        "2601901111" | Sex.MALE   | "M"
        "2601901111" | null       | "M"
        "2601901111" | Sex.FEMALE | "F"
        "2601901112" | Sex.FEMALE | "F"
        "2601901112" | null       | "F"
        null         | null       | "U"

    }

    @Unroll("Get ssn based on #ssn - handling all formats returns #result")
    void "test getSsn"() {
        given:
        Citizen citizen = new Citizen(ssn: ssn)

        expect:
        result == citizen.getFormattedSsn()

        where:
        ssn           | result
        "2601901111"  | "2601901111"
        "260190-1111" | "2601901111"
    }


}
