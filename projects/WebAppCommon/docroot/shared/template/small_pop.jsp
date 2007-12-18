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
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.resizeTo(375,335); window.focus();" topmargin="8" marginheight="8">
<%-- don't resize onLoad, tmpl used for plant tour --%>
<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="330">
<tr>
<td colspan="2"><a href="javascript:window.close();"><img src="/media_stat/images/layout/pop_up_header_sm.gif" width="330" height="43" border="0" alt="FreshDirect     (close window)"></a></td>
</tr>
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="165" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="165" height="8"></td>
</tr>
<tr><td colspan="2" align="center">
<%-- content lands here --%>
<tmpl:get name='content'/>
<%-- content ends above here--%>
</td></tr>
<tr>
<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
</tr>
<tr>
<td><a href="#top">Back to top</a></td>
<td align="right"><a href="javascript:window.close();">Close window</a></td>
</tr>
</table>
</center>
</body>
</html>
