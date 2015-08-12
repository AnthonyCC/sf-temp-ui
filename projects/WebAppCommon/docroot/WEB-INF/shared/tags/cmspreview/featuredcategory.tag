<%@ tag body-content="tagdependent" description="Trending Tag" %>

<%@tag import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@tag import="com.freshdirect.fdstore.content.ProductModel"%>
<%@tag import="com.freshdirect.cms.ContentKey"%>
<%@tag import="com.freshdirect.fdstore.content.CMSPickListItemModel"%>
<%@tag import="com.freshdirect.cms.ContentType"%>
<%@tag import="com.freshdirect.cms.application.CmsManager"%>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ attribute name="section" type="com.freshdirect.fdstore.content.CMSSectionModel" required="false" %>

<c:forEach var="component" items="${section.components}">
<c:if test="${component.componentType eq 'PICKLIST'}">
<div class="moduleFeatCatLevel"><img src="${component.image.image.path}"/>
		<div>
		<h2>${component.name}</h2>
		${component.description}
		</div>
		<div style="height:5px;"><br/></div>
		<div style="height:170px;">
		<div  data-component="carousel" class="your-class">
			 <div id="carousel123" data-component="carousel-mask">
  				<ul data-component="carousel-list">
  					<c:forEach var="item" items="${component.items}">
  				<% 
				CMSPickListItemModel item = (CMSPickListItemModel) pageContext.getAttribute("item"); 
				ProductModel productModel = (ProductModel)ContentFactory.getInstance().getContentNodeByKey( new ContentKey(ContentType.get( "Product" ), item.getProduct()) );
				pageContext.setAttribute("product",productModel);
			%>
			<li class="portrait-item carouselTransactionalItem"> <img src="${product.categoryImage.path}">   <br/>  ${product.fullName}</li>
  	</c:forEach>
  				</ul>
  				<button data-component="carousel-prev" data-carousel-nav="prev">previous</button>
				<button data-component="carousel-next" data-carousel-nav="next">next</button>
 			 </div>
		</div>
		</div>
		</div>
		</c:if>
</c:forEach>