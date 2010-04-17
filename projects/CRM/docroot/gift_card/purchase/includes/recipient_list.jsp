<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDRecipientList' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.List' %>
<%@ page import='java.util.ListIterator' %>
<%@ page import='com.freshdirect.webapp.taglib.giftcard.GiftCardUtil' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.customer.SavedRecipientModel"%>
<%@ page import="com.freshdirect.giftcard.RecipientModel"%>
<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>

<%
//for display of recipient number
int indx = 1;

String gcStr = (String)request.getAttribute("giftcard");
boolean isConfirm=false;
if("true".equalsIgnoreCase(gcStr)){
    isConfirm=true;
}

FDUserI giftUser = (FDUserI)session.getAttribute(SessionName.USER);

FDRecipientList recipList = giftUser.getRecipientList();
if(recipList != null) {
	List<RecipientModel> recipients =  recipList.getRecipients();
	if(!recipients.isEmpty()) {
		if (isConfirm) {
			//we're on payment info page
		%><table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
			<tr>
				<td><!-- <img src="/media_stat/images/giftcards/purchase/gc_review_recip_list.gif" width="176" height="13" alt="Review Your Recipient List" /> -->
                 <b>Selected Cards (<%=recipList.size()%>):</b>
                <br /><br />
					Check the names and amounts of your gifts below.
				</td>
			</tr>
		</table>
		<%
		}else{
			//we're on gift & recipient options page
		%>
		<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
			<tr>
				<td><!-- <img src="/media_stat/images/giftcards/purchase/recip_list.gif" width="120" height="21" alt="Recipient List" /> -->
					<b>Selected Cards (<%=recipList.size()%>):</b>
				</td>
			</tr>
		</table>
		<% } %>
		<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
		<tr><td>
			<table class="gc_table1 bordBlack" cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<th class="recipNumber">#</th>
					<th class="recipName">To</th>
					<th class="recipEmail">Email To</th>
					<th class="recipFrom">From</th>
					<th class="recipMsg">Message</th>
					<th class="recipTemp">Card Design</th>
					<th class="recipLinks">Options</th>
					<th class="recipAmt">Amount</th>
					<th class="pad">&nbsp;</td>
				</tr>
			</table>
			<div class="gcRecipListContainerCRM">
				<table class="recipTableCRM" cellspacing="0" cellpadding="0" width="100%">
						<%
						ListIterator<RecipientModel> j = recipients.listIterator();
						while(j.hasNext()) {
							SavedRecipientModel srm = (SavedRecipientModel)j.next();
						%>
					<tr <%if(indx%2==0){%>class="list_odd_row"<%}%>>
						<td class="recipNumber">
							<%= indx %>.&nbsp;
						</td>
						<td class="pad">&nbsp;</td>
						<td class="recipName">
							<%= srm.getRecipientName()%>
						</td>
						<td class="recipEmail">
							<%= srm.getRecipientEmail()%>
						</td>
						<td class="recipFrom">
							<%= srm.getSenderName()%>
						</td>
						<td class="recipMsg">
							<%= srm.getPersonalMessage()%>
						</td>
						<td class="recipTemp">
							<%= GiftCardUtil.getTemplateName(srm.getTemplateId()) %>
						</td>
						<td class="recipLinks">
							<a href="#" onClick="recipPreview('<%=srm.getRandomId()%>'); return false;">Preview</a><a href="/gift_card/purchase/add_giftcard.jsp?recipId=<%=srm.getRandomId()%>">Edit</a><a href="<%= request.getRequestURI() %>?deleteId=<%=srm.getRandomId()%>">Delete</a>
							<%= srm.getDeliveryMode().getDescription() %>
						</td>
						<td class="recipAmt">
							$<%= srm.getFormattedAmount() %>
						</td>
					</tr>
						<%
							indx++;
						}
						%>
				</table>
			</div>

			<table class="recipAmountTableCRM" cellspacing="0" cellpadding="0" border="0" valign="middle">
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
			<%
			if (isConfirm) {
				//we're on payment info page
			%>
				<tr>
					<td class="right">Subtotal: <div class="recipAmount">$<%=recipList.getFormattedSubTotal() %></div></td>
				</tr>
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
				<tr>
					<td class="right">FreshDirect Store Credit: <div class="recipAmount">$<%= FormatterUtil.formatToTwoDecimal(giftUser.getGiftCart().getCustomerCreditsValue()) %></div></td>
				</tr>
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
				<tr>
					<td class="recipTotal"><div class="recipTotal recipAmount" style="width: auto;">TOTAL: $<%= FormatterUtil.formatToTwoDecimal(giftUser.getGiftCart().getTotal()) %></div></td>
				</tr>
			<%
			}else{
				//we're on gift & recipient options page
			%>
				<%  if ( request.getRequestURI().toLowerCase().indexOf("purchase")>-1 && ((FDUserI) session.getAttribute( SessionName.USER )).getShoppingCart().getCustomerCreditsValue() > 0) { %>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right">Credit Applied:</td>
							<td align="right">-<%=((FDUserI) session.getAttribute( SessionName.USER )).getShoppingCart().getFormattedCustomerCreditsValue()%></td>
							<td colspan="7"></td>
						</tr>		   
				<%  } %>
				<tr>
					<td class="recipTotal"><div class="recipTotal recipAmount" style="width: auto;">SUBTOTAL: $<%=recipList.getFormattedSubTotal() %></div></td>
				</tr>
			<% } %>
			</table>
		
		</td></tr>
		</table>
		<%
	}
}
		%>
