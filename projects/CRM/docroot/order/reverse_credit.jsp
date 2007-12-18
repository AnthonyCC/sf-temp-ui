<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.customer.FDComplaintUtil"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
String orderNumber = request.getParameter("orderId");
%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Order <%= orderNumber %> Reverse Credit</tmpl:put>

<tmpl:put name='content' direct='true'>
<fd:ReverseCreditController result="result" saleId="<%=orderNumber%>" result="result">
<%
    FDOrderI order = CrmSession.getOrder(session, orderNumber);
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDCustomerCreditHistoryModel creditHistory = FDCustomerManager.getCreditHistory(new FDIdentity(order.getCustomerId()));
%>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>

<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
    <logic:iterate id="errs" collection="<%= result.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
    <tr>
        <td class="border_bottom">&nbsp;</td>
        <td class="border_bottom"><font color="red"><%=errs.getDescription()%></font></td>
    </tr>
    </logic:iterate>
</table>
<div class="list_header">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" class="list_header_text">
<TR>
			<TD width="20%">Description</TD>
			<TD width="13%">Created By</TD>
			<TD width="11%">Create Date</TD>
			<TD width="14%">Approved By</TD>
			<TD width="11%">Approved Date</TD>
			<TD width="10%">Status</TD>
			<TD width="12%">Amount</TD>
			<td width="9%"></td>
			<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
		</TR>
</table>
</div>

<div class="list_content" style="height: 65%;">
    <script language="javascript">
            function confirmReverse(thisForm) {
                var doCancel = confirm ("Are you sure you want to reverse this credit?");
                if (doCancel == true) {
                    thisForm.submit();
                }
            }
    </script>		
		<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true" retrievePending="false" retrieveRejected="false">
		<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="list_content_text">
			<logic:iterate id="complaint" collection="<%=complaints%>" type="com.freshdirect.customer.ErpComplaintModel">
			<form method="POST" action="/order/reverse_credit.jsp?orderId=<%=orderNumber%>" name="reverseCredit_<%=complaint.getPK().getId()%>">
				<input type="hidden" name="orderNum" value="<%=orderNumber%>">
				<input type="hidden" name="action" value="reverse_credit">
				<input type="hidden" name="complaint_id" value="<%=complaint.getPK().getId()%>">
				<tr>
					<td width="20%"><%=complaint.getDescription()%></td>
					<td width="13%"><%=complaint.getCreatedBy()%></td>
					<td width="11%"><%=CCFormatter.formatDate(complaint.getCreateDate())%></td>
					<td width="14%"><%=complaint.getApprovedBy()%></td>
					<td width="11%"><%=CCFormatter.formatDate(complaint.getApprovedDate())%></td>
					<td width="10%"><%=complaint.getStatus()%></td>
					<td width="12%"><%=CCFormatter.formatCurrency(complaint.getAmount())%></td>
					<td width="9%"><%if(FDComplaintUtil.isComplaintReversable(complaint, creditHistory.getCreditsForComplaint(complaint.getPK().getId()))){%><input type="button" onClick="javascript:confirmReverse(reverseCredit_<%=complaint.getPK().getId()%>)" class="submit" value="REVERSE"><%}else{%>&nbsp;<%}%>
					</td>
				</tr>
				<tr><td colspan="8" class="list_separator" style="padding: 0px;"><img src="images/clear.gif" width="1" height="1"></td></tr>
			</form>
			</logic:iterate>	
		</TABLE>	
		</fd:ComplaintGrabber>
</div>
</fd:ReverseCreditController>
</tmpl:put>

</tmpl:insert>
