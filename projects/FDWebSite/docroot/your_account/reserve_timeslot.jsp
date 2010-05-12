<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.delivery.depot.DlvLocationModel" %>
<%@ page import="com.freshdirect.delivery.depot.DlvDepotModel" %>
<%@ page import="com.freshdirect.delivery.EnumReservationType" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.FDTimeslot" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.webapp.util.TimeslotPageUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
String addressId = NVL.apply(request.getParameter("addressId"), "");
boolean isStaticSlot = false;
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;

if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}

// redirect, if not eligible
if(!user.isEligibleForPreReservation()) {
	response.sendRedirect("/your_account/");
	return;
}

String actionName = request.getParameter("actionName");

Collection shipToAddresses = new ArrayList();
ErpAddressModel address = null;

if(user.isHomeUser()){
    shipToAddresses = FDCustomerManager.getShipToAddresses(user.getIdentity());
} else {
    DlvLocationModel location = null;%>
	<fd:GetDepotLocations id='locations' depotCode='<%=user.getDepotCode()%>'>
    
		<%String dlocId = FDCustomerManager.getDefaultDepotLocationPK(user.getIdentity());%>
		<logic:iterate id="loc" collection="<%= locations %>" type="com.freshdirect.delivery.depot.DlvLocationModel">
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

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Reserve Timeslot</tmpl:put>
    <tmpl:put name='content' direct='true'>
	<fd:ReserveTimeslotController actionName="<%=actionName%>" result="result">
	<%	FDReservation rsv = user.getReservation();
		ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());
		boolean hasWeeklyReservation = customerInfo.getRsvAddressId() != null && !"".equals(customerInfo.getRsvAddressId()) && (addressId.equals(customerInfo.getRsvAddressId()) || "".equals(addressId));
		if("".equals(addressId) && rsv != null){
			addressId = rsv.getAddressId();
		}
		if("".equals(addressId)){
			addressId = NVL.apply(customerInfo.getRsvAddressId(), "");
		}
		boolean hasReservation = rsv != null && addressId.equals(rsv.getAddressId());
	%>
	
	<%// CONFIRMATION MESSAGE %>
	<%if(result.isSuccess() && "POST".equalsIgnoreCase(request.getMethod()) && !"".equals(actionName) && actionName != null){
		String confirmationMsg = "Your delivery timeslot ";
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
			if ( hasReservation ) { 
			    String reservationDate = dateFormatter.format(rsv.getStartTime());
				String reservationTime = FDTimeslot.format(rsv.getStartTime(), rsv.getEndTime());
				if ("reserveTimeslot".equalsIgnoreCase(actionName)) {
					confirmationMsg += "for " +reservationDate+", "+reservationTime+" has been reserved."; 
				} else if ("changeReservation".equalsIgnoreCase(actionName)) {
					confirmationMsg += "reservation has been changed to " +reservationDate+", "+reservationTime+".";
				}
				if ( EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType()) ) {
					confirmationMsg += " This time and day will be automatically reserved for you every week.";
				}
			} else if ("cancelReservation".equalsIgnoreCase(actionName)) {
				confirmationMsg += "reservation has been cancelled.";
			}
			
		%>
		<%@ include file="/includes/i_confirmation_messages.jspf"%>
	<%	} %>



	<%
	if (user.isChefsTable()) {
		%>
		<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
		<br>
		<%
	 }
	%>
	
		<table width="693" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="3" class="title16"><%=(hasReservation) ? "Your Delivery Timeslot Is Reserved" : "Reserve a Delivery Time"%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td>
			</tr>
			<tr>
				<td colspan="3" class="text12"><%= hasReservation ? "Displayed below is your current delivery reservation for" : "Displayed below are the timeslots currently available to be reserved for"%>:</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<%if (shipToAddresses.size() > 1 ) {%>
					<form name="addressForm" method="POST" action="/your_account/reserve_timeslot.jsp">
						<input type="hidden" name="chefstable" value="<%= request.getParameter("chefstable") %>"/>
						<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
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
						}%>
						<select name="addressId" onChange="javascript:submit()" CLASS="text12">
						<%for(Iterator saItr=shipToAddresses.iterator();saItr.hasNext();) {
							ErpAddressModel addr = (ErpAddressModel)saItr.next();
							String id = "";
							if(addr instanceof ErpDepotAddressModel){
								id = ((ErpDepotAddressModel)addr).getLocationId();
							}else {
								id = addr.getPK().getId();
							}%>
							<option value="<%=id %>" <%=id.equals(addressId) ? "SELECTED" : "" %>><%=addr.getAddress1()%>, <%=addr.getCity()%>, <%=addr.getZipCode()%></option>
						<%}%>
						</select>
						<br><br>
						<b>Please note that you may only have a delivery<br>timeslot reservation for one address at a time.</b>
					</td>
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
					<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b><%=address.getAddress1()%><br><%=address.getZipCode()%></b></td>
					<%}%>
				<%}%>
			</tr>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="663" height="15"></td>
			</tr>
			<tr>
				<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
			</tr>
			<%
			String preReserveSlotId = "";
			String timeSlotId = request.getParameter("deliveryTimeslotId");
			if("cancelReservation".equals(actionName)){
				timeSlotId = "";
			}
			boolean hasPreReserved = false;
			if(rsv != null){ 
				if(!rsv.getAddressId().equals(addressId)){
					rsv = null;
				}else{
					preReserveSlotId = rsv.getTimeslotId();
					hasPreReserved = true;
					timeSlotId = rsv.getTimeslotId();
				}
			}
			%>
			<tr>
				<td colspan="3" class="text12">
				<%
				String timeMsg = java.text.MessageFormat.format( "{0,choice,1#one hour|1<{0,number,integer} hours}", new Object[] { new Integer(FDStoreProperties.getPreReserveHours()) });
				
				if(hasReservation) {
					%>
					You can use this page to update or cancel your reservation. Please note that <b>your reservation will expire at
					<%= CCFormatter.formatDeliveryTime(rsv.getExpirationDateTime()) %> on <%= CCFormatter.formatDayName(rsv.getExpirationDateTime()) %>
					(<%= timeMsg %> prior to cutoff),</b> so be sure to complete checkout by that time to guarantee your timeslot remains available.
					Timeslots unavailable for reservation now may become available at time of checkout.
					<%
				} else {
					%>
					You can use this page to place a standing weekly timeslot reservation or to reserve a time for just this week. Please note
					that <b>delivery timeslot reservations expire <%= timeMsg %> prior to cutoff</b>, so be sure to complete checkout by that time
					to guarantee your timeslot remains open. Timeslots unavailable for reservation now may become available at time of checkout.
					<%
				}
				if (hasPreReserved) {
					%>
					<br><br><img src="/media_stat/images/layout/ff9933.gif" width="12" height="12"> = Your <%= EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType()) ? "Weekly" : "" %> Reserved Delivery Slot
					<%
				}
				%>
				</td>
			</tr>
			<tr>
				<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
			</tr>

		<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" deliveryInfo="<%=true%>">
			<%
			List timeslotList = DeliveryTimeSlotResult.getTimeslots();
			Map zones = DeliveryTimeSlotResult.getZones();
			boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
			List messages = DeliveryTimeSlotResult.getMessages();
			%>


			<tr>
				<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
			</tr>
			<form name="reserveTimeslot" method="POST" action="/your_account/reserve_timeslot.jsp" name="reserveTimeslot">
					<input type="hidden" name="chefstable" value="<%= request.getParameter("chefstable") %>"/>
		</table>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
		<img src="/media_stat/images/layout/ff9933.gif" width="693" height="1" border="0"><br>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
		<% String[] checkErrorType = {"deliveryTime", "reservationType", "reservation", "technical_difficulty", "addressId"}; %>
		<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
			<%@ include file="/includes/i_error_messages.jspf" %>
		</fd:ErrorHandler>

			<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
				<%if(rsv == null && hasWeeklyReservation){
					String foundId = "";
					int dayOfWeek = customerInfo.getRsvDayOfWeek();
					Calendar cal = Calendar.getInstance();
					if(cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
						cal.add(Calendar.DATE, 1);
					}
					while (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
						cal.add(Calendar.DATE, 1);
					}
					for(Iterator i = timeslots.getDays().iterator(); i.hasNext(); ){
						Date day = (Date)i.next();
						for(Iterator j = timeslots.getTimeslotsForDate(day).iterator(); j.hasNext(); ){
							FDTimeslot slot = (FDTimeslot)j.next();
							if(slot.isMatching(cal.getTime(), customerInfo.getRsvStartTime(), customerInfo.getRsvEndTime())){
								foundId = slot.getTimeslotId();
								break;
							}
						}
					}
					if(!"".equals(foundId)){
						timeSlotId = foundId;
						preReserveSlotId = foundId;
						hasPreReserved = true;
					}else{
						hasWeeklyReservation = false;
					}
				}%>
				<% // fix for advance orders showing on this page
				if (idx.intValue() == timeslotList.size()-1 && timeslotList.size() > idx.intValue()) { %>
					<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
				<% } %>
			</logic:iterate>
	<!-- Bryan Restriction Message Added -->
	<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_restriction_message.jspf"%>
	<% } %>
			<table width="693" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="left">
					<IMG src="/media_stat/images/template/checkout/x_trans.gif" WIDTH="9" HEIGHT="9" BORDER="0" hspace="5"> = Delivery slot full
				</td>
				<td align="right">
				You must complete checkout for next-day deliveries before the "Order by" time.
				</td>
			</tr>
			</table>
			<br/>
			</div>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
			<img src="/media_stat/images/layout/ff9933.gif" width="693" height="1" border="0"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
		</fd:DeliveryTimeSlot>
		<table width="693" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="7"><img src="/media_stat/images/template/youraccount/choose_reservation_type.gif" width="256" height="10" vspace="10"></td>
			</tr>
			<tr valign="top">
				<td><input type="radio" name="reservationType" <%=(rsv == null || !EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %> value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>" class="radio">&nbsp;</td>
				<td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><span class="text12"><b>Reserve for this week only.</b></span></td>
			</tr>
			<tr>
				<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
			</tr>
			<tr valign="top">
				<td><input type="radio" name="reservationType" <%=(rsv != null && EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %> value="<%=EnumReservationType.RECURRING_RESERVATION.getName()%>" class="radio">&nbsp;</td>
				<td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><span class="text12"><b>Reserve this day and time for me every week</b></span><br>
					Select this option to make this a standing weekly reservation. Please note that reservations not used will be released. At any time you can return to this page to update your reservation.
				</td>
			</tr>
				<input type="hidden" name="addressId" value="<%=addressId%>">
				<input type="hidden" name="actionName" value="">
			<tr>
				<td colspan="7" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
				<%if((rsv == null || rsv.isAnonymous()) && !hasWeeklyReservation){%>
					<input type="image" src="/media_stat/images/buttons/reserve_delivery.gif" onclick="reserveTimeslot.actionName.value='reserveTimeslot'">
				<%} else {%>
					<input type="image" src="/media_stat/images/buttons/reserve_delivery_cancel.gif" onclick="reserveTimeslot.actionName.value='cancelReservation'">&nbsp;&nbsp;&nbsp;<input type="image" src="/media_stat/images/buttons/reserve_delivery_save_changes.gif" onclick="reserveTimeslot.actionName.value='changeReservation'">
				<%}%>
				</td>
			</tr>
			</form>
		</table>
		
		</fd:ReserveTimeslotController>
	</tmpl:put>
</tmpl:insert>
