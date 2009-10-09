<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="com.freshdirect.cms.fdstore.*,
	com.freshdirect.framework.webapp.*,
	com.freshdirect.smartstore.service.SearchScoringRegistry"%>
<%@page import="com.freshdirect.smartstore.scoring.Score"%>
<%@page import="com.freshdirect.smartstore.scoring.ScoringAlgorithm"%>
<html>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<head>
	<link rel="stylesheet" type="text/css" href="config.css" />
	<title>Category Scoring</title>
</head>

<body>	
	
<div class="high">
	<form method="get">
		<ul>
			<li>
				<label for="searchParams">Search Terms:</label> 
				<input type="text" id="searchParams" name="searchParams"
					value="<%= request.getParameter("searchParams") != null ? request.getParameter("searchParams") : "" %>"></input>
			</li>
			<li>
				<input type="submit" value="Search!">
			</li>
		</ul>
	</form>
</div>

<div id="searchResult">
	<fd:SmartSearch searchResults="results" productList="productList" categorySet="categorySet" brandSet="brandSet" categoryTree="categoryTree" filteredCategoryTreeName="filteredCategoryTree">
	 <% if (productList!=null && productList.size() > 0) {  %>
	  <table id="searchResultTable">
	  <tr>
	  	<td>Image</td><td>Name</td><td>Product Key</td><td>Category Key</td><td>Category Score</td><td>Displayable</td>
	  	<%
	  	ScoringAlgorithm global = SearchScoringRegistry.getInstance().getGlobalScoringAlgorithm();
	  	for (int i=0;i<global.getReturnSize();i++) {
	  	    %><td>Global Factor[<%= i %>]</td><%	  	    
	  	}
	  	ScoringAlgorithm user = SearchScoringRegistry.getInstance().getUserScoringAlgorithm();
	  	for (int i=0;i<user.getReturnSize();i++) {
	  	    %><td>Personal Factor[<%= i %>]</td><%	  	    
	  	}
	  	%>
	  </tr>
		<logic:iterate id="productNode" collection="<%= results.getFilteredProducts() %>" type="com.freshdirect.fdstore.content.ProductModel">
			<tr>
				<td class="productImage">
					<display:ProductImage product="<%= productNode %>" prefix="http://www.freshdirect.com"/>
				</td>
				<td class="productName">
					<display:ProductName product="<%= productNode %>"/>
				</td>
				<td class="productKey">
					<%=
						productNode.getContentKey().getId()
					%>
				</td>
				<td class="categoryKey">
					<%=
						productNode.getParentNode().getContentKey().getId()
					%>
				</td>
				
				<td class="categoryScore">
					<%=
						results.getCategoryScore(productNode)
					%>
				</td>
				<td class="displayable">
				<%= results.isDisplayable(productNode) %>
				</td>
				<% 
				Score score = results.getGlobalScore(productNode); 
				if (score !=null) {
				    for (int i = 0;i<score.size();i++) {
				        %><td class="score"><%= score.get(i) %></td><%
				    }
				}
				score = results.getPersonalScore(productNode); 
				if (score !=null) {
				    for (int i = 0;i<score.size();i++) {
				        %><td class="score"><%= score.get(i) %></td><%
				    }
				} else {
				    for (int i = 0;i<user.getReturnSize();i++) {
				        %><td class="score"></td><%
				    }
				}
				%>
			</tr>
		</logic:iterate>
	   </table>
	<% } %>
	</fd:SmartSearch>	
</div>

</body>