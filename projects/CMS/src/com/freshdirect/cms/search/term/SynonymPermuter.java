package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.framework.util.PermutationGenerator;

public class SynonymPermuter extends TermCoder {
	private final SynonymDictionary dictionary;

	public SynonymPermuter(SynonymDictionary dictionary, List<Term> terms) {
		super(terms);
		this.dictionary = dictionary;
	}

	public SynonymPermuter(SynonymDictionary dictionary, Term term) {
		super(term);
		this.dictionary = dictionary;
	}

	public SynonymPermuter(SynonymDictionary dictionary, TermCoder coder) {
		super(coder);
		this.dictionary = dictionary;
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> terms = new ArrayList<Term>();
		for (Term term : input)
			terms.addAll(convert(term));
		return terms;
	}

	private List<Term> convert(Term term) {
		List<List<String>> results = new ArrayList<List<String>>();
		results.add(new ArrayList<String>());
		List<String> tokens = term.getTokens();
		OUTER: for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			if (dictionary != null) {
				Synonym[] synonyms = dictionary.getSynonymsForPrefix(token);
				for (Synonym synonym : synonyms) {
					if (i + synonym.getWordCount() > tokens.size())
						continue;
					String test = Term.join(tokens.subList(i, i + synonym.getWordCount()), " "); 
					if (test.equals(synonym.getTermAsString())) {
						addAlternatives(results, synonym);
						i += synonym.getWordCount() - 1;
						continue OUTER;
					}
				}
			}
			addAlternatives(results, getAlternatives(token));
		}
		List<Term> ret = new ArrayList<Term>();
		for (List<String> result : results)
			ret.add(new Term(result));
		return ret;
	}

	private void addAlternatives(List<List<String>> results, Synonym synonyms) {
		int size = results.size();
		for (Term synonym : synonyms.getSynonyms()) {
			for (int j = 0; j < size; j++) {
				List<String> result = results.get(j);
				List<String> newResult = new ArrayList<String>(result);
				newResult.addAll(synonym.getTokens());
				results.add(newResult);
			}
		}
		for (int i = 0; i < size; i++) {
			List<String> result = results.get(i);
			result.addAll(synonyms.getTerm().getTokens());
		}
	}

	private void addAlternatives(List<List<String>> results, List<List<String>> alternatives) {
		int size = results.size();
		for (int i = 1; i < alternatives.size(); i++) {
			List<String> alternative = alternatives.get(i);
			for (int j = 0; j < size; j++) {
				List<String> result = results.get(j);
				List<String> newResult = new ArrayList<String>(result);
				newResult.addAll(alternative);
				results.add(newResult);
			}
		}
		if (alternatives.size() != 0)
			for (int i = 0; i < size; i++) {
				List<String> result = results.get(i);
				result.addAll(alternatives.get(0));
			}
	}

	private List<List<String>> getAlternatives(String term) {
		List<List<String>> alternatives = new ArrayList<List<String>>();
		// the original term is always an alternative
		alternatives.add(Collections.singletonList(term));
		String[] tokens = Term.split(term, "-");
		if (dictionary != null && !isDashedDecimals(tokens)) // dashed decimals are not permuted (e.g. 6-8lbs)
			if (tokens.length > 1) {
				// split synonym finding
				List<List<String>> parts = new ArrayList<List<String>>();
				for (String token : tokens) {
					List<String> part = new ArrayList<String>();
					part.add(token);
					Synonym synonym = dictionary.getSynonymForWord(token);
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
	}
}
