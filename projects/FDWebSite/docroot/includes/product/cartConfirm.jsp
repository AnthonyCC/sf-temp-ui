<%@ page import="com.freshdirect.webapp.ajax.product.ProductConfigServlet"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigRequestData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Sku"%>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ page import="com.freshdirect.webapp.ajax.cart.data.AddToCartItem"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<potato:cartConfirm name="cartConfirmPotatoes" cartlineId='${param.cartlineId}'/>
<c:if test="${cartConfirmPotatoes == null}">
	<jsp:forward page="/checkout/view_cart.jsp"/>
</c:if>

<c:set var="potatoes" value='${cartConfirmPotatoes["cartConfirmPotatoes"]}'/>

<div class="pdp pdp-cc">
  <soy:render template="pdp.cartConfirmIterator" data="${cartConfirmPotatoes}" />
	<div class="span-16 first cc-ymalCarousel">
		<script>
          	$jq.ajax('/carousel/carousel.jsp?type=ymal&currentNodeKey=${potatoes[0].cartLine.cmskey}').then(function(page) {
          		$jq('.pdp-cc .cc-ymalCarousel').html(page);
              FreshDirect.components.carousel.changePage($jq('.pdp-cc .cc-ymalCarousel [data-component="carousel"]').first(), null);
          	});
        </script>
	</div>
	<div class="span-16 first cc-tabbedCarousel">
		<script>
          	$jq.ajax('/carousel/carousel.jsp?type=deals').then(function(page) {
          		$jq('.pdp-cc .cc-tabbedCarousel').html(page);
          	});
        </script>
	</div>
</div>
<script>cartConfirm=<fd:ToJSON object="${cartConfirmPotatoes}" noHeaders="true"/></script>
