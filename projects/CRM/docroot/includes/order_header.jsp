<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.content.ProductRef" %>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>

<%@ taglib uri='freshdirect' prefix='fd' %>


<%
String pageURI = request.getRequestURI();

FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

boolean hasCustomerCase = CrmSession.hasCustomerCase(session);

boolean isCheckout = pageURI.indexOf("/checkout") > -1;
boolean step0 = pageURI.indexOf("checkout_review_items") > -1;
boolean step1 = pageURI.indexOf("checkout_select_address") > -1;
boolean step2 = pageURI.indexOf("checkout_delivery_time") > -1;
boolean step3 = pageURI.indexOf("checkout_select_payment") > -1;
boolean step4 = pageURI.indexOf("checkout_preview") > -1;
boolean step5 = pageURI.indexOf("checkout_confirmation") > -1;

if(!isCheckout)
    session.removeAttribute("makeGoodOrder");

boolean makegood = "true".equals(request.getParameter("makegood")) || "true".equals(session.getAttribute("makeGoodOrder"));
String referencedOrder = request.getParameter("orig_sale_id") != null ? request.getParameter("orig_sale_id") : (String)session.getAttribute("referencedOrder");

%>

<script language="javascript">
	
	function confirmCancel(activityType, redirectPage, redirectURL) {
		var doCancel = confirm ("Are you sure you want to cancel " + activityType + " and return to " + redirectPage + "?");
		if (doCancel == true) {
			jumpTo(redirectURL);
		}
	}
	
	function jumpTo(locationURL) {
		if (locationURL==null) return false;
		window.location=locationURL;
	}
	
</script>

	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav">
		<tr>
			<td width="70%"  class="sub_nav_text" <% if(makegood){%> bgcolor='FF7700'<%}%>>
			<% if (isCheckout) { %>
                <% if (makegood) { %>
			        <b><a href="<%= response.encodeURL("/order/quickshop/makegood_from_order.jsp?orderId="+referencedOrder) %>">Build Make Good Order</a><span class="order_step">&nbsp;&nbsp;|&nbsp;&nbsp;Makegood Checkout</span></b><%if(step0){%><br>(Each item must have a make good reason assigned to it before proceeding.)<%}%><% if (step1 || step2 || step3 || step4) { %>&nbsp;&nbsp;<a href="<%= response.encodeURL("/checkout/checkout_review_items.jsp?makegood=true&orig_sale_id="+referencedOrder) %>"class="order">Review items</a><% } %><% if (step2 || step3 || step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_select_address.jsp" class="order">1: Delivery address</a><% } %><% if (step3 || step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_delivery_time.jsp" class="order">2: Delivery time</a><% } %><% if (step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_select_payment.jsp" class="order">3: Payment Method</a><% } %>
			    <% } else { %>
                    <b><a href="<%= response.encodeURL("/order/place_order_build.jsp") %>">Build Order</a><span class="order_step">&nbsp;&nbsp;|&nbsp;&nbsp;Checkout</span></b><% if (step1 || step2 || step3 || step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_review_items.jsp" class="order">Review items</a><% } %><% if (step2 || step3 || step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_select_address.jsp" class="order">1: Delivery address</a><% } %><% if (step3 || step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_delivery_time.jsp" class="order">2: Delivery time</a><% } %><% if (step4) { %>&nbsp;&nbsp;<a href="/checkout/checkout_select_payment.jsp" class="order">3: Payment Method</a><% } %>
                <% } %>
			<% } else { %>
			<b><span class="order_step">Build Order: <a href="<%= response.encodeURL("/order/place_order_build.jsp") %>" class="order_step">Search</a> &middot; <a href="<%= response.encodeURL("/order/build_order_browse.jsp") %>" class="order_step">Browse</a><% if ( user != null && user.getIdentity() != null ) { %> &middot; <a href="/order/quickshop/index.jsp" class="order_step">QuickShop</a> &middot; <a href="<%= response.encodeURL("/order/build_order_recipes.jsp") %>" class="order_step">Recipes</a><fd:CCLCheck> &middot; <a href="/order/quickshop/all_lists.jsp" class="order_step">Lists</a></fd:CCLCheck><% } %>&nbsp;&nbsp;|&nbsp;&nbsp;</span><%if(hasCustomerCase){%><a href="<%= response.encodeURL("/checkout/checkout_review_items.jsp") %>">Checkout</a><%} else {%><span class="cust_module_content_edit">Case required to checkout</span><% } %></b>
			<% } %>
			</td>
			<td width="30%" align="right"  class="sub_nav_text" <% if(makegood){%> bgcolor='FF7700'<%}%>>
			<% if (isCheckout) { %>
                <% if (makegood) { %>
				    <% if (pageURI.indexOf("review_items") > -1) { %>
					    <a href="javascript:confirmCancel('checkout', 'shopping', '<%= response.encodeURL("/order/quickshop/makegood_from_order.jsp?orderId="+referencedOrder) %>')" class="new">CANCEL</a>
				    <% } else if (pageURI.indexOf("confirmation") < 0) { %>
					    <a href="javascript:confirmCancel('checkout', 'review items', '<%= response.encodeURL("/checkout/checkout_review_items.jsp?makegood=true&orig_sale_id="+referencedOrder) %>')" class="new">CANCEL</a>
				    <% } %>
                <% } else { %>
				    <% if (pageURI.indexOf("review_items") > -1) { %>
					    <a href="javascript:confirmCancel('checkout', 'shopping', '<%= response.encodeURL("/order/place_order_build.jsp") %>')" class="cancel">CANCEL</a>
				    <% } else if (pageURI.indexOf("confirmation") < 0) { %>
					    <a href="javascript:confirmCancel('checkout', 'review items', '<%= response.encodeURL("/checkout/checkout_review_items.jsp") %>')" class="cancel">CANCEL</a>
				    <% } %>
                <% } %>
			<% } else { %>
			<a href="javascript:confirmCancel('shopping', 'account details', '<%= response.encodeURL("/main/account_details.jsp") %>')" class="cancel">CANCEL</a>
			<% } %></td>
		</tr>
		<%--tr><td colspan="2" align="center" bgcolor="#FF9933"></td></tr--%>
	</table>

<% if (user.isEligibleForPreReservation() && user.getReservation() != null) { %>
<div class="content_fixed" align="center" style="width:100%; background: #EEEEEE;"><%@ include file="/common/template/includes/i_reservation_warning.jspf"%></div>
<% } %>
<%	//
	// Get index of current search term
	//
	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
	
	String leftColumnTitle = "";
	boolean showFindBox = true;
	boolean showSubHeader = true;
	
	if (pageURI.indexOf("checkout") > -1) { 
		showSubHeader = false;
	} else if (pageURI.indexOf("quickshop") > -1) {
		leftColumnTitle = "Quickshop";
	} else if (pageURI.indexOf("place_order_build") > -1) {
		leftColumnTitle = "Build List";
		showFindBox = false;
	} else if (pageURI.indexOf("build_order_category") > -1 || pageURI.indexOf("build_order_browse") > -1) {
		leftColumnTitle = "Browse";
	} else if (pageURI.indexOf("place_order_batch_search_results") > -1) {
		leftColumnTitle = "Search Results";
	} else if (pageURI.indexOf("product.jsp") > -1) {
		leftColumnTitle = "Product Details";
	} else if (pageURI.indexOf("product_modify") > -1) {
		leftColumnTitle = "Modify Item in Cart";
	} else if (pageURI.indexOf("place_order_related_items") > -1) {
		String productId = request.getParameter("productId");
		String categoryId = request.getParameter("catId");
		leftColumnTitle = "Related Items for ";
		ProductModel originalProduct = (new ProductRef(categoryId, productId)).lookupProduct();
		leftColumnTitle += originalProduct.getFullName();
	}
%>

<% if (showSubHeader) { %>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header" style="padding-top: 1px; padding-bottom: 1px;">
		<tr>
			<td width="<%= showFindBox ? "29":"59" %>%" style="padding-left: 8px; padding-right: 8px;"><b><%=leftColumnTitle%></b></td>
			<% if (showFindBox) { %>
				<form name="quickfind" method="post" action="/order/place_order_batch_search_results.jsp">
				<td width="30%">Find: <input name="new_term" type="text" size="13"> <input type="submit" class="submit" value="GO"></td>
				<%	String origSearchParams = (String) session.getAttribute(SessionName.LIST_SEARCH_RAW); %>
					<input type="hidden" name="search_pad" value="<%= origSearchParams %>">
					<input type="hidden" name="searchIndex" value="<%= searchIdx %>">
				</form>
			<% } %>
			<td width="40%" style="padding-left: 8px; padding-right: 8px;"><table width="100%" cellpadding="0" cellspacing="0" class="list_header_text"><tr><td><b>Cart</b></td><td align="right"><%if(hasCustomerCase){%><a href="<%= response.encodeURL("/checkout/checkout_review_items.jsp") %>" class="checkout" style="width: 100px;">CHECKOUT >></a><%}%></td></tr></table></td>
			</td>
		</tr>
	</table>
<% } %>
