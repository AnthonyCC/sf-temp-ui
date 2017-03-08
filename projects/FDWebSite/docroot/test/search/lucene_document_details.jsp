<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.cms.index.FullIndexerService"%>
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>

<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="com.freshdirect.cms.index.IndexingConstants"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="com.freshdirect.cms.index.configuration.IndexerConfiguration"%>
<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.index.IndexReader"%>
<%@page import="org.apache.lucene.store.FSDirectory"%>
<%@page import="org.apache.lucene.search.IndexSearcher"%>
<%@page import="java.io.File"%>
<%@page import="com.freshdirect.cms.search.configuration.SearchServiceConfiguration"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.apache.lucene.search.Query"%>
<%@page import="org.apache.lucene.search.TopDocs"%>
<%@page import="org.apache.lucene.document.Document"%>
<%@page import="org.apache.lucene.document.Fieldable"%>
<%@page import="org.apache.lucene.analysis.KeywordAnalyzer"%>
<%@page import="org.apache.lucene.queryParser.QueryParser"%>
<%@page import="org.apache.lucene.analysis.TokenStream"%>
<%@page import="org.apache.lucene.analysis.tokenattributes.TermAttribute"%>
<%@page import="java.io.StringReader"%><html>
<%
String contentKey = request.getParameter("contentKey");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>Lucene Document details &ndash; <%= contentKey %></title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<div class="body">
<div class="left">
<div class="section">
	<div class="section-header">Document details</div>
	<%
		if (contentKey != null)
			contentKey = contentKey.trim();
		if (contentKey != null && contentKey.isEmpty())
			contentKey = null;

		Registry registry = FDRegistry.getInstance();
		//ContentSearchServiceI search = (ContentSearchServiceI) registry.getService(ContentSearchServiceI.class);
		FullIndexerService indexer = FullIndexerService.getInstance();
		Analyzer analyzer = IndexingConstants.ANALYZER;
		String indexDirectoryPath = SearchServiceConfiguration.getInstance().getCmsIndexLocation();
		FSDirectory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		IndexReader reader = IndexReader.open(indexDirectory, false);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(Version.LUCENE_30, IndexingConstants.FIELD_CONTENT_KEY, analyzer);
		Query query = parser.parse(IndexingConstants.FIELD_CONTENT_KEY + ":\"" + contentKey + "\"");
		TopDocs hits = searcher.search(query, 1000);
		Document document = null;
		if (hits.totalHits > 0)
			document = searcher.doc(hits.scoreDocs[0].doc);
	%>
	<div class="section-body">
		<ul>
			<%
			if (document != null) { 
				for (Fieldable field : document.getFields()) {
					StringBuilder tokens = new StringBuilder();
					if (field.isTokenized()) {
						// try to reconstruct
						TokenStream tokenStream = analyzer.tokenStream(field.name(), new StringReader(field.stringValue()));
						TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
						if (tokenStream.incrementToken())
							tokens.append(termAttr.term());
						while (tokenStream.incrementToken()) {
							tokens.append(", ");
							tokens.append(termAttr.term());
						}
					}
			%>
			<li><span class="label"><%= field.name() %></span> &ndash; <span class="value"><%= field.stringValue() %><% if (field.isTokenized()) { %> [<%= tokens %>]<% } %></span></li>
			<%
				}
			}
			%>
		</ul>
	</div>
	<%
		searcher.close();
		reader.close();
		indexDirectory.close();
	%>
</div>
</div><!--  left -->
</div><!--  body -->
</body>
</html>