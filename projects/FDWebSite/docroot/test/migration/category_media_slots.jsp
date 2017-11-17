<!DOCTYPE html>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.storeapi.content.ProductModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.freshdirect.storeapi.content.TagModel"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.freshdirect.test.TestSupport" %>
<%@ page import="java.lang.reflect.Method" %>
<%@ page import="com.freshdirect.storeapi.content.Html" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="/WEB-INF/shared/tld/fd-display.tld" %>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>Category media slots</title>
	<style>

	table {
		width:100%;
	}
	
	span.pager {
		padding: 5px 5px;
	}

	</style>
</head>
<body>
<%
	TestSupport obj = TestSupport.getInstance();
	
	List<CategoryModel> categories = obj.getCategories();
	List<Method> mediaMethods = obj.getMediaMethods();
	
	request.setAttribute("categories", categories);
	request.setAttribute("mediaMethods", mediaMethods);

	int itemPerPage = 100;
	int pageNumber = 1;	
	int categoryCounter = 0;
	int categoryDisplayed = 0;
	String contains = "";
	
	if (request.getParameter("itemPerPage") != null) {
		itemPerPage = Math.max(50, Integer.parseInt(request.getParameter("itemPerPage").toString()));
	}
	
	if (request.getParameter("pageNumber") != null) {
		pageNumber = Integer.parseInt(request.getParameter("pageNumber").toString());
	}
	
	if (request.getParameter("contains") != null) {
		contains = request.getParameter("contains");
	}
	
	if (!"".equals(contains)) {
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
	
	<form method="GET" action="/test/migration/category_media_slots.jsp">
		Item per page(min 50): <input id="itemPerPage" name="itemPerPage" type="text" value="<%=itemPerPage%>"/><br/>
		Category name contains string (case insensitive): <input id="contains" name="contains" type="text" value="<%=contains%>"/><br/>
		<input type="submit" value="Reload" />
	</form></br></br>
	<% if (pageNumber != 1) {  %>
	<span class="pager"><a href="/test/migration/category_media_slots.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=1%>&contains=<%=contains%>"><<</a></span>
	<span class="pager"><a href="/test/migration/category_media_slots.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=Math.max(1, pageNumber - 10)%>&contains=<%=contains%>"><</a></span>
	<% }
		for (int i = Math.max(1, Math.min(pageNumber, categories.size() / itemPerPage - 3) - 5); i <= Math.min(categories.size() / itemPerPage + 1, Math.max(1, Math.min(pageNumber, categories.size() / itemPerPage - 3) - 5) + 10); i++) {
			if (i == pageNumber) {
		%>
			<span class="pager"><%=i%></span>
		<%} else { %>
			<span class="pager"><a href="/test/migration/category_media_slots.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=i%>&contains=<%=contains%>"><%=i%></a></span>
		<% }
		}
	if (pageNumber != categories.size() / itemPerPage + 1) {
	%>
	
	<span class="pager"><a href="/test/migration/category_media_slots.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=Math.min(pageNumber + 10, categories.size() / itemPerPage + 1)%>&contains=<%=contains%>">></a></span>
	<span class="pager"><a href="/test/migration/category_media_slots.jsp?itemPerPage=<%=itemPerPage%>&pageNumber=<%=categories.size() / itemPerPage + 1 %>&contains=<%=contains%>">>></a><br/></span>
	<%} %>
	<br/>
	<table>
	<tr>
		<th>Category name</th>
		<c:forEach var="method" items="${mediaMethods}">
			<c:choose>
				<c:when test="${fn:startsWith(method.name, 'get')}">
					<th>&nbsp;${fn:substring(method.name, 3, -1)}&nbsp;</th>
				</c:when>
				<c:otherwise>
					<th>&nbsp;${method.name}&nbsp;</th>
				</c:otherwise>
			</c:choose>
		</c:forEach>
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
		<% 
		for (Method mediaMethod : mediaMethods) {
			try {
				Object result = mediaMethod.invoke(category);
				if (result instanceof Html) { %>
					<td align="center">
					<% if (((Html)result).getPath() != null) { %>
						<a href="<%=((Html)result).getPath()%>"><%=((Html)result).getPath()%></a>
					<% } 
				}%>
				</td>
			<%} catch (Exception e){%>
				<td align="center">
					Wrong media setup. <%= e.getCause() %>
				</td>
			<%}
		}
		%>
	</tr>
	<%} }%>
	</table>
</body>
</html>