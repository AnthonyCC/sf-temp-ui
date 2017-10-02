<!DOCTYPE html>
<%@page import="com.freshdirect.content.nutrition.EnumAllergenValue"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>ERPS Allergen Codes</title>
	<link href="migration.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>ERPS Allergen Codes</h1>
<p>
</p>

	<table>
		<th>Name</th>
		<th>Code</th>
		<c:forEach items="<%=EnumAllergenValue.getValues()%>" var="allergen" varStatus="rownum">
			<c:choose>
				<c:when test="${rownum.index mod 2 eq 0}">
					<tr class="even_col">
				</c:when> 
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			<td>${allergen.name}</td>
			<td>${allergen.code}</td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>
