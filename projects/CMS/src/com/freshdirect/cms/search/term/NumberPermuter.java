package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.framework.util.PermutationGenerator;

public class NumberPermuter extends SingleTokenCoder {
	private SynonymDictionary dictionary;
	private SynonymDictionary numberDictionary;
	private TermCoderFactory factory = new IdentityConvFactory();

	public NumberPermuter(List<Term> terms) {
		super(terms);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(Term term) {
		super(term);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(TermCoder coder) {
		super(coder);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, List<Term> terms) {
		super(terms);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, Term term) {
		super(term);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, TermCoder coder) {
		super(coder);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(Term term, boolean skipNumberSynonyms) {
		super(term);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, Term term, boolean skipNumberSynonyms) {
		super(term);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public NumberPermuter(SynonymDictionary dictionary, TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	@Override
	protected List<String> convert(String term) {
		List<String> words = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < term.length(); i++) {
			char c = term.charAt(i);
			buf.append(c);
			if ((Character.isDigit(c) && i + 1 < term.length() && Character.isLetter(term.charAt(i + 1)))
					|| (Character.isLetter(c) && i + 1 < term.length() && Character.isDigit(term.charAt(i + 1)))) {
				if (buf.length() != 0) {
					words.add(buf.toString());
					buf = new StringBuilder();
				}
			}
		}
		if (buf.length() != 0) {
			words.add(buf.toString());
		}
		if (dictionary != null && words.size() > 1) {
			Set<Term> synonyms = new LinkedHashSet<Term>();
			synonyms.add(new Term(words));
			List<List<String>> numberSyms = permuteSyms(numberDictionary, words);
			PermutationGenerator pg = new PermutationGenerator(numberSyms);
			for (; pg.hasMoreStep(); pg.step()) {
				StringBuilder buf2 = new StringBuilder();
				List<String> tokens = new ArrayList<String>();
				for (int i = 0; i < pg.length(); i++) {
					buf2.append(numberSyms.get(i).get(pg.get(i)));
					tokens.add(numberSyms.get(i).get(pg.get(i)));
				}
				synonyms.add(new Term(tokens));
				synonyms.add(new Term(buf2.toString()));
			}
			dictionary.addSynonyms(synonyms, factory);
		}
		return Collections.singletonList(Term.join(words, ""));
	}
}
