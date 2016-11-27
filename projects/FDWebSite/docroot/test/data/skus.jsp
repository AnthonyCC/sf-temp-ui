<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.SkuModel"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" lang="en-US">
<title>Available SKUs</title>
</head>
<body>
<%
Set<ContentKey> skus = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
SortedSet<ContentKey> activeSkus = new TreeSet<ContentKey>(new Comparator<ContentKey>() {
	public int compare(ContentKey o1, ContentKey o2) {
		return o1.getId().compareTo(o2.getId());
	}
});
for (ContentKey sku : skus) {
	SkuModel node = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(sku);
	if (node.isHidden() || node.isOrphan())
		continue;
	if (!node.isDiscontinued() && !node.isUnavailable())
		activeSkus.add(sku);
}
%>
<table>
	<tr>
		<th>SKU</th>
	</tr>
	<% for (ContentKey sku : activeSkus) { %>
	<tr>
		<td><%= sku.getId() %></td>
	</tr>
	<% } %>
</table>
</body>
</html>