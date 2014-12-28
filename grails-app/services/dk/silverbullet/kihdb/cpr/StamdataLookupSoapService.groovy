/**
 * Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.silverbullet.kihdb.cpr

import dk.nsi._2011._09._23.stamdatacpr.ObjectFactory
import dk.nsi._2011._09._23.stamdatacpr.PersonLookupRequestType
import dk.nsi._2011._09._23.stamdatacpr.PersonLookupResponseType
import dk.nsi._2011._09._23.stamdatacpr.StamdataPersonLookup
import grails.util.Holders
import wslite.soap.SOAPFaultException

import javax.xml.ws.BindingProvider

/**
 * Service class to handle lookup's against StamdataPersonLookup service on NSP.
 */
class StamdataLookupSoapService {
    def grailsApplication
    def sosiService

    private StamdataPersonLookup stamdataService
    private static final ObjectFactory objectFactory = new ObjectFactory()

    private boolean enabled = false

    public StamdataLookupSoapService() {
        if (Holders.grailsApplication.config.cpr.enabled) {
            enabled = Boolean.valueOf(Holders.grailsApplication.config.cpr.enabled)
            log.debug "Stamdata lookups are enabled: " + enabled
        }
    }

    /**
     * Work around to not screw up spring wiring between cxf and cxf-client plugin
     * @return The bean for the SOAP endpoint
     */
    private StamdataPersonLookup getClient() {
        if (!stamdataService) {
            stamdataService = Holders.grailsApplication.getMainContext().getBean("stamdataPersonLookupClient")
        }
        return stamdataService
    }

    /**
     * Invokes the Stamdata service. Returns the PersonLookupResponseType object
     * @param ssn The SSN to be searched for
     * @return
     */
    PersonLookupResponseType invokeStamdataLookup(String ssn) {
        PersonLookupResponseType retVal = null

        if (enabled) {
            PersonLookupRequestType msg = objectFactory.createPersonLookupRequestType()
            msg.civilRegistrationNumberPersonQuery = ssn

            try {
                BindingProvider provider = (BindingProvider) getClient()
                sosiService.exchangeAndSignIDCard(provider)
                retVal = getClient().getPersonDetails(msg)
            } catch (SOAPFaultException e) {
                log.error "Caught SOAP exception calling CPR service: " + e.getMessage() + " - caused by : " + e.getCause()
            } catch (Exception e) {
                log.error "Caught exception calling CPR service: " + e.getMessage() + " - caused by : " + e.getCause()
            }
        } else {
            log.debug "Lookups are disabled from configuration"
        }
        return retVal
    }
}
