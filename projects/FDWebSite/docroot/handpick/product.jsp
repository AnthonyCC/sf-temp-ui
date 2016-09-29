<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus id="user" />

<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}' variantId='${param.variantId}' grpId='${param.grpId}'
  version='${param.version}' />
<potato:browse name="browsePotato" pdp="true" nodeId='${param.catId}' />
<potato:images images="imagePotato" productId='${param.productId}' categoryId='${param.catId}' />

<div class="mealkit-product">
	<c:if test="${not empty param.modify}"><p class="thxgiving-bundle-modify-title">Modify Item In Cart</p></c:if>
	<div>
		<soy:render template="pdp.mealkitProductImage" data="${imagePotato}" />
	</div>
	<div id="mealkit-product-left">
	  <soy:render template="pdp.mealkitProductTitle" data="${productPotato}" />
	  
	  <div class="mealkit-product-actions">
	  	<soy:render template="pdp.mealkitProductActionsMedia" data="${productExtraPotato}" />
	  </div>
	   
	  <div class="mealkit-product-content">
	     ${ productExtraPotato.productDescription }
	  </div>
      <div class="prodDetail-accords"><%-- accordions --%>
          <ul class="pdp-accordion">
              <soy:render template="pdp.nutrition.panel" data="${productExtraPotato}"/>
              <soy:render template="pdp.accordion.allergens" data="${productExtraPotato}"/>
              <soy:render template="pdp.accordion.ingredients" data="${productExtraPotato}"/>
          </ul>
      </div>
	</div>
    <div id="mealkit-product-right">
      <c:if test="${productPotato.available}">
        <div class="mealkit-product-addtocart" data-addtocart-functionality>
          <div class="center-content<%= (user.isEligibleForStandingOrders()) ? " " : "" %>" data-component="product" data-cmeventsource="pdp_main">
            <soy:render template="pdp.productData" data="${productPotato}" />
            <soy:render template="pdp.mealkitProductAddToCart" data="${productPotato}"/>
          </div>
        </div>
      </c:if>
      <c:if test="${not productPotato.available }">
        <soy:render template="pdp.unavailability" data="${productExtraPotato}"/>
        <soy:render template="pdp.productRequest"/>
      </c:if>
    </div>

	 <div class="mealkit-recommendations transactional" data-cmeventsource="tgrec">
      	<soy:render template="pdp.mealkitProductRecommendation" data="${productPotato}"/>
  	  </div>
</div>

<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.browse = window.FreshDirect.browse || {};
    window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
    window.FreshDirect.pdp = {};
    window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true" />
    window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true" />
    window.FreshDirect.pdp.images = <fd:ToJSON object="${imagePotato}" noHeaders="true" />
</script>
