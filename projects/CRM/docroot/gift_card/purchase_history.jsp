<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.giftcard.*' %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.customer.ErpSaleInfo'%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

	
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Gift Card : Purchase History</tmpl:put>
	<tmpl:put name='content' direct='true'>

	<jsp:include page="/includes/giftcard_nav.jsp"/>
	<table class="gc_tableBody" cellspacing="0" cellpadding="0" width="100%" border="0">
		<tr>
			<td>
				<fd:GetGiftCardPurchased id="recipients">
				<% 
					JspTableSorter sort = new JspTableSorter(request);
					int i = 0;
				%>

				<table class="gc_table1 bordBlack" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<th width="16px" align="center">&nbsp;</th>
						<th width="100px">Certificate #</th>
						<th width="150px">Status / Balance</th>
						<th width="200px">Options</th>
						<th width="200px">Purchased (date)</th>
						<th width="200px">Email Recipient</th>
						<th width="200px">Used (requested date)</th>
						<th width="200px">Redeemed by </th>
						<th><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></th>
					</tr>
				</table>

				<div class="gcBulkPurchaseContainerCRM">
				
					<table class="gc_table1" cellspacing="0" cellpadding="0" width="100%" border="0">
						<logic:iterate id="recipient" collection="<%= recipients %>" type="com.freshdirect.giftcard.ErpGCDlvInformationHolder">
						<tr <%if(i%2==0){%>class="list_odd_row"<%}%>>
							<td style="vertical-align: middle; text-align: center; width: 16px;">
								<div style="background-color: #fff; padding: 0px; margin: 0px; border: 0px;">
									<a href="#" onclick="$('p<%=i%>').toggle(); $('usedOrders<%=i%>').toggle(); $('m<%=i%>').toggle(); return false" style="border: 0px; text-decoration: none; color: transparent;"><img width="16" height="16" alt="hide" src="/media_stat/crm/images/icon_minus.gif" id="m<%=i%>" class="gcIcon" style="display: none; border: 0px;" /><img width="16" height="16" alt="show" src="/media_stat/crm/images/icon_plus.gif" id="p<%=i%>" style="display: block; border: 0px;" /></a>
								</div>
							</td>
							<td style="width: 100px;">&nbsp;<%= recipient.getCertificationNumber() %></td>
							<td style="width: 150px;">
								<div style="padding: 3px; padding-left: 16px; height: auto;" id="status<%= i %>cont">
									<a href="#" id="status<%= i %>" onClick="statusCheckPHStatus('status<%= i %>', '<%= recipient.getCertificationNumber() %>', '<%= recipient.getRecepientModel().getSale_id() %>'); return false;" class="resendLink" style="color: #33c;">Check Status</a>
								</div>
							</td style="width: 200px;">
							<td style="width: 200px;">
								<span style="padding: 3px; padding-left: 16px; height: auto;" id="email<%= i %>cont">
									<a href="#" id="email<%= i %>" onClick="statusCheckPHEmail('email<%= i %>', '<%= recipient.getCertificationNumber() %>', '<%= recipient.getRecepientModel().getSale_id() %>'); return false;" class="resendLink" style="color: #639;">Send Email</a>
								</span>
								<span style="padding: 3px; padding-left: 8px; height: auto;" id="emailCancel<%= i %>cont">
									<a href="#" id="emailCancel<%= i %>" onClick="statusCheckPHEmailCancel('emailCancel<%= i %>', '<%= recipient.getCertificationNumber() %>', '<%= recipient.getRecepientModel().getSale_id() %>'); return false;" class="resendCancelLink" style="color: #9F1010;">Send Cancellation</a>
								</span>
							</td>
							<td style="width: 200px;">
								<a href="/main/order_details.jsp?orderId=<%= recipient.getRecepientModel().getSale_id() %>" class="gLink" style="color: #5EBB64;"><%= recipient.getRecepientModel().getSale_id() %></a>&nbsp;(<%= recipient.getPurchaseDate() %>)
							</td>
							<td style="width: 200px;"><%= recipient.getRecepientModel().getRecipientEmail() %></td>
							<td colspan="2" style="width: 400px;">
								<table id="usedOrders<%=i%>" style="display: none;">
								<fd:GetGiftCardUsedOrders id="orders" certNum="<%= recipient.getCertificationNumber() %>">
									<logic:iterate id="saleInfo" collection="<%= orders %>" type="com.freshdirect.customer.ErpSaleInfo">
										<tr>
											<td>
												<a href="/main/order_details.jsp?orderId=<%= saleInfo.getSaleId() %>"><%= saleInfo.getSaleId() %></a>&nbsp;(<%= saleInfo.getRequestedDate() %>)

												<fd:GetRedeemedGiftCardUserInfo  id="erpEmail" customerId="<%=saleInfo.getErpCustomerId()%>">
													<%= erpEmail %><br />
												</fd:GetRedeemedGiftCardUserInfo>
											</td>
										</tr>
									</logic:iterate>
									<% if (orders.size() == 0){%><tr><td>No activity.</td></tr><%} %>
								</fd:GetGiftCardUsedOrders>
								</table>
							</td>
							<td style="width: 10px;"><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>

						</tr>
						<% i++; %>
						</logic:iterate>
					</table>
				</div>

				</fd:GetGiftCardPurchased>
			</td>
		</tr>
	</table>
    
	</tmpl:put>
</tmpl:insert>