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
    function doAction(actionName,index) {
	if(actionName == 'fixSettlemnentBatch'){
		var doFix = confirm ("Do you want to fix this settlement batch?");
		if(doFix == false){
			return;
		}            		
		document.forms[index].elements[0].value = actionName;
		document.forms[index].submit();	
	}
    }

</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Settlement Batch</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />
<crm:GenericLocator id="searchResults" searchParam='SETTLEMENT_BATCH_SEARCH' result="result">
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
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Merchant ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Batch ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Settlement Time</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Batch Status</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Batch Response MSG</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Processor Batch ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Submission ID</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Sales Transactions</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Sales Amount</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Return Transactions</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Return Amount</b></span></td>	
    </tr>
	
	<%
		if(searchResults ==  null || searchResults.size() == 0) {
	%>
	<tr><td><span class="error">No failed settlement batch to fix.</span></td></tr>

	<%
		} else {
	%>	

	<logic:iterate id="sbInfo" collection="<%= searchResults %>" type="com.freshdirect.payment.SettlementBatchInfo" indexId="index">
	<tr>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getMerchant_id() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getBatch_id() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getSettle_date_time() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getBatch_status() %></span></td>
    <td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getBatch_response_msg() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getProcessor_batch_id() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getSubmission_id() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getSales_transactions() %></span></td>
    <td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getSales_amount() %></span></td>	
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getReturn_transactions() %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= sbInfo.getReturn_amount() %></span></td>
    </tr>
    <tr>
		<td colspan="12" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<form name="settlement" method="POST">
			<input type="hidden" name="actionName" id="actionName" value="">
            <input type="hidden" name="batch_id" id="batch_id" value="<%= sbInfo.getBatch_id() %>">
			<%
				if((!"04".equals(sbInfo.getBatch_status()) && !"05".equals(sbInfo.getBatch_status())) ||!"BATCH ERROR".equals(sbInfo.getBatch_response_msg().trim()) || sbInfo.getProcessor_batch_id() == null || sbInfo.getSales_transactions() <=0 || sbInfo.getSales_amount() <=0) {
			%>

			<input type="button" value="FIX SETTLEMENT BATCH" class="submit" disabled>&nbsp;&nbsp;
			<%
				} else {
			%>	
			<input type="button" value="FIX SETTLEMENT BATCH" class="submit" onclick="javascript:doAction('fixSettlemnentBatch',<%=index.intValue()%>);">&nbsp;&nbsp;
			<%
				}
			%>	
			
			</form>
		</td>
	</tr>
	</logic:iterate>
	<%
		}
	%>	

</table>
</crm:AdminToolsController>
</crm:GenericLocator>
</tmpl:put>
</tmpl:insert>