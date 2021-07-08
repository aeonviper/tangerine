<div class="three">{{::language.question}}</div>

<br/>

<div class="hidden" id="attachmentBox">
<%
	int attachmentListSize = question.getAttachmentList().size();
	for (tangerine.model.Attachment attachment : question.getAttachmentList()) {
		out.println("<div class='attachment'>");
		if (attachment.getPath() != null) {
			out.println("<img class='attachment' src='" + Constant.getContextPath() + "/" + Constant.assetQuestion + attachment.getPath() + "'>");
		}
		if (attachment.getDescription() != null) {
			out.println("<div class='description'>" + attachment.getDescription() + "</div>");
		}
		out.println("</div><br/>");
	}
%>
</div>

<script>

function toggleAttachment() {
	jQuery("#attachmentBox").toggle();
	jQuery("#attachmentToggle").toggle();
}

</script>

<div class="detail whitespace"><%= Utility.strip(question.getText()) %></div>

<% if (attachmentListSize > 0) { %>
<div id="attachmentToggle">
	<a href="javascript:toggleAttachment();"><%= attachmentListSize %> attachment<%= (attachmentListSize > 1 ? "s" : "") %> ...</a>
</div>
<% } %>
<br/>

<% if (question.getExplanationType() == ExplanationType.ExplanationOnly) { %>
Ingin penjelasan dulu, jawaban hanya kalau diminta<br/>
<% } %>
<% if (question.getExplanationType() == ExplanationType.ExplanationAndAnswer) { %>
Ingin penjelasan dan jawaban sekaligus<br/>
<% } %>

<% if (question.getCategory() != null) { %>
	<div class="tag">{{::language.subject}}: <%= question.getCategory().getDisplay(language) %></div>
<% } %>

<% if (question.getLevel() != null) { %>
	<div class="tag">{{::language.level}}: <%= question.getLevel().getDisplay() %></div>
<% } %>
<small>{{::language.postedOn}} <%= question.getCreatedTime() %></small>
<br/>

<span id="statusSet">
<%
if (!question.isOpen()) {
	out.println("<div class='status' id='questionClosed" + question.getId() + "' title='Closed on " + tangerine.core.DateTimeUtility.formatTime(question.getClosed()) + "'>Closed</div>");
}
%>
</span>

<%
if (isLearner) {
	String closeClass = "";
	String openClass = "";
	if (!question.isCloseable(principal.getUserId())) {
		closeClass = "hidden";
	}
	if (!question.isOpenable(principal.getUserId())) {
		openClass = "hidden";
	}
%>
<a id="closeQuestionButton" class="<%= closeClass %>" href="<%= contextPath %>/question/close/do?id=<%= question.getId() %>&answerUserId=<%= (answerUserId != null ? answerUserId : "") %>&channelId=<%= channelId %>">{{::language.close}} {{::language.question}}</a>
<a id="reopenQuestionButton" class="<%= openClass %>" href="<%= contextPath %>/question/open/do?id=<%= question.getId() %>&answerUserId=<%= (answerUserId != null ? answerUserId : "") %>&channelId=<%= channelId %>">{{::language.reopen}} {{::language.question}}</a>
<%
}
%>

<% if (question.isDeletable(principal.getUserId())) { %>
<a href="<%= contextPath %>/question/delete/do?id=<%= question.getId() %>&channelId=<%= channelId %>">{{::language.delete}} {{::language.question}}</a>
<br/>
<% } %>
