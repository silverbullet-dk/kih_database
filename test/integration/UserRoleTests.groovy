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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class UserRoleTests extends GroovyTestCase {

    def daoAuthenticationProvider

    @Before
    void setUp() {
        super.setUp()

    }

    @After
    void tearDown() {

    }

    @Test
    void testRoles1() {
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken("admin", "admin_23")
        authtoken = daoAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
        def ctx = SecurityContextHolder.getContext()

        assert ctx.authentication.authorities.size() == 22 //admin has one role: admin
    }

    @Test
    void testRoles2() {
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken("system", "system_23")
        authtoken = daoAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
        def ctx = SecurityContextHolder.getContext()

        assert ctx.authentication.authorities.size() == 3 //system has one role: system
    }

    @Test
    void testRoles3() {
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken("hp", "hp_23")
        authtoken = daoAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
        def ctx = SecurityContextHolder.getContext()

        assert ctx.authentication.authorities.size() == 12 //hp has one role: hp
    }
}
