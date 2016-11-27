package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class AmpersandConv extends SingleTokenCoder {
	public AmpersandConv(List<Term> terms) {
		super(terms);
	}

	public AmpersandConv(Term term) {
		super(term);
	}

	public AmpersandConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<String> convert(String term) {
		List<String> ret = new ArrayList<String>();
		String[] terms = Term.split(term, "&");
		boolean latch = false;
		for (String t : terms) {
			if (latch) {
				ret.add("and");
			} else
				latch = true;				
			if (!t.isEmpty())
				ret.add(t);
		}
		return ret;
	}
}
