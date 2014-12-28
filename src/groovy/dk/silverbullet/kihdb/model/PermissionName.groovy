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

import dk.silverbullet.kih.api.auditlog.AuditLogPermissionName

/**
 * Complete list of permissions in the system.
 *
 * Please note: When adding new permissions in this list, remember to insert a new description in the messages file!
 */
public class PermissionName {
    public static final String NONE = 'ROLE_NONE'
    // Default permission, giving no-one access. Should be the default permission on controller classes.

    public static final String WEB_LOGIN = 'ROLE_WEB_LOGIN'

    public static final String MEASUREMENT_READ = "ROLE_MEASUREMENT_READ"

    public static final String LABORATORY_READ = "ROLE_LABORATORY_READ"

    public static final String USER_DELETE = 'ROLE_USER_DELETE'
    public static final String USER_WRITE = 'ROLE_USER_WRITE'
    public static final String USER_READ = 'ROLE_USER_READ'
    public static final String USER_CREATE = 'ROLE_USER_CREATE'
    public static final String USER_READ_ALL = 'ROLE_USER_READ_ALL'

    public static final String ROLE_DELETE = 'ROLE_ROLE_DELETE'
    public static final String ROLE_WRITE = 'ROLE_ROLE_WRITE'
    public static final String ROLE_READ = 'ROLE_ROLE_READ'
    public static final String ROLE_CREATE = 'ROLE_ROLE_CREATE'
    public static final String ROLE_READ_ALL = 'ROLE_ROLE_READ_ALL'

    public static final String AUDITLOG_READ = AuditLogPermissionName.AUDITLOG_READ
    public static final String CITIZEN_READ_ALL = "ROLE_CITITIZEN_READ_ALL"
    public static final String CITIZEN_READ = "ROLE_CITIZEN_READ"
    public static final String CITIZEN_READ_SEARCH = "ROLE_CITIZEN_SEARCH"
    public static final String LAB_PRODUCER_READ = "ROLE_LAB_PRODUCER_READ"
    public static final String INSTRUMENT_READ = "ROLE_INSTRUMENT_READ"
    public static final String SELF_MONITORING_SAMPLE_READ_ALL = "ROLE_SELF_MONITORING_SAMPLE_READ_ALL"
    public static final String SELF_MONITORING_SAMPLE_READ = "ROLE_SELF_MONITORING_SAMPLE_READ"

    public static final String XDS_DOCUMENT_READ = "ROLE_XDS_DOCUMENT_READ"

    public static List<String> allValues() {
        PermissionName.fields.findAll { it.name.matches("\\p{Upper}+(_?|\\p{Upper})*") }.collect()*.get(PermissionName)
    }
}
