<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotModel" %>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumReservationType" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDTimeslot" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>

<%@ page import="com.freshdirect.webapp.util.TimeslotPageUtil" %>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<% //expanded page dimensions
final int W_RESERVE_TIMESLOTS_TOTAL = 970;
%>

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
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="reserve_timeslot"></fd:SEOMetaTag>
	</tmpl:put>


	
    <tmpl:put name='content' direct='true'>
	<fd:ReserveTimeslotController actionName="<%=actionName%>" result="result">
	
	<%	
		FDReservation rsv = user.getReservation();
		boolean hasReservation = rsv != null && addressId.equals(rsv.getAddressId());

		// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
		final boolean abstractTimeslots = false;
	%>

		<%//Finds the address & render the timeslots %>
		<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
	
<form name="reserveTimeslot" method="POST" action="/your_account/reserve_timeslot.jsp" name="reserveTimeslot">
		<input type="hidden" name="chefstable" value="<%= user.isChefsTable() %>"/>
		<input type="hidden" name="addressId" value="<%=address.getPK()!=null ? address.getPK().getId(): null %>">
		<input type="hidden" name="actionName" value="">
		
		<%//Render the timeslots %>
		<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
	
		<img src="/media_stat/images/layout/clear.gif" width="1" height="10">
		<%//Reservation stuff%>
		<table width="<%=W_RESERVE_TIMESLOTS_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="7"><img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_RESERVE_TIMESLOTS_TOTAL%>" height="1"></td>
			</tr>
			<tr>
				<td colspan="7"><!-- <img src="/media_stat/images/template/youraccount/choose_reservation_type.gif" width="256" height="10" vspace="10" alt="Please Choose a Reservation Type"></td> -->
				<span class="Container_Top_YourAccountTimeslot">Please Choose a Reservation Type</span>
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
					Select this option to make this a standing weekly reservation. Please note that reservations not used will be released for good. You will have to return to this page to reset your reservation settings.
				</td>
			</tr>
			<tr>
				<td colspan="7" align="center">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
				
					<%if((rsv == null || rsv.isAnonymous()) && !hasReservation){%>
						<input type="image" src="/media_stat/images/buttons/reserve_delivery.gif" alt="Reserve Delivery" onclick="reserveTimeslot.actionName.value='reserveTimeslot'">
					<%} else {%>
						<button class="cssbutton red nontransparent small" onclick="reserveTimeslot.actionName.value='cancelReservation'">CANCEL RESERVATION</button>
						<button class="cssbutton orange small" onclick="reserveTimeslot.actionName.value='changeReservation'">SAVE CHANGES</button>
					<%}%>
				</td>
			</tr>
		</table>
</form>
		</fd:ReserveTimeslotController>
	</tmpl:put>
</tmpl:insert>
