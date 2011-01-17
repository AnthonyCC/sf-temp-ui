<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionWarning'%>  
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import="com.freshdirect.fdstore.EnumCheckoutMode"%>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<%! final java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%! final java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

<fd:CheckLoginStatus id="user" />
<%
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/view_cart.jsp");
request.setAttribute("listPos", "SystemMessage");

if (user.isEligibleForClientCodes()) {
	request.setAttribute("__yui_load_autocomplete__", Boolean.TRUE);
	request.setAttribute("__fd_load_clicode_edit__", Boolean.TRUE);
}
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
<%
	// first try to figure out FDShoppingCart controller parameters dynamically

    String successPage = request.getParameter("fdsc.succpage");
	String redemptionCode = request.getParameter("redemptionCode");
    if ((request.getMethod().equalsIgnoreCase("POST") && request.getParameter("checkout.x") != null) && (redemptionCode == null || "".equals(redemptionCode))) {
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
<tmpl:put name='title' direct='true'>FreshDirect - View Cart</tmpl:put>
<tmpl:put name='content' direct='true'>
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

%>

<%@ include file="/includes/i_modifyorder.jspf" %>

<form name="viewcart" method="post" action="/view_cart.jsp" style="margin:0px ! important" id="viewcart">
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="695">
    <TR VALIGN="TOP">
	    <TD CLASS="text11" WIDTH="395" VALIGN="bottom">
	        <FONT CLASS="title18">VIEW CART</FONT><BR>
			Please review the items in your cart before going to Checkout.<BR>
	        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
	    <TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10" style="color:#666666;font-weight:bold;">
	      <FONT CLASS="space2pix"><BR></FONT>
			<table>
				<tr>
					<td align="left">Delivery Charge:</td>
					<td align="right">
						<%	
								String dlvCharge = JspMethods.formatPrice( cart.getDeliverySurcharge() );
							if(cart.isDlvPassApplied()) {
						%>
							<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
							
						<%	} else if (cart.isDeliveryChargeWaived()) {
								if((int)cart.getDeliverySurcharge() == 0){
						%>     
								Free! 
								<% }else{ %> Free!(<%= dlvCharge %> waived)<% } %>
										
						<%  } else {%>
								<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
						<%}%>
					</td>
				</tr>
				<%if (cart.getTotalDiscountValue() > 0) {
								List discounts = cart.getDiscounts();
									for (Iterator iter = discounts.iterator(); iter.hasNext();) {
										ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
										Discount discount = discountLine.getDiscount();
								%>
							<tr>
								<td align="left" style="color:#669933;font-weight:bold;">Delivery Discount:</td>
								<td align="right" style="color:#669933;font-weight:bold;">-<%= JspMethods.formatPrice(discount.getAmount()) %></td>
							</tr>
				<%}	}%>
				<tr>
					<td align="left"><A HREF="javascript:popup('/help/estimated_price.jsp','small')"></A>Estimated Total:</td>
					<td align="right"><%= currencyFormatter.format(cart.getTotal()) %></td>
				</tr>
			</table>
	    </TD>
	    <TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
	        <input type="image" name="checkout" src="/media_stat/images/buttons/checkout_right.gif"
	         BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2">
		</TD>
    </TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<%@ include file="/includes/i_cartcleanup.jspf" %>
<%@ include file="/includes/i_viewcart.jspf" %> 

</form>

<!-- ===================================== -->
<!-- ============ Cart & tabs ============ -->
<!-- ===================================== -->

<% String smartStoreFacility = "view_cart"; %>
<%@ include file="/includes/smartstore/i_recommender_tabs.jspf" %>

<BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<% if (!"true".equals(request.getAttribute("recommendationsRendered"))) { %>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<% } %>

<form name="viewcart_bottom" method="POST" style="margin:0px">
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
		<TR VALIGN="TOP">
			<TD WIDTH="658" ALIGN="RIGHT" VALIGN="MIDDLE">
				<input type="image" name="checkout_delivery_address_select"  src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="91" HEIGHT="11" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"><BR>Delivery Address<BR>
			</TD>
			<TD WIDTH="35" ALIGN="RIGHT">
				<input type="image" name="checkout" src="/media_stat/images/buttons/checkout_right.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0">
			</TD>
		</TR>
	</TABLE>
</form>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
    <TR VALIGN="TOP">
        <TD WIDTH="693">
			<%@ include file="/checkout/includes/i_footer_text_2.jspf" %>
		</TD>
    </TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<img src="/media_stat/images/layout/dotted_line.gif" width="675" height="1" border="0"><br/>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

</tmpl:put>
</fd:RedemptionCodeController>
</fd:FDShoppingCart>
</tmpl:insert>
