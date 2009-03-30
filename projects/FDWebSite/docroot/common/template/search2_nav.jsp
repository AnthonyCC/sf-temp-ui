<%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.fdstore.util.SiteFeatureHelper" 
%><%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
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
<%
}
%>
<%@ include file="/includes/search/autocomplete.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<TABLE WIDTH="745" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
	<TD VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></TD>
	<TD WIDTH="165"><img src="/media_stat/images/layout/clear.gif" height="1" width="165" border="0" alt=""></TD>
	<TD width="568" align="center"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="568" HEIGHT="1" alt=""></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></TD>
	<TD VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
</TR>
<TR>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0" alt=""></td>
	<td width="733" COLSPAN="2" valign="top" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt=""></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0" alt=""></td>
</TR>
<TR>
	<td width="733" COLSPAN="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></td>
</TR>
<TR>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34" alt=""></TD>
	<TD WIDTH="743" COLSPAN="4">
		<form name="adv_search" id="adv_search" method="GET">
		<table width="743" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<div style="margin:15px;margin-bottom:0px;padding-bottom:15px;border-bottom: 4px solid #FF9933">
					<table><tr><td style="padding-right:35px;"><img src="/media_stat/images/template/search/search_deptnav_2.gif" style="border: none;" alt=""></td>
					<td style="vertical-align:bottom;"><div id="searchContainer" style="position: relative"><INPUT TYPE="text" style="width:140px;" id="searchxParams" name="searchParams" value="<%= criteria %>" size="16" maxlength="50" class="text11">
						<div id="terms" style="position: absolute;background-color: white"></div>
					</div></td>			
					<td style="vertical-align:bottom"><input name="submit" type="image" src="/media_stat/images/template/search/search_find_button.gif" style="border:none;margin-bottom:2px;"></td>
					<td><div id="searchDebug"></div></td>
				</tr></table>
				</div>
				</td>
			</tr>
		</table>
		</form>
	</TD>
	<TD WIDTH="1" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="34" alt=""></TD>
</TR>
<TR VALIGN="TOP">
	<TD WIDTH="1" BGCOLOR="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" border="0" alt=""></td>
	<TD WIDTH="170" COLSPAN="2">		
		<tmpl:get name='categoryPanel'/>		
		<BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="170" alt="">
	</TD>
	<td width="568" valign="top">
	<div style="width: 529px;padding-left: 15px;">		
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->		
	</div>
	<BR><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="568" HEIGHT="1" BORDER="0" alt=""><BR>	
	<br><br></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
</TR>
<TR>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></TD>
	<TD WIDTH="165"><img src="/media_stat/images/layout/clear.gif" height="1" width="1" alt=""></TD>
	<TD width="568" align="center"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
	<TD WIDTH="5" VALIGN="BOTTOM"><img src="/media_stat/images/layout/clear.gif" height="1" width="5" alt=""></TD>
	<TD BGCOLOR="#999966" VALIGN="BOTTOM" WIDTH="1"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1" alt=""></TD>
</TR>
<TR VALIGN="BOTTOM">
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0" alt=""></td>
	<TD WIDTH="165"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt=""></TD>
	<td width="568"><img src="/media_stat/images/layout/clear.gif" width="568" height="5" border="0" alt=""></td>
	<td WIDTH="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0" alt=""></td>
</TR>
<TR>
	<td width="733" COLSPAN="2" BGCOLOR="#999966" VALIGN="BOTTOM"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt=""></td>
</TR>
</TABLE>

<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>

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
</BODY>
</HTML>
