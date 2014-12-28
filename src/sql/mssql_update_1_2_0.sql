-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 25-09-14 13:22
-- Against: kihdb@jdbc:jtds:sqlserver://127.0.0.1:1433:kihdb
-- Liquibase version: 2.0.5
-- *********************************************************************

--  Changeset 1_2_0_update.groovy::add_xds_document_to_self_monitoring_sample::km::(Checksum: 3:d54bf826f24dff41c8d2a731e3c77144)
ALTER TABLE [dbo].[self_monitoring_sample] ADD [author_id] BIGINT
GO

ALTER TABLE [dbo].[self_monitoring_sample] ADD [custodian_id] BIGINT
GO

ALTER TABLE [dbo].[self_monitoring_sample] ADD [legal_authenticator_id] BIGINT
GO

-- Changeset 1_2_0_update.groovy::1411391966757-1::kim (generated)::(Checksum: 3:a9f18cde87e6b9c317a9cf05fd1b012d)
CREATE TABLE [dbo].[address_postal] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [country_identification_code_type_scheme] NVARCHAR(1024), [country_identification_code_type_value] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] DATETIME, [district_name] NVARCHAR(1024), [district_subdivision_identifier] NVARCHAR(1024), [floor_identifier] NVARCHAR(1024), [mail_delivery_sublocation_identifier] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [municipality_name] NVARCHAR(1024), [post_code_identifier] NVARCHAR(1024), [post_office_box_identifier] NVARCHAR(1024), [street_building_identifier] NVARCHAR(1024), [street_name] NVARCHAR(1024), [street_name_for_addressing_name] NVARCHAR(1024), [suite_identifier] NVARCHAR(1024), CONSTRAINT [address_postaPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-5::kim (generated)::(Checksum: 3:1722a4b7770cb8e2b8c4278a087489f0)
CREATE TABLE [dbo].[author] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [address_postal_type_id] BIGINT, [assigned_person_given_name] NVARCHAR(1024), [assigned_person_middle_name] NVARCHAR(1024), [assigned_person_surname_name] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] DATETIME, [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [represented_organization] NVARCHAR(1024), [time] DATETIME, CONSTRAINT [authorPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-6::kim (generated)::(Checksum: 3:6a36830677426f1f7c0e2b4dd4c99bf4)
CREATE TABLE [dbo].[author_email_address] ([author_email_address_id] BIGINT, [email_address_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-7::kim (generated)::(Checksum: 3:37680d4bf1a9f2c74bafeeaa8504b662)
CREATE TABLE [dbo].[author_id_tp] ([author_ids_id] BIGINT, [id_tp_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-8::kim (generated)::(Checksum: 3:e9eeddbaba088fa160d65115a5e0bb14)
CREATE TABLE [dbo].[author_phone_number_subscriber] ([author_phone_number_subscribers_id] BIGINT, [phone_number_subscriber_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-10::kim (generated)::(Checksum: 3:e8d2777c86db1b14a2d1ac0947f28cc4)
CREATE TABLE [dbo].[custodian] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [address_id] BIGINT, [created_by] NVARCHAR(1024), [created_date] DATETIME, [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [name] NVARCHAR(1024), [phone_number_subscriber_id] BIGINT, CONSTRAINT [custodianPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-11::kim (generated)::(Checksum: 3:e5fcd4279199c372a22bf77582eafecf)
CREATE TABLE [dbo].[custodian_id_tp] ([custodian_ids_id] BIGINT, [id_tp_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-12::kim (generated)::(Checksum: 3:3b1f05e52062f1c0078e31f3d11fcd9e)
CREATE TABLE [dbo].[email_address] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] DATETIME, [email_address_identifier] NVARCHAR(1024), [email_address_use] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] DATETIME, CONSTRAINT [email_addressPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-13::kim (generated)::(Checksum: 3:d8882904256c052a0ce69a7eb994255a)
CREATE TABLE [dbo].[id_tp] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] DATETIME, [identifier] NVARCHAR(1024), [identifier_code] NVARCHAR(1024), [modified_by] NVARCHAR(1024), [modified_date] DATETIME, CONSTRAINT [id_tpPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-18::kim (generated)::(Checksum: 3:4cfd8ae92fd93f6d05a5d4d792df3916)
CREATE TABLE [dbo].[legal_authenticator] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [address_id] BIGINT, [assigned_person_given_name] NVARCHAR(1024), [assigned_person_middle_name] NVARCHAR(1024), [assigned_person_surname_name] NVARCHAR(1024), [created_by] NVARCHAR(1024), [created_date] DATETIME, [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [represented_organization_name] NVARCHAR(1024), [signature_code] NVARCHAR(1024), [time] DATETIME, CONSTRAINT [legal_authentPK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-19::kim (generated)::(Checksum: 3:c3e9af5dc16c5ce548b746db417bc7fe)
CREATE TABLE [dbo].[legal_authenticator_email_address] ([legal_authenticator_email_address_id] BIGINT, [email_address_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-20::kim (generated)::(Checksum: 3:362aeaac33efa1a30af97efd765557fb)
CREATE TABLE [dbo].[legal_authenticator_id_tp] ([legal_authenticator_ids_id] BIGINT, [id_tp_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-21::kim (generated)::(Checksum: 3:37af4a7634db1ddc5323c3391a7a7594)
CREATE TABLE [dbo].[legal_authenticator_phone_number_subscriber] ([legal_authenticator_phone_number_subscribers_id] BIGINT, [phone_number_subscriber_id] BIGINT)
GO

-- Changeset 1_2_0_update.groovy::1411391966757-23::kim (generated)::(Checksum: 3:9f3fe1c72b868608a2606fac1d67d09c)
CREATE TABLE [dbo].[phone_number_subscriber] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] DATETIME, [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [phone_number_identifier] NVARCHAR(1024), [phone_number_use] NVARCHAR(1024), CONSTRAINT [phone_number_PK] PRIMARY KEY ([id]))
GO

-- Changeset 1_2_0_update.groovy::1411391966757-72::kim (generated)::(Checksum: 3:2fa4f41c421c32b7d4f212efd87e6196)
CREATE UNIQUE INDEX [authority_uniq_1411391966702] ON [dbo].[role]([authority])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-73::kim (generated)::(Checksum: 3:3e83a49376a52b275403c58b4654ac5f)
CREATE UNIQUE INDEX [username_uniq_1411391966707] ON [dbo].[users]([username])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-74::kim (generated)::(Checksum: 3:9858a8c780063f552562cc5a902e991f)
CREATE UNIQUE INDEX [uuid_uniq_1411391966708] ON [dbo].[xdsdocument]([uuid])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-35::kim (generated)::(Checksum: 3:e8863d86a9545f3a0865c760a99f9ec1)
ALTER TABLE [dbo].[author] ADD CONSTRAINT [FKAC2D218B35453988] FOREIGN KEY ([address_postal_type_id]) REFERENCES [dbo].[address_postal] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-36::kim (generated)::(Checksum: 3:139d153453a9ecd1a21254f39f22bf19)
ALTER TABLE [dbo].[author_email_address] ADD CONSTRAINT [FKFB2169D3FBEE896] FOREIGN KEY ([author_email_address_id]) REFERENCES [dbo].[author] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-37::kim (generated)::(Checksum: 3:af13f820974bb281330027b35aea9b3c)
ALTER TABLE [dbo].[author_email_address] ADD CONSTRAINT [FKFB2169D766DFD0F] FOREIGN KEY ([email_address_id]) REFERENCES [dbo].[email_address] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-38::kim (generated)::(Checksum: 3:bb518dca2c2b0a6753fc972b3d50c02e)
ALTER TABLE [dbo].[author_id_tp] ADD CONSTRAINT [FK71B8412A39FAC7CF] FOREIGN KEY ([author_ids_id]) REFERENCES [dbo].[author] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-39::kim (generated)::(Checksum: 3:2f2674c2ccb54f6567b231d40ada776d)
ALTER TABLE [dbo].[author_id_tp] ADD CONSTRAINT [FK71B8412A3DA2211F] FOREIGN KEY ([id_tp_id]) REFERENCES [dbo].[id_tp] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-40::kim (generated)::(Checksum: 3:3797ce694c20de525d4b24f58659f5ce)
ALTER TABLE [dbo].[author_phone_number_subscriber] ADD CONSTRAINT [FKE3196BD958FB7E59] FOREIGN KEY ([author_phone_number_subscribers_id]) REFERENCES [dbo].[author] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-41::kim (generated)::(Checksum: 3:1f58b23ab83458866756d4ef3c8b6031)
ALTER TABLE [dbo].[author_phone_number_subscriber] ADD CONSTRAINT [FKE3196BD92BDAAB5E] FOREIGN KEY ([phone_number_subscriber_id]) REFERENCES [dbo].[phone_number_subscriber] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-42::kim (generated)::(Checksum: 3:3def431fdb0d35149a3c70abc9c84eb6)
ALTER TABLE [dbo].[custodian] ADD CONSTRAINT [FK600A71EE7A31A657] FOREIGN KEY ([address_id]) REFERENCES [dbo].[address_postal] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-43::kim (generated)::(Checksum: 3:725c1311f66ced7bb38e41db4841370a)
ALTER TABLE [dbo].[custodian] ADD CONSTRAINT [FK600A71EE2BDAAB5E] FOREIGN KEY ([phone_number_subscriber_id]) REFERENCES [dbo].[phone_number_subscriber] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-44::kim (generated)::(Checksum: 3:b2b8bc75e452f23dd3b3defabbfd2c8e)
ALTER TABLE [dbo].[custodian_id_tp] ADD CONSTRAINT [FKB1BC7E8DFFF6C593] FOREIGN KEY ([custodian_ids_id]) REFERENCES [dbo].[custodian] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-45::kim (generated)::(Checksum: 3:16c9adba194fa5fa3f99f98c67fecc05)
ALTER TABLE [dbo].[custodian_id_tp] ADD CONSTRAINT [FKB1BC7E8D3DA2211F] FOREIGN KEY ([id_tp_id]) REFERENCES [dbo].[id_tp] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-50::kim (generated)::(Checksum: 3:c4c02c9c087be150f533fc2327da75f3)
ALTER TABLE [dbo].[legal_authenticator] ADD CONSTRAINT [FK641A326D7A31A657] FOREIGN KEY ([address_id]) REFERENCES [dbo].[address_postal] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-51::kim (generated)::(Checksum: 3:92c243ea32beabcf1f29ef6977315a0e)
ALTER TABLE [dbo].[legal_authenticator_email_address] ADD CONSTRAINT [FKEF36F3FF766DFD0F] FOREIGN KEY ([email_address_id]) REFERENCES [dbo].[email_address] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-52::kim (generated)::(Checksum: 3:770790ad140c741706e2bedf60e93fb8)
ALTER TABLE [dbo].[legal_authenticator_email_address] ADD CONSTRAINT [FKEF36F3FF9E51A8A3] FOREIGN KEY ([legal_authenticator_email_address_id]) REFERENCES [dbo].[legal_authenticator] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-53::kim (generated)::(Checksum: 3:810d5ef372d4bc7503851cacc8bc5053)
ALTER TABLE [dbo].[legal_authenticator_id_tp] ADD CONSTRAINT [FK17FF500C3DA2211F] FOREIGN KEY ([id_tp_id]) REFERENCES [dbo].[id_tp] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-54::kim (generated)::(Checksum: 3:0eb32b47d1b9dc563497c6cd9b497b7b)
ALTER TABLE [dbo].[legal_authenticator_id_tp] ADD CONSTRAINT [FK17FF500C2CF9855C] FOREIGN KEY ([legal_authenticator_ids_id]) REFERENCES [dbo].[legal_authenticator] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-55::kim (generated)::(Checksum: 3:db716601944dd667e30b4e3f8bd2ac3d)
ALTER TABLE [dbo].[legal_authenticator_phone_number_subscriber] ADD CONSTRAINT [FK9B7A76BBCA2C78EA] FOREIGN KEY ([legal_authenticator_phone_number_subscribers_id]) REFERENCES [dbo].[legal_authenticator] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-56::kim (generated)::(Checksum: 3:879bc29fd83a4e3870e2d35bef59eaf9)
ALTER TABLE [dbo].[legal_authenticator_phone_number_subscriber] ADD CONSTRAINT [FK9B7A76BB2BDAAB5E] FOREIGN KEY ([phone_number_subscriber_id]) REFERENCES [dbo].[phone_number_subscriber] ([id])
GO

-- Changeset 1_2_0_update.groovy::1411391966757-62::kim (generated)::(Checksum: 3:8a12417930d3fb7ca944462d338ad651)
ALTER TABLE [dbo].[self_monitoring_sample] ADD CONSTRAINT [FK3F1EEB4EF6EFAB75] FOREIGN KEY ([legal_authenticator_id]) REFERENCES [dbo].[legal_authenticator] ([id])
GO
