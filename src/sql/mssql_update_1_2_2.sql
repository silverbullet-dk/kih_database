ALTER TABLE [dbo].[self_monitoring_sample] ADD uploaded_to_labdatabank DATETIME NULL DEFAULT NULL ;

CREATE TABLE [dbo].[measurement_type] (
  [id] BIGINT IDENTITY NOT NULL,
  [version] BIGINT NOT NULL,
  [code] NVARCHAR(1024) NOT NULL,
  [created_by] NVARCHAR(1024),
  [created_date] DATETIME,
  [long_name] NVARCHAR(1024) NOT NULL,
  [modified_by] NVARCHAR(1024),
  [modified_date] DATETIME,
  [nkn] NVARCHAR(1024) NOT NULL,
  [unit] NVARCHAR(1024),
  CONSTRAINT [measurement_tPK]
  PRIMARY KEY ([id]))
GO

