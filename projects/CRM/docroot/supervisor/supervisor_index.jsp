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
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<link rel="stylesheet" href="/ccassets/css/user_mgmt.css" type="text/css">
<tmpl:insert template='/template/supervisor_resources.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	
    <crm:SearchCaseController/>
		
		
		<% //if (currentAgent.isSupervisor()) { 
			String selected_agent = request.getParameter("agent_pk");
			String userId = CrmSecurityManager.getUserName(request);
			String userRole = CrmSecurityManager.getUserRole(request);
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
            		//CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
            		//CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.DEV_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.QA_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.SEC_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.FIN_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.ETS_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.OPS_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.SCS_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.COS_CODE),
            		CrmAgentRole.getEnum(CrmAgentRole.MOP_CODE)};
			%>
			  <logic:iterate id='role' collection="<%= DISPLAY_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
			        <div class="side_nav_module" style="position: relative; float: left; height:25%; width: <%= "ASV".equals(role.getCode())? "19" : "20"%>%;">
			        <% if(null != agentList && null !=agentList.get(CrmAgentRole.getEnum(role.getCode()))){
			        	List agents =(List)agentList.get(CrmAgentRole.getEnum(role.getCode()));
			        %>
			        	<%--
                            List agents = agentList.getAgents(role);
                            Collections.sort(agents, AGENT_STATUS_COMPARATOR);
                        --%>
				        <div class="side_nav_module_header"><%=role.getCode()%>s <span style="font-weight:normal;">(<%=agents.size()%>)</span></div>
			             <div class="side_nav_module_content">
				         <logic:iterate id='agent' collection="<%= agents %>" type="java.lang.String">
				                <a href="/supervisor/supervisor_index.jsp?agent_pk=<%=agent%>&action=searchCase" class="user_mgmt_agent"><%=agent%></a><br>
			             </logic:iterate>
			             </div>
			           <%} %>
			        </div>
			  </logic:iterate>
			</crm:GetAllAgents>
		
		
	
	</tmpl:put>

</tmpl:insert>
