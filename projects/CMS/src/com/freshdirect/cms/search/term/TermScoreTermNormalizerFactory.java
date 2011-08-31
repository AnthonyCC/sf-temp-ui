package com.freshdirect.cms.search.term;

import java.util.List;

public class TermScoreTermNormalizerFactory implements TermCoderFactory {
	@Override
	public TermCoder create(List<Term> terms) {
		return new TermScoreTermNormalizer(terms);
	}

	@Override
	public TermCoder create(Term term) {
		return new TermScoreTermNormalizer(term);
	}
}
