<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>

<%@include file="banner.jsp"%>

<div class="content">

<form method="post" action="<%= contextPath %>/register/do">
<table>
	<tr>
		<td>{{::language.name}}</td>
		<td><input type="text" name="user.name"></td>
	</tr>
	<tr>
		<td>{{::language.email}}</td>
		<td><input type="text" name="user.email"></td>
	</tr>
	<tr>
		<td>{{::language.phone}}</td>
		<td><input type="text" name="user.phone"></td>
	</tr>
	<tr>
		<td>{{::language.password}}</td>
		<td><input type="password" name="user.password"></td>
	</tr>
	<tr>
		<td>{{::language.confirmPassword}}</td>
		<td><input type="password" name="confirmPassword"></td>
	</tr>
	<tr>
		<td>{{::language.registrationCode}}</td>
		<td><input type="text" name="registrationCode" style="text-transform:uppercase;" value="FREE1MONTH"></td>
	</tr>
	<tr>
		<td colspan="2">
			{{::language.areYouTutor}}
			<input type="radio" name="isEducator" value="yes"> {{::language.yes}}
			<input type="radio" name="isEducator" value="no" checked> {{::language.no}}
		</td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.register}}</button></td>
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
