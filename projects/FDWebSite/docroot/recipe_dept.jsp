<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.*'%>
<%@ page import='com.freshdirect.cms.node.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
RecipeDepartment recipeDepartment = (RecipeDepartment) ContentFactory.getInstance().getContentNode(request.getParameter("deptId"));
//transfer to the regular department page if this is  not a recipe department
if (! (recipeDepartment instanceof RecipeDepartment)) {  %>
<jsp:forward page="/department.jsp" />
<% }  

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipeDepartment.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,SideCartBottom");

if (!ContentFactory.getInstance().getPreviewMode()) {
    if (recipeDepartment.isHidden()) {
        response.sendRedirect(response.encodeRedirectURL(recipeDepartment.getHideUrl()));
        return;
    }
}
//if there is  redirect_url setting  then go to that url regardless of the previewmode setting
String redirectURL = (recipeDepartment instanceof HasRedirectUrl ? ((HasRedirectUrl)recipeDepartment).getRedirectUrl() : null); 

if (redirectURL!=null) {
    redirectURL = response.encodeRedirectURL(redirectURL);
    response.sendRedirect(redirectURL);
    return;
}
%>
<tmpl:insert template='/common/template/right_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= recipeDepartment.getName() %></tmpl:put>
    <tmpl:put name='content' direct='true'>
<oscache:cache key='<%= "deptLayout_"+request.getQueryString() %>' time='14400'>
<% try { %>
<%@ include file="/shared/includes/layouts/i_recipe_dept_body.jspf"%>
<% } catch (Exception ex) {
		ex.printStackTrace();
%>
<oscache:usecached />
<% } %>
</oscache:cache>

    </tmpl:put>

</tmpl:insert>
