package kih_database.dk.silverbullet.kihdb.xds

import com.ctc.wstx.io.EBCDICCodec
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.model.xds.XDSMetadata
import dk.silverbullet.kihdb.xds.EbXmlBuilder
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType

class DetectsMissingDocumentsJob {

    def documentTransformationService
    def documentRegistryService


    def grailsApplication
    def concurrent = false

    static triggers = {}

    def execute() {
        def samplesToProcess = SelfMonitoringSample.findAllByXdsConversionStartedAndXdsRegisteredInRegistry(false,false)

        def startTime = System.currentTimeMillis()
        log.debug "Starting conversion of " + samplesToProcess.size() + " unconverted samples"

        for (sample in samplesToProcess) {
            log.debug "Converting " + sample
            documentTransformationService.createXdsFromSelfMonitoringSample(sample)
        }

        log.debug "Completed conversion of " + samplesToProcess.size() + " samples to XDS documents in " + (System.currentTimeMillis() - startTime) + " milliseconds"

        // Check to see if registry is available
        if (documentRegistryService.isEnabled()) {
            samplesToProcess = SelfMonitoringSample.findAllByXdsConversionStartedAndXdsRegisteredInRegistry(true,false)
            startTime = System.currentTimeMillis()
            log.debug "Starting registration of unregistered samples with documents for " + samplesToProcess.size() + " samples"

            for (sample in samplesToProcess) {
                log.debug "Converting " + sample
                documentTransformationService.registerSelfMonintoringSampleInRegistry(sample)
            }

            log.debug "Completed conversion of " + samplesToProcess.size() + " samples to XDS documents in " + (System.currentTimeMillis() - startTime) + " milliseconds"


            samplesToProcess = SelfMonitoringSample.findAllByDeletedAndXdsRegisteredInRegistry(true,false)

            startTime = System.currentTimeMillis()
            log.debug "Starting deprecation of " + samplesToProcess.size() + " samples"

            for (sample in samplesToProcess) {
                log.debug "Deprecating " + sample

                documentTransformationService.handleDeletion(sample)
            }

            log.debug "Completed deprecation of " + samplesToProcess.size() + " samples to XDS documents in " + (System.currentTimeMillis() - startTime) + " milliseconds"

            def xdsDocuments = XDSMetadata.findAllByRegistered(false)

            log.debug "Starting registration of " + xdsDocuments.size() + " xds documents"
            int count = 0
            for (doc in xdsDocuments) {
                SubmitObjectsRequest request = new EbXmlBuilder().convertStringToSubmitObjectsRequest(doc.data)

                if (request) {
                    log.debug "Registring document set.: " + doc.id
                    RegistryResponseType responseType = documentRegistryService.updateRegistry(request)

                    if (responseType && Constants.XDS_REGISTRY_SUCCESSFULLY_REGISTERED.equals(responseType?.status)) {
                        doc = doc.refresh()
                        doc.registered = true
                        if (!doc.save(flush)) {
                            log.error "COuld not update document. Errors:  " + doc.errors
                        } else {
                            count++
                        }
                    }
                }

            }

            log.debug "Registered " + count + " of " + xdsDocuments.size() + " documents"
        }
    }
}
