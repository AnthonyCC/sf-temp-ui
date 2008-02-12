<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.customer.ErpDepotAddressModel'%>
<%@ page import='com.freshdirect.delivery.depot.DlvLocationModel' %>
<%@ page import='com.freshdirect.delivery.depot.DlvDepotModel' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.delivery.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>

<%@ page import='com.freshdirect.fdstore.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
//boolean zoneCtActive = false;
if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}
%>

<fd:CheckLoginStatus />

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
boolean isStaticSlot = true;
boolean hasPreReserved = false;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

if (user.isChefsTable()) timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;

//will be null.
FDReservation rsv = user.getReservation();

request.setAttribute("sitePage", "www.freshdirect.com/your_account");
request.setAttribute("listPos", "CategoryNote");

Collection shipToAddresses = new ArrayList();
ErpAddressModel address = null;
String zone = null;
String addressId = request.getParameter("addressId");

Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);

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
<%}
SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="693" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="3" class="title16"><img src="/media_stat/images/layout/clear.gif" width="1" height="18">
<% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE="JavaScript">
                <!--
                OAS_AD('CategoryNote');
                //-->
      	</SCRIPT>
<% } %>

<%
//FDDeliveryManager deliveryManager = FDDeliveryManager.getInstance();
//DlvZoneInfoModel zoneInfoModel = deliveryManager.getZoneInfo(address, new Date());
//System.err.println(zoneInfoModel.getZoneCode());
//boolean zoneCtActive = deliveryManager.findZoneById(zoneInfoModel.getZoneCode()).isCtActive();
//boolean zoneCtActive = false;
%>
<%--@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" --%>

<br>Available Delivery Time Slots<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td colspan="3" class="text12">Here are the currently available time slots for delivery to this address:</td></tr>
<tr><td colspan="2">&nbsp;</td>
<% 
if (shipToAddresses.size() > 1 ) { %>
<FORM name="addressForm" method="POST" action="/your_account/delivery_info_avail_slots_b.jsp">
<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
<%
    if(user.isHomeUser()){
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
    }
%>
<SELECT name="addressId" onChange="javascript:submit()" CLASS="text12">
<% 
for(Iterator saItr=shipToAddresses.iterator();saItr.hasNext();) {
    ErpAddressModel addr = (ErpAddressModel)saItr.next();
    String id = "";
    if(addr instanceof ErpDepotAddressModel){
        id = ((ErpDepotAddressModel)addr).getLocationId();
    } else {
        id = addr.getPK().getId();
    }
    
    
%>
<OPTION value="<%= id %>" <%= id.equals(addressId) ? "SELECTED" : "" %>><%=addr.getAddress1()%>, <%=addr.getCity()%>, <%=addr.getZipCode()	%></OPTION>
<%	} %>
</SELECT></td></FORM>
<% } else {
if(user.isHomeUser())
    address = (ErpAddressModel)shipToAddresses.iterator().next();%>
<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b><%=address.getAddress1()%><br><%=address.getCity()%>, <%=address.getState()%> <%=address.getZipCode()%></b></td>
<% } %>
</tr>

<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" deliveryInfo="<%=true%>">
	<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	%>
	
<tr><td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="15" height="15"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="663" height="15"></td></tr>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
</table>
<% String preReserveSlotId = ""; %>
<%String timeSlotId = ""; %>
	<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
	
	<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
		<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
	</logic:iterate>
	<br>
	<table cellpadding="0" cellspacing="0" width="693"/>
		<tr>
			<td align="left">
				<img src="/media_stat/images/template/help/greendot_trans.gif" width="10" height="10" border="0" valign="bottom" alt="Green">
				Time Slot Available*
				&nbsp;
				<img src="/media_stat/images/template/help/orangedot_trans.gif" width="10" height="10" border="0" valign="bottom" alt="Orange">
				Time Slot Full
			</td>
			<td align="right" width="450">You must complete checkout for next-day deliveries before the "Order by" time.</td>
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


<table width="693" cellpadding="0" cellspacing="0" border="0"><tr><td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
<% if(user.isEligibleForPreReservation() && (!"true".equals(request.getParameter("chefstable"))) &&(!"true".equals(request.getParameter("chefstable")))) { %>
	<div align="center"><br>
	<a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/youraccount/reserve_delivery_time.gif" width="200" height="15" border="0" alt="Reserve a Delivery Time" vspace="4"></a><br>
	<span class="text12">Reserve a delivery timeslot before you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><a href="/your_account/reserve_timeslot.jsp"><b>Click here</b></a></span><br><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0" vspace="6"></div>
<% } %>
</td></tr></table>
</tmpl:put>
</tmpl:insert>
