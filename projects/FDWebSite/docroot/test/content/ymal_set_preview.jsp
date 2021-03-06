<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.smartstore.SessionInput"%>
<%@ page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@ page import="org.apache.log4j.Logger"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<%
final Logger LOG = LoggerFactory.getInstance("ymal_set_preview.jsp");

if (request.getParameter("ymalSetId") == null) {
	%><div style="text-align: center; font-size: 14px; font-weight: bold; color: red;">Nothing to render...</div><%		
} else {
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
    YmalUtil.resetActiveYmalSetSession(activeYmalSet, request);
    ProductModel productNode = null;
    try {
    	productNode = (ProductModel) ContentFactory.getInstance().getContentNode(request.getParameter("productId"));
    } catch (Exception e) {
    	
    }

	// Pass parameters to YMAL display box
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	SessionInput inp = new SessionInput(user);
	if (activeYmalSet != null) {
		inp.setYmalSource(activeYmalSet);
	}	
	inp.setCurrentNode(productNode);
	inp.setMaxRecommendations(6);

	Recommendations recs = FDStoreRecommender.getInstance().getRecommendations(
			EnumSiteFeature.YMAL, user, inp);
	List relatedProducts	= recs.getProducts();
	List relatedRecipes		= activeYmalSet.getYmalRecipes();
	List relatedCategories	= activeYmalSet.getYmalCategories();

	
%>
<tmpl:insert template='/common/template/blank.jsp'>
<%--     <tmpl:put name='title' direct='true'>YMAL set preview for <%= activeYmalSetId %></tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
    <%  activeYmalSetId = "YMAL set preview for" + activeYmalSetId; %>
    <fd:SEOMetaTag title="<%= activeYmalSetId %>" ></fd:SEOMetaTag>
    </tmpl:put>
    <tmpl:put name='content' direct='true'>
   		<form method="get" class="text13" style="text-align: center; font-weight: bold;">
   			<input type="hidden" name="ymalSetId" value="<%= request.getParameter("ymalSetId") %>"/>
   			Id of current (triggering) product: 
   			<input type="text" name="productId" value="<%= request.getParameter("productId") %>"/>
   			<input type="submit" value="Submit"/>
   		</form>
		<div id="error_pane" style="text-align: left; float: left; border: 2px solid #666; padding: 3px 3px; background-color: #eee">
<%
// -- sort out non displayable items --
for (Iterator it = relatedProducts.iterator(); it.hasNext();) {
	ProductModel prd = (ProductModel) it.next();
	if (!prd.isFullyAvailable()) {
		LOG.error("Throwing non disp prod " + prd.getContentName());
		%><div>Skip Product <span style="font-weight: bold;"><%= prd %></span></div><%
		it.remove();
	}
}

// remove unavailable recipes
for (Iterator it = relatedRecipes.iterator(); it.hasNext();) {
	Recipe rec = (Recipe) it.next();
	if (!rec.isAvailable()) {
		LOG.error("Throwing unavailable rec " + rec.getContentName());
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
			Recommender type: <%= recs.getVariant().getServiceConfig().getType().getName() %><br/>
		</div>
<%

// pass parameters to YMAL renderer
request.setAttribute("ymal_products", relatedProducts);
request.setAttribute("ymal_categories", relatedCategories);
request.setAttribute("ymal_recipes", relatedRecipes);
request.setAttribute("ymal_aset", activeYmalSet);
request.setAttribute("ymal_product", productNode);

String ymalHeader = activeYmalSet.getYmalHeader();
// Special case: Don't pass header if there are related products
if (ymalHeader != null && activeYmalSet.getRelatedProducts().size() == 0) {
	request.setAttribute("ymal_header", ymalHeader);
}

// consolidate lists
%>
<%@ include file="/includes/i_ymal_box.jspf" %>
    </tmpl:put>
</tmpl:insert>
<%
	}
%>
