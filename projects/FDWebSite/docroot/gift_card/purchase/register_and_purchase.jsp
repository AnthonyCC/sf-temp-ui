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


<%!
java.text.SimpleDateFormat cutoffDateFormat = new java.text.SimpleDateFormat("h:mm a 'on' EEEE, MM/d/yy");
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<tmpl:insert template='/common/template/giftcard.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Sign Up</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:GiftCardBuyerController actionName='registerGiftCardBuyer' result="result" registrationType='10' >
<fd:ErrorHandler result='<%=result%>' field='<%=checkGiftCardBuyerForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
<fd:ErrorHandler result='<%=result%>' name='fraud' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
<%
        String action_name = "";
        if(null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
            action_name = "deleteSavedRecipient";
        } 
%>


<table width="690" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td class="text11" width="690">
			<span class="title18"><img src="/media_stat/images/giftcards/purchase/purchase_gift_cards.gif" /></span><br />
			<b>(Already a FreshDirect customer? <a href="/login/login.jsp?successPage=/gift_card/purchase/purchase_giftcard.jsp">Click here log in.)</b>
		</td>
		<td width="99">
			<input type="image" id="submit_top" name="submit_top" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0" onclick="$('address').submit();">
		</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 4px;">
            <img style="margin: 2px 0;" src="/media_stat/images/layout/ff9900.gif" width="690" height="2" border="0" /><br /><br/>
		</td>
	</tr>
</table>


<fd:AddSavedRecipientController actionName='<%=action_name%>' resultName='result1'>
</fd:AddSavedRecipientController>
<%
FDSessionUser sessionuser = (FDSessionUser) session.getAttribute( SessionName.USER );
request.setAttribute("giftcard", "true");
UserUtil.initializeGiftCart(sessionuser);
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>




    <%@ include file="/gift_card/purchase/includes/recipient_list.jspf" %>
    <table width="690" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td colspan="2" style="padding: 2px;">
			<img style="margin: 2px 0;" width="690" height="1" border="0" src="/media_stat/images/layout/999966.gif" /><br /><br/>
		</td>
	</tr>
    </table>
<form name="address" id="address" method="post" >
    <input type="hidden" name="firstTimeVisit" value="false">
	<%@ include file="/gift_card/purchase/includes/i_gc_signup.jspf" %>
</form>

</fd:GiftCardBuyerController>
</tmpl:put>
</tmpl:insert>


