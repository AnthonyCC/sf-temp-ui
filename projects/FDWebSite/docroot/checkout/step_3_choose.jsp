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
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
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

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<%request.setAttribute("listPos", "SystemMessage,ProductNote,CategoryNote");%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Payment Information</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
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
%>
<fd:FDShoppingCart id='cart' result="sc_result">
<% if (!"skip".equalsIgnoreCase(request.getParameter("duplicateCheck"))) { %>
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%
{
	// redirect to Duplicate Order warning page if has another order for the same day
	String ignoreSaleId = null;
	if (cart instanceof FDModifyCartModel) {
		ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
	}

	Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
	if(!user.isAddressVerificationError()) {
    
        for (Iterator<FDOrderInfoI> hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
            FDOrderInfoI oi = hIter.next();
            if (!(oi.getErpSalesId().equals(ignoreSaleId))
                && oi.isPending()
                && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
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
        * If Gift card is used on the order Also calculate the 25% perishable buffer amount to decide if         * 
        * another mode of payment is needed.
    */    
    FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
    
    double gcSelectedBalance = user.getGiftcardBalance()- cart.getTotalAppliedGCAmount();
    double gcBufferAmount=0;
    double ccBufferAmount=0;
    double perishableBufferAmount = 0.0;
    double outStandingBalance = FDCustomerManager.getOutStandingBalance(cart);     
    perishableBufferAmount = FDCustomerManager.getPerishableBufferAmount((FDCartModel)cart);
    if(perishableBufferAmount > 0){
    	if(cart.getTotalAppliedGCAmount()> 0){
    		if(outStandingBalance >0){
        		/*if(gcSelectedBalance - perishableBufferAmount >=0){
        			gcBufferAmount = perishableBufferAmount;
        		}else{*/
        			gcBufferAmount = gcSelectedBalance;
        			ccBufferAmount = perishableBufferAmount - gcSelectedBalance;
        		//}
        	}
    		else{
    			gcBufferAmount = perishableBufferAmount;
    		}
    	}else{
    		ccBufferAmount = perishableBufferAmount;
    	}    	
    }
    
    boolean isPaymentRequired = true;
    if(cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0) {
          
        if(outStandingBalance <= 0.0) {
            isPaymentRequired = false;
%>
    <FORM method="post" name="step_3_choose" id="step_3_choose">

    <input type="hidden" name="actionName" id="actionName" value="setNoPaymentMethod">
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
		<TR VALIGN="TOP">
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<img src="/media_stat/images/navigation/payment_info_top.gif" WIDTH="107" HEIGHT="16" border="0" alt="DELIVERY ADDRESS">
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10">
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left" style="color:#666666;font-weight:bold;">Delivery Charge:</td>
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
							<td align="left" style="color:#666666;font-weight:bold;"><A HREF="javascript:popup('/help/estimated_price.jsp','small')"></A>Estimated Total:</td>
							<td align="right" style="color:#666666;font-weight:bold;"><%= currencyFormatter.format(cart.getTotal()) %></td>
						</tr>
					</table>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
			<FONT CLASS="space2pix"><BR></FONT>
			<input type="image" name="next_step" src="/media_stat/images/buttons/checkout_right.gif"
			 BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onClick="setActionName(this.form,'setPaymentMethod')" width="26" height="26">
		</TD>
	</TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE border="0" cellspacing="0" cellpadding="0" width="693">
	 <tr valign="top"> 
		 <td class="text12" width="415" valign="bottom"> 
			<FONT CLASS="title18">Choose Payment Information (Step 3 of 4)</FONT><BR>
			<FONT CLASS="text12">Please select a payment option.</FONT>
		</td>
	</tr>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="3" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
    
    <div style="width: 685px; text-align: left;"><span style="color: #FF9933;font-size: 17px;">NO PAYMENT NECESSARY... YOUR TOTAL IS COVERED BY YOUR GIFT CARD.</span>
    <p align="left">Good news: you do not need to select a Payment Method because the Gift Card you applied to this order is greater than 
    the order total, including any estimated prices. Please <a href="#" onclick="document.getElementById('step_3_choose').submit();">click here</a> to go to Step 4: Review & Submit, or click the orange 
    Continue Checkout button. Enjoy your free food!</p></div>


        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
        <IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

    <table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top">
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Time<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="alt_next_step" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setNoPaymentMethod')"><br/>Submit Order<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="alt_next_step" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setNoPaymentMethod')">
			</td>
	    </tr>
	</table>
</FORM>
 <%
    } }
 %>   
    
<%

if(isPaymentRequired) {
	Collection<ErpPaymentMethodI> paymentMethods = null;
	FDIdentity identity = null;
	boolean isECheckRestricted = false;
	if(user!=null && user.getIdentity()!=null) {
		identity = user.getIdentity();
		paymentMethods = FDCustomerManager.getPaymentMethods(identity);    
		isECheckRestricted = FDCustomerManager.isECheckRestricted(identity); 
	}

		boolean hasCreditCard = false;
		boolean hasCheck = false;

		if (paymentMethods!=null && paymentMethods.size() > 0){
			ArrayList<ErpPaymentMethodI> payMethodsList = new ArrayList<ErpPaymentMethodI>(paymentMethods);
			for(ListIterator saItr=payMethodsList.listIterator();saItr.hasNext();) {
				ErpPaymentMethodI paymentM = (ErpPaymentMethodI)saItr.next();
				if (EnumPaymentMethodType.CREDITCARD.equals(paymentM.getPaymentMethodType())) {
					hasCreditCard = true;
				} else if (EnumPaymentMethodType.ECHECK.equals(paymentM.getPaymentMethodType())) {
					hasCheck = true;
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

	<%@ include file="/includes/i_modifyorder.jspf" %>
	
	<fd:ErrorHandler result='<%=result%>' name="payment_method" id='errorMsg'>
			<%@ include file="/includes/i_error_messages.jspf" %>	
	</fd:ErrorHandler>	
	
	<!--<% if (isCheckEligible && !isECheckRestricted && !hasCreditCard) { // move msg to sys msg list when ready%>
		<% String errorMsg = "You must have a valid credit card on your FreshDirect account to pay for your order from a checking account. To proceed with Checkout, please review and revise your credit card information as necessary."; %>
			<%@ include file="/includes/i_error_messages.jspf" %>	
	<% } %>
	-->
	<%
		if(cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0 && gcBufferAmount >0 && ccBufferAmount > 0) {
	%>
		<div style="width: 685px; text-align: left;"><strong>PLEASE NOTE: A BACKUP PAYMENT METHOD IS REQUIRED FOR THIS ORDER.</strong> Because your Estimated Total is close to the balance of the Gift Card you entered, we require a second form of payment. This is to cover changes in price that may occur when we weigh your perishable items and fulfill your order. We guarantee that you'll always pay the true price for the actual weight of your products. <a href="javascript:popup('/help/estimated_price.jsp','small')">Learn about Estimated Totals</a>.</div>
		<br />
		<img width="693" vspace="3" height="1" border="0" src="/media_stat/images/layout/cccccc.gif" /><br /><br />

	<% } %>

<FORM method="post" name="step_3_choose" id="step_3_choose" onSubmit="return checkPromoEligibilityByPayment('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');" action="/checkout/step_3_choose.jsp?duplicateCheck=skip">
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
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
	
		<input type="hidden" name="actionName" value="">
		<input type="hidden" name="deletePaymentId" value="">
		<TR VALIGN="TOP">
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<img src="/media_stat/images/navigation/payment_info_top.gif" WIDTH="107" HEIGHT="16" border="0" alt="DELIVERY ADDRESS">
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10" >
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left" style="color:#666666;font-weight:bold;"><%= (isDepotAddress && depotAddress.isPickup())?"Service":"Delivery" %> Charge:</td>
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
							<td align="left" style="color:#666666;font-weight:bold;"><A HREF="javascript:popup('/help/estimated_price.jsp','small')"></A>Estimated Total:</td>
							<td align="right" style="color:#666666;font-weight:bold;"><%= currencyFormatter.format(cart.getTotal()) %></td>
						</tr>
					</table>
		</TD>		
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE">
			<FONT CLASS="space2pix"><BR></FONT>
			<input type="image" name="next_step" src="/media_stat/images/buttons/checkout_right.gif"
			 BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onClick="setActionName(this.form,'setPaymentMethod')" width="26" height="26">
		</TD>
	</TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE border="0" cellspacing="0" cellpadding="0" width="693">
	 <tr valign="top"> 
		 <td class="text12" width="415" valign="bottom"> 
			<FONT CLASS="title18">Choose Payment Information (Step 3 of 4)</FONT><BR>
			<FONT CLASS="text12">Please select a payment option.</FONT>
		</td>
	</tr>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="3" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<%
	JspMethods.dumpErrors(result);
	%>
		<% String[] checkPaymentForm = {"system", "order_minimum", "payment_inadequate", "technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum"}; %>
		<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentForm%>' id='errorMsg'>
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

		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
		<TR VALIGN="TOP">
			<TD CLASS="text12" WIDTH="693">
			<% if ("bnl".equalsIgnoreCase(user.getDepotCode())) {%>
				<b>If you are using a billing address on the Brookhaven Labs Campus that is different from your home address, please call FreshDirect at <%= user.getCustomerServiceContact() %> to assist you in entering the information.</b><br><br>
			<% } %>

					<% if(user.getOrderHistory().getTotalOrderCount() < 3 ) {%>
						<b>If you are paying by credit card please note that your credit card is not charged until after delivery.</b> 
						When you place your order we request <b>credit/debit card authorization</b> for 125% of the estimated price of items priced by weight (e.g. a steak or an apple) 
						and 100% of the price of fixed priced items (e.g. a box of cereal). On the day of delivery we assemble your order and weigh each item to determine your final price. 
						<b>Your card will be charged this final price after delivery.</b>
						<BR><BR>
					<% } else{%>
						Remember, you will not be charged until we have prepared your order and it is ready to go.<BR><BR>
					<%}%>

			To learn more about our <b>Security Policies</b>, <A HREF="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</A>.<BR>
			
			<%if(!isDepotAddress && user.isEligibleForSignupPromotion()){%>
				<br><font class="text11rbold">NOTE: On a home delivery order, to receive the free food promotion, your billing and delivery address must match.</font>
			<%}%>
			</TD>
		</TR>
		</TABLE><BR>
		
		<SCRIPT LANGUAGE=JavaScript>
			<!--
			OAS_AD('CategoryNote');
			//-->
		</SCRIPT>
		
		<% if (isCheckEligible && !isECheckRestricted) { // show checking acct selections %>
		<% if (hasCheck) { %>
		<table width="693" border="0" cellspacing="0" cellpadding="2">
			<tr valign="top">
			<td><img src="/media_stat/images/headers/check_acct_details.gif" width="181" height="9" alt="CHECKING ACCOUNT DETAILS"><br>
			<IMG src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" VSPACE="3"><br></td>
			</tr>
			<tr valign="middle">
			<td class="text12">If you need to enter another checking account: <a href="/checkout/step_3_checkacct_add.jsp"><IMG src="/media_stat/images/buttons/add_new_acct.gif" width="108" height="16" ALT="Add New Checking Account" border="0" ALIGN="absmiddle"></a>
			</td>
			</tr>
		</table>
			<br>
			<%@ include file="/includes/ckt_acct/checkacct_select.jspf" %>
		<% } else { %>
			<%@ include file="/includes/your_account/add_checkacct.jspf"%>
		<% } %>
		<br><br>
		<% } %>
	<%
		if (user.getFailedAuthorizations() > 0) { 
			errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br>Please choose or add a new payment method.<br><br>If you have tried this and are still experiencing problems, please do not attempt to submit your information again but contact us as soon as possible at" + user.getCustomerServiceContact() + ". A customer service representative will help you to complete your order.</span>";
		%>
			<%@ include file="/includes/i_error_messages.jspf" %>
	<% } %>
		<BR>


<%
if(user.isAddressVerificationError()) {
       
%>    
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
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
    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
</table>
<br>
<% 
//clear info from session.
user.setAddressVerificationError(false);
}
%>	


		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="693">
		<TR VALIGN="TOP">
		<TD WIDTH="693"><img src="/media_stat/images/navigation/choose_credit_card.gif" WIDTH="135" HEIGHT="9" border="0" alt="CHOOSE CREDIT CARD">&nbsp;&nbsp;&nbsp;<BR>
			<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
			<FONT CLASS="space2pix"><BR></FONT>
			<span CLASS="text12">If you need to enter another credit card: </span><a href="/checkout/step_3_card_add.jsp"><img ALT="Add New Card" src="/media_stat/images/buttons/add_new_card.gif" WIDTH="96" HEIGHT="16" border="0" VSPACE="3" HSPACE="5" ALIGN="middle"></a></TD>
		</TR>
		</TABLE><br>
		<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %><BR><BR>
			<%
			if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())){%>
				<%@ include file="/checkout/includes/i_billing_ref.jspf" %>
				<BR><BR><BR><BR>
			<%
			}%>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top">
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Time<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="alt_next_step" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setPaymentMethod')"><br/>Submit Order<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="alt_next_step" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0" onClick="setActionName(this.form,'setPaymentMethod')">
			</td>
	    </tr>
	</table>

		<%@ include file="/checkout/includes/i_footer_text.jspf" %>
	</FORM>
<% } %>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="675" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
