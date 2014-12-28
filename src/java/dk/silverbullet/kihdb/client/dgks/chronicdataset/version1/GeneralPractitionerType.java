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

package dk.silverbullet.kihdb.client.dgks.chronicdataset.version1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneralPractitionerType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="GeneralPractitionerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}MedicalPracticeIdentifier"/>
 *         &lt;element name="MedicalPracticeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/}PersonNameStructure" minOccurs="0"/>
 *         &lt;element ref="{http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/}AddressPostal" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}PhoneNumberIdentifier" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}EmailAddressIdentifier" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneralPractitionerType", propOrder = {
        "medicalPracticeIdentifier",
        "medicalPracticeName",
        "personNameStructure",
        "addressPostal",
        "phoneNumberIdentifier",
        "emailAddressIdentifier"
})
public class GeneralPractitionerType {

    @XmlElement(name = "MedicalPracticeIdentifier")
    protected int medicalPracticeIdentifier;
    @XmlElement(name = "MedicalPracticeName", required = true)
    protected String medicalPracticeName;
    @XmlElement(name = "PersonNameStructure", namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/")
    protected PersonNameStructureType personNameStructure;
    @XmlElement(name = "AddressPostal", namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/")
    protected AddressPostalType addressPostal;
    @XmlElement(name = "PhoneNumberIdentifier")
    protected String phoneNumberIdentifier;
    @XmlElement(name = "EmailAddressIdentifier")
    protected String emailAddressIdentifier;

    /**
     * Gets the value of the medicalPracticeIdentifier property.
     */
    public int getMedicalPracticeIdentifier() {
        return medicalPracticeIdentifier;
    }

    /**
     * Sets the value of the medicalPracticeIdentifier property.
     */
    public void setMedicalPracticeIdentifier(int value) {
        this.medicalPracticeIdentifier = value;
    }

    /**
     * Gets the value of the medicalPracticeName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMedicalPracticeName() {
        return medicalPracticeName;
    }

    /**
     * Sets the value of the medicalPracticeName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMedicalPracticeName(String value) {
        this.medicalPracticeName = value;
    }

    /**
     * Gets the value of the personNameStructure property.
     *
     * @return possible object is
     * {@link PersonNameStructureType }
     */
    public PersonNameStructureType getPersonNameStructure() {
        return personNameStructure;
    }

    /**
     * Sets the value of the personNameStructure property.
     *
     * @param value allowed object is
     *              {@link PersonNameStructureType }
     */
    public void setPersonNameStructure(PersonNameStructureType value) {
        this.personNameStructure = value;
    }

    /**
     * Gets the value of the addressPostal property.
     *
     * @return possible object is
     * {@link AddressPostalType }
     */
    public AddressPostalType getAddressPostal() {
        return addressPostal;
    }

    /**
     * Sets the value of the addressPostal property.
     *
     * @param value allowed object is
     *              {@link AddressPostalType }
     */
    public void setAddressPostal(AddressPostalType value) {
        this.addressPostal = value;
    }

    /**
     * Gets the value of the phoneNumberIdentifier property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhoneNumberIdentifier() {
        return phoneNumberIdentifier;
    }

    /**
     * Sets the value of the phoneNumberIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhoneNumberIdentifier(String value) {
        this.phoneNumberIdentifier = value;
    }

    /**
     * Gets the value of the emailAddressIdentifier property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmailAddressIdentifier() {
        return emailAddressIdentifier;
    }

    /**
     * Sets the value of the emailAddressIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailAddressIdentifier(String value) {
        this.emailAddressIdentifier = value;
    }

}
