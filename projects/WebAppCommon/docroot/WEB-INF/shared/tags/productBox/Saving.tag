<%@ tag import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="false"  rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%
	
		ProductModel product = ((com.freshdirect.webapp.util.ProductImpression) getJspContext().getAttribute("pi")).getProductModel();
%>
<div class="grid-item-saving"><display:ProductSaving product="<%= product %>"/></div>
