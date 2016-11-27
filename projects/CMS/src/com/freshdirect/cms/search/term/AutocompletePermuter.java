package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class AutocompletePermuter extends TermCoder {
	public AutocompletePermuter(List<Term> terms) {
		super(terms);
	}

	public AutocompletePermuter(Term term) {
		super(term);
	}

	public AutocompletePermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> output = new ArrayList<Term>();
		for (Term term : input) {
			List<String> tokens = term.getTokens();
			for (int l = 0; l < tokens.size(); l++)
				for (int i = 0; i < tokens.size() - l; i++) {
					List<String> permutation = new ArrayList<String>();
					for (int j = 0; j < l + 1; j++)
						permutation.add(tokens.get(i + j));
					output.add(new Term(permutation));
				}
		}
		return output;
	}
}
