package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialismPermuter extends PermuterTermCoder {
	public InitialismPermuter(List<Term> terms) {
		super(terms);
	}

	public InitialismPermuter(Term term) {
		super(term);
	}

	public InitialismPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		List<String> inits = new ArrayList<String>();
		boolean initialism = true;
		boolean allDigits = true;
		for (int i = 0; i < term.length(); i++) {
			char ch = term.charAt(i);
			if (i % 2 == 0) {
				if (Character.isLetterOrDigit(ch)) {
					if (Character.isLetter(ch))
						allDigits = false;
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
		if (allDigits)
			return Collections.singletonList(Collections.singletonList(term));
		else if (initialism && inits.size() > 1) {
			List<List<String>> ret = new ArrayList<List<String>>();
			ret.add(inits);
			ret.add(Collections.singletonList(Term.join(inits, "")));
			return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
