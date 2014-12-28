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
package dk.silverbullet.kihdb.cpr

import dk.silverbullet.kihdb.model.Citizen
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.services.SosiService
import grails.test.spock.IntegrationSpec
import grails.util.Holders
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.SimpleDateFormat

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
//@TestFor(StamdataLookupSoapService)
class StamdataLookupSoapServiceSpec extends IntegrationSpec {
    private Logger log = LoggerFactory.getLogger(StamdataLookupSoapServiceSpec.class)
    private String NANCY_SSN = "2512484916"


    private Citizen nancy
    private Citizen k26

    def setup() {
        nancy = new Citizen(firstName: 'Nancy', middleName: 'Ann', lastName: 'Berggren',
                ssn: 2512484916,
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                sex: Sex.FEMALE,
                streetName: 'Gammel Kongevej 62',
                zipCode: '1200',
                city: 'København',
                country: 'Denmark',
                mobilePhone: "12345678",
                contactTelephone: "12345690",
                email: "email@example.com"
        )

        k26 = new Citizen(firstName: "FornavnK26",
                lastName: "EfternavnK26",
                ssn: "2105669996",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1966-05-21"),
                sex: Sex.FEMALE,
                streetName: 'AdresseK26 26',
                zipCode: '9998',
                city: 'ByK26',
                country: 'Denmark',
                mobilePhone: null,
                contactTelephone: "99999999",
                email: "MailK26@mail.dk")
    }

    void "Test CPR using nancy "() {
        when:
        log.debug "Starting invocation of StamdataLookup"

        StamdataLookupSoapService soapService = new StamdataLookupSoapService()
        SosiService sosiService = new SosiService()
        sosiService.setGrailsApplication(Holders.grailsApplication)
        soapService.sosiService = sosiService


        def res = soapService.invokeStamdataLookup(NANCY_SSN)

        println "Res: " + res
        log.debug "Got: " + res

        then:
        res != null

        res.getPersonInformationStructure().size() == 1

        def result = res.getPersonInformationStructure().get(0)

        // Check name
        nancy.firstName == result.regularCPRPerson.simpleCPRPerson.personNameStructure.personGivenName
        nancy.middleName == result.regularCPRPerson.simpleCPRPerson.personNameStructure.personMiddleName
        nancy.lastName == result.regularCPRPerson.simpleCPRPerson.personNameStructure.personSurnameName

        // SSN
        NANCY_SSN == result.regularCPRPerson.simpleCPRPerson.personCivilRegistrationIdentifier

        // Address
        nancy.city == result.personAddressStructure.addressComplete.addressPostal.districtName
        nancy.zipCode == result.personAddressStructure.addressComplete.addressPostal.postCodeIdentifier
        nancy.streetName == result.personAddressStructure.addressComplete.addressPostal.streetName + " " + result.personAddressStructure.addressComplete.addressPostal.streetBuildingIdentifier

    }

    void "Test CPR non-existing CPR "() {
        when:
        log.debug "Starting invocation of StamdataLookup"

        StamdataLookupSoapService soapService = new StamdataLookupSoapService()
        SosiService sosiService = new SosiService()
        sosiService.setGrailsApplication(Holders.grailsApplication)
        soapService.sosiService = sosiService


        def res = soapService.invokeStamdataLookup("1212124444")

        println "Res: " + res
        log.debug "Got: " + res

        then:
        res != null

        res.getPersonInformationStructure().size() == 0
    }

}

