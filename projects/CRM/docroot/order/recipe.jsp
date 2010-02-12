<%@ page import='java.text.*' %>

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
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

<%
    RecipeVariant variant;
    Recipe recipe;

    String variantId        = request.getParameter("variantId");
    String recipeId         = request.getParameter("recipeId");
    String catIdParam       = request.getParameter("catId");
    String multiSuccessPage = request.getRequestURL().append("?").append(request.getQueryString()).toString();
    String successPage      = multiSuccessPage;

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

    int quickCartSize = 0;
    for (Iterator it = variant.getAllIngredients().iterator(); it.hasNext(); ) {
	    ConfiguredProduct cp = (ConfiguredProduct) it.next();

	    if (!cp.isUnavailable()) {
		++quickCartSize;
	    }
    }

    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
     
	//
	// Get index of current search term
	//
	int ix = 0;
	if (request.getParameter("searchIndex") != null) {
		ix = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<% request.setAttribute("needsCCL","true"); %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Browse Items</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>
<div class="order_content">
<%@ include file='/shared/includes/layouts/i_recipe_body.jspf'%>
</div>
<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>
