package dk.silverbullet.kihdb.labdatabank

class TestData {
    public static final DATA = '' +
            '<Emessage xmlns="http://rep.oio.dk/sundcom.dk/medcom.dk/xml/schemas/2007/02/01/">' +
            '<Envelope>' +
            '<Sent>' +
            '<Date>2014-12-12</Date>' +
            '<Time>13:47</Time>' +
            '</Sent>' +
            '<Identifier>00114124637019</Identifier>' +
            '<AcknowledgementCode>minuspositivkvitt</AcknowledgementCode>' +
            '</Envelope>' +
            '<LaboratoryReport>' +
            '<Letter>' +
            '<Identifier>80901082504866</Identifier>' +
            '<VersionCode>XR0130K</VersionCode>' +
            '<StatisticalCode>XRPT01</StatisticalCode>' +
            '<Authorisation>' +
            '<Date>2014-12-12</Date>' +
            '<Time>12:52</Time>' +
            '</Authorisation>' +
            '<TypeCode>XRPT01</TypeCode>' +
            '</Letter>' +
            '<Sender>' +
            '<EANIdentifier>5790000120284</EANIdentifier>' +
            '<Identifier>4201050</Identifier>' +
            '<IdentifierCode>sygehusafdelingsnummer</IdentifierCode>' +
            '<OrganisationName>Sygehus Fyn, Svendborg</OrganisationName>' +
            '<DepartmentName>Klinisk Kemisk Afdeling</DepartmentName>' +
            '<MedicalSpecialityCode>klin_biokemi</MedicalSpecialityCode>' +
            '</Sender>' +
            '<Receiver>' +
            '<EANIdentifier>5790000125012</EANIdentifier>' +
            '<Identifier>989897</Identifier>' +
            '<IdentifierCode>ydernummer</IdentifierCode>' +
            '<OrganisationName>Finn Klamer</OrganisationName>' +
            '<DepartmentName>Læge</DepartmentName>' +
            '<StreetName>Lægehuset</StreetName>' +
            '<DistrictName>Erslev</DistrictName>' +
            '<PostCodeIdentifier>5777</PostCodeIdentifier>' +
            '<Physician>' +
            '<PersonInitials>FK</PersonInitials>' +
            '</Physician>' +
            '</Receiver>' +
            '<Patient>' +
            '<CivilRegistrationNumber>1211111111</CivilRegistrationNumber>' +
            '<PersonSurnameName>Pedersen</PersonSurnameName>' +
            '<PersonGivenName>Jens</PersonGivenName>' +
            '</Patient>' +
            '<RequisitionInformation>' +
            '<Sample>' +
            '<LaboratoryInternalSampleIdentifier>00875137</LaboratoryInternalSampleIdentifier>' +
            '<RequesterSampleIdentifier>00875137</RequesterSampleIdentifier>' +
            '<SamplingDateTime>' +
            '<Date>2014-12-12</Date>' +
            '<Time>06:30</Time>' +
            '</SamplingDateTime>' +
            '</Sample>' +
            '</RequisitionInformation>' +
            '<LaboratoryResults>' +
            '<GeneralResultInformation>' +
            '<LaboratoryInternalProductionIdentifier>0000426825</LaboratoryInternalProductionIdentifier>' +
            '<ResultsDateTime>' +
            '<Date>2014-12-12</Date>' +
            '<Time>13:47</Time>' +
            '</ResultsDateTime>' +
            '</GeneralResultInformation>' +
            '<Result>' +
            '<ResultStatusCode>svar_rettet</ResultStatusCode>' +
            '<Analysis>' +
            '<AnalysisCode>NPU01807</AnalysisCode>' +
            '<AnalysisCodeType>iupac</AnalysisCodeType>' +
            '<AnalysisCodeResponsible>SST</AnalysisCodeResponsible>' +
            '<AnalysisShortName>Creatinin;P/S</AnalysisShortName>' +
            '<AnalysisCompleteName>P-Creatininium,stofk.(Jaffe).<Break/></AnalysisCompleteName>' +
            '</Analysis>' +
            '<ProducerOfLabResult>' +
            '<Identifier>TestIdentifier</Identifier>' +
            '<IdentifierCode>TST</IdentifierCode>' +
            '</ProducerOfLabResult>' +
            '<ReferenceInterval>' +
            '<TypeOfInterval>uspecificeret</TypeOfInterval>' +
            '<LowerLimit>60</LowerLimit>' +
            '<UpperLimit>130</UpperLimit>' +
            '</ReferenceInterval>' +
            '<ResultType>numerisk</ResultType>' +
            '<Value>212</Value>' +
            '<Unit>(mol/l.</Unit>' +
            '</Result>' +
            '</LaboratoryResults>' +
            '</LaboratoryReport>' +
            '</Emessage>'
}
