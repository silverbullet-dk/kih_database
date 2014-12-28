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
package dk.silverbullet.kihdb.service.kroniker.v102

import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.CreateMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.DeleteMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.GetMonitoringDataset
import dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.ObjectFactory
import dk.silverbullet.kihdb.util.HsuidUtil
import dk.silverbullet.kihdb.util.SosiTestDataUtil
import dk.silverbullet.kihdb.util.version1_0_1.TestDataUtil
import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class V102TestDataUtil {

    private static Logger log = LoggerFactory.getLogger(V102TestDataUtil.class)


    TestDataUtil tdu = new TestDataUtil()
    SosiTestDataUtil stdu = new SosiTestDataUtil()
    ObjectFactory objectFactory = new ObjectFactory()
    SOSIFactory factory = stdu.createSOSIFactory()
    HsuidUtil hsuidUtil = new HsuidUtil()


    public String generateRequest(DeleteMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(DeleteMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }


    public String generateRequest(GetMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(GetMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }

    public String generateRequest(CreateMonitoringDataset cxfRequest, Request sosiRequest) {
        IDCard signedIdCard = stdu.getSignedIDCard()
        Marshaller m = createMarshaller(CreateMonitoringDataset.class)
        Document doc = createDocument()
        m.marshal(cxfRequest, doc)
        return documentToString(sosiRequest, signedIdCard, doc)
    }

    private Marshaller createMarshaller(Class clz) {
        JAXBContext context = JAXBContext.newInstance(clz);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return m
    }

    private Document createDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        return doc
    }

    private String documentToString(Request sosiRequest, IDCard signedIdCard, Document doc) {
        sosiRequest.setIDCard(signedIdCard)
        sosiRequest.body = doc.getDocumentElement()
        def soapRequest = XmlUtil.node2String(sosiRequest.serialize2DOMDocument(), false, true)
        return soapRequest
    }


    public Element generateHsuidHeaders(String ssn) {
        return hsuidUtil.generateHsuidHeaders("my-issuer", ssn)
    }
}
