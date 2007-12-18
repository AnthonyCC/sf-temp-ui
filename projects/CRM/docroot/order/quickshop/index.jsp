<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" redirectPage='/main/index.jsp?noUser=true' />

<% request.setAttribute("needsCCL","true"); %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : New Order > Quickshop /</tmpl:put>
<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<tmpl:put name='header' direct='true'><jsp:include page='/includes/customer_header.jsp'/></tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<%	//
	// Get index of current search term
	//
	int idx = 0;
	if (request.getParameter("searchIndex") != null) {
		idx = Integer.parseInt( request.getParameter("searchIndex") );
	}  
%>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order">
	<TR VALIGN="TOP">
		<TD WIDTH="60%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
				<TR>
					<TD WIDTH="70%">
						Previous Orders
						&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="/order/quickshop/every_item.jsp">Every Item Ordered</a>
						<fd:GetCustomerRecipeList id="recipes">
							&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="/order/quickshop/recipes.jsp">Recipes</a>
						</fd:GetCustomerRecipeList>
					</TD>
					<TD WIDTH="30%" ALIGN="RIGHT"> [ <A HREF="/order/place_order_batch_search_results.jsp?searchIndex=<%= idx %>">back to list in progress</A> ]</TD>
				</TR>
			</TABLE>
			<hr class="gray1px">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
			<TR bgcolor="#dddddd">
				<TD WIDTH="25%"><b>Order #</b></TD>
				<TD WIDTH="25%"><b>Delivery Date</b></TD>
				<TD WIDTH="25%"><b>Price</b></TD>
				<TD WIDTH="25%"><b>Shop</b></TD>
			</TR>
		<fd:OrderHistoryInfo id='orderHistory'>
			<logic:iterate id="order" collection="<%= orderHistory %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI" indexId="rowCounter">
			<TR bgcolor="<%= (rowCounter.intValue() % 2 == 0) ? "#FFFFFF" : "#EEEEEE" %>">
				<TD WIDTH="25%"><a href="/order/quickshop/shop_from_order.jsp?orderId=<%= order.getErpSalesId() %>"><%= order.getErpSalesId() %></a><BR></TD>
				<TD WIDTH="25%"><%= CCFormatter.formatDate( order.getRequestedDate() ) %><BR></TD>
				<TD WIDTH="25%"><%= CCFormatter.formatCurrency( order.getTotal() ) %><BR></TD>
				<TD WIDTH="25%"><a href="/order/quickshop/shop_from_order.jsp?orderId=<%= order.getErpSalesId() %>">shop from this order</a><BR></TD>
			</TR>
			</logic:iterate>
		</fd:OrderHistoryInfo>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">

</tmpl:put>
</tmpl:insert>
