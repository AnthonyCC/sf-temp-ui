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
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Del. Reservations</tmpl:put>

<tmpl:put name='content' direct='true'>

<script>
	function openURL(inLocationURL) {

	popResize(inLocationURL, 400,400,'');

	}
	
	function doSearch() {
	   	document.getElementById("searchFlag").value = "true";
	}
</script>
<jsp:include page="/includes/admintools_nav.jsp" />
<% 
	String dlvDate = 
	NVL.apply(request.getParameter("deliveryDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
	String zone = NVL.apply(request.getParameter("zone"), "");
	String fromTimeSlot = NVL.apply(request.getParameter("fromTimeSlot"), "");
	String fromTimePeriod = NVL.apply(request.getParameter("fromTimePeriod"), "AM");
	String toTimeSlot = NVL.apply(request.getParameter("toTimeSlot"), "");
	String toTimePeriod = NVL.apply(request.getParameter("toTimePeriod"), "AM");
	String filterType = NVL.apply(request.getParameter("filterType"), "");
	String fromWaveNumber = NVL.apply(request.getParameter("fromWaveNumber"), "");
	String toWaveNumber = NVL.apply(request.getParameter("toWaveNumber"), "");
	String fromTruckNumber = NVL.apply(request.getParameter("fromTruckNumber"), "");
	String toTruckNumber = NVL.apply(request.getParameter("toTruckNumber"), "");
	String notes = NVL.apply(request.getParameter("notes"), "");
	int prcLimit = FDStoreProperties.getOrderProcessingLimit();		
%>
<div class="home_search_module_content" style="height:100%;">
<crm:AdminToolsController result="result">
	<fd:ErrorHandler result='<%= result %>' name='actionfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='returnsuccess' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<crm:GenericLocator id="returnOrders" searchParam='RETURN_ORDER_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>

<form name="returnorders" method='POST' onsubmit="javascript:doSearch();">
<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">

	<tr>
		
		<td>Delivery Date : </td>
		<td>
			<input type="hidden" name="actionName" id="actionName" value="">
			<input type="hidden" name="searchFlag" value="">
			<input type="hidden" name="deliveryDate" id="deliveryDate" value="<%=dlvDate%>">
                        <input type="text" name="newDeliveryDate" id="newDeliveryDate" size="10" value="<%=dlvDate%>" disabled="true" onchange="setDate(this);"> &nbsp;<a href="#" id="trigger_dlvDate" style="font-size: 9px;">>></a>
 		        <script language="javascript">

			    function setDate(field){
			    	document.getElementById("deliveryDate").value=field.value;

			    }


			    Calendar.setup(
			    {
			    showsTime : false,
			    electric : false,
			    inputField : "newDeliveryDate",
			    ifFormat : "%Y-%m-%d",
			    singleClick: true,
			    button : "trigger_dlvDate" 
			    }
			    );
			    
			    function clearAll(){
			    	var d = new Date();
				var date = d.getDate();
				var month = d.getMonth()+1;
				var year = d.getFullYear();
			    	var fd = year + "-" + month + "-" + date;
			    	document.getElementById("newDeliveryDate").value = fd;
			    	document.getElementById("deliveryDate").value = fd;
			    	document.getElementById("zone").value = "";
			    	document.getElementById("fromTimeSlot").value = "";
			    	document.getElementById("fromTimePeriod").value = "AM";
			    	document.getElementById("toTimeSlot").value = "";
			    	document.getElementById("toTimePeriod").value = "AM";
			    	document.getElementById("fromWaveNumber").value = "";
			    	document.getElementById("toWaveNumber").value = "";
			    	document.getElementById("fromTruckNumber").value = "";
			    	document.getElementById("toTruckNumber").value = "";
			    	
			    }
			    
			    
			    function doAction(actionName) {
			    	if(actionName == 'returnOrders'){
					var doReturn = confirm ("Are you sure you want to return these Orders completely?");
					if(doReturn == false){
						return;
					}            
					var notes = document.getElementById("notes").value;
					if(notes == ''){
						alert('Notes required');
						document.getElementById("notes").focus();
						return;
										
					}
				
					document.getElementById("actionName").value = actionName;
					document.getElementById("returnorders").submit();	
			    	}
			    }
			    
			</script>
		</td>
		<td>Wave Number : </td>
		<td>From&nbsp;
			<input type = "text" name="fromWaveNumber" value= "<%= fromWaveNumber %>" style="width: 100px;">&nbsp;
			
		    &nbsp;&nbsp;To&nbsp;
			<input type = "text" name="toWaveNumber" value= "<%= toWaveNumber %>" style="width: 100px;">&nbsp;
		</td>	
	</tr>
	<tr>
		<td align="bottom">Zone :(, seperated)</td>
		<td>
			<input type="text" name="zone" value="<%= zone %>" class="input_text" style="width: 200px;">
		</td>
		<td>Truck Number : </td>
		<td>From&nbsp;
			<input type = "text" name="fromTruckNumber" value= "<%= fromTruckNumber %>" style="width: 100px;">&nbsp;
			
		 &nbsp;&nbsp;To&nbsp;
			<input type = "text" name="toTruckNumber" value= "<%= toTruckNumber %>" style="width: 100px;">&nbsp;
		</td>	
		
	</tr>
	<tr>
		<td>Time Slot : </td>
		<td colspan="3">From(HH:MI)&nbsp;
		
			<input type = "text" name="fromTimeSlot" value= "<%= fromTimeSlot %>" style="width: 55px;">&nbsp;
			<select name="fromTimePeriod" style="width: 45px;">
				<option value="AM" <%= fromTimePeriod.equals("AM") ? "SELECTED" : "" %>>AM</option>
				<option value="PM" <%= fromTimePeriod.equals("PM") ? "SELECTED" : "" %>>PM</option>
			</select>
	</tr>	
	<tr>
		<td>&nbsp;</td>
		<td colspan="3">To(HH:MI)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
			<input type = "text" name="toTimeSlot" value= "<%= toTimeSlot %>" style="width: 55px;">&nbsp;
			<select name="toTimePeriod" style="width: 45px;">
				<option value="AM" <%= toTimePeriod.equals("AM") ? "SELECTED" : "" %>>AM</option>
				<option value="PM" <%= toTimePeriod.equals("PM") ? "SELECTED" : "" %>>PM</option>
			</select>
		</td>
		
	</tr>		
	<tr>
		<td colspan="12" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8">
			<input type="submit" value="SEARCH ORDERS" class="submit">&nbsp;&nbsp;
			<input type="button" value="CLEAR" class="submit" onclick="javascript:clearAll();">
		</td>
		

	</tr>

</table>
<table class="home_search_module_field" border="0" cellpadding="2" cellspacing="2" width="100%">
	<tr>
	<td>
		<span class="header_text"><b>List of Orders to Return</b></span>
	</td>
	</tr>
	<tr><td colspan="2">
	<div class="home_search_module_content" style="background=#FFFFFF; overflow:auto;width=100%;height:420;">
		<%@ include file="/includes/admintools/orders_for_returns.jspf"%>
	</div>		
	</td></tr>
	<!--
	<tr>
		<td>
			<input type="checkbox" name="sendEmail" value="false">&nbsp;Send E-mail to Customers
		</td>
	</tr>
	-->
	<logic:present name="returnOrders">	
		<tr>
			<td>
				<span class="info_text"><b>Total Number of Orders remaining to be Returned: </b><%= returnOrders.size() %></span>
			</td>
		</tr>	
	</logic:present>	
	<tr>
		<td align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
	<%
		if(returnOrders ==  null || returnOrders.size() == 0) {
	%>
		<input type="submit" value="RETURN ORDERS" class="submit" disabled>
		<input name="exportButton" type="button" value="EXPORT ORDERS" class="submit" disabled>
	<%
		} else {
	%>	
		<%
			if(returnOrders.size() <= prcLimit) {
		%>	
			
			<input type="button" value="RETURN ORDERS" class="submit" onclick="javascript:doAction('returnOrders');">
			
	
		<%
			} else {
		%>
			<input type="button" value="RETURN FIRST <%= prcLimit %> ORDERS" class="submit" onclick="javascript:doAction('returnOrders');">			
		<%
			}
		%>	
		&nbsp;&nbsp;
		<input name="exportResults"  type="button" value="EXPORT RESULTS" class="submit" onclick="javascript:openURL('/reports/final_return_report.xls');">
		&nbsp;&nbsp;
		<input name="exportVS" type="button" value="EXPORT TO VOICESHOT" class="submit" onclick="javascript:openURL('/reports/returnsVSExport');">
		&nbsp;&nbsp;
		<input name="exportSP" type="button" value="EXPORT TO SILVERPOP" class="submit" onclick="javascript:openURL('/reports/returnsSPExport');">
		<%
			String searchFlag = (String)request.getParameter("searchFlag");
			if(searchFlag != null && searchFlag.equals("true")) {
		%>	

		<SCRIPT LANGUAGE="JavaScript"> 
			alert('Please click on "EXPORT" buttons to export the Orders in the respective format before you return them.');
			function initArray() { 
				for (var i = 0; i < initArray.arguments.length; i++) { 
					this[i] = initArray.arguments[i]; 
				} this.length = initArray.arguments.length; 
			} 
			var colors = new initArray("#D6EBFF", "#FF0000"); 
			delay = .5; // seconds 
			link = 0;
			function blink() { 
					document.getElementById("exportResults").style.borderWidth='4px';
					document.getElementById("exportVS").style.borderWidth='4px';
					document.getElementById("exportSP").style.borderWidth='4px';
					link = (link+1)%colors.length; 
					document.getElementById("exportResults").style.borderColor = colors[link]; 
					document.getElementById("exportVS").style.borderColor = colors[link]; 
					document.getElementById("exportSP").style.borderColor = colors[link]; 					
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
		<td><span class="error"><b>Please Note: </b>You can only return <%= prcLimit  %> orders at a time.</td>
	</tr>
	<tr>
		<td colspan="2">	
			<span class="info_text">Enter notes (required):</span>
		</td>
	</tr>	
	<tr>
		<td colspan="2">
			<textarea name="notes" rows="2" wrap="VIRTUAL" style="width: 330px;"><%= notes %></textarea>
		</td>
	</tr>	

	
</table>
</form>
</crm:GenericLocator>
</crm:AdminToolsController>
</div>
</tmpl:put>
</tmpl:insert>
