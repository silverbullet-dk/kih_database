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
package dk.silverbullet.kihdb.exception

import groovy.transform.CompileStatic
import org.springframework.validation.Errors

@CompileStatic
class ServiceStorageException extends RuntimeException {

    String message
    Errors errors

    ServiceStorageException(String message) {
        super()
        this.message = message
    }

    ServiceStorageException(String message, Errors errors) {
        super()
        this.message = message
        this.errors = errors
    }
}
