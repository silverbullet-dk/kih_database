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
package dk.silverbullet.kihdb.model.phmr

import dk.silverbullet.kihdb.model.AbstractObject

class AddressPostal extends AbstractObject {

    String mailDeliverySublocationIdentifier
    String streetName
    String streetNameForAddressingName
    String streetBuildingIdentifier
    String floorIdentifier
    String suiteIdentifier
    String districtSubdivisionIdentifier
    String postOfficeBoxIdentifier
    String postCodeIdentifier
    String districtName
    String municipalityName
    String countryIdentificationCodeTypeValue
    String countryIdentificationCodeTypeScheme

    static constraints = {
        mailDeliverySublocationIdentifier nullable: true
        streetName nullable: true
        streetNameForAddressingName nullable: true
        streetBuildingIdentifier nullable: true
        floorIdentifier nullable: true
        suiteIdentifier nullable: true
        districtSubdivisionIdentifier nullable: true
        postOfficeBoxIdentifier nullable: true
        postCodeIdentifier nullable: true
        districtName nullable: true
        municipalityName nullable: true
        countryIdentificationCodeTypeValue nullable: true
        countryIdentificationCodeTypeScheme nullable: true
    }
}
