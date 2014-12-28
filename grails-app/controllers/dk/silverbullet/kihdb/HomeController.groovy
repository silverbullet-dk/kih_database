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
package dk.silverbullet.kihdb

import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.model.Role
import dk.silverbullet.kihdb.model.User
import grails.plugins.springsecurity.Secured

@Secured("IS_AUTHENTICATED_REMEMBERED")
class HomeController {

    /**
     * Dependency injection for the springSecurityService.
     */
    def springSecurityService

    /**
     * Redirects based on role.
     */
    @Secured(["IS_AUTHENTICATED_REMEMBERED"])
    def index() {

        session.cpr = null

        log.debug "Setting up view"
        def currentUserRoles = User.get(springSecurityService.principal.id).getAuthorities()
        def user = springSecurityService?.currentUser
        def unreadCount
        log.debug "Roles: " + currentUserRoles

        if (currentUserRoles.contains(Role.findByAuthority(Constants.ROLE_ADMIN))) {
            log.debug "Redirecting to admin view"

            redirect(controller: "citizen", action: "index")
        } else {
            log.debug "Everybody else"
            //TODO: Security?
            redirect(controller: "citizen", action: "search")
        }
    }
}
