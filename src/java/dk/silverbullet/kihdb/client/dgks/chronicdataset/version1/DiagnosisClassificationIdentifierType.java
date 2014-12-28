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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DiagnosisClassificationIdentifierType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="DiagnosisClassificationIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SKS"/>
 *     &lt;enumeration value="ICD10"/>
 *     &lt;enumeration value="ICPC"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DiagnosisClassificationIdentifierType")
@XmlEnum
public enum DiagnosisClassificationIdentifierType {

    SKS("SKS"),
    @XmlEnumValue("ICD10")
    ICD_10("ICD10"),
    ICPC("ICPC"),
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    DiagnosisClassificationIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DiagnosisClassificationIdentifierType fromValue(String v) {
        for (DiagnosisClassificationIdentifierType c : DiagnosisClassificationIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
