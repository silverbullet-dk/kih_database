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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InstrumentType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="InstrumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MedComID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProductType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SoftwareVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstrumentType", namespace = "urn:oio:medcom:chronicdataset:1.0.1", propOrder = {
        "medComID",
        "manufacturer",
        "productType",
        "model",
        "softwareVersion"
})
public class InstrumentType {

    @XmlElement(name = "MedComID")
    protected String medComID;
    @XmlElement(name = "Manufacturer")
    protected String manufacturer;
    @XmlElement(name = "ProductType")
    protected String productType;
    @XmlElement(name = "Model")
    protected String model;
    @XmlElement(name = "SoftwareVersion")
    protected String softwareVersion;

    /**
     * Gets the value of the medComID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMedComID() {
        return medComID;
    }

    /**
     * Sets the value of the medComID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMedComID(String value) {
        this.medComID = value;
    }

    /**
     * Gets the value of the manufacturer property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the productType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the model property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the softwareVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * Sets the value of the softwareVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSoftwareVersion(String value) {
        this.softwareVersion = value;
    }

}
