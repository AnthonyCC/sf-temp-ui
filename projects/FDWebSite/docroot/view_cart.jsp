<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionWarning'%>  
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
<%@ page import="com.freshdirect.fdstore.EnumCheckoutMode"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<script type="text/javascript" language="javascript" src="/assets/javascript/FD_PromoEligibility.js"></script>



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
<div class="groupScaleBox" style="display:none"><!--  -->
		<table cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse;" class="groupScaleBoxContent" id="groupScaleBox" >
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/top_left_curve_8A6637_filled.gif" width="6" height="6" alt="" /></td>
				<td rowspan="2" style="background-color: #8A6637; color: #fff; font-size: 14px; line-height: 14px; font-weight: bold; padding: 3px;">GROUP DISCOUNT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="Modalbox.hide(); return false;" style="text-decoration: none;border: 1px solid #5A3815; background-color: #BE973A; font-size: 10px;	"><img src="/media_stat/images/layout/clear.gif" width="10" height="10" border="0" alt="" /></a></td>
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
					<br /><img onclick="Modalbox.hide(); return false;" src="/media_stat/images/buttons/close_window.gif" width="141" height="19" alt="" /><br />
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

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="695">
    <TR VALIGN="TOP">
	    <TD CLASS="text11" WIDTH="395" VALIGN="bottom">
	        <FONT CLASS="title18"><%= custFirstName %> Cart</FONT><BR>
	        Please review the items in your cart before going to Checkout.<BR>
	        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
	    <TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
	        <FONT CLASS="space2pix"><BR></FONT><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" WIDTH="57" HEIGHT="9" alt="CHECKOUT" VSPACE="2" border="0"><BR>
	        <A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated</A> Total: <%= currencyFormatter.format(cart.getTotal()) %>
	    </TD>
	    <TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
	        <input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif"
	         BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2">
		</TD>
    </TR>
</TABLE>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<% boolean http_link = true; %>

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
    <TD WIDTH="30"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" WIDTH="28" HEIGHT="28" border="0" alt="CONTINUE SHOPPING"></a></TD>
    <TD WIDTH="345"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  WIDTH="117" HEIGHT="13" border="0" alt="CANCEL CHECKOUT"></a><BR>from <FONT CLASS="text11bold"><a href="/index.jsp">Home Page</A></FONT>.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
    <TD WIDTH="283" ALIGN="RIGHT" VALIGN="MIDDLE"><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" WIDTH="57" HEIGHT="9" border="0" alt="CHECKOUT" VSPACE="0"></TD>
    <TD WIDTH="35" ALIGN="RIGHT"><input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></TD>
    </TR>
</TABLE>
</form>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
    <TR VALIGN="TOP">
        <TD WIDTH="693"><%@ include file="/checkout/includes/i_footer_text_2.jspf" %></TD>
    </TR>
</TABLE>

</tmpl:put>
</fd:RedemptionCodeController>
</fd:FDShoppingCart>
</tmpl:insert>
