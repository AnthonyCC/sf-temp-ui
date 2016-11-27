package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.freshdirect.cms.search.SynonymDictionary;

public abstract class TermCoder {
	private static class ProxyTermList implements List<Term> {
		TermCoder termCoder;

		public ProxyTermList(TermCoder termCoder) {
			super();
			this.termCoder = termCoder;
		}

		public void add(int index, Term element) {
			termCoder.getTerms().add(index, element);
		}

		public boolean add(Term e) {
			return termCoder.getTerms().add(e);
		}

		public boolean addAll(Collection<? extends Term> c) {
			return termCoder.getTerms().addAll(c);
		}

		public boolean addAll(int index, Collection<? extends Term> c) {
			return termCoder.getTerms().addAll(index, c);
		}

		public void clear() {
			termCoder.getTerms().clear();
		}

		public boolean contains(Object o) {
			return termCoder.getTerms().contains(o);
		}

		public boolean containsAll(Collection<?> c) {
			return termCoder.getTerms().containsAll(c);
		}

		public boolean equals(Object o) {
			return termCoder.getTerms().equals(o);
		}

		public Term get(int index) {
			return termCoder.getTerms().get(index);
		}

		public int hashCode() {
			return termCoder.getTerms().hashCode();
		}

		public int indexOf(Object o) {
			return termCoder.getTerms().indexOf(o);
		}

		public boolean isEmpty() {
			return termCoder.getTerms().isEmpty();
		}

		public Iterator<Term> iterator() {
			return termCoder.getTerms().iterator();
		}

		public int lastIndexOf(Object o) {
			return termCoder.getTerms().lastIndexOf(o);
		}

		public ListIterator<Term> listIterator() {
			return termCoder.getTerms().listIterator();
		}

		public ListIterator<Term> listIterator(int index) {
			return termCoder.getTerms().listIterator(index);
		}

		public Term remove(int index) {
			return termCoder.getTerms().remove(index);
		}

		public boolean remove(Object o) {
			return termCoder.getTerms().remove(o);
		}

		public boolean removeAll(Collection<?> c) {
			return termCoder.getTerms().removeAll(c);
		}

		public boolean retainAll(Collection<?> c) {
			return termCoder.getTerms().retainAll(c);
		}

		public Term set(int index, Term element) {
			return termCoder.getTerms().set(index, element);
		}

		public int size() {
			return termCoder.getTerms().size();
		}

		public List<Term> subList(int fromIndex, int toIndex) {
			return termCoder.getTerms().subList(fromIndex, toIndex);
		}

		public Object[] toArray() {
			return termCoder.getTerms().toArray();
		}

		public <T> T[] toArray(T[] a) {
			return termCoder.getTerms().toArray(a);
		}
	}

	protected static List<List<String>> permuteSyms(SynonymDictionary dictionary, List<String> words) {
		if (dictionary != null) {
			List<List<String>> alternatives = new ArrayList<List<String>>();
			for (String word : words) {
				Synonym synonym = dictionary.getSynonymForWord(word);
				if (synonym != null) {
					List<String> synonyms = new ArrayList<String>();
					synonyms.add(synonym.getFirstWord());
					for (Term syn : synonym.getSynonyms())
						if (syn.getTokens().size() == 1)
							synonyms.add(syn.getTokens().get(0));
					alternatives.add(synonyms);
				} else {
					alternatives.add(Collections.singletonList(word));
				}
			}
			return alternatives;
		} else
			return Collections.singletonList(words);
	}

	protected static boolean isDashedDecimals(String[] tokens) {
		if (tokens.length < 2)
			return false;
		if (isDecimal(tokens[0]) && Character.isDigit(tokens[1].charAt(0)))
			return true;
		return false;
	}

	private static boolean isDecimal(String token) {
		for (int i = 0; i < token.length(); i++)
			if (!Character.isDigit(token.charAt(i)))
				return false;
		return true;
	}

	private List<Term> terms = null;
	private List<Term> input = null;

	public TermCoder(List<Term> terms) {
		this.input = terms;
	}

	public TermCoder(Term term) {
		this(Collections.singletonList(term));
	}

	public TermCoder(TermCoder coder) {
		this(new ProxyTermList(coder));
	}

	protected abstract List<Term> convert(List<Term> input);

	public final List<Term> getTerms() {
		if (terms == null)
			terms = convert(input);
		return terms;
	}

	@Override
	public String toString() {
		return Term.join(getTerms());
	}

	protected List<String> joinParts(List<List<String>> parts, int[] index) {
		StringBuilder buf = new StringBuilder();
		if (parts.size() > 0)
			buf.append(parts.get(0).get(index[0]));
		for (int i = 1; i < parts.size(); i++) {
			buf.append("-");
			buf.append(parts.get(i).get(index[i]));
		}
		return Collections.singletonList(buf.toString());
	}
}
