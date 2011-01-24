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

<fd:getOrdersByStatus id="orders" status="<%=EnumSaleStatus.REFUSED_ORDER%>">

<div class="sub_nav">
<span class="sub_nav_title">Refusals ( <span class="result"><%= orders.size() %></span> )</span>
</div>
<% boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse")); %>
<div class="content" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
<table width="100%" cellpadding="0" cellspacing="2" BORDER="0" class="list_header_text">
    <tr>
		<td width="1%"></td>
		<td width="10%"><a href="/supervisor/refusal_list.jsp?compareBy=0<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Order #</a></td>
		<td width="10%"><a href="/supervisor/refusal_list.jsp?compareBy=1<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Delivery</a></td>
		<td width="11%"><a href="/supervisor/refusal_list.jsp?compareBy=2<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Status</a></td>
		<td width="10%"><a href="/supervisor/refusal_list.jsp?compareBy=3<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Amount</a></td>
		<td width="18%"><a href="/supervisor/refusal_list.jsp?compareBy=4<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Customer Name</a></td>
		<td width="18%"><a href="/supervisor/refusal_list.jsp?compareBy=5<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Email</a></td>
		<td width="11%"><a href="/supervisor/refusal_list.jsp?compareBy=6<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Phone #</a></td>
		<td width="11%"><a href="/supervisor/refusal_list.jsp?compareBy=7<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Alt. Phone #</a></td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
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
		case 5:
			comparator = FDCustomerOrderInfo.EmailComparator; 
			break;
		case 6:
			comparator = FDCustomerOrderInfo.PhoneComparator;
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


	<div class="list_content" style="height: 95%;">
<%	if (orders.size() > 0) { %>
	<table width="100%" cellpadding="0" cellspacing="0" BORDER="0" class="list_content_text">
	<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
	<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId() + "&action=add_return")%>'">
		<td width="1%"></td>
		<td width="10%"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>&action=add_return"><%= info.getSaleId() %></a></td>
		<td width="10%"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></td>
		<td width="11%"><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></td>
		<td width="10%"><%= JspMethods.formatPrice(info.getAmount()) %></td>
		<td width="18%"><%= info.getLastName() %>, <%= info.getFirstName() %></td>
		<td width="18%"><%= info.getEmail() %></td>
		<td width="11%"><%= info.getPhone() %></td>
		<td width="11%"><%= info.getAltPhone() %></td>
	</tr>
	<tr><td colspan="9" class="list_separator" style="padding: 0px;"><img src="/images/clear.gif" width="1" height="1"></td></tr>
	</logic:iterate>
<% 	} else { %>
    <tr><td colspan="9" align="center"><br><br><b>no returned orders need to be processed</b></td></tr>
<%  }   %>
</table>
	</div>
</div>
</fd:getOrdersByStatus>

</tmpl:put>

</tmpl:insert>
