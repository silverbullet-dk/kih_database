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

package dk.silverbullet.kihdb.service.dgks.generated.monitoring.version1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AbnormalIdentifierType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AbnormalIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="to_high"/>
 *     &lt;enumeration value="to_low"/>
 *     &lt;enumeration value="abnormal"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AbnormalIdentifierType")
@XmlEnum
public enum AbnormalIdentifierType {

    @XmlEnumValue("to_high")
    TO_HIGH("to_high"),
    @XmlEnumValue("to_low")
    TO_LOW("to_low"),
    @XmlEnumValue("abnormal")
    ABNORMAL("abnormal"),
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    AbnormalIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AbnormalIdentifierType fromValue(String v) {
        for (AbnormalIdentifierType c : AbnormalIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
