<!DOCTYPE html>
<%@page import="com.freshdirect.content.nutrition.ErpNutritionType"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8"/>
	<title>ERPS Nutrition Codes</title>
	<link href="migration.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>ERPS Nutrition Codes</h1>
<p>
<span class="warning">WARNING</span> - CMS filters will only work correctly if nutrition component lines associated with the filtered product use the <b>same unit of measure</b> as in this table.
</p>

	<table>
		<th>Name</th>
		<th>Code</th>
		<th>Default Unit of Measure</th>
		<c:forEach items="<%=ErpNutritionType.nutritionTypes%>" var="nutrition" varStatus="rownum">
			<c:choose>
				<c:when test="${rownum.index mod 2 eq 0}">
					<tr class="even_col">
				</c:when> 
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			<td>${nutrition.displayName}</td>
			<td>${nutrition.name}</td>
			<td>${nutrition.uom}</td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>
