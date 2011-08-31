package com.freshdirect.cms.search.term;

import java.util.List;

public interface TermCoderFactory {
	public TermCoder create(Term term);

	public TermCoder create(List<Term> terms);
}
