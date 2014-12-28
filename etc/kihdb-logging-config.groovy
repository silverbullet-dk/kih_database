String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"

// Logging
println "Setting up external log configuration"


import grails.util.Environment
import org.apache.log4j.DailyRollingFileAppender

String commonPattern = "%d [%t] %-5p %c{2} %x - %m%n"

log4j = {
    appenders {
        console name: "stdout",
        layout: pattern(conversionPattern: commonPattern)
        appender new DailyRollingFileAppender(
            name: "kihdb", datePattern: "'.'yyyy-MM-dd",
            file: "${logDirectory}/kihdb.log",
            layout: pattern(conversionPattern: commonPattern))
    }

    root {
        error 'kihdb', 'stdout'
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
          'grails.buildtestdata.DomainInstanceBuilder',
	  'grails.app'
   
     debug 'grails.app',
	   'dk.silverbullet'
    }
