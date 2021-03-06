<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>

<fd:CheckLoginStatus id='user' />

<tmpl:insert template='/common/template/no_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Important Note"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Important Note</tmpl:put> --%>
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
<a href="<%= response.encodeURL("/checkout/step_1_choose.jsp") %>"><img src="/media_stat/images/template/registration/continue_checkout.gif" width="211" height="33" alt="Continue Checkout" border="0"></a>
</div>
</tmpl:put>
</tmpl:insert>