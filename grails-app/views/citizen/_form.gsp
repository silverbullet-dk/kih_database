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

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'ssn', 'error')} ">
	<label class="control-label" for="ssn"><g:message code="citizen.ssn.label" default="Ssn" /></label>
	<div class="controls">
		<g:textField name="ssn" value="${citizenInstance?.ssn}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'firstName', 'error')} ">
	<label class="control-label" for="firstName"><g:message code="citizen.firstName.label" default="First Name" /></label>
	<div class="controls">
		<g:textField name="firstName" value="${citizenInstance?.firstName}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'lastName', 'error')} ">
	<label class="control-label" for="lastName"><g:message code="citizen.lastName.label" default="Last Name" /></label>
	<div class="controls">
		<g:textField name="lastName" value="${citizenInstance?.lastName}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'sex', 'error')} ">
	<label class="control-label" for="sex"><g:message code="citizen.sex.label" default="Sex" /></label>
	<div class="controls">
		<g:select name="sex" from="${dk.silverbullet.kihdb.model.types.Sex?.values()}" keys="${dk.silverbullet.kihdb.model.types.Sex.values()*.name()}" value="${citizenInstance?.sex?.name()}" noSelection="['': '']"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'email', 'error')} ">
	<label class="control-label" for="email"><g:message code="citizen.email.label" default="Email" /></label>
	<div class="controls">
		<g:textField name="email" value="${citizenInstance?.email}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'contactTelephone', 'error')} ">
	<label class="control-label" for="contactTelephone"><g:message code="citizen.contactTelephone.label" default="Contact Telephone" /></label>
	<div class="controls">
		<g:textField name="contactTelephone" value="${citizenInstance?.contactTelephone}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'dateOfBirth', 'error')} required">
	<label class="control-label" for="dateOfBirth">
		<g:message code="citizen.dateOfBirth.label" default="Date Of Birth" />
		<span class="required-indicator">*</span>
	</label>
	<div class="controls">
		<g:datePicker name="dateOfBirth" precision="day"  value="${citizenInstance?.dateOfBirth}"  />
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'streetName', 'error')} ">
	<label class="control-label" for="streetName"><g:message code="citizen.streetName.label" default="Street Name" /></label>
	<div class="controls">
		<g:textField name="streetName" value="${citizenInstance?.streetName}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'streetName2', 'error')} ">
	<label class="control-label" for="streetName2"><g:message code="citizen.streetName2.label" default="Street Name2" /></label>
	<div class="controls">
		<g:textField name="streetName2" value="${citizenInstance?.streetName2}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'zipCode', 'error')} ">
	<label class="control-label" for="zipCode"><g:message code="citizen.zipCode.label" default="Zip Code" /></label>
	<div class="controls">
		<g:textField name="zipCode" value="${citizenInstance?.zipCode}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'city', 'error')} ">
	<label class="control-label" for="city"><g:message code="citizen.city.label" default="City" /></label>
	<div class="controls">
		<g:textField name="city" value="${citizenInstance?.city}"/>
	</div>
</div>

<div class="control-group ${hasErrors(bean: citizenInstance, field: 'country', 'error')} ">
	<label class="control-label" for="country"><g:message code="citizen.country.label" default="Country" /></label>
	<div class="controls">
		<g:textField name="country" value="${citizenInstance?.country}"/>
	</div>
</div>

