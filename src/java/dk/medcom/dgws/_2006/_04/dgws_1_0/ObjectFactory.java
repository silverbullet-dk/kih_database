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

package dk.medcom.dgws._2006._04.dgws_1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the dk.medcom.dgws._2006._04.dgws_1_0 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TimeOut_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "TimeOut");
    private final static QName _FlowStatus_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FlowStatus");
    private final static QName _Priority_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Priority");
    private final static QName _SecurityLevel_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "SecurityLevel");
    private final static QName _InResponseToMessageID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "InResponseToMessageID");
    private final static QName _Header_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Header");
    private final static QName _FlowID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FlowID");
    private final static QName _FaultCode_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FaultCode");
    private final static QName _MessageID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "MessageID");
    private final static QName _RequireNonRepudiationReceipt_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "RequireNonRepudiationReceipt");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.medcom.dgws._2006._04.dgws_1_0
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Linking }
     */
    public Linking createLinking() {
        return new Linking();
    }

    /**
     * Create an instance of {@link Header }
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "TimeOut")
    public JAXBElement<String> createTimeOut(String value) {
        return new JAXBElement<String>(_TimeOut_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FlowStatus")
    public JAXBElement<String> createFlowStatus(String value) {
        return new JAXBElement<String>(_FlowStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "Priority")
    public JAXBElement<String> createPriority(String value) {
        return new JAXBElement<String>(_Priority_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "SecurityLevel")
    public JAXBElement<Integer> createSecurityLevel(Integer value) {
        return new JAXBElement<Integer>(_SecurityLevel_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "InResponseToMessageID")
    public JAXBElement<String> createInResponseToMessageID(String value) {
        return new JAXBElement<String>(_InResponseToMessageID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Header }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "Header")
    public JAXBElement<Header> createHeader(Header value) {
        return new JAXBElement<Header>(_Header_QNAME, Header.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FlowID")
    public JAXBElement<String> createFlowID(String value) {
        return new JAXBElement<String>(_FlowID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FaultCode")
    public JAXBElement<String> createFaultCode(String value) {
        return new JAXBElement<String>(_FaultCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "MessageID")
    public JAXBElement<String> createMessageID(String value) {
        return new JAXBElement<String>(_MessageID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "RequireNonRepudiationReceipt")
    public JAXBElement<String> createRequireNonRepudiationReceipt(String value) {
        return new JAXBElement<String>(_RequireNonRepudiationReceipt_QNAME, String.class, null, value);
    }

}
