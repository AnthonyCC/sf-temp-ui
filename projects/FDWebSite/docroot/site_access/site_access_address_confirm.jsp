<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
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
					<%-- IF ELIGIBLE FOR COS, ENTER FROM HOME ZIP ADDRESS CHECK --%>
					The address you entered is not currently eligible for FreshDirect home delivery service but you can get <a href="javascript:popup('/cos_info.jsp','small')">FreshDirect At The Office</a>, our corporate delivery service.
					<br><br>
					Street Address, apartment<br>
					City, State ZIP code
					<br><br>
					If this is in fact a residential address and you would like our home delivery service instead, please call <b><%=user.getCustomerServiceContact()%></b>.
					<br><br>
					<img src="/media_stat/images/template/site_access/continue_to_store.gif" width="124" height="16"><br>
					<%-- ELSE IF ELIGIBLE FOR BOTH HOME AND COS --%>
					<br>The address you entered is eligible for either home delivery or <a href="javascript:popup('/cos_info.jsp','small')">FreshDirect At The Office</a>, our corporate service.
					<br><br>
					Street Address, apartment<br>
					City, State ZIP code
					<br><br>
					Please select the type of delivery you would like to receive at this address. 
					If you have any questions, please call <%=user.getCustomerServiceContact()%>.
					<br><br>
					<img src="/media_stat/images/template/site_access/i_want_home.gif" width="194" height="16" border="0" vspace="10" alt="I WANT HOME DELIVERY"><br>
					<img src="/media_stat/images/template/site_access/i_want_cos.gif" width="194" height="16" border="0" vspace="10" alt="I WANT FRESHDIRECT AT THE OFFICE">
					<%-- END IF --%>
					</td>
				</tr>
					
				<tr>
					<td align="center" class="text12">
						<img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
						<img src="/media_stat/images/layout/999966.gif" width="400" height="1" border="0" vspace="8"><br>
						<br><a href="/login/login_main.jsp"><img src="/media_stat/images/template/site_access/current_customers.gif" width="113" height="11" border="0" alt="Current Customers"><br>Sign in here</a><br><br><br>
							<%@ include file="/shared/template/includes/copyright.jspf"%>
						<br><br>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
