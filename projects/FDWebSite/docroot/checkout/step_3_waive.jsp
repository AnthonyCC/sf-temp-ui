<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.net.URLEncoder'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Pass on the offer & Continue checkout</tmpl:put>
<tmpl:put name='content' direct='true'>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>
<table width="675" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="title18"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><b>Please note: Your billing and delivery addresses do not match!</b><br><img src="/media_stat/images/layout/ff9933.gif" width="100%" height="1" vspace="6"></td>
	</tr>
	<tr>
		<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
	Since your billing address does not match your delivery address, you do not qualify for our free food offer. The offer will not be applied to your order and once you complete Checkout, cannot be applied retroactively. <a href="javascript:popup('/shared/promotion_popup.jsp','large');">Click here for offer details</a>.<br><br>
		<table width="203" cellpadding="0" cellspacing="3" border="0" align="center">
			<tr><td align="center"><a href="/checkout/step_4_submit.jsp"><img src="/media_stat/images/buttons/pass_on_offer.gif" width="203" height="36" border="0"></a>
			</td></tr>
			<tr><td class="text12" align="center">
			<b>I understand that I am not eligible for the free food offer.</b>
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
</tmpl:put>
</tmpl:insert>
