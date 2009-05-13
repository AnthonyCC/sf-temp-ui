<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='import com.freshdirect.framework.webapp.*' %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Review & Submit Order</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:FDShoppingCart id='cart' result="result">

<fd:CheckoutController actionName="submitOrder" result="result" successPage="step_4_receipt.jsp">
<%
        FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
        FDIdentity identity  = user.getIdentity();
        ErpAddressModel dlvAddress = cart.getDeliveryAddress();
        ErpPaymentMethodI paymentMethod =(ErpPaymentMethodI) cart.getPaymentMethod();
        ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(identity);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
        FDReservation reservation = cart.getDeliveryReservation();
        String fmtDlvDateTime = dateFormatter.format(reservation.getStartTime()).toUpperCase();
		String deliveryTime = FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime());

		boolean orderAmountFraud = result.hasError("order_amount_fraud");
		boolean doubleSubmit = result.hasError("processing_order");

%>

<%
//Added for Smart Savings.
    Map savingsLookupTable = (Map) session.getAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
    if(savingsLookupTable == null){
        savingsLookupTable = new HashMap();
    }
    PromoVariantHelper.updateSavingsVariant(user, savingsLookupTable);
    String usrVariant = user.getSavingsVariantId();
    if(usrVariant != null && usrVariant.length() > 0) {
        PromoVariantHelper.updateSavingsVariantFound(user, 5);
    } else {
        user.setSavingsVariantFound(false);
    }
    session.setAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE, savingsLookupTable);
    String savingsVariant =  (String) session.getAttribute(SessionName.PREV_SAVINGS_VARIANT);
    Boolean prevVariantFound = (Boolean) session.getAttribute(SessionName.PREV_VARIANT_FOUND);
    boolean usrVariantFound = user.isSavingsVariantFound();
    if((usrVariant != null && !usrVariant.equals(savingsVariant)) || (prevVariantFound != null && usrVariantFound != prevVariantFound.booleanValue())) {
        //This flag need to be enabled to perform post conflict resolution.
        user.setPostPromoConflictEnabled(true);	
        //If current savings variant is different from previous savings variant
        user.updateUserState();
        session.setAttribute(SessionName.PREV_SAVINGS_VARIANT, usrVariant);
        session.setAttribute(SessionName.PREV_VARIANT_FOUND, new Boolean(usrVariantFound));
    }
%> 

<%@ include file="/includes/i_modifyorder.jspf" %>

<table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="693">
 <form method="post">
	<tr VALIGN="TOP">
	<td CLASS="text11" width="415" VALIGN="bottom">
		<FONT CLASS="title18">Review &amp; Submit Order (Final Step)</FONT><BR>
		<FONT CLASS="text12">Please confirm all of your order details. After submitting your order you will immediately receive an e-mail confirmation.</font></td>
	<td width="245" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0"><% } %><BR>
</td>
<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"><% } %></td>
	</tr>
	</table>

	<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>
	<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/template/checkout/order_not_placed.gif" width="693" height="39" border="0" alt="Continue Checkout"><br><IMG src="/media_stat/images/layout/clear.gif" width="1" height="14" BORDER="0"><br><% } %>
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
    
    
    
    <%
if(user.isPromoConflictResolutionApplied()){
StringBuffer buffer = new StringBuffer(
					SystemMessageList.MSG_PROMOTION_APPLIED_VARY2);
			result.addWarning(new ActionWarning("promo_war2", buffer
					.toString()));
                    
    user.setPromoConflictResolutionApplied(false);                                    
%>

<fd:ErrorHandler result='<%=result%>' name='promo_war2' id='errorMsg'>
    <%@ include file="/includes/i_warning_messages.jspf" %>   
</fd:ErrorHandler>
 
<%
  }
 %> 
    
<%

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
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0"><% } %><BR>
</td>
<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"><% } %></td>
</tr>
</table><BR>

<table width="693" cellpadding="0" cellspacing="0" border="0">
<tr VALIGN="TOP">
<td width="693"><img src="/media_stat/images/navigation/cart_details.gif" width="83" height="9" border="0" alt="CART DETAILS">&nbsp;&nbsp;&nbsp;
<% if (request.getRequestURI().toLowerCase().indexOf("your_account/") != 1){ %>
<FONT CLASS="text9">If you would like to make any changes to your order, <A HREF="/view_cart.jsp?trk=chkplc">click here</A> to go back to your cart.</FONT><BR>
<% } %>
<IMG src="/media_stat/images/layout/999966.gif" width="693" height="1" BORDER="0" VSPACE="3"><br><IMG src="/media_stat/images/layout/clear.gif" width="1" height="3"><br><font class="title11"><b>Note:</b></font> <font class="text11orbold">Our goal is to fill your order with food of the highest quality. Occasionally, we'll get a shipment that doesn't meet our standards and we cannot accept it. Of course, if this happens, FreshDirect will not charge you for the missing item.</font></td>
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
	<td width="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/x_green.gif" width="20" height="19" border="0" alt="CONTINUE SHOPPING"></a></td>
	<td width="375"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" height="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" width="340" height="1" BORDER="0"></td>
	<td width="265" ALIGN="RIGHT" VALIGN="MIDDLE"><% if (!orderAmountFraud && !doubleSubmit) { %><input type="image" name="checkout_submit_order" src="/media_stat/images/buttons/submit_order_type.gif" width="113" height="34" alt="Your order will not be placed until you click SUBMIT ORDER" VSPACE="2" border="0"><% } %><BR>
</td>
<td width="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
<% if (!orderAmountFraud && !doubleSubmit ) { %><input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif"
BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"><% } %></td>
	</tr>
	</table>
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
