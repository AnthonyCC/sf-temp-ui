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
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import='com.freshdirect.delivery.DlvZoneInfoModel' %>
<%@ page import='com.freshdirect.fdstore.FDDeliveryManager' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>

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
String timeSlotId="";
ActionResult result=null;
FDReservation rsv = null;
String actionName = null;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());

if (user.isChefsTable()) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}

String zone = null;
String addressId = request.getParameter("addressId");

Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);



SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
boolean isCheckAddress = "1address".equalsIgnoreCase(request.getParameter("show"));
TimeslotContext timeSlotCtx = null;
if(isCheckAddress){
	 timeSlotCtx = TimeslotContext.CHECK_SLOTS_FOR_ADDRESS_CRM;
}else{
	 timeSlotCtx = TimeslotContext.CHECK_AVAL_SLOTS_CRM;
}
%>


<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery TimeSlots</tmpl:put>
		<tmpl:put name='content' direct='true'>

<style>
span.control img{
	margin: 7px 8px 0 4px;
}

span.time {
	float:left;
	position:relative;
	top:1px;
}
</style>

	<div class="sub_nav">
		<span class="sub_nav_title">Available Delivery TimeSlots</span> | <a href="/main/delivery_check_slots.jsp">Check available Slots for a new address</a>
	</div>
	<div class="content_fixed">
	<table width="90%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td colspan="2" class="text12" align="left" width="29%"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			<td class="text12" align="left" width="71%"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
		</tr>
		<tr>
			<td colspan="3" class="text12" align="left">Here are the currently available timeslots for delivery to this <%=isCheckAddress ? "address" : "customer's addresses"%>:
					<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR><BR>
			</td>
		</tr>	
		<tr>
			<td colspan="3">
				<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
			</td>
		</tr>

		<%//Finds the address%>
		<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
		

<%  
   if(user.isHomeUser())
       zone = FDDeliveryManager.getInstance().getZoneInfo(address, tomorrow.getTime()).getZoneCode();
       
   if ("005".equals(zone) || "008".equals(zone)) { %>

<tr>
	<td colspan="3">
		<table width="693" cellpadding="0" cellspacing="1" border="0">
				<tr>
					<td valign="top" align="center" class="text11" width="97"></td>
					<td valign="top" align="center" colspan="5" class="text11">
						<b><font class="text12" color="#CC0000">DELIVERY TIMESLOT NOTE: </font></b><br>
						Due to heightened security around the Midtown Tunnel and the 59th Street Bridge
						we have limited the number of deliveries in your area during the 4-6 PM and 6-8 PM time slots.
						This will allow us to deliver orders in a more timely manner.
						We hope this situation improves soon and will add more delivery availability as soon as possible.
					</td>
					<td valign="top" align="center" class="text11" width="97"></td>
				</tr>
				<TR>
					<td align="center" valign="top" colspan="7"><br>
					</TD>
				</TR>
		</table>
	</td>
</tr>
<%  }   %>
	
<tr>
	<td colspan="3">
		<img src="/media_stat/images/layout/clear.gif" width="693" height="15">
	</td>
</tr>

</table>
</div>
</tmpl:put>
</tmpl:insert>
