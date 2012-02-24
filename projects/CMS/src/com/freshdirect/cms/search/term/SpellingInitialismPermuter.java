package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellingInitialismPermuter extends PermuterTermCoder {
	public SpellingInitialismPermuter(List<Term> terms) {
		super(terms);
	}

	public SpellingInitialismPermuter(Term term) {
		super(term);
	}

	public SpellingInitialismPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		List<String> inits = new ArrayList<String>();
		boolean initialism = true;
		for (int i = 0; i < term.length(); i++) {
			char ch = term.charAt(i);
			if (i % 2 == 0) {
				if (Character.isLetterOrDigit(ch)) {
					inits.add(new String(new char[] { ch }));
				} else {
					initialism = false;
					break;
				}
			} else {
				if (ch != '.') {
					initialism = false;
					break;
				}
			}
		}
		if (initialism && inits.size() > 1) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(Collections.singletonList(term));
			ret.add(Collections.singletonList(Term.join(inits, "")));
			return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
