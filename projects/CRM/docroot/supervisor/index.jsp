<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<link rel="stylesheet" href="/ccassets/css/user_mgmt.css" type="text/css">
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	<crm:GetCurrentAgent id="currentAgent">
    <crm:SearchCaseController/>
		<jsp:include page="/includes/supervisor_nav.jsp" />
		
		<% if (currentAgent.isSupervisor()) { 
			String selected_agent = request.getParameter("agent_pk");
		%>
		<div class="sub_nav"><span class="sub_nav_title">View Agent's Worklist: </span> <%=(selected_agent==null || "".equals(selected_agent))?"Select an agent below":""%></div>
		
		<crm:GetAllAgents id="agentList">
    <%!
	final static Comparator AGENT_STATUS_COMPARATOR = new Comparator() {
		public final int compare(Object o1, Object o2) {
			CrmAgentModel a1 = (CrmAgentModel)o1;
			CrmAgentModel a2 = (CrmAgentModel)o2;
			if(a1 == null || a2 == null) {
				return 0;
			}
			if((a1.isActive() && a2.isActive()) || (!a1.isActive() && !a2.isActive())) {
				return a1.getFirstName().compareTo(a2.getFirstName());
			}
			if(a1.isActive()) {
				return -1;
			}
			if(a2.isActive()){
				return 1;
			}
			return 0;	
		}
	};
    %>


            <%
			  CrmAgentRole[] DISPLAY_ROLES = {
			  CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
			  CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
			  CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
			  CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
			  CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
			  CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE) };
			%>
			  <logic:iterate id='role' collection="<%= DISPLAY_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
			        <div class="side_nav_module" style="position: relative; float: left; height:25%; width: <%= "ASV".equals(role.getCode())? "19" : "20"%>%;">
			        	<%
                            List agents = agentList.getAgents(role);
                            Collections.sort(agents, AGENT_STATUS_COMPARATOR);
                        %>
				        <div class="side_nav_module_header"><%=role.getCode()%>s <span style="font-weight:normal;">(<%=agents.size()%>)</span></div>
			             <div class="side_nav_module_content">
				         <logic:iterate id='agent' collection="<%= agents %>" type="com.freshdirect.crm.CrmAgentModel">
				                <a href="/supervisor/index.jsp?agent_pk=<%=agent.getPK().getId()%>&action=searchCase" class="user_mgmt_agent<%=agent.isActive()?"":"_inactive"%>"><%=agent.getFirstName()%>&nbsp;<%=agent.getLastName()%></a><br>
			             </logic:iterate>
			             </div>
			        </div>
			  </logic:iterate>
			</crm:GetAllAgents>
		
		<br clear="all">
		
			<%
			if (selected_agent!=null && !"".equals(selected_agent)) {%>
			<crm:GetAgent id="agent" agentId="<%= selected_agent %>">
				<% //CrmCaseTemplate template = new CrmCaseTemplate();
                CrmCaseTemplate template = CrmSession.getSearchTemplate(session);
	    		template.setAssignedAgentPK( agent.getPK() ); %>
				<crm:FindCases id='cases' template='<%= template %>'>
 				<div class="content" style="height: 40%;">
					<%@ include file="/includes/case_list.jspf"%>
				</div>
				</crm:FindCases>
			</crm:GetAgent>
			<% } %>
		<br clear="all">
		<% } %>
	</crm:GetCurrentAgent>	
	
	</tmpl:put>

</tmpl:insert>
