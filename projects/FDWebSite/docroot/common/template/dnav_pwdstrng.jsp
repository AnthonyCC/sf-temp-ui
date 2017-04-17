<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DNAV_TOTAL = 970;
%>

<!DOCTYPE html>
<html>
<head>
	<tmpl:get name="seoMetaTag"/>
	<tmpl:get name='customhead'/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<fd:javascript src="/assets/javascript/timeslots.js" />
	<fd:javascript src="/assets/javascript/FD_GiftCards.js" />
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  	<%@ include file="/includes/sms_alerts/examples_layout.jspf" %>
	<fd:css href="/assets/css/giftcards.css"/>
    <fd:css href="/assets/css/timeslots.css"/>
	<%
		if ( (request.getRequestURI().indexOf("/your_account/giftcards.jsp")>-1) || (request.getRequestURI().indexOf("/your_account/gc_order_details.jsp")>-1) ) {
			//do nothing
		} else { %>
			<%@ include file="/shared/template/includes/ccl.jspf" %>
	<% } %>

<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	<script type="text/javascript">
        jQuery(function($jq) { $jq('#password1').pwstrength(); });
  	 </script>
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<CENTER class="text10">
<TABLE WIDTH="<%=W_DNAV_TOTAL%>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
<td width="<%=W_DNAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
</TR>

<TR>
<TD WIDTH="<%=W_DNAV_TOTAL%>">
<%@ include file="/common/template/includes/deptnav.jspf" %></TD>
</TR>
<TR>
<TD WIDTH="<%=W_DNAV_TOTAL%>" BGCOLOR="#999966" COLSPAN="7"><IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="1" HEIGHT="1"></TD>
</TR>

<TR VALIGN="TOP">
<TD width="<%=W_DNAV_TOTAL%>" align="center">
<img src="/media_stat/images/layout/clear.gif" alt="" height="20" width="<%=W_DNAV_TOTAL%>"><br>
<!-- content lands here -->
<tmpl:get name='content'/>
<!-- content ends above here-->
<br><br></TD>
</TR>

<%-- spacers --%>
<tr valign="top">
	<td><img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_DNAV_TOTAL%>"></td>
</tr>

<TR VALIGN="BOTTOM">
<td width="<%=W_DNAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
</TR>
</TABLE>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
