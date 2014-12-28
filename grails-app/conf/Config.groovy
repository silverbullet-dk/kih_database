// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

grails.config.locations = [
        "file:c:/kihdb/settings/kihdb-config.properties",
        "file:c:/kihdb/settings/kihdb-logging-config.groovy",
        "file:${userHome}/.kih/kihdb-config.properties",
        "file:${userHome}/.kih/kihdb-logging-config.groovy"
]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
//grails.web.disable.multipart = false     // This breaks mtom?
grails.web.disable.multipart=true  // This

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// Should we run the conversion job

registerCitizens {
    repeatIntervalMillis = 180000
}

detectMissingDocument {
    repeatIntervalMillis = 180000
}

personaliseringsindexservice.url = ""
sundhed.dk.personaliseringsindex.url
cpr.url = ""

// SOSI override
seal.check.disable = false

// HSUID check?
hsuid.check.disable = false


// set per-environment serverURL stem for creating absolute links
environments {
    development {
    }
    wintest {
    }
    kihdbdevel {
    }
    test {
    }
    rntest {
    }
    rnstag {
    }
    rnprod {
    }
    kihdbinteg {
    }
    production {
    }
}

String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"

// Logging

import grails.util.Environment
import org.apache.log4j.DailyRollingFileAppender

String commonPattern = "%d [%t] %-5p %c{2} %x - %m%n"

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'dk.silverbullet.kihdb.model.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'dk.silverbullet.kihdb.model.UserRole'
grails.plugins.springsecurity.authority.className = 'dk.silverbullet.kihdb.model.Role'

//Twitter bootstrap plugin
grails.plugins.twitterbootstrap.fixtaglib = true

// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

/* remove this line 
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
remove this line */


cxf {
    client {
        stamdataPersonLookupClient {
            clientInterface = dk.nsi._2011._09._23.stamdatacpr.StamdataPersonLookup
            serviceEndpointAddress = "${cpr.url}"
        }
    }
}

log4j = {
    appenders {
        console name: "stdout",
                layout: pattern(conversionPattern: commonPattern)
        appender new DailyRollingFileAppender(
                name: "kihdb", datePattern: "'.'yyyy-MM-dd",
                file: "${logDirectory}/kihdb.log",
                layout: pattern(conversionPattern: commonPattern))
    }

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration,
            'org.codehaus.groovy.grails.commons.cfg',
            'org.springframework',
            'org.hibernate',
            'org.apache',
            'net.sf.ehcache.hibernate',
            'grails.app.services.org.grails.plugin.resource',
            'grails.app.taglib.org.grails.plugin.resource',
            'grails.app.resourceMappers.org.grails.plugin.resource',
            'grails.app.service.grails.buildtestdata.BuildTestDataService',
            'grails.app.buildtestdata',
            'grails.app.services.grails.buildtestdata',
            'grails.buildtestdata.DomainInstanceBuilder'

    root {
        error 'kihdb', 'stdout'
    }

    environments {
        development {
            info 'dk.silverbullet.kihdb.util.KIHProxySelector'

            debug 'grails.app',
                  'dk.silverbullet'

        }
        kihdbdevel {
            debug 'grails.app',
                    'dk.silverbullet'
        }
        kihdbinteg {
            debug 'grails.app',
                    'dk.silverbullet'
        }
        test {
            info "org.apache.cxf"

            debug 'grails.app',
                    'dk.silverbullet'
        }
        rntest {
            info 'grails.app',
                    'dk.silverbullet'
        }
        rnstag {
            info 'grails.app',
                    'dk.silverbullet'
        }
        rnprod {
            info 'grails.app',
                    'dk.silverbullet'
        }
        production {
            info 'grails.app',
                    'dk.silverbullet'
        }
    }
}
