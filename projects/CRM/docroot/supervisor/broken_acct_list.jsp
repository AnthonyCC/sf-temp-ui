<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>
<script>
    function doAction(actionName) {
	if(actionName == 'fixBrokenAccounts'){
		var doFix = confirm ("Do you want to fix the Broken Accounts?");
		if(doFix == false){
			return;
		}            		
		document.getElementById("actionName").value = actionName;
		document.forms[0].submit();	
	}
    }

</script>
<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Broken Accounts</tmpl:put>

<tmpl:put name='content' direct='true'>

<crm:GenericLocator id="searchResults" searchParam='BROKEN_ACCOUNT_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='fixsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>

<table width="100%" cellpadding="0" cellspacing="3 border="3" bgcolor="#FFFFFF"> 
	<tr>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>User ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Customer ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>FDCustomer ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Zip Code</b></span></td>	
	</tr>
	
	<%
		if(searchResults ==  null || searchResults.size() == 0) {
	%>
	<tr><td><span class="error">No Broken Accounts to fix.</span></td></tr>

	<%
		} else {
	%>	

	<logic:iterate id="brokenacct" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDBrokenAccountInfo">
	<tr>
	<td class="border_bottom"><span class="detail_text"><%= brokenacct.getUserId() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= brokenacct.getErpCustomerPk() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= brokenacct.getFdCustomerPK() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= brokenacct.getZipCode() %></span></td>
	</tr>
	</logic:iterate>
	<%
		}
	%>	
	<tr>
		<td colspan="12" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<form name="brokenaccts" method="POST">
			<input type="hidden" name="actionName" id="actionName" value="">
			<%
				if(searchResults ==  null || searchResults.size() == 0) {
			%>

			<input type="button" value="FIX BROKEN ACCOUNTS" class="submit" disabled>&nbsp;&nbsp;
			<%
				} else {
			%>	
			<input type="button" value="FIX BROKEN ACCOUNTS" class="submit" onclick="javascript:doAction('fixBrokenAccounts');">&nbsp;&nbsp;
			<%
				}
			%>	
			
			</form>
		</td>
	</tr>

</table>
</crm:AdminToolsController>
</crm:GenericLocator>
</tmpl:put>
</tmpl:insert>