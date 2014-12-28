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

package dk.silverbullet.kihdb.service.dgks.generated.monitoringdataset.version1_0_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatutoryProvidedServiceType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="StatutoryProvidedServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccordingToSocialLegislation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AccordingToHealthLegislation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ProvidedByHealthCenter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatutoryProvidedServiceType", propOrder = {
        "accordingToSocialLegislation",
        "accordingToHealthLegislation",
        "providedByHealthCenter"
})
public class StatutoryProvidedServiceType {

    @XmlElement(name = "AccordingToSocialLegislation")
    protected Boolean accordingToSocialLegislation;
    @XmlElement(name = "AccordingToHealthLegislation")
    protected Boolean accordingToHealthLegislation;
    @XmlElement(name = "ProvidedByHealthCenter")
    protected Boolean providedByHealthCenter;

    /**
     * Gets the value of the accordingToSocialLegislation property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isAccordingToSocialLegislation() {
        return accordingToSocialLegislation;
    }

    /**
     * Sets the value of the accordingToSocialLegislation property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setAccordingToSocialLegislation(Boolean value) {
        this.accordingToSocialLegislation = value;
    }

    /**
     * Gets the value of the accordingToHealthLegislation property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isAccordingToHealthLegislation() {
        return accordingToHealthLegislation;
    }

    /**
     * Sets the value of the accordingToHealthLegislation property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setAccordingToHealthLegislation(Boolean value) {
        this.accordingToHealthLegislation = value;
    }

    /**
     * Gets the value of the providedByHealthCenter property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isProvidedByHealthCenter() {
        return providedByHealthCenter;
    }

    /**
     * Sets the value of the providedByHealthCenter property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setProvidedByHealthCenter(Boolean value) {
        this.providedByHealthCenter = value;
    }

}
