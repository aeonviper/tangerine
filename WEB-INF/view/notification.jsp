<%@ page import="java.lang.*" %>
<%@ page import="orion.controller.*" %>

<%
String contextPath = orion.core.Constant.getContextPath();
tangerine.controller.BaseController baseController = (tangerine.controller.BaseController) request.getAttribute(orion.core.Constant.model);
Notification notification = baseController.getNotification();
%>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>tanyatutor</title>

	<link rel="shortcut icon" href="<%= contextPath %>/resource/image/application/favicon.png">
<!--
	<link href='https://fonts.googleapis.com/css?family=Patua+One|Open+Sans|Roboto|Source+Sans+Pro:400,700' rel='stylesheet' type='text/css'>
-->
 	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resource/font/font.css">
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resource/style/style.css">

	<style>
		* { font-family:'Source Sans Pro', monospace; }
		pre { font-size:14px; }
	</style>
	
	<script src="<%= contextPath %>/resource/script/analytics.js"></script>
</head>
<body>

<%@include file="logo.jsp"%>
<div class="content">

<%
if (!notification.getNoticeList().isEmpty()) {
	out.println("<h3>Notice</h3>");
	out.println("<pre>");
	for (String message : notification.getNoticeList()) {
		out.println(message);
	}
	out.println("</pre>");
}
if (!notification.getErrorList().isEmpty()) {
	out.println("<h3>Error</h3>");
	out.println("<pre>");
	for (String message : notification.getErrorList()) {
		out.println(message);
	}
	out.println("</pre>");
}
if (!notification.getFieldErrorList().isEmpty()) {
	out.println("<h3>Field Error</h3>");
	out.println("<pre>");
	for (String message : notification.getFieldErrorList()) {
		out.println(message);
	}
	out.println("</pre>");
}
%>

</div>
</body>
</html>
