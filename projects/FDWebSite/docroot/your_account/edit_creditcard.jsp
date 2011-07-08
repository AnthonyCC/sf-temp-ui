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



<% 
String template = "/common/template/no_nav.jsp";

String gcPage = (request.getParameter("gc")!=null)?request.getParameter("gc").toLowerCase():null;
	if (gcPage != null && ("true".equals(gcPage) && FDStoreProperties.isGiftCardEnabled())) {
			template = "/common/template/giftcard.jsp";
	}
String rhPage = (request.getParameter("rh")!=null)?request.getParameter("rh").toLowerCase():null;
	if (rhPage != null && ("true".equals(rhPage) && FDStoreProperties.isRobinHoodEnabled())) {
			template = "/common/template/robinhood.jsp";
	}
%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='<%=template%>'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit Credit Card</tmpl:put>
<tmpl:put name='content' direct='true'>

<%
String successRedirect = "/your_account/payment_information.jsp";
if("true".equals(request.getParameter("gc"))) {
	successRedirect = "/gift_card/purchase/purchase_giftcard.jsp";
}else if("true".equals(request.getParameter("rh"))) { //Robin Hood..
	successRedirect = "/robin_hood/rh_submit_order.jsp";
}
boolean proceedThruCheckout=false;
%>

<fd:PaymentMethodController actionName='editPaymentMethod' result='result' successPage='<%=successRedirect%>'>

<% 
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
		<TD WIDTH="675" ALIGN="RIGHT">
			<a href="<%=cancelPage%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="72" HEIGHT="19"  HSPACE="19" VSPACE="4" alt="CANCEL" border="0"></a>
			<input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="91" HEIGHT="18" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0">
		</TD>
</TR>
</TABLE>
<br>
</FORM>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>