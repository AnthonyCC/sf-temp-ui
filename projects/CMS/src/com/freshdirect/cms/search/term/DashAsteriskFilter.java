package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.framework.util.PermutationGenerator;

public class DashAsteriskFilter extends PermuterTermCoder {
	private boolean retainPunctuation;
	private SynonymDictionary numberSynonyms;

	public DashAsteriskFilter(List<Term> terms) {
		super(terms);
		numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskFilter(Term term) {
		super(term);
		numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskFilter(TermCoder coder) {
		super(coder);
		numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskFilter(List<Term> terms, boolean retainPuncutation) {
		super(terms);
		this.retainPunctuation = retainPuncutation;
		if (!retainPuncutation)
			numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskFilter(Term term, boolean retainPuncutation) {
		super(term);
		this.retainPunctuation = retainPuncutation;
		if (!retainPuncutation)
			numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}

	public DashAsteriskFilter(TermCoder coder, boolean retainPuncutation) {
		super(coder);
		this.retainPunctuation = retainPuncutation;
		if (!retainPuncutation)
			numberSynonyms = SynonymDictionary.createNumberSynonyms();
	}
	
	@Override
	protected List<List<String>> convert(String term) {
		if (!retainPunctuation && term.length() == 1 && "-*".indexOf(term.charAt(0)) != -1)
			return null;
		if (term.length() > 1 && "-*".indexOf(term.charAt(0)) != -1)
			return Collections.singletonList(Collections.singletonList(term.substring(1)));
		if (term.length() > 1 && "-*".indexOf(term.charAt(term.length() - 1)) != -1)
			return Collections.singletonList(Collections.singletonList(term.substring(0, term.length() - 1)));
		if (numberSynonyms != null) {
			List<List<String>> alternatives = new ArrayList<List<String>>();
			// the original term is always an alternative
			alternatives.add(Collections.singletonList(term));
			String[] tokens = Term.split(term, "-");
			if (numberSynonyms != null && !isDashedDecimals(tokens)) // dashed decimals are not permuted (e.g. 6-8lbs)
				if (tokens.length > 1) {
					// split synonym finding
					List<List<String>> parts = new ArrayList<List<String>>();
					for (String token : tokens) {
						List<String> part = new ArrayList<String>();
						part.add(token);
						Synonym synonym = numberSynonyms.getSynonymForWord(token);
						if (synonym != null) {
							for (Term alt : synonym.getSynonyms())
								if (alt.getTokens().size() == 1)
									part.add(alt.getTokens().get(0));
						}
						parts.add(part);
					}
					PermutationGenerator pg = new PermutationGenerator(parts);
					for (; pg.hasMoreStep(); pg.step()) {
						if (pg.isFirst())
							continue;
						alternatives.add(joinParts(parts, pg.getIndex()));
					}
					
				}
			return alternatives;
		} else {
			return Collections.singletonList(Collections.singletonList(term));
		}
	}
}
