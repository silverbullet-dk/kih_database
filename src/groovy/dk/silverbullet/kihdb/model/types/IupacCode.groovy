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
package dk.silverbullet.kihdb.model.types


public enum IupacCode {

    FEV1("MCS88015"),
    FVC("MCS88016"),
    FEV1_FVC("MCS88017"),
    //FEV_PRC("MCS88023"),
            //Ã…NDENÃ˜D_MRC("MCS88021"),
            //Ã…NDENÃ˜D_NYHA("MCS88032"),
            SATURATION("NPU03011"),
    //EXACERBATIONER("MCS88022"),
            SYSTOLIC_CLINIC("DNK05472"),
    DIASTOLIC_CLINIC("DNK05473"),
    SYSTOLIC_HOME("MCS88019"),
    DIASTOLIC_HOME("MCS88020"),
    PULSE("NPU21692"),
    //HBA1C("NPU27412"),
            BLOODSUGAR("NPU02187"),
    //SAMLET_KOLESTEROL("NPU01566"),
            //HDL("NPU01567"),
            //LDL("NPU1568"),
            //TRIGLYCERID("NPU04094"),
            WEIGHT("NPU03804"),
    //LIVVIDDE("MCS88018"),
            //HÃ˜JDE("NPU03794"),
            //BMI("NPU27281"),
            //SKRIDT-TÃ†LLER("unspecified_1"),
            //MOTION("MCS88001"),
            TEMPERATURE("NPU08676"),
    URINE("NPU03958"), // PROTEIN in urine // Simens multistix 5 (RM): NPU03958, Siemens uristix 2857 (RH): NPU04206
    URINE_GLUCOSE("NPU03936"),
    //Ã˜DEM_GRAD("unspecified_2"),
            //FOSTER_AKTIVIET("unspecified_3")
            CRP("NPU01423")

    private final String value

    IupacCode(String value) {
        this.value = value
    }

    String value() {
        value
    }

    // Missing types:
    // HEMOGLOBIN

    public static IupacCode fromValue(String v) {
        for (IupacCode c : IupacCode.values()) {
            if (c.name().equals(v)) {
                return c
            }
        }
        return null
    }

}

