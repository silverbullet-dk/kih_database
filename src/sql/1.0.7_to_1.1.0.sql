ALTER TABLE [dbo].[citizen]
  ADD [in_personalization_index] [bit] NOT NULL DEFAULT 0;

CREATE TABLE [dbo].[xdsmetadata](
  [id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [version] [bigint] NOT NULL,
  [data] [text] NOT NULL,
  [registered] [bit] NULL,
  [created_by] [nvarchar](1024) NULL,
  [created_date] [datetime] NULL,
  [modified_by] [nvarchar](1024) NULL,
  [modified_date] [datetime] NULL);

CREATE TABLE [dbo].[xdsdocument](
  [id] [bigint] IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [version] [bigint] NOT NULL,
  [created_by] [nvarchar](1024) NULL,
  [created_date] [datetime] NULL,
  [document] [varbinary](max) NOT NULL,
  [metadata_id] [bigint] NULL,
  [mime_type] [nvarchar](1024) NULL,
  [modified_by] [nvarchar](1024) NULL,
  [modified_date] [datetime] NULL,
  [uuid] [nvarchar](1024) NOT NULL);

Alter TABLE [dbo].[laboratory_report]
  ADD [deleted] [bit] NOT NULL DEFAULT 0;

Alter TABLE [dbo].[self_monitoring_sample]
  ADD [deleted] [bit] NOT NULL DEFAULT 0;

Alter TABLE [dbo].[self_monitoring_sample]
  ADD [xds_conversion_completed] [bit] NOT NULL DEFAULT 0;

ALTER TABLE [dbo].[self_monitoring_sample]
  ADD [xds_conversion_started] [bit] NOT NULL DEFAULT 0;

ALTER TABLE [dbo].[self_monitoring_sample]
  ADD [xds_registered_in_registry] [bit] NOT NULL DEFAULT 0;

Alter TABLE [dbo].[self_monitoring_sample]
  ADD [xds_document_uuid] [nvarchar](1024) NULL;

Alter TABLE [dbo].[xdsdocument] ADD FOREIGN KEY([metadata_id])
  REFERENCES [dbo].[xdsmetadata] ([id]);

