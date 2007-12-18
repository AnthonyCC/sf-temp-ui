<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.ProfileAttributeName"%>
<%@ page import="com.freshdirect.fdstore.customer.EnumProfileAttrValueType"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerModel"%>
<%@ page import="com.freshdirect.fdstore.customer.ProfileModel"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>

<%
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

List categories = FDCustomerManager.loadProfileAttributeNameCategories();
Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
FDCustomerModel customer = user.getFDCustomer();
ProfileModel profile = customer.getProfile();

String actionType = NVL.apply(request.getParameter("action_type"), "");
String name = NVL.apply(request.getParameter("name"), "");
String value = NVL.apply(request.getParameter("value"), "");
String notes = NVL.apply(request.getParameter("notes"), "");
String category = NVL.apply(request.getParameter("category"), "");
String successPage = NVL.apply(request.getParameter("success_page"), "/main/profile_list.jsp");

if ("".equals(actionType)) {
	actionType = (profile.getAttribute(name) == null) ? "addProfileAttr" : "updateProfileAttr";
}

if ("updateProfileAttr".equals(actionType) && "".equals(value)) {
	value = NVL.apply(profile.getAttribute(name), "");
}
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'><%=("updateProfileAttr".equals(actionType)) ? "Edit" : "New"%>  Profile Attribute</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<div class="sub_nav" style="text-align: left;">
	<table cellpadding="0" cellspacing="0" border="0" width="99%">

<crm:CrmProfileSettingController user="<%=user%>" actionName="<%=actionType%>" result="result" successPage="<%=successPage%>">
	<form  name="profile" method="POST">
	<tr><td width="40%"><span class="sub_nav_title"><%=("updateProfileAttr".equals(actionType)) ? "Edit" : "New"%> Profile Attribute</span></td>
	<td>&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=ActionError.GENERIC%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
	<td width="30%"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="<%=("updateProfileAttr".equals(actionType)) ? "EDIT" : "ADD"%> PROFILE ATTRIBUTE" class="submit"></td>
	<td width="20%"align="right"><a href="/main/profile_list.jsp">View All Profile Attributes &raquo;</a></td>
	</tr>
	</table>
	</div>
	<div class="content_fixed">
		<%@ include file="/includes/profile_attr_fields.jspf" %>
	</div>
	</form>
</crm:CrmProfileSettingController>
</tmpl:put>
</tmpl:insert>