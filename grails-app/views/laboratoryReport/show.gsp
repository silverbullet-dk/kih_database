<%--
 Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ page import="dk.silverbullet.kihdb.model.LaboratoryReport" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'laboratoryReport.label', default: 'LaboratoryReport')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-laboratoryReport" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list laboratoryReport">
			
				<g:if test="${laboratoryReportInstance?.resultAbnormalIdentifier}">
				<li class="fieldcontain">
					<span id="resultAbnormalIdentifier-label" class="property-label"><g:message code="laboratoryReport.resultAbnormalIdentifier.label" default="Result Abnormal Identifier" /></span>
					
						<span class="property-value" aria-labelledby="resultAbnormalIdentifier-label"><g:message code="laboratoryReport.resultAbnormalIdentifier.value.${laboratoryReportInstance.resultAbnormalIdentifier}"/></span>
					
				</li>
				</g:if>

                <g:if test="${laboratoryReportInstance?.deleted}">
                    <li class="fieldcontain">
                        <span id="deleted-label" class="property-label"><g:message code="laboratoryReport.deleted.label" default="Slette Markeret" /></span>

                        <span class="property-value" aria-labelledby="deleted-label">${fieldValue(bean: laboratoryReportInstance, field: "deleted")}</span>

                    </li>
                </g:if>


                <g:if test="${laboratoryReportInstance?.resultEncodingIdentifier}">
				<li class="fieldcontain">
					<span id="resultEncodingIdentifier-label" class="property-label"><g:message code="laboratoryReport.resultEncodingIdentifier.label" default="Result Encoding Identifier" /></span>
					
						<span class="property-value" aria-labelledby="resultEncodingIdentifier-label"><g:message code="laboratoryReport.resultEncodingIdentifier.value.${laboratoryReportInstance.resultEncodingIdentifier}"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.resultOperatorIdentifier}">
				<li class="fieldcontain">
					<span id="resultOperatorIdentifier-label" class="property-label"><g:message code="laboratoryReport.resultOperatorIdentifier.label" default="Result Operator Identifier" /></span>
					
						<span class="property-value" aria-labelledby="resultOperatorIdentifier-label"><g:message code="laboratoryReport.resultOperatorIdentifier.value.${laboratoryReportInstance.resultOperatorIdentifier}"/></span>
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.resultMaximumText}">
				<li class="fieldcontain">
					<span id="resultMaximumText-label" class="property-label"><g:message code="laboratoryReport.resultMaximumText.label" default="Result Maximum Text" /></span>
					
						<span class="property-value" aria-labelledby="resultMaximumText-label"><g:fieldValue bean="${laboratoryReportInstance}" field="resultMaximumText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.resultMinimumText}">
				<li class="fieldcontain">
					<span id="resultMinimumText-label" class="property-label"><g:message code="laboratoryReport.resultMinimumText.label" default="Result Minimum Text" /></span>
					
						<span class="property-value" aria-labelledby="resultMinimumText-label"><g:fieldValue bean="${laboratoryReportInstance}" field="resultMinimumText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.sample}">
				<li class="fieldcontain">
					<span id="sample-label" class="property-label"><g:message code="laboratoryReport.sample.label" default="Sample" /></span>
					
						<span class="property-value" aria-labelledby="sample-label">${laboratoryReportInstance?.sample?.createdByText}</span>

				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.analysisText}">
				<li class="fieldcontain">
					<span id="analysisText-label" class="property-label"><g:message code="laboratoryReport.analysisText.label" default="Analysis Text" /></span>
					
						<span class="property-value" aria-labelledby="analysisText-label"><g:fieldValue bean="${laboratoryReportInstance}" field="analysisText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.createdDateTime}">
				<li class="fieldcontain">
					<span id="createdDateTime-label" class="property-label"><g:message code="laboratoryReport.createdDateTime.label" default="Created Date Time" /></span>
					
						<span class="property-value" aria-labelledby="createdDateTime-label"><g:formatDate date="${laboratoryReportInstance?.createdDateTime}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.iupacCode}">
				<li class="fieldcontain">
					<span id="iupacCode-label" class="property-label"><g:message code="laboratoryReport.iupacCode.label" default="Iupac Code" /></span>
					
						<span class="property-value" aria-labelledby="iupacCode-label">${laboratoryReportInstance?.iupacCode?.encodeAsHTML()}</span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.nationalSampleIdentifier}">
				<li class="fieldcontain">
					<span id="nationalSampleIdentifier-label" class="property-label"><g:message code="laboratoryReport.nationalSampleIdentifier.label" default="National Sample Identifier" /></span>
					
						<span class="property-value" aria-labelledby="nationalSampleIdentifier-label"><g:fieldValue bean="${laboratoryReportInstance}" field="nationalSampleIdentifier"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.producerOfLabResult}">
				<li class="fieldcontain">
					<span id="producerOfLabResult-label" class="property-label"><g:message code="laboratoryReport.producerOfLabResult.label" default="Producer Of Lab Result" /></span>
					
						<span class="property-value" aria-labelledby="producerOfLabResult-label"><g:link controller="labProducer" action="show" id="${laboratoryReportInstance?.producerOfLabResult?.id}">${laboratoryReportInstance?.producerOfLabResult?.identifierCode}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.resultText}">
				<li class="fieldcontain">
					<span id="resultText-label" class="property-label"><g:message code="laboratoryReport.resultText.label" default="Result Text" /></span>
					
						<span class="property-value" aria-labelledby="resultText-label"><g:fieldValue bean="${laboratoryReportInstance}" field="resultText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.resultUnitText}">
				<li class="fieldcontain">
					<span id="resultUnitText-label" class="property-label"><g:message code="laboratoryReport.resultUnitText.label" default="Result Unit Text" /></span>
					
						<span class="property-value" aria-labelledby="resultUnitText-label"><g:fieldValue bean="${laboratoryReportInstance}" field="resultUnitText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${laboratoryReportInstance?.laboratoryReportUUID}">
				<li class="fieldcontain">
					<span id="uuidIdentifier-label" class="property-label"><g:message code="laboratoryReport.uuidIdentifier.label" default="Uuid Identifier" /></span>
					
						<span class="property-value" aria-labelledby="uuidIdentifier-label"><g:link controller="selfMonitoringSample" action="show" id="${laboratoryReportInstance?.sample?.id}"><g:fieldValue bean="${laboratoryReportInstance}" field="laboratoryReportUUID"/></g:link></span>
					
				</li>
				</g:if>
			</ol>
		</div>
	</body>
</html>
