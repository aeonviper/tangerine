<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>

<%@include file="banner.jsp"%>

<div class="content">

<form method="post" action="<%= contextPath %>/reset/password">
<table>
	<tr>
		<td>{{::language.email}}</td>
		<td><input type="text" name="user.email"></td>
	</tr>
	<tr>
		<td></td>
		<td><button>{{::language.next}}</button></td>
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
