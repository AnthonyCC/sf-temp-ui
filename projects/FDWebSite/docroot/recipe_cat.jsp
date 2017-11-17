<%@ page import='java.util.*' %>
<%@ page import='java.io.*' %>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*'%>
<%@ page import='com.freshdirect.storeapi.application.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />

<%
ContentNodeModel recipeCategory = PopulatorUtil.getContentNode(request.getParameter("catId"));

String filterParam= request.getParameter("filter")!=null ?  request.getParameter("filter") : "";
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipeCategory.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");

String hideUrl=recipeCategory.getHideUrl();
if (!ContentFactory.getInstance().getPreviewMode()) {
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

String title = "FreshDirect - " + ((RecipeCategory)recipeCategory).getName();

%>
<tmpl:insert template='/common/template/recipe_DRnavs.jsp'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%=title%>"/>
    </tmpl:put>
   <tmpl:put name='title' direct='true'><%=title%></tmpl:put>
   <tmpl:put name='content' direct='true'>
   <fd:CmPageView wrapIntoScriptTag="true" currentFolder="<%=recipeCategory%>"/>
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
