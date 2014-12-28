<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:mc="urn:oio:medcom:chronicdataset:1.0.0" xmlns:cpr="http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/" xmlns:itst="http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/" xmlns:xkom="http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/" xmlns:dkcc="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/" xmlns:dkcc2005="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/"
    xmlns:fmk2008="http://www.dkma.dk/medicinecard/xml.schema/2008/06/01" xmlns:fmk2009="http://www.dkma.dk/medicinecard/xml.schema/2009/01/01">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="mc:CitizenChronicDataset">
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
        <html>
            <head>
                <title>Fælles Kroniker Data</title>
                <link href="http://svn.medcom.dk/svn/drafts/Standarder/Den%20gode%20kronikerservice/html/css/bootstrap.min.css" rel="stylesheet"/>
                <script src="http://svn.medcom.dk/svn/drafts/Standarder/Den%20gode%20kronikerservice/html/js/jquery.js"/>
                <script src="http://svn.medcom.dk/svn/drafts/Standarder/Den%20gode%20kronikerservice/html/js/bootstrap.min.js"/>
                <script src="http://svn.medcom.dk/svn/drafts/Standarder/Den%20gode%20kronikerservice/html/js/raphael-min.js"/>
                <script src="http://svn.medcom.dk/svn/drafts/Standarder/Den%20gode%20kronikerservice/html/js/morris.min.js"/>
                
                <style type="text/css">
                    <![CDATA[
                    table.edit{
                        width:100%;
                        margin:0px;
                        padding:0px;
                        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
                        font-size: 14px;
                        line-height: 20px;
                    }
                    
                    table.edit tr td{
                        border:1px solid #E5E5E5;
                        min-height:1em;
                        width:auto;
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
                    }]]></style>

                <script type="text/javascript">
                    
                    $(document).ready(function() {
                      $("span[rel=tooltip]").tooltip();
                    }
                    );
                    
                </script>

                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body>
                <div class="container">
                    <div class="row">
                        <div class="span12">
                            <div class="accordion" id="accordion">

                                <xsl:apply-templates select="mc:Citizen"/>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title"> Egen læge</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:GeneralPractitioner"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Pårørende</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:Relative"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Kontakter</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:ContactPersonCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Din læge, sygehuset og kommunen har også adgang til din kronikerjournal</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:Consent"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Lægens udredninger</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:MedicalInvestigationCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Markører for KRAM (Kost, Rygning, Alkohol og Motion)</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:KramPredictor"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Borgerens dagbog</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:DiaryNoteCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Andre relevante diagnoser</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:DiagnosisOfRelevanceCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Aktuel medicin</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:CurrentDrugEffectuationCollection"/>
                                </xsl:call-template>
                                
                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Relevante laboratoriesvar</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:LaboratoryReportOfRelevanceCollection"/>
                                </xsl:call-template>
 
                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Rehabilitering - SOFT tilbud</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:HealthAndPreventionProfile"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">SKL - noter (Kontinuation)</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:HealthProfessionalNoteCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Borgerens kalender</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:AppointmentCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Monitorering - måledata</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:SelfMonitoredSampleCollection"/>
                                </xsl:call-template>

                                <xsl:call-template name="accordian">
                                    <xsl:with-param name="title">Borgerens målsætning</xsl:with-param>
                                    <xsl:with-param name="element" select="mc:PersonalGoalCollection"/>
                                </xsl:call-template>
                            </div>
                        </div>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>



    <xsl:template name="accordian">
        <xsl:param name="title"/>
        <xsl:param name="element"/>

        <xsl:variable name="id">
            <xsl:value-of select="translate($title, ' ,)(', '')"/>
        </xsl:variable>

        <div class="accordion-group">
            <div class="accordion-heading">

                <xsl:text disable-output-escaping="yes">&lt;a class="accordion-toggle" data-toggle="collapse"  href="#</xsl:text>
                <xsl:value-of select="$id"/>
                <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                <xsl:value-of select="$title"/>
                <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
            </div>
            <xsl:text disable-output-escaping="yes">&lt;div id="</xsl:text>
            <xsl:value-of select="$id"/>
            <xsl:text disable-output-escaping="yes">" class="accordion-body collapse"&gt;</xsl:text>
            <div class="accordion-inner">
                <xsl:apply-templates select="$element"/>
            </div>
            <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
        </div>

    </xsl:template>

    <xsl:template match="mc:Citizen">

        <div class="navbar">
            <div class="navbar-inner">
                <div class="brand"><xsl:apply-templates select="itst:PersonNameStructure"/> (<xsl:value-of select="cpr:PersonCivilRegistrationIdentifier"/>)</div>
                <h4 class=" pull-right">Fælles Kroniker Data</h4>
            </div>
        </div>


        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" href="#collapseOne"> Kontaktoplysninger på <xsl:apply-templates select="itst:PersonNameStructure"/>
                </a>
            </div>
            <div id="collapseOne" class="accordion-body collapse">
                <div class="accordion-inner">
                    <dl class="dl-horizontal">
                        <dt>Adresse</dt>
                        <dd>
                            <xsl:apply-templates select="xkom:AddressPostal"/>
                        </dd>

                        <dt>Telefon</dt>
                        <dd>
                            <xsl:value-of select="mc:PhoneNumberIdentifier"/>
                        </dd>

                        <dt>E-mail</dt>
                        <dd>
                            <xsl:value-of select="mc:EmailAddressIdentifier"/>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="mc:GeneralPractitioner">
        <dl class="dl-horizontal">
            <dt>Ydernummer</dt>
            <dd>
                <xsl:value-of select="mc:MedicalPracticeIdentifier"/>
            </dd>

            <dt>Praksis</dt>
            <dd>
                <xsl:value-of select="mc:MedicalPracticeName"/>
            </dd>

            <dt>Navn</dt>
            <dd>
                <xsl:apply-templates select="itst:PersonNameStructure"/>
            </dd>

            <dt>Adresse</dt>
            <dd>
                <xsl:apply-templates select="xkom:AddressPostal"/>
            </dd>

            <dt>Telefon</dt>
            <dd>
                <xsl:value-of select="mc:PhoneNumberIdentifier"/>
            </dd>

            <dt>E-mail</dt>
            <dd>
                <xsl:value-of select="mc:EmailAddressIdentifier"/>
            </dd>
        </dl>

    </xsl:template>

    <xsl:template match="mc:Relative">
        <dl class="dl-horizontal">
            <dt>Navn</dt>
            <dd>
                <xsl:apply-templates select="itst:PersonNameStructure"/>
            </dd>

            <dt>Adresse</dt>
            <dd>
                <xsl:apply-templates select="xkom:AddressPostal"/>
            </dd>

            <dt>Telefon</dt>
            <dd>
                <xsl:value-of select="mc:PhoneNumberIdentifier"/>
            </dd>

            <dt>E-mail</dt>
            <dd>
                <xsl:value-of select="mc:EmailAddressIdentifier"/>
            </dd>
        </dl>

    </xsl:template>

    <xsl:template match="mc:ContactPersonCollection">
        <dl class="dl-horizontal">
            <xsl:apply-templates select="mc:CountyContactPerson"/>
            <xsl:apply-templates select="mc:HospitalContactPerson"/>
        </dl>
    </xsl:template>

    <xsl:template match="mc:HospitalContactPerson">
        <dt>Sygehuskontakt</dt>
        <dd>
            <xsl:value-of select="mc:ContactPersonDetailsText"/>
        </dd>
    </xsl:template>

    <xsl:template match="mc:CountyContactPerson">
        <dt>Kommunekontakt</dt>
        <dd>
            <xsl:value-of select="mc:ContactPersonDetailsText"/>
        </dd>
    </xsl:template>

    <xsl:template match="mc:Consent">
        <dl class="dl-horizontal">
            <dt>Har adgang</dt>
            <dd>
                <xsl:choose>
                    <xsl:when test="translate(substring(mc:ConsentGivenIndicator/text(),1,1),'YNTFtf10','ynynynyn')='y'">Ja</xsl:when>
                    <xsl:otherwise>Nej</xsl:otherwise>
                </xsl:choose>
            </dd>
        </dl>
    </xsl:template>

    <xsl:template match="mc:MedicalInvestigationCollection">
        <xsl:apply-templates select="mc:MedicalInvestigation"/>
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

        <dl class="dl-horizontal">
            <dt>Ønsket undersøgelse</dt>
            <dd>
                <xsl:apply-templates mode="formattedtext" select="mc:DesiredServiceFormattedText"/>
            </dd>

            <dt>Anamnsese</dt>
            <dd>
                <xsl:apply-templates mode="formattedtext" select="mc:AnamnesisFormattedText"/>
            </dd>

            <dt>Henvisningsdiagnoser</dt>
            <dd>
                <xsl:apply-templates select="mc:ReferralDiagnosisCollection/mc:ReferralDiagnosis"/>
            </dd>
        </dl>
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
        <dl class="dl-horizontal">
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
        </dl>
    </xsl:template>

    <xsl:template match="mc:DiaryNoteCollection">
        <xsl:apply-templates select="mc:DiaryNote"/>
    </xsl:template>

    <xsl:template match="mc:DiaryNote">
        <center>
            <h5>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </h5>
        </center>
        <xsl:apply-templates mode="formattedtext" select="mc:ContentsFormattedText"/>
        <xsl:if test="position() != last()">
            <hr/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mc:DiagnosisOfRelevanceCollection">
        <dl class="dl-horizontal">
            <xsl:apply-templates select="mc:Diagnosis"/>
        </dl>
    </xsl:template>

    <xsl:template match="mc:Diagnosis">
        <dt>
            <xsl:apply-templates select="mc:DiagnosisIdentifier"/> (<xsl:apply-templates select="mc:DiagnosisClassificationIdentifier"/>) </dt>
        <dd>
            <xsl:apply-templates select="mc:DescriptionText"/>
            
            <xsl:variable name="createdText">
                <xsl:apply-templates select="mc:CreatedByText"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </xsl:variable>       
            
            &#160; 
            <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
            </span>  
        </dd>
    </xsl:template>

    <xsl:template match="mc:CurrentDrugEffectuationCollection">
        <dl class="dl-horizontal">
            <xsl:apply-templates select="mc:DrugEffectuation"/>
        </dl>
    </xsl:template>

    <xsl:template match="mc:DrugEffectuation">
        <dt class="label">
            <xsl:apply-templates select="fmk2008:DrugName"/>
        </dt>
        <dd>
            <xsl:apply-templates select="fmk2008:DrugFormText"/>
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="fmk2008:DrugStrengthText"/>
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="fmk2009:DosageFreeText"/>


            <xsl:variable name="createdText">
                <xsl:apply-templates select="mc:CreatedBy"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </xsl:variable>
            
            &#160; 
            <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
            </span>  
            
        </dd>
    </xsl:template>

    <xsl:template match="mc:LaboratoryReportOfRelevanceCollection">
        <dl class="dl-horizontal">
            <xsl:for-each select="mc:LaboratoryReport">
                <xsl:call-template name="LaboratoryReport">
                    <xsl:with-param name="element" select="."/>
                </xsl:call-template>
            </xsl:for-each>
        </dl>   
    </xsl:template>

    <xsl:template match="mc:HealthAndPreventionProfile">
                    <table class="edit">
                        <tr>
                            <th/>
                            <th style="width:140px">Tilbudt</th>
                            <th style="width:140px">Gennemført</th>
                            <th style="width:140px">Ikke aktuelt</th>
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
                            <th/>
                            <th style="width:140px">Serviceloven</th>
                            <th style="width:140px">Sundhedsloven</th>
                            <th style="width:140px">Sundhedscenter</th>
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
    </xsl:template>

    <xsl:template name="HealthAndPreventionService">
        <xsl:param name="title"/>
        <xsl:param name="element"/>

        <tr>
            <td>
                <xsl:value-of select="$title"/>
            </td>
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
            <td>
                <xsl:value-of select="$title"/>
            </td>
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
        <xsl:apply-templates select="mc:HealthProfessionalNote"/>
    </xsl:template>

    <xsl:template match="mc:HealthProfessionalNote">
        <center>
            <xsl:variable name="createdText">
                <xsl:apply-templates select="mc:CreatedBy"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </xsl:variable>
            
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
                &#160; 
                <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
                </span>           
            </h5>
            
            
        </center>
        <xsl:apply-templates mode="formattedtext" select="mc:ContentsFormattedText"/>
        <xsl:if test="position() != last()">
            <hr/>
        </xsl:if>

    </xsl:template>

    <xsl:template match="mc:AppointmentCollection">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Tidspunkt</th>
                    <th>Oprettet af</th>
                    <th>Beskrivelse</th>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="mc:Appointment"/>
            </tbody>
        </table>
    </xsl:template>

    <xsl:template match="mc:Appointment">
        <tr>
            <td>
                <xsl:call-template name="dateTime">
                    <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                </xsl:call-template>
            </td>
            <td>
                <xsl:apply-templates select="mc:CreatedBy"/> 
            </td>
            <td>  <xsl:apply-templates mode="formattedtext" select="mc:DescriptionFormattedText"/> </td>
        </tr>        
    </xsl:template>

    <xsl:template match="mc:SelfMonitoredSampleCollection">
         <xsl:apply-templates select="mc:SelfMonitoredSample"/>
    </xsl:template>

    <xsl:template match="mc:SelfMonitoredSample">
        <center>
            <h5>
                <xsl:apply-templates select="mc:SampleCategoryIdentifier"/>

                <xsl:variable name="createdText">
                    <xsl:apply-templates select="mc:CreatedByText"/>
                    <xsl:call-template name="date">
                        <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                    </xsl:call-template>
                </xsl:variable>
                &#160; 
                <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
                </span>           
                
            </h5>
        </center>
        <dl class="dl-horizontal">
            <xsl:for-each select="mc:LaboratoryReportCollection/mc:LaboratoryReport">
                <xsl:call-template name="LaboratoryReport">
                    <xsl:with-param name="element" select="."/>
                </xsl:call-template>
            </xsl:for-each>
        </dl>
        <xsl:if test="position() != last()">
            <hr/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mc:PersonalGoalCollection">
                    <xsl:apply-templates select="mc:PersonalGoal"/>
    </xsl:template>

    <xsl:template match="mc:PersonalGoal">
        <center>
            <h5>
                <xsl:apply-templates select="mc:SampleCategoryIdentifier"/>
                <xsl:variable name="createdText">
                    <xsl:apply-templates select="mc:CreatedByText"/>
                    <xsl:call-template name="date">
                        <xsl:with-param name="value" select="mc:CreatedDateTime"/>
                    </xsl:call-template>
                </xsl:variable>
                &#160; 
                <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
                </span>           
                
            </h5>
        </center>
        
        <xsl:variable name="category" select="mc:SampleCategoryIdentifier"/>
        
        
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Prøve</th>
                    <th>Målsætning</th>
                    <th>Seneste målilng</th>                    
                </tr>
            </thead>
            <tbody>
                <xsl:for-each select="mc:PersonalGoalResultCollection/mc:PersonalGoalResult">
                    <xsl:call-template name="PersonalGoalResult">
                        <xsl:with-param name="element" select="."/>
                        <xsl:with-param name="category" select="$category"/>
                    </xsl:call-template>
                </xsl:for-each>
            </tbody>
        </table>
                
     
        <xsl:for-each select="mc:PersonalGoalResultCollection/mc:PersonalGoalResult">
            <xsl:variable name="analysis" select="mc:AnalysisText"/>
            <xsl:variable name="id" select="generate-id(.)"/>
            <xsl:variable name="samples" select="//mc:SelfMonitoredSample[mc:SampleCategoryIdentifier=$category]"/>
            <xsl:variable name="goal"><xsl:value-of select="mc:ResultText"/></xsl:variable>
                           
            <xsl:if test="count($samples) > 1">
        
               <div class="well">
                   
               <xsl:text disable-output-escaping="yes">&lt;div id="</xsl:text><xsl:value-of select="$id"/><xsl:text disable-output-escaping="yes">"&gt;&lt;/div&gt;</xsl:text>
               </div>   
               
               <script type="text/javascript">
                 
                 Morris.Line({
                     element: '<xsl:value-of select="$id"/>',
                     data: [
   
                       <xsl:for-each select="$samples">
                           <xsl:sort select="mc:CreatedDateTime"/>
                           { x:  '<xsl:value-of select="substring-before(mc:LaboratoryReportCollection/mc:LaboratoryReport[mc:AnalysisText=$analysis]/mc:CreatedDateTime, 'T')"/>',
                             a:  <xsl:value-of select="mc:LaboratoryReportCollection/mc:LaboratoryReport[mc:AnalysisText=$analysis]/mc:ResultText"/>,
                             b: <xsl:value-of select="$goal"/>
                           } 
                            <xsl:if test="position() != last()">,</xsl:if>
                       </xsl:for-each>
                     ],
                     xkey: 'x',
                     ykeys: ['a', 'b'],
                     xLabels: 'day',
                     labels: ['<xsl:value-of select="$analysis"/>', 'Mål']
                   });
               </script>
            
            </xsl:if>
        </xsl:for-each> 
        
        
        <xsl:if test="position() != last()">
            <hr/>
        </xsl:if>
    </xsl:template>


    <xsl:template name="PersonalGoalResult">
        <xsl:param name="element"/>
        <xsl:param name="label" select="mc:AnalysisText"/>
        <xsl:param name="category"/>

        <xsl:variable name="analysis" select="mc:AnalysisText"/>
        <xsl:variable name="samples" select="//mc:SelfMonitoredSample[mc:SampleCategoryIdentifier=$category]"/>
        
        
        <xsl:variable name="lastSampleValue">
            <xsl:for-each select="$samples">
                <xsl:sort select="mc:CreatedDateTime"/>
                <xsl:if test="position() = last()">
                    <xsl:value-of select="mc:LaboratoryReportCollection/mc:LaboratoryReport[mc:AnalysisText=$analysis]/mc:ResultText"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>  
        
        <xsl:variable name="unit">
            <xsl:for-each select="$samples">
                <xsl:sort select="mc:CreatedDateTime"/>
                <xsl:if test="position() = last()">
                    <xsl:variable name="lastSampleUnit" select="mc:LaboratoryReportCollection/mc:LaboratoryReport[mc:AnalysisText=$analysis]/mc:ResultUnitText"/>
                    <xsl:choose>
                        <xsl:when test="$lastSampleUnit/text() != 'arb.Enhed'">
                            <xsl:value-of select="$lastSampleUnit"/>
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>        
        
        
        <tr>
          <td>
              <xsl:value-of select="$label"/>
          </td>
          <td>
              <xsl:choose>
                  <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'less_than'">&lt; </xsl:when>
                  <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'greater_than'">&gt; </xsl:when>
              </xsl:choose>
              <xsl:value-of select="$element/mc:ResultText"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="$unit"/>
          </td>
          
          <td>
              <xsl:value-of select="$lastSampleValue"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="$unit"/>
          </td>
          
        </tr>
        
    </xsl:template>
    
    
    

    <!-- Common types -->

    <xsl:template name="LaboratoryReport">
        <xsl:param name="element"/>
        <xsl:param name="label" select="mc:AnalysisText"/>

        <dt>
            <xsl:value-of select="$label"/>
        </dt>
        <dd >
            <xsl:variable name="createdText">
                <xsl:value-of select="$element/mc:ProducerOfLabResult/mc:Identifier"/>
                <xsl:call-template name="date">
                    <xsl:with-param name="value" select="$element/mc:CreatedDateTime"/>
                </xsl:call-template>
            </xsl:variable>
            
                
            <xsl:choose>
                <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'less_than'">&lt; </xsl:when>
                <xsl:when test="$element/mc:ResultOperatorIdentifier/text() = 'greater_than'">&gt; </xsl:when>
            </xsl:choose>
            <xsl:value-of select="$element/mc:ResultText"/>
            <xsl:text> </xsl:text>

            <xsl:if test="$element/mc:ResultUnitText/text() != 'arb.Enhed'">
                <xsl:value-of select="$element/mc:ResultUnitText"/>
            </xsl:if>
            
            
            &#160; 
            <span rel="tooltip" data-placement="top" data-original-title="{$createdText}" class="icon-info-sign">
            </span>           
        </dd>

    </xsl:template>

    <xsl:template name="date">
        <xsl:param name="value"/>
        
        <xsl:variable name="date">
            <xsl:value-of select="substring-before($value, 'T') "/>
        </xsl:variable>
        
        <xsl:value-of select="concat(' (', 
            number(substring-after(substring-after($date, '-'), '-')), '/', 
            number(substring-before(substring-after($date, '-'), '-')), '-', 
            substring-before($date, '-'),
            ')')"/>
        
        
    </xsl:template>
    
    <xsl:template name="dateTime">
        <xsl:param name="value"/>
        
        <xsl:variable name="date">
            <xsl:value-of select="substring-before($value, 'T') "/>
        </xsl:variable>
        <xsl:variable name="time">
            <xsl:value-of select="substring-after($value, 'T') "/>
        </xsl:variable>
        
        <xsl:value-of select="concat( 
            number(substring-after(substring-after($date, '-'), '-')), '/', 
            number(substring-before(substring-after($date, '-'), '-')), '-', 
            substring-before($date, '-'))"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="substring($time, 0, 6)"></xsl:value-of>
        
    </xsl:template>
    
    <xsl:template match="itst:PersonNameStructure">
        <xsl:apply-templates select="dkcc:PersonGivenName"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="dkcc:PersonMiddleName"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="dkcc:PersonSurnameName"/>
    </xsl:template>

    <xsl:template match="xkom:AddressPostal">
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
