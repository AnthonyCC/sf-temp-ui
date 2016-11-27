<%@page import="com.freshdirect.webapp.util.RequestClassifier"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.fdstore.iplocator.IpLocatorData"%>
<%@page import="com.freshdirect.fdstore.iplocator.IpLocatorClient"%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<h2>Page for testing Melissa IP address to ZIP code resolution service</h2>

<a href="request.jsp">Test page for requests</a> | <a href="iplocator_event.jsp">Test page for IP Locator event logs</a><br><br>

New Request
<form method="post">
	<input type="text" name="ip" value="<%=request.getRemoteAddr()%>"/>
	<input type="submit" value="Submit IP"/>
</form>

<hr>

<%
if ("POST".equalsIgnoreCase(request.getMethod())){
	try {
		IpLocatorData ipLocData = IpLocatorClient.getInstance().getData(request.getParameter("ip"));
		%>IP Locator zip code is <%=ipLocData.getZipCode() %> for IP <%=request.getParameter("ip")%><%
	} catch (Exception e){
		%>Exception occurred: <%=e%><%
	}

}%>

<h3>IP Locator Information</h3>
IP Locator is <%= FDStoreProperties.isIpLocatorEnabled() ? "enabled" : "disabled"%><br/>
<% RequestClassifier requestClassifier = new RequestClassifier(request);%>
IP Locator is <%= requestClassifier.isInHashRange(FDStoreProperties.getIpLocatorRolloutPercent()) ? "" : "not "%> rolled out for the user (roll percentage is <%=FDStoreProperties.getIpLocatorRolloutPercent()%>)<br/>

User-Agent
<ul>
	<li>header is <%=requestClassifier.getUserAgent()%></li>
	<li>hash percent is <%=requestClassifier.getHashPercent()%></li>
</ul>
<br/>

</body>
</html>