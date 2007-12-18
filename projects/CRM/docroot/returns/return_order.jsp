<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
	// Get the OrderModel using the orderId from the request
	
	String orderId = request.getParameter("orderId");
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	String successPage = "/main/order_details.jsp?orderId="+orderId;
	String action = request.getParameter("action");
	if(action == null){
		action = "add_return";
	}else{
		action.trim();
	}
%>
<fd:returnController result="result" actionName="<%=action%>" orderNumber="<%=orderId%>" successPage="<%=successPage%>">
<fd:GetOrder id='order' saleId='<%= orderId %>'>
<%
	//
	// Get customer info from the order
	//
	String erpCustId = order.getCustomerId();
	FDCustomerModel _fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(erpCustId);
	FDIdentity identity = new FDIdentity(erpCustId, _fdCustomer.getPK().getId());
%>
<fd:LoadUser newIdentity="<%= identity %>" />
<%
	// Get DELIVERY ADDRESS info, PAYMENT info, APPLIED CREDIT info
	
	ErpAddressModel dlvAddress = order.getDeliveryAddress();
	ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
	Collection appliedCredits = order.getAppliedCredits();
%>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Order <%= orderId%> Return</tmpl:put>
		
<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>

<TABLE width="100%">
<form name="return_order" action="return_order.jsp?orderId=<%=orderId%>&action=<%=action%>" method="POST">
	<input type="hidden" name="orderId" value="<%= orderId %>">
	<input type="hidden" name="action" value="<%=action%>">
	 <% if(order.containsDeliveryPass()) { %>
	 	<tr align="top">
			<td class="blue_band_text" align="center">This Order contains a DeliveryPass. Do Not Return the DeliveryPass.</td> 	
	 	</tr>
 	<% } %>
</TABLE>

<%@ include file="/includes/order_summary.jspf"%>

<%
	boolean showPaymentButtons = false;
	boolean showAddressButtons = false;
	boolean showDeleteButtons = false;
%>

<div class="content_scroll" style="height: 15%; border-bottom: 1px solid;">
<%@ include file="/includes/i_order_dlv_payment.jspf"%>
</div>
<div class="sub_nav">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0">
	<TR>
		<TD align="center">
		<crm:GetCurrentAgent id="agent">
		<%if (agent.isSupervisor() && ("approve_return").equalsIgnoreCase(action)) {%>
			<input type="submit" class="submit" value="APPROVE RETURN">
		<% } else { %>
			<input type="submit" class="submit" value="PROCESS RETURN" onClick="return processReturn('return_order')">
		<% } %>
		</crm:GetCurrentAgent>
		</TD>
	</TR>
</TABLE>
</div>
<%@ include file="/includes/i_return_cart_details.jspf"%>
<table></form></table>
</tmpl:put>
</tmpl:insert>
</fd:GetOrder>
</fd:returnController>