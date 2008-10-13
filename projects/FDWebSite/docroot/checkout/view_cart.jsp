<!-- checkout / view cart -->
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>  
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


boolean showMinError = true;
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
<tmpl:put name='title' direct='true'>FreshDirect - View Cart</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
	if(!UserValidationUtil.validateContainsDlvPassOnly(request, result)) {
		//If the above validation is successful then check for order minimum.
		if (user.getShoppingCart().getDeliveryAddress()!=null ){
			UserValidationUtil.validateOrderMinimum(request, result); 
			String errorMsg = (String)session.getAttribute(SessionName.ATP_WARNING);
			if (errorMsg!=null) {
				session.removeAttribute(SessionName.ATP_WARNING);
				showMinError = false;
				%><%@ include file="/includes/i_error_messages.jspf" %><%
			}
		}
	}
%>
<fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
    <% if (showMinError) { %><%@ include file="/includes/i_error_messages.jspf" %><% } %>   
</fd:ErrorHandler>
<fd:ErrorHandler result="<%=redemptionResult%>" name="redemption_error" id="errorMsg">
    <%@ include file="/includes/i_error_messages.jspf" %>   
</fd:ErrorHandler>

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
    // get customer''s first name
    String custFirstName = user.getLevel()==FDUser.GUEST ? "Your" : user.getFirstName() + "'s"; 
    //Reload the DP status from Database to make the session and DB are in sync.
    user.updateDlvPassInfo();
    //TODO - Reset the DCPD eligiblity map to re-calculate the promo if in case promotion was modified.
   user.getDCPDPromoProductCache().clear();

%>

<%@ include file="/includes/i_modifyorder.jspf" %>

<!-- display items just added to cart  -->
<%
	/**
	 * Display recently added products
	 * FIXME: put this partial to a separate jsp
	 */
	if (cart.getRecentOrderLines().size() > 0 && "1".equalsIgnoreCase(request.getParameter("confirm"))) {
%><%@ include file="/includes/smartstore/i_recent_orderlines.jspf" %>
<br/>
<%
	}
%>
<form name="viewcart" method="post" action="/checkout/view_cart.jsp" style="margin:0px ! important">
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="695">
    <TR VALIGN="TOP">
	    <TD CLASS="text11" WIDTH="395" VALIGN="bottom">
	        <FONT CLASS="title18"><%= custFirstName %> Cart</FONT><BR>
	        Please review the items in your cart before going to Checkout.<BR>
	        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
	    <TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
	        <FONT CLASS="space2pix"><BR></FONT><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" WIDTH="57" HEIGHT="9" alt="CHECKOUT" VSPACE="2" border="0"><BR>
	        <A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= currencyFormatter.format(cart.getTotal()) %>
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

<%@ include file="/includes/i_cartcleanup.jspf" %>

<%@ include file="/includes/i_viewcart.jspf" %> 

</form>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<!-- Place of SmartStore Recommendations Tag -->
<%@ include file="/includes/smartstore/i_dyf.jspf" %>
<BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<% if (!"true".equals(request.getAttribute("recommendationsRendered"))) { %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<% } %>
<form name="viewcart_bottom" method="POST" action="/checkout/view_cart.jsp" style="margin:0px">
	<table width="693" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
			<td width="30"><a href="/index.jsp"><img src="/media_stat/images/buttons/x_green.gif" width="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></td>
			<td width="345"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><br>and return to your cart.<br><img src="/media_stat/images/layout/clear.gif" width="340" HEIGHT="1" border="0"></td>
			<td width="283" align="right" valign="middle"><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" width="57" HEIGHT="9" border="0" alt="CHECKOUT" VSPACE="0"></td>
			<td width="35" align="right"><font class="space2pix"><br></font><input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></td>
		</tr>
	</table>
<%@ include file="/checkout/includes/i_footer_text_2.jspf" %>
</form>

</tmpl:put>
</fd:RedemptionCodeController>
</fd:FDShoppingCart>
</tmpl:insert>
