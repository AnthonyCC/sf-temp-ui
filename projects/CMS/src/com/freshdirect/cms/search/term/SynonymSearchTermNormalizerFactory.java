package com.freshdirect.cms.search.term;

import java.util.List;

public class SynonymSearchTermNormalizerFactory implements TermCoderFactory {
	@Override
	public TermCoder create(List<Term> terms) {
		return new SearchTermNormalizer(terms, true);
	}

	@Override
	public TermCoder create(Term term) {
		return new SearchTermNormalizer(term, true);
	}
}
