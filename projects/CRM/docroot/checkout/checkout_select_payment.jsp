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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<fd:CheckLoginStatus guestAllowed="false" redirectPage="/registration/nw_cst_check_zone.jsp" />

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Select Payment Method</tmpl:put>

<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    FDIdentity identity = user.getIdentity();%>
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
    if(request.getParameter("card")!=null){
            cardStatus = request.getParameter("card");
    }
	
	FDCartModel cart = user.getShoppingCart();
    ErpPaymentMethodI selectedPayment = cart.getPaymentMethod();

	String selectedPaymPKId = request.getParameter("paymentId");
	String addressStatus = request.getParameter("addressStatus");
	
	if (request.getParameter("paymentMethodList") != null){
		selectedPaymPKId = request.getParameter("paymentMethodList");
	} else if(cardStatus.equalsIgnoreCase("new")){
    	selectedPaymPKId = "new";
    } else if (selectedPaymPKId==null ) {
        if (selectedPayment != null && !(cart instanceof FDModifyCartModel)) {
            selectedPaymPKId = ((ErpPaymentMethodModel)selectedPayment).getPK().getId();
        } else {
			selectedPaymPKId = FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity());
		}
    }

    // This logic allows us to choose dynamically what action the CheckoutController should perform
    String actionName = request.getParameter("actionName");
    String successPage = null;
    if (actionName==null || actionName.length()<1) actionName = "setPaymentMethod";
    if ("setPaymentMethod".equalsIgnoreCase(actionName)) {
        successPage = "checkout_preview.jsp";
    } else {
        successPage = "checkout_select_payment.jsp";
    }
    
    boolean hasRestrictedAccount = false;
	String methodChecked = "";
	boolean selectedPaymentMethodExists = false;
    
%>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>
<fd:CheckoutController actionName="<%=actionName%>" result="result" successPage="<%= successPage%>">
<% // remove after testing....dumps out the errors.
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

<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
<form name="payment" method="POST" action="">
    <input type="hidden" name="actionName" value="">
    <input type="hidden" name="deletePaymentId" value="">
    <tr>
    <td width="80%">
        &nbsp;Step 3 of 4: Select Payment Method  
        <a href="javascript:popResize('/kbit/policy.jsp?show=Payment','715','940','kbit')" 
            onmouseover="return overlib('Select a payment method for this order.&lt;br&gt;Click for Address Validation Help.', AUTOSTATUS, WRAP);" 
            onmouseout="nd();" class="help">?</a> 
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is 
            <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">
                REACTIVATED
            </a>
        <% } %>
    </td>
    <td align="right"><a href="javascript:payment.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
    </tr>
</table>
<%@ include file="/includes/i_modifyorder.jspf" %>

        <% String[] checkDlvPaymentForm = { "address", "paymentMethodList", "technical_difficulty", "referencedOrder", 
                                    "notInZone", "matching_addresses", "declinedCCD", 
                                    "undeliverableAddress", "payment", "system",
                                    "order_minimum", "credit_card_fraud", EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(),
                                    PaymentMethodName.ACCOUNT_HOLDER, PaymentMethodName.CARD_BRAND, PaymentMethodName.ACCOUNT_NUMBER,
                                    "expiration", EnumUserInfoName.BIL_ADDRESS_1.getCode(),EnumUserInfoName.BIL_APARTMENT.getCode(),
                                    EnumUserInfoName.BIL_CITY.getCode(),        
                                    EnumUserInfoName.BIL_STATE.getCode(), EnumUserInfoName.BIL_ZIPCODE.getCode(), "pickup_contact_number"
                                    }; %>
        
        <fd:ErrorHandler result='<%=result%>' field='<%=checkDlvPaymentForm%>' id='errorMsg'>
            <div class="content_fixed"><span class="error"><%=errorMsg%></span></div>   
        </fd:ErrorHandler>
            
        <%  if (user.getFailedAuthorizations() > 0) { %>
                    <div class="content_fixed"><span class="error">There was a problem with the credit card you selected.&nbsp;&nbsp;&nbsp;<br>
                    Please choose or add a new payment method.</span></div>
        <%  } %>
        
        <%
        {
            String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
            if (errorMsg==null && user.isPromotionAddressMismatch()) {
                errorMsg = SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE;
            }
        
            if (errorMsg!=null) {
                %><div class="content_fixed"><span class="error"><%@ include file="/includes/i_error_messages.jspf" %></span></div><%
            }
        }
        %>

<div class="content_scroll" style="height: 72%;">
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START OF MAKE GOOD ORDER ~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%	boolean makeGoodOrder = (selectedPayment != null && EnumPaymentType.MAKE_GOOD.equals(selectedPayment.getPaymentType())) || request.getParameter("makeGoodOrder") != null;
	String referencedOrder = "";
	if(makeGoodOrder){
		referencedOrder = selectedPayment != null ? selectedPayment.getReferencedOrder() : request.getParameter("referencedOrder");
	}
	
%>
<% if (!(cart instanceof FDModifyCartModel) || (cart instanceof FDModifyCartModel && makeGoodOrder)) {%>
<div class="cust_inner_module" style="margin-bottom: 8px;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order" style="background-color: #fcc">
		<tr valign="top">
			<td>
                <% if (cart instanceof FDModifyCartModel && makeGoodOrder) {%>
                    &nbsp;<b>This is a MAKE GOOD ORDER</b>
                    <input type="hidden" name="makeGoodOrder" value="true">
                <% } else { %>
                    <input type="checkbox" id="makeGoodOrder" name="makeGoodOrder" <%= makeGoodOrder ? "checked" : "" %>>
                    <label for="makeGoodOrder"><b>This is a MAKE GOOD ORDER</b></label>
                <% } %> 
                <a href="javascript:popResize('/kbit/procedure.jsp?show=MakeGoodOrder','715','940','kbit')" 
                    onmouseover="return overlib('Click for Make Good Order Steps.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a>
            </td>
			<td>
				<b>IN REFERENCE TO</b><% if (cart instanceof FDModifyCartModel && makeGoodOrder) {%> order #<b><%=referencedOrder %></b><input type="hidden" name="referencedOrder" value="<%=referencedOrder%>"><% } else { %>
				<select name="referencedOrder">
					<option value="">Order number</option>
					<% Collection makeGoodRefInfos = ((FDOrderHistory)user.getOrderHistory()).getMakeGoodReferenceInfos(); %>
					<logic:iterate id="order" collection="<%=makeGoodRefInfos%>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
						<option value="<%=order.getErpSalesId()%>" <%=order.getErpSalesId().equals(referencedOrder) ? "selected" : "" %>><%=order.getErpSalesId()%> ($<%= order.getTotal() %>)</option>
					</logic:iterate>
				</select><% } %>
			</td>
		</tr>
	</table>
</div>
<br clear="all">
<% } %>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END OF MAKE GOOD ORDER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_full_module_header" style="margin-bottom: 5px;"><tr><td class="cust_module_header_text">Payment Options</td></tr></table>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<% int cellCounter = 0;
    boolean showCCButtons = true;
    boolean showDeleteButtons = paymentMethods.size() > 1 ? true : false; %>
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
</script>
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
			if ( methodChecked.equals("") && numCreditCards==1 && numEChecks == 0)
				methodChecked = "checked";
	
%>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ccNum + 1%></td>
                    <td><%@ include file="/includes/i_payment_select.jspf"%></td>
                </tr>
            </table>
        </div>
    </div>
        <%if(ccNum != 0 && (ccNum+1) % 3 == 0 && ((ccNum+1) < numCreditCards)){%>
            <br clear="all">
        <%}
		ccNum++;	
	} %>        
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

<%      if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) { 			
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
			if ( methodChecked.equals("") && ccCounter.intValue() == numEChecks-1 && !selectedPaymentMethodExists)
				methodChecked = "checked";
             %>
	<crm:CrmGetIsBadAccount id="isRestrictedAccount" paymentMethod="<%=payment%>">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                <% if(!isRestrictedAccount.booleanValue()) { %>
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ecNum + 1%></td>
                 <% } else { %>
                    <td class="note"><%=ecNum + 1%></td>
                 <% } %>
                    <td><%@ include file="/includes/checking_account_select.jspf"%></td>
                </tr>
            </table>
	</crm:CrmGetIsBadAccount>
        </div>
    </div>
        <%if(ecNum != 0 && (ecNum+1) % 3 == 0 && (ecNum+1 < numEChecks)){%>
            <br clear="all">
        <%}
			ecNum++;
	} %>
        
    </logic:iterate>
	<%--END CHECKING--%>
</form>
</div>
</tmpl:put>
</fd:CheckoutController>

</crm:GetErpCustomer>
</tmpl:insert>
