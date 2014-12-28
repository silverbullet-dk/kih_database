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

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.1}CitizenMonitoringDataset"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "citizenMonitoringDataset"
})
@XmlRootElement(name = "GetMonitoringDatasetResponseMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.2")
public class GetMonitoringDatasetResponseMessage {

    @XmlElement(name = "CitizenMonitoringDataset", namespace = "urn:oio:medcom:chronicdataset:1.0.2", required = true)
    protected CitizenMonitoringDatasetType citizenMonitoringDataset;

    /**
     * Gets the value of the citizenMonitoringDataset property.
     *
     * @return possible object is
     * {@link CitizenMonitoringDatasetType }
     */
    public CitizenMonitoringDatasetType getCitizenMonitoringDataset() {
        return citizenMonitoringDataset;
    }

    /**
     * Sets the value of the citizenMonitoringDataset property.
     *
     * @param value allowed object is
     *              {@link CitizenMonitoringDatasetType }
     */
    public void setCitizenMonitoringDataset(CitizenMonitoringDatasetType value) {
        this.citizenMonitoringDataset = value;
    }

}
