package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class SpellingPunctuationCoder extends SingleTokenCoder {
	private final static String regulars = "!?[]{}<>:;_=~@\\¡¦|§¨©«¬®¯±`´¶·¸»¿";
	
	private boolean retainPunctuation;

	public SpellingPunctuationCoder(List<Term> terms) {
		super(terms);
	}

	public SpellingPunctuationCoder(Term term) {
		super(term);
	}

	public SpellingPunctuationCoder(TermCoder coder) {
		super(coder);
	}

	public SpellingPunctuationCoder(List<Term> terms, boolean retainPunctuation) {
		super(terms);
		this.retainPunctuation = retainPunctuation;
	}

	public SpellingPunctuationCoder(Term term, boolean retainPunctuation) {
		super(term);
		this.retainPunctuation = retainPunctuation;
	}

	public SpellingPunctuationCoder(TermCoder coder, boolean retainPunctuation) {
		super(coder);
		this.retainPunctuation = retainPunctuation;
	}

	@Override
	protected List<String> convert(String term) {
		List<String> ret = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < term.length(); i++) {
			char c = term.charAt(i);
			if (regulars.indexOf(c) != -1) {
				if (retainPunctuation) {
					buf.append(c);
				} else if (buf.length() != 0) {
					ret.add(buf.toString());
					buf = new StringBuilder();
				}
			} else if (c == '(' || c == ')') {
				if (isBeforeWhiteSpace(term, i) || isAfterWhiteSpace(term, i))
					if (buf.length() != 0) {
						ret.add(buf.toString());
						buf = new StringBuilder();
					}
				// else stripped
				// DO NOT bother commas or dots
			} else
				buf.append(c);
		}
		if (buf.length() != 0) {
			ret.add(buf.toString());
			buf = new StringBuilder();
		}
		return ret;
	}

	private static boolean isBeforeWhiteSpace(String term, int i) {
		if (i == 0)
			return true;
		else
			return Character.isWhitespace(term.charAt(i - 1));
	}

	private static boolean isAfterWhiteSpace(String term, int i) {
		if (i >= term.length() - 1)
			return true;
		else
			return Character.isWhitespace(term.charAt(i + 1));
	}
}
