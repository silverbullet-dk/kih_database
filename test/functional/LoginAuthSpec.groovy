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
import geb.spock.GebReportingSpec
import spock.lang.Stepwise

@Stepwise
class LoginAuthSpec extends GebReportingSpec {

    String getBaseUrl() { "http://localhost:8080/" }

    File getReportDir() { new File("target/reports/geb") }

//    def "invalid login"() {
//        given: "I am at the login page"
//        to LoginPage
//
//        when: "I am entering invalid password"
//        loginForm.j_username = "admin"
//        loginForm.j_password = "Flaf"
//        loginButton.click()
//
//        then: "I am being redirected to the login page, the password I entered is wrong"
//        at LoginPage
//        loginForm.j_username == ""
//        loginForm.j_password == ""
//    }
//
//    def "admin login"() {
//        given : "I am at the login page"
//        to LoginPage
//
//        when: "I am entering valid username and password"
//        loginForm.j_username = "admin"
//        loginForm.j_password = "admin_23"
//        loginButton.click()
//
//        then: "I am being redirected to the admin homepage"
//        at pages.citizen.SearchPage
//        $().text().contains("Logget ind som admin")
//
//        and: "I log out"
//        logoutLink.click()
//    }
}
