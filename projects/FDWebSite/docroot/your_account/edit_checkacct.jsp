<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit Checking Accountd</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<%boolean proceedThruCheckout=false;%>
<table WIDTH="690" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
<td class="text11" WIDTH="690">
<font class="title18">Edit Checking Account</font><br>
Learn more about how this service works.
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here</a>
</td>
</tr>
</table>

<%@ include file="/includes/ckt_acct/checkacct_fields.jspf"%>
<br><br>

<IMG src="/media_stat/images/layout/dotted_line.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
		<TD WIDTH="675" ALIGN="RIGHT">
			<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="19" VSPACE="4" alt="CANCEL" border="0"></a>
			<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
		</TD>
</TR>
</TABLE>
<br></form>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>