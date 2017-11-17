<%@ tag import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%@ attribute name="trackingCode" required="false" rtexprvalue="true" %>
<%
		ProductModel product = productImpression.getProductModel();
%>
<div class="grid-item-name">
<display:ProductLink product="<%= product %>" trackingCode="<%= trackingCode %>">
<display:ProductName product="<%= product %>"/>
</display:ProductLink>
</div>
