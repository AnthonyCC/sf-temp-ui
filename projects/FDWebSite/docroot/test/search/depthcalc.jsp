<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>

<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>Depth Calculator Test</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<%!
int calculateDepth(CategoryModel category) {
	int depth = 1; // the category itself is one depth
	for (CategoryModel subcat : category.getSubcategories()) {
		int d = calculateDepth(subcat) + 1;
		if (d > depth)
			depth = d;
	}
	if (category.getSubcategories().isEmpty() && !category.getProducts().isEmpty())
		depth++;
	return depth;
}
%>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<div class="section">
	<div class="section-header">Deepest depth</div>
	<%
		Set<ContentKey> deptKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.DEPARTMENT);
		int depth = 0;
		for (ContentKey deptKey : deptKeys) {
			DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNodeByKey(deptKey);
			if (department != null) {
				int d = 1; // department is one depth
				for (CategoryModel category : department.getSubcategories()) {
					int d1 = calculateDepth(category) + 1; // department is one depth + the depth of subcategory
					if (d1 > d)
						d = d1;
				}
				if (d > depth)
					depth = d;
			}
		}
	%>
	<div class="section-body">
		The highest depth is <%= depth %>.
	</div>
</div>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>
