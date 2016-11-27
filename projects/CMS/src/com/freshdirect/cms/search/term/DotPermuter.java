package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class DotPermuter extends TermCoder {
	public DotPermuter(List<Term> terms) {
		super(terms);
	}

	public DotPermuter(Term term) {
		super(term);
	}

	public DotPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> ret = new ArrayList<Term>();
		for (Term t : input) {
			List<List<String>> items = convert(t);
			for (List<String> item : items)
				if (!item.isEmpty())
					ret.add(new Term(item));
		}
		return ret;
	}

	protected List<List<String>> convert(Term term) {
		List<List<String>> ret = new ArrayList<List<String>>();
		ret.add(term.getTokens());
		List<String> noDots = new ArrayList<String>();
		boolean differs = false;
		for (String word : term.getTokens()) {
			List<String> processed = new ArrayList<String>();
			String[] tokens = Term.split(word, ".");
			if (tokens.length == 1) {
				processed.add(word);
			} else
				for (int i = 0; i < tokens.length; i++) {
					String t = tokens[i];
					if (t.isEmpty())
						continue;
					String u = i + 1 < tokens.length ? tokens[i + 1] : null;
					if (u != null && !u.isEmpty() && Character.isDigit(t.charAt(t.length() - 1)) && Character.isDigit(u.charAt(0))) {
						processed.add(t + "." + u);
						i++;
					} else {
						processed.add(t);
						differs = true;
					}
				}
			noDots.addAll(processed);
		}
		if (differs)
			ret.add(noDots);
		return ret;
	}
}
