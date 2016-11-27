package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleSingleTokenCoder extends SingleTermCoder {
	public SingleSingleTokenCoder(List<Term> terms) {
		super(terms);
	}

	public SingleSingleTokenCoder(Term term) {
		super(term);
	}

	public SingleSingleTokenCoder(TermCoder coder) {
		super(coder);
	}

	protected Term convert(Term term) {
		List<String> tokens = new ArrayList<String>(term.getTokens().size());
		for (String token : term.getTokens())
			if (token != null && !token.isEmpty()) {
				String converted = convert(token);
				if (converted != null && !converted.isEmpty())
					tokens.add(converted);
			}
		return new Term(tokens);
	}

	protected abstract String convert(String token);
}
