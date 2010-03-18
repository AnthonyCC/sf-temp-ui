<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}
		*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
     <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
     <%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<table width="745" border="0" cellpadding="0" cellspacing="0">
<tr>
<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
<td colspan="2" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</tr>

<tr>
<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>

<tr>
<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
<td colspan="4"><%@ include file="/common/template/includes/deptnav.jspf" %></td>
<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td width="745" bgcolor="#999966" colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td bgcolor="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
	<td bgcolor="#E0E3D0"><%@ include file="/common/template/includes/left_side_nav.jspf" %><BR>
		<img src="/media_stat/images/layout/clear.gif" height="1" width="125"></td>
		<td align="center"><img src="/media_stat/images/layout/clear.gif" height="15" width="1"><br>
		<%-- content lands here --%>
		<tmpl:get name='content'/>
		<%-- content ends above here--%>
		<br><br></td>
	<td><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
	<td bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr>

<tr valign="bottom">
<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve_nav.gif" width="6" height="6" border="0"></td>
<td bgcolor="#E0E3D0"><img src="/media_stat/images/layout/clear.gif" width="125" height="5" border="0"></td>
<td ><img src="/media_stat/images/layout/clear.gif" width="608" height="5" border="0"></td>
<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</tr>

<tr>
<td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
</table>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
