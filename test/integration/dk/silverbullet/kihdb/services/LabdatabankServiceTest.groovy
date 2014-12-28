package dk.silverbullet.kihdb.services

import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.services.xds.DocumentRegistryService
import dk.silverbullet.kihdb.util.TestDataUtil
import ihe.iti.xds_b._2007.DocumentRegistryPortType
import spock.lang.Specification

import javax.xml.namespace.QName
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathFactory
import java.text.SimpleDateFormat

class LabdatabankServiceTest extends Specification {

    def labdatabankService

    private URL documentRegistryURL
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


    private boolean enabled = true
    private boolean updateRegistry = true


    private DocumentRegistryPortType documentRegistry

    def kihDocumentRegistryService = new DocumentRegistryService()
    def sosiService = new SosiService()

    Citizen nancy

    def testDataUtil = new TestDataUtil()
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    def idsToDeprecated = []
}
