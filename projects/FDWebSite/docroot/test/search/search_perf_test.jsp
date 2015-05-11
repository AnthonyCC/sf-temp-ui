<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.cms.search.SearchHit"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearchUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.Recipe"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%><html>
<fd:CheckLoginStatus id="user" noRedirect="true"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>The Search Test</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<%!
long time = 0;
NumberFormat intFormat = NumberFormat.getIntegerInstance();

void start() {
	time = System.currentTimeMillis();
}

long end() {
	return System.currentTimeMillis() - time;
}

String format(long time) {
	return intFormat.format(time) + "ms";	
}

public void isOrphan(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.isOrphan();
		}
	}
}

public void isHidden(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.isOrphan();
		}
	}
}

public void getPrimarySubcategory(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.getPrimarySubcategory();
		}
	}
}

public void isSearchable(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.isSearchable();
		}
	}
}

public void isAvailable(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.isAvailable();
		}
	}
}

public void isActive(Collection<SearchHit> hits) {
	for (SearchHit hit : hits) {
		if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
			Recipe recipe = (Recipe) hit.getNode();
			recipe.isActive();
		}
	}
}
%>
<body>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<div class="float-left">
<%  if (user != null) {
		if (user.getIdentity() != null) {
%>
Logged in as <%= user.getUserId() %>.
<%      } else { %>
Not logged in. You are in area  <%= user.getZipCode() %>.
<% 		}
	} else { %>
Not logged in.
<% 	} %>
</div>
<div class="right float-right">
	<a href="search_dictionary.jsp" target="_search_terms_dictionary">Dictionary</a>
</div>
<div class="float-close"></div>
<%
String searchTerm = request.getParameter("searchParams");
if (searchTerm != null && searchTerm.trim().isEmpty()) {
	searchTerm = null;
}
if (searchTerm != null)
	searchTerm = searchTerm.trim();

%>
<div style="padding-top: 15px;">
	<span style="font-weight: bold;">Specify search term:</span>
	<input type="text" name="searchParams"<%= searchTerm != null ? "value=\"" + StringEscapeUtils.escapeHtml(searchTerm) + "\"" : "" %>>
	<input type="submit" value="Submit">
	&nbsp;
	<% if (searchTerm != null) { %><a href="/search.jsp?searchParams=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(searchTerm, "iso-8859-1")) %>" target="_search_page">Show on search page</a><% } %>
</div>
<%
if (searchTerm != null && !searchTerm.trim().isEmpty()) {
%>
<div class="section">
	<div class="section-header">Search Perf Test Results</div>
	<%
		start();
		Collection<SearchHit> hits = CmsManager.getInstance().searchProducts(searchTerm, false, false, ContentSearch.getInstance().getSearchMaxHits());
		long s = end();
		start();
		List<SearchHit> productHits = ContentSearchUtil.filterProductHits(hits);
		long p = end();

		start();
		List<SearchHit> recipeHits = ContentSearchUtil.filterRecipeHits(hits);
		long r = end();
		start();
		isOrphan(hits);
		long r1 = end();
		start();
		isHidden(hits);
		long r2 = end();
		start();
		getPrimarySubcategory(hits);
		long r3 = end();
		start();
		isSearchable(hits);
		long r4 = end();
		start();
		isAvailable(hits);
		long r5 = end();
		start();
		isActive(hits);
		long r6 = end();

		start();
		List<SearchHit> categoryHits = ContentSearchUtil.filterCategoryHits(hits);
		long c = end();
	%>
	<div class="section-body">
		<table border="1">
			<tr>
				<th>Search</th>
				<th>Filter products</th>
				<th>Filter recipes</th>
				<th>Orphan</th>
				<th>Hidden</th>
				<th>Primary Subcategory</th>
				<th>Searchable</th>
				<th>Available</th>
				<th>Active</th>
				<th>Filter categories</th>
			</tr>
			<tr>
				<td><%= format(s) %></td>
				<td><%= format(p) %></td>
				<td><%= format(r) %></td>
				<td><%= format(r1) %></td>
				<td><%= format(r2) %></td>
				<td><%= format(r3) %></td>
				<td><%= format(r4) %></td>
				<td><%= format(r5) %></td>
				<td><%= format(r6) %></td>
				<td><%= format(c) %></td>
			</tr>
		</table>
	</div>
</div>
<%
}
%>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>