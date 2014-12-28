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
<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
	<style type="text/css">
      .form-signin {
        max-width: 300px;
        padding: 29px 29px 29px;
        margin: 29px auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
</head>

<body>
	
	<form action='${postUrl}' method='POST' id='loginForm' class='form-signin' autocomplete='off'>
		<h2><g:message code="springSecurity.login.header"/></h2>
		
		<input type='text' class='input-block-level' name='j_username' id='username' placeholder="${message(code:'springSecurity.login.username.label')}"/>
		<input type='password' class='input-block-level' name='j_password' id='password' placeholder="${message(code:'springSecurity.login.password.label')}"/>
		
		<label class="checkbox">
			<input type='checkbox' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
			<g:message code="springSecurity.login.remember.me.label"/>
		</label>
	
		<button type='submit' id="submit" class="btn btn-large btn-primary"><g:message code="springSecurity.login.button"/></button>
	</form>

<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>
