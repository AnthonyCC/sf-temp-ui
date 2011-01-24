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
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_iframe.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_overtwo.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/iframecontentmws.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_scroll.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_shadow.js"></script>
	<script type="text/javascript" language="javascript" src="/ccassets/javascript/overlibmws_draggable.js"></script>

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

				<div style="overflow: auto; border: 0px solid #000;">
					<div class="side_nav">
						<jsp:include page='/includes/supervisor_nav.jsp'/>
						

					</div>
					
					<div style="width: 84%; overflow-y: auto; float: right;">
						<tmpl:get name="content"/>
					</div>
				</div>
			</div>
			<div class="footer" style="clear: both;"><jsp:include page='/includes/copyright.jsp'/></div>
		<% } catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} %>
	</div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>