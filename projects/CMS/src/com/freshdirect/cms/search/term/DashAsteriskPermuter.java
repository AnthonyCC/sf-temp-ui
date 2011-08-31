package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.framework.util.PermutationGenerator;

public class DashAsteriskPermuter extends SingleTokenCoder {
	private SynonymDictionary dictionary;
	private SynonymDictionary numberDictionary;
	private TermCoderFactory factory = new IdentityConvFactory();

	public DashAsteriskPermuter(List<Term> terms) {
		super(terms);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(Term term) {
		super(term);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(TermCoder coder) {
		super(coder);
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, List<Term> terms) {
		super(terms);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, Term term) {
		super(term);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, TermCoder coder) {
		super(coder);
		this.dictionary = dictionary;
		numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(Term term, boolean skipNumberSynonyms) {
		super(term);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, List<Term> terms, boolean skipNumberSynonyms) {
		super(terms);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, Term term, boolean skipNumberSynonyms) {
		super(term);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskPermuter(SynonymDictionary dictionary, TermCoder coder, boolean skipNumberSynonyms) {
		super(coder);
		this.dictionary = dictionary;
		if (!skipNumberSynonyms)
			numberDictionary = SynonymDictionary.createNumberSynonyms();
	}
	
	@Override
	protected List<String> convert(String term) {
		List<String> words = new ArrayList<String>();
		String[] terms = Term.split(term, "-*");
		boolean ignoreConcat = false;
		for (int i = 0; i < terms.length; i++) {
			String t = terms[i];
			if (endsWithDigit(terms, i) && startsWithDigit(terms, i + 1))
				ignoreConcat = true;
			if (!t.isEmpty()) {
				words.add(t);
			}
		}
		if (dictionary != null && !ignoreConcat && words.size() > 1) {
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
		if (ignoreConcat)
			return words;
		else
			return Collections.singletonList(Term.join(words, ""));
	}

	private boolean startsWithDigit(String[] terms, int i) {
		return i < terms.length && !terms[i].isEmpty() && Character.isDigit(terms[i].charAt(0));
	}

	private boolean endsWithDigit(String[] terms, int i) {
		String t = terms[i];
		return !t.isEmpty() && Character.isDigit(t.charAt(t.length() - 1));
	}
}
