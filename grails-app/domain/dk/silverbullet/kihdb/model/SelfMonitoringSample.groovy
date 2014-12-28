package dk.silverbullet.kihdb.model

import dk.silverbullet.kihdb.model.phmr.Author
import dk.silverbullet.kihdb.model.phmr.Custodian
import dk.silverbullet.kihdb.model.phmr.LegalAuthenticator

class SelfMonitoringSample extends AbstractObject {

    static hasMany = [reports: LaboratoryReport]

    String selfMonitoringSampleUUID;
    Date createdDateTime;
    String sampleCategoryIdentifier;
    String createdByText;
    Citizen citizen
    String xdsDocumentUuid
    boolean xdsConversionStarted
    boolean xdsRegisteredInRegistry
    Author author
    Custodian custodian
    LegalAuthenticator legalAuthenticator
    Date uploadedToLabdatabank

    boolean deleted

    static constraints = {
        selfMonitoringSampleUUID nullable: true
        createdDateTime nullable: true
        sampleCategoryIdentifier nullable: true
        createdByText nullable: false
        citizen nullable: false
        xdsDocumentUuid nullable: true
        xdsConversionStarted nullable: true
        xdsRegisteredInRegistry nullable: true
        deleted nullable: true
        author nullable: true
        custodian nullable: true
        legalAuthenticator nullable: true
        uploadedToLabdatabank nullable: true
    }

//    static mapping = {
//        reports cascade: "all-delete-orphan"
//    }

    Date getEarliestDate() {
        Date retVal = null

        reports.each { report ->
            if (retVal == null) {
                retVal = report.createdDateTime
            }
            if (report.createdDateTime.before(retVal)) {
                retVal = report.createdDateTime
            }
        }

        retVal
    }

    Date getLatestDate() {
        Date retVal = null

        reports.each { report ->
            if (retVal == null) {
                retVal = report.createdDateTime
            }
            if (report.createdDateTime.after(retVal)) {
                retVal = report.createdDateTime
            }
        }

        retVal
    }

    List<String> getMeasurementTypes() {
        def retVal = []

        if (this.sampleCategoryIdentifier) {
            retVal.add(this.sampleCategoryIdentifier)
        } else {
            for (report in reports) {
                retVal.add(report.iupacCode.code)
            }
        }
        return retVal
    }
}
