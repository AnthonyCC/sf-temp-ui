<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.CrmCaseQueue"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%CrmSession.invalidateCachedOrder((HttpSession) request.getSession());%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Order Preview</tmpl:put>
<fd:FDShoppingCart id='cart' result="result">

<crm:GetCurrentAgent id='currentAgent'>

<fd:CheckoutController actionName="submitOrder" result="result" successPage="checkout_confirmation.jsp" ccdProblemPage="checkout_select_addr_payment.jsp">
<%	
    FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
    FDCartI order = (FDCartI) cart;
    ErpAddressModel dlvAddress = order.getDeliveryAddress();
	ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
	boolean returnPage = false;
	String orderId = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	
%>
<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

			<% if (!user.isOrderMinimumMet()) { %>
				<div class="error">NOTE: Order amount is below the minimum $<%= FDUserI.MINIMUM_ORDER_AMOUNT %></div>
			<% } %>
			<fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='order_amount_fraud' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
				<div class="error">FreshDirect requires a $<%= FDUserI.MINIMUM_ORDER_AMOUNT %> minimum for each order. Current cart total is: <u><%= CCFormatter.formatCurrency(cart.getTotal()) %></u></div>
			</fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='processing_order' id='errorMsg'>
				<div class="error">Your Order has already been submitted.</div>
			</fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='invalid_deliverypass' id='errorMsg'>
				<span class="error"><%= errorMsg %></span>
		         </fd:ErrorHandler>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="checkout_header<%= user.isActive() ? "" : "_warning" %>">
<form name="submit_order" method="POST" action="">
	<TR>
	<TD>
        &nbsp;Step 4 of 4: Review & Submit Order 
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is 
            <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a>
        <% } %>
        <span class="checkout_header_detail">&nbsp; | &nbsp;<input type="checkbox" name="silent_mode"> Don't send email invoice</span>
    </TD>
	</TR>
</TABLE>

<%@ include file="/includes/i_modifyorder.jspf" %>
<%@ include file="/includes/i_promo_eligible.jspf"%>
<%-- order summary preview --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order_summary_text">
	<tr class="order_summary_header">
		<td width="9%">Order #</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="12%">Delivery</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="9%">Status</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="8%">Amount</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="9%">Created</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="9%">by</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="7%">via</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="9%">Modified</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="9%">by</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="7%">via</td>
		<td class="order_summary_separator" rowspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
		<td width="12%" align="center">Credits Issued?</td>
	</tr>
	<tr valign="top">
		<td>--</td>
		<td><%=CCFormatter.formatDeliveryDate(cart.getDeliveryReservation().getStartTime())%></td>
		<td>Pending</td>
		<td><%=CCFormatter.formatCurrency(order.getTotal())%></td>
		<td><%=CCFormatter.formatDateTime(Calendar.getInstance().getTime())%></td>
		<td><%=currentAgent.getUserId()%></td>
		<td>Telephone</td>
		<td>--</td>
		<td>--</td>
		<td>--</td>
		<td align="center">--</td> 
	</tr>
</table>
<%-- order summary preview --%>
<div class="content_scroll" style="height: 72%; padding-top: 0px;">
<%
    boolean showPaymentButtons = true;
	boolean showAddressButtons = !(user.getShoppingCart().getDeliveryAddress() instanceof ErpDepotAddressModel);
	boolean showDeleteButtons = false; 
    boolean displayDeliveryInfo = true;
%>
	
<%@ include file="/includes/i_order_dlv_payment.jspf"%>
<hr class="black1px">
<%	Collection lineComplaints = Collections.EMPTY_LIST;
	Collection deptComplaints = Collections.EMPTY_LIST;
	Collection miscComplaints = Collections.EMPTY_LIST;
	Collection complaints = Collections.EMPTY_LIST; %>
	
<%	boolean showEditOrderButtons = true;
	boolean showFeesSection = false;
	boolean cartMode = true; %>
	
<%@ include file="/includes/i_preview_cart_details.jspf"%>

<%@ include file="/includes/i_customer_service_message.jspf"%>
<div align="right" class="checkout_header" style="padding: 1px; margin="0px""><a href="javascript:submit_order.submit()" class="checkout">PLACE ORDER NOW >></a></div>

</form>
</div>
</tmpl:put>

</fd:CheckoutController>
</crm:GetCurrentAgent>
</fd:FDShoppingCart>
</tmpl:insert>
