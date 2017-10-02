<!DOCTYPE html>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="com.freshdirect.fdstore.content.TagModel"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.test.TestSupport" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>Mixed categories</title>
	<style>
	</style>
</head>
<body>
<%
	TestSupport obj = TestSupport.getInstance();
	
	List<CategoryModel> cats = new ArrayList<CategoryModel>();
	for (ContentKey key : CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY)) {
		CategoryModel cat = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
		if (/* cat.isHidden() || */ cat.isOrphan())
			continue;

		if (cat.getPrivateProducts().size() > 0 && cat.getSubcategories().size() > 0) {
			cats.add(cat);
		}
	}
	
	Collections.sort(cats, new Comparator<CategoryModel>() {
		public int compare(CategoryModel o1, CategoryModel o2) {
			int d = o1.getDepartment().getFullName().compareTo(o2.getDepartment().getFullName());
			return d == 0 ? o1.getFullName().compareTo(o2.getFullName()) : d;
		}
	});
	
	request.setAttribute("cats", cats);	
%>
<table>
<tr>
	<th>Department</th>
	<th>Category ID</th>
	<th>Status</th>
	<th>Category Name</th>
</tr>
<c:forEach var="cat" items="${cats}">
<tr>
	<td><a href="/department.jsp?deptId=${cat.department.contentKey.id}">${cat.department.fullName}</a></td>
	<td>${cat.contentKey.id}</td>
	<td>
	<c:choose>
	<c:when test="${cat.orphan}">orphan</c:when>
	<c:when test="${cat.hidden}">hidden</c:when>
	</c:choose>
	</td>
	<td><a href="/category.jsp?catId=${cat.contentKey.id}">${cat.fullName}</a></td>
</tr>
</c:forEach>
</table>
</body>
</html>