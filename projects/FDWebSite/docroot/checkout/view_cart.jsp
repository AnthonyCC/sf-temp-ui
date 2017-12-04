<%@ page import='java.util.*' %>
<%@ page import='java.io.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionWarning'%>  
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import="com.freshdirect.customer.EnumChargeType" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>

<% //expanded page dimensions
final int W_CHECKOUT_VIEW_CART_TOTAL = 970;
%>

<%! final java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%! final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>
<fd:CheckLoginStatus id="user" />
<fd:EnableXCForCSR>
<features:redirect featureName="checkout2_0" checkout="true" />
</fd:EnableXCForCSR>
<%
String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/view_cart.jsp");
request.setAttribute("listPos", "SystemMessage");

if (user.isEligibleForClientCodes()) {
	request.setAttribute("__yui_load_autocomplete__", Boolean.TRUE);
	request.setAttribute("__fd_load_clicode_edit__", Boolean.TRUE);
}

boolean showMinError = true;
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="YOUR CART-Please review the items in your cart before going to Checkout"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - View Cart</tmpl:put> --%>
<tmpl:put name='extraCss' direct='true'>
  <jwr:style src="/viewcart.css"/>
  <jwr:style src="/quickshop.css"/>
</tmpl:put>
<%
	// first try to figure out FDShoppingCart controller parameters dynamically

    String successPage = request.getParameter("fdsc.succpage");
	String redemptionCode = request.getParameter("redemptionCode");
   
	if (request.getMethod().equalsIgnoreCase("POST") && (redemptionCode == null || "".equals(redemptionCode)) 
    	&& (!"".equals(request.getParameter("nextStep")) &&  request.getParameter("nextStep") != null)
	) {
		successPage = "/checkout/step_1_choose.jsp";
	}

    String actionName = request.getParameter("fdsc.action");
	if (actionName == null) {
		actionName = "updateQuantities";
	    if ((request.getMethod().equalsIgnoreCase("POST") && request.getParameter("redemptionCodeSubmit.x") != null) || (redemptionCode != null && !"".equals(redemptionCode))) {
	        actionName = "redeemCode";
	        successPage = null;
	    }
    }
	    
	String cartSource = request.getParameter("fdsc.source"); // can be null
%>

<fd:FDShoppingCart id='cart' result='result' action='<%= actionName %>' successPage='<%= successPage %>' cleanupCart='true' source='<%= cartSource %>'>
<fd:RedemptionCodeController actionName="<%=actionName%>" result="redemptionResult">
<fd:SmartSavingsUpdate promoConflictMode="false"/>

<tmpl:put name='content' direct='true'>
<%
	if(!UserValidationUtil.validateContainsDlvPassOnly(request, result)) {
		//If the above validation is successful then check for order minimum.
		if (user.getShoppingCart().getDeliveryAddress()!=null ){
			UserValidationUtil.validateOrderMinimum(request.getSession(), result); 
			String errorMsg = (String)session.getAttribute(SessionName.ATP_WARNING);
			if (errorMsg!=null) {
				session.removeAttribute(SessionName.ATP_WARNING);
				showMinError = false;
				%><%@ include file="/includes/i_error_messages.jspf" %><%
			}
		}
	}
%>
<fd:ErrorHandler result="<%=redemptionResult%>" name="signup_warning" id="errorMsg">
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
    <% if (showMinError) { %><%@ include file="/includes/i_error_messages.jspf" %><% } %>   
</fd:ErrorHandler>
<% ActionError sw_err = redemptionResult.getError("signup_warning");
	if(sw_err != null && !"signup_warning".equals(sw_err.getType())){
%>
<fd:ErrorHandler result="<%=redemptionResult%>" name="redemption_error" id="errorMsg">
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>
<% } %>

<%-- FOR DELIVERY PASS --%>
<fd:ErrorHandler result='<%=result%>' name='error_dlv_pass_only' id='errorMsg'>
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='pass_expired' id='errorMsg'>
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='pass_cancelled' id='errorMsg'>
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>


<!-- ===================================================================================
     NOTE: there's some promo cells commented out at bottom of table, some comments there 
    =================================================================================== -->
<%
if(cart.getNonCombinableHeaderPromotion() != null && cart.getTotalLineItemsDiscountAmount()>0){
StringBuffer buffer = new StringBuffer(
					SystemMessageList.MSG_PROMOTION_APPLIED_VARY1);
			result.addWarning(new ActionWarning("promo_war1", buffer
					.toString()));
                    
                    
%>

<fd:ErrorHandler result='<%=result%>' name='promo_war1' id='errorMsg'>
    <%@ include file="/includes/i_warning_messages.jspf" %>   
</fd:ErrorHandler>
<%
}
    // get customer''s first name
    String custFirstName = user.getLevel()==FDUser.GUEST ? "Your" : user.getFirstName() + "'s"; 
    //Reload the DP status from Database to make the session and DB are in sync.
    user.updateDlvPassInfo();
    //TODO - Reset the DCPD eligiblity map to re-calculate the promo if in case promotion was modified.
	user.getDCPDPromoProductCache().clear();
    
	//button include count
	int incNextButtonCount = 0;
%>

<%if(user.getMasqueradeContext()!=null) {%>
	<fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
<%} %>

<div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
<potato:viewCart />
<% if (user.getCurrentStandingOrder() == null) { %>
<!-- product sampling carousel -->
<soy:render template="common.productSampleCarousel" data="${viewCartPotato.productSamplesTab}" />
<% } %>
</div>


<form name="viewcart" id="viewcart" method="post" style="margin:0px ! important">
	<div class="groupScaleBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;"><img src="/media_stat/images/buttons/exit_trans.gif" width="10" height="10" border="0" alt="" /></a></td>
				<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
			</tr>
			<tr>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
			</tr>
			<tr>
				<td style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" /></td>
				<td>
					<%-- all your content goes in this div, it controls the height/width --%>
					<div id="group_info" style="display:none">This is the more info hidden div.<br /><br /></div>
					<div style="height: auto; width: 200px; text-align: center; font-weight: bold;">
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="Close Window" /><br />
					</div>
				</td>
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
<%-- 	<tmpl:put name="title">YOUR CART<small>Please review the items in your cart before going to Checkout.</small></tmpl:put> --%>
	<tmpl:put name="delivery-fee"><%@ include file="/includes/i_cart_delivery_fee.jspf" %></tmpl:put>
	<tmpl:put name="next-button"><%@ include file="/includes/i_cart_next_step_button.jspf" %></tmpl:put>
</tmpl:insert>
<% boolean http_link = false; %>

<!-- PROFILE HEADER -->
<% if(!modifyOrderMode) { %>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<% } %>
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0">

<%@ include file="/includes/i_cartcleanup.jspf" %>

<%@ include file="/includes/i_viewcart.jspf" %> 

</form>


<soy:render template="common.viewCartTabbedCarousel" data="${viewCartPotato}" />

<table border="0" cellspacing="0" cellpadding="0" width="<%= W_CHECKOUT_VIEW_CART_TOTAL %>">
    <tr valign=""top"">
      <td width="<%= W_CHECKOUT_VIEW_CART_TOTAL %>">
			<%@ include file="/checkout/includes/i_footer_text_2.jspf" %>
		</td>
    </tr>
</table>

<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%= W_CHECKOUT_VIEW_CART_TOTAL-2 %>" height="1" border="0" alt="" /><br />
<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

<%if ("updateQuantities".equals(actionName)) {%>
	<fd:CmShop5 wrapIntoScriptTag="true" cart="<%=cart%>"/>	
<%}%>


</tmpl:put>
</fd:RedemptionCodeController>
</fd:FDShoppingCart>
</tmpl:insert>
