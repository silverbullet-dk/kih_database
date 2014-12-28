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
import dk.silverbullet.kihdb.model.Citizen
import grails.util.Holders
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.ObjectFactory
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.stream.StreamSource
import java.text.SimpleDateFormat

public class SubmitPHMRObjectsRequestHelper {

    private static String KIHDB_SOURCE_ID

    // As described in sec. 4.1.7 in rev. 8.0 vol 3
    private static SimpleDateFormat xdsDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    {
        xdsDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public SubmitPHMRObjectsRequestHelper() {
        log.debug "In default constructor"

        if (Holders?.grailsApplication?.config?.kihdb?.xds?.source?.oid) {
            KIHDB_SOURCE_ID = Holders.grailsApplication.config.kihdb.xds.source.oid
            log.debug "Setting Document repository oid from Config. Value: " + KIHDB_SOURCE_ID
        } else {
            KIHDB_SOURCE_ID = Constants.KIHDB_SOURCE_ID
            log.debug "Setting Document repository oid from Constants Value: " + KIHDB_SOURCE_ID
        }
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

    private String getPatientId(Citizen c) {
        return "${c.ssn}^^^&2.16.208&ISO"
    }


    private String getDocumentId(Citizen c) {
        return "urn:ad:kihdb:phmr:doc:" + UUID.randomUUID().toString()
    }


    private String getAuthorInstitution(String institution) {
        // Which form - for now

        return "${institution}^^^^^&2.16.208&ISO^^^^Yder=278467"
//        return  institution
    }

    public SubmitObjectsRequest create() {
        ObjectFactory objFactory = new ObjectFactory();
        return objFactory.createSubmitObjectsRequest();
    }

    public ExtrinsicObjectType addOnDemandDocumentMetadataEntry(
            SubmitObjectsRequest request,
            String documentId,
            String repositoryUniqueId) {
        ExtrinsicObjectType onDemandDocumentMetadata =
                createOnDemandDocumentMetadataEntry(
                        UUID.randomUUID().toString(),
                        repositoryUniqueId);
        addDocumentMetadataEntry(request, documentId, onDemandDocumentMetadata);

        return onDemandDocumentMetadata;
    }

    public ExtrinsicObjectType addStableDocumentMetadataEntry(
            SubmitObjectsRequest request,
            String documentId,
            String repositoryUniqueId) {
        ExtrinsicObjectType stableDocumentMetadata = createStableDocumentMetadataEntry(
                UUID.randomUUID().toString(),
                repositoryUniqueId);
        addDocumentMetadataEntry(request, documentId, stableDocumentMetadata);
        addStableDocumentMetadata(stableDocumentMetadata);
        return stableDocumentMetadata;
    }


    public void addDocumentMetadataAttributes(ExtrinsicObjectType documentMetadataEntry,
                                              String authorInstitution, Citizen citizen) {
        documentMetadataEntry.getClassification().add(createClassification("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d",
                "authorInstitution", getAuthorInstitution(authorInstitution), null, null, documentMetadataEntry.getId()));

        documentMetadataEntry.getExternalIdentifier().add(
                createExternalIdentifierType("urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", "XDSDocumentEntry.patientId",
                        getPatientId(citizen), documentMetadataEntry.getId()));

        documentMetadataEntry.getSlot().add(createSlot("sourcePatientId", getPatientId(citizen)));
    }

    public void addDocumentMetadataDefaultAttributes(ExtrinsicObjectType documentMetadataEntry, List<String> valueList) {

        // practiceSettingCodeDisplayName
        addClassification(documentMetadataEntry, "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead", "urn:ad:net4care:practicesettingcode",
                "urn:ad:net4care:practicesettingcode", "Net4Care Practice SettingCode");

        // classCodeDisplayName
        addClassification(documentMetadataEntry, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", "urn:ad:net4care:teleobservation",
                "urn:ad:net4care:teleobservation", "Net4Care Teleobservation");

        // formatCode
        addClassification(documentMetadataEntry, "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d", "urn:ad:net4care:phmr",
                "Net4Care PHMR format", "CDAR2/IHE 1.0");

        // eventCodeList
        addClassification(documentMetadataEntry, "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4", valueList,
                "2.16.840.1.113883.6.47", "2.16.840.1.113883.6.47");

        // healthcareFacilityTypeCodeDisplayName
        addClassification(documentMetadataEntry, "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", "urn:ad:net4care:healthcarefacilitytypecode",
                "urn:ad:net4care:healthcarefacilitytypecode", "Net4Care HealthCare Facility Code");

        // typeCodeDisplayName
        addClassification(documentMetadataEntry, "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983", "urn:ad:net4care:teleobservation",
                "Type Teleobservation", "Net4Care TypeCode");

        // ConfidentialityCode
        addClassification(documentMetadataEntry, "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f",
                "urn:ad:net4care:confidentialitycode", "Net4Care Confidentiality Code", "Normal");

        //Language
        documentMetadataEntry.getSlot().add(createSlot("languageCode", "en-US"));
    }

    public void setStatusDeprecated(ExtrinsicObjectType documentMetadataEntry) {
        documentMetadataEntry.setStatus(Constants.IHE_XDS_DOCUMENT_STATUS_DEPRECATED);
    }

    public RegistryPackageType addSubmissionSet(SubmitObjectsRequest request) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryPackageType submissionSetEntry = objFactory.createRegistryPackageType();
        submissionSetEntry.setId(UUID.randomUUID().toString());
        submissionSetEntry.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");
        request.getRegistryObjectList().getIdentifiable().add(objFactory.createRegistryPackage(submissionSetEntry));

        // Classify registry package as XDSSubmissionSet (outside the RegistryPackage)
        ClassificationType classification = objFactory.createClassificationType();
        classification.setClassificationNode("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
        // XDSSubmissionSet node value
        classification.setId(UUID.randomUUID().toString());
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setClassifiedObject(submissionSetEntry.getId());
        request.getRegistryObjectList().getIdentifiable().add(objFactory.createClassification(classification));

        return submissionSetEntry;
    }

    public void addSubmissionSetAttributes(RegistryPackageType submissionSetEntry, Citizen citizen) {
        addSubmissionSetExternalIdentifier(submissionSetEntry, "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446",
                "XDSSubmissionSet.patientId", getPatientId(citizen));
    }

    public void addSubmissionSetDefaultAttributes(RegistryPackageType submissionSetEntry) {

        // contentTypeCodeDisplayName
        addClassification(submissionSetEntry, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", "Net4CareCodingScheme",
                "Net4CareCodingSchemeValue", "Net4Care Coding Scheme");

        // XDSSubmissionSet.sourceId ??
        addSubmissionSetExternalIdentifier(submissionSetEntry, "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832",
                "XDSSubmissionSet.sourceId", KIHDB_SOURCE_ID);

        // submissionTime
        submissionSetEntry.getSlot().add(createSlot("submissionTime", toXDSDateFormat(new Date())));

        // XDSSubmissionSet.uniqueId
        addSubmissionSetExternalIdentifier(submissionSetEntry, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8",
                "XDSSubmissionSet.uniqueId", UUID.randomUUID().toString());
    }

    public AssociationType1 addAssociation(SubmitObjectsRequest request,
                                           ExtrinsicObjectType documentMetadataEntry,
                                           RegistryPackageType submissionSetEntry) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        AssociationType1 association = createAssociation("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember",
                submissionSetEntry.getId(), documentMetadataEntry.getId());
        request.getRegistryObjectList().getIdentifiable().add(objFactory.createAssociation(association));
        return association;
    }

    public List<AssociationType1> addAssociationsForReplacements(
            SubmitObjectsRequest request,
            ExtrinsicObjectType documentMetadataEntry,
            List<String> extrinsicObjectIdsReplaced) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        List<AssociationType1> result = new ArrayList<AssociationType1>();
        for (String extrinsicObjectId : extrinsicObjectIdsReplaced) {
            AssociationType1 association = createAssociation("urn:ihe:iti:2007:AssociationType:RPLC", documentMetadataEntry.getId(), extrinsicObjectId);
            request.getRegistryObjectList().getIdentifiable().add(objFactory.createAssociation(association));
            result.add(association);
        }
        return result;
    }

    private void addClassification(
            ExtrinsicObjectType documentMetadataEntry,
            String classificationScheme,
            List<String> codingValue,
            String name,
            String nodeRepresentation) {
        documentMetadataEntry.getClassification().add(
                createClassification(classificationScheme, "codingScheme", codingValue,
                        name, nodeRepresentation, documentMetadataEntry.getId()));
    }

    private void addClassification(
            ExtrinsicObjectType documentMetadataEntry,
            String classificationScheme,
            String codingValue,
            String name,
            String nodeRepresentation) {
        documentMetadataEntry.getClassification().add(
                createClassification(classificationScheme, "codingScheme", codingValue,
                        name, nodeRepresentation, documentMetadataEntry.getId()));
    }

    private ClassificationType createClassification(
            String classificationScheme,
            String slotName,
            String slotValue,
            String name,
            String nodeRepresentation,
            String registryObjectId) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ClassificationType classification = objFactory.createClassificationType();
        classification.setId(UUID.randomUUID().toString());
        classification.setClassificationScheme(classificationScheme);
        classification.setClassifiedObject(registryObjectId);
        SlotType1 slotEntry = objFactory.createSlotType1();
        slotEntry.setName(slotName);
        slotEntry.setValueList(objFactory.createValueListType());
        slotEntry.getValueList().getValue().add(slotValue);
        classification.getSlot().add(slotEntry);
        if (name != null) {
            classification.setName(objFactory.createInternationalStringType());
            LocalizedStringType localizedName = objFactory.createLocalizedStringType();
            localizedName.setValue(name);
            classification.getName().getLocalizedString().add(localizedName);
        }
        if (nodeRepresentation != null) {
            classification.setNodeRepresentation(nodeRepresentation);
        }
        return classification;
    }

    private ClassificationType createClassification(
            String classificationScheme,
            String slotName,
            List<String> slotValues,
            String name,
            String nodeRepresentation,
            String registryObjectId) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ClassificationType classification = objFactory.createClassificationType();
        classification.setId(UUID.randomUUID().toString());
        classification.setClassificationScheme(classificationScheme);
        classification.setClassifiedObject(registryObjectId);
        SlotType1 slotEntry = objFactory.createSlotType1();
        slotEntry.setName(slotName);
        slotEntry.setValueList(objFactory.createValueListType());
        for (String value : slotValues) {
            slotEntry.getValueList().getValue().add(value);
        }
        classification.getSlot().add(slotEntry);
        if (name != null) {
            classification.setName(objFactory.createInternationalStringType());
            LocalizedStringType localizedName = objFactory.createLocalizedStringType();
            localizedName.setValue(name);
            classification.getName().getLocalizedString().add(localizedName);
        }
        if (nodeRepresentation != null) {
            classification.setNodeRepresentation(nodeRepresentation);
        }
        return classification;
    }

    private SlotType1 createSlot(String name, String value) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        SlotType1 slotEntry = objFactory.createSlotType1();
        slotEntry.setName(name);
        slotEntry.setValueList(objFactory.createValueListType());
        slotEntry.getValueList().getValue().add(value);
        return slotEntry;
    }

    private void addClassification(
            RegistryPackageType submissionSetEntry, String classificationScheme, String codingValue, String name, String nodeRepresentation) {
        submissionSetEntry.getClassification().add(
                createClassification(classificationScheme, "codingScheme", codingValue,
                        name, nodeRepresentation, submissionSetEntry.getId()));
    }

    private void addSubmissionSetExternalIdentifier(
            RegistryPackageType submissionSetEntry,
            String identificationScheme,
            String name,
            String value) {
        submissionSetEntry.getExternalIdentifier().add(
                createExternalIdentifierType(identificationScheme, name, value, submissionSetEntry.getId()));
    }

    private void addStableDocumentMetadata(
            ExtrinsicObjectType stableDocumentMetadata) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        SlotType1 creationTimeSlot = objFactory.createSlotType1();
        creationTimeSlot.setName("creationTime");
        creationTimeSlot.setValueList(objFactory.createValueListType());
        creationTimeSlot.getValueList().getValue().add(toXDSDateFormat(new Date()));
        stableDocumentMetadata.getSlot().add(creationTimeSlot);

        SlotType1 sizeSlot = objFactory.createSlotType1();
        sizeSlot.setName("size");
        sizeSlot.setValueList(objFactory.createValueListType());
        sizeSlot.getValueList().getValue().add("123");
        stableDocumentMetadata.getSlot().add(sizeSlot);

        SlotType1 hashSlot = objFactory.createSlotType1();
        hashSlot.setName("hash");
        hashSlot.setValueList(objFactory.createValueListType());
        hashSlot.getValueList().getValue().add("123");
        stableDocumentMetadata.getSlot().add(hashSlot);
    }

    private String toXDSDateFormat(Date date) {
        return xdsDateFormat.format(date);
    }

    private void addDocumentMetadataEntry(SubmitObjectsRequest request,
                                          String documentId, ExtrinsicObjectType documentMetadata) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        if (request.getRegistryObjectList() == null) {
            request.setRegistryObjectList(objFactory.createRegistryObjectListType());
        }
        request.getRegistryObjectList().getIdentifiable().add(
                objFactory.createExtrinsicObject(documentMetadata));

        ExternalIdentifierType uniqueIdIdentifier =
                createExternalIdentifierType(Constants.XDS_EXTERNAL_IDENTIFIER_UNIQUE_ID_IDENTIFICATION_SCHEME,
                        "XDSDocumentEntry.uniqueId", documentId, documentMetadata.getId());
        documentMetadata.getExternalIdentifier().add(uniqueIdIdentifier);
    }

    private ExternalIdentifierType createExternalIdentifierType(
            String identificationScheme,
            String name,
            String value,
            String registryObjectId) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ExternalIdentifierType externalIdentifier = objFactory.createExternalIdentifierType();
        externalIdentifier.setId(UUID.randomUUID().toString());
        externalIdentifier.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        externalIdentifier.setIdentificationScheme(identificationScheme);
        externalIdentifier.setRegistryObject(registryObjectId);
        externalIdentifier.setName(objFactory.createInternationalStringType());
        LocalizedStringType localizedName = objFactory.createLocalizedStringType();
        localizedName.setValue(name);
        externalIdentifier.getName().getLocalizedString().add(localizedName);
        externalIdentifier.setValue(value);
        return externalIdentifier;
    }

    private ExtrinsicObjectType createOnDemandDocumentMetadataEntry(
            String documentId,
            String repositoryUniqueId) {
        return createDocumentMetadataEntry(
                documentId,
                Constants.XDS_ONDEMANDDOCUMENT_OBJ_TYPE,
                repositoryUniqueId);
    }

    private ExtrinsicObjectType createStableDocumentMetadataEntry(
            String documentId,
            String repositoryUniqueId) {
        return createDocumentMetadataEntry(
                documentId,
                Constants.XDS_STABLEDOCUMENT_OBJ_TYPE,
                repositoryUniqueId);
    }

    private ExtrinsicObjectType createDocumentMetadataEntry(
            String documentId,
            String documentObjType,
            String repositoryUniqueId) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ExtrinsicObjectType extrinsicObjectType = objFactory.createExtrinsicObjectType();
        extrinsicObjectType.setId(documentId);
        extrinsicObjectType.setMimeType("text/xml");
        extrinsicObjectType.setObjectType(documentObjType);
        extrinsicObjectType.setStatus(Constants.IHE_XDS_DOCUMENT_STATUS_APPROVED);

        SlotType1 repositoryUniqueIdSlot = objFactory.createSlotType1();
        repositoryUniqueIdSlot.setName("repositoryUniqueId");
        repositoryUniqueIdSlot.setValueList(objFactory.createValueListType());
        repositoryUniqueIdSlot.getValueList().getValue().add(repositoryUniqueId);
        extrinsicObjectType.getSlot().add(repositoryUniqueIdSlot);
        return extrinsicObjectType;
    }

    private AssociationType1 createAssociation(String associationType, String sourceObjectId, String targetObjectId) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        AssociationType1 association = objFactory.createAssociationType1();
        association.setId(UUID.randomUUID().toString());
        association.setAssociationType(associationType);
        association.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
        association.setSourceObject(sourceObjectId);
        association.setTargetObject(targetObjectId);
        return association;
    }
}
