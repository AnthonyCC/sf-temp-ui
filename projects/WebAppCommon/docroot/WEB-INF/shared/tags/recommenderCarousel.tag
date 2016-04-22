<%@ tag import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.webapp.util.ConfigurationContext"
		import="com.freshdirect.webapp.util.ConfigurationStrategy"
		import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"
		import="com.freshdirect.webapp.util.FDURLUtil"
		import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil" 
		import="com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag"
		import="java.util.Collections"
		import="com.freshdirect.webapp.util.TransactionalProductImpression"
		import="com.freshdirect.webapp.util.ProductImpression"
		import="com.freshdirect.fdstore.content.EnumBurstType"
		import="com.freshdirect.webapp.taglib.fdstore.SessionName"
		import="com.freshdirect.fdstore.customer.FDUserI"
		import="com.freshdirect.fdstore.FDSkuNotFoundException"
		import="com.freshdirect.fdstore.*"
		import="com.freshdirect.fdstore.content.ProductModel"
		import="com.freshdirect.fdstore.FDProduct"
%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ attribute name="id" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="trkCode" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="width" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="numItems" required="false" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="maxItems" required="false" rtexprvalue="true" type="java.lang.Integer" %>
<%@ attribute name="siteFeature" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="facility" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="user" required="true" rtexprvalue="true" type="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ attribute name="excludeAlcoholicContent" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="currentNode" required="false" rtexprvalue="true" type="com.freshdirect.fdstore.content.ContentNodeModel" %>
<%
	if ( excludeAlcoholicContent == null ) {excludeAlcoholicContent = false;}
%>
	<fd:ProductGroupRecommender siteFeature="<%= siteFeature %>" facility="<%= facility %>" id="recommendedProducts" itemCount="<%=maxItems%>" currentNode="<%= currentNode %>" excludeAlcoholicContent="<%= excludeAlcoholicContent %>">
		<comp:gridCarousel id="<%= id %>" width="<%= width %>" numItems="<%= numItems %>" recommendations="<%= recommendedProducts %>" trkCode="<%= trkCode %>" maxItems="<%= maxItems %>" user="<%= user %>" />
	</fd:ProductGroupRecommender>
