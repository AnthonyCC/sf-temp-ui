<%@ taglib uri='template' prefix='tmpl' %>
<%String agentPk = request.getParameter("agent_pk");%>
<crm:GetAgent id="agent" agentPk="<%=agentPk%>">
	<tmpl:insert template='/template/user_mgmt.jsp'>
	
	    <tmpl:put name='title' direct='true'>User Management > User Profile: Activity Log</tmpl:put>
	
	    	<tmpl:put name='content' direct='true'>
				
				<div class="content_scroll" style="height: 75%;">Activity Log</div>
				
		    </tmpl:put>
	
	</tmpl:insert>
</crm:GetAgent>