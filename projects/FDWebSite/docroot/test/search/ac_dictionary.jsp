<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>

<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="com.freshdirect.cms.search.AutoComplete"%>
<%@page import="com.freshdirect.cms.search.AutocompleteService"%>
<%@page import="java.util.Collections"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Autocomplete Dictionary</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<div class="section">
	<%
		Set<String> terms = Collections.emptySet();
		AutoComplete prodAc = ContentSearch.getInstance().getProductAutocomplete();
		if (prodAc != null) {
			AutocompleteService service = prodAc.getService();
			if (service != null) {
				Set<String> prefixes = service.getPrefixes();
				if (prefixes != null)
					terms = prefixes;
			}
				
		}
	%>
	<div class="section-header">Product/Recipe Dictionary (<%= terms.size() %> terms)</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Term</th>
				</tr>
			</thead>
			<tbody>
				<% for (String term : terms) { %>
				<tr>
					<td><%= term %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div>
<div class="section">
	<div class="section-header">Brand Dictionary</div>
	<%
		Set<String> terms2 = Collections.emptySet();
		AutoComplete brandAc = ContentSearch.getInstance().getBrandAutocomplete();
		if (brandAc != null) {
			AutocompleteService service = brandAc.getService();
			if (service != null) {
				Set<String> prefixes = service.getPrefixes();
				if (prefixes != null)
					terms2 = prefixes;
			}
				
		}
	%>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Term</th>
				</tr>
			</thead>
			<tbody>
				<% for (String term : terms2) { %>
				<tr>
					<td><%= term %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>