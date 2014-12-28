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

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.enums.*

class LaboratoryReport extends AbstractObject {

    static belongsTo = [sample: SelfMonitoringSample]

    String laboratoryReportUUID
    Date createdDateTime
    String analysisText
    String resultText
    EncodingIdentifier resultEncodingIdentifier
    OperatorIdentifier resultOperatorIdentifier
    String resultUnitText
    AbnormalIdentifier resultAbnormalIdentifier
    String resultMinimumText
    String resultMaximumText
    String resultTypeOfInterval
    String nationalSampleIdentifier
    IUPACCode iupacCode
    LabProducer producerOfLabResult
    Instrument instrument

    MeasurementTransferredBy measurementTransferredBy
    MeasurementLocation measurementLocation
    MeasuringDataClassification measuringDataClassification
    String measurementDuration

    MeasurementScheduled measurementScheduled

    // How to handle those?
    String healthCareProfessionalComment
    String measuringCircumstances
    boolean deleted

    static constraints = {
        resultAbnormalIdentifier(nullable: true)
        resultEncodingIdentifier(nullable: true)
        resultOperatorIdentifier(nullable: true)
        resultMaximumText(nullable: true)
        resultMinimumText(nullable: true)
        laboratoryReportUUID(nullable: false, unique: true)
        sample(nullable: true)
        instrument(nullable: true)
        measurementLocation(nullable: true)
        measurementDuration(nullable: true)
        measurementScheduled(nullable: true)
        measurementTransferredBy(nullable: true)
        measuringDataClassification(nullable: true)
        measuringCircumstances(nullable: true)
        healthCareProfessionalComment(nullable: true)
        resultTypeOfInterval(nullable: true)
        createdBy(nullable: true)
        sample(nullable: true)
        deleted(nullable: true)
    }


    static mapping = {
        healthCareProfessionalComment type: "text"
        measuringCircumstances type: "text"
    }

    boolean isVitalSigns() {
        return Constants.VITAL_SIGN_CODES.contains(this.iupacCode.code)
    }
}
