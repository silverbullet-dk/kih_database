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
package dk.medcom.dgws._2006._04.dgws_1_0;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Java class for header complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="header">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecurityLevel" type="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}securityLevel" minOccurs="0"/>
 *         &lt;element name="TimeOut" type="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}timeout" minOccurs="0"/>
 *         &lt;element ref="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}Linking"/>
 *         &lt;element name="FlowStatus" type="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}flowStatus" minOccurs="0"/>
 *         &lt;element name="Priority" type="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}priority" minOccurs="0"/>
 *         &lt;element name="RequireNonRepudiationReceipt" type="{http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd}yesNo" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='skip' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "header", propOrder = {
        "securityLevel",
        "timeOut",
        "linking",
        "flowStatus",
        "priority",
        "requireNonRepudiationReceipt"
})
public class Header {

    @XmlElement(name = "SecurityLevel")
    protected String securityLevel;
    @XmlElement(name = "TimeOut")
    protected String timeOut;
    @XmlElement(name = "Linking", required = true)
    protected Linking linking;
    @XmlElement(name = "FlowStatus")
    protected FlowStatus flowStatus;
    @XmlElement(name = "Priority")
    protected Priority priority;
    @XmlElement(name = "RequireNonRepudiationReceipt")
    protected YesNo requireNonRepudiationReceipt;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the securityLevel property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSecurityLevel() {
        return securityLevel;
    }

    /**
     * Sets the value of the securityLevel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSecurityLevel(String value) {
        this.securityLevel = value;
    }

    /**
     * Gets the value of the timeOut property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     * Sets the value of the timeOut property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTimeOut(String value) {
        this.timeOut = value;
    }

    /**
     * Gets the value of the linking property.
     *
     * @return possible object is
     * {@link Linking }
     */
    public Linking getLinking() {
        return linking;
    }

    /**
     * Sets the value of the linking property.
     *
     * @param value allowed object is
     *              {@link Linking }
     */
    public void setLinking(Linking value) {
        this.linking = value;
    }

    /**
     * Gets the value of the flowStatus property.
     *
     * @return possible object is
     * {@link FlowStatus }
     */
    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

    /**
     * Sets the value of the flowStatus property.
     *
     * @param value allowed object is
     *              {@link FlowStatus }
     */
    public void setFlowStatus(FlowStatus value) {
        this.flowStatus = value;
    }

    /**
     * Gets the value of the priority property.
     *
     * @return possible object is
     * {@link Priority }
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     *
     * @param value allowed object is
     *              {@link Priority }
     */
    public void setPriority(Priority value) {
        this.priority = value;
    }

    /**
     * Gets the value of the requireNonRepudiationReceipt property.
     *
     * @return possible object is
     * {@link YesNo }
     */
    public YesNo getRequireNonRepudiationReceipt() {
        return requireNonRepudiationReceipt;
    }

    /**
     * Sets the value of the requireNonRepudiationReceipt property.
     *
     * @param value allowed object is
     *              {@link YesNo }
     */
    public void setRequireNonRepudiationReceipt(YesNo value) {
        this.requireNonRepudiationReceipt = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * <p/>
     * <p/>
     * the map is keyed by the name of the attribute and
     * the value is the string value of the attribute.
     * <p/>
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
