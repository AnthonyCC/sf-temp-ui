<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
String recipeId = request.getParameter("recipeId");

if (recipeId == null) {
    throw new FDNotFoundException("No recipeId supplied");
}

Recipe recipe = (Recipe) PopulatorUtil.getContentNode(recipeId);
RecipeSource source = recipe.getSource();
List featuredRecipes = source.getFeaturedRecipes();
String sourceName =source !=null ? source.getName() : "";
String title = sourceName;

Html leftContent = source.getLeftContent();
Html topContent = source.getTopContent();
Html bottomContent = source.getBottomContent();

StringBuffer authorNames = new StringBuffer("");
    List authors = source.getAuthors();
    for (int i = 0; i<authors.size();i++) {
	if (i==0) {
	   authorNames.append("by <b>");
	} else if (i == authors.size()-1 && authors.size() > 1) {
	   authorNames.append(" & ");
	} else if (i >0) {
          authorNames.append(", ");	
        }
	authorNames.append( ((RecipeAuthor)authors.get(i)).getName());
   }
   if (authors.size() > 0) {
   	authorNames.append("</b>");
   }

title = "FreshDirect - " + title;
%>

<tmpl:insert template='/common/template/large_long_pop.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%=title%>"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'><%=title%></tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
		<table width="520">
			<tr valign="top">
				<td width="5%" style="padding-right:10px;"><% if (leftContent != null) { %><fd:IncludeMedia name='<%= leftContent.getPath() %>' /><% } %></td>
				<td width="95%"><span class="recipe_title"><%=title.toUpperCase()%></span><br><span class="text12"><%=authorNames.toString()%></span><br><br><% if (topContent != null) { %><fd:IncludeMedia name='<%= topContent.getPath() %>' /><% } %>
					<% if (featuredRecipes!=null && featuredRecipes.size() > 0) { %>
					<br>
					<div>
					<b>Other Recipe<%= (featuredRecipes.size() > 1) ?"s":""%> from this Book</b><br>
					  <logic:iterate id="aFeatRecipe" collection="<%=featuredRecipes%>" type="com.freshdirect.storeapi.content.Recipe">
					  <% if (!aFeatRecipe.isAvailable()) { %>
					  	<%=aFeatRecipe.getName()%><br>
					  <% } else if(!(!aFeatRecipe.isAvailable())) { %>
						  <a href="javascript:backtoWin('<%=response.encodeURL("/recipe.jsp?recipeId="+aFeatRecipe)%>&trk=rpop');"><%=aFeatRecipe.getName()%></a><br>
					<% } %>
				   </logic:iterate>
				   </div>
	  	    		<% } else { %><br><% } %>
					<% if (bottomContent != null) { %><br><fd:IncludeMedia name='<%= bottomContent.getPath() %>' /><% } %>
				</td>
			</tr>
		</table>
    </tmpl:put>
</tmpl:insert>