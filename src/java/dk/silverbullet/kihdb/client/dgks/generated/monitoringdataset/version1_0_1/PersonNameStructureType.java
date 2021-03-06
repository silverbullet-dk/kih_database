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

package dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonNameStructureType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="PersonNameStructureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}PersonGivenName"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}PersonMiddleName" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}PersonSurnameName"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonNameStructureType", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/", propOrder = {
        "personGivenName",
        "personMiddleName",
        "personSurnameName"
})
public class PersonNameStructureType {

    @XmlElement(name = "PersonGivenName", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", required = true)
    protected String personGivenName;
    @XmlElement(name = "PersonMiddleName", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/")
    protected String personMiddleName;
    @XmlElement(name = "PersonSurnameName", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", required = true)
    protected String personSurnameName;

    /**
     * Gets the value of the personGivenName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonGivenName() {
        return personGivenName;
    }

    /**
     * Sets the value of the personGivenName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonGivenName(String value) {
        this.personGivenName = value;
    }

    /**
     * Gets the value of the personMiddleName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonMiddleName() {
        return personMiddleName;
    }

    /**
     * Sets the value of the personMiddleName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonMiddleName(String value) {
        this.personMiddleName = value;
    }

    /**
     * Gets the value of the personSurnameName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPersonSurnameName() {
        return personSurnameName;
    }

    /**
     * Sets the value of the personSurnameName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPersonSurnameName(String value) {
        this.personSurnameName = value;
    }

}
