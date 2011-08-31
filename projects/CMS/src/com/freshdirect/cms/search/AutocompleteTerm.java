package com.freshdirect.cms.search;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.search.term.AutocompletePermuter;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;

public class AutocompleteTerm implements Serializable {
	private static final long serialVersionUID = -4981032017185644437L;

	private Term term;
	private ContentKey contentKey;

	public AutocompleteTerm(Term term, ContentKey contentKey) {
		super();
		this.term = term;
		this.contentKey = contentKey;
	}

	public Term getTerm() {
		return term;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	List<Term> permute() {
		TermCoder filter = new AutocompletePermuter(term);
		return filter.getTerms();
	}
}
