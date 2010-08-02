<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%	String orderId = request.getParameter("orderId"); %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Order <%= orderId %> Modify</tmpl:put>

<fd:GetOrder id='order' saleId='<%= orderId %>'>
<fd:ModifyOrderController action="modify" orderId="<%= orderId %>" result="result" successPage="/checkout/checkout_review_items.jsp">

<%!
	java.text.SimpleDateFormat modDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE MMMM d, yyyy");
%>

<%	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);%>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>

<%	if (order != null) {
		//
		// get delivery info
		//
		ErpAddressModel dlvAddress = order.getDeliveryAddress();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MM/dd/yy");
        String fmtDlvDateTime = CCFormatter.formatDate(order.getDeliveryReservation().getStartTime()).toUpperCase();
        Calendar dlvStart = Calendar.getInstance();
        dlvStart.setTime(order.getDeliveryReservation().getStartTime());
        Calendar dlvEnd =   Calendar.getInstance();
        dlvEnd.setTime(order.getDeliveryReservation().getEndTime());
		int startHour =  dlvStart.get(Calendar.HOUR_OF_DAY);
		int endHour = dlvEnd.get(Calendar.HOUR_OF_DAY); 
		
		String sStartHour = startHour==12? "noon" : (startHour>12 ? ""+(startHour-12) : ""+startHour);
		String sEndHour = endHour==0 ? "12 am" : (endHour==12 ? "noon" : (endHour>12 ? (endHour-12)+" pm" : endHour+" am"));
%>
<form name="modify_order" method="POST" action="">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="border: solid 4px #6699CC; border-top: solid 4px #FF9900;">
		<tr height="25" class="order_modify">
			<td><b>CURRENTLY SCHEDULED</FONT>&nbsp;&nbsp;<%=fmtDlvDateTime%>&nbsp;@&nbsp;<%=sStartHour%>-<%=sEndHour%></b></td>
			<td align="right"><b>Estimated Total: <%= CCFormatter.formatCurrency(order.getTotal()) %></b>&nbsp;&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="order_modify_content">
				<span class="order_modify_warning">You are about to change this order. Please note that the price and availability of some items may change.<br />
	<%
				FDCartModel originalCart = FDCustomerManager.recognize(user.getIdentity()).getShoppingCart();
	%>
			<%if(originalCart.numberOfOrderLines() > 0){%>			
				We won't forget the items that are in your cart now - you'll see them again as soon as you're done making changes.<br />
			<%}%>
			<blockquote><b>Checkout must be completed by <%= modDateFormat.format(order.getDeliveryReservation().getCutoffTime()) %>, or this order will be delivered as is.</b></blockquote>
				For full details of our order change policy, <a href="javascript:popup('/help/faq_index.jsp?show=shopping#question7','large')">click here</a>.<br /></span>
			</td>
			<td class="order_modify_content" align="right"><a href="javascript:modify_order.submit()" class="order_modify_proceed" style="width:200px;">CHANGE THIS ORDER</a><br />
				<br />
				<a href="<%= "/main/order_details.jsp?orderId=" + orderId %>" class="order_modify_cancel" style="width:200px;">CANCEL CHANGES TO ORDER</a>
			</td>
		</tr>
	</table>
</form>
<%	} %>

</tmpl:put>
</fd:ModifyOrderController>
</fd:GetOrder>
</tmpl:insert>