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

<div class="pdp pdp-cc">
	<soy:render template="serverRendered.pdp.cartConfirmIterator" data="${cartConfirmPotatoes}" injectAdditonalData="false" />
	<div class="span-16 first cc-ymalCarousel">
		<script>
			$jq(document).ready(function() {
	          	$jq.ajax('/api/carousel?siteFeature=YMAL&maxItems=25&cmEventSource=CC_YMAL&sendVariant=true&type=ymal&currentNodeKey=${cartConfirmPotatoes["cartConfirmPotatoes"][0].cartLine.cmskey}').then(function(data) {
	          		$jq('.pdp-cc .cc-ymalCarousel').html(common.ymalCarousel(data));
              FreshDirect.components.carousel.changePage($jq('.pdp-cc .cc-ymalCarousel [data-component="carousel"]').first(), null);
          		});
			});
        </script>
	</div>
	<div class="span-16 first cc-tabbedCarousel">
		<script>
			$jq(document).ready(function() {
	          	$jq.ajax('/api/carousel?type=deals&siteFeature=DEALS_QS&maxItems=15&cmEventSource=cc_tabbedRecommender').then(function(data) {
	          		if (data) {
	          			data.selected = 'deals';
	          			$jq('.pdp-cc .cc-tabbedCarousel').html(common.tabbedCarousel(data));
	          		}
	          	});
			});
        </script>
	</div>
</div>
