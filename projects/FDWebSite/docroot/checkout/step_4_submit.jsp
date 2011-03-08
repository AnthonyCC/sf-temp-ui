<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import="java.util.Locale"%>  
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>


<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<%
	String actionName = "submitOrder";
%>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Review & Submit Order</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:FDShoppingCart id='cart' result="result">

<fd:CheckoutController actionName="<%= actionName %>" result="result" successPage="step_4_receipt.jsp">
<%
        FDIdentity identity  = user.getIdentity();
        ErpAddressModel dlvAddress = cart.getDeliveryAddress();
        ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
        ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(identity);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
        FDReservation reservation = cart.getDeliveryReservation();
        String fmtDlvDateTime = dateFormatter.format(reservation.getStartTime()).toUpperCase();
		String deliveryTime = FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime());

		boolean orderAmountFraud = result.hasError("order_amount_fraud");
		boolean doubleSubmit = result.hasError("processing_order");
		
		// Save checkout mode for receipt page.
		session.setAttribute("checkout_mode", user.getCheckoutMode().toString());
%>
<fd:SmartSavingsUpdate promoConflictMode="true"/>


<%@ include file="/includes/i_modifyorder.jspf" %>


<div class="groupScaleBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td>
					<%-- all your content goes in this div, it controls the height/width --%>
					<div id="group_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="" /><br />
					</div>
				</td>
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


<form method="post" name="order_submit" id="order_submit" onSubmit="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');">
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
					The promotion code you entered <div id="promoCode"></div> has been redeemed by the maximum number of customers and is no longer available. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
					<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>					
					<a href="#" onclick="javascript:document.forms['order_submit'].submit();"><b>CONTINUE</b></a><br />
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
<table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="693">
	<tr VALIGN="TOP">
	<td CLASS="text11" width="415" VALIGN="bottom">
		<FONT CLASS="title18">Review &amp; Submit Order (Final Step)</FONT><BR>
		<FONT CLASS="text12">Please confirm all of your order details. After submitting your order you will immediately receive an e-mail confirmation.</font></td>
	<td width="245" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Top');" id="checkout_submit_order_topText"><% } %><BR>
</td>
<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Top');" id="checkout_submit_order_topArrow"><% } %></td>
	</tr>
	</table>

	<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>
	<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/template/checkout/order_not_placed.gif" width="693" height="39" border="0" alt="Continue Checkout" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Banner');" id="checkout_submit_order_banner"><br><IMG src="/media_stat/images/layout/clear.gif" width="1" height="14" BORDER="0"><br><% } %>
    <%-- error system messages happen here --%>
   <% StringBuffer sbErrorMsg= new StringBuffer(); %>
    
    <fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'>
        <%
        sbErrorMsg.append("<br>");
		sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'>
        <% 
        sbErrorMsg.append("<br>Checkout prevented because:<br>");
        sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='order_amount_fraud' id='errorMsg'>
        <%
        sbErrorMsg.append("<br>");
        sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
		<% 
        sbErrorMsg.append(errorMsg);
        %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
		<br><span class="text11rbold"><%= errorMsg %></span><br><br>
    </fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='invalid_reservation' id='errorMsg'>
		<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='invalid_deliverypass' id='errorMsg'>
    	<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
    	<fd:ErrorHandler result='<%=result%>' name='redemption_exceeded' id='errorMsg'>
        <input type = "hidden" name="ignorePromoErrors" value="true"/>
    	<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
    
    
<%
if (user.isPromoConflictResolutionApplied()) {
    user.setPromoConflictResolutionApplied(false);                                    

    result.addWarning(new ActionWarning("promo_war2", SystemMessageList.MSG_PROMOTION_APPLIED_VARY2));
%>	<fd:ErrorHandler result='<%= result %>' name='promo_war2' id='errorMsg'>
<%@ include file="/includes/i_warning_messages.jspf" %>   
	</fd:ErrorHandler>
<%
}

        if (doubleSubmit) {
			sbErrorMsg.append(result.getError("processing_order").getDescription());
        }

		String warningMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
		if (warningMsg==null && user.isPromotionAddressMismatch()) {
			Promotion promo = (Promotion)user.getEligibleSignupPromotion();
			Double totalPromo = new Double(promo.getHeaderDiscountTotal());
		    warningMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, user.getCustomerServiceContact()});
		}
		if (warningMsg!=null) {
			sbErrorMsg.append(warningMsg);
		}

        if (sbErrorMsg.length() > 0 ) {
         String errorMsg = sbErrorMsg.toString();
	%>
	<%@ include file="/includes/i_error_messages.jspf"%> 	
<%	}
%>

<%@ include file="/includes/ckt_acct/i_step_4_delivery_payment.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"><br>
<IMG src="/media_stat/images/layout/ff9933.gif" width="693" height="1"><br>
<font class="space4pix"><br></font>
<table width="693" CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<tr VALIGN="TOP">
		<td width="658" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Middle');" id="checkout_submit_order_middleText"><% } %><BR>
		</td>
		<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Middle');" id="checkout_submit_order_middleArrow"><% } %></td>
	</tr>
</table>
<BR>
<table width="693" cellpadding="0" cellspacing="0" border="0">
	<tr VALIGN="TOP">
		<td width="693"><img src="/media_stat/images/navigation/cart_details.gif" width="83" height="9" border="0" alt="CART DETAILS">&nbsp;&nbsp;&nbsp;
<% if (request.getRequestURI().toLowerCase().indexOf("your_account/") != 1){ %>
<FONT CLASS="text9">If you would like to make any changes to your order, <A HREF="/view_cart.jsp?trk=chkplc">click here</A> to go back to your cart.</FONT><BR>
<% } %>
			<IMG src="/media_stat/images/layout/999966.gif" width="693" height="1" BORDER="0" VSPACE="3"><br>
			<IMG src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
			<font class="title11"><b>Note:</b></font> <font class="text11orbold">Our goal is to fill your order with food of the highest quality. Occasionally, we'll get a shipment that doesn't meet our standards and we cannot accept it. Of course, if this happens, FreshDirect will not charge you for the missing item.</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<%@ include file="/includes/ckt_acct/i_step_4_cart_details.jspf" %>
		</td>
	</tr>
</table>

<BR>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" width="693" height="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>
<table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="693">
	<tr VALIGN="TOP">
		<td width="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>" onclick="ntptEventTag('ev=button_event&amp;ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelX"><img src="/media_stat/images/buttons/x_green.gif" width="20" height="19" border="0" alt="CONTINUE SHOPPING"></a></td>
		<td width="375"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>" onclick="ntptEventTag('ev=button_event&amp;ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" height="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR>
			<IMG src="/media_stat/images/layout/clear.gif" width="340" height="1" BORDER="0">
		</td>
		<td width="265" ALIGN="RIGHT" VALIGN="MIDDLE"><% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Bottom');" id="checkout_submit_order_bottomText"><% } %><BR>
		</td>
		<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit ) {
%>			<input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onclick="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');return ntptSubmitTag(document.order_submit, 'ev=button_event&ni_btn=submit_order&ni_btnpos=Bottom');" id="checkout_submit_order_bottomArrow">
<% } %>
		</td>
	</tr>
</table>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
