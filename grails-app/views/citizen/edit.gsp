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
<%@ page import="dk.silverbullet.kihdb.model.Citizen" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'citizen.label', default: 'Citizen')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-citizen" role="main">
			<type:pageHeading>
				<g:message code="default.edit.label" args="[entityName]" />
			</type:pageHeading>

			<g:hasErrors bean="${citizenInstance}">
				<div class="alert" role="alert">
					<g:eachError bean="${citizenInstance}" var="error">
						<p <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></p>
					</g:eachError>
				</div>
			</g:hasErrors>
			
			<g:form method="post" class="form-horizontal">
				<g:hiddenField name="id" value="${citizenInstance?.id}" />
				<g:hiddenField name="version" value="${citizenInstance?.version}" />
				<g:render template="form"/>
				<div class="control-group">
				    <div class="controls">
						<g:actionSubmit class="btn btn-primary" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					</div>
				</div>
			</g:form>
		</div>
	</body>
</html>
