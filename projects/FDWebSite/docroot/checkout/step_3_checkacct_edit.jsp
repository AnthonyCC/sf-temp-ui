<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />

<tmpl:insert template='/common/template/checkout_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Checkout - Edit Checking Account</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%= "/checkout/step_3_choose.jsp?paymentId="+ request.getParameter("paymentId")%>'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<table width="690" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
<td class="text11" width="690">
<font class="title18">Edit Checking Account</font><br>
Learn more about how this service works.
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a>
</td>
</tr>
</table>

<%@ include file="/includes/ckt_acct/checkacct_fields.jspf"%>
<br><br>

<IMG src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
	<TABLE border="0" cellspacing="0" cellpadding="2" width="675">
	<TR VALIGN="TOP">
	<TD width="675" ALIGN="RIGHT"><a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
</TR>
</TABLE>
<br>
<%@ include file="/checkout/includes/i_footer_text.jspf" %>
</form>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>