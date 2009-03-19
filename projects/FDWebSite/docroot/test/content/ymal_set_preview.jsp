<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
/*
 *  A page used for previewing ymal sets.
 *
 *  HTTP request parameters:
 *
 *  activeYmalSetId - the category id of the product.
 */

    String activeYmalSetId   = request.getParameter("ymalSetId");
    ContentFactory cf        = ContentFactory.getInstance();
    YmalSource activeYmalSet = (YmalSource) cf.getContentNode(activeYmalSetId);
    ProductModel productNode = null;

	// Pass parameters to YMAL display box
	List relatedProducts	= activeYmalSet.getYmalProducts();
	List relatedRecipes		= activeYmalSet.getYmalRecipes();
	List relatedCategories	= activeYmalSet.getYmalCategories();

	
%>
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='title' direct='true'>YMAL set preview for <%= activeYmalSetId %></tmpl:put>
    <tmpl:put name='content' direct='true'>
		<div id="error_pane" style="text-align: left; float: left; border: 2px solid #666; padding: 3px 3px; background-color: #eee">
<%
// -- sort out non displayable items --
for (Iterator it = relatedProducts.iterator(); it.hasNext();) {
	ProductModel prd = (ProductModel) it.next();
	if (!prd.isDisplayable()) {
		System.err.println("Throwing non disp prod " + prd.getContentName());
		%><div>Skip Product <span style="font-weight: bold;"><%= prd %></span></div><%
		it.remove();
	}
}

// remove unavailable recipes
for (Iterator it = relatedRecipes.iterator(); it.hasNext();) {
	Recipe rec = (Recipe) it.next();
	if (!rec.isAvailable()) {
		System.err.println("Throwing unavailable rec " + rec.getContentName());
		%><div>Skip Unavail Recipe <span style="font-weight: bold;"><%= rec %></span></div><%
		it.remove();
	}
}

// sort out hidden categories
for (Iterator it = relatedCategories.iterator(); it.hasNext();) {
	CategoryModel c = (CategoryModel) it.next();
	if (c.isHidden()) {
		%><div>Skip Hidden Cat <span style="font-weight: bold;"><%= c %></span></div><%
		it.remove();
	}
}
%>
			<br/>
			Products: <%= relatedProducts.size() %><br/>
			Recipes: <%= relatedRecipes.size() %><br/>
			Cats: <%= relatedCategories.size() %><br/>
		</div>
<%

// pass parameters to YMAL renderer
request.setAttribute("ymal_products", relatedProducts);
request.setAttribute("ymal_categories", relatedCategories);
request.setAttribute("ymal_recipes", relatedRecipes);
request.setAttribute("ymal_aset", activeYmalSet);
request.setAttribute("ymal_product", productNode);

request.setAttribute("ymal_header", activeYmalSet.getYmalHeader());

// consolidate lists
%>
<%@ include file="/includes/i_ymal_box.jspf" %>
    </tmpl:put>
</tmpl:insert>
