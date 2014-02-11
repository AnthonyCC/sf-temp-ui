<%@ page buffer="256kb" autoFlush="true" %>
<%@ page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@ page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@ page import="com.freshdirect.webapp.ajax.product.ProductConfigServlet"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigRequestData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Sku"%>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ page import="com.freshdirect.webapp.ajax.cart.data.AddToCartItem"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<potato:merchant productId='${param.productId}' categoryId='${param.catId}' upSellName="evenBetter" crossSellName="xsell"/>
<potato:images images="imagePotato" productId='${param.productId}' categoryId='${param.catId}'/>

<script>
productData=<fd:ToJSON object="${productPotato}" noHeaders="true"/>
productExtraData=<fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>
images=<fd:ToJSON object="${imagePotato}" noHeaders="true"/>
evenBetter=<fd:ToJSON object="${evenBetter}" noHeaders="true"/>
xsell=<fd:ToJSON object="${xsell}" noHeaders="true"/>
</script>
<div class="pdp">
	<div class="span-16">
    <script type="text/javascript">OAS_AD('ProductNote');</script>
		<soy:render template="pdp.productName" data="${productPotato}" />
		<div id="" class="span-7 first">
			<soy:render template="pdp.prdImage" data="${imagePotato}" />
			<soy:render template="pdp.thumbnails" data="${imagePotato}" />
<c:if test="${productPotato.available}">			
			<soy:render template="pdp.freshnessGuarantee" data="${productExtraPotato}"/>
			<% ProductModel productNode = ContentFactory.getInstance().getProduct(request.getParameter("catId"), request.getParameter("productId")); %>
			<%@ include file="/includes/product/i_product_soc_buttons.jspf" %>
		    <ul class="pdp-accordion">
		        <soy:render template="pdp.accordion.description" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.customerReviews" data="${productPotato}"/>
		        <soy:render template="pdp.accordion.allergens" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.ingredients" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.cookingAndStorage" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.source" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.recipes" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.wine" data="${productExtraPotato}"/>
		        <soy:render template="pdp.nutrition.panel" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.explanatory" data="${productPotato}"/>
		    </ul>
</c:if>
<c:if test="${not productPotato.available }">
	<soy:render template="pdp.accordion.descriptionText" data="${productExtraPotato}"/>
</c:if>		    
		</div>
		<div class="span-8 prepend-1">
<c:if test="${productPotato.available}">			
			<div class="pdp-availability"><soy:render template="pdp.availability" data="${productExtraPotato}" /></div>
			<div class="pdp-price"><soy:render template="common.price" data="${productPotato}" /><soy:render template="pdp.savestring" data="${productPotato}" /></div>
			<div class="span-7 prepend-1 first pdp-info">
				<soy:render template="pdp.skuInfo" data="${productPotato}" />
				<soy:render template="pdp.quantity" data="${productPotato}" />
				<soy:render template="pdp.ratings" data="${productPotato}" />
				<soy:render template="pdp.badges" data="${productExtraPotato}" />
				<soy:render template="pdp.scaleinfo" data="${productPotato}" />
			</div>
			<div class="pdp-productconfig" data-component="product" data-cmeventsource="pdp_main">
				<soy:render template="pdp.productDataMin" data="${productPotato}" />
				<div class="pdp-atcdata">
					<soy:render template="common.skuControlSalesunit" data="${productPotato}" />
					<soy:render template="pdp.skuControlVariations" data="${productPotato}" />
				</div>
				<soy:render template="pdp.ecoupon" data="${productPotato}" />
				<div class="pdp-atc">
					<div>
						<soy:render template="common.skuControlQuantity" data="${productPotato}" /><div class="pdp-atc-button-wrapper"><button class="cssbutton orange medium icon-cart-before" data-component="ATCButton">add to cart</button><soy:render template="pdp.atcInCart" data="${productPotato}"/></div><button class="addtolist cssbutton purple medium" data-component="addToListButton">add to list</button>
					</div>
					<div>
						<soy:render template="pdp.subtotal" data="${productPotato}"/>
					</div>
				</div>
			</div>
			
			<soy:render template="pdp.evenBetter" data="${evenBetter}" />			
			<soy:render template="pdp.likethat" data="${xsell}" />
</c:if>			
<c:if test="${not productPotato.available }">
	<soy:render template="pdp.unavailability" data="${productExtraPotato}"/>
</c:if>		    
			<soy:render template="pdp.productRequest" data="${productExtraPotato}" />
			
		</div>
	</div>
</div>

