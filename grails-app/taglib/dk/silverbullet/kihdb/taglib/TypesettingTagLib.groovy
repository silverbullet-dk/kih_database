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

class TypesettingTagLib {
    static namespace = "type"

    def pageHeading = { attrs, body ->
        out << '<div class="page-header"><h1>'
        out << body()
        out << '</h1></div>'
    }

    def navigationItem = { attrs, body ->
        out << '<li'
        if (attrs.path != null && request.forwardURI.contains(attrs.path)) {
            out << ' class="active"'
        }
        out << '><a href="'
        out << attrs.href
        out << '">'
        out << body()
        out << '</a></li>'
    }
}
