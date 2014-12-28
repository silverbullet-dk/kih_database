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
import dk.silverbullet.kihdb.interceptors.HsuidInInterceptor
import dk.silverbullet.kihdb.interceptors.HsuidOutInterceptor
import dk.silverbullet.kihdb.sosi.SosiInInterceptor
import dk.silverbullet.kihdb.sosi.SosiOutInterceptor
import grails.util.Environment

// Place your Spring DSL code here
beans = {
    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        defaultLocale = new Locale("da", "DK")
        java.util.Locale.setDefault(defaultLocale)
    }
    webServiceContext(org.apache.cxf.jaxws.context.WebServiceContextImpl)
    sosiInInterceptor(SosiInInterceptor)

    sosiOutInterceptor(SosiOutInterceptor)

    hsuidInInterceptor(HsuidInInterceptor)

    hsuidOutInterceptor(HsuidOutInterceptor)
//    sosiFaultInterceptor(SosiFaultInterceptor)
    auditLogLookupBean(dk.silverbullet.kihdb.constants.KihDbAuditLogLookup)
    userDetailsService(dk.silverbullet.kihdb.services.UserDetailsService)

    if (Environment.current.name == 'rnprod') {
        log4jConfigurer(org.springframework.beans.factory.config.MethodInvokingFactoryBean) {
            targetClass = "org.springframework.util.Log4jConfigurer"
            targetMethod = "initLogging"
            arguments = ["c:/kihdb/settings/kihdb-log4j.properties"]
        }
    }
}
