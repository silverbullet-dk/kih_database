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

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for HealthAndPreventionProfileType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="HealthAndPreventionProfileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UpdatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PhysicalRehabilitation" type="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceType" minOccurs="0"/>
 *         &lt;element name="PatientEducation" type="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceType" minOccurs="0"/>
 *         &lt;element name="SmokingCessationCourse" type="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceType" minOccurs="0"/>
 *         &lt;element name="NutritionalCounseling" type="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionServiceType" minOccurs="0"/>
 *         &lt;element name="CurrentStatutoryProvidedService" type="{urn:oio:medcom:chronicdataset:1.0.0}StatutoryProvidedServiceType" minOccurs="0"/>
 *         &lt;element name="RecentYearStatutoryProvidedService" type="{urn:oio:medcom:chronicdataset:1.0.0}StatutoryProvidedServiceType" minOccurs="0"/>
 *         &lt;element name="UpdatedByText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthAndPreventionProfileType", propOrder = {
        "updatedDateTime",
        "physicalRehabilitation",
        "patientEducation",
        "smokingCessationCourse",
        "nutritionalCounseling",
        "currentStatutoryProvidedService",
        "recentYearStatutoryProvidedService",
        "updatedByText"
})
public class HealthAndPreventionProfileType {

    @XmlElement(name = "UpdatedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedDateTime;
    @XmlElement(name = "PhysicalRehabilitation")
    protected HealthAndPreventionServiceType physicalRehabilitation;
    @XmlElement(name = "PatientEducation")
    protected HealthAndPreventionServiceType patientEducation;
    @XmlElement(name = "SmokingCessationCourse")
    protected HealthAndPreventionServiceType smokingCessationCourse;
    @XmlElement(name = "NutritionalCounseling")
    protected HealthAndPreventionServiceType nutritionalCounseling;
    @XmlElement(name = "CurrentStatutoryProvidedService")
    protected StatutoryProvidedServiceType currentStatutoryProvidedService;
    @XmlElement(name = "RecentYearStatutoryProvidedService")
    protected StatutoryProvidedServiceType recentYearStatutoryProvidedService;
    @XmlElement(name = "UpdatedByText", required = true)
    protected String updatedByText;

    /**
     * Gets the value of the updatedDateTime property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getUpdatedDateTime() {
        return updatedDateTime;
    }

    /**
     * Sets the value of the updatedDateTime property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setUpdatedDateTime(XMLGregorianCalendar value) {
        this.updatedDateTime = value;
    }

    /**
     * Gets the value of the physicalRehabilitation property.
     *
     * @return possible object is
     * {@link HealthAndPreventionServiceType }
     */
    public HealthAndPreventionServiceType getPhysicalRehabilitation() {
        return physicalRehabilitation;
    }

    /**
     * Sets the value of the physicalRehabilitation property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionServiceType }
     */
    public void setPhysicalRehabilitation(HealthAndPreventionServiceType value) {
        this.physicalRehabilitation = value;
    }

    /**
     * Gets the value of the patientEducation property.
     *
     * @return possible object is
     * {@link HealthAndPreventionServiceType }
     */
    public HealthAndPreventionServiceType getPatientEducation() {
        return patientEducation;
    }

    /**
     * Sets the value of the patientEducation property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionServiceType }
     */
    public void setPatientEducation(HealthAndPreventionServiceType value) {
        this.patientEducation = value;
    }

    /**
     * Gets the value of the smokingCessationCourse property.
     *
     * @return possible object is
     * {@link HealthAndPreventionServiceType }
     */
    public HealthAndPreventionServiceType getSmokingCessationCourse() {
        return smokingCessationCourse;
    }

    /**
     * Sets the value of the smokingCessationCourse property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionServiceType }
     */
    public void setSmokingCessationCourse(HealthAndPreventionServiceType value) {
        this.smokingCessationCourse = value;
    }

    /**
     * Gets the value of the nutritionalCounseling property.
     *
     * @return possible object is
     * {@link HealthAndPreventionServiceType }
     */
    public HealthAndPreventionServiceType getNutritionalCounseling() {
        return nutritionalCounseling;
    }

    /**
     * Sets the value of the nutritionalCounseling property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionServiceType }
     */
    public void setNutritionalCounseling(HealthAndPreventionServiceType value) {
        this.nutritionalCounseling = value;
    }

    /**
     * Gets the value of the currentStatutoryProvidedService property.
     *
     * @return possible object is
     * {@link StatutoryProvidedServiceType }
     */
    public StatutoryProvidedServiceType getCurrentStatutoryProvidedService() {
        return currentStatutoryProvidedService;
    }

    /**
     * Sets the value of the currentStatutoryProvidedService property.
     *
     * @param value allowed object is
     *              {@link StatutoryProvidedServiceType }
     */
    public void setCurrentStatutoryProvidedService(StatutoryProvidedServiceType value) {
        this.currentStatutoryProvidedService = value;
    }

    /**
     * Gets the value of the recentYearStatutoryProvidedService property.
     *
     * @return possible object is
     * {@link StatutoryProvidedServiceType }
     */
    public StatutoryProvidedServiceType getRecentYearStatutoryProvidedService() {
        return recentYearStatutoryProvidedService;
    }

    /**
     * Sets the value of the recentYearStatutoryProvidedService property.
     *
     * @param value allowed object is
     *              {@link StatutoryProvidedServiceType }
     */
    public void setRecentYearStatutoryProvidedService(StatutoryProvidedServiceType value) {
        this.recentYearStatutoryProvidedService = value;
    }

    /**
     * Gets the value of the updatedByText property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUpdatedByText() {
        return updatedByText;
    }

    /**
     * Sets the value of the updatedByText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUpdatedByText(String value) {
        this.updatedByText = value;
    }

}
