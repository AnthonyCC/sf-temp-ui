<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.giftcard.EnumGCDeliveryMode' %>
<%@ page import='com.freshdirect.fdstore.giftcard.*' %>
<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.webapp.taglib.giftcard.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import="java.text.*" %>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Your Gift Cards</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:RedemptionCodeController actionName="noaction" result="redemptionResult">
	<div class="gcResendBox">
		<div style="text-align: left;" class="gcResendBoxContent" id="gcResendBox">
			<img src="/media_stat/images/giftcards/your_account/resend_hdr.gif" width="169" height="16" alt="Resend Gift Card" />
			<a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/close.gif" width="50" height="11" alt="close" border="0" style="float: right;" /></a>
			<br />If your Recipient never received their Gift Card, you may resend it by clicking Resend Now. If there was an error in the Recipient's email address, or to use a new one, edit the email field.
			<br /><br /><img src="/media_stat/images/layout/cccccc.gif" width="390" height="1" border="0"><br /><br />
			<input type="hidden" id="gcSaleId" value=""/>
			<input type="hidden" id="gcCertNum" value=""/>
			<table border="0" cellspacing="0" cellpadding="4" width="100%">
				<tr>
					<td width="130" align="right">Recipient Name:</td>
					<td><input id="gcResendRecipName" value="" /></td>
				</tr>
				<tr valign="middle">
					<td width="130" align="right">Recipient Email (edit):</td>
					<td><input id="gcResendRecipEmail" value="" /></td>
				</tr>
				<tr>
					<td width="130" align="right">Amount:</td>
					<td><span id="gcResendRecipAmount"><!--  --></span></td>
				</tr>
				<tr>
					<td width="130" align="right">Personal Message:</td>
					<td><textarea id="gcResendRecipMsg"></textarea></td>
				</tr>
				<tr>
					<td width="150" align="right"><a href="#" onclick="Modalbox.hide(); return false;"><img src="/media_stat/images/giftcards/your_account/clear_btn.gif" width="60" height="25" alt="CANCEL" border="0" /></a></td>
					<td><a href="#" onclick="recipResendEmail(); return false;"><img src="/media_stat/images/giftcards/your_account/resend_now_btn.gif" width="85" height="25" alt="RESEND" border="0" /></a></td>
				</tr>
			</table>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br />
			PLEASE NOTE: You will NOT receive a confirmation email for resent email Gift Cards.<br /><br />
			<div id="gcResendErr">&nbsp;</div>
		</div>
	</div>
<% 
    FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );
    boolean showAllReceived = (request.getParameter("showAllReceived") != null && request.getParameter("showAllReceived").equals("true")) ? true :false;
    boolean showAllPurchased = (request.getParameter("showAllPurchased") != null && request.getParameter("showAllPurchased").equals("true")) ? true :false;
%>
		<table border="0" cellspacing="0" cellpadding="0" width="675">
			<tr valign="top">
				<td>
                    <span class="topHeader">Gift Cards</span>
					<!-- <img src="/media_stat/images/giftcards/your_account/" width="" height="" alt="Gift Cards" /> --><br />
					Here you can apply a Gift Card to your current order and view FreshDirect Gift Cards you have given or received.<br />
					For more information about Gift Cards, <a href="<%= FDStoreProperties.getGiftCardLandingUrl() %>">click here</a>.
				</td>
			</tr>
		</table>
		<br /><img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0"><br /><br />
        <fd:GiftCardController actionName='applyGiftCard' result='result' successPage='/your_account/giftcards.jsp'>
            <fd:ErrorHandler result="<%=result%>" name="account_locked" id="errorMsg">
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>
            <fd:ErrorHandler result="<%=result%>" name="apply_gc_warning" id="errorMsg">
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>            
            <fd:ErrorHandler result='<%=result%>' name='invalid_card' id='errorMsg'>
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>
            <fd:ErrorHandler result="<%=result%>" name="card_in_use" id="errorMsg">
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>
            <fd:ErrorHandler result="<%=result%>" name="card_on_hold" id="errorMsg">
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>
            <fd:ErrorHandler result="<%=result%>" name="card_zero_balance" id="errorMsg">
                <%@ include file="/includes/i_error_messages.jspf" %>   
            </fd:ErrorHandler>
            
        <form method="post">
		<table border="0" cellspacing="0" cellpadding="0" width="675">
			<tr valign="middle">
				<td style="height: 25px;" align="center">
					<img src="/media_stat/images/giftcards/your_account/card_enter.gif" width="400" height="21" alt="Enter code" /> <input type="text"name="givexNum" /> <input type="image" name="gcApplyCode" src="/media_stat/images/giftcards/your_account/apply_btn.gif" width="57" height="21" alt="APPLY" border="0" />
				</td>
			</tr>
			<tr>
				<td align="center">
					To apply a new Gift Card balance to your currentorder, enter the code from the card above and click 'Apply'.<br />
					The Gift Card will then appear in Your Cart. Enjoy your free food!
				</td>
			</tr>
		</table>
        </form>
		<br /><img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0"><br /><br />
		</fd:GiftCardController>
		<fd:GetGiftCardReceived id='giftcards'>

		<table border="0" cellspacing="0" cellpadding="0" width="675">
			<tr>
				<td>
					<span class="sideHeader">Gift Cards You Have Received</span><!-- <img src="/media_stat/images/giftcards/your_account/" width="" height="" alt="Gift Cards" /> --><br />
					Here you can apply a Gift Card to your current order and view FreshDirect Gift Cards you have given or received.<br />
					For more information about Gift Cards, <a href="<%= FDStoreProperties.getGiftCardLandingUrl() %>">click here</a>.
				</td>
			</tr>
			<tr>
				<td align="center">
					<!--  -->
					<table class="gcYourActTableList">
						<tr>
							<td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
						</tr>
						<tr class="th">
							<td width="85">Certificate #</td>
							<td width="100">Date Received</td>
							<td>Sender</td>
							<td width="80">Gift Amount</td>
							<td width="80">Balance</td>
							<td width="150">&nbsp;</td>
                            <td width="75">&nbsp;</td>
						</tr>
                        <logic:iterate id="giftcard" collection="<%= giftcards %>" type="com.freshdirect.fdstore.giftcard.FDGiftCardModel">
						<tr>
							<td><%= giftcard.getCertificateNumber() %></td>
							<td><%= DateUtil.formatDate(new Date()) %></td>
							<td><%= user.getGCSenderName(giftcard.getCertificateNumber(),giftcard.getPurchaseSaleId()) %></td>
							<td>$<%= giftcard.getFormattedOrigAmount() %></td>
							<td>$<%= giftcard.getFormattedBalance() %></td>
							<td><% if(giftcard.isRedeemable() && giftcard.getBalance() > 0) { if(!giftcard.isSelected()) {%>
                                <a href="<%= request.getRequestURI() %>?action=applyGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=true" class="note">Apply to the order</a>
                                <% } else {%>
                                <a href="<%= request.getRequestURI() %>?action=applyGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=false" class="note">Do not apply to the order</a>
                                <% } } else { %>
                                <%= giftcard.isRedeemable() ? "Redeemed" : "Cancelled" %>
                                <%}%>
                            </td>
                            <td><a href="<%= request.getRequestURI() %>?action=deleteGiftCard&certNum=<%= giftcard.getCertificateNumber() %>" class="note">Remove</a></td>
						</tr>
                        </logic:iterate>
						<tr>
							<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
						</tr>
                        <% if(!showAllReceived && user.getGiftCardList().size() > GetGiftCardReceivedTag.GC_RECEIVED_DISPLAY_LIMIT) { %>
						<tr>
							<td colspan="7"><a href="/your_account/giftcards.jsp?showAllReceived=true&showAllPurchased=<%= showAllPurchased %>">All Gift Cards Received ></a></td>
						</tr>
                        <% } %>
					</table>
				</td>
			</tr>
		</table>
    </fd:GetGiftCardReceived>
		<br /><br />
    <fd:GetGiftCardPurchased id="recipients">
		<table border="0" cellspacing="0" cellpadding="0" width="675">
			<tr>
				<td>
					<span class="sideHeader">Purchased Gift Cards</span><!-- <img src="/media_stat/images/giftcards/your_account/" width="" height="" alt="Gift Cards" /> --><br />
					Here you can apply a Gift Card to your current order and view FreshDirect Gift Cards you have given or received.<br />
					For more information about Gift Cards, <a href="<%= FDStoreProperties.getGiftCardLandingUrl() %>">click here</a>.
				</td>
			</tr>
			<tr>
				<td align="center">
					<!--  -->
					<table class="gcYourActTableList">
						<tr>
							<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
						</tr>
						<tr class="th">
							<td width="85">Certificate #</td>
							<td width="100">Date Purchased</td>
                            <td width="80">Gift Amount</td>
                            <td width="80">Card Type</td>
							<td>Recipient</td>
							<td width="70">Status</td>
							<td width="60">&nbsp;</td>
							<td width="60">&nbsp;</td>
						</tr>
                        <% DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); %>
                        <logic:iterate id="recipient" collection="<%= recipients %>" type="com.freshdirect.giftcard.ErpGCDlvInformationHolder">
						<tr>
							<td><%= recipient.getCertificationNumber() != null ? recipient.getCertificationNumber() : "" %></td>
							<td><%= dateFormatter.format(recipient.getPurchaseDate()) %></td>
							<td>$<%= recipient.getRecepientModel().getAmount() %></td>    
                            <%  
                                String cardType = GiftCardUtil.getTemplateName(recipient.getRecepientModel().getTemplateId());
                            %>
                            <td><%= cardType %></td>    
							<td><%= recipient.getRecepientModel().getRecipientName() %></td>
                            <%
                                String status = "";
                                boolean isPending = false;
                                if(recipient.getCertificationNumber() == null) {
                                    status = "In Process";
                                    isPending = true;
                                }else{
                                    if(recipient.getRecepientModel().getDeliveryMode().equals(EnumGCDeliveryMode.PDF)) status = "Completed";
                                    else status = "Sent";
                                    FDGiftCardI gc = user.getGiftCardList().getGiftCard(recipient.getCertificationNumber());
                                    if( gc!= null && gc.getBalance() != recipient.getRecepientModel().getAmount()) status = "Redeemed";
                                }
                                
                            %>
							<td><%= status %></td>
                            <% if(!isPending) { %>
                                <td><a href="#" onClick="recipResendFetch('<%= recipient.getRecepientModel().getSale_id() %>','<%= recipient.getCertificationNumber() %>'); return false;"><%= status.equals("Completed") ? "Send" : "Resend" %></a></td>
                                <td><a href="/gift_card/postbacks/pdf_gen.jsp?saleId=<%= recipient.getRecepientModel().getSale_id() %>&certNum=<%= recipient.getCertificationNumber() %>" >View/Print</a></td>
                            <% } else { %>    
                                <td>&nbsp;</a></td>
                                <td>&nbsp;</td>
                            
                            <% } %>
						</tr>
                         </logic:iterate>
						<tr>
							<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
						</tr>
                        <% if(!showAllPurchased) { %>
						<tr>
							<td colspan="7"><a href="/your_account/giftcards.jsp?showAllPurchased=true&showAllReceived=<%= showAllReceived %>">All Purchased Gift Cards ></a></td>
						</tr>
                        <% } %>
					</table>
				</td>
			</tr>
		</table>
    </fd:GetGiftCardPurchased>
		<br /><br /><div style="font-size: 16px; font-weight: bold;"><a href="<%= FDStoreProperties.getGiftCardLandingUrl() %>">Click here to purchase Gift Cards today.</a></div><br /><br />


		<br /><img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0"><br /><br />

		
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br /><br />
		<table border="0" cellspacing="0" cellpadding="0" width="675">
			<tr valign="top">
				<td width="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="continue shopping" align="left"></a></td>
				<td width="640"  class="text11" ><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="continue shopping"></a><br />from <a href="/index.jsp"><b>Home Page</b></a><br />
				<img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0" /></td>
			</tr>
		</table>
</fd:RedemptionCodeController>        
    </tmpl:put>
</tmpl:insert>
