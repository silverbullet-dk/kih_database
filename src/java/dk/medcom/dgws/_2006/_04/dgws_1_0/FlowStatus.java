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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for flowStatus.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="flowStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="flow_running"/>
 *     &lt;enumeration value="flow_finalized_succesfully"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "flowStatus")
@XmlEnum
public enum FlowStatus {

    @XmlEnumValue("flow_running")
    FLOW_RUNNING("flow_running"),
    @XmlEnumValue("flow_finalized_succesfully")
    FLOW_FINALIZED_SUCCESFULLY("flow_finalized_succesfully");
    private final String value;

    FlowStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FlowStatus fromValue(String v) {
        for (FlowStatus c : FlowStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
