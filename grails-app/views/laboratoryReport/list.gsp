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
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-laboratoryReport" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table  class="table table-hover">
				<thead>
					<tr>
                        <g:sortableColumn property="uuidIdentifier" title="${message(code: 'laboratoryReport.uuidIdentifier.label', default: 'Uuid Identifier')}" />
                        <g:sortableColumn property="createdDateTime" title="${message(code: 'laboratoryReport.createdDateTime.label', default: 'Oprettet')}" />
                        <g:sortableColumn property="nationalSampleIdentifier" title="${message(code: 'laboratoryReport.nationalSampleIdentifier.label', default: 'National Målings Id')}" />
                        <g:sortableColumn property="deleted" title="${message(code: 'laboratoryReport.deleted.label', default: 'Slette markeret')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${laboratoryReportInstanceList}" status="i" var="laboratoryReportInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${laboratoryReportInstance.id}">${fieldValue(bean: laboratoryReportInstance, field: "laboratoryReportUUID")}</g:link></td>
					
						<td><g:formatDate date="${laboratoryReportInstance.createdDateTime}" /></td>
					
						<td>${fieldValue(bean: laboratoryReportInstance, field: "nationalSampleIdentifier")}</td>

                        <td>${fieldValue(bean: laboratoryReportInstance, field: "deleted")}</td>

					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${laboratoryReportInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
