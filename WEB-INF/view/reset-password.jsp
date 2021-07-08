<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>

<%
tangerine.controller.AuthenticationController model = (tangerine.controller.AuthenticationController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
%>

<%@include file="banner.jsp"%>

<div class="content">

<form method="post" action="<%= contextPath %>/reset/password/do">
<input type="hidden" name="user.email" value="<%= user.getEmail() %>">
<table>
	<tr>
		<td>{{::language.email}}</td>
		<td><%= user.getEmail() %></td>
	</tr>
	<tr>
		<td>{{::language.verificationCode}}</td>
		<td><input type="text" name="verificationCode"></td>
	</tr>
	<tr>
		<td>{{::language.newPassword}}</td>
		<td><input type="password" name="user.password"></td>
	</tr>
	<tr>
		<td>{{::language.confirmNewPassword}}</td>
		<td><input type="password" name="confirmPassword"></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.resetPassword}}</button></td>
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
