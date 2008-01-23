<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.CallCenterServices" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType" %>

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
<% } %>
<div class="content_scroll" style="height: 72%; padding: 0px;">
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ BEGIN OPTION 1 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR BGCOLOR="#6666CC">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="98%" CLASS="text8whitebold">&nbsp;&nbsp;OPTION 1: RE-AUTHORIZE ORIGINAL CREDIT CARD</TD>
	</TR>
</TABLE>
<%		
        ErpAuthorizationModel auth = null;
		Date lastAuthDate = null;
        List auths = null;
        if (order.getOrderStatus().equals(EnumSaleStatus.AVS_EXCEPTION)) {
            auths = order.getAuthorizations();
        } else if (order.getOrderStatus().equals(EnumSaleStatus.AUTHORIZATION_FAILED) || order.getOrderStatus().equals(EnumSaleStatus.CHARGEBACK)) {
            auths = order.getFailedAuthorizations();
        }
		for (Iterator it = auths.iterator(); it.hasNext(); ) {
			ErpAuthorizationModel tmpAuth = (ErpAuthorizationModel) it.next();
			if ( lastAuthDate == null || tmpAuth.getTransactionDate().after(lastAuthDate) ) {
				auth = tmpAuth;
				lastAuthDate = tmpAuth.getTransactionDate();
			}
		}
%>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
	<TR VALIGN="TOP">
		<TD WIDTH="4%"><BR></TD>
		<TD WIDTH="24%">
		<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
			<TR>
				<TD COLSPAN="2"><B>Decline Reason</B><br><FONT CLASS="space4pix"><BR></FONT></TD>
			</TR>
			<TR>
				<TD COLSPAN="2">&nbsp;&nbsp;Response Code: <%= (auth!=null)?auth.getResponseCode().getName():"" %><br>
					&nbsp;&nbsp;Description: <%= (auth!=null)?auth.getDescription():"" %><br>
					&nbsp;&nbsp;(AVS Message: <%= (auth!=null)?auth.getAvs():"" %>)<br><FONT CLASS="space4pix"><BR></FONT>
				</TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Payment Method&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getPaymentMethodType() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Name on account&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getName() %></TD>
			</TR>
			<% if (paymentMethod.getCardType() != null) { %>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Card Type&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getCardType() %></TD>
			</TR>
			<% } %>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Account Number&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= (hideMerchantInfo) ? PaymentMethodUtil.getDisplayableAccountNumber(paymentMethod) : paymentMethod.getAccountNumber() %></TD>
			</TR>
			<% if (paymentMethod.getExpirationDate() != null) { %>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Expires&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= monthYearFormatter.format( paymentMethod.getExpirationDate() ) %></TD>
			</TR>
			<% } %>
			<TR>
				<TD WIDTH="35%"><FONT CLASS="space4pix"><BR></FONT><B>Billing Address&nbsp;&nbsp;</B><BR><FONT CLASS="space4pix"><BR></FONT></TD>
				<TD WIDTH="65%">&nbsp;</TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Address Line 1&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getAddress1() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Address Line 2&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getAddress2() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Apt./Floor&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getApartment() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">City&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getCity() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">State&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getState() %></TD>
			</TR>
			<TR>
				<TD WIDTH="35%" ALIGN="RIGHT">Zip&nbsp;&nbsp;</TD>
				<TD WIDTH="65%"><%= paymentMethod.getZipCode() %></TD>
			</TR>
		</TABLE><FONT CLASS="space4pix"><BR></FONT>
		</TD>
		<TD WIDTH="24%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<TR>
					<TD WIDTH="100%" COLSPAN="2"><b>Customer Contact Info</b><FONT CLASS="space4pix"><BR></FONT></TD>
				</TR>
				<TR>
					<TD WIDTH="35%">Name</TD>
					<TD WIDTH="65%"><%= erpCustomerInfo.getFirstName() %> <%= erpCustomerInfo.getMiddleName() %> <%= erpCustomerInfo.getLastName() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%">Home Phone</TD>
					<TD WIDTH="65%"><%= (erpCustomerInfo.getHomePhone() != null) ? erpCustomerInfo.getHomePhone().getPhone() : "" %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%">Business Phone</TD>
					<TD WIDTH="65%"><%= (erpCustomerInfo.getBusinessPhone() != null) ? erpCustomerInfo.getBusinessPhone().getPhone() : "" %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%">Email</TD>
					<TD WIDTH="65%"><%= erpCustomerInfo.getEmail() %></TD>
				</TR>
				<TR>
					<TD WIDTH="35%">Alt. Email</TD>
					<TD WIDTH="65%"><%= erpCustomerInfo.getAlternateEmail() %></TD>
				</TR>
			</TABLE>
		</TD>
		<TD WIDTH="48%">
			<%-- =========== BEGIN LOGIN / MERCHANT INFO SUB-SECTION =========== --%>
<%	if (hideMerchantInfo) { %>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<form name="merchant_info" method="POST" action="/order/payment_exception.jsp">
				<input type="hidden" name="action" value="show_merchant_info">
				<input type="hidden" name="orderId" value="<%= orderId %>">
				<TR>
					<TD WIDTH="40%" ALIGN="center"><b>Enter Password For Full CC Info</b><FONT CLASS="space4pix"><BR></FONT></TD>
					<TD WIDTH="60%" ALIGN="center"></TD>
				</TR>
				<TR>
					<TD WIDTH="40%" ALIGN="center"><input type="password" name="pwd" value="<%= request.getParameter("password") %>"><FONT CLASS="space4pix"><BR></FONT></TD>
					<TD WIDTH="60%" ALIGN="left"><% if (badSupervisorCode) { %><FONT CLASS='text11rbold'>* Please enter a valid password.</FONT><% } %></TD>
				</TR>
				<TR>
					<TD WIDTH="40%" ALIGN="center"><input type="reset" value="CLEAR" class="clear"> <input type="submit" class="submit" value="SUBMIT"></TD>
					<TD WIDTH="60%" ALIGN="center"></TD>
				</TR>
				</form>
			</TABLE>
<%	} else {
        // are we handling an address verification issue or an authorization issue? 
        //
        if (order.getOrderStatus().equals(EnumSaleStatus.AVS_EXCEPTION)) { %>
            <TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">				
				<TR>
					<TD WIDTH="100%"><b>Processor Contact Info</b><FONT CLASS="space4pix"><BR></FONT></TD>
				</TR>
				<TR>
					<TD WIDTH="100%">
                    <%  if (EnumCardType.VISA.equals(paymentMethod.getCardType()) || EnumCardType.MC.equals(paymentMethod.getCardType())) { %>
						Visa/Mastercard<br>
                        1-800-884-1002 option 2, option 3 for issuing bank #<br>
                        Merchant ID: 67212800015<br>
                    <%  } else if (EnumCardType.AMEX.equals(paymentMethod.getCardType())) {  %>
                        American Express<br>
                        <% if (auth.getAmount() <= 250.0) { %>
                        1-800-528-2121<br>
                        <% } else { %>
                        1-800-710-9638<br>
                        <% } %>
                        Merchant ID: 6310498183<br>
                    <%  } else if (EnumCardType.DISC.equals(paymentMethod.getCardType())) {  %>
                        Discover<br>
                        1-800-347-7988<br>
                        Merchant ID: 601101312821927<br>
                    <%  }   %>
                        <FONT CLASS="space4pix"><BR></FONT>
					</TD>
				</TR>
            </TABLE>

<%      } else if (order.getOrderStatus().equals(EnumSaleStatus.AUTHORIZATION_FAILED) || order.getOrderStatus().equals(EnumSaleStatus.CHARGEBACK) ){   %>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<TR>
					<TD WIDTH="100%"><b>Processor Contact Info</b><FONT CLASS="space4pix"><BR></FONT></TD>
				</TR>
				<TR>
					<TD WIDTH="100%">
						<%  if (EnumCardType.VISA.equals(paymentMethod.getCardType()) || EnumCardType.MC.equals(paymentMethod.getCardType())) { %>
						Visa/Mastercard<br>
                        1-800-884-2018<br>
                        Merchant ID: 67212800015<br>
                    <%  } else if (EnumCardType.AMEX.equals(paymentMethod.getCardType())) {  %>
                        American Express<br>
                        1-800-528-2121<br>
                        Merchant ID: 6310498183<br>
                    <%  } else if (EnumCardType.DISC.equals(paymentMethod.getCardType())) {  %>
                        Discover<br>
                        1-800-347-1111<br>
                        Merchant ID: 601101312821927<br>
                    <%  }   %>
                    <FONT CLASS="space4pix"><BR></FONT>
					</TD>
				</TR>
			</TABLE>
<%      } 
    }
%>
			<%-- =========== END LOGIN / MERCHANT INFO SUB-SECTION =========== --%>
		</TD>
	</TR>
</TABLE>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ END OPTION 1 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%		int cellCounter = 0; %>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ BEGIN OPTION 2 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR BGCOLOR="#6666CC">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="98%" CLASS="text8whitebold">&nbsp;&nbsp;OPTION 2: EDIT ORIGINAL CARD/SELECT DIFFERENT CARD</TD>
	</TR>
</TABLE>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
	<form name="authorize_new_payment" METHOD="POST" action="">
	<input type="hidden" name="action" value="<%= EnumSaleStatus.SETTLEMENT_FAILED.equals(order.getOrderStatus()) ? "new_payment_for_failed_settlement" : "new_authorization" %>">
	<input type="hidden" name="payment_id" value="">
<script language="JavaScript" type="text/javascript">
	function useCard(paymentId) {
		document.authorize_new_payment.payment_id.value=paymentId;
	}
</script>
	<TR VALIGN="TOP">
		<logic:iterate id="payment" collection="<%= erpCustomer.getPaymentMethods() %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%		boolean showDeleteButton = (erpCustomer.getPaymentMethods().size() > 1)? true : false;
		String paymentId = ((ErpPaymentMethodModel)payment).getPK().getId();%>
		<TD WIDTH="30%" align="left">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
				<tr><td colspan="2" align="center"><INPUT TYPE="checkbox" name="waive_<%= paymentId %>" value="true"> <B>Waive $2 payment exception fee<BR></td></tr>
				<TR>
					<TD WIDTH="35%" ALIGN="RIGHT">&nbsp;&nbsp;</TD>
					<TD WIDTH="65%"><INPUT TYPE="submit" name="use_<%= paymentId %>" onClick="javascript:useCard('<%= paymentId %>')" class="submit" value="SELECT &amp; SUBMIT"><BR><BR></TD>
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
						editPage = "edit_credit_card.jsp";
					} else if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) {
						editPage = "edit_echeck.jsp";
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

<%-- ~~~~~~~~~~~~~~~~~~~~~~~ BEGIN OPTION 3 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR BGCOLOR="#6666CC">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="98%" CLASS="text8whitebold">&nbsp;&nbsp;OPTION 3: CANCEL ORDER</TD>
	</TR>
</TABLE>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order_detail">
	<form name="cancel_order" method="POST" action="">
	<TR VALIGN="TOP">
		<TD WIDTH="4%"><BR></TD>
		<TD WIDTH="96%">
			<TABLE WIDTH="60%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<input type="hidden" name="action" value="cancel">
				<TR>
					<TD><B>Select Reason Code:</B>&nbsp;&nbsp;<BR><FONT CLASS="space4pix"><BR></FONT></TD>
				</TR>
				<TR>
					<TD>
						&nbsp;&nbsp;
						<SELECT name="cancel_reason">
							<OPTION VALUE="">select reason</OPTION>
							<OPTION VALUE="no_new_card" <%= "no_new_card".equals( request.getParameter("cancel_reason") ) ? "SELECTED" : "" %>>customer declined to issue another credit card</OPTION>
							<OPTION VALUE="insuff_funds" <%= "insuff_funds".equals( request.getParameter("cancel_reason") ) ? "SELECTED" : "" %>>customer acknowledged insufficient funds</OPTION>
							<OPTION VALUE="cust_changed_mind" <%= "cust_changed_mind".equals( request.getParameter("cancel_reason") ) ? "SELECTED" : "" %>>customer changed mind</OPTION>
                            <OPTION VALUE="card_lost_stolen" <%= "card_lost_stolen".equals( request.getParameter("cancel_reason") ) ? "SELECTED" : "" %>>customer reported lost/stolen credit card</OPTION>
						</SELECT>
						<FONT CLASS="space4pix"><BR><BR></FONT>
					</TD>
				</TR>
				<TR>
					<TD><B>Additional Notes:</B>&nbsp;&nbsp;<BR><FONT CLASS="space4pix"><BR></FONT></TD>
				</TR>
				<TR>
					<TD>&nbsp;&nbsp;<TEXTAREA NAME="cancel_notes" WRAP="virtual" COLS="50" ROWS="10"><%= request.getParameter("cancel_notes") %></TEXTAREA></TD>
				</TR>
			</TABLE><FONT CLASS="space4pix"><BR></FONT>
			<input onClick="return confirmCancelOrder();" type="submit" class="submit" value="CANCEL ORDER"><br><br>
		</TD>
	</TR>
	</form>
</TABLE>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~ END OPTION 3 SECTION ~~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%-- THIS IS AN AWFUL HACK THAT SHOULD BE FIXED SOON ... --%>
	<form name="delete_payment" method="POST" action="/your_account/delete_payment.jsp">
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

