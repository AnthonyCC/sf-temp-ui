<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<div class="grid-item-price-description"><display:ProductPriceDescription impression="<%= productImpression %>"/></div>
