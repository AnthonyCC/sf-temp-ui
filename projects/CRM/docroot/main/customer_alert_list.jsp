<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.*" %>
<%@ page import="com.freshdirect.webapp.crm.security.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>

<% 
SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
List customerAlerts = FDCustomerManager.getAlerts(identity.getErpCustomerPK());
boolean showAddAlertLink = false;
for (Iterator iter = EnumAlertType.iterator(); iter.hasNext();) {
	EnumAlertType alertType = (EnumAlertType) iter.next();
	boolean found = false;
	for (Iterator iter2 = customerAlerts.iterator(); iter2.hasNext();) {
		ErpCustomerAlertModel customerAlert = (ErpCustomerAlertModel) iter2.next();
		if (alertType.getName().equalsIgnoreCase(customerAlert.getAlertType())) {
			found = true;
		}
	}
	if (!found) showAddAlertLink = true;
    
}
    boolean isSupervisor=false;    
    boolean isCsr=false;
    CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);    
    //isSupervisor=agentModel.isSupervisor() || agentModel.isAdmin();        
    //isCsr=agentModel.isCSR();
%>
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Customer Alert List</tmpl:put>
<tmpl:put name='content' direct='true'>
	<div class="sub_nav">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">
	<tr><td width="80%"><span class="sub_nav_title">Customer Alert List</span> </div></td>
	<td width="20%"align="right"><% if (CrmSecurityManager.hasAccessToPage(agentModel.getRole().getLdapRoleName(),"place_alert.jsp")) {%> <a href="/customer_account/place_alert.jsp?action=place_alert">Add An Alert &raquo;</a> <% } else { %>&nbsp;<% } %></td>
	</tr>
	</table>
	</div>	
<div class="list_header">
    <table border="0" cellspacing="2" cellpadding="0" width="100%" class="list_header_text">
        <tr>
            <td width="2%">&nbsp;</td>
            <td width="25%">Alert</td>
            <td width="15%">Created On</td>
            <td width="10%">Created By</td>
            <td width="35%">Note</td>
            <td width="12%">&nbsp;</td>
            <td><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
        </tr>
    </table>
</div>
<div class="list_content">
<table border="0" cellspacing="0" cellpadding="2" width='100%'>
<%
    int counter = 0;
    for(Iterator i = customerAlerts.iterator(); i.hasNext();){
        ErpCustomerAlertModel customerAlert = (ErpCustomerAlertModel) i.next();
%>
            <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
                <td width="2%">&nbsp;</td>
                <td width="25%"><%=customerAlert.getAlertType()%></td>
                <td width="15%"><%=DATE_FORMATTER.format(customerAlert.getCreateDate())%></td>
                <td width="10%"><%=customerAlert.getCreateUserId()%></td>
                <td width="35%"><%=customerAlert.getNote()%></td>
                <td width="12%">
                <% if (CrmSecurityManager.hasAccessToPage(agentModel.getRole().getLdapRoleName(),"place_alert.jsp")) { %>
                <a href="/customer_account/place_alert.jsp?action=remove_alert&customer_alert_id=<%=customerAlert.getPK().getId()%>&alert_type=<%=customerAlert.getAlertType()%>">Remove Alert &raquo;</a>                
                <%  } %>
                </td>
	            <td><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
            </tr>
            <tr class="list_separator" style="padding: 0px;">
                <td colspan="11"></td>
            </tr>
<%
    }
%>
</table>
</div>    
</tmpl:put>
</tmpl:insert>
