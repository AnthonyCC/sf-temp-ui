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
<potato:cartConfirm name="cartConfirmPotato" cartlineId='${param.cartlineId}'/>
<potato:recommender siteFeature="DEALS_QS" name="deals" maxItems="25"  />
<potato:recommender siteFeature="YMAL" name="ymal" maxItems="25" currentNodeKey="${cartConfirmPotato.cartLine.cmskey}"/>
<c:set target="${deals}" property="selected" value="deals" />
<div class="pdp pdp-cc">
	<div class="span-16">
		<soy:render template="pdp.cartConfirm" data="${cartConfirmPotato}" />
		<div class="span-16 first cc-ymalCarousel">
			<soy:render template="common.ymalCarousel" data="${ymal}" />
		</div>
		<div class="span-16 first cc-tabbedCarousel">
			<soy:render template="common.tabbedCarousel" data="${deals}" />
		</div>
	</div>
</div>
<script>cartConfirm=<fd:ToJSON object="${cartConfirmPotato}" noHeaders="true"/></script>