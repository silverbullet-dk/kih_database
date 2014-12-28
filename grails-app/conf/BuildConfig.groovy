grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.war.file = "target/${appName}.war"

//// Plugin stuff
//grails.plugin.location.kihauditlog = "../kih-auditlog"

// NOTE: Forked mode is required, otherwise a too old BouncyCastle will be loaded, which causes SEAL.java to fail

grails.project.fork = [
        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

        // configure settings for the test-app JVM, uses the daemon by default
        test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:false, fork: false],
//        test: false,
//    // configure settings for the run-app JVM
        run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false, fork: false],
        // configure settings for the run-war JVM
        war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
        // configure settings for the Console UI JVM
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]



grails.tomcat.jvmArgs = ["-Xms256m", "-Xmx1024m", "-XX:PermSize=512m", "-XX:MaxPermSize=512m"]

grails.project.dependency.resolver = "aether" // or ivy


grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    def gebVersion = "0.9.3"
    def spockVersion = "0.9.2"
    def seleniumVersion = "2.24.1"


    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        mavenLocal()
        mavenCentral()
        grailsCentral()


        mavenRepo("http://ci.silverbullet.dk/artifactory/plugins-release-local")
        mavenRepo("http://ci.silverbullet.dk/artifactory/libs-release-local")
        mavenRepo("http://ci.silverbullet.dk/artifactory/ext-release-local")

        // must be below silverbullet repo
        mavenRepo("https://nexus.nspop.dk/nexus/content/groups/public")


    }
    dependencies {

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        runtime 'mysql:mysql-connector-java:5.1.18'
        runtime 'net.sourceforge.jtds:jtds:1.2.6'
        runtime 'javax.xml:jaxrpc-api:1.1'

//        runtime 'org.apache.santuario:xmlsec:1.4.5' // was .5
        runtime 'org.apache.ws.security:wss4j:1.5.12'
//        runtime 'org.apache.ws.security:wss4j:1.5.8'
        runtime 'commons-httpclient:commons-httpclient:3.1'
        runtime 'commons-codec:commons-codec:1.9'
        runtime 'com.unboundid:unboundid-ldapsdk:2.3.1'
        // runtime 'org.bouncycastle:bcprov-jdk15on:1.51'

        runtime "org.quartz-scheduler:quartz:2.2.1"



        compile("org.net4care:phmr-builder:0.0.2") {
          transitive = false
          exclude "org.slf4j:slf4j-parent:1.6.1"
          exclude "org.slf4j:slf4j-log4j12:1.6.1"
        }

//        compile "xerces:xerces:1.2.3"

                // From NPI - hoste on SB artifactory
        compile('dk.nsi.dgws.types:medcom-types:1.1.1')
        compile('dk.nsi.dgws.types:security-types:1.1.1')

        // XDS typoes
        compile 'org.connectopensource:DocRegistryWebservices:4.0.4'
        compile 'org.connectopensource:DirectWebservices:4.0.4'
        compile 'org.connectopensource:CONNECTCommonTypesLib:4.0.7'

        // Sundhed.dk personaliseringsindeks
        compile 'dk.silverbullet:dk.sundhed.personaliseringsindex-client.types:0.0.1'

        // CPR types
        compile 'dk.silverbullet:cpr-types:0.0.5'

        compile 'dk.silverbullet:laboratoriemedicinbank:0.0.1'

        // DGKS - version 1.0.2
        compile 'dk.medcom:dgks-types:0.5'


        // HSUID header
        compile('dk.nsi.hsuid:hsuid:1.1.0') {
//            transitive = true
            exclude "axis-jaxrpc"
        }


        // Add seal libraries
        compile('dk.sosi.seal:seal:2.2.1') {
//            transitive = true
            exclude "axis"
            exclude "axis-wsdl4j"
        }

//        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
        test "org.gebish:geb-spock:$gebVersion"
        test "org.gebish:geb-junit4:$gebVersion"
//        test("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
//        test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"

        test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
            exclude 'xml-apis'
        }
    }

    plugins {
        runtime ":database-migration:1.4.0"
        runtime ":hibernate:3.6.10.12"
        runtime ":resources:1.2.8"
        runtime ":kih-auditlog:0.36" // Our very own audit log plugin
        runtime ":spring-security-core:1.2.7.2"
        runtime ":tooltip:0.7"
        runtime(":twitter-bootstrap:2.3.2")
        compile(":cxf-client:1.5.7") {
            exclude "xmlsec"
            exclude "wss4j"

        }

        compile(":quartz:1.0.1") {
            transitive = false
        }
        runtime(":cxf:1.1.1")
        runtime ":jquery:1.8.0"

        runtime ":webxml:1.4.1"
        build ":tomcat:7.0.54"

        runtime(":wslite:0.7.1.0") {
            export = true
        }
        compile(":codenarc:0.19")
        test ":code-coverage:1.2.6"
        test ":geb:$gebVersion"

    }
}

//cobertura exclusions
coverage {
    exclusions = [
            '**/dk/silverbullet/kihdb/client/**',
            '**/dk/silverbullet/kihdb/service/dgks/generated/**'
    ]
}




codenarc.reports = {

// Each report definition is of the form: // REPORT-NAME(REPORT-TYPE) { // PROPERTY-NAME = PROPERTY-VALUE // PROPERTY-NAME = PROPERTY-VALUE // }

    MyXmlReport('xml') { // The report name "MyXmlReport" is user-defined; Measurements type is 'xml'
        outputFile = 'target/test-reports/CodeNarc-Measurements.xml'
        // Set the 'outputFile' property of the (XML) Measurements
    }

    MyXmlReport('html') { // The report name "MyXmlReport" is user-defined; Measurements type is 'xml'
        outputFile = 'target/test-reports/CodeNarc-Measurements.html'
        // Set the 'outputFile' property of the (XML) Measurements
    }

}

codenarc.properties = {
    // Each property definition is of the form:  RULE.PROPERTY-NAME = PROPERTY-VALUE
    GrailsPublicControllerMethod.enabled = false
    EmptyIfStatement.priority = 1
    coberturaXmlFile = "target/test-reports/cobertura/coverage.xml"

}

codenarc.ruleSetFiles = ["rulesets/basic.xml,rulesets/exceptions.xml, rulesets/imports.xml,rulesets/grails.xml, rulesets/unused.xml, rulesets/size.xml"]
