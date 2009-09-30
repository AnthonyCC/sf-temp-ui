<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/robinhood.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add Credit Card</tmpl:put>

    <tmpl:put name='content' direct='true'>
<%
String success_page = "rh_submit_order.jsp";
request.setAttribute("donation", "true");
FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);
%>

<fd:PaymentMethodController actionName='addPaymentMethod' result='result' successPage='<%=success_page%>'>
<% 

    if(sessionuser.isGCSignupError()) {
       
%>    
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
    <td class="text11rbold" width="100%" bgcolor="#FFFFFF">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				<%= SystemMessageList.MSG_GC_SIGNUP_SUCCESS %><br><br>
                <%= SystemMessageList.MSG_GC_CC_INVALID %>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
	</td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
</table>
<br>
<% 
//clear info from session.
sessionuser.setGCSignupError(false);
} %>	
 

<table WIDTH="690" cellspacing="0" cellpadding="0" border="0">
<form method="post">
<tr>
<td class="text11" WIDTH="675">
<font class="title18">Add Credit Card</font><br>
</td>
</tr>
</table>
<%@ include file="/robin_hood/includes/i_rh_creditcard_fields.jspf" %>


<br><br>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
	<TR VALIGN="TOP">
	<TD WIDTH="675" ALIGN="RIGHT"><a href="<%=success_page%>"><img src="/media_stat/images/buttons/cancel.gif" WIDTH="54" HEIGHT="16"  HSPACE="4" VSPACE="4" alt="CANCEL" border="0"></a><input type="image" name="checkout_credit_card_edit" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16" HSPACE="4" VSPACE="4" alt="SAVE ADDRESS"  border="0"></TD>
</TR>
</TABLE>
<br>
<%--<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP"><TD WIDTH="640"><%@ include file="/includes/i_footer_account.jspf"%></TD></TR>
</TABLE>--%>
</fd:PaymentMethodController>
</tmpl:put>
</tmpl:insert>