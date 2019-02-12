<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.fdstore.customer.FDComplaintInfo" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.ComplaintUtil"%>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Pending Credits</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/i_globalcontext.jspf" %>
<table><form method="post" action="/supervisor/pending_credits_list.jsp"></table>
<div class="sub_nav">
<% String reason = request.getParameter("reason"); 
boolean hasFilter = reason!=null && !"".equals(reason);
%>
<% List orders = null;
Map complaintCodes = null; 
%>
<span class="sub_nav_title">Pending Credits<% if (hasFilter) { %>: <%=complaintCodes.get(reason)%><% } %> ( <span class="result"><%= orders.size() %></span> )</span> &nbsp;&nbsp; <span class="note">Filter pending credits by: </span>
<%
if (!complaintCodes.isEmpty()) {
Set key = complaintCodes.keySet();
%>
<script language="javascript">
	<!--
		function filterOrders(reasonCode) {
			var thisCode = reasonCode;
			document.location.href = '/supervisor/pending_credits_list.jsp?reason=' + thisCode;
		}

	//-->
</script>
<select name="reasonCode" onChange="filterOrders(this.form.reasonCode.value)">
	<option value="">Show All</option>
	<% for(Iterator kIter = key.iterator(); kIter.hasNext();){
				String thisKey = (String)kIter.next();
				String thisValue = (String)complaintCodes.get(thisKey);
%>
	<option value="<%=thisKey%>" <%= thisKey.equalsIgnoreCase(reason)? "selected":""%>><%=thisValue%></option>
	<% } %>
</select>
A = Automatic Order   M = Manual Order
<% } %>
</div>
<div class="" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="list_header_text">
		<TR>
			<td class="pendCred-spacer"></td>
			<td class="pendCred-ordId">Order #</td>
			<td class="pendCred-appDec">App/Dec</td>
            <td class="pendCred-type">Type</td>
			<td class="pendCred-dlvDate">Dlv Date</td>
			<td class="pendCred-status">Status</td>
			<td class="pendCred-ordAmt">Amount</td>
			<td class="pendCred-custName">Customer Name</td>
			<td class="pendCred-store">Store</td>
			<td class="pendCred-facility">Facility</td>
			<td class="pendCred-credAmt">Credit Amt</td>
			<td class="pendCred-credType">Credit Type</td>
			<td class="pendCred-credNote">Credit Note</td>
			<td class="pendCred-spacer"></td>
		</TR>
	</TABLE>
	</div>
	<div class="list_content" style="height: 95%; padding: 0px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<%	if (orders.size() > 0) { %>
		<%
			int displayedLines = 0;
		%>
		<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDComplaintInfo" indexId="counter">
			<% if ( 
					((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(info.geteStore())) &&
					((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(info.getFacility()))
			) { %>
				<TR VALIGN="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId()) %>'">
					<td class="pendCred-spacer"></td>
					<td class="pendCred-ordId"><u><a href="/main/order_details.jsp?orderId=<%= info.getSaleId() %>"><%= info.getSaleId() %></u></a></TD>
					<td class="pendCred-appDec"><a href="javascript:pop('/returns/approve_credit.jsp?orderId=<%= info.getSaleId() %>&complaintId=<%= info.getComplaintId() %>&inPopup=true', 700, 800)">App/Dec</a></td>
					<td class="pendCred-type"><% if(EnumSaleType.REGULAR.equals(info.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(info.getOrderType())){%>A<%}%></TD>
		            <td class="pendCred-dlvDate"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></TD>
					<td class="pendCred-status"><%= (info.getSaleStatus() != null) ? info.getSaleStatus().getName() : "--" %></TD>
					<td class="pendCred-ordAmt"><%= JspMethods.formatPrice(info.getOrderAmount()) %></TD>
					<td class="pendCred-custName"><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
					<td class="pendCred-store"><%= info.geteStore() %></TD>
					<td class="pendCred-facility"><%= info.getFacility() %></TD>
					<td class="pendCred-credAmt"><%= JspMethods.formatPrice(info.getComplaintAmount()) %></TD>
					<td class="pendCred-credType"><%= "FDC".equals(info.getComplaintType())?"Store Credit":"CSH".equals(info.getComplaintType())?"Refund":"MIX".equals(info.getComplaintType())?"Mixed":"" %></TD>
					<td class="pendCred-credNote"><%= info.getComplaintNote() %></TD>
					<td class="pendCred-spacer"></td>
				</TR>
				<tr><td colspan="14" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
				<% displayedLines++;  %>
			<% } %>
		</logic:iterate>
		<% if (displayedLines == 0) { %>
			<tr><td colspan="14" align="center"><br>no pending credits need to be processed<br><br></td></tr>
		<% } %>
	<% 	} else { %>
	    <tr><td colspan="14" align="center"><br>no pending credits need to be processed<br><br></td></tr>
	<%  }   %>
	</TABLE>
	</div>
</div> 
<table></form></table>
</tmpl:put>

</tmpl:insert>