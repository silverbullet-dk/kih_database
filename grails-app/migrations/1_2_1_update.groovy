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
databaseChangeLog = {

    changeSet(author: "kim (generated)", id: "1412331460373-27") {
        createTable(tableName: "statistic") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "statisticPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "creator", type: '${string.type}') {
                constraints(nullable: "false")
            }
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "statistic_time", type: '${datetime.type}') {
                constraints(nullable: "false")
            }
            column(name: "statistic_type", type: '${string.type}') {
                constraints(nullable: "false")
            }
            column(name: "value", type: '${integer.type}') {
                constraints(nullable: "false")
            }
        }
    }
}
