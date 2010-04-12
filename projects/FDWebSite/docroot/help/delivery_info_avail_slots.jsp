<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>

<%@ page import='com.freshdirect.fdstore.*' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;

//boolean zoneCtActive = false;
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

request.setAttribute("sitePage", "www.freshdirect.com/help");
request.setAttribute("listPos", "SystemMessage,CategoryNote");

boolean isStaticSlot = true;
boolean hasPreReserved = false;

ErpAddressModel address = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getDeliveryAddress(); 

SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="693" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="3" class="title16">
<img src="/media_stat/images/layout/clear.gif" width="1" height="18">
<% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE="JavaScript">
                <!--
                OAS_AD('CategoryNote');
                //-->
      	</SCRIPT>
<% } %><br>Available Delivery TimeSlots<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td colspan="3" class="text12">Here are the currently available timeslots for delivery to this address:</td></tr>
<tr><td colspan="2">&nbsp;</td><td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b><%=address.getAddress1()%><br><%=address.getZipCode()%></b><br><a href="/help/delivery_info_check_slots.jsp" class="text11">change address</a></td></tr>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="663" height="15"></td></tr>
<tr>
<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" deliveryInfo="<%=true%>">
	<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	List messages = DeliveryTimeSlotResult.getMessages();
	%>

<td colspan="3">
<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
</td>
</tr>
<tr><td align="right"><img src="/media_stat/images/template/help/greendot_trans.gif" width="10" height="10" alt="Green"></td>
<td colspan="2" class="text12">&nbsp;=&nbsp;TimeSlot Available *</td></tr>
<tr><td align="right"><img src="/media_stat/images/template/help/orangedot_trans.gif" width="10" height="10" alt="Orange"></td>
<td colspan="2" class="text12">&nbsp;=&nbsp;TimeSlot Full</td></tr>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
</table>
<% String preReserveSlotId = ""; %>
<%String timeSlotId = ""; %>

	
	<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
		<% // fix for advance orders showing on this page
			if ((timeslotList.size()>1 && idx.intValue() == 1) || timeslotList.size()==1) { %>
			<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
		<% } %>
	</logic:iterate>
	<!-- Bryan Restriction Message Added -->
	<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_restriction_message.jspf"%>
	<% } %>	
<table width="693" cellpadding="0" cellspacing="0" border="0"><tr><td colspan="7""><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>* <b>You will select a delivery timeslot at Checkout.</b> Delivery timeslots are not guaranteed until completion of Checkout.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">

<%
if (timeslot_page_type != TimeslotLogic.PAGE_CHEFSTABLE) {
%>
<%@ include file="/shared/includes/delivery/i_loyalty_button.jspf" %> 
<% 
}
%>
</td></tr></table>

</fd:DeliveryTimeSlot>
</tmpl:put>
</tmpl:insert>
