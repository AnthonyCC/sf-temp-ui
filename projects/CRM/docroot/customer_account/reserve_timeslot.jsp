<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvDepotModel"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.FDReservation"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>

<%@ taglib uri="template" prefix="tmpl"%>
<%@ taglib uri="logic" prefix="logic"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="crm" prefix="crm"%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Account Details > Edit Timeslot Reservation</tmpl:put>

	<tmpl:put name="header" direct="true">
		<jsp:include page="/includes/customer_header.jsp" />
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<crm:GetFDUser id="user">
			<%
	boolean isStaticSlot = false;
	//FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String actionName = request.getParameter("actionName");
	Collection shipToAddresses = new ArrayList();
	ErpAddressModel address = null;
	String addressId = request.getParameter("addressId");
	String pageURI = request.getRequestURI();
	boolean fromCheckout = "checkout".equalsIgnoreCase(request.getParameter("from"));
	boolean fromCheckoutReview = "checkout_review".equalsIgnoreCase(request.getParameter("from"));
	boolean fromShopping = "order".equalsIgnoreCase(request.getParameter("from"));
	String cancelLink = fromCheckout ? "/checkout/checkout_select_addr_payment.jsp" : "/main/account_details.jsp";
	if (fromCheckoutReview) cancelLink = "/checkout/checkout_review_items.jsp";
	if (fromShopping) cancelLink = "/order/place_order_build.jsp";
	String backPage = fromCheckout ? "CHECKOUT STEP 2" : "ACCOUNT DETAILS";
	if (fromCheckoutReview) backPage = "CHECKOUT STEP 1";
	if (fromShopping) backPage = "BUILD ORDER";
	
	int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
	if("true".equals(request.getParameter("chefstable"))) {
		timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
	}
	if(user.isHomeUser()){
	    shipToAddresses = FDCustomerManager.getShipToAddresses(user.getIdentity());
		for (Iterator i=shipToAddresses.iterator(); i.hasNext(); ) {
			ErpAddressModel a = (ErpAddressModel)i.next();
			if ( a.getPK().getId().equals(addressId) ) {
				address = a;
				break;
			} 
		}
	} else {
	    DlvLocationModel location = null;%>
			<fd:GetDepotLocations id='locations'
				depotCode='<%=user.getDepotCode()%>'>

				<%String dlocId = FDCustomerManager.getDefaultDepotLocationPK(user.getIdentity());%>
				<logic:iterate id="loc" collection="<%= locations %>"
					type="com.freshdirect.delivery.depot.DlvLocationModel">
					<% 
				DlvDepotModel depot = FDDepotManager.getInstance().getDepotByLocationId( loc.getPK().getId() );
				ErpDepotAddressModel address1 = new ErpDepotAddressModel(loc.getAddress());
				address1.setRegionId(depot.getRegionId());
				address1.setZoneCode(loc.getZoneCode());
				address1.setLocationId(loc.getPK().getId());
				address1.setFacility(loc.getFacility());
				
				shipToAddresses.add(address1);
		
				if(addressId == null && dlocId.equals(loc.getPK().getId())){
					location = loc;
					address = address1;
				} else if(addressId.equals(loc.getPK().getId())){
					location = loc;
					address = address1;
				}
			%>
				</logic:iterate>
			</fd:GetDepotLocations>
			<%}%>

			<div class="cust_module" style="width: 90%;"><%--form name="delivery_reservation" method="POST"--%>
			<div class="cust_module_header">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="cust_module_header_text" width="25%">Reserve a
					Delivery Time</td>
					<td width="50%" align="center"><a href="<%=cancelLink%>"
						class="cancel" style="width:250px;">BACK TO <%=backPage%></a><%--&nbsp;&nbsp;<a href="javascript:document.delivery_address.submit();" class="save">SAVE</a--%></td>
					<td width="25%" align="right" class="note"
						style="padding-right: 8px;">* Required</td>
				</tr>
			</table>
			</div>
			<div class="cust_module_content"
				style="padding-top: 10px; padding-bottom: 18px;">
				<fd:ReserveTimeslotController
					actionName="<%=actionName%>" result="result">
				<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult"
					address="<%=address%>" deliveryInfo="<%=true%>">
					<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	FDReservation rsv = user.getReservation();
	List messages = DeliveryTimeSlotResult.getMessages();
	List comments = DeliveryTimeSlotResult.getComments();
	%>
					<table width="90%" cellpadding="0" cellspacing="0" border="0"
						align="center">
						<tr>
							<td colspan="3"><%@ include
								file="/shared/includes/delivery/i_loyalty_banner.jspf"%>
							</td>
						</tr>
						<tr>
							<td colspan="3" class="text12">Displayed below are the
							timeslots currently available to be reserved for:</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<%if (shipToAddresses.size() > 1 ) {%>
							<form name="addressForm" method="POST"
								action="/customer_account/reserve_timeslot.jsp">
							<td class="text12"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
							<%if(user.isHomeUser()){
							if (addressId == null && user.isHomeUser()) {
								addressId = FDCustomerManager.getDefaultShipToAddressPK(user.getIdentity()); // default address
							}
							for (Iterator i=shipToAddresses.iterator(); i.hasNext(); ) {
								ErpAddressModel a = (ErpAddressModel)i.next();
								if ( a.getPK().getId().equals(addressId) ) {
									address = a;
									break;
								} 
							}
							if (address == null) {
								// no address found (no default, or wrong pk) -> pick the first one
								address = (ErpAddressModel)shipToAddresses.iterator().next();
								addressId = address.getPK().getId();
							}
						}%> <select name="addressId" onChange="javascript:submit()"
								CLASS="text12">
								<%for(Iterator saItr=shipToAddresses.iterator();saItr.hasNext();) {
							ErpAddressModel addr = (ErpAddressModel)saItr.next();
							String id = "";
							if(addr instanceof ErpDepotAddressModel){
								id = ((ErpDepotAddressModel)addr).getLocationId();
							}else {
								id = addr.getPK().getId();
							}%>
								<option value="<%=id %>"
									<%=id.equals(addressId) ? "SELECTED" : "" %>><%=addr.getAddress1()%>,
								<%=addr.getCity()%>, <%=addr.getZipCode()%></option>
								<%}%>
							</select></td>
							</form>
							<%}else{
					if(user.isHomeUser()) {
						address = (ErpAddressModel)shipToAddresses.iterator().next();
						if(address instanceof ErpDepotAddressModel){
							addressId = ((ErpDepotAddressModel)address).getLocationId();
						}else {
							addressId = address.getPK().getId();
						}
				%>
							<td class="text12"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
							<b><%=address.getAddress1()%><br>
							<%=address.getZipCode()%></b></td>
							<%}%>
							<%}%>
						</tr>
						<tr>
							<td><img src="/media_stat/images/layout/clear.gif"
								width="15" height="15"></td>
							<td><img src="/media_stat/images/layout/clear.gif"
								width="15" height="15"></td>
							<td><img src="/media_stat/images/layout/clear.gif"
								width="663" height="15"></td>
						</tr>
						<tr>
							<td colspan="3"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
						</tr>
		<%
			String preReserveSlotId = "";
			boolean hasPreReserved = false;
			if(rsv != null){ 
				if(!rsv.getAddressId().equals(addressId)){
					rsv = null;
				}else{
					preReserveSlotId = rsv.getTimeslotId();
					hasPreReserved = true;
				}
			}
			%>
						<tr>
							<td colspan="3" class="text12">You can use this page to
							place a standing, weekly reservation or to reserve a time for
							just this week. Please note that <b>delivery time reservation
							expire one hour prior to cutoff</b>, so be sure to complete checkout
							by that time to gurantee your timeslot remains open. Timeslots
							unavailable for reservation may become available at time of
							checkout. <% if (hasPreReserved) { %> <br>
							<br>
							<img src="/media_stat/images/layout/ff9933.gif" width="12"
								height="12"> = Your <%= EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType()) ? "Weekly" : "" %>
							Reserved Delivery Slot <% } %>
							</td>
						</tr>
						<tr>
							<td colspan="3">
							<form name="reserveTimeslot" method="POST"
								action="/customer_account/reserve_timeslot.jsp?chefstable<%=request.getParameter("chefstable")%>&addressId=<%=request.getParameter("addressId")%>"
								name="reserveTimeslot"><img
								src="/media_stat/images/layout/ff9933.gif" width="100%"
								height="1" border="0" vspace="8"><br>
								<input type="hidden" name="chefstable" value="<%= request.getParameter("chefstable") %>"/>
							<% String[] checkErrorType = {"deliveryTime", "reservationType", "reservation", "technical_difficulty", "addressId"}; %>
							<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>'
								id='errorMsg'>
								<%@ include file="/includes/i_error_messages.jspf"%>
							</fd:ErrorHandler> <%String timeSlotId = ""; %> <logic:iterate id="timeslots"
								collection="<%=timeslotList%>"
								type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
								<% // fix for advance orders showing on this page
								if (idx.intValue() == timeslotList.size()-1 && timeslotList.size() > idx.intValue()) { %>
									<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
								<% } %>
							</logic:iterate> <b>X</b> = Delivery slot full<br>
							<img src="/media_stat/images/layout/ff9933.gif" width="100%"
								height="1" border="0" vspace="8">
							</td>
						</tr>
					</table>
					<!-- Bryan Restriction Message Added -->
					<% if(messages != null && messages.size() >= 1) { %>
						<%@ include file="/shared/includes/delivery/i_restriction_message.jspf"%>
					<% } %>
					<% if(comments != null && comments.size() >= 1) { %>
						<%@ include file="/shared/includes/delivery/i_restriction_comment.jspf"%>
					<% } %>
					
					<table width="90%" cellpadding="0" cellspacing="0" border="0"
						align="center">
						<tr>
							<td colspan="7"><img
								src="/media_stat/images/template/youraccount/choose_reservation_type.gif"
								width="256" height="10" vspace="10"></td>
						</tr>
						<tr valign="top">
							<td><input type="radio" name="reservationType"
								<%=(rsv == null || EnumReservationType.ONETIME_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %>
								value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>"
								class="radio">&nbsp;</td>
							<td colspan="6"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
							<span class="text12"><b>Reserve for this week only.</b></span></td>
						</tr>
						<tr>
							<td colspan="7"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
						</tr>
						<tr valign="top">
							<td><input type="radio" name="reservationType"
								<%=(rsv != null && EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %>
								value="<%=EnumReservationType.RECURRING_RESERVATION.getName()%>"
								class="radio">&nbsp;</td>
							<td colspan="6"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
							<span class="text12"><b>Reserve this day and time for
							me every week</b></span><br>
							Select this option to make this a standing, weekly reservation.
							Please note that reservations not used for two weeks will be
							cleared. At any time you can return to re-place or update your
							reservation.</td>
						</tr>
						<input type="hidden" name="addressId" value="<%=addressId%>">
						<input type="hidden" name="actionName" value="">
						<tr>
							<td colspan="7" align="center"><img
								src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
							<%if(rsv == null || rsv.isAnonymous()){%> <input type="image"
								src="/media_stat/images/buttons/reserve_delivery.gif"
								onclick="reserveTimeslot.actionName.value='reserveTimeslot'">
							<%} else {%> <input type="image"
								src="/media_stat/images/buttons/reserve_delivery_cancel.gif"
								onclick="reserveTimeslot.actionName.value='cancelReservation'">&nbsp;&nbsp;&nbsp;<input
								type="image"
								src="/media_stat/images/buttons/reserve_delivery_save_changes.gif"
								onclick="reserveTimeslot.actionName.value='changeReservation'">
							<%}%>
							</td>
						</tr>
					</table>
					</form>
				</fd:DeliveryTimeSlot>
			</fd:ReserveTimeslotController></div>
			<%--/form--%></div>
		</crm:GetFDUser>
		<br clear="all">
	</tmpl:put>

</tmpl:insert>