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
			<%@ include file="/includes/i_error_messages.jspf" %>	
		</fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='paymentMethodList' id='errorMsg'>
			<%@ include file="/includes/i_error_messages.jspf" %>	
		</fd:ErrorHandler>
<%
	if (user.getFailedAuthorizations() > 0) { 
		String errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br />Please choose or add a new payment method.<br /></span>";
	%>
	<%@ include file="/includes/i_error_messages.jspf" %>
		
<% } %>
	<br />	
<form method="post" id="form1" name="order_form">
<table width="690" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td class="text11" width="675">
			<span class="title18">Choose Payment Information</span><br />
			Please select a payment option.
		</td>
		<td width="675" align="right" colspan="2">
			<input type="image" name="submit_bot" id="submit_bot" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="submit order" border="0">
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
		<fd:ErrorHandler result='<%=resultCheckOut%>' name='Opt_in_required' id='errorMsg'>
			<%@ include file="/includes/i_error_messages.jspf" %>	
		</fd:ErrorHandler>
		<table border="0" cellspacing="0" cellpadding="2" width="675">
		<tr>
			<td align="right">
				<b>YES, I understand that I am making a charitable contribution to Robin Hood.</b><br/>
				Leave checked in order to receive a one-time e-mail from Robin Hood acknowledging your gift.
			</td>
			<td><% if("optin".equals(optInInd)){ %>
			<input type="radio" name="optinInd" id="optin" value="optin" checked/>
			<% } else { %>
			<input type="radio" name="optinInd" id="optin" value="optin" />
			<% } %>				
			</td>
		</tr>
		<tr>
	    	<td><IMG src="/media_stat/images/layout/clear.gif" width="8" height="11" border="0">
    	</td></tr>
		<tr>
			<td align="right">
			<b>NO, I want my gift to Robin Hood to be anonymous.</b><br/>
			You will not receive an acknowledgement of your gift from Robin Hood.
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