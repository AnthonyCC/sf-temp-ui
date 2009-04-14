<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ taglib uri='crm' prefix='crm' %>
<% String pageURI = request.getRequestURI(); %>
<crm:GetCurrentAgent id="currentAgent">
<a name="top"></a>
<table width="100%" cellpadding="6" cellspacing="0" border="0" class="main_nav">
	<tr>
		<td width="70%" style="padding-left: 0px;">
			<a href="/main/clear_session.jsp" class="<%=pageURI.indexOf("/main/index.jsp") > -1?"main_nav_on":"main_nav_link"%>">Home</a>
            <% if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.EXC_CODE))) { %> 
                <a href="javascript:javascript:popResize('http://reporting','715','940')" class="main_nav_link">Marketing Reports</a>
            <% } %>
            <% if(!currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)) && !currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.EXC_CODE))) { %>
                <% if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) { %> 
                    <a href="/user_mgmt/index.jsp" class="<%=pageURI.indexOf("/user_mgmt/") > -1?"main_nav_on":"main_nav_link"%>">Manage Users</a>
                <% } %>
                <% if (currentAgent.isSupervisor() || currentAgent.isMonitor()) { %> 
                    <a href="/case_mgmt/index.jsp" class="<%=pageURI.indexOf("/case_mgmt/") > -1?"main_nav_on":"main_nav_link"%>">Manage Cases</a>
                    <% if (!currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE))) { %> 
                        <a href="/supervisor/index.jsp<%= request.getParameter("agent_pk") != null ? "?agent_pk=" + request.getParameter("agent_pk") : ""%>" class="<%=pageURI.indexOf("/supervisor/") > -1?"main_nav_on":"main_nav_link"%>">Supervisor Resources</a>
                    <% } %> 
                    <% if (currentAgent.isAdmin()) { %> 
			<a href="/admintools/index.jsp" class="<%=pageURI.indexOf("/admin_tools/") > -1?"main_nav_on":"main_nav_link"%>">Admin Tools</a> 
                    <% } %> 
                    <a href="/transportation/crmLateIssues.jsp?lateLog=true" class="<%=pageURI.indexOf("/transportation/") > -1?"main_nav_on":"main_nav_link"%>">Transportation Logs</a> 
                    <a href="/reports/index.jsp" class="<%=pageURI.indexOf("/reports/") > -1?"main_nav_on":"main_nav_link"%>">Reports</a>
                <% } %> 
                <a href="/main/worklist.jsp" class="<%=pageURI.indexOf("/main/worklist.jsp") > -1?"main_nav_on":"main_nav_link"%>">Worklist</a>
            <% } %> 
            <a href="/main/available_promotions.jsp" class="<%=pageURI.indexOf("/main/available_promotions.jsp") > -1 || pageURI.indexOf("/promotion/") > -1?"main_nav_on":"main_nav_link"%>">Promotions</a> 
            <a href="/main/information.jsp" class="<%=pageURI.indexOf("/main/information.jsp") > -1?"main_nav_on":"main_nav_link"%>">Maps</a> 
            <a href="javascript:popResize('/kbit/index.jsp','715','940','kbit')" class="<%=pageURI.indexOf("/main/help.jsp") > -1?"main_nav_on":"main_nav_link"%>">Help</a>
		</td>
		<td width="30%" align="right">
			<%=currentAgent.getRole().getName()%>: <b><%=currentAgent.getUserId()%></b>
		&nbsp;&middot;&nbsp;
		<a href="/main/logout.jsp">Logout</a>
		&nbsp;&middot;&nbsp;
		<a href="javascript:popResize('http://www.freshdirect.com','715','940','freshdirect')"><img src="/media_stat/crm/images/fd_icon.gif" width="13" height="14" border="0" alt="FreshDirect"></a>
		</td>
	</tr>
</table>
</crm:GetCurrentAgent>