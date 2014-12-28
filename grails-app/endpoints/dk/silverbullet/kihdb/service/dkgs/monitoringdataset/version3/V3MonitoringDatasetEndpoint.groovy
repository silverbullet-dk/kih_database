package dk.silverbullet.kihdb.service.dkgs.monitoringdataset.version3

import dk.nsi._2011._09._23.stamdatacpr.PersonLookupResponseType
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType
import dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType
import dk.oio.rep.medcom_sundcom_dk.xml.schemas._2007._02._01.PersonInformationStructureType
import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressPostalType
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.model.*
import dk.silverbullet.kihdb.model.phmr.Custodian
import dk.silverbullet.kihdb.model.phmr.LegalAuthenticator
import dk.silverbullet.kihdb.service.MonitoringServiceUtil
import dk.silverbullet.kihdb.util.XmlConverterUtil
import oio.medcom.chronicdataset._1_0.*
import oio.medcom.monitoringdataset._1_0.*
import oio.medcom.monitoringdataset._1_0_2.FaultMessage
import oio.medcom.monitoringdataset._1_0_2.MonitoringDatasetPortType
import org.apache.cxf.jaxws.context.WrappedMessageContext
import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import javax.jws.WebParam
import javax.jws.WebService
import javax.xml.ws.WebServiceContext

@WebService(targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2", name = "MonitoringDatasetPortType", serviceName = "MonitoringDatasetService")
@GrailsCxfEndpoint(address = '/v3/monitoringDataset', inInterceptors = ["sosiInInterceptor", "hsuidInInterceptor"], outInterceptors = ["sosiOutInterceptor", "hsuidOutInterceptor"])
class V3MonitoringDatasetEndpoint implements MonitoringDatasetPortType {

    def stamdataLookupSoapService

    static expose = EndpointType.JAX_WS

    def phmrService


    private MonitoringServiceUtil util = new MonitoringServiceUtil()

    @Resource
    WebServiceContext wsContext;

    private static
    final oio.medcom.monitoringdataset._1_0.ObjectFactory monitoringDatasetObjectFactory = new oio.medcom.monitoringdataset._1_0.ObjectFactory()
    private static
    final oio.medcom.chronicdataset._1_0.ObjectFactory chronicDatasetObjectFactory = new oio.medcom.chronicdataset._1_0.ObjectFactory()

    private static
    final dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.ObjectFactory addressObjectFactory = new dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.ObjectFactory()
    private static
    final dk.oio.rep.itst_dk.xml.schemas._2006._01._17.ObjectFactory personNameObjectFactory = new dk.oio.rep.itst_dk.xml.schemas._2006._01._17.ObjectFactory()


    @Override
    @Transactional
    DeleteMonitoringDatasetResponseMessage deleteMonitoringDataset(
            @WebParam(partName = "parameter", name = "DeleteMonitoringDatasetRequestMessage", targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2") DeleteMonitoringDatasetRequestMessage deleteMonitoringDatasetRequestMessage) throws FaultMessage {
        def mContext = (WrappedMessageContext) wsContext.messageContext
        def cpr = deleteMonitoringDatasetRequestMessage.personCivilRegistrationIdentifier

        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION, "DeleteMonitoringDataset")
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_SERVICE, V3MonitoringDatasetEndpoint.name)
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR, cpr)

        def uuids = deleteMonitoringDatasetRequestMessage?.uuidIdentifier
        def citizen = Citizen.findBySsn(cpr)

        def deletedsamples = []

        def deleteLaboratoryReports = []

        def samplesCollection = []

        try {
            if (citizen && uuids && uuids.size() > 0) {
                for (uuid in uuids) {
                    log.info "Deleting laboratory report : " + uuid + " from citizen: " + citizen
                    def l = LaboratoryReport.findByLaboratoryReportUUID(uuid)
                    SelfMonitoringSample s = l.sample

                    if (!samplesCollection.contains(l.sample)) {
                        samplesCollection << l.sample
                    }

                }

            } else {
                log.debug "One is null - Citizen: " + citizen + " UUID: " + uuids
            }
        } catch (Exception e) {
            log.error "Could not delete sample. Message: " + e.message

            FaultType fault = new FaultType()

            fault.code = Constants.ERROR_CODE_COULD_NOT_DELETE_MESSAGE
            fault.cause = Constants.ERROR_TEXT_COULD_NOT_DELETE_MESSAGE
            fault.detail = e.message
            fault.system = Constants.ERROR_SYSTEM_NAME

            throw new FaultMessage(fault.cause, fault)
        }

        log.debug "Delete marking reports in samples"

        // Mark everything as deleted
        for (sample in samplesCollection) {
            SelfMonitoringSample s = (SelfMonitoringSample) sample

            // Handle the reports
            for (report in s.reports) {
                report = report.refresh()
                report.deleted = true
                report.save()
            }

            // Handle the sample
            s.deleted = true
            s.xdsRegisteredInRegistry = false
            s.save(flush: true)
        }

        DeleteMonitoringDatasetResponseMessage r = new DeleteMonitoringDatasetResponseMessage()
        return r
    }

    @Override
    @Transactional
    CreateMonitoringDatasetResponseMessage createMonitoringDataset(
            @WebParam(partName = "parameter", name = "CreateMonitoringDatasetRequestMessage", targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2") CreateMonitoringDatasetRequestMessage createMonitoringDatasetRequestMessage) throws FaultMessage {
        def mContext = (WrappedMessageContext) wsContext.messageContext
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION, "CreateMonitoringDataset v3")
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_SERVICE, V3MonitoringDatasetEndpoint.name)

        def cprs = []

        CreateMonitoringDatasetResponseMessage responseMessage = new CreateMonitoringDatasetResponseMessage()
        def inputs = createMonitoringDatasetRequestMessage.monitoringDatasetCollection

        for (input in inputs) {

            def resp = new MonitoringDatasetCollectionResponseType()

            def citizen = handleCitizen(input.citizen)
            cprs << citizen.ssn

            resp.personCivilRegistrationIdentifier = citizen.ssn

            def samples = []

            AuthorType author
            if (input?.author) {
                author = phmrService.saveAuthorType(input?.author)
                log.debug "Author: " + author
            }

            LegalAuthenticator legalAuthenticator
            if (input?.legalAuthenticator) {
                legalAuthenticator = phmrService.saveLegalAuthenticatorType(input?.legalAuthenticator)
                log.debug "Legal authenticator: " + legalAuthenticator

            }

            Custodian custodian
            if (input?.custodian) {
                custodian = phmrService.saveCustidian(input?.custodian)
                log.debug "Custodian: " + custodian
            }

            for (sample in input.selfMonitoredSample) {
                SelfMonitoringSample s = util.createSelfMonitoringSample(citizen, sample.createdByText, author, custodian, legalAuthenticator)
                samples << s.id

                List<LaboratoryReportExtendedType> reports = sample.laboratoryReportExtendedCollection.laboratoryReportExtended
                for (report in reports) {
                    LaboratoryReport l = handleLaboratoryReportExtendedType(citizen, report, sample.createdByText)
                    s.addToReports(l)
                    resp.getUuidIdentifier().add(l.laboratoryReportUUID)
                }
            }

            responseMessage.getMonitoringDatasetCollectionResponse().add(resp)
        }

        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR, cprs.join("#"))
        return responseMessage
    }

    @Override
    @Transactional
    GetMonitoringDatasetResponseMessage getMonitoringDataset(
            @WebParam(partName = "parameter", name = "GetMonitoringDatasetRequestMessage", targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.2") GetMonitoringDatasetRequestMessage getMonitoringDatasetRequestMessage) throws FaultMessage {
        def ssn = getMonitoringDatasetRequestMessage?.personCivilRegistrationIdentifier
        def mContext = (WrappedMessageContext) wsContext.messageContext
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR, ssn)
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION, "GetMonitoringDataset")

        GetMonitoringDatasetResponseMessage response = monitoringDatasetObjectFactory.createGetMonitoringDatasetResponseMessage()

        def dateFrom
        def dateTo
        def queryOptions = [sort: "createdDateTime", order: "desc"]


        response.citizenMonitoringDataset = chronicDatasetObjectFactory.createCitizenMonitoringDatasetType()

        def citizen


        if (ssn) {
            citizen = Citizen.findBySsn(ssn)
        }

        if (citizen) {
            if (getMonitoringDatasetRequestMessage?.fromDate?.toGregorianCalendar()?.time) {
                dateFrom = getMonitoringDatasetRequestMessage?.fromDate?.toGregorianCalendar()?.time
            }
            if (getMonitoringDatasetRequestMessage?.toDate?.toGregorianCalendar()?.time) {
                dateTo = getMonitoringDatasetRequestMessage?.toDate?.toGregorianCalendar()?.time
            }


            def numberOfResults = getMonitoringDatasetRequestMessage.maximumReturnedMonitorering?.intValue()

            if (numberOfResults) {
                queryOptions << [max: numberOfResults.intValue()]
            }

            response.citizenMonitoringDataset.citizen = retrieveCitizen(citizen)

            response.citizenMonitoringDataset.selfMonitoredSampleCollection = chronicDatasetObjectFactory.createSelfMonitoredSampleCollectionType()
            response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample = findSamples(citizen, queryOptions, dateFrom, dateTo,response)


            // TODO: Send Author, LegalAuthenticator, Custodian.

        }

        return response

    }

    /**
     * Map types together
     * @param addres
     * @return
     */
    private AddressPostalType handleAddress(dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType addres) {
        AddressPostalType retVal = new AddressPostalType()

        retVal.countryIdentificationCode = new CountryIdentificationCodeType()

        if (addres.countryIdentificationCode) retVal.countryIdentificationCode.value = addres.countryIdentificationCode.value
        if (addres.districtName) retVal.districtName = addres.districtName
        if (addres.districtSubdivisionIdentifier) retVal.districtSubdivisionIdentifier = addres.districtSubdivisionIdentifier
        if (addres.floorIdentifier) retVal.floorIdentifier = addres.floorIdentifier
        if (addres.mailDeliverySublocationIdentifier) retVal.mailDeliverySublocationIdentifier = addres.mailDeliverySublocationIdentifier
        if (addres.postCodeIdentifier) retVal.postCodeIdentifier = addres.postCodeIdentifier
        if (addres.postOfficeBoxIdentifier) retVal.postOfficeBoxIdentifier = addres.postOfficeBoxIdentifier
        if (addres.streetBuildingIdentifier) retVal.streetBuildingIdentifier = addres.streetBuildingIdentifier
        if (addres.streetName) retVal.streetName = addres.streetName
        if (addres.streetNameForAddressingName) retVal.streetNameForAddressingName = addres.streetNameForAddressingName
        if (addres.suiteIdentifier) retVal.suiteIdentifier = addres.streetBuildingIdentifier

        return retVal
    }

    private PersonNameStructureType handlePerson(dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType person) {
        PersonNameStructureType retVal = new PersonNameStructureType()
        if (person.personGivenName) retVal.personGivenName = person.personGivenName
        if (person.personMiddleName) retVal.personMiddleName = person.personMiddleName
        if (person.personSurnameName) retVal.personSurnameName = person.personSurnameName

        return retVal
    }

    /**
     * Buiilds a citizen type from either StamdataLookup or from model stored in the database
     * @param citizen The citizen to use
     * @return A CititenType Object
     */
    private CitizenType retrieveCitizen(Citizen citizen) {

        PersonLookupResponseType r = stamdataLookupSoapService.invokeStamdataLookup(citizen.ssn)
        log.debug "Result from Stamdata service: " + r

        CitizenType retVal = new CitizenType()
        if (r && r.personInformationStructure) {
            PersonInformationStructureType pi = r.personInformationStructure.get(0)
            retVal.personCivilRegistrationIdentifier = pi.currentPersonCivilRegistrationIdentifier
            retVal.addressPostal = handleAddress(pi.personAddressStructure.addressComplete.addressPostal)
            retVal.personNameStructure = handlePerson(pi.regularCPRPerson.simpleCPRPerson.personNameStructure)
        } else {
            retVal.personCivilRegistrationIdentifier = citizen.ssn

            if (citizen.firstName && citizen.lastName) {
                retVal.personNameStructure = new PersonNameStructureType()
                retVal.personNameStructure.personGivenName = citizen.firstName
                retVal.personNameStructure.personSurnameName = citizen.lastName
                retVal.personNameStructure.personMiddleName = citizen.middleName
            }
            if (citizen.streetName && citizen.zipCode) {
                retVal.addressPostal = new AddressPostalType()
                retVal.addressPostal.countryIdentificationCode = new CountryIdentificationCodeType().setValue(citizen.country)
                retVal.addressPostal.postCodeIdentifier = citizen.zipCode
                retVal.addressPostal.streetName = citizen.streetName
            }
        }

        if (citizen.contactTelephone) {
            retVal.phoneNumberSubscriber = chronicDatasetObjectFactory.createPhoneNumberSubscriberType()
            retVal.phoneNumberSubscriber.phoneNumberUse = AddressUseType.H
            retVal.phoneNumberSubscriber.phoneNumberIdentifier = citizen.contactTelephone
        }
        if (citizen.email) {
            retVal.emailAddress = chronicDatasetObjectFactory.createEmailAddressType()
            retVal.emailAddress.emailAddressUse = AddressUseType.H
            retVal.emailAddress.emailAddressIdentifier = citizen.email
        }


        return retVal
    }

    /**
     * Finds and creates the samples for a citizen
     * @param citizen
     * @param queryOptions
     * @param dataSince
     * @return
     */
    List<SelfMonitoredSampleType> findSamples(Citizen citizen, def queryOptions, def dateFrom, def dateTo, GetMonitoringDatasetResponseMessage response) {
        log.info "Find samples citizen id: " + citizen?.id + " " + queryOptions + " From: " + dateFrom + " to: " + dateTo
        List<SelfMonitoredSampleType> retVal = new ArrayList<SelfMonitoredSampleType>()

        def samples = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen, false)

        for (sample in samples) {
            if (sample.author) {
                AuthorType author = phmrService.authorToAuthorType(sample?.author)
                if (author) response.getCitizenMonitoringDataset().getAuthor().add(author)
            }
            if (sample.custodian) {
                CustodianType custodianType = phmrService.custodianToCustodianType(sample?.custodian)
                if (custodianType) response.citizenMonitoringDataset?.setCustodian(custodianType)
            }

            if (sample.legalAuthenticator) {
                LegalAuthenticatorType legalAuthenticatorType = phmrService.legalAuthenticatorToLegalAuthenticatorType(sample?.legalAuthenticator)
                if (legalAuthenticatorType) response.citizenMonitoringDataset.setLegalAuthenticator(legalAuthenticatorType)
            }
        }

        def res = []

        if (dateFrom || dateTo) {

            def reports = []

            if (dateFrom && dateTo) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeBetween(false, samples, dateFrom, dateTo)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeBetween(citizen,dateFrom,dateTo)
            }

            if (dateFrom && !dateTo) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeGreaterThan(false, samples, dateFrom)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeGreaterThan(citizen,dateFrom)
            }

            if (dateTo && !dateFrom) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeLessThan(false, samples, dateTo)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeLessThan(citizen,dateTo)
            }


            for (report in reports) {
                if (!res.contains(report?.sample)) {
                    res << report.sample
                }
            }

            if (queryOptions?.max && res?.size() > queryOptions.max) {
                res = res.subList(0, queryOptions.max)
            }
        }

        if (!dateFrom && !dateTo) {
            res = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen, false, queryOptions)
        }


        res.each { sample ->
            SelfMonitoredSampleType s = chronicDatasetObjectFactory.createSelfMonitoredSampleType()
            s.createdByText = sample.createdByText

            s.laboratoryReportExtendedCollection = chronicDatasetObjectFactory.createLaboratoryReportExtendedCollectionType()
            s.laboratoryReportExtendedCollection.laboratoryReportExtended = handleLaboratoryReportExtended(sample)

            retVal.add(s)
        }

        log.info "Returning " + retVal.size() + " SelfMonitoringSample"

        return retVal
    }

    List<LaboratoryReportExtendedType> handleLaboratoryReportExtended(SelfMonitoringSample selfMonitoringSample) {
        List<LaboratoryReportExtendedType> retVal = new ArrayList<LaboratoryReportExtendedType>()

        def reports = LaboratoryReport.findAllBySampleAndDeleted(selfMonitoringSample, false)

        reports.each() { report ->


            LaboratoryReport lr = (LaboratoryReport) report
            def r = chronicDatasetObjectFactory.createLaboratoryReportExtendedType()

            if (lr.laboratoryReportUUID) {
                r.uuidIdentifier = lr.laboratoryReportUUID
            }

            if (lr.createdDateTime) {
                r.createdDateTime = XmlConverterUtil.getDateAsXml(lr.createdDateTime)
            }
            if (lr.analysisText) {
                r.analysisText = lr.analysisText
            }
            if (lr.resultText) {
                r.resultText = lr.resultText
            }
            if (lr.resultEncodingIdentifier) {
                r.resultEncodingIdentifier = EncodingIdentifierType.fromValue(lr.resultEncodingIdentifier?.value())
            }
            if (lr.resultOperatorIdentifier) {
                r.resultOperatorIdentifier = OperatorIdentifierType.fromValue(lr.resultOperatorIdentifier?.value())
            }
            if (lr.resultUnitText) {
                r.resultUnitText = lr.resultUnitText
            }
            if (lr.resultAbnormalIdentifier) {
                r.resultAbnormalIdentifier = AbnormalIdentifierType.fromValue(lr.resultAbnormalIdentifier?.value())
            }
            if (lr.resultMinimumText) {
                r.resultMinimumText = lr.resultMinimumText
            }
            if (lr.resultMaximumText) {
                r.resultMaximumText = lr.resultMaximumText
            }
            if (lr.resultTypeOfInterval) {
                r.resultTypeOfInterval = lr.resultTypeOfInterval
            }
            if (lr.nationalSampleIdentifier) {
                r.nationalSampleIdentifier = lr.nationalSampleIdentifier
            }
            if (lr.iupacCode) {
                r.iupacIdentifier = lr.iupacCode?.code
            }
            if (lr.producerOfLabResult) {
                r.producerOfLabResult = handleLapProducer(lr)
            }
            if (lr.instrument) {
                r.instrument = handleInstrument(lr)
            }

            if (lr.measurementTransferredBy) {
                r.measurementTransferredBy = MeasurementTransferredByType.fromValue(lr.measurementTransferredBy.value())
            }
            if (lr.measurementLocation) {
                r.measurementLocation = MeasurementLocationType.fromValue(lr.measurementLocation.value())
            }
            if (lr.measuringDataClassification) {
                r.measuringDataClassification = MeasuringDataClassificationType.fromValue(lr.measuringDataClassification.value())
            }
            if (lr.measurementDuration) {
                r.measurementDuration = lr.measurementDuration
            }

            if (lr.measurementScheduled) {
                r.measurementScheduled = MeasurementScheduledType.fromValue(lr.measurementScheduled.value())
            }

            // How to handle those?
            if (lr.healthCareProfessionalComment) {
                r.healthCareProfessionalComment = lr.healthCareProfessionalComment

            }
            if (lr.measuringCircumstances) {
                r.measuringCircumstances = lr.measuringCircumstances
            }

            retVal.add(r)
        }


        return retVal
    }

    InstrumentType handleInstrument(LaboratoryReport r) {
        def retVal = chronicDatasetObjectFactory.createInstrumentType()

        retVal.manufacturer = r.instrument?.manufacturer
        retVal.medComID = r.instrument?.medComID
        retVal.model = r.instrument?.model
        retVal.productType = r.instrument?.productType
        retVal.softwareVersion = r.instrument?.softwareVersion

        return retVal
    }


    ProducerOfLabResultType handleLapProducer(LaboratoryReport laporatoryReport) {
        def retVal = chronicDatasetObjectFactory.createProducerOfLabResultType()

        retVal.identifier = laporatoryReport.producerOfLabResult.identifier
        retVal.identifierCode = laporatoryReport.producerOfLabResult.identifierCode

        return retVal
    }

    /**
     * Creates Laboratory object from XML structure
     * @param report The JAXB XML object to map from
     * @param sample The SelfMonitoringSample the new object should be added to.
     * @return
     */
    private LaboratoryReport handleLaboratoryReportExtendedType(Citizen citizen, LaboratoryReportExtendedType report, String createdByText) {
        LabProducer labProducer = util.createLabProducer(report.producerOfLabResult.identifier, report.producerOfLabResult.identifierCode)
        IUPACCode iupac = util.createIUPACCode(report.iupacIdentifier)
        Instrument instrument = util.createInstrument(report.instrument?.medComID, report.instrument?.manufacturer, report.instrument?.model, report.instrument?.productType, report.instrument?.softwareVersion)
        String healthCareProfessionalComment = report?.healthCareProfessionalComment
        String measuringCircumstances = report?.measuringCircumstances

        LaboratoryReport l = util.createLaboratoryReport(report.uuidIdentifier, report.createdDateTime?.toGregorianCalendar().time, report.analysisText,
                report.resultText, report.resultEncodingIdentifier?.value(), report.resultOperatorIdentifier?.value(), report.resultUnitText, report.resultAbnormalIdentifier?.value(),
                report.resultMinimumText, report.resultMaximumText, report.resultTypeOfInterval, report.nationalSampleIdentifier,
                iupac, labProducer, instrument, report.measurementTransferredBy?.value(),
                report.measurementLocation?.value(), report.measuringDataClassification?.value(), report.measurementDuration,
                report.measurementScheduled?.value(), healthCareProfessionalComment,
                measuringCircumstances)

        return l
    }


    private Citizen handleCitizen(CitizenType citizenType) {

        def cpr = citizenType?.personCivilRegistrationIdentifier

        def citizen

        if (cpr) {
            citizen = Citizen.findBySsn(cpr)
            if (!citizen) {

                citizen = new Citizen(ssn: citizenType?.personCivilRegistrationIdentifier, firstName: citizenType?.personNameStructure?.personGivenName,
                        lastName: citizenType?.personNameStructure?.personSurnameName, middleName: citizenType?.personNameStructure?.personMiddleName,
                        email: citizenType?.emailAddress?.emailAddressIdentifier, contactTelephone: citizenType?.phoneNumberSubscriber?.phoneNumberIdentifier)

                def day = Integer.parseInt(citizenType?.personCivilRegistrationIdentifier[0..1]).intValue()
                def month = Integer.parseInt(citizenType?.personCivilRegistrationIdentifier[2..3]).intValue()
                def year = Integer.parseInt(citizenType?.personCivilRegistrationIdentifier[4..5]).intValue()
                def d = new Date(month: month, date: day, year: year)
                citizen.dateOfBirth = d

                // TODO: Handle address better - potentially
                citizen.streetName = citizenType?.addressPostal?.streetName
                citizen.zipCode = citizenType?.addressPostal?.postCodeIdentifier
                citizen.country = citizenType?.addressPostal.countryIdentificationCode
                citizen.save()

                if (citizen.hasErrors()) {
                    log.error "Error storing citizen: " + citizen.errors

                    FaultType fault = new FaultType()

                    fault.code = Constants.ERROR_CODE_COULD_CREATE_CITIZEN_MESSAGE
                    fault.cause = Constants.ERROR_TEXT_COULD_CREATE_CITIZEN_MESSAGE
                    fault.detail = citizen.errors.toString()
                    fault.system = Constants.ERROR_SYSTEM_NAME

                    throw new FaultMessage(fault.cause, fault)
                }

                log.info "Saved citizen."


            }
            log.debug "Found citizen: " + citizen
        }

        return citizen
    }

}
