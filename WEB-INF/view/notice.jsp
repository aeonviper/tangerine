<%@ page import="orion.controller.*" %>

<%
tangerine.controller.BaseController baseController = (tangerine.controller.BaseController) request.getAttribute(orion.core.Constant.model);
Notification notification = baseController.getNotification();
%>

<%
if (!notification.getNoticeList().isEmpty()) {
	out.println("<h5 style='padding:0 10px;'>Notice</h5>");
	out.println("<pre style='padding:0 10px;'>");
	for (String message : notification.getNoticeList()) {
		out.println(message);
	}
	out.println("</pre>");
}
if (!notification.getErrorList().isEmpty()) {
	out.println("<h5 style='padding:0 10px;'>Error</h5>");
	out.println("<pre style='padding:0 10px;'>");
	for (String message : notification.getErrorList()) {
		out.println(message);
	}
	out.println("</pre>");
}
if (!notification.getFieldErrorList().isEmpty()) {
	out.println("<h5 style='padding:0 10px;'>Field Error</h5>");
	out.println("<pre style='padding:0 10px;'>");
	for (String message : notification.getFieldErrorList()) {
		out.println(message);
	}
	out.println("</pre>");
}
%>
