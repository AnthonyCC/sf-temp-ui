<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods'%>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	FDUserI usery = (FDUserI)session.getAttribute(SessionName.USER);
	int regType = 0;
	String regContSuccessPage = "/unattended_redirect.jsp?successPage=/checkout/step_1_choose.jsp";
	String regContFraudPage = "/unattended_redirect.jsp?successPage=/checkout/registration_note.jsp";

	if(usery.isCorporateUser()){
		regType = AccountUtil.CORP_USER;
	}else if (usery.isDepotUser()){
		regType = AccountUtil.DEPOT_USER;
	}else{
		regType = AccountUtil.HOME_USER;
	}
	/* note here that statusChangePage is obsolete -batchley 2011.01.27_09.28.58.PM */
%>
<fd:RegistrationController actionName='register' successPage='<%= regContSuccessPage %>' result='result' fraudPage='<%= regContFraudPage %>' statusChangePage='registration_status_change.jsp' signupFromCheckout='true' registrationType="<%=regType%>">
<%
ActionResult ar= new ActionResult();
Collection aerrs=result.getErrors();
for (Iterator erItr = aerrs.iterator();erItr.hasNext();) {
    ActionError aer = (ActionError)erItr.next();
}
%>
<fd:FDShoppingCart id='cart' result="sc_result">
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Sign Up</tmpl:put>
<script language="javascript" src="/assets/javascript/common_javascript.js" type="text/javascript"></script>
<tmpl:put name='content' direct='true'>

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

<table width="693" border="0" cellspacing="0" cellpadding="0">
<tr><td width="513" class="text13">
<FORM name="address" method="post">

<%
		FDUserI userx = (FDUserI)session.getAttribute(SessionName.USER);
		int promoValue = (int)userx.getMaxSignupPromotion();
%>		
		<%if(userx.isDepotUser()){

			String depotName = com.freshdirect.fdstore.FDDepotManager.getInstance().getInstance().getDepot(userx.getDepotCode()).getName();
		%>	
			<span class="title18">FreshDirect <%=depotName%> Depot Sign Up</span><br><span class="space4pix"><br></span>
				Sign up now to continue with checkout and get $<%=promoValue%> off your first order! Please note: This service is only available to current <%=depotName%> employees. To find out more about FreshDirect Depot service <a href="javascript:popup('/help/faq_index.jsp?show=delivery_depot','large')">click here</a>.<br>
			<br>
			<b>Current customer?</b><BR>
		  <%}else if(userx.isCorporateUser()){%>
                         <span class="title18">FreshDirect Corporate Services Sign Up</span><br><span class="space4pix"><br></span>
				Sign up now to start getting the best New York has to offer - delivered right to your office door. To find out more about the FreshDirect Corporate Services program, <a href="javascript:popup('/help/faq_index.jsp?show=cos','large')">click here</a>.
			<br>
			<br>
			<b>Current customer?</b><BR>
                 
                 <%}else{%>
			<span class="title18">Sign Up</span><br><span class="space4pix"><br></span>
			<% if (userx.isInZone()) {
					if (userx.isEligibleForSignupPromotion()) {
			%>
					Sign up now to continue checkout and get $<%=promoValue%> free fresh food. <a href="javascript:popup('/shared/promotion_popup.jsp','large')">Click here for offer details</a>.
				<% } else { %>
					Sign up now to continue checkout and start getting better food at a better price. 
				<% } %>
			<% } else { %>
				Sign up now to continue checkout and start getting better food at a better price. Please note that your area is not within our <a href="javascript:popup('/help/delivery_zones.jsp','large')">current delivery zones</a>, so you will only be able to place an order for pickup.
			<% } %>
			<br><br>
			<b>Already have a password?</b><br>
		 <%}%>	

</td>
	<TD WIDTH="140" ALIGN="RIGHT" CLASS="text10bold" valign="top">
		<FONT CLASS="space2pix"><BR></FONT><input type="image" name="next_step" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0" onClick="setActionName(this.form,'setPaymentMethod')"><BR>
		<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>:  <%= JspMethods.formatPrice(cart.getTotal()) %></TD>
	<TD WIDTH="35" ALIGN="RIGHT" valign="top"><FONT CLASS="space2pix"><BR></FONT>
		<input type="image" name="next_step" src="/media_stat/images/buttons/checkout_arrow.gif"
		 BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2" ></TD>

</tr>
</table>
<table width="693" border="0" cellspacing="0" cellpadding="0">
<tr><td class="bodyCopy" width="693">

<A HREF="<%=response.encodeURL("/login/login.jsp?successPage=/checkout/step_1_choose.jsp")%>">Log in here</A>.<BR>
<BR></TD>
</TR>
</TABLE>
<%@ include file="/includes/registration/i_signup.jspf" %><BR><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
    <TR VALIGN="TOP">
    <TD WIDTH="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
	<TD WIDTH="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc")%>"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="265" ALIGN="RIGHT"><input type="image" name="next_step"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" border="0" alt="CONTINUE CHECKOUT" VSPACE="0" onClick="setActionName(this.form,'setPaymentMethod')"><BR>I agree to the Customer Agreement.<br>Sign me up and go to Step 1: Delivery Address<BR></TD>
	<TD WIDTH="35" ALIGN="RIGHT"><input type="image" name="next_step" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0" onClick="setActionName(this.form,'setPaymentMethod')"></TD>
	</TR>
	</TABLE>

</form>
<%@ include file="includes/i_footer_text.jspf" %>
</tmpl:put>
</tmpl:insert>
</fd:FDShoppingCart>
</fd:RegistrationController>
