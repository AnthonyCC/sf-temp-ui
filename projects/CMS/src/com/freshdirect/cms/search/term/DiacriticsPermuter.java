package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiacriticsPermuter extends TermCoder {
	public DiacriticsPermuter(List<Term> terms) {
		super(terms);
	}

	public DiacriticsPermuter(Term term) {
		super(term);
	}

	public DiacriticsPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> ret = new ArrayList<Term>();
		for (Term term : input)
			ret.addAll(convert(term));
		return ret;
	}

	private List<Term> convert(Term term) {
		List<String> p = new ArrayList<String>(term.getTokens().size());
		boolean hasDiactrics = false;
		for (String token : term.getTokens()) {
			String d = DiacriticsRemoval.removeDiactrics(token);
			p.add(d);
			if (!hasDiactrics && !d.equals(token))
				hasDiactrics = true;
		}
		if (hasDiactrics) {
			List<Term> ret = new ArrayList<Term>();
			ret.add(term);
			ret.add(new Term(p));
			return ret;
		} else
			return Collections.singletonList(term);
	}
}
