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

public enum OperatorIdentifier {
    LESS_THAN("less_than"),
    GREATER_THAN("greater_than")

    private final String value

    private OperatorIdentifier(String value) {
        this.value = value
    }

    String value() { value }

    static OperatorIdentifier fromString(String v) {
        switch (v) {
            case GREATER_THAN.value:
                return GREATER_THAN
            case LESS_THAN.value:
                return LESS_THAN
        }
    }

}