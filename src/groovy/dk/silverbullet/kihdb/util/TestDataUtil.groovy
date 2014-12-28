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

import dk.silverbullet.kihdb.enums.MeasurementLocation
import dk.silverbullet.kihdb.enums.MeasurementScheduled
import dk.silverbullet.kihdb.enums.MeasurementTransferredBy
import dk.silverbullet.kihdb.enums.MeasuringDataClassification
import dk.silverbullet.kihdb.model.IUPACCode
import dk.silverbullet.kihdb.model.Instrument
import dk.silverbullet.kihdb.model.LabProducer
import dk.silverbullet.kihdb.model.LaboratoryReport
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestDataUtil {

    private static Logger log = LoggerFactory.getLogger(TestDataUtil.class)

    private LaboratoryReport createPulseMeasurement(Date d, String value) {
        def analysisText = "Puls;Hjerte"
        def resultText = value
        def resultUnitText = "1/min"
        def nationalSampleIdentifier = "9999999914"
        def resultEncodingIdentifier = "numeric"
        def measurementTransferredBy = "automatic"
        def measurementLocation = "home"
        def measuringDataClassification = "clinical"
        def measurementDuration = "120 sek"
        def measurementScheduled = "scheduled"
        def resultTypeOfInterval = "interval"
        def healthCareProfessionalComment = "Pt. havde meget svært ved at puste igennem idag"
        def measuringCircumstances = "Udblæsningen blev foretaget siddende på en stol lige efter morgenmaden"
        def resultMaximumText = ""
        def resultMinimumText = ""
        def iupacCode = new IUPACCode(name: "PULS", description: "Puls (20-200)", code: "NPU21692", unit: "1/min")
        def labProducer = new LabProducer(identifier: "Patient målt", identifierCode: "POT")
        def instrument = new Instrument(medComID: "EPQ12225", manufacturer: "AD Medical", productType: "Multifunction Kit", model: "UA-767PTB-C", softwareVersion: "10.0.3452r45")



        return createLaboratoryReport(resultTypeOfInterval, labProducer, iupacCode, instrument, nationalSampleIdentifier, analysisText, d, resultMaximumText, resultMinimumText, measurementDuration, measurementScheduled, measuringCircumstances, healthCareProfessionalComment, measurementTransferredBy, measurementLocation, measuringDataClassification, resultText, resultUnitText)
    }

    private LaboratoryReport createCalProtein(Date d, String value) {
        def mesultEncodingIdentifier = "numeric"
        def measurementTransferredBy = "automatic"
        def measurementLocation = "home"
        def measuringDataClassification = "clinical"
        def measurementDuration = "5 sek"
        def measurementScheduled = "scheduled"

        def iupacIdentifier = "NPU03958"

        def iupacCode = new IUPACCode(name: "PULS", description: "Puls (20-200)", code: iupacIdentifier, unit: "1/min")
        def labProducer = new LabProducer(identifier: "Patient målt", identifierCode: "POT")
        def instrument = new Instrument(medComID: "EPQ12225", manufacturer: "AD Medical", productType: "Multifunction Kit", model: "UA-767PTB-C", softwareVersion: "10.0.3452r45")


        def measuringCircumstances = "Måling foretaget før indtagelse af aftenmåltid"
        def healthCareProfessionalComment = "Aftalt at måle en gang om dagen, før aftenmåltidet."

        def resultOperatorIdentifier = "greater_than"
        def resultAbnormalIdentifier = "to_high"
        def resultMinimumText = "0"
        def resultMaximumText = "4"
        def resultTypeOfInterval = "physiological"

        def analysisText = "Protein;U"
        def resultText = value
        def resultUnitText = "g/L"
        def nationalSampleIdentifier = "999999999955"

        return createLaboratoryReport(resultTypeOfInterval, labProducer, iupacCode, instrument, nationalSampleIdentifier, analysisText, d, resultMaximumText, resultMinimumText, measurementDuration, measurementScheduled, measuringCircumstances, healthCareProfessionalComment, measurementTransferredBy, measurementLocation, measuringDataClassification, resultText, resultUnitText)

    }

    private LaboratoryReport createLaboratoryReport(String resultTypeOfInterval, LabProducer labProducer, IUPACCode iupacCode, Instrument instrument, String nationalSampleIdentifier, String analysisText, Date d, String resultMaximumText, String resultMinimumText, String measurementDuration, String measurementScheduled, String measuringCircumstances, String healthCareProfessionalComment, String measurementTransferredBy, String measurementLocation, String measuringDataClassification, String resultText, String resultUnitText) {
        def l = new LaboratoryReport()

        l.resultTypeOfInterval = resultTypeOfInterval
        l.producerOfLabResult = labProducer
        l.iupacCode = iupacCode
        l.instrument = instrument
        l.nationalSampleIdentifier = nationalSampleIdentifier
        l.analysisText = analysisText
        l.createdDateTime = d
        l.resultMaximumText = resultMaximumText
        l.resultMinimumText = resultMinimumText

        if (measurementDuration) {
            l.measurementDuration = measurementDuration
        }

        if (measurementScheduled) {
            l.measurementScheduled = MeasurementScheduled.fromValue(measurementScheduled)
        }

        if (measuringCircumstances) {
            l.measuringCircumstances = measuringCircumstances
        }

        if (healthCareProfessionalComment) {
            l.healthCareProfessionalComment = healthCareProfessionalComment
        }

        if (measurementTransferredBy) {
            l.measurementTransferredBy = MeasurementTransferredBy.fromString(measurementTransferredBy)
        }

        if (measurementLocation) {
            l.measurementLocation = MeasurementLocation.fromValue(measurementLocation)
        }


        if (measuringDataClassification) {
            l.measuringDataClassification = MeasuringDataClassification.fromValue(measuringDataClassification)
        }

        def laboratoryReportUUID = UUID.randomUUID().toString()


        l.laboratoryReportUUID = laboratoryReportUUID
        if (resultText) l.resultText = resultText
        if (resultUnitText) l.resultUnitText = resultUnitText

        return l
    }

}
