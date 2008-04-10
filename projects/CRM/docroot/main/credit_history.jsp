<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.framework.core.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Credit History</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
		
        <fd:CustomerCreditHistoryGetterTag id="customerCreditHistory">
		<% List creditHistory = customerCreditHistory.getCreditHistory();%>
		
		<div class="sub_nav">
            <table width="100%">
                <tr>
					<td width="25%" align="center">TOTAL: <b><%=customerCreditHistory.getSumCredit()  + customerCreditHistory.getSumRefund()%></b> <span class="note">(Store Credit<%=customerCreditHistory.getSumCredit()  > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumCredit() %></b>, Refund<%=customerCreditHistory.getSumRefund() > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumRefund()%></b>)</span--%></td>
                    <td width="25%" align="center">Available store credit: <b><%=CCFormatter.formatCurrency(customerCreditHistory.getRemainingAmount())%></b></td>
                    <td width="25%" align="center">Total store credit issued: <b><%=CCFormatter.formatCurrency(customerCreditHistory.getTotalCreditsIssued())%></b></td>
                    <td width="25%" align="center">Total cash back issued: <b><%=CCFormatter.formatCurrency(customerCreditHistory.getTotalCashBack())%></b></td>
                </tr>
            </table>
		</div>
        <div class="list_header">
            <table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
                <tr>
                        <td width="1%"></td>
                        <td width="9%">Date/Time</td>
                        <td width="8%">Order #</td>
                        <td width="4%">Type</td>
                        <td width="19%">Department</td>
                        <td width="12%">Status</td>
                        <td width="12%">Store Credit</td>
                        <td width="12%">Cash Back</td>
                        <td width="11%">Issued By</td>
                        <td width="12%">Approved By</td>
                        <td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
                </tr>
            </table>
        </div>

        <div class="list_content">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <%	if ( creditHistory.size() == 0 ) { %>
                    <tr>
                            <td></td>
                            <td colspan="8" align="center"><br><b>No credits found.</b></td>
                            <td></td>
                    </tr>
            <% } else { %>
                    <logic:iterate id="credit" collection="<%= creditHistory %>" type="com.freshdirect.fdstore.customer.FDCustomerCreditModel" indexId="counter">
                    <tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
                            <td width="1%" class="border_bottom">&nbsp;</td>
                            <td width="9%" class="border_bottom"><span class="time_stamp"><%=CCFormatter.formatDateTime(credit.getCreateDate()) %>&nbsp;</span></td>
                            <td width="8%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%= credit.getSaleId() %>" class="key"><%= credit.getSaleId() %></a>&nbsp;</td>
                            <td width="4%" class="border_bottom"><% if(EnumSaleType.REGULAR.equals(credit.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(credit.getOrderType())){%><Font color="red">A</Font><%}%>&nbsp;</td>
                            <td width="19%" class="border_bottom"><%= credit.getDepartment() %>&nbsp;</td>
                            <td width="12%" class="border_bottom"><%= credit.getStatus().getName() %>&nbsp;</td>
                            <td width="12%" class="border_bottom"><%= EnumComplaintLineMethod.STORE_CREDIT.equals(credit.getMethod())?CCFormatter.formatCurrency(credit.getOriginalAmount()):"&nbsp;" %></td>
                            <td width="12%" class="border_bottom"><%= EnumComplaintLineMethod.CASH_BACK.equals(credit.getMethod())? CCFormatter.formatCurrency(credit.getOriginalAmount()):"&nbsp;" %></td>    
                            <td width="11%" class="border_bottom"><span class="log_info"><%= credit.getIssuedBy() %>&nbsp;</span></td>
                            <td width="12%" class="border_bottom"><span class="log_info"><%= credit.getApprovedBy() %>&nbsp;</span></td>
                    </tr>
                    </logic:iterate>
            <% } %>       
            </table>
        </div> 
                    
    </fd:CustomerCreditHistoryGetterTag>

	</tmpl:put>
		
</tmpl:insert>
