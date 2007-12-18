<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
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
	
	<div class="sub_nav">
	<br>
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
		<fd:ErrorHandler result='<%= result %>' name='authentication' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>
	
		<form name="acctlookup" method="POST">
		<input type="hidden" name="actionName" value = "lookup"/>
		<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
			<tr>
				<td align="center">Enter the Account Number :&nbsp;
					<input type="text" name="acctNum" value="<%= acctNum %>" maxlength=20 class="input_text" style="width: 200px;">
				</td>
			</tr>		
			<tr>
				<td align="center">
					<table align="center" border="0" cellpadding="0" cellspacing="0" width="45%">
					<tr>
						<td>&nbsp;&nbsp;
							<input type="radio" name="lookupLoc" value="1" <%= lookupLoc.equals("1") ? "checked" : "" %>>&nbsp;
							Lookup Customer's Account<br>
						</td>
					</tr>
					<tr>
						<td>&nbsp;&nbsp;
							<input type="radio" name="lookupLoc" value="2" <%= lookupLoc.equals("2") ? "checked" : "" %>>&nbsp;
							Lookup Customer's Past Orders<br><br>
						</td>
					</tr>	
					</table>
				</td>
			</tr>

			<tr>
				<td align="center">
					<img src="/media_stat/crm/images/clear.gif" width="1" height="8">
					<input type="submit" value="   LOOK UP   " class="submit">
				</td>

			</tr>		
		</table>
		</form>
	</crm:LookupAcctController>
	</div>
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