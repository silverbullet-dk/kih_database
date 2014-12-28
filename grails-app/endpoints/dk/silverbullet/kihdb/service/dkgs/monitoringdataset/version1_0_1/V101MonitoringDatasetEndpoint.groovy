package dk.silverbullet.kihdb.service.dkgs.monitoringdataset.version1_0_1

import dk.nsi._2011._09._23.stamdatacpr.PersonLookupResponseType
import dk.oio.rep.medcom_sundcom_dk.xml.schemas._2007._02._01.PersonInformationStructureType
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.ServiceDeletionException
import dk.silverbullet.kihdb.exception.ServiceStorageException
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.service.MonitoringServiceUtil
import dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1.*
import dk.silverbullet.kihdb.util.XmlConverterUtil
import org.apache.cxf.jaxws.context.WrappedMessageContext
import org.grails.cxf.utils.EndpointType
import org.grails.cxf.utils.GrailsCxfEndpoint
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import javax.jws.WebParam
import javax.jws.WebService
import javax.xml.ws.WebServiceContext

@WebService(targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.1", name = "MonitoringDatasetPortType")
@GrailsCxfEndpoint(address='/monitoringDataset', inInterceptors = ["sosiInInterceptor"], outInterceptors = ["sosiOutInterceptor"])
class V101MonitoringDatasetEndpoint implements V101MonitoringDatasetPortType {

    def stamdataLookupSoapService

    static expose = EndpointType.JAX_WS
//    static wsdl = 'dk/silverbullet/kihdb/service/dgks/generated/monitoringdataset/version1_0_1/1.0.1/MonitoringDatasetService.wsdl'

    static transactional = true

    @Resource
    WebServiceContext wsContext;

    private MonitoringServiceUtil util = new MonitoringServiceUtil()

    private static def xmlUtil = new XmlConverterUtil()

    def sessionFactory
    private static objectFactory = new ObjectFactory()

    @Transactional
    @Override
    public DeleteMonitoringDatasetResponseMessage deleteMonitoringDataset(
            DeleteMonitoringDatasetRequestMessage parameter
    ) throws FaultMessage {
        def mContext = (WrappedMessageContext)wsContext.messageContext
        def cpr = parameter.personCivilRegistrationIdentifier

        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION,"DeleteMonitoringDataset")
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_SERVICE,V101MonitoringDatasetEndpoint.name)
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR,cpr)

        def uuids = parameter?.uuidIdentifier
        def citizen = Citizen.findBySsn(cpr)

        try {
            if (citizen && uuids) {
                for (uuid in uuids) {
                    log.info "Deleting laboratory report : " + uuid + " from citizen: " + citizen
                    def l = LaboratoryReport.findByLaboratoryReportUUIDAndDeleted(uuid,false)
                    def s = l.sample

                    log.debug "Reports before delete: " + s.reports.size()
                    if (l.sample.citizen == citizen) {
                        s.removeFromReports(l)
                        l.deleted = true
                        l.save(flush: true)
                    }
                }

            } else {
                log.debug "One is null - Citizen: " + citizen + " UUID: " + uuid
            }
        } catch (Exception e) {
            throw new ServiceDeletionException("Could not delete sample",e)
        }

        SelfMonitoringSample.withNewSession {
            def samples = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false)

            samples.each { sample ->
                def reports = sample.reports.grep({it.deleted == false})


                if (reports.size() == 0) {
                    log.debug "Sample has no laboratory reports. Deleting."
                    // Remove empty samples
                    sample.deleted = true
                    sample.xdsRegisteredInRegistry = false
                    sample.save(flush: true)
                }
            }

        }




        DeleteMonitoringDatasetResponseMessage r = new DeleteMonitoringDatasetResponseMessage()
        return r
    }

    @Transactional
    @Override
    public CreateMonitoringDatasetResponseMessage createMonitoringDataset(
            CreateMonitoringDatasetRequestMessage parameter
    ) throws FaultMessage {
        log.debug "Starting createMonitoring. " + parameter.personCivilRegistrationIdentifier
        def cpr = parameter.personCivilRegistrationIdentifier

        def mContext = (WrappedMessageContext)wsContext.messageContext
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR,cpr)
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION,"CreateMonitoring")
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_SERVICE,V101MonitoringDatasetEndpoint.name)

        CreateMonitoringDatasetResponseMessage responseMessage = new CreateMonitoringDatasetResponseMessage()

        if (cpr) {

            // Extract the citizen
            Citizen citizen = util.handleCitizen(cpr)

            log.debug "Found citizen: " + citizen

            // Handle the message and store it.
            List<LaboratoryReport> reports = util.handleMonitoringData(citizen, parameter, responseMessage)

            log.info "Storing " + reports.size() + " reports"
            log.debug "IDs. " + responseMessage.uuidIdentifier.size()

            // Store the citizen so that the associations are stored.
            if (!citizen.save(flush: true)) {
                def error = "Error storing result"
                log.error error + " " + citizen.errors
                throw new ServiceStorageException(error,citizen.errors)
            }

        }

        return responseMessage

    }

    @Transactional
    @Override
    public GetMonitoringDatasetResponseMessage getMonitoringDataset(
            @WebParam(partName = "parameter", name = "GetMonitoringDatasetRequestMessage", targetNamespace = "urn:oio:medcom:monitoringdataset:1.0.1")
            GetMonitoringDatasetRequestMessage parameter
    ) throws FaultMessage {

        def ssn = parameter?.personCivilRegistrationIdentifier
        def mContext = (WrappedMessageContext)wsContext.messageContext
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_CPR,ssn)
        mContext.getWrappedMessage().getExchange().put(Constants.AUDIT_OPERATION,"GetChronicDataset")

        def dateFrom
        def dateTo
        def queryOptions = [sort: "createdDateTime",order:"desc"]

        def response = objectFactory.createGetMonitoringDatasetResponseMessage()


        if (ssn) {
            def citizen = Citizen.findBySsn(ssn)

            if (parameter?.fromDate?.toGregorianCalendar()?.time) {
                dateFrom = parameter?.fromDate?.toGregorianCalendar()?.time
            }
            if (parameter?.toDate?.toGregorianCalendar()?.time) {
                dateTo = parameter?.toDate?.toGregorianCalendar()?.time
            }


            def numberOfResults = parameter.maximumReturnedMonitorering?.intValue()

            if (numberOfResults) {
                queryOptions << [max:numberOfResults.intValue()]
            }

            response.citizenMonitoringDataset = objectFactory.createCitizenMonitoringDatasetType()


            response.citizenMonitoringDataset.citizen = createCitizenType(citizen)

            response.citizenMonitoringDataset.selfMonitoredSampleCollection = objectFactory.createSelfMonitoredSampleCollectionType()
            response.citizenMonitoringDataset.selfMonitoredSampleCollection.selfMonitoredSample = findSamples(citizen,queryOptions,dateFrom,dateTo)

        }

        return response
    }

    /**
     * Map types together
     * @param addres
     * @return
     */
    private AddressPostalType handleAddress(dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType addres) {
        AddressPostalType retVal = objectFactory.createAddressPostalType()

        retVal.countryIdentificationCode = objectFactory.createCountryIdentificationCodeType()
        if ( addres.countryIdentificationCode) retVal.countryIdentificationCode.value = addres.countryIdentificationCode.value
        if (addres.districtName) retVal.districtName = addres.districtName
        if (addres.districtSubdivisionIdentifier) retVal.districtSubdivisionIdentifier = addres.districtSubdivisionIdentifier
        if (addres.floorIdentifier) retVal.floorIdentifier = addres.floorIdentifier
        if (addres.mailDeliverySublocationIdentifier)  retVal.mailDeliverySublocationIdentifier = addres.mailDeliverySublocationIdentifier
        if (addres.postCodeIdentifier) retVal.postCodeIdentifier = addres.postCodeIdentifier
        if (addres.postOfficeBoxIdentifier) retVal.postOfficeBoxIdentifier = addres.postOfficeBoxIdentifier
        if (addres.streetBuildingIdentifier) retVal.streetBuildingIdentifier = addres.streetBuildingIdentifier
        if (addres.streetName) retVal.streetName = addres.streetName
        if (addres.streetNameForAddressingName) retVal.streetNameForAddressingName = addres.streetNameForAddressingName
        if (addres.suiteIdentifier) retVal.suiteIdentifier = addres.streetBuildingIdentifier

        return retVal
    }

    private  PersonNameStructureType handlePerson(dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType person) {
        PersonNameStructureType retVal = objectFactory.createPersonNameStructureType()
        if (person.personGivenName) retVal.personGivenName = person.personGivenName
        if (person.personMiddleName) retVal.personMiddleName = person.personMiddleName
        if (person.personSurnameName) retVal.personSurnameName = person.personSurnameName

        return retVal
    }




    /**
     * Converts to JAXB based XML type
     * @param citizen object to be transformed
     * @return The create object
     */
    CitizenType createCitizenType(Citizen citizen) {

        PersonLookupResponseType r = stamdataLookupSoapService.invokeStamdataLookup(citizen.ssn)
        log.debug "Result from Stamdata service: " + r

        CitizenType retVal = objectFactory.createCitizenType()
        if (r && r.personInformationStructure) {
            PersonInformationStructureType pi = r.personInformationStructure.get(0)
            retVal.personCivilRegistrationIdentifier = pi.currentPersonCivilRegistrationIdentifier
            retVal.addressPostal = handleAddress(pi.personAddressStructure.addressComplete.addressPostal)
            retVal.personNameStructure = handlePerson(pi.regularCPRPerson.simpleCPRPerson.personNameStructure)
        } else {
            retVal.personCivilRegistrationIdentifier = citizen.ssn

            if (citizen.firstName && citizen.lastName) {
                retVal.personNameStructure = objectFactory.createPersonNameStructureType()
                retVal.personNameStructure.personGivenName = citizen.firstName
                retVal.personNameStructure.personSurnameName = citizen.lastName
                retVal.personNameStructure.personMiddleName = citizen.middleName
            }

            if (citizen.streetName && citizen.zipCode) {
                retVal.addressPostal = objectFactory.createAddressPostalType()
                retVal.addressPostal.countryIdentificationCode = objectFactory.createCountryIdentificationCodeType().setValue(citizen.country)
                retVal.addressPostal.postCodeIdentifier = citizen.zipCode
                retVal.addressPostal.streetName = citizen.streetName
            }
        }
        if (citizen.contactTelephone) retVal.phoneNumberIdentifier = citizen.contactTelephone
        if (citizen.email) retVal.emailAddressIdentifier = citizen.email

        return retVal
    }

    /**
     * Finds and creates the samples for a citizen
     * @param citizen
     * @param queryOptions
     * @param dataSince
     * @return
     */
    List<SelfMonitoredSampleType> findSamples(Citizen citizen, def queryOptions, def dateFrom, def dateTo) {
        log.info "Find samples for citizen id: " + citizen?.id + " " + queryOptions + " From: " + dateFrom + " to: " + dateTo
        List<SelfMonitoredSampleType> retVal = new ArrayList<SelfMonitoredSampleType>()

        def res = []

        if (dateFrom || dateTo) {

            def samples = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false)
            def reports = []

            if (dateFrom && dateTo) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeBetween(false,samples,dateFrom,dateTo)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeBetween(citizen,dateFrom,dateTo,queryOptions)
            }

            if (dateFrom && !dateTo) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeGreaterThan(false,samples,dateFrom)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeGreaterThan(citizen,dateFrom,queryOptions)
            }

            if (dateTo && !dateFrom) {
                reports = LaboratoryReport.findAllByDeletedAndSampleInListAndCreatedDateTimeLessThan(false,samples,dateTo)
//            res = SelfMonitoringSample.findAllByCitizenAndCreatedDateTimeLessThan(citizen,dateTo,queryOptions)
            }


            for (report in reports) {
                if (!res.contains(report?.sample)) {
                    res << report.sample
                }
            }

            if (queryOptions?.max && res?.size() > queryOptions.max) {
                res = res.subList(0,queryOptions.max)
            }


        }

        if (!dateFrom && !dateTo) {
            res = SelfMonitoringSample.findAllByCitizenAndDeleted(citizen,false,queryOptions)
        }


        res.each { sample ->
            SelfMonitoredSampleType s = objectFactory.createSelfMonitoredSampleType()
            s.createdByText = sample.createdByText

//            s.createdDateTime = XmlConverterUtil.getDateAsXml(sample.createdDate)
//            s.sampleCategoryIdentifier = sample.sampleCategoryIdentifier
//            s.uuidIdentifier = sample.selfMonitoringSampleUUID
//
//            s.laboratoryReportCollection = objectFactory.createLaboratoryReportCollectionType()
//            s.laboratoryReportCollection.laboratoryReport = handleLaboratoryReport(sample)
            s.laboratoryReportExtendedCollection = objectFactory.createLaboratoryReportExtendedCollectionType()
            s.laboratoryReportExtendedCollection.laboratoryReportExtended =  handleLaboratoryReportExtended(sample)

            retVal.add(s)
        }

        log.debug "Returning " + retVal.size() + " SelfMonitoringSample"

        return retVal
    }

    List<LaboratoryReportExtendedType> handleLaboratoryReportExtended(SelfMonitoringSample selfMonitoringSample) {
        List<LaboratoryReportExtendedType> retVal = new ArrayList<LaboratoryReportExtendedType>()

        def reports = LaboratoryReport.findAllBySampleAndDeleted(selfMonitoringSample,false)

        reports.each() { report ->

            LaboratoryReport lr = (LaboratoryReport) report
            def r = objectFactory.createLaboratoryReportExtendedType()

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
                r.resultOperatorIdentifier  = OperatorIdentifierType.fromValue(lr.resultOperatorIdentifier?.value())
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
        def retVal = objectFactory.createInstrumentType()

        retVal.manufacturer = r.instrument?.manufacturer
        retVal.medComID = r.instrument?.medComID
        retVal.model = r.instrument?.model
        retVal.productType = r.instrument?.productType
        retVal.softwareVersion = r.instrument?.softwareVersion

        return retVal
    }


    ProducerOfLabResultType handleLapProducer(LaboratoryReport laporatoryReport) {
        def retVal = objectFactory.createProducerOfLabResultType()

        retVal.identifier = laporatoryReport.producerOfLabResult.identifier
        retVal.identifierCode = laporatoryReport.producerOfLabResult.identifierCode

        return retVal
    }
}
