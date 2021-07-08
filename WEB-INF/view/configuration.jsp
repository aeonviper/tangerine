<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.ConfigurationController model = (tangerine.controller.ConfigurationController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
Utility.strip(user, "name", "email", "school");
%>

<div class="content">

<form method="post" action="<%= contextPath %>/profile/edit/do">
<table>
	<tr>
		<td>{{::language.name}}</td>
		<td><input type="text" name="user.name" value="<%= user.getName() %>"></td>
	</tr>
	<tr>
		<td>{{::language.phone}}</td>
		<td><input type="text" name="user.phone" value="<%= user.getPhone() %>"></td>
	</tr>
	<tr>
		<td>{{::language.school}}</td>
		<td>
			<textarea name="user.school" rows="5" cols="30"><%= user.getSchool() %></textarea>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.edit}} {{::language.profile}}</button></td>
	</tr>
</table>
</form>

<br/>
<br/>

<%
if (user.getEmailNew() == null) {
%>
<form method="post" action="<%= contextPath %>/email/edit/do">
<table>
	<tr>
		<td>{{::language.email}}</td>
		<td><input type="text" name="user.email" value="<%= user.getEmail() %>"></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.change}} {{::language.email}}</button></td>
	</tr>
</table>
</form>
<%
} else {
%>
<form method="post" action="<%= contextPath %>/email/new/verify/do">
<table>
	<tr>
		<td>{{::language.email}}</td>
		<td>
			<%= user.getEmail() %>
			&rarr;
			<%= user.getEmailNew() %>
		</td>
	</tr>
	<tr>
		<td>
			{{::language.verificationCode}}
		</td>
		<td>
			<input type="text" name="verificationCode" value="">
		</td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.verifyNewEmail}}</button> <a href="<%= contextPath %>/email/new/cancel/do">{{::language.cancel}}</a></td>
	</tr>
</table>
</form>
<%
}
%>


<br/>
<br/>

<form method="post" action="<%= contextPath %>/password/edit/do">
<table>
	<tr>
		<td>{{::language.currentPassword}}</td>
		<td><input type="password" name="user.password"></td>
	</tr>
	<tr>
		<td>{{::language.newPassword}}</td>
		<td><input type="password" name="newPassword"></td>
	</tr>
	<tr>
		<td>{{::language.confirmNewPassword}}</td>
		<td><input type="password" name="confirmPassword"></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.change}} {{::language.password}}</button></td>
	</tr>
</table>
</form>

<br/>
<br/>

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
