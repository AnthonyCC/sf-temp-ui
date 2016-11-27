package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.framework.util.PermutationGenerator;

public class DashAsteriskSplitPermuter extends PermuterTermCoder {
	public DashAsteriskSplitPermuter(List<Term> terms) {
		super(terms);
	}

	public DashAsteriskSplitPermuter(Term term) {
		super(term);
	}

	public DashAsteriskSplitPermuter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<List<String>> convert(String term) {
		if (term.length() == 1 && "-*".indexOf(term.charAt(0)) != -1)
			return Collections.emptyList();
		String[] terms = Term.split(term, "-*");
		String joinString = "-";
		if (term.indexOf('*') >= 0)
			joinString = "*";
		if (terms.length > 1) {
			List<List<String>> ret = new ArrayList<List<String>>();
			List<String> r = new ArrayList<String>();
			for (String t : terms) {
				if (!t.isEmpty())
					r.add(t);
			}
			if (!r.isEmpty()) {
				if (r.size() > 1)
					ret.add(Collections.singletonList(term));
					
				List<List<String>> permutations = permuteSyms(SynonymDictionary.createNumberSynonyms(), r);
				PermutationGenerator pg = new PermutationGenerator(permutations);
				for (; pg.hasMoreStep(); pg.step()) {
					if (pg.isFirst())
						continue;
					List<String> p = new ArrayList<String>();
					for (int i = 0; i < pg.length(); i++)
						p.add(permutations.get(i).get(pg.get(i)));
					ret.add(Collections.singletonList(Term.join(p, joinString)));
				}
				ret.add(r);
			}
			return ret;
		} else
			return Collections.singletonList(Collections.singletonList(term));
	}
}
