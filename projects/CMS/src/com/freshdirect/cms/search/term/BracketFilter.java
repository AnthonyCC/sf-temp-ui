package com.freshdirect.cms.search.term;

import java.util.List;

public class BracketFilter extends SingleSingleTokenCoder {
	private static final String brackets = "()<>[]{}";

	public BracketFilter(List<Term> terms) {
		super(terms);
	}

	public BracketFilter(Term term) {
		super(term);
	}

	public BracketFilter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String term) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < term.length(); i++) {
			char c = term.charAt(i);
			if (brackets.indexOf(c) < 0)
				buf.append(c);
		}
		return buf.toString();
	}
}
