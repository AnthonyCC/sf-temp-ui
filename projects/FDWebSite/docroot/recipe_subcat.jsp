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
RecipeSubcategory recipeSubCat = (RecipeSubcategory)contentFactory.getInstance().getContentNode(request.getParameter("subCatId"));

// go get first subcat if subcat was null
if (recipeSubCat==null) {
   Collection c= ((RecipeCategory)recipeCategory).getSubcategories();
   recipeSubCat = (RecipeSubcategory) c.iterator().next();
}

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipeCategory.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");

String hideUrl=recipeCategory.getHideUrl();
if (!contentFactory.getInstance().getPreviewMode()) {
    if (hideUrl!=null) {
        String redirectURL = response.encodeRedirectURL(hideUrl);
	   if (redirectURL.toUpperCase().indexOf("/RECIPE_SUBCAT.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
    }
}
String redirectURL = (recipeCategory instanceof HasRedirectUrl ? ((HasRedirectUrl)recipeCategory).getRedirectUrl() : null); 
if (redirectURL!=null && !"nm".equalsIgnoreCase(redirectURL) && !"".equals(redirectURL)) {
    redirectURL = response.encodeRedirectURL(redirectURL);
	if (redirectURL.toUpperCase().indexOf("/RECIPE_SUBCAT.JSP?")==-1) {
        response.sendRedirect(redirectURL);
        return;
    }       
}

%>
<tmpl:insert template='/common/template/recipe_DLRnavs.jsp'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
   <tmpl:put name='title' direct='true'>FreshDirect - <%= recipeSubCat.getName() %></tmpl:put>
   <tmpl:put name='content' direct='true'>
<oscache:cache key='<%= "recipe_cat_"+request.getQueryString() %>' time="300">
<% try {  %>   
  <%@ include file="/shared/includes/layouts/i_recipe_subcat_body.jspf"%>
<% } catch (Exception ex) {
		ex.printStackTrace();
%>
	<oscache:usecached />
<% } %>
</oscache:cache>

</tmpl:put>
</tmpl:insert>
