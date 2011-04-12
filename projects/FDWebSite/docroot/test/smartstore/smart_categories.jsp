<!doctype html>

<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.freshdirect.fdstore.content.Recommender"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>

<html>
	<head>
		<title>Smart Categories test page</title>
	</head>
	
	<body>
	
		<div style="width: 800px;">
			<%
				String catId = request.getParameter( "catId" ); 
				ContentKey catKey = new ContentKey( ContentType.get("Category"), catId );
				CategoryModel category = (CategoryModel)ContentFactory.getInstance().getContentNodeByKey( catKey );
			%>				
		
			<form method="get" action="<%= request.getRequestURI() %>" id="cat_form">
				Category ID = <input type="text" name="catId" id="cat_input" value="<%= catId != null ? catId : "" %>">
			</form>
		
			<hr/>
		
			<span style="font-weight: bold; color: purple; font-size: large; font-style: italic;"><%= catKey.getEncoded() %></span><br/>
			<hr/>
			
			<% if ( category == null ) { %>
		 		<span style="font-weight: bold; color: red;">CATEGORY DOES NOT EXIST</span><br/>
			<% } else {
				List<ProductModel> prodList = category.getProducts();
				Recommender recommender = category.getRecommender();
				%>
			
				<% if ( recommender == null ) { %>
			 		<span style="font-weight: bold; color: red;">THIS IS NOT A SMART CATEGORY</span><br/>
				<% } else { %>
					Recommender: <span style="font-weight: bold; font-style: italic; color: blue; font-size: large;"><%= recommender.getContentKey().getEncoded() %></span>
					<br/>Description: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getDescription() %></span>					
					<br/>Scope: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getScope() %></span>		
					<hr/>			
					Strategy: <span style="font-weight: bold; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getContentKey().getEncoded() %></span>					
					<br/>Strategy description: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getDescription() %></span>					
					<br/>Strategy generator: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getGenerator() %></span>
					<br/>Strategy scoring: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getScoring() %></span>					
					<br/>Strategy sampling: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getSampling() %></span>					
					<br/>Strategy topN: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getTopN() %></span>					
					<br/>Strategy top%: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getTopPercent() %></span>					
					<br/>Strategy exponent: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().getExponent() %></span>
					
					<br/>Strategy show temp unav: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().isShowTemporaryUnavailable() %></span>					
					<br/>Strategy brand uniq sort: <span style="font-weight: normal; font-style: italic; color: blue; font-size: large;"><%= recommender.getStrategy().isBrandUniqSort() %></span>					
										
				<% } %>
				
				<% if ( prodList == null || prodList.size() == 0 ) { %>
			 		<span style="font-weight: bold; color: red;">CATEGORY IS EMPTY</span><br/>
				<% } else { %>
				
					<hr/><br/>
					
					<display:ContentNodeIterator id="prodIt" trackingCode="test" itemsToShow="<%= prodList %>">
						<div id="prod_<%= itemIndex %>_<%= currentItem.getContentName() %>" style="text-align: left; border: 1px solid; margin-top: -1px; padding: 5px;">
							<span style="font-weight: normal; font-size:small; color: blue; "><%= itemIndex %> : <%= currentItem.getContentKey().getEncoded() %></span>
							<display:ProductImage product="<%= (ProductModel)currentItem %>"/>
							<display:ProductRating product="<%= (ProductModel)currentItem %>"/>
							<display:ProductName product="<%= (ProductModel)currentItem %>"/>
							<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>"/>
						</div>
					</display:ContentNodeIterator>
				<% } %>
			<% } %>
		</div>
	</body>
</html>



