<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%
FDSessionUser sessionuser1 = (FDSessionUser) session.getAttribute( SessionName.USER );
if(null ==sessionuser1 || null == sessionuser1.getUser() ||sessionuser1.getUser().getLevel()> FDUserI.GUEST){
%>
<fd:CreateNewGCUser result='result'> 
<%

if(result==null) result=new ActionResult();
%>
<fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'>

	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

</fd:CreateNewGCUser>
<% } %>
<style>

.text11blbold {
color:black;
font-family:Verdana,Arial,sans-serif;
font-size:10px;
font-weight:bold;
}

</style>
<script language="javascript">
function displayStatus(){	
	document.getElementById("statusMsg").style.display= "block";	
	document.getElementById("register_new_user").disabled=true;
	document.getElementById("submit_top_text").disabled=true;
	document.getElementById("submit_top_img").disabled=true;
	document.forms[0].submit();	
}
</script>
<%!
	java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<tmpl:insert template='/common/template/robinhood.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Donation Sign Up</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:DonationBuyerController actionName='registerRobinHoodBuyer' result="result" registrationType='30'>
<fd:ErrorHandler result='<%=result%>' name='Opt_in_required' id='errorMsg'>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' field='<%=checkGiftCardBuyerForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='fraud' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>



<%
FDSessionUser sessionuser = (FDSessionUser) session.getAttribute( SessionName.USER );
FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
request.setAttribute("donation", "true");
UserUtil.initializeCartForDonationOrder(user);
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>



<form name="address" method="post" >
<input type="hidden" name="firstTimeVisit" value="false">
<div id="statusMsg" style="display:none">
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
    <td rowspan="5" width="20" ><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2" bgcolor="#996699"><img src="/media_stat/images/template/system_msgs/996699_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/996699_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
</tr>
	<tr>
    <td width="18" bgcolor="#996699"><img src="/media_stat/images/template/system_msgs/exclaim_996699.gif" width="18" height="22" border="0" alt="!"></td>
    <td class="text11blbold" width="100%" >
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				We are processing your order, which may take up to 30 seconds. <br/>You will be shown a printable receipt when your order is complete. Please do not click the "Submit Order" button again.
		<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
	</td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <td bgcolor="#996699"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/996699_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td bgcolor="#ffffff"><img height="4" border="0" width="1" alt="" src="/media_stat/images/layout/clear.gif"/></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/996699_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#996699"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
</table>
<br/>
</div>
	<%@ include file="/robin_hood/includes/i_rh_donationTotal.jspf" %>
	<%@ include file="/robin_hood/includes/i_rh_signup.jspf" %>
</form>

 

</fd:DonationBuyerController>
</tmpl:put>
</tmpl:insert>

