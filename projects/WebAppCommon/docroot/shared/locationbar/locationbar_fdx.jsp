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
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserUtil" %>
<%@ page import="com.freshdirect.fdstore.customer.FDModifyCartModel" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%-- 
--%>
<%
final Logger LOGGER_LOCATIONBAR_FDX = LoggerFactory.getInstance("locationbar_fdx.jsp");
try {
%>
<fd:LocationHandler/>
<%
FDSessionUser user_locationbar_fdx = (FDSessionUser)session.getAttribute(SessionName.USER);
AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
String selectedPickupId = (String)pageContext.getAttribute(LocationHandlerTag.SELECTED_PICKUP_DEPOT_ID_ATTR);
Boolean disabled = (Boolean)pageContext.getAttribute(LocationHandlerTag.DISABLED_ATTR);
MasqueradeContext masqueradeContext = user_locationbar_fdx.getMasqueradeContext();
boolean hasFdxServices = true;//LocationHandlerTag.hasFdxService( ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) );
boolean hasFdServices = LocationHandlerTag.hasFdService( ((selectedAddress!=null) ? selectedAddress.getZipCode() : null) );

List<ErpAddressModel> allHomeAddresses = user_locationbar_fdx.getAllHomeAddresses();
List<ErpAddressModel> allCorporateAddresses = user_locationbar_fdx.getAllCorporateAddresses();
List<FDDeliveryDepotLocationModel> allPickupDepots = (List<FDDeliveryDepotLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);

int addressesBesidesPickupCount = 0;
int addressesIncludingPickupCount = 0;

if (allHomeAddresses != null) {
	addressesBesidesPickupCount += allHomeAddresses.size();
	addressesIncludingPickupCount += allHomeAddresses.size();
}
if (allCorporateAddresses != null) {
	addressesBesidesPickupCount += allCorporateAddresses.size();
	addressesIncludingPickupCount += allCorporateAddresses.size();
}
if (allPickupDepots != null) {
	addressesIncludingPickupCount += allPickupDepots.size();
}
String standingOrder_uri = request.getRequestURI();
boolean isStandingOrders = (standingOrder_uri.indexOf("/standing_orders.jsp") != -1) ? true : false;
%>


<tmpl:insert template="/shared/locationbar/locationbar_layout_fdx.jsp">
	
<%-- MASQUERADE bar --%>
	<% if (masqueradeContext != null) {
		String makeGoodFromOrderId = masqueradeContext.getMakeGoodFromOrderId();
	%>
		<tmpl:put name="topwarningbar">
			<div id="topwarningbar">
				You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user_locationbar_fdx.getUserId()%> (Store: <%= user_locationbar_fdx.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user_locationbar_fdx.getUserContext().getFulfillmentContext().getPlantId() %>)
				<%if (makeGoodFromOrderId!=null) {%>
					<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
					(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
					<a class="imgButtonOrange" href="/cancelmakegood.jsp">Cancel MakeGood</a>
				<%}%>
			</div>
		</tmpl:put>
	<% } %>
<%-- messages icon --%>
	<tmpl:put name="messages"><div class="locabar-section locabar-messages-section" style="display: none;">
			<div id="locabar_messages_trigger" class="cursor-pointer locabar_triggers" tabindex="0">
				<div class="section-warning-small" id="locabar-messages-open">
					<div id="locabar-messages-count" class="locabar-circle-cont messages-count" data-count="0">0</div>
					<span class="offscreen"> delivery alerts</span>
				</div>
			</div>
	</div></tmpl:put>

<%-- FOODKICK tab --%>
	<tmpl:put name="tab_fdx"><% if (hasFdxServices && FDStoreProperties.isFdxTabEnabled()) { %><a href="https://www.foodkick.com" class="locabar-tab locabar-tab-fdx-cont notselected"><span class="offscreen">Visit Foodkick Store</span><div class="locabar-tab-fdx"></div></a><% } else { %><!-- --><% } %></tmpl:put>

<%-- FOODKICK promo content/container --%>
	<tmpl:put name="fdx_promo">
		<%-- this is the fdx promo content container --%>
		<div id="locationbar_fdx_promo" class="">
			<div class="visWrapper" style="display: none;"><%-- don't remove this, it keeps the content from showing during page load --%>
				<fd:IncludeMedia name="/media/layout/nav/globalnav/fdx/promo/main.ftl"></fd:IncludeMedia>
			</div>
		</div>
	</tmpl:put>


<%-- FRESHDIRECT HOME tab --%>
	<%-- change boolean to true to make it linked --%>
	<tmpl:put name="tab_fd"><a href="/index.jsp?serviceType=HOME" class="locabar-tab locabar-tab-fd-cont <%= (!(user_locationbar_fdx.getShoppingCart() instanceof FDModifyCartModel) && (user_locationbar_fdx.isCorporateUser()) ? "notselected" : "") %>"><span class="offscreen">Visit FD Home Store</span><div class="locabar-tab-fd"></div></a></tmpl:put>

<%-- FRESHDIRECT COS tab --%>
	<tmpl:put name="tab_cos"><a href="/index.jsp?serviceType=CORPORATE" class="locabar-tab locabar-tab-cos-cont <%= (!(user_locationbar_fdx.getShoppingCart() instanceof FDModifyCartModel) && !(user_locationbar_fdx.isCorporateUser()) ? "notselected" : "") %>"><span class="offscreen">Visit FD Office Store</span><div class="locabar-tab-cos"><span>Office</span></div></a></tmpl:put>

<%-- ZIP/ADDRESS area --%>
	<%
		String zipAddDisplayString = "Delivery Times";
		boolean isEligibleForPreReservation = false;
		FDReservation userReservervation = null;
		SimpleDateFormat dateFormatterNoYear = new SimpleDateFormat("EEE MM/dd");
		String reservationDate = "";
		String reservationTime = "";
		boolean foundSelectedAddress = false;
		FDOrderAdapter modifyingOrder = FDUserUtil.getModifyingOrder(user_locationbar_fdx);
		String foundSelectedAddressType = "";
		// set the foundSelectedAddressType to home if the address in the order that is modifying has home service type.
		if (modifyingOrder != null && modifyingOrder.getDeliveryAddress() != null && modifyingOrder.getDeliveryAddress().getServiceType() == EnumServiceType.HOME) {
			foundSelectedAddressType = EnumServiceType.HOME.toString();
		}
		AddressModel userReservervationAddressModel = null; //matched by id, may still end up null
		
	
		if (user_locationbar_fdx != null && user_locationbar_fdx.getLevel() != FDUserI.GUEST 
			&& (
				(allHomeAddresses != null && allHomeAddresses.size()>0) || 
				(allCorporateAddresses != null && allCorporateAddresses.size() > 0) || 
				(allPickupDepots!= null && allPickupDepots.size() > 0)
			)
		) {
			zipAddDisplayString = "Delivery Times";
			isEligibleForPreReservation = user_locationbar_fdx.isEligibleForPreReservation();
			
			
			/* reservation logic */
			if (isEligibleForPreReservation) {
				userReservervation = user_locationbar_fdx.getReservation();
			}
			
			
			String addressClass = "address-icon";
			
			//for APPDEV-4742
			String ifSel_CheckImg = "none";
			String ifSel_cssClass = "";
			String ifSelected = "";
			
			
			if( disabled == null || !disabled) {
				%><tmpl:put name="address">
					<div id="locabar_addresses_choices">
						<select id="selectAddressList" aria-label="choose address" name="selectAddressList" style="width: 300px;"><%
							if(allHomeAddresses != null && allHomeAddresses.size()>0){%>
								<optgroup label="Home Delivery">
									<logic:iterate id="homeAddress" collection="<%=allHomeAddresses%>" type="com.freshdirect.common.address.AddressModel">
										<%
										addressClass = "address-icon address-type-home";
											
										if (userReservervation != null && (userReservervation.getAddressId()).equals(homeAddress.getPK().getId()) ) {
											userReservervationAddressModel = homeAddress;
											addressClass += " reservation-icon";
										}

										//whether this home address has already been selected earlier in a previous page load
										if( homeAddress.equals( ((selectedAddress!=null) ? selectedAddress : null) ) ){
											ifSel_CheckImg = "url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;)";
											ifSel_cssClass = "locabar-check-text";
											ifSelected = " selected=\"selected\"";
											
											foundSelectedAddress = true;
											foundSelectedAddressType = EnumServiceType.HOME.toString();
										}else{
											ifSel_CheckImg = "none";
											ifSel_cssClass = "";
											ifSelected = "";
										}
										%>
										<option data-class="<%=addressClass%> <%=ifSel_cssClass%>" data-style="background-image: <%=ifSel_CheckImg%>;" <%=ifSelected %> value="<%=homeAddress.getPK().getId()%>">
											<%=LocationHandlerTag.formatAddressTextWithZip(homeAddress)%>
										</option>
									</logic:iterate>
								</optgroup>
							<%}
							if(allCorporateAddresses != null && allCorporateAddresses.size()>0){%>
								<optgroup label="Office Delivery">
									<logic:iterate id="corporateAddress" collection="<%=allCorporateAddresses%>" type="com.freshdirect.common.address.AddressModel">
										<%
										addressClass = "address-icon address-type-cos";
										
										if (userReservervation != null && (userReservervation.getAddressId()).equals(corporateAddress.getPK().getId()) ) {
											userReservervationAddressModel = corporateAddress;
											addressClass += " reservation-icon";
										}
										
										//whether this home address has already been selected earlier in a previous page load
										if( corporateAddress.equals( ((selectedAddress!=null) ? selectedAddress : null) ) ){
											ifSel_CheckImg = "url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;)";
											ifSel_cssClass = "locabar-check-text";
											ifSelected = " selected=\"selected\"";
											
											foundSelectedAddress = true;
											foundSelectedAddressType = "COS";
										}else{
											ifSel_CheckImg = "none";
											ifSel_cssClass = "";
											ifSelected = "";
										}
										%>
										<option data-class="<%=addressClass%>" data-style="background-image: <%=ifSel_CheckImg%>;" <%=ifSelected %> value="<%=corporateAddress.getPK().getId()%>">
											<%=LocationHandlerTag.formatAddressTextWithZip(corporateAddress)%>
										</option>
									</logic:iterate>
								</optgroup>
							<%}
							if(allPickupDepots != null && allPickupDepots.size()>0){%>
							<optgroup label="Pickup">
								<logic:iterate id="pickupDepot" collection="<%=allPickupDepots%>" type="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel">
									<%
									addressClass = "address-icon address-type-pickup";
									
									//whether this home address has already been selected earlier in a previous page load
									if( selectedPickupId!=null && selectedPickupId.equalsIgnoreCase(pickupDepot.getId()) ){
										ifSel_CheckImg = "url(&apos;/media/layout/nav/globalnav/fdx/locabar-check.png&apos;)";
										ifSel_cssClass = "locabar-check-text";
										ifSelected = " selected=\"selected\"";
										
										foundSelectedAddress = true;
										foundSelectedAddressType = "PICKUP";
									}else{
										ifSel_CheckImg = "none";
										ifSel_cssClass = "";
										ifSelected = "";
									}
									%>
									<option data-class="<%=addressClass%>" data-style="background-image: <%=ifSel_CheckImg%>;" <%=ifSelected %> value="DEPOT_<%= pickupDepot.getId() %>">
										
											<%=LocationHandlerTag.formatAddressTextWithZip(pickupDepot.getAddress())%>
										
									</option>
								</logic:iterate>
							</optgroup>
							<%}
						%></select>
					</div>
					<%
						/* one last zipAddDisplayString change now that we know selected address type */
						if (isEligibleForPreReservation && userReservervation == null && EnumServiceType.HOME.toString().equals(foundSelectedAddressType)) {
							zipAddDisplayString = "Delivery Times";
						}

					
						if (userReservervation != null && userReservervationAddressModel != null && (userReservervationAddressModel).equals( ((selectedAddress!=null) ? selectedAddress : null) ) ) {
							reservationDate = dateFormatterNoYear.format(userReservervation.getStartTime());
							reservationTime = FDTimeslot.format(userReservervation.getStartTime(), userReservervation.getEndTime());
							
							zipAddDisplayString = (reservationDate).toUpperCase();
						}
					%>
				</tmpl:put><%
			} else { //no addresses
				//do nothing i guess	
			}
		} else { //non-signed in user
			%><tmpl:put name="address_change_zip">
				<div class="locabar_addresses-change-zip-cont">
					<div class="text"><label for="newziptext">Change zip code.</label></div>
					<span id="newzip">
<!-- 						<label for="newziptext"><span class="offscreen">change your zip code</span></label> -->
						<input type="text" id="newziptext" class="newziptext placeholder" placeholder="Enter zip code" maxlength="5" onkeydown="goButtonFocus(event);" autocomplete="off"  aria-describedby="zip_error" />						
						<button id="newzipgo"  class="newzipgo cssbutton orange orange-imp cssbutton-flat">Change</button>
						<span class="error-msg" id="zip_error" aria-live="assertive">Incomplete zip code</span>
					</span>
				</div>
			</tmpl:put><%
			

			String shortAddress = LocationHandlerTag.formatAddressShortText( ((selectedAddress!=null) ? selectedAddress : null) );
			if (hasFdServices) { //non-recognized and in deliverable zip
				%><tmpl:put name="address">
					<% if (user_locationbar_fdx == null || user_locationbar_fdx.getLevel() == FDUserI.GUEST) { %>
						<div class="locabar_addresses-anon-deliverable">
							<div class="locabar_addresses-anon-deliverable-header">About FreshDirect Delivery</div>
							<div class="locabar_addresses-anon-deliverable-item-icon-truck">
								<div><a href="/browse.jsp?id=gro_gear_dlvpass">DeliveryPass</a></div>
								<div class="locabar_addresses-anon-deliverable-item-secondary">No Delivery fees!</div>
							</div>
							<div class="locabar_addresses-anon-deliverable-item-icon-info">
								<div><a href="/help/delivery_info.jsp">About Delivery</a></div>
							</div>
							<div class="locabar_addresses-anon-deliverable-item-icon-clock">
								<div><a href="/help/delivery_info_check_slots.jsp"  class="avlTimeFocus">Available Timeslots</a></div>
							</div>
						</div>
					<% } %>
					<%--
						<div class="text bold text12"><%="".equals(shortAddress) ? "" : shortAddress %></div>
						<div class="text" style="margin-bottom: 1.5em;">Shopping in <%= selectedAddress.getZipCode() %> zip code.</div>
					 --%>
					<div class="locabar_addresses-anon-deliverable-change-zip-cont">
						<div class="locabar_addresses-anon-deliverable-change-zip-toggle-cont">Zip Code: <span class="bold"><%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : "") %></span> <button type="button" class="cssbutton small green transparent locabar_addresses-anon-deliverable-change-zip-toggle-btn">Change</button></div>
						<div class="locabar_addresses-anon-deliverable-change-zip-toggle-target" style="display:none;">
							<tmpl:get name="address_change_zip"/>
						</div>
						<button type="button" class="cssbutton green locabar_addresses-<%= (user_locationbar_fdx.getLevel() == FDUserI.GUEST) ? "anon" : "user" %>-deliverable-add-address-btn">Add Delivery Address</button>
					</div>
				</tmpl:put><%
			} else { //non-recognized and not in deliverable zip or LOGGED IN USER, NO ADDRESSES AT ALL
				%><tmpl:put name="address"><div class="locabar_addresses-anon-nondeliverable">

					<% /* LOGGED IN USER, NO ADDRESSES AT ALL */
					if (addressesIncludingPickupCount <= 0) { %>
						<div class="locabar_addresses-none"><a class="cssbutton green" href="/your_account/add_delivery_address.jsp">ADD ADDRESS</a></div>
					<% } %>
					
					<div style="display: inline-block;" class="section-warning">
						<div style="margin: 0 0 1em 10px;">
							<div>FreshDirect does not<br />deliver to zip code:</div>
							<div class="text13 bold"><%="".equals(shortAddress) ? "" : shortAddress + "," %> <%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : "") %></div>
							
						</div>
					</div>
					<tmpl:get name="address_change_zip"/>
					
					<div class="nodeliver-form">
						<% if (user_locationbar_fdx != null && !user_locationbar_fdx.isFutureZoneNotificationEmailSentForCurrentAddress()) { %>
							<form class="n">
								<div class=""><label for="location-email" class="n">Notify me when service comes to my area.</label></div>
								<div>
									<input type="text" id="location-email" class="placeholder" placeholder="Enter your e-mail" aria-describedby="email_error" /><button id="location-submit"  class="cssbutton fdxgreen cssbutton-flat">Send</button>
									<div class="clear"></div>
									<span class="error-msg" id="email_error" aria-live="assertive">E-mail address is invalid</span>
								</div>
							</form>
						<% } %>
					</div>
				</div></tmpl:put>
				<tmpl:put name="location_out_of_area_alert">
					<div id="sitemessage" class="alerts invisible" data-type="sitemessage">
						<div id="nodeliver-form">
							<div style="display: inline-block; margin-right: 30px;" class="section-warning">
								<div style="margin: 0 0 1em 10px; line-height: 14px;" class="text13 bold">
									<div>FreshDirect does not deliver to zip code:</div>
									<div><%="".equals(shortAddress) ? "" : shortAddress + "," %> <%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : "") %></div>
								</div>
							</div>
							<div style="display: inline-block; margin-right: 30px;" >
								<div class="text13"><label for="newziptextmsg">Change your Zip Code.</label></div>
								<span id="newzipmsg"><input type="text" id="newziptextmsg" class="newziptext placeholder" placeholder="Enter zip code" maxlength="5" onkeydown="goButtonFocusAlert(event);" aria-describedby="zip_error"><button id="newzipgomsg"  class="newzipgo cssbutton orange orange-imp cssbutton-flat newzipgoAlert">Change</button></span>
								<span class="error-msg" id="zip_errormsg" aria-live="assertive">Incomplete zip code</span>
							</div>
							<div class="nodeliver-form" style="display: inline-block;" >
								<% if (user_locationbar_fdx != null && !user_locationbar_fdx.isFutureZoneNotificationEmailSentForCurrentAddress()) { %>
									<form class="n">
										<div style="display: inline-block; max-width: 350px;" class="text13"><label for="location-emailmsg" class="n">Notify me when service comes to my area.</label></div>
										<div>
											<input type="text" id="location-emailmsg" class="location-email-text placeholder" placeholder="Enter your e-mail" aria-describedby="email_error" /><button id="location-submitmsg"  class="cssbutton fdxgreen cssbutton-flat">Send</button>
											<span class="error-msg" id="email_errormsg" aria-live="assertive">E-mail address is invalid</span>
										</div>
									</form>
								<% } %>
							</div>
						</div>
					</div>
				</tmpl:put><%
			}
		}
		
    	String dlvInfoLink = "";
    	if (user_locationbar_fdx != null) {
	        if (user_locationbar_fdx.isPickupOnly()) {
	            dlvInfoLink = "/help/delivery_lic_pickup";
	        } else if (user_locationbar_fdx.isDepotUser()) {
	            dlvInfoLink = "/help/delivery_info_depot";
	        } else if (user_locationbar_fdx.getAdjustedValidOrderCount() >= 1) {
	            dlvInfoLink = "/your_account/delivery_info_avail_slots";
	        } else {
	            dlvInfoLink = "/help/delivery_info";
	                if (EnumServiceType.CORPORATE.equals(user_locationbar_fdx.getSelectedServiceType())) {
	                    dlvInfoLink += "_cos";
	                }
	        }
	        dlvInfoLink += ".jsp";
    	}
	%>
	<tmpl:put name="zip_address"><div class="locabar-section locabar-addresses-section">
		<div id="locabar_addresses_trigger" class="locabar_triggers" tabindex="0" aria-haspopup="true" role="menuitem">
				<div class="cursor-pointer">
					<div class="locabar-truck" style="display: inline-block;"></div>
					<div style="display: inline-block;">
						<div class="locabar-addresses-addzip">
							<%
							/* it's possible to not have the selected address in the address list, so check that as well */
							if (user_locationbar_fdx!=null && user_locationbar_fdx.getLevel() != FDUserI.GUEST && foundSelectedAddress) { %>
								<%= LocationHandlerTag.formatAddressTextWithZip(selectedAddress) %>
							<% } else { %>
								<%= selectedAddress.getCity() %> (<%= ((selectedAddress!=null) ? selectedAddress.getZipCode() : "") %>)
							<% } %>
						</div>

						<div class="locabar-addresses-addzip-2ndline">
							<strong><%= zipAddDisplayString %></strong>
							<div class="locabar-arrow"></div>
						</div>
					</div>
				</div>
				
				<div id="locabar_addresses" class="locabar_addresses locabar_triggers_menu posAbs">
					<div class="ui-arrow-buffer"></div>
					<div class="ui-arrow ui-top"></div>
					<% if (user_locationbar_fdx != null &&  user_locationbar_fdx.getLevel() != FDUserI.GUEST) { %>
						<div class="section-header">
							DELIVERY ADDRESSES
							<% if (user_locationbar_fdx != null && user_locationbar_fdx.getLevel() != FDUserI.GUEST) { %><a href="/your_account/delivery_information.jsp" class="locabar_addresses-dlvadd-edit">Edit<span class="offscreen">delivery address</span></a><% } %>
						</div>
					<% } %>
					<tmpl:get name="address" />
					
					<% if (user_locationbar_fdx != null &&  user_locationbar_fdx.getLevel() != FDUserI.GUEST) { %>
						<%
							String temp_delivery_link = "";
							if (user_locationbar_fdx.getLevel() >= FDUserI.RECOGNIZED) {
								temp_delivery_link = "/your_account/delivery_info_avail_slots.jsp";
							} else {
								temp_delivery_link = "/your_account/delivery_info_check_slots.jsp";
							}
							//check if user has addresses besides pickup
							if (addressesBesidesPickupCount <= 0) {
								//nope, change url
								temp_delivery_link = "/help/delivery_info_check_slots.jsp";
							}
						%>
						<div class="section-header">
							TIMESLOT
							<a href="<%= dlvInfoLink %>" class="locabar_addresses-ts-info" title="Delivery Info"><span class="offscreen">view available timeslots information</span></a>
						</div>
						<% if (isEligibleForPreReservation) { %>
							<% if (userReservervation == null || !(userReservervation.getAddressId()).equals( ((selectedAddress!=null) ? selectedAddress.getId() : null) ) ) { %>
								<div class="locabar_addresses-reservation-none">
									<div class="locabar_addresses-reservation-view-cont">
										<a href="<%=temp_delivery_link %>" style="<%= ((foundSelectedAddress && (foundSelectedAddressType == EnumServiceType.HOME.toString()))) ? " display: none;" : "" %>" class="cssbutton green transparent cssbutton-flat locabar_addresses-reservation-view">View Timeslots</a>
									</div>
									<% if (foundSelectedAddress && (foundSelectedAddressType != EnumServiceType.HOME.toString())) { %>
										<div class="locabar_addresses-reservation-make-cont" style="display: none;">
											<a href="/your_account/reserve_timeslot.jsp" class="cssbutton orange cssbutton-flat locabar_addresses-reservation-make disabled">Make a Reservation</a>
											<%-- This text is also in locationbar_fdx.js --%>
											<div class="locabar_addresses-reservation-make-notFor">Not for <%= ("PICKUP".equals(foundSelectedAddressType)) ? "Pickup Option" : ("COS".equals(foundSelectedAddressType)) ? "Office Delivery" : "&nbsp;" %></div>
										</div>
									<% } else {
										request.setAttribute("temp_delivery_link_attrib", "/your_account/reserve_timeslot.jsp");
									%>
										<div class="locabar_addresses-reservation-make-cont">
											<a href="/your_account/reserve_timeslot.jsp" class="cssbutton orange cssbutton-flat locabar_addresses-reservation-make">Make a Reservation</a>
											<div class="locabar_addresses-reservation-make-notFor">&nbsp;</div>
										</div>
									<% } %>
								</div>
							<% } else { %>
								<div class="locabar_addresses-reservation-existing">
									<div id="locabar_addresses-reservation-existing-value">
										<div class="locabar_addresses-reservation-existing-time">
											<%= (reservationDate+" @ "+reservationTime).toUpperCase() %> 
											<a href="/your_account/reserve_timeslot.jsp" class="locabar_addresses-reservation-existing-change">Change</a>
										</div>
										<div class="locabar_addresses-reservation-existing-address"><%= (userReservervationAddressModel != null) ? LocationHandlerTag.formatAddressTextWithZip(userReservervationAddressModel) : "null" %></div>
									</div>
								</div>
							<% } %>
						<% }else{ %>
								<div class="locabar_addresses-reservation-none">
									<a href="<%=temp_delivery_link %>" class="cssbutton green transparent cssbutton-flat locabar_addresses-reservation-view">View Timeslots</a>
								</div>
						<% } %>
					<% } %>
				</div>
			</div>
		</div><script>

				window.FreshDirect = window.FreshDirect || {};
				FreshDirect.locabar = FreshDirect.locabar || {};
				FreshDirect.locabar.hasFdServices = <%=hasFdServices %>;
				FreshDirect.locabar.hasFdxServices = <%=hasFdxServices %>;
				FreshDirect.locabar.selectedAddress = {
					type: '<%= foundSelectedAddressType %>',
					address: decodeURIComponent('<%= URLEncoder.encode(LocationHandlerTag.formatAddressTextWithZip(selectedAddress)) %>')
				};
				FreshDirect.locabar.reservation = { address: null, time: null };
				<% if (userReservervationAddressModel != null) { %>
					FreshDirect.locabar.reservation.address = decodeURIComponent('<%= URLEncoder.encode(LocationHandlerTag.formatAddressTextWithZip(userReservervationAddressModel)) %>');
					FreshDirect.locabar.reservation.time = '<%= (reservationDate+" @ "+reservationTime).toUpperCase() %>';
				<% } %>
				FreshDirect.locabar.zipcode = '<%= user_locationbar_fdx.getZipCode() %>';
			</script></tmpl:put>
	
<%-- SIGN IN area --%>
	<tmpl:put name="sign_in"><%
			String actionString = "Sign in";
			String greetingsString = "Hi";
			boolean signedIn = false; //used for js logic for hover/click event results
			boolean recog = false; //used for js logic for hover/click event results
			if (user_locationbar_fdx != null && user_locationbar_fdx.getLevel() != FDUserI.GUEST) {
				actionString = "Your Account";
				greetingsString += " "+user_locationbar_fdx.getFirstName();
				
				if (user_locationbar_fdx.getLevel() == FDUserI.SIGNED_IN) {
					signedIn = true;
				}
				if (user_locationbar_fdx.getLevel() == FDUserI.RECOGNIZED) {
					recog = true;
				}
			}

			greetingsString += "!";
			
		%><div class="locabar-section locabar-user-section" data-signedin="<%= signedIn %>">
      <div id="locabar_user_trigger" class="locabar_triggers" <% if (signedIn || recog) { %>tabindex="0"<% } %> role="menuitem" aria-haspopup="true" data-signedin="<%= signedIn %>" data-recog="<%= recog %>" data-social="<%= FDStoreProperties.isSocialLoginEnabled() %>">
		        <% if (!signedIn) {%>
					<a id="locabar_user_login_link" class = "changeBGClr" href="/login/login.jsp" <%if(recog) { %> tabindex="-1" <%}%> fd-login-required fd-login-nosignup fd-login-successpage-current>
		        <% } %>
					<div class="cursor-pointer">
						<%-- <div class="locabar-user" style="display: inline-block;"></div> --%>
						<div style="display: inline-block;">
							<div class="locabar-user-greeting-cont">
					            <div class="locabar-user-greeting">
									<%= greetingsString %>
					            </div>
					            <div class="locabar-user-action">
									<strong><%= actionString %></strong><div class="locabar-arrow"></div>
					            </div>
							</div>
						</div>
					</div>
		        <% if (!signedIn) { %>
		          </a>
		        <% } %>
				<%
					Map<String, String> folderMap=new LinkedHashMap<String, String>();
				%>
				<%@ include file="/shared/template/includes/i_youraccount_links.jspf"%>
				
				<%-- USER RECOMMENDERS TEST - also see locationbar_fdx.js
					<div id="locabar_user_reco_cont" class="user-reco-cont">
						<div id="locabar_user_reco">test reco</div>
					</div>
				--%>
				<div id="locabar_user" class="locabar_triggers_menu posAbs">
					<div class="ui-arrow-buffer"></div>
					<div class="ui-arrow ui-top"></div>
					<div class="section-cont">
						<div class="section-header">YOUR FRESHDIRECT</div>
						<%
							String temp_delivery_link = "/help/delivery_info_check_slots.jsp";
						
							if (user_locationbar_fdx != null && user_locationbar_fdx.getLevel() >= FDUserI.RECOGNIZED) {
								if (user_locationbar_fdx.getLevel() >= FDUserI.RECOGNIZED) {
									temp_delivery_link = "/your_account/delivery_info_avail_slots.jsp";
								} else {
									temp_delivery_link = "/your_account/delivery_info_check_slots.jsp";
								}
								//check if user has addresses besides pickup
								if (addressesBesidesPickupCount <= 0) {
									//nope, change url
									temp_delivery_link = "/help/delivery_info_check_slots.jsp";
								}
							}
							
							Iterator<String> itr=folderMap.keySet().iterator();
							while (itr.hasNext()) {
								String str=itr.next();								
								
								// This is for the Global Navigation Microsite URL for Extole
								if (str == "Refer A Friend") { %>
									<div class="section-line"><a href="<%= FDStoreProperties.getPropExtoleMicrositeGlobalNavUrl() %>" target="_blank"><%= str %></a></div>
								<% } else if (!"Reminder Service".equals(str) && !"Profile".equals(str)) { %>
									<% if ("Account Preferences".equals(str)) { %>
										<hr class="line-divider" style="margin-bottom: 1em;" />										
									<% } %>
									<% if ("View Timeslots".equals(str)) { /* this item changes based on user and addresses */ %>
										<div class="section-line"><a href="<%= temp_delivery_link %>" fd-login-required><%= str %></a></div>
									<% } else if ("Reserve Delivery".equals(str) && !EnumServiceType.HOME.toString().equals(foundSelectedAddressType) ) { %>
										<%-- If the user has the ability to reserve and non-Home selected, modify Reserve Link --%>
										<div class="section-line"><a href="<%= temp_delivery_link %>" fd-login-required>View Timeslots</a></div>
									<% } else if ("DeliveryPass".equals(str) ) { %>
										<% if (EnumServiceType.HOME.equals(user_locationbar_fdx.getSelectedServiceType())) {%>
											<div class="section-line"><a href="<%= folderMap.get(str)%>" fd-login-required><%= str %></a></div>								
										<% } %>
									<% } else { %>
										<div class="section-line"><a href="<%= folderMap.get(str)%>" fd-login-required><%= str %></a></div>									
									<% } %>
								<% } %>
							<% } %>
					</div>
					<hr class="line-divider" />
					<div class="section-cont locabar-user-signout">
						<div class="footer-item"><%= ((user_locationbar_fdx != null) ? ("Not " + user_locationbar_fdx.getFirstName() + "?") : "") %></div>
						<div class="footer-item">
							<a href="#" class="cssbutton green transparent cssbutton-flat locabar-logout">Sign out</a>
						</div>
					</div>
				</div>
			</div>
		
		</div></tmpl:put>

<%-- CART area --%>
	<tmpl:put name="cartTotal"><div class="locabar-section locabar-popupcart-section" style="margin-right: 0;">
			<div id="locabar_popupcart_trigger" class="locabar_triggers" role="menuitem">
				<div class="bold cursor-pointer locabar_triggers_menuitem">
          			<a id="popup_cart" href="/view_cart.jsp">
						<span class="offscreen">ViewCart</span>
						<div style="display: inline-block;">
							<div class="locabar-cart-count-cont">
								<div class="locabar-cart"></div>
								<div class="locabar-circle-cont locabar-popupcart-count"> 0<span class="offscreen">items</span></div>
							</div>
						</div>
						<div style="display: inline-block;">
							<div class="locabar-cart-total">
								<!-- <div>&nbsp;</div> -->
								<div class="locabar-popupcart-total"> $0.00 </div>
							</div>
							<div class="locabar-popupcart-label">
								Cart
								<div class="locabar-arrow"></div>
							</div>
						</div>
					</a>
				</div>
				<div id="locabar_popupcart" class="locabar_triggers_menu posAbs">
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
						   	<div class="header"><span class="quantity">Quantity</span><span class="price">Price</span></div>
						   	<div class="body">
						       	<table class="content"></table>
						       	<p class="emptymsg">Your cart is empty.</p>
						       	<p class="spinner">Loading cart...</p>
						   	</div>
						   	<div class="footer">
								<span class="subtotal">Subtotal: </span>
								<span class="totalprice"><%=JspMethods.formatPrice(cart.getSubTotal())%></span>
								<span class="save-amount">You've Saved <%=JspMethods.formatPrice(cart.getSaveAmount(false))%></span>
							</div>
							<div class="footer-buttons">
								<span class="close"></span><br />
								<a class="cart cssbutton cssbutton-flat" aria-label="go to view cart page" href="/view_cart.jsp">View Cart</a>
								<%
									FDCartModel locationbarFdxCart = user_locationbar_fdx.getShoppingCart();
								%>
								<a class="checkout cssbutton orange cssbutton-flat" aria-label="<%= (locationbarFdxCart instanceof FDModifyCartModel) ? "go to Review Changes page" : "go to Checkout page" %>" href="/checkout/view_cart.jsp" fd-login-required><%= (locationbarFdxCart instanceof FDModifyCartModel) ? "Review Changes" : "Checkout" %></a>
							</div>
			    		</div>
					</fd:GetCart>
				</div>
			</div>
		</div>
		<div class="locabar-section side-spacer"></div>
	</tmpl:put>
		
<%-- OUT OF AREA ALERT --%>
	<% if (user_locationbar_fdx != null && user_locationbar_fdx.getLevel() == FDUserI.GUEST) { %>
	<% } %>

<%-- SO ALERTS --%>

	<%	  
	  if(user_locationbar_fdx.isNewSO3Enabled() && !isStandingOrders) {
		Map<String,Object> errorSOAlert = new HashMap<String,Object>();
		HashMap<String,Object> soData = StandingOrderHelper.getAllSoData(user_locationbar_fdx, false, false) ;
		errorSOAlert.put("soData", soData);
	%>
	
	<script>
		window.FreshDirect = window.FreshDirect || {};
		FreshDirect.standingorder = FreshDirect.standingorder || {};
		FreshDirect.standingorder.isSoCartOverlayFirstTime = <%= user_locationbar_fdx.isSoCartOverlayFirstTime() %>;
	</script>
	<tmpl:put name="error_so_alerts">
		<div id="errorsoalerts" class="alerts invisible" data-type="errorsoalerts">
			<soy:render template="standingorder.errorSOAlert" data="<%=errorSOAlert%>"/>
		</div>
	</tmpl:put>

	<tmpl:put name="min_so_alerts">
		<div id="minsoalert" class="alerts invisible" data-type="minsoalert">
			<soy:render template="standingorder.minSOAlert" data="<%=errorSOAlert%>"/>			
		</div>
	</tmpl:put>

	<%
		Map<String,Object> activateSOAlert = new HashMap<String,Object>();
		activateSOAlert.put("soData", soData);//StandingOrderHelper.getAllSoData(user_locationbar_fdx, false,false));
	%>
	<tmpl:put name="activate_so_alerts">
		<div id="activatesoalert" class="alerts invisible" data-type="activatesoalert">
			<soy:render template="standingorder.activateSOAlert" data="<%=activateSOAlert%>"/>
		</div>
	</tmpl:put>
  <% } %>
</tmpl:insert>


<%
} catch (Exception e) {
	LOGGER_LOCATIONBAR_FDX.debug("error in locationbar_fdx.jsp "+e);
	e.printStackTrace();
}%>
