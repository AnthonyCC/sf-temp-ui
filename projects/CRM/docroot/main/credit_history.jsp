<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.framework.core.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Credit History</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
		
<style>
	.yui-skin-sam .yui-pg-container {
		text-align: right;
		padding-right: 30px;
	}

	.yui-skin-sam .yui-pg-page {
		border: 0px !important;
		padding: 2px !important;		
	}
	
	
	
	
	.yui-skin-sam .yui-pg-first, .yui-skin-sam .yui-pg-previous, .yui-skin-sam .yui-pg-next, .yui-skin-sam .yui-pg-last, .yui-skin-sam .yui-pg-current, .yui-skin-sam .yui-pg-pages, .yui-skin-sam .yui-pg-page {
		font-family: Verdana,Arial,sans-serif !important;
		font-size: 9px;
		font-weight: bold;
	}
}
</style>
<!-- Combo-handled YUI CSS files: -->
<fd:css href="/assets/yui-2.9.0/paginator/assets/skins/sam/paginator.css" />
<fd:css href="/assets/yui-2.9.0/datatable/assets/skins/sam/datatable.css" />

		
        <fd:CustomerCreditHistoryGetterTag id="customerCreditHistory">
		<% List creditHistory = customerCreditHistory.getCreditHistory();%>
		
		<div class="sub_nav">
            <table width="100%">
                <tr>
					<td width="25%" align="center">TOTAL: <b><%=customerCreditHistory.getSumCredit()  + customerCreditHistory.getSumRefund()%></b> <span class="note">(Store Credit<%=customerCreditHistory.getSumCredit()  > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumCredit() %></b>, Refund<%=customerCreditHistory.getSumRefund() > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumRefund()%></b>)</span></td>
                    <td width="25%" align="center">Available store credit: <b><%=JspMethods.formatPrice(customerCreditHistory.getRemainingAmount())%></b></td>
                    <td width="25%" align="center">Total store credit issued: <b><%=JspMethods.formatPrice(customerCreditHistory.getTotalCreditsIssued())%></b></td>
                    <td width="25%" align="center">Total cash back issued: <b><%=JspMethods.formatPrice(customerCreditHistory.getTotalCashBack())%></b></td>
                </tr>
            </table>
		</div>		
		
		<% if(creditHistory.size() == 0) { %>
			<div class="list_header">
				No credits for this user at this time.
			</div>
        
		<% } else { %>
                    <div id="pagenums">
						<div id="pagenums" style="" class="yui-pg-container"><style='padding:10px;'>Page: <span id="yui-pg0-0-pages72" class="yui-pg-pages">
						<% int pages = creditHistory.size()/15 + (creditHistory.size() % 15 > 0?1:0); 
							String offset_param = request.getParameter("offset");
							int offset = offset_param != null?Integer.parseInt(offset_param):1;
							for(int i=1; i <= pages; i++ ) {
								if(i == offset) {
									%>
										<span class="yui-pg-current-page yui-pg-page"><%= i %></span>
									<%
								} else {
								%>									
									<a title="Page <%= i %>" page="<%= i %>" class="yui-pg-page" href="/main/credit_history.jsp?offset=<%=i%>"><%=i%></a>
								<%
								}
							}
						%>						
					</div>
					<div class="list_header">
							<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text" style="background:#CBCBCB;color:black;">
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
						
						<%
							int start_offset = (offset - 1) * 15;
							int end_offset = start_offset + 15;
							if(end_offset > creditHistory.size()) {
								end_offset = creditHistory.size();
							}
							int counter = 0;
							for(int i=start_offset; i< end_offset; i++) {
								FDCustomerCreditModel credit = (FDCustomerCreditModel) creditHistory.get(i);
							%>
								<tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
									<td width="1%" class="border_bottom">&nbsp;</td>
									<td width="9%" class="border_bottom"><span class="time_stamp"><%=CCFormatter.formatDateTime(credit.getCreateDate()) %>&nbsp;</span></td>
									<td width="8%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%= credit.getSaleId() %>" class="key"><%= credit.getSaleId() %></a>&nbsp;</td>
									<td width="4%" class="border_bottom"><% if(EnumSaleType.REGULAR.equals(credit.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(credit.getOrderType())){%><Font color="red">A</Font><%}%>&nbsp;</td>
									<td width="19%" class="border_bottom"><%= credit.getDepartment() %>&nbsp;</td>
									<td width="12%" class="border_bottom"><%= credit.getStatus().getName() %>&nbsp;</td>
									<td width="12%" class="border_bottom"><%= EnumComplaintLineMethod.STORE_CREDIT.equals(credit.getMethod())?JspMethods.formatPrice(credit.getOriginalAmount()):"&nbsp;" %>
									<%=(credit.getRefSaleId()!=null && credit.getRefSaleId().length() != 0)?("<a href='/main/order_details.jsp?orderId=" +credit.getRefSaleId() + "'> #" +credit.getRefSaleId()+"</a>"):"" %>
									</td>
									<td width="12%" class="border_bottom"><%= EnumComplaintLineMethod.CASH_BACK.equals(credit.getMethod())? JspMethods.formatPrice(credit.getOriginalAmount()):"&nbsp;" %></td>    
									<td width="11%" class="border_bottom"><span class="log_info"><%= credit.getIssuedBy() %>&nbsp;</span></td>
									<td width="12%" class="border_bottom"><span class="log_info"><%= credit.getApprovedBy() %>&nbsp;</span></td>
								</tr>
							<%
								counter++;
							}
						%>
						</table>
					</div> 
					
		<% } %>			
            

			
                    
    </fd:CustomerCreditHistoryGetterTag>

	</tmpl:put>
		
</tmpl:insert>
