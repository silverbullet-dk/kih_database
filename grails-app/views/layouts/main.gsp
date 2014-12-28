<%@ page import="dk.silverbullet.kihdb.model.PermissionName" %>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <g:javascript library='jquery'/>
    <r:require modules="bootstrap-responsive-css"/>

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'grails-bootstrap.css')}" type="text/css">

    <style type="text/css">
    body {
        padding-top: 60px;
        padding-bottom: 40px;
    }
    </style>

    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="${createLink(uri: '/')}">
                <img width="50px" src="${resource(dir: 'images', file: 'logo.png')}" alt="<g:message code='default.application.name'/>"/>
            </a>
            <ul class="nav">
                <sec:ifAnyGranted roles="${PermissionName.CITIZEN_READ_SEARCH}">
                    <type:navigationItem path="citizen" href="${createLink(controller: "citizen", action: "search")}"><g:message code="default.search.list.label"/></type:navigationItem>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.SELF_MONITORING_SAMPLE_READ_ALL}">
                    <type:navigationItem path="selfMonitoringSample" href="${createLink(controller: "selfMonitoringSample", action: "list")}"><g:message code="measurement.list.label"/></type:navigationItem>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.SELF_MONITORING_SAMPLE_READ_ALL}">
                    <type:navigationItem path="iUPACCode" href="${createLink(controller: "measurementType", action: "list")}"><g:message code="measurementType.label"/></type:navigationItem>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.AUDITLOG_READ}">
                    <type:navigationItem path="auditLogEntry" href="${createLink(controller: "auditLogEntry", action: "list")}"><g:message code="auditLogEntry.label"/></type:navigationItem>
                </sec:ifAnyGranted>
                <sec:ifLoggedIn>
                    <type:navigationItem href="${createLink(controller: "logout", action: "index")}"><g:message code="default.logout.label" default="Log ud"/></type:navigationItem>
                </sec:ifLoggedIn>
            </ul>
            <sec:ifLoggedIn>
                <p class="navbar-text pull-right"><i><g:message code="logged.in.as"/> <sec:username/></i></p>
            </sec:ifLoggedIn>
        </div>
    </div>
</div>

<div class="container" id="contents">
    <g:if test="${flash.message}">
        <div class="alert" role="status">${flash.message}</div>
    </g:if>

    <g:layoutBody/>

    <div class="footer pull-right" role="contentinfo">
        <div class="version">Version ${grailsApplication.metadata['app.version']}</div>

        <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
        <g:javascript library="application"/>
        <r:layoutResources/>
    </div>
</div>
</body>
</html>