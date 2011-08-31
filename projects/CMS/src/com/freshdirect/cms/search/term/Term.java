package com.freshdirect.cms.search.term;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class Term implements Serializable {
	private static final long serialVersionUID = -734544116938361150L;

	public static final String DEFAULT_SEPARATOR = " ";

	public static final String DEFAULT_TOKENIZERS = " \t\n\r\f";

	private List<String> tokens = new ArrayList<String>();

	public Term(String term) {
		this.tokens.addAll(tokenize(term, getTokenizers()));
	}

	public Term(List<String> tokens) {
		this.tokens.addAll(tokens);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}

	public boolean isEmpty() {
		return tokens.isEmpty();
	}

	public List<String> getTokens() {
		return tokens;
	}
	
	public Term subTerm(int beginIndex, int endIndex) {
		return new Term(tokens.subList(beginIndex, endIndex));
	}
	
	public int length() {
		return tokens.size();
	}
	
	public String concatenate() {
		StringBuilder buf = new StringBuilder();
		for (String token : tokens)
			buf.append(token);
		return buf.toString();
	}

	@Override
	public String toString() {
		return join(tokens, getSeparator());
	}

	public String getSeparator() {
		return DEFAULT_SEPARATOR;
	}

	public String getTokenizers() {
		return DEFAULT_TOKENIZERS;
	}

	public final static String joinTerms(List<Term> tokens, String separator) {
		int len = tokens.size();
		for (Term term : tokens)
			len += term.length();
		StringBuilder buf = new StringBuilder(len);
		Iterator<Term> it = tokens.iterator();
		if (it.hasNext())
			buf.append(it.next());
		while (it.hasNext()) {
			buf.append(separator);
			buf.append(it.next());
		}
		return buf.toString();
	}
	
	public final static String join(List<String> tokens, String separator) {
		int len = tokens.size();
		for (String term : tokens)
			len += term.length();
		StringBuilder buf = new StringBuilder(len);
		Iterator<String> it = tokens.iterator();
		if (it.hasNext())
			buf.append(it.next());
		while (it.hasNext()) {
			buf.append(separator);
			buf.append(it.next());
		}
		return buf.toString();
	}

	public final static List<String> tokenize(String term, String delimiters) {
		List<String> tokens = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(term, delimiters);
		while (tokenizer.hasMoreTokens())
			tokens.add(tokenizer.nextToken());
		return tokens;
	}

	public final static String[] split(String term, String delimiters) {
		int separators = 0;
		for (int i = 0; i < term.length(); i++)
			if (delimiters.indexOf(term.charAt(i)) >= 0)
				separators++;
		String[] strings = new String[separators + 1];
		int pos = 0;
		int next = 0;
		int i = 0;
		while ((next = nextIndex(term, delimiters, pos)) >= 0) {
			strings[i++] = term.substring(pos, next);
			pos = next + 1;
		}
		if (pos < term.length())
			strings[i] = term.substring(pos);
		else
			strings[i] = "";
		return strings;
	}

	private final static int nextIndex(String term, String delimiter, int fromIndex) {
		for (int i = fromIndex; i < term.length(); i++)
			for (int j = 0; j < delimiter.length(); j++)
				if (term.charAt(i) == delimiter.charAt(j))
					return i;
		return -1;
	}

	public static List<Term> keywordize(String term, String delimiters) {
		List<String> keywords = tokenize(term, delimiters);
		List<Term> terms = new ArrayList<Term>(keywords.size());
		for (String keyword : keywords)
			terms.add(new Term(keyword));
		return terms;
	}

	public static String join(List<Term> terms) {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (Term t : terms) {
			if (!first)
				buf.append(t.getSeparator());
			buf.append(t.toString());
			first = false;
		}
		return buf.toString();
	}

	public int indexOf(Term t) {
		int l = tokens.size() - t.tokens.size() + 1;
		OUTER: for (int i = 0; i < l; i++) {
			for (int j = 0; j < t.tokens.size(); j++)
				if (!tokens.get(i + j).equals(t.tokens.get(j)))
					continue OUTER;
			
			return i;
		}
		return -1;
	}

	public int indexOf(String token) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).equals(token))
				return i;
		return -1;
	}
}
