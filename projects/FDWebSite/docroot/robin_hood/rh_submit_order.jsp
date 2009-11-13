<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.payment.*' %>
<%@ page import='com.freshdirect.payment.fraud.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
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
	document.getElementById("submit_bot").disabled=true;
	document.getElementById("submit_bot1").disabled=true;
	document.forms[0].submit();
		
	
}
</script>

<tmpl:insert template='/common/template/robinhood.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Donation Order</tmpl:put>
	<tmpl:put name='content' direct='true'>

<%
String actionName = request.getParameter("actionName");
if(null == actionName || "".equals(actionName)){	
	actionName = "rh_submitDonationOrder";
}

java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
request.setAttribute("donation", "true");
UserUtil.initializeCartForDonationOrder(user);
String optInInd = request.getParameter("optinInd");
%>
<fd:CheckoutController actionName="<%= actionName %>" result="result" successPage="/robin_hood/rh_receipt.jsp" ccdProblemPage="/robin_hood/rh_submit_order.jsp?ccerror=true">
        
        <fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'>
        <%
			StringBuffer sbErrorMsg= new StringBuffer();
			sbErrorMsg.append("Checkout prevented because:");
	        sbErrorMsg.append("<br>");
			sbErrorMsg.append(errorMsg);
			errorMsg = sbErrorMsg.toString();
		%>
			<%@ include file="/includes/i_error_messages.jspf" %>	
		</fd:ErrorHandler>
		 <% String[] checkPaymentForm = {"system", "payment_inadequate", "technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum"}; %>
            <fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentForm%>' id='errorMsg'>
                <%@ include file="/includes/i_error_messages.jspf" %>	
            </fd:ErrorHandler>		
		<fd:ErrorHandler result='<%=result%>' name='Opt_in_required' id='errorMsg'>			
			<%@ include file="/includes/i_error_messages.jspf" %>	
		</fd:ErrorHandler>
<%
	if (user.getFailedAuthorizations() > 0) { 
		String errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br />Please choose or add a new payment method.<br><br>If you have tried this and are still experiencing problems, please do not attempt to submit your information again but contact us as soon as possible at" + user.getCustomerServiceContact() + ". A customer service representative will help you to complete your order.</span>";
	%>
	<%@ include file="/includes/i_error_messages.jspf" %>
		
<% } %>
	<br />	
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
<form method="post" id="form1" name="order_form">
<table width="690" cellspacing="0" cellpadding="0" border="0">



	<tr>
		<td class="text11" width="675">
			<span class="title18">Choose Payment Information</span><br />
			Please select a payment option.
		</td>
		<td width="675" align="right" colspan="2">
			<input type="image" name="submit_bot1" id="submit_bot1" onclick="displayStatus();" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0">
		</td>
		
		<!--<td width="60" valign="middle" align="right" class="text10bold">
			<input type="image" vspace="2" border="0" alt="CONTINUE TO CHECKOUT" src="/media_stat/images/buttons/continue_orange.gif" name="submit_top_text" />
		</td>
		<td width="35" valign="middle" align="RIGHT">
			<input type="image" vspace="2" border="0" alt="GO" src="/media_stat/images/buttons/checkout_arrow.gif" name="submit_top_img" />
		</td>-->
	  </tr>
	<tr>
		<td colspan="3" style="padding: 2px;">
			<img style="margin: 2px 0;" width="690" height="1" border="0" src="/media_stat/images/layout/ff9933.gif" /><br />
		</td>
	</tr>
	<tr>
		<td colspan="3" style="padding: 2px;">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
		</td>
	</tr>
	<tr>
		<td colspan="3" style="padding: 2px;">
			<img src="/media_stat/images/donation/robinhood/robin_hood_logo_sm.gif" height="23" width="130" alt="Robin Hood" /><br />
		</td>
	</tr>
	<tr>
		<td colspan="3" style="padding: 8px 2px;">
			<%@ include file="/robin_hood/includes/i_rh_order_info.jspf" %>
		</td>
	</tr>
</table>

<% ActionResult resultCheckOut = result;
result = null;
%>
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
<!-- error message handling here -->
<table width="675" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td width="675" class="text11">
			<% StringBuffer sbErrorMsg= new StringBuffer(); %>
       </td>
    </tr>
</table>

<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />

<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />



	<table width="675" border="0" cellspacing="0" cellpadding="2">
		<tr valign="top">
			<td width="693"><img src="/media_stat/images/navigation/choose_credit_card.gif" width="135" height="9" border="0" alt="CHOOSE CREDIT CARD">&nbsp;&nbsp;&nbsp;<br />
					<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" vspace="3"><br />
				<font class="space2pix"><br /></font>  
			</td>   
		</tr>

		<tr valign="middle">
			<td class="text11">If you need to enter another credit card: <a href="/robin_hood/rh_add_creditcard.jsp">
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
		<% String optinout=null;%>
		<fd:ErrorHandler result='<%=resultCheckOut%>' name='Opt_in_required' id='errorMsg'>
		<% errorMsg = SystemMessageList.MSG_RH_OPTIN_BELOW_REQUIRED;
		   optinout = " ";
		%>
		<tr><td align="right"><font class="text11rbold" align="right"><%= errorMsg %></font></td></tr>
		</fd:ErrorHandler>
		<tr>
			<td align="right">
				<b>Yes, I authorize FreshDirect to share my personal info with Robin Hood.</b><br />
						Leave checked in order to receive a letter from Robin Hood confirming your tax-deductible gift.
			</td>
			<td><% if("optin".equals(optInInd)){ %>
			<input type="radio" name="optinInd" id="optin" value="optin" checked/>
			<% } else { %>
			<input type="radio" name="optinInd" id="optin" value="optin" />
			<% } %>				
			</td>
			<%System.out.println(optinout); 
			if(null!=optinout){ %>
			 <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
			  <% } %>
		</tr>
		<tr>
	    	<td><IMG src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0">
    	</td></tr>
		<tr>
			<td align="right">
			<b>No, please do not share my personal info with Robin Hood.</b><br />
						You will not receive a tax deduction letter.
			</td>
			<td><% if("optout".equals(optInInd)){ %>
			<input type="radio" name="optinInd" id="optout" value="optout" checked/>
			<% } else { %>
			<input type="radio" name="optinInd" id="optout" value="optout" />
			<% } %>			
			</td>
		</tr>
		<tr>
	    	<td><IMG src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0">
    	</td>
		<tr valign="top">
			<td width="675" align="right" colspan="2">
			<input type="image" name="submit_bot" id="submit_bot" onclick="displayStatus();" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0">
			</td>
		</tr>
	</table>
	<br />
</form>
</fd:PaymentMethodController>
</fd:CheckoutController>
</tmpl:put>
</tmpl:insert>