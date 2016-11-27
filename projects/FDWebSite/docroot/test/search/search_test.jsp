<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.search.SearchHit"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="java.util.Collections"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="com.freshdirect.cms.search.LuceneSearchService"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.cms.search.term.Term"%>
<%@page import="com.freshdirect.cms.search.term.TermCoder"%>
<%@page import="com.freshdirect.cms.search.term.SearchTermNormalizer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.StringReader"%>
<%@page import="org.apache.lucene.analysis.tokenattributes.TermAttribute"%>
<%@page import="org.apache.lucene.analysis.TokenStream"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearchUtil"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.Recipe"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.fdstore.content.SearchSortType"%>
<%@page import="com.freshdirect.fdstore.content.SearchResults"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.fdstore.content.CategoryNodeTree"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.freshdirect.smartstore.scoring.Score"%>
<%@page import="com.freshdirect.fdstore.util.SearchNavigator"%>
<%@page import="com.freshdirect.fdstore.content.FilteringSortingItem"%>
<%@page import="com.freshdirect.fdstore.content.EnumSortingValue"%>
<%@page import="com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.freshdirect.fdstore.content.util.SmartSearchUtils"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
<fd:CheckLoginStatus id="user" noRedirect="true"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>The Search Test</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<div class="float-left">
<%  if (user != null) {
		if (user.getIdentity() != null) {
%>
Logged in as <%= user.getUserId() %>.
<%      } else { %>
Not logged in. You are in area  <%= user.getZipCode() %>.
<% 		}
	} else { %>
Not logged in.
<% 	} %>
</div>
<div class="right float-right">
	<a href="search_dictionary.jsp" target="_search_terms_dictionary">Dictionary</a>
</div>
<div class="float-close"></div>
<%
String searchTerm = request.getParameter("searchParams");
if (searchTerm != null && searchTerm.trim().isEmpty()) {
	searchTerm = null;
}
boolean quoted = false;
if (searchTerm != null) {
	searchTerm = searchTerm.trim();
	quoted = ContentSearchUtil.isQuoted(searchTerm);
}

%>
<div style="padding-top: 15px;">
	<span style="font-weight: bold;">Specify search term:</span>
	<input type="text" name="searchParams"<%= searchTerm != null ? "value=\"" + StringEscapeUtils.escapeHtml(searchTerm) + "\"" : "" %>>
	<input type="submit" value="Submit">
	&nbsp;
	<% if (searchTerm != null) { %><a href="/search.jsp?searchParams=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(searchTerm, "iso-8859-1")) %>" target="_search_page">Show on search page</a><% } %>
</div>
<%
if (searchTerm != null && !searchTerm.trim().isEmpty()) {
%>
<div class="section">
	<div class="section-header">Search Query</div>
	<%
		if (quoted)
			searchTerm = ContentSearchUtil.removeQuotes(searchTerm);
		TermCoder filter = new SearchTermNormalizer(new Term(searchTerm));
		String normalizedTerm = filter.getTerms().get(0).toString();
		List<String> tokens = new ArrayList<String>();
		Registry registry = FDRegistry.getInstance();
		ContentSearchServiceI search = (ContentSearchServiceI) registry.getService(ContentSearchServiceI.class);
		TokenStream tokenStream = search.getAnalyzer().tokenStream("_name_FULL_NAME", new StringReader(normalizedTerm));
		TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
		while (tokenStream.incrementToken())
			tokens.add(termAttr.term());
	%>
	<div class="section-body">
		<ul>
			<li><span class="label">Original term</span> &ndash; <span class="value"><%= searchTerm %><%= quoted ? " <i>(quoted)</i>" : "" %></span></li>
			<li><span class="label">Normalized term(s)</span> &ndash; <span class="value"><%= normalizedTerm %></span></li>
			<li><span class="label">Tokens</span> &ndash; <span class="value"><%= tokens.toString() %></span></li>
		</ul>
	</div>
</div>
<div class="section">
	<%
		Collection<SearchHit> phraseHits = CmsManager.getInstance().searchProducts(searchTerm, true, false, 5000);
		Collection<SearchHit> nonPhraseHits = quoted ? Collections.<SearchHit>emptyList() : CmsManager.getInstance().searchProducts(searchTerm, false, false, 5000);
		Iterator<SearchHit> it = nonPhraseHits.iterator();
		while (it.hasNext()) {
			SearchHit a = it.next();
			for (SearchHit e : phraseHits)
				if (a.getContentKey().equals(e.getContentKey()))
					it.remove();
		}
	%>
	<div class="section-header">Search Hits (<%= phraseHits.size() + nonPhraseHits.size() %>)</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Content key</th>
					<th>Disp</th>
					<th>Full name</th>
					<th>Exact</th>
					<th>Lucene Score<sup>*</sup></th>
				</tr>
			</thead>
			<tbody>
				<%
					for (SearchHit hit : phraseHits) {
						ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(hit.getContentKey());
						boolean disp = false;
						if (node != null) {
							if (FDContentTypes.PRODUCT.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((ProductModel) node);
							else if (FDContentTypes.CATEGORY.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((CategoryModel) node);
							else if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((Recipe) node);
						}
				%>
				<tr>
					<td><a href="lucene_document_details.jsp?contentKey=<%= hit.getContentKey().getEncoded() %>" target="_lucene_doc_details"><%= hit.getContentKey().getEncoded() %></a></td>
					<td align="center"><%= disp ? "X" : "" %></td>
					<td><%= node != null ? node.getFullName() : "&lt;unknown&gt;"%></td>
					<td align="center">X</td>
					<td><%= hit.getScore() %></td>
				</tr>
				<%
					}
				%>
				<%
					for (SearchHit hit : nonPhraseHits) {
						ContentNodeModel node = ContentFactory.getInstance().getContentNodeByKey(hit.getContentKey());
						boolean disp = false;
						if (node != null) {
							if (FDContentTypes.PRODUCT.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((ProductModel) node);
							else if (FDContentTypes.CATEGORY.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((CategoryModel) node);
							else if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType()))
								disp = ContentSearchUtil.isDisplayable((Recipe) node);
						}
				%>
				<tr>
					<td><a href="lucene_document_details.jsp?contentKey=<%= hit.getContentKey().getEncoded() %>" target="_lucene_doc_details"><%= hit.getContentKey().getEncoded() %></a></td>
					<td align="center"><%= disp ? "X" : "" %></td>
					<td><%= node != null ? node.getFullName() : "&lt;unknown&gt;"%></td>
					<td align="center"> </td>
					<td><%= hit.getScore() %></td>
				</tr>
				<%
					}
				%>
				<%
					if (phraseHits.isEmpty() && nonPhraseHits.isEmpty()) {
				%>
				<tr>
					<td colspan="5">No hits. Would you like to try <a href="spell_test.jsp?searchTerm=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(ContentSearchUtil.quote(quoted, searchTerm), "iso-8859-1")) %>" target="_spell_test">spell checking</a>?</td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
		<div style="padding-top: 5px; font-size: 10px;">
		<sup>*</sup> This is an internal score generated by the Lucene engine but it is nowhere used!
		</div>
	</div>
</div>
<div class="section">
	<% SearchNavigator nav = new SearchNavigator(request); %>
	<fd:SmartSearch nav="<%= nav %>" id="searchTag">
	<div class="section-header">Search Results &ndash; Products (<%= searchTag.getNoOfProducts() %>)</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Product</th>
					<th>sku</th>
					<th>Category</th>
					<th>Unav</th>
					<th>Exact</th>
					<th>Category Score</th>
					<th>Term Score</th>
					<th>Personal Score</th>
					<th>Global Score</th>
					<th>Sale</th>
					<th>Price</th>
				</tr>
			</thead>
			<tbody>
				<%
					String userId = user != null && user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : null;
					PricingContext pricingContext = user != null ? user.getPricingContext() : PricingContext.DEFAULT;
					ScriptedContentNodeComparator personal = ScriptedContentNodeComparator.createUserComparator(userId, pricingContext);
					ScriptedContentNodeComparator global = ScriptedContentNodeComparator.createGlobalComparator(userId, pricingContext);
					SearchResults results = searchTag.getProcessedResults();
					SmartSearchUtils.collectSaleInfo(results.getProducts(), pricingContext);
					for (FilteringSortingItem<ProductModel> item : results.getProducts()) {
						ProductModel product = item.getModel();
						CategoryModel category = (CategoryModel) product.getParentNode();
						Score globalSc = global.getScore(product);
						Score personalSc = personal.getScore(product);
				%>
				<tr>
					<td title="<%= product.getContentKey().getEncoded() %>"><a href="/product.jsp?productId=<%= product.getContentKey().getId() %>&catId=<%= category.getContentKey().getId() %>"
							target="_product_details"><%= product.getFullName() %></a></td>
					<td><%= product.getSkuCodes()%></td>
					<td title="<%= category.getContentKey().getEncoded() %>"><a href="/category.jsp?catId=<%= category.getContentKey().getId() %>"
							target="_product_details"><%= category.getFullName() %></a></td>
					<td align="center"><%= item.getSortingValue(EnumSortingValue.AVAILABILITY).intValue() == 0 ? "X" : "" %></td>
					<td align="center"><%= item.getSortingValue(EnumSortingValue.PHRASE).floatValue() != 0.0f ? "X" : "" %></td>
					<td align="right" nowrap="nowrap"><%= item.getSortingValue(EnumSortingValue.CATEGORY_RELEVANCY).intValue() %></td>
					<td align="right" nowrap="nowrap"><code><%= Long.toHexString(item.getSortingValue(EnumSortingValue.TERM_SCORE).longValue()) %></code></td>
					<td align="right" nowrap="nowrap"><%= personal != null ? Arrays.toString(personalSc.getScores()) : "" %></td>
					<td align="right" nowrap="nowrap"><%= global != null ? Arrays.toString(globalSc.getScores()) : "" %></td>
					<td align="right" nowrap="nowrap"><%= item.getSortingValue(EnumSortingValue.DEAL).intValue() %></td>
					<td align="right" nowrap="nowrap"><%= product.getPriceCalculator().getDefaultPrice() %></td>
				</tr>
				<%
					}
					if (results.isNoProducts()) {
				%>
				<tr>
					<td colspan="10">No products.</td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
	</fd:SmartSearch>
</div>
<%
}
%>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>