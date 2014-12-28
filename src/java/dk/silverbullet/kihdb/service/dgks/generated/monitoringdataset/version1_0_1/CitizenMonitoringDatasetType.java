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

package dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CitizenMonitoringDatasetType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CitizenMonitoringDatasetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Citizen"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}SelfMonitoredSampleCollection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CitizenMonitoringDatasetType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
        "citizen",
        "selfMonitoredSampleCollection"
})
public class CitizenMonitoringDatasetType {

    @XmlElement(name = "Citizen", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected CitizenType citizen;
    @XmlElement(name = "SelfMonitoredSampleCollection")
    protected SelfMonitoredSampleCollectionType selfMonitoredSampleCollection;

    /**
     * Gets the value of the citizen property.
     *
     * @return possible object is
     * {@link CitizenType }
     */
    public CitizenType getCitizen() {
        return citizen;
    }

    /**
     * Sets the value of the citizen property.
     *
     * @param value allowed object is
     *              {@link CitizenType }
     */
    public void setCitizen(CitizenType value) {
        this.citizen = value;
    }

    /**
     * Gets the value of the selfMonitoredSampleCollection property.
     *
     * @return possible object is
     * {@link SelfMonitoredSampleCollectionType }
     */
    public SelfMonitoredSampleCollectionType getSelfMonitoredSampleCollection() {
        return selfMonitoredSampleCollection;
    }

    /**
     * Sets the value of the selfMonitoredSampleCollection property.
     *
     * @param value allowed object is
     *              {@link SelfMonitoredSampleCollectionType }
     */
    public void setSelfMonitoredSampleCollection(SelfMonitoredSampleCollectionType value) {
        this.selfMonitoredSampleCollection = value;
    }

}
