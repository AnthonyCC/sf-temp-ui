<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%
String certNum = request.getParameter("certNum");
%>
<fd:GetRedemedGiftCardOrders id='orders' certNum="<%=certNum %>">
	<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1">
		<!-- <tr>
			<td colspan="4" class="gc_colHeader">Recent Orders with Gift Card Purchase</td>
		</tr> -->
		<tr class="gc_colHeader">
			<th style="width: 85px;">Order</th>
			<th style="width: 90px;">Delivery Date</th>
			<th>Amount</th>
			<th style="width: 85px;">Status</th>							
		</tr>
		<logic:iterate id="order" collection="<%= orders %>" type="com.freshdirect.customer.ErpSaleInfo" indexId="counter3">
			<%
			Integer counterTmp = (Integer) pageContext.getAttribute("counter3");
			if (counterTmp.intValue() <=4) { %>
				<tr <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
					<td><a href="/main/order_details.jsp?orderId=<%= order.getSaleId() %>"><%= order.getSaleId() %></a></td>
					<td><%= order.getRequestedDate() %></td>
					<td>$<%= order.getAmount() %></td>
					<td><%= order.getStatus().getName() %></td>
				</tr>
			<% } %> 
		</logic:iterate>

		<% if (orders.size() == 0){%><tr><td colspan="4">No activity.</td></tr><%} %>
	</table>
</fd:GetRedemedGiftCardOrders>        