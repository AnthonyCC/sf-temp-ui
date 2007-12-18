<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='import com.freshdirect.framework.webapp.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>
<% double cartTotal = ((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart().getTotal();%>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%= "/checkout/step_3_choose.jsp?paymentId="+ request.getParameter("paymentId")%>'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="690"><FORM method="POST">
<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">
	<TR VALIGN="TOP">
	<TD CLASS="text12" WIDTH="375" VALIGN="bottom">
		<FONT CLASS="title18">Edit Credit Card</FONT><BR>
		Please update credit card information.<BR>
		<IMG src="/media_stat/images/layout/clear.gif" WIDTH="375" HEIGHT="1" BORDER="0"></TD>
	<TD WIDTH="300" ALIGN="RIGHT" VALIGN="MIDDLE" CLASS="text10bold">
		<FONT CLASS="space2pix"><BR></FONT><BR>

	</TD>
	</TR>
	</TABLE>
<FORM name='ccEdit' method='POST'>
<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf"%>
<br><br>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
	<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
</TR>
</TABLE>

<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>