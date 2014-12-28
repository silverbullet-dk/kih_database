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
package dk.silverbullet.kihdb.services

import dk.silverbullet.kihdb.model.phmr.Author
import dk.silverbullet.kihdb.model.phmr.Custodian
import dk.silverbullet.kihdb.model.phmr.LegalAuthenticator
import grails.test.spock.IntegrationSpec
import oio.medcom.chronicdataset._1_0.AuthorType
import oio.medcom.chronicdataset._1_0.CustodianType
import oio.medcom.chronicdataset._1_0.LegalAuthenticatorType
import oio.medcom.monitoringdataset._1_0.CreateMonitoringDatasetRequestMessage

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class PhmrServiceSpec extends IntegrationSpec {

    def phmrService
    private CreateMonitoringDatasetRequestMessage root

    def setup() {
        JAXBContext ctx = JAXBContext.newInstance(CreateMonitoringDatasetRequestMessage.class)
        Unmarshaller m = ctx.createUnmarshaller()
        root = m.unmarshal(new File("test/data/phmr/KIH-DB version 1.0.2 eksempel1.xml")) as CreateMonitoringDatasetRequestMessage
    }

    def cleanup() {
    }

    void "test transform CustodianType to Custodian and back with null"() {
        given:
        CustodianType custodianType = null

        when:
        Custodian custodian = phmrService.custodianTypeToCustodian(custodianType)
        CustodianType custodianType2 = phmrService.custodianToCustodianType(custodian)

        then:
        custodianType == null
        custodianType2 == null
    }

    void "test transform CustodianType to Custodian and back"() {
        given:

        CustodianType custodianType = (CustodianType) root.getMonitoringDatasetCollection().get(0).getCustodian()

        when:
        Custodian custodian = phmrService.custodianTypeToCustodian(custodianType)
        CustodianType custodianType2 = phmrService.custodianToCustodianType(custodian)

        then:
        custodianType.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberUse == custodianType2.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberUse
        custodianType.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.phoneNumberSubscriber.phoneNumberIdentifier

        custodianType.assignedCustodian.representedCustodianOrganization.address.mailDeliverySublocationIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.mailDeliverySublocationIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.streetName == custodianType2.assignedCustodian.representedCustodianOrganization.address.streetName
        custodianType.assignedCustodian.representedCustodianOrganization.address.streetNameForAddressingName == custodianType2.assignedCustodian.representedCustodianOrganization.address.streetNameForAddressingName
        custodianType.assignedCustodian.representedCustodianOrganization.address.streetBuildingIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.streetBuildingIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.floorIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.floorIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.suiteIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.suiteIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.districtSubdivisionIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.districtSubdivisionIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.postOfficeBoxIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.postOfficeBoxIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.postCodeIdentifier == custodianType2.assignedCustodian.representedCustodianOrganization.address.postCodeIdentifier
        custodianType.assignedCustodian.representedCustodianOrganization.address.districtName == custodianType2.assignedCustodian.representedCustodianOrganization.address.districtName
        custodianType.assignedCustodian.representedCustodianOrganization.address.municipalityName == custodianType2.assignedCustodian.representedCustodianOrganization.address.municipalityName
        custodianType.assignedCustodian.representedCustodianOrganization.address.countryIdentificationCode?.scheme?.value() == custodianType2.assignedCustodian.representedCustodianOrganization.address.countryIdentificationCode?.scheme?.value()
        custodianType.assignedCustodian.representedCustodianOrganization.address.countryIdentificationCode?.value == custodianType2.assignedCustodian.representedCustodianOrganization.address.countryIdentificationCode?.value

        custodianType.assignedCustodian.representedCustodianOrganization.id.size() == custodianType2.assignedCustodian.representedCustodianOrganization.id.size()
        custodianType.assignedCustodian.representedCustodianOrganization.name == custodianType2.assignedCustodian.representedCustodianOrganization.name
    }

    void "test transform AuthorType to Author and back with null"() {
        given:
        AuthorType authorType = null

        when:
        Author author = phmrService.authorTypeToAuthor(authorType)
        AuthorType authorType2 = phmrService.authorToAuthorType(author)

        then:
        authorType == null
        authorType2 == null
    }

    void "test transform AuthorType to Author and back"() {
        given:
        AuthorType authorType = (AuthorType) root.getMonitoringDatasetCollection().get(0).getAuthor()

        when:
        Author author = phmrService.authorTypeToAuthor(authorType)
        AuthorType authorType2 = phmrService.authorToAuthorType(author)

        then:
        authorType.time == authorType2.time

        authorType.assignedAuthor.address.mailDeliverySublocationIdentifier == authorType2.assignedAuthor.address.mailDeliverySublocationIdentifier
        authorType.assignedAuthor.address.streetName == authorType2.assignedAuthor.address.streetName
        authorType.assignedAuthor.address.streetNameForAddressingName == authorType2.assignedAuthor.address.streetNameForAddressingName
        authorType.assignedAuthor.address.streetBuildingIdentifier == authorType2.assignedAuthor.address.streetBuildingIdentifier
        authorType.assignedAuthor.address.floorIdentifier == authorType2.assignedAuthor.address.floorIdentifier
        authorType.assignedAuthor.address.suiteIdentifier == authorType2.assignedAuthor.address.suiteIdentifier
        authorType.assignedAuthor.address.districtSubdivisionIdentifier == authorType2.assignedAuthor.address.districtSubdivisionIdentifier
        authorType.assignedAuthor.address.postOfficeBoxIdentifier == authorType2.assignedAuthor.address.postOfficeBoxIdentifier
        authorType.assignedAuthor.address.postCodeIdentifier == authorType2.assignedAuthor.address.postCodeIdentifier
        authorType.assignedAuthor.address.districtName == authorType2.assignedAuthor.address.districtName
        authorType.assignedAuthor.address.municipalityName == authorType2.assignedAuthor.address.municipalityName
        authorType.assignedAuthor.address.countryIdentificationCode?.scheme?.value() == authorType2.assignedAuthor.address.countryIdentificationCode?.scheme?.value()
        authorType.assignedAuthor.address.countryIdentificationCode?.value == authorType2.assignedAuthor.address.countryIdentificationCode?.value

        authorType.assignedAuthor.assignedPerson.personGivenName == authorType2.assignedAuthor.assignedPerson.personGivenName
        authorType.assignedAuthor.assignedPerson.personMiddleName == authorType2.assignedAuthor.assignedPerson.personMiddleName
        authorType.assignedAuthor.assignedPerson.personSurnameName == authorType2.assignedAuthor.assignedPerson.personSurnameName

        authorType.representedOrganization.name == authorType2.representedOrganization.name

        authorType.getAssignedAuthor().getId().size() == authorType2.getAssignedAuthor().getId().size()

        authorType.getAssignedAuthor().getEmailAddress().size() == authorType2.getAssignedAuthor().getEmailAddress().size()

        authorType.getAssignedAuthor().getPhoneNumberSubscriber().size() == authorType2.getAssignedAuthor().getPhoneNumberSubscriber().size()
    }


    void "test transform LegalAuthenticatorType to LegalAuthenticator and back with null"() {
        given:
        LegalAuthenticatorType legalAuthenticatorType = null

        when:
        LegalAuthenticator legalAuthenticator = phmrService.legalAuthenticatorTypeToLegalAuthenticator(legalAuthenticatorType)
        LegalAuthenticatorType legalAuthenticatorType2 = phmrService.legalAuthenticatorToLegalAuthenticatorType(legalAuthenticator)

        then:
        legalAuthenticatorType == null
        legalAuthenticatorType2 == null
    }

    void "test transform LegalAuthenticatorType to LegalAuthenticator and back"() {
        given:
        LegalAuthenticatorType legalAuthenticatorType = (LegalAuthenticatorType) root.getMonitoringDatasetCollection().get(0).getLegalAuthenticator()

        when:
        LegalAuthenticator legalAuthenticator = phmrService.legalAuthenticatorTypeToLegalAuthenticator(legalAuthenticatorType)
        LegalAuthenticatorType legalAuthenticatorType2 = phmrService.legalAuthenticatorToLegalAuthenticatorType(legalAuthenticator)

        then:
        legalAuthenticatorType.time == legalAuthenticatorType2.time

        legalAuthenticatorType.assignedEntity.address.mailDeliverySublocationIdentifier == legalAuthenticatorType2.assignedEntity.address.mailDeliverySublocationIdentifier
        legalAuthenticatorType.assignedEntity.address.streetName == legalAuthenticatorType2.assignedEntity.address.streetName
        legalAuthenticatorType.assignedEntity.address.streetNameForAddressingName == legalAuthenticatorType2.assignedEntity.address.streetNameForAddressingName
        legalAuthenticatorType.assignedEntity.address.streetBuildingIdentifier == legalAuthenticatorType2.assignedEntity.address.streetBuildingIdentifier
        legalAuthenticatorType.assignedEntity.address.floorIdentifier == legalAuthenticatorType2.assignedEntity.address.floorIdentifier
        legalAuthenticatorType.assignedEntity.address.suiteIdentifier == legalAuthenticatorType2.assignedEntity.address.suiteIdentifier
        legalAuthenticatorType.assignedEntity.address.districtSubdivisionIdentifier == legalAuthenticatorType2.assignedEntity.address.districtSubdivisionIdentifier
        legalAuthenticatorType.assignedEntity.address.postOfficeBoxIdentifier == legalAuthenticatorType2.assignedEntity.address.postOfficeBoxIdentifier
        legalAuthenticatorType.assignedEntity.address.postCodeIdentifier == legalAuthenticatorType2.assignedEntity.address.postCodeIdentifier
        legalAuthenticatorType.assignedEntity.address.districtName == legalAuthenticatorType2.assignedEntity.address.districtName
        legalAuthenticatorType.assignedEntity.address.municipalityName == legalAuthenticatorType2.assignedEntity.address.municipalityName
        legalAuthenticatorType.assignedEntity.address.countryIdentificationCode?.scheme?.value() == legalAuthenticatorType2.assignedEntity.address.countryIdentificationCode?.scheme?.value()
        legalAuthenticatorType.assignedEntity.address.countryIdentificationCode?.value == legalAuthenticatorType2.assignedEntity.address.countryIdentificationCode?.value

        legalAuthenticatorType.assignedEntity.assignedPerson.personGivenName == legalAuthenticatorType2.assignedEntity.assignedPerson.personGivenName
        legalAuthenticatorType.assignedEntity.assignedPerson.personMiddleName == legalAuthenticatorType2.assignedEntity.assignedPerson.personMiddleName
        legalAuthenticatorType.assignedEntity.assignedPerson.personSurnameName == legalAuthenticatorType2.assignedEntity.assignedPerson.personSurnameName

        legalAuthenticatorType.assignedEntity.representedOrganization.name == legalAuthenticatorType2.assignedEntity.representedOrganization.name

        legalAuthenticatorType.assignedEntity.getId().size() == legalAuthenticatorType2.assignedEntity.getId().size()

        legalAuthenticatorType.assignedEntity.getEmailAddress().size() == legalAuthenticatorType2.assignedEntity.getEmailAddress().size()

        legalAuthenticatorType.assignedEntity.getPhoneNumberSubscriber().size() == legalAuthenticatorType2.assignedEntity.getPhoneNumberSubscriber().size()
    }
}
