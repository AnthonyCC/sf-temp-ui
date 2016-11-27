package com.freshdirect.cms.search.term;

import java.util.List;

public class TermScoreTermNormalizer extends TermCoder {
	public TermScoreTermNormalizer(List<Term> terms) {
		super(terms);
	}

	public TermScoreTermNormalizer(Term term) {
		super(term);
	}

	public TermScoreTermNormalizer(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		TermCoder filter = new IdentityConv(input);
		filter = new HtmlUnescapeConv(filter); // this must come first
		filter = new IsoLatin1Filter(filter); // then this
		filter = new DiacriticsRemoval(filter); // then might be this one
		filter = new LowercaseCoder(filter);
		filter = new InitialismPermuter(filter);
		filter = new PunctuationCoder(filter);
		filter = new QuotationConv(filter);
		filter = new PercentConv(filter);
		filter = new AmpersandConv(filter);
		filter = new PlusSignConv(filter);
		filter = new SlashConv(filter);
		filter = new DashAsteriskPermuter(filter);
		filter = new NumberPermuter(filter);
		List<Term> terms = filter.getTerms();
		if (terms.isEmpty())
			terms.add(new Term(""));
		return terms;
	}
}
