<%@ page import="com.freshdirect.storeapi.content.DepartmentModel"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey" %>
<%@ page import="com.freshdirect.storeapi.application.CmsManager" %>
<%@ page import="com.freshdirect.storeapi.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.storeapi.content.ContentFactory" %>
<%@ page import="com.freshdirect.storeapi.content.CategoryModel" %>
<%@ page import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ page import="com.freshdirect.webapp.ajax.filtering.NavigationUtil" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
Collection<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY);
%>
<fd:CheckLoginStatus id="user"/>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
<title>EBT Excluded Items</title>
<style type="text/css">
	table{
	font-size: 10pt;
	font-family: Trebuchet MS, Arial, Verdana, sans-serif;
	border-spacing:0px;
	border-collapse:collapse;
	border-color:black;
	width:100%;
	}
	tr.firstline{ 
	font-weight:bold;
	text-align: center;
	border-style: solid;
	}
	td{ 
	border: 1px solid black;
	}
</style>
</head>
<body>
<h1>Table for EBT excluded products</h1>
<table style="width:100%">
    <tr class="firstline">
        <td>Product Id</td>
        <td>Product Name</td>
        <td>Category Id</td>
        <td>Category Name</td>
        <td>Link</td>
      </tr>
<%for (ContentKey category : categories)
{	
    CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(category);
    List<ProductModel> products = categoryModel.getAllChildProductsAsList();

    if(!NavigationUtil.isCategoryHiddenInContext(user, categoryModel)){
    
    for (ProductModel p : products)
    {
    	if(p.isFullyAvailable() && !p.isDiscontinued() && p.isExcludedForEBTPayment()){
    %>
      <tr>
          <td><%= p.getContentKey().getId() %></td>
          <td><%= p.getFullName() %></td>
          <td><%= categoryModel.getContentName() %></td>
          <td><%= categoryModel.getFullName() %></td>
          <td><a href="/pdp.jsp?productId=<%= p.getContentKey().getId() %>&catId=<%= categoryModel.getContentName() %>">LINK</a></td>
      </tr>
<%}}}}%>
</table>
</body>
</html>