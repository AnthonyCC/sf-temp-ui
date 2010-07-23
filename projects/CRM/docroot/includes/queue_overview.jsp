<%@ taglib uri='logic' prefix='logic' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="home_monitor_header">
	<tr>
		<td width="25%" style="padding-left: 8px;">Queues</td>
		<td align="center" width="25%">Open</td>
		<td align="center" width="25%">Unassigned</td>
		<td width="25%">Oldest</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
</table>
<% List queueOverview = CrmManager.getInstance().getQueueOverview(); %>
<div style="height: 90%; border: 0px solid red; overflow-y: scroll;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="home_monitor_content">
	<logic:iterate id='qi' indexId='i' collection="<%= queueOverview %>" type="com.freshdirect.crm.CrmQueueInfo">
		<tr onClick="document.location='/case_mgmt/index.jsp?action=searchCase&queue=<%= qi.getQueue().getCode() %>&state=OPEN&state=REVW&state=APVD'" style="cursor: pointer;" <%= (i.intValue()%2==0 ? "class=\"home_monitor_odd_row\"" : "")%>>
			<td width="5%" style="padding-left: 8px;"><b><%= qi.getQueue().getCode() %></b></td>
			<td width="20%"><%= qi.getQueue().getName() %></td>
			<td align="center" width="25%"><%= qi.getOpen() %></td>
			<td align="center" width="25%"><%= qi.getUnassigned() %></td>
			<td width="25%"><%= qi.getOldest()==null ? "" : CCFormatter.formatCaseDate( qi.getOldest() ) %></td>
		</tr>
	</logic:iterate>
	</table>
</div>