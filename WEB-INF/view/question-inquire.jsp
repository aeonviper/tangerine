<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.QuestionController model = (tangerine.controller.QuestionController) request.getAttribute(orion.core.Constant.model);
%>

<div class="content">

<form method="post" action="<%= contextPath %>/question/inquire/do" enctype="multipart/form-data">
<div class="three">{{::language.photo}}</div>
Upload foto yang berisi hal hal yang ingin kamu tanyakan<br/>
(contoh: soal PR yang kamu kesulitan menjawab, rumus yang membingungkan, atau cuplikan buku yang ingin kamu diskusikan)<br/>
Untuk memperjelas pertanyaan, kamu bisa upload sampai 10 foto, gunakan untuk foto pertanyaannya sendiri, foto rumus rumus yang berkaitan, dan info lainnya yang berkaitan dengan pertanyaan<br/>
<br/>
<% for (int i = 0; i < tangerine.controller.QuestionController.maxAttachment; i++) { %>
<div style="display:inline-block;">
<input type="file" id="file<%= i %>" name="file[<%= i %>]">
<input type="text" placeholder="{{::language.photoDescription}}" name="fileDescription[<%= i %>]">
</div>
<% } %>

<div>
	You are using the standard uploader. Use the <a href="<%= contextPath %>/question/ask">multi uploader</a>
</div>

<br/>

<div class="three">{{::language.question}}</div>
<textarea name="question.text" rows="5" style="width:100%;" placeholder="{{::language.yourQuestion}}"></textarea>

<div class="three">{{::language.explanation}}</div>
<input type="radio" name="question.explanationType" value="ExplanationOnly" checked> Ingin penjelasan dulu, jawaban hanya kalau diminta<br/>
<input type="radio" name="question.explanationType" value="ExplanationAndAnswer"> Ingin penjelasan dan jawaban sekaligus<br/>

<div class="three">{{::language.subject}}</div>
<select name="question.category">
	<option name="" value=""/>
<%
	for (Category category : Category.enumerationList) {
		if (category.equals(principal.getLastUsedCategory())) {
			out.println("<option value='" + category.name() + "' selected>" + category.getDisplay(language) + "</option>");
		} else {
			out.println("<option value='" + category.name() + "'>" + category.getDisplay(language) + "</option>");
		}
	}
%>
</select>

<div class="three">{{::language.level}}</div>
<select name="question.level">
	<option name="" value=""/>
<%
	for (Level level : Level.enumerationList) {
		if (level.equals(principal.getLastUsedLevel())) {
			out.println("<option value='" + level.name() + "' selected>" + level.getDisplay() + "</option>");
		} else {
			out.println("<option value='" + level.name() + "'>" + level.getDisplay() + "</option>");
		}
	}
%>
</select>
<br/>
<br/>

<button>{{::language.next}}</button>
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
