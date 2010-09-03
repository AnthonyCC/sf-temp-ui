<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %> 
<table width="745" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</tr>

<tr>
	<td valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>

<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td align="center" colspan="3">
		<!-- content lands here -->
		<tmpl:get name='content'/>
		<!-- content ends above here-->
	</td>
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<%-- spacers --%>
<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
	<td><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
	<td><img src="/media_stat/images/layout/clear.gif" height="1" width="733"></td>
	<td><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
	<td bgcolor="#999966"><img src="/media_stat/images/clear.gif" width="1" height="1"></td>
</tr>

<tr valign="bottom">
	<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td width="6" colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
	<td width="733" bgcolor="#999966" valign="bottom"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</tr>
</table>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
