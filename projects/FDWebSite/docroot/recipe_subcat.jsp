<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.fdstore.FDNotFoundException"%>

<fd:CheckLoginStatus />
<%
ContentNodeModel catModel = PopulatorUtil.getContentNode(request.getParameter("catId"));
ContentNodeModel subCatModel = PopulatorUtil.getContentNode(request.getParameter("subCatId"));

if (!(catModel instanceof RecipeCategory)){
    throw new FDNotFoundException("CategoryId was not a RecipeCategory");
}

if (!(subCatModel instanceof RecipeSubcategory)){
    throw new FDNotFoundException("SubCategoryId was not a RecipeSubCategory");
}

RecipeCategory recipeCategory = (RecipeCategory) catModel;
RecipeSubcategory recipeSubCat = (RecipeSubcategory) subCatModel;

// go get first subcat if subcat was null
if (recipeSubCat==null) {
   Collection<RecipeSubcategory> c= recipeCategory.getSubcategories();
   recipeSubCat = c.iterator().next();
}

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipeCategory.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");

String hideUrl=recipeCategory.getHideUrl();
if (!ContentFactory.getInstance().getPreviewMode()) {
    if (hideUrl!=null) {
        String redirectURL = response.encodeRedirectURL(hideUrl);
	   if (redirectURL.toUpperCase().indexOf("/RECIPE_SUBCAT.JSP?")==-1) {
           response.sendRedirect(redirectURL);
           return;
	   }       
    }
}
String redirectURL = recipeCategory.getRedirectUrl(); 
if (redirectURL!=null && !"nm".equalsIgnoreCase(redirectURL) && !"".equals(redirectURL)) {
    redirectURL = response.encodeRedirectURL(redirectURL);
	if (redirectURL.toUpperCase().indexOf("/RECIPE_SUBCAT.JSP?")==-1) {
        response.sendRedirect(redirectURL);
        return;
    }       
}

String title = "FreshDirect - " + recipeSubCat.getName();

%>
<tmpl:insert template='/common/template/recipe_DLRnavs.jsp'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%= title %>"/>
    </tmpl:put>
<%--    <tmpl:put name='title' direct='true'><%= title %></tmpl:put> --%>
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
