<%@page import="java.util.ArrayList"
%><%@page import="java.util.Collection"
%><%@page import="java.util.Collections"
%><%@page import="java.util.Comparator"
%><%@page import="java.util.List"
%><%@page import="com.freshdirect.cms.core.domain.ContentKey"
%><%@page import="com.freshdirect.fdstore.FDProductInfo"
%><%@page import="com.freshdirect.storeapi.content.CategoryModel"
%><%@page import="com.freshdirect.storeapi.content.ProductModel"
%><%@page import="com.freshdirect.storeapi.content.SkuModel"
%><%@page import="com.freshdirect.storeapi.content.TagModel"
%><%@page import="com.freshdirect.test.TestSupport"
%><%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld"
%><%
TestSupport obj = TestSupport.getInstance();

boolean showProducts = request.getParameter("tag") != null && !"".equalsIgnoreCase(request.getParameter("tag"));

if (showProducts && "export".equals(request.getParameter("action"))) {
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-disposition", "attachment; filename=\"tagged_products.csv\"");

    String tagName = request.getParameter("tag");

    List<ProductModel> prds = (List<ProductModel>) obj.getTaggedProducts("Tag:"+tagName);
%>"CATEGORY";"PRODUCT";"SKU";"MATERIAL"
<%  for (ProductModel product : prds) {
        CategoryModel homeCategory = product.getCategory();
        SkuModel sku = !product.getSkus().isEmpty() ? product.getSkus().get(0) : null;
        FDProductInfo fdproductinfo = null;
        if (sku != null) {
            try {
                fdproductinfo = sku.getProductInfo();
            } catch (Exception exc) {
            }
        }
%>"<%= homeCategory.getFullName() %>";"<%= product.getFullName() %>";"<%= sku != null ? sku.getSkuCode() : "n/a" %>";"<%= fdproductinfo != null ? fdproductinfo.getMaterialNumber() : "n/a" %>"
<% }
} else {
%><!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
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
	request.setAttribute("showProducts", showProducts);

	if (showProducts) {
		String tagName = request.getParameter("tag");
		
		List<ProductModel> prds = (List<ProductModel>) obj.getTaggedProducts("Tag:"+tagName);
		
		request.setAttribute("products", prds);
	}
%>
	<c:choose>
		<c:when test="${showProducts}">
			<h2>Found ${fn:length(products)} product(s) for tag '${param['tag']}'</h2>
			<h4>The following products are linked to '${param['tag']}' or its children through Product.tags, Category.productTags and Department.productTags relationships</h4>
            <div>
                <a href="?action=export&amp;tag=${param['tag']}">Download Data Sheet</a>
            </div>
			<table>
			<tr>
				<th>Category</th>
				<th>Product</th>
                <th>SKU</th>
                <th>Material</th>
			</tr>
			<c:forEach var="prd" items="${products}">
			<tr>
				<td>
				<c:choose>
					<c:when test="${prd.category.hidden}">
						<span class="disc">${prd.category.fullName}</span>
					</c:when>
					<c:otherwise>
						<a href="/browse.jsp?id=${prd.category}">${prd.category.fullName}</a></td>
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
				<c:choose>
				    <c:when test="${fn:length(prd.skus) > 0}">
				        <c:set var="sku" value="${prd.skus[0]}" />
				        <c:set var="fdproductinfo" value="${sku.productInfo}" />
                <td>${sku.skuCode}</td>
                <td>${fdproductinfo.materialNumber}</td>
				    </c:when>
				    <c:otherwise>
                <td>n/a</td>
                <td>n/a</td>
				    </c:otherwise>
			    </c:choose>
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
<%
}
%>