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
package dk.silverbullet.kihdb.services.xds

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.exception.xds.RegistryException
import dk.sosi.seal.model.Request
import grails.util.Holders
import ihe.iti.xds_b._2007.DocumentRegistryPortType
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.ws.Service
import javax.xml.ws.soap.AddressingFeature
import javax.xml.ws.soap.SOAPFaultException
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class DocumentRegistryService {
    def sosiService

    def grailsApplication

    private boolean enabled = false

    boolean updateRegistry = false

    boolean useHttpProxy = false

    private String proxyHost
    private String proxyPort
    private String proxyUsername
    private String proxyPassword


    private URL documentRegistryURL
    private DocumentRegistryPortType documentRegistry
    private String serviceName = "DocumentRegistryEndpointService"
    private static final String documentRegistryNamespace = "urn:ihe:iti:xds-b:2007"


    private QName securityQname = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse")
    private QName medcomQname = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Header", "medcom")

    // Setting up for XPath
    private XPath xPath = XPathFactory.newInstance().newXPath();

    // Define the parts
    private String soapEnvelopeXpath = "*[local-name()='Envelope'and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']"
    private String soapHeaderXpath = "*[local-name()='Header' and namespace-uri()='http://schemas.xmlsoap.org/soap/envelope/']"
    private String securityXpath = "*[local-name()='${securityQname.localPart}' and namespace-uri()='${securityQname.namespaceURI}']"
    private String medcomLocalXpath = "*[local-name()='${medcomQname.localPart}' and namespace-uri()='${medcomQname.namespaceURI}']"

    // Define the expressions
    private String wsseXpath = "/${soapEnvelopeXpath}/${soapHeaderXpath}/${securityXpath}"
    private String medcomXpath = "/${soapEnvelopeXpath}/${soapHeaderXpath}/${medcomLocalXpath}"

    /**
     * Default constructor takes care of setting up default values etc.
     */

    public DocumentRegistryService() {
        log.debug "Initializing " + this.class.name

        if (Holders.grailsApplication.config.xds.documentregistry.updateRegistry) {
            enabled = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.updateRegistry)
            log.debug "Updates are enabled: " + enabled
        }

        if (Holders.grailsApplication.config.xds.documentregistry.url) {
            documentRegistryURL = new URL(Holders.grailsApplication.config.xds.documentregistry.url)
            log.debug "URL: " + documentRegistryURL.toString()
        } else {
            log.info "URL is not specified. Disabling updates. Please specify: 'xds.documentregistry.url' "
            enabled = false
        }

        if (Holders.grailsApplication.config.xds.documentregistry.serviceName) {
            serviceName = Holders.grailsApplication.config.xds.documentregistry.serviceName
        }

//        if (Holders.grailsApplication.config.xds.documentregistry.useProxy) {
//            useHttpProxy = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.useProxy)
//            log.debug "Use proxy? " + useHttpProxy
//        }
//
//        if (useHttpProxy) {
//            proxyPort = Holders.grailsApplication.config.xds.documentregistry.proxyPort
//            proxyHost = Holders.grailsApplication.config.xds.documentregistry.proxyHost
//            proxyUsername = Holders.grailsApplication.config.xds.documentregistry.proxyUsername
//            proxyPassword = Holders.grailsApplication.config.xds.documentregistry.proxyPassword
//            log.debug "Proxy host: ${proxyHost} - proxy port: ${proxyPort}"
//        }

        log.info "Reading settings from config"
        this.updateRegistry = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.updateRegistry)
        log.debug "Update Registry: " + this.updateRegistry

    }

    /**
     Returns true is service has detected, that registry is available. Tries to reconnect to registry to see if it's available
     */
    public boolean isEnabled() {
        getDocumentRegistryProxy()

        return enabled
    }

    /**
     * Easy access to Proxy object. Handles the scenario where the proxy could not be established etc.
     * @return The proxy object to use
     * @throws RegistryException thrown in case of errors
     */

    DocumentRegistryPortType getDocumentRegistryProxy() throws RegistryException {
        if (updateRegistry && !documentRegistry) {
//            if (useHttpProxy) {
//                log.debug "Setting up HTTP Proxy connection - using system "
//
//                System.getProperties().put("proxySet", "true");
//                System.getProperties().put("http.proxyHost", proxyHost);
//                System.getProperties().put("http.proxyPort", proxyPort);
//                System.getProperties().put("https.proxyHost", proxyHost);
//                System.getProperties().put("https.proxyPort", proxyPort);
//                System.getProperties().put("http.nonProxyHosts", "localhost")
//                System.getProperties().put("https.nonProxyHosts", "localhost")
//
//                log.debug "Done setting up HTTP Proxy connection - using system "
//            }


            try {
                QName qname = new QName(documentRegistryNamespace, serviceName)
                Service service = Service.create(documentRegistryURL, qname)
                documentRegistry = service.getPort(DocumentRegistryPortType.class, new AddressingFeature())

//                if (useHttpProxy) {
//                    log.debug "Setting up HTTP Proxy connection"
//                    Client client = ClientProxy.getClient(documentRegistry);
//                    HTTPConduit http = (HTTPConduit) client.getConduit();
//
//                    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
//
//                    httpClientPolicy.setProxyServer(proxyHost)
//                    httpClientPolicy.setProxyServerPort(Integer.valueOf(proxyPort).intValue())
//                    httpClientPolicy.setNonProxyHosts("localhost")
//
//                    http.setClient(httpClientPolicy);
//                    log.debug "Done setting up proxy server"
//                }

            } catch (Exception e) {
                log.error "Caught exception while setting up document registry endpoint. Disabling. Message: " + e.message
                enabled = false
                throw new RegistryException(Constants.ERROR_CODE_XDS_REGISTRY_INITIALIZATION, Constants.ERROR_TEXT_XDS_REGISTRY_INITIALIZATION + " " + e.getMessage())
            }
        }
        return documentRegistry
    }

    /**
     * Submits registry metadata
     * @param submitObjectRequest The registry metadata to be submitted
     * @return The filled registry response object or null
     * @throws RegistryException is thrown in case of errors
     */
    public RegistryResponseType updateRegistry(SubmitObjectsRequest submitObjectRequest) throws RegistryException {
        RegistryResponseType response


        if (enabled && updateRegistry) {

            // make sure the registry is initialized
            try {
                getDocumentRegistryProxy()
            } catch (Exception e) {
                log.error "Registry is not available. Reason: " + e.getMessage()
            }

        }

        if (enabled && updateRegistry) {
            // Make sure the request is signed
            exchangeAndSignIDCard()

            try {
                response = getDocumentRegistryProxy().documentRegistryRegisterDocumentSetB(submitObjectRequest)
                log.debug "Response status: " + response?.status

                if (response?.status?.equals(Constants.XDS_REGISTRY_FAILED_REGISTERED)) {
                    for (error in response?.registryErrorList?.registryError) {
                        log.debug "Error was: " + error.errorCode + " reason: " + error.codeContext + " " + error.value
                    }
                }
            } catch (SOAPFaultException e) {
                log.error "Caught SoapFaultException while invoking registry. " + e.toString()

            } catch (ConnectException e) {
                log.error "Caught ConnectException while connecting to registry. " + e.toString()

            } catch (Exception e) {
                log.error "Caught Exception while connecting to registry. " + e.toString()
            }
        } else {
            log.info "Registry update is disabled - not updating"
        }

        return response
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    private void exchangeAndSignIDCard() {
        Request sosiRequest = sosiService.createRequest() // Gets the signed id card

        // Mapping between formats ..
        Document doc = sosiRequest.serialize2DOMDocument()

        Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
        Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

        SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
        SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)

        List<Header> headersList = new ArrayList<Header>();

        headersList.add(securitySoapHeader)
        headersList.add(medcomSoapHeader)

        // Adding the relevant headers to the request
        ((BindingProvider) getDocumentRegistryProxy()).getRequestContext().put(Header.HEADER_LIST, headersList)
    }
}
