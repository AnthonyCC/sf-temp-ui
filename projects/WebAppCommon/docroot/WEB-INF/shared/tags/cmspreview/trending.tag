<%@tag import="com.freshdirect.fdstore.content.CMSPickListItemModel"%>
<%@tag import="com.freshdirect.cms.ContentKey"%>
<%@tag import="com.freshdirect.cms.ContentNodeI"%>
<%@tag import="com.freshdirect.cms.ContentType"%>
<%@tag import="com.freshdirect.cms.application.CmsManager"%>
<%@ tag body-content="scriptless" description="Trending Tag" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ attribute name="section" type="com.freshdirect.fdstore.content.CMSSectionModel" required="false" %>
<div class="moduleTrending">
	<c:forEach var="component" items="${section.components}">
		<c:if test="${component.componentType eq 'PICKLIST'}">
		<h2>${component.name}</h2>
		<ul>
		<c:forEach var="item" items="${component.items}">
			<% 
				CMSPickListItemModel item = (CMSPickListItemModel) pageContext.getAttribute("item"); 
				//String productName =  item.getProduct();
				ContentNodeI productNode = CmsManager.getInstance().getContentNode(new ContentKey(ContentType.get("Product"),item.getProduct())); 
				String productName = (String) productNode.getAttributeValue("FULL_NAME");
			%>
			<li><%=productName%></li>
		</c:forEach>
		</ul>
		</c:if>
	</c:forEach>
</div>