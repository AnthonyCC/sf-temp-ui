<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%request.setAttribute("listPos", "CategoryNote");%>

<%-- bring in the common javascript functions  --%>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>

<%
	String actionName =  request.getParameter("actionName"); 
	FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );
	String pageName=request.getParameter("pageName");
	if(pageName==null) pageName="";
%>

<% boolean isGuest = false; %>

<crm:GetCurrentAgent id="currentAgent">
	<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %> 
</crm:GetCurrentAgent>
    
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Home</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<%
			String success_page="/gift_card/purchase/purchase_giftcard.jsp";
			if("bulkPurchase".equalsIgnoreCase(pageName))
				success_page = "/gift_card/purchase/purchase_bulk_giftcard.jsp";
				
			request.setAttribute("giftcard", "true");
		%>

	<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='<%=success_page%>'>

	<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentMethodForm%>'>
		<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; %>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
	<form method="post">
		<tr>
			<td class="text11 botBordLineBlack"">
				<font class="title18">Add Credit Card</font><br />
			</td>
		</tr>
		<tr>
			<td>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
				<%@ include file="/gift_card/purchase/includes/i_gc_creditcard_fields.jspf" %>
			</td>
		</tr>
		<tr>
			<td class="botBordLineOrange">
				<img src="/media_stat/images/layout/clear.gif" width="1" height="17" border="0" /><br />
			</td>
		</tr>
		<tr>
			<td align="center">
				<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
				<a href="<%=success_page%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16"  hspace="4" vspace="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16" hspace="4" vspace="4" alt="SAVE ADDRESS"  border="0"><br />
				<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
			</td>
		</tr>
	</table>

<%--<table border="0" cellspacing="0" cellpadding="0" width="675">
<tr valign="top"><td width="640"><%@ include file="/includes/i_footer_account.jspf"%></td></tr>
</table>--%>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>