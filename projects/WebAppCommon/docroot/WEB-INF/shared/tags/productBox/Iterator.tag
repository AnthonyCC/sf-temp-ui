<%@ tag import="java.util.Iterator" %>
<%@ tag import="com.freshdirect.webapp.util.ProductImpression" %>
<%@ tag import="com.freshdirect.webapp.util.ConfigurationContext" %>
<%@ tag import="com.freshdirect.webapp.util.ConfigurationStrategy" %>
<%@ tag import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy" %>
<%@ tag import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ attribute name="products" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="configurationContext" required="true" rtexprvalue="true" type="com.freshdirect.webapp.util.ConfigurationContext" %>
<%
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

	for (Iterator<ProductModel> it=products.iterator() ; it.hasNext();) {
		ProductModel pm = it.next();
		ProductImpression pi = confStrat.configure(pm, configurationContext);
		getJspContext().setAttribute("pi",pi);
		%><div class="grid-item-container">
			<display:ProductAvailability id="productAvailability" product="<%= pm %>"><%
	String unavailableClass = "";
	if (!productAvailability.isFullyAvailable())
		unavailableClass = "grid-item-unavailable";
%>
		
			<div class="grid-item <%= unavailableClass %> <fd:ProductCartStatus product="<%= pm %>"/>"><jsp:doBody  /></div>
		</div></display:ProductAvailability> <%
	}
%>
