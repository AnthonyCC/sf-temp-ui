<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<% //expanded page dimensions
final int W_DNAV_TOTAL = 970;
%>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
	<tmpl:get name="seoMetaTag"/>
	<tmpl:get name='customhead'/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<jwr:script src="/assets/javascript/timeslots.js" useRandomParam="false" />
	<jwr:script src="/giftcards.js" useRandomParam="false" />
  	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  	<%@ include file="/includes/sms_alerts/examples_layout.jspf" %>
	
	<jwr:style src="/giftcards.css" media="all" />
    <jwr:style src="/timeslots.css" media="all" />
	<%
		if ( (request.getRequestURI().indexOf("/your_account/giftcards.jsp")>-1) || (request.getRequestURI().indexOf("/your_account/gc_order_details.jsp")>-1) ) {
			//do nothing
		} else { %>
			<%@ include file="/shared/template/includes/ccl.jspf" %>
	<% } %>
	
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	<jwr:script src="/assets/javascript/jquery.hint.js" useRandomParam="false" />
	<jwr:script src="/assets/javascript/jquery.pwstrength.js" useRandomParam="false" />
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	
	<script type="text/javascript">
        jQuery(function($jq) {
          var pwd = $jq('#password1');
          if (pwd && pwd.pwstrength) {
            pwd.pwstrength();
          }
        });
  	 </script>
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
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
