<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<SCRIPT LANGUAGE="JavaScript"> 
	function doEnter() { 
		document.getElementById("process").innerText="Processing....Please wait.";
		document.getElementById("process").style.color="#FF0000";
		document.getElementById("process").style.fontWeight="bold";
	} 
	function openURL(url) {
	    opener.location.href = url;
	    self.close();

	}
</script>
<html>
	<head>
		<title>Account Lookup</title>
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
		String acctNum = NVL.apply(request.getParameter("acctNum"), "");
		String lookupLoc = NVL.apply(request.getParameter("lookupLoc"), "1");

	%>
		
		<crm:LookupAcctController result="result">
		<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>
		<fd:ErrorHandler result='<%= result %>' name='lookupfailure' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>
			<form name="checkPassword" method="POST" onSubmit="doEnter()">
			<input type="hidden" name="actionName" value = "authenticate"/>
			<table>
				<tr>
					<td>&nbsp;</td>
					<td><a href="/supervisor/acct_lookup.jsp">New Look Up</a></td>
					<td>&nbsp;</td>
				</tr>
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
					<td><input type="submit" value="ENTER" class="submit" name="submit" style="width: 120px;" tabindex="2"></td>
				</tr>
				<tr>
					<td align="center" colspan="3"><br><br><a name="process"></a></td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
					<td><fd:ErrorHandler result="<%= result %>" name="password" id="error"><span class="error_detail"><%=error%></span></fd:ErrorHandler></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<logic:present name="userid" scope="request">
			<%
				String userid = (String)request.getAttribute("userid");
				String last4Digits = (String)request.getAttribute("last4Digits");				
			%>
			<table>
				<tr>
					<td align="left"><b>Email ID of the Account Number ending with <%= last4Digits %>:</b></td>
					<td><%= userid %></td>
				</tr>
			</table>	
			</logic:present>
			<logic:present name="matchingOrders" scope="request">
			<%
				List orders = (List)request.getAttribute("matchingOrders");
				String last4Digits = (String)request.getAttribute("last4Digits");									
			%>
			<table>
				<tr>
					<td align="left" valign="top"><b>Orders Placed using the Account Number ending with <%= last4Digits %>:</b></td>
			<%
				if(orders == null || orders.isEmpty()) {
			%>	
				<td>None</td>
			<%
				} else {
			%>	
				<td>
				<logic:iterate id="OrderId" collection="<%= orders %>" type="java.lang.String">
							
					<a onclick="openURL('/main/order_details.jsp?orderId=<%= OrderId %>')" href="#"><%= OrderId %></a>&nbsp;&nbsp;
				</logic:iterate>
				</td>
			<%
				}
			%>	
				</tr>
			</table>	
			</logic:present>
			
			</form>
		</crm:LookupAcctController>
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