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
	<div class="crm_container">
		<% try { %>
			<%-- header on top and groups/usernames in the leftnav --%>
			<div class="content">
			<%@ include file="/includes/context_help.jspf" %>

				<%-- header on top and content below --%>

				<jsp:include page="/includes/main_nav.jsp"/>
				
				<jsp:include page='/includes/customer_header.jsp'/>
				
				<jsp:include page='/includes/case_header.jsp'/>

				<div style="height: 95%; overflow: auto; border: 0px solid #000;">
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
					
					<div style="width: 84%; overflow-y: auto; float: left;">
						<tmpl:get name="content"/>
					</div>
				</div>
			</div>
			<div class="footer"><jsp:include page='/includes/copyright.jsp'/></div>
		<% } catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} %>
	</div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>