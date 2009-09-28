<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDRecipientList' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.List' %>
<%@ page import='java.util.ListIterator' %>
<%@ page import='com.freshdirect.webapp.taglib.giftcard.GiftCardUtil' %>

<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>

<%
//for display of recipient number
int indx = 1;
//for keeping track of total gift cards on bulk orders
int bulkCount = 0;

String gcStr = (String)request.getAttribute("giftcard");
boolean isConfirm=false;
if("true".equalsIgnoreCase(gcStr)){
    isConfirm=true;
}

FDUserI giftUser = (FDUserI)session.getAttribute(SessionName.USER);
double total=0;

FDBulkRecipientList recipList = giftUser.getBulkRecipentList();
if(recipList != null) {
	List recipients =  recipList.getRecipents();
	if(!recipients.isEmpty()) {
		if (isConfirm) {
			//we're on payment info page
		%>
		
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.customer.FDBulkRecipientList"%>
<%@page import="com.freshdirect.fdstore.customer.FDBulkRecipientModel"%>

		<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
			<tr>
				<td><!-- <img src="/media_stat/images/giftcards/purchase/gc_review_recip_list.gif" width="176" height="13" alt="Review Your Recipient List" /> -->
                 <b>Selected Bulk Sets of Cards (<%=recipList.size()%>)</b>
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
                <b>Selected Bulk Sets of Cards (<%=recipList.size()%>)</b>
                <br /><br />
					Check the names and amounts for your giftcards below. <strong>Click the link by the recipient names to see how their Email Gift Card will appear in their Inbox!</strong> If you need to make changes, you may alter the amount here or click the Edit link to change personal info.
				</td>
			</tr>
		</table>
		<% } %>
		
		<table class="gc_table1 bordBlack" cellspacing="0" cellpadding="0" width="100%" border="0">
			<tr>
				<th class="recipNumberBulk">#</th>
				<th class="recipFromBulk">From</th>
				<th class="recipFromEmailBulk">From Email</th>
				<th class="recipTempBulk">Card Design</th>
				<th class="pad">&nbsp;</td>
				<th class="recipLinksBulk">Options / Delivery Mode</th>
				<th class="recipQtyBulk">Quanitity</th>
				<th class="pad">&nbsp;</td>
				<th class="recipAmtEaBulk">Amount (Each)</th>
				<th class="pad">&nbsp;</td>
				<th class="recipAmtBulk">Amount (Set)</th>
			</tr>
		</table>
		<div class="gcRecipListContainerCRM">
			<table class="recipTableCRM" cellspacing="0" cellpadding="0" width="100%" border="0">
			<%
				ListIterator j = recipients.listIterator();
				while(j.hasNext()) {
					FDBulkRecipientModel srm = (FDBulkRecipientModel)j.next();
			%>
				<tr <%if(indx%2==0){%>class="list_odd_row"<%}%>>
					<td class="recipNumberBulk">
						<%= indx %>.&nbsp;
					</td>
					<td class="recipFromBulk">
						<%= srm.getSenderName()%>
					</td>
					<td class="recipFromEmailBulk">
						<%= srm.getSenderEmail()%>
					</td>
					<td class="recipTempBulk">
						<%= GiftCardUtil.getTemplateName(srm.getTemplateId()) %>
					</td>
					<td class="pad">&nbsp;</td>
					<td class="recipLinksBulk">
						<a href="/gift_card/purchase/add_bulk_giftcard.jsp?recipId=<%=srm.getRandomId()%>">Edit</a><a href="<%= request.getRequestURI() %>?deleteId=<%=srm.getRandomId()%>">Delete</a>
						<%= srm.getDeliveryMode().getDescription() %>
					</td>
					<td class="recipQtyBulk">
						<%= srm.getQuantity()%>
					</td>
					<td class="pad">&nbsp;</td>
					<td align="center" class="recipAmtEaBulk">
						$<%= srm.getFormattedAmount() %>
					</td>
					<td class="pad">&nbsp;</td>
					<td class="recipAmtBulk">
						$<%= srm.getFormattedSubtotal() %>
					</td>
				</tr>
					<%
						bulkCount = bulkCount+Integer.parseInt(srm.getQuantity());
						indx++;
					}
					indx=1;
					%>
			</table>
		</div>


		<table width="100%" cellspacing="0" cellpadding="0" border="0" valign="middle">
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
				<td class="recipTotalInfoBULK">
					<%=recipList.size()%> Bulk Sets of Cards (<%=bulkCount%> Cards Total)
				</td>
				<td class="recipTotalBULK"><div class="recipTotal recipAmount" style="width: auto;">SUBTOTAL: $<%=recipList.getFormattedSubTotal() %></div></td>
			</tr>
		<% } %>
		</table>
		<%
	}
}
		%>
