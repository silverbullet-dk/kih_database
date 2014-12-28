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

package dk.silverbullet.kihdb.service.dgks.generated.chronicdataset.version1;

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
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CitizenChronicDataset"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "citizenChronicDataset"
})
@XmlRootElement(name = "GetChronicDatasetResponseMessage", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
public class GetChronicDatasetResponseMessage {

    @XmlElement(name = "CitizenChronicDataset", required = true)
    protected CitizenChronicDatasetType citizenChronicDataset;

    /**
     * Gets the value of the citizenChronicDataset property.
     *
     * @return possible object is
     * {@link CitizenChronicDatasetType }
     */
    public CitizenChronicDatasetType getCitizenChronicDataset() {
        return citizenChronicDataset;
    }

    /**
     * Sets the value of the citizenChronicDataset property.
     *
     * @param value allowed object is
     *              {@link CitizenChronicDatasetType }
     */
    public void setCitizenChronicDataset(CitizenChronicDatasetType value) {
        this.citizenChronicDataset = value;
    }

}
