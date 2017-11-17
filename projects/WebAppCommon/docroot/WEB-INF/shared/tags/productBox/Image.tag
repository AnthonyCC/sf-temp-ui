<%@ tag import="com.freshdirect.fdstore.FDProduct"%>
<%@ tag import="com.freshdirect.fdstore.FDCachedFactory" %>
<%@ tag import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ tag import="com.freshdirect.WineUtil" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ attribute name="productImpression" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ProductImpression" %>
<%@ attribute name="trackingCode" required="false" rtexprvalue="true" %>
<%@ attribute name="imageSize" required="false" rtexprvalue="true" %>
<%
		ProductModel product = productImpression.getProductModel();
%>
<display:ProductBurstClass id="burstClass" product="<%= product %>" hideDeal="true">
<div class="grid-item-image">
<display:ProductLink product="<%= product %>" trackingCode="<%= trackingCode %>">
<%
		String imageSize = "detail";

		FDProduct fdproduct = null;

		try {
        	fdproduct = FDCachedFactory.getProduct(productImpression.getProductInfo());
           
		if ( WineUtil.getWineAssociateId().equalsIgnoreCase(product.getDepartment().toString()) && (fdproduct != null && !"".equals(fdproduct.getMaterial().getAlcoholicContent().getCode())) ) {
			imageSize = "zoom";
		}
       } catch (Exception fdsnf){
       }
%>
<span class="<%= burstClass %>"></span><display:SimpleProductImage product="<%= product %>" size="<%= imageSize %>"/></display:ProductLink>
</div>
</display:ProductBurstClass>