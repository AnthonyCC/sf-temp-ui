<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add Credit Card</tmpl:put>

    <tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>
<%
boolean proceedThruCheckout = false;
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; 
      if( result.hasError("auth_failure") ) {
		errorMsg=result.getError("auth_failure").getDescription();
      } else if( result.hasError("payment_method_fraud") ) {
		errorMsg=result.getError("payment_method_fraud").getDescription();
      }  else  if( result.hasError("technical_difficulty") ) {
		errorMsg=result.getError("technical_difficulty").getDescription();
      }

%>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<table WIDTH="690" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
<td class="text11" WIDTH="675">
<font class="title18">Add Credit Card</font><br>
</td>
</tr>
</table>

<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf" %>
<br><br>

	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line.gif" width="693" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="693">
		<TR VALIGN="BOTTOM">
				<TD WIDTH="693" ALIGN="RIGHT">
					<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="19" VSPACE="4" alt="CANCEL" border="0"></a>
					<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
				</TD>
</TR>
</TABLE>
<br>
<%--<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP"><TD WIDTH="640"><%@ include file="/includes/i_footer_account.jspf"%></TD></TR>
</TABLE>--%>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>