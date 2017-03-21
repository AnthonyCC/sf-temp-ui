<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>

<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="com.freshdirect.cms.search.configuration.SearchServiceConfiguration"%>
<%@page import="com.freshdirect.cms.index.IndexerService"%>
<%@page import="org.apache.lucene.index.IndexReader"%>
<%@page import="org.apache.lucene.store.FSDirectory"%>
<%@page import="org.apache.lucene.search.IndexSearcher"%>
<%@page import="java.io.File"%>
<%@page import="com.freshdirect.cms.search.configuration.SearchServiceConfiguration"%>
<%@page import="org.apache.lucene.index.Term"%>
<%@page import="org.apache.lucene.index.TermPositions"%>
<%@page import="org.apache.lucene.document.Document"%>
<%@page import="org.apache.lucene.index.TermEnum"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>Spelling Dictionary</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
</head>
<body>
<form action="<%= request.getRequestURI() %>" method="get">
<div class="body">
<div class="left">
<div class="section">
	<div class="section-header">Spelling Dictionary</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Dictionary Search Term</th>
					<th>Suggested Spelling</th>
				</tr>
			</thead>
			<tbody>
				<%
					Registry registry = FDRegistry.getInstance();
			        IndexerService indexer = IndexerService.getInstance();
					String indexDirectoryPath = SearchServiceConfiguration.getInstance().getCmsIndexLocation();
                    FSDirectory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
                    IndexReader reader = IndexReader.open(indexDirectory, false);
					TermEnum terms = reader.terms(new Term("spelling_term"));
					while (terms.next()) {
						Term term = terms.term();
						String spellingTerm = term.text();
						TermPositions pos = reader.termPositions(term);
						while (pos.next()) {
							Document doc = reader.document(pos.doc());
							String searchTerm = doc.get("search_term");
							boolean isSynonym = doc.get("is_synonym") != null;
							String style = isSynonym ? " style=\"font-weight: bold; font-style: italic;\"" : "";
				%>
				<tr>
					<td<%= style %>><%= searchTerm %></td>
					<td<%= style %>><%= spellingTerm %></td>
				</tr>
				<%
						}
						pos.close();
					}
					terms.close();
					reader.close();
                    indexDirectory.close();
				%>
			</tbody>
		</table>
	</div>
</div>
</div><!--  left -->
</div><!--  body -->
</form>
</body>
</html>