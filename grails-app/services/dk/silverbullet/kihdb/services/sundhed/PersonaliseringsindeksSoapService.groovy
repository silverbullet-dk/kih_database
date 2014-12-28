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
package dk.silverbullet.kihdb.services.sundhed

import dk.sundhed._2009._01._01.personaliseringsindeks.kontrakt.IPersonaliseringsindeksService
import grails.util.Holders
import wslite.soap.SOAPClient

import javax.xml.namespace.QName

class PersonaliseringsindeksSoapService {

    private boolean enabled = false
    private URL personaliseringsIndeksURL
    private IPersonaliseringsindeksService personaliseringsIndeksService
    private String serviceName = "PersonaliseringsIndeksEndpointService"
    private static String personaliseringsNamespace = "http://sundhed.dk/2009/01/01/personaliseringsindeks"

    private Integer applicationId = 532

    private String username
    private String password


    private SOAPClient soapClient


    private String generateSubmitRequest(Integer applicationId, String cpr) {
        return """
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Header>
    <wsse:Security
      xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
      xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
      soap:mustUnderstand="1">
       <wsse:UsernameToken wsu:Id="UsernameToken-1">
         <wsse:Username>${username}</wsse:Username>
         <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">${
            password
        }</wsse:Password>
      </wsse:UsernameToken>
     </wsse:Security>
  </soap:Header>
  <soap:Body>
   <SubmitPersonalization xmlns="http://sundhed.dk/2009/01/01/personaliseringsindeks/kontrakt" xmlns:ns2="http://schemas.microsoft.com/2003/10/Serialization/Arrays" xmlns:ns3="http://schemas.microsoft.com/2003/10/Serialization/">
    <personalizationType>
     <PersonCivilRegistrationIdentifier>${cpr}</PersonCivilRegistrationIdentifier>
     <RecipientType>1</RecipientType>
     <Url>
      <ApplicationId>${applicationId.toString()}</ApplicationId>
      <ShortName>KIHDB</ShortName>
     </Url>
    </personalizationType>
   </SubmitPersonalization>
 </soap:Body>
</soap:Envelope>
    """
    }


    private
    final QName securityQname = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse")

    /**
     * Constructor. Reads configuration values and configures service.
     */
    PersonaliseringsindeksSoapService() {
        log.debug "Initializing " + this.class.name

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.personaliseringsindex?.enabled) {
            enabled = Boolean.valueOf(Holders.grailsApplication.config.sundhed.dk.personaliseringsindex.enabled)
            log.debug "Updates are enabled: " + enabled
        }

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.personaliseringsindex?.url) {
            personaliseringsIndeksURL = new URL(Holders.grailsApplication.config.sundhed.dk.personaliseringsindex.url)
            log.debug "URL: " + personaliseringsIndeksURL.toString()
        } else {
            log.info "URL is not specified. Disabling updates. Please specify: 'sundhed.dk.personaliseringsindex.url' "
            enabled = false
        }

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.personaliseringsindex?.serviceName) {
            serviceName = Holders.grailsApplication.config.sundhed.dk.personaliseringsindex.serviceName
        }

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.personaliseringsindex?.serviceNamespace) {
            personaliseringsNamespace = Holders.grailsApplication.config.sundhed.dk.personaliseringsindex.serviceNamespace
        }

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.username) {
            username = Holders?.grailsApplication?.config?.sundhed?.dk?.username
        }
        if (Holders?.grailsApplication?.config?.sundhed?.dk?.password) {
            password = Holders?.grailsApplication?.config?.sundhed?.dk?.password
        }

        if (Holders?.grailsApplication?.config?.sundhed?.dk?.application?.id) {
            applicationId = Integer.valueOf(Holders?.grailsApplication?.config?.sundhed?.dk?.application?.id)
        }

        if (enabled) {
//            getServiceProxy()        .
            log.info "Setting up for URL" + personaliseringsIndeksURL.toString()
            soapClient = new SOAPClient(personaliseringsIndeksURL.toString())
        }


    }

    /**
     * Determines, if service is enabled by calling method to either return the proxy object or create it.
     * @return True if configured and ready for use
     */
    public boolean isEnabled() {
        return enabled
    }

    /**
     * Invoke the method on personaliseringsindeks
     * @param input
     * @return
     */
    boolean updatePersonaliseringsindeks(String ssn) {
        def retVal = false

        if (enabled) {
            try {
                def soapRequest = generateSubmitRequest(applicationId, ssn)
                log.debug "Sending: \n" + soapRequest

                def response = soapClient.send(SOAPAction: "http://sundhed.dk/2009/01/01/personaliseringsindeks/kontrakt/IPersonaliseringsindeksService/SubmitPersonalization",
                        soapRequest)
                log.info "Personalization result: " + response?.SubmitPersonalizationResponse?.SubmitPersonalizationResult.toString()
                if (BigInteger.ZERO == Integer.parseInt(response?.SubmitPersonalizationResponse?.SubmitPersonalizationResult.toString())) {
                    retVal = true
                } else {
                    log.warn "Got return code: " + response
                }
            } catch (Exception e) {
                log.error "Detailed message: " + e?.getCause()?.getMessage()
                println "E: " + e.message + " " + e?.getCause()?.getMessage()
                log.error "Caught exception. " + e.message
            }

        } else {
            log.info "Updates to sundhed.dk PersonaliseringsindeksService is not enabled."
        }

        return retVal
    }
}
