<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.net.URLEncoder'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_CHECKOUT_STEP_2_VERIFY_AGE_TOTAL = 970;
%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Checkout - Age Verification for orders containing alcohol"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Age Verification for orders containing alcohol</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>
<fd:AgeVerificationController result="result" successPage="/checkout/step_2_select.jsp" blockedAddressPage="/checkout/no_alcohol_address.jsp">
<table width="<%=W_CHECKOUT_STEP_2_VERIFY_AGE_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
<tr><td class="title18" colspan="4"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6"><br><b>Age Verification for orders containing alcohol</b><br><img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="100%" height="1" vspace="6"></td></tr>
<tr><td colspan="4" align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"><br>
<% String[] checkErrorType = {"didnot_agree", "technical_difficulty"}; %>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<table width="<%=W_CHECKOUT_STEP_2_VERIFY_AGE_TOTAL-100%>" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td colspan="4" class="text12">
By law, purchasers of alcoholic beverages must be at least 21 years of age. You may not legally order any alcoholic beverages unless you are at least 21 years of age. Furthermore, you may not purchase alcoholic beverages for anyone who is under the age of 21. FreshDirect reserves the right to refuse service, terminate accounts, remove alcoholic beverages, or cancel orders at its sole discretion.<br><br> 
If your order contains alcoholic beverages, the person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature. If no one over the age of 21 can sign for delivery, the driver will remove alcoholic beverages from the order and you will be charged a 50% restocking fee.<br><br><br>
		</td>
	</tr>
<form method="POST" name="step2Form" id="step2Form">
	<tr valign="top">
		<td>
			<img src="/media_stat/images/layout/clear.gif" alt="" width="50" height="1">
		</td>
		<td>
<%
	if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
%>			<input id="age_verified" name="age_verified" type="checkbox">
<%
	} else {
%>			<input id="age_verified" name="age_verified" type="checkbox" <%= user.getCurrentStandingOrder().isAlcoholAgreement() ? "checked=\"checked\"" : "" %>>
<%
	}
%>		</td>
		<td width="<%=W_CHECKOUT_STEP_2_VERIFY_AGE_TOTAL-200%>" class="text12">
			<label for="age_verified"><b>I certify that I am over 21 years of age.<br>I will present identification at the time of delivery.</b></label>
		</td>
		<td>
			<img src="/media_stat/images/layout/clear.gif" alt="" width="50" height="1">
		</td>
	</tr>
	<tr>
		<td colspan="4" class="text12">
			<br><br>IT IS A VIOLATION PUNISHABLE UNDER LAW FOR ANY PERSON UNDER THE AGE OF TWENTY-ONE TO PRESENT ANY WRITTEN EVIDENCE OF AGE WHICH IS FALSE, FRAUDULENT OR NOT ACTUALLY HIS OWN FOR THE PURPOSE OF ATTEMPTING TO PURCHASE ANY ALCOHOLIC BEVERAGE.<br><br>
		</td>
	</tr>
</table>
	</td>
</tr>
<tr>
	<td colspan="4">
		<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="100%" height="1" vspace="8">
	</td>
</tr>
<tr valign="TOP">
		<TD width="35">
					<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
		</TD>
	    <TD width="340" style="text-align: left;">
			<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Address<br/>
				<img src="/media_stat/images/layout/clear.gif" alt="" width="340" height="1" border="0">
		</TD>
		<TD ALIGN="RIGHT" VALIGN="MIDDLE">
			<button class="imgButtonOrange" type="submit">choose time <img src="/media_stat/images/buttons/button_orange_arrow.gif" alt="" /></button>
		</TD>
</TR>
<tr>
	<td colspan="4"><br><%@ include file="/checkout/includes/i_footer_text.jspf" %>
	</td>
</tr>
</form>
</table>
</fd:AgeVerificationController>
</tmpl:put>
</tmpl:insert>