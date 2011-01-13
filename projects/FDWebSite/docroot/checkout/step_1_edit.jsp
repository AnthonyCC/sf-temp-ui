<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
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


<FORM name="address" method="POST">
<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">
	
	<%	if (proceedThruCheckout) { %>
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="395" HEIGHT="1" BORDER="0">
		</TD>
		<TD WIDTH="205" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10" style="color:#666666;font-weight:bold;">
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left">Delivery Charge:</td>
							<td align="right">
								<%	
									FDCartModel cart = user.getShoppingCart();
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
						<tr>
							<td align="left"><A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated</A> Total:</td>
							<td align="right"><%= currencyFormatter.format(cart.getTotal()) %></td>
						</tr>
					</table>
		</TD>		
		<TD WIDTH="26" height="26" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
				<input type="image" src="/media_stat/images/buttons/checkout_right.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></a>
		</TD>
	</TR>
</TABLE>
<%	} else { %>
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="690" VALIGN="bottom">
			<FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="690" HEIGHT="1" BORDER="0">
		</TD>
	</TR>
</TABLE>
<%	} %>

<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0"><br/>
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<FONT CLASS="title18">Edit Delivery Address</FONT><BR>
			Please update delivery address information.<BR>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0">
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
</TABLE>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>	<BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<TD WIDTH="493">
		<%if(user.isDepotUser()){%>
					<img src="/media_stat/images/template/depot/home_address.gif" width="96" height="9" alt="" border="0">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
		<%}else{%>
					<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="133" HEIGHT="15" border="0" alt="DELIVERY ADDRESS">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
		<%}%>
		</TD>
		<TD WIDTH="200" ALIGN="RIGHT">
			<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">
			<image src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a>
			<input type="image" name="checkout_delivery_address_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
		</TD>
	</TR>
</TABLE>
	
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="1" BORDER="0" VSPACE="3"><BR>

	<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
	
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
<%	if (proceedThruCheckout) { %>
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" onclick="ntptEventTag('ev=button_event&ni_btn=cancel_checkout');var d=new Date();var cD;do{cD=new Date();}while((cD.getTime()-d.getTime())<500);" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				View Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Delivery Time<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0">
			</td>	
<%	} else { %>
		<TD WIDTH="675" ALIGN="RIGHT">
			<a href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">
			<img  src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_delivery_address_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
		</TD>
<%	} %>
		</TR>
	</TABLE>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1" border="0"><br/>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</FORM>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>