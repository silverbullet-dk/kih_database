-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: changelog.groovy
-- Ran at: 03-10-14 12:29
-- Against: SA@jdbc:h2:mem:kihdb
-- Liquibase version: 2.0.5
-- *********************************************************************
CREATE TABLE `statistic` (`id` INT AUTO_INCREMENT NOT NULL, `version` INT NOT NULL, `created_by` VARCHAR(1024) NULL, `created_date` DATETIME NULL, `creator` VARCHAR(1024) NOT NULL, `modified_by` VARCHAR(1024) NULL, `modified_date` DATETIME NULL, `statistic_time` DATETIME NOT NULL, `statistic_type` VARCHAR(1024) NOT NULL, `value` INT NOT NULL, CONSTRAINT `statisticPK` PRIMARY KEY (`id`)) ENGINE=InnoDB;
