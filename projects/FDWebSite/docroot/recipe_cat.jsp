<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.cms.*'%>
<%@ page import='com.freshdirect.cms.application.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%
ContentFactory contentFactory = ContentFactory.getInstance();
ContentNodeModel recipeCategory = contentFactory.getContentNode(request.getParameter("catId"));


String filterParam= request.getParameter("filter")!=null ?  request.getParameter("filter") : "";
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipeCategory.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");

String hideUrl=recipeCategory.getHideUrl();
if (!contentFactory.getPreviewMode()) {
    if (hideUrl!=null) {
        String redirectURL = response.encodeRedirectURL(hideUrl);
	   if (redirectURL.toUpperCase().indexOf("/RECIPE_CAT.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
    }
}
String redirectURL = (recipeCategory instanceof HasRedirectUrl ? ((HasRedirectUrl)recipeCategory).getRedirectUrl() : null); 

if (redirectURL!=null && !"nm".equalsIgnoreCase(redirectURL)  && !"".equals(redirectURL)) {
    redirectURL = response.encodeRedirectURL(redirectURL);
    if (redirectURL.toUpperCase().indexOf("/RECIPE_CAT.JSP?")==-1) {
        response.sendRedirect(redirectURL);
        return;
    }       
}


%>
<tmpl:insert template='/common/template/recipe_DRnavs.jsp'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
   <tmpl:put name='title' direct='true'>FreshDirect - <%= ((RecipeCategory)recipeCategory).getName() %></tmpl:put>
   <tmpl:put name='content' direct='true'>
<oscache:cache key='<%= "recipe_cat_"+request.getQueryString() %>' time="300">
<% try {  %>
<%@ include file="/shared/includes/layouts/i_recipe_cat_body.jspf"%>
<% } catch (Exception ex) {
		ex.printStackTrace();
%>
	<oscache:usecached />
<% } %>
</oscache:cache>

</tmpl:put>
</tmpl:insert>
