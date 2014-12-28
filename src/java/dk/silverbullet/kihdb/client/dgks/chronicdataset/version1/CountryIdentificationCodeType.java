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


/**
 * Landeidentifikations kode baseret på de 4 forskellige formater.
 * <p/>
 * <p>Java class for CountryIdentificationCodeType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CountryIdentificationCodeType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/>_CountryIdentificationCodeType">
 *       &lt;attribute name="scheme" use="required" type="{http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/}_CountryIdentificationSchemeType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CountryIdentificationCodeType", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", propOrder = {
        "value"
})
public class CountryIdentificationCodeType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "scheme", required = true)
    protected CountryIdentificationSchemeType scheme;

    /**
     * Dette er en støttetype til CountryIdentificationCodeType. Det regulære udtryk er et valg for de 4 forskellige regulære udtryk for de forskellige formater. ISO 3166 standard, alpha 2: [a-z,A-Z]{2}. Eksempel "DK" for Danmark. ISO 3166 standard, alpha 3:  [a-z,A-Z]{3}. Eksempel "DKN" for Danmark. UN Statistics Divisions country codes: [0-9]{3}. Eksempel "208" for Danmark AuthorityCode from the Central Office of Civil Registration: [0-9]{4}. Eksempel "5100" for Danmark.
     *
     * @return possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the scheme property.
     *
     * @return possible object is
     * {@link CountryIdentificationSchemeType }
     */
    public CountryIdentificationSchemeType getScheme() {
        return scheme;
    }

    /**
     * Sets the value of the scheme property.
     *
     * @param value allowed object is
     *              {@link CountryIdentificationSchemeType }
     */
    public void setScheme(CountryIdentificationSchemeType value) {
        this.scheme = value;
    }

}
