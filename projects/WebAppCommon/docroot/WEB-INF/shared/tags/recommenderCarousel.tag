<%@ tag import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.webapp.util.ConfigurationContext"
		import="com.freshdirect.webapp.util.ConfigurationStrategy"
		import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"
		import="com.freshdirect.webapp.util.FDURLUtil"
		import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil" 
		import="com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag"
		import="java.util.Collections"
		import="com.freshdirect.webapp.util.TransactionalProductImpression"
		import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.fdstore.content.EnumBurstType"
		import="com.freshdirect.webapp.taglib.fdstore.SessionName"
		import="com.freshdirect.fdstore.customer.FDUserI"
		import="com.freshdirect.fdstore.FDSkuNotFoundException"
		import="com.freshdirect.fdstore.*"
		import="com.freshdirect.fdstore.content.ProductModel"
		import="com.freshdirect.fdstore.FDProduct"
%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ attribute name="id" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="trkCode" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="width" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="numItems" required="false" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="maxItems" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="siteFeature" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="facility" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %>
<%
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

	Integer seq = (Integer) request.getAttribute("i_product_box_counter");
	if (seq == null) {
		seq = 0;
	}
%>
<div class="grid-carousel grid-view">
	<fd:ProductGroupRecommender siteFeature="<%= siteFeature %>" facility="<%= facility %>" id="recommendedProducts">
	<display:Carousel id="carousel" carouselId="<%= id %>" width="<%= width %>" numItems="<%= numItems %>" showCategories="false" itemsToShow="<%=recommendedProducts.getProducts()%>" trackingCode="<%= trkCode %>" maxItems="<%= maxItems %>">
		<span class="smartstore-carousel-item">
		<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
			<% ProductModel pm = (ProductModel)currentItem; %>
			<% ProductImpression pi = confStrat.configure(pm, confContext); %>
			<a href="<%= FDURLUtil.getProductURI(pm, trkCode)%>" hidden style="display: none;" class="product-name-link"></a> <%-- For Coremetrics impression tracking --%>
			<%pageContext.setAttribute("PRODUCT_BOX_VARIANT",recommendedProducts.getVariant().getId());%>
			<div class="grid-item-container featurebox">
<%
	if(pi!=null) {
	String imageSize = "detail";
%><display:ProductAvailability id="productAvailability" product="<%= pm %>"><%
	String unavailableClass = "";
	if (!productAvailability.isFullyAvailable())
		unavailableClass = "grid-item-unavailable";
%>
<div class="grid-item <%= unavailableClass %> <fd:ProductCartStatus product="<%= pm %>"/>">
<div class="grid-item-image-wrapper">
<display:ProductBurstClass id="burstClass" product="<%= pm %>">
<div class="grid-item-image"><span class="grid-item-image-helper"></span>
<display:ProductLink product="<%= pm %>" trackingCode="<%= trkCode %>">
<%
  		FDProduct fdproduct = null;
        try {
        	fdproduct = FDCachedFactory.getProduct(pi.getProductInfo());
           
		if ( "USQ".equalsIgnoreCase(pm.getDepartment().toString()) && (fdproduct != null && !"".equals(fdproduct.getMaterial().getAlcoholicContent().getCode())) ) {
			imageSize = "zoom";
		}
       } catch (Exception fdsnf){
       }
%>
<span class="burst <%= burstClass %>" style="<%= pageContext.getAttribute("burstStyleString") %>"></span><fd:USQProductBurst product="<%= pm %>"/><display:SimpleProductImage product="<%= pm %>" size="<%= imageSize %>"/></display:ProductLink>
</div>
</display:ProductBurstClass>
<div class="grid-item-controls">
<span class="grid-item-controls-content">
<%
	String statusPlaceholderId = "instant_atc_status_" + seq;
	if (productAvailability.isFullyAvailable()) {
		String namespaceName = "instant_atc_ns_" + seq;
		String formName = "instant_atc_form_" + seq;
		String subTotalId = "instant_atc_subtotal_" + seq;
%>
<fd:TxSingleProductPricingSupport formName="<%= formName %>" namespace="<%= namespaceName %>"
		customer="<%= (FDUserI) session.getAttribute(SessionName.USER) %>" impression="<%= pi %>" statusPlaceholder="<%= statusPlaceholderId %>"
		subTotalPlaceholderId="<%= subTotalId %>" />
<%
		if (pi.isTransactional()) {
%>
<form method="get" action="#" id="<%= formName %>" name="<%= formName %>" onsubmit="return false;">
<fd:AddToCartPending id="<%= formName %>" action="addMultipleToCart" rebindSubmit="true"/>
<input type="hidden" name="itemCount" value="1">
<fd:TxProductControl txNumber="0" namespace="<%= namespaceName %>" impression="<%= (TransactionalProductImpression) pi %>" setMinimumQt="true" />
<div id="<%= subTotalId %>" class="subtotal">subtotal: <span id="<%= subTotalId %>_value" class="value"></span></div>
<div><button class="addtocart" id='<%= "instantATC" + formName %>' onclick="<%= namespaceName %>.instantATC();"></button></div>
<fd:PopupHandler id="<%= formName %>" event="onclick" elementId='<%= "instantATC" + formName %>' instant='<%="window.parent." + namespaceName + ".instantATC()" %>' skuCode="<%=((TransactionalProductImpression) pi).getSku().getSkuCode()%>" action="addMultipleToCart" rebindSubmit="true" />
<input type="hidden" name="<%=FDShoppingCartControllerTag.PARAM_ADDED_FROM_SEARCH%>" value="<%=pageContext.getAttribute("ISONSEARCHPAGE") != null ? "true":"false" %>">
<%Object variant = pageContext.getAttribute("PRODUCT_BOX_VARIANT");
if (variant!=null){%>
<input type="hidden" name="variant_0" value="<%=variant%>">
<%}%>
</form>
<%
		} else {
			String deptId = pm.getDepartment().getContentKey().getId();
%>
<button class="customize" onclick="FD_QuickBuy.showPanel('<%= deptId %>', '<%= pm.getCategory().getContentKey().getId() %>', '<%= pm.getContentKey().getId() %>', '<%= namespaceName %>')();"></button>
<%
		}
	} else {
%>
Sorry! Unavailable!
<%
	}
%>
</span>
  <div class="grid-item-status" id="<%= statusPlaceholderId %>">
    <div class="grid-item-status-in-cart"><fd:ProductCartStatusMessage product="<%= pm %>"/></div>
    <div class="grid-item-status-added"></div>
  </div>
</div>
</div>
<div class="grid-item-rating">
<display:ProductLink product="<%= pm %>" trackingCode="<%= trkCode %>">
<display:ProductRating product="<%= pm %>" noBr="true"/>
</display:ProductLink>
</div>
<div class="grid-item-name">
<display:ProductLink product="<%= pm %>" trackingCode="<%= trkCode %>">
<display:ProductName product="<%= pm %>"/>
</display:ProductLink>
</div>
<div class="grid-item-price-description"><display:ProductPriceDescription impression="<%= pi %>"/></div>
<div class="grid-item-price">
<% if (productAvailability.isFullyAvailable()) { %>
<span class="prices">
<display:ProductDefaultPrice product="<%= pm %>" showDescription="true"/>
<% if (pm.getPriceCalculator().isOnSale()) { %><span class="was-price">(<span><display:ProductWasPrice product="<%= pm %>"/></span>)</span><% } %>
</span>
<display:ProductAboutPrice product="<%= pm %>" />
<% } %>
<display:ProductGroupLink impression="<%= pi %>" trackingCode="<%= trkCode %>"><display:ProductGroupPricing product="<%= pm %>"/></display:ProductGroupLink>
</div>
<div class="grid-item-saving"><display:ProductSaving product="<%= pm %>" excludeDeals="true"/></div>
<div class="grid-item-separator"></div>
</div><!--  grid-item -->
</display:ProductAvailability>
<%
	}
	++seq;
%>
		</div>
		</display:GetContentNodeWebId>
		</span>
	</display:Carousel>
	</fd:ProductGroupRecommender>
</div>
<% request.setAttribute("i_product_box_counter", seq);  %>