<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.PromoVariantHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionWarning'%>  
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
<%
//Added for Smart Savings.
    Map savingsLookupTable = (Map) session.getAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
    if(savingsLookupTable == null){
        savingsLookupTable = new HashMap();
    }
    PromoVariantHelper.updateSavingsVariant(user, savingsLookupTable);
    String usrVariant = user.getSavingsVariantId();
    if(usrVariant != null && usrVariant.length() > 0) {
        PromoVariantHelper.updateSavingsVariantFound(user, 5, request);
    } else {
        user.setSavingsVariantFound(false);
    }
    session.setAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE, savingsLookupTable);
    String savingsVariant =  (String) session.getAttribute(SessionName.PREV_SAVINGS_VARIANT);
    Boolean prevVariantFound = (Boolean) session.getAttribute(SessionName.PREV_VARIANT_FOUND);
    boolean usrVariantFound = user.isSavingsVariantFound();
    if((usrVariant != null && !usrVariant.equals(savingsVariant)) || (prevVariantFound != null && usrVariantFound != prevVariantFound.booleanValue())) {
        //If current savings variant is different from previous savings variant

        user.updateUserState();
        session.setAttribute(SessionName.PREV_SAVINGS_VARIANT, usrVariant);
        session.setAttribute(SessionName.PREV_VARIANT_FOUND, new Boolean(usrVariantFound));
    }
%>    
<tmpl:put name='title' direct='true'>FreshDirect - View Cart</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:ErrorHandler result='<%=result%>' name='order_minimum' id='errorMsg'>
    <%@ include file="/includes/i_error_messages.jspf" %>   
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
<%
if(cart.hasHeaderDiscount() && cart.getTotalLineItemsDiscountAmount()>0){
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

<form name="viewcart" method="post" action="/view_cart.jsp" style="margin:0px ! important">
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

<%@ include file="/includes/i_cartcleanup.jspf" %>
<%@ include file="/includes/i_viewcart.jspf" %> 

</form>

<!-- ===================================== -->
<!-- ============ Cart & tabs ============ -->
<!-- ===================================== -->

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
