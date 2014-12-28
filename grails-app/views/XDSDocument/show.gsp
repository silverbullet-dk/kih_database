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
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-XDSDocument" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list XDSDocument">
			
				<g:if test="${XDSDocumentInstance?.createdDate}">
				<li class="fieldcontain">
					<span id="createdDate-label" class="property-label"><g:message code="XDSDocument.createdDate.label" default="Created Date" /></span>
					
						<span class="property-value" aria-labelledby="createdDate-label"><g:formatDate date="${XDSDocumentInstance?.createdDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${XDSDocumentInstance?.createdBy}">
				<li class="fieldcontain">
					<span id="createdBy-label" class="property-label"><g:message code="XDSDocument.createdBy.label" default="Created By" /></span>
					
						<span class="property-value" aria-labelledby="createdBy-label"><g:fieldValue bean="${XDSDocumentInstance}" field="createdBy"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${XDSDocumentInstance?.modifiedBy}">
				<li class="fieldcontain">
					<span id="modifiedBy-label" class="property-label"><g:message code="XDSDocument.modifiedBy.label" default="Modified By" /></span>
					
						<span class="property-value" aria-labelledby="modifiedBy-label"><g:fieldValue bean="${XDSDocumentInstance}" field="modifiedBy"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${XDSDocumentInstance?.modifiedDate}">
				<li class="fieldcontain">
					<span id="modifiedDate-label" class="property-label"><g:message code="XDSDocument.modifiedDate.label" default="Modified Date" /></span>
					
						<span class="property-value" aria-labelledby="modifiedDate-label"><g:formatDate date="${XDSDocumentInstance?.modifiedDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${XDSDocumentInstance?.uuid}">
				<li class="fieldcontain">
					<span id="uuid-label" class="property-label"><g:message code="XDSDocument.uuid.label" default="Uuid" /></span>
					
						<span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${XDSDocumentInstance}" field="uuid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${XDSDocumentInstance?.mimeType}">
				<li class="fieldcontain">
					<span id="mimeType-label" class="property-label"><g:message code="XDSDocument.mimeType.label" default="Mime Type" /></span>
					
						<span class="property-value" aria-labelledby="mimeType-label"><g:fieldValue bean="${XDSDocumentInstance}" field="mimeType"/></span>
					
				</li>
				</g:if>
                <li class="fieldcontain">
                    <span id="mimeType-label" class="property-label"><g:message code="XDSDocument.mimeType.label" default="Hent dokument" /></span>

                    <span class="property-value" aria-labelledby="mimeType-label">
                        <g:link controller="XDSDocument" action="download" params="[uuid:XDSDocumentInstance.uuid]"><g:message code="XDSDocument.document.download" default="Hent dokument"/></g:link>
                    </span>

                </li>

            </ol>
		</div>
	</body>
</html>
