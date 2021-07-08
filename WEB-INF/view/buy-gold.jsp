<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.SubscriptionController model = (tangerine.controller.SubscriptionController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
SubscriptionPackage subscriptionPackage = SubscriptionPackage.Gold;
%>

<div class="content">

<div>
<form method="post" action="<%= contextPath%>/subscription/buy/gold/do">
<% if (language == Language.English) { %>
	You are buying the Gold Class Subscription Package<br/>
	Your credit balance after this transaction will be Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>Up to 60 questions per month
	<li>Rp 500.000 per month
<% } else { %>
	Anda akan membeli Paket Gold<br/>
	Saldo kredit setelah transaksi ini akan menjadi Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>60 pertanyaan per bulan
	<li>Rp 500.000 per bulan
<% } %>

<br/>
<% if (user.getSubscriptionCredit() >= subscriptionPackage.getPrice()) { %>
<button>{{::language.buy}} {{::language.goldSubscription}}</button>
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
