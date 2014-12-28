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

	changeSet(author: "lch (generated)", id: "1390590581424-1") {
		createTable(tableName: "audit_log_controller_entity") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_conPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "action_name", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "controller_name", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "number_of_calls", type: '${integer.type}') {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-2") {
		createTable(tableName: "audit_log_entry") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_entPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "action_id", type: '${id.type}')

			column(name: "authority", type: '${string255.type}')

			column(name: "calling_ip", type: '${string255.type}')

			column(name: "correlation_id", type: '${string255.type}')

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "duration", type: '${id.type}')

			column(name: "end_date", type: '${datetime.type}')

			column(name: "end_time", type: '${id.type}')

			column(name: "exception", type: '${boolean.type}') {
				constraints(nullable: "false")
			}

			column(name: "exception_message", type: '${string255.type}')

			column(name: "http_session_id", type: '${string255.type}')

			column(name: "id_card", type: '${string255.type}')

			column(name: "message_id", type: '${string255.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "operation", type: '${string255.type}')

			column(name: "patient_cpr", type: '${string255.type}')

			column(name: "request", type: '${string255.type}')

			column(name: "response", type: '${string255.type}')

			column(name: "result", type: '${string255.type}')

			column(name: "service", type: '${string255.type}')

			column(name: "start_date", type: '${datetime.type}')

			column(name: "start_time", type: '${id.type}')

			column(name: "success", type: '${boolean.type}') {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-3") {
		createTable(tableName: "audit_log_parameter") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_parPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "entry_id", type: '${id.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "parameter_key", type: '${string255.type}')

			column(name: "parameter_value", type: '${string255.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-4") {
		createTable(tableName: "citizen") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "citizenPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "city", type: '${string255.type}')

			column(name: "contact_telephone", type: '${string255.type}')

			column(name: "country", type: '${string255.type}')

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "date_of_birth", type: '${datetime.type}') {
				constraints(nullable: "false")
			}

			column(name: "email", type: '${string255.type}')

			column(name: "first_name", type: '${string255.type}')

			column(name: "last_name", type: '${string255.type}')

			column(name: "middle_name", type: '${string255.type}')

			column(name: "modified_by", type: '${string255.type}') 

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "sex", type: '${string255.type}')

			column(name: "ssn", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "street_name", type: '${string255.type}')

			column(name: "street_name2", type: '${string255.type}')

			column(name: "zip_code", type: '${string255.type}')

            column(name: "in_personalization_index", type: '${boolean.type}')

		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-5") {
		createTable(tableName: "instrument") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "instrumentPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "manufacturer", type: '${string255.type}')

			column(name: "med_comid", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "model", type: '${string255.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "product_type", type: '${string255.type}')

			column(name: "software_version", type: '${string255.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-6") {
		createTable(tableName: "iupaccode") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "iupaccodePK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "code", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "comment", type: '${string255.type}')

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "description", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "name", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "unit", type: '${string255.type}') {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-7") {
		createTable(tableName: "lab_producer") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "lab_producerPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "identifier", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "identifier_code", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-8") {
		createTable(tableName: "laboratory_report") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "laboratory_rePK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "analysis_text", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "created_date_time", type: '${datetime.type}') {
				constraints(nullable: "false")
			}

			column(name: "health_care_professional_comment", type: '${text.type}')  // TODO: HRA - clarify

			column(name: "instrument_id", type: '${id.type}')

			column(name: "iupac_code_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "laboratory_reportuuid", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "measurement_duration", type: '${string255.type}')

			column(name: "measurement_location", type: '${string255.type}')

			column(name: "measurement_scheduled", type: '${string255.type}')

			column(name: "measurement_transferred_by", type: '${string255.type}')

			column(name: "measuring_circumstances", type: '${text.type}') // TODO: HRA - clarify

			column(name: "measuring_data_classification", type: '${string255.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "national_sample_identifier", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "producer_of_lab_result_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "result_abnormal_identifier", type: '${string255.type}')

			column(name: "result_encoding_identifier", type: '${string255.type}')

			column(name: "result_maximum_text", type: '${string255.type}')

			column(name: "result_minimum_text", type: '${string255.type}')

			column(name: "result_operator_identifier", type: '${string255.type}')

			column(name: "result_text", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "result_type_of_interval", type: '${string255.type}')

			column(name: "result_unit_text", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "sample_id", type: '${id.type}')

            column(name: "deleted", type: '${boolean.type}')

		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-9") {
		createTable(tableName: "permission") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "permissionPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "permission", type: '${string255.type}') {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-10") {
		createTable(tableName: "role") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "authority", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-11") {
		createTable(tableName: "role_permission") {
			column(name: "permission_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "role_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-12") {
		createTable(tableName: "self_monitoring_sample") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "self_monitoriPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "citizen_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_by_text", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_date", type: '${datetime.type}')

			column(name: "created_date_time", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "sample_category_identifier", type: '${string255.type}')

			column(name: "self_monitoring_sampleuuid", type: '${string255.type}')

            column(name: "xds_document_uuid", type: '${string255.type}')
            column(name: "xds_conversion_started", type: '${boolean.type}')
            column(name: "xds_registered_in_registry", type: '${boolean.type}')
            column(name: "deleted", type: '${boolean.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-13") {
		createTable(tableName: "user_role") {
			column(name: "role_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-14") {
		createTable(tableName: "users") {
			column(autoIncrement: "true", name: "id", type: '${id.type}') {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "usersPK")
			}

			column(name: "version", type: '${id.type}') {
				constraints(nullable: "false")
			}

			column(name: "account_expired", type: '${boolean.type}') {
				constraints(nullable: "false")
			}

			column(name: "account_locked", type: '${boolean.type}') {
				constraints(nullable: "false")
			}

			column(name: "created_by", type: '${string255.type}')

			column(name: "created_date", type: '${datetime.type}')

			column(name: "enabled", type: '${boolean.type}') {
				constraints(nullable: "false")
			}

			column(name: "modified_by", type: '${string255.type}')

			column(name: "modified_date", type: '${datetime.type}')

			column(name: "password", type: '${string255.type}') {
				constraints(nullable: "false")
			}

			column(name: "password_expired", type: '${boolean.type}') {
				constraints(nullable: "false")
			}

			column(name: "username", type: '${string255.type}') {
				constraints(nullable: "false")
			}
		}
	}


    changeSet(author: "hra", id: "123-hra-01") {

        createTable(tableName: "xdsmetadata") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "xdsMetadataPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "data", type: '${text.type}') {
                constraints(nullable: "false")
            }

            column(name: "registered", type: '${boolean.type}')

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }

        createTable(tableName: "xdsdocument") {
            column(autoIncrement: "true", name: "id", type: '${id.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "xdsDocumentPK")
            }

            column(name: "version", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: '${string255.type}') {
                constraints(nullable: "false")
            }

            column(name: "mime_type", type: '${string255.type}')

            column(name: "document", type: '${blob.type}') {
                constraints(nullable: "false")
            }

            column(name: "metadata_id", type: '${id.type}')

            column(name: "created_by", type: '${string255.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "modified_by", type: '${string255.type}')

            column(name: "modified_date", type: '${datetime.type}')
        }

        addForeignKeyConstraint(baseColumnNames: "metadata_id", baseTableName: "xdsdocument", constraintName: "XDSDOC_METADATA_FK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "xdsmetadata", referencesUniqueColumn: "false")

        createIndex(indexName: "xdsdoc_uuid_uniq", tableName: "xdsdocument", unique: "true") {
            column(name: "uuid")
        }
    }



	changeSet(author: "lch (generated)", id: "1390590581424-15") {
		addPrimaryKey(columnNames: "permission_id, role_id", constraintName: "role_permissiPK", tableName: "role_permission")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-16") {
		addPrimaryKey(columnNames: "role_id, user_id", constraintName: "user_rolePK", tableName: "user_role")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-28") {
		createIndex(indexName: "end_time_idx", tableName: "audit_log_entry") {
			column(name: "end_time")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-29") {
		createIndex(indexName: "patient_cpr_idx", tableName: "audit_log_entry") {
			column(name: "patient_cpr")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-30") {
		createIndex(indexName: "start_time_idx", tableName: "audit_log_entry") {
			column(name: "start_time")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-31") {
		createIndex(indexName: "citizen_ssn_idx", tableName: "citizen") {
			column(name: "ssn")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-32") {
		createIndex(indexName: "code_uniq_1390590581344", tableName: "iupaccode", unique: "true") {
			column(name: "code")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-33") {
		createIndex(indexName: "laboratory_reportuuid_uniq_1390590581349", tableName: "laboratory_report", unique: "true") {
			column(name: "laboratory_reportuuid")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-34") {
		createIndex(indexName: "authority_uniq_1390590581354", tableName: "role", unique: "true") {
			column(name: "authority")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-35") {
		createIndex(indexName: "username_uniq_1390590581362", tableName: "users", unique: "true") {
			column(name: "username")
		}
	}

	changeSet(author: "lch (generated)", id: "1390590581424-17") {
		addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "audit_log_entry", constraintName: "FK40704C53F84F8915", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "audit_log_controller_entity", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-18") {
		addForeignKeyConstraint(baseColumnNames: "entry_id", baseTableName: "audit_log_parameter", constraintName: "FK4D351F0AF5DB4508", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "audit_log_entry", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-19") {
		addForeignKeyConstraint(baseColumnNames: "instrument_id", baseTableName: "laboratory_report", constraintName: "FKE2DD634072DCB95D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "instrument", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-20") {
		addForeignKeyConstraint(baseColumnNames: "iupac_code_id", baseTableName: "laboratory_report", constraintName: "FKE2DD6340BF822EE4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "iupaccode", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-21") {
		addForeignKeyConstraint(baseColumnNames: "producer_of_lab_result_id", baseTableName: "laboratory_report", constraintName: "FKE2DD6340344CE08C", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "lab_producer", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-22") {
		addForeignKeyConstraint(baseColumnNames: "sample_id", baseTableName: "laboratory_report", constraintName: "FKE2DD6340145CCC91", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "self_monitoring_sample", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-23") {
		addForeignKeyConstraint(baseColumnNames: "permission_id", baseTableName: "role_permission", constraintName: "FKBD40D5383C5E905D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "permission", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-24") {
		addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "role_permission", constraintName: "FKBD40D538FBCCF93D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-25") {
		addForeignKeyConstraint(baseColumnNames: "citizen_id", baseTableName: "self_monitoring_sample", constraintName: "FK3F1EEB4EA5548A97", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "citizen", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-26") {
		addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FK143BF46AFBCCF93D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
	}

	changeSet(author: "lch (generated)", id: "1390590581424-27") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK143BF46AA0F7BD1D", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "users", referencesUniqueColumn: "false")
	}
}
