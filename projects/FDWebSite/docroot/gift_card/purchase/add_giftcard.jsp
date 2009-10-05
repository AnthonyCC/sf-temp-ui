<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/giftcard.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Add Gift Card</tmpl:put>
	<tmpl:put name='content' direct='true'>
    <div class="gcCheckAddressBox">
		<div style="text-align: left;" class="gcCheckAddressBoxContent" id="gcCheckAddressBox">
			<img src="/media_stat/images/giftcards/your_account/check_address_hdr.gif" width="132" height="16" alt="Check An Address" style="float: left;" />
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a><br style="clear: both;" />
			<div id="gcCheckAddressBoxErr">
				<span id="gcCheckAddressBoxMsg">Sorry, we don't currently deliver to this address.<br />
			If this is a New York address, may we suggest our Pickup Window in Long Island City?</span>
			</div><br />
			<img src="/media_stat/images/layout/cccccc.gif" width="390" height="1" border="0"><br /><br />
			<table border="0" cellspacing="0" cellpadding="4" width="100%">
				<tr>
					<th width="130" align="right">Street Address 1:</th>
					<td><input name="address1" id="address1" value="" /></td>
				</tr>
				<tr valign="middle">
					<th width="130" align="right">Street Address 2:</th>
					<td><input name = "address2" id="address2" value="" /></td>
				</tr>
				<tr>
					<th width="130" align="right">City/Town:</th>
					<td><input name="city" id="city" value="" /></td>
				</tr>
				<tr>
					<th width="130" align="right">State:</th>
					<td><input type="radio" name="state" id="stateNY" value="NY" checked /> New York <input type="radio" name="state" id="stateNJ" value="NJ"/> New Jersey</td>
				</tr>
				<tr>
					<th width="130" align="right">ZIP Code:</th>
					<td><input name="zipcode" id="zipcode" value="" /></td>
				</tr>
				<tr>
					<td width="150" align="right"><a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/clear_btn.gif" width="60" height="25" alt="CANCEL" border="0" /></a></td>
					<td><a href="#" onclick="checkAddress(); return false;"><img id ="checkAddrBtnImg" src="/media_stat/images/giftcards/your_account/chk_addr_btn.gif" width="102" height="25" alt="CHECK ADDRESS" border="0" /></a></td>
				</tr>
			</table>
		</div>
	</div>

<%
    //used?
    String success_page = "/gift_card/purchase/purchase_giftcard.jsp";
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
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
			if (sessionuser.getGiftCart().getDeliveryAddress()!=null) {
				UserValidationUtil.validateRecipientListEmpty(request, result); 
				sessionuser.getGiftCart().setDeliveryAddress(null);
				
             }
				FDRecipientList recipListContinue = sessionuser.getRecipentList();
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

			<%
				if(sessionuser.isLastRecipAdded() && (recipListContinue != null && recipListContinue.size() > 0)) {
				
				String infoMsg = SystemMessageList.MSG_GC_ADD_RECIP_SUCCESS;
			%>
					
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					<td rowspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
					<td colspan="2" bgcolor="#FF9900"><img src="/media_stat/images/layout/ff9900.gif" width="1" height="1"></td>
					<td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
					<td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
				</tr>
				<tr>
					<td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
				</tr>
				<tr>
					<td width="18" bgcolor="#FF9900"><img src="/media_stat/images/template/system_msgs/check_FF9900.gif" width="18" height="22" border="0" alt="check"></td>
					<td class="text11orbold" width="100%" bgcolor="#ffffff">
							<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
								<%=infoMsg%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
					</td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
					<td bgcolor="#ff9900"><img src="/media_stat/images/layout/ff9900.gif" width="1" height="1"></td>
				</tr>
				<tr>
					<td rowspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					<td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
				</tr>
				<tr>
					<td colspan="2" bgcolor="#FF9900"><img src="/media_stat/images/layout/ff9900.gif" width="1" height="1"></td>
				</tr>
				</table>
				<br />
			<%
				}
			%>

			<table width="690" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="text11" width="675">
						<span class="title18">Enter Gift Card Info</span><br />
						Provide your recipients' personal info and details of your gift.
					</td>
					<td width="99">
						&nbsp;
						<%
							//only show continue if user has recips in list

							if(recipListContinue != null && recipListContinue.size() > 0) {
						%>
							<input type="image" onClick="return pendGC();" name="form_action_name" src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25"  hspace="4" vspace="4" alt="continue" border="0">
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px;">
						<img style="margin: 2px 0;" width="675" height="1" border="0" src="/media_stat/images/layout/999966.gif" /><br />
					</td>
				</tr>
			</table>
			
			<form name="giftcard_form" id="giftcard_form" method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" id="deleteId" name="deleteId" value="">
                <input type="hidden" id="checkout" name="checkout" value="">
				<%@ include file="/gift_card/purchase/includes/i_giftcard_fields.jspf" %>
				<%@ include file="/gift_card/purchase/includes/recipient_list.jspf" %>
			

			<%--
				<table border="0" cellspacing="0" cellpadding="0" width="675">
					<tr valign="top">
						<td width="640">
							<%@ include file="/includes/i_footer_account.jspf"%>
						</td>
					</tr>
				</table>
			--%>

		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
		<img src="/media_stat/images/layout/ff9900.gif" width="675" height="1" border="0" /><br />
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
		<table width="675" cellspacing="0" cellpadding="0" border="0" valign="middle">
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
			</tr>
			<tr valign="top">
				<td align="right" colspan="2">
					&nbsp;
					<%
						//only show continue if user has recips in list
						if(recipListContinue != null && recipListContinue.size() > 0) {
					%>
						<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
						<input type="image" onClick="return pendGC();" name="form_action_name" src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25"  hspace="4" vspace="4" alt="continue" border="0">
					<% } %>
				</td>
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