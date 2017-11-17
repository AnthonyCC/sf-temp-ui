<%@ tag import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.webapp.util.ConfigurationContext"
		import="com.freshdirect.webapp.util.ConfigurationStrategy"
		import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"
		import="com.freshdirect.webapp.util.FDURLUtil"
		import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil" 
		import="com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag"
		import="java.util.*"
		import="com.freshdirect.webapp.util.TransactionalProductImpression"
		import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.fdstore.content.EnumBurstType"
		import="com.freshdirect.webapp.taglib.fdstore.SessionName"
		import="com.freshdirect.fdstore.customer.FDUserI"
		import="com.freshdirect.fdstore.FDSkuNotFoundException"
		import="com.freshdirect.fdstore.*"
		import="com.freshdirect.storeapi.content.ProductModel"
		import="com.freshdirect.fdstore.FDProduct"
		import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"
		import="com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem"
		import="com.freshdirect.webapp.soy.SoyTemplateEngine"		
%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="/WEB-INF/shared/tld/soy.tld" prefix='soy' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ attribute name="id" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="maxItems" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="siteFeature" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="facility" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ attribute name="excludeAlcoholicContent" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="currentNode" required="false" rtexprvalue="true" type="com.freshdirect.storeapi.content.ContentNodeModel" %>
<%@ attribute name="itemTemplate" required="false" rtexprvalue="true" type="java.lang.String" %>
<%
	if ( excludeAlcoholicContent == null ) excludeAlcoholicContent = false;
	if ( itemTemplate == null ) itemTemplate = "common.gridItem";
%>
<fd:ProductGroupRecommender siteFeature="<%= siteFeature %>" facility="<%= facility %>" id="recommendedProducts" itemCount="<%=maxItems%>" currentNode="<%= currentNode %>" excludeAlcoholicContent="<%= excludeAlcoholicContent %>">
<div data-component="carousel"> 
<ul data-component="carousel-list">
<logic:iterate collection="<%= recommendedProducts.getProducts() %>" id="product" type="com.freshdirect.storeapi.content.ProductModel">
<%
	QuickShopLineItem productLineItem = QuickShopHelper.createItemFromProduct(product, null, user, true);
	Map<String,Object> dataMap = new HashMap<String,Object>();
	dataMap.put("item",SoyTemplateEngine.convertToMap( productLineItem ));
%>
	<soy:render template="<%= itemTemplate %>" data="<%= dataMap %>" />
</logic:iterate>
</ul>
<button data-component="carousel-prev">previous</button>
<button data-component="carousel-next">next</button>
</div>
</fd:ProductGroupRecommender>
