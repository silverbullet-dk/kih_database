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
package dk.silverbullet.kihdb.constants

class Constants {
    public static final String SUPPORTED_SEGMENT = "SelfMonitoredSampleCollection"

    public static String AUDIT_ISSUER = "AUDIT_ISSUER"
    public static String AUDIT_IDCARD = "AUDIT_IDCARD"
    public static String AUDIT_CPR = "AUDIT_CPR"
    public static String AUDIT_OPERATION = "AUDIT_OPERATION"
    public static String AUDIT_SERVICE = "AUDIT_SERVICE"
    public static String AUDIT_START_TIME = "AUDIT_START_TIME"
    public static String AUDIT_URL = "AUDIT_URL"
    public static String AUDIT_IP = "AUDIT_IP"

    // Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN"
    public static final String ROLE_HEALTHCARE_PROFESSIONAL = "ROLE_HEALTHCARE_PROFESSIONAL"
    public static final String ROLE_SYSTEM = "ROLE_SYSTEM"

    // SOSI and HSUID
    public static final String SOSI_OVERRIDE = "SOSI_OVERRIDE"
    public static final String HSUID_OVERRIDE = "HSUID_OVERRIDE"

    public static final String SOSI_DGWS_VERSION = "SOSI_DGWS_VERSION"
    public static final String SOSI_MESSAGE_ID = "SOSI_MESSSAGE_ID"
    public static final String SOSI_FLOW_ID = "SOSI_FLOW_ID"
    public static final String SOSI_FLOW_VALUE = "SOSI_FLOW_STATUS_VALUE"

    public static final String SOSI_FAULT_CODE = "SOSI_FAULT_CODE"
    public static final String SOSI_FAULT_STRING = "SOSI_FAULT_STRING"
    public static final int SOSI_FAULT_UNAUTHORIZED_VALUE = 100

    static final String XDS_BODY_MESSAGE = "XDS_BODY_MESSAGE"


    public static final int ERROR_CODE_IDCARD_NOT_VALID = 100
    public static final int ERROR_CODE_SERVICE_STORAGE_ERROR = 200
    public static final int ERROR_CODE_DOCUMENT_SERVICE_STORAGE_ERROR = 210
    public static final int ERROR_CODE_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA = 300
    public static final int ERROR_CODE_COULD_NOT_DELETE_MESSAGE = 400
    public static final int ERROR_CODE_COULD_CREATE_CITIZEN_MESSAGE = 500
    public static final int ERROR_CODE_HSUID_HEADER_MISSING = 600

    public static final int ERROR_CODE_XDS_REGISTRY_INITIALIZATION = 600

    public static final String ERROR_TEXT_DOCUMENT_SERVICE_STORAGE_ERROR = "Could not store XDS document"
    public static final String ERROR_TEXT_COULD_NOT_DELETE_MESSAGE = "Could not delete sample"
    public static final String ERROR_TEXT_COULD_CREATE_CITIZEN_MESSAGE = "Could not create or update citizen"
    public static
    final String ERROR_TEXT_REQUESTING_USER_NOT_ALLOWED_ACCESS_TO_DATA = "User does not have access to requested measurement"

    public static final String ERROR_TEXT_XDS_REGISTRY_INITIALIZATION = "Error initializing XDS registry"
    public static final String ERROR_TEXT_HSUID_HEADER_MISSING = "HSUID Header is missing"


    public static final String ERROR_SYSTEM_NAME = "KIH DB"
    public static final String HSUID_ACTING_USER_SSN = "HSUID_ACTING_USER"
    public static
    final ArrayList<String> HSUID_ENABLED_OPERATIONS = new ArrayList<String>(Arrays.asList("GetMonitoringDataset"))

    // Test XDS UUID
    public static final String TEST_NANCY_PHMR_UUID = "6c56d94d-1347-43b2-97ae-d3d50409c590"

    // PHMR vitals signs mappning to UIPAC kodes
    public static
    final List<String> VITAL_SIGN_CODES = new ArrayList<String>(Arrays.asList("NPU03011", "DNK05472", "MCS88019", "MCS88020", "NPU21692"))

    // XDS
    public final
    static String XDS_CLASSIFICATION_AUTHOR_IDENTIFICATION_SCHEME = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public final
    static String XDS_EXTERNAL_IDENTIFIER_UNIQUE_ID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    public final
    static String IHE_REGISTRY_STORED_QUERY_FIND_DOCUMENTS_QUERY_ID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
//    public final static String IHE_REGISTRY_STORED_QUERY_PATIENT_ID_QUERY_PARAM_SLOT_NAME = "$XDSDocumentEntryPatientId";
//    public final static String IHE_REGISTRY_STORED_QUERY_DOC_ENTRY_STATUS_QUERY_PARAM_SLOT_NAME = "$XDSDocumentEntryStatus";
//    public final static String IHE_REGISTRY_STORED_QUERY_DOC_ENTRY_TYPE_QUERY_PARAM_SLOT_NAME = "$XDSDocumentEntryType";
    public final
    static String IHE_XDS_DOCUMENT_STATUS_DEPRECATED = "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated";
    public final static String IHE_XDS_DOCUMENT_STATUS_APPROVED = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
    public final
    static String IHE_XDS_DOCUMENT_ENTRY_STABLE_DOCUMENT_CLASSIFICATION_UUID = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    public final
    static String IHE_XDS_DOCUMENT_ENTRY_ONDEMAND_DOCUMENT_CLASSIFICATION_UUID = "urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248";
    public final static String XDS_REGISTRY_STORED_QUERY_RESPONSE_OPTION_LEAF_CLASS = "LeafClass";
    public final static String XDS_STABLEDOCUMENT_OBJ_TYPE = IHE_XDS_DOCUMENT_ENTRY_STABLE_DOCUMENT_CLASSIFICATION_UUID;
    public final
    static String XDS_ONDEMANDDOCUMENT_OBJ_TYPE = IHE_XDS_DOCUMENT_ENTRY_ONDEMAND_DOCUMENT_CLASSIFICATION_UUID;

    public final
    static String XDS_REGISTRY_SUCCESSFULLY_REGISTERED = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    public final
    static String XDS_REGISTRY_FAILED_REGISTERED = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"

    // Net4Care OID

    /** The LOINC code */
    public static final String LOINC_OID = "2.16.840.1.113883.6.1";
    /** The IUPAC code, as found in the HL7 registry (http://www.hl7.org/oid/index.cfm),
     *  http://www.iupac.org. Perhaps Denmark has a profile with another OID ?
     */
    public static final String IUPAC_OID = "2.16.840.1.113883.6.47w";
    /**
     * The code for waveform-observables from SNOMED CT
     */
    public static final String SNOMED_CT = "2.16.840.1.113883.6.96";

    // === Net4Care OIDs
    /**
     *
     You have now registered the following OID:

     OID
     2.16.840.1.113883.3.1558

     OID Key
     8FE6306B-7F31-42FD-884F-66580DA6C94A

     Please record this for future reference; you will need both the OID and the OID Key to edit your entry.

     You can also edit your OID using the following link:

     http://www.hl7.org/oid/index.cfm?Comp_OID=2.16.840.1.113883.3.1558&OID_Key=8FE6306B-7F31-42FD-884F-66580DA6C94A

     *
     */
    public static final String MEDCOM_ROOT_OID = "2.16.840.1.113883.3.4208";

    // 100 for registers
    // 2 for CPR

    public static final String MEDCOM_CPR_OID = MEDCOM_ROOT_OID + ".100.2";

    // Branch 1 : Danish Medical coding systems
    private static final String DK_MEDICAL_CODE_BRANCH = ".1";

    // Requirement by Sekoia and Region Hovedstaden: Who is the person making the measurement
    // Opsamler               (egenopsamling/assisteret opsamling/behandleropsamling)
    public static final String DK_MEASUREMENT_CONTEXT_AUTHOR_OID =
            MEDCOM_ROOT_OID + DK_MEDICAL_CODE_BRANCH + ".1";

    // Requirement by Sekoia and Region Hovedstaden: How has the measurement been provided
    // Provision: manual entered, electronic transmission
    public static final String DK_MEASUREMENT_CONTEXT_PROVISION_OID =
            MEDCOM_ROOT_OID + DK_MEDICAL_CODE_BRANCH + ".2";

    // Branch 2 : Danish public coding systems
    private static final String DK_PUBLIC_CODE_BRANCH = ".2";
    public static final String DK_CPR_OID = MEDCOM_ROOT_OID + DK_PUBLIC_CODE_BRANCH + ".1";

    // Branch 10: Danish Hospital Organizational Units
    private static final String DK_HOSPITAL_ORGANIZATIONAL_UNITS = ".10";

    public static final String DK_REGION_MIDT = MEDCOM_ROOT_OID + DK_HOSPITAL_ORGANIZATIONAL_UNITS + ".6";
    // old 06 telephone area :)

    // Skejby sygehus
    public static final String DK_REGION_MIDT_SKEJBY_SYGEHUS = DK_REGION_MIDT + ".1";

    // Devices used in the net4care project
    private static final String DK_DEVICE_TYPE_ROOT_CODE = ".3";

    public static final String NET4CARE_DEVICE_OID = MEDCOM_ROOT_OID + DK_DEVICE_TYPE_ROOT_CODE;

    // KIH DB XDS DocumentRepositoryID
    public static final String KIHDB_DOCUMENTREPOSITORY_ID = "2.16.840.1.113883.3.1558.2.1"
    //TODO - MDM / Systematic / NPI - provide other.
    public static final String KIHDB_SOURCE_ID = "1.3.6.1.4.1.21367.2005.3.7"

    // XDS

    static final String XDS_B_STATUS_SUCCESS = 'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success'
    public static final String XDSDocumentEntry = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";

    // codes for the attributes of the XDSDocumentEntry
    // TF3 p 23
    public static final String XDSDocumentEntry_formatCode = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String XDSDocumentEntry_classCode = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    public static final String XDSDocumentEntry_typeCode = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    public static final String XDSDocumentEntry_eventCodeList = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    public static final String XDSDocumentEntry_confidentialityCode = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    public static
    final String XDSDocumentEntry_healthcareFacilityTypeCode = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    public static final String XDSDocumentEntry_practiceSettingCode = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    public static final String XDSDocumentEntry_documentEntryUniqueId = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    public static final String XDSDocumentEntry_patientId = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    public static final String XDSDocumentEntry_authorId = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d"
    // "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d";
    public static final String XDSDocumentEntry_contentTypeCode = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";

    public static final String XDSSubmissionSet_uniqueId = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    public static final String XDSSubmissionSet_sourceId = "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";
    public static final String XDSSubmissionSet_patientId = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    public static final String XDSSubmissionSet_classificationNode = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";


}
