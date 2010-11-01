<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.AdminToolsControllerTag" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<script language="javascript">
	function doAction(actionName) {
		if(actionName == 'modifyOrders'){
			var doCancel = confirm ("Click OK to Continue");
			if(doCancel == false){
				return;
			}            
			if(document.getElementById("sendEmail").checked == true){
				document.getElementById("sendEmail").value = "true";
			}
			document.getElementById("actionName").value = actionName;
			document.getElementById("modifyorders").submit();	
		
		}
	}

</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Admin Tools > Remove Skus from Order</tmpl:put>

<tmpl:put name='content' direct='true'>


<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:100%;">
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:GenericLocator id="Orders" searchParam='GET_ORDERS_TO_MODIFY' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>	
<form id="modifyorders" name="modifyorders" method='POST'>
<input type="hidden" name="actionName" id="actionName" value="">
<%
	int prcLimit = FDStoreProperties.getOrderProcessingLimit();
	String sendEmail = NVL.apply(request.getParameter("sendEmail"), "false");
%>
<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:400;">
		<%@ include file="/includes/admintools/orders_for_modification.jspf" %>
	</div>		
	</td>
	</tr>
	<tr><td colspan="2">
		<%@ include file="/includes/admintools/i_search_order_for_skus.jspf"%>
	</td>
	</tr>		
	<tr>
		<td>
			<input type="checkbox" id="sendEmail" name="sendEmail" value="<%= sendEmail %>">&nbsp;Send E-mail to Customers
		</td>
	</tr>
	<logic:present name="Orders">	
		<tr>
			<td>
				<span class="info_text"><b>Total Number of Orders remaining to be Modified: </b><%= Orders.size() %></span>
			</td>
		</tr>	
	</logic:present>
	<tr>
		<td align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
	<%
		if(Orders ==  null || Orders.size() == 0) {
	%>
		<input type="submit" value="MODIFY ORDERS" class="submit" disabled>
		<input name="exportButton" type="button" value="EXPORT ORDERS" class="submit" disabled>
	<%
		} else {
	%>	
		
		<%
			if(Orders.size() <= prcLimit) {
		%>
				<input type="button" value="MODIFY ORDERS" class="submit" onclick="javascript:doAction('modifyOrders');">

		<%
			} else {
		%>
				<input type="button" value="MODIFY FIRST <%= prcLimit %> ORDERS" class="submit" onclick="javascript:doAction('modifyOrders');">			
		<%
			}
		%>	


			&nbsp;&nbsp;
			<input name="exportButton" type="button" value="EXPORT ORDERS" class="submit" onclick="javascript:openURL('/reports/final_cancel_report.xls');">
	<%
		}
	%>			
	</td>
	</tr>
</table>
</form>
</crm:GenericLocator>
</crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>

