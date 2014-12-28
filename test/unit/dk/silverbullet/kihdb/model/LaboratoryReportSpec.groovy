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

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class LaboratoryReportSpec extends Specification {


    @Unroll("isVitalSigns returns #isVitalSign based on #iupac")
    void "test isVitalSigns()"() {
        given:
        IUPACCode iupacCode = new IUPACCode(comment: "TESTCOMMENT", name: "TESTNAME")
        iupacCode.code = iupac
        LaboratoryReport l = new LaboratoryReport(iupacCode: iupacCode, resultText: "3.7", resultUnitText: "L")


        expect:
        isVitalSign == l.isVitalSigns()

        where:
        iupac      | isVitalSign
        "NPU33444" | false
        "NPU03011" | true
        "DNK05472" | true
        "MCS88019" | true
        "MCS88020" | true
        "NPU21692" | true
    }
}
