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
package dk.silverbullet.kihdb.service

import spock.lang.Specification
import spock.lang.Unroll

class MonitoringServiceUtilSpec extends Specification {
    private MonitoringServiceUtil mu = new MonitoringServiceUtil()


    @Unroll("Maps #iupaccode to #identifier")
    def "Test handling of broken iupacCode codes"() {
        expect:
        iupaccode == mu.handleBrokenIupacCodes(identifier)

        where:
        identifier       | iupaccode
        "FEV1"           | "MCS88015"
        "SATURATION"     | "NPU03011"
        "SYSTOLIC_HOME"  | "MCS88019"
        "DIASTOLIC_HOME" | "MCS88020"
        "PULSE"          | "NPU21692"
        "BLOODSUGAR"     | "NPU02187"
        "WEIGHT"         | "NPU03804"
        "CRP"            | "NPU01423"
        "URINE"          | "NPU03958"
        "NPU17997"       | "NPU17997"
    }


}
