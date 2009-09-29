<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.FDDepotManager"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderHistory" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%-- ------------ EDIT/DELETE CREDIT CARD ADDRESS JAVASCRIPT ------------ --%>
<script language="javascript">

	//sets the value of the hidden field named deletePaymentId
	/***************
	function setDeletePaymentId(frmObj,payid) {
		alert("setDeletePaymentId called " + payid);
		if (frmObj["deletePaymentId"]!=null) {
			frmObj.deletePaymentId.value=payid;         
		}
		return true;
	}    
	*******************/    
	function confirmDeletePayment(frmObj,payId) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (frmObj == null) {
			frmObj = document.forms[0];
		}
		if (doDelete == true) {
			setDeletePaymentId(frmObj, payId);
			setActionName(frmObj,'deletePaymentMethod');
			frmObj.submit();
		}
	}

	function setGCPayment() {

		setActionName(frmObj,'setNoPaymentMethod');

	}

</script>

<fd:CheckLoginStatus guestAllowed="false" redirectPage="/registration/nw_cst_check_zone.jsp" />

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Checkout > Select Payment Method</tmpl:put>

<%  
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    	FDIdentity identity = user.getIdentity();
%>
<crm:GetErpCustomer id="customer" user="<%=user%>">
<%  
   	// Get user's payment methods
	List paymentMethods = customer.getPaymentMethods();
	int numCreditCards = 0;
	int ccNum = 0;	
	int numEChecks = 0;
	int ecNum = 0;	
    
    	// Get cart's selected payment method
	String cardStatus ="";
    	if(request.getParameter("card")!=null) {
        	cardStatus = request.getParameter("card");
    	}	
	
	FDCartModel cart = user.getShoppingCart();
    	ErpPaymentMethodI selectedPayment = cart.getPaymentMethod();
	String selectedPaymPKId = request.getParameter("paymentId");
	String addressStatus = request.getParameter("addressStatus");
	if (request.getParameter("paymentMethodList") != null){
		selectedPaymPKId = request.getParameter("paymentMethodList");
	} else if(cardStatus.equalsIgnoreCase("new")) {
    		selectedPaymPKId = "new";
    	} else if (selectedPaymPKId==null && selectedPayment != null && null !=((ErpPaymentMethodModel)selectedPayment).getPK() && !(cart instanceof FDModifyCartModel)) {
        	selectedPaymPKId = ((ErpPaymentMethodModel)selectedPayment).getPK().getId();
        } else {
		selectedPaymPKId = FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity());
	}
    	
    	// This logic allows us to choose dynamically what action the CheckoutController should perform
    	String actionName = request.getParameter("actionName");
    	String successPage = null;
    	if(user.getUser().getShoppingCart() != null && user.getShoppingCart().getCCPaymentAmount() <= 0) {
    		actionName = "setNoPaymentMethod";
    		successPage = "checkout_preview.jsp";
    	}
    
    	if (actionName==null || actionName.length()<1) {
    		actionName = "setPaymentMethod";
    	}
    	if ("setPaymentMethod".equalsIgnoreCase(actionName)) {
        	successPage = "checkout_preview.jsp";
    	} else if(!"setNoPaymentMethod".equalsIgnoreCase(actionName)) {
        	successPage = "checkout_select_payment.jsp";
    	}
    
    	boolean hasRestrictedAccount = false;
	String methodChecked = "";
	boolean selectedPaymentMethodExists = false;   
	
	FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
	boolean isPaymentRequired = true;
	if(cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0) {
    	double outStandingBalance = FDCustomerManager.getOutStandingBalance(cart);
    	if(outStandingBalance <= 0.0) {
        		isPaymentRequired = false;
        	} else{
        		actionName = "setPaymentMethod";
        	}
	}  
%>
<fd:CheckoutController actionName="<%=actionName%>" result="result" successPage="<%= successPage%>">
<%
    	/*
         * Apply Customer credit -- This is done here for knowing the final order amount before displaying
         * the payment selection
         * If Gift card is used on the order Also calculate the 25% perishable buffer amount to decide if         * 
         * another mode of payment is needed.
    	*/    
    
    	
    	

    	double perishableBufferAmount = 0;
        double gcSelectedBalance = user.getGiftcardBalance()- cart.getTotalAppliedGCAmount();
        double gcBufferAmount=0;
        double ccBufferAmount=0;
        if(cart instanceof FDCartModel){
        	perishableBufferAmount = FDCustomerManager.getPerishableBufferAmount((FDCartModel)cart);
        }
        if(perishableBufferAmount > 0){
        	if(cart.getTotalAppliedGCAmount()> 0){
        		if(cart.getCCPaymentAmount() > 0 && isPaymentRequired) {
        		//if(!EnumPaymentMethodType.GIFTCARD.equals(paymentMethod.getPaymentMethodType())){
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

	// remove after testing....dumps out the errors.
	if (!result.isSuccess()){
    		Collection errs = result.getErrors();
		Iterator itr = errs.iterator();
    		while( itr.hasNext() ){
        		ActionError ae = (ActionError) itr.next();
        		JspLogger.CC_CHECKOUT.debug(ae.getType() + " - " + ae.getDescription());
    		}
	}
%>
<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<form name="payment" method="POST" action="">
	<input type="hidden" name="actionName" value="">
	<input type="hidden" name="deletePaymentId" value="">
	<% if(ccBufferAmount >0 && gcBufferAmount > 0) { %>
	<table>
	<tr>
		<td>
		PLEASE NOTE: A BACKUP PAYMENT METHOD IS REQUIRED FOR THIS ORDER. Because your Estimated Total is close to the balance of the Gift Card you entered, we require a second form of payment. This is to cover changes in price that may occur when we weigh your perishable items and fulfill your order. We guarantee that you'll always pay the true price for the actual weight of your products. Learn about Estimated Totals.
		
		</td>
		</tr>
	</table>
	<% } %>
	<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
	    <tr>
	    <td width="80%">
		&nbsp;Step 3 of 4: Select Payment Method  
		<a href="javascript:popResize('/kbit/policy.jsp?show=Payment','715','940','kbit')" 
		    onmouseover="return overlib('Select a payment method for this order.&lt;br&gt;Click for Address Validation Help.', AUTOSTATUS, WRAP);" 
		    onmouseout="nd();" class="help">?</a> 
		<% if (!user.isActive()) { %>
		    &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is 
		    <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">
			REACTIVATED
		    </a>
		<% } %>
	    </td>
	    <td align="right"><a href="javascript:payment.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
	    </tr>
	</table>
<%@ include file="/includes/i_modifyorder.jspf" %>
<% 
	String[] checkDlvPaymentForm = { "address", "paymentMethodList", "technical_difficulty", "referencedOrder", 
                                         "notInZone", "matching_addresses", "declinedCCD", 
                                    	 "undeliverableAddress", "payment", "system",
                                    	 "order_minimum", "credit_card_fraud", EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(),
                                    	 PaymentMethodName.ACCOUNT_HOLDER, PaymentMethodName.CARD_BRAND, PaymentMethodName.ACCOUNT_NUMBER,
                                    	 "expiration", EnumUserInfoName.BIL_ADDRESS_1.getCode(),EnumUserInfoName.BIL_APARTMENT.getCode(),
                                    	 EnumUserInfoName.BIL_CITY.getCode(),        
                                    	 EnumUserInfoName.BIL_STATE.getCode(), EnumUserInfoName.BIL_ZIPCODE.getCode(), "pickup_contact_number"
         }; 
%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkDlvPaymentForm%>' id='errorMsg'>
		<div class="content_fixed"><span class="error"><%=errorMsg%></span></div>   
	</fd:ErrorHandler>
<%  
	if (user.getFailedAuthorizations() > 0) { 
%>
        	<div class="content_fixed"><span class="error">There was a problem with the credit card you selected.&nbsp;&nbsp;&nbsp;<br>
                Please choose or add a new payment method.</span></div>
<%  
	}
        
	String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
        if (errorMsg==null && user.isPromotionAddressMismatch()) {
        	errorMsg = SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE;
        }
        
        if (errorMsg!=null) {
        	%><div class="content_fixed"><span class="error"><%@ include file="/includes/i_error_messages.jspf" %></span></div><%
        }
        // show will be false if order total is fully covered by a gift card.  
        // Payment select is not necessary.
        boolean show = true;
	if (user.getGiftcardBalance() > 0) { 
		show = false;
%>
		<table width="100%" cellpadding="2" cellspacing="0" border="0">
	     		<tr bgcolor="white">
		 	<td  colspan="2" align="right" style="BORDER-LEFT: orange thin dashed">Order total :</td><!--  (at 125% if estimated): -->
		 	<td align="left" ><%= CCFormatter.formatCurrency(cart.getTotal()) %></td>
		 	<td style="BORDER-RIGHT: orange thin dashed"  align="left">&nbsp;</td>
		 	<% if(isPaymentRequired) { 
		 		show = true; 
		 	%>
		 		<%--<td rowspan="3">&nbsp;&nbsp;Additional Payment <BR/>&nbsp;&nbsp;Method Required for: <%= CCFormatter.formatCurrency(cart.getCCPaymentAmount()) %></td>--%>
	     		<% } else { %>
	     			<td rowspan="3">&nbsp;&nbsp;Proceed <BR/>&nbsp;&nbsp;No amount due </td>
	     		<% } %>
	     		</tr>
	     		<%-- <tr bgcolor="white">
		      	<td colspan="2" align="right" style="BORDER-LEFT: orange thin dashed; BORDER-BOTTOM: orange thin dashed">Gift Card Amount to Be Applied:</td>
		      	<td align="left" style="BORDER-BOTTOM: orange thin dashed"><b><%= CCFormatter.formatCurrency(cart.getTotalAppliedGCAmount()) %></b></td>
		      	<td style="BORDER-RIGHT: orange thin dashed; BORDER-BOTTOM: orange thin dashed" align="left">&nbsp;</td>	           
	     		</tr>  
	      		<tr valign="top" style="background-color: #FF9933">
		      	<td colspan="2" align="right" style="color: white">Amount Due:</td>
		      	<td align="left" style="color: white"><b><%= CCFormatter.formatCurrency(cart.getCCPaymentAmount()) %></b></td>
		      	<td align="left"></td>
	     		</tr>	     --%>
		</table>     
<% 
	}
        if(show) {    
        
%>		
		<div class="content_scroll" style="height: 72%;">

		<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START OF MAKE GOOD ORDER ~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%	
		boolean makeGoodOrder = "true".equals(session.getAttribute("makeGoodOrder"));
		String referencedOrder = "";
		if(makeGoodOrder){
        		referencedOrder = (String)session.getAttribute("referencedOrder");
		}
	
		if (!(cart instanceof FDModifyCartModel) || (cart instanceof FDModifyCartModel && makeGoodOrder)) { %>
			<div class="cust_inner_module" style="margin-bottom: 8px;">
			<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order" style="background-color: #fcc">
				<tr valign="top">
				<td>
				<% if (makeGoodOrder) {%>
			    		&nbsp;<b>This is a MAKE GOOD ORDER</b>
			    		<input type="hidden" name="makeGoodOrder" value="true">
				<% } %> 
				</td>
				<td>
				<% if (makeGoodOrder) {%><b>IN REFERENCE TO</b> order #<b><%=referencedOrder %>
					</b><input type="hidden" name="referencedOrder" value="<%=referencedOrder%>">
				<% } %>
				</td>
				</tr>
			</table>
			</div>
			<br clear="all">
<% 
		} 
%>
		<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END OF MAKE GOOD ORDER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>

		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_full_module_header" style="margin-bottom: 5px;">
			<tr>
			<td class="cust_module_header_text">Payment Options
			</td>
			</tr>
		</table>

		<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<% 
		int cellCounter = 0;
    		boolean showCCButtons = true;
    		boolean showDeleteButtons = paymentMethods.size() > 1 ? true : false; 
%>
		
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;">
			<tr>
    			<td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border: solid 1 px #666666; border-bottom: none;">
        		<b>Credit cards</b>
    			</td>
    			<td align="right" width="79%">
        		<a href="/checkout/checkout_new_credit_card.jsp" class="add">ADD</a>
    			</td>
    			<td width="1%"></td>
			</tr>
		</table>
		<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%  
			if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) { 
				numCreditCards++;
			} else if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) { 
				numEChecks++;
			}		
%>
		</logic:iterate>

		<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%
			if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) {
%>
				<div class="cust_inner_module" style="width: 33%;<%=ccNum < 3 ?"border-top: none;":""%>">
				<div class="cust_module_content">
<%
				methodChecked = "";
				String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
				if (paymentPKId.equals(selectedPaymPKId)){
					methodChecked = "checked";
					selectedPaymentMethodExists = true;
				}	
				else if("new".equalsIgnoreCase(selectedPaymPKId) && ccCounter.intValue() == numCreditCards-1 && numEChecks == 0){
					methodChecked = "checked";
				}
				if ( methodChecked.equals("") && numCreditCards==1 && numEChecks == 0) {
					methodChecked = "checked";
				}
%>
				<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
					<tr valign="top">
					<td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ccNum + 1%></td>
					<td><%@ include file="/includes/i_payment_select.jspf"%></td>
					</tr>
				</table>

				</div>
				</div>
<%
				if(ccNum != 0 && (ccNum+1) % 3 == 0 && ((ccNum+1) < numCreditCards)){
%>
					<br clear="all">
<%	
				}
				ccNum++;	
			} 
%>        
		</logic:iterate>
		<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
		<br clear="all">

		<%-- START CHECKING ACCT --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;">
        		<tr>
        	        <td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border-top: solid 1 px #666666; border-left: solid 1 px #666666; border-right: solid 1 px #666666;">
        	        <b>Checking accounts</b>
        	        </td>
        	        <td width="59%" class="field_note">
        	        &nbsp;A valid credit card is required as a guarantee for orders using check as payment. 
        	        </td>
        	        <td align="right" width="20%">
        	        <a href="/checkout/checkout_new_checkacct.jsp" class="add">ADD</a>
        	        </td>
        	        <td width="1%"></td>
        	        </tr>
		</table>
        	<%-- using cc data for display purposes --%>
		<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%      
			if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) { 			
%>
				<div class="cust_inner_module" style="width: 33%;<%=ecNum < 3 ?"border-top: none;":""%>">
				<div class="cust_module_content">
<%
				methodChecked = "";
				String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();

				if (paymentPKId.equals(selectedPaymPKId)){
					methodChecked = "checked";
					selectedPaymentMethodExists = true;
				}
				else if("new".equalsIgnoreCase(selectedPaymPKId) && ccCounter.intValue() == numEChecks-1){
					methodChecked = "checked";
				}
				if ( methodChecked.equals("") && ccCounter.intValue() == numEChecks-1 && !selectedPaymentMethodExists) {
					methodChecked = "checked";
				}
%>
				<crm:CrmGetIsBadAccount id="isRestrictedAccount" paymentMethod="<%=payment%>">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
					<tr valign="top">
<% 
					if(!isRestrictedAccount.booleanValue()) { 
%>	
						td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ecNum + 1%></td>
<% 	
					} else {
%>	
						<td class="note"><%=ecNum + 1%></td>
<% 
					} 
%>
					<td><%@ include file="/includes/checking_account_select.jspf"%></td>
					</tr>
				</table>
				</crm:CrmGetIsBadAccount>
				</div>
				</div>
<%
				if(ecNum != 0 && (ecNum+1) % 3 == 0 && (ecNum+1 < numEChecks)) {
%>
					<br clear="all">
<%
				}
				ecNum++;
			} 
%>
        	</logic:iterate>
		<%--END CHECKING--%>
<% 
	}
%>	
</form>
</div>
</tmpl:put>
</fd:CheckoutController>
</crm:GetErpCustomer>
</tmpl:insert>