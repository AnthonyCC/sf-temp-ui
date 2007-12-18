<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<% 	String orderId = request.getParameter("orderId"); %>
<fd:ModifyOrderController orderId="<%=orderId %>" result="result" successPage='<%= "/your_account/cancel_order_confirm.jsp?orderId=" + orderId %>'>
<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Cancel Order</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
	ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
	
	FDOrderI cartOrOrder = FDCustomerManager.getOrder(orderId);
	
	if (allowCancelOrder.booleanValue() && cartOrOrder != null) {
	
        StringBuffer custName = new StringBuffer(50);
        custName.append(customerModel.getFirstName());
        if (customerModel.getMiddleName()!=null && customerModel.getMiddleName().trim().length()>0) {
            custName.append(" ");
            custName.append(customerModel.getMiddleName());
            custName.append(" ");
        }
        custName.append(customerModel.getLastName());
		
		//
		// get delivery info
		//
		ErpAddressModel dlvAddress = cartOrOrder.getDeliveryAddress();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MM/dd/yy");
        String fmtDlvDateTime = dateFormatter.format(cartOrOrder.getDeliveryReservation().getStartTime()).toUpperCase();
        Calendar dlvStart = Calendar.getInstance();
        dlvStart.setTime(cartOrOrder.getDeliveryReservation().getStartTime());
        Calendar dlvEnd =   Calendar.getInstance();
        dlvEnd.setTime(cartOrOrder.getDeliveryReservation().getEndTime());
		int startHour =  dlvStart.get(Calendar.HOUR_OF_DAY);
		int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY); 
		
		String sStartHour = startHour==12? "noon" : (startHour>12 ? ""+(startHour-12) : ""+startHour);
		String sEndHour = endHour==0 ? "12 am" : (endHour==12 ? "noon" : (endHour>12 ? (endHour-12)+" pm" : endHour+" am"));
		
		//
		// get payment info
		//
		ErpPaymentMethodI paymentMethod =(ErpPaymentMethodI) cartOrOrder.getPaymentMethod();
		
		//
		// get order line info
		//
%>
<!-- error message handling here -->
<table width="675" border="0" cellpadding="0" cellspacing="0">
	<tr><td class="title18" colspan="3">Cancel Order # <%= orderId %> ?</td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="10" height="6"></td><td><img src="/media_stat/images/layout/clear.gif" width="465" height="6"></td><td><img src="/media_stat/images/layout/clear.gif" width="200" height="6"></td></tr>
	<tr bgcolor="#FF9933">
		<td class="text10w" colspan="2" height="16">&nbsp;&nbsp;<img src="/media_stat/images/template/youraccount/currently_scheduled.gif" width="124" height="8" border="0" alt="CURRENTLY SCHEDULED" vspace="2" align="absbottom">&nbsp;&nbsp;<%=fmtDlvDateTime%>@<%=sStartHour%>-<%=sEndHour%></td>
		<td align="right" class="text11wbold">Estimated Total: <%= currencyFormatter.format(cartOrOrder.getTotal()) %>&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="2" class="text13"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>You are about to cancel this order. If you cancel it, you will not receive a delivery and your account will not be charged. We will save the items from this order in Quickshop.<br><br>If you would like to change this order instead, <a href="/your_account/modify_order.jsp?orderId=<%= orderId %>">click here</a>.<br><br>For full details of our cancellation policy, <a href="javascript:popup('/help/faq_index.jsp?show=shopping#question7','large')">click here</a>.
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><div align="center">
			<form name="cancel_order" method="POST" action="/your_account/cancel_order.jsp">
			<input type="hidden" name="action" value="cancel">
			<input type="hidden" name="orderId" value="<%= orderId %>">
			<input type="image" src="/media_stat/images/buttons/cancel_this_order_now.gif" width="142" height="16" border="0" alt="CANCEL ORDER">
			</form></div><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
<tr><td bgcolor="#CCCCCC" colspan="3"><img src="/media_stat/images/layout/clear.gif" width="675" height="1" border="0"></td></tr>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			    <td rowspan="2"><a href="order_details.jsp?orderId=<%= orderId %>"><img src="/media_stat/images/template/youraccount/cross.gif" border="0"></a></td>
			    <td><a href="order_details.jsp?orderId=<%= orderId %>"><img src="/media_stat/images/template/youraccount/do_not_cancel_order.gif" border="0"></a></td>
			</tr>
			<tr><td>and deliver as originally specified</td></tr>
			</table></td></tr>
<%	} %>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="20"><br><b>Having Problems?</b><br><%@ include file="/includes/i_footer_account.jspf" %></td></tr></table>
</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>
