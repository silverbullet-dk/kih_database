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

import dk.silverbullet.kihdb.model.KihDbGrailsUserDetails
import dk.silverbullet.kihdb.model.RolePermission
import dk.silverbullet.kihdb.model.User
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Implement UserDetails service to handle roles and permissions
 */
class UserDetailsService implements GrailsUserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class.name)
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException, DataAccessException {
        return loadUserByUsername(username)
    }

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        User.withTransaction { status ->
            User user = User.findByUsername(s)
            if (!user) {
                throw new UsernameNotFoundException('User not found', s)
            }

            def authorities = []
            user.authorities.each {
                def perms = RolePermission.findAllByRole(it)
                perms.each { perm ->
                    authorities << new GrantedAuthorityImpl(perm.permission.permission)
                }
            }

            //  remove this block when moved to permissions.
            user.authorities.each {
                authorities << new GrantedAuthorityImpl(it.authority)
            }

            log.debug "Returning authorities: " + authorities


            return new KihDbGrailsUserDetails(user.username, user.password, user.enabled,
                    !user.accountExpired, !user.passwordExpired,
                    !user.accountLocked, authorities ?: NO_ROLES, user.id)
        }
    }
}
