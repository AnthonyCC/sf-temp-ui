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
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Pending Order Merge</tmpl:put>
    <tmpl:put name='content' direct='true'>
    	<%
    		String source = (String)session.getAttribute("tempMergeSource");
			String multiSuccessPage = (String)session.getAttribute("tempMergeMultiSuccessPage");
			String successPage = (String)session.getAttribute("tempMergeSuccessPage");
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
				<form action="/ajax/merge_cart_penOrder_choice.jsp" id="mergeChoice" name="mergeChoice" method="post">
					<input type="hidden" name="actionString" value="<%=action%>">
				</form>
				<div style="width: 900px; text-align: left; padding: 30px 0;">
					<div class="title24"><span style="color: #5491D1;">FORGET SOMETHING?</span> <span style="color: #99BCE1;">CHANGE IS EASY...</span></div>
					<div style="padding: 0 0 12px 0;" class="text12px">We noticed that you have one (or more) scheduled orders for delivery.</div>
					<div class="hline"><!-- --></div>
					<%--
						if the user has more than N items to be ADDED to cart, set this height so the div will scroll inside the overlay
					--%>
					<%
						FDCartModel tempCart = new FDCartModel();
						if (session.getAttribute("tempMergePendCart") != null) {
							tempCart = (FDCartModel)session.getAttribute("tempMergePendCart");
						}
						int tempCartRowCount = 1;
						int tempCartCartlineSize = tempCart.getOrderLines().size();
					%>
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
									%><table cellpadding="0" cellspacing="0"><%
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
									<% if (showCheckBox == true) { %>
											<td style="width: 23px; padding: 0 5px; vertical-align: middle;"><input type="checkbox" id="addCLID_<%= cartLine.getCartlineId() %>" name="addCLID_<%= cartLine.getCartlineId() %>" style="margin: auto 0;" /></td>
									<% } %>
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
					<div style="background-color: #FAF7F0; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; padding: 10px 20px;">
						<div class="fleft" style="width: 380px;">
							<div class="fleft" style="width: 23px; padding: 0 5px;"><input type="radio" id="mergeChoicePending" name="mergeChoice"<%=(user.hasPendingOrder()) ? "checked=\"true\"":""%> /></div>
							<div class="fleft" style="width: 347px;">
								<div>OPTION A</div>
								<div class="text14bold">Modify existing order</div>
								<div>You can add to your pending order anytime, as many times as you like. <span style="color: #D7AE54;">Get it all delivered at the same time for no added cost!</span></div>
							</div>
						</div>
						<div class="fleft" style="width: 380px;">
							<div class="fleft" style="width: 23px; padding: 0 5px;"><input type="radio" id="mergeChoiceNone" name="mergeChoice" /></div>
							<div class="fleft" style="width: 347px;">
								<div>OPTION B</div>
								<div class="text14bold">Continue with new order</div>
								<div>You can continue shopping and place a new order.</div>								
							</div>
						</div>
						<br style="clear: both;" />
					</div>
					<div style="padding: 12px 30px 30px 0;">
						<table class="butCont fright" style="margin-left: 10px;">
							<tr>
								<td class="butOrangeLeft"><!-- --></td>
								<td class="butOrangeMiddle"><a class="butText" href="#" onclick="submitPendOrderMergeChoice(); return false;">Continue Shopping</a></td>
								<td class="butOrangeRight"><!-- --></td>
							</tr>
						</table>
						<br style="clear: both;" />
					</div>
				</div>
		<% } %>
		</fd:FDShoppingCart>
	</tmpl:put>
</tmpl:insert>