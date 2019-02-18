<%@ page import="java.util.*"%>
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

<% //expanded page dimensions
final int W_CHECKOUT_STEP_1_EDIT_TOTAL = 970;
%>

<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage="/login/login.jsp?successPage=/checkout/step_1_choose.jsp" />
<%
	//stick the current id being edited into the session so we can check it back on delivery info
	session.setAttribute("lastEditedAddressId", (String)NVL.apply(request.getParameter("addressId"), ""));
%>
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
<tmpl:put name="seoMetaTag" direct="true">
	<fd:SEOMetaTag pageId=""></fd:SEOMetaTag>
</tmpl:put>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:CheckoutController actionName="editAndSetDeliveryAddress" result="result" successPage="<%= successPage %>">
	<script>var doubleSubmitAddrAdd = false;</script>
	
<%
	String fName = user.getFirstName();
	
	String missingContactPhone = NVL.apply(request.getParameter("missingContactPhone"), "");
	if ("true".equals(missingContactPhone)) {
		String errorMsg = "Alt Contact Phone number is required for Hamptons addresses";
		result.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
		%>
		<%@ include file="/includes/i_error_messages.jspf" %>
		<script>doubleSubmitAddrAdd = false;</script>
		<%
	}
%>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<script>doubleSubmitAddrAdd = false;</script>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<script>doubleSubmitAddrAdd = false;</script>
</fd:ErrorHandler>

<% String[] checkErrorType2 = {"dont_deliver_to_address", "address", "order_minimum"}; %>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType2%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
	<script>doubleSubmitAddrAdd = false;</script>	
</fd:ErrorHandler>


<FORM name="address" method="POST" onSubmit="doubleSubmitAddrAdd=true;">
<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">
	
	<%	if (proceedThruCheckout) { %>
<TABLE WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" cellspacing="0" cellpadding="0" border="0">	
	<TR>
	<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="395" HEIGHT="1" BORDER="0">
		</TD>
		<TD WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL-421%>" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10" style="color:#666666;font-weight:bold;">
				  <FONT CLASS="space2pix"><BR></FONT>
					<table>
						<tr>
							<td align="left">Delivery Fee:</td>
							<td align="right">
								<%	
									FDCartModel cart = user.getShoppingCart();
									String dlvCharge = JspMethods.formatPrice(cart.getChargeAmount(EnumChargeType.DELIVERY));
									if(cart.isDlvPassApplied()) {
								%>
									<%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
									
								<%	} else if (cart.isChargeWaived(EnumChargeType.DELIVERY)) {
										if((int) cart.getChargeAmount(EnumChargeType.DELIVERY) == 0){
								%>     
										Free! 
										<% }else{ %> Free!(<%= dlvCharge %> waived)<% } %>
												
								<%  } else {%>
										<%= (int) cart.getChargeAmount(EnumChargeType.DELIVERY) == 0 ? "Free!" : dlvCharge %>
								<%}%>
						</td>
						</tr>
						<%if (cart.getChargeAmount(EnumChargeType.DLVPREMIUM) > 0) {  %>
						<tr>
							<td align="left">Delivery Premium (Hamptons):</td>
							<td align="right">
								<%	
									String dlvPremium = JspMethods.formatPrice( cart.getChargeAmount(EnumChargeType.DLVPREMIUM));
									if (cart.isChargeWaived(EnumChargeType.DLVPREMIUM)) {
										if((int)cart.getChargeAmount(EnumChargeType.DLVPREMIUM) == 0){
								%>     
										Free! 
										<% }else{ %> Free!(<%= dlvPremium %> waived)<% } %>
												
								<%  } else { %>
										<%= (int)cart.getChargeAmount(EnumChargeType.DLVPREMIUM) == 0 ? "Free!" : dlvPremium %>
								<%}%>
						</td>
						</tr>
						<% } %>
						
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
<TABLE WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" VALIGN="bottom">
			<FONT CLASS="title18">DELIVERY ADDRESS</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" HEIGHT="1" BORDER="0">
		</TD>
	</TR>
</TABLE>
<%	} %>

<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="16" border="0"><br />
<!-- PROFILE HEADER -->
<%@ include file="/shared/includes/i_loyalty_bar.jspf" %>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>

<TABLE WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
		<FONT CLASS="title18">Edit Delivery Address</FONT><BR>
		Please update delivery address information.<BR>
			<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL-300%>" HEIGHT="1" BORDER="0">
		</TD>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
	</TABLE>

	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>	<BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>">
	<TR VALIGN="TOP">
		<TD WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL-200%>">
<%if(user.isDepotUser()){%>
					<img src="/media_stat/images/template/depot/home_address.gif" width="96" height="9" alt="" border="0">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
<%}else{%>
					<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="133" HEIGHT="15" border="0" alt="DELIVERY ADDRESS">&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT>
<%}%>
		</TD>
		<TD WIDTH="300" ALIGN="RIGHT">
			<a class="cssbutton green transparent small" href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">CANCEL</a>
			<button class="cssbutton green small" name="checkout_delivery_address_edit">SAVE CHANGES</button>
		</TD>
	</TR>
	</TABLE>
	
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" HEIGHT="1" BORDER="0" VSPACE="3"><BR>

	<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
	
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" HEIGHT="1" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>">
		<TR VALIGN="TOP">
<%	if (proceedThruCheckout) { %>
			<td width="35">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340" style="text-align: left;">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				View Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" alt="" width="340" height="1" border="0">
			</td>
			<td width="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL-410%>" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Delivery Time<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" name="checkout_delivery_address_select" src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0">
			</td>	
<%	} else { %>
		<TD WIDTH="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" ALIGN="RIGHT">
			<a class="cssbutton green transparent small" href="<%=response.encodeURL("/checkout/step_1_choose.jsp")%>">CANCEL</a>
			<button class="cssbutton green small" name="checkout_delivery_address_edit" >SAVE CHANGES</button>
		</TD>
<%	} %>
		</TR>
	</TABLE>

	<table border="0" cellspacing="0" cellpadding="0" width="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>">
	    <tr valign="top">
			<td width="35" style="text-align: left;">
					<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL-35%>" style="text-align: left;">
				<a href="<%=response.encodeURL("/checkout/view_cart.jsp?trk=chkplc ")%>" id="cancelText">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Your Cart<br/>
				<img src="/media_stat/images/layout/clear.gif" alt="" width="340" height="1" border="0">
			</td>
		</tr>
	</table>

<%@ include file="/checkout/includes/i_footer_text.jspf" %>

<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
<img src="/media_stat/images/layout/dotted_line_w.gif" width="<%=W_CHECKOUT_STEP_1_EDIT_TOTAL%>" height="1" border="0"><br/>
<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</FORM>
	<script>
		$jq('input.edit_delivery_address').each( function(index, elem){
			$jq(elem).on('click', function(event) {
				if (doubleSubmitAddrAdd) { event.preventDefault(); }
			});
		});
	</script>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>