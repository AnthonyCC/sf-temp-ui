<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%request.setAttribute("listPos", "CategoryNote");%>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<%-- bring in the common javascript functions  --%>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>


<tmpl:insert template='/common/template/giftcard.jsp'>
   <tmpl:put name='title' direct='true'>FreshDirect - Purchase Gift Card</tmpl:put>
    <tmpl:put name='content' direct='true'>


<% 
    FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);
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
                <%= sessionuser.getGcFraudReason() %>
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
sessionuser.setGcFraudReason("");
} %>

<%
        String action_name = "";
        if(null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
            action_name = "deleteSavedRecipient";
        } 
%>

<fd:AddSavedRecipientController actionName='<%=action_name%>' resultName='result'>
</fd:AddSavedRecipientController>

<%
String actionName =  request.getParameter("actionName");
if(null == actionName || "".equals(actionName)){
	actionName = "gc_submitGiftCardOrder";
}
java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
request.setAttribute("giftcard", "true");
UserUtil.initializeGiftCart(user);
%>
<fd:CheckoutController actionName="<%= actionName %>" result="result" successPage="/gift_card/purchase/receipt.jsp" ccdProblemPage="/gift_card/purchase/purchase_giftcard.jsp?ccerror=true">
        <fd:ErrorHandler result='<%=result%>' name='gc_order_amount_fraud' id='errorMsg'>
            <%@ include file="/includes/i_error_messages.jspf" %>	
        </fd:ErrorHandler>
        <fd:ErrorHandler result='<%=result%>' name='gc_order_count_fraud' id='errorMsg'>
            <%@ include file="/includes/i_error_messages.jspf" %>	
        </fd:ErrorHandler>    
        <fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'>
                <% 
                StringBuffer sbErrorMsg= new StringBuffer(); 
                sbErrorMsg.append("<br>Checkout prevented because:<br>");
                sbErrorMsg.append(errorMsg);
                sbErrorMsg.append("<br>");
                errorMsg = sbErrorMsg.toString();
                %>
            <%@ include file="/includes/i_error_messages.jspf"%>                 
        </fd:ErrorHandler>    
        <fd:ErrorHandler result='<%=result%>' name='limitReached' id='errorMsg'>
           <%@ include file="/includes/i_error_messages.jspf" %>
        </fd:ErrorHandler>       
        <% String[] checkPaymentForm = {"system", "order_minimum", "payment_inadequate", "technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum"}; %>
            <fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentForm%>' id='errorMsg'>
                <%@ include file="/includes/i_error_messages.jspf" %>	
            </fd:ErrorHandler>      
            <fd:ErrorHandler result='<%=result%>' name='payment_method_fraud' id='errorMsg'>
            <%@ include file="/includes/i_error_messages.jspf" %>	
        </fd:ErrorHandler>        
		<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>        

<%
	if (user.getFailedAuthorizations() > 0) { 
		String errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br>Please choose or add a new payment method.<br><br>If you have tried this and are still experiencing problems, please do not attempt to submit your information again but contact us as soon as possible at" + user.getCustomerServiceContact() + ". A customer service representative will help you to complete your order.</span>";
	%>
		<%@ include file="/includes/i_error_messages.jspf" %>
<% } %>
	<BR>	
<form method="post" id="form1" name="order_form">
<table width="690" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td class="text11" width="675">
			<span class="title18"><img src="/media_stat/images/giftcards/purchase/purchase_gift_cards.gif" /></span><br />
			Please enter your credit card information below.
		</td>
		<td width="99">
			<input type="image" id="submit_top" name="submit_top" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0">
		</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 2px;">
			<img style="margin: 2px 0;" width="675" height="1" border="0" src="/media_stat/images/layout/999966.gif" /><br />
		</td>
	</tr>
</table>
<br /><br />

    <%@ include file="/gift_card/purchase/includes/recipient_list.jspf" %>
    
<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>
<%

boolean hasCheck = false;
Collection paymentMethods = null;
FDIdentity identity = null;

if(user!=null  && user.getIdentity()!=null) {
    identity = user.getIdentity();
    paymentMethods = FDCustomerManager.getPaymentMethods(identity);	
}

//these booleans are used in /includes/ckt_acct/i_credit_cardfields.jspf
// set both to false -- gift card purchase is allowed only with a credit card
boolean isECheckRestricted = false;
boolean isCheckEligible = false;
%>
  <% StringBuffer sbErrorMsg= new StringBuffer(); %>

<table width="675" border="0" cellpadding="0" cellspacing="0" class="text11">
    <%
    	String serviceType = request.getParameter("serviceType");
    	String value1 = "personal"; 
    	String value2 = "professional"; 
        	String value1Selected = "checked";
        	String value2Selected = "";
    	if (value1.equals(serviceType)) {
    		value1Selected = "checked";
    	} else if(value2.equals(serviceType)) {
    		value2Selected = "checked";
    	}
    %>
    	<tr>
    	<td><IMG src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0">
    	</td>
    	<td><IMG src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0">
    	</td>
    	</tr>
    	<tr>
    	<td class="text12">
    		<span <fd:ErrorHandler result="<%=result%>" name="serviceType">  class="text11rbold"</fd:ErrorHandler>>
    			&nbsp;Purchase&nbsp;Type
    		</span>
        	</td>
    	<td>
    		<input type="radio" class="text11" name="serviceType" id="Personal" value="<%=value1%>" <%= value1Selected %> />Personal
    		<input type="radio" class="text11" name="serviceType" id="Professional" value="<%=value2%>" <%= value2Selected %> />Corporate
        	</td>
    	</tr>
</table>

<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />

<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />

<script language="javascript">
	<!--
	OAS_AD('CategoryNote');
	//-->
</script>


	<table width="675" border="0" cellspacing="0" cellpadding="2">
		<tr valign="top">
			<td width="693"><img src="/media_stat/images/navigation/choose_credit_card.gif" width="135" height="9" border="0" alt="CHOOSE CREDIT CARD">&nbsp;&nbsp;&nbsp;<br />
					<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" vspace="3"><br />
				<font class="space2pix"><br /></font>  
			</td>   
		</tr>

		<tr valign="middle">
			<td class="text11">If you need to enter another credit card: <a href="/gift_card/purchase/includes/gc_add_creditcard.jsp">
				<IMG src="/media_stat/images/buttons/add_new_card.gif" WIDTH="96" HEIGHT="16" ALT="Add New Credit Card" BORDER="0" ALIGN="absmiddle"></a>
			</td>
		</tr>
		<tr>
			<td>
				<%-- <form id="form2" name="creditcard_form" method="post">  --%>
				<input type="hidden" name="actionName" value="">
				<input type="hidden" name="deletePaymentId" value="">
				<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %>
				<%-- </form> --%>
			</td>
		</tr>
	</table>
		<br />
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br />
		<font class="space4pix"><br /><br /></font>
		<table border="0" cellspacing="0" cellpadding="2" width="675">
		<tr valign="top">
			<td width="675" align="right">
			<input type="image" name="submit_bot" id="submit_bot" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0">
			</td>
		</tr>
	</table>
	<br />
</form>
</fd:PaymentMethodController>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>