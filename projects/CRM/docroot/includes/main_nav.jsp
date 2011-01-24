<%@	page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@	page import="com.freshdirect.crm.CrmAgentRole"%>
<%@	page import='com.freshdirect.fdstore.FDStoreProperties'	%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='java.util.*' %>
<%@	taglib uri='crm' prefix='crm' %>

<% String pageURI =	request.getRequestURI(); %>

<%
	try {
		
		String userId = CrmSecurityManager.getUserName(request);
		String userRole = CrmSecurityManager.getUserRole(request);
		CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
		List linksList = MenuManager.getInstance().getLinksForRole(request);
		if(null == linksList){
	linksList = new ArrayList();	
		}
		//menuGroup.get
%>


<a name="top"></a>
<table width="100%"	cellpadding="6"	cellspacing="0"	border="0" class="main_nav">
	<tr>
		<td	width="75%"	style="padding-left: 0px;">
			<a href="/main/clear_session.jsp" class="<%=pageURI.indexOf("/main/index.jsp") > -1?"main_nav_on":"main_nav_link"%>">Home</a>
			<%-- if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.EXC_CODE))) {	%> 
				<a href="javascript:javascript:popResize('http://reporting','715','940')" class="main_nav_link">Marketing Reports</a>
			<% } --%>
				<%-- if(linksList.contains("user_mgmt_index.jsp")){	%> 
					<a href="/user_mgmt/index.jsp" class="<%=pageURI.indexOf("/user_mgmt/")	> -1?"main_nav_on":"main_nav_link"%>">Manage Users</a>
				<% } --%>
				<% if(linksList.contains("case_mgmt_index.jsp")){	 %> 
					<a href="/case_mgmt/case_mgmt_index.jsp" class="<%=pageURI.indexOf("/case_mgmt/")	> -1?"main_nav_on":"main_nav_link"%>">Manage Cases</a>
				<%} %>
				<% if(linksList.contains("supervisor_index.jsp")){ %> 
					<a href="/supervisor/supervisor_index.jsp<%= request.getParameter("agent_pk") != null ? "?agent_pk=" +	request.getParameter("agent_pk") : ""%>" class="<%=pageURI.indexOf("/supervisor/") > -1?"main_nav_on":"main_nav_link"%>">Supervisor Resources</a>
				<% } %>	
				<% if(linksList.contains("admintools_index.jsp")){ %>	
					<a href="/admintools/admintools_index.jsp"	class="<%=pageURI.indexOf("/admin_tools/") > -1?"main_nav_on":"main_nav_link"%>">Admin Tools</a> 
				<% } %>	
				<% if(linksList.contains("crmLateIssues.jsp")){%>	
				<a href="/transportation/crmLateIssues.jsp?lateLog=true" class="<%=pageURI.indexOf("/transportation/") > -1?"main_nav_on":"main_nav_link"%>">Transportation Ops</a>
				<% } %>
				<% if(linksList.contains("reports_index.jsp")){%>		
				<a href="/reports/reports_index.jsp" class="<%=pageURI.indexOf("/reports/")	> -1?"main_nav_on":"main_nav_link"%>">Reports</a>
				<% } %>
				<% if(linksList.contains("worklist.jsp")){%>
				<a href="/main/worklist.jsp" class="<%=pageURI.indexOf("/main/worklist.jsp") > -1?"main_nav_on":"main_nav_link"%>">Worklist</a>
				<% } %>
				
			
			<%--
				<a href="/main/available_promotions.jsp" class="<%=(pageURI.indexOf("_promotion.jsp") > -1 || pageURI.indexOf("_promotions.jsp") > -1)?"main_nav_on":"main_nav_link"%>">P.(Old)</a> 
			--%>
			<% if(linksList.contains("promo_home.jsp")){ %>
			<a href="/main/promo_home.jsp" class="<%=pageURI.indexOf("/main/promo_home.jsp") > -1 || pageURI.indexOf("/promotion/")	> -1?"main_nav_on":"main_nav_link"%>">Promotions</a>
			<% } %> 
			<!-- <a	href="/main/information.jsp" class="<%=pageURI.indexOf("/main/information.jsp")	> -1?"main_nav_on":"main_nav_link"%>">Maps</a>  -->
			<% if(linksList.contains("giftcard_landing.jsp")){%>
			<a href="/gift_card/giftcard_landing.jsp" class="<%=pageURI.indexOf("/main/information.jsp") > -1?"main_nav_on":"main_nav_link"%>">GiftCard</a>
			<% } %> 
			<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmMainHelpLink() %>','715','940','kbit')" class="<%=pageURI.indexOf("/main/help.jsp")	> -1?"main_nav_on":"main_nav_link"%>">Help</a>
		</td>
		<td	width="25%"	align="right">
			<%=crmRole.getName()%>: <b><%=userId%></b>
		&nbsp;&middot;&nbsp;
		<a href="/main/logout.jsp">Logout</a>
		&nbsp;&middot;&nbsp;
		<a href="javascript:popResize('http://www.freshdirect.com','715','940','freshdirect')"><img	src="/media_stat/crm/images/fd_icon.gif" width="13"	height="14"	border="0" alt="FreshDirect"></a>
		</td>
	</tr>
</table>
<% } catch (Exception e) {
	e.printStackTrace();
}%>

