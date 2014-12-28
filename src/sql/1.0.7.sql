USE [kihdb]
GO
/****** Object:  Table [dbo].[lab_producer]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[lab_producer](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[identifier] [nvarchar](1024) NOT NULL,
	[identifier_code] [nvarchar](1024) NOT NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[iupaccode]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[iupaccode](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[code] [nvarchar](1024) NOT NULL,
	[comment] [nvarchar](1024) NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[description] [nvarchar](1024) NOT NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[name] [nvarchar](1024) NOT NULL,
	[unit] [nvarchar](1024) NOT NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED
(
	[code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[instrument]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[instrument](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[manufacturer] [nvarchar](1024) NULL,
	[med_comid] [nvarchar](1024) NOT NULL,
	[model] [nvarchar](1024) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[product_type] [nvarchar](1024) NULL,
	[software_version] [nvarchar](1024) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[citizen]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[citizen](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[city] [nvarchar](1024) NULL,
	[contact_telephone] [nvarchar](1024) NULL,
	[country] [nvarchar](1024) NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[date_of_birth] [datetime2](7) NOT NULL,
	[email] [nvarchar](1024) NULL,
	[first_name] [nvarchar](1024) NULL,
	[last_name] [nvarchar](1024) NULL,
	[middle_name] [nvarchar](1024) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[sex] [nvarchar](1024) NULL,
	[ssn] [nvarchar](1024) NOT NULL,
	[street_name] [nvarchar](1024) NULL,
	[street_name2] [nvarchar](1024) NULL,
	[zip_code] [nvarchar](1024) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[role]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[authority] [nvarchar](1024) NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED
(
	[authority] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[permission]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[permission](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[permission] [nvarchar](1024) NOT NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[audit_log_controller_entity]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[audit_log_controller_entity](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[action_name] [nvarchar](1024) NOT NULL,
	[controller_name] [nvarchar](1024) NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[number_of_calls] [int] NOT NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[account_expired] [bit] NOT NULL,
	[account_locked] [bit] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[enabled] [bit] NOT NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[password] [nvarchar](1024) NOT NULL,
	[password_expired] [bit] NOT NULL,
	[username] [nvarchar](1024) NOT NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED
(
	[username] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user_role]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_role](
	[role_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED
(
	[role_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[self_monitoring_sample]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[self_monitoring_sample](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[citizen_id] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_by_text] [nvarchar](1024) NOT NULL,
	[created_date] [datetime2](7) NULL,
	[created_date_time] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[sample_category_identifier] [nvarchar](1024) NULL,
	[self_monitoring_sampleuuid] [nvarchar](1024) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[role_permission]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role_permission](
	[permission_id] [bigint] NOT NULL,
	[role_id] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED
(
	[permission_id] ASC,
	[role_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[audit_log_entry]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[audit_log_entry](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[action_id] [bigint] NULL,
	[authority] [nvarchar](1024) NULL,
	[calling_ip] [nvarchar](1024) NULL,
	[correlation_id] [nvarchar](1024) NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[duration] [bigint] NULL,
	[end_date] [datetime2](7) NULL,
	[end_time] [bigint] NULL,
	[exception] [bit] NOT NULL,
	[exception_message] [nvarchar](1024) NULL,
	[http_session_id] [nvarchar](1024) NULL,
	[id_card] [nvarchar](1024) NULL,
	[message_id] [nvarchar](1024) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[operation] [nvarchar](1024) NULL,
	[patient_cpr] [nvarchar](1024) NULL,
	[request] [nvarchar](1024) NULL,
	[response] [nvarchar](1024) NULL,
	[result] [nvarchar](1024) NULL,
	[service] [nvarchar](1024) NULL,
	[start_date] [datetime2](7) NULL,
	[start_time] [bigint] NULL,
	[success] [bit] NOT NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[laboratory_report]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[laboratory_report](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[analysis_text] [nvarchar](1024) NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[created_date_time] [datetime2](7) NOT NULL,
	[health_care_professional_comment] [varchar](max) NULL,
	[instrument_id] [bigint] NULL,
	[iupac_code_id] [bigint] NOT NULL,
	[laboratory_reportuuid] [nvarchar](1024) NOT NULL,
	[measurement_duration] [nvarchar](1024) NULL,
	[measurement_location] [nvarchar](1024) NULL,
	[measurement_scheduled] [nvarchar](1024) NULL,
	[measurement_transferred_by] [nvarchar](1024) NULL,
	[measuring_circumstances] [varchar](max) NULL,
	[measuring_data_classification] [nvarchar](1024) NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[national_sample_identifier] [nvarchar](1024) NOT NULL,
	[producer_of_lab_result_id] [bigint] NOT NULL,
	[result_abnormal_identifier] [nvarchar](1024) NULL,
	[result_encoding_identifier] [nvarchar](1024) NULL,
	[result_maximum_text] [nvarchar](1024) NULL,
	[result_minimum_text] [nvarchar](1024) NULL,
	[result_operator_identifier] [nvarchar](1024) NULL,
	[result_text] [nvarchar](1024) NOT NULL,
	[result_type_of_interval] [nvarchar](1024) NULL,
	[result_unit_text] [nvarchar](1024) NOT NULL,
	[sample_id] [bigint] NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED
(
	[laboratory_reportuuid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[audit_log_parameter]    Script Date: 01/14/2014 09:23:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[audit_log_parameter](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NOT NULL,
	[created_by] [nvarchar](1024) NULL,
	[created_date] [datetime2](7) NULL,
	[entry_id] [bigint] NULL,
	[modified_by] [nvarchar](1024) NULL,
	[modified_date] [datetime2](7) NULL,
	[parameter_key] [nvarchar](1024) NULL,
	[parameter_value] [nvarchar](1024) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  ForeignKey [FK40704C53F84F8915]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[audit_log_entry]  WITH CHECK ADD  CONSTRAINT [FK40704C53F84F8915] FOREIGN KEY([action_id])
REFERENCES [dbo].[audit_log_controller_entity] ([id])
GO
ALTER TABLE [dbo].[audit_log_entry] CHECK CONSTRAINT [FK40704C53F84F8915]
GO
/****** Object:  ForeignKey [FK4D351F0AF5DB4508]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[audit_log_parameter]  WITH CHECK ADD  CONSTRAINT [FK4D351F0AF5DB4508] FOREIGN KEY([entry_id])
REFERENCES [dbo].[audit_log_entry] ([id])
GO
ALTER TABLE [dbo].[audit_log_parameter] CHECK CONSTRAINT [FK4D351F0AF5DB4508]
GO
/****** Object:  ForeignKey [FKE2DD6340145CCC91]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[laboratory_report]  WITH CHECK ADD  CONSTRAINT [FKE2DD6340145CCC91] FOREIGN KEY([sample_id])
REFERENCES [dbo].[self_monitoring_sample] ([id])
GO
ALTER TABLE [dbo].[laboratory_report] CHECK CONSTRAINT [FKE2DD6340145CCC91]
GO
/****** Object:  ForeignKey [FKE2DD6340344CE08C]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[laboratory_report]  WITH CHECK ADD  CONSTRAINT [FKE2DD6340344CE08C] FOREIGN KEY([producer_of_lab_result_id])
REFERENCES [dbo].[lab_producer] ([id])
GO
ALTER TABLE [dbo].[laboratory_report] CHECK CONSTRAINT [FKE2DD6340344CE08C]
GO
/****** Object:  ForeignKey [FKE2DD634072DCB95D]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[laboratory_report]  WITH CHECK ADD  CONSTRAINT [FKE2DD634072DCB95D] FOREIGN KEY([instrument_id])
REFERENCES [dbo].[instrument] ([id])
GO
ALTER TABLE [dbo].[laboratory_report] CHECK CONSTRAINT [FKE2DD634072DCB95D]
GO
/****** Object:  ForeignKey [FKE2DD6340BF822EE4]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[laboratory_report]  WITH CHECK ADD  CONSTRAINT [FKE2DD6340BF822EE4] FOREIGN KEY([iupac_code_id])
REFERENCES [dbo].[iupaccode] ([id])
GO
ALTER TABLE [dbo].[laboratory_report] CHECK CONSTRAINT [FKE2DD6340BF822EE4]
GO
/****** Object:  ForeignKey [FKBD40D5383C5E905D]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKBD40D5383C5E905D] FOREIGN KEY([permission_id])
REFERENCES [dbo].[permission] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKBD40D5383C5E905D]
GO
/****** Object:  ForeignKey [FKBD40D538FBCCF93D]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FKBD40D538FBCCF93D] FOREIGN KEY([role_id])
REFERENCES [dbo].[role] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FKBD40D538FBCCF93D]
GO
/****** Object:  ForeignKey [FK3F1EEB4EA5548A97]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[self_monitoring_sample]  WITH CHECK ADD  CONSTRAINT [FK3F1EEB4EA5548A97] FOREIGN KEY([citizen_id])
REFERENCES [dbo].[citizen] ([id])
GO
ALTER TABLE [dbo].[self_monitoring_sample] CHECK CONSTRAINT [FK3F1EEB4EA5548A97]
GO
/****** Object:  ForeignKey [FK143BF46AFBCCF93D]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FK143BF46AFBCCF93D] FOREIGN KEY([role_id])
REFERENCES [dbo].[role] ([id])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FK143BF46AFBCCF93D]
GO
/****** Object:  ForeignKey [FK143BF46AA0F7BD1D]    Script Date: 01/14/2014 09:23:19 ******/
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FK143BF46AA0F7BD1D] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FK143BF46AA0F7BD1D]
GO
