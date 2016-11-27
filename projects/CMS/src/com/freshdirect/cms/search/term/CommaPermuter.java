package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class CommaPermuter extends SplitPermuter {
	private boolean retainPunctuation;

	public CommaPermuter(List<Term> terms) {
		super(terms);
	}

	public CommaPermuter(Term term) {
		super(term);
	}

	public CommaPermuter(TermCoder coder) {
		super(coder);
	}

	public CommaPermuter(List<Term> terms, boolean retainPunctuation) {
		super(terms);
		this.retainPunctuation = retainPunctuation;
	}

	public CommaPermuter(Term term, boolean retainPunctuation) {
		super(term);
		this.retainPunctuation = retainPunctuation;
	}

	public CommaPermuter(TermCoder coder, boolean retainPunctuation) {
		super(coder);
		this.retainPunctuation = retainPunctuation;
	}

	@Override
	protected List<String> split(String token) {
		List<String> ret = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < token.length(); i++) {
			char c = token.charAt(i);
			if (c == ',') {
				if (retainPunctuation && i != 0 && i != token.length() - 1) {
					buf.append(c);
				} else if (!((i != 0 && Character.isDigit(token.charAt(i - 1))
						&& (i < token.length() - 1 && Character.isDigit(token.charAt(i + 1)))))) {
					ret.add(buf.toString());
					buf = new StringBuilder();
				}
				// else stripped
			} else
				buf.append(c);
		}
		ret.add(buf.toString());
		return ret;
	}
}
