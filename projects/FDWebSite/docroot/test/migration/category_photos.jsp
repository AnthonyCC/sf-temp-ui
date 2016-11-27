<!DOCTYPE html>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.freshdirect.fdstore.content.TagModel"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.test.TestSupport" %>
<%@ page import="java.lang.reflect.Method" %>
<%@ page import="com.freshdirect.fdstore.content.Html" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>Category media slots</title>
	<style>
		span.pager {
			padding: 5px 5px;
		}
	</style>
</head>
<body>
<%
	TestSupport obj = TestSupport.getInstance();
	
	List<CategoryModel> categories = obj.getCategories();
	
	request.setAttribute("categories", categories);

	int itemPerPage = 100;
	int pageNumber = 1;	
	int categoryCounter = 0;
	int categoryDisplayed = 0;
	String contains = "";
	String missing = "";
	
	if (request.getParameter("itemPerPage") != null) {
		itemPerPage = Math.max(50, Integer.parseInt(request.getParameter("itemPerPage").toString()));
	}
	
	if (request.getParameter("pageNumber") != null) {
		pageNumber = Integer.parseInt(request.getParameter("pageNumber").toString());
	}
	
	if (request.getParameter("contains") != null) {
		contains = request.getParameter("contains");
	}
	
	if (request.getParameter("missing") != null) {
		missing = request.getParameter("missing");
	}

	if ("missing".equals(missing)) {
		Iterator<CategoryModel> iterator = categories.iterator();
		while (iterator.hasNext()) {
			CategoryModel cat = iterator.next();
			if (cat.getPhoto() != null) {
				iterator.remove();
			}
		}
		
	} else if (!"".equals(contains)) {
		Iterator<CategoryModel> iterator = categories.iterator();
		while (iterator.hasNext()) {
			CategoryModel cat = iterator.next();
			if (!(cat.getFullName() == null ? "" : cat.getFullName().toLowerCase()).contains(contains.toLowerCase())) {
				iterator.remove();
			}
		}
	}
%>
	<h2>${fn:length(categories)} categories</h2>
	
	<form method="GET" action="/test/migration/category_photos.jsp">
		Item per page(min 50): <input id="itemPerPage" name="itemPerPage" type="text" value="<%=itemPerPage%>"/><br/>
		Category name contains string (case insensitive): <input id="contains" name="contains" type="text" value="<%=contains%>"/><br/>
		Show only categories with missing category photo: <input id="missing" name="missing" type="checkbox" value="<%=missing%>" <%="missing".equals(missing) ? "checked" : ""%>/><br/>
		<input type="submit" value="Reload" />
	</form></br></br>
	<% if (pageNumber != 1) {  %>
	<span class="pager"><a href="/test/migration/category_photos.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=1%>&contains=<%=contains%>&missing=<%=missing%>"><<</a></span>
	<span class="pager"><a href="/test/migration/category_photos.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=Math.max(1, pageNumber - 10)%>&contains=<%=contains%>&missing=<%=missing%>"><</a></span>
	<% }
		for (int i = Math.max(1, Math.min(pageNumber, categories.size() / itemPerPage - 3) - 5); i <= Math.min(categories.size() / itemPerPage + 1, Math.max(1, Math.min(pageNumber, categories.size() / itemPerPage - 3) - 5) + 10); i++) {
			if (i == pageNumber) {
		%>
			<span class="pager"><%=i%></span>
		<%} else { %>
			<span class="pager"><a href="/test/migration/category_photos.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=i%>&contains=<%=contains%>&missing=<%=missing%>"><%=i%></a></span>
		<% }
		}
	if (pageNumber != categories.size() / itemPerPage + 1) {
	%>
	
	<span class="pager"><a href="/test/migration/category_photos.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=Math.min(pageNumber + 10, categories.size() / itemPerPage + 1)%>&contains=<%=contains%>&missing=<%=missing%>">></a></span>
	<span class="pager"><a href="/test/migration/category_photos.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=categories.size() / itemPerPage + 1 %>&contains=<%=contains%>&missing=<%=missing%>">>></a><br/></span>
	<%} %>
	<br/>
	<table>
	<tr>
		<th>Category name</th>
		<th>Category photo</th>
	</tr>
	<% for (CategoryModel category : categories) {
		categoryCounter ++;
		if (categoryCounter >= (pageNumber - 1) * itemPerPage && categoryDisplayed <= itemPerPage) {
			categoryDisplayed ++;
	%>
	<tr>
		<td>
			<a href="/category.jsp?catId=<%=category.getContentKey().getId() %>"><%=category.getFullName() == null ? "No category fullName." : category.getFullName() + "(" + category.getContentKey().getId() + ")" %></a>
		</td>
		<td>
			<%if (category.getPhoto() == null) { %>
				<b>No category image set.</b>
			<%} else { %>
				<img src="<%=category.getPhoto().getPath()%>" />
			<%} %>
		</td>
	</tr>
	<%} }%>
	</table>
</body>
</html>