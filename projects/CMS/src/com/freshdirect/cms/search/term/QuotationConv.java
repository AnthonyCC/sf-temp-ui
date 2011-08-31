package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class QuotationConv extends SingleTokenCoder {
	public QuotationConv(List<Term> terms) {
		super(terms);
	}

	public QuotationConv(Term term) {
		super(term);
	}

	public QuotationConv(TermCoder coder) {
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
		if (inch)
			ret.add("inch");
		return ret;
	}
}
