<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import="com.freshdirect.ErpServicesProperties" %>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%	String orderId = request.getParameter("orderId"); %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Charge Order #<%= orderId %></tmpl:put>

<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>


<fd:GetOrder id='order' saleId='<%= orderId %>'>

<crm:GetErpCustomer id="customer" user="<%= user %>">

<%
	// Get user's payment methods
    List paymentMethods = customer.getPaymentMethods();
	ErpPaymentMethodI selectedPayment = order.getPaymentMethod();
	int numCreditCards = 0;
	int ccNum = 0;	
	int numEChecks = 0;
	int ecNum = 0;	
	double totalAmount = 0.0;
	double totalFees = 0;
	double lineAmount = 0;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");

    // Get cart's selected payment method
	String cardStatus ="";
    if(request.getParameter("card")!=null){
            cardStatus = request.getParameter("card");
    }

	String selectedPaymPKId = request.getParameter("paymentId");
	
    ErpSaleModel saleModel = ((com.freshdirect.fdstore.customer.adapter.FDOrderAdapter) order).getSale();
    List txList = new ArrayList( saleModel.getTransactions() );
  	Collections.sort(txList, ErpTransactionI.TX_DATE_COMPARATOR);
    
%>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>

<fd:ModifyOrderController orderId="<%= orderId %>" action="charge_order"  result="result" successPage='<%= "/main/order_details.jsp?orderId=" + orderId %>'>
        
<form NAME="charge_order" METHOD="POST">
    <input type="hidden" name="payment_id" value="">
	<%
	
     String savedErrorMsg = (String) session.getAttribute("SAVED_ERROR_MSG");
     session.removeAttribute("SAVED_ERROR_MSG");
     
     if (savedErrorMsg != null) {
	%>
        <div class="content_fixed"><span class="error"><%=savedErrorMsg%></span></div>   	
	<%     
     }
 	 String[] checkChargePaymentForm = { "paymentMethodList", "technical_difficulty", 
	                                   "payment", "system", "order_status", "no_payment_selected"}; 
	%>
    
    <fd:ErrorHandler result='<%=result%>' field='<%=checkChargePaymentForm%>' id='errorMsg'>
<%
    	if (errorMsg != null) {
	        session.setAttribute("SAVED_ERROR_MSG", errorMsg);    		
    	} 
%>        
    </fd:ErrorHandler>
        
    <%  
    	if (user.getFailedAuthorizations() > 0) { %>
                <div class="content_fixed"><span class="error">There was a problem with the credit card you selected.&nbsp;&nbsp;&nbsp;<br>
                Please choose or add a new payment method.</span></div>
    <%  } %>
    
    <%
        String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
        if (errorMsg==null && user.isPromotionAddressMismatch()) {
            errorMsg = SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE;
        }
    
        if (errorMsg!=null) {
    %>
    	<div class="content_fixed"><span class="error"><%@ include file="/includes/i_error_messages.jspf" %></span></div>
    <%  } %>

<div class="content_scroll" style="height: 72%;">
<table width="100%"><tr valign="top"><td width="50%"><table width="100%" border="0" cellspacing="0" cellpadding="3" class="order_detail">
  <tr bgcolor="#EEEEEE">
    <td colspan="4"><b>Order outstanding total</b></td>
    <td align="right">(<a href="javascript:pop('/main/order_details.jsp?orderId=<%=orderId%>&for=print','600','680')" class="order_detail">View Invoice</a>)</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><b>Type</b></td>
    <td><b>Date</b></td>
    <td><b>Status</b></td>
    <td align="right"><b>Charge/credit</b></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>

<logic:iterate id="view" collection="<%= order.getOrderViews() %>" type="com.freshdirect.fdstore.customer.WebOrderViewI">
<%
	if (view.getAffiliate() != null && view.getAffiliate().isPrimary()) {
		if(order.hasInvoice()) {
			view = order.getInvoicedOrderView(view.getAffiliate());
		}
		lineAmount = view.getSubtotal() + view.getTax() + view.getDepositValue();
		totalAmount += lineAmount;
%>
<tr>
    <td>&nbsp;</td>
    <td><%=view.getAffiliate().getName()%> Total</td>
    <td><%= sdf.format(order.getDatePlaced()) %></td>
    <td><%= order.getOrderStatus() %> </td>
    <td align="right"><%=JspMethods.formatPrice(lineAmount)%></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
 <%
 	}
 %>
</logic:iterate>
<logic:iterate id="view" collection="<%= order.getOrderViews() %>" type="com.freshdirect.fdstore.customer.WebOrderViewI">
<%
	if (view.getAffiliate() != null && !view.getAffiliate().isPrimary()) {
		if(order.hasInvoice()) {
			view = order.getInvoicedOrderView(view.getAffiliate());
		}		
		lineAmount = view.getSubtotal() + view.getTax() + view.getDepositValue();
		totalAmount += lineAmount;
%>
<tr>
    <td>&nbsp;</td>
    <td><%=view.getAffiliate().getName()%> Total</td>
    <td><%= sdf.format(order.getDatePlaced()) %></td>
    <td><%= order.getOrderStatus() %> </td>
    <td align="right"><%=JspMethods.formatPrice(lineAmount)%></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
 <%
 	}
 %>
</logic:iterate>
<%

if(order.hasInvoice()) {
%>
<logic:iterate id="chargeLine" collection="<%= order.getCharges() %>" type="com.freshdirect.customer.ErpChargeLineModel">
<%
		lineAmount = chargeLine.getTotalAmount() + ((chargeLine.isTaxed()) ? (chargeLine.getTotalAmount() * chargeLine.getTaxRate()) : 0);
		totalAmount += lineAmount;
%>
    <td>&nbsp;</td>
    <td><%=chargeLine.getType().getName()%></td>
    <td><%= sdf.format(order.getDatePlaced()) %></td>
    <td><%= order.getOrderStatus() %> </td>
    <td align="right"><%=JspMethods.formatPrice(lineAmount)%></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</logic:iterate>
	<%
	if (order.getActualCustomerCreditsValue() != 0.0) {
		totalAmount -= order.getActualCustomerCreditsValue();
	%>
    <td>&nbsp;</td>
    <td>Applied Credits</td>
    <td><%= sdf.format(order.getDatePlaced()) %></td>
    <td><%= order.getOrderStatus() %> </td>
    <td align="right"><%=JspMethods.formatPrice(order.getActualCustomerCreditsValue())%></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>	
  </tr>
<%
	}	
}

%>
  <tr>
    <td>&nbsp;</td>
    <td colspan="3" style="border-top: #999999 solid 1px;">&nbsp;</td>
    <td style="border-top: #999999 solid 1px;" align="right"><b><%=JspMethods.formatPrice(totalAmount)%></b></td>
    <td style="border-top: #999999 solid 1px;">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>


  <tr>
    <td colspan="7" bgcolor="#EEEEEE"><b>Payment failed charges </b></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><b>Type</b></td>
    <td><b>Date</b></td>
    <td><b>Description</b></td>
    <td align="right"><b>Fee</b></td>
    <td align="center"><b>Waive?</b></td>
    <td>&nbsp;</td>
  </tr>
<%
	List failedSettlements = new ArrayList();
    ErpAbstractSettlementModel m = saleModel.getLastFailSettlement();
    if(m != null){
        failedSettlements.add(m);
    }
	failedSettlements.addAll((Collection)saleModel.getFailedChargeSettlements());	
    
    for (Iterator txIter = failedSettlements.iterator(); txIter.hasNext(); ) {
		ErpAbstractSettlementModel failedSettlementModel = (ErpAbstractSettlementModel) txIter.next(); 			
		EnumPaymentResponse paymentResponse = failedSettlementModel.getResponseCode();
		if (paymentResponse != null) {  			
%>
  <tr>
    <td>&nbsp;</td>
    <td><%="Bounced Check"%></td>
    <td><%=sdf.format(failedSettlementModel.getTransactionDate())%>&nbsp;</td>
    <td><%=paymentResponse.getName()%><B>&nbsp;<%=paymentResponse.getDescription()%></B></td>
    <td align="right"><%=ErpServicesProperties.getBouncedCheckFee()%></td>
    <td align="center"><input type="checkbox" name="waive" value="Y"></td>

	<input type="hidden" name="additional_charge" value="<%=ErpServicesProperties.getBouncedCheckFee()%>">
<%
		try {
			double bounceCheckFee = Double.parseDouble(ErpServicesProperties.getBouncedCheckFee());
			totalFees += bounceCheckFee;
		} catch (Exception e) {}						
%>
    <td>&nbsp;</td>
  </tr>
<%
		}
	}

%>
	<input type="hidden" name="order_amount" value="<%=order.getInvoicedTotal()%>">
  <tr>
  	<td>&nbsp;</td>
    <td colspan="3" style="border-top: #999999 solid 1px;">&nbsp;</td>
    <td style="border-top: #999999 solid 1px;" align="right"><b><%=JspMethods.formatPrice(totalFees)%></b></td>
    <td style="border-top: #999999 solid 1px;">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr bgcolor="#EEEEEE">
    <td colspan="4" align="right"><b>Total Charge</b></td>
    <td align="right"><b><input type="text" align="right" size="7" name="charge_amount_1" readonly value="<%=JspMethods.formatPrice(order.getInvoicedTotal()+totalFees)%>"></b></td>    
    <td colspan="2">&nbsp;<a onclick="javascript:recalculateTotal(document.forms['charge_order']);"  class="update_price">RECALCULATE</a></td>
  </tr>
</table>
</td><td width="49%" style="border-left: solid 1px #999999; padding-left: 15px;">
<table width="100%"  border="0" cellspacing="0" cellpadding="3" class="order_detail">
  <tr>
    <td colspan="5" class="order"><b>Order summary</b></td>
  </tr>
  <tr>
    <td><b>Date</b></td>
    <td><b>Activity</b></td>
    <td><b>By</b></td>
    <td align="right"><b>Amount</b></td>
    <td align="right"><b>Payment method </b></td>
  </tr>
<%
    for (Iterator txIter = txList.iterator(); txIter.hasNext(); ) {
        ErpTransactionModel txModel = (ErpTransactionModel) txIter.next();
%>
  <tr>
    <td class="border_bottom"><%=sdf.format(txModel.getTransactionDate())%>&nbsp;</td>
    <td class="border_bottom"><%=txModel.getTransactionType().getName()%>&nbsp;</td>
    <td class="border_bottom"><%=(txModel.getTransactionInitiator() != null) ? txModel.getTransactionInitiator() : "System" %>&nbsp;</td>
    <td class="border_bottom" align="right">&nbsp;<%=JspMethods.formatPrice(txModel.getAmount())%></td>
    <%
    	String paymentMethodStr = "";
    	if (txModel.getTransactionType().equals(EnumTransactionType.AUTHORIZATION) ||
    		txModel.getTransactionType().equals(EnumTransactionType.ADJUSTMENT) ||
    		txModel.getTransactionType().equals(EnumTransactionType.CAPTURE) ||
			txModel.getTransactionType().equals(EnumTransactionType.CASHBACK) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.SETTLEMENT) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.SETTLEMENT_FAILED) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.SETTLEMENT_CHARGE) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.FUNDS_REDEPOSIT) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.SETTLEMENT_CHARGE_FAILED) ||    		
			txModel.getTransactionType().equals(EnumTransactionType.VOID_CAPTURE)) { 
                ErpPaymentModel payment = (ErpPaymentModel) txModel;
                if (payment.getPaymentMethodType() != null) {
                	paymentMethodStr += payment.getPaymentMethodType().getDescription() + "#: XXXXXXXX" 
	               					+ payment.getCcNumLast4();                
                }
     	} else if (txModel.getTransactionType().equals(EnumTransactionType.CREATE_ORDER) ||
     		txModel.getTransactionType().equals(EnumTransactionType.MODIFY_ORDER)) {
                ErpPaymentMethodI payment = ((ErpAbstractOrderModel) txModel).getPaymentMethod();
                String accountNumber = payment.getAccountNumber();
                if (payment.getPaymentMethodType() != null && accountNumber != null && accountNumber.length() > 4) {
                	paymentMethodStr += payment.getPaymentMethodType().getDescription() +  "#: XXXXXXXX" 
                					+ accountNumber.substring(accountNumber.length()-4);
                }
     	}
    	
    %>
    <td class="border_bottom" align="right">&nbsp;<%=paymentMethodStr%></td>
  </tr>
  <% } %>
</table>
</td></tr>
</table>
</form>
</fd:ModifyOrderController>

<table width=""100% class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td class="cust_module_header_text">Payment Options</td></tr></table>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<% int cellCounter = 0;
    boolean showCCButtons = true;
    boolean showDeleteButtons = paymentMethods.size() > 1 ? true : false; %>
<%-- ------------ EDIT/DELETE CREDIT CARD ADDRESS JAVASCRIPT ------------ --%>
<script language="javascript">
    function recalculateTotal(frmObj) {
			if (frmObj == null) {
				frmObj = document.forms.charge_order;
			}
			if (frmObj.waive == null || frmObj.additional_charge == null) {
				return false;
			}
			var chargeAmount = <%=order.getInvoicedTotal()%>;
            if (frmObj.waive != null && !frmObj.waive.checked) {
            	chargeAmount += parseInt(frmObj.additional_charge.value, 10);
            } else if (frmObj.waive.length > 1) {
            	for (var i = 0; i < frmObj.waive.length; i++) {
            		if (!frmObj.waive[i].checked) {
		            	chargeAmount += parseInt(frmObj.additional_charge[i].value, 10);
            		}
            	}
            }
            frmObj.charge_amount_1.value = "$" + chargeAmount;
            document.forms.submit_charge.charge_amount_2.value = "$" + chargeAmount;
            return false;
    }

    //sets the value of the hidden field named deletePaymentId
    function setDeletePaymentId(frmObj,payId) {
			if (frmObj == null) {
				frmObj = document.forms.payment;
			}
            if (frmObj["deletePaymentId"]!=null) {
                frmObj.deletePaymentId.value=payId;         
            }
            return true;
    }
	function confirmDeletePayment(frmObj,payId) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (doDelete == true) {
			if (frmObj == null) {
				frmObj = document.forms.payment;
			}
			setDeletePaymentId(frmObj, payId);
			setActionName(frmObj,'deletePaymentMethod');
			frmObj.submit();
		}
	}
    
    function setPaymentId(frmObj,payId) {
			if (frmObj == null) {
				frmObj = document.forms.charge_order;
			}
            if (frmObj["payment_id"]!=null) {
                frmObj.payment_id.value=payId;         
            }
            return true;
    }

</script>
<% 	String actionName = request.getParameter("actionName");
	String returnPage = "/order/charge_order.jsp?orderId=" + orderId;
	String returnParam = "returnPage=" + returnPage;
%>
<crm:CrmPaymentMethodController actionName="<%=actionName%>" result="ccResult" successPage="<%=returnPage%>">
<form NAME="payment" METHOD="POST">
    <input type="hidden" name="deletePaymentId" value="">
    <input type="hidden" name="actionName" value="">
<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border: solid 1 px #666666; border-bottom: none;"><b>Credit cards</b></td><td align="right" width="79%"><a href="/customer_account/new_credit_card.jsp?<%=returnParam%>" class="add">ADD</a></td><td width="1%"></td></tr></table>

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
	if(EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())){
	%>
    <div class="cust_inner_module" style="width: 33%;<%=ccNum < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">	
	<%
            String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
			
			String methodChecked = "";
           
/***********           
            if (paymentPKId.equals(selectedPaymPKId)){
                methodChecked = "checked";
            }
			else if("new".equalsIgnoreCase(selectedPaymPKId) && ccNum == numCreditCards-1){
			    methodChecked = "checked";
			}
			if ( methodChecked.equals("") && numCreditCards==1)
				methodChecked = "checked";
*****************/	

             %>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %> onClick="javascript:setPaymentId(document.forms.charge_order, <%= paymentPKId %>);"> <%=ccNum + 1%></td>
                    <td><%@ include file="/includes/i_payment_select.jspf"%></td>
                </tr>
            </table>
        </div>
    </div>
        <%if(ccNum != 0 && (ccNum + 1) % 3 == 0 && ((ccNum + 1) < numCreditCards)){%>
            <br clear="all">
        <%}
		ccNum++;	
	} %>        
    </logic:iterate>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<br clear="all">
<%-- START CHECKING ACCT  (FOR DISPLAY ONLY!!!) --%>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border-top: solid 1 px #666666; border-left: solid 1 px #666666; border-right: solid 1 px #666666;"><b>Checking accounts</b></td><td width="59%" class="field_note">&nbsp;A valid credit card is required as a guarantee for orders using check as payment. <a href="#">View check usage promotion</a></td><td align="right" width="20%"><a href="/customer_account/new_checking_account.jsp?<%=returnParam%>" class="add">ADD</a></td><td width="1%"></td></tr></table>
        	 <%-- using cc data for display purposes --%>
<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
	<%
	if(EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())){
	%>
    <div class="cust_inner_module" style="width: 33%;<%=ecNum < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">	
	<%			
            String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
			String methodChecked = "";
/**************           
            if (paymentPKId.equals(selectedPaymPKId)){
                methodChecked = "checked";
            }
			else if("new".equalsIgnoreCase(selectedPaymPKId) && ecNum == numEChecks-1){
			    methodChecked = "checked";
			}
			if ( methodChecked.equals("") && numEChecks==1)
				methodChecked = "checked";	
*************/				
             %>
	<crm:CrmGetIsBadAccount id="isRestrictedAccount" paymentMethod="<%=payment%>">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %> onClick="javascript:setPaymentId(document.forms.charge_order, <%= paymentPKId %>);"> <%=ecNum + 1%></td>
                    <td><%@ include file="/includes/checking_account_select.jspf"%></td>
                </tr>
            </table>
	</crm:CrmGetIsBadAccount>
        </div>
    </div>
        <%if(ecNum != 0 && (ecNum + 1) % 3 == 0 && ((ecNum + 1) < numEChecks)){%>
            <br clear="all">
        <%}
		ecNum++;
	} %>
    </logic:iterate>
	<%--END CHECKING--%>
</form>
</crm:CrmPaymentMethodController>
<br clear="all">
<table width="100%" class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td width="40%"><span class="cust_module_header_text"> 
<FORM name="submit_charge">
<b><i>Total Charge:&nbsp;&nbsp;</i><input type="text" align="right" size="7" name="charge_amount_2" readonly value="<%=JspMethods.formatPrice(order.getInvoicedTotal()+totalFees)%>">
</FORM>
</span></b></span></td><td width="60%"><input type="submit" class="submit" style="width: 250px;" value="CHARGE THIS ORDER" onClick="javascript:document.forms['charge_order'].submit();"></td></tr></table></div>
</tmpl:put>

</crm:GetErpCustomer>
</fd:GetOrder>

</tmpl:insert>