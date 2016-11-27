<!DOCTYPE html>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.BackgroundStatus"%>
<%@page import="com.freshdirect.cms.search.IBackgroundProcessor"%>
<%
	IBackgroundProcessor tool = (IBackgroundProcessor) FDRegistry.getInstance().getService("com.freshdirect.cms.backgroundProcessor", IBackgroundProcessor.class);
	BackgroundStatus status = tool.getStatus();

	DateFormat fmt = new SimpleDateFormat();
%>
<html>
<head>
	<meta charset="UTF-8"/>
	<title>Search Re-Index Page</title>
</head>
<body>
	<% if (status.isRunning()) {
		final Calendar CAL = Calendar.getInstance();
		CAL.setTimeInMillis(status.getStarted());

		%>
		<div><a href="?action=refresh">Refresh page</a></div>
		<h2>Re-index process is running</h2>
		<div>Please refresh the page to check the status. If this text disappears and the button comes back, then the process is finished</div>

		<div>Started at: <span><%= fmt.format(CAL.getTime()) %></span></div>
		<div>Status: <span><%= status.getCurrent() %></span></div>

		<div>Last Reindex Status: <span><%= status.getLastReindexResult() %></span></div>
	<%
	} else if ("post".equalsIgnoreCase(request.getMethod()) &&  "reindex".equalsIgnoreCase(request.getParameter("action"))) {
		tool.backgroundReindex();

		response.sendRedirect( request.getRequestURI() );
	} else {
		%>
		<form method="POST" action="?action=reindex">
			<button>Start Search Re-Index</button>
		</form>
		<%		
	}
	%>
</body>
</html>