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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ContactPersonType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ContactPersonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ContactPersonDetailsText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CreatedByText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactPersonType", propOrder = {
        "uuidIdentifier",
        "createdDateTime",
        "contactPersonDetailsText",
        "createdByText"
})
public class ContactPersonType {

    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "ContactPersonDetailsText", required = true)
    protected String contactPersonDetailsText;
    @XmlElement(name = "CreatedByText", required = true)
    protected String createdByText;

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

    /**
     * Gets the value of the createdDateTime property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Sets the value of the createdDateTime property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setCreatedDateTime(XMLGregorianCalendar value) {
        this.createdDateTime = value;
    }

    /**
     * Gets the value of the contactPersonDetailsText property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContactPersonDetailsText() {
        return contactPersonDetailsText;
    }

    /**
     * Sets the value of the contactPersonDetailsText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContactPersonDetailsText(String value) {
        this.contactPersonDetailsText = value;
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
