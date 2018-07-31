<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.net.*"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>

<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-features" prefix="features" %>



<% //expanded page dimensions
final int W_CHECKOUT_STEP_1_CHOOSE_TOTAL = 970;

%>

<fd:CheckLoginStatus id="user" guestAllowed="false" redirectPage="/checkout/signup_ckt.jsp" />
<fd:EnableXCForCSR>
<features:redirect featureName="checkout2_0" />
</fd:EnableXCForCSR>
<%

// redirect to Survey if this is the second order && first order is delivered
if (!user.isSurveySkipped() && user.getAdjustedValidOrderCount()==1 && user.getDeliveredOrderCount()==1) {



// leave previous 4th order logic
    FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
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

        FDSurvey usability = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.SECOND_ORDER_SURVEY, user);
 		if (usability == null) {
			response.sendRedirect(response.encodeRedirectURL("/checkout/survey_cos.jsp?successPage=/checkout/step_1_choose.jsp"));
		} else {
	        FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(user.getIdentity(), user);
           int coverage=SurveyHtmlHelper.getResponseCoverage(usability,surveyResponse);
           if(coverage<usability.getAcceptableCoverage()) {

        	    response.sendRedirect(response.encodeRedirectURL("/checkout/survey.jsp?successPage=/checkout/step_1_choose.jsp"));
                return;
           }
    	}

    }
}
request.setAttribute("sitePage", "www.freshdirect.com/checkout/step_1_choose.jsp");
request.setAttribute("listPos", "SystemMessage,ZDeliveryRight");
%>
<%-- =================================================================================================== --%>

<% //check unattended %>
<%
if (user.getLevel()==FDUserI.RECOGNIZED) {
    response.sendRedirect(response.encodeRedirectURL("/login/login.jsp?successPage=/checkout/step_1_choose.jsp"));
    return;
}

String forwardPage = "/checkout/step_1_choose.jsp";

List<ErpAddressModel> dlvAddresses = FDCustomerFactory.getErpCustomer(user.getIdentity()).getShipToAddresses();
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="DELIVERY ADDRESS"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Checkout - Choose Delivery Address</tmpl:put> --%>
<tmpl:put name="extraCSSjs" direct="true">
<%@ include file="/includes/i_check_unattended_delivery.jspf" %>

<%@ include file="/common/template/includes/i_javascripts.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</tmpl:put>
<tmpl:put name='content' direct='true'>
<style type="text/css">
	td.chooser_radio {
		text-align: center;
	}
	td.chooser_radio input {
		margin: 0px;
		padding: 0px;
	}
</style>



<%
String actionName = "setDeliveryAddress";
String successPage = "/checkout/step_2_select.jsp";

//button include count
int incNextButtonCount = 0;
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


<%-- this action should be kept in sync with LocationHandlerTag.doSelectAddressAction() --%>
<fd:CheckoutController actionName='<%=actionName%>' result="result" successPage="<%=successPage%>">
	<%
	if (result.hasError("error_dlv_pass_only") || result.hasError("order_minimum")) {
	    response.sendRedirect(response.encodeRedirectURL("/checkout/view_cart.jsp"));
	    return;
	}

	double cartTotal = user.getShoppingCart().getTotal();
	if (result.hasError("cantGeocode")) {
	  String errorMsg=SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS;
	%>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<% } %>

	<% if (user.isEligibleForSignupPromotion() && user.getShoppingCart().getSubTotal() < user.getSignupDiscountRule().getMinSubtotal()) {
	    String errorMsg = "Because your cart contains less than $" + (int)user.getSignupDiscountRule().getMinSubtotal() + " of food, you are not eligible for the free fresh food promotion. You may return to your cart to add more food or continue Checkout now. <a href=\"javascript:popup('/shared/promotion_popup.jsp','large');\">Click for details</a>.";
	    %>
	    <%@ include file="/includes/i_error_messages.jspf" %>
	   <br/>
	<% }

	FDCartModel cart = user.getShoppingCart(); /* cart required for i_cart_delivery_fee.jspf include */

	%>

	<form method="POST" name="step1Form" id="step1Form" onSubmit="return checkPromoEligibilityByAddress('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');">
		<div class="gcResendBox" style="display:none"><!--  -->
			<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="gcResendBoxContent" id="gcResendBox">
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
					<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">IMPORTANT MESSAGE &nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
					<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				</tr>
				<tr>
					<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
					<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				</tr>
				<tr>
					<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
					<td><div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
						<%-- all your content goes in this div, it controls the height/width --%>
						The promotion code you entered <div id="promoCode"></div> is not valid for the address you selected. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
						<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>
						<a href="#" onclick="Modalbox.hide(); return false;">CHOOSE ANOTHER</a><br />
						<a href="#" onclick="javascript:document.forms['step1Form'].submit();"><b>CONTINUE</b></a><br />
						(promotion code will be removed)
					</div></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
					<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				</tr>
				<tr>
					<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_left_curve_8A6637.gif" width="6" height="6" alt="" /></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /></td>
					<td rowspan="2" colspan="2" style="background-color: #8A6637"><img src="/media_stat/images/layout/bottom_right_curve_8A6637.gif" width="6" height="6" alt="" /></td>
				</tr>
				<tr>
					<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				</tr>
			</table>
		</div>

	<%-- Start Header --%>
<%@ include file="/includes/i_modifyorder.jspf"  %>

<tmpl:insert template='<%= ((modifyOrderMode) ? "/includes/checkout_header_modify.jsp" : "/includes/checkout_header.jsp") %>'>
<% if(modifyOrderMode) { %>
	<tmpl:put name="ordnumb"><%= modifiedOrderNumber %></tmpl:put>
	<tmpl:put name="note"><%= modifyNote %></tmpl:put>
<% } %>
<%-- 	<tmpl:put name="title">DELIVERY ADDRESS</tmpl:put> --%>
	<tmpl:put name="delivery-fee">
		<span class="checkout-delivery-fee"><% if (FDStoreProperties.isNewFDTimeslotGridEnabled()) { %><fd:IncludeMedia name="/media/editorial/timeslots/msg_timeslots_learnmore.html"/><% } %></span>
		<%@ include file="/includes/i_cart_delivery_fee.jspf" %>
	</tmpl:put>
	<tmpl:put name="next-button"><%@ include file="/includes/i_cart_next_step_button.jspf" %></tmpl:put>
</tmpl:insert>
<!-- PROFILE HEADER -->
	<% if(!modifyOrderMode) { %>
		<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<% } %>
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

	<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_1_CHOOSE_TOTAL%>">
		 <tr valign="top">
	    <td class="text12"  valign="top">
	            <font class="title18">Choose Delivery Address (Step 1 of 4)</font><br/>
					<%if(user.isPickupOnly() ){%>
	        <b>Please Note: </b>Your home address is not in a FreshDirect <a href="javascript:fd.components.zipCheckPopup.openZipCheckPopup()">delivery zone</a>.
	        Please select one of our pickup locations to place an order.
					<%}else if(!user.isDepotUser()){%>
	        Please choose a delivery address for this order.
	            <%}else{%>
	        Where would you like to receive this order?
	            <%}%>
	        </td>
	        <td align="right">
		                    <% if (FDStoreProperties.isAdServerEnabled()) { %>
                          <div id='oas_ZDeliveryRight'>
				                    <SCRIPT LANGUAGE=JavaScript>
		                        <!--
		                            OAS_AD('ZDeliveryRight');
		                        //-->
		      	                </SCRIPT><br><br>
                          </div>
		                	 <% } %>

				        </td>
	    </tr>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
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
	if (errorMsg==null && user.isPromotionAddressMismatch()) {
	    Promotion promo = (Promotion)user.getEligibleSignupPromotion();
	    Double totalPromo = new Double(promo.getHeaderDiscountTotal());
	    errorMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, user.getCustomerServiceContact()});
	}
	if (errorMsg!=null) {%>
	    <%@ include file="/includes/i_error_messages.jspf"%>
	<%}%>
	    <%@ include file="/includes/ckt_acct/i_delivery_address_select.jspf" %>
	    <br/><br/>

	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br/>
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_1_CHOOSE_TOTAL%>" height="1" border="0"><br/>
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br/>

	<div style="margin-bottom: 10px;">
	    <div style="float: left;">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
		</div>
		<div style="float: left; margin-left: 5px; text-align: left;">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>Your Cart
		</div>
		<div style="float: right">
				<% if(modifyOrderMode) { %><a class="imgButtonWhite cancel_updates" href="/your_account/cancel_modify_order.jsp">cancel updates</a><% } %><%@ include file="/includes/i_cart_next_step_button.jspf" %>
		</div>
		<div style="clear: both;"></div>
	</div>

	<%
	if(ClickToCallUtil.evaluateClick2CallInfoDisplay(user,null)) {
	%>
		<%@ include file="/checkout/includes/i_click2call_footer_text.jspf" %>
	<% } else { %>
		<%@ include file="/checkout/includes/i_footer_text.jspf" %>
	<% } %>
	</form>

	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%=W_CHECKOUT_STEP_1_CHOOSE_TOTAL%>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</fd:CheckoutController>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>

</tmpl:put>
</tmpl:insert>
