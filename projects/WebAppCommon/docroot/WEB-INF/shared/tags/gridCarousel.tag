<%@ tag import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.webapp.util.ConfigurationContext"
		import="com.freshdirect.webapp.util.ConfigurationStrategy"
		import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"
		import="com.freshdirect.webapp.util.FDURLUtil"
		import="com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag"
		import="java.util.Collections"
		import="java.util.List"
		import="com.freshdirect.webapp.util.TransactionalProductImpression"
		import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.fdstore.content.EnumBurstType"
		import="com.freshdirect.webapp.taglib.fdstore.SessionName"
		import="com.freshdirect.fdstore.customer.FDUserI"
		import="com.freshdirect.fdstore.FDSkuNotFoundException"
		import="com.freshdirect.fdstore.*"
		import="com.freshdirect.storeapi.content.ProductModel"
		import="com.freshdirect.fdstore.FDProduct"
		import="com.freshdirect.WineUtil"
%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ attribute name="id" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="trkCode" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="width" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="numItems" required="false" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="maxItems" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ attribute name="itemsToShow" required="false" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="recommendations" required="false" rtexprvalue="true" type="com.freshdirect.smartstore.fdstore.Recommendations" %>
<%
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

	Integer seq = (Integer) request.getAttribute("i_product_box_counter");
	if (seq == null) {
		seq = 0;
	}
	
	if(itemsToShow == null && recommendations == null) {
		throw new JspTagException("missing one of itemsToShow or recommendations attributes!");
	}
	
	if(itemsToShow != null && recommendations != null) {
		throw new JspTagException("both of itemsToShow and recommendations attributes are set!");
	}
	
	List items = itemsToShow;
	if(items == null ) {
		items = recommendations.getProducts();
	}
	
%>
<div class="grid-carousel grid-view">
	<!--[if IE]>
	<script type="text/javascript">
		(function($){
			$(document).on('mouseover','.grid-carousel .grid-item-container', function(e){
				$(this).addClass('hover');
			});
			$(document).on('mouseout','.grid-carousel .grid-item-container', function(e){
				if(e.target.tagName.toLowerCase()!=='select') {
					$(this).removeClass('hover');
				}
			});
		})(jQuery);
	</script>
	<![endif]-->
	<display:Carousel id="carousel" carouselId="<%= id %>" width="<%= width %>" numItems="<%= numItems %>" showCategories="false" itemsToShow="<%= items %>" trackingCode="<%= trkCode %>" maxItems="<%= maxItems %>">
		<span class="smartstore-carousel-item">
		<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
			<% ProductModel pm = (ProductModel)currentItem; %>
			<% ProductImpression pi = confStrat.configure(pm, confContext); %>
			<% if(recommendations != null) { %>
				<a href="<%= FDURLUtil.getProductURI(pm, trkCode)%>" hidden style="display: none;" class="product-name-link"></a> 
				<%jspContext.setAttribute("PRODUCT_BOX_VARIANT",recommendations.getVariant().getId());
			} %>
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
           
		if ( WineUtil.getWineAssociateId().equalsIgnoreCase(pm.getDepartment().toString()) && (fdproduct != null && !"".equals(fdproduct.getMaterial().getAlcoholicContent().getCode())) ) {
			imageSize = "zoom";
		}
       } catch (Exception fdsnf){
       }
%>
<span class="burst <%= burstClass %>" style="<%= jspContext.getAttribute("burstStyleString") %>"></span><fd:USQProductBurst product="<%= pm %>"/><display:SimpleProductImage product="<%= pm %>" size="<%= imageSize %>"/></display:ProductLink>
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
		customer="<%= user %>" impression="<%= pi %>" statusPlaceholder="<%= statusPlaceholderId %>"
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
<input type="hidden" name="<%=FDShoppingCartControllerTag.PARAM_ADDED_FROM_SEARCH%>" value="<%=jspContext.getAttribute("ISONSEARCHPAGE") != null ? "true":"false" %>">
<%Object variant = jspContext.getAttribute("PRODUCT_BOX_VARIANT");
if (variant!=null){%>
<input type="hidden" name="variant_0" value="<%=variant%>">
<%}%>
</form>
<%
		} else {
			String deptId = pm.getDepartment().getContentKey().getId();
%>
<button class="customize" onclick="FD_QuickBuy.showPanel('<%= deptId %>', '<%= pm.getCategory().getContentKey().getId() %>', '<%= pm.getContentKey().getId() %>', 'CUSTOMIZE', '<%= namespaceName %>')();"></button>
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
</div>
<% request.setAttribute("i_product_box_counter", seq);  %>