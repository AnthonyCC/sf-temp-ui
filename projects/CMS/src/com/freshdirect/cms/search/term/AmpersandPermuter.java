package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmpersandPermuter extends PermuterTermCoder {
	public AmpersandPermuter(List<Term> terms) {
		super(terms);
	}

	public AmpersandPermuter(Term term) {
		super(term);
	}

	public AmpersandPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		String[] terms = Term.split(term, "&");
		if (terms.length > 1) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(Collections.singletonList(term));
			boolean latch = false;
			List<String> r = new ArrayList<String>();
			for (String t : terms) {
				if (latch) {
					r.add("and");
				} else
					latch = true;				
				if (!t.isEmpty())
					r.add(t);
			}
			ret.add(r);
			return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
