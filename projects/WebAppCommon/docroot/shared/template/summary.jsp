<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
        <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.focus();" topmargin="10" marginheight="10">
<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" align="center" class="text15"><img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38"><br><span class="title16"><tmpl:get name='pageTitle'/></span><br>INTERNAL USE ONLY</td>
</tr>
<tr>
</tr>
<tr valign="top">
	<td colspan="3" align="center">
	<%-- content lands here --%>
	<tmpl:get name='content'/>
	<%-- content ends above here--%>
	</td>
</tr>
<tr><td colspan="3" align="center" class="text11" style="color: #999999;"><br><%@ include file="/shared/template/includes/copyright.jspf" %><span class="space8pix"><br><br></span>
All content is the property of FreshDirect or its content suppliers and<br>may not be reproduced in any form without prior written permission.<br><br>
</td></tr>
<%--tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"></td></tr>
<tr>
	<td width="33%"><a href="#top">Back to top</a></td>
	<td align="center" width="34%"><a href="javascript:window.print();">Print page</a></td>
	<td align="right" width="33%"><a href="javascript:window.close();">Close window</a></td>
</tr--%>
</table>
</center>
</body>
</html>
