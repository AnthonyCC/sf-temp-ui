<%@ tag import="com.freshdirect.fdstore.content.ContentNodeModel"
				import="com.freshdirect.fdstore.content.ProductModel" 
				import="com.freshdirect.fdstore.customer.FDUserI"
				import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%>
<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>

<%@ attribute name="siteFeature" required="true" rtexprvalue="true" %>
<%@ attribute name="facility" required="false" rtexprvalue="true" %>
<%@ attribute name="excludeAlcoholicContent" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="currentNode" required="false" rtexprvalue="true" type="com.freshdirect.fdstore.content.ContentNodeModel" %>

<%	// Default values
	// if ( facility == null ) facility = "recommenderbox";
	if ( excludeAlcoholicContent == null ) {excludeAlcoholicContent = false;}
%>
<fd:ProductGroupRecommender id="recommendedProducts" itemCount="16" siteFeature="<%= siteFeature %>" facility="<%= facility %>" currentNode="<%= currentNode %>" excludeAlcoholicContent="<%= excludeAlcoholicContent %>">
	<div class="rightnav-ymal">
			<h3 class="rightnav-ymal-title"><%= recommendedProducts.getVariant().getServiceConfig().getPresentationTitle() %></h3>
			<comp:gridCarousel user="<%= (FDUserI) session.getAttribute(SessionName.USER) %>" trkCode="recommenderbox" recommendations="<%= recommendedProducts %>" numItems="1" width="153" maxItems="8"/>
	</div>
</fd:ProductGroupRecommender>	
