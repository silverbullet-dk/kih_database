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



<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'createdDate', 'error')} ">
	<label for="createdDate">
		<g:message code="labProducer.createdDate.label" default="Created Date" />
		
	</label>
	<g:datePicker name="createdDate" precision="day"  value="${labProducerInstance?.createdDate}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'createdBy', 'error')} ">
	<label for="createdBy">
		<g:message code="labProducer.createdBy.label" default="Created By" />
		
	</label>
	<g:textField name="createdBy" value="${labProducerInstance?.createdBy}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'modifiedBy', 'error')} ">
	<label for="modifiedBy">
		<g:message code="labProducer.modifiedBy.label" default="Modified By" />
		
	</label>
	<g:textField name="modifiedBy" value="${labProducerInstance?.modifiedBy}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'modifiedDate', 'error')} ">
	<label for="modifiedDate">
		<g:message code="labProducer.modifiedDate.label" default="Modified Date" />
		
	</label>
	<g:datePicker name="modifiedDate" precision="day"  value="${labProducerInstance?.modifiedDate}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'identifier', 'error')} ">
	<label for="identifier">
		<g:message code="labProducer.identifier.label" default="Identifier" />
		
	</label>
	<g:textField name="identifier" value="${labProducerInstance?.identifier}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: labProducerInstance, field: 'identifierCode', 'error')} ">
	<label for="identifierCode">
		<g:message code="labProducer.identifierCode.label" default="Identifier Code" />
		
	</label>
	<g:textField name="identifierCode" value="${labProducerInstance?.identifierCode}"/>
</div>

