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

<table><form method="post" action="/supervisor/pending_credits_list.jsp"></table>
<div class="sub_nav">
<% String reason = request.getParameter("reason"); 
boolean hasFilter = reason!=null && !"".equals(reason);
%>
<% List orders = CallCenterServices.getPendingComplaintOrders(reason); 
Map complaintCodes = new TreeMap(CallCenterServices.getComplaintCodes()); 
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
<div class="content" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" class="list_header_text">
		<TR>
			<td width="1%"></td>
			<td width="10%">Order #</td>
            <td width="2%">Type</td>
			<td width="10%">Delivery Date</td>
			<td width="10%">Status</td>
			<td width="8%">Amount</td>
			<td width="18%">Customer Name</td>
			<td width="11%">Credit Amount</td>
			<td width="10%">Credit Type</td>
			<td width="20%">Credit Note</td>
			<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
		</TR>
	</TABLE>
	</div>
	<div class="list_content" style="height: 95%; padding: 0px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<%	if (orders.size() > 0) { %>
		<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDComplaintInfo" indexId="counter">
	
		<TR VALIGN="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId()) %>'">
			<td width="1%"></td>
			<TD width="10%"><u><a href="/main/order_details.jsp?orderId=<%= info.getSaleId() %>"><%= info.getSaleId() %></u></a>&nbsp;&nbsp;&nbsp;<a href="javascript:pop('/returns/approve_credit.jsp?orderId=<%= info.getSaleId() %>&complaintId=<%= info.getComplaintId() %>&inPopup=true', 700, 800)">App/Dec</a></TD>
			<TD width="2%"><% if(EnumSaleType.REGULAR.equals(info.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(info.getOrderType())){%>A<%}%></TD>
            <TD width="10%"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></TD>
			<TD width="10%"><%= (info.getSaleStatus() != null) ? info.getSaleStatus().getName() : "--" %></TD>
			<TD width="8%"><%= JspMethods.formatPrice(info.getOrderAmount()) %></TD>
			<TD width="18%"><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
			<TD width="11%"><%= JspMethods.formatPrice(info.getComplaintAmount()) %></TD>
			<TD width="10%"><%= "FDC".equals(info.getComplaintType())?"Store Credit":"CSH".equals(info.getComplaintType())?"Refund":"MIX".equals(info.getComplaintType())?"Mixed":"" %></TD>
			<TD width="20%"><%= info.getComplaintNote() %></TD>
		</TR>
		<tr><td colspan="10" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
		</logic:iterate>
	<% 	} else { %>
	    <tr><td colspan="10" align="center"><br>no pending credits need to be processed<br><br></td></tr>
	<%  }   %>
	</TABLE>
	</div>
</div> 
<table></form></table>
</tmpl:put>

</tmpl:insert>