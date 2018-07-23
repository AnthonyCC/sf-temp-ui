<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>

<html lang="en-US" xml:lang="en-US">
<head>
	<%--<title>FreshDirect - Help</title> --%>
     <fd:SEOMetaTag title="FreshDirect - Help - About FreshDirect"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="147">
			    <TR VALIGN="TOP">
				    <TD WIDTH="6"><IMG src="/media_stat/images/layout/clear.gif"
			            WIDTH="6" HEIGHT="1" ALT="" BORDER="0"></TD>
				    <TD WIDTH="141"><BR>
<A HREF="/help/faq_index.jsp" TARGET="_top"><img src="/media_stat/images/template/help/fdqa_catnav.gif" width="118" height="56" border="0" alt="FreshDirect Q &amp; A"></A><BR>
<BR>
<A HREF="/help/faq_about.jsp" TARGET="_top">What We Do</A><BR>
<A HREF="/help/faq_signing_up.jsp" TARGET="_top">Signing Up</A><BR>
<A HREF="/help/faq_security.jsp" TARGET="_top">Security &amp; Privacy</A><BR>
<A HREF="/help/faq_shopping.jsp" TARGET="_top">Shopping</A><BR>
<A HREF="/help/faq_payment.jsp" TARGET="_top">Payment</A><BR>
<A HREF="/help/faq_delivery.jsp" TARGET="_top">Home Delivery</A><BR>
<%	if(user.isDepotUser()){%>	
<A HREF="/help/faq_delivery_depot.jsp" TARGET="_top">Depot Delivery</A><BR>
<%	}%>	
<A HREF="/help/faq_inside_fd.jsp" TARGET="_top">Jobs & Corporate Info</A><BR></TD>
<TD WIDTH="4"><BR></TD>
</TR>
</TABLE>
</BODY>		
</html>		
