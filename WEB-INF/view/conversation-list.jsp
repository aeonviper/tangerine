<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.QuestionController model = (tangerine.controller.QuestionController) request.getAttribute(orion.core.Constant.model);
Question question = model.getQuestion();
List<Answer> answerList = model.getAnswerList();
List<Conversation> conversationList = model.getConversationList();
Long answerUserId = model.getAnswerUserId();
String channelId = Channel.generateChannelId();
%>

<div class="content">

<%@include file="question.jsp"%>
<br/>

<div class='three' id='answer'>{{::language.conversation}}</div>

<div id="conversationList">
<%
for (Conversation conversation : conversationList) {
	out.print("<div id='conversationBox" + conversation.getId() + "'><div class='answer whitespace'>");
	out.print("<a class='link' href='" + Constant.getContextPath() + "/question/" + question.getId() + "/" + conversation.getUserId() + "'>");
	out.print(Utility.ellipsis(conversation.getLatestAnswer().getText()));
	out.print("</a> <div class='unshowed righty' id='conversation" + conversation.getId() + "'></div><br class='unfloat'/>");
	out.print("<small>{{::language.by}} " + conversation.getUser().getName() + "</small>");
	out.println("</div></div>");
}
%>
</div>

<script type="application/javascript">

var conversationList = [];
<%
for (Conversation conversation : conversationList) {
	out.println("conversationList.push(" + conversation.getId() + ");");
}
%>

Channel.connect('<%= contextPath %>/channel/conversation/list?channelId=<%= channelId %>&questionId=<%= question.getId() %>', function(message) {
	console.log(message.data);
	var data = jQuery.parseJSON(message.data);
	if (data.total !== undefined) {
		jQuery('#unshowedTotal').text(data.total);
	}
	if (data.conversationList !== undefined) {
		jQuery.each(conversationList, function(index, entry) {
			jQuery('#conversation' + entry).text('');
		});
		jQuery.each(data.conversationList, function(index, entry) {
			var conversationId = entry.id;
			var conversationTotal = entry.total;
			jQuery('#conversation' + conversationId).text(conversationTotal);
			jQuery('#conversationBox' + conversationId + ' > a').html(entry.lastAnswerText);
		});
	}
	if (data.conversationDeleted !== undefined) {
		jQuery('#conversationBox' + data.conversationDeleted).fadeOut(500, function() { jQuery(this).remove(); });
		jQuery.each(conversationList, function(index, entry) {
			if (entry === data.conversationDeleted) {
				conversationList.splice(index, 1);
			}
		});
	}
	if (data.conversationAdded !== undefined) {
		var conversation = data.conversationAdded;
		jQuery('#conversationList').append('<div id="conversationBox' + conversation.id + '" class="hidden"><div class="answer whitespace"><a class="link" href="<%= contextPath %>/question/' + conversation.questionId + '/' + conversation.userId + '">' + conversation.latestAnswer.text + '</a> <div class="unshowed righty" id="conversation' + conversation.id + '">1</div><br class="unfloat"><small>' + language.by + ' ' + conversation.user.name + '</small></div></div>');
		jQuery('#conversationBox' + conversation.id).fadeIn(500);
		conversationList.push(conversation.id);
	}
	if (data.questionDeleted === <%= question.getId() %>) {
		jQuery('#body').append('<div class="overlay"></div><div class="modal">This question has been deleted<br/>Go to <a href="<%= Constant.getContextPath() %>/dashboard">Dashboard</a></div>');
	}
	if (data.questionClosed === <%= question.getId() %>) {
		jQuery('#statusSet').append('<div class="status" id="questionClosed' + data.questionClosed + '" title="">Closed</div>');
		<% if (isLearner) { %>
		jQuery('#closeQuestionButton').hide();
		jQuery('#reopenQuestionButton').show();
		<% } %>
	}
	if (data.questionOpened === <%= question.getId() %>) {
		jQuery('#questionClosed' + data.questionOpened).fadeOut(500, function() { jQuery(this).remove(); });
		<% if (isLearner) { %>
		jQuery('#closeQuestionButton').show();
		jQuery('#reopenQuestionButton').hide();
		<% } %>
	}
});

</script>

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
