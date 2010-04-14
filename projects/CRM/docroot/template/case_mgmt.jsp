<%@ taglib uri='template' prefix='tmpl' %>
<%
String pageURI = request.getRequestURI();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case_mgmt.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
    <link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>

</head>
<body>
<div class="tophelp1">
<%@ include file="/includes/context_help.jspf" %>

<%try{%>
    <%-- header on top and groups/usernames in the leftnav --%>

    <div class="main_nav">
        <jsp:include page='/includes/main_nav.jsp'/>
    </div>
	
	<jsp:include page='/includes/case_header.jsp'/>
	
    <div class="side_nav">
        <jsp:include page='/includes/case_mgmt_side_nav.jsp'/>
		<div class="note" style="padding-left: 6px;">
		<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
		<b>P</b> <span class="legend">PRIORITY:<br>
		<img src="/media_stat/crm/images/clear.gif" width="1" height="4"><br>
		<img src="/media_stat/crm/images/priority_hi.gif" width="11" height="11">&nbsp;&nbsp;HIGH<br>
		<img src="/media_stat/crm/images/clear.gif" width="1" height="4"><br>
		<img src="/media_stat/crm/images/priority_md.gif" width="11" height="11">&nbsp;&nbsp;MEDIUM<br>
		<img src="/media_stat/crm/images/clear.gif" width="1" height="4"><br>
		<img src="/media_stat/crm/images/priority_lo.gif" width="11" height="11">&nbsp;&nbsp;LOW
		</span>
		<br><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
		Idle time in minutes,<br>
		-- closed<br>
		&nbsp;
		<br>
		&nbsp;
		</div>
    </div>
	
    <div class="content_bgcolor">
    <tmpl:get name='content'/>
    </div>
</div>

	<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
<%}catch (Exception ex) {
	ex.printStackTrace();
	throw ex;
}
%>
</body>
</html>