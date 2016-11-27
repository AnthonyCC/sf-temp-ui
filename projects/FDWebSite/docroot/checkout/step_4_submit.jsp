<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='java.text.MessageFormat' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import='com.freshdirect.payment.EnumPaymentMethodType'%>
<%@ page import="java.util.Locale"%>  
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% //expanded page dimensions
final int W_CHECKOUT_STEP_4_SUBMIT_TOTAL = 970;
Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
%>

<%!
// final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<%
	// [APPDEV-2149] Display generic timeslot table (Just days of week, no restrictions, etc.)
	final boolean abstractTimeslots = EnumCheckoutMode.MODIFY_SO_TMPL == user.getCheckoutMode();

	final String actionName = abstractTimeslots ? "modifyStandingOrderTemplate" : "submitOrder";
	boolean isEBTPayment = false;
	
	
	if(null !=user){
		user.setRefreshCouponWalletRequired(true);
		user.setCouponEvaluationRequired(true);
	}
%>
<tmpl:insert template='/common/template/checkout_nav.jsp'>

<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag pageId=""></fd:SEOMetaTag>
</tmpl:put>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Review & Submit Order</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
		FDSessionUser fdSessionUser = (FDSessionUser) user;
		MasqueradeContext masqueradeContext = user.getMasqueradeContext();
%>
<fd:FDShoppingCart id='cart' result="result" filterCoupons="true">
<tmpl:put name="changeDeliveryDate-button">
	<% if ( cart.getExpCouponDeliveryDate() != null && !"".equals(cart.getExpCouponDeliveryDate()) ) {
		%><a href="/checkout/step_2_select.jsp" class="imgButtonWhite">Change Delivery Date</a><%
	} %>
</tmpl:put>
<%
	String succPage = "step_4_receipt.jsp";

	if (abstractTimeslots) {
		final FDStandingOrder so = user.getCurrentStandingOrder();
	
		StringBuilder buf = new StringBuilder(FDURLUtil.getStandingOrderLandingPage(so, null, user));
		buf.append("&tmpl_saved=1");
	
		succPage = buf.toString();
	}
	
	boolean isFdxOrder = false;
	EnumEStoreId EStoreIdEnum = null;
	EStoreIdEnum = cart.getEStoreId();
	if (EStoreIdEnum != null && (EStoreIdEnum).equals(EnumEStoreId.FDX)) { isFdxOrder = true; } 
	
%>
<fd:CheckoutController actionName="<%= actionName %>" result="result" successPage="<%= succPage %>">
<%
        FDIdentity identity  = user.getIdentity();
        ErpAddressModel dlvAddress = cart.getDeliveryAddress();
        ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
        ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(identity);

		final boolean orderAmountFraud = abstractTimeslots ? true : result.hasError("order_amount_fraud");
		final boolean doubleSubmit = abstractTimeslots ? true : result.hasError("processing_order");
		final boolean __noErr = abstractTimeslots ? true : !orderAmountFraud && !doubleSubmit;
		
		// Save checkout mode for receipt page.
		session.setAttribute("checkout_mode", user.getCheckoutMode().toString());
		
		//for Google Analytics (used in shared include i_step_4_cart_details.jspf)
		String sem_orderNumber = "0";
%>
<% if (!abstractTimeslots) { %><fd:SmartSavingsUpdate promoConflictMode="true"/><% } 
		//button include count
		int incNextButtonCount = 0;
%>

<%@ include file="/includes/i_modifyorder.jspf" %>

<script type="text/javascript">
				FreshDirect.terms=true;
</script>

	 <%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
				<script type="text/javascript">
				FreshDirect.terms=<%=fdTcAgree.booleanValue()%>;
				$jq(document).on('ready',  function() {
					doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'400px\' height=\'400px\' frameborder=\'0\' ></iframe>');
				});
				</script>
	<%}%>

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


<% if (!abstractTimeslots) { %><form method="post" name="order_submit" id="order_submit">
<% } else { %><form method="post" name="order_submit" id="order_submit" onSubmit="return checkPromoEligibilityByMaxRedemptions('<%= null==user.getRedeemedPromotion()?"null":"not null" %>');">
<% } %>
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
					The promotion code you entered <div id="promoCode"></div> has been redeemed by the maximum number of customers and is no longer available. <a href="#" onclick="javascript:$('more_info').toggle()">More Info</a><br /><br />
					<div id="more_info" style="display:none">This is the more info hidden div.<br /><br /></div>					
					<a href="#" onclick="javascript:document.forms['order_submit'].submit();"><b>CONTINUE</b></a><br />
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

	<div>
	<%-- Start Header --%>
<tmpl:insert template='<%= ((modifyOrderMode) ? "/includes/checkout_header_modify.jsp" : "/includes/checkout_header_submit.jsp") %>'>
<% if(modifyOrderMode) { %>
	<tmpl:put name="ordnumb"><%= modifiedOrderNumber %></tmpl:put>
	<tmpl:put name="note"><%= modifyNote %></tmpl:put>
<% } %>
	<tmpl:put name="title">Review Your Order</tmpl:put>
	<tmpl:put name="delivery-fee">
		<span class="checkout-delivery-fee"><% if (FDStoreProperties.isNewFDTimeslotGridEnabled()) { %><fd:IncludeMedia name="/media/editorial/timeslots/msg_timeslots_learnmore.html"/><% } %></span>
		<%@ include file="/includes/i_cart_delivery_fee.jspf" %>
	</tmpl:put>
	<tmpl:put name="changeDeliveryDate-button">
		<% if ( cart.getExpCouponDeliveryDate() != null && !"".equals(cart.getExpCouponDeliveryDate()) ) {
			%><a href="/checkout/step_2_select.jsp" class="imgButtonWhite">Change Delivery Date</a><%
		} %>
	</tmpl:put>
	<tmpl:put name="next-button">
		<%if(user.getMasqueradeContext()!=null) {%>
			Don't send email invoice<input type="checkbox" name="silent_mode">
		<%}%>
		<% if (__noErr) { %>
			<% if (abstractTimeslots) { %>
				<button class="cssbutton green small" type="submit">Click here to save your standing order</button> 
			<% } else { %>
				<%@ include file="/includes/i_cart_next_step_button.jspf" %>
			<% } %>
		<% } %>
	</tmpl:put>
</tmpl:insert>
<!-- PROFILE HEADER -->
	<div>
	<% if(!modifyOrderMode) { %>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<% } %>
	<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
	<div style="clear: both;"></div>
	</div>
	</div>
   
    <%-- error system messages happen here --%>
   <% StringBuffer sbErrorMsg= new StringBuffer(); %>
    
    <fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'>
        <%
        sbErrorMsg.append("<br>");
		sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'>
        <% 
        sbErrorMsg.append("<br>Checkout prevented because:<br>");
        sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='order_amount_fraud' id='errorMsg'>
        <%
        sbErrorMsg.append("<br>");
        sbErrorMsg.append(errorMsg);
        sbErrorMsg.append("<br>");
        %>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
		<% 
        sbErrorMsg.append(errorMsg);
        %>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
		<br><span class="text11rbold"><%= errorMsg %></span><br><br>
    </fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='invalid_reservation' id='errorMsg'>
		<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='invalid_deliverypass' id='errorMsg'>
    	<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
    	<fd:ErrorHandler result='<%=result%>' name='redemption_exceeded' id='errorMsg'>
        <input type = "hidden" name="ignorePromoErrors" value="true"/>
    	<br><span class="text11rbold"><%= errorMsg %></span><br><br>
	</fd:ErrorHandler>
    
    
<%
if (!abstractTimeslots && user.isPromoConflictResolutionApplied()) {
    user.setPromoConflictResolutionApplied(false);                                    

    result.addWarning(new ActionWarning("promo_war2", SystemMessageList.MSG_PROMOTION_APPLIED_VARY2));
%>	<fd:ErrorHandler result='<%= result %>' name='promo_war2' id='errorMsg'>
<%@ include file="/includes/i_warning_messages.jspf" %>   
	</fd:ErrorHandler>
<%
}

        if (!abstractTimeslots && doubleSubmit) {
			sbErrorMsg.append(result.getError("processing_order").getDescription());
        }

		String warningMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
		if (!abstractTimeslots && warningMsg==null && user.isPromotionAddressMismatch()) {
			Promotion promo = (Promotion)user.getEligibleSignupPromotion();
			Double totalPromo = new Double(promo.getHeaderDiscountTotal());
		    warningMsg = MessageFormat.format(SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE, new Object[]{totalPromo, user.getCustomerServiceContact()});
		}
		if (warningMsg!=null) {
			sbErrorMsg.append(warningMsg);
		}

        if (sbErrorMsg.length() > 0 ) {
         String errorMsg = sbErrorMsg.toString();
	%>
	<%@ include file="/includes/i_error_messages.jspf"%> 	
<%		}
%>
<% String receipt = ""; %>
<%@ include file="/includes/ckt_acct/i_step_4_delivery_payment.jspf" %>

<IMG src="/media_stat/images/layout/clear.gif" width="1" height="1"><br>
<IMG src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>" height="1"><br>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="20"><br>

<table width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>" cellpadding="0" cellspacing="0" border="0" style="margin-top:1em">
	<tr VALIGN="TOP">
		<td width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>"><img src="/media_stat/images/navigation/cart_details.gif" width="96" height="15" border="0" alt="CART DETAILS">&nbsp;&nbsp;&nbsp;
<% if (request.getRequestURI().toLowerCase().indexOf("your_account/") != 1){ %>
<FONT CLASS="text9">If you would like to make any changes to your order, <A HREF="/view_cart.jsp?trk=chkplc">click here</A> to go back to your cart.</FONT><BR>
<% } %>
			<IMG src="/media_stat/images/layout/999966.gif" width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>" height="1" BORDER="0" VSPACE="3"><br>
			<IMG src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
			<% if (!abstractTimeslots) { %><font class="title11"><b>Note:</b></font> <font class="text11orbold">Our goal is to fill your order with food of the highest quality. Occasionally, we'll get a shipment that doesn't meet our standards and we cannot accept it. Of course, if this happens, FreshDirect will not charge you for the missing item.</font><% } %>
		</td>
	</tr>
	<tr>
		<td align="left">
			<% if ( cart.getExpCouponDeliveryDate() != null && !"".equals(cart.getExpCouponDeliveryDate()) ) { %>
				<div id="fdCoupon_cartAlertExpire">
					<table>
						<tr>
							<td rowspan="3" valign="top" width="90">ATTENTION</td>
							<td><img src="/media/images/ecoupon/red_triangle.png" alt="" /></td>
							<td>Some coupons not valid on date of delivery. For all coupons to be valid, select date on or before:</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td class="fdCoupon_cartAlertExpire_date"><%= cart.getExpCouponDeliveryDate() %></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><tmpl:get name="changeDeliveryDate-button" /></td>
						</tr>
					</table>
				</div>
			<% } %>
<% if (abstractTimeslots) { %>
<%-- CART DETAILS START --%>
			<table width="<%= W_CHECKOUT_STEP_4_SUBMIT_TOTAL %>" style="">
<%
	// final DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	for (WebOrderViewI ov : cart.getOrderViews()) {
		String __lastDeptDesc = null;
%>
				<tbody>
<%
		for (FDCartLineI l : ov.getOrderLines()) {
			final String deptDesc = l.getDepartmentDesc();
			
			if ( __lastDeptDesc == null || __lastDeptDesc.compareTo(deptDesc) != 0) {
%>
					<tr>
						<td class="text10" style="width: 8em; text-align: right; padding-left: 2em; padding-right: 2em;"></td>
						<td class="text10" style="width: 22px"></td>
						<td class="text11orbold" colspan="3" style="padding-top: 1em;"><%= deptDesc %></td>
					</tr>
<%
				__lastDeptDesc = deptDesc;
			}
			
			
%>
					<tr>
						<td class="text10" style="width: 8em; text-align: right; padding-left: 2em; padding-right: 2em;"><%= l.getDisplayQuantity() %></td>
						<td class="text10" style="width: 22px"><%= l.getLabel()%></td>
						<td class="text10" style="width: 100%"><span class="text10bold" style="margin-left: 6px;"><%= l.getDescription() %></span><% if (l.getConfigurationDesc() != null) { %> (<%= l.getConfigurationDesc() %>)<% } %></td>
						<td class="text10" style="width: 70px; text-align: right; padding-right: 60px;">(<%= l.getUnitPrice() %>)</td>
						<td class="text10bold" style="text-align: right"><%= JspMethods.formatPrice(l.getPrice()) %></td>
					</tr>
<%			
		}
%>
					<tr>
						<td colspan="5" class="orderViewSeparator"></td>
					</tr>
					<tr class="orderViewSummary">
						<td colspan="4" style="text-align: right; padding-right: 60px;"><%= ov.getDescription() %><% if (ov.isEstimatedPrice()) { %> Estimated<% } %> Subtotal:</td>
						<td style="align: right; font-weight: bold"><%= JspMethods.formatPrice(ov.getSubtotal()) %></td>
					</tr>
				</tbody>
<%
	}
%>
			</table>
<%-- CART DETAILS END --%>
<% } else { %>
			<%@ include file="/includes/ckt_acct/i_step_4_cart_details.jspf" %>
<% } %>
		</td>
	</tr>
</table>

<BR>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>" height="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" width="1" height="8" BORDER="0"><BR>

<div style="margin-bottom: 15px; margin-top: 5px; position: relative; text-align: right; min-height: 26px;">
		<div style="position: absolute; top: 0px; left: 0px; width: 150px; height: 26px; text-align: left;">
			<span style="display: inline-block;">
				<a href="<%=response.encodeURL("/checkout/step_3_choose.jsp")%>" id="previousX">
				<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</span>
			<span style="display: inline-block; vertical-align: top; text-align: center; margin-left: 5px; text-align: left;">
				<a href="<%=response.encodeURL("/checkout/step_3_choose.jsp")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Payment Method<br/>
			</span>
		</div>

<% if (__noErr) { %>
	<% if (abstractTimeslots) { %>
		<button class="cssbutton green small" type="submit">Click here to save your standing order</button> 
	<% } else { %>
		 <% if(modifyOrderMode) { %><a class="imgButtonWhite cancel_updates" href="/your_account/cancel_modify_order.jsp">cancel updates</a><% } %><%@ include file="/includes/i_cart_next_step_button.jspf" %>
	<% } %>
	<% if (cart instanceof FDModifyCartModel) { %><div style="clear: both;"></div><% } %>
<% } %>
</div>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>

</FORM>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_4_SUBMIT_TOTAL%>" height="1" border="0"><br/>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>
