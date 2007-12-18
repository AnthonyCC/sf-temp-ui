<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>

<div class="user_mgmt_nav_link">
    <i>Go to:</i> 
    <a href="#GUE" class="side_nav_link">GUE </a> &middot; 
    <a href="#EXC" class="side_nav_link">EXC </a> &middot; 
    <a href="#CSR" class="side_nav_link">CSR </a> &middot; 
    <a href="#TRN" class="side_nav_link">TRN </a> &middot; 
    <a href="#ASV" class="side_nav_link">ASV </a> &middot; 
    <a href="#SUP" class="side_nav_link">SUP </a> &middot; 
    <a href="#ADM" class="side_nav_link">ADM </a> &middot; 
    <a href="#CSRH" class="side_nav_link">CSRH </a>
</div>
<crm:GetAllAgents id="agentList" useCache="false">
<%

	String agentLink = "account_setting.jsp"; 
		if (request.getRequestURI().indexOf("/user_mgmt/worklist.jsp") > -1) {
			agentLink = "worklist.jsp"; 
		} 
	
  CrmAgentRole[] DISPLAY_ROLES = {
  CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.EXC_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
  CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE)};
  
  
%>
<div class="side_nav_module_content" style="height: 88%; padding-top: 8px;">
  <logic:iterate id='role' collection="<%= DISPLAY_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
        <%List agents = agentList.getAgents(role);%>
        <a name="<%=role.getCode()%>"><div class="side_nav_module_header"><%=role.getName()%><%="TRN".equals(role.getCode())?"er":""%>s <span style="font-weight:normal;">(<%=agents.size()%>)</span></div></a>
             
	         <% 
			 StringBuffer activeAgent = new StringBuffer(); 
			 StringBuffer inactiveAgent = new StringBuffer(); 
			 String selectedAgent = request.getParameter("agent_pk");
			 %>
			 <logic:iterate id='agent' collection="<%= agents %>" type="com.freshdirect.crm.CrmAgentModel" indexId="i">
				 <% if (agent.isActive()) {
				 	activeAgent.append("<a name=\"");
					activeAgent.append(agent.getPK().getId());
					activeAgent.append("\"></a><div>");
				 	activeAgent.append("<a href=\"");
					activeAgent.append(agentLink);
					activeAgent.append("?agent_pk=");
					activeAgent.append(agent.getPK().getId());
                    activeAgent.append("&action=searchCase");
					activeAgent.append("#"+agent.getPK().getId());
					activeAgent.append("\"class=\"user_mgmt_agent");
					if (agent.getPK().getId().equals(selectedAgent)) {
						activeAgent.append("_on");
					}
					activeAgent.append("\">");
					activeAgent.append(agent.getFirstName());
					activeAgent.append("&nbsp;");
					activeAgent.append(agent.getLastName());
					if (agent.getPK().getId().equals(selectedAgent)) {
						activeAgent.append(" &raquo;");
					}
					activeAgent.append("</a>");
					activeAgent.append("</div>");
				 } else {
				 	inactiveAgent.append("<a name=\"");
					inactiveAgent.append(agent.getPK().getId());
					inactiveAgent.append("\"><div>");
				 	inactiveAgent.append("<a href=\"");
					inactiveAgent.append(agentLink);
					inactiveAgent.append("?agent_pk=");
					inactiveAgent.append(agent.getPK().getId());
                    inactiveAgent.append("&action=searchCase");
					inactiveAgent.append("#"+agent.getPK().getId());
					if (agent.getPK().getId().equals(selectedAgent)) {
						inactiveAgent.append("\"class=\"user_mgmt_agent_on\">");
					} else {
						inactiveAgent.append("\"class=\"user_mgmt_agent_inactive\">");
					}
					inactiveAgent.append(agent.getFirstName());
					inactiveAgent.append("&nbsp;");
					inactiveAgent.append(agent.getLastName());
					if (agent.getPK().getId().equals(selectedAgent)) {
						inactiveAgent.append(" &raquo;");
					}
					inactiveAgent.append("</a>");
					inactiveAgent.append("</div>");
				 } %>
             </logic:iterate>
			 <%=activeAgent.toString()%>
			 <%=inactiveAgent.toString()%>
			 <br clear="all">
  </logic:iterate>
</div>
<div class="user_mgmt_nav_link"><a href="new_user.jsp" class="side_nav_link">New User ></a></div>
</crm:GetAllAgents>