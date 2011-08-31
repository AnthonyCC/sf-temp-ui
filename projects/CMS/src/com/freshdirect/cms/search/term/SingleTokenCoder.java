package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTokenCoder extends SingleTermCoder {
	public SingleTokenCoder(List<Term> terms) {
		super(terms);
	}

	public SingleTokenCoder(Term term) {
		super(term);
	}

	public SingleTokenCoder(TermCoder coder) {
		super(coder);
	}

	protected Term convert(Term term) {
		List<String> tokens = new ArrayList<String>(term.getTokens().size());
		for (String token : term.getTokens())
			if (token != null && !token.isEmpty()) {
				List<String> converted = convert(token);
				if (converted != null)
					for (String c : converted)
						if (c != null && !c.isEmpty())
							tokens.add(c);
			}
		return new Term(tokens);
	}

	protected abstract List<String> convert(String token);
}
