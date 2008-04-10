<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Place auto_renew Order</tmpl:put>
<% 
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    System.out.println("Hello..");
    FDCartI order = (FDCartI) session.getAttribute("SUBSCRIPTION_CART");
    ErpPaymentMethodI paymentMethod=order.getPaymentMethod();
     
    
%>
<crm:GetCurrentAgent id="currentAgent">
<crm:GetErpCustomer id="customer" user="<%= user %>">

<tmpl:put name='content' direct='true'>

<crm:SubscriptionController actionName='<%= request.getParameter("actionName") %>' result='result' successPage='<%= "/main/order_details.jsp?orderId=" %>'>
<TABLE>
<form name="place_order" METHOD="POST" action="">
<input type="hidden" name="actionName" value="place_subscription_order">
<input type="hidden" name="payment_id" value="">
</form>
</TABLE>





   
			<table width="50%" cellpadding="0" cellspacing="0" border="0" class="order_detail" style="padding: 1px;">
				<tr>
					<td width="35%">Payment Method</td>
					<td width="65%"><%= paymentMethod.getPaymentMethodType() %></td>
				</tr>
				<tr>
					<td>Name on account&nbsp;&nbsp;</td>
					<td><%= paymentMethod.getName() %></td>
				</tr>
				<% if (paymentMethod.getCardType() != null) { %>
				<tr>
					<td>Card Type&nbsp;&nbsp;</td>
					<td><%= paymentMethod.getCardType() %></td>
				</tr>
				<% } %>
				<tr>
					<td>Account Number&nbsp;&nbsp;</td>
					<td><%= PaymentMethodUtil.getDisplayableAccountNumber(paymentMethod) %></td>
				</tr>
				<% if (paymentMethod.getAbaRouteNumber() != null) { %>
				<tr>
					<td>ABA Route Number&nbsp;&nbsp;</td>
					<td><%= paymentMethod.getAbaRouteNumber() %></td>
				</tr>
				<% } %>
				<% if (paymentMethod.getBankAccountType() != null) { %>
				<tr>
					<td>Bank Account Type&nbsp;&nbsp;</td>
					<td><%= paymentMethod.getBankAccountType() %></td>
				</tr>
				<% } %>
				<% if (paymentMethod.getExpirationDate() != null) { %>
				<tr>
					<td>Expires&nbsp;&nbsp;</td>
					<td><%= CCFormatter.formatCreditCardExpDate( paymentMethod.getExpirationDate() ) %></td>
				</tr>
				<% } %>
				<% if (!EnumPaymentType.REGULAR.equals(paymentMethod.getPaymentType())) {
					%>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td><b>Payment</b></td>
						<td style="color: red; font-weight: bold;"><%= paymentMethod.getPaymentType().getDescription() %></td>
					</tr>
					<tr>
						<td>Referenced order</td>
						<td><%= paymentMethod.getReferencedOrder() %></td>
					</tr>
					<%
				}
				%>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr valign="top">
					<td>Billing address</td>
					<td><%=paymentMethod.getAddress1()%>&nbsp;Apt. <%=paymentMethod.getApartment()%>
                                    <%if(paymentMethod.getAddress2() != null && !"".equalsIgnoreCase(paymentMethod.getAddress2())){%>
                                        <br><%=paymentMethod.getAddress2()%><%}%><br><%=paymentMethod.getCity()%>,&nbsp;<%=paymentMethod.getState()%>&nbsp;<%=paymentMethod.getZipCode()%></td>
				</tr>
				<tr>
					<td>Billing Reference&nbsp;&nbsp;</td>
					<td><%= paymentMethod.getBillingRef() != null && !"".equals(paymentMethod.getBillingRef()) ? paymentMethod.getBillingRef() : "<i>---</i>" %></td>
				</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2"><b>Note: Under no circumstance should payment method information be read to customer. If customer provides correct information to you it may be confirmed or denied.</b>
			</td></tr>
			</table>
		</td>
	</tr>
</table>

<table border="0" cellspacing="0" cellpadding="1" width="100%" class="order_detail">   
<logic:iterate id="view" collection="<%= order.getOrderViews() %>" type="com.freshdirect.fdstore.customer.WebOrderViewI">
	<%
	String lastDept = null;
	double tax = 0.0;
	double deposit = 0.0;

	if (!view.isDisplayDepartment()) {
		%>
		<tr class="order_item_dept">
			<td colspan="2"></td>
			<td colspan="9"><b><%= view.getDescription() %> Charges</b></td>
		</tr>
		<%
	}
	%>

	
<%@ include file="/includes/i_subscriptionline_details.jspf"%>
</logic:iterate>


<tr><td colspan="11"><br></td></tr>
<tr>
	<td colspan="3" align="right"><b>Order Totals and Extras</b></td>
	<td colspan="4" align="right">Total Price</td>
	<td colspan="4"></td>
</tr>
<tr><td colspan="11" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>	
            
         <%
       	    String displayString = "";
	    StringBuffer value = new StringBuffer();
	    StringBuffer returnValue = new StringBuffer();
         
         
		displayString = "Delivery Charge:";
		value.append(CCFormatter.formatCurrency(order.getDeliverySurcharge()));
		value.append(order.isDeliveryChargeTaxable() ? "&nbsp;T" : "");
	    
	  %>   
        <tr>
            <td colspan="3"></td>
            <td colspan="2"><%= displayString %></td>
            <td colspan="2" align="right"><%= value %><b><%= returnValue %></b></td>
            <td colspan="3"></td>
        </tr>    
	  
			
		<%if(order.getTaxValue() > 0){%>
			</tr>
				<td colspan="3"></td>
				<td colspan="2">Total Tax: </td>
				<td colspan="2" align="right"><%=CCFormatter.formatCurrency(order.getTaxValue())%></td>
			<tr>
		<%}%>
		
		            <td colspan="3"></td>
				    <td colspan="2"><b>Order Total</b></td>
				    <td colspan="2" align="right"><b><%=CCFormatter.formatCurrency(order.getTotal()) %></td>

		</tr>

</table>


	<%
	
     String savedErrorMsg = (String) session.getAttribute("SAVED_ERROR_MSG");
     session.removeAttribute("SAVED_ERROR_MSG");
     
     if (savedErrorMsg != null) {
	%>
        <div class="content_fixed"><span class="error"><%=savedErrorMsg%></span></div>   	
	<%     
     }
 	 String[] checkChargePaymentForm = { "delivery_pass_error", "technical_difficulty", 
	                                   "no_payment_selected","expiration"}; 
	%>
    
    <fd:ErrorHandler result='<%=result%>' field='<%=checkChargePaymentForm%>' id='errorMsg'>
<%
    	if (errorMsg != null) {
	        session.setAttribute("SAVED_ERROR_MSG", errorMsg);    		
    	} 
%>        
    </fd:ErrorHandler>
</crm:SubscriptionController>

<br clear="all">
<table width="100%" class="cust_full_module_header" style="margin-top: 0px; margin-bottom: 5px;"><tr><td width="40%"><span class="cust_module_header_text"> 
<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td align="right" width="79%"><a href="/main/place_auto_renew_order.jsp" class="Cancel">Back</a></td><td width="1%"></td></tr></table>
</span></b></span></td><td width="60%"><input type="submit" class="submit" style="width: 250px;" value="PLACE THIS ORDER" onClick="javascript:document.forms['place_order'].submit();"></td></tr></table></div>


</tmpl:put>

</crm:GetErpCustomer>
</crm:GetCurrentAgent>
</tmpl:insert>