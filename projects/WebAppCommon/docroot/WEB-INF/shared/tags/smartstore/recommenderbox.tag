<%@ tag import="com.freshdirect.storeapi.content.ContentNodeModel"
				import="com.freshdirect.fdstore.customer.FDUserI"
				import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%>
<%@ taglib uri="freshdirect" prefix='fd'%>
<%@ taglib uri="carousel" prefix='car' %>

<%@ attribute name="siteFeature" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="facility" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="excludeAlcoholicContent" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="currentNode" required="false" rtexprvalue="true" type="com.freshdirect.storeapi.content.ContentNodeModel" %>

<fd:ProductGroupRecommender id="recommendedProducts" itemCount="16" siteFeature="<%= siteFeature %>" facility="<%= facility %>" currentNode="<%= currentNode %>" excludeAlcoholicContent="<%= excludeAlcoholicContent %>">
	<div class="rightnav-ymal">
			<h3 class="rightnav-ymal-title"><%= recommendedProducts.getVariant().getServiceConfig().getPresentationTitle() %></h3>
			<car:gridCarousel user="<%= (FDUserI) session.getAttribute(SessionName.USER) %>" trkCode="recommenderbox" recommendations="<%= recommendedProducts %>" numItems="1" width="153" maxItems="8" />
	</div>
</fd:ProductGroupRecommender>
