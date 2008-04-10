<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.CallCenterServices" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType" %>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>

<tmpl:insert template='/template/top_nav.jsp'>
<crm:GetFDUser id="user">
<%!	SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MM/yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>

<%	boolean hideMerchantInfo = true;
	boolean badSupervisorCode = false;
	String supervisorCode = request.getParameter("pwd");
	Collection codes = CallCenterServices.getSupervisorApprovalCodes();
	if (codes.contains(supervisorCode)) {
		hideMerchantInfo = false;
	} else if (null != supervisorCode) {
		badSupervisorCode = true;
	}
%>

<%	//
	// Get the OrderModel using the orderId from the request
	//
	String orderId = request.getParameter("orderId");
%>
<fd:GetOrder id='order' saleId='<%= orderId %>'>
<%		//
		// Get delivery address info, payment info
		//
		ErpAddressModel dlvAddress = order.getDeliveryAddress();
		ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
        EnumSaleType type = order.getOrderType();
        //String type = "REG";
%>
<script language="javascript">
	function confirmCancelOrder() {
		if (confirm("Are you sure you want to cancel this order?")) { 
			return true;
		}
		return false;
	}
</script>

<fd:GetCustomerObjs identity='<%= user.getIdentity() %>' fdCustomerId="fdCustomer" erpCustomerId="erpCustomer" erpCustomerInfoId="erpCustomerInfo">

<fd:ModifyOrderController action='<%= request.getParameter("action") %>' orderId='<%= orderId %>' result='result' successPage='<%= "/main/order_details.jsp?orderId=" + orderId %>'>

<tmpl:put name='title' direct='true'>Order <%= orderId%> Payment Exception</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>
<% if (result != null) { %>
<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='order_status' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='no_customer' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='expiration' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
<% } %>
<div class="content_scroll" style="height: 72%; padding: 0px;">
<%		int cellCounter = 0; %>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ BEGIN OPTION 2 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR BGCOLOR="#6666CC">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="98%" CLASS="text8whitebold">&nbsp;&nbsp; PAYMENT OPTION</TD>
	</TR>
</TABLE>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
	<form name="authorize_new_payment" METHOD="POST" action="">
    <% if (!EnumSaleType.SUBSCRIPTION.equals(type)) {%>
	    <input type="hidden" name="action" value="<%= EnumSaleStatus.SETTLEMENT_FAILED.equals(order.getOrderStatus()) ? "new_payment_for_failed_settlement" : "new_authorization" %>">
    <% }else {%>
        <input type="hidden" name="action" value="auto_renew_auth">
    <%}%>    
	<input type="hidden" name="payment_id" value="">
<script language="JavaScript" type="text/javascript">
	function useCard(paymentId) {
		document.authorize_new_payment.payment_id.value=paymentId;
	}
</script>
	<TR VALIGN="TOP">
		<logic:iterate id="payment" collection="<%= erpCustomer.getPaymentMethods() %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%		boolean showDeleteButton = false;
		String paymentId = ((ErpPaymentMethodModel)payment).getPK().getId();%>
        
		<TD WIDTH="30%" align="left">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
				
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">&nbsp;&nbsp;</TD>
                    <%if(!EnumSaleType.SUBSCRIPTION.equals(type) || !EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())){%>
					    <TD WIDTH="65%"><INPUT TYPE="submit" name="use_<%= paymentId %>" onClick="javascript:useCard('<%= paymentId %>')" class="submit" value="SELECT &amp; SUBMIT"><BR><BR></TD>
                    <%}%>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Payment Method&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getPaymentMethodType() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Name on account&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getName() %></TD>
				</TR>
				<% if (payment.getCardType() != null) { %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Card Type&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getCardType() %></TD>
				</TR>
				<% } %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Account Number&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= PaymentMethodUtil.getDisplayableAccountNumber(payment) %></TD>
				</TR>
				<% if (payment.getExpirationDate() != null) { %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Expires&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= monthYearFormatter.format( payment.getExpirationDate() ) %></TD>
				</TR>
				<% } %>
				<% if (payment.getAbaRouteNumber() != null) { %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">Aba Route Number&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getAbaRouteNumber() %></TD>
				</TR>
				<% } %>
				<% if (payment.getBankAccountType() != null) { %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">Bank Account type&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getBankAccountType() %></TD>
				</TR>
				<% } %>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT"><FONT CLASS="space4pix"><BR></FONT><B>Billing Address</B><BR><FONT CLASS="space4pix"><BR></FONT></TD>
					<TD WIDTH="65%">&nbsp;</TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Address Line 1&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getAddress1() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">Address Line 2&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getAddress2() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">Apt./Floor&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getApartment() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* City&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getCity() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* State&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getState() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">* Zip&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><%= payment.getZipCode() %></TD>
				</TR>
			</TABLE><FONT CLASS="space4pix"><BR></FONT>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
				<TR>
					<TD WIDTH="35%"><BR><FONT CLASS="space4pix"><BR></FONT></TD>
					<%
					String editPage = "";
					if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) { 
						editPage = "/customer_account/edit_credit_card.jsp";
					} else if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) {
						editPage = "/customer_account/edit_checking_account.jsp";
					}
					%>
					<TD WIDTH="65%"><a href="<%= response.encodeURL(editPage+"?custId=" + fdCustomer.getPK().getId() + "&paymentId=" + paymentId + "&successPage=/order/payment_exception.jsp?orderId=" + orderId) %>" class="edit">EDIT</a>&nbsp;<% if (showDeleteButton) { %><A HREF="javascript:confirmDeletePayment('<%= paymentId %>')" class="delete">DELETE</A><% } %></TD>
				</TR>
			</TABLE>
		</TD>
        
		<TD WIDTH="2%">&nbsp;</TD>
<%		cellCounter = ccCounter.intValue();
 		if (ccCounter.intValue() > 0 && ccCounter.intValue() % 2 == 0) { %>
	</tr>
	<tr>
<%		} 
%>
		</logic:iterate>
<%		for (int i = cellCounter % 2; i < 2; i++) { %>
		<td></td>
<% 		} %>
	</TR>
	</form>
</TABLE>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ END OPTION 2 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>


	<%-- THIS IS AN AWFUL HACK THAT SHOULD BE FIXED SOON ... --%>
	<form name="delete_payment" method="POST" action="/customer_account/delete_payment.jsp">
	<input type="hidden" name="deletePaymentId" value="">
	<input type="hidden" name="successPage" value="/order/payment_exception.jsp?orderId=<%= orderId %>">
	</form>

</div>
</tmpl:put>

</fd:ModifyOrderController>
</fd:GetCustomerObjs>
</fd:GetOrder>
</crm:GetFDUser>
</tmpl:insert>

