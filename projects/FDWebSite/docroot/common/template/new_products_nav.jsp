<%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.fdstore.util.SiteFeatureHelper" 
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>

<% //expanded page dimensions
final int W_NEW_PRODUCTS_NAV_TOTAL = 970;
%>


<html>  
<head>
    <title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>

	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<fd:css href="/assets/css/ccl.css"/>

<%
final String trk = "srch"; // tracking code
String criteria = request.getParameter("searchParams");

%>
<fd:javascript src="/assets/javascript/rounded_corners.inc.js"/>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<%@ include file="/includes/search/autocomplete.jspf" %>
<center class="text10">
<table width="<%=W_NEW_PRODUCTS_NAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="<tmpl:get name='colleftwidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="<tmpl:get name='colRightWidth'/>" border="0" alt=""></td>
	</tr>
	<tr>
		<td width="<tmpl:get name='colLeftWidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="<tmpl:get name='colRightWidth'/>" border="0" alt=""></td>

	</tr>
	<tr>
		<td width="<%=W_NEW_PRODUCTS_NAV_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
	</tr>
	<tr>
		<!-- header_1 lands here -->
		<tmpl:get name='header_1'/>
		<!-- header_1 ends above here-->
	</tr>
	<tmpl:get name='header_seperator'/>
	<tr valign="top">
		<tmpl:get name='categoryPanel'/>
		<td>
			<!-- header_2 lands here -->
			<tmpl:get name='header_2'/>
			<!-- header_2 ends above here-->
			<!-- featured lands here -->
			<tmpl:get name='featured'/>
			<!-- featured ends above here-->
			<!-- content lands here -->
			<tmpl:get name='content'/>
			<!-- content ends above here-->
		</td>
		<tmpl:get name='rightNav'/>
	</tr>
	<tr>
		<td width="<tmpl:get name='colLeftWidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="1" alt=""></td>
	</tr>
	<!-- banner2 lands here -->
	<tmpl:get name='banner2'/>
	<!-- banner2 ends above here-->
	<tr valign="bottom">
		<td width="<tmpl:get name='colLeftWidth'/>"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="5" border="0" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
	</tr>
</table>
</center>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>
