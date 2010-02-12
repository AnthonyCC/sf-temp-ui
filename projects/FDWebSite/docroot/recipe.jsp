<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />

<%
RecipeVariant variant;
Recipe recipe;

String variantId        = request.getParameter("variantId");
String recipeId         = request.getParameter("recipeId");
String catIdParam       = request.getParameter("catId");

String jspTemplate ="/common/template/recipe_DLnavs.jsp";



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

if (!recipe.isAvailable()) {
    // if the recipe is unavailable, just display such a message
    // and bail out
%>
<tmpl:insert template='<%=jspTemplate%>'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
   <tmpl:put name='title' direct='true'>FreshDirect - <%= recipe.getName() %></tmpl:put>
   <tmpl:put name='content' direct='true'>

    Sorry, but this recipe is not available.

</tmpl:put>
</tmpl:insert>
<%
    return;
}

if (variant == null) {
	throw new IllegalArgumentException("No variant found");
}

if (!variant.isAvailable()) {
	throw new IllegalArgumentException("Recipe variant unavailable");
}


if (catIdParam!=null && !"".equals(catIdParam)) {
  ContentNodeModel catNode = null;
  catNode = ContentFactory.getInstance().getContentNode(catIdParam);
  if (catNode instanceof CategoryModel) {
	jspTemplate = "/common/template/left_dnav.jsp";
  } 
} else {
   RecipeSubcategory rcpSubcat = recipe.getPrimarySubcategory();
   if (rcpSubcat!=null) {
        catIdParam = ((RecipeCategory)rcpSubcat.getParentNode()).getContentName();
   }
}


int quickCartSize = 0;
for (Iterator it = variant.getAllIngredients().iterator(); it.hasNext(); ) {
    ConfiguredProduct cp = (ConfiguredProduct) it.next();

    if (!cp.isUnavailable()) {
        ++quickCartSize;
    }
}


FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
// String multiSuccessPage = "/grocery_cart_confirm.jsp?catId="+catIdParam+"&recipeId="+recipeId;
// String successPage      = "/grocery_cart_confirm.jsp?catId="+catIdParam+"&recipeId="+recipeId;  //"/cart_confirm.jsp?catId="+catIdParam+"&recipeId="+recipeId;
String multiSuccessPage	= FDURLUtil.getRecipeCartConfirmPageURI(request, catIdParam);
String successPage		= FDURLUtil.getRecipeCartConfirmPageURI(request, catIdParam);

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", recipe.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote");

%>
<tmpl:insert template='<%=jspTemplate%>'>
   <tmpl:put name='leftnav' direct='true'>
   </tmpl:put>
   <tmpl:put name='title' direct='true'>FreshDirect - <%= recipe.getName() %></tmpl:put>
   <tmpl:put name='content' direct='true'>

  <%@ include file="/shared/includes/layouts/i_recipe_body.jspf"%>

</tmpl:put>
</tmpl:insert>
