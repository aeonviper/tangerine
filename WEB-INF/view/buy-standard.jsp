<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.SubscriptionController model = (tangerine.controller.SubscriptionController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
SubscriptionPackage subscriptionPackage = SubscriptionPackage.Standard;
%>

<div class="content">

<div>
<form method="post" action="<%= contextPath%>/subscription/buy/standard/do">
<% if (language == Language.English) { %>
	You are buying the Standard Class Subscription Package<br/>
	Your credit balance after this transaction will be Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>Up to 30 questions per month
	<li>Rp 200.000 per month
<% } else { %>
	Anda akan membeli Paket Standard<br/>
	Saldo kredit setelah transaksi ini akan menjadi Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>30 pertanyaan per bulan
	<li>Rp 200.000 per bulan
<% } %>
<br/>
<% if (user.getSubscriptionCredit() >= subscriptionPackage.getPrice()) { %>
<button>{{::language.buy}} {{::language.standardSubscription}}</button>
<% } else { %>
{{::language.notEnoughCreditToBuyPackage}}
<% } %>
</form>
</div>
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
