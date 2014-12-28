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
package dk.silverbullet.kihdb.taglib

class FormattingTagLib {

    // KIHDB Graph Tag
    static namespace = "otformat"

    def formatCPR = { attrs, body ->
        def cpr = attrs.message
        out << cpr[0..5] + "-" + cpr[6..9]
    }

    def formatPhoneNumber = { attrs, body ->
        def digits = attrs.message
        def hasPlus = false
        if (digits.size() > 0 && digits[0] == "+") {
            digits = attrs.message[1..attrs.message.length() - 1]
            hasPlus = true
        }

        StringBuilder b = new StringBuilder();
        if (hasPlus) {
            b.append("+")
        }
        for (int i = 1; i < digits.size(); i += 2) {
            b.append(digits[i - 1])
            b.append(digits[i])
            b.append(" ")
        }

        out << b.toString()
    }
}
