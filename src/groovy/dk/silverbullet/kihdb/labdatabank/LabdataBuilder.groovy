package dk.silverbullet.kihdb.labdatabank

import dk.oio.rep.sundcom_dk.medcom_dk.xml.schemas._2007._02._01.*
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar
import java.text.SimpleDateFormat

class LabdataBuilder {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat('HH:mm')
    private static final JAXBContext jaxbContext = JAXBContext.newInstance(Emessage.class)

    public static final String EAN_IDENTIFIER = '5790000120314' // Medcom
    public static final String IDENTIFIER = '999999' // Dummy

    public static String convertToString(Emessage emessage) {

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller()
        StringWriter stringWriter = new StringWriter()

        // output pretty printed
        jaxbMarshaller.setProperty Marshaller.JAXB_FORMATTED_OUTPUT, true
        ObjectFactory of = new ObjectFactory()
        jaxbMarshaller.marshal emessage, stringWriter

        return stringWriter.toString()
    }

    public static Emessage getEmessage(SelfMonitoringSample selfMonitoringSample) {

        Emessage emessage = new Emessage()
        if (selfMonitoringSample) {
            emessage.setEnvelope getEnvelopeType(selfMonitoringSample)
            emessage.getMicrobiologyWebReportOrMicrobiologyReportOrDoctorsCallCenterLetter().add getLaboratoryReportType(selfMonitoringSample)
        }
        return emessage
    }

    private static EnvelopeType getEnvelopeType(SelfMonitoringSample selfMonitoringSample) {
        EnvelopeType envelope = new EnvelopeType()
        envelope.setSent getDateTimeType(selfMonitoringSample.createdDate)
        envelope.setIdentifier selfMonitoringSample.id.toString()
        envelope.setAcknowledgementCode AcknowledgementCodeType.MINUSPOSITIVKVITT
        envelope
    }

    private static LaboratoryReportType getLaboratoryReportType(SelfMonitoringSample selfMonitoringSample) {
        LaboratoryReportType laboratoryReportType = new LaboratoryReportType()
        laboratoryReportType.setLetter getLaboratoryReportLetterType(selfMonitoringSample)

        laboratoryReportType.setPatient getLaboratoryReportPatientType(selfMonitoringSample)
        laboratoryReportType.setSender getLaboratoryReportSenderType(selfMonitoringSample)
        laboratoryReportType.setReceiver getLaboratoryReportReceiverType(selfMonitoringSample)
        laboratoryReportType.setRequisitionInformation getRequisitionInformation(selfMonitoringSample)
        laboratoryReportType.setLaboratoryResults getLaboratoryResults(selfMonitoringSample)
        laboratoryReportType
    }

    private static LaboratoryReportLetterType getLaboratoryReportLetterType(SelfMonitoringSample selfMonitoringSample) {
        LaboratoryReportLetterType letter = new LaboratoryReportLetterType()
        letter.setIdentifier selfMonitoringSample.id.toString()
        letter.setVersionCode VersionCodeType.XR_0130_K
        letter.setStatisticalCode TypeCodeType.XRPT_01.value()
        letter.setAuthorisation getDateTimeType()
        letter.setTypeCode TypeCodeType.XRPT_01
        letter
    }

    private static LaboratoryReportSenderType getLaboratoryReportSenderType(SelfMonitoringSample selfMonitoringSample) {
        LaboratoryReportSenderType sender = new LaboratoryReportSenderType()
        sender.setEANIdentifier EAN_IDENTIFIER
        sender.setIdentifier IDENTIFIER
        sender.setIdentifierCode(IdentifierCodeType.SYGEHUSAFDELINGSNUMMER)
        sender.setOrganisationName selfMonitoringSample.createdByText
        sender.setDepartmentName selfMonitoringSample.createdByText
        sender.setMedicalSpecialityCode(MedicalSpecialityCodeType.KLIN_BIOKEMI)
        sender
    }

    private
    static LaboratoryReportReceiverType getLaboratoryReportReceiverType(SelfMonitoringSample selfMonitoringSample) {
        LaboratoryReportReceiverType receiver = new LaboratoryReportReceiverType()
        receiver.setEANIdentifier EAN_IDENTIFIER
        receiver.setIdentifier IDENTIFIER
        receiver.setIdentifierCode(IdentifierCodeType.YDERNUMMER)
        receiver.setOrganisationName selfMonitoringSample.createdByText
        receiver.setDepartmentName selfMonitoringSample.createdByText
        receiver.setStreetName 'Forskerparken 10'
        receiver.setDistrictName 'Odense M'
        receiver.setPostCodeIdentifier '5230'
        LaboratoryReportReceiverType.Physician physician = new LaboratoryReportReceiverType.Physician()
        physician.setPersonInitials 'MCM'
        receiver.setPhysician(physician)
        receiver
    }

    private
    static LaboratoryReportPatientType getLaboratoryReportPatientType(SelfMonitoringSample selfMonitoringSample) {
        LaboratoryReportPatientType patient = new LaboratoryReportPatientType()
        patient.setCivilRegistrationNumber selfMonitoringSample?.citizen?.ssn
        def firstname = selfMonitoringSample?.citizen?.firstName
        if (selfMonitoringSample?.citizen?.middleName)
            firstname += ' ' + selfMonitoringSample?.citizen?.middleName
        patient.setPersonGivenName firstname?.trim()
        patient.setPersonSurnameName selfMonitoringSample?.citizen?.lastName
        patient
    }

    private static LaboratoryReportType.RequisitionInformation getRequisitionInformation(
            SelfMonitoringSample selfMonitoringSample) {

        LaboratoryReportType.RequisitionInformation requisitionInformation = new LaboratoryReportType.RequisitionInformation()
        BreakableText comments = new BreakableText()
        comments.getContent().add("Måling fra KIH-DB")
        requisitionInformation.setComments(comments)

        LaboratoryReportType.RequisitionInformation.Sample sample = new LaboratoryReportType.RequisitionInformation.Sample()
        sample.setLaboratoryInternalSampleIdentifier selfMonitoringSample.id.toString()
        sample.setRequesterSampleIdentifier selfMonitoringSample.id.toString()
        sample.setSamplingDateTime getDateTimeType(selfMonitoringSample.createdDate)

        requisitionInformation.setSample(sample)
        requisitionInformation
    }

    private static LaboratoryReportType.LaboratoryResults getLaboratoryResults(
            SelfMonitoringSample selfMonitoringSample) {

        LaboratoryReportType.LaboratoryResults laboratoryResults = new LaboratoryReportType.LaboratoryResults()

        LaboratoryReportType.LaboratoryResults.GeneralResultInformation generalResultInformation = new LaboratoryReportType.LaboratoryResults.GeneralResultInformation()
        generalResultInformation.setLaboratoryInternalProductionIdentifier selfMonitoringSample.id.toString()
        generalResultInformation.setResultsDateTime getDateTimeType(selfMonitoringSample.createdDate)
        laboratoryResults.setGeneralResultInformation(generalResultInformation)

        for (kihLaboratoryReport in selfMonitoringSample.reports)
            laboratoryResults.result.add(getResult(kihLaboratoryReport))

        laboratoryResults
    }

    private static LaboratoryReportType.LaboratoryResults.Result getResult(LaboratoryReport laboratoryReport) {

        LaboratoryReportType.LaboratoryResults.Result result = new LaboratoryReportType.LaboratoryResults.Result()

        result.setResultStatusCode 'svar_endeligt' // FAST

        LaboratoryReportType.LaboratoryResults.Result.Analysis analysis = new LaboratoryReportType.LaboratoryResults.Result.Analysis()
//        analysis.setAnalysisCode laboratoryReport.iupacCode.
//        analysis.setAnalysisCodeType laboratoryReport.iupacCode


        result.setAnalysis(analysis)
        LaboratoryReportType.LaboratoryResults.Result.ProducerOfLabResult producerOfLabResult = new LaboratoryReportType.LaboratoryResults.Result.ProducerOfLabResult()
        producerOfLabResult.setIdentifier 'setIdentifier'
        producerOfLabResult.setIdentifierCode 'SKS'
        result.setProducerOfLabResult(producerOfLabResult)
        LaboratoryReportType.LaboratoryResults.Result.ReferenceInterval referenceInterval = new LaboratoryReportType.LaboratoryResults.Result.ReferenceInterval()
        result.setReferenceInterval(referenceInterval)
        result.setResultType 'alfanumerisk' // AV = alfanumerisk, NV = numerisk
        result.setOperator 'mindre_end' // 7 = mindre_end, 6 = stoerre_end
        result.setValue '12'
        result.setUnit 'setUnit'
        result.setResultValidation 'for_hoej' // for_hoej = HI, for_lav = LO, unormal = UN
        result
    }

    private static DateTimeType getDateTimeType(Date date = new Date()) {
        DateTimeType authorisation = new DateTimeType()
        GregorianCalendar c = new GregorianCalendar()
        c.setTime(date)
        XMLGregorianCalendar aDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c)
        authorisation.setDate(aDate)
        authorisation.setTime TIME_FORMAT.format(date)
        authorisation
    }
}
