package com.freshdirect.cms.search.term;

import java.util.List;

import org.tartarus.snowball.ext.EnglishStemmer;

public class EnglishStemmerConv extends SingleSingleTokenCoder {
	EnglishStemmer stemmer = new EnglishStemmer();

	public EnglishStemmerConv(List<Term> terms) {
		super(terms);
	}

	public EnglishStemmerConv(Term term) {
		super(term);
	}

	public EnglishStemmerConv(TermCoder coder) {
		super(coder);
	}

	@Override
	protected String convert(String token) {
		stemmer.setCurrent(token);
		stemmer.stem();
		return stemmer.getCurrent();
	}
}
