package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.List;

public class IsoLatin1Filter extends SingleTokenCoder {
	public IsoLatin1Filter(List<Term> terms) {
		super(terms);
	}

	public IsoLatin1Filter(Term term) {
		super(term);
	}

	public IsoLatin1Filter(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<String> convert(String term) {
		List<String> ret = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < term.length(); i++) {
			if (term.charAt(i) == '\u00ad') // soft-hyphen
				buf.append('-');
			else if (Character.isISOControl(term.charAt(i)) || term.charAt(i) == '\u00a0') { // non-breaking space (nbsp)
				if (buf.length() != 0) {
					ret.add(buf.toString());
					buf = new StringBuilder();
				}
			} else if (term.charAt(i) <= '\u00ff')
				buf.append(term.charAt(i));
		}
		if (buf.length() != 0)
			ret.add(buf.toString());
		return ret;
	}
}
