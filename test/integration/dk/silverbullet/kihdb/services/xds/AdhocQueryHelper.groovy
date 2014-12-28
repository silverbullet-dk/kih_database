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
package dk.silverbullet.kihdb.services.xds

import dk.silverbullet.kihdb.constants.Constants
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse
import oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*

import javax.xml.bind.JAXBElement


/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2012,2013 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
class AdhocQueryHelper {

    private String getPatientId(String c) {
        return "${c}^^^&2.16.208&ISO"
    }


    public AdhocQueryRequest createRegistryStoredQueryFindDocumentsRequest(
            String patientId,
            List<String> documentEntryStatus,
            List<String> documentEntryTypes) {
        AdhocQueryRequest request =
                createAdhocQueryRequest(Constants.IHE_REGISTRY_STORED_QUERY_FIND_DOCUMENTS_QUERY_ID);
        addQueryParameters(
                request,
                patientId,
                documentEntryStatus,
                documentEntryTypes);
        return request;
    }

    public AdhocQueryRequest createRequestStoredQueryFindDocumentsApprovedOnly(String patientId) {
        return createRegistryStoredQueryFindDocumentsRequest(
                patientId,
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_STATUS_APPROVED),
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_ENTRY_ONDEMAND_DOCUMENT_CLASSIFICATION_UUID,
                        Constants.IHE_XDS_DOCUMENT_ENTRY_STABLE_DOCUMENT_CLASSIFICATION_UUID));
    }

    public AdhocQueryRequest createRequestApprovedOnDemandDocumentsOnly(String patientId) {
        return createRegistryStoredQueryFindDocumentsRequest(
                patientId,
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_STATUS_APPROVED),
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_ENTRY_ONDEMAND_DOCUMENT_CLASSIFICATION_UUID));
    }

    public AdhocQueryRequest createRequestApprovedStableDocumentsOnly(String patientId) {
        return createRegistryStoredQueryFindDocumentsRequest(
                patientId,
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_STATUS_APPROVED),
                Arrays.asList(Constants.IHE_XDS_DOCUMENT_ENTRY_STABLE_DOCUMENT_CLASSIFICATION_UUID));
    }

    private List<JAXBElement<? extends IdentifiableType>> findIdentifiableHavingExternalIdentifier(AdhocQueryResponse response, String id) {
        List<JAXBElement<? extends IdentifiableType>> result = new ArrayList<JAXBElement<? extends IdentifiableType>>();
        for (JAXBElement<? extends IdentifiableType> identifiable : response.getRegistryObjectList().getIdentifiable()) {
            if (identifiable != null && ExtrinsicObjectType.class.isAssignableFrom(identifiable.getDeclaredType())) {
                ExtrinsicObjectType extObj = (ExtrinsicObjectType) identifiable.getValue();
                for (ExternalIdentifierType extIdentifier : extObj.getExternalIdentifier()) {
                    if (Constants.XDS_EXTERNAL_IDENTIFIER_UNIQUE_ID_IDENTIFICATION_SCHEME.equals(extIdentifier.getIdentificationScheme()) &&
                            id.equals(extIdentifier.getValue())) {
                        result.add(identifiable);
                    }
                }
            }
        }
        return result;
    }

    private List<ExtrinsicObjectType> findDocument(AdhocQueryResponse response, String id) {
        List<ExtrinsicObjectType> result = new ArrayList<ExtrinsicObjectType>();
        List<JAXBElement<? extends IdentifiableType>> identifiableList =
                findIdentifiableHavingExternalIdentifier(response, id);
        for (JAXBElement<? extends IdentifiableType> identifiable : identifiableList) {
            if (identifiable != null && ExtrinsicObjectType.class.isAssignableFrom(identifiable.getDeclaredType())) {
                result.add((ExtrinsicObjectType) identifiable.getValue());
            }
        }
        return result;
    }


    private String getSingleValue(IdentifiableType identifiable, String slotName) {
        ValueListType valueList = getValueList(identifiable, slotName);
        if (valueList != null) {
            return getSingleValue(valueList);
        }
        return null;
    }

    private ValueListType getValueList(IdentifiableType identifiable, String slotName) {
        for (SlotType1 slot : identifiable.getSlot()) {
            if (slotName.equals(slot.getName())) {
                return slot.getValueList();
            }
        }
        return null;
    }

    private String getSingleValue(ValueListType valueList) {
        if (valueList.getValue().size() != 1) {
            return null;
        }
        return valueList.getValue().get(0);
    }

    private AdhocQueryRequest createAdhocQueryRequest(String queryId) {
        ObjectFactory objFactory = new ObjectFactory();
        AdhocQueryRequest adhocQueryRequest = objFactory.createAdhocQueryRequest();
        adhocQueryRequest.setId(generateId());
        ResponseOptionType responseOption = objFactory.createResponseOptionType();
        responseOption.setReturnComposedObjects(true);
        responseOption.setReturnType(Constants.XDS_REGISTRY_STORED_QUERY_RESPONSE_OPTION_LEAF_CLASS);
        adhocQueryRequest.setResponseOption(responseOption);

        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        AdhocQueryType adhocQueryType = rimObjFactory.createAdhocQueryType();
        adhocQueryType.setId(queryId);
        adhocQueryRequest.setAdhocQuery(adhocQueryType);
        return adhocQueryRequest;
    }

    private void addQueryParameters(
            AdhocQueryRequest request,
            String patientId,
            List<String> documentEntryStatus,
            List<String> documentEntryTypes) {
        // Patient id

        // Patient id
        request.getAdhocQuery().getSlot().add(createQueryParameterSlot("\$XDSDocumentEntryPatientId",
                createSingleValueQueryParameter(patientId)));
        // Status
        List<String> documentStatusQueryParams = new ArrayList<String>();
        for (String status : documentEntryStatus) {
            documentStatusQueryParams.add(createQueryParameter(status));
        }
        request.getAdhocQuery().getSlot().add(createQueryParameterSlot("\$XDSDocumentEntryStatus",
                documentStatusQueryParams));
        // Types
        List<String> documentTypeQueryParams = new ArrayList<String>();
        for (String type : documentEntryTypes) {
            documentTypeQueryParams.add(createQueryParameter(type));
        }
        request.getAdhocQuery().getSlot().add(createQueryParameterSlot("\$XDSDocumentEntryType",
                documentTypeQueryParams));

    }

    private SlotType1 createQueryParameterSlot(String name, String value) {
        SlotType1 slot = createSlot(name);
        slot.setValueList(createValueList(Arrays.asList(value)));
        return slot;
    }

    private SlotType1 createQueryParameterSlot(String name, List<String> values) {
        SlotType1 slot = createSlot(name);
        slot.setValueList(createValueList(values));
        return slot;
    }

    private SlotType1 createSlot(String name) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        SlotType1 slot = objFactory.createSlotType1();
        slot.setName(name);
        return slot;
    }

    private ValueListType createValueList(List<String> values) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory objFactory =
                new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ValueListType valueListType = objFactory.createValueListType();
        for (String value : values) {
            valueListType.getValue().add(value);
        }
        return valueListType;
    }

    private String createQueryParameter(String value) {
        return "(\'" + value + "\')";
    }

    private String createSingleValueQueryParameter(String value) {
        return "\'" + value + "\'";
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}

