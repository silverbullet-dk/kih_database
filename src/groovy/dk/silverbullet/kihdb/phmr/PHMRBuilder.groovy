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
package dk.silverbullet.kihdb.phmr

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.Instrument
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.xds.RegistryMetadata
import org.apache.log4j.Logger
import org.hl7.v3.*
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import java.text.SimpleDateFormat

class PHMRBuilder {
    private static final String MEDCOM_TEMPLATE_ID = "2.16.840.1.113883.3.4208.11.1"

    private static final String HL7_PHMR_VITAL_SIGNS_FIRST_ID = "2.16.840.1.113883.10.20.1.16"
    private static final String HL7_PHMR_VITAL_SIGNS_SECOND_ID = "2.16.840.1.113883.3.4208.11.1"

    private static final String HL7_PHMR_RESULTS_FIRST_ID = "2.16.840.1.113883.10.20.1.14"
    private static final String HL7_PHMR_RESULTS_SECOND_ID = "2.16.840.1.113883.3.4208.11.1"

    private static final String HL7_PHMR_MEDICAL_EQUIPMENT_FIRST_ID = "2.16.840.1.113883.10.20.1.7"
    private static final String HL7_PHMR_MEDICAL_EQUIPMENT_SECOND_ID = "2.16.840.1.113883.3.4208.11.1"

    def sdf = new SimpleDateFormat("yyyyMMddHHmmssZ")
    def birthTimeFormatter = new SimpleDateFormat("yyyyMMdd")

    private Logger log = Logger.getLogger(PHMRBuilder.class)
    private ObjectFactory of = new ObjectFactory()

    private static JAXBContext jaxbContext = JAXBContext.newInstance(POCDMT000040ClinicalDocument.class)

    Document doc

    public PHMRBuilder() {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(false)
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
        }
        doc = docBuilder.newDocument();
        doc.setXmlStandalone(false)
    }

    public static POCDMT000040ClinicalDocument createClinicalDocument(InputStream is) {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        JAXBElement<POCDMT000040ClinicalDocument> root = unmarshaller.unmarshal(is) as JAXBElement<POCDMT000040ClinicalDocument>
        POCDMT000040ClinicalDocument clinicalDocument = root.getValue()
        return clinicalDocument
    }

    public static POCDMT000040ClinicalDocument createClinicalDocument(String xml) {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()
        JAXBElement<POCDMT000040ClinicalDocument> root = unmarshaller.unmarshal(xml) as JAXBElement<POCDMT000040ClinicalDocument>
        POCDMT000040ClinicalDocument clinicalDocument = root.getValue()

        return clinicalDocument
    }


    public static String convertToString(POCDMT000040ClinicalDocument clinicalDocument) {

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller()
        StringWriter stringWriter = new StringWriter()

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        ObjectFactory of = new ObjectFactory()
        jaxbMarshaller.marshal(of.createClinicalDocument(clinicalDocument), stringWriter)

        return stringWriter.toString()
    }


    public RegistryMetadata buildRegistryEntry(SelfMonitoringSample sample) {
        // A bit cumbersome - to insert all codes for measured
        // clinical quantities we insert them into a String list
        // that next is converted into a string array.
        List<String> list = new ArrayList<String>();
        1
        // collect the codes used
        for (report in sample.reports) {
            list << report.iupacCode.code
        }

        String[] codesOfMeasuredValues =
                new String[list.size()];
        codesOfMeasuredValues = list.toArray(codesOfMeasuredValues);

        // Finally, fill in all meta data information
        RegistryMetadata metadata =
                new RegistryMetadata()
        metadata.setCpr(sample.citizen.ssn)

        if (sample?.createdDateTime) {
            metadata.setTimestamp(sample.createdDateTime.time)
        } else {
            metadata.setTimestamp(System.currentTimeMillis())
        }

        metadata.codeSystem = Constants.IUPAC_OID
        metadata.codesOfValuesMeasured = codesOfMeasuredValues
        metadata.organizationId = sample.createdByText
        metadata.treatmentId = null
        metadata.cpr = sample.citizen.ssn

        return metadata;
    }

    /**
     * Utility method to generate Titel element
     * @param titelString
     * @return
     */
    private STExplicit generateTitle(String titelString) {
        STExplicit title = new STExplicit()
        title.getContent().add(titelString)
        title
    }

    /**
     * Appended template id's easily
     * @param template
     * @param id
     */
    private void addTemplateIds(List<II> template, String id) {
        def ii = new II()
        ii.setRoot(id)
        template.add(ii)
    }

    /**
     * Create ID
     * @param root
     * @param extension
     * @return
     */
    private II createID(String root, String extension) {
        II id = of.createII()
        id.root = root
        id.extension = extension
        return id
    }

    /**
     * Create code based on code and codeSystem
     * @param code
     * @param codeSystem
     * @return
     */
    private CE createCode(String code, String codeSystem) {
        createCode(code, codeSystem, null)
    }

    /**
     * Create code based on code, code system and codesystem name
     * @param code
     * @param codeSystem
     * @param codeSystemName
     * @return
     */
    private CE createCode(String code, String codeSystem, String codeSystemName) {
        createCode(code, codeSystem, codeSystemName, null)
    }

    /**
     * Create code type
     * @param code the code to be sued
     * @param codeSystem codesystem
     * @param codeSystemName name
     * @param displayName display name
     * @return
     */
    private CE createCode(String code, String codeSystem, String codeSystemName, String displayName) {
        CE ce = new CE()
        if (code) ce.setCode(code)
        if (codeSystem) ce.setCodeSystem(codeSystem)
        if (codeSystemName) ce.setCodeSystemName(codeSystemName)
        if (displayName) ce.setDisplayName(displayName)

        ce
    }


    private void addTemplateIds(List<II> template, List<String> ids) {
        for (id in ids) {
            def ii = new II()
            ii.setRoot(id)
            template.add(ii)
        }
    }

    /**
     * Build CDA from Citizen, SelfMonitoringSample and a UUID
     * @param c
     * @param s
     * @param uuid
     * @return
     */
    POCDMT000040ClinicalDocument create(Citizen c, SelfMonitoringSample s, String uuid) {
        def startTime = System.currentTimeMillis()
        POCDMT000040ClinicalDocument cda = getRoot(c, s, uuid)
        cda.getRecordTarget().add(createRecordTargetFromCitizen(c))
        cda.getAuthor().add(getPOCDMT000040Author(c, s))
        cda.setCustodian(getPOCDMT000040Custodian(c, s))
        cda.getDocumentationOf().add(getPOCDMT000040DocumentationOf(s))
        cda.setComponent(createComponentsFromSelfMonitoringSample(s))
        log.debug "Generated CDA took " + (System.currentTimeMillis() - startTime) + " milliseconds"

        return cda
    }

    /**
     * Generate author element based on measurement and citizen (if POT)
     * @param c
     * @param s
     * @return
     */
    POCDMT000040Author getPOCDMT000040Author(Citizen c, SelfMonitoringSample s) {
        POCDMT000040Author author = new POCDMT000040Author()
        author.setContextControlCode(ContextControlPropagating.OP.value())
        author.getTypeCode().add(XParticipationAuthorPerformer.AUT.value())

        POCDMT000040AssignedAuthor assignedAuthor = new POCDMT000040AssignedAuthor()
        assignedAuthor.setClassCode(XInformationRecipientRole.ASSIGNED.value())
        II ii = new II()
        ii.setExtension("1")
        ii.setRoot("2.16.840.1.113883.3.1558.10.6.1")
        assignedAuthor.getId().add(ii)

        TSExplicit time = new TSExplicit()
        ADExplicit addr = new ADExplicit() // TODO
        TELExplicit telecom = new TELExplicit()
        TELExplicit email = new TELExplicit()
        PNExplicit name = new PNExplicit() // TODO

        POCDMT000040Person assignedPerson = new POCDMT000040Person()

        boolean generateAuthorFromCitizen = false


        if (s.reports.size() > 0) {
            LaboratoryReport l = s.reports.first()
            if ("POT".equals(l.producerOfLabResult.identifierCode)) {
                generateAuthorFromCitizen = true
            }
        }

        // Assign values
        if (generateAuthorFromCitizen) {
            time.value = sdf.format(s.getEarliestDate())
            addr = generateAddress(c)
            telecom = generateTelecom(c.getContactTelephone(), "tel", "WP")
            email = generateTelecom(c.email, "mailto", "WP")
            name = generateName(c)

        } else {
            // Set up nullFlavour
            time.getNullFlavor().add("NI")
            addr.getNullFlavor().add("NI")
            name.getNullFlavor().add("NI")
            telecom.getNullFlavor().add("NI")
            email.getNullFlavor().add("NI")

        }

        // Set them into reply
        assignedPerson.getName().add(name)
        assignedAuthor.getTelecom().add(telecom)
        assignedAuthor.getTelecom().add(email)
        assignedAuthor.getAddr().add(addr)
        author.setTime(time)

        assignedAuthor.setAssignedPerson(assignedPerson)
        author.setAssignedAuthor(assignedAuthor)

        return author
    }

    /**
     * Create PHMR componet based on the SelfMonitoringSample
     * @param selfMonitoringSample
     * @return Component structure ready to move add to PHMR document
     */
    POCDMT000040Component2 createComponentsFromSelfMonitoringSample(SelfMonitoringSample selfMonitoringSample) {
        POCDMT000040Component2 component2 = new POCDMT000040Component2()
        component2.setTypeCode(ActRelationshipHasComponent.COMP)
        POCDMT000040StructuredBody structuredBody = new POCDMT000040StructuredBody()

        structuredBody.getClassCode().add(ActClassOrganizer.DOCBODY.value())
        structuredBody.getMoodCode().add(XDocumentActMood.EVN.value())


        def vitalSigns = []
        def measurements = []
        def instruments = []

        selfMonitoringSample.reports.each { report ->
            Instrument instrument
            instrument = report?.instrument
            if (instrument) {
                instruments << instrument
            }

            if (report.isVitalSigns()) {
                vitalSigns << report
            } else {
                measurements << report
            }

        }


        log.debug "Adding components with " + instruments.size() + " instruments and " + vitalSigns.size() + " vital signs and " + measurements.size() + " measurements"

        structuredBody.getComponent().add(createVitalSignsFromFromLaboratory(vitalSigns))
        structuredBody.getComponent().add(createMeasurementsSignsFromFromLaboratory(measurements))
        structuredBody.getComponent().add(createInstrumentSection(instruments))

        component2.setStructuredBody(structuredBody)
        return component2;
    }

    POCDMT000040Component3 createMeasurementsSignsFromFromLaboratory(ArrayList<LaboratoryReport> measurements) {
        log.debug "Creating obversations from " + measurements.size() + " reports"
        List<String> templateIds = new ArrayList<String>(Arrays.asList(HL7_PHMR_RESULTS_FIRST_ID, HL7_PHMR_RESULTS_SECOND_ID))
        CE ce = createCode("30954-2", "2.16.840.1.113883.6.1")
        String stringTitle = "Results"

        POCDMT000040Component3 retVal = createGenericMeasurements(templateIds, ce, stringTitle, measurements)
        return retVal
    }


    POCDMT000040Component3 createVitalSignsFromFromLaboratory(ArrayList<LaboratoryReport> vitalSigns) {
        log.debug "Creating vital signs from " + vitalSigns.size() + " reports"

        List<String> templateIds = new ArrayList<String>(Arrays.asList(HL7_PHMR_VITAL_SIGNS_FIRST_ID, HL7_PHMR_VITAL_SIGNS_SECOND_ID))
        CE ce = createCode("8716-3", "2.16.840.1.113883.6.1")
        def stringTitle = "Vital Signs"


        POCDMT000040Component3 retVal = createGenericMeasurements(templateIds, ce, stringTitle, vitalSigns)

        return retVal
    }

    /**
     * Create instrument section from list of instruments
     * @param instruments The list to create instrument from
     * @return Section of instruments
     */
    private POCDMT000040Component3 createInstrumentSection(ArrayList<Instrument> instruments) {
        POCDMT000040Component3 retVal = new POCDMT000040Component3()
        retVal.setTypeCode(ActRelationshipHasComponent.COMP)

        POCDMT000040Section section = new POCDMT000040Section()

        List<String> templateIds = new ArrayList<String>(Arrays.asList(HL7_PHMR_MEDICAL_EQUIPMENT_FIRST_ID,
                HL7_PHMR_MEDICAL_EQUIPMENT_SECOND_ID))
        for (id in templateIds) {
            def ii = new II()
            ii.setRoot(id)
            section.getTemplateId().add(ii)
        }

        section.setCode(createCode("46264-8", "2.16.840.1.113883.6.1"))

        String titelString = "Medical Equipment"
        section.setTitle(generateTitle(titelString))

        def uniqueInstruments = instruments.unique(false) { a, b -> a.medComID <=> b.medComID }

        Element textElement = doc.createElement("text")

        if (uniqueInstruments.size() > 0) {
            StringBuffer sb = new StringBuffer()
            // Jaxb and HMTL tags does not play nice, so using printf instead

            sb.append("\n")
            sb.append(String.format("%-21s", "MedCom ID"))
            sb.append(String.format("%-21s", "System Manufacturer"))
            sb.append(String.format("%-21s", "System Product type"))
            sb.append(String.format("%-21s", "System Model"))
            sb.append(String.format("%-21s", "System Software version"))

            sb.append("\n")

            for (instrument in uniqueInstruments) {
                sb.append(String.format("%-21s", (instrument.medComID ? "${instrument.medComID}" : "")))
                sb.append(String.format("%-21s", (instrument.manufacturer ? "${instrument.manufacturer}" : "")))
                sb.append(String.format("%-21s", (instrument.productType ? "${instrument.productType}" : "")))
                sb.append(String.format("%-21s", (instrument.model ? "${instrument.model}" : "")))
                sb.append(String.format("%-21s", (instrument.softwareVersion ? "${instrument.softwareVersion}" : "")))
                sb.append("\n")
            }
            sb.append("\n")


            textElement.setTextContent(sb.toString())
        } else {
            textElement.setTextContent("No ${titelString}")
        }

        section.setText(textElement)

        for (i in uniqueInstruments) {
            POCDMT000040Entry entry = createEntryFromInstrument(i)
            if (entry) section.getEntry().add(entry)
        }

        retVal.setSection(section)
        return retVal
    }

    /**
     * Creates en section containing vitalsigns and results
     * @param templateIds The template ids to be used
     * @param ce The code system to be used
     * @param stringTitle The title in the Title the section
     * @param measurements The measurements to be presented
     * @return
     */
    private POCDMT000040Component3 createGenericMeasurements(ArrayList<String> templateIds, CE ce, String stringTitle, ArrayList<LaboratoryReport> measurements) {
        ArrayList<LaboratoryReport> vitalSigns
        POCDMT000040Component3 retVal = new POCDMT000040Component3()
        retVal.setTypeCode(ActRelationshipHasComponent.COMP)

        POCDMT000040Section section = new POCDMT000040Section()

        for (id in templateIds) {
            def ii = new II()
            ii.setRoot(id)
            section.getTemplateId().add(ii)
        }

        section.setCode(ce)

        STExplicit title = new STExplicit()
        title.getContent().add(stringTitle)
        section.setTitle(title)

        Element textElement = doc.createElement("text")

        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-dd HH:MM");


        if (measurements.size() > 0) {
            StringBuffer sb = new StringBuffer()

            sb.append("\n")
            sb.append(String.format("%-12s", "Measurement"))
            sb.append(String.format("%-8s", "Unit"))
            sb.append(String.format("%-16s", "Time"))
            sb.append(String.format("%-15s", "By"))

            sb.append("\n")


            for (report in measurements) {
                sb.append(String.format("%-12s", (report.resultText ? "${report.resultText}" : "")))
                sb.append(String.format("%-8s", (report.resultUnitText ? "${report.resultUnitText}" : "")))
                sb.append(String.format("%-16s", (report.createdDateTime ? ft.format(report.createdDateTime) : "")))
                sb.append(String.format("%-15s", (report.createdBy ? "${report.createdBy}" : "")))
                sb.append("\n")
            }

            textElement.setTextContent(sb.toString())
        } else {
            textElement.setTextContent("No ${stringTitle}")
        }

        section.setText(textElement)

        for (report in measurements) {
            POCDMT000040Entry entry = createEntryFromLaboratoryReport(report)
            section.getEntry().add(entry)
        }

        retVal.setSection(section)
        retVal
    }

    /**
     * Generates a specific section from a laboratory report
     * @param report The report to be used for geenration
     * @return The filled CDA object
     */
    private POCDMT000040Entry createEntryFromLaboratoryReport(LaboratoryReport report) {
        List<String> templateIds
        POCDMT000040Entry entry = new POCDMT000040Entry()
        entry.setTypeCode(XActRelationshipEntry.COMP)
        POCDMT000040Organizer organizer = new POCDMT000040Organizer()
        organizer.setClassCode(XActClassDocumentEntryOrganizer.CLUSTER)
        organizer.getMoodCode().add(XDocumentActMood.EVN.value())

        II ii2 = new II()
        ii2.setRoot("2.16.840.1.113883.10.20.9.8")
        organizer.getTemplateId().add(ii2)

        CS cs = new CS()
        cs.setCode("completed")

        organizer.setStatusCode(cs)

        POCDMT000040Component4 component4 = new POCDMT000040Component4()
        IVLTSExplicit effectiveTime = new IVLTSExplicit()
        String effectiveDate = sdf.format((report.getCreatedDateTime() ? report.getCreatedDateTime() : new Date()))


        effectiveTime.setValue(effectiveDate)

        POCDMT000040Observation observation = new POCDMT000040Observation()
        observation.effectiveTime = effectiveTime
        observation.classCode.add("OBS")
        observation.setMoodCode(XActMoodDocumentObservation.EVN)

        templateIds = new ArrayList<String>(Arrays.asList("2.16.840.1.113883.10.20.9.8"))

        for (id in templateIds) {
            def ii = new II()
            ii.setRoot(id)
            observation.getTemplateId().add(ii)
        }

        CD cd = new CD()
        cd.code = report.iupacCode.code
        cd.codeSystem = "2.16.840.1.113883.3.4208.100.1"
        cd.displayName = report.iupacCode.name

        observation.setCode(cd)

        PQ value = of.createPQ()
        value.value = report.resultText
        value.unit = report.resultUnitText

        observation.getValue().add(value)

        component4.setObservation(observation)
        organizer.getComponent().add(component4)
        entry.setOrganizer(organizer)
        entry
    }

    /**
     * Creates record target from Citizen object
     * @param c
     * @return
     */
    POCDMT000040RecordTarget createRecordTargetFromCitizen(Citizen c) {
        POCDMT000040RecordTarget recordTarget = new POCDMT000040RecordTarget()
        recordTarget.setContextControlCode("OP")
        recordTarget.getTypeCode().add("RCT")

        //
        POCDMT000040PatientRole patientRole = new POCDMT000040PatientRole()
        patientRole.getClassCode().add("PAT")

        patientRole.getId().add(createID(Constants.MEDCOM_CPR_OID, c.ssn))

        patientRole.getTelecom().add(generateTelecom(c.contactTelephone, "tel", "WP"))

        patientRole.getTelecom().add(generateTelecom(c.email, "mailto", "WP"))

        patientRole.getAddr().add(generateAddress(c))

        POCDMT000040Patient patient = new POCDMT000040Patient()
        patient.getClassCode().add("PSN")
        patient.setDeterminerCode("INSTANCE")
        patient.getName().add(generateName(c))

        patient.setAdministrativeGenderCode(createCode(c.getGenderCode(), "2.16.840.1.113883.5.1"))

        TSExplicit birth = new TSExplicit()
        birth.setValue(birthTimeFormatter.format(c.getBirthdate()))

        patient.setBirthTime(birth)

        patientRole.setPatient(patient)

        recordTarget.setPatientRole(patientRole)

        return recordTarget
    }

    /**
     * Generate telecom entry for email
     * @param email The email to be used
     * @return the created object
     */
    TELExplicit generateEmail(String email) {
        TELExplicit mail = new TELExplicit()
        mail.getUse().add("WP")
        mail.value = "mailto:${email}"
        return mail
    }

    /**
     * Create the root structure
     * @param c
     * @param s
     * @param uuid
     * @return
     */
    POCDMT000040ClinicalDocument getRoot(Citizen c, SelfMonitoringSample s, String uuid) {

        POCDMT000040ClinicalDocument clinicalDocument = of.createPOCDMT000040ClinicalDocument()
        clinicalDocument.setClassCode(ActClassClinicalDocument.DOCCLIN)

        POCDMT000040InfrastructureRootTypeId rootTypeId = new POCDMT000040InfrastructureRootTypeId()
        rootTypeId.setExtension("POCD_HD000040")
        rootTypeId.setRoot("2.16.840.1.113883.1.3")
        clinicalDocument.setTypeId(rootTypeId)

        // TODO templateId
        II ii = new II()
        ii.setRoot(MEDCOM_TEMPLATE_ID)
        clinicalDocument.getTemplateId().add(ii)

        ii = new II()
        ii.setExtension(uuid)
        ii.setRoot(Constants.MEDCOM_ROOT_OID)
        clinicalDocument.setId(ii)

        clinicalDocument.setCode(createCode("53576-5", "2.16.840.1.113883.6.1", "LOINC", "Personal Health Monitoring Report"))

        STExplicit st = new STExplicit()
        st.getContent().add("Hjemmemonitorering for " + c.ssn)
        clinicalDocument.setTitle(st)

        TSExplicit ts = new TSExplicit()


        ts.setValue(sdf.format((s.createdDateTime ? s.createdDateTime : new Date())))
        clinicalDocument.setEffectiveTime(ts)

        clinicalDocument.setConfidentialityCode(createCode("N", "2.16.840.1.113883.5.25"))

        CS cs = new CS()
        cs.setCode("da")
        clinicalDocument.setLanguageCode(cs)

        ii = new II()
        ii.setExtension("2358344")
        ii.setRoot("2.16.840.1.113883.3.4208.100.6")

        clinicalDocument.setSetId(ii)

        INT version = new INT()
        version.value = BigInteger.ONE

        clinicalDocument.setVersionNumber(version)

        return clinicalDocument
    }

    /**
     * Sets all values with nullFlavour
     *
     * @param Citizen to form the basis of guardian element to be generated
     * @param s The SelfmonitoringSample which forms the basis of the PHMR
     * @return a created and populated Custodian element
     */
    POCDMT000040Custodian getPOCDMT000040Custodian(Citizen c, SelfMonitoringSample s) {
        POCDMT000040Custodian custodian = new POCDMT000040Custodian()
        custodian.getTypeCode().add(CodeSystem.CST.value())

        POCDMT000040AssignedCustodian assignedCustodian = new POCDMT000040AssignedCustodian()
        assignedCustodian.setClassCode(XInformationRecipientRole.ASSIGNED.value())


        POCDMT000040CustodianOrganization representedCustodianOrganization = new POCDMT000040CustodianOrganization()
        representedCustodianOrganization.setClassCode(ParticipationTargetLocation.ORG.value())
        representedCustodianOrganization.setDeterminerCode(XDeterminerInstanceKind.INSTANCE.value())

        II ii = new II()
        ii.getNullFlavor().add("NI")
        representedCustodianOrganization.getId().add(ii)

        ONExplicit name = new ONExplicit() // TODO
        name.getNullFlavor().add("NI")
        representedCustodianOrganization.setName(name)

        TELExplicit telecom = new TELExplicit()
        telecom.getNullFlavor().add("NI")
        representedCustodianOrganization.setTelecom(telecom)

        ADExplicit addr = new ADExplicit() // TODO
        addr.getNullFlavor().add("NI")
        representedCustodianOrganization.setAddr(addr)

        assignedCustodian.setRepresentedCustodianOrganization(representedCustodianOrganization)

        custodian.setAssignedCustodian(assignedCustodian)

        return custodian
    }

    /**
     * Generate address based on citizen
     * @param c
     * @return
     */
    private ADExplicit generateAddress(Citizen c) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ADExplicit address = (ADExplicit) (factory.createADExplicit());
        List<Serializable> addrlist = address.getContent();

        if (c.getStreetName() && c.getCity() && c.getZipCode()) {

            // Street
            AdxpExplicitStreetAddressLine streetHl7 = new AdxpExplicitStreetAddressLine();
            streetHl7.setContent(c.getStreetName() + (c.getStreetName2() ? " " + c.getStreetName2() : ""));
            addrlist.add(factory.createADExplicitStreetAddressLine(streetHl7));

            // City
            AdxpExplicitCity cityHl7 = new AdxpExplicitCity();
            cityHl7.setContent(c.getCity());
            addrlist.add(factory.createADExplicitCity(cityHl7));

            // Zip code
            AdxpExplicitPostalCode zipHl7 = new AdxpExplicitPostalCode();
            zipHl7.setContent(c.getZipCode());
            addrlist.add(factory.createADExplicitPostalCode(zipHl7));


            if (c.getCountry()) {
                AdxpExplicitCountry countryHl7 = new AdxpExplicitCountry()
                countryHl7.setContent(c.getCountry())
                addrlist.add(factory.createADExplicitCountry(countryHl7))

            }

        } else {
            address.getNullFlavor().add("NI")
        }

        return address
    }

    /**
     * Generic method for create telecom
     * @param address
     * @param protocol
     * @param use
     * @return
     */
    private TELExplicit generateTelecom(String address, String protocol, String use) {
        TELExplicit phone = new TELExplicit()

        if (address) {
            phone.value = "${protocol}:${address}"
            phone.getUse().add(use)

        } else {
            phone.getNullFlavor().add("NI")
        }
        return phone
    }

    /**
     * Create PNExplicit structure (name) for citizen
     * @param c
     * @return
     */
    private PNExplicit generateName(Citizen c) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());

        if (c.firstName || c.lastName) {

            if (c.lastName) {
                EnExplicitFamily familyName = new EnExplicitFamily();
                familyName.setPartType("FAM");
                familyName.setContent(c.lastName);
                name.getContent().add(factory.createPNExplicitFamily(familyName));
            }

            if (c.firstName) {
                EnExplicitGiven givenName = new EnExplicitGiven();
                givenName.setPartType("GIV");
                givenName.setContent(c.firstName);
                name.getContent().add(factory.createPNExplicitGiven(givenName));
            }
            if (c.middleName) {
                EnExplicitGiven givenName = new EnExplicitGiven();
                givenName.setPartType("GIV");
                givenName.setContent(c.middleName);
                name.getContent().add(factory.createPNExplicitGiven(givenName));
            }

        } else {
            name.getNullFlavor().add("NI")
        }

        return name;
    }

    /**
     * Generate documentOf segment
     * @param s
     * @return
     */
    POCDMT000040DocumentationOf getPOCDMT000040DocumentationOf(SelfMonitoringSample s) {
        POCDMT000040DocumentationOf documentationOf = new POCDMT000040DocumentationOf()
        POCDMT000040ServiceEvent serviceEvent = new POCDMT000040ServiceEvent()
        serviceEvent.getClassCode().add("MPROT")

        IVLTSExplicit effectiveTime = new IVLTSExplicit()

        IVXBTSExplicit low = of.createIVXBTSExplicit()
        low.setValue(sdf.format((s.getEarliestDate() ? s.getEarliestDate() : new Date())))

        IVXBTSExplicit high = of.createIVXBTSExplicit()
        high.setValue(sdf.format((s.getLatestDate() ? s.getLatestDate() : new Date())))

        effectiveTime.getContent().add(of.createIVLTSExplicitLow(low))
        effectiveTime.getContent().add(of.createIVLTSExplicitHigh(high))

        serviceEvent.setEffectiveTime(effectiveTime)
        documentationOf.setServiceEvent(serviceEvent)
        return documentationOf
    }

    /**
     * Setup instrument (Medical Device) section
     * @param i
     * @return
     */
    public POCDMT000040Component3 createComponentFromInstrument(Instrument i) {
        POCDMT000040Component3 retVal = new POCDMT000040Component3()
        retVal.setTypeCode(ActRelationshipHasComponent.COMP)

        POCDMT000040Section section = new POCDMT000040Section()

        List<String> templateIds = new ArrayList<String>(Arrays.asList("2.16.840.1.113883.10.20.1.7",
                "2.16.840.1.113883.10.20.9.1"))
        for (id in templateIds) {
            def ii = new II()
            ii.setRoot(id)
            section.getTemplateId().add(ii)
        }

        section.setCode(createCode("46264-8", "2.16.840.1.113883.6.1"))

        String titelString = "Medical Equipment"

        section.setTitle(generateTitle(titelString))

        POCDMT000040Entry entry = createEntryFromInstrument(i)
        if (entry) section.getEntry().add(entry)

        retVal.setSection(section)

        return retVal
    }

    /**
     * Creates PHMR device entry section from Instrument object
     * @param i The Instrument to use
     * @return
     */
    private POCDMT000040Entry createEntryFromInstrument(Instrument i) {
        POCDMT000040Entry entry

        if (i) {
            String instrumentDevice = "MedCom ID: " + i.medComID + " / Manufacturer: " + i.manufacturer + " / ProductType " + i.productType + " / Model: " + i.model
            String instrumentVersion = "SW: " + i.softwareVersion

            POCDMT000040Participant2 participant = createParticipant(instrumentDevice, instrumentVersion)
            POCDMT000040Organizer organizer = createOrganizer(participant)

            entry = createEntry(organizer)
        }

        entry
    }

    /**
     * Create organizer
     * @return
     */
    private POCDMT000040Organizer createOrganizer() {
        List<String> templateIds = new ArrayList<String>(Arrays.asList("2.16.840.1.113883.10.20.9.4",))
        POCDMT000040Organizer organizer = new POCDMT000040Organizer()
        organizer.setClassCode(XActClassDocumentEntryOrganizer.CLUSTER)
        organizer.getMoodCode().add(XDocumentActMood.EVN.value())

        addTemplateIds(organizer.getTemplateId(), templateIds)

        CS cs = new CS()
        cs.setCode("completed")
        organizer.setStatusCode(cs)
        organizer
    }

    private POCDMT000040Organizer createOrganizer(POCDMT000040Participant2 participant) {
        POCDMT000040Organizer organizer = createOrganizer()
        organizer.getParticipant().add(participant)
        organizer
    }

    private POCDMT000040Entry createEntry(POCDMT000040Organizer organizer) {
        POCDMT000040Entry entry = new POCDMT000040Entry()
        entry.setTypeCode(XActRelationshipEntry.COMP)
        entry.setOrganizer(organizer)

        entry
    }


    private POCDMT000040Participant2 createParticipant(String instrumentDevice, String instrumentVersion) {
        POCDMT000040Participant2 participant = new POCDMT000040Participant2()
        participant.getTypeCode().add(ParticipationTargetSubject.SBJ.value())

        addTemplateIds(participant.getTemplateId(), "2.16.840.1.113883.10.20.9.9")


        POCDMT000040ParticipantRole participantRole = new POCDMT000040ParticipantRole()
        participantRole.getClassCode().add(RoleClassManufacturedProduct.MANU.value())

        POCDMT000040Device playingDevice = new POCDMT000040Device()
        SCExplicit value = new SCExplicit()

        value.getContent().add(instrumentDevice)
        playingDevice.setManufacturerModelName(value)

        SCExplicit soft = new SCExplicit()
        soft.getContent().add(instrumentVersion)
        playingDevice.setSoftwareName(soft)

        participantRole.setPlayingDevice(playingDevice)
        participant.setParticipantRole(participantRole)
        participant
    }

}
