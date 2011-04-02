<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.content.attributes.EnumAttributeName' %>
<%@ page import='com.freshdirect.common.customer.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.util.ClickToCallUtil'%>
<%@ page import="com.freshdirect.dataloader.autoorder.create.util.DateUtil" %>
<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import='java.util.List' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Add Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>
<%	
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
double cartTotal = user.getShoppingCart().getTotal();
	boolean proceedThruCheckout = false;
	if ( "true".equals( request.getParameter("proceed") ) )
		proceedThruCheckout = true;
	String nextPage = "/checkout/step_3_choose.jsp?card=new";
	if (proceedThruCheckout)
		nextPage = "/checkout/step_4_submit.jsp";
%>
<fd:FDShoppingCart id='cart' result="sc_result">
<fd:CheckoutController actionName='<%= proceedThruCheckout ? "addAndSetPaymentMethod" : "addPaymentMethod" %>' result="result" successPage="<%=nextPage%>">

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<form method="post">

<%	if (proceedThruCheckout) { %>
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="395" VALIGN="bottom">
			<FONT CLASS="title18">PAYMENT INFO</FONT><BR>
		    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="395" HEIGHT="1" BORDER="0">
		</TD>
		<TD WIDTH="245" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10" style="color:#666666;font-weight:bold;">
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
												
<%	} else { %>
										<%= (int)cart.getDeliverySurcharge() == 0 ? "Free!" : dlvCharge %>
<%	} %>
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
		<TD WIDTH="26" height="26" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
				<input type="image" src="/media_stat/images/buttons/checkout_right.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></a>
		</TD>
	</TR>
</TABLE>
<%	} else { %>
<TABLE WIDTH="690" cellspacing="0" cellpadding="0" border="0">	
	<TR>
		<TD CLASS="text11" WIDTH="690" VALIGN="bottom">
				<FONT CLASS="title18">PAYMENT INFO</FONT><BR>
			    <IMG src="/media_stat/images/layout/clear.gif" WIDTH="395" HEIGHT="1" BORDER="0">
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
		<TD class="text11" WIDTH="395">
			<font class="title18">Add Credit Card</font><br>
			Please enter new credit card information.<BR>
		</TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
			<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR>
		</TD>
	</TR>
</TABLE>

<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf" %> 
<BR><BR>
<%      if(EnumServiceType.CORPORATE.equals(user.getSelectedServiceType()) && FDCustomerManager.getPaymentMethods(user.getIdentity()).size() == 0){%>
            <%@ include file="/checkout/includes/i_billing_ref.jspf" %>
            <BR><BR><BR><BR>
<%      } %>

<% if(user.getOrderHistory().getTotalOrderCount() == 0 ) {%>
					<div style="width: 675px; text-align: left;"><b>Please note that your credit card is not charged until after delivery.</b> 
					When you place your order we request  <b>credit/debit card authorization</b> for 125% of the estimated price of items priced by weight (e.g. a steak or an apple) 
					and 100% of the price of fixed priced items (e.g. a box of cereal). On the day of delivery we assemble your order and weigh each item to determine your final price. 
					<b>Your card will be charged this final price after delivery.</b>
                    </div><BR>
                <% } %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%	if (proceedThruCheckout) { %>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="340">
				<a href="<%=response.encodeURL("/checkout/checkout/step_2_select.jsp  ")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Time<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
			<td width="265" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image" src="/media_stat/images/buttons/continue_checkout.gif" width="91" height="11" border="0" alt="CONTINUE CHECKOUT" vspace="0"><br/>Submit Order<br/>
			</td>
			<td width="35" align="right" valign="middle">
				<font class="space2pix"><br/></font>
				<input type="image"  src="/media_stat/images/buttons/checkout_right.gif" width="26" height="26" border="0" alt="CONTINUE CHECKOUT" vspace="0">
		</td>
	</TR>
	</TABLE>
<%	} else { %>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="693">
		<TR VALIGN="TOP">
			<TD WIDTH="693" ALIGN="RIGHT">
				<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19" alt="CANCEL" HSPACE="4" border="0"></a>
				<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" alt="SAVE ADDRESS"  border="0">
			</TD>
		</TR>
	</TABLE>
        
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="693" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<td width="35">
					<a href="<%=response.encodeURL("/checkout/step_2_select.jsp ")%>" id="previousX">
					<img src="/media_stat/images/buttons/checkout_left.gif" width="26" height="26" border="0" alt="PREVIOUS STEP"></a>
			</td>
		    <td width="640">
				<a href="<%=response.encodeURL("/checkout/step_2_select.jsp  ")%>" id="previousX">
				<img src="/media_stat/images/buttons/previous_step.gif" WIDTH="66" HEIGHT="11" border="0" alt="PREVIOUS STEP"></a><br/>
				Delivery Time<br/>
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
			</td>
	</TR>
	</TABLE>
<%	} %>
	
        
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="16" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>

<%-- ~~~~~~~~~~~~~~~~~~~~~~ START BOTTOM MODULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>

	<%@ include file="/includes/delivery/i_bottom_modules.jspf" %>
	
<%-- ~~~~~~~~~~~~~~~~~~~~~~ END BOTTOM MODEULES DISPLAY SECTION ~~~~~~~~~~~~~~~~~~~~~~ --%>
</FORM>
</fd:CheckoutController>
</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>