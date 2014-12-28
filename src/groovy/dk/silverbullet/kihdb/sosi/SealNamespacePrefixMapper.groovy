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
package dk.silverbullet.kihdb.sosi

import com.sun.xml.bind.marshaller.NamespacePrefixMapper

class SealNamespacePrefixMapper extends NamespacePrefixMapper {


    private Map<String, String> prefixMap = new HashMap<String, String>();

    {
        // Essential for seal validation
        prefixMap.put("urn:oasis:names:tc:SAML:2.0:assertion", "saml");
        prefixMap.put("http://www.w3.org/2000/09/xmldsig#", "ds");

        // Non essential but pretty.
        prefixMap.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
        prefixMap.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
        prefixMap.put("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "medcom");
    }

    /**
     * Returns the preferred namespace prefix for the supplied uri
     */
    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requiredPrefix) {
        if (prefixMap.containsKey(namespaceUri)) {
            return prefixMap.get(namespaceUri);
        }
        return suggestion;
    }

}
