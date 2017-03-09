<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.cms.search.LuceneSearchService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="com.freshdirect.cms.index.FullIndexerService"%>
<%@page import="com.freshdirect.cms.index.configuration.IndexerConfiguration"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"
	lang="en-US">
<title>SEARCH TEST PAGES - INDEX</title>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf"%>

<style type="text/css">
.test-page {
	margin: 20px 60px;
	color: #333333;
	background-color: #fff;
}

.test-page input {
	font-weight: normal;
}

.test-page p {
	margin: 0px;
	padding: 0px 0px 10px;
}

.test-page .bull {
	color: #cccccc;
}

.test-page a {
	color: #336600;
}

.test-page a:VISITED {
	color: #336600;
}

.test-page .rec-chooser {
	margin: 0px 0px 6px;
	text-align: left;
}

.text11 {
	font-size: 11px;
	color: #333333;
	font-weight: normal;
}

.hidden {
	visibility: hidden;
}
</style>

</head>
<body class="test-page">
	<div class="rec-chooser title14">
		<h3>General Test Pages</h3>
		<p>
			<span class="bull">&bull;</span> <a href="brandautocomplete.jsp"><span>Autocomplete
					test page</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">You
				can test both product/recipe autocomplete and brand autocomplete.
				You can retrieve the autocomplete dictionary as well.</span>
		</p>
		<p>
			<span class="bull">&bull;</span> <a href="spell_test.jsp"><span>Spelling
					Suggestion (aka Did you mean?) Test page</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">Shows
				you the spell checker candidates and highlights the chosen spelling
				suggestion. It also offers a spelling dictionary.</span>
		</p>
		<p>
			<span class="bull">&bull;</span> <a href="search_test.jsp"><span>The
					Search Test page</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">You
				can test your product search here. It shows you the normalized
				search term, the stemmed word term.</span> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">It
				gives you the Lucene results directly and also displays the
				products/recipes actually displayed on the search page using
				relevancy sorting.</span> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">Relevancy
				scores are also displayed. Comes with dictionary.</span>
		</p>
		<p></p>
		<p>
			<span class="bull">&bull;</span> <a href="category_scoring.jsp"><span>Search
					scoring</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">A
				page for showing the relevancy scoring and sorting for a given
				search term.</span>
		</p>
		<p>
			<span class="bull">&bull;</span> <a href="relevancy_config.jsp"><span>Relevancy
					config</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">A
				page for showing the criteria of the global and the personal (user)
				scoring.</span> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">These
				criteria are basically lists of SmartStore factors (e.g. Popularity,
				DealsPercentage, etc.)</span>
		</p>
		<p></p>
		<h3>Developer Test Pages</h3>
		<p>
			<span class="bull">&bull;</span> <a href="compare.jsp"><span>Search
					compare page</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">You
				can compare the result set of two search queries.</span>
		</p>
		<p>
			<span class="bull">&bull;</span> <a href="smart_search.jsp"><span>Smart
					search test page</span></a> <br>
			<span class="bull hidden">&bull;</span> <span class="text11">This
				is the page where you can create and compare snapshots of various
				search tests.</span>
		</p>
		<h3>CSVs</h3>
		<p>
			<a href="csv_products.jsp"><span>Products</span></a> &ndash; <a
				href="csv_recipes.jsp"><span>Recipes</span></a> &ndash; <a
				href="csv_categories.jsp"><span>Categories</span></a>
		</p>
		<p>
			<a href="csv_synonyms.jsp"><span>Regular Synonyms</span></a> &ndash;
			<a href="csv_spellingsynonyms.jsp"><span>Spelling Synonyms</span></a>
		</p>
		<h3>Disable Features</h3>
		<p>
			<span class="text11">Note, that you have to re-index search
				and spelling after you change either attributes.</span>
			<%
			    Registry registry = FDRegistry.getInstance();
			    FullIndexerService indexer = FullIndexerService.getInstance();
			    IndexerConfiguration indexerConfiguration = IndexerConfiguration.getDefaultConfiguration();
			    String keywords = request.getParameter("keywords");
			    if (keywords != null) {
			        if ("enable".equals(keywords))
			            indexerConfiguration.setKeywordsDisabled(false);
			        else if ("disable".equals(keywords))
			            indexerConfiguration.setKeywordsDisabled(true);
			    }
			    String synonyms = request.getParameter("synonyms");
			    if (synonyms != null) {
			        if ("enable".equals(synonyms))
			            indexerConfiguration.setSynonymsDisabled(false);
			        else if ("disable".equals(synonyms))
			            indexerConfiguration.setSynonymsDisabled(true);
			    }
			%><br> <br>
			<span class="bull">&bull;</span> Keyword Attributes:
			<%
			    if (!indexerConfiguration.isKeywordsDisabled()) {
			%>
			Enabled <a href="index.jsp?keywords=disable"><span>Disable</span></a>
			<%
			    } else {
			%>
			<a href="index.jsp?keywords=enable"><span>Enable</span></a> Disabled
			<%
			    }
			%>
			<br>
			<span class="bull">&bull;</span> Synonyms:
			<%
			    if (!indexerConfiguration.isSynonymsDisabled()) {
			%>
			Enabled <a href="index.jsp?synonyms=disable"><span>Disable</span></a>
			<%
			    } else {
			%>
			<a href="index.jsp?synonyms=enable"><span>Enable</span></a> Disabled
			<%
			    }
			%>
		</p>
	</div>
</body>
</html>
