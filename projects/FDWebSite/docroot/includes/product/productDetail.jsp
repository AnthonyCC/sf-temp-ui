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
<%@ taglib uri='fd-certona-tag' prefix='certona' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<certona:resonanceJSObject action="init"/>

<potato:merchant productId='${param.productId}' categoryId='${param.catId}' upSellName="evenBetter" crossSellName="xsell"/>
<potato:images images="imagePotato" productId='${param.productId}' categoryId='${param.catId}'/>
<potato:annotations annotations="annotations" productId='${param.productId}' categoryId='${param.catId}'/>

<% ProductModel productNode = ContentFactory.getInstance().getProduct(request.getParameter("catId"), request.getParameter("productId")); %>
<script>
var FreshDirect = FreshDirect || {};
FreshDirect.pdp = FreshDirect.pdp || {};

productData=<fd:ToJSON object="${productPotato}" noHeaders="true"/>
productExtraData=<fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>
images=<fd:ToJSON object="${imagePotato}" noHeaders="true"/>
evenBetter=<fd:ToJSON object="${evenBetter}" noHeaders="true"/>
xsell=<fd:ToJSON object="${xsell}" noHeaders="true"/>

FreshDirect.pdp.annotations=<fd:ToJSON object="${annotations}" noHeaders="true"/>
FreshDirect.pdp.coremetrics=<fd:CmElement elementCategory="reviews" productId="<%=productNode.getContentKey().getId()%>" wrapIntoFunction="true" />;
</script>
<div itemscope itemtype="http://schema.org/Product" class="pdp">
	<div>
	<fd:CmFieldDecorator/>
		<%if (FDStoreProperties.isAdServerEnabled()) {%>
			<center class="oas-cnt"><script type="text/javascript">OAS_AD('ProductNote');</script></center>
		<%} else {%>
    		<center><%@ include file="/shared/includes/product/i_product_quality_note.jspf" %></center>
		<%}%>
		
		<soy:render template="pdp.productName" data="${productPotato}" />
		<div id="" class="span-7 first">
			<soy:render template="pdp.prdImage" data="${imagePotato}" />
			<soy:render template="pdp.thumbnails" data="${imagePotato}" />
			<c:if test="${productPotato.available}">			
				<soy:render template="pdp.freshnessGuarantee" data="${productExtraPotato}"/>
				<%@ include file="/includes/product/i_product_soc_buttons.jspf" %>
			</c:if>
		    <ul class="pdp-accordion">
		        <soy:render template="pdp.accordion.description" data="${productExtraPotato}"/>
		        <soy:render template="pdp.nutrition.panel" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.customerReviews" data="${productPotato}"/>
		        <soy:render template="pdp.accordion.allergens" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.ingredients" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.cookingAndStorage" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.servingSuggestions" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.source" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.recipes" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.wine" data="${productExtraPotato}"/>
		        <soy:render template="pdp.accordion.explanatory" data="${productPotato}"/>
		    </ul>

		</div>
		<div class="span-8 prepend-1">
				<c:if test="${productPotato.available}">			
							<div class="pdp-availability"><soy:render template="pdp.availability" data="${productPotato}" /></div>
							<div class="pdp-price">
								<soy:render template="common.price" data="${productPotato}" />
								<soy:render template="pdp.savestring" data="${productPotato}" />
							</div>
							<div class="span-7 prepend-1 first pdp-info">
								<soy:render template="pdp.skuInfo" data="${productPotato}" />
								<soy:render template="pdp.quantity" data="${productPotato}" />
								<%-- don't show scale info if we're in group scale context --%>
								<c:if test="${empty param.grpId and empty param.version}">
									<soy:render template="pdp.scaleinfo" data="${productPotato}" />
								</c:if>
								<soy:render template="pdp.ratings" data="${productPotato}" />
								<soy:render template="pdp.badges" data="${productExtraPotato}" />
								<soy:render template="pdp.heatRating" data="${productPotato}" />
							</div>
							<div class="pdp-productconfig" data-component="product" data-cmeventsource="pdp_main">
								<soy:render template="pdp.productDataMin" data="${productPotato}" />
								<soy:render template="pdp.configWrapper" data="${productPotato}" />
								<soy:render template="pdp.ecoupon" data="${productPotato}" />
								<div class="pdp-atc">
									<div class="pdp-atc-buttons">
										<soy:render template="common.skuControlQuantity" data="${productPotato}" /><div class="pdp-atc-button-wrapper"><button class="cssbutton orange medium" alt="" data-component="ATCButton">add to cart</button><soy:render template="pdp.atcInCart" data="${productPotato}"/></div><button id="pdp-atc-addtolist" class="addtolist cssbutton purple medium" alt="" data-component="addToListButton">add to list</button>
									</div>
									<div>
										<soy:render template="pdp.subtotal" data="${productPotato}"/>
									</div>
								</div>
							</div>
							<soy:render template="pdp.groupProducts" data="${productExtraPotato}" />
								<%-- don't show evenBetter if we're in group scale context --%>
							<c:if test="${empty param.grpId}">
								<soy:render template="pdp.familyProducts" data="${productExtraPotato}" />
								<c:if test="${empty productExtraPotato.familyProducts}">					
									<soy:render template="pdp.evenBetter" data="${evenBetter}" />					
								</c:if>	
							</c:if>
							<soy:render template="pdp.likethat" data="${xsell}" />
				</c:if>			
				<c:if test="${not productPotato.available }">
					<soy:render template="pdp.unavailability" data="${productExtraPotato}"/>
				</c:if>		    
						<soy:render template="pdp.productRequest"/>
						
		</div>
	</div>
</div>