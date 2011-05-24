<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
<head>
	<title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
<%
	{
		String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
		if (onbeforeunload != null && onbeforeunload.length() > 0) {
	%>
			<script language="javascript">
				window.onbeforeunload = <%= onbeforeunload %>;
			</script>
	<%
		} // if
	} // local block
%>

</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" class="text10" 
	onload="<%= request.getAttribute("bodyOnLoad")%>" 
	onunload="<%= request.getAttribute("bodyOnUnload")%>" >
<%
	//
	// annotation mode, add overlib stuff
	//
	if (FDStoreProperties.isAnnotationMode()) {
%>
		<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		<script language="JavaScript" src="/assets/javascript/overlib_mini.js"></script>
<%	} %>
	<center>
		<%@ include file="/common/template/includes/globalnav.jspf" %> 
		<table width="745" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0" alt="" /></td>
				<td width="125"><img src="/media_stat/images/layout/clear.gif" width="125" height="1" border="0" alt="" /></td>
				<td width="458"><img src="/media_stat/images/layout/clear.gif" width="458" height="1" border="0" alt="" /></td>
				<td width="150"><img src="/media_stat/images/layout/clear.gif" width="150" height="1" border="0" alt="" /></td>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0" alt="" /></td>
				<td width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td>
			</tr>
			<tr><td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0" alt="" /></td>
				<td width="733" colspan="3" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt="" /></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="733" colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
			</tr>
			<tr>
				<td width="1" bgcolor="#999966"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="34" alt="" /></td>
				<td width="743" colspan="5">
				<%@ include file="/common/template/includes/deptnav.jspf" %>
				</td>
				<td width="1" bgcolor="#999966"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="34" alt="" /></td>
			</tr>
			<tr>
				<td width="745" bgcolor="#999966" colspan="7"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr valign="top">
				<td bgcolor="#999966" valign="bottom" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></td>
				<td width="5" bgcolor="#E0E3D0"><br /></td>
				<td width="125" bgcolor="#E0E3D0">
					<img src="/media_stat/images/layout/clear.gif" height="1" width="125" alt="" />
					<!-- left nav, manual start -->
					<tmpl:get name='left_nav_manual'/><br />
					<!-- left nav, manual end -->
					<img src="/media_stat/images/layout/clear.gif" height="1" width="125" alt="" />
				</td>
				<td width="458" align="center">
					<img src="/media_stat/images/layout/clear.gif" width="458" height="15" alt="" /><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
					<br /><br />
				</td>
				<td width="155" colspan="2" align="center">
					<img src="/media_stat/images/layout/clear.gif" height="10" width="1" border="0" alt="" /><br />
					<%@ include file="/common/template/includes/right_side_nav.jspf" %>
				</td>
				<td bgcolor="#999966" valign="bottom" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr valign="bottom">
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve_nav.gif" width="6" height="6" border="0" alt="" /></td>
				<td width="125" bgcolor="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
				<td width="608" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" alt="" /></td>
				<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0" alt="" /></td>
			</tr>
			<tr>
			<td width="733" colspan="3" bgcolor="#999966" valign="bottom"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0" alt="" /></td>
			</tr>
		</TABLE>
	<%@ include file="/common/template/includes/footer.jspf" %>
	</center>
</body>
</html>
