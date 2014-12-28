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
 * <p>Java class for KramPredictorType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="KramPredictorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Weight" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Height" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Smoking" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Alcohol" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *         &lt;element name="Exercise" type="{urn:oio:medcom:chronicdataset:1.0.0}LaboratoryReportType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KramPredictorType", propOrder = {
        "weight",
        "height",
        "smoking",
        "alcohol",
        "exercise"
})
public class KramPredictorType {

    @XmlElement(name = "Weight")
    protected LaboratoryReportType weight;
    @XmlElement(name = "Height")
    protected LaboratoryReportType height;
    @XmlElement(name = "Smoking")
    protected LaboratoryReportType smoking;
    @XmlElement(name = "Alcohol")
    protected LaboratoryReportType alcohol;
    @XmlElement(name = "Exercise")
    protected LaboratoryReportType exercise;

    /**
     * Gets the value of the weight property.
     *
     * @return possible object is
     * {@link LaboratoryReportType }
     */
    public LaboratoryReportType getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportType }
     */
    public void setWeight(LaboratoryReportType value) {
        this.weight = value;
    }

    /**
     * Gets the value of the height property.
     *
     * @return possible object is
     * {@link LaboratoryReportType }
     */
    public LaboratoryReportType getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportType }
     */
    public void setHeight(LaboratoryReportType value) {
        this.height = value;
    }

    /**
     * Gets the value of the smoking property.
     *
     * @return possible object is
     * {@link LaboratoryReportType }
     */
    public LaboratoryReportType getSmoking() {
        return smoking;
    }

    /**
     * Sets the value of the smoking property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportType }
     */
    public void setSmoking(LaboratoryReportType value) {
        this.smoking = value;
    }

    /**
     * Gets the value of the alcohol property.
     *
     * @return possible object is
     * {@link LaboratoryReportType }
     */
    public LaboratoryReportType getAlcohol() {
        return alcohol;
    }

    /**
     * Sets the value of the alcohol property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportType }
     */
    public void setAlcohol(LaboratoryReportType value) {
        this.alcohol = value;
    }

    /**
     * Gets the value of the exercise property.
     *
     * @return possible object is
     * {@link LaboratoryReportType }
     */
    public LaboratoryReportType getExercise() {
        return exercise;
    }

    /**
     * Sets the value of the exercise property.
     *
     * @param value allowed object is
     *              {@link LaboratoryReportType }
     */
    public void setExercise(LaboratoryReportType value) {
        this.exercise = value;
    }

}
