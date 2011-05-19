<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.net.URLEncoder'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" redirectPage="/registration/nw_cst_check_zone.jsp" />

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Age Verification for orders containing alcohol</tmpl:put>

<tmpl:put name='content' direct='true'>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>
<fd:AgeVerificationController result="result" successPage="checkout_delivery_time.jsp" blockedAddressPage="no_alcohol_address.jsp">
<div class="content_scroll" align="center">
<table width="675" cellpadding="0" cellspacing="0" border="0">
<tr><td class="title18" colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><b>Age Verification for orders containing alcohol</b><br><img src="/media_stat/images/layout/ff9933.gif" width="100%" height="1" vspace="6"></td></tr>
<tr><td colspan="4" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
<fd:ErrorHandler result='<%=result%>' name="didnot_agree" id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<table width="575" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="4" class="text12">
By law, purchasers of alcoholic beverages must be at least 21 years of age. You may not legally order any alcoholic beverages unless you are at least 21 years of age. Furthermore, you may not purchase alcoholic beverages for anyone who is under the age of 21. FreshDirect reserves the right to refuse service, terminate accounts, remove alcoholic beverages, or cancel orders at its sole discretion.<br><br> 
If your order contains alcoholic beverages, the person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature. If no one over the age of 21 can sign for delivery, the driver will remove alcoholic beverages from the order and you will be charged a 50% restocking fee.<br><br><br>
</td></tr>
<form method="POST">
<tr valign="top"><td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td><td><input name="age_verified" type="checkbox"></td>
<td width="475" class="text12">
	<b>I certify that I am over 21 years of age.<br>I will present identification at the time of delivery.</b>
</td><td><img src="/media_stat/images/layout/clear.gif" width="50" height="1"></td></tr>
<tr><td colspan="4" class="text12"><br><br>IT IS A VIOLATION PUNISHABLE UNDER LAW FOR ANY PERSON UNDER THE AGE OF TWENTY-ONE TO PRESENT ANY WRITTEN EVIDENCE OF AGE WHICH IS FALSE, FRAUDULENT OR NOT ACTUALLY HIS OWN FOR THE PURPOSE OF ATTEMPTING TO PURCHASE ANY ALCOHOLIC BEVERAGE.<br><br></td></tr>
</table>
</td></tr>
<tr><td colspan="4"><img src="/media_stat/images/layout/ff9933.gif" width="100%" height="1" vspace="8"></td></tr>
<tr valign="middle">
	<td width="25" valign="top"><a href="<%=response.encodeURL("/order/build_order_browse.jsp")%>"><img src="/media_stat/images/buttons/x_green.gif" width="20" height="19" border="0" alt="CONTINUE SHOPPING"></a></td>
	<td width="350"><a href="<%=response.encodeURL("/order/build_order_browse.jsp")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" height="7" border="0" alt="CANCEL CHECKOUT"></a><br>and return to your cart.</td>
	<td width="265" align="right"><input type="image" name="age_verification"  src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"><br>Go to Step 2: Delivery Time<br></td>
	<td width="35" align="right"  valign="top"><input type="image" name="age_verification" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CONTINUE CHECKOUT"></td>
</tr>
</form>
</table>
</div>
</fd:AgeVerificationController>
</tmpl:put>
</tmpl:insert>