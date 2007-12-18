<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
		<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
        <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
</head>

<body <%= "yes".equals(request.getParameter("scroll")) ? "resize=\"yes\"" : "scroll=\"no\""  %> style="padding:0px; margin: 0px;">

<a name="top"></a>
<div class="sub_nav" style="padding-top: 4px;">
	<div class="column" style="padding-left: 8px; padding-top: 10px;"><b>FreshDirect CRM : <tmpl:get name='title'/></b></div>
	<div class="column" style="float: right; padding-right: 8px; padding-top: 10px;"><a href="javascript:window.close();">x Close</a></div><br clear="all">
</div>

<div class="content_scroll" style="height:85%;">
	<tmpl:get name='content'/>
</div>

<div class="sub_nav" style="padding-top: 4px; border-top: 1px solid; border-bottom: none;">
	<div class="column" style="padding-left: 8px; padding-top: 10px;"><a href="#top">Back to top</a></div>
	<div class="column" style="float: right; padding-right: 8px; padding-top: 10px;"><a href="javascript:window.close();">x Close</a></div><br clear="all">
</div>

</body>
</html>
