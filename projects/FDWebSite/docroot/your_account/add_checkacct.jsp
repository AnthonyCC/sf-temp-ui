<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_ADD_CHECKACCT = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Add Checking Account"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add Checking Account</tmpl:put>

    <tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<%
boolean proceedThruCheckout = false;
%>
<form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter method="post">
<table WIDTH="<%= W_YA_ADD_CHECKACCT %>" cellspacing="0" cellpadding="0" border="0">

<tr>
<td class="text11" WIDTH="<%= W_YA_ADD_CHECKACCT %>">
<font class="title18">Add Checking Account</font><br><span class="space2pix"><br></span>
<a href="javascript:popup('/pay_by_check.jsp','large')">Learn more<span class="offscreen">about how this service works</span></a> about how this service works.<br>
To learn more about our <b>Security Policies</b>, <a href="javascript:popup('/help/faq_index.jsp?show=security','large')">click here<span class="offscreen">To learn more about our Security Policies</span></a>
</td>
</tr>
</table>
<%@ include file="/includes/ckt_acct/checkacct_fields.jspf" %>
<br><br>
	<IMG src="/media_stat/images/layout/dotted_line_w.gif" alt="" WIDTH="<%= W_YA_ADD_CHECKACCT %>" HEIGHT="1" BORDER="0" VSPACE="6"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="<%= W_YA_ADD_CHECKACCT %>">
	<TR VALIGN="TOP">
				<TD WIDTH="<%= W_YA_ADD_CHECKACCT %>" ALIGN="RIGHT">
					<a class="cssbutton green transparent small" href="<%=cancelPage%>">CANCEL</a>
					<button class="cssbutton green small" name="checkout_credit_card_edit">SAVE CHANGES</button>
				</TD>
</TR>
</TABLE>
</form>
<br>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>
