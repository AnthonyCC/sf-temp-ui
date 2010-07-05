<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.EnumSaleType" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>

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
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />
<%
Date ccrNow = new Date();
if (user.isEligibleForClientCodes()) {
	request.setAttribute("__yui_load_calendar__", Boolean.TRUE);
	request.setAttribute("__fd_load_cc_report__", Boolean.TRUE);
	request.setAttribute("__fd_cc_report_now__", ccrNow);
}
%>
<tmpl:insert template='/common/template/dnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Your Orders</tmpl:put>

    <tmpl:put name='content' direct='true'>
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
	NumberFormat currencyFormatter = JspMethods.currencyFormatter;
	
	boolean printHeader = false;
%>
<!-- error message handling here -->
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%	if (orderHistoryInfo.size() > 0) {
		int orderNumber = 0;
		int rowCounter = 0;

		// first orderInfo
		FDOrderInfoI firstOrderInfo = (FDOrderInfoI) orderHistoryInfo.iterator().next();
		if (EnumSaleStatus.REFUSED_ORDER.equals(firstOrderInfo.getOrderStatus()) ) {
			Calendar dlvStart = Calendar.getInstance();
            dlvStart.setTime(firstOrderInfo.getDeliveryStartTime());
            Calendar dlvEnd = Calendar.getInstance();
            dlvEnd.setTime(firstOrderInfo.getDeliveryEndTime());
			String deliveryTime = getTimeslotString(dlvStart, dlvEnd);
			String errorMsg= "We were unable to deliver your order (#"+firstOrderInfo.getErpSalesId()+") scheduled for between "+deliveryTime+" on "+dateFormatter.format( firstOrderInfo.getRequestedDate() )+". Please contact us as soon as possible at "+user.getCustomerServiceContact()+" to reschedule delivery.";
			ActionResult result = new ActionResult();
%>
<TABLE WIDTH="675" ALIGN="CENTER" BORDER="0" CELLPADDING="0" CELLSPACING="0">
	<tr>
		<td><%@ include file="/includes/i_error_messages.jspf"%></td>
	</tr>
</table>
<%
		}
%>
<%-- Order Info --%>
<div class="text11" style="width: 675px; text-align: left;">
	<font class="title18">Your Orders</font><br>
	Please review your orders. To check the status of an order, click on the order number.<BR>
</div>
<div style="height: 1px; width: 675px; background-color: #ff9933; margin-top: 8px; margin-bottom: 8px;"></div>
<!-- client codes begin -->
<% if (user.isEligibleForClientCodes()) { 
	DateFormat usDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat pageDateFormat = new SimpleDateFormat("MM/yyyy");
	DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Calendar ccrCal = Calendar.getInstance();
	ccrCal.setTime(ccrNow);
	ccrCal.add(Calendar.MONTH, -13);
	String ccrFirstDate = usDateFormat.format(ccrCal.getTime());
	String ccrFirstIso = isoDateFormat.format(ccrCal.getTime());
%>
<div style="width: 675px; text-align: right; overflow: hidden;">
<table border="0" cellspacing="0" cellpadding="0" style="text-align: left; float: right;">
	<tr>
		<td class="text11" style="text-align: right; vertical-align: middle; font-weight: bold;">
		Export client codes for all orders delivered from&emsp;
		</td>
		<td class="text11"><input id="ccrep_start" class="text11" style="background-color: #FFF;" size="10" readonly="readonly" autocomplete="off" value="<%= ccrFirstDate %>"><div id="ccrep_startCont" style="display: none; position: absolute; z-index: 2;">&nbsp;</div></td>
		<td class="text11">&emsp;</td>
		<td class="text11"><input id="ccrep_end" class="text11" style="background-color: #FFF;" size="10" readonly="readonly" autocomplete="off" value=""><div id="ccrep_endCont" style="display: none; position: absolute; z-index: 1;">&nbsp;</div></td>
		<td class="text11">&emsp;</td>
		<td class="text11" style="text-align: center; vertical-align: middle; font-weight: bold; width: 66px;">
		<a id="ccrep_action" href="/api/clientCodeReport?customer=<%= user.getIdentity().getErpCustomerPK() %>&start=<%= ccrFirstIso %>" style="text-decoration: none; outline: none;"><img src="/media_stat/images/buttons/export.gif" width="64" height="15" style="border: none;"></a>
		</td>
	</tr>
</table>
</div>
<div style="height: 1px; width: 675px; background-color: #ff9933; margin-top: 8px; margin-bottom: 8px;"></div>
<!-- client codes end -->
<% } %>
<br>
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
	
	<fd:ListStandingOrders id="solist">
		<% for (FDStandingOrder so : solist) { %>
			<tr bgcolor="<%= (rowCounter++ % 2 == 0) ? "#FFFFFF" : "#EEEEEE" %>">
				<td><div style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 75px;">
					<a title="<%= so.getCustomerListName() %>" href="<%= FDURLUtil.getStandingOrderLandingPage(so, null) %>"><%= so.getCustomerListName() %></a>
				</div></td>
				<td class="text10"><%= StandingOrderHelper.getDeliveryDate(so,true) %></td>
				<td class="text10">Corporate Delivery</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><%= so.isError() ? "Failure" : "Active" %></td>
				<td><a href="<%= FDURLUtil.getStandingOrderLandingPage(so, null) %>">Edit Schedule or Contents</a></td>
			</tr>
		<% } %>
	</fd:ListStandingOrders>

<%
for (FDOrderInfoI orderInfo : orderHistoryInfo) {
	orderNumber++;
%>	<tr bgcolor="<%= (rowCounter++ % 2 == 0) ? "#FFFFFF" : "#EEEEEE" %>">
<%
    String orderDetailsUrl = "";
	if (orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD)) { 
		orderDetailsUrl = "/your_account/gc_order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
    } else if (orderInfo.getSaleType().equals(EnumSaleType.DONATION)) { 
		orderDetailsUrl = "/your_account/rh_order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
    } else {
		orderDetailsUrl = "/your_account/order_details.jsp?orderId="+ orderInfo.getErpSalesId() ;
    }
%>	    <td><a href="<%= orderDetailsUrl %>"><%= orderInfo.getErpSalesId() %></a></td>
		<td class="text10"><%= dateFormatter.format( orderInfo.getRequestedDate() ) %></td>
<%
	String deliveryType = "";
	if (orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD)) {
		deliveryType = "Gift Card";
	} else if(orderInfo.getSaleType().equals(EnumSaleType.DONATION)) {
		deliveryType = "Robin Hood Donation";
	} else {
		deliveryType = orderInfo.getDeliveryType().getName();
	}
%>
        <td class="text10"><%= deliveryType %></td>
		<td class="text10" align=right><%= currencyFormatter.format( orderInfo.getTotal() ) %>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td></td>
<%
	String status = "";
	if (orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD) || orderInfo.getSaleType().equals(EnumSaleType.DONATION)) {
		status = orderInfo.isPending() ? "In Process" : "Completed";
	} else {
		status = orderInfo.getOrderStatus().getDisplayName();
	}
%>		<td><%= status %></td>
		<td>
			<a href="<%= orderDetailsUrl %>"><%= orderInfo.isModifiable() ? "View/Modify" : "View" %></a>
            <% if (orderInfo.isShopFromThisOrder()) { %>
            | <a href="/quickshop/shop_from_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>">Shop From This Order</a>
            <% } %>
		</td>
	</tr>
<%
} // orderInfo : orderHistoryInfo
%>
</TABLE>
<% 	} else { %>
<div style="width: 675px; text-align: center; font-weight: bold;">You have not yet placed an order with us.</div>
<% 	} // end if-else orderHistory > 0%>
</fd:OrderHistoryInfo>
<br>
<br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
	<tr VALIGN="TOP">
		<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
		<td WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a><BR>
			from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0">
		</td>
	</tr>
</TABLE>

</tmpl:put>
</tmpl:insert>