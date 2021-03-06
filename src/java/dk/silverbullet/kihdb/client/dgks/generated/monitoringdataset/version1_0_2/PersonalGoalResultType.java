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
 * <p>Java class for PersonalGoalResultType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="PersonalGoalResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AnalysisText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResultEncodingIdentifier" type="{urn:oio:medcom:chronicdataset:1.0.0}EncodingIdentifierType"/>
 *         &lt;element name="ResultOperatorIdentifier" type="{urn:oio:medcom:chronicdataset:1.0.0}OperatorIdentifierType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonalGoalResultType", propOrder = {
        "analysisText",
        "resultText",
        "resultEncodingIdentifier",
        "resultOperatorIdentifier"
})
public class PersonalGoalResultType {

    @XmlElement(name = "AnalysisText", required = true)
    protected String analysisText;
    @XmlElement(name = "ResultText", required = true)
    protected String resultText;
    @XmlElement(name = "ResultEncodingIdentifier", required = true)
    protected EncodingIdentifierType resultEncodingIdentifier;
    @XmlElement(name = "ResultOperatorIdentifier")
    protected OperatorIdentifierType resultOperatorIdentifier;

    /**
     * Gets the value of the analysisText property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAnalysisText() {
        return analysisText;
    }

    /**
     * Sets the value of the analysisText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAnalysisText(String value) {
        this.analysisText = value;
    }

    /**
     * Gets the value of the resultText property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResultText() {
        return resultText;
    }

    /**
     * Sets the value of the resultText property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResultText(String value) {
        this.resultText = value;
    }

    /**
     * Gets the value of the resultEncodingIdentifier property.
     *
     * @return possible object is
     * {@link EncodingIdentifierType }
     */
    public EncodingIdentifierType getResultEncodingIdentifier() {
        return resultEncodingIdentifier;
    }

    /**
     * Sets the value of the resultEncodingIdentifier property.
     *
     * @param value allowed object is
     *              {@link EncodingIdentifierType }
     */
    public void setResultEncodingIdentifier(EncodingIdentifierType value) {
        this.resultEncodingIdentifier = value;
    }

    /**
     * Gets the value of the resultOperatorIdentifier property.
     *
     * @return possible object is
     * {@link OperatorIdentifierType }
     */
    public OperatorIdentifierType getResultOperatorIdentifier() {
        return resultOperatorIdentifier;
    }

    /**
     * Sets the value of the resultOperatorIdentifier property.
     *
     * @param value allowed object is
     *              {@link OperatorIdentifierType }
     */
    public void setResultOperatorIdentifier(OperatorIdentifierType value) {
        this.resultOperatorIdentifier = value;
    }

}
