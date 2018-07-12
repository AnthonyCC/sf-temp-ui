<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
AddressModel address = null;
if (user != null) { user.getAddress(); }
String successPage = "/index.jsp";
String loginlink = "/login/login_main.jsp";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
	<head>
		<%-- <title>FreshDirect</title> --%>
         <fd:SEOMetaTag title="FreshDirect"/>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		
		<%
	if("slite".equals(request.getParameter("referrer_page"))) {
	%>
		<script>
			function resizeFrame() {
				setFrameHeightSL('signupframe', 600);
				setFrameWidthSL('signupframe',750);
				window.parent.document.getElementById('MB_window').style.left=200 + 'px';
				window.parent.document.getElementById('MB_window').style.width=780 + 'px';
			}
			
			//window.onload = resizeFrame();
		</script>
	<%
		successPage = "#\" onclick=\"window.top.location=\'/index.jsp\'";
		loginlink = "#\" onclick=\"window.top.location=\'/login/login_main.jsp\'";
	}
	%>
	
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
	<body bgcolor="#ffffff" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
			<div align="center"><br />
			<table border="0" cellspacing="0" cellpadding="0" width="420">
				<tr>
					<td align="center" class="text12">
					<img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" alt="FreshDirect" border="0">
					<br /><img src="/media_stat/images/layout/999966.gif" alt="" width="400" height="1" border="0" vspace="8"><br />
					The address you entered is not currently eligible for FreshDirect home delivery service but you can get <a href="javascript:popup('/cos_info.jsp','small')">FreshDirect At The Office</a>, our corporate delivery service.
					<br /><br />
					<% if (address != null) { %>
						<%=address.getAddress1()%>, <%=address.getApartment()%><br />
						ZIP <%=address.getZipCode()%>
					<% } %>
					<br /><br />
					If this is in fact a residential address and you would like our home delivery service instead, please call <b><%=(user == null)?"1-212-796-8002":user.getCustomerServiceContact()%></b>.
					<br /><br />
					<a href="<%= successPage %>"><img src="/media_stat/images/template/site_access/continue_to_store.gif" width="124" height="16" border="0"></a><br />
					</td>
				</tr>
					
				<tr>
					<td align="center" class="text12">
						<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"><br />
						<img src="/media_stat/images/layout/999966.gif" alt="" width="400" height="1" border="0" vspace="8"><br />
						<br /><a href="<%= loginlink %>"><img src="/media_stat/images/template/site_access/current_customers.gif" width="113" height="11" border="0" alt="Current Customers"><br />Sign in here</a><br /><br /><br />
							<%@ include file="/shared/template/includes/copyright.jspf"%>
						<br /><br />
					</td>
				</tr>
			</table>
		</div>
		<%
	if("slite".equals(request.getParameter("referrer_page"))) {
	%>
		<script>
			window.onload = resizeFrame();
		</script>
	<%
	}
%>
	</body>
</html>
