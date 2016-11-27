<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="com.freshdirect.fdstore.customer.CustomerCreditModel"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Auto Late Delivery Credits</tmpl:put>

<tmpl:put name='content' direct='true'>

<%	List sales = CallCenterServices.getAutoLateDeliveryCredits(); %>

<div class="sub_nav">
<span class="sub_nav_title">Auto Late Delivery Credits </span> 
</div>

<%if(request.getParameter("errString") != null && request.getParameter("errString").length() > 0)  { %>
<br/>
<font color="red"><b>These orders have problems approving credits, please contact Appsupport <br/>
<%= request.getParameter("errString") %>
</b></font>
<% } %>
<div class="" style="height: 80%;">
	<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="1%"></td>
		<td width="25%">Date</td>
		<td width="20%">Status</td>
		<td width="12%">Approved By</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	</table>
</div>

<div class="list_content" style="height: 95%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<%
	java.util.Iterator iter = sales.iterator();
	int count = 0;
	while(iter.hasNext()) {
		CustomerCreditModel ccm = (CustomerCreditModel) iter.next();
		count++;
	
%>
	<tr valign="top" <%= count % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='/supervisor/auto_late_credit_orders.jsp?id=<%=ccm.getId()%>'">
	<td width="1%"></td>	
		<td width="25%"><a class="key" href="/supervisor/auto_late_credit_orders.jsp?id=<%=ccm.getId()%>"><b><%=ccm.getOrderDate()%></b></a></td>
		<td width="20%"><%=  "A".equals(ccm.getStatus())?"Approved":"" %></td>
		<td width="12%"><%=ccm.getApprovedBy()%></td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	<tr class="list_separator" style="padding: 0px;"><td colspan="12"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
<% } %>
	</table>
	</div>
</div>
</tmpl:put>


</tmpl:insert>
