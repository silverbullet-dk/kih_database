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
<%@ page import="dk.silverbullet.kihdb.model.MeasurementType" %>

<p />
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span2"><g:message code="measurementType.code.label" default="Kode" /></div>
        <div class="span10"><g:textField class="input-medium" name="code" value="${measurementTypeInstance?.code}" /></div>
    </div>

    <div class="row-fluid">
        <div class="span2"><g:message code="measurementType.nkn.label" default="nkn" /></div>
        <div class="span10"><g:textField class="input-xlarge" name="nkn" value="${measurementTypeInstance?.nkn}" /></div>
    </div>

    <div class="row-fluid">
        <div class="span2"><g:message code="measurementType.longName.label" default="Langt Navn"/></div>
        <div class="span10"><g:textField class="input-xxlarge" name="longName" value="${measurementTypeInstance?.longName}" /></div>
    </div>

    <div class="row-fluid">
        <div class="span2"><g:message code="measurementType.unit.label" default="unit" /></div>
        <div class="span10"><g:textField class="input-mini" name="unit" value="${measurementTypeInstance?.unit}" /></div>
    </div>
</div>
