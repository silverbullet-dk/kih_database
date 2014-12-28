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
 *         &lt;element name="FlowID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InResponseToMessageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "flowID",
        "messageID",
        "inResponseToMessageID"
})
@XmlRootElement(name = "Linking")
public class Linking {

    @XmlElement(name = "FlowID")
    protected String flowID;
    @XmlElement(name = "MessageID", required = true)
    protected String messageID;
    @XmlElement(name = "InResponseToMessageID")
    protected String inResponseToMessageID;

    /**
     * Gets the value of the flowID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFlowID() {
        return flowID;
    }

    /**
     * Sets the value of the flowID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFlowID(String value) {
        this.flowID = value;
    }

    /**
     * Gets the value of the messageID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the inResponseToMessageID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInResponseToMessageID() {
        return inResponseToMessageID;
    }

    /**
     * Sets the value of the inResponseToMessageID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInResponseToMessageID(String value) {
        this.inResponseToMessageID = value;
    }

}
