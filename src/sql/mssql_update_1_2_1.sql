-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 03-10-14 12:29
-- Against: SA@jdbc:h2:mem:kihdb
-- Liquibase version: 2.0.5
-- *********************************************************************
CREATE TABLE [dbo].[statistic] ([id] BIGINT IDENTITY NOT NULL, [version] BIGINT NOT NULL, [created_by] NVARCHAR(1024), [created_date] DATETIME, [creator] NVARCHAR(1024) NOT NULL, [modified_by] NVARCHAR(1024), [modified_date] DATETIME, [statistic_time] DATETIME NOT NULL, [statistic_type] NVARCHAR(1024) NOT NULL, [value] BIGINT NOT NULL, CONSTRAINT [statisticPK] PRIMARY KEY ([id]))

