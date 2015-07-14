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
<%@ page import='com.freshdirect.fdlogistics.model.*' %>
<%@ page import='com.freshdirect.fdstore.FDDeliveryManager' %>
<%@ page import='com.freshdirect.logistics.delivery.model.EnumReservationType' %>

<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.FDTimeslotUtil" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ page import="com.freshdirect.fdstore.util.RestrictionUtil" %>
<%@ page import="com.freshdirect.customer.EnumChargeType %>
 <jwr:style src="/global.css"/>

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
	int page_type = TimeslotLogic.PAGE_NORMAL;
	
	final boolean forceOrder = "true".equalsIgnoreCase(request.getParameter("forceorder"));
	TimeslotContext timeSlotCtx= TimeslotContext.CHECKOUT_TIMESLOTS;
%>

<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>" timeSlotId="<%=timeSlotId%>" forceOrder="<%= forceOrder %>" timeSlotContext="<%=timeSlotCtx %>">

<%
	FDDeliveryTimeslotModel deliveryModel = DeliveryTimeSlotResult.getDeliveryTimeslotModel();
	FDCartModel cart = deliveryModel.getShoppingCart();
    
	List<FDTimeslotUtil> timeslotList = deliveryModel.getTimeslotList();
	Map zones = deliveryModel.getZones();
	boolean zoneCtActive = deliveryModel.isZoneCtActive();
	List messages = deliveryModel.getGeoRestrictionmessages();
	
	String selectedSlotId = deliveryModel.getTimeSlotId();
	String preReserveSlotId = deliveryModel.getPreReserveSlotId();
	boolean hasPreReserved = deliveryModel.isPreReserved();
	
	FDReservation rsv = (user!=null && user.getShoppingCart()!=null && user.getShoppingCart().getDeliveryReservation()!=null && 
			((selectedSlotId!=null && selectedSlotId.equals(user.getShoppingCart().getDeliveryReservation().getTimeslotId())) || (hasPreReserved && preReserveSlotId!=null && preReserveSlotId.equals(user.getShoppingCart().getDeliveryReservation().getTimeslotId()))))?user.getShoppingCart().getDeliveryReservation():null;

	if(rsv == null) rsv = (user!=null && user.getReservation()!=null && 
			((selectedSlotId!=null && selectedSlotId.equals(user.getReservation().getTimeslotId())) || (hasPreReserved && preReserveSlotId!=null && preReserveSlotId.equals(user.getReservation().getTimeslotId()))))?user.getReservation():null;

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

	// [APPDEV-2149] Display abstract timeslot table (Just days of week, no restrictions, etc.)
	final boolean abstractTimeslots = false;
	
	session.setAttribute("inCrmCheckout", true);
%>

<crm:GetCurrentAgent id="currentAgent">
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Select Delivery Time</tmpl:put>
<fd:CheckoutController actionName="reserveDeliveryTimeSlot" result="result" successPage="<%= successPage %>">

<tmpl:put name='content' direct='true'>

<link rel="stylesheet" type="text/css" href="/assets/css/timeslots.css"/>
<script type="text/javascript" language="javascript" src="/assets/javascript/timeslots.js"></script>

<script type="text/javascript">
	var zonePromoString="";
	var zonePromoEnabled=false;
	<%if(zonePromoAmount>0){ %>
		zonePromoString="<%=zonePromoString %>";
		zonePromoEnabled=true;
	<%} %>
</script>

<jsp:include page='/includes/order_header.jsp'/>

<% String[] checkErrorType = {"deliveryTime", "technical_difficulty", "pickup_didnot_agree", "bypassedDpTcBlock"}; %>
<fd:ErrorHandler result="<%=result%>" field="<%=checkErrorType%>" id="errorMsg">
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<TABLE WIDTH="730" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
<FORM name="select_delivery_slot" id="select_delivery_slot"  method="POST" action="">
	<TR>
	<TD WIDTH="75%">
        &nbsp;Step 2 of 4: Select Delivery Time
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is
            <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a>
        <% } %>
    </TD>

    <% if (CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"forceorder")) { %>
		<td align="right"><a href="/checkout/checkout_delivery_time.jsp?forceorder=true" class="checkout">FORCE ORDER >></a></td>
	<% } %>
		<script type="text/javascript" style="display:none">
					function step2Validator(formId) {
						if (!$(formId)) { return false; }
						var premiumslots = document.getElementsByName('deliveryTimeslotId');
						var premiumslotId ="";
						for(var i =0;i<=premiumslots.length;i++){
							if(premiumslots[i].checked==true){
								premiumslotId = premiumslots[i];
								break;
							}
						}
						checkPremiumSlot(premiumslotId.value, formId);
					}
		</script>
		<td align="right"><a href="#" onclick="step2Validator('select_delivery_slot'); return false;" class="checkout">CONTINUE CHECKOUT >></a></td>
	</TR>
</TABLE>

<%@ include file="/includes/i_modifyorder.jspf" %>


<div class="cust_module_content" style="height: 100%;">
<TABLE width="730" cellpadding="2" cellspacing="0" border="0" class="order" align="center">
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

<% 
String view_cart_redir = "/order/place_order_build.jsp";
if(TimeslotLogic.isTSPreReserved(rsv, deliveryModel)){%>
<%@ include file="/shared/includes/delivery/i_variable_minnotmet_messages.jspf"%> 
<%} %>

<!-- GEO Restriction Message Added -->
<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_geowarning_message.jspf"%>
	<%}%>

<!-- Alcohol, SpecialItems, holiday, deliveryPass, noCapacity warning Messages -->

<%if(cart.containsAlcohol() && deliveryModel.isAlcoholDelivery()){%>

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

<%if(FDStoreProperties.isNewFDTimeslotGridEnabled()){%>
<table cellpadding="0" cellspacing="0" width="675" id="legendCheckout">
	<tr>
		<td align="right">
					<table>
							<tr>
								<%if(deliveryModel.isMinOrderReqd()){%>
										<td>
											<img src="/media_stat/images/timeslots/diamond_icon.png" WIDTH="16" HEIGHT="16" border="0">
										</td>
										<td>
										<a onClick="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/editorial/timeslots/msg_variable_minimum.html&windowSize=small&name=Minimum Order','large');return false;"><%= FDStoreProperties.getMinOrderLabel()%></td>
										<td>&nbsp;</td>
								<%}%>
								<%if(deliveryModel.getEcoFriendlyCount() > 0){%>
									<td>
										<img src="/media_stat/images/timeslots/ecofriendly_leaf.gif" WIDTH="16" HEIGHT="16" border="0" alt="Eco-Friendly">
									</td>
									<td><div onClick="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/editorial/timeslots/msg_ecofriendly_timeslot.html&windowSize=small&name=Eco Friendly','small');"><%= FDStoreProperties.getEcoFriendlyLabel()%></div></td>
									<td>&nbsp;</td>
								<%}%>
								<%if(deliveryModel.getNeighbourhoodCount() > 0){%>
									<td>
										<img src="/media_stat/images/timeslots/neighbourhood_favs.gif" WIDTH="15" HEIGHT="20" border="0">
									</td>
									<td>
										<div onClick="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/editorial/timeslots/msg_neighbourhood_timeslot.html&windowSize=small&name=Neighbourhood+Favs','small');"><%= FDStoreProperties.getMyBuildingFavsLabel()%></div>
									</td>
									<td>&nbsp;</td>
								<%}%>
								<%if(deliveryModel.getAlcoholRestrictedCount() > 0){%>
									<td>
										<img src="/media_stat/images/timeslots/no_alcohol.gif" WIDTH="16" HEIGHT="16" border="0">
								</td>
									<td> 
										<div onClick="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/editorial/timeslots/msg_alcoholrestriction_timeslot.html&windowSize=small&name=Alcohol Restriction','small');"><%= FDStoreProperties.getAlcoholRestrictedLabel()%></div>
									</td>
									<td>&nbsp;</td>
								<%}%>
								<%if(deliveryModel.getEarlyAMCount() > 0) { %>
										<td>
											<img src="/media_stat/images/timeslots/early_delivery_icon_web.png" WIDTH="20" HEIGHT="16" border="0">
										</td>
										<td>
										&nbsp;
										</td>
										<td Valign="top">
										<a onClick="javascript:popup('/shared/template/generic_popup.jsp?contentPath=/media/editorial/timeslots/msg_early_am_timeslot.html&windowSize=small&name=Early AM','large');return false;">Early AM - Unattended</td>
										<td>&nbsp;</td>
								<%}%>
							</tr>
					</table>
		</td>
	</tr>
</table>
<BR><BR>

<!-- ~~~~~~~~~~~~~~~~~~~~~~ START TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
<table CELLSPACING="0" CELLPADDING="0" id="tsContainer">
<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.util.FDTimeslotUtil" indexId="idx">
	<tr>
		<td>
<%
	if(timeslotList.size()>1 && idx.intValue()==1){
%>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
		<span class="title13 fleft tsAdvanceHeader">Advance Order Delivery Timeslots&nbsp;&nbsp;
			<a class="tsDivHide" id="displayAdvanceOrderGrid" href="javascript:hideAdvanceOrder();">Hide Delivery Timeslots</a>
		</span>
	<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
<% } %>

<%
	Boolean isForAdvOrdSlots = false;
	if(timeslotList.size()>1 && idx.intValue()==1){
		isForAdvOrdSlots = true;
    }
	Boolean isForNewAdvOrdSlots = false;
	if(timeslotList.size()==3 && idx.intValue()==2){
		isForNewAdvOrdSlots = true;
	}
	
	if((timeslotList.size() > 1 && idx.intValue()==0)){
		showAdvanceOrderBand=false;
 %>
	<span class="title13">Standard Delivery Timeslots</span><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
			
<%} else { 
    showAdvanceOrderBand = timeslotList.size()>1 ? true && advOrdRangeOK: false;
 } %>

	<BR><%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>

<!-- ~~~~~~~~~~~~~~~~~~~~~~ END TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
	</td>
</TR>
</logic:iterate>
</table>
	<%}else{%>
	<table CELLSPACING="0" CELLPADDING="0">
		<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.util.FDTimeslotUtil" indexId="idx">
		<tr>
			<td align="center">
				<%    if(idx.intValue() == 1){
						showAdvanceOrderBand=false;
				%>
				<span class="text12"><b>Standard Delivery Slots</b></span>
				<%    } else {
					showAdvanceOrderBand = timeslotList.size()>1 ? true & advOrdRangeOK : false;
				}%>
			
				<%@ include file="/shared/includes/delivery/i_restriction_band.jspf"%>
				<%@ include file="/shared/includes/delivery/i_delivery_slots_old.jspf"%>
			</td>
		</tr>
		</logic:iterate>
	</table>
	<br/>
	<table CELLSPACING="0" CELLPADDING="0" width="735">
			<tr>
				<%if(page_type == TimeslotLogic.PAGE_CHEFSTABLE){%>
					<td align="right">
						<img align="bottom" style="position: relative; top: 2px;" hspace="4" vspace="0" width="12px" height="12px" src="/media_stat/images/background/prp1x1.gif"> <b>Chef's Table only</b>
					</td>
				<%}if(hasPreReserved){%>
					<td align="left">
						&nbsp;&nbsp;<img src="/media_stat/images/layout/ff9933.gif" width="12" height="12"> <b> Your Reserved Delivery Slot </b>
					</td>
				<%}%>
				<%if(zonePromoAmount > 0){ %>
					<td align="right">
						<img align="bottom" style="position: relative; top: 2px;" hspace="4" vspace="0" width="12px" height="12px" src="/media_stat/images/background/green1x1.gif"><b> Save $<%= zonePromoString %> when you choose a <a href="javascript:popup('/checkout/step_2_green_popup.jsp','small')">green timeslot</b></a>
					</td>
				<%}%>
			</tr>
	</table>
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td align="left">
				<img src="/media_stat/images/template/checkout/x_trans.gif" width="10" height="10" border="0" valign="bottom">
				= Delivery slot full&nbsp;&nbsp;&nbsp;
			</td>
			<td align="right">You must complete checkout for next-day deliveries before the "Order by" time.</td>
		</tr>
	</table>
	<%}%>
	</TD>
</TR>
</TABLE>
<!-- ~~~~~~~~~~~~~~~~~~~~~~ END TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
<TABLE>
<TR VALIGN="TOP">
    <TD WIDTH="70%" style="padding-left: 10px;">
		<b>Delivery Fee:</b> 
		<%
	String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge());
	if (cart.isDlvPassApplied()) {
	%>
	<%= cart.getDeliveryCharge()>0?JspMethods.formatPrice(cart.getDeliveryCharge()):DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
	<%
	} else if (cart.isDeliveryChargeWaived()) {
	%>
		Free! We've waived the standard <%= dlvCharge %> delivery fee for this order.
	<%}else {%>
		<%= JspMethods.formatPrice(cart.getDeliveryCharge()) %>
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
</div>
</tmpl:put>
	</fd:CheckoutController>
</tmpl:insert>
</crm:GetCurrentAgent>
</fd:DeliveryTimeSlot>
