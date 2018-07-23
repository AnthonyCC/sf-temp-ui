<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
<%--     <title><tmpl:get name='title'/></title> --%>
    <tmpl:get name="seoMetaTag"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <fd:javascript src="/assets/javascript/common_javascript.js"/>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.focus();" topmargin="0" marginheight="0">
<table border="0" cellpadding="0" cellspacing="0" width="330">
<tr valign="top">
	<td><a href="javascript:try { window.reallyClose(); window.parent.FreshDirect.components.ifrPopup.close(); } catch (e) {}"><img src="/media_stat/images/template/pop_up_header_sm.gif" alt="FreshDirect" width="330" height="43" border="0"></a><br>
	<font class="space2pix"><br></font></td>
</tr>
<tr>
	<td valign="top" align="center"><tmpl:get name='content'/></td>
</tr>
</table>
</body>
</html>
