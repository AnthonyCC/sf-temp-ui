<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.delivery.depot.*' %>
<%@ page import="com.freshdirect.delivery.restriction.DlvRestrictionsList"%>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.fdstore.promotion.PromotionHelper" %>
<%@ page import="com.freshdirect.fdstore.standingorders.DeliveryInterval"%>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason"%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.MessageFormat' %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotContext" %>
<%@ page import="com.freshdirect.fdstore.util.FDTimeslotUtil" %>
<%@ page import="com.freshdirect.fdstore.util.AddressFinder" %>
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.freshdirect.deliverypass.*" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%

request.setAttribute("CHECK_UNATTENDED_DELIVERY","true");

boolean showAdvanceOrderBand = false;

TimeslotContext timeSlotCtx= TimeslotContext.CHECKOUT_TIMESLOTS;
DateRange advOrdRange = FDStoreProperties.getAdvanceOrderRange();
Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);
tomorrow = DateUtil.truncate(tomorrow);
DateRange validRange = new DateRange(tomorrow.getTime(),DateUtil.addDays(tomorrow.getTime(),FDStoreProperties.getHolidayLookaheadDays()));
boolean advOrdRangeOK = advOrdRange.overlaps(validRange);
boolean isAdvOrderGap = FDStoreProperties.IsAdvanceOrderGap();

%>

<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<%
    String successPage = "/checkout/step_3_choose.jsp";

	boolean isStaticSlot = false;
	boolean isDepotAddress = false;
	ErpAddressModel address;
	ErpDepotAddressModel depotAddress = null;
	String depotCode=null;
	final String HAMPTONS_DEPOT_CODE ="HAM";

	String addressId="";
	String timeSlotId = request.getParameter("deliveryTimeslotId");
	
	FDStandingOrder currentStandingOrder = user.getCurrentStandingOrder();
	
	if ( EnumCheckoutMode.NORMAL == user.getCheckoutMode() || EnumCheckoutMode.CREATE_SO == user.getCheckoutMode() ) {

		address = AddressFinder.getShipToAddress(user, addressId, timeSlotCtx, request);

	} else { //if ( EnumCheckoutMode.MODIFY_SO == user.getCheckoutMode() ) {
		address = currentStandingOrder.getDeliveryAddress();
		/* STANDING ORDER - UNFINISHED CODE */
	} 

	if(address instanceof ErpDepotAddressModel){
		isDepotAddress = true;
		depotAddress =  (ErpDepotAddressModel) address;
		depotCode = com.freshdirect.fdstore.FDDepotManager.getInstance().getDepotByLocationId(depotAddress.getLocationId()).getDepotCode();
	}

    List<ErpPaymentMethodI> payMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
    if (payMethods==null || payMethods.size()==0) {
        successPage = "/checkout/step_3_card_add.jsp?proceed=true";
    }
    successPage = "/checkout/step_2_check.jsp?successPage="+URLEncoder.encode(successPage);

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

	if (zoneId==null ) {
		//redirect back to the view cart page 
		response.sendRedirect(response.encodeRedirectURL("/checkout/view_cart.jsp?trk=chkplc"));
		return;
	}
	boolean hasCapacity = deliveryModel.hasCapacity();;
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
	//get zone promotion amount
    double zonePromoAmount = deliveryModel.getZonePromoAmount();
    String zonePromoString=null;
	if(zonePromoAmount > 0)
	{
        zonePromoString = CCFormatter.formatQuantity(zonePromoAmount);
		request.setAttribute("SHOW_WINDOWS_STEERING","true");
	}
%>

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Time </tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckoutController actionName="reserveDeliveryTimeSlot" result="result" successPage="<%=successPage%>">
<script type="text/javascript">
var zonePromoString=""; 
var zonePromoEnabled=false;
<%if(zonePromoAmount>0){ %>
zonePromoString="<%=zonePromoString %>"; 
zonePromoEnabled=true;
<%} %>
</script>

<%@ include file="/includes/i_modifyorder.jspf" %>

<FORM method="post" name="step2Form" onSubmit="return checkPromoEligibilityByTimeSlot('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');" action="/checkout/step_2_select.jsp">
<div class="gcResendBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="gcResendBoxContent" id="gcResendBox">
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">IMPORTANT MESSAGE &nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td><div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<%-- all your content goes in this div, it controls the height/width --%>
					The promotion code you entered <div id="promoCode"></div> is not valid for the day and time you selected. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
					<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<a href="#" onclick="Modalbox.hide(); return false;">CHOOSE ANOTHER</a><br />
					<a href="#" onclick="javascript:document.forms['step2Form'].submit();"><b>CONTINUE</b></a><br />
					(promotion code will be removed)
				</div></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
			<tr>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_left_curve_8A6637.gif" width="6" height="6" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /></td>
				<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_right_curve_8A6637.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
			</tr>
		</table>
	</div>

<!--START TIMESLOT PAGE HEADER-->
<table border="0" cellspacing="0" cellpadding="0" width="695">
	<TR VALIGN="TOP">
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
<%if (isDepotAddress) {%>
			<FONT CLASS="title18">CHOOSE A <%=user.isCorporateUser() ? "DELIVERY" : "PICKUP"%> TIME</FONT><BR>
                <%} else {%>
			<FONT CLASS="title18">CHOOSE TIME</FONT><BR>
<%}%>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
		
	<%if(hasCapacity){%>

			<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10">
		      <FONT CLASS="space2pix"><BR></FONT>
				<table>
					<tr>
						<td align="left"  style="color:#666666;font-weight:bold;"><%= (isDepotAddress && depotAddress.isPickup())?"Service":"Delivery" %> Charge:</td>
						<td align="right" style="color:#666666;font-weight:bold;">
<%	
	String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge() );
	if(cart.isDlvPassApplied()) {
%>
	<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
	
<%	} else if (cart.isDeliveryChargeWaived()) {
        if((int)cart.getDeliverySurcharge() == 0){
%>     
									Free! 
									<% }else{ %> Free!(<%= dlvCharge %> waived)<% } %>
                
	<%}else {%>
		<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
							<%}%>
					</td>
					</tr>
					<%if (cart.getTotalDiscountValue() > 0) {
								List discounts = cart.getDiscounts();
									for (Iterator iter = discounts.iterator(); iter.hasNext();) {
										ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
										Discount discount = discountLine.getDiscount();
										PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
										String desc = EnumPromotionType.SIGNUP.equals(promotion.getPromotionType()) ? "Free Food" : promotion.getDescription();
										%>
										<tr>
											<td align="left" style="color:#669933;font-weight:bold;">Delivery Discount:</td>
											<td align="right" style="color:#669933;font-weight:bold;">-<%= JspMethods.formatPrice(discount.getAmount()) %></td>
										</tr>
							<%}	}%>
					<tr>
						<td align="left"  style="color:#666666;font-weight:bold;"><A HREF="javascript:popup('/help/estimated_price.jsp','small')"></A>Estimated Total:</td>
						<td align="right"  style="color:#666666;font-weight:bold;"><%= currencyFormatter.format(cart.getTotal()) %></td>
					</tr>
				</table>
			</TD>		
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
					<input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_right.gif"
					BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2">
				</TD>
	<%}else{%>
				<td width="265" align="right" valign="middle" class="text10bold">&nbsp;</td>
		<td width="35" align="right" valign="middle">&nbsp;</td>
	<%}%>
		
	</TR>
</table>
<!--END TIMESLOT PAGE HEADER-->	
	
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="3" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	


<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="695">
	<TR VALIGN="TOP">
		<TD CLASS="text12" width="455" colspan="2"></TD>
	</TR>

	<TR>
		<td colspan="2">
			<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
		</td>
	</TR>
	<TR>
		<td colspan="2">
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

			<%@ include file="/includes/delivery/i_top_modules.jspf" %>
		</td>
	</TR>
	
</TABLE>

<!--START MESSAGING SECTION-->

<!-- GEO Restriction Message Added -->
<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_geowarning_message.jspf"%>
<% } %>


<!-- Error Messages -->
	<% String[] checkErrorType = {"system", "technical_difficulty", "deliveryTime", "pickup_didnot_agree"}; %>
    <fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		<%@ include file="/includes/i_timeslot_error_messages.jspf" %>
    </fd:ErrorHandler>
    <%if(DeliveryTimeSlotResult != null && !DeliveryTimeSlotResult.isSuccess() ){%>
		<fd:ErrorHandler result='<%=DeliveryTimeSlotResult%>' name='deliveryTime' id='errorMsg'>
				<%@ include file="/includes/i_timeslot_error_messages.jspf" %>
		</fd:ErrorHandler>
	<%}%>
<%

String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
if (errorMsg==null && user.isPromotionAddressMismatch()) {
	
	Promotion promo = (Promotion)user.getEligibleSignupPromotion();
	Double totalPromo = new Double(promo.getHeaderDiscountTotal());
    errorMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, user.getCustomerServiceContact()});
}
if (errorMsg!=null) {%> 
	<%@ include file="/includes/i_timeslot_error_messages.jspf"%> 
<%}%>

<!-- Alcohol, SpecialItems, holiday, deliveryPass, noCapacity warning Messages -->

<%if(cart.containsAlcohol() && deliveryModel.isAlcoholDelivery()){%>

	<%@ include file="/includes/delivery/i_alcohol_warning.jspf"%>	
		
		<% } %>

<%if(user.getSelectedServiceType() == EnumServiceType.CORPORATE) { %>
	
	<%@ include file="/includes/delivery/i_delivery_pass_not_applied.jspf"%>	
	
<%}%>

<%if(!hasCapacity){%>

	<%@ include file="/includes/delivery/i_slots_no_capacity.jspf"%>
	
<%}else{ %>
	<% if (FDStoreProperties.isAdServerEnabled()) { %>
			<SCRIPT LANGUAGE="JavaScript">
					<!--
					OAS_AD('CategoryNote');
					//-->
			</SCRIPT>
<% } %>

	
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
	
<%if(deliveryModel.isEasterMealRestriction() && !advOrdRangeOK){%>
	
	<%@ include file="/includes/delivery/i_eastermeal.jspf"%>
		
<%}%>
<%if(deliveryModel.isThxgiving_meal_Restriction()){%>

	<%@ include file="/includes/delivery/i_thanksgiving_meal.jspf"%>
		
<%}%>
<%if(deliveryModel.isValentineRestriction()){%>
	
	<%@ include file="/includes/delivery/i_valentine.jspf"%>
	
<%}%>
<!--END MESSAGING SECTION-->
<BR>

<!-- ~~~~~~~~~~~~~~~~~~~~~~ LEGEND DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
<table>
<tr>
		<td>
			<table cellpadding="0" cellspacing="0" width="675" id="legendCheckout">
				<tr>	
					<td Valign="middle" align="right">
					 	<table>
							<tr>
								<%if(deliveryModel.getEcoFriendlyCount() > 0){%>
									<td>
										<img src="/media_stat/images/timeslots/ecofriendly_leaf.gif" WIDTH="16" HEIGHT="16" border="0">
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
							</tr>
						</table>
	</td>
</tr>
			</table>
	</td>
</tr>
</table>
<!-- ~~~~~~~~~~~~~~~~~~~~~~ END LEGEND DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ -->
	
<!-- ~~~~~~~~~~~~~~~~~~~~~~ START TIMESLOT GRID ~~~~~~~~~~~~~~~~~~~~~~ -->
<table CELLSPACING="0" CELLPADDING="0" id="tsContainer" align="center" width="100%">

<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.util.FDTimeslotUtil" indexId="idx">
	<tr>
		<td colspan="2">
<%
	if(timeslotList.size()>1 && idx.intValue()==0){
%>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0">
	<span class="text12 fleft tsAdvanceHeader"><b>ADVANCE DELIVERY</b>&nbsp;&nbsp;<a class="tsDivHide" id="displayAdvanceOrderGrid" href="javascript:hideAdvanceOrder();">Hide Timeslots</a></span>
	<BR>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
<% } %>

<%
	if(timeslotList.size()==3 && idx.intValue()==1){
%>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
		<span class="text12 fleft tsAdvanceHeader"><b>ADVANCE DELIVERY</b></span>
	<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
<% } %>


<%	
	Boolean isForAdvOrdSlots = false;
	if(timeslotList.size()>1 && idx.intValue()==0){
		isForAdvOrdSlots = true;
	}
	Boolean isForNewAdvOrdSlots = false;
	if(timeslotList.size()==3 && idx.intValue()==1){
		isForNewAdvOrdSlots = true;
	}

	// If there are 2 advance order timeslots then show standard delivery header accordingly
	if((timeslotList.size()>2 && isAdvOrderGap && idx.intValue()==2) ||
		(timeslotList.size()==2 && isAdvOrderGap && idx.intValue()==1) ||
		(!isAdvOrderGap && idx.intValue()==1)){
	//if(idx.intValue() == 1){
	
	showAdvanceOrderBand=false;
%>
	<span class="text12 tsAdvanceHeader"><b>REGULAR DELIVERY</b><BR></span>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10" BORDER="0"><BR>
			
<%} else { 
    showAdvanceOrderBand = timeslotList.size()>1 ? true && advOrdRangeOK: false;
 } %>

<%
	// calculate which timeslot to select when modifying standing orders
	if ( currentStandingOrder != null ) {
		//out.print( "SO=" + currentStandingOrder + "<br/>");
		DeliveryInterval deliveryInterval;
		try { 
			deliveryInterval = new DeliveryInterval( currentStandingOrder );
		} catch ( IllegalArgumentException e ) {
			deliveryInterval = null;
		}
		if ( deliveryInterval != null && deliveryInterval.isWithinDeliveryWindow() ) {
			for ( FDTimeslot tsl : timeslots.getTimeslotsFlat() ) {
				if ( deliveryInterval.checkTimeslot( tsl ) ) {
					deliveryModel.setTimeSlotId(tsl.getTimeslotId());
					//out.print("timeslot has been selected :" + tsl +"<br/>");
					break;
				}
			}
		}
	}
%>
	<BR><%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>

<!-- ~~~~~~~~~~~~~~~~~~~~~~ END TIMESLOT GRID ~~~~~~~~~~~~~~~~~~~~~~ -->
		</td>
</TR>
</logic:iterate>
</table>
	<%}%>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="3" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP">
			<TD width="35">
					<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</TD>
		    <TD width="340">
				<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Address<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</TD>
	<%if(hasCapacity){%>
			<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE">
				<input type="image" name="checkout_delivery_timeslot_select"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="91" HEIGHT="11" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"><BR>
				Payment Method<BR>
			</TD>
			<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
				<FONT CLASS="space2pix"><BR></FONT>
				<input type="image" name="checkout_delivery_timeslot_select" src="/media_stat/images/buttons/checkout_right.gif" WIDTH="26" HEIGHT="26" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></TD>
	<%}else{%>
		<td width="265" align="right" valign="middle">&nbsp;</td>
		<td width="35" align="right" valign="middle">&nbsp;</td>
	<%}%>
</TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="3" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>


</FORM>
<script language="javascript">
	
	var c2ctimeslot = document.getElementById("c2cnextdaytimeslot");	
	var timeslotvalue=c2ctimeslot.value;	
	if(timeslotvalue==1){
		if(document.getElementById("loyalty1")){
			document.getElementById("loyalty1").style.display="block";
		}
		if(document.getElementById("loyalty3")){
			document.getElementById("loyalty3").style.display="block";
		}
	}else{
		if(document.getElementById("loyalty2")){
			document.getElementById("loyalty2").style.display="block";
		}
	}
	
</script>

</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>
</fd:DeliveryTimeSlot>