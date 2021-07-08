<% if (isLearner) { %>
<div style="background:linear-gradient(90deg, #0072C6, #27C4E7);color:#fff;padding:10px;border-radius:0 0 5px 5px;margin:0 10px;box-shadow:0px 2px 1px #aaa;">
	<table>
	<tr>
		<td>
			<a style="text-decoration:none;" href="<%= contextPath %>/dashboard"><span class="logo" style="font-family: 'Patua One', cursive;">tt. <sup class="beta">Beta</sup></span></a>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/question/ask">{{::language.ask}}</a>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/panel">{{::language.chat}}</a>
			<span class="unshowed" id="unshowedTotal"></span>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/configuration">{{::language.setting}}</a>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/logout/do">{{::language.logout}}</a>
		</td>
	</tr>
	</table>
</div>
<% } %>

<% if (isEducator) { %>
<div style="background:linear-gradient(90deg, #0072C6, #27C4E7);color:#fff;padding:10px;border-radius:0 0 5px 5px;margin:0 10px;box-shadow:0px 2px 1px #aaa;">
	<table>
	<tr>
		<td>
			<a style="text-decoration:none;" href="<%= contextPath %>/dashboard"><span class="logo" style="font-family: 'Patua One', cursive;">tt. <sup class="beta">Beta</sup></span></a>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/panel">{{::language.chat}}</a>
			<span class="unshowed" id="unshowedTotal"></span>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/configuration">{{::language.setting}}</a>
		</td>
		<td>
			<a style="color:white;" href="<%= contextPath %>/logout/do">{{::language.logout}}</a>
		</td>
	</tr>
	</table>
</div>
<% } %>

<%@include file="notice.jsp"%>
