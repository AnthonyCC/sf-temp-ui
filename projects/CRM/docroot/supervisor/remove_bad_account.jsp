<%@ page import="java.net.URLEncoder" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<html>
	<head>
		<title>Remove Bad Account</title>
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
		<%
			String restrictedPaymentMethodId = request.getParameter("restrict_payment_method_id");
		%>		
		<crm:CrmGetRestrictedPaymentMethod id="restrictedPaymentMethodModel" restrictedPaymentMethodId="<%=restrictedPaymentMethodId%>">
		<crm:CrmRestrictedPaymentMethodController restrictedPaymentMethod="<%=restrictedPaymentMethodModel%>" successPage="/main/close_window.jsp?reloadParent=true" result="result" actionName="delete">
		<table>
			<form name="remove_bad_account" method="POST">
			<tr>
				<td>&nbsp;</td>
				<td><fd:ErrorHandler result="<%= result %>" name="authentication" id="error"><span class="error_detail"><%=error%></span></fd:ErrorHandler></td>
				<td>&nbsp;</td>
			</tr>
			<tr>			
				<td>&nbsp;</td>
				<td><fd:ErrorHandler result="<%= result %>" name="technical_difficulty" id="error"><span class="error_detail"><%=error%></span></fd:ErrorHandler></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b>Please re-enter your password:</b> </td>
				<td><input type="password" class="input_text" style="width: 150px;" tabindex="1" name="password"></td>
				<td><input type="submit" value="YES, RE-VALIDATE ACCOUNT" class="submit" name="submit" style="width: 180px;" tabindex="2"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><fd:ErrorHandler result="<%= result %>" name="password" id="error"><span class="error_detail"><%=error%></span></fd:ErrorHandler></td>
				<td>&nbsp;</td>
			</tr>
		</table>
			<%if(restrictedPaymentMethodModel != null){%>
				<table>
					<tr>
						<td align="left"><b>Account Number:</b></td>
						<td><%=restrictedPaymentMethodModel.getAccountNumber()%></td>
					</tr>
					<tr>
						<td align="left"><b>Bank ABA Route Number:</b></td>
						<td><%=restrictedPaymentMethodModel.getAbaRouteNumber()%></td>
					</tr>
					<tr>
						<td align="left"><b>Bank Account Type:</b></td>
						<td><%= (restrictedPaymentMethodModel.getBankAccountType() != null) ? restrictedPaymentMethodModel.getBankAccountType().getDescription(): "&nbsp;" %></td>
					</tr>
					<tr>
						<td align="left"><b>Name On Account:</b></td>
						<td><%=restrictedPaymentMethodModel.getFirstName()%> <%=restrictedPaymentMethodModel.getLastName()%></td>
					</tr>
				</table>
			<%}%>
			</form>
		</table>
		</crm:CrmRestrictedPaymentMethodController>
		</crm:CrmGetRestrictedPaymentMethod>
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