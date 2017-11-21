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
<%@ page import="com.freshdirect.storeapi.content.ProductGrabberModel" %>

<%!
Collection<ContentKey> categories = CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY);
List virtualCategoryList;
List productGrabberList;
%>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
<title>/ FD Category-Virtual Category-ProductGrabber Report /</title>
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
<h1>Table for ProductGrabbers</h1>
<table style="width:100%">
    <tr class="firstline">
        <td>Department Name</td>
        <td>Department Id</td>        
        <td>Category Name</td>
        <td>Category Id</td>
        <td>ProductGrabber ID-Name</td>
        <td>ProductGrabber Scope</td>        
      </tr>
<%for (ContentKey category : categories)
{
    CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(category);
    productGrabberList = categoryModel.getProductGrabbers();

       
    for (int i = 0; i < productGrabberList.size(); i++) 
    {                     
    ProductGrabberModel productGrabberModel = (ProductGrabberModel) productGrabberList.get(i); 
    DepartmentModel departmentModel = (DepartmentModel)categoryModel.getDepartment();
    %>
      <tr>
          <td><%if(departmentModel != null){ %> <%= departmentModel.getFullName() %> <%}%></td>
          <td><%= categoryModel.getDepartment() %></td>
          <td><%= categoryModel.getFullName() %></td>    
          <td><%= categoryModel.getContentName() %></td>
        <td><%= productGrabberModel.getContentName()%></td>    
        <td><%= productGrabberModel.getScope() %></td>
      </tr>
<%    }
} %>
</table>
<h1>Table for virtual categories</h1>
<table style="width:100%">
    <tr class="firstline">
        <td>Department Name</td>
        <td>Department Id</td>        
        <td>Category Name</td>
        <td>Category Id</td>
        <td>Virtual Department Name</td>
        <td>Virtual Department Id</td>        
        <td>Virtual Category Name</td>
        <td>Virtual Category Id</td>
      </tr>
<%for (ContentKey category1 : categories)
{
    CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(category1);
    DepartmentModel departmentModel = (DepartmentModel)categoryModel.getDepartment();
    virtualCategoryList = categoryModel.getVirtualGroupRefs();

       
    for (int i = 0; i < virtualCategoryList.size(); i++) 
    {
    CategoryModel virtualModel= (CategoryModel) virtualCategoryList.get(i);
    DepartmentModel virtualDepartmentModel = (DepartmentModel)virtualModel.getDepartment();
    %>
      <tr>
          <td><%if(departmentModel != null){ %> <%= departmentModel.getFullName() %> <%}%></td>
          <td><%= categoryModel.getDepartment() %></td>
          <td><%= categoryModel.getFullName() %></td>
          <td><%= categoryModel.getContentName() %></td>        
           <td><%if(virtualDepartmentModel != null){ %> <%= virtualDepartmentModel.getFullName() %> <%}%></td>
   
        <td><%= virtualModel.getDepartment() %></td>
        <td><%= virtualModel.getFullName() %></td>
        <td><%= virtualModel.getContentName() %></td>
      </tr>
<%    }
} %>
</table>
</body>
</html>