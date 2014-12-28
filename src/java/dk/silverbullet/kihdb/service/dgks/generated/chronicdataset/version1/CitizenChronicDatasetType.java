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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CitizenChronicDatasetType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CitizenChronicDatasetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Citizen"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}GeneralPractitioner" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Relative" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}ContactPersonCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Consent" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}KramPredictor" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}MedicalInvestigationCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}DiagnosisOfRelevanceCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}DiaryNoteCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}CurrentDrugEffectuationCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportOfRelevanceCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HealthAndPreventionProfile" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}HealthProfessionalNoteCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}AppointmentCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}SelfMonitoredSampleCollection" minOccurs="0"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}PersonalGoalCollection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CitizenChronicDatasetType", propOrder = {
        "citizen",
        "generalPractitioner",
        "relative",
        "contactPersonCollection",
        "consent",
        "kramPredictor",
        "medicalInvestigationCollection",
        "diagnosisOfRelevanceCollection",
        "diaryNoteCollection",
        "currentDrugEffectuationCollection",
        "laboratoryReportOfRelevanceCollection",
        "healthAndPreventionProfile",
        "healthProfessionalNoteCollection",
        "appointmentCollection",
        "selfMonitoredSampleCollection",
        "personalGoalCollection"
})
public class CitizenChronicDatasetType {

    @XmlElement(name = "Citizen", required = true)
    protected CitizenType citizen;
    @XmlElement(name = "GeneralPractitioner")
    protected GeneralPractitionerType generalPractitioner;
    @XmlElement(name = "Relative")
    protected RelativeType relative;
    @XmlElement(name = "ContactPersonCollection")
    protected ContactPersonCollectionType contactPersonCollection;
    @XmlElement(name = "Consent")
    protected ConsentType consent;
    @XmlElement(name = "KramPredictor")
    protected KramPredictorType kramPredictor;
    @XmlElement(name = "MedicalInvestigationCollection")
    protected MedicalInvestigationCollectionType medicalInvestigationCollection;
    @XmlElement(name = "DiagnosisOfRelevanceCollection")
    protected DiagnosisOfRelevanceCollectionType diagnosisOfRelevanceCollection;
    @XmlElement(name = "DiaryNoteCollection")
    protected DiaryNoteCollectionType diaryNoteCollection;
    @XmlElement(name = "CurrentDrugEffectuationCollection")
    protected CurrentDrugEffectuationCollectionType currentDrugEffectuationCollection;
    @XmlElement(name = "LaboratoryReportOfRelevanceCollection")
    protected LaboratoryReportCollectionType laboratoryReportOfRelevanceCollection;
    @XmlElement(name = "HealthAndPreventionProfile")
    protected HealthAndPreventionProfileType healthAndPreventionProfile;
    @XmlElement(name = "HealthProfessionalNoteCollection")
    protected HealthProfessionalNoteCollectionType healthProfessionalNoteCollection;
    @XmlElement(name = "AppointmentCollection")
    protected AppointmentCollectionType appointmentCollection;
    @XmlElement(name = "SelfMonitoredSampleCollection")
    protected SelfMonitoredSampleCollectionType selfMonitoredSampleCollection;
    @XmlElement(name = "PersonalGoalCollection")
    protected PersonalGoalCollectionType personalGoalCollection;

    /**
     * Gets the value of the citizen property.
     *
     * @return possible object is
     * {@link CitizenType }
     */
    public CitizenType getCitizen() {
        return citizen;
    }

    /**
     * Sets the value of the citizen property.
     *
     * @param value allowed object is
     *              {@link CitizenType }
     */
    public void setCitizen(CitizenType value) {
        this.citizen = value;
    }

    /**
     * Gets the value of the generalPractitioner property.
     *
     * @return possible object is
     * {@link GeneralPractitionerType }
     */
    public GeneralPractitionerType getGeneralPractitioner() {
        return generalPractitioner;
    }

    /**
     * Sets the value of the generalPractitioner property.
     *
     * @param value allowed object is
     *              {@link GeneralPractitionerType }
     */
    public void setGeneralPractitioner(GeneralPractitionerType value) {
        this.generalPractitioner = value;
    }

    /**
     * Gets the value of the relative property.
     *
     * @return possible object is
     * {@link RelativeType }
     */
    public RelativeType getRelative() {
        return relative;
    }

    /**
     * Sets the value of the relative property.
     *
     * @param value allowed object is
     *              {@link RelativeType }
     */
    public void setRelative(RelativeType value) {
        this.relative = value;
    }

    /**
     * Gets the value of the contactPersonCollection property.
     *
     * @return possible object is
     * {@link ContactPersonCollectionType }
     */
    public ContactPersonCollectionType getContactPersonCollection() {
        return contactPersonCollection;
    }

    /**
     * Sets the value of the contactPersonCollection property.
     *
     * @param value allowed object is
     *              {@link ContactPersonCollectionType }
     */
    public void setContactPersonCollection(ContactPersonCollectionType value) {
        this.contactPersonCollection = value;
    }

    /**
     * Gets the value of the consent property.
     *
     * @return possible object is
     * {@link ConsentType }
     */
    public ConsentType getConsent() {
        return consent;
    }

    /**
     * Sets the value of the consent property.
     *
     * @param value allowed object is
     *              {@link ConsentType }
     */
    public void setConsent(ConsentType value) {
        this.consent = value;
    }

    /**
     * Gets the value of the kramPredictor property.
     *
     * @return possible object is
     * {@link KramPredictorType }
     */
    public KramPredictorType getKramPredictor() {
        return kramPredictor;
    }

    /**
     * Sets the value of the kramPredictor property.
     *
     * @param value allowed object is
     *              {@link KramPredictorType }
     */
    public void setKramPredictor(KramPredictorType value) {
        this.kramPredictor = value;
    }

    /**
     * Gets the value of the medicalInvestigationCollection property.
     *
     * @return possible object is
     * {@link MedicalInvestigationCollectionType }
     */
    public MedicalInvestigationCollectionType getMedicalInvestigationCollection() {
        return medicalInvestigationCollection;
    }

    /**
     * Sets the value of the medicalInvestigationCollection property.
     *
     * @param value allowed object is
     *              {@link MedicalInvestigationCollectionType }
     */
    public void setMedicalInvestigationCollection(MedicalInvestigationCollectionType value) {
        this.medicalInvestigationCollection = value;
    }

    /**
     * Gets the value of the diagnosisOfRelevanceCollection property.
     *
     * @return possible object is
     * {@link DiagnosisOfRelevanceCollectionType }
     */
    public DiagnosisOfRelevanceCollectionType getDiagnosisOfRelevanceCollection() {
        return diagnosisOfRelevanceCollection;
    }

    /**
     * Sets the value of the diagnosisOfRelevanceCollection property.
     *
     * @param value allowed object is
     *              {@link DiagnosisOfRelevanceCollectionType }
     */
    public void setDiagnosisOfRelevanceCollection(DiagnosisOfRelevanceCollectionType value) {
        this.diagnosisOfRelevanceCollection = value;
    }

    /**
     * Gets the value of the diaryNoteCollection property.
     *
     * @return possible object is
     * {@link DiaryNoteCollectionType }
     */
    public DiaryNoteCollectionType getDiaryNoteCollection() {
        return diaryNoteCollection;
    }

    /**
     * Sets the value of the diaryNoteCollection property.
     *
     * @param value allowed object is
     *              {@link DiaryNoteCollectionType }
     */
    public void setDiaryNoteCollection(DiaryNoteCollectionType value) {
        this.diaryNoteCollection = value;
    }

    /**
     * Gets the value of the currentDrugEffectuationCollection property.
     *
     * @return possible object is
     * {@link CurrentDrugEffectuationCollectionType }
     */
    public CurrentDrugEffectuationCollectionType getCurrentDrugEffectuationCollection() {
        return currentDrugEffectuationCollection;
    }

    /**
     * Sets the value of the currentDrugEffectuationCollection property.
     *
     * @param value allowed object is
     *              {@link CurrentDrugEffectuationCollectionType }
     */
    public void setCurrentDrugEffectuationCollection(CurrentDrugEffectuationCollectionType value) {
        this.currentDrugEffectuationCollection = value;
    }

    /**
     * Gets the value of the laboratoryReportOfRelevanceCollection property.
     *
     * @return possible object is
     * {@link LaboratoryReportCollectionType }
     */
    public LaboratoryReportCollectionType getLaboratoryReportOfRelevanceCollection() {
        return laboratoryReportOfRelevanceCollection;
    }

    /**
     * Sets the value of the laboratoryReportOfRelevanceCollection property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportCollectionType }
     */
    public void setLaboratoryReportOfRelevanceCollection(LaboratoryReportCollectionType value) {
        this.laboratoryReportOfRelevanceCollection = value;
    }

    /**
     * Gets the value of the healthAndPreventionProfile property.
     *
     * @return possible object is
     * {@link HealthAndPreventionProfileType }
     */
    public HealthAndPreventionProfileType getHealthAndPreventionProfile() {
        return healthAndPreventionProfile;
    }

    /**
     * Sets the value of the healthAndPreventionProfile property.
     *
     * @param value allowed object is
     *              {@link HealthAndPreventionProfileType }
     */
    public void setHealthAndPreventionProfile(HealthAndPreventionProfileType value) {
        this.healthAndPreventionProfile = value;
    }

    /**
     * Gets the value of the healthProfessionalNoteCollection property.
     *
     * @return possible object is
     * {@link HealthProfessionalNoteCollectionType }
     */
    public HealthProfessionalNoteCollectionType getHealthProfessionalNoteCollection() {
        return healthProfessionalNoteCollection;
    }

    /**
     * Sets the value of the healthProfessionalNoteCollection property.
     *
     * @param value allowed object is
     *              {@link HealthProfessionalNoteCollectionType }
     */
    public void setHealthProfessionalNoteCollection(HealthProfessionalNoteCollectionType value) {
        this.healthProfessionalNoteCollection = value;
    }

    /**
     * Gets the value of the appointmentCollection property.
     *
     * @return possible object is
     * {@link AppointmentCollectionType }
     */
    public AppointmentCollectionType getAppointmentCollection() {
        return appointmentCollection;
    }

    /**
     * Sets the value of the appointmentCollection property.
     *
     * @param value allowed object is
     *              {@link AppointmentCollectionType }
     */
    public void setAppointmentCollection(AppointmentCollectionType value) {
        this.appointmentCollection = value;
    }

    /**
     * Gets the value of the selfMonitoredSampleCollection property.
     *
     * @return possible object is
     * {@link SelfMonitoredSampleCollectionType }
     */
    public SelfMonitoredSampleCollectionType getSelfMonitoredSampleCollection() {
        return selfMonitoredSampleCollection;
    }

    /**
     * Sets the value of the selfMonitoredSampleCollection property.
     *
     * @param value allowed object is
     *              {@link SelfMonitoredSampleCollectionType }
     */
    public void setSelfMonitoredSampleCollection(SelfMonitoredSampleCollectionType value) {
        this.selfMonitoredSampleCollection = value;
    }

    /**
     * Gets the value of the personalGoalCollection property.
     *
     * @return possible object is
     * {@link PersonalGoalCollectionType }
     */
    public PersonalGoalCollectionType getPersonalGoalCollection() {
        return personalGoalCollection;
    }

    /**
     * Sets the value of the personalGoalCollection property.
     *
     * @param value allowed object is
     *              {@link PersonalGoalCollectionType }
     */
    public void setPersonalGoalCollection(PersonalGoalCollectionType value) {
        this.personalGoalCollection = value;
    }

}
