<%@	page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@	page import="com.freshdirect.crm.CrmAgentRole"%>
<%@	page import='com.freshdirect.fdstore.FDStoreProperties'	%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='java.util.*' %>
<%@	taglib uri='crm' prefix='crm' %>
<% String pageURI =	request.getRequestURI(); %>
<crm:GetCurrentAgent id="currentAgent">
<%
CrmAgentRole crmRole = currentAgent.getRole();
List linksList = MenuManager.getInstance().getLinksForRole(request);
%>
<a name="top"></a>
<table width="100%"	cellpadding="6"	cellspacing="0"	border="0" class="main_nav">
	<tr>
		<td	width="75%"	style="padding-left: 0px;">
			<% if(!crmRole.getCode().equals("HR")) { %>
			<a href="/main/clear_session.jsp" class="<%=pageURI.indexOf("/main/main_index.jsp") > -1?"main_nav_on":"main_nav_link"%>">Home</a>
			<% } %>
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
				<a href="/transportation/VSStatusLog.jsp" class="<%=pageURI.indexOf("/transportation/") > -1?"main_nav_on":"main_nav_link"%>">Transportation Ops</a>
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
			<a href="/main/promo_home.jsp" class="<%=pageURI.indexOf("/main/promo_home.jsp") > -1 || (pageURI.indexOf("/promotion/")	> -1 &&  pageURI.indexOf("/promo_ws")	<= -1 && pageURI.indexOf("dow_admin") == -1)?"main_nav_on":"main_nav_link"%>">Promotions</a>
			<% } %> 
			<% if(linksList.contains("promo_ws_view.jsp")){ %>
			<a href="/promotion/promo_ws_view.jsp" class="<%=pageURI.indexOf("/promotion/promo_ws_view.jsp") > -1 || pageURI.indexOf("/promo_ws")	> -1 || pageURI.indexOf("dow_admin") != -1?"main_nav_on":"main_nav_link"%>">WS Promotions</a>
			<% } %>
			<!-- <a	href="/main/information.jsp" class="<%=pageURI.indexOf("/main/information.jsp")	> -1?"main_nav_on":"main_nav_link"%>">Maps</a>  -->
			<% if(linksList.contains("giftcard_landing.jsp")){%>
			<a href="/gift_card/giftcard_landing.jsp" class="<%=pageURI.indexOf("/main/information.jsp") > -1?"main_nav_on":"main_nav_link"%>">GiftCard</a>
			<% } %> 
			<% if(linksList.contains("crm_standing_orders.jsp")){%>
			<a href="/main/crm_standing_orders.jsp" class="<%=pageURI.indexOf("/main/crm_standing_orders.jsp") > -1?"main_nav_on":"main_nav_link"%>">Standing Orders</a>
			<% } %>
			<% if(linksList.contains("ppicks_email_products.jsp")){%>
			<a href="/main/ppicks_email_products.jsp" class="<%=pageURI.indexOf("/main/ppicks_email_products.jsp") > -1?"main_nav_on":"main_nav_link"%>">Product Promotions</a>
			<% } %> 
			<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmMainHelpLink() %>','715','940','kbit')" class="<%=pageURI.indexOf("/main/help.jsp")	> -1?"main_nav_on":"main_nav_link"%>">Help</a>
		</td>
		<td	width="16%"	align="right">
			<%=currentAgent.getRole().getName()%>: <b><%=currentAgent.getLdapId()%></b></td>
		<td	width="7%"	align="right">
		<a href="/main/logout.jsp">Logout</a>
		&middot;
		<a href="javascript:popResize('http://www.freshdirect.com','715','940','freshdirect')"><img	src="/media_stat/crm/images/fd_icon.gif" width="13"	height="14"	border="0" alt="FreshDirect"></a>
		</td>
	</tr>
</table>
</crm:GetCurrentAgent>