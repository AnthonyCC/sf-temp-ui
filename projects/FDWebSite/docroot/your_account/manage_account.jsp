<%@ page import = "com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>

<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
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

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account</tmpl:put>
    <tmpl:put name='content' direct='true'>
<% 
DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
<fd:OrderHistoryInfo id='orderHistoryInfo'>		
<%  if (orderHistoryInfo != null && orderHistoryInfo.size() != 0) {
		for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
		    FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
			if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER) { 
			    Calendar dlvStart = Calendar.getInstance();
				dlvStart.setTime(orderInfo.getDeliveryStartTime());
				Calendar dlvEnd = Calendar.getInstance();
				dlvEnd.setTime(orderInfo.getDeliveryEndTime());

				String deliveryTime = getTimeslotString(dlvStart, dlvEnd);	
				String errorMsg= "We were unable to deliver your order (#"+orderInfo.getErpSalesId()+") scheduled for between "+deliveryTime+" on "+dateFormatter.format( orderInfo.getRequestedDate() )+". Please contact us as soon as possible at "+user.getCustomerServiceContact()+" to reschedule delivery.";
				ActionResult result = new ActionResult();
			%>
			<%@ include file="/includes/i_error_messages.jspf"%>
			<% } %>
<%          break;
		}
	}
%>
</fd:OrderHistoryInfo>
<table width="675" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="text13">
			<font class="title18">Welcome to Your Account</font><br>If you need to make any changes or updates to
your account information, this is the place to do it.<br></td>
	</tr>
</table>
<img src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><br>
<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" BORDER="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><br><br>

<table width="675" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="500">
<!-- ct user logo -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td valign="top" class="text13">
	<% if(user.isChefsTable()) { %>
	<table align="left" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/media_stat/images/template/checkout/loy_ctcard_top.gif"/></td>
	</tr>
	<tr>
		<td style="color: white; background: #221e1f; left: inherit; text-align: center;">
			<%= user.getFirstName() %> <%= user.getLastName() %>
			<br/>
			Since <%= TimeslotPageUtil.formatFirstOrderYear(user.getOrderHistory().getFirstNonPickupOrderDate()) %>
			<br/>
			<br/>
		</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/template/checkout/loy_ctcard_bot.gif"/></td>
	</tr>
	</table>
	<% } %>
    </td></tr><tr>
	<td valign="top" class="text13"><br><br>
		<font class="text13bold"><a href="<%=response.encodeURL("/your_account/order_history.jsp")%>">Your Orders</a></font><br>Check your order status and update open orders.
		<br><br>
		<%if(user.isEligibleForPreReservation()){%>
			<font class="text13bold">
						<a href="<%=response.encodeURL("/your_account/reserve_timeslot.jsp")%>">Reserve a Delivery Time </a>
			</font><br>
			Reserve a delivery slot before you place your order.
			<br><br>
		<%}%>
		<%if(user.isEligibleForDeliveryPass()){%>
			<font class="text13bold">
						<a href="<%=response.encodeURL("/your_account/delivery_pass.jsp")%>">FreshDirect Unlimited DeliveryPass</a>
			</font><br>
						See  your membership details.
			<br><br>
		<%}%>
		
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>">Delivery Addresses</a>
		</font><br>
		Update your delivery address information.
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/payment_information.jsp")%>">Payment Options</a>
		</font><br>
		Update your payment information.
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/signin_information.jsp")%>">User Name, Password &amp; Contact Information</a>
		</font><br>
		Change your user name, password, and contact info.
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/newsletter.jsp")%>">President's Picks Newsletter</a>
		</font><br>
				Subscribe to the President's Picks alert to get each week's deals delivered right to your inbox.
		<br><br>
		<font class="text13bold">
		<a href="<%=response.encodeURL("/your_account/reminder_service.jsp")%>">Reminder Service</a>
		</font><br>
		Change your e-mail reminder preferences.
		<br><br>
        <font class="text13bold">
		<a href="<%=response.encodeURL("/quickshop/all_lists.jsp")%>">Your Shopping Lists</a> </font>  <font class="text13bold" color="#990000">*NEW </font>
		<br>
		Visit Quickshop for your shopping lists
		<br><br>
		
	</td>
	<td width="30"><img src="/media_stat/images/layout/clear.gif" ALT="" width="30" height="1"></td>
	</tr>
</table>
</td>
<td valign="top">
	<!-- ct user logo -->
	<% if(user.isChefsTable()) { %>
	<table align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
        <fd:IncludeMedia name="/media/editorial/site_pages/account/right_ct.html" />
        </td>
	</tr>
	</table>
	<% }else{ %>
    	<table align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
        <fd:IncludeMedia name="/media/editorial/site_pages/account/right_all.html" />
        </td>
	</tr>
	</table>
    <% } %>
</td>
</tr>
</table>

<br><br>
<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" BORDER="0"><br>
<FONT CLASS="space4pix"><br><br></FONT>
<table BORDER="0" CELLSPACING="0" CELLPADDING="0" width="675">
<TR VALIGN="TOP">
<TD width="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
<TD width="640"  class="text11" ><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<br>from <A HREF="/index.jsp"><b>Home Page</b></A><br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" BORDER="0"></TD>
</TR>
</table>

<br>

 </tmpl:put>
</tmpl:insert>
