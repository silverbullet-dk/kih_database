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
package dk.silverbullet.kihdb.enums

public enum MeasurementTransferredBy {

    AUTOMATIC("automatic"),
    TYPED("typed"),
    TYPEDBYHCPROF("typedbyhcprof")

    private final String value

    private MeasurementTransferredBy(String value) {
        this.value = value
    }

    String value() { value }

    static MeasurementTransferredBy fromString(String v) {
        if (!v) {
            return null
        }
        switch (v) {
            case AUTOMATIC.value:
                return AUTOMATIC
            case TYPED.value:
                return TYPED
            case TYPEDBYHCPROF.value:
                return TYPEDBYHCPROF
        }
    }
}
