<!DOCTYPE html>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@page import="com.freshdirect.storeapi.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.storeapi.application.CmsManager"%>
<%@page import="com.freshdirect.storeapi.content.ContentNodeModel"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.storeapi.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="com.freshdirect.storeapi.content.TagModel"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.test.TestSupport" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>Deep categories</title>
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

		if (cat.getCategoryLevel() > 2) {
			cats.add(cat);
		}
	}
	
	Collections.sort(cats, new Comparator<CategoryModel>() {
		public int compare(CategoryModel o1, CategoryModel o2) {
			final int d1 = o1.getCategoryLevel();
			final int d2 = o2.getCategoryLevel();

			final int k = o1.getDepartment().getFullName().compareTo(o2.getDepartment().getFullName());
			
			return k == 0 ? -1 * (d1-d2) : k;
		}
	});
	
	request.setAttribute("cats", cats);	
%>
<table>
<tr>
	<th>Department</th>
	<th>Category ID</th>
	<th>Level</th>
	<th>Category Name</th>
</tr>
<c:forEach var="cat" items="${cats}">
<tr>
	<td><a href="/department.jsp?deptId=${cat.department.contentKey.id}">${cat.department.fullName}</a></td>
	<td>${cat.contentKey.id}</td>
	<td>${cat.categoryLevel}</td>
	<td><a href="/category.jsp?catId=${cat.contentKey.id}">${cat.fullName}</a></td>
</tr>
</c:forEach>
</table>
</body>
</html>