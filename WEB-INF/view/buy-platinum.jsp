<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.SubscriptionController model = (tangerine.controller.SubscriptionController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
SubscriptionPackage subscriptionPackage = SubscriptionPackage.Platinum;
%>

<div class="content">

<div>
<form method="post" action="<%= contextPath%>/subscription/buy/platinum/do">
<% if (language == Language.English) { %>
	You are buying the Platinum Class Subscription Package<br/>
	Your credit balance after this transaction will be Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>Up to 900 questions per month
	<li>Rp 1.000.000 per month
<% } else { %>
	Anda akan membeli Paket Platinum<br/>
	Saldo kredit setelah transaksi ini akan menjadi Rp <%= Utility.numberFormatCurrency.format(user.getSubscriptionCredit() - subscriptionPackage.getPrice()) %>
	<li>900 pertanyaan per bulan
	<li>Rp 1.000.000 per bulan
<% } %>

<br/>
<% if (user.getSubscriptionCredit() >= subscriptionPackage.getPrice()) { %>
<button>{{::language.buy}} {{::language.platinumSubscription}}</button>
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
