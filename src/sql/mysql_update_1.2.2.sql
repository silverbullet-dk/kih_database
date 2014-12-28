use kihdb;

ALTER TABLE self_monitoring_sample ADD uploaded_to_labdatabank DATETIME NULL DEFAULT NULL ;

-- Changeset 1_2_2_update.groovy::1418641749140-22::kim (generated)::(Checksum: 3:90b0f58ce3f8cc26db46e05871f94f70)
CREATE TABLE measurement_type (id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NOT NULL, code VARCHAR(1024) NOT NULL, created_by VARCHAR(1024), created_date TIMESTAMP, long_name VARCHAR(1024) NOT NULL, modified_by VARCHAR(1024), modified_date TIMESTAMP, nkn VARCHAR(1024) NOT NULL, unit VARCHAR(1024), CONSTRAINT measurement_tPK PRIMARY KEY (id));

