<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>



<% boolean isGuest = false; %>
	
    
<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Home</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
<jsp:include page="/includes/giftcard_nav.jsp"/>

<%
    //used?
    String success_page = "/gift_card/purchase/purchase_giftcard.jsp";
    boolean hasCustomerCase = CrmSession.hasCustomerCase(session);
    String action_name = "addSavedRecipient";
    if(request.getParameter("checkout") != null && request.getParameter("checkout").equals("true")) {
        //user clicked checkout/continue button.
        action_name = "checkout";
    } else {
        if(null != request.getParameter("recipId") && !"".equals(request.getParameter("recipId"))) {
            action_name = "editSavedRecipient";
        }
        if(null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
            action_name = "deleteSavedRecipient";
        }
     }
%>
		<fd:AddSavedRecipientController actionName='<%=action_name%>' resultName='result' successPage='/gift_card/purchase/add_giftcard.jsp'>
            <%
               FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);
            if(sessionuser.getGiftCart().getDeliveryAddress()!=null) {
                 UserValidationUtil.validateRecipientListEmpty(request, result); 
                 sessionuser.getGiftCart().setDeliveryAddress(null);
             }
             %>
            <fd:ErrorHandler result='<%=result%>' name='gc_amount_minimum' id='errorMsg'>
               <%@ include file="/includes/i_error_messages.jspf" %>
            </fd:ErrorHandler>        
            <fd:ErrorHandler result='<%=result%>' name='gc_amount_maximum' id='errorMsg'>
               <%@ include file="/includes/i_error_messages.jspf" %>
            </fd:ErrorHandler>                    
            <fd:ErrorHandler result='<%=result%>' name='recipients_empty' id='errorMsg'>
               <%@ include file="/includes/i_error_messages.jspf" %>
            </fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' field='<%=checkGiftCardForm%>'>
				<% String errorMsg=SystemMessageList.MSG_MISSING_INFO; %>	
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>

			<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>


			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td class="text11 botBordLineBlack">
						<span class="title18">Enter Gift Card Info</span><br />
						Provide your recipients' personal info and details of your gift.
					</td>
					<td class="botBordLineBlack" align="right" width="50%">
                       <% if(hasCustomerCase){  %>
						<input type="image" onClick='javascript:setCheckOut();' name="form_action_name" src="/media_stat/images/giftcards/purchase/arrow_continue.gif" width="99" height="37" border="0" alt="continue" />
                        <% }else{ %>
							<b>(Note : You need to create case to purchase Gift Card)</b>&nbsp;
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px;">
						<img style="margin: 2px 0;" width="675" height="1" border="0" src="/media_stat/images/layout/clear.gif" /><br />
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px;">
						<form name="giftcard_form" id="giftcard_form" method="post">
                        <input type="hidden" name="actionName" value="">
                        <input type="hidden" id="deleteId" name="deleteId" value="">
                        <input type="hidden" id="checkout" name="checkout" value="">
						<%@ include file="/gift_card/purchase/includes/i_giftcard_fields.jspf" %>        
						<%@ include file="/gift_card/purchase/includes/recipient_list.jsp" %>
						
					</td>
				</tr>
			</table>
			<table width="100%" cellspacing="0" cellpadding="0" border="0" valign="middle" class="gc_tableBody">
				<tr>
					<td colspan="2" class="botBordLineOrange">
						<img src="/media_stat/images/layout/clear.gif" width="1" height="17" border="0" /><br />
					</td>
				</tr>
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
				<tr>
					<% if(hasCustomerCase){  %> 
						<td align="right">
							When all of your gift card information is set, click 'Continue' to <br />
							confirm it and choose a payment method.
						</td>
						<td valign="bottom" align="right" width="100">
							<input type="image" onClick='javascript:setCheckOut();' name="form_action_name" src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25"  hspace="4" vspace="4" alt="continue" border="0" />
						</td>
					<% }else{ %>
						<td align="right" colspan="2">
							<b>(Note : You need to create case to purchase Gift Card)</b>&nbsp;
						</td>
					<% } %>
				</tr>
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
			</table>
			</form>
			<script>
				function setCheckOut() {
					document.giftcard_form.checkout.value = 'true';
					document.giftcard_form.submit();
				}
			</script>
		</fd:AddSavedRecipientController>
	</tmpl:put>
</tmpl:insert>