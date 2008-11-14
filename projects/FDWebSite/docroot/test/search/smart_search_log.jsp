<%@page import="com.freshdirect.webapp.taglib.test.SnapshotGenerator"%>
<%
/**
* author: gmark
*/

	if (!SnapshotGenerator.isRunning()) {
		response.sendRedirect("/test/search/smart_search.jsp");
		return;	
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Smart Search Snapshot Generator log</title>
<meta http-equiv="refresh" content="5">
</head>
<body>

<pre>
<%=SnapshotGenerator.getLog()%>
</pre>

</body>
</html>	