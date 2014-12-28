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
package dk.silverbullet.kihdb.model

import org.springframework.security.core.context.SecurityContextHolder

abstract class AbstractObject implements Serializable {

    Date createdDate
    Date modifiedDate
    String createdBy
    String modifiedBy


    static constraints = {
        createdDate nullable: true
        createdBy nullable: true
        modifiedBy nullable: true
        modifiedDate nullable: true
    }

    def beforeUpdate() {
        this.modifiedBy = SecurityContextHolder.context.authentication?.name ?: "Unknown"
        this.modifiedDate = new Date()
    }

    def beforeInsert() {
        Date now = new Date()
        this.createdDate = now
        this.createdBy = SecurityContextHolder.context.authentication?.name ?: "Unknown"
        this.modifiedBy = createdBy
        this.modifiedDate = now
    }

}