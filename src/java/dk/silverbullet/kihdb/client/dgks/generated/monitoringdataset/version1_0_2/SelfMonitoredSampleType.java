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

package dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SelfMonitoredSampleType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SelfMonitoredSampleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}LaboratoryReportExtendedCollection"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CreatedByText"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelfMonitoredSampleType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
        "laboratoryReportExtendedCollection",
        "createdByText"
})
public class SelfMonitoredSampleType {

    @XmlElement(name = "LaboratoryReportExtendedCollection", required = true)
    protected LaboratoryReportExtendedCollectionType laboratoryReportExtendedCollection;
    @XmlElement(name = "CreatedByText", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected String createdByText;

    /**
     * Gets the value of the laboratoryReportExtendedCollection property.
     *
     * @return possible object is
     * {@link LaboratoryReportExtendedCollectionType }
     */
    public LaboratoryReportExtendedCollectionType getLaboratoryReportExtendedCollection() {
        return laboratoryReportExtendedCollection;
    }

    /**
     * Sets the value of the laboratoryReportExtendedCollection property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportExtendedCollectionType }
     */
    public void setLaboratoryReportExtendedCollection(LaboratoryReportExtendedCollectionType value) {
        this.laboratoryReportExtendedCollection = value;
    }

    /**
     * Gets the value of the createdByText property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCreatedByText() {
        return createdByText;
    }

    /**
     * Sets the value of the createdByText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreatedByText(String value) {
        this.createdByText = value;
    }

}
