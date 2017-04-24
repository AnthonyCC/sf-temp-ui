<%@ page import='java.util.*' %>
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
	<tmpl:put name='title' direct='true'>FreshDirect - Donate Gift Card</tmpl:put>
	<tmpl:put name='content' direct='true'>
	

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
    
    
    request.setAttribute("donation", "true");
   	String gcDonId = null;
    if (request.getParameter("gcDonId") != null) {
    	gcDonId = request.getParameter("gcDonId");
    }   
%>
 
		<fd:AddSavedRecipientController actionName='<%= action_name %>' resultName='result' successPage='/gift_card/purchase/add_donation_giftcard.jsp'>
            <%
               FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);
            	sessionuser.setGiftCardType(EnumGiftCardType.DONATION_GIFTCARD);
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
				<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
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

			<table width="<%=W_ADD_GIFTCARD_TOTAL%>" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td class="text11" width="<%=W_ADD_GIFTCARD_TOTAL-99%>">
						<span class="title18">Enter Gift Card Donation Information</span><br />
					</td>
					<td width="99">
						&nbsp;
							<input type="image" onClick="return setCheckOut();" name="form_action_name" src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25"  hspace="4" vspace="4" alt="continue" border="0">
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px 0px;">
						<img style="margin: 2px 0;" width="<%=W_ADD_GIFTCARD_TOTAL%>" height="1" alt="" border="0" src="/media_stat/images/layout/999966.gif" /><br />
					</td>
				</tr>
			</table>

			<form name="giftcard_donation_form" id="giftcard_donation_form" method="post">
				<input type="hidden" name="actionName" value="">
				<input type="hidden" id="deleteId" name="deleteId" value="">
                <input type="hidden" id="checkout" name="checkout" value="">
                <input type="hidden" id="gcDonId" name="gcDonId" value="<%= gcDonId %>">				
				<table width="<%=W_ADD_GIFTCARD_TOTAL%>" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td colspan="5" align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="6" border="0" /></td>
					</tr>
					<tr>
						<td width="380" valign="top">
							<%@ include file="/gift_card/purchase/includes/i_donation_giftcard_fields.jspf" %>
						</td>
						<td bgcolor="#cccccc" width="1">
							<img src="/media_stat/images/layout/dotted_line_w.gif" width="1" height="1" border="0" />
						</td>
						<td valign="top" width="489">
							<%
								if(_donOrganization != null){								
							%>
							<table cellspacing="0" cellpadding="0" border="0">
								<tr>
									<td align="center" colspan="2">
										<%
											Html editorialHeaderDetailHtml = _donOrganization.getEditorialHeaderMedia();
											if (editorialHeaderDetailHtml != null) {
										%>
												<fd:IncludeMedia name='<%= editorialHeaderDetailHtml.getPath() %>' />
										<%															
											}
										%>&nbsp;										
									</td>									
								</tr>
								<tr>
									<td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="40" border="0" /></td>
								</tr>
								<tr>
									<td align="center" width="250" valign="top">
										<%= (_donOrganization.getOrganizationLogo() != null) ? _donOrganization.getOrganizationLogo().toHtml() : "" %>&nbsp;
									</td>
									<td align="left" width="280">
										<%
											Html editorialDetailHtml = _donOrganization.getEditorialDetail();
											if (editorialDetailHtml != null) {
										%>
												<fd:IncludeMedia name='<%= editorialDetailHtml.getPath() %>' />
										<%															
											}
										%>
										&nbsp;
									</td>
								</tr>
							</table>
							<%
								}
							%>
						</td>
					</tr>
				</table>
	        </form>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
		<img src="/media_stat/images/layout/999966.gif" alt="" width="<%=W_ADD_GIFTCARD_TOTAL%>" height="1" border="0" /><br />
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /><br />
		
		<table width="<%=W_ADD_GIFTCARD_TOTAL%>" cellspacing="0" cellpadding="0" border="0" valign="middle">			
			<tr valign="top">
				<td align="right" colspan="2">
					&nbsp;
					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" />
					<input type="image" onClick="return setCheckOut();" name="form_action_name" src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25"  hspace="4" vspace="4" alt="continue" border="0">
				</td>
			</tr>
		</table>
		
        <script>
            function setCheckOut() {
                document.giftcard_donation_form.checkout.value = 'true';
                document.giftcard_donation_form.submit();
            }
        </script>
</fd:AddSavedRecipientController>
	</tmpl:put>
</tmpl:insert>