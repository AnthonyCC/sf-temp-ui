package com.freshdirect.cms.search.term;

import java.util.List;

public class IdentityConvFactory implements TermCoderFactory {
	@Override
	public TermCoder create(List<Term> terms) {
		return new IdentityConv(terms);
	}

	@Override
	public TermCoder create(Term term) {
		return new IdentityConv(term);
	}
}
