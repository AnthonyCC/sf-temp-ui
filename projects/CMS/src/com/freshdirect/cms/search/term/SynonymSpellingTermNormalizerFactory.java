package com.freshdirect.cms.search.term;

import java.util.List;

public class SynonymSpellingTermNormalizerFactory implements TermCoderFactory {
	@Override
	public TermCoder create(List<Term> terms) {
		return new SpellingTermNormalizer(terms);
	}

	@Override
	public TermCoder create(Term term) {
		return new SpellingTermNormalizer(term);
	}
}
