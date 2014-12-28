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
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>OpenTele isAlive</title>
</head>
<body>
    <table>
    <g:each in="${info}" var='row'>
        <tr>
            <td style="width: 160px;">${row.key}</td>
            <td>${row.value}</td>
        </tr>
    </g:each>
    </table>
</body>
</html>