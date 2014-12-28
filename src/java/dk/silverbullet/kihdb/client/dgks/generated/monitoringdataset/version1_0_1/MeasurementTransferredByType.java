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
 * <p>Java class for MeasurementTransferredByType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="MeasurementTransferredByType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="automatic"/>
 *     &lt;enumeration value="typed"/>
 *     &lt;enumeration value="typedbyhcprof"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "MeasurementTransferredByType", namespace = "urn:oio:medcom:chronicdataset:1.0.1")
@XmlEnum
public enum MeasurementTransferredByType {

    @XmlEnumValue("automatic")
    AUTOMATIC("automatic"),
    @XmlEnumValue("typed")
    TYPED("typed"),
    @XmlEnumValue("typedbyhcprof")
    TYPEDBYHCPROF("typedbyhcprof");
    private final String value;

    MeasurementTransferredByType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MeasurementTransferredByType fromValue(String v) {
        for (MeasurementTransferredByType c : MeasurementTransferredByType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
