<%@ page import='java.text.*, java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.delivery.restriction.DlvRestrictionsList"%>
<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import='com.freshdirect.fdstore.util.TimeslotLogic'%>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import='com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine' %>
<%@ page import='com.freshdirect.delivery.DlvZoneInfoModel' %>
<%@ page import='com.freshdirect.fdstore.FDDeliveryManager' %>
<%@ page import='com.freshdirect.delivery.EnumReservationType' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.FDTimeslotUtil" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>

<%
    String successPage = "checkout_select_payment.jsp";
    successPage = "checkout_ATP_check.jsp?successPage="+URLEncoder.encode(successPage);
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

    ErpAddressModel address = user!=null ? user.getShoppingCart().getDeliveryAddress():null;
    boolean isStaticSlot = false;
    boolean showAdvanceOrderBand = false;
    boolean forceOrders=false;
    if("true".equals(request.getParameter("force_order"))) {
	    forceOrders = true;
    }

    DateRange advOrdRange = FDStoreProperties.getAdvanceOrderRange();
    Calendar tomorrow = Calendar.getInstance();
    tomorrow.add(Calendar.DATE, 1);
    tomorrow = DateUtil.truncate(tomorrow);
    DateRange validRange = new DateRange(tomorrow.getTime(),DateUtil.addDays(tomorrow.getTime(),FDStoreProperties.getHolidayLookaheadDays()));
    boolean advOrdRangeOK = advOrdRange.overlaps(validRange); 
	boolean isAdvOrderGap = FDStoreProperties.IsAdvanceOrderGap();
	String timeSlotId = request.getParameter("deliveryTimeslotId");
   
%>

<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" timeSlotId="<%=timeSlotId%>">

<%	
	FDDeliveryTimeslotModel deliveryModel = user.getDeliveryTimeslotModel();
	FDCartModel cart = deliveryModel.getShoppingCart();
	
	List<FDTimeslotUtil> timeslotList = deliveryModel.getTimeslotList();
	Map zones = deliveryModel.getZones();
	boolean zoneCtActive = deliveryModel.isZoneCtActive();
	List messages = deliveryModel.getGeoRestrictionmessages();
	
	String selectedSlotId = deliveryModel.getTimeSlotId();
	String preReserveSlotId = deliveryModel.getPreReserveSlotId();
	boolean hasPreReserved = deliveryModel.isPreReserved();
	boolean defaultColExp = false;
	String zoneId = deliveryModel.getZoneId();

	
	boolean hasCapacity = deliveryModel.hasCapacity();	
	//get zone promotion amount
    double zonePromoAmount = deliveryModel.getZonePromoAmount();
    String zonePromoString=null;
	if(zonePromoAmount > 0)
	{
        zonePromoString = CCFormatter.formatQuantity(zonePromoAmount);
		request.setAttribute("SHOW_WINDOWS_STEERING","true");
	}
%>
<script>
	var zonePromoString=""; 
	var zonePromoEnabled=false;
	<%if(zonePromoAmount>0){ %>
		zonePromoString="<%=zonePromoString %>"; 
		zonePromoEnabled=true;
	<%} %>
</script>


<crm:GetCurrentAgent id="currentAgent">
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
<FORM name="select_delivery_slot" method="POST" action="">
	<TR>
	<TD WIDTH="75%">
        &nbsp;Step 2 of 4: Select Delivery Time
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is
            <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a>
        <% } %>
    </TD>

    <% if (currentAgent != null && (currentAgent.isSupervisor() || currentAgent.isAdmin())) { %>
		<td align="right"><a href="/checkout/checkout_delivery_time.jsp?forceorder=true" class="checkout">FORCE ORDER >></a></td>
	<% } %>
	<td align="right"><a href="javascript:select_delivery_slot.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
	</TR>
</TABLE>

<%@ include file="/includes/i_modifyorder.jspf" %>


<div class="content_scroll" style="height: 72%;">
<TABLE width="100%" cellpadding="2" cellspacing="0" border="0" class="order">
	<TR>
		<TD  align="center" class="order_detail">	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ START TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>


<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<%if(DeliveryTimeSlotResult != null && !DeliveryTimeSlotResult.isSuccess() ){%>
		<fd:ErrorHandler result='<%=DeliveryTimeSlotResult%>' name='deliveryTime' id='errorMsg'>
				<%@ include file="/includes/i_timeslot_error_messages.jspf" %>
		</fd:ErrorHandler>
<%}%>

<!-- LOYALTY -->
	<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
<!-- LOYALTY -->

<!--START MESSAGING SECTION-->

<!-- GEO Restriction Message Added -->
<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_geowarning_message.jspf"%>
<% } %>

<!-- Alcohol, SpecialItems, holiday, deliveryPass, noCapacity warning Messages -->

<%if(cart.containsAlcohol()){%>

	<%@ include file="/includes/delivery/i_alcohol_warning.jspf"%>	
		
<%}%>

<%if(user.getSelectedServiceType() == EnumServiceType.CORPORATE) { %>
	
	<%@ include file="/includes/delivery/i_delivery_pass_not_applied.jspf"%>	
	
<%}%>

<%if(!hasCapacity){%>

	<%@ include file="/includes/delivery/i_slots_no_capacity.jspf"%>
	
<%}%>

<%@ include file="/includes/delivery/i_holiday_warning.jspf"%>

<%if(deliveryModel.isKosherRestriction()){%>
	
	<%@ include file="/includes/delivery/i_kosher_restriction_warning.jspf"%>
		
<%}%>

<%if(cart.hasAdvanceOrderItem() && advOrdRangeOK && deliveryModel.isThxgivingRestriction()){%>
	
	<%@ include file="/includes/delivery/i_specialitem_thanksgiving_delivery.jspf"%>
		
<%}%>


<%if(cart.hasAdvanceOrderItem() && advOrdRangeOK && deliveryModel.isEasterMealRestriction()){%>
	
	<%@ include file="/includes/delivery/i_specialitem_easter_delivery.jspf"%>
		
<%}%>
	
<%if(deliveryModel.isEasterMealRestriction() && !advOrdRangeOK){ %>
	
	<%@ include file="/includes/delivery/i_eastermeal.jspf"%>
		
<%}%>

<%if(deliveryModel.isThxgiving_meal_Restriction()){%>

	<%@ include file="/includes/delivery/i_thanksgiving_meal.jspf"%>
		
<%}%>
<%if(deliveryModel.isValentineRestriction()){%>
	
	<%@ include file="/includes/delivery/i_valentine.jspf"%>
	
<%}%>
<!--END MESSAGING SECTION-->



<!-- ~~~~~~~~~~~~~~~~~~~~~~ START TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
<table>
<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.util.FDTimeslotUtil" indexId="idx">
	<tr>
		<td>
<%
	if(timeslotList.size()>1 && idx.intValue()==0){
%>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
	<span class="text12"><b>SPECIAL DELIVERY</b></span><BR>	
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
<%}%>


<%	
	Boolean isForAdvOrdSlots = false;
	if(timeslotList.size()>1 && idx.intValue()==0){
		isForAdvOrdSlots = true;
	}

	// If there are 2 advance order timeslots then show standard delivery header accordingly
	if((timeslotList.size()>2 && isAdvOrderGap && idx.intValue()==2) ||
		(timeslotList.size()==2 && isAdvOrderGap && idx.intValue()==1) ||
		(!isAdvOrderGap && idx.intValue()==1)){
	//if(idx.intValue() == 1){
	
	showAdvanceOrderBand=false;
%>
	<span class="text12"><b>REGULAR DELIVERY</b></span><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
			
<%} else { 
    showAdvanceOrderBand = timeslotList.size()>1 ? true && advOrdRangeOK: false;
 } %>

	<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>

<!-- ~~~~~~~~~~~~~~~~~~~~~~ END TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
	</td>
</TR>
</logic:iterate>
</table>

<table cellpadding="0" cellspacing="0" width="675">
	<tr>
		<td align="right">
				<table>
						<tr>
								<%if(user.isChefsTable()){%>
									<td>
										<img src="/media/editorial/timeslots/images/star_ct_delivery_time.gif" WIDTH="15" HEIGHT="18" border="0" alt="PREVIOUS STEP">
									</td>
									<td style="color:#77A642;">Chefs Table Delivery Times</td>
								<%}%>
								<td>
									<img src="/media/editorial/timeslots/images/dollar_discount_delivery_time.gif" WIDTH="15" HEIGHT="18" border="0" alt="PREVIOUS STEP">
								</td>
								<td style="color:#77A642;">Discount Delivery Times</td>
						</tr>
				</table>	
		</td>
		
	</tr>
</table>
<BR><BR>
	</TD>
</TR>
</TABLE>

<TABLE>
<TR VALIGN="TOP">
    <TD WIDTH="70%" style="padding-left: 10px;">
		<b>Delivery Charge:</b> 
		<%
		String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge() );
		if(cart.isDlvPassApplied()) {
		%>
		<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
		<%
		} else if (cart.isDeliveryChargeWaived()) {
		%>
			Free! We've waived the standard <%= dlvCharge %> delivery charge for this order.
		<%}else {%>
			<%= dlvCharge %>
		<%}%><br>(Our delivery personnel are allowed to accept tips if exceptional service is provided).
		</TD>
</TR>
<%
	if(user.getSelectedServiceType() == EnumServiceType.CORPORATE && (user.isDlvPassActive() || cart.getDeliveryPassCount() > 0)) {
%>
	<TR>
		<td style="padding-left: 10px;"><font class="text12bold" color="#FF0000"><b>Important note: </b></font>
		<font class="text12bold"><b>Delivery pass can only be used for Home delivery orders.</b></font></td>
	</TR>
<%}%>

	</FORM>
</TABLE>
<BR><BR>
</div>
<% 
if(!defaultColExp){%>
	<script>
		document.observe("dom:loaded", function() {
			selectTS('tsContainer', 'div_set_0','ts_set_0_ts_0');
		});
	</script>
<%}%>
</tmpl:put>
</fd:CheckoutController>
</tmpl:insert>
</crm:GetCurrentAgent>
</fd:DeliveryTimeSlot>



