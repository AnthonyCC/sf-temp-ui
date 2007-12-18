<%@ page isErrorPage="true" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<html>
<head>
<title>Error Encountered</title>
<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
</head>
<body>
<br>
<center>
<div class="content" style="width: 60%; height: auto;" align="center">
<br>
<span class="order_step">FD CRM : Error Encountered</span>
<hr class="gray1px">
<span class="order"><b>We've encountered an error :(</b><br><br>
<a href="/main/index.jsp"><b>Click here</b></a> to return to the home page.
<br><br>
</span>
<%	if (exception!=null) { %>
<span class="error_detail"><b><%= exception %></b></span></center>
<br>
<pre><span class="note">
<%		JspLogger.CC_GENERIC.error("CallCenter got an error page", exception); %>
</span></pre>
<% } %>
<br><br><br>
</div>
</center>
</body>
</html>
