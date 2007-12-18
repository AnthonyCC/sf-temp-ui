<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<HTML>
<HEAD>
<TITLE>CRM - Request Processed</TITLE>
</HEAD>
<%
	String onLoadStr = "javascript:window.close();";
	String reloadParent = request.getParameter("reloadParent");
	if ("true".equalsIgnoreCase(reloadParent)) {
		onLoadStr = "javascript:opener.location.reload(true);window.close();";
	} 
%>
<body bgcolor="#FFFFFF" text="#333333" ONLOAD="<%=onLoadStr%>">
Thank you.  Your request has been processed.
</body>
</HTML>