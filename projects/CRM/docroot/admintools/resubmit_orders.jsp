<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Re-Submit Orders</tmpl:put>

<tmpl:put name='content' direct='true'>
	<%
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
	%>
	<%@ include file="/includes/i_globalcontext.jspf" %>
	<jsp:include page="/includes/admintools_nav.jsp" />

	<crm:CrmResubmitOrders id="nsmOrders" result="result">
	<form method='POST' name="frmResubmitOrders">

		<% if (!result.isSuccess() ) { %>
			<div id="result" class="list_content" style="height:70%;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
				<logic:iterate id="errs" collection="<%= result.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
				<tr>
					<td class="border_bottom">&nbsp;</td>
					<td class="border_bottom"><%=errs.getDescription()%></td>
				</tr>
				</logic:iterate>
				</table>
			</div>
		<% } %>
		
		<div class="sub_nav sub_nav_title">
			Resubmit Orders that are in Non-Submitted Mode
		</div>
		<div>
			<% if (!isPost && nsmOrders.size() == 0) { %><strong>There were no Non-Submitted Orders found.</strong><% } %>
			<% if (!isPost && !nsmOrders.isEmpty()) { %>
				&nbsp;<span style="font-weight: normal;">(<%=nsmOrders.size()%> orders found) &nbsp;&nbsp;&nbsp;
				Amount marked for resubmittal: <input onFocus="blur();" type=text size="6" name="ordersSelected" id="ordersSelected" readonly value="0" />&nbsp;&nbsp;&nbsp;
				Select: <a href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 100, true, 'first'); return false;">first 100</a>/<a  href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 100, true, 'last'); return false;">last 100</a>/<a  href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 0, true); return false;">all</a> orders&nbsp;&nbsp;
				<a href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 0, false); return false;">Clear all</a>&nbsp;&nbsp;&nbsp;
				<input type="submit" value="resubmit checked items" />
				</span>
			<% } %>
		</div>

		<% if (!isPost && !nsmOrders.isEmpty()) { %>
			<div class="list_header">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show">
					<tr bgcolor="#333366" class="list_header_text">
						<td class="resubOrd-spacer">&nbsp;</td>
						<td class="resubOrd-ordId">Order ID</td>
						<td class="resubOrd-lastActDate">Last Action Date</td>
						<td class="resubOrd-status">Status</td>
						<td class="resubOrd-ordAmt">Amount</td>
						<td class="resubOrd-dlvDate">Delivery Date</td>
						<td class="resubOrd-custName">Customer Name</td>
						<td class="resubOrd-store">Store</td>
						<td class="resubOrd-facility">Facility</td>
						<td class="resubOrd-spacer-auto">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div id="result" class="list_content" style="height:76%;">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_orders">
				<%
					int displayedLines = 0;
				%>
				<logic:iterate id="orderInfo" collection="<%= nsmOrders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="idx">
					<% if ( 
						((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(orderInfo.geteStore())) &&
						((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(orderInfo.getFacility()))
					) { %>
						<tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
							<td class="resubOrd-spacer border_bottom">&nbsp;</td>
							<td class="resubOrd-ordId border_bottom">
								<input name="resubmitSaleId" type="checkbox" onClick="countChecked(this);" value="<%=orderInfo.getSaleId()%>">
								<a href="/main/order_details.jsp?orderId=<%=orderInfo.getSaleId()%>"><%=orderInfo.getSaleId()%></a>&nbsp;
							</td>
							<td class="resubOrd-lastActDate border_bottom"><%=orderInfo.getLastCroModDate()%></td>
							<td class="resubOrd-status border_bottom"><%=orderInfo.getOrderStatus()%></td>
							<td class="resubOrd-ordAmt border_bottom"><%=orderInfo.getAmount()%></td>
							<td class="resubOrd-dlvDate border_bottom"><%=orderInfo.getDeliveryDate()%>&nbsp;</td>
							<td class="resubOrd-custName border_bottom"><%=orderInfo.getFirstName()+" "+orderInfo.getLastName()%>&nbsp;</td>
							<td class="resubOrd-store border_bottom"><%=orderInfo.geteStore() %></td>
							<td class="resubOrd-facility border_bottom"><%=orderInfo.getFacility() %></td>
							<td class="resubOrd-spacer-auto border_bottom">&nbsp;</td>
						</tr>
						<% displayedLines++;  %>
					<% } %>
				</logic:iterate>
				<% if (displayedLines == 0) { %>
					<tr><td colspan="10" align="center"><strong>There were no Non-Submitted Orders found.</strong></td></tr>
				<% } %>
				</table>
			</div>
		<% } %>
	</form>
	</crm:CrmResubmitOrders>
</tmpl:put>
</tmpl:insert>