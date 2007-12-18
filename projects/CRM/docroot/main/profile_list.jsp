<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.referral.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri="crm" prefix="crm" %>

<% 
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
FDCustomerModel customer = user.getFDCustomer();
ProfileModel profile = customer.getProfile();
Map profileAttributes = profile.getAttributes();
Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
%>
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Profile List</tmpl:put>
<tmpl:put name='content' direct='true'>
<crm:GetCurrentAgent id="currentAgent">
	<div class="sub_nav">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">
	<tr><td width="80%"><span class="sub_nav_title">Profile List</span> </div></td>
	<% if (currentAgent.isSupervisor()) { %><td width="20%"align="right"><a href="/customer_account/set_profile_attr.jsp">Add a Profile Attribute &raquo;</a></td><% } %>
	</tr>
	</table>
	</div>	
<script language="JavaScript">
	function removeProfileAttribute(formName) {
		var doDelete = confirm ("Are you sure you want to delete this Profile Attribute?");
		if (doDelete == true) {
			form = document.forms[formName];
			form.submit();
		}		
	}
</script>
<div class="list_header">
    <table border="0" cellspacing="2" cellpadding="0" width="100%" class="list_header_text">
        <tr>
            <td width="2%">&nbsp;</td>
            <td width="25%">Name</td>
            <td width="25%">Value</td>
            <td width="10%">Category</td>
            <td width="25%">Description</td>
            <td width="12%">&nbsp;</td>
            <td><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
        </tr>
    </table>
</div>

<div class="list_content">
<table border="0" cellspacing="0" cellpadding="2" width='100%'>
<%
    int counter = 0;
    for(Iterator i = profileAttributes.keySet().iterator(); i.hasNext();){
        String name = (String) i.next();
        String value = (String) profileAttributes.get(name);        
        ProfileAttributeName profileAttributeName = (ProfileAttributeName) profileAttributeNames.get(name);	        
        boolean isEditable = currentAgent.isSupervisor() && (profileAttributeName != null && profileAttributeName.getIsEditable());
%>
            <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
                <td width="2%">&nbsp;</td>
                <td width="25%"><% if(isEditable) { %><a href="/customer_account/set_profile_attr.jsp?name=<%=name%>"><%=name%></a><%} else {%><%=name%><% } %>&nbsp;</td>
                <td width="25%"><%=value%></td>
                <td width="10%"><%=(profileAttributeName != null && profileAttributeName.getCategory() != null) ? profileAttributeName.getCategory() : "" %></td>
                <td width="25%"><%=(profileAttributeName != null && profileAttributeName.getDescription() != null) ? profileAttributeName.getDescription() : "" %></td>
            <% if(isEditable) { %>
				<FORM name="remove_<%=name%>" METHOD="POST" ACTION="/customer_account/set_profile_attr.jsp">
                <td width="12%"><a href="javascript:removeProfileAttribute('remove_<%=name%>');">Remove</a>&nbsp;</td>
				<input type="hidden" name="name" value="<%=name%>">
				<input type="hidden" name="action_type" value="removeProfileAttr">
			    </FORM>
		    <%} else {%>
		    	<td width="12%">&nbsp;</td>
		    <% } %>
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
</crm:GetCurrentAgent>
</tmpl:put>
</tmpl:insert>
