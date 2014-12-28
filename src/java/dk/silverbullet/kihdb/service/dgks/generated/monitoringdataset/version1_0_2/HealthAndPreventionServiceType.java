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

package dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HealthAndPreventionServiceType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="HealthAndPreventionServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceStatus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthAndPreventionServiceType", propOrder = {
        "healthAndPreventionServiceStatus"
})
public class HealthAndPreventionServiceType {

    @XmlElement(name = "HealthAndPreventionServiceStatus", required = true)
    protected HealthAndPreventionServiceStatusType healthAndPreventionServiceStatus;

    /**
     * Gets the value of the healthAndPreventionServiceStatus property.
     *
     * @return possible object is
     * {@link HealthAndPreventionServiceStatusType }
     */
    public HealthAndPreventionServiceStatusType getHealthAndPreventionServiceStatus() {
        return healthAndPreventionServiceStatus;
    }

    /**
     * Sets the value of the healthAndPreventionServiceStatus property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionServiceStatusType }
     */
    public void setHealthAndPreventionServiceStatus(HealthAndPreventionServiceStatusType value) {
        this.healthAndPreventionServiceStatus = value;
    }

}
