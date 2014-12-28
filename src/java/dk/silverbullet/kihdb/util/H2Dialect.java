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
package dk.silverbullet.kihdb.util;


import java.sql.Types;

public class H2Dialect extends org.hibernate.dialect.H2Dialect {

    /**
     * Initializes a new instance of the {@link H2Dialect} class.
     */
    public H2Dialect() {
        registerColumnType(Types.VARBINARY, 255, "blob");
        registerColumnType(Types.VARCHAR, 255, "varchar(512)"); // Hack: Seems Grails desperately maps strings to 255 chars pr. default
    }
}
