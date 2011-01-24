<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Payment Exceptions</tmpl:put>

<tmpl:put name='content' direct='true'>

<%	Collection sales = CallCenterServices.getFailedAuthorizationSales(); %>

<div class="sub_nav">
<span class="sub_nav_title">Payment Exceptions ( <span class="result"><%= sales.size() %></span> )</span> A = Automatic Order   M = Manual Order
</div>

<div class="content" style="height: 80%;">
	<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="1%"></td>
		<td width="5%">Order #</td>
		<td width="2%">Type</td>
		<td width="12%">Delivery</td>
		<td width="12%">Status</td>
		<td width="10%" align="center">Amount</td>
		<td width="12%">Created</td>
		<td width="9%">by</td>
		<td width="8%">via</td>
		<td width="12%">Modified</td>
		<td width="9%">by</td>
		<td width="8%">via</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	</table>
</div>

<div class="list_content" style="height: 95%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<logic:iterate id="order" collection="<%= sales %>" type="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter" indexId="idx">
	<fd:OrderSummary id="summary" order="<%= order %>">
	<tr valign="top" <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='/main/order_details.jsp?orderId=<%=summary.getOrderId()%>'">
		<td width="1%"></td>
		<td width="5%"><a class="key"><b><%=summary.getOrderId()%></b></a></td>
		<td width="2%"><% if(EnumSaleType.REGULAR.equals(summary.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(summary.getOrderType())){%>A<%}%></td>
		<td width="12%"><%=CCFormatter.formatDeliveryDate(summary.getDeliveryDate())%></td>
		<td width="12%"><%=summary.getOrderStatus()%></td>
		<td width="10%" align="center"><%=JspMethods.formatPrice(summary.getOrderTotal())%></td>
		<td width="12%" class="time_stamp"><%=CCFormatter.formatDateTime(summary.getCreateDate())%></td>
		<td width="9%" class="log_info"><%=summary.getCreatedBy()%></td>
		<td width="8%" class="log_info"><%=summary.getCreateSource()%></td>
		<td width="12%" class="time_stamp"><%=CCFormatter.formatDateTime(summary.getLastModifiedDate())%></td>
		<td width="9%" class="log_info"><%=summary.getLastModifiedBy()%></td>
		<td width="8%" class="log_info"><%=summary.getLastModifiedSource()%></td>
	</tr>
	<tr class="list_separator" style="padding: 0px;"><td colspan="12"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
	</fd:OrderSummary>
</logic:iterate>
	</table>
	</div>
</div>
</tmpl:put>


</tmpl:insert>
