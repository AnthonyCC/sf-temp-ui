<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.EnumSaleType" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.DateUtil' %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" />

<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String template = "/common/template/dnav.jsp";
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
	}
%>

<% //expanded page dimensions
	final int W_YA_ORDER_HISTORY_TOTAL = 970;
%>

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
<%
Date ccrNow = new Date();
if (user.isEligibleForClientCodes()) {
	request.setAttribute("__yui_load_calendar__", Boolean.TRUE);
	request.setAttribute("__fd_load_cc_report__", Boolean.TRUE);
	request.setAttribute("__fd_cc_report_now__", ccrNow);
}
%>
<tmpl:insert template='<%= template %>'>

<%--     <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Your Orders</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Account - Your Orders" pageId="order_history"></fd:SEOMetaTag>
	</tmpl:put>

    <tmpl:put name='content' direct='true'>
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
	
	boolean printHeader = false;
%>
<!-- error message handling here -->
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%	if (orderHistoryInfo.size() > 0) {
		String modifyingOrderId = FDUserUtil.getModifyingOrderId(user);
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
<TABLE WIDTH="<%= (mobWeb)? "100%" : W_YA_ORDER_HISTORY_TOTAL %>" ALIGN="CENTER" BORDER="0" CELLPADDING="0" CELLSPACING="0">
	<tr>
		<td><%@ include file="/includes/i_error_messages.jspf"%></td>
	</tr>
</table>
<%
		}
%>
<%-- Order Info --%>
<div class="text11 order_history_header" style="width: <%= (mobWeb)? "100%" : (W_YA_ORDER_HISTORY_TOTAL + "px") %>; text-align: left;">
	<div class="order_history_header_h"><font class="title18">Your Orders</font></div>
	<div class="order_history_header_desc">Please review your orders. To check the status of an order, click on the order number.</div>
</div>
<div class="NOMOBWEB" style="height: 1px; width: <%= (mobWeb)? "100%" : (W_YA_ORDER_HISTORY_TOTAL + "px") %>; background-color: #ff9933; margin-top: 8px; margin-bottom: 8px;"></div>
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
<div style="width: <%= (mobWeb)? "100%" : (W_YA_ORDER_HISTORY_TOTAL + "px") %>; text-align: right; overflow: hidden;">
<table border="0" cellspacing="0" cellpadding="0" style="text-align: left; float: right;">
	<tr>
		<td class="text11" style="text-align: right; vertical-align: middle; font-weight: bold;">
		Export client codes for all orders delivered from&nbsp;&nbsp;
		</td>
		<td class="text11"><input id="ccrep_start" class="text11" style="background-color: #FFF;" size="11" readonly="readonly" autocomplete="off" value="<%= ccrFirstDate %>"><div id="ccrep_startCont" style="display: none; position: absolute; z-index: 2;">&nbsp;</div></td>
		<td class="text11">&nbsp;&nbsp;</td>
		<td class="text11"><input id="ccrep_end" class="text11" style="background-color: #FFF;" size="11" readonly="readonly" autocomplete="off" value=""><div id="ccrep_endCont" style="display: none; position: absolute; z-index: 1;">&nbsp;</div></td>
		<td class="text11">&nbsp;&nbsp;</td>
		<td class="text11" style="text-align: center; vertical-align: middle; font-weight: bold; width: 66px;">
		<a id="ccrep_action" href="/api/clientCodeReport.jsp?customer=<%= user.getIdentity().getErpCustomerPK() %>&start=<%= ccrFirstIso %>" style="text-decoration: none; outline: none;"><img src="/media_stat/images/buttons/export.gif" width="64" height="15" style="border: none;"></a>
		</td>
	</tr>
</table>
</div>
<div style="height: 1px; width: <%= (mobWeb)? "100%" : (W_YA_ORDER_HISTORY_TOTAL + "px") %>; background-color: #ff9933; margin-top: 8px; margin-bottom: 8px;"></div>
<!-- client codes end -->
<% } %>
<br>
<TABLE WIDTH="<%= (mobWeb)? "100%" : W_YA_ORDER_HISTORY_TOTAL %>" ALIGN="CENTER" BORDER="0" CELLPADDING="2" CELLSPACING="0" class="order_history_table">
	<tr>
		<td class="text10bold order_history_table_id_header" bgcolor="#DDDDDD" <%= (mobWeb)? "" : "WIDTH='125'" %>>Order #</td>
		<td class="text10bold order_history_table_date_header" bgcolor="#DDDDDD" <%= (mobWeb)? "" : "WIDTH='175'" %>><%= (mobWeb)? "" : "&nbsp;&nbsp;&nbsp;&nbsp;" %>Delivery<%= (mobWeb)? "" : " Date" %></td>
		<% if(!mobWeb){ %><td class="text10bold" bgcolor="#DDDDDD" WIDTH="<%= (mobWeb)? "0" : "140" %>">Delivery Type</td><% } %>
		<td class="text10bold order_history_table_total_header" bgcolor="#DDDDDD" <%= (mobWeb)? "" : "WIDTH='75'" %> align="right"><%= (mobWeb)? "" : "Order " %>Total</td>
		<% if(!mobWeb){ %><td bgcolor="#DDDDDD"><img src="/media_stat/images/layout/clear.gif" width="40" height="1" alt="" border="0"></td> <% } %>
		<td class="text10bold order_history_table_status_header" bgcolor="#DDDDDD" <%= (mobWeb)? "" : "WIDTH='90'" %>><%= (mobWeb)? "" : "Order " %>Status</td>
		<td class="text10bold" bgcolor="#DDDDDD" <%= (mobWeb)? "" : "WIDTH='250'" %>>Details</td>
	</tr>
	
<%
Date currentDate = new Date();
for (FDOrderInfoI orderInfo : orderHistoryInfo) {
	orderNumber++;
	Date requestedDate = orderInfo.getRequestedDate();
	int diffInMonths= DateUtil.monthsBetween(currentDate, requestedDate);
	
	if(diffInMonths > FDStoreProperties.getOrderHistoryFromInMonths()){
		continue;
	}
	//add the condition here
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
%>	    <td class="order_history_table_id"><a href="<%= orderDetailsUrl %>"><span class="offscreen">details of order number</span><%= orderInfo.getErpSalesId() %></a></td>
		<td class="text10 order_history_table_date"><div class="order_history_table_date_display"><%= dateFormatter.format( orderInfo.getRequestedDate() ) %></div></td>
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
        <% if(!mobWeb){ %><td class="text10"><%= deliveryType %></td><% } %>
		<td class="text10 order_history_table_total" align=right><%= JspMethods.formatPrice( orderInfo.getTotal() ) %><%= (mobWeb)? "" : "&nbsp;&nbsp;&nbsp;&nbsp;" %></td>
		
		<% if(!mobWeb){ %><td></td><% } %>
<%
	String status = "";
	boolean orderIsModifying = modifyingOrderId !=null && modifyingOrderId.equals(orderInfo.getErpSalesId());
	if (orderIsModifying) {
		status = "Modifying...";
	} else if (orderInfo.getSaleType().equals(EnumSaleType.GIFTCARD) || orderInfo.getSaleType().equals(EnumSaleType.DONATION)) {
		status = orderInfo.isPending() ? "In Process" : "Completed";
	} else {
		status = orderInfo.getOrderStatus().getDisplayName();
	}
      if( EnumSaleStatus.AUTHORIZATION_FAILED.equals(orderInfo.getOrderStatus())) {%>
<td class="order_history_table_status"><font color="#FF0000"><%= status %></font></td>
   <%} else {%>
<td class="order_history_table_status"><%= status %></td>
   <%}%>
		<td>
			<a href="<%= orderDetailsUrl %>">View<span class="offscreen">order <%= orderInfo.getErpSalesId() %> </span></a>
            <% if (orderIsModifying) { %>
            | <a href="/your_account/cancel_modify_order.jsp">Cancel Changes</a>
           	<% } else if (orderInfo.isModifiable()){%>
            | <a href="/your_account/modify_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>&action=modify">Modify<span class="offscreen">order <%= orderInfo.getErpSalesId() %> </span></a>
            <% }%>
            <% if (orderInfo.isShopFromThisOrder()) { %>
            | <a href="/quickshop/shop_from_order.jsp?orderId=<%= orderInfo.getErpSalesId() %>">Shop Order<span class="offscreen"> number <%= orderInfo.getErpSalesId() %></span></a>
            <% } %>
		</td>
	</tr>
<%
} // orderInfo : orderHistoryInfo
%>
</TABLE>
<% 	} else { %>
<div style="width: <%= (mobWeb)? "100%" : (W_YA_ORDER_HISTORY_TOTAL + "px") %>; text-align: center; font-weight: bold;">You have not yet placed an order with us.</div>
<% 	} // end if-else orderHistory > 0%>
</fd:OrderHistoryInfo>
<br class="NOMOBWEB">
<br class="NOMOBWEB">
<IMG class="NOMOBWEB" src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%= (mobWeb)? "100%" : W_YA_ORDER_HISTORY_TOTAL %>" HEIGHT="1" BORDER="0"><BR class="NOMOBWEB">
<FONT CLASS="space4pix NOMOBWEB"><BR><BR></FONT>
<TABLE class="NOMOBWEB" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= (mobWeb)? "100%" : W_YA_ORDER_HISTORY_TOTAL %>">
	<tr VALIGN="TOP">
		<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
          CONTINUE SHOPPING<BR>
			from <FONT CLASS="text11bold">Home Page</a></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0">
		</td>
	</tr>
</TABLE>

</tmpl:put>
</tmpl:insert>
