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
<%@ page import="dk.silverbullet.kihdb.model.Citizen"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"	value="${message(code: 'citizen.label', default: 'Citizen')}" />
<title><g:message code="default.show.label" args="[entityName]" />
	${citizenInstance?.firstName} ${citizenInstance?.lastName}</title>
</head>
<body>
	<div id="show-citizen" class="content scaffold-show" role="main">
		<type:pageHeading>
			${citizenInstance?.firstName} ${citizenInstance?.lastName}
		</type:pageHeading>

		<dl class="dl-horizontal">
			<g:if test="${citizenInstance?.ssn}">
				<dt><g:message code="citizen.ssn.label"/></dt>
				<dd><otformat:formatCPR message="${citizenInstance.ssn}" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.firstName}">
				<dt><g:message code="citizen.firstName.label" default="First Name" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="firstName" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.lastName}">
				<dt><g:message code="citizen.lastName.label" default="Last Name" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="lastName" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.sex}">
				<dt><g:message code="citizen.sex.label" default="Sex" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="sex" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.email}">
				<dt><g:message code="citizen.email.label" default="Email" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="email" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.contactTelephone}">
				<dt><g:message code="citizen.contactTelephone.label" default="Contact Telephone" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="contactTelephone" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.dateOfBirth}">
				<dt><g:message code="citizen.dateOfBirth.label" default="Date Of Birth" /></dt>
				<dd><g:formatDate date="${citizenInstance?.dateOfBirth}" style="MEDIUM" type="date" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.streetName}">
				<dt><g:message code="citizen.streetName.label" default="Street Name" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="streetName" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.streetName2}">
				<dt><g:message code="citizen.streetName2.label" default="Street Name2" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="streetName2" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.zipCode}">
				<dt><g:message code="citizen.zipCode.label" default="Zip Code" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="zipCode" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.city}">
				<dt><g:message code="citizen.city.label" default="City" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="city" /></dd>
			</g:if>

			<g:if test="${citizenInstance?.country}">
				<dt><g:message code="citizen.country.label" default="Country" /></dt>
				<dd><g:fieldValue bean="${citizenInstance}" field="country" /></dd>
			</g:if>
		</dl>
		<g:form class="form-inline">
			<g:hiddenField name="id" value="${citizenInstance?.id}" />
			<g:link class="btn" action="edit" id="${citizenInstance?.id}">
				<g:message code="default.button.edit.label" default="Edit" />
			</g:link>
		</g:form>
		
		<legend>Målinger</legend>
		<g:if test="${selfMonitoringSampleList.size() > 0}">
			<table class="table table-hover">
				<thead>
					<tr>
                        <th><g:message code="measurement.createdByText.label" default="Oprettet af" /></th>
                        <th><g:message code="measurement.id.label" default="ID label" /></th>
                        <th><g:message code="measurement.xdsdocument.uuid.label" default="PHMR Dokument" /></th>
						<th><g:message code="measurement.type.label" default="Measurement Type" /></th>
						<th><g:message code="measurement.time.label" default="Date" /></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${selfMonitoringSampleList}" var="s">
                        <tr>
                            <td>
                                <g:link action="show" controller="selfMonitoringSample" id="${s.id}">${s.createdByText}</g:link>
                            </td>

                            <td>
                                <g:link action="show" controller="selfMonitoringSample" id="${s.id}">${s.id}</g:link>
                            </td>

                            <td>
                                <g:link controller="XDSDocument" action="showUuid" params="[uuid:s?.xdsDocumentUuid]">${s?.xdsDocumentUuid}</g:link>

                            </td>

                            <td><ul>
                                <g:each in="${s.getMeasurementTypes()}" var="m">
                                    <li>${m}</li>
                                </g:each>

                            </ul>
                              </td>

                            <td><g:formatDate date="${(s.createdDateTime?s.createdDateTime:s.getEarliestDate())}"/></td>
                        </tr>
					</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${selfMonitoringSampleListTotal}" params="${[id: citizenInstance.id]}"/>
			</div>
		</g:if>
		<g:if test="${selfMonitoringSampleList.size() == 0}">
			<i>Ingen målinger for denne patient</i>
		</g:if>
	</div>
</body>
</html>
