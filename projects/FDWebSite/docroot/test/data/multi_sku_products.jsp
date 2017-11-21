<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@page import="com.freshdirect.storeapi.application.CmsManager"%>
<%@page import="com.freshdirect.storeapi.fdstore.FDContentTypes"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.storeapi.content.SkuModel"%>
<%@page import="com.freshdirect.storeapi.content.ProductModel"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.SortedMap"%><html lang="en-US" xml:lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" lang="en-US">
<title>Available Multi-SKU Products</title>
</head>
<body>
<%
Set<ContentKey> keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
SortedMap<ProductModel, Integer> activeNodes = new TreeMap<ProductModel, Integer>(new Comparator<ProductModel>() {
	public int compare(ProductModel o1, ProductModel o2) {
		return o1.getFullName().compareTo(o2.getFullName());
	}
});
for (ContentKey key : keys) {
	ProductModel node = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(key);
	if (!node.isFullyAvailable())
		continue;
	int count = 0;
	for (SkuModel sku : node.getSkus())
		if (!sku.isUnavailable())
			count++;
	if (count > 1)
		activeNodes.put(node, count);
}
%>
<table>
	<tr>
		<th>Product</th>
	</tr>
	<% for (ProductModel node : activeNodes.keySet()) {
	%>
	<tr>
		<td>
		<display:ProductUrl product="<%= node %>" id="url">
			<a href="<%= url %>"><%= node.getFullName() %></a> <%= activeNodes.get(node) %>
		</display:ProductUrl>
		</td>
	</tr>
	<% } %>
</table>
</body>
</html>