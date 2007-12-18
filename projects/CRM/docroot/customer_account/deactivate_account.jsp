<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%	String successPage = request.getParameter("successPage");
	if (successPage == null) successPage = "/main/account_details.jsp";
	FDUserI user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDIdentity identity = user.getIdentity();
%>
<fd:DeactivateAccount results="result" successPage="<%= successPage %>">
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Account Details > <%= user.isActive() ? "Deactivate" : "Activate" %> Customer Account</tmpl:put>

<tmpl:put name='content' direct='true'>
<fd:AccountActivity activities='activities'>
	<%	ErpActivityRecord acctOpenActivity = null;
			for (Iterator it = activities.iterator(); it.hasNext(); ) {
				ErpActivityRecord rec = (ErpActivityRecord) it.next();
				if (EnumAccountActivityType.CREATE_ACCOUNT.equals(rec.getActivityType())) {
					acctOpenActivity = rec;
				}
			}
	%>
<div class="sub_nav" style="padding: 0px;">
<table width="100%" cellpadding="5" cellspacing="0" border="0">
<form name="deactivate_acct" method="POST">
<input type="hidden" name="action" value="<%= user.isActive() ? "deactivate" : "activate" %>">
	<tr>
		<td width="33%" class="sub_nav_title"><% if ( user.isActive() ) { %>Deactivate<% } else { %>Activate<% } %> Account &nbsp;&nbsp; <span class="note">* Notes required</span></td>
		<td align="right"><input type="submit" value="<%= user.isActive() ? "DEACTIVATE" : "ACTIVATE" %> ACCOUNT" class="submit"></td>
		<td><A HREF="<%= response.encodeURL("/main/account_details.jsp?custId=" + identity.getFDCustomerPK() ) %>" class="cancel">CANCEL</A></td>
		<td width="33%"  align="right"></td>
	</tr>
</table>
</div>
<div class="content_fixed">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
<tr><td></td><td>Account created: <b><%= acctOpenActivity == null ? "<span class='not_set'>- No date -</span>" : CCFormatter.formatDate(acctOpenActivity.getDate()) %></b> &nbsp;&nbsp; Currently: <b><% if ( user.isActive() ) { %>Active<% } else { %>Deactivated<% } %></b></td></tr>
	<TR VALIGN="TOP">
		<TD WIDTH="1%">&nbsp;</TD>
		<TD><br>Notes:<BR>
		<TEXTAREA name="deactivate_notes" WRAP="virtual" COLS="50" ROWS="10"><%= request.getParameter("deactivate_notes") %></TEXTAREA>
		<br><fd:ErrorHandler result='<%= result %>' name='empty_field' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%= result %>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><br><br>
		</TD>
	</TR>
	</form>
</TABLE>
</div>
</fd:AccountActivity>
</tmpl:put>

</tmpl:insert>
</fd:DeactivateAccount>