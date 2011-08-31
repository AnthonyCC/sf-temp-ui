package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApostrophePermuter extends PermuterTermCoder {
	public ApostrophePermuter(List<Term> terms) {
		super(terms);
	}

	public ApostrophePermuter(Term term) {
		super(term);
	}

	public ApostrophePermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		if (term.endsWith("'s")) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(Collections.singletonList(term));
			ret.add(Collections.singletonList(term.substring(0, term.length() - 2)));
			return ret;
		} else if (term.startsWith("d'")) {
				List<List<String>> ret = new ArrayList<List<String>>();
				ret.add(Collections.singletonList(term));
				ret.add(Collections.singletonList(term.substring(2, term.length())));
				return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
