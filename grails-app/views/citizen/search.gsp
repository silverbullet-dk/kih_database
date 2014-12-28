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
	<g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
	<title><g:message code="default.patient.search.label" default="Søg patient" /></title>
</head>
<body>
	<type:pageHeading>
		<g:message code="default.patient.search.label"/>
	</type:pageHeading>

	<g:form class="form-inline" method="post" action="search">
		<div class="input-append">
			<g:textField name="ssn" value="${ssn}" placeholder="${message(code:'patient.overview.form.SSN') }"/>
			<g:actionSubmit class="btn btn-primary" action="search" value="${message(code:'patient.search.form.submit') }"/>
		</div>
		
		<g:link class="btn" action="create"><g:message code="citizen.search.form.create"/></g:link>
	</g:form>
	
	<g:if test="${citizens?.size() > 0}">
		<g:render template="patientlist"/>
	</g:if>
	<g:if test="${citizens?.size() <= 0}">
	   	<i>Ingen resultater</i>
   </g:if>
</body>
</html>
