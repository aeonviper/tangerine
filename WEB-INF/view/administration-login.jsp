<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.AdministrationController model = (tangerine.controller.AdministrationController) request.getAttribute(orion.core.Constant.model);
%>

<div class="content">

<form method="post" action="<%= contextPath %>/administration/login/do">
<table>
	<tr><td>Name:</td><td><input type="text" name="name"></td></tr>
	<tr><td>Password:</td><td><input type="password" name="password"></td></tr>
	<tr><td></td><td><button>Login</button></td></tr>
</table>
</form>

</div>

<script>
var tangerineApplication = angular.module('tangerineApplication', []);
tangerineApplication.controller('controller', ['$scope', function($scope) {
	$scope.language = language;
}]);
</script>

<%@include file="language.jsp"%>
</body>
<%@include file="footer.jsp"%>
