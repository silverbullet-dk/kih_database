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

public class SQLServerDialect extends org.hibernate.dialect.SQLServer2008Dialect {

    /**
     * Initializes a new instance of the {@link SQLServerDialect} class.
     */
    public SQLServerDialect() {
        //        registerColumnType(Types.BIGINT, "bigint");
//        registerColumnType(Types.BIT, "bit");
//        registerColumnType(Types.CHAR, "nchar(1)");
//        registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, 255, "nvarchar(1024)");
        registerColumnType(Types.VARBINARY, 4000, "varbinary($1)");
        registerColumnType(Types.VARBINARY, 255, "varbinary(max)");
        registerColumnType(Types.BLOB, "varbinary(max)");
        registerColumnType(Types.CLOB, "varchar(max)");

    }
}


