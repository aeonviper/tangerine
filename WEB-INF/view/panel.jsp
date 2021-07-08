<%@include file="header.jsp"%>
<body id="body" ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.PanelController model = (tangerine.controller.PanelController) request.getAttribute(orion.core.Constant.model);
User user = model.getUser();
List<Question> questionList = model.getQuestionList();
String channelId = Channel.generateChannelId();
Integer pageNumber = model.getPage();
%>

<div class="content">

<div id="questionList">
<%
for (Question question : questionList) {
	out.print("<div id='questionBox" + question.getId() + "'><div class='question'>");
	if (isLearner) {
		out.print("<a class='link' href='" + Constant.getContextPath() + "/question/" + question.getId() + "'>");
	} else if (isEducator) {
		out.print("<a class='link' href='" + Constant.getContextPath() + "/question/" + question.getId() + "/" + principalUserId + "'>");
	}
	out.print(Utility.ellipsis(question.getText()));
	out.print("</a> <div class='unshowed righty' id='question" + question.getId() + "'></div><br class='unfloat'/>");
	if (isEducator) {
		out.print("<small>{{::language.by}} " + question.getUser().getName() + "</small>");
	}
	out.print("<div class='status'" + (question.isOpen() ? " style='display:none;' " : " ") + "id='questionClosed" + question.getId() + "'>Closed</div>");
	out.print("<div class='small righty'>" + question.getCreatedTime() + "</div><br class='unfloat'/>");
	out.println("</div></div>");
}
%>
</div>

<div class="paging">
<div class="lefty page"><a href="<%= contextPath %>/panel?page=<%= (pageNumber - 1) %>">&larr; {{::language.previousPage}}</a></div>
<div class="righty page"><a href="<%= contextPath %>/panel?page=<%= (pageNumber + 1) %>">{{::language.nextPage}} &rarr;</a></div>
<br class="unfloat"/>
</div>

</div>

<script type="application/javascript">

var questionList = [];
<%
for (Question question : questionList) {
	out.println("questionList.push(" + question.getId() + ");");
}
%>

Channel.connect('<%= contextPath %>/channel/question/list?channelId=<%= channelId %>', function(message) {
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
<% if (isLearner) { %>
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
		html += '<a class="link" href="<%= contextPath %>/question/' + question.id + '">';
		html += question.text;
		html += '</a> <div class="unshowed righty" id="question' + question.id + '"></div><br class="unfloat"/>';
		html += '<div class="status hidden" id="questionClosed' + question.id + '">Closed</div>';
		html += '<div class="small righty">' + question.createdTime + '</div><br class="unfloat"/>';
		html += '</div></div>';

		jQuery('#questionList').prepend(html);
		jQuery('#questionBox' + question.id).fadeIn(500);

		questionList.push(question.id);
	}
<% } %>
<% } %>
<% if (isEducator) { %>
	if (data.conversationDeletedConversation !== undefined) {
		var conversation = data.conversationDeletedConversation;
		jQuery('#questionBox' + conversation.questionId).fadeOut(500, function() { jQuery(this).remove(); });
		jQuery.each(questionList, function(index, entry) {
			if (entry === conversation.questionId) {
				questionList.splice(index, 1);
			}
		});
	}
	if (data.conversationAddedQuestion !== undefined) {
		var question = data.conversationAddedQuestion;

		var html = '';
		html += '<div id="questionBox' + question.id + '" class="hidden"><div class="question">';
		html += '<a class="link" href="<%= contextPath %>/question/' + question.id + '/<%= principal.getUserId() %>">';
		html += question.text;
		html += '</a> <div class="unshowed righty" id="question' + question.id + '"></div><br class="unfloat"/>';
		html += '<small>' + language.by + ' ' + question.user.name + '</small>';
		html += '<div class="status hidden" id="questionClosed' + question.id + '">Closed</div>';
		html += '<div class="small righty">' + question.createdTime + '</div><br class="unfloat"/>';
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
