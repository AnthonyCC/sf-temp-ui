<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_ADD_CREDITCARD = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Add EBT Card"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add EBT Card</tmpl:put>

    <tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>
<%
boolean proceedThruCheckout = false;
if (!result.hasError("payment_method_fraud") && !result.hasError("technical_difficulty")) {%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; 
	if( result.hasError("auth_failure") ) {
		errorMsg=result.getError("auth_failure").getDescription();
	} %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<%} else {%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

<%} %>

<table WIDTH="<%= W_YA_ADD_CREDITCARD %>" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
<td><IMG src="/media_stat/images/snapIcon.jpg" WIDTH="29" HEIGHT="33" BORDER="0"></td>
<td>&nbsp;</td>
<td class="text11" WIDTH="<%= W_YA_ADD_CREDITCARD %>">
<font class="title18">Add EBT Card</font><br>
Please enter new EBT card information.
</td>
</tr>
</table>

<%@ include file="/includes/ckt_acct/i_ebtcard_fields.jspf" %>
<br><br>

	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<img src="/media_stat/images/layout/dotted_line_w.gif" alt="" width="<%= W_YA_ADD_CREDITCARD %>" height="1" border="0"><br/>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%= W_YA_ADD_CREDITCARD %>">
		<TR VALIGN="BOTTOM">
				<TD WIDTH="<%= W_YA_ADD_CREDITCARD %>" ALIGN="RIGHT">
					<a class="cssbutton green transparent small" href="<%=cancelPage%>">CANCEL</a>
					<button class="cssbutton green small" name="checkout_credit_card_edit">SAVE CHANGES</button>
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
