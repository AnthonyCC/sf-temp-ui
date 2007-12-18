<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Customer > Delivery Zone</tmpl:put>

<tmpl:put name='content' direct='true'>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
AddressModel address = user.getAddress();
%>
<div class="content_scroll">
<div align="center">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
	<head>
		<title>FreshDirect</title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
		<div align="center"><br>
			<table border="0" cellspacing="0" cellpadding="0" width="420">
				<tr>
					<td align="center" class="text12">
					<img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" alt="FreshDirect" border="0">
					<br><img src="/media_stat/images/layout/999966.gif" width="400" height="1" border="0" vspace="8"><br>
					The address you entered is not currently eligible for FreshDirect At the office delivery service but you can get, our home delivery service.
					<br><br>
					<%=address.getAddress1()%>, <%=address.getApartment()%><br>
					ZIP <%=address.getZipCode()%>
					<br><br>
					<a href="/main/index.jsp"><img src="/media_stat/images/template/site_access/continue_to_store.gif" width="124" height="16" border="0"></a><br>
					</td>
				</tr>
					
				<tr>
					<td align="center" class="text12">
						<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
						<img src="/media_stat/images/layout/999966.gif" width="400" height="1" border="0" vspace="8"><br>
						<br><a href="/main/index.jsp"><img src="/media_stat/images/template/site_access/current_customers.gif" width="113" height="11" border="0" alt="Current Customers"><br>Sign in here</a><br><br><br>
						<br><br>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
</div>
</div>
</tmpl:put>
</tmpl:insert>
