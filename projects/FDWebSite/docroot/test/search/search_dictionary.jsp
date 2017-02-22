<!DOCTYPE html>
<%@page import="com.freshdirect.cms.search.LuceneSearchService"%>
<%@page import="com.freshdirect.cms.index.FullIndexerService"%>
<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="iso-8859-1"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.ContentSearch"%>
<%@page import="com.freshdirect.cms.search.AutoComplete"%>
<%@page import="com.freshdirect.cms.search.AutocompleteService"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.cms.search.ContentSearchServiceI"%>
<%@page import="com.freshdirect.framework.conf.FDRegistry"%>
<%@page import="org.apache.hivemind.Registry"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.cms.search.AttributeIndex"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.freshdirect.cms.ContentNodeI"%>
<%@page import="com.freshdirect.cms.search.SearchUtils"%>
<%@page import="com.freshdirect.cms.search.term.Term"%>
<%@page import="com.freshdirect.cms.search.term.SearchTermNormalizer"%>
<%@page import="com.freshdirect.cms.search.SynonymDictionary"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="com.freshdirect.cms.search.term.Synonym"%>
<%@page import="com.freshdirect.cms.search.term.SynonymSearchTermNormalizerFactory"%>
<%@page import="com.freshdirect.cms.search.term.SynonymSpellingTermNormalizerFactory"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%><html>
<%!
Logger LOG = LoggerFactory.getInstance("search_dictionary.jsp");
class MySynonymDictionary extends SynonymDictionary {
	public MySynonymDictionary() {
		
	}
}

SortedSet<String> pAttributes = new TreeSet<String>(); 
SortedSet<String> rAttributes = new TreeSet<String>();
SynonymDictionary synonyms = SynonymDictionary.createFromCms(new SynonymSearchTermNormalizerFactory());
SynonymDictionary spellingSynonyms = SynonymDictionary.createSpellingFromCms(new SynonymSpellingTermNormalizerFactory());
SynonymDictionary permutations = new MySynonymDictionary();

class Normalized {
	String original;
	List<String> normalized;
	
	Normalized(String original, List<String> normalized) {
		this.original = original;
		this.normalized = normalized;
	}
}

class Item {
	ContentKey key;
	Map<String, List<Normalized>> normalized = new HashMap<String, List<Normalized>>();
	boolean dirty = false;
	
	void putNormalized(String attribute, String original, List<String> normalized) {
		if (!this.normalized.containsKey(attribute)) {
			List<Normalized> items = new ArrayList<Normalized>();
			items.add(new Normalized(original, normalized));
			this.normalized.put(attribute, items);
		} else {
			this.normalized.get(attribute).add(new Normalized(original, normalized));
		}
		dirty = true;
	}
}

Comparator<Item> comparator = new Comparator<Item>() {
	public int compare(Item o1, Item o2) {
		int i = o1.key.getType().getName().compareTo(o2.key.getType().getName());
		if (i == 0)
			return o1.key.getId().compareTo(o2.key.getId());
		else
			return i;
	}
};

%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" lang="en-US">
<title>Search Dictionary</title>
<link rel="stylesheet" href="/assets/css/test/search/style.css" type="text/css">
<style type="text/css">
td {
	vertical-align: top;
	white-space: nowrap;
}

.normalized {
	color: #000099;
	font-style: italic;
}
</style>
</head>
<%
	Registry registry = FDRegistry.getInstance();
	ContentSearchServiceI search = new LuceneSearchService();
	CmsManager instance = CmsManager.getInstance();
	FullIndexerService indexer = FullIndexerService.getInstance();
	Set<ContentKey> keys = instance.getContentKeys();
	List<Item> products = new ArrayList<Item>(keys.size());
	List<Item> recipes = new ArrayList<Item>(keys.size());
	boolean phEnabled = FDStoreProperties.isPrimaryHomeKeywordsEnabled();
	boolean recuEnabled = FDStoreProperties.isSearchRecurseParentAttributesEnabled();
	
	int i = 0;
	for (ContentKey key : keys) {
		ContentType type = key.getType();
		char tp = '\0';
		if (FDContentTypes.PRODUCT.equals(type))
			tp = 'p';
		else if (FDContentTypes.RECIPE.equals(type))
			tp = 'r';
		else
			continue;

		i++;
		if (i % 10000 == 0)
			LOG.info("processed " + i + " nodes so far");
		List<AttributeIndex> indexes = indexer.getIndexesForType(key.getType());
		if (indexes == null || indexes.isEmpty())
			continue;
		
		ContentNodeI node = instance.getContentNode(key);
		if (node == null)
			continue;

		Item item = new Item();
		item.key = key;
		for (AttributeIndex index : indexes) {
			String attributeName;
			attributeName = index.getAttributeName().toLowerCase();
			if (attributeName.startsWith("keyword"))
				attributeName = attributeName.toLowerCase();
			if (attributeName.equals("full_name"))
				attributeName = "full_name";
			if (attributeName.equals("name"))
				attributeName = "full_name";
			if (index.getRelationshipAttributeName() != null)
				attributeName += "." + index.getRelationshipAttributeName();

			List<Term> values = SearchUtils.collectValues(node, index, true, phEnabled, recuEnabled, instance);
			for (Term value : values) {
				if (index.isText()) {
					if (attributeName.toLowerCase().startsWith("keyword")) {
						if (tp == 'p')
							pAttributes.add(attributeName);
						else
							rAttributes.add(attributeName);
						item.putNormalized(attributeName, value.toString(), Collections.<String>emptyList());
					} else {
						item.putNormalized(attributeName, value.toString(), Collections.<String>emptyList());
						if (tp == 'p')
							pAttributes.add(attributeName);
						else
							rAttributes.add(attributeName);
					}
				} else {
					String original = value.toString();
					List<Term> ts = new SearchTermNormalizer(permutations, value).getTerms();
					List<String> normalized = new ArrayList<String>(ts.size());
					for (Term t : ts) {
						normalized.add(t.toString());
					}
					item.putNormalized(attributeName, original, normalized);
					if (tp == 'p')
						pAttributes.add(attributeName);
					else
						rAttributes.add(attributeName);
				}
			}
		}
		
		if (item.dirty) {
			if (tp == 'p')
				products.add(item);
			else
				recipes.add(item);
		}
	}
	LOG.info("processed total of " + i + " nodes");
	Collections.sort(products, comparator);
	LOG.info("sorted " + products.size() + " products");
	Collections.sort(recipes, comparator);
	LOG.info("sorted " + recipes.size() + " recipes");
%>
<body>
<div>
<div class="left">
<div class="section">
	<div class="section-header">Product Dictionary</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Content Key</th>
					<% for (String attribute : pAttributes) { %>
					<th><%= attribute %></th>
					<% } %>
				</tr>
			</thead>
			<tbody>
				<% for (Item item : products) {
				%>
				<tr>
					<td><%= item.key.getEncoded() %></td>
					<% for (String attribute : pAttributes) {
						List<Normalized> values = item.normalized.get(attribute);  %>
					<td><% if (values != null) { for (Normalized value : values) { %>
					<div><%= value.original %></div>
						<% for (String normalized : value.normalized) { %>
						<div class="normalized"><%= normalized %></div>
						<% } %>
					<% } } %></td>
					<% } %>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div><!-- section -->
<div class="section">
	<div class="section-header">Recipe Dictionary</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Content Key</th>
					<% for (String attribute : rAttributes) { %>
					<th><%= attribute %></th>
					<% } %>
				</tr>
			</thead>
			<tbody>
				<% for (Item item : recipes) {
				%>
				<tr>
					<td><%= item.key.getEncoded() %></td>
					<% for (String attribute : rAttributes) {
						List<Normalized> values = item.normalized.get(attribute);  %>
					<td><% if (values != null) { for (Normalized value : values) { %>
					<div><%= value.original %></div>
						<% for (String normalized : value.normalized) { %>
						<div class="normalized"><%= normalized %></div>
						<% } %>
					<% } } %></td>
					<% } %>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div><!-- section -->
<div class="section">
	<% { %><div class="section-header">Synonyms</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Prefix</th>
					<th>Term</th>
					<th>Synonyms</th>
				</tr>
			</thead>
			<tbody>
				<%
					Map<String, List<Synonym>> synonymMap = synonyms.getSynonyms();
					SortedSet<String> prefixes = new TreeSet<String>(synonymMap.keySet());
					for (String prefix : prefixes) {
						List<Synonym> syns = synonymMap.get(prefix);
						for (int j = 0; j < syns.size(); j++) {
							Synonym syn = syns.get(j);
				%>
				<tr>
					<% if (j == 0) { %><td rowspan="<%= syns.size() %>"><%= prefix %></td><% } %>
					<td><%= syn.getTermAsString() %></td>
					<td><% for (Term t : syn.getSynonyms()) { %>
						<div><%= t.toString() %></div>
					<% } %></td>
				</tr>
				<%
						}
					}
				%>
			</tbody>
		</table>
	</div><% } %>
</div><!-- section -->
<div class="section">
	<% { %><div class="section-header">Spelling Synonyms</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Prefix</th>
					<th>Term</th>
					<th>Synonyms</th>
				</tr>
			</thead>
			<tbody>
				<%
					Map<String, List<Synonym>> synonymMap = spellingSynonyms.getSynonyms();
					SortedSet<String> prefixes = new TreeSet<String>(synonymMap.keySet());
					for (String prefix : prefixes) {
						List<Synonym> syns = synonymMap.get(prefix);
						for (int j = 0; j < syns.size(); j++) {
							Synonym syn = syns.get(j);
				%>
				<tr>
					<% if (j == 0) { %><td rowspan="<%= syns.size() %>"><%= prefix %></td><% } %>
					<td><%= syn.getTermAsString() %></td>
					<td><% for (Term t : syn.getSynonyms()) { %>
						<div><%= t.toString() %></div>
					<% } %></td>
				</tr>
				<%
						}
					}
				%>
			</tbody>
		</table>
	</div><% } %>
</div><!-- section -->
<div class="section">
	<% { %><div class="section-header">Compound Synonyms</div>
	<div class="section-body">
		<table class="table">
			<thead>
				<tr>
					<th>Prefix</th>
					<th>Term</th>
					<th>Synonyms</th>
				</tr>
			</thead>
			<tbody>
				<%
					Map<String, List<Synonym>> synonymMap = permutations.getSynonyms();
					SortedSet<String> prefixes = new TreeSet<String>(synonymMap.keySet());
					for (String prefix : prefixes) {
						List<Synonym> syns = synonymMap.get(prefix);
						for (int j = 0; j < syns.size(); j++) {
							Synonym syn = syns.get(j);
				%>
				<tr>
					<% if (j == 0) { %><td rowspan="<%= syns.size() %>"><%= prefix %></td><% } %>
					<td><%= syn.getTermAsString() %></td>
					<td><% for (Term t : syn.getSynonyms()) { %>
						<div><%= t.toString() %></div>
					<% } %></td>
				</tr>
				<%
						}
					}
				%>
			</tbody>
		</table>
	</div><% } %>
</div><!-- section -->
</div><!-- left -->
</div><!-- body -->
</body>
</html>