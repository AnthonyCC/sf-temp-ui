<%@ tag import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%@ attribute name="trackingCode" required="false" rtexprvalue="true" %>
<%
		ProductModel product = productImpression.getProductModel();
%>
<div class="grid-item-rating">
<display:ProductLink product="<%= product %>" trackingCode="<%= trackingCode %>">
<display:ProductRating product="<%= product %>" noBr="true"/>
</display:ProductLink>
</div>
