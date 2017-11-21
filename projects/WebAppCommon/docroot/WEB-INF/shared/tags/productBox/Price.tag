<%@ tag import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%@ attribute name="trackingCode" required="false" rtexprvalue="true" %>
<%@ attribute name="isFullyAvailable" required="true" rtexprvalue="true" type="Boolean" %>
<%
		ProductModel product = productImpression.getProductModel();
%>
<div class="grid-item-price">
<% if ( isFullyAvailable == true ) { %>
<display:ProductDefaultPrice product="<%= product %>" showDescription="true"/>
<% if (product.getPriceCalculator().isOnSale()) { %><span class="was-price">(was <display:ProductWasPrice product="<%= product %>"/>)</span><% } %>
<display:ProductAboutPrice product="<%= product %>" />
<% } %>
<display:ProductGroupLink impression="<%= productImpression %>" trackingCode="<%= trackingCode %>"><display:ProductGroupPricing product="<%= product %>"/></display:ProductGroupLink>
</div>