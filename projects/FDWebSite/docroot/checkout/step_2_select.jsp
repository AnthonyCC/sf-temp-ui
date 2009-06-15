<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.delivery.depot.*' %>
<%@ page import="com.freshdirect.delivery.restriction.DlvRestrictionsList"%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.delivery.EnumReservationType"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>

<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason"%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.MessageFormat' %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%

request.setAttribute("CHECK_UNATTENDED_DELIVERY","true");

int timeslot_page_type = TimeslotLogic.PAGE_NORMAL;
boolean showAdvanceOrderBand = false;

if("true".equals(request.getParameter("chefstable"))) {
	timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;
}

DateRange advOrdRange = FDStoreProperties.getAdvanceOrderRange();
Calendar tomorrow = Calendar.getInstance();
tomorrow.add(Calendar.DATE, 1);
tomorrow = DateUtil.truncate(tomorrow);
DateRange validRange = new DateRange(tomorrow.getTime(),DateUtil.addDays(tomorrow.getTime(),FDStoreProperties.getHolidayLookaheadDays()));
boolean advOrdRangeOK = advOrdRange.overlaps(validRange);

 //System.out.println("validRange:"+validRange);

%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<%
    String successPage = "/checkout/step_3_choose.jsp";
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	if (user.isChefsTable()) timeslot_page_type = TimeslotLogic.PAGE_CHEFSTABLE;

	FDCartModel cart = user.getShoppingCart();
	ErpAddressModel address = cart.getDeliveryAddress();
	FDReservation rsv = user.getReservation();
	String preReserveSlotId = "";
	String hamptonsDepotCode ="HAM";
	String depotCode=null;
	boolean hasPreReserved = false;
	if(rsv != null){
		preReserveSlotId = rsv.getTimeslotId();
		hasPreReserved = address.getPK()!=null && address.getPK().getId().equals(rsv.getAddressId());
	}
	String timeSlotId = request.getParameter("deliveryTimeslotId");
	if (cart.getDeliveryReservation() != null) {
		rsv = cart.getDeliveryReservation();
	}
	if (timeSlotId == null) {
		if(rsv != null){
			timeSlotId = rsv.getTimeslotId();
		}else{
			timeSlotId = "";
		}
	}
	
	if(cart instanceof FDModifyCartModel && rsv != null && !EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType()) && "".equals(preReserveSlotId) && !hasPreReserved){
		preReserveSlotId = rsv.getTimeslotId();
		hasPreReserved = address.getPK()!=null && address.getPK().getId().equals(rsv.getAddressId());
	}
	
	boolean isDepotAddress = false;
	ErpDepotAddressModel depotAddress = null;
	
	if(address instanceof ErpDepotAddressModel){
		isDepotAddress = true;
		
		depotAddress =  (ErpDepotAddressModel) address;
		depotCode = com.freshdirect.fdstore.FDDepotManager.getInstance().getDepotByLocationId(depotAddress.getLocationId()).getDepotCode();
	}
	double cartTotal = cart.getTotal();
    List payMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
    if (payMethods==null || payMethods.size()==0) {
        successPage = "/checkout/step_3_card_add.jsp?proceed=true";
    }
    successPage = "/checkout/step_2_check.jsp?successPage="+URLEncoder.encode(successPage);

	String zoneId = cart.getDeliveryZone();
	if (zoneId==null ) {
		//redirect back to the view cart page 
		response.sendRedirect(response.encodeRedirectURL("/checkout/view_cart.jsp?trk=chkplc"));
		return;
	}
	boolean isStaticSlot = false;
	boolean thxgivingRestriction = false;
	boolean easterRestriction = false;
	boolean easterMealRestriction = false; //easter meals
	boolean valentineRestriction = false;
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
		//easter
        if(EnumDlvRestrictionReason.EASTER.equals(reason)){
           easterRestriction=true;
           continue;
        }
		//easter meals
        if(EnumDlvRestrictionReason.EASTER_MEALS.equals(reason)){
           easterMealRestriction=true;
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
		if(EnumDlvRestrictionReason.VALENTINES.equals(reason)){
			valentineRestriction = true;
			continue;
		}
	}
%>
<fd:DeliveryTimeSlot id="DeliveryTimeSlotResult" address="<%=address%>">
<%
	List timeslotList = DeliveryTimeSlotResult.getTimeslots();
	Map zones = DeliveryTimeSlotResult.getZones();
	boolean zoneCtActive = DeliveryTimeSlotResult.isZoneCtActive();
	List messages = DeliveryTimeSlotResult.getMessages();
	
	DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
	boolean isKosherSlotAvailable = false;
	boolean hasCapacity = true;
	for(Iterator i = timeslotList.iterator(); i.hasNext(); ){
		FDTimeslotList lst = (FDTimeslotList)i.next();
		isKosherSlotAvailable = isKosherSlotAvailable || lst.isKosherSlotAvailable(restrictions);
		hasCapacity = hasCapacity || lst.hasCapacity();
	}
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
%>

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Time </tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckoutController actionName="reserveDeliveryTimeSlot" result="result" successPage="<%=successPage%>">

<%@ include file="/includes/i_modifyorder.jspf" %>

<table border="0" cellspacing="0" cellpadding="0" width="695">
<FORM method="post" action="/checkout/step_2_select.jsp">
	<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="455" VALIGN="bottom">
<%if (isDepotAddress) {%>
		<FONT CLASS="title18">Choose Time of <%=user.isCorporateUser() ? "Delivery" : "Pickup"%> (Step 2 of 4)</FONT><BR>	
		<FONT CLASS="text12">
		Please choose the day and time for your <%=user.isCorporateUser() ? "delivery" : "pickup"%> below.<br>
                <%if (depotAddress.isPickup()) {%>
                    <a href="javascript:popup('/delivery_popup.jsp','large')">Learn more about our Pickup location</a>.
                <%} else if(user.isCorporateUser()){%>
                    <a href="javascript:popup('/help/faq_index.jsp?show=cos','large')">Find out</a> more information about Corporate Services here.
                <%} else {%>
                    <a href="javascript:popup('/help/faq_index.jsp?show=delivery_depot','large')">Find out</a> more information about Corporate Services here.
                <%}%><BR>
		</FONT>		
<%}else{%>
		<FONT CLASS="title18">Choose Delivery Time (Step 2 of 4)</FONT><BR>	
		<FONT CLASS="text12">
		Please select the date and time for your delivery. Remember, the first available slot is tomorrow. To find out more about delivery, <a href="javascript:popup('/help/faq_index.jsp?show=delivery','large')">click here</a>. <BR>
		</FONT>		
<%}%>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
	<%if(hasCapacity){%>
		<TD WIDTH="205" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
			<FONT CLASS="space2pix"><BR></FONT><input type="image" name="form_action_name" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0"><BR>
			<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= CCFormatter.formatCurrency(cartTotal) %></TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
		 	BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></TD>
	<%}else{%>
		<td width="205" align="right" valign="middle" class="text10bold">&nbsp;</td>
		<td width="35" align="right" valign="middle">&nbsp;</td>
	<%}%>
		
	</TR>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	<% String[] checkErrorType = {"system", "technical_difficulty", "deliveryTime", "pickup_didnot_agree"}; %>
    <fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
    </fd:ErrorHandler>
<%
String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
if (errorMsg==null && user.isPromotionAddressMismatch()) {
	
	Promotion promo = (Promotion)user.getEligibleSignupPromotion();
	Double totalPromo = new Double(promo.getHeaderDiscountTotal());
    errorMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, user.getCustomerServiceContact()});
}
if (errorMsg!=null) {%> 
    <%@ include file="/includes/i_error_messages.jspf"%> 
<%}%>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="695">
<TR VALIGN="TOP">
<TD CLASS="text12" width="455">
<b><%= (isDepotAddress && depotAddress.isPickup())?"Service":"Delivery" %> Charge:  </b>

<%	
	String dlvCharge = CCFormatter.formatCurrency( cart.getDeliverySurcharge() );
	if(cart.isDlvPassApplied()) {
%>
	<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
	
<%	} else if (cart.isDeliveryChargeWaived()) {
        if((int)cart.getDeliverySurcharge() == 0){
%>     
		Free! <% }else{ %> Free! We've waived the standard <%= dlvCharge %> delivery charge for this order. <% } %>	
                
	<%}else {%>
		<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
	<%}%>		
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></TD>

<td width="240" class="text12" align="right">
	<% if(user.isEligibleForPreReservation()){
	 		FDReservation userRsv = user.getReservation();
			if(userRsv != null && address.getPK()!=null && userRsv.getAddressId().equals(address.getPK().getId())){%>
				<img src="/media_stat/images/layout/ff9933.gif" width="12" height="12"> = Your <%= EnumReservationType.RECURRING_RESERVATION.equals(userRsv.getReservationType()) ? "Weekly" : "" %> Reserved Delivery Slot
		<% } 
	}%>
</TD>
</TR>

<%if(kosherRestriction){%>
<tr valign="top">
	<td colspan="2" class="text12">
	<table width="100%"><tr><td><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0"></td>
	<td class="text12"><b><span class="kosher">Kosher Delivery Note:</span></b> 
	The custom-cut kosher items in your cart are not available for delivery on Friday, Saturday, or Sunday morning.
		<fd:GetDlvRestrictions id="kosherRestrictions" reason="<%=EnumDlvRestrictionReason.KOSHER%>" withinHorizon="true">
		<% if (kosherRestrictions.size() > 0) { %>They are also unavailable during
			<logic:iterate indexId='i' collection="<%= kosherRestrictions %>" id="restriction" type="com.freshdirect.delivery.restriction.RestrictionI">
			<b><%=restriction.getName()%></b>, <%=restriction.getDisplayDate()%><% if (i.intValue() < kosherRestrictions.size() -1) {%>; <% } else { %>.<% } %>
			</logic:iterate>
		<% } %>
		</fd:GetDlvRestrictions><% if (user.isDepotUser() && isKosherSlotAvailable) { %><b>Unfortunately there is no time during the next week that custom-cut kosher items can be delivered. If you continue Checkout, these items will be removed from your cart.</b> <% } else { %>Available delivery days for all of your kosher items are marked in blue. <% } %><a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Learn More</a>
	</td>
	</tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
	</table>
	</td>
</tr>
<%}%>
<tr>
<td colspan="2">
<%@ include file="/shared/includes/delivery/i_loyalty_banner.jspf" %>
</td>
</tr>

<%if(cart.hasAdvanceOrderItem() && advOrdRangeOK && thxgivingRestriction){%>
<tr valign="top">
	<td colspan="2" class="text12">
	<fd:IncludeMedia name='/media/editorial/holiday/advance_order/delivtext_adv.html'/>
	</td>
</tr>
<%}%>

	<%
		//start add easter meals
		//easterMeal is for advanced ordering, easter is a sperate restriction
	
	//System.out.println("hasAdvanceOrderItem:"+cart.hasAdvanceOrderItem());
	//System.out.println("advOrdRangeOK:"+advOrdRangeOK);
	//System.out.println("easterMealRestriction:"+easterMealRestriction);
	
	if(cart.hasAdvanceOrderItem() && advOrdRangeOK && easterMealRestriction){%>
	<tr valign="top">
		<td colspan="2" class="text12">
		<fd:IncludeMedia name='/media/editorial/holiday/advance_order/eastermeals/delivbar_adv_msg.html'/>
		</td>
	</tr>
	<%}%>

	<%if(easterMealRestriction){%>
	<tr valign="top">
		<td colspan="2" class="text12">
		<fd:IncludeMedia name='/media/editorial/holiday/easter/eastermeals_chkout_msg.htm'/>
		</td>
	</tr>
	<%}%>

<%if(thxgiving_meal_Restriction){%>
<tr valign="top">
	<td colspan="2" class="text12">
	<fd:IncludeMedia name='/media/editorial/holiday/thanksgiving/thanksgiv_chkout_msg.htm'/>
	</td>
</tr>
<%}%>
<%if(valentineRestriction){%>
<tr valign="top">
	<td colspan="2" class="text12">
	<fd:IncludeMedia name='/media/editorial/holiday/valentines/valentines_chkout_msg.htm'/>
	</td>
</tr>
<%}%>

<%
	if(user.getSelectedServiceType() == EnumServiceType.CORPORATE) {
%>
	
	<TR>
		<td width="100%" align="center"  align="center"><font class="title16" color="#339900">Delivery Charge $9.99 Tuesday-Friday ($14.99 Monday).</font><br><br></td>	
	</TR>	
<%}%>


<%
	if(user.getSelectedServiceType() == EnumServiceType.CORPORATE  && (user.isDlvPassActive() || cart.getDeliveryPassCount() > 0)) {
%>
	<TR>
	<td width="100%" align="center"><font class="text12bold" color="#FF0000">IMPORTANT INFORMATION FOR OFFICE DELIVERIES</font></td>	
	</TR>
	<TR>
		<td class="text12" align="center">Unlimited DeliveryPass is only good for residential deliveries and will not apply to this order.<br><br></td>	
	</TR>	
<%}%>	

<%if(!hasCapacity){%>
	<TR><td colspan="2">
		<%@ include file="/includes/delivery/i_slots_no_capacity.jspf"%>
	<br></td></TR>
<%}else{%>
<% if (FDStoreProperties.isAdServerEnabled()) { %>
		<SCRIPT LANGUAGE="JavaScript">
                <!--
                OAS_AD('CategoryNote');
                //-->
      	</SCRIPT>
<% } %>
<logic:iterate id="timeslots" collection="<%=timeslotList%>" type="com.freshdirect.fdstore.FDTimeslotList" indexId="idx">
<TR><td colspan="2" align="center">
<%if(idx.intValue() == 1){
	showAdvanceOrderBand=false;
%>
	<span class="text12"><b>Standard Delivery Slots</b></span>
<%} else { 
    showAdvanceOrderBand = timeslotList.size()>1 ? true && advOrdRangeOK: false;
	
		//System.out.println("=====================showAdvanceOrderBand:"+showAdvanceOrderBand);
  }
%>
<%-- ~~~~~~~~~~~~~~~~~~~~~~ START TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
	<%@ include file="/shared/includes/delivery/i_restriction_band.jspf"%>
	<%@ include file="/shared/includes/delivery/i_delivery_slots.jspf"%>
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END TIME SLOT SELECTION SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</td></TR>
</logic:iterate>
<%}%>
<tr><td colspan="2">
	<!-- Bryan Restriction Message Added -->
	<% if(messages != null && messages.size() >= 1) { %>
		<%@ include file="/shared/includes/delivery/i_restriction_message.jspf"%>
	<% } %>
</td></tr>
<tr><td colspan="2">
<table cellpadding="0" cellspacing="0" width="675">
	<tr>
		<td align="left">
			<img src="/media_stat/images/template/checkout/x_trans.gif" width="10" height="10" border="0" valign="bottom" alt="X">
			= Delivery slot full
		</td>
		<td align="right">You must complete checkout for next-day deliveries before the "Order by" time.</td>
	</tr>
</table>

</td></tr>
<%
if (timeslot_page_type != TimeslotLogic.PAGE_CHEFSTABLE) {
%>
<tr>
<td colspan="2">
<%@ include file="/shared/includes/delivery/i_loyalty_button.jspf" %>
</td>
</tr>
<%
}
%>

<tr><td colspan="2" align="center" class="text12">
	<%if(cart.containsAlcohol()){%>
		<fd:IncludeMedia name="/media/editorial/site_pages/alcohol_restriction.html"/>
		<br>
	<%}%>
	<br>
	</td>
</tr>
<% if ( !user.isPickupOnly() && user.isEligibleForSignupPromotion() && isDepotAddress && depotAddress.isPickup()) {%>
<tr><td colspan="2" align="center" class="text12"><b>PLEASE NOTE: PICKUP ORDERS ARE NOT ELIGIBLE FOR OUR FREE FOOD PROMOTION.</b><br>The free fresh food promotion will not be applied to this order. When you place your first home or depot delivery order you will be eligible for any promotions in effect at that time.<br><br></td></tr>
<% } %>

</TABLE>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<tr>
	<td class="text12">
	<%if(isDepotAddress && !depotAddress.isPickup() && !user.isCorporateUser()){%>
		<br><br><img src="/media_stat/images/template/depot/special_pickup_instructions.gif" width="313" height="9" alt="" border="0"><br>
		<%if( depotAddress.getInstructions() != null){%>
			<%=depotAddress.getInstructions()%><br>
		<%}else{%>
			Pull up to the designated area in your company's parking lot. Present your ID to the driver and pick up your food.	<br>
		<%}%>
		<br>
	<%}%>
	</td>
</tr>
</table>
<%
	if (FDStoreProperties.getHamptons()) {   	%>
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
			<tr><td align="center" class="text12">

<%	if (!isDepotAddress && "SUFFOLK".equalsIgnoreCase(address.getAddressInfo().getCounty()) ){  	%>
	<fd:IncludeMedia name="/media/editorial/hamptons/timeslot_hamptons_fri_sat.html" />
<%	} else if (isDepotAddress && hamptonsDepotCode.equalsIgnoreCase(depotCode)){ %>
	<fd:IncludeMedia name="/media/editorial/hamptons/timeslot_hamptons_wkday.html" />
<%	} %>
			<br><br></td></tr>
		</table>
<%	}  %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP">
	<TD WIDTH="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
	<TD WIDTH="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
	<%if(hasCapacity){%>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE"><input type="image" name="checkout_delivery_address_select"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"><BR>Go to Step 3: Payment Info<BR></TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT><input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></TD>
	<%}else{%>
		<td width="265" align="right" valign="middle">&nbsp;</td>
		<td width="35" align="right" valign="middle">&nbsp;</td>
	<%}%>
</TR>
</TABLE>
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>
</fd:DeliveryTimeSlot>
