package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DictionaryPermuter extends TermCoder {
	public DictionaryPermuter(List<Term> terms) {
		super(terms);
	}

	public DictionaryPermuter(Term term) {
		super(term);
	}

	public DictionaryPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> output = new ArrayList<Term>();
		Set<String> words = new LinkedHashSet<String>();
		Set<Term> twoWords = new LinkedHashSet<Term>();
		Set<Term> threeWords = new LinkedHashSet<Term>();
		for (Term term : input) {
			List<String> tokens = term.getTokens();
			words.addAll(tokens);
			for (int i = 0; i < term.length() - 1; i++)
				twoWords.add(term.subTerm(i, i + 2));
			for (int i = 0; i < term.length() - 2; i++)
				threeWords.add(term.subTerm(i, i + 3));
		}
		for (String word : words)
			output.add(new Term(Collections.singletonList(word)));
		output.addAll(twoWords);
		output.addAll(threeWords);
		return output;
	}
}
