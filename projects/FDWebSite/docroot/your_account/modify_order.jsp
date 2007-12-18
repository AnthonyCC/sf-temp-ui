<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import ='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%	String orderId = request.getParameter("orderId"); %>
<fd:ModifyOrderController orderId="<%= orderId %>" result="result" successPage='/view_cart.jsp'>
<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Modify Order</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	java.text.SimpleDateFormat modDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE MMMM d, yyyy");
	
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
	ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
	
	FDOrderI cartOrOrder = FDCustomerManager.getOrder(identity, orderId);
	FDReservation reservation = cartOrOrder.getDeliveryReservation();
	
	if (allowModifyOrder.booleanValue() && cartOrOrder != null) {
	
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
        String fmtDlvDateTime = dateFormatter.format(reservation.getStartTime()).toUpperCase();
        Calendar dlvStart = Calendar.getInstance();
        dlvStart.setTime(reservation.getStartTime());
        Calendar dlvEnd =   Calendar.getInstance();
        dlvEnd.setTime(reservation.getEndTime());
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
	<tr><td class="title18" colspan="3">Change Order # <%= orderId %> ?</td>
	</tr>
	<tr><td><img src="/media_stat/images/layout/clear.gif" width="10" height="6"></td><td><img src="/media_stat/images/layout/clear.gif" width="465" height="6"></td><td><img src="/media_stat/images/layout/clear.gif" width="200" height="6"></td></tr>
	<tr bgcolor="#FF9933">
		<td class="text10w" colspan="2" height="16">&nbsp;&nbsp;<img src="/media_stat/images/template/youraccount/currently_scheduled.gif" width="124" height="8" border="0" alt="CURRENTLY SCHEDULED" vspace="2" align="bottom">&nbsp;&nbsp;<%=fmtDlvDateTime%>@<%=sStartHour%>-<%=sEndHour%></td>
		<td align="right" class="text11wbold">Estimated Total: <%= currencyFormatter.format(cartOrOrder.getTotal()) %>&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="2" class="text13"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
		You are about to change this order. Please note that the price and availability of some items may change. Changing the order date may cause date-restricted promotional items to be removed.<br><br>
<%
			FDUserI currentUser = (FDUserI) session.getAttribute(SessionName.USER);
			// !!! huh?
			//FDCartModel originalCart = FDCustomerManager.loadShoppingCart(identity);
			FDCartModel originalCart = FDCustomerManager.recognize(identity).getShoppingCart();
%>		
		<%if(originalCart.numberOfOrderLines() > 0){%>			
			We won't forget the items that are in your cart now - you'll see them again as soon as you're done making changes.<br><br> 
		<%}%>
		<b>You must complete checkout by <%= modDateFormat.format(reservation.getCutoffTime()) %>, or this order will be delivered as is.</b><br><br>For full details of our order change policy, <a href="javascript:popup('/help/faq_index.jsp?show=shopping#question7','large')">click here</a>.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><div align="center">			
		<form name="modify_order" method="POST" action="/your_account/modify_order.jsp">
			<input type="hidden" name="orderId" value="<%= request.getParameter("orderId") %>">
			<input type="hidden" name="action" value="modify">
			<input type="image" src="/media_stat/images/buttons/change_this_order_now.gif" width="145" height="16" border="0" alt="MAKE CHANGES TO ORDER">
		</form></div><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
<tr><td bgcolor="#CCCCCC" colspan="3"><img src="/media_stat/images/layout/clear.gif" width="675" height="1" border="0"></td></tr>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><table cellspacing="0" cellpadding="0" border="0">
			<tr><td rowspan="2"><a href="/your_account/order_details.jsp?orderId=<%= request.getParameter("orderId") %>"><img src="/media_stat/images/template/youraccount/cross.gif" border="0"></a></td>
			    <td><a href="/your_account/order_details.jsp?orderId=<%= request.getParameter("orderId") %>"><img src="/media_stat/images/template/youraccount/do_not_change_order.gif" width="122" height="8" border="0"></a></td>
			</tr>
			<tr><td>and deliver as originally specified</td></tr>
			</table></td></tr>
<%	} %>
<tr><td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="20"><br><b>Having Problems?</b><br><%@ include file="/includes/i_footer_account.jspf" %></td></tr></table>
</tmpl:put>
</tmpl:insert>
</fd:ModifyOrderController>