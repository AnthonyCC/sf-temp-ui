<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Reverse Authorize Orders</tmpl:put>

<tmpl:put name='content' direct='true'>
	<%
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
	%>
	<%@ include file="/includes/i_globalcontext.jspf" %>
	<jsp:include page="/includes/admintools_nav.jsp" />

	<crm:CrmReverseAuthOrders id="orders" result="result">
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
			
			<%@ include file="/includes/admintools/i_order_search.jspf"%>
		</div>
		<div>
			<% if (!isPost && orders.size() == 0) { %><strong>There were no orders to reverse authorize.</strong><% } %>
			<% if (!isPost && !orders.isEmpty()) { %>
				&nbsp;<span style="font-weight: normal;">(<%=orders.size()%> orders found) &nbsp;&nbsp;&nbsp;
				Amount marked for resubmittal: <input onFocus="blur();" type=text size="6" name="ordersSelected" id="ordersSelected" readonly value="0" />&nbsp;&nbsp;&nbsp;
				Select: <a href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 100, true, 'first'); return false;">first 100</a>/<a  href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 100, true, 'last'); return false;">last 100</a>/<a  href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 0, true); return false;">all</a> orders&nbsp;&nbsp;
				<a href="#" onClick="javascript:$('ordersSelected').value=selectNCB('resubmit_orders', 0, false); return false;">Clear all</a>&nbsp;&nbsp;&nbsp;
				<input type="submit" value="Reverse auth checked items" />
				</span>
			<% } %>
		</div>

		<% if (!isPost && !orders.isEmpty()) { %>
			<table width="100%" cellspacing="0" border="0" style="empty-cells: show">
				<tr bgcolor="#333366" class="list_header_text">
					<td align="left">&nbsp;</td>
					<td align="left">Order ID</td>
					<td align="left">Store</td>
					<td align="left">Last Action Date</td>
					<td align="left">Status</td>
					<td align="left">Amount</td>
					<td align="left">Customer Name</td>
				</tr>
			</table>
			<div id="result" class="list_content" style="height:76%;">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_orders">
				<%
					int displayedLines = 0;
				%>
				<logic:iterate id="orderInfo" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="idx">
					<%
						/* this page does not match FD eStore */
						String eStoreString = orderInfo.geteStore();
						if (eStoreString.equals("FreshDirect")) {
							eStoreString = "FD";
						}
					%>
					<% if ( 
						((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(eStoreString)) &&
						((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(orderInfo.getFacility()))
					) { %>
						<tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
							<td class="border_bottom">&nbsp;</td>
							<td class="border_bottom">
								<input name="resubmitSaleId" type="checkbox" onClick="countChecked(this);" value="<%=orderInfo.getSaleId()%>">
								<% if (orderInfo.getSaleId().indexOf("X")!=-1) {%>
									<a href="/main/order_details.jsp?orderId=<%=orderInfo.getSaleId()%>"><%=orderInfo.getSaleId()%></a>&nbsp;
								<% } else { %>
									<%=orderInfo.getSaleId()%>
								<% } %>	
							</td>
							<td class="border_bottom"><%=orderInfo.geteStore()%></td>
							<td class="border_bottom"><%=orderInfo.getLastCroModDate()%></td>
							<td class="border_bottom"><%=orderInfo.getOrderStatus()%></td>
							<td class="border_bottom"><%=orderInfo.getAmount()%></td>
							<td class="border_bottom"><%=orderInfo.getFirstName()+" "+orderInfo.getLastName()%>&nbsp;</td>
						</tr>
						<% displayedLines++;  %>
					<% } %>
				</logic:iterate>
				<% if (displayedLines == 0) { %>
					<tr><td colspan="10" align="center"><strong>There were no orders to reverse authorize.</strong></td></tr>
				<% } %>
				</table>
			</div>
		<% } %>
	</form>
	</crm:CrmReverseAuthOrders>
</tmpl:put>
</tmpl:insert>