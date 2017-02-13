<!DOCTYPE html>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="com.freshdirect.fdstore.content.TagModel"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.test.TestSupport" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>CMS Data Migration Tools and Reports</title>
	<style>
	form {
		background-color: #eee;
		padding: 2px 2px;
	}
	
	span.disc {
		color: #ccc;
	}
	
	span.head {
		font-weight: bold;
	}
	</style>
</head>
<body>
<%
	TestSupport obj = TestSupport.getInstance();
	
	boolean showProducts = request.getParameter("tag") != null && !"".equalsIgnoreCase(request.getParameter("tag"));
	request.setAttribute("showProducts", showProducts);

	if (showProducts) {
		String tagName = request.getParameter("tag");
		
		List<ProductModel> prds = (List<ProductModel>) obj.getTaggedProducts("Tag:"+tagName);
		Collections.sort(prds, new Comparator<ProductModel>() {
			public int compare(ProductModel p1, ProductModel p2) {
				CategoryModel c1 = p1.getCategory();
				CategoryModel c2 = p2.getCategory();

				int c = c1.getFullName().compareTo(c2.getFullName());
				int p = p1.getFullName().compareTo(p2.getFullName());
				if (c == 0)
					return p;
				
				return c;
			}
		});
		
		request.setAttribute("products", prds);
	}
%>
	<c:choose>
		<c:when test="${showProducts}">
			<h2>Found ${fn:length(products)} product(s) for tag '${param['tag']}'</h2>
			<h4>The following products are linked to '${param['tag']}' or its children through Product.tags, Category.productTags and Department.productTags relationships</h4>
			
			<table>
			<tr>
				<th>Category</th>
				<th>Product</th>
			</tr>
			<c:forEach var="prd" items="${products}">
			<tr>
				<td>
				<c:choose>
					<c:when test="${prd.category.hidden}">
						<span class="disc">${prd.category.fullName}</span>
					</c:when>
					<c:otherwise>
						<a href="/category.jsp?catId=${prd.category}">${prd.category.fullName}</a></td>
					</c:otherwise>
				</c:choose>
				<td>
				<c:choose>
					<c:when test="${prd.discontinued || prd.hidden}">
						<span class="disc">${prd.fullName}</span>
					</c:when>
					<c:otherwise>
						<display:ProductLink product="${prd}">${prd.fullName}</display:ProductLink>
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			Please select a tag by pressing 'Preview' on a tag content node in CMS. (Or add a 'tag' parameter to URL directly.) 
		</c:otherwise>
	</c:choose>
</body>
</html>