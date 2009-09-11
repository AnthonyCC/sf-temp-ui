<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>

<%@ page import='com.freshdirect.fdstore.*' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" recognizedAllowed="true" />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

if (user.isChefsTable()) timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;

//will be null.
FDReservation rsv = user.getReservation();

boolean isStaticSlot = true;
boolean hasPreReserved = false;

ErpAddressModel address = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getDeliveryAddress(); 

SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");

request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "SystemMessage,CategoryNote,TimeslotBottom");
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="693" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="3" class="title16"><img src="/media_stat/images/layout/clear.gif" width="1" height="18">
<% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('CategoryNote');
                //-->
      	</SCRIPT>
<% } %><br>Available Delivery Time Slots<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td colspan="3" class="text12">Here are the currently available time slots for delivery to this address:</td></tr>
<tr><td colspan="2">&nbsp;</td><td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b><%=address.getAddress1()%><br><%=address.getZipCode()%></b><br><a href="/your_account/delivery_info_check_slots.jsp" class="text11">change address</a></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="663" height="15"></td></tr>
</table>
<% String preReserveSlotId = ""; %>
<%String timeSlotId = ""; %>
<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" deliveryInfo="<%=true%>">
	<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	List messages = DeliveryTimeSlotResult.getMessages();
	%>
	<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
	
	<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
		
		<% // fix for advance orders showing on this page
		if (idx.intValue() == timeslotList.size()-1 && timeslotList.size() > idx.intValue()) { %>
			<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
		<% } %>
	</logic:iterate>
	<!-- Bryan Restriction Message Added -->
	<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_restriction_message.jspf"%>
	<% } %>
	<table cellpadding="0" cellspacing="0" width="675">
		<tr>
			<td align="left">
				<img src="/media_stat/images/template/help/greendot_trans.gif" width="10" height="10" border="0" valign="bottom" alt="Green">
				= Time Slot Available *
				&nbsp;&nbsp;
				<img src="/media_stat/images/template/help/orangedot_trans.gif" width="10" height="10" border="0" valign="bottom" alt="Orange">
				= Time Slot Full
			</td>
			<td align="right">You must complete checkout for next-day deliveries before the "Order by" time.</td>
		</tr>
		<tr>
			<td colspan="2">
			<br>
			* <b>You will select a delivery time slot at Checkout.</b> Delivery time slots are not guaranteed until completion of Checkout.
			</td>
		</tr>
	</table>
	<br>
	<% if (timeslot_page_type != TimeslotLogic.PAGE_CHEFSTABLE) { %>
	<%@ include file="/shared/includes/delivery/i_loyalty_button.jspf" %>
	<% } %>
</fd:DeliveryTimeSlot>

<table width="693" cellpadding="0" cellspacing="0" border="0"><tr><td colspan="7""><img src="/media_stat/images/layout/clear.gif" width="1" height="14">
<% if(user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable"))) && user.isDlvPassActive()) { %>
	<div align="center"><br>
	<a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/youraccount/reserve_delivery_time.gif" width="200" height="15" border="0" alt="Reserve a Delivery Time" vspace="4"></a><br>
	<span class="text12">Reserve a delivery timeslot before you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><a href="/your_account/reserve_timeslot.jsp"><b>Click here</b></a></span><br><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0" vspace="6"></div>
<% }else if(!user.isDlvPassActive() && !(user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable"))))) {%>
    <div align="center"><br>
	 <% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('TimeslotBottom');
                //-->
      	</SCRIPT><br><br>
	 <% } %>
     </div>
<% }else if(!user.isDlvPassActive() && user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable")))) {%>
	<div align="center">
    <table>
    <tr><td align="center">
     	<a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/youraccount/reserve_delivery_time.gif" width="200" height="15" border="0" alt="Reserve a Delivery Time" vspace="4"></a><br>
	    <span class="text12">Reserve a delivery timeslot before you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><a href="/your_account/reserve_timeslot.jsp"><b>Click here</b></a></span><br>
    </td>
    <td align="center">
    <img src="/media_stat/images/layout/clear.gif" width="1" height="34"><br>
    	 <% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE=JavaScript>
                <!--
                OAS_AD('TimeslotBottom');
                //-->
      	</SCRIPT><br><br>
	     <% } %>
    </td></tr>
    </table>
     </div>
<%}%>
</td></tr></table>

</tmpl:put>
</tmpl:insert>
