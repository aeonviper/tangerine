<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.AdministrationController model = (tangerine.controller.AdministrationController) request.getAttribute(orion.core.Constant.model);
%>

<div class="content">

<form method="post" action="<%= contextPath %>/administration/logout/do">
<button>Logout</button>
</form>

<form method="post" action="<%= contextPath %>/administration/impersonate/do">
Email <input type="text" name="email"><button>Impersonate</button>
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
