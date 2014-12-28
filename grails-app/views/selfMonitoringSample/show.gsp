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
<%@ page import="dk.silverbullet.kihdb.model.SelfMonitoringSample" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'selfMonitoringSample.label', default: 'SelfMonitoringSample')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-selfMonitoringSample" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list selfMonitoringSample">

                <g:if test="${selfMonitoringSampleInstance?.deleted}">
                    <li class="fieldcontain">
                        <span id="deleted-label" class="property-label"><g:message code="selfMonitoringSample.deleted.label" default="Slette markeret" /></span>

                        <span class="property-value" aria-labelledby="deleted-label"><g:fieldValue bean="${selfMonitoringSampleInstance}" field="deleted"/></span>

                    </li>
                </g:if>


				<g:if test="${selfMonitoringSampleInstance?.selfMonitoringSampleUUID}">
				<li class="fieldcontain">
					<span id="uuidIdentifier-label" class="property-label"><g:message code="selfMonitoringSample.uuidIdentifier.label" default="Uuid Identifier" /></span>
					
						<span class="property-value" aria-labelledby="uuidIdentifier-label"><g:fieldValue bean="${selfMonitoringSampleInstance}" field="selfMonitoringSampleUUID"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${selfMonitoringSampleInstance?.createdDateTime}">
				<li class="fieldcontain">
					<span id="createdDateTime-label" class="property-label"><g:message code="selfMonitoringSample.createdDateTime.label" default="Created Date Time" /></span>
					
						<span class="property-value" aria-labelledby="createdDateTime-label"><g:formatDate date="${selfMonitoringSampleInstance?.createdDateTime}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${selfMonitoringSampleInstance?.sampleCategoryIdentifier}">
				<li class="fieldcontain">
					<span id="sampleCategoryIdentifier-label" class="property-label"><g:message code="selfMonitoringSample.sampleCategoryIdentifier.label" default="Sample Category Identifier" /></span>
					
						<span class="property-value" aria-labelledby="sampleCategoryIdentifier-label"><g:fieldValue bean="${selfMonitoringSampleInstance}" field="sampleCategoryIdentifier"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${selfMonitoringSampleInstance?.createdByText}">
				<li class="fieldcontain">
					<span id="createdByText-label" class="property-label"><g:message code="selfMonitoringSample.createdByText.label" default="Created By Text" /></span>
					
						<span class="property-value" aria-labelledby="createdByText-label"><g:fieldValue bean="${selfMonitoringSampleInstance}" field="createdByText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${selfMonitoringSampleInstance?.citizen}">
				<li class="fieldcontain">
					<span id="citizen-label" class="property-label"><g:message code="selfMonitoringSample.citizen.label" default="Citizen" /></span>
					
						<span class="property-value" aria-labelledby="citizen-label"><g:link controller="citizen" action="show" id="${selfMonitoringSampleInstance?.citizen?.id}">${selfMonitoringSampleInstance?.citizen?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>

                <g:if test="${selfMonitoringSampleInstance?.xdsDocumentUuid}">
                    <li class="fieldcontain">
                        <span id="xdsdocument-label" class="property-label"><g:message code="selfMonitoringSample.xdsdocument.label" default="PHMR dokument" /></span>

                        <span class="property-value" aria-labelledby="citizen-label">
                            <g:link controller="XDSDocument" action="showUuid" params="[uuid:selfMonitoringSampleInstance?.xdsDocumentUuid]">${selfMonitoringSampleInstance?.xdsDocumentUuid}</g:link></span>

                    </li>
                </g:if>

			
				<g:if test="${selfMonitoringSampleInstance?.reports}">
				<li class="fieldcontain">
					<span id="reports-label" class="property-label"><g:message code="selfMonitoringSample.reports.label" default="Reports" /></span>
					
						<g:each in="${selfMonitoringSampleInstance.reports}" var="r">
						<span class="property-value" aria-labelledby="reports-label"><g:link controller="laboratoryReport" action="show" id="${r.id}">
                            ${r?.analysisText}: ${r?.resultText} ${r?.resultUnitText}</g:link></span>
						</g:each>
				</li>
				</g:if>
			</ol>
		</div>
	</body>
</html>
