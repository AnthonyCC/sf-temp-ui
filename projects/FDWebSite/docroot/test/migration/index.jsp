<!DOCTYPE html>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html>
<head>
	<meta charset="utf-8"/>
	<title>CMS Data Migration Tools and Reports</title>
</head>
<body>
<ul>
	<li>
		<a href="products_tagged.jsp">List</a> of products assigned to a given tag
	</li>
	<li>
		<a href="aliases.jsp">List</a> category aliases
	</li>
	<li>
		<a href="mixedcats.jsp">List</a> of mixed categories (cats having both subcats and products)
	</li>
	<li>
		<a href="deep_cats.jsp">List</a> of categories deeper than 2 category levels.
	</li>
	<li>
		<a href="erps_allergen_codes.jsp">Codes</a> for creating ERPS Allergen info based filters in CMS
	</li>
	<li>
		<a href="erps_claim_codes.jsp">Codes</a> for creating ERPS Claim based filters in CMS
	</li>
	<li>
		<a href="erps_nutrition_codes.jsp">Codes</a> for creating ERPS Nutrition info based filters in CMS
	</li>
	<li>
		<a href="redirect_virtual.jsp">List</a> of categories have both redirect url and virtual group configured
	</li>
	<li>
		<a href="cats_show_self.jsp">List</a> of categories having <i>SHOWSELF</i> attribute set to false
	</li>
	<li>
		<a href="category_media_slots.jsp">List</a> of category media slots
	</li>
	<li>
		<a href="category_photos.jsp">List</a> of category photos
	</li>
	<li>
		<a href="cats_wide.jsp">Report</a> of category displays
	</li>
</ul>
</body>
</html>