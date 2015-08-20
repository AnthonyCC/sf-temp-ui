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
			<jsp:include page='/includes/supervisor_nav.jsp'/>
			
			<tmpl:get name="content"/>
		<% } catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} %>
	</div>
	<div class="crm_globalfooter"><jsp:include page='/includes/copyright.jsp'/></div>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000"></div>
</body>
</html>