<%@page import="java.io.PrintWriter"
%><%@page import="java.util.Collection"
%><%@page import="java.util.Iterator"
%><%@page import="java.util.List"
%><%@page import="java.util.Map"
%><%@page import="java.util.SortedSet"
%><%@page import="java.util.TreeSet"
%><%@page import="com.freshdirect.cms.search.SynonymDictionary"
%><%@page import="com.freshdirect.cms.search.term.Synonym"
%><%@page import="com.freshdirect.cms.search.term.Term"
%><%@page import="com.freshdirect.framework.util.CSVUtils"
%><%@page import="com.freshdirect.cms.search.term.IdentityConvFactory"
%><%@page import="com.freshdirect.cms.search.term.SynonymSpellingTermNormalizerFactory"
%><%@page language="java" contentType="application/csv"%><%!
	String escape(String string) {
		return CSVUtils.escape(string);
	}
%><%
	response.setHeader("Expires", "0");
	response.setHeader("Cache-control", "private");
	response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	response.setHeader("Content-Description", "File Transfer");
	response.setHeader("Content-Disposition", "attachment; filename=synonyms.csv");
	out.print(escape("prefix"));
	out.print(",");
	out.print(escape("term"));
	out.print(",");
	out.print(escape("synonym"));
	out.println();
	SynonymDictionary dictionary = SynonymDictionary.createFromCms(new SynonymSpellingTermNormalizerFactory());
	Map<String, List<Synonym>> synonymMap = dictionary.getSynonyms();
	SortedSet<String> prefixes = new TreeSet<String>(synonymMap.keySet());
	for (String prefix : prefixes) {
		List<Synonym> synonyms = synonymMap.get(prefix);
		Iterator<Synonym> it = synonyms.iterator();
		if (it.hasNext()) {
			Synonym synonym = it.next();
			out.print(escape(prefix));
			out.print(",");
			out.print(escape(synonym.getTermAsString()));
			out.print(",");
			Collection<Term> alternatives = synonym.getSynonyms();
			Iterator<Term> it2 = alternatives.iterator();
			if (it2.hasNext()) {
				Term term = it2.next();
				out.print(escape(term.toString()));				
			}
			out.println();			
			while (it2.hasNext()) {
				Term term = it2.next();
				out.print(",");
				out.print(",");
				out.print(escape(term.toString()));				
				out.println();			
			}
		}
		while (it.hasNext()) {
			Synonym synonym = it.next();
			out.print(",");
			out.print(escape(synonym.getTermAsString()));
			out.print(",");
			Collection<Term> alternatives = synonym.getSynonyms();
			Iterator<Term> it2 = alternatives.iterator();
			if (it2.hasNext()) {
				Term term = it2.next();
				out.print(escape(term.toString()));				
			}
			out.println();			
			while (it2.hasNext()) {
				Term term = it2.next();
				out.print(",");
				out.print(",");
				out.print(escape(term.toString()));				
				out.println();			
			}
		}
	}
%>
