<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import='com.freshdirect.crm.*'%>
<%@ taglib uri='crm' prefix='crm' %>
<%String agentPK = request.getParameter("agent_pk");%>
<crm:GetAgent id="agent" agentId="<%=agentPK%>">
<%CrmAgentInfo info = CrmManager.getInstance().getCSROverview( agent.getPK() );%>
<table width="100%" border="0" cellpadding="0" cellspacing="2" class="user_mgmt_profile">
	<tr valign="top">
		<td width="15%" rowspan="5"><div style="border: 1px solid #000000; background: #CCCCCC; width: 80; height:100; margin-left:2px; margin-right:4px;"></div></td>
		<td width="15%" class="user_mgmt_profile_header">Name:</td>
		<td width="35%"><%=agent.getFirstName()%>&nbsp;<%=agent.getLastName()%> <%--(<span class="user_mgmt_status">logged in</span>)--%></td>
		<td width="15%" class="user_mgmt_profile_header">Assigned:</td>
		<td width="20%"><%= info.getAssigned() %></td>
	</tr>
	<tr valign="top">
		<td class="user_mgmt_profile_header">Username:</td>
		<td><%=agent.getUserId()%></td>
		<td class="user_mgmt_profile_header">Open:</td>
		<td><%= info.getOpen() %></td>
	</tr>
	<tr valign="top">
		<td class="user_mgmt_profile_header">Since:</td>
		<td><%=agent.getCreateDate()%></td>
		<td class="user_mgmt_profile_header">Closed:</td>
		<td><%= info.getClosed() %></td>
	</tr>
	<tr valign="top">
		<td class="user_mgmt_profile_header">Level:</td>
		<td><%=agent.getRole().getName()%></td>
		<td class="user_mgmt_profile_header">Oldest:</td>
		<td><%= info.getOldest()==null ? "" : CCFormatter.formatCaseDate( info.getOldest() ) %></td>
	</tr>
	<%-- tr valign="top">
		<td class="user_mgmt_profile_header">Last Login:</td>
		<td>06.30.2003 9:20 a</td>
		<td class="user_mgmt_profile_header">Last Logout:</td>
		<td>06.29.2003 8:45 p</td>
	</tr --%>
</table>
</crm:GetAgent>