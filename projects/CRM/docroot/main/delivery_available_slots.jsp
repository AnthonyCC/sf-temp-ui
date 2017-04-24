<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.customer.ErpDepotAddressModel'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryDepotModel' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.delivery.*'%>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory"%>
<%@ page import="com.freshdirect.logistics.delivery.model.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>

<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdlogistics.model.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<%
boolean isStaticSlot = true;
String timeSlotId="";
ActionResult result=null;
FDReservation rsv = null;
String actionName = null;

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());

String addressId = request.getParameter("addressId");



SimpleDateFormat deliveryDayFormat = new SimpleDateFormat("EEE MM/d");
boolean isCheckAddress = "1address".equalsIgnoreCase(request.getParameter("show"));
TimeslotContext timeSlotCtx = null;
if(isCheckAddress){
	 timeSlotCtx = TimeslotContext.CHECK_SLOTS_FOR_ADDRESS_CRM;
}else{
	 timeSlotCtx = TimeslotContext.CHECK_AVAL_SLOTS_CRM;
}

// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
final boolean abstractTimeslots = false;
%>


<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Available Delivery TimeSlots</tmpl:put>
		<tmpl:put name='content' direct='true'>

	<link rel="stylesheet" type="text/css" href="/assets/css/timeslots.css"/>
	<script type="text/javascript" language="javascript" src="/assets/javascript/timeslots.js"></script>

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
	<div class="content_fixed" align="left" height="100%">
		<table width="90%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td colspan="3" class="text12" align="left" width="100%"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr>
				<td colspan="3" class="text12" align="left" style="padding-bottom: 5px;">Here are the currently available timeslots for delivery to this <%=isCheckAddress ? "address" : "customer's addresses"%>:
						<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0">
				</td>
			</tr>
		</table>
		
		<%@ include file="/shared/includes/delivery/i_address_finder.jspf"%>
		<%@ include file="/shared/includes/delivery/i_delivery_timeslots.jspf"%>
		
	</div>
</tmpl:put>
</tmpl:insert>
