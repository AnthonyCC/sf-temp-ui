<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>


<%request.setAttribute("listPos", "CategoryNote");%>

<% boolean isGuest = false; %>
	<crm:GetCurrentAgent id="currentAgent">
		<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %> 
	</crm:GetCurrentAgent>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>    
<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Gift Card : Purchase</tmpl:put>
    <tmpl:put name='content' direct='true'>

		<jsp:include page="/includes/giftcard_nav.jsp"/>

			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td class="text11" width="675">
						<span class="title18"><img src="/media_stat/images/giftcards/purchase/purchase_gift_cards.gif" /></span><br />
						Please enter your credit card information below.
					</td>
					<td width="99">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px;" class="botBordLineBlack">
						<img style="margin: 2px 0;" width="675" height="1" border="0" src="/media_stat/images/layout/clear.gif" /><br />
					</td>
				</tr>
			</table>
			
		<%
			String action_name = "";
			if(null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
				action_name = "deleteSavedRecipient";
			} 
		%>

		<% //needed? %>
		<fd:AddSavedRecipientController actionName='<%=action_name%>' resultName='result'>
		</fd:AddSavedRecipientController>

		<%
			String actionName =  request.getParameter("actionName"); 
			if(null == actionName || "".equals(actionName)){
				actionName = "gc_submitGiftCardOrder";
			}
			FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
			request.setAttribute("giftcard", "true");
			UserUtil.initializeGiftCart(user);
		%>

		<%@ include file="/gift_card/purchase/includes/recipient_list.jsp" %>
    
		<fd:CheckoutController actionName="<%= actionName %>" result="result" successPage="/gift_card/purchase/receipt.jsp" ccdProblemPage="/gift_card/purchase/includes/gc_add_creditcard.jsp?ccerror=true">
					<!-- error message handling here -->
					<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
						<tr>
							<td width="675" class="text11">
							<fd:ErrorHandler result='<%=result%>' name='gc_order_amount_fraud' id='errorMsg'>
					            <%@ include file="/includes/i_error_messages.jspf" %>	
					        </fd:ErrorHandler>        
					        <fd:ErrorHandler result='<%=result%>' name='gc_order_count_fraud' id='errorMsg'>
					            <%@ include file="/includes/i_error_messages.jspf" %>	
					        </fd:ErrorHandler>    
					        <fd:ErrorHandler result='<%=result%>' name='address_verification_failed' id='errorMsg'>
					            <%@ include file="/includes/i_error_messages.jspf" %>	
					        </fd:ErrorHandler>
					         <fd:ErrorHandler result='<%=result%>' name='authorization_failed' id='errorMsg'>
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
							<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
						</tr>
						<tr>
							<td>
								<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />
								<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />
							</td>
						</tr>
					</table>
                    
                    <%
	if (user.getFailedAuthorizations() > 0) { 
		String errorMsg = "<span class=\"text12\">There was a problem with the credit card you selected.<br>Please choose or add a new payment method.<br><br>If you have tried this and are still experiencing problems, please do not attempt to submit your information again but contact us as soon as possible at" + user.getCustomerServiceContact() + ". A customer service representative will help you to complete your order.</span>";
	%>
		<%@ include file="/includes/i_error_messages.jspf" %>
<% 
    }
    if(user.getOneTimeGCPaymentError()!=null && user.getOneTimeGCPaymentError().size()>0){
        String errorMsg="";
           List alist=user.getOneTimeGCPaymentError();
           for(int k=0;k<alist.size();k++){
             errorMsg=errorMsg+(String)alist.get(k)+"\n";
           }  
           System.out.println("errorMsg:"+errorMsg);
     %>        
       <%@ include file="/includes/i_error_messages.jspf" %>
<%       
     }
%>



<%
if(user.isAddressVerificationError()) {
       
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
                <%= user.getAddressVerficationMsg() %>
                
                
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
user.setAddressVerificationError(false);
}
%>



			<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>

				<form name="submitForm" id="submitForm" method="post" style="padding:0px;margin:0px;">
					<%
						boolean hasCheck = false;
						Collection paymentMethods = null;
						FDIdentity identity = null;

						if(user!=null && user.getIdentity()!=null) {
							identity = user.getIdentity();
							paymentMethods = FDCustomerManager.getPaymentMethods(identity);	
						}

						//System.out.println("The identity = "+identity);
						//these booleans are used in /includes/ckt_acct/i_credit_cardfields.jspf
						// set both to false -- gift card purchase is allowed only with a credit card
						boolean isECheckRestricted = false;
						boolean isCheckEligible = false;
					%>
					<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
						<tr>
							<td>
								<%
									String serviceType = "personal"; 
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
								Purchase Type
								<input type="radio" class="text11" name="serviceType" id="Personal" value="<%=value1%>" <%= value1Selected %> />Personal
								<input type="radio" class="text11" name="serviceType" id="Professional" value="<%=value2%>" <%= value2Selected %> />Corporate
							</td>
						</tr>
					</table>


					<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
						<tr valign="top">
							<td width="693"><img src="/media_stat/images/navigation/choose_credit_card.gif" width="135" height="9" border="0" alt="CHOOSE CREDIT CARD">&nbsp;&nbsp;&nbsp;<br />
									<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" vspace="3"><br />
								<font class="space2pix"><br /></font>  
							</td>   
						</tr>

						<tr valign="middle">
							<td class="text11">If you need to enter another credit card: <a href="/gift_card/purchase/includes/gc_add_creditcard.jsp?pageName=singlePurchase">
								<img src="/media_stat/images/buttons/add_new_card.gif" width="96" height="16" alt="Add New Credit Card" border="0" /></a>
							</td>
						</tr>
						<tr>
							<td>
								<form name="creditcard_form" method="post">
									<input type="hidden" name="actionName" value="">
									<input type="hidden" name="deletePaymentId" value="">
									<%@ include file="/checkout/i_creditcard_select.jspf" %>
								</form>
							</td>
						</tr>
						<tr>
							<td class="botBordLineOrange"><img src="/media_stat/images/layout/clear.gif" width="675" height="8" border="0" />
							</td>
						</tr>
						<tr valign="top">
							<td width="100%" align="right">
								<input type="image" name="form_action_name" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="continue" border="0" onclick="$('submitForm').submit();" />
							</td>
						</tr>
					</table>
				</form>
			</fd:PaymentMethodController>
		</fd:CheckoutController>
	</tmpl:put>
</tmpl:insert>