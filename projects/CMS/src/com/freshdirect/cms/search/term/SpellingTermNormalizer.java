package com.freshdirect.cms.search.term;

import java.util.List;

public class SpellingTermNormalizer extends TermCoder {
	private boolean retainPunctuation;

	public SpellingTermNormalizer(List<Term> terms) {
		super(terms);
	}

	public SpellingTermNormalizer(Term term) {
		super(term);
	}

	public SpellingTermNormalizer(TermCoder coder) {
		super(coder);
	}

	public SpellingTermNormalizer(List<Term> terms, boolean retainPunctuation) {
		super(terms);
		this.retainPunctuation = retainPunctuation;
	}

	public SpellingTermNormalizer(Term term, boolean retainPunctuation) {
		super(term);
		this.retainPunctuation = retainPunctuation;
	}

	public SpellingTermNormalizer(TermCoder coder, boolean retainPunctuation) {
		super(coder);
		this.retainPunctuation = retainPunctuation;
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		TermCoder filter = new IdentityConv(input);
		filter = new HtmlUnescapeConv(filter); // this must come first
		filter = new IsoLatin1Filter(filter); // then this
		filter = new DiacriticsRemoval(filter);
		filter = new LowercaseCoder(filter);
		filter = new SpellingInitialismPermuter(filter);
		filter = new CommaPermuter(filter, retainPunctuation);
		filter = new DotPermuter(filter);
		filter = new SpellingPunctuationCoder(filter, retainPunctuation);
		filter = new QuotationPermuter(filter);
		// filter = new PercentConv(filter); no percent conversion
		filter = new AmpersandSpellingPermuter(filter);
		// filter = new PlusSignConv(filter); we do not bother plus signs
		filter = new SlashConv(filter);
		filter = new ApostropheFilter(filter);
		filter = new DashAsteriskFilter(filter, retainPunctuation);
		List<Term> terms = filter.getTerms();
		if (terms.isEmpty())
			terms.add(new Term(""));
		return terms;
	}
}
