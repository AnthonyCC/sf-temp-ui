<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.freshdirect.fdstore.content.util.FourMinuteMealsHelper"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.content.nutrition.ErpNutritionInfoType"%>
<%@page import="com.freshdirect.content.nutrition.EnumClaimValue"%>
<%@page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@page import="com.freshdirect.fdstore.FDProduct"%>
<%@page import="com.freshdirect.fdstore.content.SkuModel"%>
<%@page import="com.freshdirect.fdstore.content.Domain"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='logic' prefix='logic' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>FD - 4MM test page</title>
</head>

<%
	DepartmentModel dept = FourMinuteMealsHelper.get4mmDepartment();

	// ingredients filter 
	Domain ingredientsDomain = FourMinuteMealsHelper.getIngredientsDomain();
	List<DomainValue> ingredientsDomainValues = FourMinuteMealsHelper.getIngredientsDomainValues();
	
	// nutrition filter 
	List<EnumClaimValue> nutritionClaims = FourMinuteMealsHelper.getNutritionClaims();
	
	List<CategoryModel> cats = FourMinuteMealsHelper.getRestaurants();
%>

<body>
	<b>ALL 4MM TEST PAGE</b><br/><br/>	
	
	<hr/>
	RESTAURANTS
	<hr/>
	<table>	
		<display:ContentNodeIterator trackingCode="4mm-all" itemsToShow="<%= cats %>" noTypeCheck="true" id="nodeIterator">
			<tr>
				<td>
					<% if ( currentItem instanceof CategoryModel ) { %>
						<font color="green"><%= currentItem.getFullName() %></font>
					<% } else { %>
						<font color="red"><%= currentItem.getFullName() %> is not a Category!</font>
					<% } %>					
				</td> 
				<td><b><%= currentItem.getContentKey().getEncoded() %></b></td>
			</tr>
		</display:ContentNodeIterator>
	</table>
	
	<hr/>
	INGREDIENTS
	<hr/>
	<table>	
		<display:ContentNodeIterator trackingCode="4mm-all" itemsToShow="<%= ingredientsDomainValues %>" noTypeCheck="true" id="nodeIterator">
			<tr>
				<td>
					<% if ( currentItem instanceof DomainValue ) { DomainValue dv = (DomainValue)currentItem; %>
						<font color="green"><%= dv.getDomain()%> / <b><%= dv.getLabel() %></b> [<%= dv.getTheValue() %>]</font>
					<% } else { %>
						<font color="red"><%= currentItem.getFullName() %> is not a DomainValue!</font>
					<% } %>					
				</td> 
				<td><b><%= currentItem.getContentKey().getEncoded() %></b></td>
			</tr>
		</display:ContentNodeIterator>
	</table>


	<hr/>
	NUTRITION
	<hr/>
	<table>	
		<logic:iterate id="claim" collection="<%= nutritionClaims %>" type="EnumClaimValue">
			<tr>
				<td>
					<font color="green"><b><%= claim.getName() %></b> [<%= claim.getCode() %>]</font>
				</td> 
			</tr>
		</logic:iterate>
	</table>
	
	
	<hr/>
	ALL NODES
	<hr/>
	<display:ItemGrabber id='items' category='<%= dept %>' depth='8' ignoreShowChildren="true">
		<table border="1px">	
			<display:ContentNodeIterator trackingCode="4mm-all" itemsToShow="<%= items %>" noTypeCheck="true" id="nodeIterator">
				<tr>
				
					<td>
						<% if ( currentItem instanceof ProductModel) { %>
							<font color="blue"><%= currentItem.getFullName() %></font>
						<% } else if ( currentItem instanceof CategoryModel ) { %>
							<font color="green"><%= currentItem.getFullName() %></font>
						<% } else { %>
							<font color="red"><%= currentItem.getFullName() %></font>
						<% } %>					
					</td> 
					
					<td><font color="purple">
						<% if ( currentItem instanceof ProductModel) { 
							ProductModel prod = (ProductModel)currentItem;
							List<DomainValue> ingredients = prod.getRating();
							for ( DomainValue d : ingredients ) {
								if ( ingredientsDomain.equals( d.getDomain() ) ) {
									out.print( d.getLabel() + "<br/>" );
								}
							}
						} %>
					</font></td>
					
					<% 
					SkuModel sku = null;
					if ( currentItem instanceof ProductModel) { 
						ProductModel prod = (ProductModel)currentItem;
						sku = prod.getDefaultSku();
					} %>
					<td><font color="orange">
						<% if ( sku != null ) {
							FDProduct fdprod = sku.getProduct();
							//fdprod.hasClaim( EnumClaimValue.)
							List<EnumClaimValue> claims = fdprod.getClaims();
							if ( claims != null ) {
								for ( EnumClaimValue claim : claims ) {
									if ( nutritionClaims.contains( claim ) ) {
										out.print( claim + "<br/>" );	
									}
								}
							}
						} %>
					</font></td>

					<td><font color="grey">
						<% if ( sku != null ) {
							FDProduct fdprod = sku.getProduct();
							out.print( sku.getSkuCode() );
						} else if ( currentItem instanceof ProductModel ) {
							out.print( "<font color=\"red\"><b>NO SKU</b></font>" );	
						} else if ( currentItem instanceof CategoryModel ) {
							out.print( "<b>CATEGORY</b>" );							
						}
						%>
					</font></td>
					
					<td><font color="grey">
						<%= currentItem.getContentKey().getEncoded() %>
					</font></td>
					
				</tr>
			</display:ContentNodeIterator>
		</table>
	</display:ItemGrabber>
	
	
</body>
</html>