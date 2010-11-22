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

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Del. Reservations</tmpl:put>

<tmpl:put name='content' direct='true'>


<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:100%;">
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='cancelsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:GenericLocator id="cancelOrders" searchParam='CANCEL_ORDER_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>	
<form name="cancelorders" method='POST' onsubmit="javascript:doSearch();">
<%@ include file="/includes/admintools/i_search.jspf"%>
<%
	int prcLimit = FDStoreProperties.getOrderProcessingLimit();
%>
<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<td colspan="2" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="submit" value="SEARCH ORDERS" class="submit">&nbsp;&nbsp;
			<input type="button" value="CLEAR" class="submit" onclick="javascript:clearAll();">
		</td>
		

	</tr>
	<tr colspan="2">
	<td>
		<span class="header_text"><b>List of Orders to Cancel</b></span>
	</td>
	</tr>
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:420;">
		<%@ include file="/includes/admintools/orders_for_cancellation.jspf"%>
	</div>		
	</td></tr>
	<tr>
		<td>
			<input type="checkbox" name="sendEmail" id="sendEmail" value="<%= sendEmail %>">&nbsp;Send E-mail to Customers
		</td>
	</tr>
	<logic:present name="cancelOrders">	
		<tr>
			<td>
				<span class="info_text"><b>Total Number of Orders remaining to be Cancelled: </b><%= cancelOrders.size() %></span>
			</td>
		</tr>	
	</logic:present>
	<tr>
		<td align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
	<%
		if(cancelOrders ==  null || cancelOrders.size() == 0) {
	%>
		<input name="cancelorders" id="cancelorders" type="submit" value="CANCEL ORDERS" class="submit" disabled>
		<input name="exportButton" id="exportButton" type="button" value="EXPORT ORDERS" class="submit" disabled>
	<%
		} else {
	%>	
		
		<%
			if(cancelOrders.size() <= prcLimit) {
		%>
				<input name="cancelorders" id="cancelorders" type="button" value="CANCEL ORDERS" class="submit" onclick="javascript:doAction('cancelOrders');">

		<%
			} else {
		%>
				<input name="cancelorders" id="cancelorders" type="button" value="CANCEL FIRST <%= prcLimit %> ORDERS" class="submit" onclick="javascript:doAction('cancelOrders');">			
		<%
			}
		%>	


			&nbsp;&nbsp;
			<input name="exportButton" id="exportButton" type="button" value="EXPORT ORDERS" class="submit" onclick="javascript:openURL('/reports/final_cancel_report.xls');">
		<%
			String searchFlag = (String)request.getParameter("searchFlag");
			if(searchFlag != null && searchFlag.equals("true")) {
		%>	
			<SCRIPT LANGUAGE="JavaScript"> 
				alert('Please click on "EXPORT ORDERS" button to export the Orders before you cancel them.');
				function initArray() { 
					for (var i = 0; i < initArray.arguments.length; i++) { 
						this[i] = initArray.arguments[i]; 
					} this.length = initArray.arguments.length; 
				} 
				var colors = new initArray("#D6EBFF", "#FF0000"); 
				delay = .5; // seconds 
				link = 0;
				function blink() { 
						document.getElementById("exportButton").style.borderWidth='4px';
						link = (link+1)%colors.length; 
						document.getElementById("exportButton").style.borderColor = colors[link]; 
						setTimeout("blink()",delay*1000); 
				} 
				blink(); 
			</script>	
			<%
				}
			%>	
			
	<%
		}
	%>	
	</td>
	</tr>
	<tr>
	<td><span class="error"><b>Please Note: </b>You can only cancel <%= prcLimit %> orders at a time.</td>
	</tr>
	<tr>
		<td colspan="2">	
			<span class="info_text">Enter notes (required):</span>
		</td>
	</tr>	
	<tr>
		<td colspan="2">
			<textarea name="notes" id="notes" rows="2" wrap="VIRTUAL" style="width: 330px;"><%= notes %></textarea>
		</td>
	</tr>	

	
</table>
</form>
</crm:GenericLocator>
</crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>

