package com.freshdirect.cms.search.term;

import java.util.List;

import com.freshdirect.cms.search.SynonymDictionary;

public class SearchTermNormalizer extends TermCoder {
	private SynonymDictionary dictionary;
	private boolean skipNumberSynonyms;
	private boolean retainPunctuation;

	public SearchTermNormalizer(List<Term> terms) {
		super(terms);
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(Term term) {
		super(term);
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(TermCoder coder) {
		super(coder);
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, List<Term> terms) {
		super(terms);
		this.dictionary = dictionary;
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, Term term) {
		super(term);
		this.dictionary = dictionary;
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, TermCoder coder) {
		super(coder);
		this.dictionary = dictionary;
		skipNumberSynonyms = false;
	}

	public SearchTermNormalizer(List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		this.skipNumberSynonyms = skipNumberSynonyms;
	}

	public SearchTermNormalizer(Term term, boolean skipNumberSynonyms) {
		super(term);
		this.skipNumberSynonyms = skipNumberSynonyms;
	}

	public SearchTermNormalizer(TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		this.skipNumberSynonyms = skipNumberSynonyms;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		this.dictionary = dictionary;
		this.skipNumberSynonyms = skipNumberSynonyms;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, Term term, boolean skipNumberSynonyms) {
		super(term);
		this.dictionary = dictionary;
		this.skipNumberSynonyms = skipNumberSynonyms;
	}

	public SearchTermNormalizer(SynonymDictionary dictionary, TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		this.dictionary = dictionary;
		this.skipNumberSynonyms = skipNumberSynonyms;
	}
	
	public SearchTermNormalizer(Term searchTerm, boolean skipNumberSynonyms, boolean retainPunctuation) {
		super(searchTerm);
		this.skipNumberSynonyms = skipNumberSynonyms;
		this.retainPunctuation = retainPunctuation;
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		TermCoder filter = new IdentityConv(input);
		filter = new HtmlUnescapeConv(filter); // this must come first
		filter = new IsoLatin1Filter(filter); // then this
		filter = new DiacriticsRemoval(filter); // then might be this one
		filter = new LowercaseCoder(filter);
		filter = new InitialismPermuter(filter);
		filter = new PunctuationCoder(filter, retainPunctuation);
		filter = new QuotationConv(filter);
		filter = new PercentConv(filter);
		filter = new AmpersandConv(filter);
		filter = new PlusSignConv(filter);
		filter = new SlashConv(filter);
		filter = new ApostropheConv(filter);
		filter = new DashAsteriskPermuter(dictionary, filter, skipNumberSynonyms);
		filter = new NumberPermuter(dictionary, filter, skipNumberSynonyms);
		List<Term> terms = filter.getTerms();
		if (terms.isEmpty())
			terms.add(new Term(""));
		return terms;
	}
}
