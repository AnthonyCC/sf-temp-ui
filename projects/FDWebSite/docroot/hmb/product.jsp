<%@ page import="com.freshdirect.webapp.taglib.fdstore.CartName" %>
<%@ page import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCartLineI" %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus id="user" />
<fd:CheckDraftContextTag/>

<%
	String cartMode = (String) request.getAttribute("cartMode");
	String productId = NVL.apply(request.getParameter("productId"),NVL.apply(request.getAttribute("modProductId"), "")).toString();
	String catId = NVL.apply(request.getParameter("catId"),NVL.apply(request.getAttribute("modCatId"), "")).toString();
%>
<potato:product name="productPotato" extraName="productExtraPotato" productId='<%= productId %>' categoryId='<%= catId %>' variantId='${param.variantId}' grpId='${param.grpId}'
  version='${param.version}' />
<potato:browse name="browsePotato" pdp="true" nodeId='<%= catId %>' />
<potato:images images="imagePotato" productId='<%= productId %>' categoryId='<%= catId %>' />
<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>
<div class="pdp">
	<div class="prodDetail">
		<div class="thxgiving-bundle">
		  <soy:render template="pdp.bundleProductTitle" data="${productPotato}" />
		  <div class="thxgiving-top">
		    <div class="thxgiving-top-media">
		      ${ productExtraPotato.productDescription }
		    </div>
		    <div class="thxgiving-images">
		      <soy:render template="pdp.bundleProductImage" data="${imagePotato}" />
		    </div>
		    <%-- TODO: REVISE where data comes to templates below!! --%>
		    <div class="thxgiving-includes">
		      <soy:render template="pdp.bundleProductIncludes" data="${productPotato}" />
		    </div><!-- /thxgiving-includes  -->
		    <div class="thxgiving-actions">
		      <soy:render template="pdp.bundleProductActionsMedia" data="${productExtraPotato}" />
		      <soy:render template="pdp.bundleProductActionsDetails" data="${productPotato}" />
		    </div>
		  </div>
		  <div class="thxgiving-addtocart<%= (mobWeb) ? " prodDetail-atc" : "" %>" data-addtocart-functionality>
		    <div class="addtocart-left">
		       ${ productExtraPotato.productTermsMedia }
		       <soy:render template="pdp.bundleProductQuestions" data="${productExtraPotato}" />
		       <c:if test="${not empty productExtraPotato.productTermsMedia}">
		       <label class="agree"><input type="checkbox" name="agree" data-component="agree-terms" />I have read and agree to the terms</label>
		       </c:if>
		    </div>
		    <div class="addtocart-right center-content<%= (mobWeb) ? " pdp-atc" : "" %><%= (user.isEligibleForStandingOrders()) ? " soShow" : "" %>" data-component="product" data-eventsource="pdp_main">
		      <soy:render template="pdp.productData" data="${productPotato}" />
		      <% if (CartName.MODIFY_CART.equals(cartMode)) { %>
				<%
					ProductModel productNode=(ProductModel)request.getAttribute("productNode");
					FDCartLineI templateLine = (FDCartLineI) request.getAttribute("templateLine");
					double defaultQuantity = (templateLine != null) ? templateLine.getQuantity() : (productNode != null) ? productNode.getQuantityMinimum() : 1;
					double modPrice = (templateLine != null) ? templateLine.getPrice() : -1;
				
					((Map)productPotato).put("modCartLineId", request.getParameter("cartLine"));
					((Map)productPotato).put("modQuantity", defaultQuantity);
				%>
				<soy:render template="pdp.bundleProductModifyInCart" data="${productPotato}"/>
			<% } else { %>
				<soy:render template="pdp.bundleProductAddToCart" data="${productPotato}"/>
			<% } %>
		    </div>
		  </div>
		  <div class="thxgiving-recommendations transactional<%= (mobWeb) ? " pdp-optProducts" : "" %>" data-eventsource="tgrec">
		      <soy:render template="pdp.bundleProductRecommendation" data="${productPotato.holidayMealBundleContainer}"/>
		  </div>
		</div>
	</div>
</div>

<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.browse = window.FreshDirect.browse || {};
    window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};

    window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
    window.FreshDirect.pdp = {};
    window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true" />
    window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true" />
    window.FreshDirect.pdp.images = <fd:ToJSON object="${imagePotato}" noHeaders="true" />
    window.FreshDirect.activeDraft = "${activeDraft}"
    window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}"
</script>
