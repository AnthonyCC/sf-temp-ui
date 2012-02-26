<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="true" />
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Pending Order Merge</tmpl:put>
    <tmpl:put name='content' direct='true'>
    	<%
    		//session.setAttribute("usedOverlay", true);
    	
    		String source = request.getParameter("source");
			String successPage = request.getParameter("successPage");
			String multiSuccessPage = request.getParameter("multiSuccessPage");
			
			String successPages = "?";
				if ( (successPage == null || "null".equalsIgnoreCase(successPage)) && (multiSuccessPage == null || "null".equalsIgnoreCase(multiSuccessPage)) ) {
					successPages += "successPage=/view_cart.jsp";
				} else {
					if ( successPage != null && !"null".equalsIgnoreCase(successPage) ) {
						successPages += "successPage="+successPage;
					}
					if ( multiSuccessPage != null && !"null".equalsIgnoreCase(multiSuccessPage) ) {
						if ( successPage != null && !"null".equalsIgnoreCase(successPage) ) {
							successPages += "&";
						}
						successPages += "multiSuccessPage="+multiSuccessPage;
					}
				}
				
			
			String action = "pendOrderMergeChoice";
			
			int maxItemsPerSection = 9; //max items to show before changing height of section for scrolling
			int maxItemsAcross = 3; //number of items across
			int colsPerItem = 2; //number of columns an item has (tds)
			int maxItemsHeight = (maxItemsPerSection / maxItemsAcross) * 50; //this is the pixel height of the section when over max (otherwise it's ITEMS/maxItemsAcross*50)
			int maxProdDescripWidth = 650; //width for all prod description columns TOTAL
			int prodDescripWidth = (int)Math.floor(maxProdDescripWidth/maxItemsAcross); //prod description width EACH
			boolean showCheckBox = false;
	   	%>
    	<fd:FDShoppingCart id='cart' action='<%=action%>' source='<%=source%>' result='result' multiSuccessPage='<%=multiSuccessPage%>' successPage='<%=successPage%>'>
		<%--
			we should only get this overlay if the user HAS pending orders that can be modified
			HOWEVER, due to timing, it could be possible to get this overlay, and have no pending orders
		--%>
		<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
				<%--
					if the user has more than N items to be ADDED to cart, set this height so the div will scroll inside the overlay
				--%>
				<%
					FDCartModel tempCart = user.getMergePendCart();
					int tempCartRowCount = 1;
					int tempCartCartlineSize = tempCart.getOrderLines().size();

					int pendingOrderCount = 0;
					List validPendingOrders = new ArrayList();
					
					validPendingOrders.addAll(user.getPendingOrders());
					//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
					pendingOrderCount = validPendingOrders.size();
				%>
				<form action="/overlays/merge_cart_penOrder_choice.jsp" id="mergeChoice" name="mergeChoice" method="post">
					<input type="hidden" name="actionString" value="<%=action%>">
				</form>
				<div style="width: 920px; text-align: left;">
					<form action="/overlays/merge_cart_penOrder_choice.jsp" id="mergeToPending" name="mergeToPending" method="post">
						<input type="hidden" name="actionString" value="<%=action%>">
						<div class="title24" style="padding-left: 22px;"><span style="color: #5491D1;">FORGET SOMETHING?</span> <span style="color: #99BCE1;">CHANGE IS EASY...</span></div>
						<div style="padding: 0 0 12px 22px;" class="text12px">
							Do you wish to add <%= (tempCartCartlineSize == 1) ? "this item" : "these items" %> to your pending order: <%
									//check for any valid orders
									if (pendingOrderCount > 0) {
										if (pendingOrderCount == 1) {
											//single order, straight display
											FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);
											%>
											<%= orderInfo.getErpSalesId() %> - 
												<span class="title12"><%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) %><input type="hidden" name="pendOrderId" value="<%= orderInfo.getErpSalesId() %>"></span><%
										} else {
											//multiple orders, dropdown
											%>
											<select style="margin-left: 20px;" name="pendOrderId">
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
						</div>
						<div class="hline"><!-- --></div>
						<div style="margin: 12px 10px 12px 10px;">
							<%--
								<div class="text12px">
									<span class="title14">Select the items you wish to add to your order</span> <span style="color: #aaa;">|</span> <a href="#" onclick="selectAllCB('mergePend_itemsToAdd'); return false;">Select All</a>
								</div>
							--%>
							<div style="margin: 0 10px;<%= (tempCart.getOrderLines().size() > maxItemsPerSection) ? "height: "+maxItemsHeight+"px; overflow-y: scroll;" : "" %>" id="mergePend_itemsToAdd">
								<%
									if (tempCartCartlineSize > 0) {
										//start a table if we have items
										%><table cellpadding="0" cellspacing="0" style="border-collapse: collapse;"><%
									}
								%>
								<logic:iterate id="cartLine" collection="<%= tempCart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
									<%
										if (tempCartRowCount == maxItemsAcross+1) {
											//reset row
											tempCartRowCount = 1;
										}
										if (tempCartRowCount == 1) {
											//start new row
											%><tr><%
										}
									%>
										
										<td style="width: 23px; padding: 0 5px; vertical-align: middle;"><% if (showCheckBox == true) { %><input type="checkbox" id="addCLID_<%= cartLine.getCartlineId() %>" name="addCLID_<%= cartLine.getCartlineId() %>" style="margin: auto 0;" /><% } %></td>
										<%
											ProductModel pm = cartLine.lookupProduct();
										
											if (pm != null && pm.getCategoryImage() != null) {
												Image catImage = pm.getCategoryImage();
												%><td style="width: 50px; height: 50px; vertical-align: middle;"><img border="0" src="<%= catImage.getPath() %>" width="<%= catImage.getFittedWidth(45, 45) %>" height="<%= catImage.getFittedHeight(45, 45) %>" alt="" style="margin-top: 5px;" /></td><%
											}
										%>
										<td style="width: <%= prodDescripWidth %>px; vertical-align: middle;"><div style="margin-left: 8px; text-indent: -8px; font-weight: bold;"><display:ProductName product="<%= pm %>" disabled="true" /></div></td>
									<%
										tempCartRowCount++;
				
										if (tempCartRowCount > 1 && idx+1 == tempCartCartlineSize) {
											//add in empty td to balance row
											%><td colspan="<%= ((maxItemsAcross+1)-tempCartRowCount)*colsPerItem %>"><!-- --></td><%
										}
										if (tempCartRowCount == maxItemsAcross+1 || idx+1 == tempCartCartlineSize) {
											//end row
											%></tr><%
										}
									%>
								</logic:iterate>
								<%
									if (tempCartCartlineSize > 0) {
										//end a table if we have items
										%></table><%
									}
								%>
							</div>
						</div>
					
						<%
							showCheckBox = true;
						%>
						<div style="width: 920px; text-align: left; overflow: hidden;">
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
												%><table cellpadding="0" cellspacing="0" style="border-collapse: collapse;"><%
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
												<td style="width: 23px; padding: 0 5px; vertical-align: middle;"><% if (showCheckBox == true) { %><input type="checkbox" id="userCLID_<%= cartLine.getCartlineId() %>" name="userCLID_<%= cartLine.getCartlineId() %>" style="margin: auto 0;" /><% } %></td>
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
							</div>
							<div class="hline"><!-- --></div>
						</form>
					</div>
					<div style="margin: 12px 30px 0 0;">
						<table style="width: 800px;">
							<tr>
								<td style="padding-left: 20px;">
									<form action="/overlays/merge_cart_penOrder_choice.jsp<%= successPages %>" id="mergeToPendingChoice" name="mergeToPendingChoice"  method="post"><input type="hidden" name="action" value="pendOrderMergeChoiceNewOrder" /></form>
									<table class="butCont fright" style="margin-left: 10px;">
										<tr>
											<td class="butBrownLeft"><!-- --></td>
											<td class="butBrownMiddle"><a class="butText" style="color:#333;" href="#" onclick="submitPendOrderMergeChoice(); return false;">No, continue with new order</a></td>
											<td class="butBrownRight"><!-- --></td>
										</tr>
									</table>
								</td>
								<td style="padding-right: 20px;">
									<table class="butCont fleft" style="margin-left: 10px;">
										<tr>
											<td class="butOrangeLeft"><!-- --></td>
											<td class="butOrangeMiddle"><a class="butText" href="#" onclick="$('mergeToPending').submit(); return false;">Yes, add to my order</a></td>
											<td class="butOrangeRight"><!-- --></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<br style="clear: both;" />
					</div>
				</div>
		<% } %>
		</fd:FDShoppingCart>
	</tmpl:put>
</tmpl:insert>