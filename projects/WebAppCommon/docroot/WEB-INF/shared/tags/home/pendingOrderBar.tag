<%@tag import="com.freshdirect.fdstore.EnumEStoreId"%>
<%@tag import="com.freshdirect.webapp.util.StandingOrderHelper"
	import="com.freshdirect.fdstore.promotion.SignupDiscountRule"
	import="com.freshdirect.fdstore.customer.FDUserI"
	import="java.util.ArrayList"
	import="java.util.List"
	import="com.freshdirect.fdstore.customer.FDOrderInfoI"
	import="java.text.SimpleDateFormat"
	import="com.freshdirect.fdlogistics.model.FDTimeslot"
	import="java.util.Date"
	import="com.freshdirect.framework.util.DateUtil"
	import="com.freshdirect.fdstore.content.util.DeliveryDateComparator"
	import="java.util.Collection"
	import="com.freshdirect.fdstore.standingorders.*"
	import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"
	import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"
	import="com.freshdirect.webapp.util.JspMethods"
	import="com.freshdirect.fdstore.customer.FDCartModel"
	import="com.freshdirect.fdstore.customer.FDModifyCartModel"
%><%@ attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %><%@
attribute name="modifyOrderAlert" required="false" rtexprvalue="true" type="java.lang.Boolean"
%><%
	if (user != null && user.getLevel() >= FDUserI.RECOGNIZED) {
		FDCartModel modifyOrderBarTagCart = user.getShoppingCart();
		boolean isModifyingOrder = modifyOrderBarTagCart instanceof FDModifyCartModel;
		
		if (!isModifyingOrder) {
			boolean inMobWebTemplate = (request.getAttribute("inMobWebTemplate") != null) ? (Boolean)request.getAttribute("inMobWebTemplate") : false;
			boolean isMobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
			
			int pendingOrderCount = 0;
			
			/* if there is any order in 'NEW' status, clear the order history to make the order modifiable */
			if (user.isAnyNewOrder()){
				user.invalidateOrderHistoryCache();
				user.setAnyNewOrder(false);
			}
			
			List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();
			validPendingOrders.addAll(user.getPendingOrders());
			
			/* SO orders */
			Map<String, String> soUpcomingDelivery = new HashMap<String,String>();

			/* Only for FD */
			if (user.getUserContext() != null && EnumEStoreId.FD.equals(user.getUserContext().getStoreContext().getEStoreId())) {
				Collection<FDStandingOrder> sos = FDStandingOrdersManager.getInstance().loadCustomerNewStandingOrders( user.getIdentity());
				sos = FDStandingOrdersManager.getInstance().getAllSOUpcomingOrders( user, sos);
				for (FDStandingOrder fd:sos) {
					soUpcomingDelivery.put(fd.getUpcomingDelivery().getErpSalesId(), fd.getCustomerListName());
				}
			}
			
			if (validPendingOrders.size() > 0) {
				/* sort pending orders based on delivery date (the closer date goes first) */
				Collections.sort(validPendingOrders, new DeliveryDateComparator());
				
				%>
				<div id="pendingOrderBarWrapper" class="sticky-fixed-wrapper">
					<%-- PENDING ORDER OVERLAY --%>
					<div id="" class="pendingOrderBar-overlay-cont" style="display: none;">
					<%
						String orderName = "";
						String orderDOW = "";
						String orderDate = "";
						String orderTime = "";
						String orderId = "";
						boolean isSoOrder = false;
						for (FDOrderInfoI item : validPendingOrders) {
							isSoOrder = false;
							orderName = item.getErpSalesId();
							if (soUpcomingDelivery.containsKey(item.getErpSalesId())) {
								orderName = soUpcomingDelivery.get(item.getErpSalesId());
								isSoOrder = true;
							}
							orderDOW = new SimpleDateFormat("EEEEE").format(item.getRequestedDate());
							orderDate = new SimpleDateFormat("EEEEE, MMM d").format(item.getRequestedDate());
							orderTime = DateUtil.formatHourAMPMRange(item.getDeliveryStartTime(), item.getDeliveryEndTime()).replace("-", "&nbsp;-&nbsp;");
							orderId = item.getErpSalesId();
					%>
						<div class="pendingOrderBar-overlay-order">
							<div class="pendingOrderBar-overlay-order-status-cont status-<%= item.getOrderStatus().getStatusCode() %>">
								<div class="pendingOrderBar-overlay-order-status-icon"><img src="/media_stat/images/pendingOrder/status_<%= item.getOrderStatus().getStatusCode() %>.svg"></div>
								<div class="pendingOrderBar-overlay-order-status-value"><%= item.getOrderStatus().getDisplayName() %></div>
							</div>
							<div class="pendingOrderBar-overlay-order-detail-cont">
								<div class="pendingOrderBar-overlay-order-detail-left">
									<div class="pendingOrderBar-overlay-order-detail-date"><%= orderDate %></div>
									<div class="pendingOrderBar-overlay-order-detail-time"><%= orderTime %></div>
									<div class="pendingOrderBar-overlay-order-detail-ordnum-cont">
										<div class="pendingOrderBar-overlay-order-detail-ordnum-label"><%= (isSoOrder) ? "" : "Order#" %></div>
										<div class="pendingOrderBar-overlay-order-detail-ordnum-value"><%= orderName %></div>
									</div>
								</div>
								<div class="pendingOrderBar-overlay-order-detail-right">
									<a href="/your_account/modify_order.jsp?orderId=<%= orderId %>&action=modify" class="cssbutton orange<%= (isMobWeb) ? " large" : "" %> pendingOrderBar-overlay-order-detail-modifyorder-btn" data-gtm-source="banner">Modify&nbsp;Order<span class="offscreen">of <%= orderDate %>, <%= orderTime %> with order# <%= orderId %></span></a>
									<a href="/your_account/order_details.jsp?orderId=<%= orderId %>" class="cssbutton green transparent<%= (isMobWeb) ? " large" : "" %> pendingOrderBar-overlay-order-detail-seedetails-btn" data-gtm-source="banner"><span class="NOMOBWEB">See&nbsp;</span>Details<span class="offscreen"> of order number <%= orderName %></span></a>
								</div>
							</div>
						</div>
					<% } %>
						<div class="pendingOrderBar-overlay-footer"><a href="/your_account/order_history.jsp">View Order History</a></div>
					</div>
					<%-- PENDING ORDER BAR --%>
					<div id="" class="pendingOrderBar-cont">
						<% if (validPendingOrders.size() == 1) { %>
							<span class="pendingOrderBar-label">Your Delivery</span>
							<span class="pendingOrderBar-value"><%= (isMobWeb) ? "<a href=\"/your_account/order_details.jsp?orderId="+ orderId +"\" class=\"\">" : "" %><span class="pendingOrderBar-value-dow"><%= orderDOW %></span><span class="pendingOrderBar-value-time"><%= orderTime %></span><%= (isMobWeb) ? "</a>" : "" %></span>
							<a href="/your_account/order_details.jsp?orderId=<%= orderId %>" class="cssbutton transparent whiteborder small pendingOrderBar-seedetails-btn NOMOBWEB">See Details<span class="offscreen"> of order number <%= orderName %></span></a><!--
							--><a href="/your_account/modify_order.jsp?orderId=<%= orderId %>&action=modify" class="cssbutton orange small pendingOrderBar-modifyorder-btn">Modify<span class="NOMOBWEB"> Order</span></a>
						<% } else { %>
							<span class="pendingOrderBar-label">Your Deliveries</span><span class="pendingOrderBar-value"><%= validPendingOrders.size() %> Upcoming Orders</span><button class="cssbutton orange small pendingOrderBar-viewall-btn">View All<span class="offscreen">your <%= validPendingOrders.size() %> Upcoming Orders</span></button>
						<% } %>
					</div>
				</div>
				<%
			}
		}
	}
%>