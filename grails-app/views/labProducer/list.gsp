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
<%@ page import="dk.silverbullet.kihdb.model.LabProducer" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'labProducer.label', default: 'LabProducer')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-labProducer" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table table-hover">
				<thead>
					<tr>

						<g:sortableColumn property="identifierCode" title="${message(code: 'labProducer.identifierCode.label', default: 'Identifier Code')}" />
						<g:sortableColumn property="identifier" title="${message(code: 'labProducer.identifier.label', default: 'Identifier')}" />


					</tr>
				</thead>
				<tbody>
				<g:each in="${labProducerInstanceList}" status="i" var="labProducerInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${labProducerInstance.id}">${fieldValue(bean: labProducerInstance, field: "identifierCode")}</g:link></td>
						<td>${fieldValue(bean: labProducerInstance, field: "identifier")}</td>


					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${labProducerInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
