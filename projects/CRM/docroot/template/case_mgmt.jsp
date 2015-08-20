<%@ taglib uri='template' prefix='tmpl' %>
<%
String pageURI = request.getRequestURI();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect CRM : <tmpl:get name='title'/> /</title>
    
    <%@ include file="/common/template/includes/i_stylesheets.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>

</head>
<body>
	<div class="crm_globalnav">
		<jsp:include page="/includes/main_nav.jsp"/>
	</div>
	<%-- header on top and content below --%>
	<div class="content">
	<% try { %>
		<%-- header on top and groups/usernames in the leftnav --%>

		<%-- header on top and content below --%>

		
		<jsp:include page='/includes/customer_header.jsp'/>
		
		<jsp:include page='/includes/case_header.jsp'/>

		<div style="overflow: auto; border: 0px solid #000;">
			<div class="side_nav">
				<jsp:include page='/includes/case_mgmt_side_nav.jsp'/>
				

				<div class="side_nav_module note" style="padding-left: 6px; margin-bottom: 0;">
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
				
			<div style="width: 84%; overflow-y: auto; float: right;">
				<tmpl:get name="content"/>
			</div>
		</div>
	<% } catch (Exception ex) {
		ex.printStackTrace();
		throw ex;
	} %>
	</div>
		
	<div class="crm_globalfooter"><jsp:include page='/includes/copyright.jsp'/></div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>

</body>
</html>