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

import dk.silverbullet.kihdb.model.Citizen

public class RegistryMetadata {
    private String title;
    private String type;
    private String cpr;
    private long timestamp;
    private String codeSystem;
    private String[] codesOfValuesMeasured;
    private String organizationId;
    private String treatmentId;
    private Citizen citizen

    String getRegistryPackageId() {
        return handleUUID(registryPackageId)
    }

    void setRegistryPackageId(String registryPackageId) {
        this.registryPackageId = registryPackageId
    }

    String getExtrinsicObjectId() {
        return handleUUID(extrinsicObjectId)
    }

    void setExtrinsicObjectId(String extrinsicObjectId) {
        this.extrinsicObjectId = extrinsicObjectId
    }

    private String registryPackageId
    private String extrinsicObjectId


    String submissionSet = "SubmissionSet01"
    String documentID = "Document01"

    String getUniqueID() {
        handleUUID(uniqueID)
    }

    String getUniqueID(boolean withUrn) {
        String retVal = uniqueID

        if (withUrn) retVal = "urn:uuid:${retVal}"

        return retVal
    }

    void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID
    }

    private String uniqueID
    private String associatedID
    private String submissionSetID

    String getAssociatedID() {
        handleUUID(associatedID)
    }

    String handleUUID(String id) {
        boolean uuidDetected = false

        try {
            UUID.fromString(id)
            uuidDetected = true
            log.debug "UUID detected"
        } catch (IllegalArgumentException e) {
            log.debug "No UUID detected"

        }


        if (uuidDetected) {
            return "urn:uuid:$id"
        } else {
            return id
        }
    }


    String getAssociatedID(boolean withUrn) {
        String retVal = getAssociatedID()

        if (withUrn) {
            retVal = "urn:uuid:${retVal}"
        }

        return retVal
    }


    void setAssociatedID(String associatedID) {
        this.associatedID = associatedID
    }

    String getSubmissionSetID() {
        return submissionSetID
    }

    void setSubmissionSetID(String submissionSetID) {
        this.submissionSetID = submissionSetID
    }

    String getClassification() {
        return classification
    }

    void setClassification(String classification) {
        this.classification = classification
    }

    private String classification;

    public RegistryMetadata() {}

    public RegistryMetadata(String cpr, long timestamp,
                            String codeSystem,
                            String[] codesOfValuesMeasured,
                            String organizationId,
                            String treatmentId) {
        super();
        this.cpr = cpr;
        this.timestamp = timestamp;
        this.codeSystem = codeSystem;
        this.codesOfValuesMeasured = codesOfValuesMeasured;
        this.organizationId = organizationId;
        this.treatmentId = treatmentId;
        this.type = "KIH tele observation"; // TODO: review with XDS metadata
        this.title = organizationId + " tele observation"; // TODO: review with XDS metadata
    }


    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    String getCpr() {
        return cpr
    }

    void setCpr(String cpr) {
        this.cpr = cpr
    }

    long getTimestamp() {
        return timestamp
    }

    void setTimestamp(long timestamp) {
        this.timestamp = timestamp
    }

    String getCodeSystem() {
        return codeSystem
    }

    void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem
    }

    String[] getCodesOfValuesMeasured() {
        return codesOfValuesMeasured
    }

    void setCodesOfValuesMeasured(String[] codesOfValuesMeasured) {
        this.codesOfValuesMeasured = codesOfValuesMeasured
    }

    String getOrganizationId() {
        return organizationId
    }

    void setOrganizationId(String organizationId) {
        this.organizationId = organizationId
    }

    String getTreatmentId() {
        return treatmentId
    }

    void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId
    }


    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }
}
