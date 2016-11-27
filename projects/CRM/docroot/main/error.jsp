<%@ page isErrorPage="true" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<html>
	<head>
		<title>Error Encountered</title>
		<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css" />
	</head>
<body>
	<br />
	<center>
		<div class="" style="width: 60%; height: auto;">
			<br />
			<span class="order_step">FD CRM : Error Encountered</span>
			<hr class="gray1px" />
			<span class="order"><b>We've encountered an error :(</b></span><br /><br />
				<a href="/main/main_index.jsp"><b>Click here</b></a> to return to the home page.
				<br /><br />
			
			<%	if (exception!=null) { %>
				<span class="error_detail"><b><%= exception %></b></span>
				<br />
				<pre><span class="note">
					<% JspLogger.CC_GENERIC.error("CallCenter got an error page", exception); %>
				</span></pre>
			<% } %>
			<br /><br /><br />
		</div>
	</center>
</body>
</html>