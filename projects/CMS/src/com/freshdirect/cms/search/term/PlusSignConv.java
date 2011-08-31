package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlusSignConv extends PermuterTermCoder {
	private boolean retainPunctuation;

	public PlusSignConv(List<Term> terms) {
		super(terms);
	}

	public PlusSignConv(Term term) {
		super(term);
	}

	public PlusSignConv(TermCoder coder) {
		super(coder);
	}

	public PlusSignConv(List<Term> terms, boolean retainPunctuation) {
		super(terms);
		this.retainPunctuation = retainPunctuation;
	}

	public PlusSignConv(Term term, boolean retainPunctuation) {
		super(term);
		this.retainPunctuation = retainPunctuation;
	}

	public PlusSignConv(TermCoder coder, boolean retainPunctuation) {
		super(coder);
		this.retainPunctuation = retainPunctuation;
	}

	@Override
	protected List<List<String>> convert(String term) {
		if (term.indexOf('+') < 0)
			return Collections.singletonList(Collections.singletonList(term));
		List<List<String>> ret = new ArrayList<List<String>>();
		if (retainPunctuation == true)
			ret.add(Collections.singletonList(term));

		List<String> plusRet = new ArrayList<String>();
		List<String> andRet = new ArrayList<String>();

		String[] terms = Term.split(term, "+");
		boolean latch = false;
		for (String t : terms) {
			if (latch) {
				plusRet.add("plus");
				andRet.add("and");
			} else
				latch = true;
			if (!t.isEmpty()) {
				plusRet.add(t);
				andRet.add(t);
			}
		}
		
		ret.add(plusRet);
		if (term.length() == 1 || term.charAt(term.length() - 1) != '+')
			ret.add(andRet);
		return ret;
	}
}
