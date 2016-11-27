<%@page import='java.util.*' %>
<%@page import="com.freshdirect.framework.core.PrimaryKey"%>
<%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.UUID"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"%>
<%@ page import="com.freshdirect.fdstore.customer.ejb.EnumCustomerListType"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerListInfo"%>
<%@ page import="com.freshdirect.fdstore.lists.FDCustomerList"%>
<%@ page import="com.freshdirect.fdstore.lists.FDListManager"%>
<%@ page import="com.freshdirect.framework.util.DateUtil"%>
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.lists.FDStandingOrderList"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"%>
<%@ page import="com.freshdirect.customer.ErpAddressModel"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>

<%@ include file="/shared/template/includes/yui.jspf" %>


<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false'/>

<script type="text/javascript">
	function isLocked(soId) {
		
		YAHOO.util.Connect.asyncRequest('GET', '/api/so_api.jsp?action=isLocked&soId='+soId, {
			  success: function(o) { document.getElementById("is_locked_"+o.argument).value = o.responseText;},
			  failure: function(o) {/*failure handler code*/},
			  argument: soId
			});
	}
</script>

<%
Map<String,String> soLockIds = (Map<String,String>) session.getAttribute("soLockIds");
if (soLockIds == null){
	soLockIds = new HashMap<String, String>();
	session.setAttribute("soLockIds",soLockIds);
}

Map<String,String> soLockDates = (Map<String,String>) session.getAttribute("soLockDates");
if (soLockDates == null){
	soLockDates = new HashMap<String, String>();
	session.setAttribute("soLockDates",soLockDates);
}


if ("lock".equals(request.getParameter("operation"))){
	String soPk = request.getParameter("so_pk");
	String lockId = UUID.randomUUID().toString();
	FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soPk));
	boolean success = FDStandingOrdersManager.getInstance().lock(so,lockId);

	String soLockDate = new SimpleDateFormat("HH:mm:ss").format(new Date());

	if (success){
		soLockIds.put(soPk,lockId);
		soLockDates.put(soPk,soLockDate);
	}
%>
	Lock SO "<%=so.getCustomerListName()%>" with lock_id "<%=lockId%>" at <%=soLockDate  %> <font color=<%= success ? "\"green\">successful":"\"red\">failed" %></font>  
<%

} else if ("unlock".equals(request.getParameter("operation"))){
	String soPk = request.getParameter("so_pk");
	String lockId = request.getParameter("lock_id");
	FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soPk));
	boolean success = FDStandingOrdersManager.getInstance().unlock(so,lockId);

	String soLockDate = new SimpleDateFormat("HH:mm:ss").format(new Date());

	if (success){
		soLockIds.remove(soPk);
		soLockDates.remove(soPk);
	}
%>
	Unlock SO "<%=so.getCustomerListName()%>" with lock_id "<%=lockId%>" at <%=soLockDate  %> <font color=<%= success ? "\"green\">successful":"\"red\">failed" %></font>  
<%
}

%>
<br><br>
<fd:ManageStandingOrders id="sorders">
	<table>
	<tr bgcolor="#BBBBBB">
		<td>SO name</td>
		<td>Lock</td>
		<td>Lock time</td>
		<td>Unlock</td>
		<td>Status</td>
	</tr>
	<%
	int i=0; 
	for (FDStandingOrder so : sorders) {
	%>			
		<tr <%=(i++ % 2 == 0) ? "bgcolor=\"#DDDDDD\"" : ""%>>
			<td><%= so.getCustomerListName() %></td>
			<td>
				<form method="post" action="/test/standingorder/lock.jsp">
				<input type="hidden" name="so_pk" value="<%=so.getId()%>">
				<input type="hidden" name="operation" value="lock">
				<input type="submit" value="Lock with random lock id">
				</form>
			</td>
			<td width="100">
				<%=soLockDates.get(so.getId())%>
			</td>
			<td>
				<form method="post" action="/test/standingorder/lock.jsp">
				<input type="hidden" name="so_pk" value="<%=so.getId()%>">
				<input type="submit" value="Unlock with my lock id">
				<input name="lock_id" value="<%=soLockIds.get(so.getId())%>" size="36">
				<input type="hidden" name="operation" value="unlock">
				</form>
			</td>
			<td>
				<a href="#" onclick='isLocked("<%=so.getId()%>")'>isLocked?</a>
				<input id="is_locked_<%=so.getId()%>" value="unknown"/>
			</td>
		<tr>
	<% } %>
	</table>
</fd:ManageStandingOrders>
