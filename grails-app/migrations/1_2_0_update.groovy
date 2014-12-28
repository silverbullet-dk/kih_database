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

    changeSet(author: "km", id: "add_xds_document_to_self_monitoring_sample") {
        addColumn(tableName: "self_monitoring_sample") {
            column(name: "author_id", type: '${integer.type}')
            column(name: "custodian_id", type: '${integer.type}')
            column(name: "legal_authenticator_id", type: '${integer.type}')
        }

    }

    changeSet(author: "kim (generated)", id: "1411391966757-1") {
        createTable(tableName: "address_postal") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "address_postaPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "country_identification_code_type_scheme", type: '${string.type}')
            column(name: "country_identification_code_type_value", type: '${string.type}')
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "district_name", type: '${string.type}')
            column(name: "district_subdivision_identifier", type: '${string.type}')
            column(name: "floor_identifier", type: '${string.type}')
            column(name: "mail_delivery_sublocation_identifier", type: '${string.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "municipality_name", type: '${string.type}')
            column(name: "post_code_identifier", type: '${string.type}')
            column(name: "post_office_box_identifier", type: '${string.type}')
            column(name: "street_building_identifier", type: '${string.type}')
            column(name: "street_name", type: '${string.type}')
            column(name: "street_name_for_addressing_name", type: '${string.type}')
            column(name: "suite_identifier", type: '${string.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-5") {
        createTable(tableName: "author") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "authorPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "address_postal_type_id", type: '${integer.type}')
            column(name: "assigned_person_given_name", type: '${string.type}')
            column(name: "assigned_person_middle_name", type: '${string.type}')
            column(name: "assigned_person_surname_name", type: '${string.type}')
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "represented_organization", type: '${string.type}')
            column(name: "time", type: '${datetime.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-6") {
        createTable(tableName: "author_email_address") {
            column(name: "author_email_address_id", type: '${integer.type}')
            column(name: "email_address_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-7") {
        createTable(tableName: "author_id_tp") {
            column(name: "author_ids_id", type: '${integer.type}')
            column(name: "id_tp_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-8") {
        createTable(tableName: "author_phone_number_subscriber") {
            column(name: "author_phone_number_subscribers_id", type: '${integer.type}')
            column(name: "phone_number_subscriber_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-10") {
        createTable(tableName: "custodian") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "custodianPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "address_id", type: '${integer.type}')
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "name", type: '${string.type}')
            column(name: "phone_number_subscriber_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-11") {
        createTable(tableName: "custodian_id_tp") {
            column(name: "custodian_ids_id", type: '${integer.type}')

            column(name: "id_tp_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-12") {
        createTable(tableName: "email_address") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "email_addressPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "email_address_identifier", type: '${string.type}')
            column(name: "email_address_use", type: '${string.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-13") {
        createTable(tableName: "id_tp") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "id_tpPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "identifier", type: '${string.type}')
            column(name: "identifier_code", type: '${string.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-18") {
        createTable(tableName: "legal_authenticator") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "legal_authentPK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "address_id", type: '${integer.type}')
            column(name: "assigned_person_given_name", type: '${string.type}')
            column(name: "assigned_person_middle_name", type: '${string.type}')
            column(name: "assigned_person_surname_name", type: '${string.type}')
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "represented_organization_name", type: '${string.type}')
            column(name: "signature_code", type: '${string.type}')
            column(name: "time", type: '${datetime.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-19") {
        createTable(tableName: "legal_authenticator_email_address") {
            column(name: "legal_authenticator_email_address_id", type: '${integer.type}')
            column(name: "email_address_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-20") {
        createTable(tableName: "legal_authenticator_id_tp") {
            column(name: "legal_authenticator_ids_id", type: '${integer.type}')
            column(name: "id_tp_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-21") {
        createTable(tableName: "legal_authenticator_phone_number_subscriber") {
            column(name: "legal_authenticator_phone_number_subscribers_id", type: '${integer.type}')
            column(name: "phone_number_subscriber_id", type: '${integer.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-23") {
        createTable(tableName: "phone_number_subscriber") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "phone_number_PK")
            }
            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
            column(name: "phone_number_identifier", type: '${string.type}')
            column(name: "phone_number_use", type: '${string.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-72") {
        createIndex(indexName: "authority_uniq_1411391966702", tableName: "role", unique: "true") {
            column(name: "authority")
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-73") {
        createIndex(indexName: "username_uniq_1411391966707", tableName: "users", unique: "true") {
            column(name: "username")
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-74") {
        createIndex(indexName: "uuid_uniq_1411391966708", tableName: "xdsdocument", unique: "true") {
            column(name: "uuid")
        }
    }

    changeSet(author: "kim (generated)", id: "1411391966757-35") {
        addForeignKeyConstraint(baseColumnNames: "address_postal_type_id", baseTableName: "author", constraintName: "FKAC2D218B35453988", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "address_postal", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-36") {
        addForeignKeyConstraint(baseColumnNames: "author_email_address_id", baseTableName: "author_email_address", constraintName: "FKFB2169D3FBEE896", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "author", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-37") {
        addForeignKeyConstraint(baseColumnNames: "email_address_id", baseTableName: "author_email_address", constraintName: "FKFB2169D766DFD0F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "email_address", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-38") {
        addForeignKeyConstraint(baseColumnNames: "author_ids_id", baseTableName: "author_id_tp", constraintName: "FK71B8412A39FAC7CF", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "author", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-39") {
        addForeignKeyConstraint(baseColumnNames: "id_tp_id", baseTableName: "author_id_tp", constraintName: "FK71B8412A3DA2211F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "id_tp", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-40") {
        addForeignKeyConstraint(baseColumnNames: "author_phone_number_subscribers_id", baseTableName: "author_phone_number_subscriber", constraintName: "FKE3196BD958FB7E59", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "author", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-41") {
        addForeignKeyConstraint(baseColumnNames: "phone_number_subscriber_id", baseTableName: "author_phone_number_subscriber", constraintName: "FKE3196BD92BDAAB5E", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "phone_number_subscriber", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-42") {
        addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "custodian", constraintName: "FK600A71EE7A31A657", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "address_postal", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-43") {
        addForeignKeyConstraint(baseColumnNames: "phone_number_subscriber_id", baseTableName: "custodian", constraintName: "FK600A71EE2BDAAB5E", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "phone_number_subscriber", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-44") {
        addForeignKeyConstraint(baseColumnNames: "custodian_ids_id", baseTableName: "custodian_id_tp", constraintName: "FKB1BC7E8DFFF6C593", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custodian", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-45") {
        addForeignKeyConstraint(baseColumnNames: "id_tp_id", baseTableName: "custodian_id_tp", constraintName: "FKB1BC7E8D3DA2211F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "id_tp", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-50") {
        addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "legal_authenticator", constraintName: "FK641A326D7A31A657", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "address_postal", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-51") {
        addForeignKeyConstraint(baseColumnNames: "email_address_id", baseTableName: "legal_authenticator_email_address", constraintName: "FKEF36F3FF766DFD0F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "email_address", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-52") {
        addForeignKeyConstraint(baseColumnNames: "legal_authenticator_email_address_id", baseTableName: "legal_authenticator_email_address", constraintName: "FKEF36F3FF9E51A8A3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "legal_authenticator", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-53") {
        addForeignKeyConstraint(baseColumnNames: "id_tp_id", baseTableName: "legal_authenticator_id_tp", constraintName: "FK17FF500C3DA2211F", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "id_tp", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-54") {
        addForeignKeyConstraint(baseColumnNames: "legal_authenticator_ids_id", baseTableName: "legal_authenticator_id_tp", constraintName: "FK17FF500C2CF9855C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "legal_authenticator", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-55") {
        addForeignKeyConstraint(baseColumnNames: "legal_authenticator_phone_number_subscribers_id", baseTableName: "legal_authenticator_phone_number_subscriber", constraintName: "FK9B7A76BBCA2C78EA", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "legal_authenticator", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-56") {
        addForeignKeyConstraint(baseColumnNames: "phone_number_subscriber_id", baseTableName: "legal_authenticator_phone_number_subscriber", constraintName: "FK9B7A76BB2BDAAB5E", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "phone_number_subscriber", referencesUniqueColumn: "false")
    }

    changeSet(author: "kim (generated)", id: "1411391966757-62") {
        addForeignKeyConstraint(baseColumnNames: "legal_authenticator_id", baseTableName: "self_monitoring_sample", constraintName: "FK3F1EEB4EF6EFAB75", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "legal_authenticator", referencesUniqueColumn: "false")
    }
}
