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
<%@ page import="dk.silverbullet.kihdb.model.IUPACCode" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'measurementType.label')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-measurementType" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table table-hover">
				<thead>
					<tr>
                        <g:sortableColumn property="code" title="${message(code: 'measurementType.code.label', default: 'Kode')}" />
						<g:sortableColumn property="nkn" title="${message(code: 'selfMonitoringSample.nkn.label', default: 'nkn')}" />
						<g:sortableColumn property="longName" title="${message(code: 'selfMonitoringSample.longName.label', default: 'Langt navn')}" />
						<g:sortableColumn property="unit" title="${message(code: 'selfMonitoringSample.unit.label', default: 'Enhed')}" />
                    </tr>
				</thead>
				<tbody>
				<g:each in="${measurementTypeList}" status="i" var="measurementTypeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td><g:link id="${measurementTypeInstance.id}" action="edit">${measurementTypeInstance.code}</g:link></td>
                        <td>${measurementTypeInstance.nkn} </td>
                        <td>${measurementTypeInstance.longName} </td>
                        <td>${measurementTypeInstance.unit} </td>
                    </tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${measurementTypeTotal}" />
			</div>
		</div>
	</body>
</html>
