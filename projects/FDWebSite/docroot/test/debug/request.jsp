<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Enumeration"%>
<%@page import="com.freshdirect.webapp.util.RequestUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>

<h2>Request details</h2>
<b>getRemoteAddr()</b>: <%=request.getRemoteAddr()%><br/>
<b>getLocalAddr()</b>: <%=request.getLocalAddr()%><br/>
<b>getRequestURI()</b>: <%=request.getRequestURI()%><br/>
<b>getRequestURL()</b>: <%=request.getRequestURL()%><br/>
<b>getAuthType()</b>: <%=request.getAuthType()%><br/>
<b>getCharacterEncoding()</b>: <%=request.getCharacterEncoding()%><br/>
<b>getMethod()</b>: <%=request.getMethod()%><br/>
<b>getRequestedSessionId()</b>: <%=request.getRequestedSessionId()%><br/>
<b>getCharacterEncoding()</b>: <%=request.getCharacterEncoding()%><br/>
<b>RequestUtil.getClientIp()</b>: <%=RequestUtil.getClientIp(request)%><br/>

<br/><h3>Headers</h3><%
String header="";
Enumeration input=request.getHeaderNames();
while(input.hasMoreElements()) {
	header=(String)input.nextElement();%>
	<%="<b>"+header+"</b>: "+ request.getHeader(header)%><br/>
<%}

%><br/><h3>AttributeNames</h3><%
input=request.getAttributeNames();
while(input.hasMoreElements()) {
	header=(String) input.nextElement();%>
	<%="<b>"+header+"</b>: "+request.getAttribute(header)%><br/>
<%}

%><br/><h3>ParameterNames</h3><%
input = request.getParameterNames();
while(input.hasMoreElements()) {
	header=(String)input.nextElement();
	String[] values=request.getParameterValues(header);%>
	<%="<b>"+header+" </b>: ["%><%
	for(int i=0;i<values.length;i++) {%>
		<%=values[i]%><%
		if(i<(values.length-1)) {
		%>, <%
		} else {
		%>] <%;
		}
	}
}%>
</body>
</html>
