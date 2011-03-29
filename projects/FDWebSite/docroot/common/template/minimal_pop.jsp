<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.resizeTo(585,400); window.focus();" topmargin="10" marginheight="10">
<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="520">
<tr>
	<td colspan="2"><a href="javascript:window.close();"><img src="/media_stat/images/layout/pop_up_header_lg.gif" width="520" height="33" border="0" alt="freshdirect    (close window)"></a></td>
</tr>
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="260" height="10"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="260" height="10"></td>
</tr>
<tr valign="top">
	<td colspan="2" align="center">
<% if (request.getParameter("backTo") != null && !"".equalsIgnoreCase(request.getParameter("backTo"))) { %>
	<div align="left"><a href="<%=request.getParameter("backTo")%>" target="_self">&laquo; Back to previous page</a><br><span class="space8pix"><br></span></div>
<% } %>
	<%-- content lands here --%>
	<tmpl:get name='content'/>
	<%-- content ends above here--%>
	</td>
</tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"></td></tr>
</table>
</center>
</body>
</html>
