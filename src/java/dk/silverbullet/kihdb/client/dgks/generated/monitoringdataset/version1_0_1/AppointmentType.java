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

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AppointmentType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="AppointmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}UuidIdentifier"/>
 *         &lt;element name="CreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TitleIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DescriptionFormattedText" type="{urn:oio:medcom:chronicdataset:1.0.0}FormattedTextType"/>
 *         &lt;element name="CreatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppointmentType", propOrder = {
        "uuidIdentifier",
        "createdDateTime",
        "titleIdentifier",
        "descriptionFormattedText",
        "createdBy"
})
public class AppointmentType {

    @XmlElement(name = "UuidIdentifier", required = true)
    protected String uuidIdentifier;
    @XmlElement(name = "CreatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDateTime;
    @XmlElement(name = "TitleIdentifier", required = true)
    protected String titleIdentifier;
    @XmlElement(name = "DescriptionFormattedText", required = true)
    protected FormattedTextType descriptionFormattedText;
    @XmlElement(name = "CreatedBy", required = true)
    protected String createdBy;

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
     * Gets the value of the titleIdentifier property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    /**
     * Sets the value of the titleIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitleIdentifier(String value) {
        this.titleIdentifier = value;
    }

    /**
     * Gets the value of the descriptionFormattedText property.
     *
     * @return possible object is
     * {@link FormattedTextType }
     */
    public FormattedTextType getDescriptionFormattedText() {
        return descriptionFormattedText;
    }

    /**
     * Sets the value of the descriptionFormattedText property.
     *
     * @param value allowed object is
     *              {@link FormattedTextType }
     */
    public void setDescriptionFormattedText(FormattedTextType value) {
        this.descriptionFormattedText = value;
    }

    /**
     * Gets the value of the createdBy property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

}
