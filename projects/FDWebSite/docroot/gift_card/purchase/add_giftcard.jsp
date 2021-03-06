<%@ page import="java.util.*"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.giftcard.EnumGiftCardType' %>

<% //expanded page dimensions
final int W_ADD_GIFTCARD_TOTAL = 970;
%>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/giftcard.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Add Gift Card"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Add Gift Card</tmpl:put> --%>
	<tmpl:put name='pageType' direct='true'>gc_add</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<style>
			.W_ADD_GIFTCARD_TOTAL { width: <%= W_ADD_GIFTCARD_TOTAL %>px; }
			.W_ADD_GIFTCARD_TOTAL-99 { width: calc( <%= W_ADD_GIFTCARD_TOTAL %>px - 99px ); }
		</style>
    <div class="gcCheckAddressBox"> 
		<div style="text-align: left;" class="gcCheckAddressBoxContent" id="gcCheckAddressBox"><form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter>
			<span class="title18">Check An Address</span>
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a><br style="clear: both;" />
			<div id="gcCheckAddressBoxErr">
				<span id="gcCheckAddressBoxMsg" class="gcCheckAddressBoxMsg">Sorry, we don't currently deliver to this address.<br />
			If this is a New York address, may we suggest our Pickup Window in Long Island City?</span>
			</div><br />
			<img src="/media_stat/images/layout/cccccc.gif" alt="" width="390" height="1" border="0"><br /><br />
			<table  role="presentation" class="accessibilitySpacing" border="0" cellspacing="0" cellpadding="4" width="100%">
				<tr>
					<th width="130" align="right"><label for="address1">Street Address 1:</label></th>
					<td><input name="address1" id="address1" class="gcCheckAddress-address1" value="" /></td>
				</tr>
				<tr valign="middle">
					<th width="130" align="right"><label for="address2">Street Address 2:</label></th>
					<td><input name = "address2" class="gcCheckAddress-address2" id="address2" value="" /></td>
				</tr>
				<tr>
					<th width="130" align="right"><label for="city">City/Town:</label></th>
					<td><input name="city" id="city" class="gcCheckAddress-city" value="" /></td>
				</tr>
				<tr>
					<th width="130" align="right"><label>State:</label></th>
					
					<td><fieldset><legend class="offscreen">state:</legend><input type="radio" name="state" id="stateNY" value="NY" checked /> <label for="stateNY">New York </label><input type="radio" name="state" id="stateNJ" value="NJ"/> <label for="stateNJ">New Jersey</label><br />
					<input type="radio" name="state" id="stateCT" value="CT" /><label for="stateCT"> Connecticut</label> <input type="radio" name="state" id="statePA" value="PA" /><label for="statePA"> Pennsylvania </label><br/>
					<input type="radio" name="state" id="stateDE" value="DE" /> <label for="stateDE">Delaware</label> <input type="radio" name="state" id="stateDC" value="DC" /><label for="stateDC"> Washington, D.C. </label></fieldset></td>
				</tr>
				<tr>
					<th width="130" align="right"><label for="zipcode">ZIP Code:</label></th>
					<td><input name="zipcode" id="zipcode" class="gcCheckAddress-zipcode" value="" /></td>
				</tr>
				<tr><td colspan="2">&nbsp;&nbsp;</td></tr>
				<tr>
					<td width="150" align="right"><button class="cssbutton transparent green small" onclick="Modalbox.hide(); return false;">CANCEL</button></td>
					<td><button class="cssbutton small green space"  onclick="checkAddress(); return false;">CHECK ADDRESS</button></td>
				</tr>
			</table>
		</form></div>
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
               sessionuser.setGiftCardType(EnumGiftCardType.REGULAR_GIFTCARD);

				if (sessionuser.getGiftCart().getDeliveryAddress()!=null) {
					UserValidationUtil.validateRecipientListEmpty(request, result); 
					sessionuser.getGiftCart().setDeliveryAddress(null);
					
	             }
				FDRecipientList recipListContinue = sessionuser.getRecipientList();
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
				
				if (("GET".equalsIgnoreCase(request.getMethod())) && !"deleteSavedRecipient".equalsIgnoreCase(action_name)) {sessionuser.setLastRecipAdded(false);}

				String infoMsg = SystemMessageList.MSG_GC_ADD_RECIP_SUCCESS;
			%>
					
				<table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					<td rowspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_tp_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
					<td colspan="2" bgcolor="#FF9900"><img src="/media_stat/images/layout/ff9900.gif" alt="" width="1" height="1"></td>
					<td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_tp_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
					<td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
				</tr>
				<tr>
					<td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
				</tr>
				<tr>
					<td width="18" bgcolor="#FF9900"><img src="/media_stat/images/template/system_msgs/check_FF9900.gif" width="18" height="22" border="0" alt="check"></td>
					<td class="success13text" width="100%" bgcolor="#ffffff">
							<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
								<%=infoMsg%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
					</td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
					<td bgcolor="#ff9900"><img src="/media_stat/images/layout/ff9900.gif" alt="" width="1" height="1"></td>
				</tr>
				<tr>
					<td rowspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_bt_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
					<td bgcolor="#ffffff"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					<td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/FF9900_bt_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
				</tr>
				<tr>
					<td colspan="2" bgcolor="#FF9900"><img src="/media_stat/images/layout/ff9900.gif" alt="" width="1" height="1"></td>
				</tr>
				</table>
				<br />
			<%
				}
			%>

			<table role="presentation" class="W_ADD_GIFTCARD_TOTAL" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="text11" class="W_ADD_GIFTCARD_TOTAL-99">
						<span class="title18">Enter Gift Card Info</span><br />
						Provide your recipients' personal info and details of your gift.
					</td>
					<td width="99">
						&nbsp;
						<%
							//only show continue if user has recips in list

							if(recipListContinue != null && recipListContinue.size() > 0) {
						%>
							<button onClick="return pendGC();" name="form_action_name" class="cssbutton small orange">CONTINUE</button>
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px 0px;">
						<img style="margin: 2px 0;" class="W_ADD_GIFTCARD_TOTAL" height="1" border="0" alt="" src="/media_stat/images/layout/999966.gif" /><br />
					</td>
				</tr>
			</table>
			
			<form fdform class="top-margin10 dispblock-fields" fdform-displayerrorafter name="giftcard_form" id="giftcard_form" method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" id="deleteId" name="deleteId" value="">
                <input type="hidden" id="checkout" name="checkout" value="">
				<%@ include file="/gift_card/purchase/includes/i_giftcard_fields.jspf" %>
				<%@ include file="/gift_card/purchase/includes/recipient_list.jspf" %>
			

			<%--
				<table border="0" cellspacing="0" cellpadding="0" class="W_ADD_GIFTCARD_TOTAL">
					<tr valign="top">
						<td width="640">
							<%@ include file="/includes/i_footer_account.jspf"%>
						</td>
					</tr>
				</table>
			--%>

        </form>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
		<img src="/media_stat/images/layout/ff9900.gif" alt="" class="W_ADD_GIFTCARD_TOTAL" height="1" border="0" /><br />
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
		<table role="presentation" class="W_ADD_GIFTCARD_TOTAL" cellspacing="0" cellpadding="0" border="0" valign="middle">
			<tr>
				<td colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
			</tr>
			<tr valign="top">
				<td align="right" colspan="2">
					&nbsp;
					<%
						//only show continue if user has recips in list
						if(recipListContinue != null && recipListContinue.size() > 0) {
					%>
						<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
                        <button onClick="return pendGC();" name="form_action_name" class="cssbutton small orange">CONTINUE</button>
					<% } %>
				</td>
			</tr>
		</table>
        <script>
            function setCheckOut() {
                document.giftcard_form.checkout.value = 'true';
                 document.giftcard_form.submit();
            }
        </script>
</fd:AddSavedRecipientController>
	</tmpl:put>
</tmpl:insert>