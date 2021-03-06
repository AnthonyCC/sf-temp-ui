<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.storeapi.content.ProductModel"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.storeapi.content.PriceCalculator"%>
<%@page import="com.freshdirect.fdstore.content.EnumWineRating"%>
<%@page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.freshdirect.storeapi.content.ContentNodeModel"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.storeapi.content.Image"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<% //expanded page dimensions
final int W_WINE_EXPERT_FAVS_TOTAL = 601;
%>

<div style="width: <%=W_WINE_EXPERT_FAVS_TOTAL%>px; text-align: center; margin: 0px auto;">
<div style="text-align: left;">
<%
CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", request.getParameter("catId"));
List<ProductModel> products;
List<CategoryModel> subcategories;
{
	@SuppressWarnings("unchecked")
    Collection<ContentNodeModel> sortedColl = (Collection<ContentNodeModel>) request.getAttribute("itemGrabberResult");
    products = new ArrayList<ProductModel>(sortedColl.size());
    subcategories = new ArrayList<CategoryModel>();
    for (ContentNodeModel node : sortedColl)
    	if (node instanceof ProductModel)
    		products.add((ProductModel) node);
    	else if (node instanceof CategoryModel && ((CategoryModel) node).getShowSelf())
    		subcategories.add((CategoryModel) node);
}

String trk = "cpage"; // it's a category

if (subcategories.size() > 3)
	subcategories = subcategories.subList(0, 4); // we can display up to 4 tabs

{
	Iterator<CategoryModel> it = subcategories.iterator();
	while (it.hasNext()) {
		CategoryModel subcategory = it.next();
		int size = 0;
		%><display:ItemGrabber id="prods" category="<%= subcategory %>" depth="0" filterUnavailable="true"><% size = prods.size(); %></display:ItemGrabber><%
		if (size == 0)
			it.remove();
	}
}

CategoryModel dftSubcat = subcategories.isEmpty() ? null : subcategories.get(0);
boolean useAlternateImage = true;
boolean isProductShown = !products.isEmpty();
%>

<div align="center">
<fd:IncludeHtml html="<%= category.getEditorial() %>"/>
</div>

<% if (!subcategories.isEmpty()) { %>
<script type="text/javascript">
<% for (CategoryModel subcategory : subcategories) { %>
FreshDirect.Wine.addTabItem("favs", "<%= "tab_" + subcategory.getContentKey().getId() %>");
<% } %>
</script>
<div class="usq" style="padding-top: 15px;">
<table width="<%=W_WINE_EXPERT_FAVS_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="wine-xprt-bg" width="6" valign="top"><img src="/media_stat/images/wine/top_left_<%= JspMethods.getWineAssociateId().toLowerCase() %>_xprt_filled.png" width="7" height="7"></td>
		<td class="wine-xprt-bg wine-lightbrown" align="left">
			<div class="title19 wine-narrow" style="padding: 0.4em 5px 0.25em;"><%= category.getAltText() %></div>
		</td>
		<td class="wine-xprt-bg" width="6" valign="top"><img src="/media_stat/images/wine/top_right_<%= JspMethods.getWineAssociateId().toLowerCase() %>_xprt_filled.png" width="7" height="7"></td>
	</tr>
</table>
<div class="wine-xprt-bg">
<div style="float: left;">
<div class="wine-brown-border" style="border-width: 0px 0px 1px; font-size: 0px;"></div>
<table class="wine-xprt-bg" width="<%= subcategories.size() < 4 ? "auto" : W_WINE_EXPERT_FAVS_TOTAL %>" height="27" cellspacing="0" cellpadding="0" border="0" style="text-align: center; line-height: 19px;">
	<tr>
	<% for (int i = 0; i < subcategories.size(); i++) {
		CategoryModel subcategory = subcategories.get(i);
		String clazz = "carousel-tab-label";
		if (i == 0)
			clazz += "-selected";
		if (i > 1)
			clazz += " carousel-tab-left-label";
		if (i < subcategories.size() -1 && i > 0)
			clazz += " carousel-tab-right-label";
	%>
		<td id="<%= "tab_" + subcategory.getContentKey().getId() + "_label" %>" class="<%= clazz %> text14 wine-narrow">
			<div onclick="return FreshDirect.Wine.showTab(&quot;favs&quot;, &quot;<%= "tab_" + subcategory.getContentKey().getId() %>&quot;);"
				style="<%= subcategories.size() < 4 ? "padding: 0px 10px;" : "" %>">
				<span><%= subcategory.getFullName() %></span>
			</div>
		</td>
	<% } %>
	</tr>
</table>
</div>
<div style="clear: both; font-size: 0px;"></div>
</div>
<div style="width: <%=W_WINE_EXPERT_FAVS_TOTAL%>px;" class="usq" style="background: #fff none;">
	<div id="jesseRecommends" class="wine-brown-border wine-xprt">
		<% for (CategoryModel cat : subcategories) { boolean hideOnly = !cat.isHideWineRatingPricing(); %>
			<% int imgHeight = 0; String tabId = cat != dftSubcat ? "tab_" + cat.getContentKey().getId() : null; %>
			<display:ItemGrabber id="prods" category="<%= cat %>" depth="0" filterUnavailable="true">
				<display:ContentNodeIterator trackingCode="" itemsToShow="<%= prods %>" id="iterator1"><%
					ProductModel prod = (ProductModel) currentItem;
					isProductShown = true;
					Image img = useAlternateImage ? prod.getAlternateImage() : prod.getProdImage();
					if (img == null) img = prod.getProdImage();
					if (imgHeight < img.getHeight()) imgHeight = img.getHeight();
				%></display:ContentNodeIterator>
			</display:ItemGrabber>
		<div id="tab_<%= cat.getContentKey().getId() %>" class="fd-carousel-tab">
			<display:ItemGrabber id="prods" category="<%= cat %>" depth="0" filterUnavailable="true">
				<display:Carousel id="carouselTag" carouselId="<%= cat.getContentKey().getId() %>" itemsToShow="<%= prods %>"
            hideContainer="<%= tabId %>" width="<%=W_WINE_EXPERT_FAVS_TOTAL-90%>" trackingCode="<%= trk %>" numItems="4" appendWineParams="<%= true %>" parentId="jesseRecommends" offset="45" eventHandlersObj='cmPageCarousel["<%=cat.getContentKey().getId()%>"]'><%
						ProductModel product = (ProductModel) currentItem; 
						PriceCalculator pc = product.getPriceCalculator(); %>
					<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
						<display:WineShowRatings product="<%= product %>" hideOnly="<%= hideOnly %>">
						<div><display:WineRating product="<%= product %>" action="<%= actionUrl %>"/></div>
						<div style="padding-bottom: 10px;"><display:WinePrice product="<%= product %>" action="<%= actionUrl %>"/></div>
						</display:WineShowRatings>
						<display:ProductImage priceCalculator="<%= pc %>" showRolloverImage="true" action="<%= actionUrl %>"
								useAlternateImage="<%= useAlternateImage %>" className="productImage" height="<%= imgHeight %>" enableQuickBuy="true" webId="<%= webId %>" excludeCaseDeals="true"/>
						<div class="productname">
							<display:ProductName priceCalculator="<%= pc %>" action="<%= actionUrl %>" showBrandName="true" />
						</div>
						<display:ProductPrice impression="<%= new ProductImpression(pc) %>" showDescription="false" excludeCaseDeals="<%= true %>"/>
					</display:GetContentNodeWebId>
				</display:Carousel>
			</display:ItemGrabber>
		</div>
		<% } %>
	</div>
</div>
</div>
<% } // !subcategories.isEmpty() %>

<% if (category.getContentTemplatePath() != null) {
	isProductShown = true;
	Map<String, String> params = new HashMap<String, String>();
	params.put("baseUrl", "");
%>
<div style="padding-top: 15px;">
<fd:IncludeMedia name="<%= category.getContentTemplatePath() %>" parameters="<%= params %>" />
</div>
<% } %>

<% if (isProductShown && !category.isHideWineRatingPricing()) { %>
<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
<% } %>
</div>
</div>

