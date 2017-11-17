<%@page import="java.io.PrintWriter"
%><%@page import="java.util.List"
%><%@page import="java.util.Set"
%><%@page import="org.apache.hivemind.Registry"
%><%@page import="com.freshdirect.cms.ContentKey"
%><%@page import="com.freshdirect.cms.ContentNodeI"
%><%@page import="com.freshdirect.cms.application.CmsManager"
%><%@page import="com.freshdirect.cms.fdstore.FDContentTypes"
%><%@page import="com.freshdirect.cms.search.AttributeIndex"
%><%@page import="com.freshdirect.cms.search.ContentSearchServiceI"
%><%@page import="com.freshdirect.cms.index.IndexerService"
%><%@page import="com.freshdirect.cms.search.SearchUtils"
%><%@page import="com.freshdirect.cms.search.term.Term"
%><%@page import="com.freshdirect.framework.conf.FDRegistry"
%><%@page import="com.freshdirect.framework.util.CSVUtils"
%><%@page import="com.freshdirect.fdstore.FDStoreProperties"
%><%@page language="java" contentType="application/csv"%><%!
	String escape(String string) {
		return CSVUtils.escape(string);
	}
%><%
	response.setHeader("Expires", "0");
	response.setHeader("Cache-control", "private");
	response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	response.setHeader("Content-Description", "File Transfer");
	response.setHeader("Content-Disposition", "attachment; filename=products.csv");
	CmsManager instance = CmsManager.getInstance();
	Set<ContentKey> keys = instance.getContentKeysByType(FDContentTypes.PRODUCT);
	Registry registry = FDRegistry.getInstance();
	IndexerService indexer = IndexerService.getInstance();
	List<AttributeIndex> attributes = indexer.getIndexesForType(FDContentTypes.PRODUCT);
	out.print(escape("contentKey"));
	for (AttributeIndex index : attributes) {
		out.print(",");
		String attribute = index.getAttributeName();
		if (index.getRelationshipAttributeName() != null)
			attribute += "." + index.getRelationshipAttributeName();
		out.print(escape(attribute));
	}
	out.println();
	boolean phEnabled = FDStoreProperties.isPrimaryHomeKeywordsEnabled();
	boolean recEnabled = FDStoreProperties.isSearchRecurseParentAttributesEnabled();
	for (ContentKey key : keys) {
		out.print(escape(key.getEncoded()));
		ContentNodeI node = instance.getContentNode(key);
		for (AttributeIndex index : attributes) {
			out.print(",");
			List<Term> values = SearchUtils.collectValues(node, index, true, phEnabled, recEnabled, instance);
			out.print(escape(Term.joinTerms(values, "; ")));
		}
		out.println();
	}
%>
