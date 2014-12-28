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
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-selfMonitoringSample" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table table-hover">
				<thead>
					<tr>
                        <g:sortableColumn property="citizen" title="${message(code: 'selfMonitoringSample.citizen.label', default: 'Borger')}" />
                        <g:sortableColumn property="createdDateTime" title="${message(code: 'selfMonitoringSample.createdDateTime.label', default: 'Oprettet')}" />
                        <th>${message(code: 'selfMonitoringSample.sampleCategoryIdentifier.label', default: 'Målingstype')}</th>
                        <th>${message(code: 'selfMonitoringSample.xdsdocument.label', default: 'Uuid Identifier')}</th>
                        <th>${message(code: 'selfMonitoringSample.deleted.label', default: 'Slettet markeret')}</th>
                    </tr>
				</thead>
				<tbody>
				<g:each in="${selfMonitoringSampleInstanceList}" status="i" var="selfMonitoringSampleInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${selfMonitoringSampleInstance.id}">${fieldValue(bean: selfMonitoringSampleInstance, field: "citizen")}</g:link></td>

						<td><g:formatDate date="${(selfMonitoringSampleInstance.createdDateTime?selfMonitoringSampleInstance.createdDateTime:selfMonitoringSampleInstance.getEarliestDate())}" /></td>

                        <td><ul>
                            <g:each in="${selfMonitoringSampleInstance.getMeasurementTypes()}" var="m">
                                <li>${m}</li>
                            </g:each>

                        </ul>
                        </td>

                        <td>
                            <g:link controller="XDSDocument" action="showUuid" params="[uuid:selfMonitoringSampleInstance?.xdsDocumentUuid]">${selfMonitoringSampleInstance?.xdsDocumentUuid}</g:link>

                        </td>



                        <td>${fieldValue(bean: selfMonitoringSampleInstance, field: "deleted")} </td>

                    </tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${selfMonitoringSampleInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
