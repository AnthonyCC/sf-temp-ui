<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Refusals</tmpl:put>

<tmpl:put name='content' direct='true'>

<%@ include file="/includes/i_globalcontext.jspf" %>
<fd:getOrdersByStatus id="orders" status="<%=EnumSaleStatus.REFUSED_ORDER%>">

<div class="sub_nav">
<span class="sub_nav_title">Refusals ( <span class="result"><%= orders.size() %></span> )</span>
</div>
<% boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse")); %>
<div class="" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
<table width="100%" cellpadding="0" cellspacing="0" BORDER="0">
    <tr>
		<td class="pendRef-spacer"></td>
		<td class="pendRef-ordId"><a href="/supervisor/refusal_list.jsp?compareBy=0<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Order #</a></td>
		<td class="pendRef-dlvDate"><a href="/supervisor/refusal_list.jsp?compareBy=1<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Delivery</a></td>
		<td class="pendRef-status"><a href="/supervisor/refusal_list.jsp?compareBy=2<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Status</a></td>
		<td class="pendRef-ordAmt"><a href="/supervisor/refusal_list.jsp?compareBy=3<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Amount</a></td>
		<td class="pendRef-custName"><a href="/supervisor/refusal_list.jsp?compareBy=4<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Customer Name</a></td>
		<td class="pendRef-store"><a href="/supervisor/refusal_list.jsp?compareBy=8<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Store</a></td>
		<td class="pendRef-facility"><a href="/supervisor/refusal_list.jsp?compareBy=9<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Facility</a></td>
		<td class="pendRef-email"><a href="/supervisor/refusal_list.jsp?compareBy=5<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Email</a></td>
		<td class="pendRef-hPhone"><a href="/supervisor/refusal_list.jsp?compareBy=6<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Phone #</a></td>
		<td class="pendRef-aPhone"><a href="/supervisor/refusal_list.jsp?compareBy=7<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Alt. Phone #</a></td>
		<td class="pendRef-spacer"></td>
	</tr>
	</table>
</div>

<%	Comparator comparator = null;
	String compareByParam = request.getParameter("compareBy");
	int compareBy = 0;
	if (compareByParam != null) {
		compareBy = Integer.parseInt(compareByParam);
	}
	switch (compareBy) {
		case 0:
			comparator = FDCustomerOrderInfo.SaleIdComparator; 
			break;
		case 1:
			comparator = FDCustomerOrderInfo.DeliveryDateComparator;
			break;
		case 2:
			comparator = FDCustomerOrderInfo.OrderStatusComparator; 
			break;
		case 3:
			comparator = FDCustomerOrderInfo.AmountComparator; 
			break;
		case 4:
			comparator = FDCustomerOrderInfo.CustomerNameComparator; 
			break;
		case 8:
			comparator = FDCustomerOrderInfo.CustomerNameComparator; 
			break;
		case 9:
			comparator = FDCustomerOrderInfo.CustomerNameComparator; 
			break;
		case 5:
			comparator = FDCustomerOrderInfo.StoreComparator; 
			break;
		case 6:
			comparator = FDCustomerOrderInfo.FacilityComparator;
			break;
		case 7:
			comparator = FDCustomerOrderInfo.AltPhoneComparator;
			break;
		default:
			comparator = FDCustomerOrderInfo.DeliveryDateComparator;
			break;
	}

	Collections.sort(orders, comparator);
	if (sortReversed)
		Collections.reverse(orders);
%>


	<div class="list_content" style="height: 95%; padding: 0px;">
<%	if (orders.size() > 0) { %>
	<table width="100%" cellpadding="0" cellspacing="0" BORDER="0">
	<%
		int displayedLines = 0;
	%>
	<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
	<% if ( 
				((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(info.geteStore())) &&
				((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(info.getFacility()))
	) { %>
		<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId() + "&action=add_return")%>'">
			<td class="pendRef-spacer"></td>
			<td class="pendRef-ordId"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>&action=add_return"><%= info.getSaleId() %></a></td>
			<td class="pendRef-dlvDate"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></td>
			<td class="pendRef-status"><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></td>
			<td class="pendRef-ordAmt"><%= JspMethods.formatPrice(info.getAmount()) %></td>
			<td class="pendRef-custName"><%= info.getLastName() %>, <%= info.getFirstName() %></td>
			<td class="pendRef-store"><%= info.geteStore() %></td>
			<td class="pendRef-facility"><%= info.getFacility() %></td>
			<td class="pendRef-email"><%= info.getEmail() %></td>
			<td class="pendRef-hPhone"><%= info.getPhone() %></td>
			<td class="pendRef-aPhone"><%= info.getAltPhone() %></td>
			<td class="pendRef-spacer"></td>
		</tr>
		<tr><td colspan="12" class="list_separator" style="padding: 0px;"><img src="/images/clear.gif" width="1" height="1"></td></tr>
		<% displayedLines++;  %>
	<% } %>
	</logic:iterate>
	<% if (displayedLines == 0) { %>
		<tr><td colspan="12" align="center"><br><br><b>no returned orders need to be processed</b></td></tr>
	<% } %>
<% 	} else { %>
    <tr><td colspan="12" align="center"><br><br><b>no returned orders need to be processed</b></td></tr>
<%  }   %>
</table>
	</div>
</div>
</fd:getOrdersByStatus>

</tmpl:put>

</tmpl:insert>
