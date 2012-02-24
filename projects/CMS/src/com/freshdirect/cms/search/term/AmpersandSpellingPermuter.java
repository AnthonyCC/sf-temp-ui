package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmpersandSpellingPermuter extends PermuterTermCoder {
	public AmpersandSpellingPermuter(List<Term> terms) {
		super(terms);
	}

	public AmpersandSpellingPermuter(Term term) {
		super(term);
	}

	public AmpersandSpellingPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		if (term.equals("&")) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(Collections.singletonList("&"));
			ret.add(Collections.singletonList("and"));
			return ret;
		}
		String[] terms = Term.split(term, "&");
		if (terms.length > 1) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(Collections.singletonList(term));
			boolean latch = false;
			List<String> r = new ArrayList<String>();
			List<String> q = new ArrayList<String>();
			for (String t : terms) {
				if (latch) {
					r.add("and");
					q.add("&");
				} else
					latch = true;				
				if (!t.isEmpty()) {
					r.add(t);
					q.add(t);
				}
			}
			if (!r.isEmpty())
				ret.add(r);
			if (!q.isEmpty())
				ret.add(q);
			return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
