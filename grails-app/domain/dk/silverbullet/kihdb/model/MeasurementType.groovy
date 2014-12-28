package dk.silverbullet.kihdb.model

class MeasurementType extends AbstractObject {

    String code
    String nkn
    String longName
    String unit

    static constraints = {
        code nullable: false, unique: true
        nkn nullable: false
        longName nullable: false
        unit nullable: true
    }
}
