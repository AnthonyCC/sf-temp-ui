<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="java.util.List"
%><%@ page import="java.util.Iterator"
%><%@ page import="com.freshdirect.crm.CrmAgentModel"
%><%@ page import="com.freshdirect.crm.CrmCaseModel"
%><%@ page import="com.freshdirect.crm.CrmCaseAction"
%><%@ page import="com.freshdirect.crm.CrmManager"
%><%@ page import="com.freshdirect.webapp.crm.util.CustomerSummaryUtil"
%><%@ page import="com.freshdirect.webapp.util.CCFormatter"
%><%@ taglib uri="crm" prefix="crm" %>
<crm:GetFDUser id="user">
<%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		CustomerSummaryUtil util = new CustomerSummaryUtil(request, user);
		
		String caseId = request.getParameter("case");
		List actions = util.getRecentActions(caseId, 5);
		
		int k = util.getNumberOfActions(caseId);

%><table cellspacing="0" cellpadding="0">
	<tr class="case-row-hdr">
		<td>&nbsp;</td>
		<td style="text-align: left; padding-left: 0.5em">Actions</td>
		<td style="text-align: right;">
			<button class="case-edit-button" onclick="window.location.href='/case/case.jsp?case=<%= caseId %>&erpCustId=<%= user.getIdentity().getErpCustomerPK() %>'; return false;">EDIT FIELDS</button>
		</td>
	</tr>
<%
		for (Iterator it=actions.iterator(); it.hasNext();) {
			CrmCaseAction action = (CrmCaseAction) it.next();
			
			CrmAgentModel agent = CrmManager.getInstance().getAgentByPk( action.getAgentPK().getId() );
%>
	<tr class="case-row">
		<td>#<%= k %></td>
		<td>
			<div><%= CCFormatter.formatCaseDate(action.getTimestamp()) %></div>
			<div style="color: red; text-decoration: underline;"><%= agent.getUserId() %></div>
		</td>
		<td>
			<div style="font-weight: bold;"><%= action.getType().getName() %></div>
			<div><%= action.getNote() %></div>
		</td>
	</tr>
<%
			k--;
		}
%>
</table>
<%
	}
%>
</crm:GetFDUser>
