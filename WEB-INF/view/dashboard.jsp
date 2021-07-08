<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.DashboardController model = (tangerine.controller.DashboardController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
List<Question> questionList = model.getQuestionList();
String channelId = Channel.generateChannelId();
Integer pageNumber = model.getPage();
%>

<div class="content">

<% if (isLearner) { %>

<% if (language == Language.Indonesian) { %>

<br/>
Saldo <strong>Rp <%= user.getSubscriptionCreditCurrency() %></strong>
<br/>
<br/>

<form method="post" action="<%= contextPath %>/voucher/apply">
<div>
Ada kode voucher atau kode promosi ? Tulis di sini<br/>
<input type="text" name="voucherName" style="width:150px;text-transform:uppercase;">
<button>Simpan</button>
</div>
</form>

Daftar Paket<br/><br/>

<table>
<%
for (UserSubscription userSubscription : user.getSubscriptionList()) {
%>
	<form id="form<%= userSubscription.getId() %>" action="<%= contextPath %>/subscription/<%= userSubscription.getId() %>/renewal/toggle/do" method="post">
	<tr><td>Paket</td><td><%= userSubscription.getSubscriptionPackage() %></td></tr>
	<tr><td>Tanggal Dari</td><td><%= DateTimeUtility.formatBusinessDate(userSubscription.getFromDate()) %></td></tr>
	<tr><td>Tanggal Ke</td><td><%= DateTimeUtility.formatBusinessDate(userSubscription.getToDate()) %></td></tr>
	<tr><td>Pertanyaan Terpakai</td><td><%= userSubscription.getUsedQuestion() + " dari " + userSubscription.getSubscriptionQuestion() %></td></tr>
	<tr><td>Langganan</td><td><input type="checkbox"<%= (userSubscription.getRenewal() != null ? " checked" : "") %> onchange="jQuery('#form<%= userSubscription.getId() %>').submit();"></td></tr>
	<tr><td><br/></td></tr>
	</form>
<%
}
%>
</table>
<div>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Standard.getPrice()) { %>
Untuk top up saldo anda, harap melakukan bank transfer ke<br/>
<% } else { %>
Anda tidak mempunyai saldo cukup<br/>
Segera melakukan top up dengan melakukan bank transfer ke<br/>
<% } %>
<table>
<tr><td>Bank</td><td>Bank Central Asia (BCA)</td></tr>
<tr><td>Nomer Rekening</td><td></td></tr>
<tr><td>Nama Rekening</td><td>PT. Tanya Tutor</td></tr>
<tr><td>Berita</td><td>TT <%= user.getTextId() %></td></tr>
</table>
</div>
<br/>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/standard">
Paket Standard<br/>
<ul>
<li>30 pertanyaan per bulan</li>
<li>Rp 200.000 per bulan</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Standard.getPrice()) { %>
<button>Beli Paket Standard</button>
<% } else { %>
Saldo anda tidak cukup untuk membeli paket ini
<% } %>
</form>
</div>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/gold">
Paket Gold<br/>
<ul>
<li>60 pertanyaan per bulan</li>
<li>Rp 500.000 per bulan</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Gold.getPrice()) { %>
<button>Beli Paket Gold</button>
<% } else { %>
Saldo anda tidak cukup untuk membeli paket ini
<% } %>
</form>
</div>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/platinum">
Paket Platinum<br/>
<ul>
<li>900 pertanyaan per bulan</li>
<li>Rp 1.000.000 per bulan</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Platinum.getPrice()) { %>
<button>Beli Paket Platinum</button>
<% } else { %>
Saldo anda tidak cukup untuk membeli paket ini
<% } %>
</form>
</div>
<br/>

<% } else { %>

<br/>
Credit Balance <strong>Rp <%= user.getSubscriptionCreditCurrency() %></strong>
<br/>
<br/>

<form method="post" action="<%= contextPath %>/voucher/apply">
<div>
Have a promotion code ? Enter it here<br/>
<input type="text" name="voucherName" style="width:150px;text-transform:uppercase;">
<button>Apply</button>
</div>
</form>

Subscription List<br/><br/>

<table>
<%
for (UserSubscription userSubscription : user.getSubscriptionList()) {
%>
	<form id="form<%= userSubscription.getId() %>" action="<%= contextPath %>/subscription/<%= userSubscription.getId() %>/renewal/toggle/do" method="post">
	<tr><td>Package</td><td><%= userSubscription.getSubscriptionPackage() %></td></tr>
	<tr><td>From Date</td><td><%= DateTimeUtility.formatBusinessDate(userSubscription.getFromDate()) %></td></tr>
	<tr><td>To Date</td><td><%= DateTimeUtility.formatBusinessDate(userSubscription.getToDate()) %></td></tr>
	<tr><td>Used Question</td><td><%= userSubscription.getUsedQuestion() + " out of " + userSubscription.getSubscriptionQuestion() %></td></tr>
	<tr><td>Auto Renew</td><td><input type="checkbox"<%= (userSubscription.getRenewal() != null ? " checked" : "") %> onchange="jQuery('#form<%= userSubscription.getId() %>').submit();"></td></tr>
	<tr><td><br/></td></tr>
	</form>
<%
}
%>
</table>
<div>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Standard.getPrice()) { %>
To top up your credit balance, please transfer funds to<br/>
<% } else { %>
You do not have enough credit balance on your account<br/>
Please top up by transfering funds to<br/>
<% } %>
<table>
<tr><td>Bank</td><td>Bank Central Asia (BCA)</td></tr>
<tr><td>Account Number</td><td></td></tr>
<tr><td>Account Name</td><td>PT. Tanya Tutor</td></tr>
<tr><td>Berita</td><td>TT <%= user.getTextId() %></td></tr>
</table>
</div>
<br/>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/standard">
Standard Class Subscription<br/>
<ul>
<li>Up to 30 questions per month</li>
<li>Rp 200.000 per month</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Standard.getPrice()) { %>
<button>Buy Standard Subscription</button>
<% } else { %>
Not enough credit balance to buy this package
<% } %>
</form>
</div>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/gold">
Gold Class Subscription<br/>
<ul>
<li>Up to 60 questions per month</li>
<li>Rp 500.000 per month</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Gold.getPrice()) { %>
<button>Buy Gold Subscription</button>
<% } else { %>
Not enough credit balance to buy this package
<% } %>
</form>
</div>
<br/>

<div>
<form method="get" action="<%= contextPath%>/subscription/buy/platinum">
Platinum Class Subscription<br/>
<ul>
<li>Up to 900 questions per month</li>
<li>Rp 1.000.000 per month</li>
</ul>
<% if (user.getSubscriptionCredit() >= SubscriptionPackage.Platinum.getPrice()) { %>
<button>Buy Platinum Subscription</button>
<% } else { %>
Not enough credit balance to buy this package
<% } %>
</form>
</div>
<br/>

<% } %>

<% } %>

<% if (isEducator) { %>

<div id="filterBox" class="hidden">
<form action="<%= contextPath %>/dashboard/filter/do" method="post">
<table class="layout">
<tr>
	<td>
		Status:<br/>
<%
	for (QuestionStatus questionStatus : QuestionStatus.enumerationList) {
		out.println("<input type='checkbox' name='questionStatus' value='" + questionStatus.name() + "'" + (principal.getQuestionStatusSet().contains(questionStatus) ? " checked" : "") + ">" + questionStatus.getDisplay() + "</br>");
	}
%>
	</td>
	<td>
		Subject:<br/>
<%
	for (Category category : Category.enumerationList) {
		out.println("<input type='checkbox' name='category' value='" + category.name() + "'" + (principal.getCategorySet().contains(category) ? " checked" : "") + ">" + category.getDisplay(language) + "</br>");
	}
%>
	</td>
	<td>
		Level:<br/>
<%
	for (Level level : Level.enumerationList) {
		out.println("<input type='checkbox' name='level' value='" + level.name() + "'" + (principal.getLevelSet().contains(level) ? " checked" : "") + ">" + level.getDisplay() + "<br/>");
	}
%>
	</td>
	<td>
		<button>Filter</button>
		<a href="javascript:toggleFilterBox()">Close</a>
	</td>
</tr>
</table>
</form>
</div>
<div id="filterToggle">
	<button onclick="javascript:toggleFilterBox()">Filter</button>
</div>

<div id="questionList">
<%
for (Question question : questionList) {
	out.print("<div id='questionBox" + question.getId() + "'><div class='question'>");
	out.print("<a class='link' href='" + contextPath + "/question/" + question.getId() + "/" + principalUserId + "'>");
	out.print(Utility.ellipsis(question.getText()));
	out.print("</a> <div class='unshowed righty' id='question" + question.getId() + "'></div>");
	out.print("<br class='unfloat'/>");
	if (question.getCategory() != null) {
		out.println("<div class='tag'>" + question.getCategory().getDisplay(language) + "</div>");
	}
	if (question.getLevel() != null) {
		out.println("<div class='tag'>" + question.getLevel().getDisplay() + "</div>");
	}
	out.print("<div class='status'" + (question.isOpen() ? " style='display:none;' " : " ") + "id='questionClosed" + question.getId() + "'>Closed</div>");
	if (question.getConversationSize() > 0) {
		out.print("<div>" + question.getConversationSize() + " " + "conversation" + (question.getConversationSize() > 1 ? "s" : "") +"</div>");
	}
	out.print("<div class='small righty'>" + question.getCreatedTime() + "</div><br class='unfloat'/>");
	out.println("</div></div>");
}
%>
</div>

<div class="paging">
<div class="lefty page"><a href="<%= contextPath %>/dashboard?page=<%= (pageNumber - 1) %>">&larr; {{::language.previousPage}}</a></div>
<div class="righty page"><a href="<%= contextPath %>/dashboard?page=<%= (pageNumber + 1) %>">{{::language.nextPage}} &rarr;</a></div>
<br class="unfloat"/>
</div>

<% } %>

</div>

<script type="application/javascript">

var questionList = [];

<%
if (isLearner) {
%>

Channel.connect('<%= contextPath %>/channel/summary?channelId=<%= channelId %>', function(message) {
	console.log(message.data);
	var data = jQuery.parseJSON(message.data);
	jQuery('#unshowedTotal').text(data.total);
});

<%
} else if (isEducator) {
	for (Question question : questionList) {
		out.println("questionList.push(" + question.getId() + ");");
	}
%>

Channel.connect('<%= contextPath %>/channel/summary?channelId=<%= channelId %>', function(message) {
	console.log(message.data);
	var data = jQuery.parseJSON(message.data);
	if (data.total !== undefined) {
		jQuery('#unshowedTotal').text(data.total);
	}
	if (data.questionList !== undefined) {
		jQuery.each(questionList, function(index, entry) {
			jQuery('#question' + entry).text('');
		});
		jQuery.each(data.questionList, function(index, entry) {
			var questionId = entry.id;
			var questionTotal = entry.total;
			jQuery('#question' + questionId).text(questionTotal);
		});
	}
	if (data.questionDeleted !== undefined) {
		jQuery('#questionBox' + data.questionDeleted).fadeOut(500, function() { jQuery(this).remove(); });
		jQuery.each(questionList, function(index, entry) {
			if (entry === data.questionDeleted) {
				questionList.splice(index, 1);
			}
		});
	}
<% if (pageNumber == 1) { %>
	if (data.questionAdded !== undefined) {
		var question = data.questionAdded;

		var html = '';

		html += '<div id="questionBox' + question.id + '" class="hidden"><div class="question">';

		html += '<a class="link" href="<%= contextPath %>/question/' + question.id + '/<%= principal.getUserId() %>">' + question.text + '</a>';
		html += '<div class="unshowed righty" id="question' + question.id + '"></div>';
		html += '<br class="unfloat"/>';
		if (question.category !== undefined) {
			html += '<div class="tag">';
			if ('English' === '<%= language %>') {
				html += question.category.english;
			} else if ('Indonesian' === '<%= language %>') {
				html += question.category.indonesian;
			}
			html += '</div>';
		}
		if (question.level !== undefined) {
			html += '<div class="tag">' + question.level.display + '</div>';
		}
		html += '<div class="status" style="display:none;" id="questionClosed' + question.id + '">Closed</div>';
		if (question.conversationSize > 0) {
			html += '<div>' + question.conversationSize + ' ' + 'conversation' + (question.conversationSize > 1 ? 's' : '') + '</div>';
		}
		html += '<div class="small righty">' + question.createdTime + '</div><br class="unfloat"/>'
		html += '</div></div>';

		jQuery('#questionList').prepend(html);
		jQuery('#questionBox' + question.id).fadeIn(500);
		questionList.push(question.id);
	}
<% } %>
	if (data.questionClosed !== undefined) {
		jQuery('#questionClosed' + data.questionClosed).show();
	}
	if (data.questionOpened !== undefined) {
		jQuery('#questionClosed' + data.questionOpened).hide();
	}
});

<%
}
%>

function toggleFilterBox() {
	var box = jQuery('#filterBox');
	var toggle = jQuery('#filterToggle');
	box.toggle();
	toggle.toggle();
}
</script>

<script>
var tangerineApplication = angular.module('tangerineApplication', []);
tangerineApplication.controller('controller', ['$scope', function($scope) {
	$scope.language = language;
}]);
</script>

<%@include file="language.jsp"%>
</body>
<%@include file="footer.jsp"%>
