<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
    xmlns:mc="urn:oio:medcom:chronicdataset:1.0.0" 
    xmlns:cpr="http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/"
    xmlns:itst="http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/" 
    xmlns:xkom="http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/" 
    xmlns:dkcc="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/"
    xmlns:dkcc2005="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/"
    xmlns:fmk2008 = "http://www.dkma.dk/medicinecard/xml.schema/2008/06/01"
    xmlns:fmk2009 = "http://www.dkma.dk/medicinecard/xml.schema/2009/01/01"
    >
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="mc:CitizenChronicDataset">
        <html>
            <head>
                <title>Fælles Kroniker Data</title>
                <style type="text/css">
                    <![CDATA[
                    body{
                        font-size:10pt;
                    }
                    
                    
                    table{
                        width:100%;
                        padding:10px 0px 10px 0px;
                    }
                    
                    td{
                        background-color:white;
                        width:50%;
                        padding:3px;
                    }
                    
                    table.edit{
                        margin:0px;
                        padding:0px;
                    }
                    
                    table.edit tr td{
                        border:1px solid black;
                        min-height:1em;
                        width:auto;
                    }
                    
                    td.field{
                        border:1px solid black;
                        min-height:1em;
                    }
                    
                    td.container{
                        padding:0px;
                        margin:0px;
                        background-color:transparent;
                    }
                    
                    td.heading{
                        background-color:#bedefc;
                        border:1px solid black;
                    }
                    
                    div.background{
                        background-color:#7ebefd;
                        border:1px solid black;
                        margin:10px;
                        padding:10px;
                        width:60em;
                    }
                    
                    h4{
                        font-size:14pt;
                        margin:10px 2px 0px 10px;
                        padding-left:2px;
                    }
                    
                    h5{
                        font-size:12pt;
                        margin:0px 2px 0px 10px;
                        padding-left:2px;
                    }
                    
                    h6{
                        font-size:10pt;
                        margin:0px 2px 0px 2px;
                        padding-left:2px;
                    }
                    h7{
                        font-size:8pt;
                        margin:0px 2px 0px 2px;
                        padding-left:2px;
                    }
                    
                    span.generaltext{
                        padding:5px;
                    }
                    
                    .label{
                        font-weight:700;
                    }
                    
                    table.part{
                        width:auto;
                    }
                    
                    table.part tr td{
                        width:auto;
                    }
                    
                    table.part tr td{
                        padding:2px 4px 2px 2px;
                        border:0px solid black;
                    }
                    
                    span.bold{
                        font-weight:700;
                    }
                    
                    span.italic{
                        font-style:italic;
                    }
                    
                    span.underline{
                        text-decoration:underline;
                    }
                    
                    p.right{
                        text-align:right;
                        margin-bottom:0px;
                        margin-top:0px;
                    }
                    
                    span.fixedfont{
                        font-family:"Courier New", Courier, mono;
                    }
                    
                    table.table{
                        background-color:white;
                        border:none;
                    }
                    
                    table.table th{
                        text-align:left;
                    }
                    
                    table.table tr td{
                        width:auto;
                    }
                    
                    table.table tr td.subtitle{
                        padding-top:0px;
                        padding-left:10px;
                    }
                    
                    table.table tr.subtitle td{
                        padding-top:0px;
                    }]]></style>
            </head>
            <body>
                <div class="background">
                    <h4>Fælles Kroniker Data</h4>

                    <table>
                        <tr>
                            <td class="heading">Borger</td>
                            <td class="heading">Egen læge</td>
                        </tr>
                        <tr valign="top">
                            <td class="field">
                                <xsl:apply-templates select="mc:Citizen"/>
                            </td>
                            <td class="field">
                                <xsl:apply-templates select="mc:GeneralPractitioner"/>
                            </td>
                        </tr>
                    </table>

                    <table>
                        <tr>
                            <td class="heading">Pårørende</td>
                            <td class="heading">Kontakter</td>
                        </tr>
                        <tr valign="top">
                            <td class="field">
                                <xsl:apply-templates select="mc:Relative"/>
                            </td>
                            <td class="field">
                                <xsl:apply-templates select="mc:ContactPersonCollection"/>
                            </td>
                        </tr>
                    </table>

                    <xsl:apply-templates select="mc:Consent"/>
                    <xsl:apply-templates select="mc:MedicalInvestigationCollection"/>
                    <xsl:apply-templates select="mc:KramPredictor"/>
                    <xsl:apply-templates select="mc:DiaryNoteCollection"/>
                    <xsl:apply-templates select="mc:DiagnosisOfRelevanceCollection"/>
                    <xsl:apply-templates select="mc:CurrentDrugEffectuationCollection"/>
                    <xsl:apply-templates select="mc:LaboratoryReportOfRelevanceCollection"/>
                    <xsl:apply-templates select="mc:HealthAndPreventionProfile"/>
                    <xsl:apply-templates select="mc:HealthProfessionalNoteCollection"/>
                    <xsl:apply-templates select="mc:AppointmentCollection"/>
                    <xsl:apply-templates select="mc:SelfMonitoredSampleCollection"/>
                    <xsl:apply-templates select="mc:PersonalGoalCollection"/>
                </div>
            </body>
        </html>
    </xsl:template>



    <xsl:template match="mc:Citizen">
        <table class="part">
            <tr>
                <td class="label">CPR nummer</td>
                <td>
                    <xsl:value-of select="cpr:PersonCivilRegistrationIdentifier"/>
                </td>
            </tr>
            <xsl:apply-templates select="itst:PersonNameStructure"/>
            <xsl:apply-templates select="xkom:AddressPostal"/>

            <tr>
                <td class="label">Telefon</td>
                <td>
                    <xsl:value-of select="mc:PhoneNumberIdentifier"/>
                </td>
            </tr>
            <tr>
                <td class="label">E-mail</td>
                <td>
                    <xsl:value-of select="mc:EmailAddressIdentifier"/>
                </td>
            </tr>

        </table>
    </xsl:template>

    <xsl:template match="mc:GeneralPractitioner">
        <table class="part">
            <tr>
                <td class="label">Ydernummer</td>
                <td>
                    <xsl:value-of select="mc:MedicalPracticeIdentifier"/>
                </td>
            </tr>
            <tr>
                <td class="label">Praksis</td>
                <td>
                    <xsl:value-of select="mc:MedicalPracticeName"/>
                </td>
            </tr>
            <xsl:apply-templates select="itst:PersonNameStructure"/>
            <xsl:apply-templates select="xkom:AddressPostal"/>

            <tr>
                <td class="label">Telefon</td>
                <td>
                    <xsl:value-of select="mc:PhoneNumberIdentifier"/>
                </td>
            </tr>
            <tr>
                <td class="label">E-mail</td>
                <td>
                    <xsl:value-of select="mc:EmailAddressIdentifier"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:Relative">
        <table class="part">
            <xsl:apply-templates select="itst:PersonNameStructure"/>
            <xsl:apply-templates select="xkom:AddressPostal"/>

            <tr>
                <td class="label">Telefon</td>
                <td>
                    <xsl:value-of select="mc:PhoneNumberIdentifier"/>
                </td>
            </tr>
            <tr>
                <td class="label">E-mail</td>
                <td>
                    <xsl:value-of select="mc:EmailAddressIdentifier"/>
                </td>
            </tr>

        </table>
    </xsl:template>

    <xsl:template match="mc:ContactPersonCollection">
        <table class="part">
            <xsl:apply-templates select="mc:CountyContactPerson"/>
            <xsl:apply-templates select="mc:HospitalContactPerson"/>
        </table>
    </xsl:template>

    <xsl:template match="mc:HospitalContactPerson">
        <tr>
            <td class="label">Sygehuskontakt</td>
            <td>
                <xsl:value-of select="mc:ContactPersonDetailsText"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="mc:CountyContactPerson">
        <tr>
            <td class="label">Kommunekontakt</td>
            <td>
                <xsl:value-of select="mc:ContactPersonDetailsText"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="mc:Consent">
        <table>
            <xsl:apply-templates select="mc:ConsentGivenIndicator" mode="boolean-row">
                <xsl:with-param name="heading">Din læge, sygehuset og kommunen har også adgang til din kronikerjournal</xsl:with-param>
            </xsl:apply-templates>
        </table>
    </xsl:template>

    <xsl:template match="mc:MedicalInvestigationCollection">
        <table>
            <tr>
                <td class="heading">Lægens udredninger</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <br/>
                    <xsl:apply-templates select="mc:MedicalInvestigation"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:MedicalInvestigation">
        <center>
            <h5>
                <xsl:apply-templates select="mc:CreatedByText"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </h5>
        </center>

        <table class="part">
            <tr>
                <td class="label">Ønsket undersøgelse</td>
                <td>
                    <xsl:apply-templates mode="formattedtext" select="mc:DesiredServiceFormattedText"/>
                </td>
            </tr>
            <tr>
                <td class="label">Anamnsese</td>
                <td>
                    <xsl:apply-templates mode="formattedtext" select="mc:AnamnesisFormattedText"/>
                </td>
            </tr>
            <tr>
                <td class="label">Henvisningsdiagnoser</td>
                <td>
                    <xsl:apply-templates select="mc:ReferralDiagnosisCollection/mc:ReferralDiagnosis"/>
                </td>
            </tr>

        </table>
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
        
    </xsl:template>

    <xsl:template match="mc:ReferralDiagnosis">
        <xsl:apply-templates select="mc:DiagnosisIdentifier"/> (<xsl:apply-templates select="mc:DiagnosisClassificationIdentifier"/>) <xsl:if test="mc:DescriptionText">
            <xsl:text> - </xsl:text>
            <xsl:apply-templates select="mc:DescriptionText"/>
        </xsl:if>
        <br/>
    </xsl:template>

    <xsl:template match="mc:KramPredictor">
        <table>
            <tr>
                <td class="heading">Markører for KRAM (Kost, Rygning, Alkohol og Motion)</td>
            </tr>
            <tr valign="top">
                <td class="field">

                    <table class="part">
                        <xsl:call-template name="LaboratoryReport">
                            <xsl:with-param name="label">Vægt</xsl:with-param>
                            <xsl:with-param name="element" select="mc:Weight"/>
                        </xsl:call-template>

                        <xsl:call-template name="LaboratoryReport">
                            <xsl:with-param name="label">Højde</xsl:with-param>
                            <xsl:with-param name="element" select="mc:Height"/>
                        </xsl:call-template>

                        <xsl:call-template name="LaboratoryReport">
                            <xsl:with-param name="label">Rygning</xsl:with-param>
                            <xsl:with-param name="element" select="mc:Smoking"/>
                        </xsl:call-template>

                        <xsl:call-template name="LaboratoryReport">
                            <xsl:with-param name="label">Alkoholforbrug</xsl:with-param>
                            <xsl:with-param name="element" select="mc:Alcohol"/>
                        </xsl:call-template>

                        <xsl:call-template name="LaboratoryReport">
                            <xsl:with-param name="label">Motion</xsl:with-param>
                            <xsl:with-param name="element" select="mc:Exercise"/>
                        </xsl:call-template>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:DiaryNoteCollection">
        <table>
            <tr>
                <td class="heading">Borgerens dagbog</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <br/>
                    <xsl:apply-templates select="mc:DiaryNote"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:DiaryNote">
        <center>
            <h5>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </h5>
        </center>
        <br/>
        <xsl:apply-templates mode="formattedtext" select="mc:ContentsFormattedText"/>
        <br/>
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
    </xsl:template>

    <xsl:template match="mc:DiagnosisOfRelevanceCollection">
        <table>
            <tr>
                <td class="heading">Andre relevante diagnoser</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <table class="part">
                        <xsl:apply-templates select="mc:Diagnosis"/>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:Diagnosis">
        <tr>
            <td class="label">
                <xsl:apply-templates select="mc:DiagnosisIdentifier"/> (<xsl:apply-templates select="mc:DiagnosisClassificationIdentifier"/>) </td>
            <td>
                <xsl:apply-templates select="mc:DescriptionText"/>
            </td>
            <td>
                <small>
                    <i>
                    <xsl:apply-templates select="mc:CreatedByText"/>
                    <xsl:call-template name="date">
                        <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                    </xsl:call-template>
                    </i>
                </small>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="mc:CurrentDrugEffectuationCollection">
        <table>
            <tr>
                <td class="heading">Aktuel medicin</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <table class="part"> 
                      <xsl:apply-templates select="mc:DrugEffectuation"/>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>
    
    <xsl:template match="mc:DrugEffectuation">
        <tr>
            <td class="label">
                <xsl:apply-templates select="fmk2008:DrugName"/>
            </td>
            <td>
                <xsl:apply-templates select="fmk2008:DrugFormText"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="fmk2008:DrugStrengthText"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="fmk2009:DosageFreeText"/>
            </td>
            <td>
                <small>
                    <i>
                        <xsl:apply-templates select="mc:CreatedBy"/>
                        <xsl:call-template name="date">
                            <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                        </xsl:call-template>
                    </i>
                </small>
            </td>
        </tr>
    </xsl:template>
    

    <xsl:template match="mc:LaboratoryReportOfRelevanceCollection">
        <table>
            <tr>
                <td class="heading">Relevante laboratoriesvar</td>
            </tr>
            <tr valign="top">
                <td class="field">

                    <table class="part">
                        <xsl:for-each select="mc:LaboratoryReport">
                            <xsl:call-template name="LaboratoryReport">
                                <xsl:with-param name="element" select="."/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:HealthAndPreventionProfile">
        <table>
            <tr>
                <td class="heading">Rehabilitering - SOFT tilbud</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <table class="edit">
                        <tr>
                            <th></th>
                            <th style="width:120px">Tilbudt</th>
                            <th style="width:120px">Gennemført</th>
                            <th style="width:120px">Ikke aktuelt</th>
                        </tr>
                        
                        <xsl:call-template name="HealthAndPreventionService">
                            <xsl:with-param name="title" select="'Fysisk træning'"/>
                            <xsl:with-param name="element" select="mc:PhysicalRehabilitation"/>
                        </xsl:call-template>
                        
                        <xsl:call-template name="HealthAndPreventionService">
                            <xsl:with-param name="title" select="'Patientuddannelse'"/>
                            <xsl:with-param name="element" select="mc:PatientEducation"/>
                        </xsl:call-template>
                        
                        <xsl:call-template name="HealthAndPreventionService">
                            <xsl:with-param name="title" select="'Rygestopkursus'"/>
                            <xsl:with-param name="element" select="mc:SmokingCessationCourse"/>
                        </xsl:call-template>
                        
                        <xsl:call-template name="HealthAndPreventionService">
                            <xsl:with-param name="title" select="'Kostvejledninig'"/>
                            <xsl:with-param name="element" select="mc:NutritionalCounseling"/>
                        </xsl:call-template>
                        
                    </table>
                    
                    <table class="edit">
                        <tr>
                            <th></th>
                            <th style="width:120px">Serviceloven</th>
                            <th style="width:120px">Sundhedsloven</th>
                            <th style="width:120px">Sundhedscenter</th>
                        </tr>
                        
                        <xsl:call-template name="StatutoryProvidedService">
                            <xsl:with-param name="title" select="'Modtager aktuelt ydelser'"/>
                            <xsl:with-param name="element" select="mc:CurrentStatutoryProvidedService"/>
                        </xsl:call-template>
                        <xsl:call-template name="StatutoryProvidedService">
                            <xsl:with-param name="title" select="'Modtaget ydelser seneste år'"/>
                            <xsl:with-param name="element" select="mc:RecentYearStatutoryProvidedService"/>
                        </xsl:call-template>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>
    
    <xsl:template name="HealthAndPreventionService">
        <xsl:param name="title"/>
        <xsl:param name="element"/>
        
        <tr>
            <td><xsl:value-of select="$title"/></td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="$element/mc:HealthAndPreventionServiceStatus/text()='offered'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="$element/mc:HealthAndPreventionServiceStatus/text()='finish'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="$element/mc:HealthAndPreventionServiceStatus/text()='not_relevant'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
        </tr>        
    </xsl:template>
    
    
    <xsl:template name="StatutoryProvidedService">
        <xsl:param name="title"/>
        <xsl:param name="element"/>
        
        <tr>
            <td><xsl:value-of select="$title"/></td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="translate(substring($element/mc:AccordingToSocialLegislation/text(),1,1),'YNTFtf10','ynynynyn')='y'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="translate(substring($element/mc:AccordingToHealthLegislation/text(),1,1),'YNTFtf10','ynynynyn')='y'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
            <td>
                <center>
                    <xsl:choose>
                        <xsl:when test="translate(substring($element/mc:ProvidedByHealthCenter/text(),1,1),'YNTFtf10','ynynynyn')='y'">X</xsl:when>
                        <xsl:otherwise>&#160;</xsl:otherwise>
                    </xsl:choose>
                </center>
            </td>
        </tr>        
    </xsl:template>

    <xsl:template match="mc:HealthProfessionalNoteCollection">
        <table>
            <tr>
                <td class="heading">SKL - noter (Kontinuation)</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <br/>
                    <xsl:apply-templates select="mc:HealthProfessionalNote"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:HealthProfessionalNote">
        <center>
            <h5>
                <xsl:choose>
                    <xsl:when test="mc:HealthCareAreaIdentifier/text() = 'gp'">Egen læge</xsl:when>
                    <xsl:when test="mc:HealthCareAreaIdentifier/text() = 'county'">Kommune</xsl:when>
                    <xsl:when test="mc:HealthCareAreaIdentifier/text() = 'hospital'">Sygehus</xsl:when>
                </xsl:choose>
                <xsl:if test="mc:TitleText">
                    <xsl:text>: </xsl:text>
                    <xsl:apply-templates select="mc:TitleText"/>                    
                </xsl:if>
            </h5>
            <small>
                <i>
                <xsl:apply-templates select="mc:CreatedBy"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
                </i>
            </small>
        </center>
        <br/>
        <xsl:apply-templates mode="formattedtext" select="mc:ContentsFormattedText"/>
        <br/>
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
        
    </xsl:template>

    <xsl:template match="mc:AppointmentCollection">
        <table>
            <tr>
                <td class="heading">Borgerens kalender</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <br/>
                    <xsl:apply-templates select="mc:Appointment"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:Appointment">
        <center>
            <h5>
                <xsl:apply-templates select="mc:CreatedBy"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </h5>
        </center>
        <br/>
        <xsl:apply-templates mode="formattedtext" select="mc:DescriptionFormattedText"/>
        <br/>
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="mc:SelfMonitoredSampleCollection">
        <table>
            <tr>
                <td class="heading">Monitorering - måledata</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <xsl:apply-templates select="mc:SelfMonitoredSample"/>
                    <table class="part"> </table>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:SelfMonitoredSample">
        <center>
            <h5>
                <xsl:apply-templates select="mc:SampleCategoryIdentifier"/>
            </h5>
            <small>
                <i>
                    <xsl:apply-templates select="mc:CreatedByText"/>
                    <xsl:call-template name="date">
                        <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                    </xsl:call-template>
                    
                </i>
            </small>
        </center>
        <br/>

        <table class="part">
            <xsl:for-each select="mc:LaboratoryReportCollection/mc:LaboratoryReport">
                <xsl:call-template name="LaboratoryReport">
                    <xsl:with-param name="element" select="."/>
                </xsl:call-template>
            </xsl:for-each>
        </table>
        
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="mc:PersonalGoalCollection">
        <table>
            <tr>
                <td class="heading">Borgerens målsætning</td>
            </tr>
            <tr valign="top">
                <td class="field">
                    <xsl:apply-templates select="mc:PersonalGoal"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="mc:PersonalGoal">
        <center>
            <h5>
                <xsl:apply-templates select="mc:SampleCategoryIdentifier"/>
            </h5>
            <small>
                <i>
                    <xsl:apply-templates select="mc:CreatedByText"/>
                    <xsl:call-template name="date">
                        <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                    </xsl:call-template>
                    
                </i>
            </small>
        </center>
        <br/>
        
        <table class="part">
            <xsl:for-each select="mc:PersonalGoalResultCollection/mc:PersonalGoalResult">
                <xsl:call-template name="PersonalGoalResult">
                    <xsl:with-param name="element" select="."/>
                </xsl:call-template>
            </xsl:for-each>
        </table>
        
        <br/>
        <xsl:if test="position() != last()">
            <hr/>    
        </xsl:if>
    </xsl:template>
    

    <!-- Common types -->

    <xsl:template name="LaboratoryReport">
        <xsl:param name="element"/>
        <xsl:param name="label" select="mc:AnalysisText"/>

        <tr>
            <td class="label">
                <xsl:value-of select="$label"/>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'less_than'">&lt; </xsl:when>
                    <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'greater_than'">&gt; </xsl:when>
                </xsl:choose>
                <xsl:value-of select="$element/mc:ResultText"/>
                <xsl:text> </xsl:text>

                <xsl:if test="$element/mc:ResultUnitText/text() != 'arb.Enhed'">
                    <xsl:value-of select="$element/mc:ResultUnitText"/>
                </xsl:if>

            </td>
            <td>
                <i>
                    <small>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$element/mc:ProducerOfLabResult/mc:Identifier"/>
                        <xsl:call-template name="date">
                            <xsl:with-param name="value" select="$element/mc:CreatedDateTime"/>
                        </xsl:call-template>
                    </small>
                </i>
            </td>
        </tr>



    </xsl:template>

    <xsl:template name="PersonalGoalResult">
        <xsl:param name="element"/>
        <xsl:param name="label" select="mc:AnalysisText"/>
        
        <tr>
            <td class="label">
                <xsl:value-of select="$label"/>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'less_than'">&lt; </xsl:when>
                    <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'greater_than'">&gt; </xsl:when>
                </xsl:choose>
                <xsl:value-of select="$element/mc:ResultText"/>
                <xsl:text> </xsl:text>
                
                <xsl:if test="$element/mc:ResultUnitText/text() != 'arb.Enhed'">
                    <xsl:value-of select="$element/mc:ResultUnitText"/>
                </xsl:if>
                
            </td>
        </tr>
        
        
        
    </xsl:template>
    
    <xsl:template name="date">
        <xsl:param name="value"/>

        <xsl:variable name="date">
            <xsl:value-of select="substring-before($value, 'T') "/>
        </xsl:variable>

        <xsl:value-of
            select="concat(' (', 
            number(substring-after(substring-after($date, '-'), '-')), '/', 
            number(substring-before(substring-after($date, '-'), '-')), '-', 
            substring-before($date, '-'),
            ')')"/>


    </xsl:template>

    <xsl:template match="itst:PersonNameStructure">
        <tr>
            <td class="label">Navn</td>
            <td>
                <xsl:apply-templates select="dkcc:PersonGivenName"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="dkcc:PersonMiddleName"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="dkcc:PersonSurnameName"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="xkom:AddressPostal">
        <tr>
            <td class="label">Adresse</td>
            <td>
                <xsl:apply-templates select="dkcc2005:StreetName"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="dkcc:StreetBuildingIdentifier"/>
                <xsl:if test="dkcc:FloorIdentifier">
                    <xsl:text>, </xsl:text>
                    <xsl:apply-templates select="dkcc:FloorIdentifier"/>
                    <xsl:text>.</xsl:text>
                </xsl:if>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="dkcc:SuiteIdentifier"/>
                <br/>
                <xsl:apply-templates select="dkcc2005:PostCodeIdentifier"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="dkcc2005:DistrictName"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template mode="boolean-row" match="*">
        <xsl:param name="heading"/>
        <tr>
            <td class="heading">
                <xsl:value-of select="$heading"/>
            </td>
            <td class="field">
                <xsl:choose>
                    <xsl:when test="translate(substring(text(),1,1),'YNTFtf10','ynynynyn')='y'">Ja</xsl:when>
                    <xsl:otherwise>Nej</xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>
    <xsl:template mode="formattedtext" match="text()">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Center">
        <center>
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </center>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Bold">
        <span class="bold">
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </span>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Italic">
        <span class="italic">
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </span>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Right">
        <p class="right">
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </p>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:FixedFont">
        <span class="fixedfont">
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </span>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Underline">
        <span class="underline">
            <xsl:apply-templates mode="formattedtext" select="text() | *"/>
        </span>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Space">
        <xsl:text disable-output-escaping="yes"> &#160;</xsl:text>
    </xsl:template>
    <xsl:template mode="formattedtext" match="mc:Break">
        <br/>
    </xsl:template>

</xsl:stylesheet>
