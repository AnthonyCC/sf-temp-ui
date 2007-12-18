<%@ taglib uri='template' prefix='tmpl' %>
<tmpl:insert template='/template/user_mgmt.jsp'>

    <tmpl:put name='title' direct='true'>User Management</tmpl:put>

    	<tmpl:put name='content' direct='true'>
		
			<%@ include file="/includes/agent_monitor.jsp"%>
	    
		</tmpl:put>

</tmpl:insert>