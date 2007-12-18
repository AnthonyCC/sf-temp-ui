<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Add Checking Account</tmpl:put>
<tmpl:put name='content' direct='true'>
<%	
double cartTotal = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getTotal();
	boolean proceedThruCheckout = false;
	if ( "true".equals( request.getParameter("proceed") ) )
		proceedThruCheckout = true;
	String nextPage = "/checkout/step_3_choose.jsp?card=new";
	if (proceedThruCheckout)
		nextPage = "/checkout/step_4_submit.jsp";
%>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='<%= "/checkout/step_3_choose.jsp?paymentId="+ request.getParameter("paymentId")%>'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<table WIDTH="690" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
	<td class="text12" WIDTH="395">
		<font class="title18">Add Checking Account</font><br>
		<a href="javascript:popup('/pay_by_check.jsp?from=checkout','large')">Learn more</a> about how this service works.<BR></td>
<%	if (proceedThruCheckout) { %>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><input type="image" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0"><BR>
		<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated Total</A>: <%= currencyFormatter.format(cartTotal) %></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
		<input type="image" src="/media_stat/images/buttons/checkout_arrow.gif" BORDER="0" alt="CONTINUE CHECKOUT" VSPACE="2"></a></TD>
<%	} else { %>
	<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold"><FONT CLASS="space2pix"><BR></FONT>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="265" HEIGHT="1" BORDER="0"><BR></TD>
	<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"><BR></TD>
<%	} %>
</tr>
</table>

<%@ include file="/includes/ckt_acct/checkacct_fields.jspf" %> 
<BR><BR>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR> 
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
<%	if (proceedThruCheckout) { %>
		<TD WIDTH="25"><a href="/view_cart.jsp?trk=chkplc"><img src="/media_stat/images/buttons/x_green.gif" WIDTH="20" HEIGHT="19" border="0" alt="CONTINUE SHOPPING"></a></TD>
		<TD WIDTH="350"><FONT CLASS="space2pix"><BR></FONT><a href="/view_cart.jsp?trk=chkplc"><img src="/media_stat/images/buttons/cancel_checkout.gif" WIDTH="92" HEIGHT="7" border="0" alt="CANCEL CHECKOUT"></a><BR>and return to your cart.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
		<TD WIDTH="265" ALIGN="RIGHT" VALIGN="MIDDLE"><INPUT type="image" src="/media_stat/images/buttons/continue_checkout.gif" alt="CONTINUE CHECKOUT" VSPACE="2" border="0"></a><BR>Go to Step 4: Submit.</TD>
		<TD WIDTH="35" ALIGN="RIGHT" VALIGN="MIDDLE"><FONT CLASS="space2pix"><BR></FONT><INPUT type="image" src="/media_stat/images/buttons/checkout_arrow.gif" WIDTH="29" HEIGHT="29" border="0" alt="CONTINUE CHECKOUT" VSPACE="0"></a></TD>
<%	} else { %>
		<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
<%	} %>
	</TR>
	</TABLE>
        
	<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>