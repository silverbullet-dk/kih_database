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
package dk.silverbullet.kihdb.sundhed

import dk.silverbullet.kihdb.services.sundhed.PersonaliseringsindeksSoapService
import dk.silverbullet.kihdb.util.KIHProxySelector
import dk.sundhed._2009._01._01.personaliseringsindeks.kontrakt.ObjectFactory
import grails.test.spock.IntegrationSpec
import spock.lang.Ignore

class PersonaliseringsIndeksFunctionalSoapSpec extends IntegrationSpec {

    private String NANCY_SSN = "2512484916"

    @Ignore("Requires connection to SDN")
    def "test lookup against PersonalisationIndex"() {
        given:

        ProxySelector.setDefault(new KIHProxySelector())

        def soapService = new PersonaliseringsindeksSoapService()
        def objectFactory = new ObjectFactory()

        when:

        /*
        <xsd:element minOccurs="0" name="ApplicationId" type="xsd:int"/>
        <xsd:element minOccurs="0" name="Href" nillable="true" type="xsd:string"/>
        <xsd:element minOccurs="0" name="LongName" nillable="true" type="xsd:string"/>
        <xsd:element minOccurs="0" name="ShortName" nillable="true" type="xsd:string"/>
        <xsd:element minOccurs="0" name="Target" nillable="true" type="xsd:string"/>
         */


        println "Invoking"

        def retVal = soapService.updatePersonaliseringsindeks(NANCY_SSN)
        print " Got : " + retVal

        then:
        retVal == true
    }

}

