<!DOCTYPE html>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@page import="com.freshdirect.storeapi.application.CmsManager"%>
<%@page import="com.freshdirect.storeapi.fdstore.FDContentTypes"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.storeapi.content.DepartmentModel"%>
<%@page import="java.util.List"%>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>Wide Report</title>
	<style>
		table.main {
			border: 1px solid #666;
			border-collapse: collapse;
		}
		table.main td, table.main th {
			border: 1px solid #666;
		}
	
		table.main td.even {
			color: #88a;
		}
	
		table.main td.odd {
			color: #a88;
		}

		.main tr {
			font-size: 10px;
		}
	
		.skipped {
			margin: 0;
			padding: 5px 5px;
		}
		.skipped li {
			font-size: 12px;
			display: inline-block;
			margin-right: 10px;
		}
	</style>
</head>
<%!
void processCategory(CategoryModel cat, List<CategoryModel> out) {
	if (cat == null || cat.isOrphan() || cat.isHidden()) {
		return;
	}
	
	out.add(cat);
	
	// process subcats
	for (CategoryModel subCat : cat.getSubcategories()) {
		processCategory(subCat, out);
	}
}
%>
<%
List<DepartmentModel> deps = new ArrayList<DepartmentModel>();
List<DepartmentModel> skippedDeps = new ArrayList<DepartmentModel>();
for (ContentKey key : CmsManager.getInstance().getContentKeysByType(FDContentTypes.DEPARTMENT)) {
	DepartmentModel dep = (DepartmentModel) ContentFactory.getInstance().getContentNodeByKey(key);

	if (dep == null) {
		continue;
	}

	if (	"Archive".equalsIgnoreCase( key.getId()) ||
			"vin".equalsIgnoreCase( key.getId()) ||
			"kosher_temp".equalsIgnoreCase( key.getId()) ||
			dep.isOrphan() || dep.isHidden()) {
		skippedDeps.add(dep);
		continue;
	}

	deps.add(dep);
}

// -- new part -- //
List<CategoryModel> cats = new ArrayList<CategoryModel>();
for (DepartmentModel dep : deps) {
	for (CategoryModel c : dep.getCategories()) {
		processCategory(c, cats);
	}
}

int maxDepth = 0;
for (CategoryModel cat : cats) {
	maxDepth = Math.max(maxDepth, cat.getCategoryLevel());
}

%>
<body>
<div>
	<table class="main">
	<tr>
		<th>D ID</th>
		<th>DEPT</th>
		<% for (int i=1; i<=maxDepth; i++) {%>
		<th>C ID<%= i %></th>
		<th>CAT<%= i %></th>
		<th>NO GRP<%= i %></th>
		<th>SHOW ALL<%= i %></th>
		<% } %>
	</tr>

	<% for (CategoryModel cat : cats) {
		DepartmentModel dep = cat.getDepartment();
		
		LinkedList<CategoryModel> path = new LinkedList<CategoryModel>();
		path.push(cat);
		while (cat.getParentNode() instanceof CategoryModel) {
			cat = (CategoryModel) cat.getParentNode();
			path.push(cat);
		}
		int k = maxDepth;
		Iterator<CategoryModel> it = path.iterator();
    %><tr>
    	<td><%= dep.getContentKey().getId() %></td>
    	<td><%= dep.getFullName() %></td>
    <% while (it.hasNext()) {
    	CategoryModel c = it.next();
    	%>
    	<td><%= c.getContentKey().getId() %></td>
    	<td><%= c.getFullName() %></td>
    	<td><%= Boolean.valueOf(c.isNoGroupingByCategory()).toString().toUpperCase() %></td>
    	<td><%= Boolean.valueOf(c.isShowAllByDefault()).toString().toUpperCase() %></td>
	<% k--;
	} %>
	<% for (int i=0; i<k; i++) { %>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	<% } %>
    </tr>
	<% } %>
	</table>

	<h4>Skipped Departments</h4>
	<ul class="skipped">
	<% for (DepartmentModel dep : skippedDeps) { %>
		<li title="<%= dep.getContentKey().getId() %>"><%= dep.getFullName() %></li>
	<% } %>
	</ul>
</div>
</body>
</html>