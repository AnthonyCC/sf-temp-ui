<%@ tag body-content="tagdependent" description="Trending Tag" %>
<%@tag import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@tag import="com.freshdirect.storeapi.content.ProductModel"%>
<%@tag import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@tag import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@tag import="com.freshdirect.cms.core.domain.ContentKeyFactory"%>
<%@tag import="com.freshdirect.storeapi.content.CMSPickListItemModel"%>
<%@tag import="com.freshdirect.cms.core.domain.ContentType"%>
<%@tag import="com.freshdirect.storeapi.application.CmsManager"%>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ attribute name="section" type="com.freshdirect.storeapi.content.CMSSectionModel" required="false" %>
<div class="moduleFeatCatLevel"><img src="${section.imageBanner.image.path}"
			width="${section.imageBanner.image.width}"
			height="${section.imageBanner.image.height}">
		<div>
		<h2>${section.headlineText}</h2>
		<h2>${section.captionText}</h2>
		${section.bodyText}
		<button formaction="${section.linkURL}">${section.linkText}</button>
		</div>
		<div style="height:5px;"><br/></div>
		<div style="height:170px;">
		<div  data-component="carousel" class="your-class">
			 <div id="carousel123" data-component="carousel-mask">
  				<ul data-component="carousel-list">
  					<c:forEach var="item" items="${section.productList}">
  				<% 
				String item = (String) pageContext.getAttribute("item"); 
  				ProductModel productModel = (ProductModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKeyFactory.get(ContentType.valueOf(item.split(":")[0]), item.split(":")[1]));
				pageContext.setAttribute("product", productModel);
			%>
			<!-- <li class="portrait-item carouselTransactionalItem"> --> <img src="${product.categoryImage.path}">  <!--  <br/> -->  ${product.fullName}<!-- </li> -->
  	</c:forEach>
  	<c:forEach var="item1" items="${section.categoryList}">
  				<% 
				String item1 = (String) pageContext.getAttribute("item1"); 
  				CategoryModel categoryModel = (CategoryModel) ContentFactory
						.getInstance().getContentNodeByKey(ContentKeyFactory.get(ContentType.valueOf(item1.split(":")[0]), item1.split(":")[1]));
				pageContext.setAttribute("category", categoryModel);
			%>
			<!-- <li class="portrait-item carouselTransactionalItem">  --><img src="${category.photo.path}">  <!--  <br/>  --> ${category.fullName}<!-- </li> -->
  	</c:forEach>
  				</ul>
  				<button data-component="carousel-prev" data-carousel-nav="prev">previous</button>
				<button data-component="carousel-next" data-carousel-nav="next">next</button>
 			 </div>
		</div>
		</div>
		</div>