<!DOCTYPE html>
<%@page import="com.freshdirect.content.nutrition.EnumClaimValue"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>ERPS Claim Codes</title>
	<link href="migration.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>ERPS Claim Codes</h1>
<p>
</p>

	<table>
		<th>Name</th>
		<th>Code</th>
		<c:forEach items="<%=EnumClaimValue.getValues()%>" var="claim" varStatus="rownum">
			<c:choose>
				<c:when test="${rownum.index mod 2 eq 0}">
					<tr class="even_col">
				</c:when> 
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			<td>${claim.name}</td>
			<td>${claim.code}</td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>
