<%@ page import='java.text.*, java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.restriction.DlvRestrictionsList"%>
<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel" %>
<%@ page import="com.freshdirect.fdstore.FDTimeslotList" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %> 

<%
    String successPage = "checkout_select_payment.jsp";
    successPage = "checkout_ATP_check.jsp?successPage="+URLEncoder.encode(successPage);
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    FDCartModel cart = user.getShoppingCart();
    FDReservation rsv = user.getReservation();
    ErpAddressModel address = cart.getDeliveryAddress(); 
    boolean isStaticSlot = false;
    boolean showAdvanceOrderBand = false;

    DateRange advOrdRange = FDStoreProperties.getAdvanceOrderRange();
    Calendar tomorrow = Calendar.getInstance();
    tomorrow.add(Calendar.DATE, 1);
    tomorrow = DateUtil.truncate(tomorrow);
    DateRange validRange = new DateRange(tomorrow.getTime(),DateUtil.addDays(tomorrow.getTime(),FDStoreProperties.getHolidayLookaheadDays()));
    boolean advOrdRangeOK = advOrdRange.overlaps(validRange);

    
    String preReserveSlotId = "";
    boolean hasPreReserved = false;
    if(rsv != null){
        preReserveSlotId = rsv.getTimeslotId();
        hasPreReserved = address.getPK()!=null && address.getPK().getId().equals(rsv.getAddressId());
    }
    String timeSlotId = request.getParameter("deliveryTimeslotId");
    if (timeSlotId == null) {
        rsv  = cart.getDeliveryReservation();
        if(rsv != null){
            timeSlotId = rsv.getTimeslotId();
        }else{
            timeSlotId = "";
        }
    }
    
    boolean thxgivingRestriction = false;
    boolean kosherRestriction = false;
    boolean alcoholRestriction = false;
    boolean thxgiving_meal_Restriction=false;
    
    for(Iterator i = cart.getApplicableRestrictions().iterator(); i.hasNext(); ){
        EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason) i.next();
        if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason)){
            thxgivingRestriction = true;
            continue;
        }
        if(EnumDlvRestrictionReason.THANKSGIVING_MEALS.equals(reason)){
           thxgiving_meal_Restriction=true;
           continue;
        }
        if(EnumDlvRestrictionReason.ALCOHOL.equals(reason)){
            alcoholRestriction = true;
            continue;
        }
        if(EnumDlvRestrictionReason.KOSHER.equals(reason)){
            kosherRestriction = true;
            continue;
        }
    }
    int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
    if("true".equals(request.getParameter("chefstable")) || user.isChefsTable()) {
    	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
    }
%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Select Delivery Time</tmpl:put>
<fd:CheckoutController actionName="reserveDeliveryTimeSlot" result="result" successPage="<%= successPage %>">

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<% String[] checkErrorType = {"deliveryTime", "technical_difficulty", "pickup_didnot_agree"}; %>
<fd:ErrorHandler result="<%=result%>" field="<%=checkErrorType%>" id="errorMsg">
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
<form name="select_delivery_slot" method="POST" action="">
	<TR>
	<TD WIDTH="80%">
        &nbsp;Step 2 of 4: Select Delivery Time
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is 
            <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a>
        <% } %>
    </TD>
	<td align="right"><a href="javascript:select_delivery_slot.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
	</TR>
</TABLE>

<%@ include file="/includes/i_modifyorder.jspf" %>


<div class="content_scroll" style="height: 72%;">
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="order">
	<tr><td colspan="2">
		<%
		if(alcoholRestriction){%>
			<b>Beer note for Sunday Delivery.</b><br>
			New York state law prohibits the sale of Beer from 3 A.M. until Noon on Sunday. If you select Sunday<br>
			delivery slot that does not begin after 12 Noon, all Beer items will be removed form your cart.
		<%}%>
	</td></tr>
	<tr><td colspan="2" align="center" class="order_detail">
<%-- ~~~~~~~~~~~~~~~~~~~~~~ START TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

<fd:DeliveryTimeSlot id='DeliveryTimeSlotResult' address='<%=address%>'>
<%
List timeslotList = DeliveryTimeSlotResult.getTimeslots();
Map zones = DeliveryTimeSlotResult.getZones();
boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
%>

<%
	DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
	boolean isKosherSlotAvailable = false;
	boolean hasCapacity = false;
	for(Iterator i = timeslotList.iterator(); i.hasNext(); ){
		FDTimeslotList lst = (FDTimeslotList)i.next();
		isKosherSlotAvailable = isKosherSlotAvailable || lst.isKosherSlotAvailable(restrictions);
		hasCapacity = hasCapacity || lst.hasCapacity();
	}
%>
<%if(kosherRestriction){%>
<table>
	<tr valign="top">
		<td colspan="2" class="text12">
		<table width="100%"><tr><td><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0"></td>
		<td class="text12"><b><span class="kosher">Kosher Delivery Note:</span></b> 
		The custom-cut kosher items in your cart are not available for delivery on Friday, Saturday, or Sunday morning.<br>
			<fd:GetDlvRestrictions id="kosherRestrictions" reason="<%=EnumDlvRestrictionReason.KOSHER%>" withinHorizon="true">
			<% if (kosherRestrictions.size() > 0) { %>They are also unavailable during
				<logic:iterate indexId='i' collection="<%= kosherRestrictions %>" id="restriction" type="com.freshdirect.delivery.restriction.RestrictionI">
				<b><%=restriction.getName()%></b>, <%=restriction.getDisplayDate()%><% if (i.intValue() < kosherRestrictions.size() -1) {%>; <% } else { %>.<% } %>
				</logic:iterate>
			<% } %>
			</fd:GetDlvRestrictions>
            <% if (user.isDepotUser() && isKosherSlotAvailable) { %>
                <b>Unfortunately there is no time during the next week that custom-cut kosher items can be delivered. If you continue Checkout, these 
                items will be removed from your cart.</b> 
            <% } else { %>
                Available delivery days for all of your kosher items are marked in blue. 
            <% } %>
            <a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Learn More</a>
		</td>
		</tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
		</table>
		</td>
	</tr>
</table>
<%}%>

<!-- LOYALTY -->
	<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
	<!-- LOYALTY -->

<%if(cart.hasAdvanceOrderItem() && advOrdRangeOK && thxgivingRestriction){%>
	<table width="100%">
		<tr>
			<td class="text12" align="center">
	<fd:IncludeMedia name='/media/editorial/holiday/advance_order/delivtext_adv.html'/>
			</td>
		</tr>
		<tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
	</table>
<%}%>

<%if(thxgiving_meal_Restriction){%>
	<table width="100%">
		<tr>
			<td class="text12" align="center">
	                   <fd:IncludeMedia name='/media/editorial/holiday/thanksgiving/thanksgiv_chkout_msg.htm'/>
			</td>
		</tr>
		<tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
	</table>
<%}%>



<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
<%    if(idx.intValue() == 1){
		showAdvanceOrderBand=false;

	%>
		<span class="text12"><b>Standard Delivery Slots</b></span>
<%    } else { 
           showAdvanceOrderBand = timeslotList.size()>1 ? true & advOrdRangeOK : false;
      }
 %>
<%@ include file="/shared/includes/delivery/i_restriction_band.jspf"%>
<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</logic:iterate>
<table cellpadding="0" cellspacing="0" width="675">
	<tr>
		<td align="left">
			<img src="/media_stat/images/template/checkout/x_trans.gif" width="10" height="10" border="0" valign="bottom">
			= Delivery slot full
		</td>
		<td align="right">You must complete checkout for next-day deliveries before the "Order by" time.</td>
	</tr>
</table>

<% if (timeslot_page_type != TimeslotLogic.PAGE_CHEFSTABLE) { %>

<%@ include file="/shared/includes/delivery/i_loyalty_button.jspf" %>

<% } %>

</fd:DeliveryTimeSlot>
<BR>
<BR>
	</td></tr>
	
<TR VALIGN="TOP">
    <TD WIDTH="70%" style="padding-left: 10px;"><b>Delivery Charge:</b> <%	
	String dlvCharge = CCFormatter.formatCurrency( cart.getDeliverySurcharge() );
	System.out.println("isDlvPassApplied in checkout_delivery_time.jsp "+cart.isDlvPassApplied());
	if(cart.isDlvPassApplied()) {
	%>
	<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
	<%	
	} else if (cart.isDeliveryChargeWaived()) {
	%>
		Free! We've waived the standard <%= dlvCharge %> delivery charge for this order.	
	<%}else {%>
		<%= dlvCharge %>
	<%}%><br>(Our delivery personnel are allowed to accept tips if exceptional service is provided).</TD>
	<TD WIDTH="30%" align="right"><img src="/media_stat/images/template/checkout/unavailable_X.gif" width="9" height="9" border="0" alt="X">= Delivery slot full</TD>
</TR>
<%
	if(user.getSelectedServiceType() == EnumServiceType.CORPORATE && (user.isDlvPassActive() || cart.getDeliveryPassCount() > 0)) {
%>
	<TR>
	<td style="padding-left: 10px;"><font class="text12bold" color="#FF0000"><b>Important note: </b></font>
	<font class="text12bold"><b>Delivery pass can only be used for Home delivery orders.</b></font></td>	
	</TR>
<%}%>		

</form>
</TABLE>
<BR>
<BR>
</div>
</tmpl:put>
	</fd:CheckoutController>
</tmpl:insert>




