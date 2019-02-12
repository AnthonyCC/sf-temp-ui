<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType"%>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<html>
	<head>
		<title>Credit Card Info</title>
		<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
		<script language="javascript">
			function closeTimer(timeout) { 
				self.setTimeout("self.close()", timeout); 
			}
		</script>
	</head>
	<body onLoad="closeTimer('300000')">
		<div align="center">
			<div class="main_nav" style="background: #FFFFFF; border-bottom: 2px #000000 solid;">
				<div class="login_header" style="padding: 12px; border-bottom: 1px #000000 solid;">
					<table align="center" cellpadding="0" cellspacing="0">
						<tr valign="bottom">
							<td><img src="/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect"></td>
							<td class="login_header" style="padding-left:14px;">Customer Relationship Management</td>
						</tr>
					</table>
				</div>
			<div>
		</div>
		
		<%String orderId = NVL.apply(request.getParameter("orderId"), "");%>
		
		</table>
		<br>
		<br>
		<div class="main_nav" style="background: #FFFFFF; border-top: 2px #000000 solid;">
			<table>
				<tr>
					<td align="center"><i>this window will automatically close after 5 mins.</i></td>
				</tr>
			</table>
		</div>
	</body>
</html>