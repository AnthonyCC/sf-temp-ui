<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_GC_ADD_CREDITCARD_TOTAL = 970;
%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/giftcard.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Add Credit Card"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add Credit Card</tmpl:put> --%>
	<tmpl:put name='pageType' direct='true'>gc_cc_add</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<style>
			.W_GC_ADD_CREDITCARD_TOTAL { width: <%= W_GC_ADD_CREDITCARD_TOTAL %>px; }
		</style>
<%
String success_page = "/gift_card/purchase/purchase_giftcard.jsp";
request.setAttribute("giftcard", "true");
FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);

%>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='<%=success_page%>'>
<% 

    if(sessionuser.isGCSignupError()) {
       
%>    
<table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
    <td class="errortext" width="100%" bgcolor="#FFFFFF">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				<%= SystemMessageList.MSG_GC_SIGNUP_SUCCESS %><br><br>
                <%= SystemMessageList.MSG_GC_CC_INVALID %>
                
                
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
	</td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
</tr>
</table>
<br>
<% 
//clear info from session.
sessionuser.setGCSignupError(false);
}



if(sessionuser.isAddressVerificationError()) {
       
%>    
<table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
    <td class="errortext" width="100%" bgcolor="#FFFFFF">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				<%= SystemMessageList.MSG_GC_SIGNUP_SUCCESS %><br><br>
                <%= sessionuser.getAddressVerficationMsg() %>
                
                
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
	</td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
</tr>
</table>
<br>
<% 
//clear info from session.
sessionuser.setAddressVerificationError(false);
}

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




	<form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter method="post" style="padding: 0px; margin: 0px;">
	<table role="presentation" class="W_GC_ADD_CREDITCARD_TOTAL" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td class="title18" class="W_GC_ADD_CREDITCARD_TOTAL">
				ADD CREDIT CARD<br />
			</td>
		</tr>
	</table>

<%@ include file="/gift_card/purchase/includes/i_gc_creditcard_fields.jspf" %>

<br><br>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" class="W_GC_ADD_CREDITCARD_TOTAL" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<TABLE role="presentation" BORDER="0" CELLSPACING="0" CELLPADDING="2" class="W_GC_ADD_CREDITCARD_TOTAL">
	<TR VALIGN="TOP">
	<TD class="W_GC_ADD_CREDITCARD_TOTAL" ALIGN="RIGHT">
	<a class="cssbutton green transparent small" href="<%=success_page%>">CANCEL</a>
	<button class="cssbutton green small">SAVE CHANGES</button>
	</TD>
</TR>
</TABLE>
</form>
<br>

</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>