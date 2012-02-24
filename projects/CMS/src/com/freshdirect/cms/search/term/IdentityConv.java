package com.freshdirect.cms.search.term;

import java.util.List;

public class IdentityConv extends TermCoder {
	public IdentityConv(List<Term> terms) {
		super(terms);
	}

	public IdentityConv(Term term) {
		super(term);
	}

	public IdentityConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		return input;
	}
}
