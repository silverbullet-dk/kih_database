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
 *         &lt;element name="Since" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="MaximumReturnedMonitorering" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DataSegmentList" type="{urn:oio:medcom:chronicdataset:all:1.0.0}DataSegmentListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "personCivilRegistrationIdentifier",
        "since",
        "maximumReturnedMonitorering",
        "dataSegmentList"
})
@XmlRootElement(name = "GetChronicDatasetRequestMessage", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
public class GetChronicDatasetRequestMessage {

    @XmlElement(name = "PersonCivilRegistrationIdentifier", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0", required = true)
    protected String personCivilRegistrationIdentifier;
    @XmlElement(name = "Since", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar since;
    @XmlElement(name = "MaximumReturnedMonitorering", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
    protected BigInteger maximumReturnedMonitorering;
    @XmlElement(name = "DataSegmentList", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
    protected DataSegmentListType dataSegmentList;

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
     * Gets the value of the since property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getSince() {
        return since;
    }

    /**
     * Sets the value of the since property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setSince(XMLGregorianCalendar value) {
        this.since = value;
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

    /**
     * Gets the value of the dataSegmentList property.
     *
     * @return possible object is
     * {@link DataSegmentListType }
     */
    public DataSegmentListType getDataSegmentList() {
        return dataSegmentList;
    }

    /**
     * Sets the value of the dataSegmentList property.
     *
     * @param value allowed object is
     *              {@link DataSegmentListType }
     */
    public void setDataSegmentList(DataSegmentListType value) {
        this.dataSegmentList = value;
    }

}
