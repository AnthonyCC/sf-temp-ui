package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlashConv extends SingleTokenCoder {
	public SlashConv(List<Term> terms) {
		super(terms);
	}

	public SlashConv(Term term) {
		super(term);
	}

	public SlashConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<String> convert(String term) {
		if (term.equals("/"))
			return Collections.emptyList();
		if (term.equals("w/o"))
			return Collections.singletonList("without");
		else if (term.equals("w/"))
			return Collections.singletonList("with");
		else {
			List<String> ret = new ArrayList<String>();
			if (term.startsWith("w/")) {
				ret.add("with");
				term = term.substring(2);
			}
			String[] terms = Term.split(term, "/");
			for (String t : terms) {
				if (!t.isEmpty())
					ret.add(t);
			}
			return ret;			
		}
	}
}
