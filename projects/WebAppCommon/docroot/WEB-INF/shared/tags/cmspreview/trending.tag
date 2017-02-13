<%@ tag body-content="tagdependent" description="Trending Tag" %>
<%@tag import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@tag import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@tag import="com.freshdirect.fdstore.content.ProductModel"%>
<%@tag import="com.freshdirect.fdstore.content.CMSPickListItemModel"%>
<%@tag import="com.freshdirect.cms.ContentKey"%>
<%@tag import="com.freshdirect.cms.ContentNodeI"%>
<%@tag import="com.freshdirect.cms.ContentType"%>
<%@tag import="com.freshdirect.cms.application.CmsManager"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ attribute name="section" type="com.freshdirect.fdstore.content.CMSSectionModel" required="false" %>
<div class="moduleTrending">
	<%-- <c:forEach var="component" items="${section.components}">
		<c:if test="${component.componentType eq 'PICKLIST'}">
		<h2>${component.name}</h2>
		<ul>
		<c:forEach var="item" items="${component.items}">
			<% 
				CMSPickListItemModel item = (CMSPickListItemModel) pageContext.getAttribute("item"); 
				//String productName =  item.getProduct();
				ContentNodeI productNode = CmsManager.getInstance().getContentNode(ContentKey.getContentKey(ContentType.get("Product"),item.getProduct())); 
				String productName = (String) productNode.getAttributeValue("FULL_NAME");
			%>
			<li><%=productName%></li>
		</c:forEach>
		</ul>
		</c:if>
	</c:forEach> --%>
	<div>
		<h2>${section.headlineText}</h2>
		${section.bodyText}
	</div>
	<div style="height: 5px;">
		<br />
	</div>
	<div style="height: 170px;">
			<c:forEach var="item" items="${section.productList}">
  				<% 
				String item = (String) pageContext.getAttribute("item"); 
  				if(item!=null){
  				ProductModel productModel = (ProductModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get(item.split(":")[0]), item.split(":")[1]));
				pageContext.setAttribute("product", productModel);
			%>
			<li class="portrait-item carouselTransactionalItem"> <img src="${product.categoryImage.path}">   <br/>  ${product.fullName}</li>
			<%} %>
			
  	</c:forEach>
		<c:forEach var="item1" items="${section.categoryList}">
			<% 
				String item1 = (String) pageContext.getAttribute("item1"); 	
  				CategoryModel categoryModel = (CategoryModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get(item1.split(":")[0]), item1.split(":")[1]));
				pageContext.setAttribute("category", categoryModel);
			%>
			<li class="portrait-item carouselTransactionalItem"> <img src="${category.photo.path}"><br/>${category.fullName}</li>			
		</c:forEach>
	</div>
</div>