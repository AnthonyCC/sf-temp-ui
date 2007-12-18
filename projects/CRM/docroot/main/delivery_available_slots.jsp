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
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>

<%@ page import='com.freshdirect.fdstore.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}

boolean isStaticSlot = true;
boolean hasPreReserved = false;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

if (user.isChefsTable()) timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;

FDReservation rsv = user.getReservation();

Collection shipToAddresses = new ArrayList();
ErpAddressModel address = null;
String zone = null;
String addressId = request.getParameter("addressId");

Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);

if (user != null) {
if(user.isHomeUser()){
    shipToAddresses = FDCustomerManager.getShipToAddresses(user.getIdentity());
	for (Iterator i=shipToAddresses.iterator(); i.hasNext(); ) {
		ErpAddressModel a = (ErpAddressModel)i.next();
		if ( a.getPK().getId().equals(addressId) || addressId == null ) {
			address = a;
			addressId = address.getPK().getId();
			break;
		} 
	}
} else {
    DlvLocationModel location = null;
%>
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
}
SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
boolean isCheckAddress = "1address".equalsIgnoreCase(request.getParameter("show"));
%>
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery Time Slots</tmpl:put>
		<tmpl:put name='content' direct='true'>
<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" deliveryInfo="<%=true%>">
	<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	%>
<div class="sub_nav">
<span class="sub_nav_title">Available Delivery Time Slots</span> | <a href="/main/delivery_check_slots.jsp">Check available Slots for a new address</a>
</div>
<div class="content_fixed">
<table width="90%" cellpadding="0" cellspacing="0" border="0" align="center">
<tr><td colspan="3" class="text12">Here are the currently available time slots for delivery to this <%=isCheckAddress ? "address" : "customer's addresses"%>:</td></tr>
<tr><td colspan="2">&nbsp;</td>
<% 
if (shipToAddresses.size() > 1 && !isCheckAddress) { %>
<FORM name="addressForm" method="GET" action="/main/delivery_available_slots.jsp">
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
</SELECT></td>
<% } else {
if(user.isHomeUser())
    address = (ErpAddressModel)shipToAddresses.iterator().next();
	
	%>
<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b><%=address.getAddress1()%><br><%=address.getZipCode()%></b><% if (isCheckAddress) {%><br><br><a href="/main/delivery_available_slots.jsp" class="order_detail">View all addresses for this customer</a><% } %></td>
<% } %>
</tr>
<tr>
<td colspan="3">
<br>
<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
</td>
</tr>
<%  
    if(user.isHomeUser())
        zone = FDDeliveryManager.getInstance().getZoneInfo(address, tomorrow.getTime()).getZoneCode();
        
    if ("005".equals(zone) || "008".equals(zone)) { %>
<tr><td colspan="3"><table width="693" cellpadding="0" cellspacing="0" border="0">
<tr>
    <td valign="top" align="center" class="text11" width="97"></td>
    <td valign="top" align="center" colspan="5" class="text11">
    <b><font class="text12" color="#CC0000">DELIVERY TIME SLOT NOTE: </font></b><br>
    Due to heightened security around the Midtown Tunnel and the 59th Street Bridge
    we have limited the number of deliveries in your area during the 4-6 PM and 6-8 PM time slots.
    This will allow us to deliver orders in a more timely manner.
    We hope this situation improves soon and will add more delivery availability as soon as possible.
    </td>
    <td valign="top" align="center" class="text11" width="97"></td>
</tr>
<TR>
	<td align="center" valign="top" colspan="7"><br></TD>
</TR>
</table></td></tr>
<%  }   %>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="693" height="15"></td></tr>
<% String preReserveSlotId = ""; %>
<tr><td colspan="3"><%String timeSlotId = ""; %>
	<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
		<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
	</logic:iterate>
	<table cellpadding="0" cellspacing="0" width="675" style="font-size: 11px">
		<tr>
			<td align="left">
				<img src="/media_stat/images/template/help/greendot_trans.gif" width="10" height="10" border="0" valign="bottom">
				= Time Slot Available *
				&nbsp;&nbsp;
				<img src="/media_stat/images/template/help/orangedot_trans.gif" width="10" height="10" border="0" valign="bottom">
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
</FORM>
</fd:DeliveryTimeSlot><br>
</td></tr>
</table>
</div>
</tmpl:put>
</tmpl:insert>
