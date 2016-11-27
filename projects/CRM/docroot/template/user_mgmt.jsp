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
	<div class="crm_container">
		<% try { %>
			<%-- header on top and groups/usernames in the leftnav --%>
			<div class="content">
			<%@ include file="/includes/context_help.jspf" %>

				<%-- header on top and content below --%>

				<jsp:include page="/includes/main_nav.jsp"/>
				
				<div style="height: 95%; overflow: auto; border: 0px solid #000;">
					<div class="side_nav" style="width:27%;">
						<jsp:include page='/includes/user_mgmt_side_nav.jsp'/>
					</div>
					
					<div style="width: 72%; overflow-y: auto; float: left;">
						<% if (pageURI.indexOf("index") < 0 && pageURI.indexOf("new_user") < 0) { %>
							<div class="content_fixed" style="padding-top: 8px;">
								<jsp:include page='/includes/user_mgmt_profile.jsp'/>
							</div>
							
							<div class="user_mgmt_profile_nav">
								<jsp:include page='/includes/user_mgmt_profile_nav.jsp'/>
							</div>
						<% } %>
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