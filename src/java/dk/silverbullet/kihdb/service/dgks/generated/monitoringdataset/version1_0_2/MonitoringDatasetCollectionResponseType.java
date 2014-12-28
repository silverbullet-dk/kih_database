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
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for MonitoringDatasetCollectionResponseType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="MonitoringDatasetCollectionResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifier"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoringDatasetCollectionResponseType", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", propOrder = {
        "personCivilRegistrationIdentifier",
        "uuidIdentifier"
})
public class MonitoringDatasetCollectionResponseType {

    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", required = true)
    protected String personCivilRegistrationIdentifier;
    @XmlElement(name = "UuidIdentifier", namespace = "urn:oio:medcom:chronicdataset:1.0.0", required = true)
    protected List<String> uuidIdentifier;

    /**
     * Gets the value of the personCivilRegistrationIdentifier property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonCivilRegistrationIdentifier() {
        return personCivilRegistrationIdentifier;
    }

    /**
     * Sets the value of the personCivilRegistrationIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonCivilRegistrationIdentifier(String value) {
        this.personCivilRegistrationIdentifier = value;
    }

    /**
     * Gets the value of the uuidIdentifier property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uuidIdentifier property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUuidIdentifier().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getUuidIdentifier() {
        if (uuidIdentifier == null) {
            uuidIdentifier = new ArrayList<String>();
        }
        return this.uuidIdentifier;
    }

}
