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
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{urn:oio:medcom:monitoringdataset:1.0.1}MonitoringDatasetCollectionResponse" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "monitoringDatasetCollectionResponse"
})
@XmlRootElement(name = "CreateMonitoringDatasetResponseMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class CreateMonitoringDatasetResponseMessage {

    @XmlElement(name = "MonitoringDatasetCollectionResponse", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected List<MonitoringDatasetCollectionResponseType> monitoringDatasetCollectionResponse;

    /**
     * Gets the value of the monitoringDatasetCollectionResponse property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the monitoringDatasetCollectionResponse property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMonitoringDatasetCollectionResponse().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link MonitoringDatasetCollectionResponseType }
     */
    public List<MonitoringDatasetCollectionResponseType> getMonitoringDatasetCollectionResponse() {
        if (monitoringDatasetCollectionResponse == null) {
            monitoringDatasetCollectionResponse = new ArrayList<MonitoringDatasetCollectionResponseType>();
        }
        return this.monitoringDatasetCollectionResponse;
    }

}
