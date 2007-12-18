<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.delivery.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.EnumTransactionType" %>
<%@ page import="com.freshdirect.customer.EnumTransactionType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Settlement Problems</tmpl:put>

<tmpl:put name='content' direct='true'>
<% 
SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
DateFormatSymbols symbols = new DateFormatSymbols();

String sm = request.getParameter("failure_start_month");
String sd = request.getParameter("failure_start_day");  
String sy = request.getParameter("failure_start_year");  
String em = request.getParameter("failure_end_month");
String ed = request.getParameter("failure_end_day");  
String ey = request.getParameter("failure_end_year");  

// seet start date default to 1 week prior --> this speeds up query
if (sm == null || sd == null || sy == null) {	
	// set start date to 7 days prior (1 week)
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());
	cal.add(Calendar.DATE, -7);
	sd = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	sm = String.valueOf(cal.get(Calendar.MONTH)+1);
	sy = String.valueOf(cal.get(Calendar.YEAR));	
}


%>
<jsp:include page="/includes/reports_nav.jsp" />
<fd:CrmSettlementProblemSearch result="result" actionName="searchSettlementProblems" reportName="report">
<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav">
<form method='POST' name="settlement_problem"><tr><td><span class="sub_nav_title">Settlement Problem</span> Search for orders that have a settlement problem <% if ("POST".equals(request.getMethod()) && report.size() > 0) {%><span class="note" style="font-weight: normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></td><td align="right"><a href="/reports/index.jsp">All Reports >></a></td></tr></table>
<table width="100%" cellpadding="0" cellspacing="0" class="content_fixed">
        <tr>
			<td><b>Select Type: </b></td>
			<td><input type="checkbox" name="status_trans_type" checked value="<%=EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode()%> <%=EnumTransactionType.SETTLEMENT_FAILED.getCode()%>"><%=EnumSaleStatus.SETTLEMENT_FAILED.getName()%></td>
			<td><input type="checkbox" name="status_trans_type" value="<%=EnumSaleStatus.SETTLED.getStatusCode()%> <%=EnumTransactionType.FUNDS_REDEPOSIT.getCode()%>"><%=EnumTransactionType.FUNDS_REDEPOSIT.getName()%></td>
			<td><input type="checkbox" name="status_trans_type" value="<%=EnumSaleStatus.CHARGEBACK.getStatusCode()%> <%=EnumTransactionType.CHARGEBACK.getCode()%>"><%=EnumSaleStatus.CHARGEBACK.getName()%></td>
        </tr>
        <tr>
			<td>&nbsp;</td>
	        <td>Failure Start Date:&nbsp;
	        	<select name="failure_start_month" required="true" class="pulldown">
	                <option value="">Month</option>
	                            <%
								int failureStartMonth = (sm != null && !"".equals(sm)) ? Integer.parseInt(sm) : -1;
	                            for (int i=0; i<12; i++) {  %>
	                            <option value="<%= i+1 %>" <%= (i+1==failureStartMonth)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
	                            <%  }   %>
	            </select>&nbsp;            
	            <select name="failure_start_day" required="true" class="pulldown">
	                <option value="">Day</option>
	                            <%  
								int failureStartDay = (sd != null && !"".equals(sd)) ? Integer.parseInt(sd) : -1;
	                            for (int i=1; i<=31; i++) { %>
	                <option value="<%= i %>" <%= (i==failureStartDay)?"selected":"" %>><%= i %></option>
	                            <%  } %>
	            </select>&nbsp;	
	            <select name="failure_start_year" required="true" class="pulldown">
	                <option value="">Year</option>
	                            <%  
								int failureStartYear = (sy != null && !"".equals(sy)) ? Integer.parseInt(sy) : -1;
	                            for (int i=2005; i<2011; i++) { %>
	                <option value="<%= i %>" <%= (i==failureStartYear)?"selected":"" %>><%= i %></option>
	                            <%  } %>
	            </select>&nbsp;&nbsp;
			</td>
	        <td>Failure End Date:&nbsp;
	        	<select name="failure_end_month" required="true" class="pulldown">
	                <option value="">Month</option>
	                            <%
								int failureEndMonth = (em != null && !"".equals(em)) ? Integer.parseInt(em) : -1;
	                            for (int i=0; i<12; i++) {  %>
	                            <option value="<%= i+1 %>" <%= (i+1==failureEndMonth)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
	                            <%  }   %>
	            </select>&nbsp;            
	            <select name="failure_end_day" required="true" class="pulldown">
	                <option value="">Day</option>
	                            <%  
								int failureEndDay = (ed != null && !"".equals(ed)) ? Integer.parseInt(ed) : -1;
	                            for (int i=1; i<=31; i++) { %>
	                <option value="<%= i %>" <%= (i==failureEndDay)?"selected":"" %>><%= i %></option>
	                            <%  } %>
	            </select>&nbsp;	
	            <select name="failure_end_year" required="true" class="pulldown">
	                <option value="">Year</option>
	                            <%  
								int failureEndYear = (ey != null && !"".equals(ey)) ? Integer.parseInt(ey) : -1;
	                            for (int i=2005; i<2011; i++) { %>
	                <option value="<%= i %>" <%= (i==failureEndYear)?"selected":"" %>><%= i %></option>
	                            <%  } %>
	            </select>&nbsp;&nbsp;
			</td>
			<td colspan=2><input type="submit" value="SEARCH SETTLEMENT PROBLEM" class="submit"></td>
		<tr>
    </form>
</table>
<%
String status = request.getParameter("status");
if(report != null){%>
<div class="list_header">
<table class="list_header_text" width="100%">
    <tr>
		<td width="1%"></td>
        <td width="10%">Order ID</td>
        <td width="15%">Customer Name</td>
        <td width="9%">Amount</td>
        <td width="15%">Status</td>
        <td width="15%">Transaction Type</td>
        <td width="10%">Delivery Date</td>
        <td width="10%">Failure Date</td>
        <td width="10%">Payment Method</td>
		<td><img src="/media_stat/images/layout/clear.gif" width="12" height="1"></td>
    </tr>
</table>
</div>
<div id="result" class="list_content" style="height:70%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
    <logic:iterate id="settlementProblem" collection="<%= report %>" type="com.freshdirect.crm.CrmSettlementProblemReportLine" indexId="idx">
    <tr>
		<td width="1%" class="border_bottom">&nbsp;</td>
        <td width="10%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=settlementProblem.getSaleId()%>"><%=settlementProblem.getSaleId()%></a>&nbsp;</td>
        <td width="15%" class="border_bottom"><%=settlementProblem.getCustomerName()%>&nbsp;</td>
        <td width="9%" class="border_bottom"><%=settlementProblem.getAmount()%>&nbsp;</td>
        <td width="15%" class="border_bottom"><%=settlementProblem.getStatus()%>&nbsp;</td>
        <td width="15%" class="border_bottom"><%=settlementProblem.getTransactionType().getName()%>&nbsp;</td>
        <td width="10%" class="border_bottom"><%=sdf.format(settlementProblem.getDeliveryDate())%>&nbsp;</td>
        <td width="10%" class="border_bottom"><%=sdf.format(settlementProblem.getFailureDate())%>&nbsp;</td>
        <td width="10%" class="border_bottom"><%=settlementProblem.getPaymentMethodType()%>&nbsp;</td>
    </tr>
    </logic:iterate>
</table>
</div>
<% } %>
<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.settlement_problem.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>

</fd:CrmSettlementProblemSearch>
</tmpl:put>

</tmpl:insert>