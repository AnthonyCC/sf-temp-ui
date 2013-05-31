<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<h2>Page for displaying Melissa IP Locator event logs</h2>

<a href="request.jsp">Test page for requests</a> | <a href="iplocator.jsp">Test page for IP Locator service</a><br><br>

<%
FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER );

%><h3>User data</h3>
Address: <%=user.getAddress().toString().replace(",","<br>")%><br>
SelectedServiceType: <%=user.getSelectedServiceType()%><br>
<hr>
New Request
<form method="post">
<input type="text" name="fduserid" value="<%=user.getPrimaryKey()%>"/>
<input type="submit" value="Submit FDUSER_ID"/>
</form>
<hr>
<%
if ("POST".equalsIgnoreCase(request.getMethod())){
	try {
		%><h3>IP Locator event for fduserid (<%=request.getParameter("fduserid")%>) was:</h3> <%=FDCustomerManager.loadIpLocatorEvent(request.getParameter("fduserid")).toString().replace(",","<br>") %><%
	} catch (Exception e){
		%>Exception occurred: <%=e%><%
	}

}%>


</body>
</html>