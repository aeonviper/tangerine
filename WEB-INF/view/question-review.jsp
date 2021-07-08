<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.QuestionController model = (tangerine.controller.QuestionController) request.getAttribute(orion.core.Constant.model);
Question question = model.getQuestion();
%>

<div class="content">

<form method="post" action="<%= contextPath%>/question/review/do">

<input type="hidden" name="question.id" value="<%= question.getId() %>">

<div class="three">{{::language.review}}</div>

<br/>

<%
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

<textarea rows="5" style="width:100%;" readonly><%= Utility.strip(question.getText()) %></textarea>

<% if (question.getExplanationType() == ExplanationType.ExplanationOnly) { %>
Ingin penjelasan dulu, jawaban hanya kalau diminta<br/>
<% } %>
<% if (question.getExplanationType() == ExplanationType.ExplanationAndAnswer) { %>
Ingin penjelasan dan jawaban sekaligus<br/>
<% } %>

<% if (question.getCategory() != null) { %>
	<div class="text">{{::language.subject}}: <strong><%=question.getCategory().getDisplay(language)%></strong></div>
<% } %>

<% if (question.getLevel() != null) { %>
	<div class="text">{{::language.level}}: <strong><%= question.getLevel().getDisplay() %></strong></div>
<% } %>
<small>{{::language.postedOn}} <%= question.getCreatedTime() %></small>
<br/>

<button>{{::language.ask}}</button>
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
