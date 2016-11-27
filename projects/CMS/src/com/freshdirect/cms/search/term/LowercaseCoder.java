package com.freshdirect.cms.search.term;

import java.util.List;

public class LowercaseCoder extends SingleSingleTokenCoder {
	public LowercaseCoder(List<Term> terms) {
		super(terms);
	}

	public LowercaseCoder(Term term) {
		super(term);
	}

	public LowercaseCoder(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String term) {
		return term.toLowerCase();
	}
}
