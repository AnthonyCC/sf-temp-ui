<%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.fdstore.util.SiteFeatureHelper" 
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>
<html>  
<head>
	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <title><tmpl:get name='title'/></title>

    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<link rel="stylesheet" href="/assets/yui/container/assets/container.css" type="text/css">
	<link rel="stylesheet" href="/assets/css/ccl.css" type="text/css">

    <script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>

<%
final String trk = "srch"; // tracking code
String criteria = request.getParameter("searchParams");

if (FDStoreProperties.isCclAjaxDebugClient()) { 
	// debug JS libs
%>
	<script type="text/javascript" src="/assets/javascript/rounded_corners.inc.js"></script>
<%
} else {
	// production JS libs
%>
	<script type="text/javascript" src="/assets/javascript/rounded_corners-min.js"></script>
<% } %>
<%@ include file="/includes/search/autocomplete.jspf" %>
</head>
<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10">
<center>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<table width="745" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td width="<tmpl:get name='colleftwidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="<tmpl:get name='colRightWidth'/>" border="0" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
	</tr>
	<tr>
		<td valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td width="<tmpl:get name='colLeftWidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="<tmpl:get name='colRightWidth'/>" border="0" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
	</tr>
	<tr>
		<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0" alt=""></td>
		<td width="733" colspan="2" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt=""></td>
		<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0" alt=""></td>
	</tr>
	<tr>
		<td width="733" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
	</tr>
	<tr>
		<td width="1" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="34" alt=""></td>
		<!-- header_1 lands here -->
		<tmpl:get name='header_1'/>
		<!-- header_1 ends above here-->
		<td width="1" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="34" alt=""></td>
	</tr>
	<tmpl:get name='header_1_seperator'/>
	<tr valign="top">
		<td width="1" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt=""></td>
		<tmpl:get name='categoryPanel'/>
		<td colspan="2">
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
		<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" alt=""></td>
	</tr>
	<tr>
		<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td width="<tmpl:get name='colLeftWidth'/>" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" height="1" width="1" alt=""></td>
		<td width="5" valign="bottom"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></td>
		<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" alt=""></td>
	</tr>
	<!-- banner2 lands here -->
	<tmpl:get name='banner2'/>
	<!-- banner2 ends above here-->
	<tr valign="bottom">
		<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0" alt=""></td>
		<td width="<tmpl:get name='colLeftWidth'/>"><img src="/media_stat/images/layout/clear.gif" width="<tmpl:get name='colLeftWidth'/>" height="5" border="0" alt=""></td>
		<td width="<tmpl:get name='colRightWidth'/>"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
		<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0" alt=""></td>
	</tr>
	<tr>
		<td width="733" colspan="2" bgcolor="#999966" valign="bottom"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt=""></td>
	</tr>
</table>
<%@ include file="/common/template/includes/footer.jspf" %>
</center>

<script type="text/javascript">
	if (document.getElementById('categoryPanel')) {
		var full_settings = {
	        tl: { radius: 6 },
	        tr: { radius: 6 },
	        bl: { radius: 6 },
	        br: { radius: 6 },
	        topColour: "#999967",
	        bottomColour: "#E0E3D0",
	        antiAlias: true,
	        autoPad: false
	    };
	
	    var fullCorn = new curvyCorners(full_settings, document.getElementById('categoryPanel'));
	    fullCorn.applyCornersToAll();
	}
</script>
</body>
</html>
