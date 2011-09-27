<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.smartstore.sorting.SaleComparator"%>
<%@page import="com.freshdirect.fdstore.content.PriceCalculator"%>
<%@page import="com.freshdirect.fdstore.content.EnumWineRating"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<div style="width: 425px; text-align: center; margin: 0px auto;">
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
	subcategories = subcategories.subList(0, 3); // we can display only 3 tabs

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

<% if (!subcategories.isEmpty()) { %>
<div class="title16" style="padding-top: 20px;">OUR BEST DEALS!</div>
<script type="text/javascript">
<% for (CategoryModel subcategory : subcategories) { %>
FreshDirect.Wine.addTabItem("deals", "<%= "tab_" + subcategory.getContentKey().getId() %>");
<% } %>
</script>
<div class="usq" style="padding-top: 15px;">
<div>
<div style="float: left;">
<div class="usq-brown-border" style="border-width: 0px 0px 1px; font-size: 0px;"></div>
<table class="usq-deals" width="<%= subcategories.size() < 3 ? "auto" : "425" %>" height="27" cellspacing="0" cellpadding="0" border="0" style="text-align: center; line-height: 19px;">
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
			<div onclick="return FreshDirect.Wine.showTab(&quot;deals&quot;, &quot;<%= "tab_" + subcategory.getContentKey().getId() %>&quot;);"
				style="<%= subcategories.size() < 3 ? "padding: 0px 10px;" : "" %>">
				<span><%= subcategory.getFullName() %></span>
			</div>
		</td>
	<% } %>
	</tr>
</table>
</div>
<div style="clear: both; font-size: 0px;"></div>
</div>
<div style="width: 425px;" class="usq">
	<div id="dealsTop" class="usq-brown-border usq-deals">
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
						width="335" trackingCode="<%= trk %>"
						hideContainer="<%= tabId %>" numItems="3" bottomHeader="<%= cat.getAltText() %>"
						bottomHeaderClass="title14 usq-sienna" appendWineParams="<%= true %>" parentId="dealsTop" offset="80"><% ProductModel product = (ProductModel) currentItem; PriceCalculator pc = product.getPriceCalculator(); %>
					<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
						<display:ProductImage priceCalculator="<%= pc %>" showRolloverImage="true" action="<%= actionUrl %>"
								useAlternateImage="<%= useAlternateImage %>" className="productImage" height="<%= imgHeight %>" enableQuickBuy="true" webId="<%= webId %>" excludeCaseDeals="true"/>
						<display:WineShowRatings product="<%= product %>" hideOnly="<%= hideOnly %>">
						<div><display:WinePrice priceCalculator="<%= pc %>" action="<%= actionUrl %>"/></div>
						<div><display:WineRating product="<%= product %>" small="true" action="<%= actionUrl %>"/></div>
						</display:WineShowRatings>
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

<%
// sort by sale
Collections.sort( products, new SaleComparator(false, products, ((FDUserI) session.getAttribute(SessionName.USER)).getPricingContext() ));


ProductModel product1 = products.size() > 0 ? products.get(0) : null;
ProductModel product2 = products.size() > 1 ? products.get(1) : null;
List<ProductModel> rest = products.size() > 2 ? products.subList(2, products.size()) : null;

List<ProductModel> bigs = new ArrayList<ProductModel>(2);
if (product1 != null)
	bigs.add(product1);
if (product2 != null)
	bigs.add(product2);

if (bigs.size() > 0) {
	final int bigsW = 210 * bigs.size();

%>
	<div style="padding-top: 10px;"><img src="/media/editorial/win_usq/deals/hdr_biggest_deal.gif" alt="MISSING HEADER IMAGE"></div>
	<div style="text-align: center; padding-top: 20px;">
	<table width="<%= bigsW %>" cellspacing="0" cellpadding="0" border="0" style="text-align: center; margin: 0px auto;">
		<tr>
		<%
		for (ProductModel bigPrd : bigs) {
			Image prdImg = bigPrd.getDetailImage();
		%><display:GetContentNodeWebId id="webId" product="<%= bigPrd %>" clientSafe="<%= true %>">
			<td width="210">
				<display:ProductUrl id="actionUrl" product="<%= bigPrd %>" trackingCode="<%= trk %>" appendWineParams="<%= true %>">
				<div id="hotspot-<%= webId %>" style="position: relative; text-align: left; width: 210px; height: <%= prdImg.getHeight() %>px;">
					<a href="<%= actionUrl %>"
							style="display: inline-block; position: absolute; left: <%= 105 - prdImg.getWidth() / 2 %>px; top: 0px;"><fd:IncludeImage image="<%= prdImg %>" className="no-outline"/></a>
					<a href="<%= actionUrl %>" style="display: inline-block; position: absolute; left: 105px; top: 30px;"><display:ProductBurst product="<%= bigPrd %>" large="true" className="no-outline"/> </a>
					<%-- QUICK BUY SECTION START --%>
					<img id="qbButton-<%= webId %>" class="qbButtonLarge" style="display: inline-block; position: absolute; left: 36px; bottom: 183px;" src="/media_stat/images/quickbuy/quickbuy_big.gif">
					<script>
						FD_QuickBuy.decorate('hotspot-<%= webId %>', ['qbButton-<%= webId %>', 'hotspot-<%= webId %>'], {
								departmentId: '<%= bigPrd.getDepartment().getContentName() %>',
								categoryId: '<%=  bigPrd.getParentNode().getContentName() %>',
								productId: '<%= bigPrd.getContentName() %>'
						});
					</script>
					<%-- QUICK BUY SECTION END --%>
				</div>
				</display:ProductUrl>
			</td>
		</display:GetContentNodeWebId><%
		}
		%>
		</tr>
		<tr>
		<%
		for (ProductModel bigPrd : bigs) {
		    PriceCalculator pc = bigPrd.getPriceCalculator();
		%>
			<td width="210" valign="top">
				<div style="padding: 0px 10px;">
					<display:ProductUrl id="actionUrl" product="<%= bigPrd %>" trackingCode="<%= trk %>" appendWineParams="<%= true %>">
					<div class="title16" style="padding-top: 10px;"><a href="<%= actionUrl %>"><%=bigPrd.getFullName()%></a></div>
					</display:ProductUrl>
					<div class="title16" style="padding-top: 2px;">
						<display:ProductDefaultPrice priceCalculator="<%= pc %>" />
					</div>
					<% if (pc.isOnSale()) { %>
					<div class="title14 save-base-price" style="padding-top: 2px; font-weight: bold;">
						was <display:ProductWasPrice priceCalculator="<%= pc %>"/>
					</div>
					<% } %>
				</div>
			</td>
		<% } %>
		</tr>
	</table>
	</div>
<%
}

if (rest != null) {
%>
	<div style="padding-top: 10px;"><img src="/media/editorial/win_usq/deals/hdr_allsale.gif" alt="MISSING HEADER IMAGE"></div>
	<table width="425" cellspacing="0" cellpadding="0" border="0">
	<% for (ProductModel product : rest) { PriceCalculator price = product.getPriceCalculator(); 
		   Image img = product.getDescriptiveImage(); %>
		<display:GetContentNodeWebId id="webId" product="<%= product %>" clientSafe="<%= true %>">
		<tr>
			<td width="120">
				<div id="hotspot-<%= webId %>" style="padding: 7px 5px 8px;">
					<display:ProductUrl id="actionUrl" product="<%= product %>" trackingCode="<%= trk %>" appendWineParams="<%= true %>">
					<div style="position: relative; width: 110px; height: <%= img.getHeight() %>px;">
						<a href="<%= actionUrl %>"><fd:IncludeImage image="<%= img %>" className="no-outline"/></a>
						<a href="<%= actionUrl %>" style="display: inline-block; position: absolute; right: 0px; top: 10px;">
							<display:ProductBurst priceCalculator="<%= price %>" className="no-outline" excludeCaseDeals="true"/>
						</a>
						<%-- QUICK BUY SECTION START --%>
						<img id="qbButton-<%= webId %>" class="qbButton" style="display: inline-block; position: absolute; left: <%= (img.getWidth() - 103) / 2 %>px; bottom: 10px;" src="/media_stat/images/quickbuy/quickbuy_button_hover.gif"
								width="103" height="22">
						<script>
							FD_QuickBuy.decorate('hotspot-<%= webId %>', ['qbButton-<%= webId %>', 'hotspot-<%= webId %>'], {
									departmentId: '<%= product.getDepartment().getContentName() %>',
									categoryId: '<%=  product.getParentNode().getContentName() %>',
									productId: '<%= product.getContentName() %>'
							});
						</script>
						<%-- QUICK BUY SECTION END --%>
					</div>
					</display:ProductUrl>
				</div>
			</td>
			<td width="305" style="vertical-align: top; padding-left: 10px;">
				<display:WineShowRatings product="<%= product %>">
				<div style="padding-top: 7px;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<% if (EnumWineRating.getEnumByRating(product.getProductRatingEnum()) != EnumWineRating.NOT_RATED) { %>
						<td style="padding-right: 5px; vertical-align: bottom;"><display:WineRating product="<%= product %>"/></td>
						<% } %>
						<td style="vertical-align: bottom;"><display:WinePrice priceCalculator="<%= price %>" /></td>
					</tr>
				</table>
				</div>
				</display:WineShowRatings>
				<display:ProductUrl id="actionUrl" product="<%= product %>" trackingCode="<%= trk %>" appendWineParams="<%= true %>">
				<div class="title16" style="padding-top: 4px;"><a href="<%= actionUrl %>"><%=product.getFullName()%></a></div>
				</display:ProductUrl>
				<div class="usq_region" style="padding-top: 4px;"><display:WineRegionLabel product="<%= product %>"/></div>
				<div class="title16" style="padding-top: 4px;">
					<display:ProductDefaultPrice priceCalculator="<%= price %>"/>
					<% if (price.isOnSale()) { %>
						<span class="save-base-price" style="font-weight: bold;">- was <display:ProductWasPrice priceCalculator="<%= price %>" /></span>
					<% } %>
				</div>
			</td>
		</tr>
		</display:GetContentNodeWebId>
	<% } %>
	</table>
<%
}
%>

<% if (isProductShown) { %>
<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
<% } %>
</div>
</div>

