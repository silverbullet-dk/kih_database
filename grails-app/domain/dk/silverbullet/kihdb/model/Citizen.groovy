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
package dk.silverbullet.kihdb.model

import dk.silverbullet.kihdb.model.types.Sex

class Citizen extends AbstractObject {

    String ssn
    String firstName;
    String middleName
    String lastName;
    Date dateOfBirth;
    Sex sex;
    String contactTelephone
    String email

    String streetName
    String streetName2
    String zipCode
    String city
    String country

    boolean inPersonalizationIndex

    String toString() {
        "${firstName} ${lastName}"
    }

    static hasMany = [samples: SelfMonitoringSample]

    static mapping = {
        ssn index: 'citizen_ssn_idx'
        samples(lazy: false)
    }

    static constraints = {
        ssn(validator: { val, obj ->
            def retVal = true
            def similarUser = Citizen.findBySsnIlike(val)
            if (similarUser && obj?.id != similarUser?.id) {
                retVal = false
            }
            if (val?.length() != 10) {
                retVal = false
            }
            return retVal
        })
        firstName(nullable: true, blank: false)
        middleName(nullable: true, blank: true)
        lastName(nullable: true, blank: false)
        sex(nullable: true, blank: false)
        email(nullable: true)
        contactTelephone(nullable: true)
        dateOfBirth(nullable: false)
        streetName(nullable: true)
        streetName2(nullable: true)
        zipCode(nullable: true)
        city(nullable: true)
        country(nullable: true)
        inPersonalizationIndex(nullable: true)
    }

    String getFormattedSsn() {
        return ssn.replace("-", "")
    }

    Date getBirthdate() {
        Date retVal = null
        if (this.dateOfBirth) {
            retVal = dateOfBirth
        } else {
            def day = Integer.parseInt(ssn[0..1]).intValue()
            def month = Integer.parseInt(ssn[2..3]).intValue()
            def year = Integer.parseInt(ssn[4..5]).intValue()
            this.dateOfBirth = new Date(month: month, date: day, year: year, hours: 0, minutes: 0, seconds: 0)
            retVal = dateOfBirth
        }

        return retVal
    }

    String getGenderCode() {
        String retVal = sex?.value()

        if (!retVal) {

            // FInd the last four digits
            if (ssn?.length() == 10) {
                String lastFour = ssn?.substring(6, 10)
                int remainder = Integer.valueOf(lastFour).intValue() % 2

                if (remainder == 1) {

                    retVal = "M"
                } else {
                    retVal = "F"
                }
            } else {
                retVal = "U"
            }
        }

        return retVal
    }
}
