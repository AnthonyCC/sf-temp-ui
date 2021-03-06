<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='com.freshdirect.storeapi.node.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="true" />
<%
RecipeDepartment recipeDepartment = (RecipeDepartment) PopulatorUtil.getContentNode(request.getParameter("deptId"));
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
    FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
    String title = "FreshDirect - " + recipeDepartment.getName();
%>

<tmpl:insert template='/common/template/right_nav.jsp'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="<%= title %>"></fd:SEOMetaTag>
	</tmpl:put>
<%--     <tmpl:put name='title' direct='true'><%= title %></tmpl:put> --%>
	<tmpl:put name='pageType' direct='true'>recipe_dept</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<% try { %>
			<%@ include file="/shared/includes/layouts/i_recipe_dept_body.jspf"%>
		<% } catch (Exception ex) {
				ex.printStackTrace();
		%>
		<% } %>
    </tmpl:put>

</tmpl:insert>
