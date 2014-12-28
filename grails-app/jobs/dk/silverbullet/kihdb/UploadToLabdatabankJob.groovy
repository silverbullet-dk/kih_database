package dk.silverbullet.kihdb

class UploadToLabdatabankJob {

    def labdatabankService

    def concurrent = false
    static triggers = {}

    def execute() {
        log.debug "Check for upload til labdatabank..."
        labdatabankService.populateData()
    }
}
