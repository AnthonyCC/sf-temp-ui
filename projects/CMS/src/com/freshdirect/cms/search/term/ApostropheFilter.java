package com.freshdirect.cms.search.term;

import java.util.List;

public class ApostropheFilter extends SingleSingleTokenCoder {
	public ApostropheFilter(List<Term> terms) {
		super(terms);
	}

	public ApostropheFilter(Term term) {
		super(term);
	}

	public ApostropheFilter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String term) {
		if (term.equals("'"))
			return null;
		if (term.equals("'s"))
			return null;
		if (term.equals("d'"))
			return null;
		if (term.startsWith("'"))
			return term.substring(1);
		if (term.endsWith("'"))
			return term.substring(0, term.length() - 1);
		else
			return term;
	}
}
