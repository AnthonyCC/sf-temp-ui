<%@ page import="java.util.*"%>
<%@ page import='java.util.Enumeration' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.Collection' %>
<%@ page import='java.util.List' %>
<%@ page import='java.util.ArrayList' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.customer.FDCartModel'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_3_CHOOSE_TOTAL = 970;
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<%request.setAttribute("listPos", "SystemMessage,ProductNote,CategoryNote");%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="PAYMENT INFO"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Choose Payment Information</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<style type="text/css">
	td.chooser_radio {
		text-align: center;
	}
	td.chooser_radio input {
		margin: 0px;
		padding: 0px;
	}
</style>
<%
	String _errorMsg="";
	String actionName = request.getParameter("actionName");
	if (actionName==null)
		actionName = "noAction";
	boolean isSetPayment = false;
	boolean isDeleteCC = false;
	if ( "".equals(actionName) ) {
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
			String n = (String) e.nextElement();
			if ( n.startsWith("next_step") ) {
				isSetPayment = true;
				actionName = "setPaymentMethod";
				break;
			}
            if ( n.startsWith("alt_next_step") ) {
				isSetPayment = true;
				actionName = "setNoPaymentMethod";
				break;
			}
		}
		if (!isSetPayment) {
			for (Enumeration<String> e2 = request.getParameterNames(); e2.hasMoreElements(); ) {
				String n = (String) e2.nextElement();
				if ( n.startsWith("delete_cc") ) {
					isDeleteCC = true;
					actionName = "deletePaymentMethod";
					break;
				}
			}
		}
	}

	String successPage = "";
	if ("setPaymentMethod".equalsIgnoreCase(actionName) || "setNoPaymentMethod".equalsIgnoreCase(actionName)) {
	    successPage = "/checkout/step_4_submit.jsp";
	} else if ("deletePaymentMethod".equalsIgnoreCase(actionName)) {
    	successPage = "/checkout/step_3_choose.jsp";
	}

	// [APPDEV-2149] SO template only checkout => no order, no dlv timeslot, no giftcard magic
	//
	final boolean isSOTMPL = EnumCheckoutMode.MODIFY_SO_TMPL.equals( user.getCheckoutMode() );

	//button include count
	int incNextButtonCount = 0;
%>
<fd:FDShoppingCart id='cart' result="sc_result">
<% if (!"skip".equalsIgnoreCase(request.getParameter("duplicateCheck")) && !isSOTMPL) { %>
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%
{
	// redirect to Duplicate Order warning page if has another order for the same day
	String ignoreSaleId = null;
	if (cart instanceof FDModifyCartModel) {
		ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
		if(EnumSaleStatus.AUTHORIZATION_FAILED.equals(((FDModifyCartModel) cart).getOriginalOrder().getOrderStatus())) {
		   _errorMsg= PaymentMethodUtil.getAuthFailErrorMessage(((FDModifyCartModel) cart).getOriginalOrder().getAuthFailDescription());
		   System.out.println("_errorMsg"+_errorMsg);
		}
	}

	Date currentDlvStart = null;
	if(cart.getDeliveryReservation()!=null)
		currentDlvStart=DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
	Calendar now = Calendar.getInstance();
	if(!user.isAddressVerificationError()) {

        for (Iterator<FDOrderInfoI> hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
            FDOrderInfoI oi = hIter.next();
            if (!(oi.getErpSalesId().equals(ignoreSaleId))
                && oi.isPending()
                && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime())) && now.getTime().before(oi.getDeliveryCutoffTime())) {
                response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_duplicate.jsp?successPage=/checkout/step_3_choose.jsp"));
                return;
            }
        }
    }
}
%>
</fd:OrderHistoryInfo>
<% } %>
<fd:CheckoutController actionName='<%=actionName%>' result='result' successPage='<%=successPage%>' >
<%
    /*
        * Apply Customer credit -- This is done here for knowing the final order amount before displaying
        * the payment selection
        * If Gift card is used on the order Also calculate the 25% perishable buffer amount to decide if
        * another mode of payment is needed.
    */
    if (!isSOTMPL)
	    FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());

	double gcSelectedBalance = isSOTMPL ? 0 : user.getGiftcardBalance()- cart.getTotalAppliedGCAmount();
	double gcBufferAmount=0;
	double ccBufferAmount=0;
	double perishableBufferAmount = isSOTMPL ? 0 : FDCustomerManager.getPerishableBufferAmount((FDCartModel)cart);
	double outStandingBalance = isSOTMPL ? 0 : FDCustomerManager.getOutStandingBalance(cart);
	boolean isPaymentRequired = true;

	if(!isSOTMPL && perishableBufferAmount > 0){
    	if(cart.getTotalAppliedGCAmount()> 0){
    		if(outStandingBalance >0){
        		/*if(gcSelectedBalance - perishableBufferAmount >=0){
        			gcBufferAmount = perishableBufferAmount;
        		}else{*/
        			gcBufferAmount = gcSelectedBalance;
        			ccBufferAmount = perishableBufferAmount - gcSelectedBalance;
        		//}
        	} else {
    			gcBufferAmount = perishableBufferAmount;
    		}
    	}else{
    		ccBufferAmount = perishableBufferAmount;
    	}
    }

    /* determine form tag to use so we can include the header section once here */
    if(!isSOTMPL && cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0 && outStandingBalance <= 0.0) {
        /* GC-only form */
    	%>
    	<form method="post" name="step_3_choose" id="step_3_choose"><%
    } else {
    	/* nonGC-only form */
    	if (isSOTMPL) {%>
			<form method="post" name="step_3_choose" id="step_3_choose">
		<% } else { %>
			<form method="post" name="step_3_choose" id="step_3_choose" onSubmit="return checkPromoEligibilityByPayment('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');" action="/checkout/step_3_choose.jsp?duplicateCheck=skip">
		<% }
    }

    %>
	<%-- Start Header --%>
<%@ include file="/includes/i_modifyorder.jspf"  %>
<tmpl:insert template='<%= ((modifyOrderMode) ? "/includes/checkout_header_modify.jsp" : "/includes/checkout_header.jsp") %>'>
<% if(modifyOrderMode) { %>
	<tmpl:put name="ordnumb"><%= modifiedOrderNumber %></tmpl:put>
	<tmpl:put name="note"><%= modifyNote %></tmpl:put>
<% } %>
<%-- 	<tmpl:put name="title">PAYMENT INFO</tmpl:put> --%>
	<tmpl:put name="delivery-fee"><%@ include file="/includes/i_cart_delivery_fee.jspf" %></tmpl:put>
	<%
		//next button needs to know if it's GC covered

		if(!isSOTMPL && cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0) {
    		/* using Giftcards */
        	if(outStandingBalance <= 0.0) {
				request.setAttribute("gcCovered", "true");
        	}
		}
	%>
	<tmpl:put name="next-button"><%@ include file="/includes/i_cart_next_step_button.jspf" %></tmpl:put>
</tmpl:insert>
<!-- PROFILE HEADER -->
    <%


    if(!isSOTMPL && cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0) {
    	/* using Giftcards */
        if(outStandingBalance <= 0.0) {
            /* No additional payment type is needed, covered by Giftcards */
            isPaymentRequired = false;
		%>

		    <input type="hidden" name="actionName" id="actionName" value="setNoPaymentMethod" />

			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
			<!-- PROFILE HEADER -->
			<% if(!modifyOrderMode) { %>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />
			<% } %>
			<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
			<img src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="16" border="0" alt="" /><br />

			<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
				 <tr valign="top">
					 <td class="text12" width="415" valign="bottom">
						<span class="title18">Choose Payment Information (Step 3 of 4)</span><br />
						<span class="text12">Please select a payment option.</span>
					</td>
				</tr>
			</table>

			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
			<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="3" border="0" alt="" /><br />
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />

		    <div style="width: <%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>px; text-align: left;"><span style="color: #FF9933;font-size: 17px;">NO PAYMENT NECESSARY... YOUR TOTAL IS COVERED BY YOUR GIFT CARD.</span>
		    <p align="left">Good news: you do not need to select a Payment Method because the Gift Card you applied to this order is greater than
		    the order total, including any estimated prices. Please <a href="#" onclick="document.getElementById('step_3_choose').submit();">click here</a> to go to Step 4: Review & Submit, or click the orange
		    Continue Checkout button. Enjoy your free food!</p></div>


			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	        <img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="1" border="0" alt="" /><br />
            <img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />

		    <table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
			    <tr valign="top">
					<td width="35">
						<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
						<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
					</td>
				    <td width="340" style="text-align: left;">
						<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
						<img src="/media_stat/images/buttons/previous_step.gif" width="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br />
						Delivery Time<br />
						<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0" alt="" />
					</td>
					<td width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL-410%>" align="right" valign="middle">
						<input type="image" name="alt_next_step" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setNoPaymentMethod')"><br />Review Order<br />
					</td>
					<td width="35" align="right" valign="middle">
						<input type="image" name="alt_next_step" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setNoPaymentMethod')">
					</td>
			    </tr>
			</table>
 		<%
    	}
	}

if (isPaymentRequired) {
	Collection<ErpPaymentMethodI> paymentMethods = null;
	FDIdentity identity = null;
	boolean isECheckRestricted = false;
	if(user!=null && user.getIdentity()!=null) {
		identity = user.getIdentity();
		paymentMethods = FDCustomerManager.getPaymentMethods(identity);

		if(paymentMethods != null && !paymentMethods.isEmpty()){
			List<ErpPaymentMethodI> paymentsNew = new ArrayList<ErpPaymentMethodI>();
			Iterator payItr = paymentMethods.iterator();
			while (payItr.hasNext()) {
		  		ErpPaymentMethodI  paymentM = (ErpPaymentMethodI) payItr.next();
		  		if (paymentM.geteWalletID() == null) {
		 			paymentsNew.add(paymentM);
		        }
			}
			paymentMethods = paymentsNew;
		}

		isECheckRestricted = FDCustomerManager.isECheckRestricted(identity);
	}

		boolean hasCreditCard = false;
		boolean hasCheck = false;
		boolean hasEBTCard=false;

		if (paymentMethods!=null && paymentMethods.size() > 0){
			ArrayList<ErpPaymentMethodI> payMethodsList = new ArrayList<ErpPaymentMethodI>(paymentMethods);
			for(ListIterator saItr=payMethodsList.listIterator();saItr.hasNext();) {
				ErpPaymentMethodI paymentM = (ErpPaymentMethodI)saItr.next();
				if (EnumPaymentMethodType.CREDITCARD.equals(paymentM.getPaymentMethodType())) {
					hasCreditCard = true;
				} else if (EnumPaymentMethodType.ECHECK.equals(paymentM.getPaymentMethodType())) {
					hasCheck = true;
				}	else if (EnumPaymentMethodType.EBT.equals(paymentM.getPaymentMethodType())) {
					hasEBTCard = true;
				}
			}
		}

		boolean isCheckEligible	= user.isCheckEligible();

		boolean isDepotAddress = false;
		ErpDepotAddressModel depotAddress = null;

		if(user.getShoppingCart().getDeliveryAddress() instanceof ErpDepotAddressModel){
				isDepotAddress = true;
				depotAddress =  (ErpDepotAddressModel) user.getShoppingCart().getDeliveryAddress();
		}

	%>
<%
	JspMethods.dumpErrors(result);
	%>

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
					The promotion code you entered <div id="promoCode"></div> is not valid for the payment method you selected. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
					<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<a href="#" onclick="Modalbox.hide(); return false;">CHOOSE ANOTHER</a><br />
					<a href="#" onclick="javascript:document.forms['step_3_choose'].submit();"><b>CONTINUE</b></a><br />
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

	<input type="hidden" name="actionName" value="">
	<input type="hidden" name="deletePaymentId" value="">

	<!-- PROFILE HEADER -->
	<% if(!modifyOrderMode) { %>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />
	<% } %>
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />

	<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
		<tr valign="top">
			 <td class="text12" width="415" valign="bottom">
				<font class="title18">Choose Payment Information (Step 3 of 4)</font><br />
				<font class="text12">Please select a payment option.</font>
			</td>
		</tr>
	</table>

	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="3" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<%
		JspMethods.dumpErrors(result);
	%>
	<% String[] checkPaymentForm = {"system", "order_minimum", "payment_inadequate", "payment_method","technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum","ebtPaymentNotAllowed"}; %>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentForm%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' name="payment_method" id='errorMsg'>
			<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<!--<% if (isCheckEligible && !isECheckRestricted && !hasCreditCard) { // move msg to sys msg list when ready%>
		<% String errorMsg = "You must have a valid credit card on your FreshDirect account to pay for your order from a checking account. To proceed with Checkout, please review and revise your credit card information as necessary."; %>
			<%@ include file="/includes/i_error_messages.jspf" %>
	<% } %>
	-->
	<%
		if (cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0 && gcBufferAmount > 0 && ccBufferAmount > 0) {
	%>
		<div style="width: <%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>px; text-align: left;"><strong>PLEASE NOTE: A BACKUP PAYMENT METHOD IS REQUIRED FOR THIS ORDER.</strong> Because your Estimated Total is close to the balance of the Gift Card you entered, we require a second form of payment. This is to cover changes in price that may occur when we weigh your perishable items and fulfill your order. We guarantee that you'll always pay the true price for the actual weight of your products. <a href="javascript:popup('/help/estimated_price.jsp','small')">Learn about Estimated Totals</a>.</div>
		<br />
		<img width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" vspace="3" height="1" border="0" alt="" src="/media_stat/images/layout/cccccc.gif" /><br /><br />

	<% } %>

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

	<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
		<tr valign="top">
			<td class="text12" width="<%= W_CHECKOUT_STEP_3_CHOOSE_TOTAL %>">
			<% if ("bnl".equalsIgnoreCase(user.getDepotCode())) { %>
				<b>If you are using a billing address on the Brookhaven Labs Campus that is different from your home address, please call FreshDirect at <%= user.getCustomerServiceContact() %> to assist you in entering the information.</b><br><br>
			<% } %>
			<% if (user.getOrderHistory().getTotalOrderCount() < 3 ) { %>
				<b>If you are paying by credit card please note that your credit card is not charged until after delivery.</b>
				When you place your order we request <b>credit/debit card authorization</b> for 125% of the estimated price of items priced by weight (e.g. a steak or an apple)
				and 100% of the price of fixed priced items (e.g. a box of cereal). On the day of delivery we assemble your order and weigh each item to determine your final price.
				<b>Your card will be charged this final price after delivery.</b>
				<br /><br />
			<% } else{ %>
				Remember, you will not be charged until we have prepared your order and it is ready to go.<br /><br />
			<% } %>

			To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here<span class="offscreen">To learn more about our Security Policies</span></a>.<br />

			<%if(!isDepotAddress && user.isEligibleForSignupPromotion()){%>
				<br><font class="text11rbold">NOTE: On a home delivery order, to receive the free food promotion, your billing and delivery address must match.</font>
			<%}%>
			</td>
		</tr>
	</table>
	<br />

  <div id='oas_CategoryNote'>
    <script type="text/javascript">
  		OAS_AD('CategoryNote');
  	</script>
  </div>

<fd:GetStandingOrderDependencyIds id="standingOrderDependencyIds" type="paymentMethod">
<fd:GetStandingOrderHelpInfo id="helpSoInfo">
	<script type="text/javascript">var helpSoInfo=<%=helpSoInfo%>;</script>
	<% if (isCheckEligible && !isECheckRestricted) { // show checking acct selections %>
		<% if (hasCheck) { %>
			<table width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" border="0" cellspacing="0" cellpadding="0">
				<tr valign="top">
					<td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS"><br>
						<img src="/media_stat/images/layout/999966.gif" alt="" width="970" height="1" border="0" VSPACE="3"><br></td>
				</tr>
				<tr valign="middle">
					<td class="text12">If you need to enter another checking account: <a href="/checkout/step_3_checkacct_add.jsp"><img src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" alt="Add New Checking Account" border="0" align="absmiddle" /></a></td>
				</tr>
			</table>
			<br />
			<%@ include file="/includes/ckt_acct/checkacct_select.jspf" %>
		<% } else { %>
			<%@ include file="/includes/your_account/add_checkacct.jspf"%>
		<% } %>
		<br />
	<% } %>
	<%

	if (session.getAttribute("authFailMessage")!=null) {
		errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br>Please choose or add a new payment method.<br><br>If you have tried this and are still experiencing problems, please do not attempt to submit your information again but contact us as soon as possible at" + user.getCustomerServiceContact() + ". A customer service representative will help you to complete your order.</span>";
		errorMsg=(String) session.getAttribute("authFailMessage");;
	%>
		<%@ include file="/includes/i_error_messages.jspf" %>
<% session.removeAttribute("authFailMessage");} %>

	<br />
<%
	if(user.isAddressVerificationError()) {
%>
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
			    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
			    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0" alt="" /></td>
			    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
			    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0" alt="" /></td>
			    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
			</tr>
			<tr>
			    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
			    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
			</tr>
			<tr>
			    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
			    <td class="text11rbold" width="100%" bgcolor="#FFFFFF">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
					<%= user.getAddressVerficationMsg() %>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				</td>
			    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
			    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
			</tr>
			<tr>
			    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0" alt="" /></td>
			    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
			    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0" alt="" /></td>
			</tr>
			<tr>
			    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
			</tr>
		</table>
		<br />
	<%
		//clear info from session.
		user.setAddressVerificationError(false);
	}
%>


	<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
		<tr valign="top">
		<td width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>"><img src="/media_stat/images/navigation/choose_credit_card.gif" width="135" height="9" border="0" alt="CHOOSE CREDIT CARD" />&nbsp;&nbsp;&nbsp;<br />
			<img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="1" border="0" vspace="3"><br />
			<font class="space2pix"><br /></font>
			<span class="text12">If you need to enter another credit card: </span><a href="/checkout/step_3_card_add.jsp"><img alt="Add New Card" src="/media_stat/images/buttons/add_new_credit_card.jpg" width="137" height="16" border="0" vspace="3" hspace="5" align="middle" /></a></TD>
		</tr>
		</table><br>
		<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %><br /><br />
			<% if(user.isEbtAccepted()||hasEBTCard){ %>
			<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>">
		<tr valign="top">
		<td width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>"><img src="/media_stat/images/navigation/choose_ebt_card.gif" width="118" HEIGHT="11" border="0" alt="CHOOSE EBT CARD" />&nbsp;&nbsp;&nbsp;<br />
			<img src="/media_stat/images/layout/999966.gif" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="1" border="0" vspace="3" alt="" /><br />
			<font class="space2pix"><br /></font>
			<%if(user.isEbtAccepted()){ %>
			<span class="text12">If you need to enter another EBT card: </span><a href="/checkout/step_3_ebt_add.jsp"><img ALT="Add New Card" src="/media_stat/images/buttons/add_new_ebt_card.jpg" width="117" height="16" border="0" vspace="3" hspace="5" align="middle" /></a></TD>
			<% } %>
		</tr>
		</table>
		<%@ include file="/includes/ckt_acct/i_ebtcard_select.jspf" %><br /><br />
		<% } %>
			<%
			if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())){%>
				<%@ include file="/checkout/includes/i_billing_ref.jspf" %>
				<br /><br />
			<%	 _errorMsg="";

			}%>

</fd:GetStandingOrderHelpInfo>
</fd:GetStandingOrderDependencyIds>

	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="1" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />

	<div style="margin-bottom: 10px;">
		<div style="float: left;">
			<a href="<%=response.encodeURL("/checkout/step_2_select.jsp")%>" id="previousX">
			<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP" /></a>
		</div>
		<div style="float: left; margin-left: 5px; text-align: left;">
			<a href="<%=response.encodeURL("/checkout/step_2_select.jsp")%>" id="cancelText">
			<img src="/media_stat/images/buttons/previous_step.gif" width="66" height="11" border="0" alt="PREVIOUS STEP" /></a><br />
			Delivery Time
		</div>
		<div style="float: right;">
			<% if(modifyOrderMode) { %><a class="imgButtonWhite cancel_updates" href="/your_account/cancel_modify_order.jsp">cancel updates</a><% } %><%@ include file="/includes/i_cart_next_step_button.jspf" %>
		</div>
		<div style="clear: both;"></div>
	</div>

		<%@ include file="/checkout/includes/i_footer_text.jspf" %>

<%
	}

	/* always end the form tag */ %>
	</form>

	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_3_CHOOSE_TOTAL%>" height="1" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
