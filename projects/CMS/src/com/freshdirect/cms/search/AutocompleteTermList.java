package com.freshdirect.cms.search;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.search.term.AutocompleteTermNormalizer;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;

public abstract class AutocompleteTermList {
	private List<AutocompleteTerm> terms = null;

	public static void addTerm(List<AutocompleteTerm> terms, ContentKey key, String name) {
		TermCoder filter = new AutocompleteTermNormalizer(new Term(name));
		terms.add(new AutocompleteTerm(filter.getTerms().get(0), key));
	}

	protected abstract List<AutocompleteTerm> doGetTerms();

	public synchronized List<AutocompleteTerm> getTerms() {
		if (terms == null)
			terms = doGetTerms();
		return terms;
	}

	public synchronized void invalidate() {
		terms = null;
	}
}
