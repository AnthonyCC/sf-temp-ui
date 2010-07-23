<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.net.*"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<script type="text/javascript" language="javascript" src="/assets/javascript/prototype.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/scriptaculous.js?load=effects,builder"></script>
	<script type="text/javascript" language="javascript" src="/assets/javascript/modalbox.js"></script>	
	<link rel="stylesheet" type="text/css" href="/assets/css/modalbox.css" />
	<script type="text/javascript" language="javascript" src="/assets/javascript/FD_PromoEligibility.js"></script>
<fd:CheckLoginStatus id="yuzer" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<%

// redirect to Survey if this is the second order && first order is delivered
if (!yuzer.isSurveySkipped() && yuzer.getAdjustedValidOrderCount()==1 && yuzer.getDeliveredOrderCount()==1) {



// leave previous 4th order logic
    FDCustomerModel customer = FDCustomerFactory.getFDCustomer(yuzer.getIdentity());
    boolean alreadyTookFirstSurvey = "YES".equals(customer.getProfile().getAttribute("signup_survey")) ;
    boolean alreadyTookSecondSurvey = "FILL".equals(customer.getProfile().getAttribute("signup_survey_v2"))
                                       || "FILL".equals(customer.getProfile().getAttribute("fourth_order_survey"))
									   || "FILL".equals(customer.getProfile().getAttribute("fourth_order_cos_survey"))
									   || "FILL".equals(customer.getProfile().getAttribute("second_order_survey"));
    boolean skippedSecondSurvey = "SKIP".equals(customer.getProfile().getAttribute("signup_survey_v2"))
                                   ||  "SKIP".equals(customer.getProfile().getAttribute("fourth_order_survey"))
								   ||  "SKIP".equals(customer.getProfile().getAttribute("fourth_order_cos_survey"))
								   ||  "SKIP".equals(customer.getProfile().getAttribute("second_order_survey"));

 if (!alreadyTookFirstSurvey && !alreadyTookSecondSurvey && !skippedSecondSurvey) {

        FDSurvey usability = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.SECOND_ORDER_SURVEY, yuzer);
 		if (usability == null) {
			response.sendRedirect(response.encodeRedirectURL("/checkout/survey_cos.jsp?successPage=/checkout/step_1_choose.jsp"));
		} else {
	        FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(yuzer.getIdentity(), yuzer);
           int coverage=SurveyHtmlHelper.getResponseCoverage(usability,surveyResponse);
           if(coverage<usability.getAcceptableCoverage()) {
           
        	    response.sendRedirect(response.encodeRedirectURL("/checkout/survey.jsp?successPage=/checkout/step_1_choose.jsp"));
                return;
           } 
    	}
	    
    }
}

%>
<%-- =================================================================================================== --%>

<% 
if (yuzer.getLevel()==FDUserI.RECOGNIZED) {
    response.sendRedirect(response.encodeRedirectURL("/login/login.jsp?successPage=/checkout/step_1_choose.jsp"));
    return;
}

String forwardPage = "/checkout/step_1_choose.jsp";

List<ErpAddressModel> dlvAddresses = FDCustomerFactory.getErpCustomer(yuzer.getIdentity()).getShipToAddresses();
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<%  
String actionName = "setDeliveryAddress";
String successPage = "/checkout/step_2_select.jsp";

Enumeration<String> e = request.getParameterNames();
while (e.hasMoreElements()) {
    String name = e.nextElement();
    if (name.startsWith("delete_")) {
        actionName = "deleteDeliveryAddress";
        successPage ="/checkout/step_1_choose.jsp";
        String addrId = name.substring(name.indexOf("_") + 1, name.indexOf("."));
        request.setAttribute("deleteShipToAddressId", addrId);
        break;
    }
} 

%>
<fd:CheckoutController actionName='<%=actionName%>' result="result" successPage="<%=successPage%>">
	<%
	if (result.hasError("error_dlv_pass_only") || result.hasError("order_minimum")) {
	    response.sendRedirect(response.encodeRedirectURL("/checkout/view_cart.jsp"));
	    return;
	}
	
	double cartTotal = yuzer.getShoppingCart().getTotal();
	if (result.hasError("cantGeocode")) {
	  String errorMsg=SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS;
	%>
	<%@ include file="/includes/i_error_messages.jspf" %>   
	<% } %>
	
	<% if (yuzer.isEligibleForSignupPromotion() && yuzer.getShoppingCart().getSubTotal() < yuzer.getSignupDiscountRule().getMinSubtotal()) { 
	    String errorMsg = "Because your cart contains less than $" + (int)yuzer.getSignupDiscountRule().getMinSubtotal() + " of food, you are not eligible for the free fresh food promotion. You may return to your cart to add more food or continue Checkout now. <a href=\"javascript:popup('/shared/promotion_popup.jsp','large');\">Click for details</a>.";
	    %>
	    <%@ include file="/includes/i_error_messages.jspf" %> 
	   <br/>
	<% } %>

	<%@ include file="/includes/i_modifyorder.jspf" %>
 
	<form method="POST" name="step1Form" onSubmit="return checkPromoEligibilityByAddress('<%= null==yuzer.getRedeemedPromotion()?"null":"not null" %>');">
	<div class="gcResendBox" style="display:none">
		<div style="text-align: left;" class="gcResendBoxContent" id="gcResendBox">
			<img src="/media_stat/images/giftcards/your_account/resend_hdr.gif" width="169" height="16" alt="Resend Gift Card" />
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a>
			<br />If your Recipient never received their Gift Card, you may resend it by clicking Resend Now. If there was an error in the Recipient's email address, or to use a new one, edit the email field.
			<br /><br /><img src="/media_stat/images/layout/cccccc.gif" width="390" height="1" border="0"><br /><br />
			<input type="hidden" id="gcSaleId" value="" />
			<input type="hidden" id="gcCertNum" value="" />
			<table border="0" cellspacing="0" cellpadding="4" width="100%">
				<tr>
					<td colspan ="2"width="130" align="right">The promotion code you entered <span id="gcResendRecipAmount"></span> is not valid for the address you selected. More Info</td>
					
				</tr>				
				
				<tr>
					<td width="150" colspan="2" align="center"><a href="#" onclick="Modalbox.hide(); return false;">CHOOSE ANOTHER</a></td>					
				</tr>
				<tr>					
					<td width="150" colspan="2" align="center"><a href="#" onclick="javascript:document.forms['step1Form'].submit();"><b>CONTINUE</b></a></td>
				</tr>
			</table>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
			
			<div id="gcResendErr">&nbsp;</div>
		</div>
	</div>
		<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top"> 
	    <td class="text12" width="375" valign="bottom"> 
	            <font class="title18">Choose Delivery Address (Step 1 of 4)</font><br/>
	            <%if(yuzer.isPickupOnly() ){%>   
	        <b>Please Note: </b>Your home address is not in a FreshDirect <a href="javascript:popup('/help/delivery_zones.jsp','large');">delivery zone</a>.
	        Please select one of our pickup locations to place an order.
	            <%}else if(!yuzer.isDepotUser()){%>
	        Please choose a delivery address for this order.
	            <%}else{%>
	        Where would you like to receive this order?
	            <%}%>
	        </td>
	       
	    <td width="265" align="right" valign="middle" class="text10bold">
	            <font class="space2pix"><br/></font><input type="image" name="form_action_name" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" vspace="2" border="0"><br/>
	            <a href="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</a>: <%=currencyFormatter.format(cartTotal)%>
	        </td>
	    <td width="35" align="right" valign="middle"><font class="space2pix"><br/></font>
	            <input type="image" name="form_action_name" src="/media_stat/images/buttons/checkout_arrow.gif" border="0" alt="CONTINUE CHECKOUT" vspace="2">
	        </td>
	    </tr>
	</table>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br/>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	
	<% 
	String[] checkDlvAddressForm =  {EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(), "address",
	                                "pickup_contact_number", "order_minimum","apartment",
	                                "technical_difficulty"}; 
	%>
	
	<fd:ErrorHandler result='<%=result%>' field='<%=checkDlvAddressForm%>' id='errorMsg'>
	    <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<%
	String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
	if (errorMsg==null && yuzer.isPromotionAddressMismatch()) {
	    Promotion promo = (Promotion)yuzer.getEligibleSignupPromotion();
	    Double totalPromo = new Double(promo.getHeaderDiscountTotal());
	    errorMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, yuzer.getCustomerServiceContact()});
	}
	if (errorMsg!=null) {%> 
	    <%@ include file="/includes/i_error_messages.jspf"%> 
	<%}%>
	    <%@ include file="/includes/ckt_acct/i_delivery_address_select.jspf" %>
	    <br/><br/>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br/>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br/>
	<table border="0" cellspacing="0" cellpadding="0" width="675">
	    <tr valign="top">
	    <td width="25"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelX"><img src="/media_stat/images/buttons/x_green.gif" width="20" height="19" border="0" alt="CONTINUE SHOPPING"></a></td>
	    <td width="350"><a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><br/>and return to your cart.<br/><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0"></td>
	    <td width="265" align="right" valign="middle"><font class="space2pix"><br/></font><input type="image" name="checkout_delivery_address_select"  src="/media_stat/images/buttons/continue_checkout.gif" width="117" height="9" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Go to Step 2: Delivery Time<br/></td>
	    <td width="35" align="right" valign="middle"><font class="space2pix"><br/></font><input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CONTINUE CHECKOUT" vspace="0"></td>
	    </tr>
	</table>
	
	<%
	if(ClickToCallUtil.evaluateClick2CallInfoDisplay(yuzer,null)) {
	%>
		<%@ include file="/checkout/includes/i_click2call_footer_text.jspf" %>
	<% } else { %>
		<%@ include file="/checkout/includes/i_footer_text.jspf" %>
	<% } %>
	</form>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>
