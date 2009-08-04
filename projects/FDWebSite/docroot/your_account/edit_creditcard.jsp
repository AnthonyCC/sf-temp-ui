<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(); %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='/your_account/payment_information.jsp'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<table WIDTH="690" cellspacing="0" cellpadding="0" border="0">
<FORM name='ccEdit' method='POST'>

<tr>
<td class="text11" WIDTH="690">
<font class="title18">Edit Credit Card</font><br>
</td>
</tr>
</table>

<%@ include file="/includes/ckt_acct/i_creditcard_fields.jspf"%>
<br><br>

<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
	<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
</TR>
</TABLE>
<br>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>