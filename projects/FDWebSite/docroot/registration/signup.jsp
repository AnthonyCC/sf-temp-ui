<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_SIGNUP_TOTAL = 970;
%>

<fd:CheckLoginStatus />
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<%
	FDUserI userx = (FDUserI)session.getAttribute(SessionName.USER);
	int regType = 0;
	String regContSuccessPage = "/unattended_redirect.jsp?successPage=";
	String regContFraudPage = "/unattended_redirect.jsp?successPage=/registration/registration_note.jsp";

	if(userx.isCorporateUser()){
		regType = AccountUtil.CORP_USER;
		regContSuccessPage += "/index.jsp?serviceType=CORPORATE";
	}else if (userx.isDepotUser()){
		regType = AccountUtil.DEPOT_USER;
		regContSuccessPage += "/index.jsp?serviceType=DEPOT";
	}else{
		regType = AccountUtil.HOME_USER;
		regContSuccessPage += "/index.jsp?serviceType=HOME";
	}
	/* note here that statusChangePage is obsolete -batchley 2011.01.27_09.28.58.PM */
	
%>
<fd:RegistrationController actionName='register' successPage='<%= regContSuccessPage %>' result='result' fraudPage='<%= regContFraudPage %>' statusChangePage='registration_status_change.jsp' registrationType="<%=regType%>">
<tmpl:insert template='/common/template/no_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Sign Up"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Sign Up</tmpl:put> --%>
<tmpl:put name='content' direct='true'>


<%--pageContext.setAttribute("listOfFields", FIELD_NAMES ,1 );--%>

<fd:ErrorHandler result='<%=result%>' name='fraud' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkRegistrationForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<table role="presentation" width="<%=W_SIGNUP_TOTAL%>" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td width="<%=W_SIGNUP_TOTAL%>" class="text13">
<%
		int promoValue = 0;
                PromotionI promotion = userx.getEligibleSignupPromotion();
                if(promotion != null) promoValue = (int)promotion.getHeaderDiscountTotal();
%>
		<%if(userx.isDepotUser()){

			String depotName = com.freshdirect.fdstore.FDDeliveryManager.getInstance().getInstance().getDepot(userx.getDepotCode()).getName();
		%>	
			<span class="title18">FreshDirect <%=depotName%> Depot Sign Up</span><br /><span class="space4pix"><br /></span>
				Sign up now and get $<%=promoValue%> off your first order! Please note: This service is only available to current <%=depotName%> employees. To find out more about FreshDirect Depot service <a href="javascript:popup('/help/faq_index.jsp?show=delivery_depot','large')">click here</a>.
			<br />
			<br />       
         <%}else{%>
			<span class="title18">Sign Up</span><br /><span class="space4pix"><br /></span>
			<% if (userx.isInZone()) {
					if (userx.isEligibleForSignupPromotion()) {
			%>
					Sign up now and get $<%=promoValue%> free fresh food. <a href="javascript:popup('/shared/promotion_popup.jsp','large')">Click here for offer details</a>. No credit card information is required until you place an order.
				<% } else { %>
					Sign up now and start getting better food at a better price. No credit card information is required until you place an order.
				<% } %>
			<% } else { %>
				Sign up now and start getting better food at a better price. No credit card information is required until you place an order. Please note that your area is not within our <a href="javascript:fd.components.zipCheckPopup.openZipCheckPopup()">current delivery zones</a>, so you will only be able to place an order for pickup.
			<% } %>
			<br /><br />
			<b>Already have a password?</b><br />
		 <%}%>			 
		<a href="<%=response.encodeURL("/login/login.jsp")%>">Log in now</a>.

		<% if (FDStoreProperties.isGiftCardEnabled()) { %>
			<br /><br />
			<b>Looking for Gift Cards?</b><br />
			<a href="<%=FDStoreProperties.getGiftCardLandingUrl()%>">Click here<span class="offscreen">for more details about gift cards</span></a>.
		<% } %>
	</td>
</tr>
</table>
<form name="address" id="address" method="post" ><fieldset><legend class="offscreen">signup for freshdirect</legend>
<%@ include file="/includes/registration/i_signup.jspf" %>
</fieldset></form>
</tmpl:put>
</tmpl:insert>
</fd:RegistrationController>