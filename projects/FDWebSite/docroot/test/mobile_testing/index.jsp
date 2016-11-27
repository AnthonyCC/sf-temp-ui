<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%
	//the simple way

	String UA = request.getHeader("User-Agent");
	String gotoStore = "FALSE";
	if ( request.getParameter("gotoStore") != null && "TRUE".equals(request.getParameter("gotoStore")) ) {
		gotoStore = "TRUE";
	}
%>
Detected User-String: <%=UA%><br /><br />
Detected iPhone: <%=UA.toLowerCase().indexOf("iphone;")>=0%><br />
Detected iPod: <%=UA.toLowerCase().indexOf("ipod;")>=0%><br />
<br /><br />
Test redirector: <a href="/test/mobile_testing/redirector.jsp">Test</a>
<br /><br />
Parameter: gotoStore=<%=gotoStore%>
<br /><br />
Property: fdstore.mobile.iphone.landingEnabled=<%=FDStoreProperties.isIphoneLandingEnabled()%>
<%
	/*
		more complicated way
	*/
	//WURFL : http://wurfl.sourceforge.net/
%>