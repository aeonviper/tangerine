<%@ page import="java.util.*" %>
<%@ page import="tangerine.enumeration.*" %>
<%@ page import="tangerine.model.*" %>
<%@ page import="tangerine.bean.*" %>
<%@ page import="tangerine.controller.*" %>
<%@ page import="tangerine.core.*" %>
<%@ page import="tangerine.websocket.*" %>
<%@ page import="orion.view.*" %>

<%
String contextPath = orion.core.Constant.getContextPath();
tangerine.controller.BaseController baseModel = (tangerine.controller.BaseController) request.getAttribute(orion.core.Constant.model);
Language language = Language.Indonesian;
boolean isEducator = false;
boolean isLearner = false;
Long principalUserId = null;
Principal principal = null;
if (baseModel != null) {
	isEducator =  baseModel.isEducator();
	isLearner = baseModel.isLearner();
	principal = baseModel.getPrincipal();
	language = baseModel.getLanguage() != null ? baseModel.getLanguage() : language.Indonesian;
	if (principal != null) {
		principalUserId = principal.getUserId();
	}
}
boolean mobile = false;
String userAgent = request.getHeader("user-agent");
if (userAgent != null && userAgent.indexOf("Mobi") != -1) {
	mobile = true;
}
%>

<html ng-app="tangerineApplication">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>tanyatutor</title>

	<link rel="shortcut icon" href="<%= contextPath %>/resource/image/application/favicon.png">

<!--
	<link href='https://fonts.googleapis.com/css?family=Patua+One|Open+Sans|Roboto|Source+Sans+Pro:400,700' rel='stylesheet' type='text/css'>
	<link href='https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700|Open+Sans:300,400,600,700,800|PT+Sans:400,700|Roboto:400,300,500,700,900|Coustard:400,900|Asap:400,700|Lilita+One|Source+Sans+Pro:200,300,400,600,700,900' rel='stylesheet' type='text/css'>
-->

	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resource/font/font.css">
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resource/style/style.css">

	<script type="text/javascript" src="<%= contextPath %>/resource/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%= contextPath %>/resource/script/channel.js"></script>

	<script src="<%= contextPath %>/resource/angular/angular.min.js"></script>
<!--
	<script src="<%= contextPath %>/resource/angular/i18n/angular-locale_id-id.js"></script>

	<script src="<%= contextPath %>/resource/angular/angular-sanitize.min.js"></script>
	<script src="<%= contextPath %>/resource/angular/angular-route.min.js"></script>
	<script src="<%= contextPath %>/resource/script/application.js"></script>
	<script src="<%= contextPath %>/resource/script/analytics.js"></script>
-->	
	<script src="<%= contextPath %>/resource/script/language.js"></script>

	<script type="text/javascript" src="<%= contextPath %>/resource/plupload/plupload.full.min.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%= contextPath %>/resource/plupload/jquery.plupload.queue/jquery.plupload.queue.min.js" charset="UTF-8"></script>
<!-- 
	<link type="text/css" rel="stylesheet" href="<%= contextPath %>/resource/plupload/jquery.plupload.queue/css/jquery.plupload.queue.css" media="screen" />
-->
	<script>
	var confirmDelete = function(url) {
		if (confirm('Are you sure you want to delete this?')) {
			// top.location.href=url;
			window.location=url;
		}
	};
	</script>
<%@include file="analytics.jsp"%>
</head>

