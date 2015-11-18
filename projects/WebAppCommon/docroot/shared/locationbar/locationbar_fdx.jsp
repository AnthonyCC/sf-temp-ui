<%@page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel"%>
<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import='com.freshdirect.logistics.delivery.model.EnumDeliveryStatus' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel' %>
<%@ page import="com.freshdirect.fdlogistics.model.FDReservation" %>
<%@ page import='java.util.List' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<fd:LocationHandler/>
<%
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
String selectedPickupId = (String)pageContext.getAttribute(LocationHandlerTag.SELECTED_PICKUP_DEPOT_ID_ATTR);
Boolean disabled = (Boolean)pageContext.getAttribute(LocationHandlerTag.DISABLED_ATTR);
MasqueradeContext masqueradeContext = user.getMasqueradeContext();
boolean hasFdxServices = LocationHandlerTag.hasFdxService(selectedAddress.getZipCode());
boolean hasFdServices = LocationHandlerTag.hasFdService(selectedAddress.getZipCode());
%>

<tmpl:insert template="/shared/locationbar/locationbar_layout_fdx.jsp">

<%-- new login form --%>
	
	<tmpl:put name="fdx_login_form">
    	<div id="login_cont_formContent" style="display: none; z-index: 1002" class="locabar_login">
    		<div class="logo"><img src="/media/layout/nav/globalnav/fdx/logo.png"  alt="FreshDirect" border="0" /></div>
    		<div class="sign-in">Sign in</div>
    		<form id="login_cont_formContentForm">
    			<div class="fieldInputs"><input id="login_cont_formContent_email" name="userId" value="Email" data-deftext="Email" class="ccc" /></div>
    			<div class="fieldInputs"><input id="login_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" type="text" /></div>
        		<div id="login_cont_formContentForm_signInCont">
        			<div style="display: none;" id="login_cont_formContentForm_loggingIn">Logging in...</div>
        			<button id="login_cont_formContentForm_signIn" name="submit" class="cssbutton fdxgreen">Sign in</button>
        		</div>
    		</form>
			<div class="errorMsg" style="display: none;">
				<div class="header">Please re-enter your Email and Password.</div> 
				The information you entered is incorrect. Please try again.
			</div>
    		<div id="login_cont_formContent_forgotpass"><a href="/login/forget_password.jsp">Forgot your password?</a></div>
    	</div>
    	<%-- this script chunk is necessary to move the login form out of toptoolbar, because of relative z-index issues --%>
		<script>
			$jq('body').append($jq('#login_cont_formContent'));
		</script>
	</tmpl:put>
	
<%-- MASQUERADE bar --%>
	<% if (masqueradeContext != null) {
		String makeGoodFromOrderId = masqueradeContext.getMakeGoodFromOrderId();
	%>
		<tmpl:put name="topwarningbar">
			<div id="topwarningbar">
				You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user.getUserId()%> (Store: <%= user.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user.getUserContext().getFulfillmentContext().getPlantId() %>)
				<%if (makeGoodFromOrderId!=null) {%>
					<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
					(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
					<a class="imgButtonOrange" href="/cancelmakegood.jsp">Cancel MakeGood</a>
				<%}%>
			</div>
		</tmpl:put>
	<% } %>

<%-- alerts icon --%>
	<tmpl:put name="alerts"><div class="locabar-section locabar-alerts-section" style="display: none;">
			<div id="locabar_alerts_trigger" class="cursor-pointer">
				<div class="section-warning-small" id="locabar-alerts-open">
					<div id="locabar-alerts-count" class="locabar-circle-cont alerts-count" data-count="0">0</div>
				</div>
			</div>
			
			<%-- TEST ALERTS
			--%>
			
				<div class="messages invisible" id="test1" data-type="test1">this is a test message</div>
				<div class="messages invisible" id="test2" data-type="test2">this is a test message</div>
				<div class="messages invisible" id="test3" data-type="test3">this is a test message</div>
				<script>
					$jq(document).ready(function() { 
						$jq('#test1').messages('add','test1');
						$jq('#test2').messages('add','test2');
						$jq('#test3').messages('add','test3');
					});
				</script>
	</div></tmpl:put>

<%-- FOODKICK tab --%>
	<tmpl:put name="tab_fdx"><% if (hasFdxServices && FDStoreProperties.isFdxTabEnabled()) { %><a href="https://foodkick.freshdirect.com" class="locabar-tab locabar-tab-fdx-cont"><div class="locabar-tab-fdx"></div></a><% } else { %><!-- --><% } %></tmpl:put>

<%-- COS tab --%>
	<tmpl:put name="tab_cos"><!-- --><%-- PLACEHOLDER, NOT LAUNCHING 20151109 -- <a href="/cos.jsp" class="locabar-tab"><div class="locabar-tab-cos"></div></a>	--%></tmpl:put>

<%-- ZIP/ADDRESS area --%>
	<%
		String zipAddDisplayString = "Change Zip Code";
		boolean isEligibleForPreReservation = false;
		FDReservation userReservervation = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
		String reservationDate = "";
		String reservationTime = "";
		boolean foundSelectedAddress = false;
		
	
		if (user!=null && user.getLevel() != FDUserI.GUEST) {
			zipAddDisplayString = "Choose Delivery Time";
			isEligibleForPreReservation = user.isEligibleForPreReservation();
			
			
			/* reservation logic */
			if (isEligibleForPreReservation) {
				userReservervation = user.getReservation();
				
				if (userReservervation != null) {
					reservationDate = dateFormatter.format(userReservervation.getStartTime());
					reservationTime = FDTimeslot.format(userReservervation.getStartTime(), userReservervation.getEndTime());
					
					zipAddDisplayString = (reservationDate+" @ "+reservationTime).toUpperCase();
				}
			}
			
			
			List<ErpAddressModel> allHomeAddresses = user.getAllHomeAddresses();
			List<ErpAddressModel> allCorporateAddresses = user.getAllCorporateAddresses();
			List<FDDeliveryDepotLocationModel> allPickupDepots = (List<FDDeliveryDepotLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);
			
			if( allHomeAddresses.size() + allCorporateAddresses.size() + allPickupDepots.size() > 1 && (disabled == null || !disabled)) {
				%><tmpl:put name="address">
					<div id="locabar_addresses_choices">
						<select id="selectAddressList" name="selectAddressList"><%
							if(allHomeAddresses.size()>0){%>
								<optgroup label="Home Delivery">
									<logic:iterate id="homeAddress" collection="<%=allHomeAddresses%>" type="com.freshdirect.common.address.AddressModel">
										<option 
											<%= ( selectedAddress.equals(homeAddress) )
												? " data-class=\"address-icon\" data-style=\"background-image: url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;);\" selected=\"selected\""
												: " data-class=\"address-icon\" data-style=\"background-image: none;\""
											%>
											 value="<%=homeAddress.getPK().getId()%>">
											 	<%=LocationHandlerTag.formatAddressText(homeAddress)%>
										</option>
										<% if ( selectedAddress.equals(homeAddress) ) { foundSelectedAddress = true; } %>
									</logic:iterate>
								</optgroup>
							<%}
							if(allCorporateAddresses.size()>0){%>
								<optgroup label="Office Delivery">
									<logic:iterate id="corporateAddress" collection="<%=allCorporateAddresses%>" type="com.freshdirect.common.address.AddressModel">
										<option 
											<%= ( selectedAddress.equals(corporateAddress) )
												? " data-class=\"address-icon\" data-style=\"background-image: url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;);\" selected=\"selected\""
												: " data-class=\"address-icon\" data-style=\"background-image: none;\""
											%>
											 value="<%=corporateAddress.getPK().getId()%>">
											 	<%=LocationHandlerTag.formatAddressText(corporateAddress)%>
										</option>
										<% if ( selectedAddress.equals(corporateAddress) ) { foundSelectedAddress = true; } %>
									</logic:iterate>
								</optgroup>
							<%}
							if(allPickupDepots.size()>0){%>
							<optgroup label="Pickup">
								<logic:iterate id="pickupDepot" collection="<%=allPickupDepots%>" type="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel">
									<option 
										<%= ( selectedPickupId!=null && selectedPickupId.equalsIgnoreCase(pickupDepot.getId()) )
											? " data-class=\"address-icon\" data-style=\"background-image: url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;);\" selected=\"selected\""
											: " data-class=\"address-icon\" data-style=\"background-image: none;\""
										%>
										 value="DEPOT_<%= pickupDepot.getId() %>">
										 	<%= LocationHandlerTag.formatAddressText(pickupDepot.getAddress()) %>
									</option>
									<% if ( selectedPickupId!=null && selectedPickupId.equalsIgnoreCase(pickupDepot.getId()) ) { foundSelectedAddress = true; } %>
								</logic:iterate>
							</optgroup>
							<%}
						%></select>
					</div>
				</tmpl:put><%
			} else { //only one address
				%><tmpl:put name="address"><span class="text"><%=LocationHandlerTag.formatAddressText(selectedAddress)%></span></tmpl:put><%	
			}
	
		} else { //non-signed in user
			%><tmpl:put name="address_change_zip">
				<div class="text">Change zip code.</div>
				<span id="newzip"><input type="text" id="newziptext" class="placeholder" placeholder="Enter zip code" maxlength="5" onkeydown="goButtonFocus(event);"><button id="newzipgo" class="cssbutton orange orange-imp cssbutton-flat">Go</button></span>
			</tmpl:put><%
			

			String shortAddress = LocationHandlerTag.formatAddressShortText(selectedAddress);
			if (hasFdServices) {
				%><tmpl:put name="address">
					<div class="text bold text12"><%="".equals(shortAddress) ? "" : shortAddress %></div>
					<div class="text" style="margin-bottom: 1.5em;">Shopping in <%= selectedAddress.getZipCode() %> zip code.</div>
					<tmpl:get name="address_change_zip"/>
				</tmpl:put><%
			} else { //non-recognized and not in deliverable zip
				%><tmpl:put name="address"><div style="display: inline-block;" class="section-warning">
						<div style="margin: 0 0 1em 10px;">
							<div class="text13 bold"><%="".equals(shortAddress) ? "" : shortAddress + "," %> <%= selectedAddress.getZipCode() %></div>
							<div>FreshDirect is not available in<br /> this zip code.</div>
						</div>
					</div>
					<tmpl:get name="address_change_zip"/>
					
					<div class="nodeliver-form">
						<% if (user != null && !user.isFutureZoneNotificationEmailSentForCurrentAddress()) { %>
							<form class="n">
								<div class=""><label class="n">We'll notify you when service expands in<br />your area.</label></div>
								<div>
									<input type="text" id="location-email" class="placeholder" placeholder="Enter your e-mail" /><button id="location-submit" class="cssbutton fdxgreen cssbutton-flat">Submit</button>
								</div>
							</form>
						<% } %>
				</div></tmpl:put><%
			}
		}
		
    	String dlvInfoLink = "";
    	if (user != null) {
	        if (user.isPickupOnly()) {
	            dlvInfoLink = "/help/delivery_lic_pickup";
	        } else if (user.isDepotUser()) {
	            dlvInfoLink = "/help/delivery_info_depot";
	        } else if (user.getAdjustedValidOrderCount() >= 1) {
	            dlvInfoLink = "/your_account/delivery_info_avail_slots";
	        } else {
	            dlvInfoLink = "/help/delivery_info";
	                if (EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
	                    dlvInfoLink += "_cos";
	                }
	        }
	        dlvInfoLink += ".jsp";
    	}
	%>
	<tmpl:put name="zip_address"><div class="locabar-section locabar-addresses-section"><div style="display: inline-block; position: relative;" id="locabar_addresses_trigger">
				<div style="display: inline-block;" class="bold cursor-pointer">
					<div class="locabar-truck" style="display: inline-block;"></div>
					<div style="display: inline-block;">
						<div>
							<%
							/* it's possible to not have the selected address in the address list, so check that as well */
							if (user!=null && user.getLevel() != FDUserI.GUEST && foundSelectedAddress) { %>
								<%= LocationHandlerTag.formatAddressText(selectedAddress) %>
							<% } else { %>
								<%= selectedAddress.getCity() %> (<%= selectedAddress.getZipCode() %>)
							<% } %>
						</div>
						<div>
							<%= zipAddDisplayString %>
							<div class="locabar-down-arrow"></div>
						</div>
					</div>
				</div>
				
				<div id="locabar_addresses" class="posAbs" >
					<div class="ui-arrow-buffer"></div>
					<div class="ui-arrow ui-top"></div>
					<% if (user != null &&  user.getLevel() != FDUserI.GUEST) { %>
						<div class="section-header">
							DELIVERY ADDRESSES
							<% if (user != null && user.getLevel() == FDUserI.SIGNED_IN) { %><a href="/your_account/delivery_information.jsp" class="locabar_addresses-dlvadd-edit">Edit</a><% } %>
						</div>
					<% } %>
					
					<tmpl:get name="address" />
					
					<% if (user != null &&  user.getLevel() != FDUserI.GUEST) { %>
						<div class="section-header">
							TIME SLOT
							<a href="<%= dlvInfoLink %>" class="locabar_addresses-ts-info" title="Delivery Info"></a>
						</div>
						<% if (isEligibleForPreReservation) { %>
							<% if (userReservervation == null) { %>
								<div class="locabar_addresses-reservation-none">
									<a href="/your_account/reserve_timeslot.jsp" class="cssbutton orange cssbutton-flat">Make a Reservation</a>
								</div>
							<% } else { %>
								<div class="locabar_addresses-reservation-existing">
									<span id="locabar_addresses-reservation-existing-value"><%= (reservationDate+" @ "+reservationTime).toUpperCase() %></span>
									<a href="/your_account/reserve_timeslot.jsp" class="cssbutton orange cssbutton-flat fright">Change</a>
								</div>
							<% } %>
						<% } %>
					<% } %>
				</div>
			</div>
		</div><script>
				FreshDirect.locabar.hasFdServices = <%=hasFdServices %>;
				FreshDirect.locabar.hasFdxServices = <%=hasFdxServices %>;
			</script></tmpl:put>
	
<%-- SIGN IN area --%>
	<tmpl:put name="sign_in"><%
			String greetingsString = "Sign in";
			boolean signedIn = false; //used for js logic for hover/click event results
			if (user != null && user.getLevel() != FDUserI.GUEST) {
				greetingsString = user.getFirstName();
				
				if (user.getLevel() == FDUserI.SIGNED_IN) {
					signedIn = true;
				}
			}
			
		%><div class="locabar-section locabar-user-section">
			<div style="display: inline-block; position: relative;" id="locabar_user_trigger" data-signedin="<%= signedIn %>"data-social="<%= FDStoreProperties.isSocialLoginEnabled() %>">
				<div class="bold cursor-pointer">
					<div>Hi!</div>
					<div>
						<div>
							<%= greetingsString %><div class="locabar-down-arrow"></div>
						</div>
					</div>
				</div>
				<%
					Map<String, String> folderMap=new LinkedHashMap<String, String>();
				%>
				<%@ include file="/shared/template/includes/i_youraccount_links.jspf"%>
				<div id="locabar_user" class="posAbs">
					<div class="ui-arrow-buffer"></div>
					<div class="ui-arrow ui-top"></div>
					<div class="section-cont">
						<div class="section-header">YOUR SETTINGS</div>
						<%
							Iterator<String> itr=folderMap.keySet().iterator();
							while (itr.hasNext()) {
								String str=itr.next();
								%><div class="section-line"><a href="<%= folderMap.get(str)%>"><%= str %></a></div><%
							}
						%>
					</div>
					<hr class="line-divider" />
					<div>
						<div class="footer-item"><%= ((user != null) ? ("Not " + user.getFirstName() + "?") : "") %></div>
						<div class="footer-item right"><a href="#" class="locabar-logout">Sign out</a></div>
					</div>
				</div>
			</div>
			<tmpl:get name="fdx_login_form" />
		</div></tmpl:put>

<%-- CART area --%>
	<tmpl:put name="cartTotal"><div class="locabar-section locabar-popupcart-section" style="margin-right: 0;">
			<div id="locabar_popupcart_trigger">
				<div class="bold cursor-pointer">
					<div class="locabar-cart-count-cont">
						<div class="locabar-cart"></div>
						<div class="locabar-circle-cont locabar-popupcart-count">25</div>
					</div>
					
					<div style="display: inline-block;">
						<div class="locabar-cart-total">
							<div>&nbsp;</div>
							<div class="locabar-popupcart-total">$0.00</div>
							<div class="locabar-down-arrow"></div>
						</div>
					</div>
				</div>
				<div id="locabar_popupcart" class="posAbs">
					<div class="ui-arrow-buffer"></div>
					<div class="ui-arrow ui-top"></div>
					<fd:GetCart id="cart">
						<div id="sidecartbuttons">
							<div class="buttons">
								<a class="cart" href="/view_cart.jsp"><div class="vahidden">cart</div></a><a class="checkout" href="/checkout/view_cart.jsp"><div class="vahidden">checkout</div></a>
							</div>
							<div class="summary">
								<span class="nritems"><em><%= cart.getItemCount() %></em> <% if (cart.getItemCount()>1) {%>items<%} else {%>item<%}%></span><span class="totalprice"><%= JspMethods.formatPrice(cart.getSubTotal()) %></span>
							</div>
						</div>
						<div id="popupcart">
						   	<div class="modifynote"><div class="modifynote-content">You are changing a placed order. <a href="/view_cart.jsp?trk=sidcrt">Click for details.</a></div></div>
						   	<div class="header"><span class="quantity">Quantity</span><span class="price">Price</span></div>
						   	<div class="body">
						       	<table class="content"></table>
						       	<p class="emptymsg">Your cart is empty.</p>
						       	<p class="spinner">Loading cart...</p>
						   	</div>
						   	<div class="footer"><span class="subtotal">Subtotal: </span><span class="totalprice"><%= JspMethods.formatPrice(cart.getSubTotal()) %></span></div>
						   	<div class="footer-buttons">
								<span class="close"></span>
								<a class="cart cssbutton cssbutton-flat" href="/view_cart.jsp">View Cart</a>
								<a class="checkout cssbutton orange cssbutton-flat" href="/checkout/view_cart.jsp">Checkout</a>
							</div>
			    		</div>
					</fd:GetCart>
				</div>
			</div>
		</div></tmpl:put>
</tmpl:insert>