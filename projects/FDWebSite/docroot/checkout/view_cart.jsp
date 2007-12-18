<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.framework.webapp.*;'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>
<fd:CheckLoginStatus id="user" />
<%
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/view_cart.jsp");
request.setAttribute("listPos", "SystemMessage");
%>

<% boolean showMinError = true; %>

<tmpl:insert template='/common/template/no_nav.jsp'>
<%
//System.out.println("Requester ="
    String successPage = null;
    String redemptionCode = request.getParameter("redemptionCode");
    if ((request.getMethod().equalsIgnoreCase("POST") && request.getParameter("checkout.x") != null) && (redemptionCode == null || "".equals(redemptionCode))) {
        successPage = "/checkout/step_1_choose.jsp";
    }
    
    String actionName = "updateQuantities";
    if ((request.getMethod().equalsIgnoreCase("POST") && request.getParameter("redemptionCodeSubmit.x") != null) || (redemptionCode != null && !"".equals(redemptionCode))) {
        actionName = "redeemCode";
        successPage = null;
    }
%>

<fd:FDShoppingCart id='cart' result='result' action='<%= actionName%>' successPage='<%= successPage %>' cleanupCart='true'>
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
    // get customer's first name
    String custFirstName = user.getLevel()==FDUser.GUEST ? "Your" : user.getFirstName() + "'s"; 
    //Reload the DP status from Database to make the session and DB are in sync.
     user.updateDlvPassInfo();
    
%>

<%@ include file="/includes/i_modifyorder.jspf" %>

<table border="0" cellspacing="0" cellpadding="0" width="695">
<FORM name="viewcart" method="post">
    <tr valign="top">
        <td class="text11" width="395" valign="bottom">
            <font class="title18"><%= custFirstName %> Cart</font><br>
            Please review the items in your cart before going to Checkout.<br>
            <img src="/media_stat/images/layout/clear.gif" width="375" HEIGHT="1" border="0"></td>
        <td width="265" align="right" valign="middle" class="text10bold">
            <font class="space2pix"><br></font><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" width="57" HEIGHT="9" alt="CHECKOUT" VSPACE="2" border="0"><br>
            <A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= currencyFormatter.format(cart.getTotal()) %>
        </td>
        <td width="35" align="right" valign="middle"><font class="space2pix"><br></font>
            <input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif"
             border="0" alt="CONTINUE CHECKOUT" VSPACE="2"></td>
    </tr>
</table>
    <img src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="8" border="0"><br>
    <img src="/media_stat/images/layout/ff9933.gif" width="693" HEIGHT="1" border="0"><br>
    <img src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="8" border="0"><br>
    
    <%@ include file="/includes/i_cartcleanup.jspf" %>
    
    <%@ include file="/includes/i_viewcart.jspf" %>
    
    <br><br>
    <img src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="8" border="0"><br>
    <img src="/media_stat/images/layout/ff9933.gif" width="693" HEIGHT="1" border="0"><br>
    <img src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="8" border="0"><br>
    
<table width="693" border="0" cellspacing="0" cellpadding="0">
    <tr valign="top">
        <td width="30"><a href="/index.jsp"><img src="/media_stat/images/buttons/x_green.gif" width="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></td>
        <td width="345"><img src="/media_stat/images/buttons/cancel_checkout.gif" width="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><br>and return to your cart.<br><img src="/media_stat/images/layout/clear.gif" width="340" HEIGHT="1" border="0"></td>
        <td width="283" align="right" valign="middle"><input type="image" name="checkout" src="/media_stat/images/buttons/checkout.gif" width="57" HEIGHT="9" border="0" alt="CHECKOUT" VSPACE="0"></td>
        <td width="35" align="right"><font class="space2pix"><br></font><input type="image" name="checkout" src="/media_stat/images/buttons/checkout_arrow.gif" width="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></td>
    </tr>
</table>
    <%@ include file="/checkout/includes/i_footer_text_2.jspf" %>
     
</FORM>

</tmpl:put>
</fd:RedemptionCodeController>
</fd:FDShoppingCart>
</tmpl:insert>