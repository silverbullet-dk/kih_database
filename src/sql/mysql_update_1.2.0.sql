--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: changelog.groovy
--  Ran at: 25-09-14 10:45
--  Against: kih@localhost@jdbc:mysql://localhost:3306/kihdb
--  Liquibase version: 2.0.5
--  *********************************************************************

-- CREATE TABLE `audit_log_controller_entity` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `action_name` VARCHAR(255) NOT NULL, `controller_name` VARCHAR(255) NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `number_of_calls` BIGINT NOT NULL, CONSTRAINT `audit_log_conPK` PRIMARY KEY (`id`));
-- CREATE TABLE `audit_log_entry` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `action_id` BIGINT NULL, `authority` VARCHAR(255) NULL, `calling_ip` VARCHAR(255) NULL, `correlation_id` VARCHAR(255) NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `duration` BIGINT NULL, `end_date` DATETIME NULL, `end_time` BIGINT NULL, `exception` bit NOT NULL, `exception_message` VARCHAR(255) NULL, `http_session_id` VARCHAR(255) NULL, `id_card` VARCHAR(255) NULL, `message_id` VARCHAR(255) NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `operation` VARCHAR(255) NULL, `patient_cpr` VARCHAR(255) NULL, `request` VARCHAR(255) NULL, `response` VARCHAR(255) NULL, `result` VARCHAR(255) NULL, `service` VARCHAR(255) NULL, `start_date` DATETIME NULL, `start_time` BIGINT NULL, `success` bit NOT NULL, CONSTRAINT `audit_log_entPK` PRIMARY KEY (`id`));
-- CREATE TABLE `audit_log_parameter` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `entry_id` BIGINT NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `parameter_key` VARCHAR(255) NULL, `parameter_value` VARCHAR(255) NULL, CONSTRAINT `audit_log_parPK` PRIMARY KEY (`id`));
-- CREATE TABLE `citizen` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `city` VARCHAR(255) NULL, `contact_telephone` VARCHAR(255) NULL, `country` VARCHAR(255) NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `date_of_birth` DATETIME NOT NULL, `email` VARCHAR(255) NULL, `first_name` VARCHAR(255) NULL, `last_name` VARCHAR(255) NULL, `middle_name` VARCHAR(255) NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `sex` VARCHAR(255) NULL, `ssn` VARCHAR(255) NOT NULL, `street_name` VARCHAR(255) NULL, `street_name2` VARCHAR(255) NULL, `zip_code` VARCHAR(255) NULL, `in_personalization_index` bit NULL, CONSTRAINT `citizenPK` PRIMARY KEY (`id`));
-- CREATE TABLE `instrument` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `manufacturer` VARCHAR(255) NULL, `med_comid` VARCHAR(255) NOT NULL, `model` VARCHAR(255) NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `product_type` VARCHAR(255) NULL, `software_version` VARCHAR(255) NULL, CONSTRAINT `instrumentPK` PRIMARY KEY (`id`));
-- CREATE TABLE `iupaccode` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `code` VARCHAR(255) NOT NULL, `comment` VARCHAR(255) NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `description` VARCHAR(255) NOT NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `name` VARCHAR(255) NOT NULL, `unit` VARCHAR(255) NOT NULL, CONSTRAINT `iupaccodePK` PRIMARY KEY (`id`));
-- CREATE TABLE `lab_producer` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `identifier` VARCHAR(255) NOT NULL, `identifier_code` VARCHAR(255) NOT NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, CONSTRAINT `lab_producerPK` PRIMARY KEY (`id`));
-- CREATE TABLE `laboratory_report` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `analysis_text` VARCHAR(255) NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `created_date_time` DATETIME NOT NULL, `health_care_professional_comment` longtext NULL, `instrument_id` BIGINT NULL, `iupac_code_id` BIGINT NOT NULL, `laboratory_reportuuid` VARCHAR(255) NOT NULL, `measurement_duration` VARCHAR(255) NULL, `measurement_location` VARCHAR(255) NULL, `measurement_scheduled` VARCHAR(255) NULL, `measurement_transferred_by` VARCHAR(255) NULL, `measuring_circumstances` longtext NULL, `measuring_data_classification` VARCHAR(255) NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `national_sample_identifier` VARCHAR(255) NOT NULL, `producer_of_lab_result_id` BIGINT NOT NULL, `result_abnormal_identifier` VARCHAR(255) NULL, `result_encoding_identifier` VARCHAR(255) NULL, `result_maximum_text` VARCHAR(255) NULL, `result_minimum_text` VARCHAR(255) NULL, `result_operator_identifier` VARCHAR(255) NULL, `result_text` VARCHAR(255) NOT NULL, `result_type_of_interval` VARCHAR(255) NULL, `result_unit_text` VARCHAR(255) NOT NULL, `sample_id` BIGINT NULL, `deleted` bit NULL, CONSTRAINT `laboratory_rePK` PRIMARY KEY (`id`));
-- CREATE TABLE `permission` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `permission` VARCHAR(255) NOT NULL, CONSTRAINT `permissionPK` PRIMARY KEY (`id`));
-- CREATE TABLE `role` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `authority` VARCHAR(255) NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, CONSTRAINT `rolePK` PRIMARY KEY (`id`));
-- CREATE TABLE `role_permission` (`permission_id` BIGINT NOT NULL, `role_id` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL);
-- CREATE TABLE `self_monitoring_sample` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `citizen_id` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_by_text` VARCHAR(255) NOT NULL, `created_date` DATETIME NULL, `created_date_time` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `sample_category_identifier` VARCHAR(255) NULL, `self_monitoring_sampleuuid` VARCHAR(255) NULL, `xds_document_uuid` VARCHAR(255) NULL, `xds_conversion_started` bit NULL, `xds_registered_in_registry` bit NULL, `deleted` bit NULL, CONSTRAINT `self_monitoriPK` PRIMARY KEY (`id`));
-- CREATE TABLE `user_role` (`role_id` BIGINT NOT NULL, `user_id` BIGINT NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL);
-- CREATE TABLE `users` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `account_expired` bit NOT NULL, `account_locked` bit NOT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `enabled` bit NOT NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, `password` VARCHAR(255) NOT NULL, `password_expired` bit NOT NULL, `username` VARCHAR(255) NOT NULL, CONSTRAINT `usersPK` PRIMARY KEY (`id`));
-- CREATE TABLE `xdsmetadata` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `data` longtext NOT NULL, `registered` bit NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, CONSTRAINT `xdsMetadataPK` PRIMARY KEY (`id`));
-- CREATE TABLE `xdsdocument` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `uuid` VARCHAR(255) NOT NULL, `mime_type` VARCHAR(255) NULL, `document` BLOB NOT NULL, `metadata_id` BIGINT NULL, `created_by` VARCHAR(255) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(255) NULL, `modified_date` DATETIME NULL, CONSTRAINT `xdsDocumentPK` PRIMARY KEY (`id`));
-- ALTER TABLE `xdsdocument` ADD CONSTRAINT `XDSDOC_METADATA_FK` FOREIGN KEY (`metadata_id`) REFERENCES `xdsmetadata` (`id`);
-- CREATE UNIQUE INDEX `xdsdoc_uuid_uniq` ON `xdsdocument`(`uuid`);
-- INSERT BIGINTO `DATABASECHANGELOG` (`AUTHOR`, `COMMENTS`, `DATEEXECUTED`, `DESCRIPTION`, `EXECTYPE`, `FILENAME`, `ID`, `LIQUIBASE`, `MD5SUM`, `ORDEREXECUTED`) VALUES ('hra', '', NOW(), 'Create Table (x2), Add Foreign Key Constraint, Create Index', 'EXECUTED', '1_0_baseline.groovy', '123-hra-01', '2.0.5', '3:f0bc56b6b174085c007e8037a565f22f', 15);
-- ALTER TABLE `role_permission` ADD PRIMARY KEY (`permission_id`, `role_id`);
-- ALTER TABLE `user_role` ADD PRIMARY KEY (`role_id`, `user_id`);
-- CREATE INDEX `end_time_idx` ON `audit_log_entry`(`end_time`);
-- CREATE INDEX `patient_cpr_idx` ON `audit_log_entry`(`patient_cpr`);
-- CREATE INDEX `start_time_idx` ON `audit_log_entry`(`start_time`);
-- CREATE INDEX `citizen_ssn_idx` ON `citizen`(`ssn`);
-- CREATE UNIQUE INDEX `code_uniq_1390590581344` ON `iupaccode`(`code`);
-- CREATE UNIQUE INDEX `laboratory_reportuuid_uniq_1390590581349` ON `laboratory_report`(`laboratory_reportuuid`);
-- CREATE UNIQUE INDEX `authority_uniq_1390590581354` ON `role`(`authority`);
-- CREATE UNIQUE INDEX `username_uniq_1390590581362` ON `users`(`username`);
-- ALTER TABLE `audit_log_entry` ADD CONSTRAINT `FK40704C53F84F8915` FOREIGN KEY (`action_id`) REFERENCES `audit_log_controller_entity` (`id`);
-- ALTER TABLE `audit_log_parameter` ADD CONSTRAINT `FK4D351F0AF5DB4508` FOREIGN KEY (`entry_id`) REFERENCES `audit_log_entry` (`id`);
-- ALTER TABLE `laboratory_report` ADD CONSTRAINT `FKE2DD634072DCB95D` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`id`);
-- ALTER TABLE `laboratory_report` ADD CONSTRAINT `FKE2DD6340BF822EE4` FOREIGN KEY (`iupac_code_id`) REFERENCES `iupaccode` (`id`);
-- ALTER TABLE `laboratory_report` ADD CONSTRAINT `FKE2DD6340344CE08C` FOREIGN KEY (`producer_of_lab_result_id`) REFERENCES `lab_producer` (`id`);
-- ALTER TABLE `laboratory_report` ADD CONSTRAINT `FKE2DD6340145CCC91` FOREIGN KEY (`sample_id`) REFERENCES `self_monitoring_sample` (`id`);
-- ALTER TABLE `role_permission` ADD CONSTRAINT `FKBD40D5383C5E905D` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`);
-- ALTER TABLE `role_permission` ADD CONSTRAINT `FKBD40D538FBCCF93D` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
-- ALTER TABLE `self_monitoring_sample` ADD CONSTRAINT `FK3F1EEB4EA5548A97` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`id`);
-- ALTER TABLE `user_role` ADD CONSTRAINT `FK143BF46AFBCCF93D` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
-- ALTER TABLE `user_role` ADD CONSTRAINT `FK143BF46AA0F7BD1D` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--  Changeset 1_2_0_update.groovy::add_xds_document_to_self_monitoring_sample::km::(Checksum: 3:878a14337dac99bf5d44138b487ee48e)
ALTER TABLE `self_monitoring_sample` ADD `author_id` BIGINT;
ALTER TABLE `self_monitoring_sample` ADD `custodian_id` BIGINT;
ALTER TABLE `self_monitoring_sample` ADD `legal_authenticator_id` BIGINT;

--  Changeset 1_2_0_update.groovy::1411391966757-1::kim (generated)::(Checksum: 3:25d2d60622d2493ad83bdc36165f7ae3)
CREATE TABLE `address_postal` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `country_identification_code_type_scheme` VARCHAR(1024) NULL, `country_identification_code_type_value` VARCHAR(1024) NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `district_name` VARCHAR(1024) NULL, `district_subdivision_identifier` VARCHAR(1024) NULL, `floor_identifier` VARCHAR(1024) NULL, `mail_delivery_sublocation_identifier` VARCHAR(1024) NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `municipality_name` VARCHAR(1024) NULL, `post_code_identifier` VARCHAR(1024) NULL, `post_office_box_identifier` VARCHAR(1024) NULL, `street_building_identifier` VARCHAR(1024) NULL, `street_name` VARCHAR(1024) NULL, `street_name_for_addressing_name` VARCHAR(1024) NULL, `suite_identifier` VARCHAR(1024) NULL, CONSTRAINT `address_postaPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-5::kim (generated)::(Checksum: 3:85fcffe609bf79f52cc855e34a6c613a)
CREATE TABLE `author` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `address_postal_type_id` BIGINT NULL, `assigned_person_given_name` VARCHAR(1024) NULL, `assigned_person_middle_name` VARCHAR(1024) NULL, `assigned_person_surname_name` VARCHAR(1024) NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `represented_organization` VARCHAR(1024) NULL, `time` DATETIME NULL, CONSTRAINT `authorPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-6::kim (generated)::(Checksum: 3:5d6b0f70e387571cfaf03b7943fb0797)
CREATE TABLE `author_email_address` (`author_email_address_id` BIGINT NULL, `email_address_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-7::kim (generated)::(Checksum: 3:c47bc19536a57949e325d619bf83b12d)
CREATE TABLE `author_id_tp` (`author_ids_id` BIGINT NULL, `id_tp_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-8::kim (generated)::(Checksum: 3:0f69a5f48051140e66d61da6b1dd2c64)
CREATE TABLE `author_phone_number_subscriber` (`author_phone_number_subscribers_id` BIGINT NULL, `phone_number_subscriber_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-10::kim (generated)::(Checksum: 3:4043e3788a62daad6e292787c9653339)
CREATE TABLE `custodian` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `address_id` BIGINT NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `name` VARCHAR(1024) NULL, `phone_number_subscriber_id` BIGINT NULL, CONSTRAINT `custodianPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-11::kim (generated)::(Checksum: 3:404fd2a5da1408846e0d86c518fe0e93)
CREATE TABLE `custodian_id_tp` (`custodian_ids_id` BIGINT NULL, `id_tp_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-12::kim (generated)::(Checksum: 3:1eb4a2876c442e9dd87ae2f86256d8f4)
CREATE TABLE `email_address` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `email_address_identifier` VARCHAR(1024) NULL, `email_address_use` VARCHAR(1024) NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, CONSTRAINT `email_addressPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-13::kim (generated)::(Checksum: 3:96fe7cb637ee3ffb180e82f0efcf25e4)
CREATE TABLE `id_tp` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `identifier` VARCHAR(1024) NULL, `identifier_code` VARCHAR(1024) NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, CONSTRAINT `id_tpPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-18::kim (generated)::(Checksum: 3:f2c6b2627b2aaf900c86712016e102f9)
CREATE TABLE `legal_authenticator` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `address_id` BIGINT NULL, `assigned_person_given_name` VARCHAR(1024) NULL, `assigned_person_middle_name` VARCHAR(1024) NULL, `assigned_person_surname_name` VARCHAR(1024) NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `represented_organization_name` VARCHAR(1024) NULL, `signature_code` VARCHAR(1024) NULL, `time` DATETIME NULL, CONSTRAINT `legal_authentPK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-19::kim (generated)::(Checksum: 3:a6a7554329b03c7d27834a3d4692286b)
CREATE TABLE `legal_authenticator_email_address` (`legal_authenticator_email_address_id` BIGINT NULL, `email_address_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-20::kim (generated)::(Checksum: 3:99767a8418cfd7eabca8711093912dbe)
CREATE TABLE `legal_authenticator_id_tp` (`legal_authenticator_ids_id` BIGINT NULL, `id_tp_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-21::kim (generated)::(Checksum: 3:5221dc080c5830ec5749afabfd0c4a75)
CREATE TABLE `legal_authenticator_phone_number_subscriber` (`legal_authenticator_phone_number_subscribers_id` BIGINT NULL, `phone_number_subscriber_id` BIGINT NULL);

--  Changeset 1_2_0_update.groovy::1411391966757-23::kim (generated)::(Checksum: 3:eb73b450a4398d62a9c57af4f3ddeae3)
CREATE TABLE `phone_number_subscriber` (`id` BIGINT AUTO_INCREMENT NOT NULL, `version` BIGINT NOT NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `phone_number_identifier` VARCHAR(1024) NULL, `phone_number_use` VARCHAR(1024) NULL, CONSTRAINT `phone_number_PK` PRIMARY KEY (`id`));

--  Changeset 1_2_0_update.groovy::1411391966757-72::kim (generated)::(Checksum: 3:2fa4f41c421c32b7d4f212efd87e6196)
CREATE UNIQUE INDEX `authority_uniq_1411391966702` ON `role`(`authority`);

--  Changeset 1_2_0_update.groovy::1411391966757-73::kim (generated)::(Checksum: 3:3e83a49376a52b275403c58b4654ac5f)
CREATE UNIQUE INDEX `username_uniq_1411391966707` ON `users`(`username`);

--  Changeset 1_2_0_update.groovy::1411391966757-74::kim (generated)::(Checksum: 3:9858a8c780063f552562cc5a902e991f)
CREATE UNIQUE INDEX `uuid_uniq_1411391966708` ON `xdsdocument`(`uuid`);

--  Changeset 1_2_0_update.groovy::1411391966757-35::kim (generated)::(Checksum: 3:e8863d86a9545f3a0865c760a99f9ec1)
ALTER TABLE `author` ADD CONSTRAINT `FKAC2D218B35453988` FOREIGN KEY (`address_postal_type_id`) REFERENCES `address_postal` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-36::kim (generated)::(Checksum: 3:139d153453a9ecd1a21254f39f22bf19)
ALTER TABLE `author_email_address` ADD CONSTRAINT `FKFB2169D3FBEE896` FOREIGN KEY (`author_email_address_id`) REFERENCES `author` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-37::kim (generated)::(Checksum: 3:af13f820974bb281330027b35aea9b3c)
ALTER TABLE `author_email_address` ADD CONSTRAINT `FKFB2169D766DFD0F` FOREIGN KEY (`email_address_id`) REFERENCES `email_address` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-38::kim (generated)::(Checksum: 3:bb518dca2c2b0a6753fc972b3d50c02e)
ALTER TABLE `author_id_tp` ADD CONSTRAINT `FK71B8412A39FAC7CF` FOREIGN KEY (`author_ids_id`) REFERENCES `author` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-39::kim (generated)::(Checksum: 3:2f2674c2ccb54f6567b231d40ada776d)
ALTER TABLE `author_id_tp` ADD CONSTRAINT `FK71B8412A3DA2211F` FOREIGN KEY (`id_tp_id`) REFERENCES `id_tp` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-40::kim (generated)::(Checksum: 3:3797ce694c20de525d4b24f58659f5ce)
ALTER TABLE `author_phone_number_subscriber` ADD CONSTRAINT `FKE3196BD958FB7E59` FOREIGN KEY (`author_phone_number_subscribers_id`) REFERENCES `author` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-41::kim (generated)::(Checksum: 3:1f58b23ab83458866756d4ef3c8b6031)
ALTER TABLE `author_phone_number_subscriber` ADD CONSTRAINT `FKE3196BD92BDAAB5E` FOREIGN KEY (`phone_number_subscriber_id`) REFERENCES `phone_number_subscriber` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-42::kim (generated)::(Checksum: 3:3def431fdb0d35149a3c70abc9c84eb6)
ALTER TABLE `custodian` ADD CONSTRAINT `FK600A71EE7A31A657` FOREIGN KEY (`address_id`) REFERENCES `address_postal` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-43::kim (generated)::(Checksum: 3:725c1311f66ced7bb38e41db4841370a)
ALTER TABLE `custodian` ADD CONSTRAINT `FK600A71EE2BDAAB5E` FOREIGN KEY (`phone_number_subscriber_id`) REFERENCES `phone_number_subscriber` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-44::kim (generated)::(Checksum: 3:b2b8bc75e452f23dd3b3defabbfd2c8e)
ALTER TABLE `custodian_id_tp` ADD CONSTRAINT `FKB1BC7E8DFFF6C593` FOREIGN KEY (`custodian_ids_id`) REFERENCES `custodian` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-45::kim (generated)::(Checksum: 3:16c9adba194fa5fa3f99f98c67fecc05)
ALTER TABLE `custodian_id_tp` ADD CONSTRAINT `FKB1BC7E8D3DA2211F` FOREIGN KEY (`id_tp_id`) REFERENCES `id_tp` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-50::kim (generated)::(Checksum: 3:c4c02c9c087be150f533fc2327da75f3)
ALTER TABLE `legal_authenticator` ADD CONSTRAINT `FK641A326D7A31A657` FOREIGN KEY (`address_id`) REFERENCES `address_postal` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-51::kim (generated)::(Checksum: 3:92c243ea32beabcf1f29ef6977315a0e)
ALTER TABLE `legal_authenticator_email_address` ADD CONSTRAINT `FKEF36F3FF766DFD0F` FOREIGN KEY (`email_address_id`) REFERENCES `email_address` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-52::kim (generated)::(Checksum: 3:770790ad140c741706e2bedf60e93fb8)
ALTER TABLE `legal_authenticator_email_address` ADD CONSTRAINT `FKEF36F3FF9E51A8A3` FOREIGN KEY (`legal_authenticator_email_address_id`) REFERENCES `legal_authenticator` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-53::kim (generated)::(Checksum: 3:810d5ef372d4bc7503851cacc8bc5053)
ALTER TABLE `legal_authenticator_id_tp` ADD CONSTRAINT `FK17FF500C3DA2211F` FOREIGN KEY (`id_tp_id`) REFERENCES `id_tp` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-54::kim (generated)::(Checksum: 3:0eb32b47d1b9dc563497c6cd9b497b7b)
ALTER TABLE `legal_authenticator_id_tp` ADD CONSTRAINT `FK17FF500C2CF9855C` FOREIGN KEY (`legal_authenticator_ids_id`) REFERENCES `legal_authenticator` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-55::kim (generated)::(Checksum: 3:db716601944dd667e30b4e3f8bd2ac3d)
ALTER TABLE `legal_authenticator_phone_number_subscriber` ADD CONSTRAINT `FK9B7A76BBCA2C78EA` FOREIGN KEY (`legal_authenticator_phone_number_subscribers_id`) REFERENCES `legal_authenticator` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-56::kim (generated)::(Checksum: 3:879bc29fd83a4e3870e2d35bef59eaf9)
ALTER TABLE `legal_authenticator_phone_number_subscriber` ADD CONSTRAINT `FK9B7A76BB2BDAAB5E` FOREIGN KEY (`phone_number_subscriber_id`) REFERENCES `phone_number_subscriber` (`id`);

--  Changeset 1_2_0_update.groovy::1411391966757-62::kim (generated)::(Checksum: 3:8a12417930d3fb7ca944462d338ad651)
ALTER TABLE `self_monitoring_sample` ADD CONSTRAINT `FK3F1EEB4EF6EFAB75` FOREIGN KEY (`legal_authenticator_id`) REFERENCES `legal_authenticator` (`id`);

-- ALTER TABLE `kihdb`.`xdsdocument` CHANGE COLUMN `document` `document` TINYBLOB NOT NULL ;

