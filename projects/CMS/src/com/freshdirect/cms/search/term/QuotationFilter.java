package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class QuotationFilter extends SingleTokenCoder {
	public QuotationFilter(List<Term> terms) {
		super(terms);
	}

	public QuotationFilter(Term term) {
		super(term);
	}

	public QuotationFilter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<String> convert(String term) {
		boolean inch = false;
		if (term.equals("\""))
			return null;
		if (term.endsWith("\"") && Character.isDigit(term.charAt(term.length() - 2)))
			inch = true;
		List<String> ret = new ArrayList<String>();
		String[] terms = Term.split(term, "\"");
		for (String t : terms) {
			if (!t.isEmpty())
				ret.add(t);
		}
		if (inch) {
			String last = ret.remove(ret.size() - 1);
			ret.add(last + "\"");
		}
		return ret;
	}
}
