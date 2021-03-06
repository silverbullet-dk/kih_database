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
 * <p>Java class for HealthAndPreventionServiceStatusType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="HealthAndPreventionServiceStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="offered"/>
 *     &lt;enumeration value="finish"/>
 *     &lt;enumeration value="not_relevant"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "HealthAndPreventionServiceStatusType")
@XmlEnum
public enum HealthAndPreventionServiceStatusType {

    @XmlEnumValue("offered")
    OFFERED("offered"),
    @XmlEnumValue("finish")
    FINISH("finish"),
    @XmlEnumValue("not_relevant")
    NOT_RELEVANT("not_relevant");
    private final String value;

    HealthAndPreventionServiceStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HealthAndPreventionServiceStatusType fromValue(String v) {
        for (HealthAndPreventionServiceStatusType c : HealthAndPreventionServiceStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
