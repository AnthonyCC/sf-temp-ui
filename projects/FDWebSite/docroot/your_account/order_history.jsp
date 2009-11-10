<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.EnumSaleType" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>

<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
private String getTimeslotString(Calendar startTimeCal, Calendar endTimeCal){
		StringBuffer sb = new StringBuffer();
		int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
		sb.append(startHour==12 ? "noon" : (startHour > 12 ? startHour - 12+"": startHour+""));
		int startMin = startTimeCal.get(Calendar.MINUTE);
		if(startMin != 0){
			sb.append(":"+startMin);
		}
		sb.append(startTimeCal.get(Calendar.AM_PM)==1?" PM":" AM");
		
		sb.append(" - ");
		
		int endHour = endTimeCal.get(Calendar.HOUR_OF_DAY); 
		sb.append(endHour == 0 ? "12" : (endHour == 12 ? "noon" : (endHour > 12 ? endHour - 12+"" : endHour+"")));
		int endMin = endTimeCal.get(Calendar.MINUTE);
		if(endMin != 0){
			sb.append(":"+endMin);
		}
		sb.append(endTimeCal.get(Calendar.AM_PM)==1?" PM":" AM");
		return sb.toString();
	}
%>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>	

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Your Orders</tmpl:put>

    <tmpl:put name='content' direct='true'>
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
	//DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	
	boolean printHeader = false;
%>
<!-- error message handling here -->
<TABLE WIDTH="675" ALIGN="CENTER" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%	if (orderHistoryInfo.size() > 0) {
		int orderNumber = 0;
		int rowCounter = 0;
%>
	<logic:iterate id="orderInfo" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
		<% if (orderNumber == 0){%>
		<% if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) {
			Calendar dlvStart = Calendar.getInstance();
            dlvStart.setTime(orderInfo.getDeliveryStartTime());
            Calendar dlvEnd = Calendar.getInstance();
            dlvEnd.setTime(orderInfo.getDeliveryEndTime());
			String deliveryTime = getTimeslotString(dlvStart, dlvEnd);
			String errorMsg= "We were unable to deliver your order (#"+orderInfo.getErpSalesId()+") scheduled for between "+deliveryTime+" on "+dateFormatter.format( orderInfo.getRequestedDate() )+". Please contact us as soon as possible at "+user.getCustomerServiceContact()+" to reschedule delivery.";
			ActionResult result = new ActionResult();
	%>
			<tr><td><%@ include file="/includes/i_error_messages.jspf"%></td></tr>
		<% } %>
<tr><td class="text11">
<font class="title18">Your Orders</font><br>
Please review your orders. To check the status of an order, click on the order number.<BR></td>
</tr></table>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>
<TABLE WIDTH="690" ALIGN="CENTER" BORDER="0" CELLPADDING="2" CELLSPACING="0">
	<tr>
		<td class="text10bold" bgcolor="#DDDDDD" WIDTH="75">Order #</td>
		<td class="text10bold" bgcolor="#DDDDDD" WIDTH="175">&nbsp;&nbsp;&nbsp;&nbsp;Delivery Date</td>
                <td class="text10bold" bgcolor="#DDDDDD" WIDTH="140">Delivery Type</td>
		<td class="text10bold" bgcolor="#DDDDDD" WIDTH="75" align="right">Order Total</td>
		<td bgcolor="#DDDDDD"><img src="/media_stat/images/layout/clear.gif" width="40" height="1" alt="" border="0"></td>
		<td class="text10bold" bgcolor="#DDDDDD" WIDTH="90">Order Status</td>
		<td class="text10bold" bgcolor="#DDDDDD" WIDTH="250">Details</td>
	</tr>
	<% } orderNumber++; %>
	<tr bgcolor="<%= (rowCounter++ % 2 == 0) ? "#FFFFFF" : "#EEEEEE" %>">
	
	<%
     String orderDetailsUrl = "";
	 if(orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD)) { 
        orderDetailsUrl = "/your_account/gc_order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
     }else  if(orderInfo.getSaleType().equals(EnumSaleType.DONATION)) { 
        orderDetailsUrl = "/your_account/rh_order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
     }else {
       orderDetailsUrl = "/your_account/order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
     }
    %>
     
	    <td><a href="<%= orderDetailsUrl %>"><%= orderInfo.getErpSalesId() %></a></td>
	
		<td class="text10"><%= dateFormatter.format( orderInfo.getRequestedDate() ) %></td>
        <%
            String deliveryType = "";
            if(orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD)) {
                deliveryType = "Gift Card";
            } else if(orderInfo.getSaleType().equals(EnumSaleType.DONATION)) {
                deliveryType = "Robin Hood Donation";
            }else {
               deliveryType = orderInfo.getDeliveryType().getName();
            }
        %>
                <td class="text10"><%= deliveryType %></td>
		<td class="text10" align=right><%= currencyFormatter.format( orderInfo.getTotal() ) %>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td></td>
        <%
            String status = "";
            if(orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD) || orderInfo.getSaleType().equals(EnumSaleType.DONATION)) {
                status = orderInfo.isPending() ? "In Process" : "Completed";
            } else {
                status = orderInfo.getOrderStatus().getDisplayName();
            }
            
        %>
		<td><%= status %></td>
		<td>
			<a href="<%= orderDetailsUrl %>"><%= (EnumSaleStatus.SUBMITTED.equals(orderInfo.getOrderStatus()) || EnumSaleStatus.AUTHORIZED.equals(orderInfo.getOrderStatus()) || EnumSaleStatus.AVS_EXCEPTION.equals(orderInfo.getOrderStatus())) && !orderInfo.getSaleType().equals(EnumSaleType.DONATION) ? "View/Modify" : "View" %></a>
            <% if (!orderInfo.isPending() && !orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD)&& !orderInfo.getSaleType().equals(EnumSaleType.DONATION)) { %>
            | <a href="/quickshop/shop_from_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>">Shop From This Order</a>
            <% } %>
           
           
		</td>
	</tr>
	</logic:iterate>
<% 	} else { %>
	<tr>
		<td WIDTH="675" colspan="5" align="center"><b>You have not yet placed an order with us.</b></td>
	</tr>
<% 	} // end if-else orderHistory > 0%>
</fd:OrderHistoryInfo>
</TABLE>
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>
<%--<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
<tr><td colspan="2"><%@include file="/includes/i_footer_account.jspf" %></td></tr>--%>
</TABLE>

</tmpl:put>
</tmpl:insert>