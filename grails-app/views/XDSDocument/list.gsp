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

<%@ page import="dk.silverbullet.kihdb.model.xds.XDSDocument" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'XDSDocument.label', default: 'XDSDocument')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-XDSDocument" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table table-hover">
				<thead>
					<tr>
					
						<g:sortableColumn property="createdDate" title="${message(code: 'XDSDocument.createdDate.label', default: 'Created Date')}" />

						<g:sortableColumn property="uuid" title="${message(code: 'XDSDocument.uuid.label', default: 'Uuid')}" />
					
						<g:sortableColumn property="mimeType" title="${message(code: 'XDSDocument.mimeType.label', default: 'Mime Type')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${XDSDocumentInstanceList}" status="i" var="XDSDocumentInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>${fieldValue(bean: XDSDocumentInstance, field: "createdDate")}</td>

						<td><g:link action="show" id="${XDSDocumentInstance.id}">${fieldValue(bean: XDSDocumentInstance, field: "uuid")}</g:link></td>

						<td>${fieldValue(bean: XDSDocumentInstance, field: "mimeType")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${XDSDocumentInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
