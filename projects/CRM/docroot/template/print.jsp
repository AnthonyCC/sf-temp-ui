<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
		<link rel="stylesheet" type="text/css" href="/assets/css/giftcards.css" />
		<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
        <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
</head>
<body style="padding:0px; margin: 0px;">

<a name="top"></a>
<table width="100%" cellpadding="5">
	<tr>
		<td>
			<b><tmpl:get name='title'/></b>
		</td>
		<td align="right" class="popup">
			<a href="javascript:window.print();">Print page</a> &nbsp;|&nbsp; <a href="javascript:window.close();">x Close</a>
		</td>
	</tr>
</table>

<tmpl:get name='content'/>

<table width="100%" cellpadding="5" class="popup">
<tr><td><a href="#top">^ Back to top</a></td><td align="right"><a href="javascript:window.close();">x Close</a></td></tr>
</table>

</body>
</html>
