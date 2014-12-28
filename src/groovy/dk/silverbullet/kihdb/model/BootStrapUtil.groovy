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

import dk.silverbullet.kihdb.service.MonitoringServiceUtil

class BootStrapUtil {
    Date now = new Date()

    def util = new MonitoringServiceUtil()

    public static enum RoleName {
        ROLE_ADMIN("Administrator"), ROLE_HEALTHCARE_PROFESSIONAL("Healthcare Professional"), ROLE_SYSTEM("System")

        private final String value

        RoleName(String value) {
            this.value = value
        }

        String value() {
            value
        }
    }


    void addUserToRoleIfNotExists(User user, Role role) {
        if (!user?.authorities.contains(role)) {
            UserRole.create(user, role)
        }
    }

    User setupUserIfNotExists(String username, String password) {
        def user = User.findByUsername(username) ?: new User(
                username: username,
                password: password,
                enabled: true).save(failOnError: true)

        return user
    }

    void setupuAdminUserRoleIfNotExists(Date date) {

        def adminUser = User.findByUsername('admin') ?: new User(
                username: 'admin',
                password: 'admin',
                enabled: true,
                createdBy: "System",
                modifiedBy: "System",
                createdDate: date,
                modifiedDate: date).save(failOnError: true)

        if (!adminUser.authorities.contains(adminRole)) {
            UserRole.create(adminUser, adminRole)
        }
    }

    Role setupRoleIfNotExists(String authority) {
        setupRoleIfNotExists(authority, null)
    }

    Role setupRoleIfNotExists(String authority, Date date) {
        Role.findByAuthority(authority) ?: new Role(authority: authority, createdBy: "System", modifiedBy: "System", createdDate: date, modifiedDate: date).save(failOnError: true)
    }


    Permission createPermissionIfNotExists(String permission) {
        Permission retVal = Permission.findByPermission(permission) ?: new Permission(permission: permission).save(failOnError: true)
        return retVal
    }

    RolePermission createRolePermissionIfNotExists(Role r, Permission p) {
        RolePermission retVal = RolePermission.findByRoleAndPermission(r, p) ?: new RolePermission(role: r, permission: p).save(failOnError: true)
        return retVal
    }


    List<RolePermission> setupPermissionsForRole(Role role) {
        List<RolePermission> result = new ArrayList<RolePermission>()
        permissionsForRole(role).each {
//            log.debug "Role / permission: " + it
            def permission = Permission.findByPermission(it)
            def rolePermission = createRolePermissionIfNotExists(role, permission)
            result << rolePermission
        }

        result
    }

    private def permissionsForRole(Role r) {
//        log.debug "Added permissions to Role: " + r
        switch (r.authority) {
            case RoleName.ROLE_ADMIN.value():
                return [
                        PermissionName.WEB_LOGIN,
                        PermissionName.USER_DELETE,
                        PermissionName.USER_WRITE,
                        PermissionName.USER_READ,
                        PermissionName.USER_CREATE,
                        PermissionName.USER_READ_ALL,
                        PermissionName.ROLE_DELETE,
                        PermissionName.ROLE_WRITE,
                        PermissionName.ROLE_READ,
                        PermissionName.ROLE_CREATE,
                        PermissionName.ROLE_READ_ALL,
                        PermissionName.AUDITLOG_READ,
                        PermissionName.SELF_MONITORING_SAMPLE_READ_ALL,
                        PermissionName.SELF_MONITORING_SAMPLE_READ,
                        PermissionName.CITIZEN_READ_ALL,
                        PermissionName.CITIZEN_READ,
                        PermissionName.CITIZEN_READ_SEARCH,
                        PermissionName.INSTRUMENT_READ,
                        PermissionName.LABORATORY_READ,
                        PermissionName.LAB_PRODUCER_READ,
                        PermissionName.XDS_DOCUMENT_READ
                ]
            case RoleName.ROLE_HEALTHCARE_PROFESSIONAL.value():
                return [
                        PermissionName.WEB_LOGIN,
                        PermissionName.MEASUREMENT_READ,
                        PermissionName.SELF_MONITORING_SAMPLE_READ_ALL,
                        PermissionName.SELF_MONITORING_SAMPLE_READ,
                        PermissionName.CITIZEN_READ_ALL,
                        PermissionName.CITIZEN_READ,
                        PermissionName.CITIZEN_READ_SEARCH,
                        PermissionName.INSTRUMENT_READ,
                        PermissionName.LABORATORY_READ,
                        PermissionName.LAB_PRODUCER_READ,
                        PermissionName.XDS_DOCUMENT_READ
                ]
            case RoleName.ROLE_SYSTEM.value():
                return [
                        PermissionName.WEB_LOGIN, // Maybe, maybe not?
                        PermissionName.AUDITLOG_READ
                ]
        }
    }


    LaboratoryReport createCalProtein(Date d, String value) {
        def ResultOperatorIdentifier = "less_than"
        def ResultAbnormalIdentifier = "to_high"
        def ResultMinimumText = "0"
        def ResultMaximumText = "50"
        def ResultTypeOfInterval = "physiological"


        def AnalysisText = "Calprotectin;F"
        def ResultText = value
        def ResultUnitText = "mg/kg"
        def IupacIdentifier = "NPU26814"
        def NationalSampleIdentifier = "999999999951"

        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier, ResultOperatorIdentifier, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval)

    }


    LaboratoryReport createProteinUri(Date d, String value) {


        def ResultEncodingIdentifier = "numeric"
        def MeasurementTransferredBy = "automatic"
        def MeasurementLocation = "home"
        def MeasuringDataClassification = "clinical"
        def MeasurementDuration = "5 sek"
        def MeasurementScheduled = "scheduled"

        def Instrument_MedComID = "EPQ12265"
        def Instrument_Manufacturer = "Siemens"
        def Instrument_ProductType = "Urine Analyzer"
        def Instrument_Model = "Clinitek Status+"
        def Instrument_SoftwareVersion = "SUA-2014r74-4"


        def instrument = createInstrument(Instrument_MedComID, Instrument_Manufacturer, Instrument_ProductType, Instrument_Model, Instrument_SoftwareVersion)
        def MeasuringCircumstances = "Måling foretaget før indtagelse af aftenmåltid"
        def HealthCareProfessionalComment = "Aftalt at måle en gang om dagen, før aftenmåltidet."

        def ResultOperatorIdentifier = "greater_than"
        def ResultAbnormalIdentifier = "to_high"
        def ResultMinimumText = "0"
        def ResultMaximumText = "4"
        def ResultTypeOfInterval = "physiological"


        def analysisText = "Protein;U"
        def resultText = value
        def resultUnitText = "g/L"
        def iupacIdentifier = "NPU03958"
        def nationalSampleIdentifier = "999999999955"

        return util.createLaboratoryReport(null, d, analysisText, resultText, ResultEncodingIdentifier, ResultOperatorIdentifier, resultUnitText, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval,
                nationalSampleIdentifier, util.createIUPACCode(iupacIdentifier), createLabProducer(), instrument, MeasurementTransferredBy, MeasurementLocation,
                MeasuringDataClassification, MeasurementDuration, MeasurementScheduled, HealthCareProfessionalComment, MeasuringCircumstances)

    }


    LaboratoryReport createSaturation(Date d, String value) {
        def analysisText = "O2 sat.;Hb(aB)"
        def resultText = value
        def resultUnitText = "%"
        def iupacIdentifier = "NPU03011"

        def nationalSampleIdentifier = "9999999996"

        def ResultOperatorIdentifier = "less_than"
        def ResultAbnormalIdentifier = "to_low"
        def ResultMinimumText = "70"
        def ResultMaximumText = "100"


        def ResultEncodingIdentifier = "numeric"
        def MeasurementTransferredBy = "automatic"
        def MeasurementLocation = "home"
        def MeasuringDataClassification = "clinical"
        def MeasurementDuration = "30 sek"
        def MeasurementScheduled = "scheduled"

        def Instrument_MedComID = "EPQ12275"
        def Instrument_Manufacturer = "Nonin"
        def Instrument_ProductType = "Pulsoximeter"
        def Instrument_Model = "Pulsoximeter"
        def Instrument_SoftwareVersion = "NO-2013r75-5"


        def instrument = createInstrument(Instrument_MedComID, Instrument_Manufacturer, Instrument_ProductType, Instrument_Model, Instrument_SoftwareVersion)
        def MeasuringCircumstances = "Måling foretaget før indtagelse af aftenmåltid"
        def HealthCareProfessionalComment = "Aftalt at måle en gang om dagen, før aftenmåltidet."

        def ResultTypeOfInterval = "physiological"

        return util.createLaboratoryReport(null, d, analysisText, resultText, ResultEncodingIdentifier, ResultOperatorIdentifier, resultUnitText, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval,
                nationalSampleIdentifier, util.createIUPACCode(iupacIdentifier), createLabProducer(), instrument, MeasurementTransferredBy, MeasurementLocation,
                MeasuringDataClassification, MeasurementDuration, MeasurementScheduled, HealthCareProfessionalComment, MeasuringCircumstances)



        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)

    }

    LaboratoryReport createBloodsugar(Date d, String value) {
        def ResultOperatorIdentifier = "less_than"
        def ResultAbnormalIdentifier = "unspecified"
        def ResultMinimumText = "2.5"
        def ResultMaximumText = "25"


        def AnalysisText = "Glukose;B"
        def ResultText = value
        def ResultUnitText = "mmol/l"
        def IupacIdentifier = "NPU02187"
        def NationalSampleIdentifier = "9999999916"

        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier, ResultOperatorIdentifier, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText)


    }


    LaboratoryReport createFev1(Date d, String value) {
        def ResultTypeOfInterval = "therapeutic"

        def ResultOperatorIdentifier = "less_than"
        def ResultAbnormalIdentifier = "to_high"
        def ResultMinimumText = "1"
        def ResultMaximumText = "3"

        def AnalysisText = "FEV1"
        def ResultUnitText = "Liter"
        def IupacIdentifier = "MCS88015"
        def NationalSampleIdentifier = "9999999999"

        def ResultText = value

        return createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier, ResultOperatorIdentifier, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval)
    }


    LaboratoryReport createHbA1c(Date d, String value) {
        def AnalysisText = "Glukose, middel (fra HbA1c);P"
        def ResultText = value
        def ResultUnitText = "mmol/l"
        def IupacIdentifier = "NPU27412"
        def NationalSampleIdentifier = "9999999915"

        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)
    }


    LaboratoryReport createProteinUri(Date d) {
        def AnalysisText = "Albumin (semikvant);U"
        def ResultText = "2"
        def ResultUnitText = "Arb.enhed 1 2 3 4"
        def IupacIdentifier = "NPU17997"
        def NationalSampleIdentifier = "9999999925"

        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)
    }

    LaboratoryReport createWeight(Date d, String value) {
        def AnalysisText = "Legeme masse;Pt"
        def ResultText = value
        def ResultUnitText = "kg"
        def IupacIdentifier = "NPU03804"
        def NationalSampleIdentifier = "9999999921"

        createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)
    }


    LaboratoryReport createLaboratoryReport(Date d, String analysisText, String resultText, String resultUnitText, String iupacCode, String nationalSampleIdentifier, String ResultOperatorIdentifier,
                                            String ResultAbnormalIdentifier, String ResultMinimumText, String ResultMaximumText, String ResultTypeOfInterval) {
        def ResultEncodingIdentifier = "numeric"
        def MeasurementTransferredBy = "automatic"
        def MeasurementLocation = "home"
        def MeasuringDataClassification = "clinical"
        def MeasurementDuration = "120 sek"
        def MeasurementScheduled = "scheduled"
        def HealthCareProfessionalComment = "Pt. havde meget svært ved at puste igennem idag"
        def MeasuringCircumstances = "Udblæsningen blev foretaget siddende på en stol lige efter morgenmaden"


        return util.createLaboratoryReport(null, d, analysisText, resultText, ResultEncodingIdentifier, ResultOperatorIdentifier, resultUnitText, ResultAbnormalIdentifier, ResultMinimumText, ResultMaximumText, ResultTypeOfInterval,
                nationalSampleIdentifier, util.createIUPACCode(iupacCode), createLabProducer(), createInstrument(), MeasurementTransferredBy, MeasurementLocation,
                MeasuringDataClassification, MeasurementDuration, MeasurementScheduled, HealthCareProfessionalComment, MeasuringCircumstances)
    }


    LaboratoryReport createLaboratoryReport(Date d, String analysisText, String resultText, String resultUnitText, String iupacIdentifier, String nationalSampleIdentifier, String resultOperatorIdentifier,
                                            String resultAbnormalIdentifier, String resultMinimumText, String resultMaximumText) {
        return createLaboratoryReport(d, analysisText, resultText, resultUnitText, iupacIdentifier, nationalSampleIdentifier, resultOperatorIdentifier, resultAbnormalIdentifier, resultMinimumText, resultMaximumText, null)

    }


    LaboratoryReport createLaboratoryReport(Date d, String analysisText, String resultText, String resultUnitText, String iupacCode, String nationalSampleIdentifier) {
        return createLaboratoryReport(d, analysisText, resultText, resultUnitText, iupacCode, nationalSampleIdentifier, null, null, null, null)
    }


    LaboratoryReport createPulseMeasurement(Date d, String value) {
        def AnalysisText = "Puls;Hjerte"
        def ResultText = value
        def ResultUnitText = "1/min"
        def NationalSampleIdentifier = "9999999914"
        def IupacIdentifier = "NPU21692"
        return createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)

    }

    Instrument createInstrument(String id, String manufacturer, String productType, String model, String swVersion) {
        def instrument = util.createInstrument(id, manufacturer, model, productType, swVersion)

        return instrument
    }


    Instrument createInstrument() {
        def Instrument_MedComID = "EPQ12225"
        def Instrument_Manufacturer = "AD Medical"
        def Instrument_ProductType = "Multifunction Kit"
        def Instrument_Model = "UA-767PTB-C"
        def Instrument_SoftwareVersion = "10.0.3452r45"
        def instrument = util.createInstrument(Instrument_MedComID, Instrument_Manufacturer, Instrument_Model, Instrument_ProductType, Instrument_SoftwareVersion)

        return instrument
    }

    LabProducer createLabProducer() {
        def LabProducer_Identifier = "Patient målt"
        def LabProducer_Identifier_Code = "POT"

        def labProducer = util.createLabProducer(LabProducer_Identifier, LabProducer_Identifier_Code)

        return labProducer
    }


    LaboratoryReport createDiastolicBloodpressure(Date d, String value) {
        def AnalysisText = "Blodtryk diastolisk;Arm"
        def ResultText = value
        def ResultUnitText = "mm/HG"
        def NationalSampleIdentifier = "9999999912"
        def IupacIdentifier = "MCS88020"

        return createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)
    }

    LaboratoryReport createSystolicBloodpressure(Date d, String value) {
        def AnalysisText = "Blodtryk systolisk;Arm"
        def ResultText = value
        def ResultUnitText = "mm/HG"
        def NationalSampleIdentifier = "9999999913"
        def IupacIdentifier = "MCS88019"
        return createLaboratoryReport(d, AnalysisText, ResultText, ResultUnitText, IupacIdentifier, NationalSampleIdentifier)

    }

}