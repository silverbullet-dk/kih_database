package dk.silverbullet.kihdb.services

import dk.dsdn.laboratoriemedicinbank.IVirtuelLaboratoriebankService
import dk.silverbullet.kihdb.labdatabank.LabdataBuilder
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import grails.transaction.Transactional
import grails.util.Holders

import javax.xml.namespace.QName
import javax.xml.ws.Service

@Transactional
class LabdatabankService {

    def grailsApplication

    private boolean enabled = false
    private static String labdatabankServiceURL
    private static String namespaceURI
    private static String localPart
    private static String username
    private static String password

    private static IVirtuelLaboratoriebankService laboratoriebankService

    public LabdatabankService() {
        log.debug "Initializing " + this.class.name

        if (Holders.grailsApplication.config.uploadLabdatabankJob.enabled) {
            enabled = Boolean.valueOf(Holders.grailsApplication.config.uploadLabdatabankJob.enabled)
        }
        log.debug "Updates are enabled: " + enabled
        if (enabled && Holders.grailsApplication.config.uploadLabdatabankJob.url) {
            labdatabankServiceURL = Holders.grailsApplication.config.uploadLabdatabankJob.url
            log.debug "labdatabankServiceURL: " + labdatabankServiceURL
        } else {
            log.info "URL is not specified. Disabling updates. Please specify: 'uploadLabdatabankJob.url' "
            enabled = false
        }

        if (enabled && Holders.grailsApplication.config.uploadLabdatabankJob.namespaceURI) {
            namespaceURI = Holders.grailsApplication.config.uploadLabdatabankJob.namespaceURI
            log.debug "NamespaceURI: " + namespaceURI
        } else {
            log.info "NamespaceURI is not specified. Disabling updates. Please specify: 'uploadLabdatabankJob.namespaceURI' "
            enabled = false
        }

        if (enabled && Holders.grailsApplication.config.uploadLabdatabankJob.localPart) {
            localPart = Holders.grailsApplication.config.uploadLabdatabankJob.localPart
            log.debug "LocalPart: " + localPart
        } else {
            log.info "LocalPart is not specified. Disabling updates. Please specify: 'uploadLabdatabankJob.localPart' "
            enabled = false
        }

        if (enabled && Holders.grailsApplication.config.uploadLabdatabankJob.username) {
            username = Holders.grailsApplication.config.uploadLabdatabankJob.username
            log.debug "Username: " + username
        } else {
            log.info "Username is not specified. Disabling updates. Please specify: 'uploadLabdatabankJob.username' "
            enabled = false
        }

        if (enabled && Holders.grailsApplication.config.uploadLabdatabankJob.password) {
            password = Holders.grailsApplication.config.uploadLabdatabankJob.password
            log.debug "Password: " + password
        } else {
            log.info "Password is not specified. Disabling updates. Please specify: 'uploadLabdatabankJob.password' "
            enabled = false
        }

        if (enabled) {
            try {
                URL wsdlURL = new URL(labdatabankServiceURL)
                Service service = Service.create(wsdlURL, new QName(namespaceURI, localPart))
                laboratoriebankService = service.getPort(IVirtuelLaboratoriebankService.class)
            } catch (Exception e) {
                log.error('Error setting up connection to labdatabank...', e)
            }
        }
    }

    /**
     * Returns true is service has detected, that registry is available. Tries to reconnect to registry to see if
     * it's available
     */
    public boolean isEnabled() {
        return enabled
    }

    def populating(String analysesvar) {

        if (enabled)
            laboratoriebankService?.gemAnalysesvar(analysesvar, username, password);
    }

    def populateData() {
        log.debug 'Populating Data...'
        if (enabled) {
            Date now = new Date()
            def samplesToProcess = SelfMonitoringSample.findAllByUploadedToLabdatabankIsNull(max: 1000)

            def startTime = System.currentTimeMillis()
            log.info "Starting conversion of " + samplesToProcess.size() + " unconverted samples"

            samplesToProcess.each {
                def emessage = LabdataBuilder.getEmessage(it)
                def xml = LabdataBuilder.convertToString(emessage)
//                log.debug "Got: \n" + xml + '\n'
                try {
                    populating(xml)
                } catch (Exception e) {
                    // log.debug 'fejl ved upload:' + e.getCause()
                    // 2014-12-12 11:11:38,097 [main] DEBUG labdatabank.LabdataBuilderSpec  - fejl ved upload:org.apache.cxf.binding.soap.SoapFault: The 'LaboratoryReport' start tag on line 1 position 257 does not match the end tag of 'Emessage'. Line 1, position 2619.| Completed 1 unit test, 0 failed in 0m 5s
                    // log.debug 'fejl ved upload:' + e.getMessage()
                    // 2014-12-12 11:12:36,875 [main] DEBUG labdatabank.LabdataBuilderSpec  - fejl ved upload:The 'LaboratoryReport' start tag on line 1 position 257 does not match the end tag of 'Emessage'. Line 1, position 2619.| Completed 1 unit test, 0 failed in 0m 6s
                    log.error 'Fejl ved upload (se XML næste linie):', e
                    log.error 'Calling with XML:' + xml
                    return
                }
                it.uploadedToLabdatabank = now
                it.save(validate: true, flush: true)
            }

            log.info "Completed uploading of " + samplesToProcess.size() + " samples to labdatabanken in " + (System.currentTimeMillis() - startTime) + " milliseconds"
        } else
            log.debug 'Not enabled...'
    }
}
