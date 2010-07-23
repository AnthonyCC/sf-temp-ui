<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Re-Submit Customers</tmpl:put>

<tmpl:put name='content' direct='true'>
	<% 
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
		int customersToDisplay = 0;
	%>
	<jsp:include page="/includes/admintools_nav.jsp" />

	<crm:ResubmitCustomerController id="nsmCustomers" actionName='<%="resubmitCustomer"%>' result="resubmitResult" successPage="<%= request.getRequestURI() %>">
	<form method='POST' name="frmResubmitCustomers">

		<%	if (!resubmitResult.isSuccess() ) {	%>
			<div id="result" class="list_content" style="height:70%;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
				<logic:iterate id="errs" collection="<%= resubmitResult.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
				<tr>
					<td class="border_bottom">&nbsp;</td>
					<td class="text11rbold"><%=errs.getDescription()%></td>
				</tr>
				</logic:iterate>
				</table>
			</div>
		<% } %>
		
		<div class="sub_nav sub_nav_title">
			Resubmit Customers that are in Non-Submitted Mode
		</div>
		<div>
			<% if (!isPost && nsmCustomers!=null && nsmCustomers.size() == 0) { %><strong>There were no Non-Submitted Orders found.</strong><% } %>
			<% if (nsmCustomers.size() > 0) { %>
				&nbsp;<span style="font-weight: normal;">(<%=nsmCustomers.size()%> customers found) &nbsp;&nbsp;&nbsp;
				Amount marked for resubmittal: <input onFocus="blur();" type=text size="6" id="customersSelected" name="customersSelected" readonly value="0" />&nbsp;&nbsp;&nbsp;
				Select: <a href="#" onClick="javascript:$('customersSelected').value=selectNCB('resubmit_customers', 100, true, 'first'); return false;">first 100</a>/<a  href="#" onClick="javascript:$('customersSelected').value=selectNCB('resubmit_customers', 100, true, 'last'); return false;">last 100</a>/<a  href="#" onClick="javascript:$('customersSelected').value=selectNCB('resubmit_customers', 0, true); return false;">all</a> customers&nbsp;&nbsp;
				<a href="#" onClick="javascript:$('customersSelected').value=selectNCB('resubmit_customers', 0, false); return false;">Clear all</a>&nbsp;&nbsp;&nbsp;
				<input type="submit" value="resubmit checked items" />
				</span>
			<% } %>
		</div>

		<% if (nsmCustomers.size() > 0) { %>
			<table width="100%" cellspacing="0" border="0" style="empty-cells: show">
				<tr bgcolor="#333366" class="list_header_text">
					<td align="left">&nbsp;</td>
					<td align="left">User ID</td>
					<td align="left">First Name</td>
					<td align="left">Last Name</td>
				</tr>
			</table>
			<div id="result" class="list_content" style="height:76%;">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_customers">
				<logic:iterate id="customerInfo" collection="<%= nsmCustomers %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="idx">
				<tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
					<td class="border_bottom">&nbsp;</td>
					<td class="border_bottom"><input name="customerId" type="checkbox" onClick="countChecked(this);" value="<%=customerInfo.getIdentity().getErpCustomerPK()%>"><a href="/main/account_details.jsp?erpCustId=<%=customerInfo.getIdentity().getErpCustomerPK()%>"><%=customerInfo.getEmail()%></a>&nbsp;</td>
					<td class="border_bottom"><%=customerInfo.getFirstName()%>&nbsp;</td>
					<td class="border_bottom"><%=customerInfo.getLastName()%>&nbsp;</td>
				</tr>
				</logic:iterate>
				</table>
			</div>
		<% } %>
	</form>
	</crm:ResubmitCustomerController>
</tmpl:put>
</tmpl:insert>
