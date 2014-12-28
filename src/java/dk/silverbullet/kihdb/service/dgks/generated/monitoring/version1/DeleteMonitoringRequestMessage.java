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

package dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1;

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
 *         &lt;element name="PersonCivilRegistrationIdentifier" type="{http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/}PersonCivilRegistrationIdentifierType"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "personCivilRegistrationIdentifier",
        "uuidIdentifier"
})
@XmlRootElement(name = "DeleteMonitoringRequestMessage", namespace = "urn:oio:medcom:chronicdataset:monitoring:1.0.0")
public class DeleteMonitoringRequestMessage {

    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "urn:oio:medcom:chronicdataset:monitoring:1.0.0", required = true)
    protected String personCivilRegistrationIdentifier;
    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;

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
     *
     * @return possible object is
     * {@link String }
     */
    public String getUuidIdentifier() {
        return uuidIdentifier;
    }

    /**
     * Sets the value of the uuidIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUuidIdentifier(String value) {
        this.uuidIdentifier = value;
    }

}
