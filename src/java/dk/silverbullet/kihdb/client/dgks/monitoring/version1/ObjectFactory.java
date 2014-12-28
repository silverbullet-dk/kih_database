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

package dk.silverbullet.kihdb.client.dgks.monitoring.version1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the dk.silverbullet.kihdb.client.dgks.monitoring.version1 package.
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

    private final static QName _LaboratoryReportCollection_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "LaboratoryReportCollection");
    private final static QName _OperatorIdentifier_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "OperatorIdentifier");
    private final static QName _LaboratoryReport_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "LaboratoryReport");
    private final static QName _LaboratoryReportOfRelevanceCollection_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "LaboratoryReportOfRelevanceCollection");
    private final static QName _SelfMonitoredSample_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "SelfMonitoredSample");
    private final static QName _EncodingIdentifier_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "EncodingIdentifier");
    private final static QName _Fault_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "Fault");
    private final static QName _AbnormalIdentifier_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "AbnormalIdentifier");
    private final static QName _ProducerOfLabResult_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "ProducerOfLabResult");
    private final static QName _PersonCivilRegistrationIdentifier_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "PersonCivilRegistrationIdentifier");
    private final static QName _UuidIdentifier_QNAME = new QName("urn:oio:medcom:chronicdataset:1.0.0", "UuidIdentifier");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.silverbullet.kihdb.client.dgks.monitoring.version1
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SelfMonitoredSampleType }
     */
    public SelfMonitoredSampleType createSelfMonitoredSampleType() {
        return new SelfMonitoredSampleType();
    }

    /**
     * Create an instance of {@link LaboratoryReportCollectionType }
     */
    public LaboratoryReportCollectionType createLaboratoryReportCollectionType() {
        return new LaboratoryReportCollectionType();
    }

    /**
     * Create an instance of {@link LaboratoryReportType }
     */
    public LaboratoryReportType createLaboratoryReportType() {
        return new LaboratoryReportType();
    }

    /**
     * Create an instance of {@link ProducerOfLabResultType }
     */
    public ProducerOfLabResultType createProducerOfLabResultType() {
        return new ProducerOfLabResultType();
    }

    /**
     * Create an instance of {@link FaultType }
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link CreateMonitoringRequestMessage }
     */
    public CreateMonitoringRequestMessage createCreateMonitoringRequestMessage() {
        return new CreateMonitoringRequestMessage();
    }

    /**
     * Create an instance of {@link CreateMonitoringResponseMessage }
     */
    public CreateMonitoringResponseMessage createCreateMonitoringResponseMessage() {
        return new CreateMonitoringResponseMessage();
    }

    /**
     * Create an instance of {@link DeleteMonitoringResponseMessage }
     */
    public DeleteMonitoringResponseMessage createDeleteMonitoringResponseMessage() {
        return new DeleteMonitoringResponseMessage();
    }

    /**
     * Create an instance of {@link DeleteMonitoringRequestMessage }
     */
    public DeleteMonitoringRequestMessage createDeleteMonitoringRequestMessage() {
        return new DeleteMonitoringRequestMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LaboratoryReportCollectionType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "LaboratoryReportCollection")
    public JAXBElement<LaboratoryReportCollectionType> createLaboratoryReportCollection(LaboratoryReportCollectionType value) {
        return new JAXBElement<LaboratoryReportCollectionType>(_LaboratoryReportCollection_QNAME, LaboratoryReportCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperatorIdentifierType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "OperatorIdentifier")
    public JAXBElement<OperatorIdentifierType> createOperatorIdentifier(OperatorIdentifierType value) {
        return new JAXBElement<OperatorIdentifierType>(_OperatorIdentifier_QNAME, OperatorIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LaboratoryReportType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "LaboratoryReport")
    public JAXBElement<LaboratoryReportType> createLaboratoryReport(LaboratoryReportType value) {
        return new JAXBElement<LaboratoryReportType>(_LaboratoryReport_QNAME, LaboratoryReportType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LaboratoryReportCollectionType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "LaboratoryReportOfRelevanceCollection")
    public JAXBElement<LaboratoryReportCollectionType> createLaboratoryReportOfRelevanceCollection(LaboratoryReportCollectionType value) {
        return new JAXBElement<LaboratoryReportCollectionType>(_LaboratoryReportOfRelevanceCollection_QNAME, LaboratoryReportCollectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelfMonitoredSampleType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "SelfMonitoredSample")
    public JAXBElement<SelfMonitoredSampleType> createSelfMonitoredSample(SelfMonitoredSampleType value) {
        return new JAXBElement<SelfMonitoredSampleType>(_SelfMonitoredSample_QNAME, SelfMonitoredSampleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncodingIdentifierType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "EncodingIdentifier")
    public JAXBElement<EncodingIdentifierType> createEncodingIdentifier(EncodingIdentifierType value) {
        return new JAXBElement<EncodingIdentifierType>(_EncodingIdentifier_QNAME, EncodingIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "Fault")
    public JAXBElement<FaultType> createFault(FaultType value) {
        return new JAXBElement<FaultType>(_Fault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbnormalIdentifierType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "AbnormalIdentifier")
    public JAXBElement<AbnormalIdentifierType> createAbnormalIdentifier(AbnormalIdentifierType value) {
        return new JAXBElement<AbnormalIdentifierType>(_AbnormalIdentifier_QNAME, AbnormalIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProducerOfLabResultType }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "ProducerOfLabResult")
    public JAXBElement<ProducerOfLabResultType> createProducerOfLabResult(ProducerOfLabResultType value) {
        return new JAXBElement<ProducerOfLabResultType>(_ProducerOfLabResult_QNAME, ProducerOfLabResultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "PersonCivilRegistrationIdentifier")
    public JAXBElement<String> createPersonCivilRegistrationIdentifier(String value) {
        return new JAXBElement<String>(_PersonCivilRegistrationIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "urn:oio:medcom:chronicdataset:1.0.0", name = "UuidIdentifier")
    public JAXBElement<String> createUuidIdentifier(String value) {
        return new JAXBElement<String>(_UuidIdentifier_QNAME, String.class, null, value);
    }

}
