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
attribute name="htmlId" required="true" rtexprvalue="true" type="java.lang.String" %><%@
attribute name="modifyOrderAlert" required="true" rtexprvalue="true" type="java.lang.Boolean"
%><%

	boolean inMobWebTemplate = (request.getAttribute("inMobWebTemplate") != null) ? (Boolean)request.getAttribute("inMobWebTemplate") : false;
	boolean mobWebModifyOrderTag = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	boolean isModifyingOrder = false;
					if(user.getLevel() >= FDUserI.RECOGNIZED){
							int pendingOrderCount = 0;
							List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();
							//If there is any order in 'NEW' status, clear the order history to fetch and see the order is submitted to SAP which makes the order modifiable.
							if(!modifyOrderAlert && user.isAnyNewOrder()){
								user.invalidateOrderHistoryCache();
								user.setAnyNewOrder(false);
							}
							validPendingOrders.addAll(user.getPendingOrders());
							Map<String,String> soUpcomingDelivery = new HashMap<String,String>();
							//Only for FD.
							if(null!=user.getUserContext() && EnumEStoreId.FD.equals(user.getUserContext().getStoreContext().getEStoreId())) {
								Collection<FDStandingOrder> sos = FDStandingOrdersManager.getInstance().loadCustomerNewStandingOrders( user.getIdentity());
								sos = FDStandingOrdersManager.getInstance().getAllSOUpcomingOrders( user, sos);
								for(FDStandingOrder fd:sos){
									soUpcomingDelivery.put(fd.getUpcomingDelivery().getErpSalesId(), fd.getCustomerListName());
								}
							}
							//sort pending orders based on delivery date (the closer date goes first)
							Collections.sort(validPendingOrders, new DeliveryDateComparator());
							
							FDCartModel modifyOrderBarTagCart = user.getShoppingCart();
							isModifyingOrder = modifyOrderBarTagCart instanceof FDModifyCartModel;
							if (isModifyingOrder) {
								
							} else if(validPendingOrders.size() > 0) {
								if (modifyOrderAlert) { %>
									<script>
										$jq("#locationbar .locabar-section.locabar-modify-order-section").css("display","block");
										$jq("#locabar_modify_order_trigger #locabar-modify-order-count").html("<%= validPendingOrders.size() %>");
										$jq("#locabar_modify_order_trigger .locabar-modify-order-container .locabar-modify-order-container-message").html("<%= new SimpleDateFormat("EEE").format(validPendingOrders.get(0).getRequestedDate()) %> <%=FDTimeslot.format(validPendingOrders.get(0).getDeliveryStartTime(),validPendingOrders.get(0).getDeliveryEndTime())%>");
									</script>
									<% if (inMobWebTemplate && mobWebModifyOrderTag) { %>
									
										<%
											String orderName;
											for(FDOrderInfoI item : validPendingOrders){
										%>
										
											<div class="modify-order-alert-table">
											
												<div class="">
													<div class="modify-order-alert-table-status-img"></div>
													<div class="modify-order-alert-table-date-and-time">
														<span class="modify-order-alert-table-date"><%= new SimpleDateFormat("EEEEE, MMM d").format(item.getRequestedDate()) %></span><%=  DateUtil.formatHourAMPMRange(item.getDeliveryStartTime(),item.getDeliveryEndTime()) %>
													</div>
													<div class="modify-order-alert-table-name">
														<div class="modify-order-alert-table-status"><div class="modify-order-alert-table-status-text"><%=item.getOrderStatus().getDisplayName()%></div></div>
														<div class="modify-order-alert-table-name-text">
															<%	
																orderName = item.getErpSalesId();
																if (soUpcomingDelivery.containsKey(item.getErpSalesId())) {
																	orderName = soUpcomingDelivery.get(item.getErpSalesId());
																} 
															%>
														    <%= orderName %>
													    </div>
													</div>
												</div>
												<div class="modify-order-alert-table-actions">
													<div class="modify-order-alert-table-delails"><a href="/your_account/order_details.jsp?orderId=<%= item.getErpSalesId() %>">See Details <span class="offscreen">of order number <%= orderName %></span></a></div>
													<div class="modify-order-alert-table-modify"><button class="modify-order-alert-button cssbutton cssbutton-flat orange" onclick="$jq('#modifyorderalert').find('.alert-closeHandler').click(); window.location.href='/your_account/modify_order.jsp?orderId=<%= item.getErpSalesId() %>&action=modify';">Modify Order</button></div>
												</div>
											</div>
										<% } %>
									<% } else { %>
										<% if (!(modifyOrderBarTagCart instanceof FDModifyCartModel)) { %>
											<table width="100%" class="modify-order-alert-table">
												<tr>
													<td width="21%" class="modify-order-alert-table-header-status">Order Status</td>
													<td width="22%" class="modify-order-alert-table-header-time">Delivery Time</td>
													<td width="31%" class="modify-order-alert-table-header-name">Name</td>
													<td width="26%" colspan="2"></td>
												</tr>
												<%
													String orderName;
													for(FDOrderInfoI item : validPendingOrders){
												%>		
												<tr>
													<td colspan="5">
														<hr class="so-alert-line-separator">
													</td>
												</tr>						
												<tr>
													<td class="modify-order-alert-table-status"><div class="modify-order-alert-table-status-img"></div><div class="modify-order-alert-table-status-text"><%=item.getOrderStatus().getDisplayName()%></div></td>
													<td class="modify-order-alert-table-date-and-time"><span class="modify-order-alert-table-date"><%= new SimpleDateFormat("EEEEE, MMM d").format(item.getRequestedDate()) %></span><%=  DateUtil.formatHourAMPMRange(item.getDeliveryStartTime(),item.getDeliveryEndTime()) %></td>
													<td class="modify-order-alert-table-name">
														<div class="modify-order-alert-table-name-text">
														<%	
															orderName = item.getErpSalesId();
															if(soUpcomingDelivery.containsKey(item.getErpSalesId())){
																orderName = soUpcomingDelivery.get(item.getErpSalesId());
															} 
														%>
													    <%= orderName %>
													    </div>
													</td>
													<td width="10%" class="modify-order-alert-table-delails"><a href="/your_account/order_details.jsp?orderId=<%= item.getErpSalesId() %>">See Details<span class="offscreen">of order number <%= orderName %></span></a></td>
													<td width="16%" class="modify-order-alert-table-modify"><button class="modify-order-alert-button cssbutton cssbutton-flat orange" onclick="$jq('#modifyorderalert').find('.alert-closeHandler').click(); window.location.href='/your_account/modify_order.jsp?orderId=<%= item.getErpSalesId() %>&action=modify'"">Modify Order</button></td>
												</tr>
												<% } %>
											</table>
										<% } %>
									<% } %>
								<% } else { %>
								<div class="locabar-modify-order-dropdown">
										<%
											String orderName;
											for(FDOrderInfoI item : validPendingOrders){										
										%>
											<hr class="so-alert-line-separator">
											<div class="locabar-modify-order-dropdown-item">
												<div class="locabar-modify-order-dropdown-img"></div>
												<div class="locabar-modify-order-dropdown-container">
													<div class="locabar-modify-order-dropdown-container-status-bold">Order Status: </div>
													<div class="locabar-modify-order-dropdown-container-status"> <%=item.getOrderStatus().getDisplayName()%></div>
													<div class="locabar-modify-order-dropdown-container-name">
														<%
														orderName = item.getErpSalesId();
														if(soUpcomingDelivery.containsKey(item.getErpSalesId())){
															orderName = soUpcomingDelivery.get(item.getErpSalesId());
														}
														%>
													    <%= orderName %>
													</div>
													<div class="locabar-modify-order-dropdown-container-date-and-time"><span class="locabar-modify-order-alert-table-date"><%= new SimpleDateFormat("EEEEE, MMM d").format(item.getRequestedDate()) %></span><%=  DateUtil.formatHourAMPMRange(item.getDeliveryStartTime(),item.getDeliveryEndTime()) %></div>
													<div class="locabar-modify-order-dropdown-container-buttons">
														<div class="locabar-modify-order-dropdown-container-delails"><a href="/your_account/order_details.jsp?orderId=<%= item.getErpSalesId() %>">See Details <span class="offscreen">of order number <%= orderName %></span></a></div>
														<div class="locabar-modify-order-dropdown-container-modify"><button class="modify-order-alert-button cssbutton cssbutton-flat green transparent" onclick="window.location.href='/your_account/modify_order.jsp?orderId=<%= item.getErpSalesId() %>&action=modify'"">Modify Order</button></div>
														<div class="clear"></div>
													</div>												
												</div>
												<div class="clear"></div>
											</div>
										<% } %>
									</div>
								<%} %>
							<% } %>							
					<% } %>
