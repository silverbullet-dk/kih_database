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

import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.LaboratoryReport
import dk.silverbullet.kihdb.model.SelfMonitoringSample
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.phmr.PHMRBuilder
import dk.silverbullet.kihdb.util.TestDataUtil
import org.slf4j.LoggerFactory
import spock.lang.Specification

import java.text.SimpleDateFormat

class EbXmlBuilderSpec extends Specification {


    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")


    def log = LoggerFactory.getLogger(EbXmlBuilderSpec.class.name)

    def testDataUtil = new TestDataUtil()


    void "test build submit object request"() {
        given:
        def ebBuilder = new EbXmlBuilder()
        // Create nancy
        def nancy = new Citizen(firstName: 'Nancy Ann ',
                lastName: 'Berggren',
                ssn: 2512484916,
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                sex: Sex.FEMALE,
                streetName: 'Åbogade 15',
                zipCode: '8200',
                city: 'Aarhus N',
                country: 'Denmark',
                mobilePhone: "12345678",
                contactTelephone: "12345690",
                email: "email@example.com")

        def s = new SelfMonitoringSample()
        s.citizen = nancy
        s.createdByText = "Helbredsprofilen"

        s.reports = new HashSet<LaboratoryReport>()

        // Puls
        def dates = [["2013-05-24 13:06:29 CEST", "66.0"], ["2013-05-22 14:18:44 CEST", "57.0"],
                     ["2013-05-21 10:17:18 CEST", "55.0"], ["2013-05-20 16:15:38 CEST", "58.0"],
                     ["2013-05-18 14:13:37 CEST", "56.0"], ["2013-05-15 15:13:01 CEST", "57.0"],
                     ["2013-05-15 13:56:45 CEST", "58.0"], ["2013-05-11 10:50:00 CEST", "54.0"],
                     ["2013-05-12 10:50:00 CEST", "61.0"], ["2013-05-13 11:01:00 CEST", "58.0"]]
        for (d in dates) {
            def l = testDataUtil.createPulseMeasurement(sdf.parse(d[0]), d[1])
            s.reports.add(l)
            l = testDataUtil.createCalProtein(sdf.parse(d[0]), d[1])
            s.reports.add(l)

        }

        s.createdDateTime = sdf.parse(dates[0][0])

        def phmrBuilder = new PHMRBuilder()
        def metadata = phmrBuilder.buildRegistryEntry(s)
        metadata.uniqueID = UUID.randomUUID().toString()
        metadata.associatedID = UUID.randomUUID().toString()


        when:
        def submitRequestObject = ebBuilder.buildSubmitObjectsRequest(metadata)
        def xmlString = ebBuilder.convertSubmitObjectsRequestToString(submitRequestObject)
//        def xmlSlurper = new XmlSlurper().parseText(xmlString)

//        for (node in nodes) {
//            log.debug "Node: " + node.name
//            walkNode(node)
//        }
//        log.debug "Got: \n" + xmlString


        then:
//        def stuff = xmlSlurper.RegistryObjectList.children().each { n ->
//            log.debug "Found node...."  + n
//
//            if ( n instanceof groovy.util.slurpersupport.NodeChild) {
//                def node = (groovy.util.slurpersupport.Node) n
//
//                if (node.name().equals("ExtrinsicObject")) {
//                    node.children().size() == 17
//
//                }
//
//            }
//
//        }
//        xmlSlurper.RegistryObjectList.list().size() > 0
        submitRequestObject != null


    }

    def walkNode(def n) {

        if (n instanceof String) {
            log.debug "Name: " + n

        } else {
            log.debug "Name: " + n.name

            if (n.children().size() > 0) {
                for (node in n.children()) {
                    walkNode(node)
                }
            } else {
                log.debug "value: " + n.value()
            }
        }


    }

}
