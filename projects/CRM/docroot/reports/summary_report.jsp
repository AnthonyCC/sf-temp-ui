<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<style type="text/css">
    table.summaryReport {
        font-family: Trebuchet MS, Arial, Verdana, sans-serif;
        font-size: 10pt;
        border-color: gray;
        border-collapse: collapse;
        background-color: #D6EBFF;
    }
    table.summaryReport td {
		font-family: Trebuchet MS, Arial, Verdana, sans-serif;
        border-width: 0px;
        border-top: 2px;
        border-right: 1px;
        border-left: 1px;
        border-bottom: 1px;
        padding: 1px;
        border-style: inset;
        border-color: gray;
    }
</style>
<body bgColor="#D6EBFF">

<crm:GenericLocator id="searchResults" searchParam='EXEC_SUMMARY_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<logic:present name ='searchResults'>
		<% if(searchResults == null || searchResults.size() == 0){ %>
		    No orders placed 
		<%} %>
		<div align="center">
		<b><span style="color:#990000;font-family: Trebuchet MS, Arial, Verdana, sans-serif;font-size: 11pt;">Report date : <%=request.getParameter("summaryDate")%></span></b><br><br>
		<table class="summaryReport">
		<logic:iterate id="info" collection="<%= searchResults %>" type="java.util.Map" indexId="counter">
		    <tr>
			<td align="right">Sales :</td>
			<td><%=CCFormatter.formatCurrency(((Double)info.get("sales")).doubleValue())%></td>
		    </tr>
		    <tr>
			<td align="right"># of Orders :</td>
			<td><%= info.get("Total Orders")%></td>
		    </tr>
		    <tr>
			<td align="right">Avg. Order Size :</td>
			<td><%= CCFormatter.formatCurrency(((Double)info.get("Average Order Size")).doubleValue())%></td>
		    </tr>
		    <tr>
			<td align="right">Total Promotion Amount :</td>
			<td><%= CCFormatter.formatCurrency(((Double)info.get("Total Promotions")).doubleValue())%></td>
		    </tr>
		    <tr>    
			<td align="right"># of Promotions :</td>
			<td><%= info.get("Promotion Count")%></td>
		    </tr>
		    <tr>    
			<td align="right">Average Promotion :</td>
			<td><%= CCFormatter.formatCurrency(((Double)info.get("Average Promotion")).doubleValue())%></td>
		    </tr>
		    <tr>
			<td align="right">Promotion % :</td>
			<td><%= CCFormatter.formatPercentage(((Double)info.get("Promotion Percentage")).doubleValue())%></td>
		    </tr>
		</logic:iterate>
		</table>
	</logic:present>	
</crm:GenericLocator>
</div>
</body>