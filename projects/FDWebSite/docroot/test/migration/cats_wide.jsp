<!DOCTYPE html>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="java.util.List"%>
<html>
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
<%
class Filler {
	public void fill(CategoryModel cat, List<String> row) {
		row.add(cat.getContentKey().getId());
		row.add(cat.getFullName());
		row.add(Boolean.valueOf(cat.isNoGroupingByCategory()).toString());
		row.add(Boolean.valueOf(cat.isShowAllByDefault()).toString());
	}
}

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

List<List<String>> result = new ArrayList<List<String>>(deps.size());
int maxSize = 0;
Filler f = new Filler();
for (DepartmentModel dep : deps) {
	List<String> row = new ArrayList<String>();

	row.add(dep.getContentKey().getId());
	row.add(dep.getFullName());
	
	for (CategoryModel topCat : dep.getCategories()) {
		f.fill(topCat, row);

		for (CategoryModel subCat : topCat.getSubcategories()) {
			f.fill(subCat, row);
		}
	}

	maxSize = Math.max(maxSize, row.size());
	result.add(row);
}

%>
<body>
<div>
	<table class="main">
	<tr>
		<th>D ID</th>
		<th>DEPT</th>
		<% for (int i=0; i<(maxSize-2)/4; i++) {%>
		<th>C ID</th>
		<th>CAT</th>
		<th>NO GRP</th>
		<th>SHOW ALL</th>
		<% } %>
	</tr>

	<% for (List<String> row : result) {
    %><tr>
    	<td><%= row.get(0) %></td>
    	<td><%= row.get(1) %></td>
    <% for (int k=2; k<row.size(); k++) { %>
    	<td class="<%= ( ((k-2)/4)%2 == 0 ? "even" : "odd")  %>"><%= row.get(k) %></td>
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