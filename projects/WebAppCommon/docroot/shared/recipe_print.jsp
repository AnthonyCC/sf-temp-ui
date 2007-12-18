<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%
RecipeVariant variant;
Recipe recipe;

String variantId = request.getParameter("variantId");
String recipeId = request.getParameter("recipeId");

if (variantId != null) {
	variant = (RecipeVariant) ContentFactory.getInstance().getContentNode(variantId);
	recipe = (Recipe) variant.getParentNode();

} else if (recipeId !=null) {
	recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
	variant = recipe.getDefaultVariant();

} else {
	throw new IllegalArgumentException("No variantId or recipeId supplied");
}

if (recipe == null) {
	throw new IllegalArgumentException("No recipe found");
}
if (variant == null) {
	throw new IllegalArgumentException("No variant found");
}

if (!variant.isAvailable()) {
	throw new IllegalArgumentException("Recipe variant unavailable");
}


FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);


if (recipeId != null) {

recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
RecipeSource source          = recipe.getSource();
String       sourceName      = "";

if (source != null) {
    sourceName      =  "From \"" + source.getName() + "\"";
}

MediaI recipeDesc = recipe.getDescription();
Attribute attrib  = recipe.getAttribute("titleImage");
MediaI recipePhoto = attrib==null ? null : (MediaI) attrib.getValue();
attrib  = recipe.getAttribute("ingredientsMedia");
MediaI recipeIngrdMedia = attrib==null ? null : (MediaI) attrib.getValue();
attrib  = recipe.getAttribute("preparationMedia");
MediaI recipePrepdMedia = attrib==null ? null : (MediaI) attrib.getValue();
attrib  = recipe.getAttribute("copyrightMedia");
MediaI recipeCpyrghtMedia = attrib==null ? null : (MediaI) attrib.getValue();

	
%>

<tmpl:insert template='/common/template/print_pop.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= recipe.getName() %></tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table cellpadding="0" cellspacing="0" border="0" width="600">
			<tr valign="top">
				<td width="90%">
				<span class="title16"><%=recipe.getName().toUpperCase()%></span><br>
	 			<span class="recipe_author"><%=sourceName%> <%=recipe.getAuthorNames()%></span><br>
	 			<% if(recipeDesc!=null){ %><br><fd:IncludeMedia name='<%= recipeDesc.getPath() %>' /><br><% } %>
				<% if(recipeIngrdMedia!=null){ %><br><img src="/media_stat/recipe/rec_hdr_ingredients.gif" width="92" height="10"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br><fd:IncludeMedia name='<%= recipeIngrdMedia.getPath() %>' /><br><% } %>
				</td>
				<td width="10%" style="padding-left:15px;"><% if(recipePhoto!=null){ %><img src=<%=recipePhoto.getPath()%> width="<%=recipePhoto.getWidth()%>" height="<%=recipePhoto.getHeight()%>" border="0"><% } %></td>
			</tr>
			<tr>
				<td colspan="2"><% if(recipePrepdMedia!=null){ %><img src="/media_stat/recipe/rec_hdr_preparation.gif" width="93" height="10"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br><fd:IncludeMedia name='<%= recipePrepdMedia.getPath() %>' /><br><% } %>
				<% if (recipeCpyrghtMedia!=null) {  %><br><span class="recipe_copyright"><fd:IncludeMedia name='<%= recipeCpyrghtMedia.getPath() %>' /></span><% } %>
				<div class="recipe_fd" style="border-top:solid 1px #CCCCCC; border-bottom:solid 1px #CCCCCC; margin-top: 10px; margin-bottom: 10px; padding-top: 6px; padding-bottom: 6px; font-weight:bold;">This recipe &mdash; and hundreds of others from your favorite chefs and authors &mdash; can be found at FreshDirect.com</strong></div>
				</td>
			</tr>
		</table>
		
    </tmpl:put>
</tmpl:insert>
<% } %>
