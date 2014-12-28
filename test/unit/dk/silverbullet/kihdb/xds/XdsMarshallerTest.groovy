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
package dk.silverbullet.kihdb.xds

import dk.silverbullet.kihdb.phmr.XdsMarshaller
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType
import org.junit.Ignore
import org.junit.Test

class XdsMarshallerTest {

//    @Test
//    void testXds() {
//
//
//        XdsMarshaller xdsMarshaller = new XdsMarshaller()
//        File file = new File("test/data/ProvideAndRegisterDocumentSet-test.xml")
////        File file = new File("test/data/thomas.xml")
//
//        ProvideAndRegisterDocumentSetRequestType provideAndRegisterDocumentSetRequestType =
//                xdsMarshaller.getProvideAndRegisterDocumentSetRequest(new FileInputStream(file))
//
//        for (def dok in provideAndRegisterDocumentSetRequestType.getDocument()) {
//            String id = dok.getId()
//            println 'id..:' + id
//
//            // Base64decoded
//            // println 'value..:' + new String(dok.value)
//
//        }
////        xdsMarshaller.createXML(clinicalDocument)
//    }

    @Test
    @Ignore
    void testPhmrWrite() {
        XdsMarshaller xdsMarshaller = new XdsMarshaller()
        //PHMRBuilder phmrWrite = new PHMRBuilder()

        //phmr.createXML(phmrWrite.create())
    }

    @Test
    void testXDSRead() {
        XdsMarshaller xdsMarshaller = new XdsMarshaller()
        File file = new File("test/data/xds/ProvideAndRegisterDocumentSet-test-body.xml")

        ProvideAndRegisterDocumentSetRequestType clinicalDocument = xdsMarshaller.getProvideAndRegisterDocumentSetRequest(new FileInputStream(file))

        println 'HEJSA...:' + clinicalDocument.getDocument()[0].value.contentType

        // println 'clinicalDocument..:' + clinicalDocument

        //xdsMarshaller.phmr2Domain(clinicalDocument)
    }

}
