<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@page import="com.freshdirect.common.pricing.ZoneInfo"%>

<%

String name = "";
String contentPath = "";
String windowSize = "";


// Default setting for small pop up.
String wSize = "375,335";
String headerImg = "/media_stat/images/layout/pop_up_header_lg.gif";
String tableWidth = "330";
String cellWidth = "155";


if(request.getParameter("name") !=null){
	name = request.getParameter("name");
}

if(request.getParameter("contentPath") !=null){
	contentPath = request.getParameter("contentPath");
}

if(request.getParameter("windowSize") !=null){
	windowSize = request.getParameter("windowSize");
}

if(windowSize.equalsIgnoreCase("large")){
	wSize = "400,585";
	headerImg = "/media_stat/images/layout/pop_up_header_lg.gif";
	tableWidth = "520";
	cellWidth = "275";
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html lang="en-US" xml:lang="en-US">
<head>
<%--     <title> <%=name%></title> --%>
    <fd:SEOMetaTag title="<%=name%>"/> 
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>

<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333"  onLoad="window.focus()">


<A NAME="top"></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="<%=tableWidth%>">
<TR VALIGN="TOP">
	<TD COLSPAN="5" WIDTH="<%=tableWidth%>">
		<A HREF="javascript:window.reallyClose();"><img src="<%=headerImg%>" border="0" alt="freshdirect (close window)"></A><BR>
		<FONT CLASS="space10pix"><BR></FONT>
	</TD>
</TR>
<TR VALIGN="TOP">
	<td height="230">
<!-- content lands here -->



<style type="text/css">
.A {font-family: Verdana, Arial, sans-serif; font-size: 11px; line-height:13px; color:#003300;}
.B {font-family: Verdana, Arial, sans-serif; font-size: 14px; line-height:17px; color:#003300; font-weight: bold}
.C {font-family: Verdana, Arial, sans-serif; font-size: 9px; line-height:17px; color:#003300;}
.D {font-family: Verdana, Arial, sans-serif; font-size: 9px; line-height:13px; color:#003300;}
</style>

<%
FDProductInfo prodInfo=FDCachedFactory.getProductInfo(request.getParameter("sku"));
FDUserI sessionuser = (FDUserI) request.getSession().getAttribute(SessionName.USER);
String term = request.getParameter("term");
String price = "";

if (sessionuser != null){
    price = JspMethods.formatPrice(prodInfo.getZonePriceInfo(sessionuser.getPricingContext().getZoneInfo()).getDefaultPrice()); 
}

if(request.getParameter("sku").equals(FDStoreProperties.getTwoMonthTrailDPSku())) {
	term = "Six-Month";
	price = FDStoreProperties.getTwoMonthTrailDPrice();
}
%>
<A NAME="top"></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="520">

<TR VALIGN="TOP">

<TD WIDTH="520" CLASS="A">

<SPAN CLASS="B">About DeliveryPass Renewals</SPAN><br><br>

We've made it easy to renew your pass and never pay a delivery fee again. Your pass will be renewed when your current pass (or last refill) expires.<br><br>


On the day your pass expires, a new <%=term%> DeliveryPass will automatically be added to your account. We'll bill your last used payment option for the normal price of the <%=term%> DeliveryPass pass and you'll be set for <%=term%> of hassle-free deliveries for just <%=price%>! You may opt out of automatic renewal at any time.<br><br>
<SPAN CLASS="c"><b>DELIVERYPASS TERMS & CONDITIONS</b></SPAN><br>
<SPAN CLASS="D">

<fd:IncludeMedia name="/media/editorial/picks/deliverypass/about_renewals_tc_ar.html">
</fd:IncludeMedia>
</SPAN><br><br>
</TD>
</TR>
</TABLE>



<!-- content ends above here-->
	</td>
</tr>
</table>

<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="520">
<TR VALIGN="TOP">
	<TD WIDTH="275">&nbsp;<A HREF="#top">Back to top</A></TD>
	<TD WIDTH="275" ALIGN="RIGHT">&nbsp;<A HREF="javascript:window.reallyClose();">close window</A></TD>
</TR>
</TABLE>

</BODY>
</HTML>


