<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage="/login/login.jsp?successPage=/checkout/step_1_choose.jsp" />

<%
    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	double cartTotal = user.getShoppingCart().getTotal();
	
	String custId = null;
	FDIdentity identity = null;
	try {
		//FDUserI _user = (FDUserI) session.getAttribute(SessionName.USER);
		identity = user.getIdentity();
		custId = identity.getFDCustomerPK();
	} catch (NullPointerException ex) {
		System.out.println("\n caught exception trying to get USER from session...\n");
	}
	//FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(custId);

	boolean proceedThruCheckout = false;
	if ( "true".equals( request.getParameter("proceed") ) )
		proceedThruCheckout = true;
		
	String successPage = "/checkout/step_1_choose.jsp?addressId="+ request.getParameter("addressId");
	if (proceedThruCheckout) {
		successPage = "/checkout/step_2_verify_age.jsp";
		//successPage = "/checkout/step_2_select.jsp";
	}
%>
<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckoutController actionName="editAndSetDeliveryAddress" result="result" successPage="<%= successPage %>">

<%
	String fName = user.getFirstName();
	
	String missingContactPhone = NVL.apply(request.getParameter("missingContactPhone"), "");
	if ("true".equals(missingContactPhone)) {
		String errorMsg = "Alt Contact Phone number is required for Hamptons addresses";
		result.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
		%>
		<%@ include file="/includes/i_error_messages.jspf" %>
		<%
	}
%>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<% String[] checkErrorType2 = {"dont_deliver_to_address", "address", "order_minimum"}; %>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType2%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<FORM name="address" method="POST">
<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">
	<TR VALIGN="TOP">
	<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
		<FONT CLASS="title18">Edit Delivery Address</FONT><BR>
		Please update delivery address information.<BR>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
<%	if (proceedThruCheckout) { %>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><input type="image" src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" alt="CHECKOUT" VSPACE="2" border="0"><BR>
		<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= currencyFormatter.format(cartTotal) %></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
		<input type="image" src="/media_stat/images/buttons/checkout_arrow.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></a></TD>
<%	} else { %>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR></TD>
<%	} %>
	</TR>
	</TABLE>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<TD WIDTH="675">
<%if(user.isDepotUser()){%>
			<img src="/media_stat/images/template/depot/home_address.gif" width="96" height="9" alt="" border="0">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT><BR>
<%}else{%>
			<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="116" HEIGHT="9" border="0" alt="DELIVERY ADDRESS">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT><BR>
<%}%>
			<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
		</TD>
	</TR>
	</TABLE>
	<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
	
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
		<TR VALIGN="TOP">
<%	if (proceedThruCheckout) { %>
		<TD WIDTH="25"><a href="/view_cart.jsp?trk=chkplc" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelX"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
		<TD WIDTH="350"><FONT CLASS="space2pix"><BR></FONT><a href="/view_cart.jsp?trk=chkplc" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE"><INPUT type="image" src="/media_stat/images/buttons/continue_checkout.gif" WIDTH="117" HEIGHT="9" border="0" alt="CHECKOUT" VSPACE="0"></a><BR>Go to Step 2: Delivery Time.</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT><INPUT type="image" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></a></TD>
<%	} else { %>
		<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>"><img  src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_delivery_address_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
<%	} %>
		</TR>
	</TABLE>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>