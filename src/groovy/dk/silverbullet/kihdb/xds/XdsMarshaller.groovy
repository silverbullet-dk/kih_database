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
package dk.silverbullet.kihdb.phmr

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType
import org.apache.log4j.Logger
import org.hl7.v3.ObjectFactory

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

class XdsMarshaller {

    Logger log = Logger.getLogger(XdsMarshaller.class)

    ProvideAndRegisterDocumentSetRequestType getProvideAndRegisterDocumentSetRequest(InputStream is) {

        JAXBContext jaxbContext = JAXBContext.newInstance('ihe.iti.xds_b._2007')
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller()

        def root = unmarshaller.unmarshal(is)

        ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType = root.getValue()
        log.debug 'ProvideAndRegisterDocumentSetRequestType...: ' + provideAndRegisterDocumentSetRequestType

        return provideAndRegisterDocumentSetRequestType
    }


    def createXML(ProvideAndRegisterDocumentSetRequestType clinicalDocument) {
        JAXBContext jaxbContext = JAXBContext.newInstance(ProvideAndRegisterDocumentSetRequestType.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

        ObjectFactory of = new ObjectFactory()
        jaxbMarshaller.marshal(of.createClinicalDocument(clinicalDocument), System.out)
    }

    RetrieveDocumentSetRequestType getRetrieveDocumentSetRequestType(InputStream is) {
        JAXBContext.newInstance('ihe.iti.xds_b._2007').createUnmarshaller().unmarshal(is).getValue()
    }
}
