package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTermCoder extends TermCoder {
	public SingleTermCoder(List<Term> terms) {
		super(terms);
	}

	public SingleTermCoder(Term term) {
		super(term);
	}

	public SingleTermCoder(TermCoder coder) {
		super(coder);
	}

	@Override
	protected final List<Term> convert(List<Term> input) {
		List<Term> ret = new ArrayList<Term>(input.size());
		for (Term term : input) {
			Term t = convert(term);
			if (t != null && !t.isEmpty())
				ret.add(t);
		}
		return ret;
	}

	protected abstract Term convert(Term term);
}
