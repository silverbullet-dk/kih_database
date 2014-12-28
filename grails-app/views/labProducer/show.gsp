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
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-labProducer" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list labProducer">
			
				<g:if test="${labProducerInstance?.identifier}">
				<li class="fieldcontain">
					<span id="identifier-label" class="property-label"><g:message code="labProducer.identifier.label" default="Identifier" /></span>
					
						<span class="property-value" aria-labelledby="identifier-label"><g:fieldValue bean="${labProducerInstance}" field="identifier"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${labProducerInstance?.identifierCode}">
				<li class="fieldcontain">
					<span id="identifierCode-label" class="property-label"><g:message code="labProducer.identifierCode.label" default="Identifier Code" /></span>
					
						<span class="property-value" aria-labelledby="identifierCode-label"><g:fieldValue bean="${labProducerInstance}" field="identifierCode"/></span>
					
				</li>
				</g:if>

			</ol>
		</div>
	</body>
</html>
