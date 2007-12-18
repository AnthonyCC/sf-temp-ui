<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>

<fd:CheckLoginStatus id='user' />

<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>Important Note</tmpl:put>
		<tmpl:put name='content' direct='true'>
<%
String promoChange = "false";
String dlvChange = "false";

Promotion promotion = (Promotion) user.getEligibleSignupPromotion();
int promotionValue = promotion!=null ? (int)promotion.getHeaderDiscountTotal(): 0;
%>
<div align="center">
<img src="/media_stat/images/template/registration/thank_you_signup.gif" width="544" height="34" alt="Thank you for signing up!" border="0"><br><br>
<br>
<%@ include file="/includes/registration/i_promoEligibility.jspf" %>
<a href="<%= response.encodeURL("/index.jsp") %>"><img src="/media_stat/images/template/registration/continue_shopping.gif" width="211" height="33" alt="Continue Shopping" border="0"></a>
</div>
</tmpl:put>
</tmpl:insert>