<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Del. Reservations</tmpl:put>

<tmpl:put name='content' direct='true'>


<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:100%;">

<form name="searchcriteria" id="searchcriteria" method='POST' onsubmit="javascript:doSearch();">
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='deletesuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:GenericLocator id="reservations" searchParam='RESERVATION_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<%@ include file="/includes/admintools/i_search.jspf"%>		

	<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
		<tr>
			<td colspan="2" align="center">
				<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
				<input type="submit" value="SEARCH RESERVATIONS" class="submit">&nbsp;&nbsp;
				<input type="button" value="CLEAR" class="submit" onclick="javascript:clearAll();">
			</td>


		</tr>
		<tr><td colspan="2">
		<span class="header_text"><b>List of Reservations</b></span><br>
		<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:250;">
		<%@ include file="/includes/admintools/uncommitted_resv_list.jspf"%>
		</div><br>
		</td></tr>
		<tr>
			<td width="35%">	
				<span class="info_text">Enter notes <span class="error">*</span>:</span>&nbsp;
				<textarea name="notes" id="notes" rows="2" wrap="VIRTUAL" style="width: 330px;"></textarea>
			</td>

			<td width="65%">
		<%
			if(reservations ==  null || reservations.size() == 0) {
		%>
			<input type="button" value="DELETE RESERVATIONS" class="submit" disabled>&nbsp;&nbsp;
			<input type="button" name="exportResv" id="exportResv" value="EXPORT RESERVATION LIST" class="submit" disabled>
		<%
			} else {
		%>
			<input type="button" value="DELETE RESERVATIONS" class="submit" onclick="javascript:doAction('deleteReservations');">&nbsp;&nbsp;
			<input type="button" name="exportResv" id="exportResv" value="EXPORT RESERVATIONS" class="submit" onclick="javascript:openURL('/reports/reservations_report.xls');">
		<%
			String searchFlag = (String)request.getParameter("searchFlag");
			if(searchFlag != null && searchFlag.equals("true")) {
		%>	
			<SCRIPT LANGUAGE="JavaScript"> 
				alert('Please click on EXPORT RESERVATIONS to export the Reservations before you delete it.');
				function initArray() { 
					for (var i = 0; i < initArray.arguments.length; i++) { 
						this[i] = initArray.arguments[i]; 
					} this.length = initArray.arguments.length; 
				} 
				var colors = new initArray("#D6EBFF", "#FF0000"); 
				delay = .5; // seconds 
				link = 0;
				function blink() { 
						document.getElementById("exportResv").style.borderWidth='4px';
						link = (link+1)%colors.length; 
						document.getElementById("exportResv").style.borderColor = colors[link]; 
						setTimeout("blink()",delay*1000); 
				} 
				blink(); 
			</script>	
		<%
			}
		}
		%>	
			</td>


		</tr>	

	</table>
</crm:GenericLocator>
</crm:AdminToolsController>
<%
	String filterType = NVL.apply(request.getParameter("filterType"), "");
%>
<crm:GenericLocator id="orders" searchParam='ORDERS_BY_RESV_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
		<tr>
		<td>
			<span class="header_text"><b>List of Orders for Standard and Pre-Reservations</b></span>
		</td>
		<td align="right">
			<span class="info_text"><b>Filter By:<b></span>&nbsp;
			<select name="filterType" id="filterType" style="width: 125px;" onchange="javascript:doFilter();">
				<option value="ALL" <%= filterType.equals("ALL") ? "SELECTED" : "" %>>All</option>
				<option value="STD" <%= filterType.equals("STD") ? "SELECTED" : "" %>>Standard</option>
				<option value="PRE" <%= filterType.equals("PRE") ? "SELECTED" : "" %>>Pre-Reservation</option>
			</select>
		</td>
		</tr>
		<tr><td colspan="2">
		<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:250;">
			<%@ include file="/includes/admintools/orders_for_resv_list.jspf"%>
		</div>	
		</td></tr>
		<tr>
			<td colspan="2" align="center">
				<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
		<%
			if(orders ==  null || orders.size() == 0) {
		%>

				<input type="submit" value="EXPORT ORDER LIST" class="submit" disabled>
		<%
			} else {
		%>
			<input type="button" value="EXPORT ORDERS" class="submit" onclick="javascript:openURL('/reports/orders_by_reservations_report.xls');">
		<%
			}
		%>	

			</td>
		</tr>

	</table>
</crm:GenericLocator>
</form>
</div>
</tmpl:put>
</tmpl:insert>

