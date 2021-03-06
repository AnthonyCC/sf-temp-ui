<%@ page buffer="256kb" autoFlush="true" %>
<%@ page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@ page import="com.freshdirect.storeapi.content.ProductModel"%>
<%@ page import="com.freshdirect.webapp.ajax.product.ProductConfigServlet"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigRequestData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Sku"%>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ page import="com.freshdirect.webapp.ajax.cart.data.AddToCartItem"%>
<%@ page import="com.freshdirect.webapp.util.StandingOrderHelper"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<potato:merchant productId='<%=request.getParameter("productId")%>' categoryId='<%=request.getParameter("categoryId")%>' upSellName="evenBetter" crossSellName="xsell"/>
<potato:images images="imagePotato" productId='<%=request.getParameter("productId")%>' categoryId='<%=request.getParameter("catId")%>'/>
<potato:annotations annotations="annotations" productId='<%=request.getParameter("productId")%>' categoryId='<%=request.getParameter("catId")%>'/>

<%
	ProductModel productNode = ContentFactory.getInstance().getProduct(request.getParameter("catId"), request.getParameter("productId"));
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>
	    <script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.browse = window.FreshDirect.browse || {};
			window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};

			window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
			window.FreshDirect.activeDraft = "${activeDraft}"
			window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}";

			window.FreshDirect.pdp = window.FreshDirect.pdp || {};

			window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true"/>;
			window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>;

			FreshDirect.pdp.images=<fd:ToJSON object="${imagePotato}" noHeaders="true"/>;
			FreshDirect.pdp.evenBetter=<fd:ToJSON object="${evenBetter}" noHeaders="true"/>;
			FreshDirect.pdp.xsell=<fd:ToJSON object="${xsell}" noHeaders="true"/>;
			FreshDirect.pdp.annotations=<fd:ToJSON object="${annotations}" noHeaders="true"/>;

			/* are these used? if not, remove and use name-spaced versions */
				productData=window.FreshDirect.pdp.data;
				productExtraData=window.FreshDirect.pdp.extraData;
				images=FreshDirect.pdp.images;
				evenBetter=FreshDirect.pdp.evenBetter;
				xsell=FreshDirect.pdp.xsell;
		</script>
<div itemscope itemtype="http://schema.org/Product" class="pdp">
	<div class="prodDetail">
		<%if (FDStoreProperties.isAdServerEnabled()) {%>
			<div class="center oas-cnt" id='oas_ProductNote'><script type="text/javascript">OAS_AD('ProductNote');</script></div>
		<%} else {%>
    		<div class="center"><%@ include file="/shared/includes/product/i_product_quality_note.jspf" %></div>
		<%}%>

		<% if (mobWeb) { %>
			<div class="prodDetail-images">
				<soy:render template="pdp.prdImage" data="${imagePotato}" />
				<soy:render template="pdp.thumbnails" data="${imagePotato}" />
			</div>
			<div class="prodDetail-nrp"><%-- name/ratings/prices --%>
				<soy:render template="pdp.productName" data="${productPotato}" />

				<c:if test="${productPotato.available}">
					<div class="pdp-availability"><soy:render template="pdp.availability" data="${productPotato}" /></div>
					<div class="pdp-ratings"><soy:render template="pdp.ratings" data="${productPotato}" /></div>
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
						<soy:render template="pdp.badges" data="${productExtraPotato}" /><%-- pdp don't need no stinkin' badges --%>
						<soy:render template="pdp.heatRating" data="${productPotato}" />
					</div>

					<!-- <soy:render template="common.soPdp" data="${productPotato}" /> -->

				</c:if>
				<c:if test="${not productPotato.available }">
					<soy:render template="pdp.unavailability" data="${productExtraPotato}"/>
				</c:if>
			</div>

			<div class="prodDetail-atc"><%-- coupon/group atc controls --%>
				<c:if test="${productPotato.available}">
					<form fdform="pdpatc" fdform-submit="FreshDirect.components.AddToCart.formAddToCart" class="pdp-productconfig" data-component="product" data-eventsource="pdp_main">
						<soy:render template="pdp.productDataMin" data="${productPotato}" />
						<soy:render template="pdp.configWrapper" data="${productPotato}" />

						<soy:render template="pdp.ecoupon" data="${productPotato}" />

						<div class="pdp-atc<%= (StandingOrderHelper.isEligibleForSo3_0(user)) ? " soShow" : "" %>">
							<div class="pdp-atc-buttons"><%-- nice class, except the buttons AREN'T IN THIS DIV --%>
								<div class="pdp-subtotal-cont"><soy:render template="pdp.subtotal" data="${productPotato}"/></div>
								<div class="pdp-skucontrol-cont"><soy:render template="common.skuControlQuantity" data="${productPotato}" /></div>
							</div>

							<div class="pdp-atc-buttons-cont"><%-- new class for mobWeb --%>
								<div class="pdp-atc-button-wrapper">
									<button class="cssbutton cssbutton-flat orange medium" type="button" data-component="ATCButton" data-ignoreredirect="true">Add to Cart</button>
									<soy:render template="pdp.atcInCart" data="${productPotato}"/>
								</div>
								<%-- <button id="pdp-atc-addtolist" class="addtolist cssbutton cssbutton-flat purpleborder medium" type="button" data-component="addToListButton">Add to List</button> --%>
							</div>
							<soy:render template="common.soShowBtnPdp" data="${productPotato}" />
						</div>
					</form>
				</c:if>
			</div>

			<div class="prodDetail-xsell"><%-- group/family/evenbetter/likethat --%>
				<soy:render template="pdp.groupProducts" data="${productExtraPotato}" />

				<%-- don't show evenBetter if we're in group scale context --%>
				<c:if test="${empty param.grpId}">
					<soy:render template="pdp.familyProducts" data="${productExtraPotato}" />
					<c:if test="${empty productExtraPotato.familyProducts}">
						<soy:render template="pdp.evenBetter" data="${evenBetter}" />
					</c:if>
				</c:if>
				<soy:render template="pdp.likethat" data="${xsell}" />

				<soy:render template="pdp.compOptProducts" data="${productExtraPotato}" />
			</div>

			<div class="prodDetail-accords"><%-- accordions --%>
				<c:if test="${productPotato.available}">
					<soy:render template="pdp.freshnessGuarantee" data="${productExtraPotato}"/>
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
			<div class="prodDetail-footer"><%-- LAST PROD DETAILS --%>
				<%-- <soy:render template="pdp.productRequest"/> --%>

				<c:if test="${productPotato.available}">
					<%@ include file="/includes/product/i_product_soc_buttons.jspf" %>
				</c:if>

			</div>
		<%} else {%>

			<div class="span-7 first">
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
					<soy:render template="pdp.productName" data="${productPotato}" />

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
						<form fdform="pdpatc" fdform-submit="FreshDirect.components.AddToCart.formAddToCart" class="pdp-productconfig" data-component="product" data-eventsource="pdp_main">
							<soy:render template="pdp.productDataMin" data="${productPotato}" />
							<soy:render template="pdp.configWrapper" data="${productPotato}" />
							<soy:render template="pdp.ecoupon" data="${productPotato}" />
							<div class="pdp-atc<%= (StandingOrderHelper.isEligibleForSo3_0(user)) ? " soShow" : "" %>">
								<div class="pdp-atc-buttons">
									<div style="display: inline-block;"><soy:render template="common.skuControlQuantity" data="${productPotato}" /></div>
									<div style="display: inline-block;"><soy:render template="pdp.subtotal" data="${productPotato}"/></div>
								</div>

								<div>
									<div class="pdp-atc-button-wrapper"><button class="cssbutton cssbutton-flat orange medium" type="button" data-component="ATCButton">Add to Cart</button><soy:render template="pdp.atcInCart" data="${productPotato}"/></div>
									<soy:render template="common.soShowBtnPdp" data="${productPotato}" />
									<button id="pdp-atc-addtolist" class="addtolist cssbutton cssbutton-flat purpleborder medium" type="button" data-component="addToListButton">Add to List</button>									
								</div>
							</div>
						</form>

						<!-- <soy:render template="common.soPdp" data="${productPotato}" /> -->

						<soy:render template="pdp.groupProducts" data="${productExtraPotato}" />
							<%-- don't show evenBetter if we're in group scale context --%>
						<c:if test="${empty param.grpId}">
							<soy:render template="pdp.familyProducts" data="${productExtraPotato}" />
							<c:if test="${empty productExtraPotato.familyProducts}">
								<c:choose>
									<c:when test="${not empty evenBetter.productList}"><%-- show even better --%>
										<soy:render template="pdp.evenBetter" data="${evenBetter}" />
									</c:when>
									<c:otherwise><%-- show criteo --%>
										<soy:render template="pdp.criteo" data="${browsePotato.adProducts.products}" />
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:if>
						<soy:render template="pdp.likethat" data="${xsell}" />
					</c:if>
					<c:if test="${not productPotato.available }">
						<soy:render template="pdp.unavailability" data="${productExtraPotato}"/>
					</c:if>
					<% if (user.getLevel() == FDUserI.SIGNED_IN) { %>
						<%-- show only if user is logged in (not RECOG) --%>
						<soy:render template="pdp.productRequest"/>
					<% } %>

			</div>
		<% } %>

	</div>
</div>
