package com.freshdirect.cms.search.term;

import java.util.List;

public class AutocompleteTermNormalizer extends TermCoder {
	private boolean removeDiactrics;

	public AutocompleteTermNormalizer(List<Term> terms) {
		super(terms);
		removeDiactrics = false;
	}

	public AutocompleteTermNormalizer(Term term) {
		super(term);
		removeDiactrics = false;
	}

	public AutocompleteTermNormalizer(TermCoder coder) {
		super(coder);
		removeDiactrics = false;
	}

	public AutocompleteTermNormalizer(List<Term> terms, boolean removeDiactrics) {
		super(terms);
		this.removeDiactrics = removeDiactrics;
	}

	public AutocompleteTermNormalizer(Term term, boolean removeDiactrics) {
		super(term);
		this.removeDiactrics = removeDiactrics;
	}

	public AutocompleteTermNormalizer(TermCoder coder, boolean removeDiactrics) {
		super(coder);
		this.removeDiactrics = removeDiactrics;
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		TermCoder filter = new IdentityConv(input);
		filter = new HtmlUnescapeConv(filter); // this must come first
		filter = new IsoLatin1Filter(filter); // then this
		if (removeDiactrics)
			filter = new DiacriticsRemoval(filter);
		filter = new LowercaseCoder(filter);
		filter = new QuotationFilter(filter);
		filter = new BracketFilter(filter);
		filter = new SlashConv(filter);
		List<Term> terms = filter.getTerms();
		if (terms.isEmpty())
			terms.add(new Term(""));
		return terms;
	}
}
