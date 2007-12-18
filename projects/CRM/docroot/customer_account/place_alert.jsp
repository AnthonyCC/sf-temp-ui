<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.util.JspTableSorter" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%	
	String successPage = request.getParameter("successPage");
	if (successPage == null) successPage = "/main/customer_alert_list.jsp";

	String action = NVL.apply(request.getParameter("action"), "");	
	
	FDUserI user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDIdentity identity = user.getIdentity();		
	
	String alertType = NVL.apply(request.getParameter("alert_type"), "");	
	String customerAlertId = NVL.apply(request.getParameter("customer_alert_id"), "");	
	
%>

<fd:PlaceAlert results="result" successPage="<%= successPage %>">

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Account Details > Remove/Place Customer Alert</tmpl:put>

<fd:AccountActivity activities='activities'>
	<%
	
		// get the create account activity 
		ErpActivityRecord acctOpenActivity = null;
		for (Iterator it = activities.iterator(); it.hasNext(); ) {
			ErpActivityRecord rec = (ErpActivityRecord) it.next();
			if (EnumAccountActivityType.CREATE_ACCOUNT.equals(rec.getActivityType())) {
				acctOpenActivity = rec;
			}
		}
	%>

<tmpl:put name='content' direct='true'>
	
<div class="sub_nav" style="padding: 0px;">
<table width="100%" cellpadding="5" cellspacing="0" border="0">
<form name="place_alert" method="POST">
<input type="hidden" name="action" value="<%=action%>">
	<tr>
		<td width="33%" class="sub_nav_title"><% if ( "remove_alert".equalsIgnoreCase(action) ) {%>Remove<% } else {%>Place<% }%> Alert &nbsp;&nbsp; <span class="note">* Notes required</span></td>
		<td align="right"><input type="submit" value="<%="remove_alert".equalsIgnoreCase(action) ? "REMOVE" : "PLACE" %> ALERT" class="submit"></td>
		<td><A HREF="<%= response.encodeURL("/main/customer_alert_list.jsp") %>" class="cancel">CANCEL</A></td>
		<td width="33%"  align="right"></td>
	</tr>
</table>
</div>
<div style="background-color:#FFF;">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<tr><td></td><td>Account created: <b><%= acctOpenActivity == null ? "<span class='not_set'>- No date -</span>" : CCFormatter.formatDate(acctOpenActivity.getDate()) %></b> &nbsp;&nbsp;<b><%=("remove_alert".equalsIgnoreCase(action)) ? "On Alert" : ""%></b></td></tr>
	<TR VALIGN="TOP">
		<TD WIDTH="1%">&nbsp;</TD>
	<% if ( "place_alert".equalsIgnoreCase(action) ) {%>
		<TD><br>Type:&nbsp;
		<SELECT name="alert_type" class="pulldown">
			<OPTION></OPTION>
		<%
			for(Iterator iter = EnumAlertType.iterator(); iter.hasNext(); ) {
		        EnumAlertType enumAlertType = (EnumAlertType) iter.next();		        
		        // add to drop down box only alerts that customer doesn't already have
				if (!FDCustomerManager.isOnAlert(identity.getErpCustomerPK(), enumAlertType.getName())) {
		        %>
				<OPTION <%= (enumAlertType.getName().equalsIgnoreCase(alertType) ? "SELECTED" : "") %> value="<%=enumAlertType.getName()%>"><%=enumAlertType.getDescription()%></option>
			  <%} %>
		<% 	} %>
		</SELECT>&nbsp;<fd:ErrorHandler result="<%= result %>" name="alert_type" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>			
		<% } else { %>
			<td><br>Type:&nbsp;<input type="text" name="alert_type" class="input" style="width: 300px;" readonly value="<%=alertType%>"></td>
	<%  } %>
	</TR>
	<TR VALIGN="TOP">
		<TD WIDTH="1%">&nbsp;</TD>
		<TD><br>Notes:<BR>
		<TEXTAREA name="alert_notes" WRAP="virtual" COLS="50" ROWS="10"><%=request.getParameter("alert_notes")%></TEXTAREA>
		<br><fd:ErrorHandler result='<%= result %>' name='alert_notes' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%= result %>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><br><br>
		</TD>
	</TR>
	<input type="hidden" name="customer_alert_id"value="<%=customerAlertId%>">	
	</form>
</TABLE>
</div>
</tmpl:put>
</fd:AccountActivity>
</tmpl:insert>
</fd:PlaceAlert>