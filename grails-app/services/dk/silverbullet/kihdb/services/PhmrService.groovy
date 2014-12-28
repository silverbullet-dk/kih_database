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

import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationCodeType
import dk.oio.rep.ebxml.xml.schemas.dkcc._2003._02._13.CountryIdentificationSchemeType
import dk.oio.rep.itst_dk.xml.schemas._2006._01._17.PersonNameStructureType
import dk.oio.rep.xkom_dk.xml.schemas._2005._03._15.AddressPostalType
import dk.silverbullet.kihdb.model.phmr.*
import grails.transaction.Transactional
import oio.medcom.chronicdataset._1_0.*

import javax.xml.datatype.DatatypeFactory

@Transactional
class PhmrService {

    def saveCustodianType(CustodianType custodianType) {
        custodianTypeToCustodian(custodianType).save(failOnError: true)
    }

    def saveAddressPostalType(AddressPostalType addressPostalType) {
        addressPostalTypeToAddressPostal(addressPostalType).save(failOnError: true)
    }

    def saveLegalAuthenticatorType(LegalAuthenticatorType legalAuthenticatorType) {
        legalAuthenticatorTypeToLegalAuthenticator(legalAuthenticatorType).save(failOnError: true)
    }

    AddressPostal addressPostalTypeToAddressPostal(AddressPostalType addressPostalType) {
        new AddressPostal(
                mailDeliverySublocationIdentifier: addressPostalType?.getMailDeliverySublocationIdentifier(),
                streetName: addressPostalType?.getStreetName(),
                streetNameForAddressingName: addressPostalType?.getStreetNameForAddressingName(),
                streetBuildingIdentifier: addressPostalType?.getStreetBuildingIdentifier(),
                floorIdentifier: addressPostalType?.getFloorIdentifier(),
                suiteIdentifier: addressPostalType?.getSuiteIdentifier(),
                districtSubdivisionIdentifier: addressPostalType?.getDistrictSubdivisionIdentifier(),
                postOfficeBoxIdentifier: addressPostalType?.getPostOfficeBoxIdentifier(),
                postCodeIdentifier: addressPostalType?.getPostCodeIdentifier(),
                districtName: addressPostalType?.getDistrictName(),
                municipalityName: addressPostalType?.getMunicipalityName(),
                countryIdentificationCodeTypeValue: addressPostalType?.getCountryIdentificationCode()?.getValue(),
                countryIdentificationCodeTypeScheme: addressPostalType?.getCountryIdentificationCode()?.getScheme()
        )
    }

    AddressPostalType addressPostalToAddressPostalType(AddressPostal addressPostal) {
        AddressPostalType addressPostalType = new AddressPostalType()

        addressPostalType.setMailDeliverySublocationIdentifier(addressPostal?.mailDeliverySublocationIdentifier)
        addressPostalType.setStreetName(addressPostal?.streetName)
        addressPostalType.setStreetBuildingIdentifier(addressPostal?.streetBuildingIdentifier)
        addressPostalType.setFloorIdentifier(addressPostal?.floorIdentifier)
        addressPostalType.setSuiteIdentifier(addressPostal?.suiteIdentifier)
        addressPostalType.setDistrictSubdivisionIdentifier(addressPostal?.districtSubdivisionIdentifier)
        addressPostalType.setPostOfficeBoxIdentifier(addressPostal?.postOfficeBoxIdentifier)
        addressPostalType.setPostCodeIdentifier(addressPostal?.postCodeIdentifier)
        addressPostalType.setDistrictName(addressPostal?.districtName)
        addressPostalType.setMunicipalityName(addressPostal?.municipalityName)
        addressPostalType.setStreetNameForAddressingName(addressPostal?.streetNameForAddressingName)

        CountryIdentificationCodeType countryIdentificationCodeType = new CountryIdentificationCodeType()
        countryIdentificationCodeType.setValue(addressPostal?.countryIdentificationCodeTypeValue)
        if (addressPostal?.countryIdentificationCodeTypeScheme)
            try {
                countryIdentificationCodeType.setScheme(CountryIdentificationSchemeType.fromValue(addressPostal?.countryIdentificationCodeTypeScheme))
            } catch (Exception e) {
            }
        addressPostalType.setCountryIdentificationCode(countryIdentificationCodeType)

        return addressPostalType
    }

    Custodian custodianTypeToCustodian(CustodianType custodianType) {
        if (!custodianType)
            return null

        Custodian custodian = new Custodian()

        custodian.name = custodianType?.getAssignedCustodian()?.getRepresentedCustodianOrganization()?.getName()

        custodian.phoneNumberSubscriber = new PhoneNumberSubscriber(
                phoneNumberIdentifier: custodianType?.getAssignedCustodian()?.getRepresentedCustodianOrganization()?.getPhoneNumberSubscriber()?.getPhoneNumberIdentifier(),
                phoneNumberUse: custodianType?.getAssignedCustodian()?.getRepresentedCustodianOrganization()?.getPhoneNumberSubscriber()?.getPhoneNumberUse()?.value()
        )

        custodian.address = addressPostalTypeToAddressPostal(custodianType?.getAssignedCustodian()?.getRepresentedCustodianOrganization()?.getAddress())

        custodianType?.getAssignedCustodian()?.getRepresentedCustodianOrganization()?.getId()?.each {
            custodian.addToIds(new IdTp(identifier: it.identifier, identifierCode: it.identifierCode))
        }

        return custodian
    }

    CustodianType custodianToCustodianType(Custodian custodian) {
        if (!custodian)
            return null

        CustodianType custodianType = new CustodianType()

        AssignedCustodianType assignedCustodianType = new AssignedCustodianType()
        RepresentedCustodianOrganizationType representedCustodianOrganizationType = new RepresentedCustodianOrganizationType()

        representedCustodianOrganizationType.setName(custodian?.name)

        custodian.ids.each {
            IdType idType = new IdType()
            idType.setIdentifier(it.identifier)
            idType.setIdentifierCode(it.identifierCode)
            representedCustodianOrganizationType.id.add(idType)
        }

        representedCustodianOrganizationType.setAddress(addressPostalToAddressPostalType(custodian.address))

        PhoneNumberSubscriberType phoneNumberSubscriberType = new PhoneNumberSubscriberType()
        phoneNumberSubscriberType.setPhoneNumberIdentifier(custodian?.phoneNumberSubscriber?.phoneNumberIdentifier)
        if (custodian?.phoneNumberSubscriber?.phoneNumberUse)
            phoneNumberSubscriberType.setPhoneNumberUse(AddressUseType.fromValue(custodian?.phoneNumberSubscriber?.phoneNumberUse))
        representedCustodianOrganizationType.setPhoneNumberSubscriber(phoneNumberSubscriberType)

        assignedCustodianType.setRepresentedCustodianOrganization(representedCustodianOrganizationType)
        custodianType.setAssignedCustodian(assignedCustodianType)

        return custodianType
    }

    Author authorTypeToAuthor(AuthorType authorType) {
        if (!authorType)
            return null

        Author author = new Author()

        author.time = authorType?.getTime()?.toGregorianCalendar()?.getTime()

        author.addressPostalType = addressPostalTypeToAddressPostal(authorType.getAssignedAuthor().getAddress())

        author.assignedPersonGivenName = authorType.getAssignedAuthor().getAssignedPerson().getPersonGivenName()
        author.assignedPersonMiddleName = authorType.getAssignedAuthor().getAssignedPerson().getPersonMiddleName()
        author.assignedPersonSurnameName = authorType.getAssignedAuthor().getAssignedPerson().getPersonSurnameName()

        author.representedOrganization = authorType.getRepresentedOrganization().getName()

        authorType.getAssignedAuthor().getId().each {
            author.addToIds(new IdTp(identifier: it.identifier, identifierCode: it.identifierCode))
        }

        authorType.getAssignedAuthor().getPhoneNumberSubscriber().each {
            author.addToPhoneNumberSubscribers(new PhoneNumberSubscriber(
                    phoneNumberIdentifier: it.getPhoneNumberIdentifier(),
                    phoneNumberUse: it.getPhoneNumberUse()
            ))
        }

        authorType.getAssignedAuthor().getEmailAddress().each {
            author.addToEmailAddress(new EmailAddress(
                    emailAddressIdentifier: it.getEmailAddressIdentifier(),
                    emailAddressUse: it.getEmailAddressUse().value()
            ))
        }

        return author
    }

    AuthorType authorToAuthorType(Author author) {
        if (!author)
            return null

        AuthorType authorType = new AuthorType()

        GregorianCalendar gCalendar = new GregorianCalendar()
        gCalendar.setTime(author.time)
        authorType.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar))

        AssignedAuthorType assignedAuthorType = new AssignedAuthorType()
        PersonNameStructureType personNameStructureType = new PersonNameStructureType()
        personNameStructureType.setPersonGivenName(author.assignedPersonGivenName)
        personNameStructureType.setPersonMiddleName(author.assignedPersonMiddleName)
        personNameStructureType.setPersonSurnameName(author.assignedPersonSurnameName)
        assignedAuthorType.setAssignedPerson(personNameStructureType)
        assignedAuthorType.setAddress(addressPostalToAddressPostalType(author.addressPostalType))

        RepresentedOrganizationType representedOrganizationType = new RepresentedOrganizationType()
        representedOrganizationType.setName(author.representedOrganization)
        authorType.setRepresentedOrganization(representedOrganizationType)

        author.ids.each {
            IdType idType = new IdType()
            idType.setIdentifier(it.identifier)
            idType.setIdentifierCode(it.identifierCode)
            assignedAuthorType.getId().add(idType)
        }

        author.phoneNumberSubscribers.each {
            PhoneNumberSubscriberType phoneNumberSubscriberType = new PhoneNumberSubscriberType()
            phoneNumberSubscriberType.setPhoneNumberIdentifier(it.phoneNumberIdentifier)
            phoneNumberSubscriberType.setPhoneNumberUse(AddressUseType.fromValue(it.phoneNumberUse))
            assignedAuthorType.getPhoneNumberSubscriber().add(phoneNumberSubscriberType)
        }

        author.emailAddress.each {
            EmailAddressType emailAddressType = new EmailAddressType()
            emailAddressType.setEmailAddressIdentifier(it.emailAddressIdentifier)
            emailAddressType.setEmailAddressUse(AddressUseType.fromValue(it.emailAddressUse))
            assignedAuthorType.getEmailAddress().add(emailAddressType)
        }

        authorType.setAssignedAuthor(assignedAuthorType)
        return authorType
    }

    LegalAuthenticator legalAuthenticatorTypeToLegalAuthenticator(LegalAuthenticatorType legalAuthenticatorType) {
        if (!legalAuthenticatorType)
            return null

        LegalAuthenticator legalAuthenticator = new LegalAuthenticator()

        legalAuthenticator.time = legalAuthenticatorType?.getTime()?.toGregorianCalendar()?.getTime()

        legalAuthenticator.signatureCode = legalAuthenticatorType?.signatureCode

        legalAuthenticator.address = addressPostalTypeToAddressPostal(legalAuthenticatorType?.getAssignedEntity()?.getAddress())

        legalAuthenticator.assignedPersonGivenName = legalAuthenticatorType.getAssignedEntity().getAssignedPerson().getPersonGivenName()
        legalAuthenticator.assignedPersonMiddleName = legalAuthenticatorType.getAssignedEntity().getAssignedPerson().getPersonMiddleName()
        legalAuthenticator.assignedPersonSurnameName = legalAuthenticatorType.getAssignedEntity().getAssignedPerson().getPersonSurnameName()

        legalAuthenticator.representedOrganizationName = legalAuthenticatorType.getAssignedEntity().getRepresentedOrganization().getName()

        legalAuthenticatorType.getAssignedEntity().getId().each {
            legalAuthenticator.addToIds(new IdTp(identifier: it.identifier, identifierCode: it.identifierCode))
        }

        legalAuthenticatorType.getAssignedEntity().getPhoneNumberSubscriber().each {
            legalAuthenticator.addToPhoneNumberSubscribers(new PhoneNumberSubscriber(
                    phoneNumberIdentifier: it.getPhoneNumberIdentifier(),
                    phoneNumberUse: it.getPhoneNumberUse()
            ))
        }

        legalAuthenticatorType.getAssignedEntity().getEmailAddress().each {
            legalAuthenticator.addToEmailAddress(new EmailAddress(
                    emailAddressIdentifier: it.getEmailAddressIdentifier(),
                    emailAddressUse: it.getEmailAddressUse()?.value()
            ))
        }

        return legalAuthenticator
    }


    LegalAuthenticatorType legalAuthenticatorToLegalAuthenticatorType(LegalAuthenticator legalAuthenticator) {
        if (!legalAuthenticator)
            return null

        LegalAuthenticatorType legalAuthenticatorType = new LegalAuthenticatorType()

        if (legalAuthenticator.time) {
            GregorianCalendar gCalendar = new GregorianCalendar()
            gCalendar.setTime(legalAuthenticator.time)
            legalAuthenticatorType.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar))
        }

        legalAuthenticatorType.setSignatureCode(legalAuthenticator.getSignatureCode())

        AssignedEntityType assignedEntityType = new AssignedEntityType()
        assignedEntityType.setAddress(addressPostalToAddressPostalType(legalAuthenticator.address))

        PersonNameStructureType personNameStructureType = new PersonNameStructureType()
        personNameStructureType.setPersonGivenName(legalAuthenticator.assignedPersonGivenName)
        personNameStructureType.setPersonMiddleName(legalAuthenticator.assignedPersonMiddleName)
        personNameStructureType.setPersonSurnameName(legalAuthenticator.assignedPersonSurnameName)
        assignedEntityType.setAssignedPerson(personNameStructureType)

        RepresentedOrganizationType representedOrganizationType = new RepresentedOrganizationType()
        representedOrganizationType.setName(legalAuthenticator.representedOrganizationName)
        assignedEntityType.setRepresentedOrganization(representedOrganizationType)

        legalAuthenticator.ids.each {
            IdType idType = new IdType()
            idType.setIdentifier(it.identifier)
            idType.setIdentifierCode(it.identifierCode)
            assignedEntityType.getId().add(idType)
        }

        legalAuthenticator.phoneNumberSubscribers.each {
            PhoneNumberSubscriberType phoneNumberSubscriberType = new PhoneNumberSubscriberType()
            phoneNumberSubscriberType.setPhoneNumberIdentifier(it.phoneNumberIdentifier)
            if (it.phoneNumberUse)
                phoneNumberSubscriberType.setPhoneNumberUse(AddressUseType.fromValue(it.phoneNumberUse))
            assignedEntityType.getPhoneNumberSubscriber().add(phoneNumberSubscriberType)
        }

        legalAuthenticator.emailAddress.each {
            EmailAddressType emailAddressType = new EmailAddressType()
            emailAddressType.setEmailAddressIdentifier(it.emailAddressIdentifier)
            if (it.emailAddressUse)
                emailAddressType.setEmailAddressUse(AddressUseType.fromValue(it.emailAddressUse))
            assignedEntityType.getEmailAddress().add(emailAddressType)
        }

        legalAuthenticatorType.setAssignedEntity(assignedEntityType)
        return legalAuthenticatorType
    }
}