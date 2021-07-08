<%@ page import="java.util.*" %>
<html>
<head>
<style>
* { font-family:monospace,sans-serif; }
span { font-size:13px; }
</style>
</head>
<body>
<span>
Exception <%= response.getStatus() %>
<%
Object object = request.getAttribute("javax.servlet.error.exception");
if (object != null && object instanceof Throwable) {
	Throwable throwable = (Throwable) object;
	out.print("<pre>");
	if (throwable.getCause() != null) {
		out.print(throwable.getCause().getMessage());
	} else {
		out.print(throwable.getMessage());
	}
	out.print("</pre>");
}
%>
</span>
</body>
</html>
