<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.crm.*' %>
<%
List queueOverview = CrmManager.getInstance().getQueueOverview();
%>
<crm:GetCurrentAgent id="currentAgent">
<div class="side_nav_module" style="border-top: none;">
	<div class="side_nav_module_header">Queues <span style="font-weight: normal;">(# open)</span></div>
	<logic:iterate id='qi' collection="<%= queueOverview %>" type="com.freshdirect.crm.CrmQueueInfo">
		<div class="side_nav_module_content" style="height: auto;">
		<% if (currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE))|| currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRNSP_CODE))) { //TRN %>
			<% if (CrmCaseQueue.isTrnQueue(qi.getQueue().getCode())) { %>
			<a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_OPEN %>" class="case_mgmt_module_content"><b><%= qi.getQueue().getName() %></b> (<%= qi.getOpen() %>)</a><!--  <br>
			&nbsp;&nbsp;<a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_REVIEW %>" class="case_mgmt_module_note">In Review</a> | <a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_APPROVED %>" class="case_mgmt_module_note">Approved</a>-->
			<% } %>		
		<% } else { %>
			<a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_OPEN %>" class="case_mgmt_module_content"><b><%= qi.getQueue().getName() %></b> (<%= qi.getOpen() %>)</a><!-- <br>
			&nbsp;&nbsp;<a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_REVIEW %>" class="case_mgmt_module_note">In Review</a> | <a href="?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=<%= CrmCaseState.CODE_APPROVED %>" class="case_mgmt_module_note">Approved</a> -->
		<% } %>
		</div>
	</logic:iterate>
</div>

</crm:GetCurrentAgent>