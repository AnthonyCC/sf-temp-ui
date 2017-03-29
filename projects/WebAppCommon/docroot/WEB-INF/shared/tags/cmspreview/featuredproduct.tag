<%@ tag body-content="tagdependent" description="Featured Product Level" %>
<%@tag import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@tag import="com.freshdirect.fdstore.content.ProductModel"%>
<%@tag import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@tag import="com.freshdirect.cms.ContentKey"%>
<%@tag import="com.freshdirect.fdstore.content.CMSPickListItemModel"%>
<%@tag import="com.freshdirect.cms.ContentType"%>
<%@tag import="com.freshdirect.cms.application.CmsManager"%>
<%@ tag import="com.freshdirect.fdstore.content.CMSComponentType"%>
<%@ tag import="com.freshdirect.fdstore.content.CMSSectionModel"%>
<%@ attribute name="section" type="com.freshdirect.fdstore.content.CMSSectionModel" required="false" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<div class="modulePeekAhead">
<%-- <c:forEach var="component" items="${section.components}">
<c:if test="${component.componentType eq 'BANNER'}">
	<div class="ModuleBanner">
		<img src="${component.image.path}" width="${component.image.width}" height="${component.image.height}">
		<h6 style="color:green">${component.flagText}</h6>
		<h2 style="color:green">${component.name}</h2>
	</div>
</c:if>
</c:forEach> --%>

<div class="moduleFeatCatLevel"><img src="${section.imageBanner.image.path}"
			width="${section.imageBanner.image.width}"
			height="${section.imageBanner.image.height}">
		<div>
		<h2>${section.headlineText}</h2>
		<h2>${section.captionText}</h2>
		${section.bodyText}
		</div>
		<div style="height:5px;"><br/></div>
		<div style="height:170px;">
		 					<c:forEach var="item" items="${section.productList}">
  				<% 
				String item = (String) jspContext.getAttribute("item"); 
  				ProductModel productModel = (ProductModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get(item.split(":")[0]), item.split(":")[1]));
				jspContext.setAttribute("product", productModel);
			%>
			<li class="portrait-item carouselTransactionalItem"> <img src="${product.categoryImage.path}">   <br/>  ${product.fullName}</li>
			
			
  	</c:forEach>
  				<c:forEach var="item1" items="${section.categoryList}">
  				<% 
				String item1 = (String) jspContext.getAttribute("item1"); 
  				CategoryModel categoryModel = (CategoryModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get(item1.split(":")[0]), item1.split(":")[1]));
				jspContext.setAttribute("category", categoryModel);
			%>
			<li class="portrait-item carouselTransactionalItem"> <img src="${category.photo.path}">   <br/>  ${category.fullName}</li>
  	</c:forEach>
  				
		</div>
		</div>
