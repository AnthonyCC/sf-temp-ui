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
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.resizeTo(650,700); window.focus();" topmargin="10" marginheight="10">
<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="600">
<tr valign="bottom">
	<td><a href="javascript:window.close();"><img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect"></a></td>
	<td align="right"><a href="javascript:window.print();" onMouseover="swapImage('PRINT_IMG','/media_stat/images/navigation/global_nav/print_page_01.gif')" onMouseout="swapImage('PRINT_IMG','/media_stat/images/navigation/global_nav/print_page.gif')"><img src="/media_stat/images/navigation/global_nav/print_page.gif" width="54" height="26" border="0" name="PRINT_IMG"></a></td>
</tr>
<tr>
<td><img src="/media_stat/images/layout/cccccc.gif" width="300" height="1" vspace="6"></td>
<td><img src="/media_stat/images/layout/cccccc.gif" width="300" height="1" vspace="6"></td>
</tr>
<tr valign="top">
	<td colspan="2" align="center">
	<%-- content lands here --%>
	<tmpl:get name='content'/>
	<%-- content ends above here--%>
	</td>
</tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"></td></tr>
<tr>
	<td><a href="#top">Back to top</a></td>
	<td align="right"><a href="javascript:window.close();">Close window</a></td>
</tr>
</table>
</center>
</body>
</html>
