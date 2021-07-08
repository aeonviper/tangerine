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

<div class='three'>{{::language.conversation}}</div>
<div class='text'>{{::language.betweenYou}} <%= model.getOtherUser().getName() %></div>

<div id="answerList">
<%
for (Answer answer : answerList) {
	out.println("<div id='answerBox" + answer.getId() + "'>");
	if (answer.getUserId().equals(question.getUserId())) {
		if (answer.getShowed() != null) {
			out.print("<div class='answer showed whitespace'>");
		} else {
			out.print("<div class='answer whitespace'>");
		}
	} else {
		if (answer.getShowed() != null) {
			out.print("<div class='answer showed whitespace' style='margin-left:20px;'>");
		} else {
			out.print("<div class='answer whitespace' style='margin-left:20px;'>");
		}
	}
	out.print(Utility.strip(answer.getText()) + "<div class='time'>");

	if (answer.isDeletable(principal.getUserId())) {
		out.print("<a style='font-size:12px;' href='" + Constant.getContextPath() + "/answer/delete/do?id=" + answer.getId() + "&questionId=" + question.getId() + "&answerUserId=" + answerUserId + "&channelId=" + channelId + "'>{{::language.delete}}</a> ");
	}

	out.print(answer.createdTime() + "</div>");

	out.println("</div><br/>");

	for (tangerine.model.Attachment attachment : answer.getAttachmentList()) {
		out.println("<div class='attachment'>");
		if (attachment.getPath() != null) {
			out.println("<img class='attachment' src='" + Constant.getContextPath() + "/" + Constant.assetQuestion + attachment.getPath() + "'>");
		}
		if (attachment.getDescription() != null) {
			out.println("<div class='description'>" + attachment.getDescription() + "</div>");
		}
		out.println("</div>");
	}
	out.println("</div>");

}
%>
</div>

<script type="application/javascript">

Channel.connect('<%= contextPath %>/channel/answer/list?questionId=<%= question.getId() %>&channelId=<%= channelId %>', function(message) {
	console.log(message.data);
	var data = jQuery.parseJSON(message.data);
	if (data.total !== undefined) {
		jQuery('#unshowedTotal').text(data.total);
	}
	if (data.recentlyShowedList !== undefined) {
		jQuery.each(data.recentlyShowedList, function(index, entry) {
			jQuery('#answerBox' + entry + ' > div').addClass('showed');
		});
	}
	if (data.answerDeleted !== undefined) {
		jQuery('#answerBox' + data.answerDeleted).fadeOut(500, function() { jQuery(this).remove(); });
	}
	if (data.answerAdded !== undefined) {
		var answer = data.answerAdded;

		var html = '<div id="answerBox' + answer.id + '">';
		if (answer.userId === <%= question.getUserId()%>) {
			html += '<div class="answer showed whitespace hidden">';
		} else {
			html += '<div class="answer showed whitespace hidden" style="margin-left:20px;"">';
		}

		if (answer.text !== undefined) {
			html += answer.text;
		}

		html += '<div class="time">';
		if (answer.userId === <%= principal.getUserId()%>) {
			html += '<a style="font-size:12px;" href="<%= Constant.getContextPath() %>/answer/delete/do?id=' + answer.id + '&questionId=<%= question.getId() %>&answerUserId=<%= answerUserId %>&channelId=<%= channelId %>">' + language.delete + '</a> ';
		}
		html += answer.createdTime + '</div>';

		html += '</div></br>';

		jQuery.each(answer.attachmentList, function(index, entry) {
			html += '<div class="attachment">';

			if (entry.path !== '') {
				html += '<img class="attachment" src="<%= Constant.getContextPath() %>/<%= Constant.assetQuestion %>' + entry.path + '">';
			}

			if (entry.description !== undefined) {
				html += '<div class="description">' + entry.description + '</div>';
			}

			html += '</div>';
		});

		html += '</div>';

		jQuery('#answerList').append(html);
		jQuery('#answerBox' + answer.id).fadeIn(500);
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

<form method="post" action="<%= contextPath%>/answer/do" enctype="multipart/form-data" id='answer'>
<input type="hidden" name="questionId" value="<%= question.getId() %>">
<input type="hidden" name="answerUserId" value="<%= answerUserId %>">
<input type="hidden" name="channelId" value="<%= channelId %>">
<textarea rows="5" style="width:100%;" name="answer.text" placeholder="{{::language.yourAnswer}}"></textarea>
<table class="placement">
<tr>
	<td>
<% for (int i = 0; i < tangerine.controller.AnswerController.maxAttachment; i++) { %>
		<div style="display:inline-block;">
			<input type="file" id="file<%= i %>" name="file[<%= i %>]">
			<input type="text" placeholder="{{::language.photoDescription}}" name="fileDescription[<%= i %>]">
		</div>
<% } %>
	</td>
	<td align="right">
		<button>{{::language.send}}</button>
	</td>
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
