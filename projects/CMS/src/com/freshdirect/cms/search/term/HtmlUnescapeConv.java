package com.freshdirect.cms.search.term;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlUnescapeConv extends SingleSingleTokenCoder {
	public HtmlUnescapeConv(List<Term> terms) {
		super(terms);
	}

	public HtmlUnescapeConv(Term term) {
		super(term);
	}

	public HtmlUnescapeConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String term) {
		return StringEscapeUtils.unescapeHtml(term);
	}
}
