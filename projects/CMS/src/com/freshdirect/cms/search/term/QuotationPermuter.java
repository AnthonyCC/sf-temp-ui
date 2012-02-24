package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuotationPermuter extends PermuterTermCoder {
	public QuotationPermuter(List<Term> terms) {
		super(terms);
	}

	public QuotationPermuter(Term term) {
		super(term);
	}

	public QuotationPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		boolean inch = false;
		if (term.equals("\""))
			return null;
		if (term.endsWith("\"") && Character.isDigit(term.charAt(term.length() - 2)))
			inch = true;
		List<String> word = new ArrayList<String>();
		String[] terms = Term.split(term, "\"");
		for (String t : terms) {
			if (!t.isEmpty())
				word.add(t);
		}
		if (inch && !word.isEmpty()) {
			List<List<String>> ret = new ArrayList<List<String>>();
			List<String> one = new ArrayList<String>();
			one.addAll(word);
			String last = one.get(one.size() - 1).concat("\"");
			one.set(one.size() - 1, last);
			ret.add(one);
			word.add("inch");
			ret.add(word);
			return ret;
		}
		else
			return Collections.singletonList(word);
	}
}
