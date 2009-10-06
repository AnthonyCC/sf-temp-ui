<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.DateUtil'%>

<% boolean isGuest = false; %>
<crm:GetCurrentAgent id="currentAgent">
	<% isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); %> 
</crm:GetCurrentAgent>

<%
	boolean hasCustomerCase = CrmSession.hasCustomerCase(session);
%>

<script type="text/javascript">
function recEmailSubmit(){
	
	
	if(document.forms["giftcard_search"].recEmail.value.trim() ==""){
		return false;
	}
	document.forms["giftcard_search"].submit();
}
function gcNumberSubmit(){
	
	if(document.forms["giftcard_search1"].gcNumber.value.trim() ==""){
		return false;
	}
	document.forms["giftcard_search1"].submit();
}
</script>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Gift Card : Summary</tmpl:put>
	<tmpl:put name='content' direct='true'>

		<jsp:include page="/includes/giftcard_nav.jsp"/>

		<fd:RedemptionCodeController actionName="noaction" result="redemptionResult">
			<table border="0" cellspacing="5" cellpadding="0" width="100%" class="gc_tableBody">
				<% 
					FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
					boolean allowGCUsage = user.getFDCustomer().getProfile().allowApplyGC();
				%>
				<tr valign="top">
					<td colspan="3">
						<table class="<%= (allowGCUsage)?"gc_caseEnabled":"gc_caseDisabled" %>">
							<tr>
								<td style="width: 10px;"><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td class="gc_caseStatus">Usage <%= (allowGCUsage)?"enabled":"disabled" %></td>
								<td>
									Customer <% if (!allowGCUsage) { %>NOT<% } %> allowed to Add Gift Card to their Account
								</td>
								<td style="width: 70px;">
									<% if (hasCustomerCase) { %>
										<fd:GiftCardController actionName='setAllowGCUsage' result='result' successPage='/gift_card/giftcard_summary.jsp'>
											<form method="post" style="padding:0px;margin:0px;">
												<input type="submit" class="button" name="<%= (allowGCUsage)?"allowGCUsageFALSE":"allowGCUsageTRUE" %>" value="<%= (allowGCUsage)?"DISABLE":"ENABLE" %>" />
											</form>
										</fd:GiftCardController>
									<% }else{ %>
										<input type="submit" class="button disabled" name="<%= (allowGCUsage)?"allowGCUsageFALSE":"allowGCUsageTRUE" %>" value="<%= (allowGCUsage)?"DISABLE":"ENABLE" %>" DISABLED />
									<% } %>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr valign="top">
					<td width="33%">
						<fd:GetGiftCardReceived id='giftcards'>
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1">
							<tr>
								<th width="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></th>
								<th colspan="4">Gift Cards Added To Account</th>
							</tr>
							<tr class="gc_colHeader">
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td width="85">Certificate #</td>
								<td class="gc_balance">Balance</td>
								<td class="gc_options">Options</td>
								<td >Status</td>
							</tr>
							<logic:iterate id="giftcard" collection="<%= giftcards %>" type="com.freshdirect.fdstore.giftcard.FDGiftCardModel" indexId="counter1">
								<%
								Integer counterTmp = (Integer) pageContext.getAttribute("counter1");
								if (counterTmp.intValue() <=4) { %>                                            
									<tr valign="top" <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
										<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
										<td><%= giftcard.getCertificateNumber() %></td>
										<td class="gc_balance">$<%= giftcard.getFormattedBalance() %>&nbsp;&nbsp;</td>
										<td><% if(hasCustomerCase){ %> <a href="<%= request.getRequestURI() %>?action=deleteGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=true" class="rLink">remove</a><% } else { %> remove <% } %></td>
										<td><%= (giftcard.isRedeemable()&& giftcard.getBalance()>0)?"Active":(giftcard.isRedeemable()?"Redeemed":"Cancelled")%></td>
									</tr>
								<% } %>
							</logic:iterate>
							<tr>
								<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
							</tr>
							<tr>
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td colspan="3">
									<% if (user.getGiftCardList().size()>5) {%>
										<a href="/gift_card/giftcard_addused.jsp" class="gLink" style="color: #5EBB64;">More &gt;&gt;</a>
									<% }else{ %>
										&nbsp;
									<% } %>
								</td>
							</tr>
                            <% if(!hasCustomerCase){ %><tr><td colspan="4"><b>Note: Case Required to remove a Gift Card.</b></td></tr><% } %>
						</table>
						</fd:GetGiftCardReceived>  
					</td>
					<td width="33%">
						<fd:GetGiftCardPurchased id="recipients">
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table2">
							<tr>
								<th width="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></th>
								<th colspan="3">Recently purchased Gift Cards</th>
							</tr>
							<tr class="gc_colHeader">
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td width="85">Order #</td>
								<td width="100">Certificate #</td>
								<td>Recipient</td>
							</tr>
							<logic:iterate id="recipient" collection="<%= recipients %>" type="com.freshdirect.giftcard.ErpGCDlvInformationHolder" indexId="counter2">
								<%
								Integer counterTmp = (Integer) pageContext.getAttribute("counter2");
								if (counterTmp.intValue() <=4) { %>          
									<tr valign="top" <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
										<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
										<td><a href="/main/order_details.jsp?orderId=<%= recipient.getPurchaseSaleId() %>" class="gLink" style="color: #5EBB64;"><%= recipient.getPurchaseSaleId() %></a></td>
										<td><%= recipient.getCertificationNumber() %></td>
										<td><%= recipient.getRecepientModel().getRecipientName() %></td>
									</tr>
								<% } %> 
							</logic:iterate>
						</table>
						</fd:GetGiftCardPurchased>
					</td>
					<td width="33%">       
						<fd:GetRedemedGiftCardOrders id='orders'>
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table3">
							<tr>
								<th width="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></th>
								<th colspan="4">Recent Orders with Gift Card Purchase</th>
							</tr>
							<tr class="gc_colHeader">
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td width="85">Order #</td>
								<td width="85">Delivery Date</td>
								<td class="gc_balance">Amount</td>
								<td>Status</td>							
							</tr>
							<logic:iterate id="order" collection="<%= orders %>" type="com.freshdirect.customer.ErpSaleInfo" indexId="counter3">
								<%
								Integer counterTmp = (Integer) pageContext.getAttribute("counter3");
								if (counterTmp.intValue() <=4) { %>          
									<tr <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
										<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
										<td><a href="/main/order_details.jsp?orderId=<%= order.getSaleId() %>" class="gLink" style="color: #5EBB64;"><%= order.getSaleId() %></a></td>
										<td><%= order.getRequestedDate() %></td>
										<td class="gc_balance">$<%= order.getAmount() %></td>
										<td><%= order.getStatus().getName() %></td>							
									</tr>
								<% } %> 
							</logic:iterate>
							<tr>
								<td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
							</tr>
							<tr>
								<td><img src="/media_stat/images/layout/clear.gif" width="10" height="8" border="0" /></td>
								<td colspan="4">
									<% if (orders.size()>5) {%>
										<a href="/main/order_history.jsp" class="gLink" style="color: #5EBB64;">More &gt;&gt;</a>
									<% }else{ %>
										&nbsp;
									<% } %>
								</td>
							</tr>
						</table>
						</fd:GetRedemedGiftCardOrders>        
					</td>
				</tr>

				<tr>
					<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>

				<tr>
					<td width="33%" class="gc_table_footer">
						<fd:GiftCardController actionName='applyGiftCard' result='result'>
							<fd:ErrorHandler result='<%=result%>' name='invalid_card' id='errorMsg'>
								<%@ include file="/includes/i_error_messages.jspf" %>   
							</fd:ErrorHandler>
							<fd:ErrorHandler result="<%=result%>" name="card_in_use" id="errorMsg">
								<%@ include file="/includes/i_error_messages.jspf" %>   
							</fd:ErrorHandler>
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table1footer">
								<tr valign="middle">
									<td align="center">
                                        <% if(hasCustomerCase){ %> 
											<form method="post" style="padding:0px;margin:0px;">
												Add Gift Card:&nbsp;<input type="text"name="givexNum" />&nbsp;&nbsp;&nbsp;<input type="submit" name="gcApplyCode" id="gcApplyCode" value="ADD" class="button" />
											</form>
										<% }else{ %>
											Add Gift Card:&nbsp;<input type="text"name="givexNum" />&nbsp;&nbsp;&nbsp;<input type="submit" name="gcApplyCode" id="gcApplyCode" value="ADD" class="button disabled" DISABLED />
										<% } %>
									</td>
								</td>
								</tr>
							</table>
						</fd:GiftCardController>
					</td>
					<td width="33%" class="gc_table_footer">
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table2footer">
							<form name="giftcard_search" id="giftcard_search" action="/gift_card/giftcard_landing.jsp">
							<tr valign="middle">
								<td align="center">
									Search Recipient:&nbsp;<input type="text" style="width: 150px;" name="recEmail"/>&nbsp;&nbsp;&nbsp;                                
                                    <input type="button" name="SEARCH" value="SEARCH" class="button" onclick="javascript:recEmailSubmit();" />
								</td>
							</tr>
							</form>
						 </table>
					</td>
					<td width="33%" class="gc_table_footer">
						<table border="0" cellspacing="0" cellpadding="0" width="100%" class="gc_table3footer">
						<form name="giftcard_search1" id="giftcard_search1" action="/gift_card/giftcard_landing.jsp"><!-- /main/order_search_results.jsp?search=quick -->
							<tr valign="middle">
								<td align="center">
									Search Order with Gift Card:&nbsp;<input type="text" style="width: 150px;" name="gcNumber"  value="<%= "null".equalsIgnoreCase(request.getParameter("giftCardNumber")) ? "" : request.getParameter("giftCardNumber") %>" />&nbsp;&nbsp;&nbsp;                                      
                                    <input type="button" name="SEARCH1" value="SEARCH" class="button" onclick="javascript:gcNumberSubmit();;" />
                                    
								</td>
							</tr>
						</form>
						</table>
					</td>
				</tr>
				
				<tr>
					<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
				</tr>
			</table>
		</fd:RedemptionCodeController>        


	</tmpl:put>
</tmpl:insert>