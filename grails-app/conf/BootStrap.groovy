import dk.silverbullet.kihdb.StatisticJob
import dk.silverbullet.kihdb.UploadToLabdatabankJob
import dk.silverbullet.kihdb.constants.Constants
import dk.silverbullet.kihdb.dk.sundhed.RegisterCitizensJob
import dk.silverbullet.kihdb.model.*
import dk.silverbullet.kihdb.model.phmr.*
import dk.silverbullet.kihdb.model.types.Sex
import dk.silverbullet.kihdb.model.xds.XDSDocument
import dk.silverbullet.kihdb.util.KIHProxySelector
import grails.util.Holders
import kih_database.dk.silverbullet.kihdb.xds.DetectsMissingDocumentsJob
import org.codehaus.groovy.grails.commons.ApplicationAttributes

import java.text.SimpleDateFormat

class BootStrap {
    private static final CREATED_BY_NAME = "BootStrap"

    BootStrapUtil bootStrapUtil
    def grailsApplication
    def documentTransformationService

    def sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    Set<UUID> randomUUIDs = new HashSet<UUID>()

    def init = { servletContext ->

        configureDatasource(servletContext)

        bootStrapUtil = new BootStrapUtil()

        Date now = new Date()

        def useProxySet = Holders?.grailsApplication?.config?.xds?.documentregistry?.useProxy
        if (useProxySet) {
            if (Boolean.valueOf(useProxySet)) {
                log.info "Setting proxy selector"
                ProxySelector.setDefault(new KIHProxySelector())
            } else {
                log.debug "Not setting proxy"
            }
        }

        def runStatisticJob = Holders.grailsApplication.config?.statisticJob?.run
        log.info "Activacting scheduled statistic job: " + runStatisticJob
        if (runStatisticJob && Boolean.valueOf(runStatisticJob)) {
            def cron = Holders?.grailsApplication?.config?.statisticJob?.cron
            if (cron) {
                StatisticJob.schedule(cron)
            } else {
                log.warn 'Please configure statisticJob.cron'
            }
        }

        def runUploadLabdatabankJob = Holders.grailsApplication.config?.uploadLabdatabankJob?.run
        log.info "Activacting scheduled upload-to-Labdatabank job: " + runUploadLabdatabankJob
        if (runUploadLabdatabankJob && Boolean.valueOf(runUploadLabdatabankJob)) {
            def cron = Holders?.grailsApplication?.config?.uploadLabdatabankJob?.cron
            if (cron) {
                UploadToLabdatabankJob.schedule(cron)
            } else {
                log.warn 'Please configure uploadLabdatabankJob.cron'
            }
        }

        def useRegisterCitizenRunSet = Holders.grailsApplication.config?.registerCitizens?.run
        log.info 'Activacting scheduled registering citizens job: ' + Boolean.valueOf(useRegisterCitizenRunSet)

        if (useRegisterCitizenRunSet) {
            if (Boolean.valueOf(useRegisterCitizenRunSet)) {
                def schedule = Holders?.grailsApplication?.config?.registerCitizens?.repeatIntervalMillis

                if (schedule) {
                    RegisterCitizensJob.schedule(schedule as Long, -1, [name: 'RegisterCitizens', startDelay: 20000 as Long])
                } else {
                    log.warn "Please configure registerCitizen.repeatIntervalMillis"
                }
            }
        }

        def useDetectMissingDocumentsSet = Holders?.grailsApplication?.config?.detectMissingDocument?.run

        log.info "Activacting scheduled detects missing document registry registration job : " + Boolean.valueOf(
                useDetectMissingDocumentsSet)
        if (Boolean.valueOf(useDetectMissingDocumentsSet)) {
            def schedule = Holders?.grailsApplication?.config?.detectMissingDocument?.repeatIntervalMillis

            if (schedule) {
                DetectsMissingDocumentsJob.schedule(schedule as Long, -1, [name: 'DetectMissingDocument', startDelay: 30000 as Long])
            } else {
                log.warn "Please configure detectMissingDocument.repeatIntervalMillis"
            }
        }

        environments {
            development {
                log.info "Initializing for DEVEL"
                initTypesEtcForDevelAndTest()
            }
            kihdbdevel {
                log.info "Initializing for KIHDB DEVEL"
                initTypesEtcForDevelAndTest()
            }
            winbuild {
                log.info "Initializing for DEVEL"
                initTypesEtcForDevelAndTest()
            }
            test {
                log.info "Initializing for TEST"
                initTypesEtcForDevelAndTest()
            }
            rntest {
                log.info "Initializing for RN TEST"
                initTypesEtcForDevelAndTest()
            }
            rnprod {
                log.info "Initializing for RN PROD"
                initTypesEtcForProduction()
            }
            rnstag {
                log.info "Initializing for RN STAG"
                initTypesEtcForProduction()
            }
            production {
                log.info "Initializing for PROD"
                // TODO Nothing here - yet..
            }
        }
    }

/**
 * Configure connectionpool to check connections
 *
 * @see <a href="http://java.dzone.com/news/database-connection-pooling">http://java.dzone.com/news/database-connection-pooling</a>
 * @see <a href="http://greybeardedgeek.net/2010/09/12/database-connection-pooling-in-grails-solving-the-idle-timeout-issue/">http://greybeardedgeek.net/2010/09/12/database-connection-pooling-in-grails-solving-the-idle-timeout-issue/</a>
 */
    def configureDatasource(def servletContext) {

        // Avoid dead connections in connectionpool

        def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
        def dataSource = ctx.dataSourceUnproxied

        dataSource.setMinEvictableIdleTimeMillis(1000 * 60 * 30)
        dataSource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 30)
        dataSource.setNumTestsPerEvictionRun(3)
        dataSource.setTestOnBorrow(true)
        dataSource.setTestWhileIdle(false)
        dataSource.setTestOnReturn(false)
        dataSource.setValidationQuery("SELECT 1")

//        dataSource.properties.each { log.debug it }
    }

    def setupRolesAndTypes() {
        createPermissions()
        createRoles()
    }


    def createPermissions() {
        PermissionName.allValues().collect {
            bootStrapUtil.createPermissionIfNotExists(it)
        }
    }

    def createRoles() {

        Date now = new Date()
        def role

        def adminRole = bootStrapUtil.setupRoleIfNotExists(BootStrapUtil.RoleName.ROLE_ADMIN.value())
        bootStrapUtil.setupPermissionsForRole(adminRole)

        // Roles
        def healthcareProfessionalRole = bootStrapUtil.setupRoleIfNotExists(BootStrapUtil.RoleName.ROLE_HEALTHCARE_PROFESSIONAL.value())
        bootStrapUtil.setupPermissionsForRole(healthcareProfessionalRole)
        def systemRole = bootStrapUtil.setupRoleIfNotExists(BootStrapUtil.RoleName.ROLE_SYSTEM.value())
        bootStrapUtil.setupPermissionsForRole(systemRole)
    }

    User setupUserIfNotExists(String username, String password) {
        def user = User.findByUsername(username) ?: new User(
                username: username,
                password: password,
                cleartextPassword: password,
                enabled: true,
                createdBy: CREATED_BY_NAME,
                modifiedBy: CREATED_BY_NAME,
                createdDate: new Date(),
                modifiedDate: new Date()
        ).save(failOnError: true)

        return user
    }


    def createAdminUser(String code) {
        def adminUser = setupUserIfNotExists("admin", code)
        Role adminRole = Role.findByAuthority(BootStrapUtil.RoleName.ROLE_ADMIN.value())
        bootStrapUtil.addUserToRoleIfNotExists(adminUser, adminRole)
    }

    def createXdsDocument(def nancy) {
        if (!XDSDocument.findByUuid("1.3.6.1.4...2300")) {
            log.debug 'Creating XDSDocument 1.3.6.1.4...2300...'
            new XDSDocument(uuid: "1.3.6.1.4...2300", document: "TEST-DOK".bytes, mimeType: "text/plain").save(failOnError: true)
        }

        log.debug "Citizen for XDS: " + nancy.id

        // Create PHMR and XDS document from sample
        if (!SelfMonitoringSample.findByXdsDocumentUuid(Constants.TEST_NANCY_PHMR_UUID)) {
            SelfMonitoringSample s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()

            s.xdsDocumentUuid = Constants.TEST_NANCY_PHMR_UUID

            s.citizen = nancy
            s.createdByText = "Helbredsprofilen"

            // Create FEV1
            def dates = [
                    ["2013-08-10T18:13:51Z", "3.2"],
                    ["2013-08-11T18:13:51Z", "3.7"],
                    ["2013-08-12T18:13:51Z", "3.3"],
                    ["2013-08-13T18:13:51Z", "3.9"],
                    ["2013-08-14T18:13:51Z", "3.4"]
            ]

            s.author = createAuthor(nancy)


            for (d in dates) {
                def l = bootStrapUtil.createFev1(sdf.parse(d[0]), d[1])
                s.addToReports(l)
            }

            if (!s.save(failOnError: true)) {
                log.error "Errors while saving: " + sample.errors
            }

            //        s.createdDateTime = sdf.parse(dates[0][0])
            // TODO Virker ikke offline
            documentTransformationService.createXdsFromSelfMonitoringSample(s)

            log.debug "Created XDS document with UUID: " + s.xdsDocumentUuid + " for Nancy "
        }
    }

    AddressPostal createAddressPostal() {
        new AddressPostal(
                mailDeliverySublocationIdentifier: "Hjertemedicinsk afdeling B",
                streetName: "Valdemarsgade",
                streetBuildingIdentifier: "53",
                postCodeIdentifier: "5700",
                districtName: "Svendborg",
        ).save(failOnError: true)
    }

    AddressPostal createAddressPostal(Citizen citizen) {

        new AddressPostal(
                streetName: citizen.streetName,
                postCodeIdentifier: citizen?.zipCode,
                districtName: citizen?.city,
                municipalityName: "?",
                countryIdentificationCodeTypeValue: "?",
                countryIdentificationCodeTypeScheme: "?"
        ).save(failOnError: true)
    }


    Custodian createCustodian() {

        Custodian custodian = new Custodian()

        custodian.addToIds(new IdTp(identifier: "88878685", identifierCode: "SOR").save(failOnError: true))

        custodian.name = "Odense Universitetshospital - Svendborg Sygehus"

        custodian.phoneNumberSubscriber = new PhoneNumberSubscriber(phoneNumberIdentifier: "65223344", phoneNumberUse: "WP").save(failOnError: true)
        custodian.address = createAddressPostal()

        custodian.save(failOnError: true)
        return custodian
    }

    Author createAuthor() {

        Author author = new Author()
        author.time = new Date()

        author.addToIds(new IdTp(identifier: "88878685", identifierCode: "SOR").save(failOnError: true))

        author.addressPostalType = createAddressPostal()

        author.assignedPersonGivenName = "Anders"
        author.assignedPersonMiddleName = "?"
        author.assignedPersonSurnameName = "Andersen"
        author.representedOrganization = "Odense Universitetshospital - Svendborg Sygehus"

        author.addToPhoneNumberSubscribers(new PhoneNumberSubscriber(phoneNumberIdentifier: "65112233", phoneNumberUse: "WP").save(failOnError: true))
        author.addToEmailAddress(new EmailAddress(emailAddressIdentifier: "hjma@ouh-svendborg.dk", emailAddressUse: "WP").save(failOnError: true))

        author.save(failOnError: true)
        return author
    }

    Author createAuthor(Citizen citizen) {
        Author author = new Author()
        author.time = new Date()

        author.addToIds(new IdTp(identifier: "88878685", identifierCode: "SOR").save(failOnError: true))
        author.addToIds(new IdTp(identifier: "88878685", identifierCode: "SOR").save(failOnError: true))

        author.addressPostalType = createAddressPostal(citizen)

        author.assignedPersonGivenName = (citizen?.firstName ? citizen?.firstName : null)
        author.assignedPersonMiddleName = (citizen?.middleName ? citizen?.middleName : null)
        author.assignedPersonSurnameName = (citizen?.lastName ? citizen?.lastName : null)
        author.representedOrganization = null

        if (citizen?.contactTelephone) {
            author.addToPhoneNumberSubscribers(new PhoneNumberSubscriber(phoneNumberIdentifier: citizen.contactTelephone, phoneNumberUse: "H").save(failOnError: true))
        }
        if (citizen.email) {
            author.addToEmailAddress(new EmailAddress(emailAddressIdentifier: citizen.email, emailAddressUse: "H").save(failOnError: true))
        }

        author.save(failOnError: true)
        return author
    }


    LegalAuthenticator createLegalAuthenticator() {
        LegalAuthenticator legalAuthenticator = new LegalAuthenticator()
        legalAuthenticator.time = new Date()
        legalAuthenticator.signatureCode = "NI"

        legalAuthenticator.addToIds(new IdTp(identifier: "88878685", identifierCode: "SOR").save(failOnError: true))

        legalAuthenticator.addToPhoneNumberSubscribers(new PhoneNumberSubscriber(phoneNumberIdentifier: "65223344", phoneNumberUse: "WP").save(failOnError: true))

        legalAuthenticator.addToEmailAddress(new EmailAddress(emailAddressIdentifier: "hjma@ouh-svendborg.dk", emailAddressUse: "WP").save(failOnError: true))

        legalAuthenticator.assignedPersonGivenName = "Anders"
        legalAuthenticator.assignedPersonSurnameName = "Andersen"

        legalAuthenticator.representedOrganizationName = "Odense Universitetshospital - Svendborg Sygehus"

        legalAuthenticator.address = createAddressPostal()

        legalAuthenticator.save(failOnError: true)
        return legalAuthenticator
    }

    def initTypesEtcForProduction() {
        setupRolesAndTypes()

        initializeIUPAC()
        initializeMeasurementTypes()
    }


    def initTypesEtcForDevelAndTest() {

        setupRolesAndTypes()

        def systemUser = bootStrapUtil.setupUserIfNotExists("system", "system_23")
        def healthcareProfUser = bootStrapUtil.setupUserIfNotExists("hp", "hp_23")

        def systemRole = Role.findByAuthority(BootStrapUtil.RoleName.ROLE_SYSTEM.value())
        def healthcareProfessionalRole = Role.findByAuthority(BootStrapUtil.RoleName.ROLE_HEALTHCARE_PROFESSIONAL.value())

        bootStrapUtil.addUserToRoleIfNotExists(systemUser, systemRole)
        bootStrapUtil.addUserToRoleIfNotExists(healthcareProfUser, healthcareProfessionalRole)

        (1..10).each { num ->
            def user = bootStrapUtil.setupUserIfNotExists("hp" + num, "hp_23")
            bootStrapUtil.addUserToRoleIfNotExists(user, healthcareProfessionalRole)
        }

        initializeIUPAC()
        initializeMeasurementTypes()

        def nancy = createCitizen(firstName: 'Nancy', middleName: 'Ann', lastName: 'Berggren',
                ssn: 2512484916,
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                sex: Sex.FEMALE,
                streetName: 'Gammel Kongevej 62',
                zipCode: '1200',
                city: 'København',
                country: 'Denmark',
                mobilePhone: "12345678",
                contactTelephone: "12345690",
                email: "email@example.com"
        )

        def firstNames = ["Katrine", "Line", "Lisa", "Lone", "Mette", "Susanne"]
        def lastNames = ["Andersen", "Petersen", "Hansen", "Jensen", "Sørensen"]

        for (i in 1..13) { //Enough to activate pagination
            createCitizen(firstName: firstNames[(int) (Math.random() * firstNames.size())],
                    lastName: lastNames[(int) (Math.random() * lastNames.size())],
                    ssn: 2512484916 - i,
                    dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1948-12-25"),
                    sex: Sex.FEMALE,
                    streetName: 'Åbogade 15',
                    zipCode: '8200',
                    city: 'Aarhus N',
                    country: 'Denmark',
                    mobilePhone: null,
                    contactTelephone: "99999999",
                    phone: null,
                    email: null)
        }


        def pk26 = createCitizen(firstName: "FornavnK26",
                lastName: "EfternavnK26",
                ssn: "2105669996",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1966-05-21"),
                sex: Sex.FEMALE,
                streetName: 'AdresseK26 26',
                zipCode: '9998',
                city: 'ByK26',
                country: 'Denmark',
                mobilePhone: null,
                contactTelephone: "99999999",
                email: "MailK26@mail.dk")


        def pk8 = createCitizen(firstName: "FornavnK8",
                lastName: "EfternavnK8",
                ssn: "0307539996",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1953-03-07"),
                sex: Sex.FEMALE,
                streetName: 'AdresseK8 8',
                zipCode: '9998',
                city: 'ByK8',
                country: 'Denmark',
                mobilePhone: null,
                contactTelephone: "99999999",
                phone: "99999999",
                email: "MailK8@mail.dk")



        def pm33 = createCitizen(firstName: "FornavnM33",
                lastName: "EfternavnM33",
                ssn: "2303439995",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1943-03-23"),
                sex: Sex.MALE,
                streetName: 'AdresseM33 33',
                zipCode: '9998',
                city: 'ByM33',
                country: 'Denmark',
                mobilePhone: null,
                contactTelephone: "99999999",
                phone: "99999999",
                email: "MailM33@mail.dk")

        def minimal = createCitizen(
                ssn: "0101860124",
                dateOfBirth: new SimpleDateFormat("yyyy-MM-dd").parse("1986-01-01"))





        if (pm33) {
            log.debug "Creating dataset for Mand33"
            createTestdataForM33(pm33, sdf)
            createTestdataForM33(nancy, sdf)
            createTestdataForM33(minimal, sdf)
        }

        // Test data for K8
        if (pk8) {
            log.debug "Creating dataset for Kvinde8"
            createTestdataForK8(pk8, sdf)
            createTestdataForK8(nancy, sdf)
        }

        //Create test data for K26
        if (pk26) {
            log.debug "Creating dataset for Kvinde26"
            createTestDataForK26(pk26, sdf)
            createTestDataForK26(nancy, sdf)
        }

        createXdsDocument(nancy)

//        log.debug "Started server with " + SelfMonitoringSample.count + " samples and " + LaboratoryReport.count + " reports"
    }

    private void initializeIUPAC() {
        //Load IUPAC data
        def filePath = "resources/IUPAC_data.xml"
        def text = grailsApplication.parentContext.getResource("classpath:$filePath").inputStream.text

        StringBuffer sb = new StringBuffer()
        text.eachLine { line ->
            sb.append(line)
        }

        log.debug("Loading IUPAC XML")
        def elementsIUPAC = new XmlParser().parseText(sb.toString())
        elementsIUPAC.Code.each {
            if (!IUPACCode.findByCode(it.@code)) {
                def u = new IUPACCode()
                u.name = it.@name
                u.code = it.@code
                u.description = it.@description
                u.unit = it.@unit
                u.comment = it.@comment
                u.save(flush: true, failOnError: true)
            }
        }
    }

    private void initializeMeasurementTypes() {
        //Load MeasurementType data
        def filePath = "resources/measurement_type_data.csv"
        def text = grailsApplication.parentContext.getResource("classpath:$filePath").inputStream.getText('UTF-8')

        def measurementTypes = []
        text.splitEachLine(":") { fields ->
            if (!MeasurementType.findByCode(fields[0])) {
                new MeasurementType(
                        code: fields[0],
                        nkn: fields[1],
                        longName: fields[2],
                        unit: fields[3]
                ).save(flush: true, failOnError: true)
            }
        }
    }

    private void createTestDataForK26(Citizen pk26, SimpleDateFormat sdf) {
        def dates = []

        def s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pk26
        s.createdByText = "Helbredsprofilen"

        // Create FEV1
        dates = [
                ["2013-08-10T18:13:51Z", "3.2"],
                ["2013-08-11T18:13:51Z", "3.7"],
                ["2013-08-12T18:13:51Z", "3.3"],
                ["2013-08-13T18:13:51Z", "3.9"],
                ["2013-08-14T18:13:51Z", "3.4"]
        ]

        for (d in dates) {
            def l = bootStrapUtil.createFev1(sdf.parse(d[0]), d[1])
            s.addToReports(l)
        }

//        s.createdDateTime = sdf.parse(dates[0][0])
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null
        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pk26
        s.createdByText = "Helbredsprofilen"

        // Create FEV1
        dates = [
                ["2013-08-20T18:13:51Z", "3.3"],
                ["2013-08-21T18:13:51Z", "3.7"],
                ["2013-08-22T18:13:51Z", "3.2"],
                ["2013-08-23T18:13:51Z", "3.6"],
                ["2013-08-24T18:13:51Z", "3.1"]
        ]

        for (d in dates) {
            def l = bootStrapUtil.createFev1(sdf.parse(d[0]), d[1])
            s.addToReports(l)
        }
//        s.createdDateTime = sdf.parse(dates[0][0])

        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pk26
        s.createdByText = "MedCom"

        dates = [
                ["2013-08-21T13:06:29Z", "66.0"],
                ["2013-08-21T14:10:44Z", "57.0"]
        ]

        for (d in dates) {
            def l = bootStrapUtil.createPulseMeasurement(sdf.parse(d[0]), d[1])
            s.addToReports(l)
        }

//        s.createdDateTime = sdf.parse(dates[0][0])
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        // Create Pulse
        dates = [
                ["2013-08-21T14:17:18Z", "57.0"],
                ["2013-08-21T14:15:38Z", "57.0"],
                ["2013-08-22T16:10:37Z", "56.0"],
                ["2013-08-23T10:13:01Z", "55.0"],
                ["2013-08-23T13:56:45Z", "58.0"],
                ["2013-08-24T13:55:54Z", "60.0"]
        ]

        for (d in dates) {
            s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk26
            s.createdByText = "trifork-demo"
            def l = bootStrapUtil.createPulseMeasurement(sdf.parse(d[0]), d[1])
            s.addToReports(l)
//            s.createdDateTime = sdf.parse(d[0])
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }

        // Create bloodsugar
        dates = [
                ["2013-08-10T13:55:54Z", "5.5"],
                ["2013-08-11T13:55:54Z", "6.5"],
                ["2013-08-12T13:55:54Z", "4.5"],
                ["2013-08-13T13:55:54Z", "6.8"],
                ["2013-08-14T13:55:54Z", "5.5"],
                ["2013-08-20T13:55:54Z", "5.5"],
                ["2013-08-21T13:55:54Z", "6.5"],
                ["2013-08-22T13:55:54Z", "7.5"],
                ["2013-08-23T13:55:54Z", "6.5"],
                ["2013-08-24T13:55:54Z", "7.8"],
                ["2013-08-25T13:55:54Z", "7.5"],
                ["2013-08-26T13:55:54Z", "7.5"],
                ["2013-08-27T13:55:54Z", "6.0"],
                ["2013-08-28T13:55:54Z", "5.5"],
                ["2013-08-29T13:55:54Z", "5.6"],
                ["2013-08-30T13:55:54Z", "6.1"],
        ]

        for (d in dates) {
            s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk26
            s.createdByText = "MedCom-demo"
            def l = bootStrapUtil.createBloodsugar(sdf.parse(d[0]), d[1])
            s.addToReports(l)
//            s.createdDateTime = sdf.parse(d[0])
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }

        // Create Hb1Ac
        dates = [["2013-08-10T13:55:54Z", "6.0"],
                 ["2013-08-11T13:55:54Z", "6.4"],
                 ["2013-08-12T13:55:54Z", "6.8"],
                 ["2013-08-13T13:55:54Z", "6.1"],
                 ["2013-08-14T13:55:54Z", "6.2"],
                 ["2013-08-15T13:55:54Z", "6.3"],
                 ["2013-08-20T13:55:54Z", "6.4"],
                 ["2013-08-21T13:55:54Z", "6.6"],
                 ["2013-08-22T13:55:54Z", "6.3"],
                 ["2013-08-23T13:55:54Z", "6.1"],
                 ["2013-08-24T13:55:54Z", "6.4"],
                 ["2013-08-25T13:55:54Z", "6.6"]]

        for (date in dates) {
            s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk26
            s.createdByText = "MedCom-demo"
            def l = bootStrapUtil.createHbA1c(sdf.parse(date[0]), date[1])
            s.addToReports(l)
//            s.createdDateTime = sdf.parse(date[0])
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }
    }

    private void createTestdataForK8(Citizen pk8, SimpleDateFormat sdf) {
        def dates = []

        // CalProtein
        dates = [
                ["2013-08-10T11:15:25Z", "81.2"],
                ["2013-08-11T11:11:25Z", "81.9"],
                ["2013-08-12T10:15:25Z", "68.1"],
                ["2013-08-13T11:15:25Z", "90.1"],
                ["2013-08-14T09:25:25Z", "84.4"],
                ["2013-08-15T12:34:27Z", "79.7"],
                ["2013-08-23T11:15:25Z", "83.2"],
                ["2013-08-24T11:11:25Z", "80.2"],
                ["2013-08-25T10:15:25Z", "78.1"],
                ["2013-08-26T11:15:25Z", "80.7"],
                ["2013-08-27T09:25:25Z", "87.1"],
                ["2013-08-28T12:34:27Z", "77.9"]
        ]

        for (d in dates) {
            def s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk8
            s.createdByText = "ConstantMed"
            def l = bootStrapUtil.createCalProtein(sdf.parse(d[0]), d[1])
//            s.createdDateTime = sdf.parse(d[0])
            s.addToReports(l)
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }

        // Weight
        dates = [
                ["2013-08-10T12:34:27Z", "110.9"], ["2013-08-11T12:34:27Z", "112.1"],
                ["2013-08-12T12:34:27Z", "110.0"], ["2013-08-13T12:34:27Z", "109.2"],
                ["2013-08-14T12:34:27Z", "111.3"], ["2013-08-15T12:34:27Z", "111.6"],
                ["2013-08-23T12:34:27Z", "110.3"],
                ["2013-08-24T12:34:27Z", "109.4"], ["2013-08-25T12:34:27Z", "110.9"],
                ["2013-08-26T12:34:27Z", "111.8"], ["2013-08-27T12:34:27Z", "112.1"],
                ["2013-08-28T12:34:27Z", "112.7"]]
        for (d in dates) {
            def s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk8
            s.createdByText = "ConstantMed"
            def l = bootStrapUtil.createWeight(sdf.parse(d[0]), d[1])
            s.addToReports(l)
//            s.createdDateTime = sdf.parse(d[0])
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }

        // ProteinUri
        dates = [
                ["2013-08-10T12:34:27Z", "1.7"], ["2013-08-11T12:34:27Z", "1.8"],
                ["2013-08-12T12:34:27Z", "1.6"], ["2013-08-13T12:34:27Z", "1.7"],
                ["2013-08-14T12:34:27Z", "1.8"], ["2013-08-15T12:34:27Z", "1.8"],
                ["2013-08-23T12:34:27Z", "1.9"], ["2013-08-24T12:34:27Z", "1.9"],
                ["2013-08-25T12:34:27Z", "1.7"], ["2013-08-26T12:34:27Z", "1.5"],
                ["2013-08-27T12:34:27Z", "1.6"], ["2013-08-28T12:34:27Z", "1.7"]]
        for (d in dates) {
            def s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

            s.citizen = pk8
            s.createdByText = "ConstantMed"
            def l = bootStrapUtil.createProteinUri(sdf.parse(d[0]), d[1])
            s.addToReports(l)
//            s.createdDateTime = sdf.parse(d[0])
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }

        // Saturation
        dates = [["2013-08-10T12:34:27Z", "69"], ["2013-08-11T12:34:27Z", "71"],
                 ["2013-08-12T12:34:27Z", "72"], ["2013-08-13T12:34:27Z", "73"],
                 ["2013-08-14T12:34:27Z", "72"], ["2013-08-15T12:34:27Z", "69"],
                 ["2013-08-23T12:34:27Z", "68"], ["2013-08-24T12:34:27Z", "69"],
                 ["2013-08-25T12:34:27Z", "70"], ["2013-08-26T12:34:27Z", "70"],
                 ["2013-08-27T12:34:27Z", "69"], ["2013-08-28T12:34:27Z", "71"]]
        for (d in dates) {
            def s = new SelfMonitoringSample()
            s.legalAuthenticator = createLegalAuthenticator()
            s.author = createAuthor()
            s.custodian = createCustodian()

//            s.createdDateTime = sdf.parse(d[0])
            s.citizen = pk8
            s.createdByText = "ConstantMed"
            def l = bootStrapUtil.createSaturation(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            s.save(failOnError: true)
//            log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

            s = null
        }
    }

    private void createTestdataForM33(Citizen pm33, SimpleDateFormat sdf) {
        def dates

        // FEV1
        dates = [["2013-08-23T10:15:25Z", "3.2"], ["2013-08-24T11:15:25Z", "3.7"],
                 ["2013-08-25T09:15:25Z", "2.9"], ["2013-08-26T10:20:20Z", "3.5"],
                 ["2013-08-22T11:20:20Z", "2.9"], ["2013-08-10T10:15:25Z", "3.1"],
                 ["2013-08-11T11:15:25Z", "3.7"], ["2013-08-12T09:15:25Z", "2.8"],
                 ["2013-08-13T10:20:20Z", "3.9"], ["2013-08-14T11:20:20Z", "2.5"]]

        def s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"

        for (d in dates) {
            def l = bootStrapUtil.createFev1(sdf.parse(d[0]), d[1])
            s.addToReports(l)
        }

//        s.createdDateTime = sdf.parse(dates[0][0])
        s.save(failOnError: true)

//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"

        // Puls
        dates = [["2013-05-24T13:06:29Z", "66.0"], ["2013-05-22T14:18:44Z", "57.0"],
                 ["2013-05-21T10:17:18Z", "55.0"], ["2013-05-20T16:15:38Z", "58.0"],
                 ["2013-05-18T14:13:37Z", "56.0"], ["2013-05-15T15:13:01Z", "57.0"],
                 ["2013-05-15T13:56:45Z", "58.0"], ["2013-05-11T10:50:00Z", "54.0"],
                 ["2013-05-12T10:50:00Z", "61.0"], ["2013-05-13T11:01:00Z", "58.0"]]
        for (d in dates) {
            def l = bootStrapUtil.createPulseMeasurement(sdf.parse(d[0]), d[1])
            s.addToReports(l)
        }
//        s.createdDateTime = sdf.parse(dates[0][0])
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"

        // Blodtryk

        dates = [["2013-08-23T07:35:10Z", "124", "60"],
                 ["2013-08-23T22:35:10Z", "147", "70"],
                 ["2013-08-24T07:40:10Z", "136", "58"],
                 ["2013-08-24T23:10:10Z", "128", "58"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"

//        s.createdDateTime = sdf.parse(dates[0][0])
        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null


        dates = [["2013-08-25T07:46:10Z", "131", "57"],
                 ["2013-08-25T23:15:10Z", "125", "62"],
                 ["2013-08-22T06:46:10Z", "116", "57"],
                 ["2013-08-22T21:15:10Z", "142", "68"]]


        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"

//        s.createdDateTime = sdf.parse(dates[0][0])
        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        dates = [["2013-08-26T06:46:10Z", "125", "64"],
                 ["2013-08-26T21:15:10Z", "147", "66"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        dates = [["2013-08-10T07:35:10Z", "141", "65"],
                 ["2013-08-10T22:35:10Z", "147", "66"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
//        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        dates = [["2013-08-11T07:40:10Z", "138", "55"],
                 ["2013-08-11T23:10:10Z", "134", "60"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        dates = [["2013-08-12T07:46:10Z", "133", "54"],
                 ["2013-08-12T23:15:10Z", "129", "61"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
//        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)

//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()
        s = null

        dates = [["2013-08-13T06:46:10Z", "126", "58"],
                 ["2013-08-13T21:15:10Z", "148", "65"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
//        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)
//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()

        s = null

        dates = [["2013-08-14T06:46:10Z", "143", "60"],
                 ["2013-08-14T21:15:10Z", "141", "59"]]

        s = new SelfMonitoringSample()
        s.legalAuthenticator = createLegalAuthenticator()
        s.author = createAuthor()
        s.custodian = createCustodian()

        s.citizen = pm33
        s.createdByText = "Helbredsprofilen"
//        s.createdDateTime = sdf.parse(dates[0][0])

        for (d in dates) {
            def l = bootStrapUtil.createSystolicBloodpressure(sdf.parse(d[0]), d[1])
            s.addToReports(l)
            l = bootStrapUtil.createDiastolicBloodpressure(sdf.parse(d[0]), d[2])
            s.addToReports(l)
        }
        s.save(failOnError: true)

//        log.debug "Saved measurement : " + s.createdByText + " reports: " + s.reports.size()
        s = null
    }

    def destroy = {
    }

    def createCitizen(params) {
        Date now = new Date()

        Citizen p = Citizen.findBySsn(params.ssn)

        if (!p) {

            p = new Citizen(params)

            p.createdBy = "System"
            p.modifiedBy = "System"
            p.createdDate = now
            p.modifiedDate = now

            p.save(failOnError: true)
        }

        return p
    }

    def resolveTypeFromNode(n) {
        def type
        switch (n.name()) {
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}MRC':
                type = MeasurementTypeName.MRC
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}NYHA':
                type = MeasurementTypeName.NYHA
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Iltmaetning':
                type = MeasurementTypeName.OXYGENSATURATION
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}INR':
                type = MeasurementTypeName.INR
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}AntalExacerbationer':
                type = MeasurementTypeName.NUMBER_OF_EXACERBARATION
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Puls':
                type = MeasurementTypeName.PULSE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}HbA1C':
                type = MeasurementTypeName.HBA1C
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Blodsukker':
                type = MeasurementTypeName.BLOODSUGAR
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Kost':
                type = MeasurementTypeName.DIET
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Vaegt':
                type = MeasurementTypeName.WEIGHT
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Livvidde':
                type = MeasurementTypeName.WAIST_SIZE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Hoejde':
                type = MeasurementTypeName.HEIGHT
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}BMI':
                type = MeasurementTypeName.BMI
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Skridttaeller':
                type = MeasurementTypeName.PEDOMETER
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Motion':
                type = MeasurementTypeName.EXERCISE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}ProteinUri':
                type = MeasurementTypeName.PROTEIN_URI
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}OedemGrad':
                type = MeasurementTypeName.EDEMA_GRADE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}FosterAktivitet':
                type = MeasurementTypeName.FETUS_ACTIVITY
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Spirometri':
                type = MeasurementTypeName.SPIROMETRI
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}FEV1':
                type = MeasurementTypeName.FEV1
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}FVC':
                type = MeasurementTypeName.FVC
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}FEV1FVCRatio':
                type = MeasurementTypeName.FEV1FVCRATIO
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}FEV1Pct':
                type = MeasurementTypeName.FEV1PCT
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}KlinikBT':
                type = MeasurementTypeName.CLINCIAN_BLOODPRESSURE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Systolisk':
                type = MeasurementTypeName.SYSTOLIC
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Diastolisk':
                type = MeasurementTypeName.DIASTOLIC
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}HjemmeBT':
                type = MeasurementTypeName.PATIENT_BLOODPRESSURE
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Kolesterol':
                type = MeasurementTypeName.CHOLESTEROL
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}SamletKolesterol':
                type = MeasurementTypeName.COMBINED_CHOLESTEROL
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}HDL':
                type = MeasurementTypeName.HDL
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}LDL':
                type = MeasurementTypeName.LDL
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Triglycerid':
                type = MeasurementTypeName.TRIGLYCERID
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Rygning':
                type = MeasurementTypeName.SMOKING
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}PakkeAar':
                type = MeasurementTypeName.PACK_YEAR
                break
            case '{http://rep.oio.dk/medcom.dk/xml/schemas/2011/06/20/}Status':
                type = MeasurementTypeName.STATUS
                break

            default: break
        }
        return type
    }
}
