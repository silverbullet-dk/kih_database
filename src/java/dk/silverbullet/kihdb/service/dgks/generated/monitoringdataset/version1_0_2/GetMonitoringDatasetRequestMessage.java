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

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;


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
 *         &lt;element name="FromDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ToDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="MaximumReturnedMonitorering" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "personCivilRegistrationIdentifier",
        "fromDate",
        "toDate",
        "maximumReturnedMonitorering"
})
@XmlRootElement(name = "GetMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class GetMonitoringDatasetRequestMessage {

    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected String personCivilRegistrationIdentifier;
    @XmlElement(name = "FromDate", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fromDate;
    @XmlElement(name = "ToDate", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar toDate;
    @XmlElement(name = "MaximumReturnedMonitorering", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
    protected BigInteger maximumReturnedMonitorering;

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
     * Gets the value of the fromDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setFromDate(XMLGregorianCalendar value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setToDate(XMLGregorianCalendar value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the maximumReturnedMonitorering property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getMaximumReturnedMonitorering() {
        return maximumReturnedMonitorering;
    }

    /**
     * Sets the value of the maximumReturnedMonitorering property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setMaximumReturnedMonitorering(BigInteger value) {
        this.maximumReturnedMonitorering = value;
    }

}
