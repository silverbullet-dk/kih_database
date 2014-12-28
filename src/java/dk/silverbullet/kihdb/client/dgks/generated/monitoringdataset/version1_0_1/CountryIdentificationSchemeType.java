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

package dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for _CountryIdentificationSchemeType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="_CountryIdentificationSchemeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="iso3166-alpha2"/>
 *     &lt;enumeration value="iso3166-alpha3"/>
 *     &lt;enumeration value="un-numeric3"/>
 *     &lt;enumeration value="imk"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "_CountryIdentificationSchemeType", namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/")
@XmlEnum
public enum CountryIdentificationSchemeType {


    /**
     * Dette format følge ISO 3166 standarden, alpha 2.
     */
    @XmlEnumValue("iso3166-alpha2")
    ISO_3166_ALPHA_2("iso3166-alpha2"),

    /**
     * Dette format følge ISO 3166 standarden, alpha 3.
     */
    @XmlEnumValue("iso3166-alpha3")
    ISO_3166_ALPHA_3("iso3166-alpha3"),

    /**
     * Dette format følger FNs Statistik Kontor landekoder
     */
    @XmlEnumValue("un-numeric3")
    UN_NUMERIC_3("un-numeric3"),

    /**
     * Dette format følger MyndighedsKoden fra Det Centrale Personregister
     */
    @XmlEnumValue("imk")
    IMK("imk");
    private final String value;

    CountryIdentificationSchemeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CountryIdentificationSchemeType fromValue(String v) {
        for (CountryIdentificationSchemeType c : CountryIdentificationSchemeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
