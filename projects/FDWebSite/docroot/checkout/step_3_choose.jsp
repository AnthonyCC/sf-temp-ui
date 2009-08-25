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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>

<%request.setAttribute("listPos", "SystemMessage,ProductNote,CategoryNote");%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

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
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
			String n = (String) e.nextElement();
			if ( n.startsWith("next_step") ) {
				isSetPayment = true;
				actionName = "setPaymentMethod";
				break;
			}
		}
		if (!isSetPayment) {
			for (Enumeration e2 = request.getParameterNames(); e2.hasMoreElements(); ) {
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
	if ("setPaymentMethod".equalsIgnoreCase(actionName)) {
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
	
	for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
		FDOrderInfoI oi = (FDOrderInfoI) hIter.next();
		if (!(oi.getErpSalesId().equals(ignoreSaleId))
			&& oi.isPending()
			&& currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
			response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_duplicate.jsp?successPage=/checkout/step_3_choose.jsp"));
			return;
		}
	}
}
%>
</fd:OrderHistoryInfo>
<% } %>

<fd:CheckoutController actionName='<%=actionName%>' result='result' successPage='<%=successPage%>' >
<%
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
Collection paymentMethods = null;
FDIdentity identity = null;
boolean isECheckRestricted = false;
if(user!=null  && user.getIdentity()!=null) {
    identity = user.getIdentity();
    paymentMethods = FDCustomerManager.getPaymentMethods(identity);    
    isECheckRestricted = FDCustomerManager.isECheckRestricted(identity); 
}

	boolean hasCreditCard = false;
	boolean hasCheck = false;

	if (paymentMethods!=null && paymentMethods.size() > 0){
		ArrayList payMethodsList = new ArrayList(paymentMethods);
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

<% if (isCheckEligible && !isECheckRestricted && !hasCreditCard) { // move msg to sys msg list when ready%>
	<% String errorMsg = "You must have a valid credit card on your FreshDirect account to pay for your order from a checking account. To proceed with Checkout, please review and revise your credit card information as necessary."; %>
		<%@ include file="/includes/i_error_messages.jspf" %>	
<% } %>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
<FORM method="post" name="step_3_choose">
    <input type="hidden" name="actionName" value="">
	<input type="hidden" name="deletePaymentId" value="">
	<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="415" VALIGN="bottom">
		<FONT CLASS="title18">Choose Payment Information (Step 3 of 4)</FONT><BR>
		<FONT CLASS="text12">Please select a payment option.</FONT>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="245" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><input type="image" name="next_step" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0" onClick="setActionName(this.form,'setPaymentMethod')"><BR>
		<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>:  <%= currencyFormatter.format(cart.getTotal()) %></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
		<input type="image" name="next_step" src="/media_stat/images/buttons/checkout_arrow.gif"
		 BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" onClick="setActionName(this.form,'setPaymentMethod')"></TD>
	</TR>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<%
JspMethods.dumpErrors(result);
%>
	<% String[] checkPaymentForm = {"system", "order_minimum", "technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum"}; %>
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
            <IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
            <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
            <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
            <TR VALIGN="TOP">
            <TD WIDTH="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelX"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
            <TD WIDTH="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
            <TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE"><input type="image" name="next_step"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" border="0" alt="CONTINUE CHECKOUT" VSPACE="0" onClick="setActionName(this.form,'setPaymentMethod')"><BR>Go to Step 4: Submit Order<BR></TD>
            <TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT><input type="image" name="next_step" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0" onClick="setActionName(this.form,'setPaymentMethod')"></TD>
            </TR>
            </TABLE>

	<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>