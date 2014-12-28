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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ConsentType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ConsentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConsentDeclaredDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ConsentGivenIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsentType", propOrder = {
        "consentDeclaredDateTime",
        "consentGivenIndicator"
})
public class ConsentType {

    @XmlElement(name = "ConsentDeclaredDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consentDeclaredDateTime;
    @XmlElement(name = "ConsentGivenIndicator")
    protected boolean consentGivenIndicator;

    /**
     * Gets the value of the consentDeclaredDateTime property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getConsentDeclaredDateTime() {
        return consentDeclaredDateTime;
    }

    /**
     * Sets the value of the consentDeclaredDateTime property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setConsentDeclaredDateTime(XMLGregorianCalendar value) {
        this.consentDeclaredDateTime = value;
    }

    /**
     * Gets the value of the consentGivenIndicator property.
     */
    public boolean isConsentGivenIndicator() {
        return consentGivenIndicator;
    }

    /**
     * Sets the value of the consentGivenIndicator property.
     */
    public void setConsentGivenIndicator(boolean value) {
        this.consentGivenIndicator = value;
    }

}
