<%@ page import='com.freshdirect.crm.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/user_mgmt.jsp'>

    <tmpl:put name='title' direct='true'>User Management > User Profile: Worklist</tmpl:put>

    	<tmpl:put name='content' direct='true'>
			
			<div class="content" style="height: 75%;">
				<% String selected_agent = request.getParameter("agent_pk"); %>
				<crm:GetAgent id="agent" agentId="<%= selected_agent %>">
					<% 
						String agentId = CrmSecurityManager.getUserName(request);
	            		String agentRole = CrmSecurityManager.getUserRole(request);
                        CrmCaseTemplate template = new CrmCaseTemplate();
                        template.setAssignedAgentPK( agent.getPK() ); 
                        template.setSortBy(request.getParameter("sortBy"));
                        template.setSortOrder(request.getParameter("sortOrder"));
                        template.setStartRecord(Integer.parseInt(NVL.apply(request.getParameter("startRecord"), "0")));
                        template.setEndRecord(Integer.parseInt(NVL.apply(request.getParameter("endRecord"), FDStoreProperties.getCaseListLength(request.getRequestURI().indexOf("case_history.jsp")>-1))));
                    %>
					<crm:FindCases id='cases' template='<%= template %>'>
						<%@ include file="/includes/case_list.jspf"%>
					</crm:FindCases>
				</crm:GetAgent>
			</div>
			
	    </tmpl:put>

</tmpl:insert>