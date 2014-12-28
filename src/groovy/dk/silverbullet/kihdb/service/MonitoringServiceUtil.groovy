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

import dk.silverbullet.kihdb.enums.*
import dk.silverbullet.kihdb.exception.ServiceStorageException
import dk.silverbullet.kihdb.model.*
import dk.silverbullet.kihdb.model.phmr.Author
import dk.silverbullet.kihdb.model.phmr.Custodian
import dk.silverbullet.kihdb.model.phmr.LegalAuthenticator
import dk.silverbullet.kihdb.model.types.IupacCode
import dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1.CreateMonitoringRequestMessage
import dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1.CreateMonitoringResponseMessage
import dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1.LaboratoryReportType
import dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1.SelfMonitoredSampleType
import dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.CreateMonitoringDatasetRequestMessage
import dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.CreateMonitoringDatasetResponseMessage
import dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.FormattedTextType
import dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.LaboratoryReportExtendedType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.xml.bind.JAXBElement

class MonitoringServiceUtil {
    private static Logger log = LoggerFactory.getLogger(MonitoringServiceUtil.class)

    /**
     * Retrieves or creates new cititzen for use by servicess.
     * @param cpr The SSN of the citizen to be retrieved
     * @return
     */
    public Citizen handleCitizen(String cpr) {

        def citizen = Citizen.findBySsn(cpr)
//        Citizen.withNewTransaction {
        if (!citizen) {
            citizen = new Citizen(ssn: cpr)

            def day = Integer.parseInt(cpr[0..1]).intValue()
            def month = Integer.parseInt(cpr[2..3]).intValue()
            def year = Integer.parseInt(cpr[4..5]).intValue()
            def d = new Date(month: month, date: day, year: year)
            citizen.dateOfBirth = d

            if (citizen.hasErrors()) {
                log.error "Error storing citizen: " + citizen.errors
                throw new ServiceStorageException("Error storing result", citizen.errors)
            } else {
                citizen.save()
            }

            log.info "Saved citizen."
        }
//        }

        return citizen
    }

    /**
     * Handles the extraction and storage of a monitoring request.
     * @param parameter The incoming marshalled object structure of the SOAP request
     * @param citizen The citizen that is the samples is to associated to.
     * @return The response message containing the UUID of the stored samples
     */
    public List<SelfMonitoringSample> handleMonitoringData(Citizen citizen, CreateMonitoringRequestMessage parameter, CreateMonitoringResponseMessage responseMessage) {
        List<SelfMonitoredSampleType> samples = parameter.selfMonitoredSample

        List<SelfMonitoringSample> retVal = new ArrayList<SelfMonitoringSample>()

        for (sample in samples) {
            SelfMonitoringSample s = createSelfMonitoringSample(sample, citizen)
            s.save()

            List<LaboratoryReportType> reports = sample.laboratoryReportCollection.laboratoryReport
            for (report in reports) {
                LaboratoryReport l = handleLaboratoryReportType(citizen, report, s)
                s.addToReports(l)
            }

            if (!s.save()) {
                def error = "Error storing result"
                log.error error + " " + s.errors
                throw new ServiceStorageException(error, s.errors)
            }

            responseMessage.uuidIdentifier.add(s.selfMonitoringSampleUUID)
            retVal.add(s)
        }
        return retVal
    }

    /**
     * Handles the extraction and storage of a monitoring request.
     * @param parameter The incoming marshalled object structure of the SOAP request
     * @param citizen The citizen that is the samples is to associated to.
     * @return The response message containing the UUID of the stored samples
     */
    public List<SelfMonitoringSample> handleMonitoringData(Citizen citizen, CreateMonitoringDatasetRequestMessage parameter, CreateMonitoringDatasetResponseMessage responseMessage) {
        List<dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.SelfMonitoredSampleType> samples = parameter.selfMonitoredSample
        List<SelfMonitoringSample> retVal = new ArrayList<SelfMonitoringSample>()

        for (sample in samples) {
            SelfMonitoringSample s = createSelfMonitoringSample(citizen, sample.createdByText)
            s.save()

            List<LaboratoryReportExtendedType> reports = sample.laboratoryReportExtendedCollection.laboratoryReportExtended
            for (report in reports) {
                LaboratoryReport l = handleLaboratoryReportExtendedType(citizen, report, sample.createdByText)
                s.addToReports(l)
                responseMessage.uuidIdentifier.add(l.laboratoryReportUUID)
            }
            retVal.add(s)
        }
        return retVal
    }

    /**
     * Creates Laboratory object from XML structure
     * @param report The JAXB XML object to map from
     * @param sample The SelfMonitoringSample the new object should be added to.
     * @return
     */
    private LaboratoryReport handleLaboratoryReportExtendedType(Citizen citizen, LaboratoryReportExtendedType report, String createdByText) {
        LabProducer labProducer = createLabProducer(report.producerOfLabResult.identifier, report.producerOfLabResult.identifierCode)
        IUPACCode iupac = createIUPACCode(report.iupacIdentifier)
        Instrument instrument = createInstrument(report.instrument?.medComID, report.instrument?.manufacturer, report.instrument?.model, report.instrument?.productType, report.instrument?.softwareVersion)
        String healthCareProfessionalComment = report?.healthCareProfessionalComment
        String measuringCircumstances = report?.measuringCircumstances

        LaboratoryReport l = createLaboratoryReport(report.uuidIdentifier, report.createdDateTime?.toGregorianCalendar().time, report.analysisText,
                report.resultText, report.resultEncodingIdentifier?.value(), report.resultOperatorIdentifier?.value(), report.resultUnitText, report.resultAbnormalIdentifier?.value(),
                report.resultMinimumText, report.resultMaximumText, report.resultTypeOfInterval, report.nationalSampleIdentifier,
                iupac, labProducer, instrument, report.measurementTransferredBy?.value(),
                report.measurementLocation?.value(), report.measuringDataClassification?.value(), report.measurementDuration,
                report.measurementScheduled?.value(), healthCareProfessionalComment,
                measuringCircumstances)

        return l
    }

    private String handleFormattedTextType(FormattedTextType formattedTextType) {
        def retVal = null

        if (formattedTextType) {
            def items = formattedTextType.content

            for (item in items) {
                log.debug "Item is type: " + item.class
                if (item instanceof javax.xml.bind.JAXBElement) {
                    log.debug "Items is jaxb"
                    log.debug "Name: : " + ((JAXBElement) item).name.toString()
                    log.debug "Value: : " + ((JAXBElement) item).value.toString()
                }
            }

            def text = formattedTextType.content
        }
        return null;
    }

    public LaboratoryReport createLaboratoryReport(String laboratoryReportUUID,
                                                   Date createdDateTime,
                                                   String analysisText,
                                                   String resultText,
                                                   String resultEncodingIdentifier,
                                                   String resultOperatorIdentifier,
                                                   String resultUnitText,
                                                   String resultAbnormalIdentifier,
                                                   String resultMinimumText,
                                                   String resultMaximumText,
                                                   String resultTypeOfInterval,
                                                   String nationalSampleIdentifier,
                                                   IUPACCode iupacCode,
                                                   LabProducer producerOfLabResult,
                                                   Instrument instrument,
                                                   String measurementTransferredBy,
                                                   String measurementLocation,
                                                   String measuringDataClassification,
                                                   String measurementDuration,
                                                   String measurementScheduled,
                                                   String healthCareProfessionalComment,
                                                   String measuringCircumstances, boolean saveLaboratoryReport) {
        def l = new LaboratoryReport()

        if (resultTypeOfInterval) l.resultTypeOfInterval = resultTypeOfInterval
        if (producerOfLabResult) l.producerOfLabResult = producerOfLabResult
        if (iupacCode) l.iupacCode = iupacCode
        if (instrument) l.instrument = instrument
        if (nationalSampleIdentifier) l.nationalSampleIdentifier = nationalSampleIdentifier
        if (analysisText) l.analysisText = analysisText
        if (createdDateTime) l.createdDateTime = createdDateTime
        if (resultMaximumText) l.resultMaximumText = resultMaximumText
        if (resultMinimumText) l.resultMinimumText = resultMinimumText

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

        if (!laboratoryReportUUID) {
//            log.info "UUID is empty. Generating new UUID"
            laboratoryReportUUID = UUID.randomUUID().toString()
        } else {
            laboratoryReportUUID = UUID.fromString(laboratoryReportUUID).toString()
        }

        // Check if the same UUID has been used. if yes, throw an exception
//        log.debug  "Laboratory UUID: " + laboratoryReportUUID

        def samplesSameUUID = LaboratoryReport.findAllByLaboratoryReportUUID(laboratoryReportUUID).size()
        if (samplesSameUUID > 0) {
            def errorMsg = "Found " + samplesSameUUID + " LaboratoryReports with the same UUID. Aborting."
            log.error errorMsg
            throw new ServiceStorageException(errorMsg)
        }


        l.laboratoryReportUUID = laboratoryReportUUID
        if (resultText) l.resultText = resultText
        if (resultUnitText) {
            l.resultUnitText = resultUnitText
        } else {
            l.resultUnitText = " " // Handle PROTEINURI
        }

        if (resultAbnormalIdentifier) {
            l.resultAbnormalIdentifier = AbnormalIdentifier.fromString(resultAbnormalIdentifier)
        }

        if (resultEncodingIdentifier) {
            l.resultEncodingIdentifier = EncodingIdentifier.fromString(resultEncodingIdentifier)
        }

        if (resultOperatorIdentifier) {
            l.resultOperatorIdentifier = OperatorIdentifier.fromString(resultOperatorIdentifier)
        }

        // Save it, or just return it?
        if (saveLaboratoryReport) {
            if (!l.save()) {
                def error = "Error storing result"
                log.error error + " " + l.errors
                throw new ServiceStorageException(error, l.errors)
            }
        }
        return l
    }


    public LaboratoryReport createLaboratoryReport(
            String laboratoryReportUUID,
            Date createdDateTime,
            String analysisText,
            String resultText,
            String resultEncodingIdentifier,
            String resultOperatorIdentifier,
            String resultUnitText,
            String resultAbnormalIdentifier,
            String resultMinimumText,
            String resultMaximumText,
            String resultTypeOfInterval,
            String nationalSampleIdentifier,
            IUPACCode iupacCode,
            LabProducer producerOfLabResult,
            Instrument instrument,
            String measurementTransferredBy,
            String measurementLocation,
            String measuringDataClassification,
            String measurementDuration,
            String measurementScheduled,
            String healthCareProfessionalComment,
            String measuringCircumstances) {

        return createLaboratoryReport(laboratoryReportUUID, createdDateTime, analysisText, resultText, resultEncodingIdentifier, resultOperatorIdentifier, resultUnitText, resultAbnormalIdentifier,
                resultMinimumText, resultMaximumText, resultTypeOfInterval, nationalSampleIdentifier, iupacCode, producerOfLabResult, instrument, measurementTransferredBy, measurementLocation,
                measuringDataClassification, measurementDuration, measurementScheduled, healthCareProfessionalComment, measuringCircumstances, true)
    }

    /**
     * Create instrument, if it does not exists, otherwise returns the instrument.
     * @param medcomId
     * @param manufacturer
     * @param model
     * @param productType
     * @param softwareVersion
     * @return the found object, or the created.
     */
    public Instrument createInstrument(String medcomId, String manufacturer, String model, String productType, String softwareVersion) {
        def instrument = null

        if (medcomId) {
            instrument = Instrument.findByMedComID(medcomId)

            def ma = (manufacturer ? manufacturer : null)
            def mo = (model ? model : null)
            def po = (productType ? productType : null)
            def so = (softwareVersion ? softwareVersion : null)


            if (!instrument) {
                instrument = new Instrument(medComID: medcomId, manufacturer: ma, model: mo, productType: po, softwareVersion: so)

                if (!instrument.save()) {
                    def error = "Error storing result"
                    log.error error + " " + instrument.errors
                    throw new ServiceStorageException(error, instrument.errors)
                }
            }
        }


        return instrument
    }

    private LaboratoryReport handleLaboratoryReportType(Citizen citizen, LaboratoryReportType report, SelfMonitoringSample sample) {
        def l = new LaboratoryReport()

        LabProducer labProducer = createLabProducer(report)
        IUPACCode iupac = createIUPACCode(report)

        l.sample = sample

        l.producerOfLabResult = labProducer
        l.iupacCode = iupac
        l.nationalSampleIdentifier = report.nationalSampleIdentifier
        l.analysisText = report.analysisText
        l.createdDateTime = report.createdDateTime.toGregorianCalendar().time
        l.resultMaximumText = report.resultMaximumText
        l.resultMinimumText = report.resultMinimumText


        String uuid
        if (!report?.uuidIdentifier) {
            log.info "UUID is empty. Generating new UUID"
            uuid = UUID.randomUUID().toString()
        } else {
            uuid = UUID.fromString(report.uuidIdentifier).toString()
        }

        // Check if the same UUID has been used. if yes, throw an exception
        log.debug "Laboratory UUID: " + uuid

        def samplesSameUUID = LaboratoryReport.findAllByLaboratoryReportUUID(uuid).size()
        if (samplesSameUUID > 0) {
            def errorMsg = "Found " + samplesSameUUID + " LaboratoryReports with the same UUID. Aborting."
            log.error errorMsg
            throw new ServiceStorageException(errorMsg)
        }


        l.laboratoryReportUUID = uuid
        l.resultText = report.resultText
        l.resultUnitText = report.resultUnitText


        if (report.resultAbnormalIdentifier?.value()) {
            l.resultAbnormalIdentifier = AbnormalIdentifier.fromString(report.resultAbnormalIdentifier.value())
        }

        if (report.resultEncodingIdentifier?.value()) {
            l.resultEncodingIdentifier = EncodingIdentifier.fromString(report.resultEncodingIdentifier.value())
        }

        if (report.resultOperatorIdentifier?.value()) {
            l.resultOperatorIdentifier = OperatorIdentifier.fromString(report.resultOperatorIdentifier.value())
        }
        if (!l.save()) {
            def error = "Error storing result"
            log.error error + " " + l.errors
            throw new ServiceStorageException(error, l.errors)
        }
        return l
    }

    /**
     * Finds an exisiting IUPAC Code or creates a new one, if required.
     * @param report The LaboratoryReport to use
     * @return An IUPAC Code object
     *
     *
     */
    public IUPACCode createIUPACCode(LaboratoryReportType report) {
        return createIUPACCode(report.iupacIdentifier)
    }


    public IUPACCode createIUPACCode(String iupacIdentifier) {
        def iupac

        String iupacCode = handleBrokenIupacCodes(iupacIdentifier)

        iupac = IUPACCode.findByCode(iupacCode)
        if (!iupac) {
            iupac = new IUPACCode(code: iupacCode, name: iupacCode, description: iupacCode, unit: iupacCode)

            if (!iupac.validate()) {
                log.error "Error validating IUpac: " + iupac.errors
            }

            if (!iupac.save()) {
                def error = "Error storing result"
                log.error error + " " + iupac.errors
                status.setRollbackOnly()
                throw new ServiceStorageException(error, iupac.errors)
            }
        }
        return iupac
    }

    String handleBrokenIupacCodes(String s) {
        String retVal

        IupacCode iu = IupacCode.fromValue(s)
        switch (iu) {
            case IupacCode.FEV1:
                retVal = "MCS88015"
                break;
            case IupacCode.FVC:
                retVal = "MCS88016"
                break;
            case IupacCode.FEV1_FVC:
                retVal = "MCS88017"
                break;
            case IupacCode.SATURATION:
                retVal = "NPU03011"
                break;
            case IupacCode.SYSTOLIC_CLINIC:
                retVal = "DNK05472"
                break;
            case IupacCode.DIASTOLIC_CLINIC:
                retVal = "DNK05473"
                break;
            case IupacCode.SYSTOLIC_HOME:
                retVal = "MCS88019"
                break;
            case IupacCode.DIASTOLIC_HOME:
                retVal = "MCS88020"
                break;
            case IupacCode.PULSE:
                retVal = "NPU21692"
                break;
            case IupacCode.BLOODSUGAR:
                retVal = "NPU02187"
                break;
            case IupacCode.WEIGHT:
                retVal = "NPU03804"
                break;
            case IupacCode.TEMPERATURE:
                retVal = "NPU08676"
                break;
            case IupacCode.URINE:
                retVal = "NPU03958"
                break;
            case IupacCode.URINE_GLUCOSE:
                retVal = "NPU03936"
                break;
            case IupacCode.CRP:
                retVal = "NPU01423"
                break;
            default:
                retVal = null
                break;
        }

        if (!retVal) {
            retVal = s
        }

        return retVal
    }

    /**
     * Finds an exisiting LabProducer or creates a new one, if required.
     * @param report The LaboratoryReport to use for data
     * @return
     */
    public LabProducer createLabProducer(LaboratoryReportType report) {
        return createLabProducer(report.producerOfLabResult.identifier, report.producerOfLabResult.identifierCode)
    }

    public LabProducer createLabProducer(String identifier, String identifierCode) {
        def labProducer = LabProducer.findByIdentifierCode(identifierCode)
        if (!labProducer) {
            labProducer = new LabProducer(identifier: identifier, identifierCode: identifierCode)

            if (!labProducer.save()) {
                def error = "Error storing result"
                log.error error + " " + labProducer.errors
                log.error "Rolling back transaction"
                status.setRollbackOnly()
                throw new ServiceStorageException(error, labProducer.errors)
            }
        }
        return labProducer
    }

    private SelfMonitoringSample createSelfMonitoringSample(Citizen citizen, String createdBy, Author author = null, Custodian custodian = null, LegalAuthenticator legalAuthenticator = null) {
        SelfMonitoringSample s = new SelfMonitoringSample()
        s.citizen = citizen
        s.createdByText = createdBy
        s.author = author
        s.custodian = custodian
        s.legalAuthenticator = legalAuthenticator

        if (!s.save()) {
            def errorMsg = "Could not store SelfMonitoringSample"
            log.error errorMsg + " " + s.errors
            throw new ServiceStorageException(errorMsg, s.errors)
        }

        return s
    }

    private SelfMonitoringSample createSelfMonitoringSample(SelfMonitoredSampleType sample, Citizen citizen) {
        def createdBy = sample?.createdByText
        def createdAt = sample?.createdDateTime.toGregorianCalendar().time
        def identifier = sample?.sampleCategoryIdentifier


        String uuid

        // Handle empty UUID's.
        if (!sample?.uuidIdentifier) {
            log.info "UUID is empty. Generating new UUID"
            uuid = UUID.randomUUID().toString()
        } else {
            uuid = UUID.fromString(sample.uuidIdentifier).toString()
        }

        def samplesSameUUID = SelfMonitoringSample.findAllBySelfMonitoringSampleUUID(uuid).size()

        if (samplesSameUUID > 0) {
            def errorMsg = "Found " + samplesSameUUID + " SelfMonitoringSample with the same UUID. Aborting."
            log.error errorMsg
            throw new ServiceStorageException(errorMsg)
        }

        log.debug "SelfMonitoringSample UUID: " + uuid
        def s = new SelfMonitoringSample()
        s.createdByText = createdBy
        s.createdDateTime = createdAt
        s.selfMonitoringSampleUUID = uuid
        s.sampleCategoryIdentifier = identifier
        s.citizen = citizen
        return s
    }
}
