<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.net.URLEncoder'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Age Verification for orders containing alcohol</tmpl:put>
<tmpl:put name='content' direct='true'>

<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDCartModel cart = user.getShoppingCart();

	if("POST".equals(request.getMethod())){
		cart.removeAlcoholicLines();
		response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_select.jsp"));
	}
%>

<table width="675" cellpadding="0" cellspacing="0" border="0">
<form method="POST">
<tr><td class="title18"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><b>Alcohol is not currently delivered to your address</b><br><img src="/media_stat/images/layout/ff9933.gif" width="100%" height="1" vspace="6"></td></tr>
<tr><td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
<table width="575" cellpadding="0" cellspacing="0" border="0">
<tr><td colspan="4" class="text12">
Unfortunately, FreshDirect does not deliver alcohol to your building. You may choose a different address or continue checkout -- without the alcohol but with all of the other items in your cart.<br> 
<%
     boolean belowMinimum = false;
	if(!user.isOrderMinimumMet(true)){
          belowMinimum = true;
		out.println("<br><b>Because your order falls below our $" + (int)user.getMinimumOrderAmount() + " minimum when alcohol is removed, please return to your cart to add items before continuing checkout.</b><br>");
	}
%>
<%--<br>
For details of our alcoholic beverage policy, click here.--%><br><br>
</td></tr>
<tr valign="top">
	<td width="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/green_back_arrow.gif" width="29" height="30" border="0" alt="CONTINUE SHOPPING"></a></td>
	<td width="200" <%if (belowMinimum){%>valign="middle"<%}%>><%--if (!belowMinimum){%><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br><%}--%><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/cancel_checkout_return_cart.gif" width="158" height="23" border="0" alt="CANCEL CHECKOUT"></a></td>
	<td width="315" align="right"><%if (!belowMinimum){%><input type="image" name="no_alcohol"  src="/media_stat/images/buttons/remove_alcohol_continue.gif" width="155" height="23" border="0" alt="CONTINUE CHECKOUT"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>Items containing alcohol<br>will be removed from your cart.<%}%></td>
	<td width="35" align="right"  valign="top"><%if (!belowMinimum){%><input type="image" name="no_alcohol" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CONTINUE CHECKOUT"><%}%></td>
</tr>
</table><br>
</td></tr>
<tr><td><img src="/media_stat/images/layout/ff9933.gif" width="100%" height="1" vspace="8"></td></tr>
<tr><td><%@ include file="/checkout/includes/i_footer_text.jspf" %></td></tr>
</form>
</table>
</tmpl:put>
</tmpl:insert>