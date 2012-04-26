<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="com.freshdirect.cms.fdstore.*,
	com.freshdirect.framework.webapp.*,
	com.freshdirect.smartstore.service.SearchScoringRegistry"%>
<%@page import="com.freshdirect.smartstore.scoring.Score"%>
<%@page import="com.freshdirect.smartstore.scoring.ScoringAlgorithm"%>

<%@page import="com.freshdirect.fdstore.util.SearchNavigator"%>
<%@page import="com.freshdirect.fdstore.content.FilteringSortingItem"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.EnumSortingValue"%>
<%@page import="com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator"%><html>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<head>
	<fd:css href="/assets/css/test/search/config.css" />
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
	<% SearchNavigator nav = new SearchNavigator(request); %>
	<fd:SmartSearch id="search" nav="<%= nav %>">
	 <% if (search.getNoOfProducts() > 0) {  %>
	  <table id="searchResultTable">
	  <tr>
	  	<td>Image</td><td>Name</td><td>Product Key</td><td>Category Key</td><td>Term Score</td><td>Category Score</td><td>Displayable</td>
	  	<%
	  	ScoringAlgorithm global = SearchScoringRegistry.getInstance().getGlobalScoringAlgorithm();
	  	String[] globExpr = global.getExpressions();
	  	for (int i=0;i<global.getReturnSize();i++) {
	  	    %><td>Global Factor[ <i><%= globExpr[i] %></i> ]</td><%	  	    
	  	}
	  	ScoringAlgorithm user = SearchScoringRegistry.getInstance().getUserScoringAlgorithm();
	  	String[] userExpr = user.getExpressions();
	  	
	  	for (int i=0;i<user.getReturnSize();i++) {
	  	    %><td>Personal Factor[ <i><%= userExpr[i] %></i> ]</td><%	  	    
	  	}

		ScriptedContentNodeComparator personal = ScriptedContentNodeComparator.createUserComparator(search.getUserId(), search.getPricingContext());
		ScriptedContentNodeComparator global2 = ScriptedContentNodeComparator.createGlobalComparator(search.getUserId(), search.getPricingContext());
	  	%>
	  </tr>
		<% for (FilteringSortingItem<ProductModel> item : search.getProcessedResults().getProducts()) { %>
			<tr>
				<td class="productImage">
					<display:ProductImage product="<%= item.getModel() %>" prefix="http://www.freshdirect.com"/>
				</td>
				<td class="productName">
					<display:ProductName product="<%= item.getModel() %>"/>
				</td>
				<td class="productKey">
					<%=
						item.getModel().getContentKey().getId()
					%>
				</td>
				<td class="categoryKey">
					<%=
						item.getModel().getParentNode().getContentKey().getId()
					%>
				</td>
				<td class="termScore">
					<%=
						item.getSortingValue(EnumSortingValue.TERM_SCORE)
					%>
				</td>
				
				<td class="categoryScore">
					<%=
						item.getSortingValue(EnumSortingValue.CATEGORY_RELEVANCY)
					%>
				</td>
				<td class="displayable">
				<%= item.getSortingValue(EnumSortingValue.AVAILABILITY).intValue() > 0 ? "X" : "" %>
				</td>
				<% 
				Score score = global2.getScore(item.getModel()); 
				if (score !=null) {
				    for (int i = 0;i<score.size();i++) {
				        %><td class="score"><%= score.get(i) %></td><%
				    }
				}
				score = personal.getScore(item.getModel()); 
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
		<% } %>
	   </table>
	<% } %>
	</fd:SmartSearch>	
</div>

</body>