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
package dk.silverbullet.kihdb.message

class DeleteMeasuringResults implements Serializable {

    static final long serialVersionUID = -7588980448693010499L;


    List<String> samples
    List<String> laboratoryReports

    String ssn

    public String toString() {
        return "SSN: " + this.ssn + " Samples: " + samples.join(" ") + " LaboratoryReports: " + laboratoryReports.join(" ")
    }

}
