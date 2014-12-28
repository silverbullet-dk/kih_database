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
package dk.silverbullet.kihdb.services

import dk.nsi._2011._09._23.stamdatacpr.PersonLookupResponseType
import dk.oio.rep.cpr_dk.xml.schemas.core._2006._01._17.RegularCPRPersonType
import dk.oio.rep.medcom_sundcom_dk.xml.schemas._2007._02._01.PersonAddressStructureType
import dk.oio.rep.xkom_dk.xml.schemas._2006._01._06.AddressPostalType
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.ServiceStorageException
import dk.silverbullet.kihdb.message.DeleteMeasuringResults
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.Instrument
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.model.phmr.AddressPostal
import dk.silverbullet.kihdb.model.phmr.PhoneNumberSubscriber
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import dk.silverbullet.kihdb.phmr.PHMRBuilder
import dk.silverbullet.kihdb.xds.SubmitPHMRObjectsRequestHelper
import grails.util.Holders
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import org.net4care.phmr.builders.DanishPHMRBuilder
import org.net4care.phmr.builders.UUIDStrategy
import org.net4care.phmr.model.*

import javax.xml.bind.JAXBException
import javax.xml.datatype.XMLGregorianCalendar

class DocumentTransformationService {
    def mimeType = "text/xml"


    def documentService
    def documentRegistryService

    def stamdataLookupSoapService


    def grailsApplication

    private static String repositoryId

    private SubmitPHMRObjectsRequestHelper requestHelper

    private PHMRBuilder builder

    public DocumentTransformationService() {
        log.debug "In default constructor"

        if (Holders.grailsApplication.config.kihdb.xds.documentrepository.oid) {
            repositoryId = Holders.grailsApplication.config.kihdb.xds.documentrepository.oid
            log.info "Setting Document repository oid from Config. Value: " + repositoryId
        } else {
            throw new ServiceStorageException("Uninitialized OID for repository OID")
        }

        requestHelper = new SubmitPHMRObjectsRequestHelper();
        builder = new PHMRBuilder()
    }


    private List<String> extractIupacCode(SelfMonitoringSample sample) {
        List<String> uipacCodes = []
        for (report in sample.reports) {
            uipacCodes << report.iupacCode.code
        }

        return uipacCodes

    }

    private SubmitObjectsRequest prepareForRegisterDeprecatedDocument(SelfMonitoringSample sample, String documentId) throws JAXBException {

        SubmitObjectsRequest request = requestHelper.create();
        ExtrinsicObjectType metadata = requestHelper.addStableDocumentMetadataEntry(request, documentId, repositoryId);

        requestHelper.addDocumentMetadataAttributes(metadata, sample.createdByText, sample.citizen);

        List<String> uipacCodes = extractIupacCode(sample)

        requestHelper.setStatusDeprecated(metadata)

        requestHelper.addDocumentMetadataDefaultAttributes(metadata, uipacCodes);

        RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
        requestHelper.addSubmissionSetAttributes(submissionSet, sample.citizen);
        requestHelper.addSubmissionSetDefaultAttributes(submissionSet);
        requestHelper.addAssociation(request, metadata, submissionSet);

        return request;
    }


    private SubmitObjectsRequest prepareForRegisterStableDocument(SelfMonitoringSample sample, String documentId) throws JAXBException {
        SubmitObjectsRequest request = requestHelper.create();
        ExtrinsicObjectType metadata = requestHelper.addStableDocumentMetadataEntry(request, documentId, repositoryId);

        requestHelper.addDocumentMetadataAttributes(metadata, sample.createdByText, sample.citizen);

        List<String> uipacCodes = extractIupacCode(sample)

        requestHelper.addDocumentMetadataDefaultAttributes(metadata, uipacCodes);

        RegistryPackageType submissionSet = requestHelper.addSubmissionSet(request);
        requestHelper.addSubmissionSetAttributes(submissionSet, sample.citizen);
        requestHelper.addSubmissionSetDefaultAttributes(submissionSet);
        requestHelper.addAssociation(request, metadata, submissionSet);

        return request;
    }

    private PersonIdentity.PersonBuilder handleAdddress(AddressPostalType addres, PersonIdentity.PersonBuilder builder) {
        builder = handleNullBuilder(builder)

        AddressData.AddressBuilder addressBuilder = new AddressData.AddressBuilder()

        if (addres.countryIdentificationCode) {
            addressBuilder.country = addres.countryIdentificationCode.value
        }
        if (addres.streetName && addres.streetBuildingIdentifier) {
            addressBuilder.addAddressLine("${addres.streetName} ${addres.streetBuildingIdentifier}")
        }
        if (addres.postCodeIdentifier) {
            addressBuilder.postalCode = addres.postCodeIdentifier
        }

        if (addres.districtName) {
            addressBuilder.city = addres.districtName
        }

        addressBuilder.use = AddressData.Use.HomeAddress

        builder.address = addressBuilder.build()
        return builder
    }

    /**
     * Handle name structure
     * @param person name structure from StamdataLookupService
     * @param personIdentity an existing PersonIdentity object. If null it's created
     * @return The populated PersonIdentity object
     */
    private PersonIdentity.PersonBuilder handlePerson(RegularCPRPersonType regularCPRPersonType, PersonIdentity.PersonBuilder builder) {

        builder = handleNullBuilder(builder)

        def person = regularCPRPersonType.simpleCPRPerson.personNameStructure

        if (person.personGivenName) builder.addGivenName(person.personGivenName)
        if (person.personMiddleName) builder.addGivenName(person.personMiddleName)
        if (person.personSurnameName) builder.addFamilyName(person.personSurnameName)

        if (regularCPRPersonType.simpleCPRPerson.personCivilRegistrationIdentifier) {
            builder.personID = regularCPRPersonType.simpleCPRPerson.personCivilRegistrationIdentifier
        }

        XMLGregorianCalendar birthDate = regularCPRPersonType?.personBirthDateStructure?.birthDate

        if (birthDate) {
            int year = birthDate.getYear()
            int month = birthDate.getMonth()
            int day = birthDate.getDay()
            builder.setBirthTime(year, month, day)
        }

        if (regularCPRPersonType.personGenderCode) {
            String genderCode = regularCPRPersonType?.personGenderCode?.value()
            if ("K".equalsIgnoreCase(genderCode)) {
                builder.gender = PersonIdentity.Gender.Female
            }
            if ("M".equalsIgnoreCase(genderCode)) {
                builder.gender = PersonIdentity.Gender.Male
            }
        }

        return builder
    }

    private PersonIdentity buildPerson(Citizen c) {
        // 1.2 Populate the document with patient information

        PersonLookupResponseType r = stamdataLookupSoapService.invokeStamdataLookup(c.ssn)

        log.debug "Got reply from "
        PersonIdentity.PersonBuilder builder = new PersonIdentity.PersonBuilder()

        if (r?.personInformationStructure?.size() == 1) {
            // Positive reply from service
            RegularCPRPersonType regularCPRPersonType = r?.personInformationStructure?.get(0)?.regularCPRPerson
            PersonAddressStructureType addressStructureType = r?.personInformationStructure?.get(0)?.personAddressStructure
            builder = handlePerson(regularCPRPersonType, builder)
            builder = handleAdddress(addressStructureType.addressComplete.addressPostal, builder)
        } else {
            builder = handleCitizen(c, builder)
        }

        if (c.contactTelephone) {
            builder.addTelecom(AddressData.Use.HomeAddress, "tel", c.contactTelephone)
        }
        if (c.email) {
            builder.addTelecom(AddressData.Use.HomeAddress, "mailto", c.contactTelephone)
        }

        return builder.build()
    }

    private PersonIdentity.PersonBuilder handleNullBuilder(PersonIdentity.PersonBuilder builder) {
        if (!builder) {
            builder = new PersonIdentity.PersonBuilder()
        }

        return builder
    }

    private PersonIdentity.PersonBuilder handleCitizen(Citizen citizen, PersonIdentity.PersonBuilder personBuilder) {
        personBuilder = handleNullBuilder(personBuilder)

        // Handle name
        if (citizen.lastName) {
            personBuilder.addFamilyName(citizen.lastName)
        }
        if (citizen.firstName) {
            personBuilder.addGivenName(citizen.firstName)
        }

        if (citizen.middleName) {
            personBuilder.addGivenName(citizen.middleName)
        }

        if (citizen.getBirthdate()) {
            Date birthDate = citizen.getBirthdate()
            int year = birthDate.getYear()
            int month = birthDate.getMonth()
            int day = birthDate.getDay()
            personBuilder.setBirthTime(year, month, day)
        }

        personBuilder.setPersonID(citizen.ssn)

        // Handle address
        if (citizen.streetName || citizen.zipCode || citizen.city || citizen.country) {
            AddressData.AddressBuilder addressBuilder = new AddressData.AddressBuilder()
            if (citizen.streetName) {
                addressBuilder.addAddressLine(citizen.streetName)
            }
            if (citizen.zipCode) {
                addressBuilder.postalCode = citizen.zipCode
            }
            if (citizen.country) {
                addressBuilder.country = citizen.country
            }
            if (citizen.city) {
                addressBuilder.city = citizen.city
            }
            personBuilder.address = addressBuilder.build()
        }

        if (citizen.getSex()) {
            if (Sex.FEMALE.equals(citizen.getSex())) {
                personBuilder.setGender(PersonIdentity.Gender.Female)
            }
            if (Sex.MALE.equals(citizen.getSex())) {
                personBuilder.setGender(PersonIdentity.Gender.Male)
            }
        }

        return personBuilder
    }

    /**
     * The data is currently not avaible, thus create a nullFlavour one
     * @param sample
     * @return
     */
    private PersonIdentity buildPerson(SelfMonitoringSample sample) {
        return new PersonIdentity.PersonBuilder(null).build()
    }

    private SimpleClinicalDocument createClinicalDocument(SelfMonitoringSample sample) {
        // 1. Create a PHMR document as a "Green CDA", that is,
        // a data structure containing only the dynamic data
        // of a CDA.
        SimpleClinicalDocument cda = new DanishPHMRModel();
        Date documentCreationTme = (sample.createdDateTime ? sample.createdDateTime : new Date())

        // 1.1 Populate with time and version info
        cda.setDocumentVersion("2358344", 1);
        cda.setEffectiveTime(documentCreationTme);

        PersonIdentity patient = buildPerson(sample.citizen)
        cda.setPatient(patient);

        PersonIdentity doctor = buildPerson(sample)

        OrganizationIdentity custodian
        if (sample.custodian) {
            OrganizationIdentity.OrganizationBuilder custodianBuilder = new OrganizationIdentity.OrganizationBuilder()

            custodianBuilder = buildTelecom(custodianBuilder, sample?.custodian?.phoneNumberSubscriber)
            custodianBuilder.setName(sample?.custodian?.name)
            custodianBuilder.setAddress(buildAdress(sample.custodian.address, sample.reports[0]))
            custodian = custodianBuilder.build()
        } else {
            custodian = new OrganizationIdentity.OrganizationBuilder(null).build()
        }

        OrganizationIdentity author
        if (sample.author) {
            OrganizationIdentity.OrganizationBuilder authorBuilder = new OrganizationIdentity.OrganizationBuilder()

            String name = (sample?.author?.assignedPersonGivenName ? sample.author.assignedPersonGivenName : "") +
                    (sample?.author?.assignedPersonMiddleName ? " " + sample.author.assignedPersonMiddleName : "") +
                    (sample?.author?.assignedPersonSurnameName ? " " + sample.author.assignedPersonSurnameName : "")

            authorBuilder.setName(name)

            for (phone in sample?.author?.phoneNumberSubscribers) {
                authorBuilder = buildTelecom(authorBuilder, phone)
            }

            AddressData addressData = buildAdress(sample?.author?.addressPostalType, sample.reports[0])
            authorBuilder.setAddress()

            author = authorBuilder.build()
        } else {
            author = new OrganizationIdentity.OrganizationBuilder(null).build()
        }

        OrganizationIdentity authenticator
        if (sample?.legalAuthenticator) {
            OrganizationIdentity.OrganizationBuilder legalBuilder = new OrganizationIdentity.OrganizationBuilder()

            legalBuilder.setName(sample?.legalAuthenticator?.representedOrganizationName)
            legalBuilder.setAddress(buildAdress(sample?.legalAuthenticator?.address, sample?.reports[0]))

            for (phone in sample?.legalAuthenticator?.phoneNumberSubscribers) {
                legalBuilder = buildTelecom(legalBuilder, phone)
            }

            authenticator = legalBuilder.build()
        } else {
            authenticator = new OrganizationIdentity.OrganizationBuilder(null).build()
        }

        // 1.3 Populate with Author, Custodian, and Authenticator
        cda.setAuthor(author, doctor, documentCreationTme);
        cda.setCustodian(custodian);
        cda.setAuthenticator(authenticator, doctor, documentCreationTme);

        // 1.4 Define the service period
        Date from = sample.getEarliestDate()
        Date to = sample.getLatestDate()
        cda.setDocumentationTimeInterval(from, to);

        List<Instrument> instruments = []

        // extract vital signs, measuremnts and medical equipment
        for (r in sample.reports) {
            Instrument i = r.instrument

            if (!instruments.contains(i)) {
                instruments << i
            }

            Context.PerformerType performerType = null

            // Home measured
            if ("POT".equals(r.producerOfLabResult.identifierCode)) {
                performerType = Context.PerformerType.Citizen
            }
            if ("PNT".equals(r.producerOfLabResult.identifierCode)) {
                performerType = Context.PerformerType.HealthcareProfessional;
            }

            Context context = new Context(Context.ProvisionMethod.Electronically, performerType);
            Measurement m = createMeasurementFromReport(r, context)

            if (r.isVitalSigns()) {
                cda.addVitalSign(m)
            } else {
                cda.addResult(m)
            }
        }

        // Instrument(s)
        for (i in instruments) {
            if (i) {
                MedicalEquipment equipment = new MedicalEquipment(i.medComID, i.productType, "${i.manufacturer}/${i.model}", i.softwareVersion)
                cda.addMedicalEquipment(equipment)
            }
        }

        return cda;
    }

    private
    static OrganizationIdentity.OrganizationBuilder buildTelecom(OrganizationIdentity.OrganizationBuilder builder, PhoneNumberSubscriber phone) {
        if (!builder) {
            builder = new OrganizationIdentity.OrganizationBuilder()
        }

        def split = phone.phoneNumberIdentifier.split(":")

        AddressData.Use use = phone?.phoneNumberUse?.equals("W") ? AddressData.Use.WorkPlace : AddressData.Use.HomeAddress

        if (split.size() == 2) {
            builder.addTelecom(use, split[0], split[1])
        } else {
            builder.addTelecom(use, "tel", phone?.phoneNumberIdentifier?.toString())
        }

        return builder
    }

    private AddressData buildAdress(AddressPostal addressPostal, LaboratoryReport report) {
        if (addressPostal)
            return new AddressData.AddressBuilder()
                    .addAddressLine(addressPostal.streetName)
                    .addAddressLine(addressPostal.streetNameForAddressingName)
                    .addAddressLine(addressPostal.streetBuildingIdentifier)
                    .addAddressLine(addressPostal.floorIdentifier)
                    .addAddressLine(addressPostal.suiteIdentifier)
                    .addAddressLine(addressPostal.districtSubdivisionIdentifier)
                    .setPostalCode(addressPostal.postCodeIdentifier)
                    .setCity(addressPostal.districtName)
                    .setCountry(addressPostal.countryIdentificationCodeTypeValue)
                    .setUse((report.producerOfLabResult.identifierCode.equals("POT") ? AddressData.Use.HomeAddress : AddressData.Use.WorkPlace) as AddressData.Use)
                    .build()

    }

    /**
     * Create measurement from LaboratoryReport type
     * @param laboratoryReport The source for creating the measurement
     * @param context The Context of the measurement
     * @return Created measurement
     */

    Measurement createMeasurementFromReport(LaboratoryReport laboratoryReport, Context context) {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(laboratoryReport.createdDateTime, Measurement.Status.COMPLETED)
        builder.setPhysicalQuantity(laboratoryReport.resultText, laboratoryReport.resultUnitText,
                laboratoryReport.iupacCode.code, laboratoryReport.iupacCode.description)
        builder.setContext(context)
        return builder.build()
    }

    def createXdsFromSelfMonitoringSample(SelfMonitoringSample sample) {
        log.debug "Creating PHMR for SSN : " + sample.citizen?.ssn + " and sample: " + sample + " with " + sample.reports?.size() + " measurements "

        sample.xdsConversionStarted = true
        if (!sample.save()) {
            log.error("Error updating SelfMonitoringSample after XDS/CDA/HL7/PHMR creation and persistence. Errors: " + sample.errors)
        }

        String uuid
        if (sample.xdsDocumentUuid) {
            uuid = sample.xdsDocumentUuid

        } else {
            uuid = UUID.randomUUID() as String
        }

        // Instantiate the new UUID strategy
        DanishPHMRBuilder danishPHMRBuilder = new DanishPHMRBuilder(new UUIDStrategy() {
            @Override
            String generate() {
                UUID.randomUUID().toString()
            }
        })

        // Create CDA from sample
        SimpleClinicalDocument doc = createClinicalDocument(sample)

        //Use the Danish PHMR Builder.
        doc.construct(danishPHMRBuilder)

        // Setup metadata for PHMR
        SubmitObjectsRequest submitObjectsRequest = prepareForRegisterStableDocument(sample, uuid)
        XDSMetadata m = new XDSMetadata(data: requestHelper.convertSubmitObjectsRequestToString(submitObjectsRequest))

        // Unmarshall document to string and store i
        XDSDocument d = new XDSDocument()
        d.document = danishPHMRBuilder.clinicalDocumentAsString().bytes
        d.mimeType = "text/xml"
        d.uuid = uuid
        d.metadata = m

        XDSDocument xdsDocument = documentService.persistDocument(d, false)
        sample.xdsDocumentUuid = xdsDocument.uuid

        RegistryResponseType response = updateRegistry(sample, submitObjectsRequest)

        m.addToDocuments(d)

        if (!m.save()) {
            log.error "Errors while saving: " + m.errors
        }

        if (!sample.save()) {
            log.error "Errors while saving sample ${sample.id} for ${xdsDocument.uuid}: " + sample.errors
        }


        return response
    }

    def createXdsFromSelfMonitoringSample(String ssn, Long sampleId) {
        assert ssn != null
        assert sampleId != null

        log.debug "SSN: " + ssn + " Sample Id: " + sampleId
        def citizen = Citizen.findBySsn(ssn)

        if (citizen) {
            def sample = SelfMonitoringSample.findById(sampleId)
            createXdsFromSelfMonitoringSample(sample)
        }
    }

    def handleDeletion(SelfMonitoringSample sample) {
        log.debug "Handling deletion - deprecating sample: " + sample

        SubmitObjectsRequest request = prepareForRegisterDeprecatedDocument(sample, sample.xdsDocumentUuid)
        RegistryResponseType responseType = updateRegistry(sample, request)

        return responseType
    }

    def handleDeletion(DeleteMeasuringResults result) {
        log.debug "Handling deletion"
        for (sample in result.samples) {
            handleDeletion(sample)
        }
    }

    /**
     * Actually call registry and update selfmonitoring sample
     * @param sample The sample to be updated
     * @param request The registry medata data
     * @return The response object from registry
     */
    private RegistryResponseType updateRegistry(SelfMonitoringSample sample, SubmitObjectsRequest request) {
        RegistryResponseType response
        try {
            response = documentRegistryService.updateRegistry(request)
        } catch (Exception e) {
            log.error "Caught exception deprecation sample: " + sample.id + " error: " + e.message
        }

        if (response?.status?.equals(Constants.XDS_REGISTRY_SUCCESSFULLY_REGISTERED)) {
            sample.xdsRegisteredInRegistry = true
        }
        if (!sample.save()) {
            log.error("Error updating SelfMonitoringSample after XDS/CDA/HL7/PHMR creation and persistence. Errors: " + sample.errors)
        }

        return response
    }


    def registerSelfMonintoringSampleInRegistry(SelfMonitoringSample selfMonitoringSample) {
        def xdsDocumentUUID = selfMonitoringSample.xdsDocumentUuid
        XDSDocument document = XDSDocument.findByUuid(xdsDocumentUUID)

        if (document) {
            SubmitObjectsRequest metaData = requestHelper.convertStringToSubmitObjectsRequest(document.metadata.data)

            RegistryResponseType response = updateRegistry(selfMonitoringSample, metaData)
            log.info "Updated sample - registry response: " + response?.status
        }
    }
}
