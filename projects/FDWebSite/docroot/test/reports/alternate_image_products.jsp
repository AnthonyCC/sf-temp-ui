<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.freshdirect.cms.ContentKey" %>
<%@ page import="com.freshdirect.cms.application.CmsManager" %>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.fdstore.content.ContentFactory" %>
<%@ page import="com.freshdirect.fdstore.content.CategoryModel" %>
<%@ page import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.content.DepartmentModel" %>

<%!
Collection<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY);
List productList;
int count = 0;
%>

<!DOCTYPE html>
<html>
<head>
<title>Query for non-Archive products with ALTERNATE_IMAGE</title>
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
<h1>Query for non-Archive products with ALTERNATE_IMAGE</h1>
<table style="width:100%">
    <tr class="firstline">
        <td>Department Name</td>
        <td>Department Id</td>        
        <td>Category Name</td>
        <td>Category Id</td>
        <td>Product Name</td>
        <td>Product ID</td>
        <td>Alt. Image Path</td>
     </tr>
<% 
for (ContentKey category : categories){
    CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(category);
    productList = categoryModel.getProducts();

    for (int i = 0; i < productList.size(); i++){
    ProductModel productModel = (ProductModel) productList.get(i); 
    DepartmentModel departmentModel = (DepartmentModel)categoryModel.getDepartment();

    if(categoryModel.getDepartment().toString() != "Archive" && productModel.getAlternateImage() != null && productModel.getAlternateImage().getPath() != ""){
	count += 1;
%>
      <tr>
          <td><%if(departmentModel != null){ %> <%= departmentModel.getFullName() %> <%}%></td>
          <td><%= categoryModel.getDepartment() %></td>
          <td><%= categoryModel.getFullName() %></td>
          <td><%= categoryModel.getContentName() %></td>
	<td><%= productModel.getFullName() %></td>
        <td><%= productModel.getContentName()%></td>
        <td><%= productModel.getAlternateImage().getPath() %></td>
      </tr>
<%
}}}
%>
</table>
<p>Row count: <strong><%= count %></strong></p>
</body>
</html>
