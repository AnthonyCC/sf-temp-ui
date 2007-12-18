<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - An order is already placed</tmpl:put>
<tmpl:put name='content' direct='true'>
<%! DateFormat duplicateDate = new SimpleDateFormat("EEEE, MMM dd"); %>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>
<fd:FDShoppingCart id='cart' result="sc_result">
<fd:OrderHistoryInfo id='orderHistoryInfo'>
<%
// link to same day order details/ order history if multiple
String ignoreSaleId = null;
if (cart instanceof FDModifyCartModel) {
	ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
}


Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());

int sameDayPendingOrderCount = 0;
String modifyLink = "/your_account/order_history.jsp";
String orderId = "";

for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
	FDOrderInfoI oi = (FDOrderInfoI) hIter.next();
	if (!(oi.getErpSalesId().equals(ignoreSaleId))
		&& oi.isPending()
		&& currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
			orderId = oi.getErpSalesId();
			sameDayPendingOrderCount++;
	}
}

if (sameDayPendingOrderCount == 1) {
	modifyLink = "/your_account/order_details.jsp?orderId=" + orderId;
}
	
%>

<table width="675" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><span class="text13" style="color: #CC0000;"><b>Important note!</b></span><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
	An order for <b><%=duplicateDate.format(currentDlvStart)%></b> has already been placed from this account. Are you sure that you want to place another one for the same day?<br><br>Please select one of these options:<br><br>
		<table cellpadding="0" cellspacing="3" border="0" align="center">
			<tr><td align="center">
			</td></tr>
			<tr><td class="text12" align="center">
			<b>Make changes</b> to the order you've already placed.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
			<table cellpadding="1" cellspacing="0" border="0"><tr><td rowspan="2" width="32"><a href="<%=modifyLink%>"><img src="/media_stat/images/buttons/arrow_blue_left.gif" width="29" height="30" border="0" alt="MODIFY ORDER"></a></td><td class="text12" valign="bottom"><a href="<%=modifyLink%>"><img src="/media_stat/images/buttons/modify_order.gif" width="82" height="9" border="0" alt="MODIFY ORDER"></a></td></tr><tr><td valign="top">Go to Your Account</td></tr></table>
			<br><br>
			<b>Place this additional order</b> for the same day.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
			<table cellpadding="1" cellspacing="0" border="0"><tr><td align="right" valign="bottom"><a href="/checkout/step_3_choose.jsp?duplicateCheck=skip"><img src="/media_stat/images/buttons/continue_checkout.gif" width="117" height="9" border="0" alt="CONTINUE CHECKOUT"></a></td><td rowspan="2" width="32" align="right"><a href="/checkout/step_3_choose.jsp?duplicateCheck=skip"><img src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CONTINUE CHECKOUT"></a></td></tr><tr><td align="right" valign="top">Go to Step 3: Payment Info</td></tr></table>
			</td></tr>
		</table><br><br>
		</td>
	</tr>
	<tr><td>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
		<table border="0" cellspacing="0" cellpadding="0">
		    <tr valign="top">
			<td width="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>"><img src="/media_stat/images/buttons/x_green.gif" width="20" height="19" border="0" alt="CONTINUE SHOPPING"></a></td>
			<td width="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" height="7" border="0" alt="CANCEL CHECKOUT"></a><br>and return to your cart.<br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0"></td>
		    </tr>
		</table>
	</td></tr>
	<tr><td><br><%@ include file="/checkout/includes/i_footer_text.jspf" %></td></tr>
</table>
</fd:OrderHistoryInfo>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>