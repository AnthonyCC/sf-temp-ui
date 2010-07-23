<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%!
private final static Map COMPARATORS = new HashMap();
static{
    COMPARATORS.put("name", CrmAgentComparator.NAME_COMP);
    COMPARATORS.put("role", CrmAgentComparator.ROLE_COMP);
    COMPARATORS.put("assigned", new ReverseComparator(CrmAgentComparator.ASSIGNED_COMP));
    COMPARATORS.put("open", new ReverseComparator(CrmAgentComparator.OPEN_COMP));
    COMPARATORS.put("closed", new ReverseComparator(CrmAgentComparator.CLOSED_COMP));
    COMPARATORS.put("review", new ReverseComparator(CrmAgentComparator.REVIEW_COMP));
    COMPARATORS.put("oldest", new ReverseComparator(CrmAgentComparator.OLDEST_COMP));
}
%>
<%
List csrOverview = CrmManager.getInstance().getCSROverview();

JspTableSorter sort = new JspTableSorter(request);
Comparator comp = (Comparator)COMPARATORS.get(sort.getSortBy());
if (comp == null) {
    Collections.sort(csrOverview, new ReverseComparator(CrmAgentComparator.ASSIGNED_COMP));
} else {
    Collections.sort(csrOverview, sort.isAscending() ? comp : new ReverseComparator(comp));
}

String page_URI = request.getRequestURI();
String height = "94";

if (page_URI.indexOf("/main/index") > -1 ) {
	height = "12";
}

%>
<crm:GetCurrentAgent id='currentAgent'>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="home_monitor_header">
	<tr align="center">
		<td width="26%" align="left" style="padding-left: 8px;"><a href="?<%= sort.getFieldParams("name") %>" class="home_monitor_header_text">Agents (<%=csrOverview.size()%>)</a></td>
		<td width="8%" align="left"><a href="?<%= sort.getFieldParams("role") %>" class="home_monitor_header_text">Role</a></td>
		<td width="12%"><a href="?<%= sort.getFieldParams("assigned") %>" class="home_monitor_header_text">Assigned</a></td>
		<td width="12%"><a href="?<%= sort.getFieldParams("open") %>" class="home_monitor_header_text">Open</a></td>
		<td width="12%"><a href="?<%= sort.getFieldParams("review") %>" class="home_monitor_header_text">Review</a></td>
		<td width="12%"><a href="?<%= sort.getFieldParams("closed") %>" class="home_monitor_header_text">Closed</a></td>
		<td width="18%"><a href="?<%= sort.getFieldParams("oldest") %>" class="home_monitor_header_text">Oldest</a></td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
</table>
<div style="height: 90%; border: 0px solid red; overflow-y: scroll;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="home_monitor_content">
	<% int i = 0; %>
	<logic:iterate id='ai' collection="<%= csrOverview %>" type="com.freshdirect.crm.CrmAgentInfo">
		<% if (!ai.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE))) {%>
			<% if (ai.isActive()) { %>
				<tr align="center" 
                    <%if (currentAgent.isSupervisor()) {%>
                        onClick="document.location='<% if (currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) {%>/user_mgmt/worklist<%} else if (currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE))) {%>/supervisor/index<%}%>.jsp?agent_pk=<%=ai.getAgentPK().getId()%>&action=searchCase'" style="cursor: pointer;"<%}%> <%= (i%2==0 ? "class=\"home_monitor_odd_row\"" : "")%>>
					<td width="10%" align="left" style="padding-left: 8px;"><b><%= ai.getUserId() %></b></td>
					<td width="16%" align="left"><%=ai.getFirstName()%>&nbsp;<%=ai.getLastName()%></td>
					<td width="8%" align="left"><%= ai.getRole() %></td>
					<td width="12%"><%= ai.getAssigned() %></td>
					<td width="12%"><%= ai.getOpen() %></td>
					<td width="12%"><%= ai.getReview() %></td>
					<td width="12%"><%= ai.getClosed() %></td>
					<td width="18%"><%= ai.getOldest()==null ? "" : CCFormatter.formatCaseDate( ai.getOldest() ) %></td>
				</tr>
			<% 
			i++;
			} 
		} %>
	</logic:iterate>
	</table>
</crm:GetCurrentAgent>
</div>