<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>

<html>
<head>
	<title>FreshDirect - Help</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="147">
			    <TR VALIGN="TOP">
				    <TD WIDTH="6"><IMG src="/media_stat/images/layout/clear.gif" 
			            WIDTH="6" HEIGHT="1" BORDER="0"></TD>
				    <TD WIDTH="141"><BR>
<A HREF="faq_index.jsp" TARGET="_top"><img src="/media_stat/images/template/help/fdqa_catnav.gif" width="118" height="56" border="0" alt="FreshDirect Q &amp; A"></A><BR>
<BR>
<A HREF="faq_about.jsp" TARGET="_top">What We Do</A><BR>
<A HREF="faq_signing_up.jsp" TARGET="_top">Signing Up</A><BR>
<A HREF="faq_security.jsp" TARGET="_top">Security &amp; Privacy</A><BR>
<FONT CLASS="text10bold">Shopping</FONT><BR>
<A HREF="faq_payment.jsp" TARGET="_top">Payment</A><BR>
<A HREF="faq_delivery.jsp" TARGET="_top">Home Delivery</A><BR>
<%	if(user.isDepotUser()){%>	
<A HREF="/help/faq_delivery_depot.jsp" TARGET="_top">Depot Delivery</A><BR>
<%	}%>	
<A HREF="faq_inside_fd.jsp" TARGET="_top">Jobs & Corporate Info</A><BR></TD>
<TD WIDTH="4"><BR></TD>
</TR>
</TABLE>
</BODY>		
</html>		
