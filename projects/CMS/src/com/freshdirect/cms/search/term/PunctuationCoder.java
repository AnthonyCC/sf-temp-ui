package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class PunctuationCoder extends SingleTokenCoder {
	private final static String regulars = "!?[]{}<>:;_=~@\\¡¦|§¨©«¬®¯±`´¶·¸»¿";
	
	private boolean retainPunctuation;

	public PunctuationCoder(List<Term> terms) {
		super(terms);
	}

	public PunctuationCoder(Term term) {
		super(term);
	}

	public PunctuationCoder(TermCoder coder) {
		super(coder);
	}

	public PunctuationCoder(List<Term> terms, boolean stripPunctuation) {
		super(terms);
		this.retainPunctuation = stripPunctuation;
	}

	public PunctuationCoder(Term term, boolean stripPunctuation) {
		super(term);
		this.retainPunctuation = stripPunctuation;
	}
	
	public PunctuationCoder(TermCoder coder, boolean retainPunctuation) {
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
			} else if (c == '.') {
				if (isBeforeDigit(term, i) && isAfterDigit(term, i))
					buf.append(c);
				else if (buf.length() != 0) {
					ret.add(buf.toString());
					buf = new StringBuilder();
				}
			} else if (c == ',') {
				if (retainPunctuation && i != 0 && i != term.length() - 1) {
					buf.append(c);
				} else if (!(isBeforeDigit(term, i) && isAfterDigit(term, i))) {
					if (buf.length() != 0) {
						ret.add(buf.toString());
						buf = new StringBuilder();
					}
				}
				// else stripped
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

	private static boolean isBeforeDigit(String term, int i) {
		if (i == 0)
			return true;
		else
			return Character.isDigit(term.charAt(i - 1));
	}

	private static boolean isAfterDigit(String term, int i) {
		if (i >= term.length() - 1)
			return true;
		else
			return Character.isDigit(term.charAt(i + 1));
	}
}
