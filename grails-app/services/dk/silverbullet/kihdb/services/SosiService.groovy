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
package dk.silverbullet.kihdb.services

import dk.silverbullet.kihdb.sosi.SosiUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import org.apache.cxf.binding.soap.SoapHeader
import org.apache.cxf.headers.Header
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class SosiService {

    SosiUtil sosiUtil
    String cvr
    String systemName

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

    //TODO mss check that this method is called
    void setGrailsApplication(GrailsApplication grailsApplication) {
        cvr = grailsApplication.config.seal.cvr
        systemName = grailsApplication.config.seal.systemName
        sosiUtil = new SosiUtil(grailsApplication.config)
    }

    private Request createRequest() {
        SOSIFactory sosiFactory = sosiUtil.getSOSIFactory()

        Request sosiRequest = sosiFactory.createNewRequest(false, "flow")
        IDCard signedIdCard = sosiUtil.getSignedIDCard()
        sosiRequest.setIDCard(signedIdCard)
//      setIDCard(sosiRequest)
        return sosiRequest
    }

    private void setIDCard(Request sosiRequest) {
        IDCard signedIdCard = sosiUtil.getSignedIDCard()
        sosiRequest.setIDCard(signedIdCard)
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    public List<Header> exchangeAndSignIDCard() {
        Request sosiRequest = createRequest() // Gets the signed id card

        // Mapping between formats ..
        Document doc = sosiRequest.serialize2DOMDocument()

        Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
        Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

        SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
        SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)

        List<Header> headersList = new ArrayList<Header>();

        headersList.add(securitySoapHeader)
        headersList.add(medcomSoapHeader)

        return headersList
    }

    /**
     * utility methods which generates and signs SOSI IDCard to be used in connection to registry
     */
    public void exchangeAndSignIDCard(BindingProvider provider) {
        Request sosiRequest = this.createRequest()// Gets the signed id card

        // Mapping between formats ..
        Document doc = sosiRequest.serialize2DOMDocument()

        Element securityHeader = (Element) xPath.compile(wsseXpath).evaluate(doc, XPathConstants.NODE)
        Element medcomHeader = (Element) xPath.compile(medcomXpath).evaluate(doc, XPathConstants.NODE)

        SoapHeader securitySoapHeader = new SoapHeader(securityQname, securityHeader)
        SoapHeader medcomSoapHeader = new SoapHeader(medcomQname, medcomHeader)

        List<Header> headersList = new ArrayList<Header>();

        headersList.add(securitySoapHeader)
        headersList.add(medcomSoapHeader)

        provider.getRequestContext().put(Header.HEADER_LIST, headersList)
    }
}
