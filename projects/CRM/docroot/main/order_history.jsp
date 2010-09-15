<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
    private String sourceToMethod(EnumTransactionSource source) {
        return EnumTransactionSource.CUSTOMER_REP.equals(source) ? "CSR" : EnumTransactionSource.SYSTEM.equals(source) ? "SYSTEM" : "CUSTOMER";
    }
%>

<%
    FDUserI user = (FDSessionUser) session.getAttribute(SessionName.USER);
%>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Order History</tmpl:put>
	
		<tmpl:put name='content' direct='true'>
<% Collection orderInfos = ((FDOrderHistory)user.getOrderHistory()).getFDOrderInfos(); %>

<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="7%">Order #</td>
		<td width="3%">Type</td>
		<td width="8%">Delivery</td>
		<td width="8%">Status</td>
		<td width="7%" align="center">Amount</td>
		<td width="8%" align="center">Method</td>
		<td width="11%">Created</td>
		<td width="8%">by</td>
		<td width="6%">via</td>
		<td width="11%">Modified</td>
		<td width="8%">by</td>
		<td width="6%">via</td>
		<td width="10%" align="center">Credits<br><span style="font-weight: normal; font-size: 8pt;">Approved / Pending</span></td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
</table>
</div>

<div class="list_content">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<%	if ( orderInfos.isEmpty() ) { %>
	<tr>
		<td></td>
		<td colspan="10" align="center"><br><b>No orders found.</b></td>
		<td></td>
	</tr>
<% 	} %>
	<logic:iterate id="order" collection="<%= orderInfos %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI" indexId="counter">
<%
		String createdBy = sourceToMethod(order.getOrderSource())+ (order.getCreatedBy()!=null ? " / "+order.getCreatedBy() : "");
		String modifiedBy = sourceToMethod(order.getModificationSource())+ (order.getModifiedBy()!=null ? " / "+order.getModifiedBy() : "");
		String paymentMethodType = (order.getPaymentMethodType() != null) ? order.getPaymentMethodType().getDescription() : "&nbsp;";
		String styleClassName = order.isDlvPassApplied() ? "dlv_pass_used" : "border_bottom";
		styleClassName=EnumSaleType.SUBSCRIPTION.equals(order.getSaleType())?"subscription_order" :styleClassName;
%>
	
	<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer; padding: 2px;" onClick="document.location='/main/order_details.jsp?orderId=<%= order.getErpSalesId() %>'">
		<td width="7%" class='<%= styleClassName %>'><a class="key"><%= order.getErpSalesId() %></a></td>
		<td width="3%" class='<%= styleClassName %>'><% if(EnumSaleType.REGULAR.equals(order.getSaleType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(order.getSaleType())){%><Font color="red">A</Font>&nbsp;<% }else if(EnumSaleType.GIFTCARD.equals(order.getSaleType())){%><Font color="orange">G</Font><%}%>&nbsp;</td>
		<td width="8%" class='<%= styleClassName %>'><% if(EnumSaleType.REGULAR.equals(order.getSaleType())){%><%= CCFormatter.formatDate(order.getRequestedDate()) %><%}else {}%>&nbsp;</td>
		<td width="8%" class='<%= styleClassName %>'><span class="log_info"><%= order.getSaleStatus().getName() %></span></td>
		<td width="7%" align="center" class='<%= styleClassName %>'><%= JspMethods.formatPrice(order.getTotal()) %></td>
		<td width="8%" align="center" class='<%= styleClassName %>'><%=paymentMethodType%></td>
		<td width="11%" class='<%= styleClassName %>'><span class="time_stamp"><%= CCFormatter.formatDateTime(order.getCreateDate()) %></span></td>
		<td width="8%" class='<%= styleClassName %>'><span class="log_info"><%= createdBy %></span></td>
		<td width="6%" class='<%= styleClassName %>'><span class="log_info"><%= order.getOrderSource().getName() %></span></td>
		<td width="11%" class='<%= styleClassName %>'><span class="time_stamp"><%= CCFormatter.formatDateTime(order.getModificationDate()) %></span></td>
		<td width="8%" class='<%= styleClassName %>'><span class="log_info"><%= modifiedBy %></span></td>
		<td width="6%" class='<%= styleClassName %>'><span class="log_info"><%= order.getModificationSource().getName() %></span></td>
		<td width="10%" class='<%= styleClassName %>' align="center">
            <%= JspMethods.formatPrice(order.getApprovedCreditAmount()) %>
            <% if (order.getPendingCreditAmount() > 0) { %> 
                / <b><span class="error_detail"><%= JspMethods.formatPrice(order.getPendingCreditAmount()) %></span></b>
            <% } %>
	        </td>
	</tr>
	</logic:iterate>
</table>
</div>
	</tmpl:put>

</tmpl:insert>


