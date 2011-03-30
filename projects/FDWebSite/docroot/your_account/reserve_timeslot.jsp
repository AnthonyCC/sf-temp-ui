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
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
String addressId = NVL.apply(request.getParameter("addressId"), "");
boolean isStaticSlot = false;
boolean isCheckAddress =false;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

// redirect, if not eligible
if(!user.isEligibleForPreReservation()) {
	response.sendRedirect("/your_account/");
	return;
}

TimeslotContext timeSlotCtx=TimeslotContext.RESERVE_TIMESLOTS; 
String actionName = request.getParameter("actionName");

%>

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Reserve Timeslot</tmpl:put>
    <tmpl:put name='content' direct='true'>
	<fd:ReserveTimeslotController actionName="<%=actionName%>" result="result">
	
	

<form name="reserveTimeslot" method="POST" action="/your_account/reserve_timeslot.jsp" name="reserveTimeslot">
		<input type="hidden" name="chefstable" value="<%= user.isChefsTable() %>"/>

	<%	
		FDReservation rsv = user.getReservation();
		boolean hasReservation = rsv != null && addressId.equals(rsv.getAddressId());
	%>
	
	<%//Finds the address & render the timeslots %>
	<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
	
	
		<img src="/media_stat/images/layout/clear.gif" width="1" height="10">
		<%//Reservation stuff%>
		<table width="693" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="7"><img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1"></td>
			</tr>
			<tr>
				<td colspan="7"><img src="/media_stat/images/template/youraccount/choose_reservation_type.gif" width="256" height="10" vspace="10"></td>
			</tr>
			<tr valign="top">
				<td>
					<input type="radio" name="reservationType" <%=(rsv == null || EnumReservationType.ONETIME_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %> value="<%=EnumReservationType.ONETIME_RESERVATION.getName()%>" class="radio">&nbsp;
				</td>
				<td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
					<span class="text12"><b>Reserve for this week only.</b>
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
			</tr>
			<tr valign="top">
				<td>
					<input type="radio" name="reservationType" <%=(rsv != null && EnumReservationType.RECURRING_RESERVATION.equals(rsv.getReservationType())) ? "checked" : "" %> value="<%=EnumReservationType.RECURRING_RESERVATION.getName()%>" class="radio">&nbsp;
				</td>
				<td colspan="6">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
					<span class="text12"><b>Reserve this day and time for me every week</b></span><br>
					Select this option to make this a standing weekly reservation. Please note that reservations not used will be released. At any time you can return to this page to update your reservation.
				</td>
			</tr>
				<input type="hidden" name="addressId" value="<%=address.getPK()!=null ? address.getPK().getId(): null %>">
				<input type="hidden" name="actionName" value="">
			
			<tr>
				<td colspan="7" align="center">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
				
					<%if((rsv == null || rsv.isAnonymous()) && !hasReservation){%>
						<input type="image" src="/media_stat/images/buttons/reserve_delivery.gif" onclick="reserveTimeslot.actionName.value='reserveTimeslot'">
					<%} else {%>
						<input type="image" src="/media_stat/images/buttons/reserve_delivery_cancel.gif" onclick="reserveTimeslot.actionName.value='cancelReservation'">&nbsp;&nbsp;&nbsp;
						<input type="image" src="/media_stat/images/buttons/reserve_delivery_save_changes.gif" onclick="reserveTimeslot.actionName.value='changeReservation'">
					<%}%>
				</td>
			</tr>
			
		</table>
</form>
		</fd:ReserveTimeslotController>
	</tmpl:put>
</tmpl:insert>
