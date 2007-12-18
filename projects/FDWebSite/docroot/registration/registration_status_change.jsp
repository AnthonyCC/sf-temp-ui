<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>

<fd:CheckLoginStatus id='user' />

<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>Important Note</tmpl:put>
		<tmpl:put name='content' direct='true'>
<%
String promoChange = request.getParameter("promoChange");
String dlvChange = request.getParameter("dlvChange");
boolean promoChanged = "true".equalsIgnoreCase(promoChange); 
boolean dlvChanged = "true".equalsIgnoreCase(dlvChange);
boolean promotionEligible = user.isEligibleForSignupPromotion();
boolean isPopup = false;

Promotion promotion = (Promotion) user.getEligibleSignupPromotion();
int promotionValue = promotion!=null ? (int)promotion.getHeaderDiscountTotal(): 0;
%>
<div align="center">
<br>
<img src="/media_stat/images/template/registration/thank_you_signup.gif" width="544" height="34" alt="Thank you for signing up!" border="0">
<br><br>
<table width="500">
<tr>
	<td class="text12">
		<% if (dlvChanged) { %>
			<%@ include file="/includes/registration/i_deliveryStatusChange.jspf" %>
		<% } %>
		<% if (promoChanged && !user.isFraudulent()) { %>
			<%@ include file="/includes/registration/i_promo_status_change.jspf" %>
		<% } else if (!promoChanged && promotionEligible && !user.isFraudulent()) {%>
			<b>Enjoy your $<%=promotionValue%> free, fresh food.*</b> Here's how it works:
		<% } %>
		<% if (promotionEligible && !user.isFraudulent()) { %>
			<%@ include file="/includes/promotions/signup.jspf" %>
		<% } %>
	</td>
</tr>
</table>
<% if (promotionEligible) { %>
	<%@ include file="/includes/registration/i_promoEligibility.jspf" %>
<% } %>
<a href="<%= response.encodeURL("/index.jsp") %>"><img src="/media_stat/images/template/registration/continue_shopping.gif" width="211" height="33" alt="Continue Shopping" border="0"></a>
</div>

</tmpl:put>
</tmpl:insert>