<%@ taglib uri='template' prefix='tmpl' %>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(servletContext.length(), pageURI.lastIndexOf("/")+1);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<link rel="stylesheet" href="<%= servletContext %>/common/css/store_admin.css" type="text/css">
	<script language="Javascript" src="<%= servletContext %>/common/javascript/common.js"></script>
</head>
<body class="basecolor" scroll="no" style="font-size: 8pt;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr class="popHeader">
<td width="1%"><img src="images/clear.gif" width="1" height="25"></td>
<td width="92%"><tmpl:get name='header'/></td>
<td width="6%" align="center"><a href="javascript:window.close();"><b>X</b></a></td>
<td width="1%">&nbsp;</td>
</tr>

<% if ( pageURI.indexOf("add_domain") < 0 ) { %>
<tr><td colspan="4" class="separator"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td colspan="4"><img src="images/clear.gif" width="1" height="6"></td></tr>
<tr>
<td><img src="images/clear.gif" width="1" height="1"></td>
<td colspan="2">&nbsp;<tmpl:get name='direction'/>: <tmpl:get name='path'/></td>
<td>&nbsp;</td>
</tr>
<% } %>

<tr><td colspan="4"><img src="images/clear.gif" width="1" height="6"></td></tr>
<tr><td colspan="4" class="separator"><img src="images/clear.gif" width="1" height="1"></td></tr>
</table>

<tmpl:get name='content'/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
<tr><td colspan="4" class="separator"><img src="images/clear.gif" width="1" height="1"></td></tr>
<tr><td colspan="4"><img src="images/clear.gif" width="1" height="8"></td></tr>
<tr class="popHeader">
<td width="1%">&nbsp;</td>
<td width="92%" align="center"><tmpl:get name='button'/></td>
<td width="6%" align="center"><a href="javascript:window.close();"><b>X</b></a></td>
<td width="1%">&nbsp;</td>
</tr>
</table>

</body>
</html>
