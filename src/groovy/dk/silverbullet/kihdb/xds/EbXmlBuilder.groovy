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
package dk.silverbullet.kihdb.xds

import dk.silverbullet.kihdb.constants.Constants
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.stream.StreamSource
import java.text.Format
import java.text.SimpleDateFormat

/**
 * This class is largely based on the Net4Care.org org.net4care.xdsproxy.EbXmlBuilder
 */
class EbXmlBuilder {

    private static final String REGISTRY_PACKAGE_NAME = "Test submissionset"
    private static final String KIHDB_FORMAT_DISPLAY_NAME = "Net4Care PHMR format"
    private static final String KIH_PHMR_URN = "urn:ad:net4care:phmr"
    private static final String KIH_DB_TELEOBS_URN = "urn:ad:net4care:teleobservation"
    private static final String KIH_CONFIDENTIALITYCODE_URN = "urn:ad:net4care:confidentialitycode"
    private static final String KIH_CONFIDENTIALITY_NAME = "Net4Care Confidentiality Code"
    private static final String KIH_HEALTHCARE_FACILITY_CODE = "urn:ad:net4care:healthcarefacilitytypecode"
    private static final String KIH_HEALTHCARE_FACILITY_NAME = "Net4Care Healthcare Facility Type Code"
    private static final String KIH_PRACTISESETTING_CODE = "urn:ad:net4care:practicesettingcode"
    private static final String KIH_PRACTISESETTING_NAME = "Net4Care Practice Setting Code"
    private static final String KIH_AUTHOR = "KIH^^Automatic"
    private static final String KIH_CODING_SCHEME_VALUE = "Net4CareCodingSchemeValue"
    private static final String KIH_CODING_SCHEME_NAME = "Net4CareCodingScheme"

    private ObjectFactory factory


    private ObjectFactory createObjectFactory() {
        if (!factory) {
            factory = new ObjectFactory()
        }

        return this.factory
    }


    private String formatCprOid(String cpr) {
        return cpr?.replace("-", "") + "^^^&" + Constants.DK_CPR_OID + "&ISO";
    }

    private static JAXBContext jaxbContext = JAXBContext.newInstance(SubmitObjectsRequest.class)


    private DocumentBuilder createDocumentBuilder() {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
        }

        return docBuilder
    }

    // Create a formatter for the format required by eBXML
    private static Format formatter = new SimpleDateFormat("yyyyMMddHHmmss"); ;

    private static Logger log = LoggerFactory.getLogger(EbXmlBuilder.class)


    public String convertSubmitObjectsRequestToString(SubmitObjectsRequest input) {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller()
        StringWriter stringWriter = new StringWriter()

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
//        jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

        jaxbMarshaller.marshal(input, stringWriter)

        return stringWriter.toString()
    }

    public SubmitObjectsRequest convertStringToSubmitObjectsRequest(String submitObjectsRequestString) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SubmitObjectsRequest.class);
        Object request = context.createUnmarshaller().unmarshal(new StreamSource(new StringReader(submitObjectsRequestString)));
        return (SubmitObjectsRequest) request;
    }

    /** Build SOAP body XML that represents a ProvideAndRegisterDocumentSetRequest
     * as defined by the XDS.b standard as outlined in IHE-ITI-TF-Vol3.
     * The xml will represent the ebXML header
     * for a Net4Care observation as defined by the metadata given in the parameter.
     * Also you MUST provide the uniqueID as defined by XDS.b, that is the
     * unique ID of the provided document (generate it using Java UUID class).
     * Similarly provide a UUID for the associated document that will go into
     * the final message (It binds the metadata to the set of documents attached).
     * @param metadata The metadata associated with the submission
     * @param uniqueID unique ID of the submission itself
     * @param associatedTargetObjectID unique ID of the document
     * @return XML document representing the SubmitObjectRequest header
     * @throws javax.xml.bind.JAXBException
     */
    public SubmitObjectsRequest buildSubmitObjectsRequest(RegistryMetadata metadata) {

        log.debug "Building ebXML using unique ID for document: ${metadata.uniqueID} and associated ID: ${metadata.associatedID}"

        ObjectFactory factory =
                new ObjectFactory();

        // Algorithm:
        // 1) Build the SubjectObjectRequest based upon format outlined in TF-Vol3
        // using the generated classes from the SubmitObjectRequest schema

        String timeStampForCreation = formatter.format(System.currentTimeMillis());

        String submissionSetId = "KIHDBSubmissionSet-" + System.currentTimeMillis();
        log.debug "Creating submission set ID: ${metadata.submissionSetID}"


        SubmitObjectsRequest sor = new SubmitObjectsRequest();

        RegistryObjectListType rol = factory.createRegistryObjectListType();
        sor.setRegistryObjectList(rol);

        ClassificationType ct = factory.createClassificationType();
        ct.setClassificationNode(Constants.XDSSubmissionSet_classificationNode);
        ct.setClassifiedObject(metadata.getAssociatedID());
        ct.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification")
        ct.setId("urn:uuid:${UUID.randomUUID().toString()}"); // Should this be something else?

        // Setting up hasMember Assocciastion version 4 - p 24 IHE_ITI_TF_VOl3.pdf
        AssociationType1 at = factory.createAssociationType1();
        at.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");
        at.setId("association1"); // TODO: is this right? From NPI examples ?
        at.setSourceObject(metadata.submissionSet);

        at.setTargetObject(metadata.getAssociatedID());
        addSlotTo(at, "SubmissionSetStatus", "Original");

        ExtrinsicObjectType eot = createExtrinsicObject(metadata, timeStampForCreation)

        RegistryPackageType rpt = createRegistryPackage(metadata, timeStampForCreation)

        // trick-of-the-trade to assign eot to the registryobject   list...
        // we cannot do that directly as the types mismatch
        JAXBElement<? extends IdentifiableType> eo = factory.createExtrinsicObject(eot);
        rol.getIdentifiable().add(eo);
        // ditto for the registry package type...
        JAXBElement<? extends IdentifiableType> rp = factory.createRegistryPackage(rpt);
        rol.getIdentifiable().add(rp);
        // ...and classification type...
        JAXBElement<? extends IdentifiableType> classification = factory.createClassification(ct);
        rol.getIdentifiable().add(classification);
        // ...and association type...
        JAXBElement<? extends IdentifiableType> assoc = factory.createAssociation(at);
        rol.getIdentifiable().add(assoc);


        return sor
    }

    private RegistryPackageType createRegistryPackage(RegistryMetadata metadata, String submissionTimeText) {
        log.debug "Creating RegistryPackaged for ${metadata.submissionSetID} and associatedID: ${metadata.associatedID}"

        // **********************
        // RegistryPackage

        RegistryPackageType rpt = createObjectFactory().createRegistryPackageType();
        rpt.setId(metadata.getAssociatedID());


        addSlotToRegistryPackage(rpt, "submissionTime", submissionTimeText);
        rpt.objectType = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage"
        setName(rpt, REGISTRY_PACKAGE_NAME);

//        addClassificationTo(rpt,
//                Constants.XDSDocumentEntry_authorId,
//                metadata.getAssociatedID(),
//                null,
//                "authorId",
//                ["authorPerson": [KIH_AUTHOR], "authorInstitution": ["Unknown^^^^^&amp;2.16.208&amp;ISO^^^^Yder=278467"], "authorRole": ["telemetric measurement collector"], "authorSpeciality": ["collection"]],
//                null,
//                "authorPerson");


        addClassificationTo(rpt,
                Constants.XDSDocumentEntry_contentTypeCode,
                metadata.getAssociatedID(),
                null,
                "codingSchemeId",
                KIH_CODING_SCHEME_VALUE,
                KIH_CODING_SCHEME_NAME,
                "codingScheme");

        addExternalIdentifierTo(rpt,
                metadata.getAssociatedID(),
                "thisUniqueId",
                Constants.XDSSubmissionSet_uniqueId,
                UUID.randomUUID().toString(),
                "XDSSubmissionSet.uniqueId",
                "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier"
        );

        addExternalIdentifierTo(rpt,
                metadata.getAssociatedID(),
                "thisSourceId",
                Constants.XDSSubmissionSet_sourceId,
                UUID.randomUUID().toString(),
                "XDSSubmissionSet.sourceId",
                "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");

        addExternalIdentifierTo(rpt,
                metadata.getAssociatedID(),
                "thisPatientId",
                Constants.XDSSubmissionSet_patientId,
                formatCprOid(metadata.getCpr()),
                "XDSSubmissionSet.patientId",
                "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");

        return rpt
    }

    private ExtrinsicObjectType createExtrinsicObject(RegistryMetadata metadata, String submissionTimeText) {
        ExtrinsicObjectType eot = createObjectFactory().createExtrinsicObjectType();
        setName(eot, metadata.getTitle());
        setDescription(eot, "This is a " + metadata.getTitle() + " document");
        eot.setMimeType("text/xml");
        eot.setId(metadata.documentID);
        eot.setObjectType(Constants.XDSDocumentEntry);

        // Add the general information slots
        String timeStampOfObservation = formatter.format(metadata.getTimestamp());
        addSlotTo(eot, "repositoryUniqueId", Constants.KIHDB_DOCUMENTREPOSITORY_ID)
        addSlotTo(eot, "creationTime", submissionTimeText);
        addSlotTo(eot, "serviceStartTime", timeStampOfObservation);
        addSlotTo(eot, "serviceStopTime", timeStampOfObservation);
        addSlotTo(eot, "sourcePatientId", formatCprOid(metadata.getCpr()));
        addSlotTo(eot, "sourcePatientInfo", "Patient with ID: " + metadata.getCpr());
        addSlotTo(eot, "languageCode", "en-us");


        addClassificationTo(eot,
                Constants.XDSDocumentEntry_authorId,
                metadata.getAssociatedID(),
                null,
                "authorId",
                ["authorPerson": [KIH_AUTHOR], "authorInstitution": ["TestAuthorInstitution^^^^^&2.16.208&ISO^^^^Yder=278467"], "authorRole": ["telemetric measurement collector"], "authorSpeciality": ["collection"]],
                null,
                "authorPerson");

        // TODO: Fix mapping from N4C to something else
        // formatCode
        addClassificationTo(eot,
                Constants.XDSDocumentEntry_formatCode,
                metadata.getAssociatedID(),
                "CDAR2/IHE 1.0",
                UUID.randomUUID().toString(), // TODO: is this right?
                KIH_PHMR_URN,     // Issue: is there a URN for PHMR
                KIHDB_FORMAT_DISPLAY_NAME,
                "codingScheme");

        // classCode
        addClassificationTo(eot,
                Constants.XDSDocumentEntry_classCode,
                metadata.getAssociatedID(),
                metadata.getClassification(),
                UUID.randomUUID().toString(), // TODO: Should this be something controlled?
                KIH_DB_TELEOBS_URN,
                metadata.getClassification(),
                "codingScheme");

        // typeCode
        // TODO map properly
        addClassificationTo(eot,
                Constants.XDSDocumentEntry_typeCode,
                metadata.getAssociatedID(),
                metadata.getClassification(),
                UUID.randomUUID().toString(),
                KIH_DB_TELEOBS_URN,
                metadata.getClassification(),
                "codingScheme");

        // TODO map properly
        addClassificationTo(eot,
                Constants.XDSDocumentEntry_confidentialityCode,
                metadata.getAssociatedID(),
                "confidentialityCode",
                UUID.randomUUID().toString(),
                KIH_CONFIDENTIALITYCODE_URN, // Issue: is there a URN for confidentiality code?
                KIH_CONFIDENTIALITY_NAME,
                "codingScheme");

        addClassificationTo(eot,
                Constants.XDSDocumentEntry_healthcareFacilityTypeCode,
                metadata.getAssociatedID(),
                "healthcareFacilityTypeCode",
                UUID.randomUUID().toString(),
                KIH_HEALTHCARE_FACILITY_CODE, // Issue: is there a URN for this one
                KIH_HEALTHCARE_FACILITY_NAME,
                "codingScheme");

        // TODO map properly
        addClassificationTo(eot,
                Constants.XDSDocumentEntry_practiceSettingCode,
                metadata.getAssociatedID(),
                "practiceSettingCode",
                UUID.randomUUID().toString(),
                KIH_PRACTISESETTING_CODE, // Issue: is there a URN for this one
                KIH_PRACTISESETTING_NAME,
                "codingScheme");

        addExternalIdentifierTo(eot,
                metadata.getAssociatedID(),
                UUID.randomUUID().toString(),
                Constants.XDSDocumentEntry_documentEntryUniqueId,
                metadata.getUniqueID(true),
                "XDSDocumentEntry.uniqueId");

        addExternalIdentifierTo(eot,
                metadata.getAssociatedID(),
                UUID.randomUUID().toString(),
                Constants.XDSDocumentEntry_patientId,
                formatCprOid(metadata.getCpr()),
                "XDSDocumentEntry.patientId");

        // eventCode - here the list of actually used codes are included
        addClassificationValueSetTo(eot,
                Constants.XDSDocumentEntry_eventCodeList,
                metadata.getAssociatedID(),
                metadata.getCodeSystem(),
                UUID.randomUUID().toString(),
                metadata.getCodesOfValuesMeasured(),
                metadata.getCodeSystem());

        return eot
    }

    /** add a Slot to an extrinsic object
     *
     * @param rot the RegistryObject to add the slot to
     * @param key the key
     * @param value the value
     */
    void addSlotTo(RegistryObjectType rot, String key, String value) {
        SlotType1 sl = new SlotType1();
        sl.setName(key);
        ValueListType vl = new ValueListType();
        vl.getValue().add(value);
        sl.setValueList(vl);

        rot.getSlot().add(sl);
    }

    void addSlotToRegistryPackage(RegistryPackageType rpt, String key, String value) {
        SlotType1 sl = new SlotType1();
        sl.setName(key);
        ValueListType vl = new ValueListType();
        vl.getValue().add(value);
        sl.setValueList(vl);

        rpt.getSlot().add(sl);
    }

    void setName(RegistryObjectType rot, String name) {
        InternationalStringType ist = new InternationalStringType();
        LocalizedStringType lst = new LocalizedStringType();
        lst.setValue(name);
        ist.getLocalizedString().add(lst);
        rot.setName(ist);
    }

    void setDescription(ExtrinsicObjectType eot, String description) {
        InternationalStringType ist = new InternationalStringType();
        LocalizedStringType lst = new LocalizedStringType();
        lst.setValue(description);
        ist.getLocalizedString().add(lst);
        eot.setDescription(ist);
    }

    void addExternalIdentifierTo(RegistryObjectType rot, String registryObject, String id, String identificationScheme, String value, String name, String objectType = null) {
        ExternalIdentifierType eit = new ExternalIdentifierType();
        eit.setId(id);
        eit.setIdentificationScheme(identificationScheme);
        eit.setValue(value);
        eit.setRegistryObject(registryObject == null ? "" : registryObject);
        if (objectType) eit.objectType = objectType
        InternationalStringType ist = new InternationalStringType();
        LocalizedStringType lst = new LocalizedStringType();
        lst.setValue(name);
        ist.getLocalizedString().add(lst);
        eit.setName(ist);
        rot.getExternalIdentifier().add(eit);
    }

    /** Template method to add a classification to
     * the ExtrinsicObject
     * @param rot the RegistryObject this classification is added to
     * @param classificationScheme
     * @param classifiedObject
     * @param nodeRepresentation
     * @param id Classification elements must have a unique id (if symbolic must be unique within the submission, if UUID must be globally unique)
     * See http://wiki.ihe.net/index.php?title=Annotated_ProvideAndRegister.b_Transaction
     * @param value
     * @param displayName
     */
    void addClassificationTo(RegistryObjectType rot,
                             String classificationScheme, String classifiedObject,
                             String nodeRepresentation, String id, String value, String displayName, String slotName) {
        ClassificationType cst = new ClassificationType();
        cst.setClassificationScheme(classificationScheme);
        cst.setClassifiedObject(classifiedObject);
        if (nodeRepresentation != null)
            cst.setNodeRepresentation(nodeRepresentation);
        cst.setId(id);

        SlotType1 slot = new SlotType1();
        slot.setName(slotName);
        ValueListType vl = new ValueListType();
        vl.getValue().add(value);
        slot.setValueList(vl);

        cst.getSlot().add(slot);

        if (displayName != null) {
            InternationalStringType ist = new InternationalStringType();
            LocalizedStringType lst = new LocalizedStringType();
            lst.setValue(displayName);
            ist.getLocalizedString().add(lst);
            cst.setName(ist);
        }

        rot.getClassification().add(cst);
    }

    void addClassificationTo(RegistryObjectType rot,
                             String classificationScheme, String classifiedObject,
                             String nodeRepresentation, String id, Map<String, List> values, String displayName, String slotName) {
        ClassificationType cst = new ClassificationType();
        cst.setClassificationScheme(classificationScheme);
        cst.setClassifiedObject(classifiedObject);
        if (nodeRepresentation != null)
            cst.setNodeRepresentation(nodeRepresentation);
        cst.setId(id);


        for (key in values.keySet()) {
            SlotType1 slot = new SlotType1();
            slot.setName(key);
            ValueListType vl = new ValueListType();
            slot.setValueList(vl);
            cst.getSlot().add(slot);

            List v = values.get(key)
            for (value in v) {
                vl.getValue().add(value)
            }
        }

        if (displayName != null) {
            InternationalStringType ist = new InternationalStringType();
            LocalizedStringType lst = new LocalizedStringType();
            lst.setValue(displayName);
            ist.getLocalizedString().add(lst);
            cst.setName(ist);
        }

        rot.getClassification().add(cst);
    }


    void addClassificationValueSetTo(ExtrinsicObjectType eot,
                                     String classificationScheme, String classifiedObject,
                                     String nodeRepresentation, String id, String[] value, String displayName) {
        ClassificationType cst = new ClassificationType();
        cst.setClassificationScheme(classificationScheme);
        cst.setClassifiedObject(classifiedObject);
        cst.setNodeRepresentation(nodeRepresentation);
        cst.setId(id);

        // The present encoding is OR semantics,
        // See Vol2a line 2405
        SlotType1 codingScheme = new SlotType1();
        codingScheme.setName("codingScheme");
        ValueListType vl = new ValueListType();
        for (int i = 0; i < value.length; i++) {
            vl.getValue().add(value[i]);
        }
        codingScheme.setValueList(vl);

        cst.getSlot().add(codingScheme);

        InternationalStringType ist = new InternationalStringType();
        LocalizedStringType lst = new LocalizedStringType();
        lst.setValue(displayName);
        ist.getLocalizedString().add(lst);
        cst.setName(ist);

        eot.getClassification().add(cst);
    }


}

