<%@ page import='java.util.*' %>
<%@ page import='java.io.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionWarning'%>  
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import="com.freshdirect.fdstore.EnumCheckoutMode"%>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import="com.freshdirect.customer.EnumChargeType" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>

<% //expanded page dimensions
final int W_VIEWCART_TOTAL = 970;
final int W_VIEWCART_FHALF = 395;
final int W_VIEWCART_LHALF = 478;
final int W_VIEWCART_LHALF_FP = 360;
final int W_VIEWCART_TOTAL_FP = 935;
final int W_VIEWCART_LP = 215;
%>

<%! final java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%! final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

<fd:CheckLoginStatus id="user" />
<fd:EnableXCForCSR>
<features:redirect featureName="checkout2_0">
	<potato:pendingExternalAtcItem/>
	<%
	String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/view_cart.jsp");
	request.setAttribute("listPos", "SystemMessage");
	
	if (user.isEligibleForClientCodes()) {
		/** request.setAttribute("__yui_load_autocomplete__", Boolean.TRUE); **/
		request.setAttribute("__fd_load_clicode_edit__", Boolean.TRUE);
	}
	%>
	<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="YOUR CART-Please review the items in your cart before going to Checkout"></fd:SEOMetaTag>
	</tmpl:put>
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
	<%-- <tmpl:put name='title' direct='true'>FreshDirect - View Cart</tmpl:put> --%>
	<tmpl:put name='content' direct='true'>
	<fd:FDShoppingCart id='cart' result='result' action='<%= actionName %>' successPage='<%= successPage %>' cleanupCart='true' source='<%= cartSource %>'>
	<fd:RedemptionCodeController actionName="<%=actionName%>" result="redemptionResult">
	<fd:javascript src="/assets/javascript/FD_PromoEligibility.js"/>
	<fd:ErrorHandler result="<%=redemptionResult%>" name="signup_warning" id="errorMsg">
	    <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
	    <%@ include file="/includes/i_error_messages.jspf" %>   
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
	    // get customer's first name
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
	
	<potato:viewCart />
	<div fd-toggle="product-sample-carousel" fd-toggle-state="enabled">
	  <% if (user.getCurrentStandingOrder() == null) { %>
	    <soy:render template="common.productSampleCarousel" data="${viewCartPotato.recommendationTabs[0]}" />
	  <% } %>
	</div>
	
	<form name="viewcart" id="viewcart" method="post" action="/view_cart.jsp" style="margin:0px ! important">
		<div class="groupScaleBox" style="display:none"><!--  -->
			<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
					<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/buttons/exit_trans.gif" width="10" height="10" border="0" alt="" /></a></td>
					<td colspan="2"><img src="/media_stat/images/layout/top_right_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				</tr>
				<tr>
					<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
					<td colspan="2" style="background-color: #8A6637;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td>
				</tr>
				<tr>
					<td style="background-color: #8A6637;"><img src="/media_stat/images/buttons/clear.gif" width="1" height="1" alt="" /></td>
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
	<% boolean http_link = true; %>
	<!-- PROFILE HEADER -->
	<% if(!modifyOrderMode) { %>
		<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<% } %>
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" />
	
	<%@ include file="/includes/i_cartcleanup.jspf" %>
	
	<%@ include file="/includes/i_viewcart.jspf" %> 
	
	</form>
	
	  <div id="cartCarousels">
	    <soy:render template="common.viewCartTabbedCarousel" data="${viewCartPotato}" />
	  </div>
	
	<table border="0" cellspacing="0" cellpadding="0" width="<%= W_VIEWCART_TOTAL %>">
	    <TR valign="TOP">
	      <TD width="<%= W_VIEWCART_TOTAL %>">
				<%@ include file="/checkout/includes/i_footer_text_2.jspf" %>
			</TD>
	    </TR>
	</table>
	
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%= W_VIEWCART_TOTAL-2 %>" height="1" border="0" alt="" /><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
	
	<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
	
		<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
		
	<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
	
	</fd:RedemptionCodeController>
	</fd:FDShoppingCart>
	
	<script>
	  window.FreshDirect = window.FreshDirect || {};
	  window.FreshDirect.pendingCustomizations = <fd:ToJSON object="${pendingExternalAtcItemPotato}" noHeaders="true"/>;
	  window.FreshDirect.viewcart = window.FreshDirect.viewcart || {};
	  window.FreshDirect.viewcart.data = <fd:ToJSON object="${viewCartPotato}" noHeaders="true"/>;
	</script>
	
	</tmpl:put>
	</tmpl:insert>
</features:redirect>
</fd:EnableXCForCSR>