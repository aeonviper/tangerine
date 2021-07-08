<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>

<%
tangerine.controller.VerificationController model = (tangerine.controller.VerificationController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
%>

<%@include file="banner.jsp"%>

<div class="content">

<form method="post" action="<%= contextPath %>/verify/code">
<input type="hidden" name="userTextId" value="<%= user.getTextId() %>">
<table>
	<tr>
		<td colspan="2">
			A verification code has been sent to your email <%= user.getEmail() %>.<br/>
			Please enter it here.<br/>
			<br/>
		</td>
	</tr>
	<tr>
		<td>{{::language.email}}</td>
		<td><%= user.getEmail() %></td>
	</tr>
	<tr>
		<td>{{::language.verificationCode}}</td>
		<td><input type="text" name="code"></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.verify}}</button></td>
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
