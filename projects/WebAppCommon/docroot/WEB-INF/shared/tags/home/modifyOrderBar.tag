<%@tag 	import="com.freshdirect.fdstore.promotion.SignupDiscountRule"
		import="com.freshdirect.fdstore.customer.FDUserI"
		import="java.util.ArrayList"
		import="java.util.List"
		import="com.freshdirect.fdstore.customer.FDOrderInfoI"
		import="java.text.SimpleDateFormat"
		import="com.freshdirect.fdlogistics.model.FDTimeslot"
		import="java.util.Date"
		import="com.freshdirect.fdstore.content.util.DeliveryDateComparator"%><%@ 
		
		attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %><%@ 		
		attribute name="htmlId" required="true" rtexprvalue="true" type="java.lang.String" %><%
					if (user.getLevel() >= FDUserI.RECOGNIZED) { 
							int pendingOrderCount = 0;
							List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();							
							//If there is any order in 'NEW' status, clear the order history to fetch and see the order is submitted to SAP which makes the order modifiable.
							if(user.isAnyNewOrder()){
								user.invalidateOrderHistoryCache();
								user.setAnyNewOrder(false);
							}
							validPendingOrders.addAll(user.getPendingOrders());
							//Collections.reverse(validPendingOrders);
							
							//sort pending orders based on delivery date (the closer date goes first)
							Collections.sort(validPendingOrders, new DeliveryDateComparator());							
							
							//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
							pendingOrderCount = validPendingOrders.size();
	
							if (pendingOrderCount > 0) {
	
								FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);
	
								%>
								

<div class="index_ordMod_cont" id="<%= htmlId %>">
				   					<div class="index_ordMod_cont_child">
				   						<div class="ordModifyInfoCont">
											<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="orderNumb"><%= orderInfo.getErpSalesId() %></a>
											<span style="padding-left: 30px;"><span class="dow"><%= new SimpleDateFormat("EEEEE").format(orderInfo.getRequestedDate()) %>,</span> <%= new SimpleDateFormat("MM/dd/yyyy").format(orderInfo.getRequestedDate()) %> 
												<span class="pipeSep">|</span> <%= FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></span>
										</div>
										<div class="ordModifyButCont">
											<% if ( new Date().before(orderInfo.getDeliveryCutoffTime())) { %>
												<a href="/your_account/order_details.jsp?orderId=<%= orderInfo.getErpSalesId() %>" class="ordModifyViewDetails">view details</a>&nbsp;
												<a class="imgButtonOrange ordModifyModOrderButton" style="margin-left: 10px;" href="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>&action=modify">modify order</a>
											<% } else { %>
												&nbsp;
											<% } %>
										</div>
									</div>
								</div>
							<% } %>
					<% } %>
