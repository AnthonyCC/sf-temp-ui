<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>

<%@page import="com.freshdirect.fdstore.content.SearchResults"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.freshdirect.cms.search.LuceneSearchService"%>
<%@page import="com.freshdirect.cms.search.spell.SpellingHit"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.cms.search.term.SpellingTermNormalizer"%>
<%@page import="com.freshdirect.cms.search.term.Term"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.freshdirect.cms.search.spell.SpellingCandidate"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.search.SearchHit"%>
<%@page import="com.freshdirect.cms.search.term.ApproximationsPermuter"%>
<%@page import="com.freshdirect.cms.search.SpellingUtils"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearchUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.freshdirect.cms.index.configuration.IndexerConfiguration"%>
<html lang="en-US" xml:lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>Spell (aka Did you mean?) Test</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<%! DecimalFormat scoreFormat = new DecimalFormat("0.000"); %>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<%
String searchTerm = request.getParameter("searchTerm");
if (searchTerm != null && searchTerm.trim().isEmpty()) {
	searchTerm = null;
}

boolean quoted = false;
if (searchTerm != null) {
	searchTerm = searchTerm.trim();
	if (ContentSearchUtil.isQuoted(searchTerm)) {
		searchTerm = ContentSearchUtil.removeQuotes(searchTerm);
		quoted = true;
	}
}
%>
	<div class="right float-right">
		<a href="spell_dictionary.jsp" target="_spell_terms_dictionary">Dictionary</a> (long running !!!)
	</div>
	<div class="float-close"></div>
<div style="padding-top: 15px;">
	<span style="font-weight: bold;">Specify search term:</span> <input type="text" name="searchTerm"<%= searchTerm != null ? "value=\"" + StringEscapeUtils.escapeHtml(ContentSearchUtil.quote(quoted, searchTerm)) + "\"" : "" %>> <input type="submit" value="Submit">
	&nbsp;
	<% if (searchTerm != null) { %><a href="/search.jsp?searchParams=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(ContentSearchUtil.quote(quoted, searchTerm), "iso-8859-1")) %>" target="_search_page">Show on search page</a><% } %>
</div>
<%
if (searchTerm != null && !searchTerm.trim().isEmpty()) {
%>
<div class="section">
	<div class="section-header">Search Results</div>
	<%
		ContentSearch contentSearch = ContentSearch.getInstance();
		SearchResults res = contentSearch.searchProducts(ContentSearchUtil.quote(quoted, searchTerm));
	%>
	<div class="section-body">
	<%
		if (res.isEmpty()) {
	%>
	No results. <a href="/test/search/search_test.jsp?searchParams=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(ContentSearchUtil.quote(quoted, searchTerm), "iso-8859-1")) %>" target="_search_test">Test Search</a>
	<%
		} else {
	%>
	Search return <%= res.getProducts().size() %> product(s) and <%= res.getRecipes().size() %> recipe(s).
	<a href="/test/search/search_test.jsp?searchParams=<%= StringEscapeUtils.escapeHtml(URLEncoder.encode(ContentSearchUtil.quote(quoted, searchTerm), "iso-8859-1")) %>" target="_search_test">Test Search</a>
	<%
		}
	%>
	</div>
</div>
<div class="section">
	<div class="section-header">Spelling Candidates</div>
	<%
		SpellingTermNormalizer filter = new SpellingTermNormalizer(new Term(searchTerm));
		List<String> original = filter.getTerms().get(0).getTokens();
		Registry registry = FDRegistry.getInstance();
		ContentSearchServiceI search = LuceneSearchService.getInstance();
		IndexerConfiguration indexConfig = IndexerConfiguration.getDefaultConfiguration();
		
		Collection<String> suggestions = res.getSpellingSuggestions();
	%>
	<div class="section-body">
		<div class="float-left left-column"><% { %>
			<p class="subsection-header">
				Direct search results
			</p>
			<%
				List<SpellingHit> spellingHits = search.getSpellService().getSpellingHits(indexConfig.getIndexDirectoryPath(), searchTerm, contentSearch.getDidYouMeanMaxHits());
				Set<SpellingHit> filteredHits = new HashSet<SpellingHit>(SpellingUtils.filterBestSpellingHits(spellingHits, contentSearch.getDidYouMeanThreshold()));
			%>
			<table class="table">
				<thead>
					<tr>
						<th class="padded">Suggestion</th>
					</tr>
				</thead>
				<tbody>
					<% for (SpellingHit hit : spellingHits) {
						String style = filteredHits.contains(hit) ? "" : "text-decoration: line-through;";
						if (!SpellingUtils.checkPartialThreshold(original, Term.tokenize(hit.getSpellingMatch(), Term.DEFAULT_TOKENIZERS),
								contentSearch.getDidYouMeanThreshold(), search.getSpellService().getStringDistance()))
							style = "color: gray; text-decoration: line-through;";
					%>
					<tr>
						<td style="<%= style %>"><%= ContentSearchUtil.quote(quoted, hit.getPhrase()) %> <i>(<b><%= hit.getDistance() %></b> &ndash; <%= scoreFormat.format(hit.getScore()) %>)</i></td>
					</tr>
					<% } %>
				</tbody>
			</table>
		<% } %></div>
		<div class="float-left left-column"><% { %>
			<p class="subsection-header">
				Reconstructed search results
			</p>
			<div class="float-left left-column">
				<p class="subsubsection-header">
					Particle candidates
				</p>
				<%
					List<List<SpellingHit>> particles = search.generateSpellingParticles(original, contentSearch.getDidYouMeanThreshold(), contentSearch.getDidYouMeanMaxHits());
					int i = 1;
					for (List<SpellingHit> particle : particles) {
				%>
				<div class="float-left">
				<table class="table">
					<thead>
						<tr>
							<th class="padded">Particle #<%= i %></th>
						</tr>
					</thead>
					<tbody>
						<% for (SpellingHit hit : particle) {
						%>
						<tr>
							<td><%= ContentSearchUtil.quote(quoted, hit.getPhrase()) %> <i>(<b><%= hit.getDistance() %></b> &ndash; <%= scoreFormat.format(hit.getScore()) %>)</i></td>
						</tr>
						<% } %>
					</tbody>
				</table>
				</div>
				<%
					i++; }
				%>
				<div class="float-close"></div>
			</div>
			<div class="float-left">
				<p class="subsubsection-header">
					Combined permutations
				</p>
				<%
					Collection<SpellingHit> spellingHits = search.reconstructSpelling(searchTerm, contentSearch.getDidYouMeanThreshold(), contentSearch.getDidYouMeanMaxHits());
				%>
				<table class="table">
					<thead>
						<tr>
							<th class="padded">Suggestion</th>
						</tr>
					</thead>
					<tbody>
						<% for (SpellingHit hit : spellingHits) {
						%>
						<tr>
							<td><%= ContentSearchUtil.quote(quoted, hit.getPhrase()) %> <i>(<b><%= hit.getDistance() %></b> &ndash; <%= scoreFormat.format(hit.getScore()) %>)</i></td>
						</tr>
						<% } %>
					</tbody>
				</table>
			</div>
			<div class="float-close"></div>
		<% } %></div>
		<div class="float-close"></div>
	</div>
</div>
<div class="section">
	<div class="section-header">Combined results</div>
	<div class="section-body">
		<%
			List<SpellingHit> spellingHits = contentSearch.combineSpellingHitResults(searchTerm);
			SearchResults res1 = contentSearch.searchProductsInternal(searchTerm, quoted, false);
			List<SpellingCandidate> candidates = contentSearch.extractCandidatesFromSpellingHits(spellingHits, quoted, res1);
		%>
		<table class="table">
			<thead>
				<tr>
					<th class="padded">Suggestion</th>
					<th class="padded">Matching Items</th>
					<th class="padded">Ratio</th>
				</tr>
			</thead>
			<tbody>
				<%
				List<SpellingCandidate> candidates1 = new ArrayList<SpellingCandidate>();
				for (SpellingHit hit : spellingHits) {
					SpellingCandidate candidate = new SpellingCandidate(hit);
					SearchResults hitResults = contentSearch.searchProductsInternal(hit.getPhrase(), false, false);
					candidate.setSearchResults(hitResults);
					candidates1.add(candidate);
				}
				Collections.sort(candidates1, new Comparator<SpellingCandidate>() {
					public int compare(SpellingCandidate o1, SpellingCandidate o2) {
						int d = Double.compare(o2.getScore(), o1.getScore());
						if (d != 0)
							return d;
						else
							return o2.getSearchResults().size() - o1.getSearchResults().size();
					}
				});
				
				for (SpellingCandidate candidate : candidates1) {
					String style = "color: gray; text-decoration: line-through;";
					if (candidate.getSearchResults().isEmpty())
						style = "text-decoration: line-through;";
					else for (SpellingCandidate can : candidates)
						if (can.getPhrase().equals(candidate.getPhrase()))
							style ="font-weight: bold;";
				%>
				<tr>
					<td style="<%= style %>"><%= ContentSearchUtil.quote(quoted, candidate.getPhrase()) %> <i>(<b><%= candidate.getDistance() %></b> &ndash; <%= scoreFormat.format(candidate.getScore()) %>)</i></td>
					<td style="<%= style %>"><%= candidate.getSearchResults().getProducts().size() %> prods /
							<%= candidate.getSearchResults().getRecipes().size() %> recipes / <%= candidate.getSearchResults().getCategories().size() %> cats</td>
					<td style="<%= style %>"><%= res1.size() != 0 ? scoreFormat.format((double) candidate.getSearchResults().size() / (double) res1.size()) : "" %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div>
<div class="section">
	<div class="section-header">Approximations</div>
	<div class="section-body"><% { %>
		<%
			if (!res1.isEmpty()) {
		%>
		<p>We have search results, no approximations are given.</p>
		<%
			} else if (!candidates.isEmpty()) {
		%>
		<p>We have spelling candidates, no approximations are given.</p>
		<%
			} else {
				Collection<SpellingCandidate> candidates2 = contentSearch.suggestApproximationSpellingInternal(spellingHits, false, res1);
				if (!spellingHits.isEmpty()) {
					// take the best (first) hit as a basis
					SpellingHit hit = spellingHits.iterator().next();
					ApproximationsPermuter permuter = new ApproximationsPermuter(new Term(hit.getPhrase()));
					List<List<Term>> permutations = permuter.permute();
		%>
		<table class="table">
			<thead>
				<tr>
					<th class="padded">Suggestion</th>
					<th class="padded">Matching Items</th>
				</tr>
			</thead>
			<tbody>
				<% 
					for (List<Term> permLevel : permutations) {
						for (Term permutation : permLevel) {
							String style = "color: gray; text-decoration: line-through;";
							SpellingCandidate candidate = new SpellingCandidate(new SpellingHit(permutation.toString(), hit.getDistance()));
							SearchResults hitResults = contentSearch.searchProductsInternal(candidate.getPhrase(), false, false);
							candidate.setSearchResults(hitResults);
							if (hitResults.isEmpty())
								style = "text-decoration: line-through;";
							else for (SpellingCandidate can : candidates2)
								if (can.getPhrase().equals(candidate.getPhrase()))
									style ="font-weight: bold;";
				%>
				<tr>
					<td style="<%= style %>"><%= ContentSearchUtil.quote(quoted, candidate.getPhrase()) %></td>
					<td style="<%= style %>"><%= candidate.getSearchResults().getProducts().size() %> prods /
							<%= candidate.getSearchResults().getRecipes().size() %> recipes / <%= candidate.getSearchResults().getCategories().size() %> cats</td>
				</tr>
				<%
						}
					} 
				%>
			</tbody>
		</table>
		<%
				} else {
		%>
		We don't have spelling hits (oops)!!!
		<%
				}
			}
		%>
	<% } %></div>
</div>
<%
}
%>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>