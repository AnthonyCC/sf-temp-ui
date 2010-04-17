<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>
<crm:GetFDUser id="user">
<fd:AccountActivity activities='activities'>
<%!
private final static Map<String,Comparator<ErpActivityRecord>> ACTIVITY_COMPARATORS = new HashMap<String,Comparator<ErpActivityRecord>>();
static {
	ACTIVITY_COMPARATORS.put("date", ErpActivityRecord.COMP_DATE);
	ACTIVITY_COMPARATORS.put("activity", ErpActivityRecord.COMP_ACTIVITY);
	ACTIVITY_COMPARATORS.put("initiator", ErpActivityRecord.COMP_INITIATOR);
	ACTIVITY_COMPARATORS.put("source", ErpActivityRecord.COMP_SOURCE);
}
%>
<%
JspTableSorter sort = new JspTableSorter(request);

Comparator<ErpActivityRecord> comp = ACTIVITY_COMPARATORS.get(sort.getSortBy());
if (comp == null) {
	Collections.sort(activities, new ReverseComparator(ErpActivityRecord.COMP_DATE));
} else {
	if (comp.equals(ErpActivityRecord.COMP_DATE)) {
		Collections.sort(activities, sort.isAscending() ? new ReverseComparator(comp) : comp);
	} else {
		Collections.sort(activities, sort.isAscending() ? comp : new ReverseComparator(comp));
	}
}
%>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Activity Log</tmpl:put>
	
		<tmpl:put name='content' direct='true'>
		
		<div class="list_header">
		<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
			<tr>
				<td width="1%"></td>
				<td width="18%"><a href="?<%= sort.getFieldParams("date") %>" class="list_header_text">Date | Time</a></td>
				<td width="52%"><a href="?<%= sort.getFieldParams("activity") %>" class="list_header_text">Action</a></td>
				<td width="14%"><a href="?<%= sort.getFieldParams("initiator") %>" class="list_header_text">By</a></td>
				<td width="14%"><a href="?<%= sort.getFieldParams("source") %>" class="list_header_text">Source</a></td>
				<td><img src="/media_stat/crm/images/clear.gif" width="8" height="1"></td>
			</tr>
		</table>
		</div>

		<div class="list_content">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
		<% if (activities.size() > 0) {%>
			<logic:iterate id="activity" collection="<%= activities %>" type="com.freshdirect.customer.ErpActivityRecord"> 
			<tr valign="top"">
				<td width="1%"></td>
				<td width="18%"><%= CCFormatter.formatDateTime(activity.getDate()) %></td>
				<td width="52%"><b><%= activity.getActivityType().getName() %></b><%= (activity.getNote() != null && !"".equals(activity.getNote())) ? " - " : "" %><%= activity.getNote() %></td>
				<td width="14%"><%= activity.getInitiator() %></td>
				<td width="14%"><%= activity.getSource().getName() %></td>
			</tr>
			<tr class="list_separator" style="padding: 0px;"><td colspan="5"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
			</logic:iterate>
		<% } else { %>
			<tr>
				<td></td>
				<td colspan="5"><br><i>No activity logged.</i></td>
				<td></td>
			</tr>
		<% } %>
		</table>
		</div>

	</tmpl:put>

</tmpl:insert>
</fd:AccountActivity>
</crm:GetFDUser>