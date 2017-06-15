<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.sort.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdlogistics.model.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user" noRedirect="true" />
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/blank.jsp'>
		<tmpl:put name="extraHead">
			<fd:css href="/assets/css/global.css" />
			<style>
				.step {
					background:white;
					overflow:hidden;
					top:0;
					left:0;
					position:absolute;
					text-align:left;
				}
				
				.step h1,
				.step h2 {
					padding-left:8px;
					margin:0;
				}
				
				.step h1 {
					color:white;
					font-weight:bold;
					font-size:16px;
					height:32px;
					line-height:32px;
					background:#75a5d6;
				}
				
				.step h2 {
					color:black;
					background:lightgray;
					font-size:14px;
					font-weight:bold;
					line-height:3em;
				}
				
				#step1 { 
					z-index:2;
					width:600px;
					height:120px;
				}
				
				#step1 .buttons {
					text-align:center;
					height:88px;
					line-height:88px;
				}
				
				
				#step2 {
					width:950px;
					z-index:1;
					display:none;
				}
				
				#step2 .content {
					padding:10px;
				}
				
				#step2 .buttons {
					text-align:right;
					padding:0 10px 10px 0;
				}
				
				#step2 h3 {
					margin:0;
					line-height: 1.2em;
					font-weight: bold;
					color: orange;
					font-size: 12px;
					padding: 0.5em 0;
				}
				#step2 h3 small {
					color:black;
					font-weight:normal;
					font-size:10px;
					display:block;
				}
				
				#pending_items_form {
					text-align:left;
				}
				
				#cartlines {
					font-size:0;
					overflow:auto;
					max-height:125px;
				}
				
				#cartlines .cartline {
					display:inline-block;
					margin-left:10px;
					width:290px;
					vertical-align:top;
					margin-bottom:10px;
				}

				#cartlines .cartline img {
					float:left;
					max-width:50px;
					max-height:50px;
				}

				#cartlines .cartline table {
					font-weight:bold;
					margin:0 0 0 60px;
					width:230px;
				}
				
				#cartlines .cartline caption {
					margin:0;
					font-family: Verdana, Arial, sans-serif;
					font-size: 12px;
					font-style: normal;
					font-variant: normal;
					font-weight: bold;
					text-align:left
				}
				
				#cartlines .cartline th,
				#cartlines .cartline td {
					font-size:10px;
				}
				
				#cartlines .cartline th {
					font-weight:normal;
					width:70px;
				}

				#cartlines .cartline td {
					font-weight: bold;
				}
				
				#selectors {
					padding-left:30px;
					margin:1.5em 0 2em 0;
				}
				
				#selectors label {
					font-size:14px;
					font-weight:bold;
				}
				
				#cartitems {
					max-height:180px;
					overflow:auto;
					border-collapse:collapse;
					border:1px solid lightgray;
					padding:4px 0;
				}
				
				#cartitems table {
					border-collapse:collapse;
					vertical-align:top;
					margin-top:10px 0;
				}
				
				#cartitems col.dashed {
					border-right:1px dashed lightgrey;
				}
				
				#cartitems td {
					width:280px;
					white-space:nowrap;
					overflow:hidden;
					height:50px;
					padding:0 10px 10px 10px;
				}
								
				#cartitems input {
					vertical-align:middle;
				}
				
				#cartitems img {
					max-width:50px;
					max-height:50px;
					vertical-align:middle;
					margin:0 1em;					
				}
				#cartitems span {
					vertical-align:middle;
					font-weight:bold;
					font-size:10px;		
					display:inline-block;						
					white-space:normal;		
					max-width:190px;
				}

				.close {
					position:absolute;
					top:0;
					right:0;
					background:url(/media_stat/images/buttons/round_x.png) transparent center center no-repeat;
					border:none;
					width:20px;
					height:20px;
					overflow:hidden;
					text-indent:20px;
				}
								
				body.step2 #step1 { z-index:1; display:none; }
				body.step2 #step2 { z-index:2; display:block; }
				body {
					overflow:hidden;
				}
			</style>
		</tmpl:put>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Pending Order Merge"/>
  </tmpl:put>
    <tmpl:put name='title'>FreshDirect - Pending Order Merge</tmpl:put>
    <tmpl:put name='content'>
    	<%
	    	String instant = request.getParameter("instant");
			int maxItemsPerSection = 9; //max items to show before changing height of section for scrolling
			int maxItemsAcross = 3; //number of items across
			int colsPerItem = 2; //number of columns an item has (tds)
			int maxItemsHeight = (maxItemsPerSection / maxItemsAcross) * 50; //this is the pixel height of the section when over max (otherwise it's ITEMS/maxItemsAcross*50)
			int maxProdDescripWidth = 650; //width for all prod description columns TOTAL
			int prodDescripWidth = (int) Math.floor(maxProdDescripWidth / maxItemsAcross); //prod description width EACH
			
			boolean validState = !"CALLCENTER".equalsIgnoreCase((String) session.getAttribute(SessionName.APPLICATION))
					&& user.isPopUpPendingOrderOverlay();
					
			String cancelclick="window.parent.FreshDirect.ATC_Pending.doNormalSubmit(location.search); return false;";			
			if(instant != null && !"".equals(instant)) {
				cancelclick = instant +";window.parent.FreshDirect.ATC_Pending.blockSubmit(location.search); return false;";
			}
	   	%>
		<%--
			we should only get this overlay if the user HAS pending orders that can be modified
			HOWEVER, due to timing, it could be possible to get this overlay, and have no pending orders
		--%>
		<% if (validState) { %>
				<%
					// if the user has more than N items to be ADDED to cart, set this height so the div will scroll inside the overlay
					FDCartModel tempCart = user.getMergePendCart();
					int tempCartRowCount = 1;
					int tempCartCartlineSize = tempCart.getOrderLines().size();

					int pendingOrderCount = 0;
					List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();
					
					validPendingOrders.addAll(user.getPendingOrders());
					Collections.sort(validPendingOrders, new DeliveryTimeComparator());
//					Collections.reverse(validPendingOrders);
					//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
					pendingOrderCount = validPendingOrders.size();
				%>
			<div id="step1" class="step">
				<button class="close">close</button>
				<h1>I would like to...</h1>				
				<div class="buttons">
						<button class="imgButtonBrown" onclick="<%= cancelclick %>">Create a New Order</button>
						<button class="imgButtonOrange" onclick="step2()">Modify an Existing Order</button>
				</div>
			</div>
			<div id="step2" class="step">
			<button class="close">close</button>
			<h1>You are modifying a pending order.</h1>				
			<form id="pending_items_form">
					<h2><%
								//check for any valid orders
								if (pendingOrderCount > 0) {
									if (pendingOrderCount == 1) {
										//single order, straight display
										FDOrderInfoI orderInfo = (FDOrderInfoI) validPendingOrders.get(0);
										%>
										<span style="font-weight:normal"><%= orderInfo.getErpSalesId() %> - </span>
										<span class="font13orbold"><%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) %><input type="hidden" name="pendingOrderId" value="<%= orderInfo.getErpSalesId() %>"></span><%
									} else {
										//multiple orders, dropdown
										%>Choose which order to modify:
										<select style="margin-left: 0.5em;" name="pendingOrderId">
										<% for (Iterator<FDOrderInfoI> hIter = validPendingOrders.iterator(); hIter.hasNext(); ) {
											FDOrderInfoI orderInfo = hIter.next();
											%><option value="<%= orderInfo.getErpSalesId() %>"><%= orderInfo.getErpSalesId() %> - 
												<%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) +" "+ FDTimeslot.format(orderInfo.getDeliveryStartTime(),orderInfo.getDeliveryEndTime())%></option>
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
					</h2>
					<%--
						Pending Items section
					 --%>
					<div class="content">
					<h3>ADD <%= tempCart.getOrderLines().size()>1 ? "THESE ITEMS" : "THIS ITEM" %> TO YOUR PENDING ORDER</h3>
					<div id="cartlines">
							<logic:iterate id="cartLine" collection="<%= tempCart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
								<fd:ProductGroup id='prodNode' categoryId='<%= cartLine.getCategoryName() %>' productId='<%= cartLine.getProductName() %>'>  
								<div class="cartline">
									<%									
										if (prodNode != null && prodNode.getCategoryImage() != null) {
											Image catImage = prodNode.getCategoryImage();
											%><img border="0" src="<%= catImage.getPath() %>" alt="" ><%
										}
									%>
									<table>
										<caption><%= prodNode.getFullName() %></caption>
										<tr><th>Quantity:</th><td><display:OrderLineQuantity product="<%= prodNode %>" orderline="<%= cartLine %>" customer="<%= user %>"/></td></tr>
										<tr><th>Options:</th><td><%= cartLine.getConfigurationDesc() %></td></tr>
										<tr><th>Est.price:</th><td><%= currencyFormatter.format(cartLine.getPrice()) %></td></tr>
									</table>
								</div>
								</fd:ProductGroup>
							</logic:iterate>
					</div>

					<%--
					Items already in cart section
					 --%>				
						<%
							FDCartModel cart = user.getShoppingCart();
							int rowCount = 1;
							int cartlineSize = cart.getOrderLines().size();
						%>
						<% if (cartlineSize > 0) { %>
						<h3 style="margin-top:2em">YOU HAVE OTHER ITEMS IN YOUR CART - YOU CAN ADD THEM TO YOUR PENDING ORDER.
						<small>Choose the items you want to add to your order. Select them all, or individually.</small></h3>
						<div id="selectors">
							<input type="radio" name="select" value="all" id="all_select"><label for="all_select">Select All</label><br>
							<input type="radio" name="select" value="none" id="none_select"><label for="none_select">Deselect All</label>
						</div>
						<div id="cartitems">
							<table>
								<colgroup>
									<col class="dashed">
									<col class="dashed">
									<col>
								</colgroup>
								<tr>
								<logic:iterate id="cartLine" collection="<%= cart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
									<%= (idx > 0) && (idx % 3 == 0) ? "</tr><tr>" : "" %>
									<td class="column_<%=(idx % 3)+1 %>">
									<input type="checkbox" id="userCLID_<%= cartLine.getCartlineId() %>" name="userCLID_<%= cartLine.getCartlineId() %>" style="margin: auto 0;" /><label for="userCLID_<%= cartLine.getCartlineId() %>">
									<%
										ProductModel pm = cartLine.lookupProduct();
									
										if (pm != null && pm.getCategoryImage() != null) {
											Image catImage = pm.getCategoryImage();
											%><img border="0" src="<%= catImage.getPath() %>"  /><%
										}
									%><span><display:ProductName product="<%= pm %>" disabled="true" /></span></label>
									</td>
								</logic:iterate>
								</tr>
							</table>
						</div>
						<% } %>
						</div>
					<div class="buttons">
										<button class="imgButtonBrown" onclick="<%= cancelclick %>">Cancel</button><%
										{
										String onclick = "window.parent.FreshDirect.ATC_Pending.doPendingOrderModify(location.search, document.getElementById('pending_items_form')); return false;";
										
										if (instant != null && !"".equals(instant)) {
											onclick = "window.parent.FreshDirect.ATC_Pending.doPendingOrderModify(location.search, document.getElementById('pending_items_form'));window.parent.FreshDirect.ATC_Pending.blockSubmit(location.search); return false;";
								     	}  
								    %><button class="imgButtonOrange" onclick="<%= onclick %>">Continue to Modify Order</button>
								<% } %>									
					</div>
				</form>
			</div>
		<% } %>
		<script type="text/javascript">
			YAHOO.util.Event.onDOMReady(function() {
			<% if (!validState) { 
				if (instant != null && !"".equals(instant)) {%>;
					<%=instant%>;window.parent.FreshDirect.ATC_Pending.blockSubmit(location.search);
				<%} else { %>
					window.parent.FreshDirect.ATC_Pending.doNormalSubmit(location.search);
				<%}%>
			<% } else { %>;
//			setFrameSize('modify_pending_choice_frame_<%= request.getParameter("formId") %>', 20, 20);
			window.parent.FreshDirect.ATC_Pending.refreshPanel(location.search);
			<% } %>;
				var step1=jQuery('#step1');
				window.parent.FreshDirect.ATC_Pending.setSize(location.search,step1.width(),step1.height());
			});
			
			(function($){
				var $document = $(document);
				$document.on('change','input[name=select]',function(e){
					$('#cartitems input[type="checkbox"]').prop('checked',$(e.currentTarget).val() === 'all');
				});
				
				$document.on('click','button.close',function(e){
					e.preventDefault();
					<%= cancelclick %>									
				})
			})(jQuery)
			
			function step2(){
				var step2=jQuery('#step2');
				jQuery(document.body).addClass('step2');
				window.parent.FreshDirect.ATC_Pending.setSize(location.search,step2.width(),step2.height());
			}
		</script>
	</tmpl:put>
</tmpl:insert>
