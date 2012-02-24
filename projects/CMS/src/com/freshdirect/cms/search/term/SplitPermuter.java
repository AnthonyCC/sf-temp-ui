package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public abstract class SplitPermuter extends TermCoder {
	public SplitPermuter(List<Term> terms) {
		super(terms);
	}

	public SplitPermuter(Term term) {
		super(term);
	}

	public SplitPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> terms = new ArrayList<Term>();
		for (Term term : input)
			terms.addAll(convert(term));
		return terms;
	}

	/**
	 * This is very tricky. If the {@link #split(String)} method returns multiple strings then it will split the Term into two (or
	 * more) Terms
	 * 
	 * @param term
	 * @return
	 */
	protected final List<Term> convert(Term term) {
		List<Term> ret = new ArrayList<Term>();
		List<String> tokens = new ArrayList<String>();
		for (String token : term.getTokens()) {
			List<String> splits = split(token);
			if (splits.size() > 1) {
				String first = splits.get(0);
				if (!first.isEmpty())
					tokens.add(first);
				for (String split : splits.subList(1, splits.size())) {
					if (!tokens.isEmpty()) {
						ret.add(new Term(tokens));
						tokens = new ArrayList<String>();
					}
					if (!split.isEmpty())
						tokens.add(split);
				}
			} else if (!splits.isEmpty())
				for (String split : splits)
					if (!split.isEmpty())
						tokens.add(split);
		}
		if (!tokens.isEmpty())
			ret.add(new Term(tokens));
		return ret;
	}

	protected abstract List<String> split(String token);
}
