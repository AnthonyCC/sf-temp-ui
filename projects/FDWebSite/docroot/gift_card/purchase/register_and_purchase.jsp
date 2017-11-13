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

<% //expanded page dimensions
final int W_REGISTER_AND_PURCHASE_TOTAL = 970;
%>

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
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Sign Up"/>
  </tmpl:put>
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


<table width="<%=W_REGISTER_AND_PURCHASE_TOTAL%>" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td class="text11" width="<%=W_REGISTER_AND_PURCHASE_TOTAL-135%>">
			<span class="title18">purchase gift cards</span><br />
			<b>(Already a FreshDirect customer? <a href="/login/login.jsp?successPage=/gift_card/purchase/purchase_giftcard.jsp">Click here log in</a>.)</b>
		</td>
		<td width="135">
			<button class="cssbutton small orange" id="submit_top" name="submit_top" onclick="$('address').submit();">SUBMIT ORDER</button>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 4px 0px;">
            <img style="margin: 2px 0;" src="/media_stat/images/layout/ff9900.gif" alt="" width="<%=W_REGISTER_AND_PURCHASE_TOTAL%>" height="2" border="0" /><br /><br/>
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
    <table width="<%=W_REGISTER_AND_PURCHASE_TOTAL%>" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td colspan="2" style="padding: 2px 0px;">
			<img style="margin: 2px 0;" width="<%=W_REGISTER_AND_PURCHASE_TOTAL%>" height="1" border="0" alt="" src="/media_stat/images/layout/999966.gif" /><br /><br/>
		</td>
	</tr>
    </table>
<form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter name="address" id="address" method="post" >
    <input type="hidden" name="firstTimeVisit" value="false">
	<%@ include file="/gift_card/purchase/includes/i_gc_signup.jspf" %>
</form>

</fd:GiftCardBuyerController>
</tmpl:put>
</tmpl:insert>


