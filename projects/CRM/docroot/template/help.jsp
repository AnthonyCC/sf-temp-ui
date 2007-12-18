<%@ taglib uri='template' prefix='tmpl' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ CRM Help: <tmpl:get name="title"/> /</title>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
</head>
<body bgcolor="#FFFFFF" style="background: #FFFFFF; margin: 0px; padding: 0px;" onLoad="focus()">
<% String show = request.getParameter("show"); %>
<%@ include file="/includes/context_help.jspf" %>
<%try{%>
	
	<tmpl:get name="content"/>
	
<%}catch (Exception ex){
	ex.printStackTrace();
	throw ex;
}
%>
</body>
</html>