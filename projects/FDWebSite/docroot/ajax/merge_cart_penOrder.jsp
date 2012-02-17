<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.lists.FDCustomerCreatedList' %>
<%@ page import='com.freshdirect.fdstore.lists.FDCustomerProductListLineItem' %>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="true" />
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Pending Order Merge</tmpl:put>
    <tmpl:put name='content' direct='true'>
    <%= request.getParameter("catId") %>
   	<%
   		String source = (String)session.getAttribute("tempMergeSource");
		String multiSuccessPage = "/your_account/modify_order.jsp"; //set in FDShoppingCartController
		String successPage = "/your_account/modify_order.jsp"; //set in FDShoppingCartController
		String action = "pendOrderMerge";
		
		int maxItemsPerSection = 9; //max items to show before changing height of section for scrolling
		int maxItemsAcross = 3; //number of items across
		int colsPerItem = 3; //number of columns an item has (tds)
		int maxItemsHeight = (maxItemsPerSection / maxItemsAcross) * 50; //this is the pixel height of the section when over max (otherwise it's ITEMS/maxItemsAcross*50)
		int maxProdDescripWidth = 590; //width for all prod description columns TOTAL
		int prodDescripWidth = (int)Math.floor(maxProdDescripWidth/maxItemsAcross); //prod description width EACH
   	%>
   	<fd:FDShoppingCart id='cart' action='<%=action%>' source='<%=source%>' result='result' multiSuccessPage='<%=multiSuccessPage%>' successPage='<%=successPage%>'>
		<%
			int pendingOrderCount = 0;
			List validPendingOrders = new ArrayList();
		%>
		<% if (user.getLevel() >= FDUserI.RECOGNIZED) {
			validPendingOrders.addAll(user.getPendingOrders());
			//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
			pendingOrderCount = validPendingOrders.size();
		} %>
		<div style="width: 900px; text-align: left; overflow: hidden;">
			<form action="/ajax/merge_cart_penOrder.jsp" id="mergeToPending" name="mergeToPending" method="post">
				<input type="hidden" name="actionString" value="<%=action%>">
				<div class="title24" style="margin-top: 30px;"><span style="color: #5491D1;">FORGET SOMETHING?</span> <span style="color: #99BCE1;">CHANGE IS EASY...</span></div>
				<div style="background-color: #FAF7F0; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; margin: 10px 0;">
					<%
						//check for any valid orders
						if (pendingOrderCount > 0) {
							if (pendingOrderCount == 1) {
								//single order, straight display
								FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);
								%>
								<span class="title14">Pending order:</span>&nbsp;<%= orderInfo.getErpSalesId() %> - 
									<span class="title12"><%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) %><input type="hidden" name="pendOrderId" value="<%= orderInfo.getErpSalesId() %>"></span><%
							} else {
								//multiple orders, dropdown
								%>
								<span class="title14">Select pending order</span>&nbsp;<select style="margin-left: 20px;" name="pendOrderId">
								<% for (Iterator hIter = validPendingOrders.iterator(); hIter.hasNext(); ) {
									FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
									%><option val="<%= orderInfo.getErpSalesId() %>"><%= orderInfo.getErpSalesId() %> - 
										<%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) %></option>
									<%
								} %>
								</select>
								<%
							}
						} else {
							//we have no pending orders at all
							%>
							No pending orders!
							<%
						}
					%>
					<br style="clear: both;" />
				</div>
				<%
					//we already have this.
					//FDCartModel cart = user.getShoppingCart();
					int rowCount = 1;
					int cartlineSize = cart.getOrderLines().size();
				%>
				<% if (cartlineSize > 0) { %>
					<%--
						if the user has more than N items in cart, set this height so the div will scroll inside the overlay
					--%>
					<div style="margin: 12px 10px 12px 10px;">
						<div class="text12" style="line-height: 24px;">
							<img src="/media_stat/images/layout/down_arrow_orange.jpg" alt="" height="22" width="14" class="fleft" style="padding-right: 6px;" /> You have other items in your cart. Select the items you wish to add to your order: <span style="color: #aaa;">|</span> <a href="#" onclick="selectAllCB('mergePend_itemsFromCart'); return false;">Select All</a>
						</div>
						<div style="margin: 0 10px;<%= (cart.getOrderLines().size() > maxItemsPerSection) ? "height: "+maxItemsHeight+"px; overflow-y: scroll;" : "" %>" id="mergePend_itemsFromCart">
							<%
								if (cartlineSize > 0) {
									//start a table if we have items
									%><table cellpadding="0" cellspacing="0"><%
								}
							%>
							<logic:iterate id="cartLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
								<%
									if (rowCount == maxItemsAcross+1) {
										//reset row
										rowCount = 1;
									}
									if (rowCount == 1) {
										//start new row
										%><tr><%
									}
								%>
									<td style="width: 23px; padding: 0 5px; vertical-align: middle;"><input type="checkbox" id="userCLID_<%= cartLine.getCartlineId() %>" name="userCLID_<%= cartLine.getCartlineId() %>" style="margin: auto 0;" /></td>
									<%
										ProductModel pm = cartLine.lookupProduct();
									
										if (pm != null && pm.getCategoryImage() != null) {
											Image catImage = pm.getCategoryImage();
											%><td style="width: 50px; height: 50px; vertical-align: middle;"><img border="0" src="<%= catImage.getPath() %>" width="<%= catImage.getFittedWidth(45, 45) %>" height="<%= catImage.getFittedHeight(45, 45) %>" alt="" style="margin-top: 5px;" /></td><%
										}
									%>
									<td style="width: <%= prodDescripWidth %>px; vertical-align: middle;"><div style="margin-left: 8px; text-indent: -8px; font-weight: bold;"><display:ProductName product="<%= pm %>" disabled="true" /></div></td>
								<%
									rowCount++;
			
									if (rowCount > 1 && idx+1 == cartlineSize) {
										//add in empty td to balance row
										%><td colspan="<%= ((maxItemsAcross+1)-cartlineSize)*colsPerItem %>"><!-- --></td><%
									}
									if (rowCount == maxItemsAcross+1 || idx+1 == cartlineSize) {
										//end row
										%></tr><%
									}
								%>
							</logic:iterate>
							<%
								if (cartlineSize > 0) {
									//end a table if we have items
									%></table><%
								}
							%>
						</div>
					</div>
				<% } %>
				<div class="hline"><!-- --></div>
				<div style="margin: 12px 30px 0 0;">
					<table style="width: 800px;">
						<tr>
							<td style="width: 30px; padding-left: 20px;">
								<a href="#" id="" onclick="Modalbox.hide(); doRemoteOverlay('/ajax/merge_cart_penOrder_choice.jsp'); return false;"><img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP" /></a>
							</td>
							<td style="vertical-align: middle;"> Go back</td>
							<td style="padding-right: 20px;">
								<table class="butCont fright" style="margin-left: 10px;">
									<tr>
										<td class="butOrangeLeft"><!-- --></td>
										<td class="butOrangeMiddle"><a class="butText" href="#" onclick="$('mergeToPending').submit(); return false;">Continue Shopping</a></td>
										<td class="butOrangeRight"><!-- --></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<br style="clear: both;" />
				</div>
			</form>
		</div>
	</fd:FDShoppingCart>
	</tmpl:put>
</tmpl:insert>