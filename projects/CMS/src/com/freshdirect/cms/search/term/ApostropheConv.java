package com.freshdirect.cms.search.term;

import java.util.List;

public class ApostropheConv extends SingleSingleTokenCoder {
	public ApostropheConv(List<Term> terms) {
		super(terms);
	}

	public ApostropheConv(Term term) {
		super(term);
	}

	public ApostropheConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String term) {
		if (term.equals("'"))
			return null;
		if (term.equals("'n"))
			return "and";
		if (term.equals("'s"))
			return null;
		if (term.endsWith("'s"))
			return term.substring(0, term.length() - 2);
		if (term.equals("d'"))
			return null;
		if (term.startsWith("d'"))
			return term.substring(2, term.length());
		else
			return term.replace("'", "");
	}
}
