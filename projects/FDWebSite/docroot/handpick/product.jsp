<%@ page import="com.freshdirect.webapp.taglib.fdstore.CartName" %>
<%@ page import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCartLineI" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fd:CheckLoginStatus id="user" />
<%
	String cartMode = (String) request.getAttribute("cartMode");
	String productId = NVL.apply(request.getParameter("productId"),NVL.apply(request.getAttribute("modProductId"), "")).toString();
	String catId = NVL.apply(request.getParameter("catId"),NVL.apply(request.getAttribute("modCatId"), "")).toString();
%>
<potato:product name="productPotato" extraName="productExtraPotato" productId='<%= productId %>' categoryId='<%= catId %>' variantId='${param.variantId}' grpId='${param.grpId}'
  version='${param.version}' />
<potato:browse name="browsePotato" pdp="true" nodeId='<%= catId %>' />
<potato:images images="imagePotato" productId='<%= productId %>' categoryId='<%= catId %>' />


<div class="mealkit-product">
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
					<% if (CartName.MODIFY_CART.equals(cartMode)) { %>
						<%
							ProductModel productNode=(ProductModel)request.getAttribute("productNode");
							FDCartLineI templateLine = (FDCartLineI) request.getAttribute("templateLine");
							double defaultQuantity = (templateLine != null) ? templateLine.getQuantity() : (productNode != null) ? productNode.getQuantityMinimum() : 1;
							double modPrice = (templateLine != null) ? templateLine.getPrice() : -1;
						
							((Map)productPotato).put("modCartLineId", request.getParameter("cartLine"));
							((Map)productPotato).put("modQuantity", defaultQuantity);
						%>
						<soy:render template="pdp.mealkitProductModifyInCart" data="${productPotato}"/>
					<% } else { %>
						<soy:render template="pdp.mealkitProductAddToCart" data="${productPotato}"/>
					<% } %>
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
