package com.freshdirect.cms.search.term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class PermuterTermCoder extends TermCoder {
	public PermuterTermCoder(List<Term> terms) {
		super(terms);
	}

	public PermuterTermCoder(Term term) {
		super(term);
	}

	public PermuterTermCoder(TermCoder coder) {
		super(coder);
	}

	@Override
	protected List<Term> convert(List<Term> input) {
		List<Term> ret = new ArrayList<Term>();
		for (Term term : input) {
			List<List<String>> sofar = new ArrayList<List<String>>();
			sofar.add(new ArrayList<String>());
			for (String token : term.getTokens())
				permute(sofar, normalize(convert(token)));
			for (List<String> sf : sofar)
				ret.add(new Term(sf));
		}
		return ret;
	}
	
	private void permute(List<List<String>> sofar, List<List<String>> result) {
		if (result.size() == 0)
			return;
		else if (result.size() == 1) {
			List<String> r = result.get(0);
			for (List<String> item : sofar)
				item.addAll(r);
		} else {
			List<List<String>> perms = new ArrayList<List<String>>();
			Iterator<List<String>> itr = result.iterator();
			List<String> first = Collections.emptyList();
			if (itr.hasNext())
				first = itr.next();
			while (itr.hasNext()) {
				List<String> add = itr.next();
				List<List<String>> dup = new ArrayList<List<String>>();
				for (List<String> sf : sofar)
					dup.add(new ArrayList<String>(sf));
				for (List<String> d : dup)
					d.addAll(add);
				perms.addAll(dup);
			}
			for (List<String> sf : sofar)
				sf.addAll(first);
			sofar.addAll(perms);
		}
	}
	
	private List<List<String>> normalize(List<List<String>> input) {
		List<List<String>> ret = new ArrayList<List<String>>();
		if (input != null) {
			for (List<String> item : input) {
				if (item != null) {
					List<String> n = new ArrayList<String>();
					for (String s : item) {
						if (s != null) {
							if (!s.isEmpty())
								n.add(s);
						}
					}
					if (!n.isEmpty())
						ret.add(n);
				}
			}
		}
		return ret;
	}

	protected abstract List<List<String>> convert(String token);
}
