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
package dk.silverbullet.kihdb.model.xds

import dk.silverbullet.kihdb.model.AbstractObject

class XDSDocument extends AbstractObject {

    static belongsTo = [metadata: XDSMetadata]

    String uuid
    String mimeType

    // document must ALWAYS by base64Encoded
    Serializable document

    static mapping = {
        document type: 'serializable'
    }

    static constraints = {
        uuid nullable: false, unique: true
        mimeType nullable: true
        document nullable: false, maxSize: 1024 * 1024 * 5 //
        metadata nullable: true
    }
}
