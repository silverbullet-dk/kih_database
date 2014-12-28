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
package dk.silverbullet.kihdb.service.sundhed

import dk.silverbullet.kihdb.services.sundhed.PersonaliseringsindeksSoapService
import dk.sundhed._2009._01._01.personaliseringsindeks.kontrakt.ObjectFactory
import dk.sundhed._2009._01._01.personaliseringsindeks.kontrakt.SubmitPersonalizationType
import dk.sundhed._2009._01._01.personaliseringsindeks.kontrakt.UrlType
import spock.lang.Ignore
import spock.lang.Specification

import javax.xml.bind.JAXBElement
import javax.xml.namespace.QName

class PersonaliseringsIndeksFunctionalSoapSpec extends Specification {

    def personaliseringsindeksSoapService
    private String NANCY_SSN = "2512484916"

    @Ignore
    def "test lookup against PersonalisationIndex"() {
        given:
        def soapService = new PersonaliseringsindeksSoapService()
        def objectFactory = new ObjectFactory()

        when:
        SubmitPersonalizationType personalization = objectFactory.createSubmitPersonalizationType()


        def jaxQ = new QName("http://sundhed.dk/2009/01/01/personaliseringsindeks/kontrakt", "PersonCivilRegistrationIdentifier")
        def jaxbString = new JAXBElement(jaxQ, String.class, new String(NANCY_SSN))
        personalization.personCivilRegistrationIdentifier = jaxbString

        UrlType t = objectFactory.createUrlType()
        t.applicationId = 532
        personalization.setUrl(t)

        // This can be 1 => Citizen, 2 => healthcareprofessional, 3 => both according to email exchange with Sundhed.dk
        personalization.recipientType = BigInteger.ONE.toInteger()

        def retVal = soapService.updatePersonaliseringsindeks(NANCY_SSN)

        then:
        retVal != null


    }

}
