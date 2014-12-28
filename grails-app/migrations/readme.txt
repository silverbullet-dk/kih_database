Kør:
dbm-generate-gorm-changelog 1_2_2_up.groovy

rename:
"bigint"            '${integer.type}'
"varchar(512)"      '${string.type}'
"timestamp"         '${datetime.type}'
"boolean"           '${boolean.type}'
"longvarchar"       '${string2048.type}'
"integer"           '${integer.type}'
"blob"              '${blob.type}'
