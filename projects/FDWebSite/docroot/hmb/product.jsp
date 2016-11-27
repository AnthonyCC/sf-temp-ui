<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus id="user" />
<fd:CheckDraftContextTag/>
<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}' variantId='${param.variantId}' grpId='${param.grpId}'
  version='${param.version}' />
<potato:browse name="browsePotato" pdp="true" nodeId='${param.catId}' />
<potato:images images="imagePotato" productId='${param.productId}' categoryId='${param.catId}' />
<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>
<div class="pdp">
	<div class="prodDetail">
		<div class="thxgiving-bundle">
		  <c:if test="${not empty param.modify}"><p class="thxgiving-bundle-modify-title">Modify Item In Cart</p></c:if>
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
		    <div class="addtocart-right center-content<%= (mobWeb) ? " pdp-atc" : "" %><%= (user.isEligibleForStandingOrders()) ? " soShow" : "" %>" data-component="product" data-cmeventsource="pdp_main">
		      <soy:render template="pdp.productData" data="${productPotato}" />
		      <soy:render template="pdp.bundleProductAddToCart" data="${productPotato}"/>
		    </div>
		  </div>
		  <div class="thxgiving-recommendations transactional<%= (mobWeb) ? " pdp-optProducts" : "" %>" data-cmeventsource="tgrec">
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
