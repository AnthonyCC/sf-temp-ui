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
<%@ page import="com.freshdirect.storeapi.content.DepartmentModel" %>
<%@ page import="com.freshdirect.storeapi.content.ContentNodeModel" %>

<%!
Collection<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY);
List productList;
int count = 0;
%>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
<title>Query for all usq products showing RECOMMENDED_ALTERNATIVES</title>
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
<h1>Query for all usq products showing RECOMMENDED_ALTERNATIVES</h1>
<table style="width:100%">
    <tr class="firstline">
        <td>Department Name</td>
        <td>Department Id</td>        
        <td>Category Name</td>
        <td>Category Id</td>
        <td>Product Name</td>
        <td>Product ID</td>
	<td>Recommended Altern. Name</td>
        <td>Recommended Altern. Id</td>
     </tr>
<% 
for (ContentKey category : categories){
    CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(category);
    productList = categoryModel.getProducts();

    for (int i = 0; i < productList.size(); i++){
    ProductModel productModel = (ProductModel) productList.get(i); 
    DepartmentModel departmentModel = (DepartmentModel)categoryModel.getDepartment();
    List recommendedAlternatives = productModel.getRecommendedAlternatives();

    if(categoryModel.getDepartment().toString() == "usq"){
	count += 1;
%>
      <tr>
          <td><%if(departmentModel != null){ %> <%= departmentModel.getFullName() %> <%}%></td>
          <td><%= categoryModel.getDepartment() %></td>
          <td><%= categoryModel.getFullName() %></td>
          <td><%= categoryModel.getContentName() %></td>
	  <td><%= productModel.getFullName() %></td>
          <td><%= productModel.getContentName()%></td>
          <% if(recommendedAlternatives != null && recommendedAlternatives.size() > 0){ 
		ContentNodeModel alt = (ContentNodeModel)recommendedAlternatives.get(0); %>
	     <td> <%= alt.getFullName() %> </td>
	     <td> <%= alt.getContentName() %> </td>
	  <%}%>
      </tr>
<%
}}}
%>
</table>
<p>Row count: <strong><%= count %></strong></p>
</body>
</html>
