<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="banner.jsp"%>

<div class="content">

<form method="post" action="<%= contextPath %>/login/do">
<table>
	<tr>
		<td>{{::language.email}}</td>
		<td><input type="text" name="user.email"></td>
	</tr>
	<tr>
		<td>{{::language.password}}</td>
		<td><input type="password" name="user.password"></td>
	</tr>
	<tr>
		<td></td>
		<td>Forgot your password ? Click <a href="<%= contextPath %>/forgot/password">here</a></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.login}}</button></td>
	</tr>
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
