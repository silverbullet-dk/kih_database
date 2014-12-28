databaseChangeLog = {

    changeSet(author: "km", id: "add_uploaded_to_labdatabank_to_self_monitoring_sample") {
        addColumn(tableName: "self_monitoring_sample") {
            column(name: "uploaded_to_labdatabank", type: '${datetime.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1418641749140-22") {
        createTable(tableName: "measurement_type") {
            column(autoIncrement: "true", name: "id", type: '${integer.type}') {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "measurement_tPK")
            }

            column(name: "version", type: '${integer.type}') {
                constraints(nullable: "false")
            }

            column(name: "code", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "created_by", type: '${string.type}')

            column(name: "created_date", type: '${datetime.type}')

            column(name: "long_name", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "modified_by", type: '${string.type}')

            column(name: "modified_date", type: '${datetime.type}')

            column(name: "nkn", type: '${string.type}') {
                constraints(nullable: "false")
            }

            column(name: "unit", type: '${string.type}')
        }
    }

    changeSet(author: "kim (generated)", id: "1418641749140-74") {
        createIndex(indexName: "code_uniq_1418641749067", tableName: "measurement_type", unique: "true") {
            column(name: "code")
        }
    }

}
