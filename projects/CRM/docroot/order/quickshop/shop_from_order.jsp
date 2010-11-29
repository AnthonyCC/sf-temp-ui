<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>
<%@ page import='com.freshdirect.fdstore.customer.adapter.FDOrderAdapter' %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>


<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% request.setAttribute("needsCCL","true"); %>

<%
RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
%>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Shop from Order /</tmpl:put>
<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
%>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<%	//
	// Get index of current search term
	//
	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR>
		<TD WIDTH="60%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
				<TR>
					<TD WIDTH="75%">
						Previous Order (<a href="/order/quickshop/index.jsp">All</a>)
						&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="/order/quickshop/every_item.jsp">Shop from Every Item Ordered</a>
						<fd:GetCustomerRecipeList id="recipes">
							&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="/order/quickshop/recipes.jsp">Recipes</a>
						</fd:GetCustomerRecipeList>
					</TD>
					<TD WIDTH="25%" ALIGN="RIGHT"> [ <A HREF="/order/place_order_batch_search_results.jsp?searchIndex=<%= searchIdx %>">back to list in progress</A> ]</TD>
				</TR>
			</TABLE>
			<hr class="gray1px">
			<div class="order_detail">We've remembered every item, just the way you like it. Use your previous purchases to re-order with a couple clicks. Just update the quantities (or delete items) and then add them to your cart.<br></div>
			<hr class="gray1px">

<%	String orderId = request.getParameter("orderId");
	FDCartModel order = new FDCartModel();
	// FDOrderI origOrder = null;
	FDOrderI currentOrder = FDCustomerManager.getOrder(user.getIdentity(), orderId);
%>

<fd:OrderHistoryInfo id='orderInfos'>

			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<TR bgcolor="#ffffff">
					<TD WIDTH="90%">
					<logic:iterate id="lastOrder" length="8" collection="<%= orderInfos %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
<% if (lastOrder.getErpSalesId().equals(orderId)) { %>
						<b><%= dateFormatter.format( lastOrder.getRequestedDate() ) %> (<%= JspMethods.formatPrice( lastOrder.getTotal() ) %>)</b>. &nbsp;
<% } else { %>	
						<a href="/order/quickshop/shop_from_order.jsp?orderId=<%= lastOrder.getErpSalesId() %>"><%= dateFormatter.format( lastOrder.getRequestedDate() ) %></a> (<%= JspMethods.formatPrice( lastOrder.getTotal() ) %>) &middot;&nbsp;
<% } %>
					</logic:iterate>
					<BR>
					</TD>
					<TD WIDTH="10%">
						<A HREF="/order/quickshop/index.jsp">view all &gt;</A><br/>
					</TD>
				</TR>
				
				<TR bgcolor="#ffffff">
					<td colspan="2" align="right">
					<% if ( (currentOrder.getCartonContents() != null) &&  (currentOrder.getCartonContents().size() > 0) ){ %>
					<a href="javascript:pop('/main/carton_contents_view.jsp?showForm=true&orderId=<%= orderId %>&scroll=yes','600','800');">Carton Contents</a>
					<% } %>
					</td>
				</TR>
			</TABLE>
<% String actionName = request.getParameter("fdAction");
	if (actionName == null) {
		actionName = "";
	}
%>
<fd:QuickShopController id="quickCart" orderId="<%= orderId %>" action="<%= actionName %>">
<%	String qsPage = "shop_from_order.jsp"; 
	String qsDeptId = null;
	boolean hasDeptId = false;
	request.setAttribute("crm_source","true");
	final boolean isStandingOrderPage = false;
%>
<%@ include file="/shared/quickshop/includes/i_vieworder.jspf" %>
</fd:QuickShopController>

</fd:OrderHistoryInfo>
			
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
