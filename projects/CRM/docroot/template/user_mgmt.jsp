<%@ taglib uri='template' prefix='tmpl' %>
<%
String pageURI = request.getRequestURI();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/user_mgmt.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
</head>
<body>

<%@ include file="/includes/context_help.jspf" %>

<%try{%>

    <div class="main_nav">
        <jsp:include page='/includes/main_nav.jsp'/>
    </div>

    <div class="side_nav" style="width:27%;">
        <jsp:include page='/includes/user_mgmt_side_nav.jsp'/>
    </div>

    <div class="content" style="width:72%; float: left;">
		<% if (pageURI.indexOf("index") < 0 && pageURI.indexOf("new_user") < 0) { %>
			<div class="content_fixed" style="padding-top: 8px;">
	        	<jsp:include page='/includes/user_mgmt_profile.jsp'/>
	    	</div>
			
			<div class="user_mgmt_profile_nav">
				<jsp:include page='/includes/user_mgmt_profile_nav.jsp'/>
			</div>
		<% } %>

        <tmpl:get name='content'/>
    </div>
	<br clear="all">
	<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
<%}catch (Exception ex){
	ex.printStackTrace();
	throw ex;
}	
%>
</body>
</html>