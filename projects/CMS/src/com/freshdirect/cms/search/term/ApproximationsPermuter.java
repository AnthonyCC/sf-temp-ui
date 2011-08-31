package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class ApproximationsPermuter {
	private final Term term;

	public ApproximationsPermuter(Term term) {
		super();
		this.term = term;
	}

	public List<List<Term>> permute() {
		List<List<Term>> permutations = new ArrayList<List<Term>>();
		List<String> tokens = term.getTokens();

		// one word missing permutation
		List<Term> permutation = new ArrayList<Term>();
		for (int i = 0; i < tokens.size(); i++) {
			List<String> permTokens = new ArrayList<String>();
			for (int k = 0; k < tokens.size(); k++)
				if (k != i)
					permTokens.add(tokens.get(k));
			permutation.add(new Term(permTokens));
		}
		permutations.add(permutation);

		// two words missing permutation
		if (tokens.size() > 3) {
			permutation = new ArrayList<Term>();
			for (int i = 0; i < tokens.size(); i++)
				for (int j = i + 1; j < tokens.size(); j++) {
					List<String> permTokens = new ArrayList<String>();
					for (int k = 0; k < tokens.size(); k++)
						if (k != i && k != j)
							permTokens.add(tokens.get(k));
					permutation.add(new Term(permTokens));
				}
			permutations.add(permutation);
		}

		return permutations;
	}
}
