<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.net.*"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<%

// redirect to Survey if this is the second order && first order is delivered
if (!user.isSurveySkipped() && user.getAdjustedValidOrderCount()==1 && user.getDeliveredOrderCount()==1) {



// leave previous 4th order logic
    FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
    boolean alreadyTookFirstSurvey = "YES".equals(customer.getProfile().getAttribute("signup_survey")) ;
    boolean alreadyTookSecondSurvey = "FILL".equals(customer.getProfile().getAttribute("signup_survey_v2"))
                                       || "FILL".equals(customer.getProfile().getAttribute("fourth_order_survey"))
									   || "FILL".equals(customer.getProfile().getAttribute("fourth_order_cos_survey"))
									   || "FILL".equals(customer.getProfile().getAttribute("second_order_survey"));
    boolean skippedSecondSurvey = "SKIP".equals(customer.getProfile().getAttribute("signup_survey_v2"))
                                   ||  "SKIP".equals(customer.getProfile().getAttribute("fourth_order_survey"))
								   ||  "SKIP".equals(customer.getProfile().getAttribute("fourth_order_cos_survey"))
								   ||  "SKIP".equals(customer.getProfile().getAttribute("second_order_survey"));

 if (!alreadyTookFirstSurvey && !alreadyTookSecondSurvey && !skippedSecondSurvey) {

        FDSurvey usability = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.SECOND_ORDER_SURVEY, user);
 		if (usability == null) {
			response.sendRedirect(response.encodeRedirectURL("/checkout/survey_cos.jsp?successPage=/checkout/step_1_choose.jsp"));
		} else {
	        FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(user.getIdentity(), user);
           int coverage=SurveyHtmlHelper.getResponseCoverage(usability,surveyResponse);
           if(coverage<usability.getAcceptableCoverage()) {
           
        	    response.sendRedirect(response.encodeRedirectURL("/checkout/survey.jsp?successPage=/checkout/step_1_choose.jsp"));
                return;
           } 
    	}
	    
    }
}

%>
<%-- =================================================================================================== --%>

<% 
if (user.getLevel()==FDUserI.RECOGNIZED) {
    response.sendRedirect(response.encodeRedirectURL("/login/login.jsp?successPage=/checkout/step_1_choose.jsp"));
    return;
}

String forwardPage = "/checkout/step_1_choose.jsp";

List<ErpAddressModel> dlvAddresses = FDCustomerFactory.getErpCustomer(user.getIdentity()).getShipToAddresses();
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<%  
String actionName = "setDeliveryAddress";
String successPage = "/checkout/step_2_select.jsp";

Enumeration<String> e = request.getParameterNames();
while (e.hasMoreElements()) {
    String name = e.nextElement();
    if (name.startsWith("delete_")) {
        actionName = "deleteDeliveryAddress";
        successPage ="/checkout/step_1_choose.jsp";
        String addrId = name.substring(name.indexOf("_") + 1, name.indexOf("."));
        request.setAttribute("deleteShipToAddressId", addrId);
        break;
    }
} 

%>
<fd:CheckoutController actionName='<%=actionName%>' result="result" successPage="<%=successPage%>">
	<%
	if (result.hasError("error_dlv_pass_only") || result.hasError("order_minimum")) {
	    response.sendRedirect(response.encodeRedirectURL("/checkout/view_cart.jsp"));
	    return;
	}
	
	double cartTotal = user.getShoppingCart().getTotal();
	if (result.hasError("cantGeocode")) {
	  String errorMsg=SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS;
	%>
	<%@ include file="/includes/i_error_messages.jspf" %>   
	<% } %>
	
	<% if (user.isEligibleForSignupPromotion() && user.getShoppingCart().getSubTotal() < user.getSignupDiscountRule().getMinSubtotal()) { 
	    String errorMsg = "Because your cart contains less than $" + (int)user.getSignupDiscountRule().getMinSubtotal() + " of food, you are not eligible for the free fresh food promotion. You may return to your cart to add more food or continue Checkout now. <a href=\"javascript:popup('/shared/promotion_popup.jsp','large');\">Click for details</a>.";
	    %>
	    <%@ include file="/includes/i_error_messages.jspf" %> 
	   <br/>
	<% } %>

	<%@ include file="/includes/i_modifyorder.jspf" %>
 
	<form method="POST" name="step1Form" onSubmit="return checkPromoEligibilityByAddress('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');">
	
	<div class="gcResendBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="gcResendBoxContent" id="gcResendBox">
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">IMPORTANT MESSAGE &nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td><div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<%-- all your content goes in this div, it controls the height/width --%>
					The promotion code you entered <div id="promoCode"></div> is not valid for the address you selected. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
					<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<a href="#" onclick="Modalbox.hide(); return false;">CHOOSE ANOTHER</a><br />
					<a href="#" onclick="javascript:document.forms['step1Form'].submit();"><b>CONTINUE</b></a><br />
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

	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top"> 
			<td CLASS="text11" WIDTH="395" VALIGN="bottom">
				<FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
				<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
			</td>
			<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10">
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left"  style="color:#666666;font-weight:bold;">Delivery Charge:</td>
							<td align="right"  style="color:#666666;font-weight:bold;">
								<%	
									FDCartModel cart = user.getShoppingCart();
									String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge() );
									if(cart.isDlvPassApplied()) {
								%>
									<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
									
								<%	} else if (cart.isDeliveryChargeWaived()) {
										if((int)cart.getDeliverySurcharge() == 0){
								%>     
										Free! 
										<% }else{ %> Free!(<%= dlvCharge %> waived)<% } %>
												
								<%  } else {%>
										<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
								<%}%>
						</td>
						</tr>
						<%if (cart.getTotalDiscountValue() > 0) {
								List discounts = cart.getDiscounts();
									for (Iterator iter = discounts.iterator(); iter.hasNext();) {
										ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
										Discount discount = discountLine.getDiscount();
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
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
	            <input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_right.gif" border="0" alt="CONTINUE CHECKOUT" vspace="2">
	        </td>
	    </tr>
	</table>
	
	<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0"><br/>
	<!-- PROFILE HEADER -->
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

	<TABLE border="0" cellspacing="0" cellpadding="0" width="675">
		 <tr valign="top"> 
			 <td class="text12" width="375" valign="bottom"> 
					<font class="title18">Choose Delivery Address (Step 1 of 4)</font><br/>
					<%if(user.isPickupOnly() ){%>   
				<b>Please Note: </b>Your home address is not in a FreshDirect <a href="javascript:popup('/help/delivery_zones.jsp','large');">delivery zone</a>.
				Please select one of our pickup locations to place an order.
					<%}else if(!user.isDepotUser()){%>
				Please choose a delivery address for this order.
					<%}else{%>
				Where would you like to receive this order?
					<%}%>
			</td>
		</tr>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<% 
	String[] checkDlvAddressForm =  {EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(), "address",
	                                "pickup_contact_number", "order_minimum","apartment",
	                                "technical_difficulty"}; 
	%>
	
	<fd:ErrorHandler result='<%=result%>' field='<%=checkDlvAddressForm%>' id='errorMsg'>
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
	    <%@ include file="/includes/ckt_acct/i_delivery_address_select.jspf" %>
	    <br/><br/>
	
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	<img src="/media_stat/images/layout/dotted_line.gif" width="675" height="1" border="0"><br/>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	
	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top">
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				View Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Delivery Time<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0">
			</td>
	    </tr>
	</table>
	
	<%
	if(ClickToCallUtil.evaluateClick2CallInfoDisplay(user,null)) {
	%>
		<%@ include file="/checkout/includes/i_click2call_footer_text.jspf" %>
	<% } else { %>
		<%@ include file="/checkout/includes/i_footer_text.jspf" %>
	<% } %>
	</form>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="675" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>
