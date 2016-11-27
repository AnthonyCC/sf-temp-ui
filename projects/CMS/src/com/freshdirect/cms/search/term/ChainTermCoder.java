package com.freshdirect.cms.search.term;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class ChainTermCoder extends TermCoder {
	List<Class<? extends TermCoder>> termCoders = new ArrayList<Class<? extends TermCoder>>();

	public ChainTermCoder(List<Term> terms) {
		super(terms);
	}

	public ChainTermCoder(Term term) {
		super(term);
	}

	public ChainTermCoder(TermCoder coder) {
		super(coder);
	}

	protected void addTermCoder(Class<? extends TermCoder> termCoder) {
		termCoders.add(termCoder);
	}
	
	protected abstract void initCoders();

	@Override
	protected List<Term> convert(List<Term> terms) {
		termCoders.clear();
		initCoders();
		TermCoder filter = new IdentityConv(terms);
		for (Class<? extends TermCoder> clazz : termCoders) {
			try {
				Constructor<? extends TermCoder> constructor = clazz.getConstructor(TermCoder.class);
				filter = constructor.newInstance(filter);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return filter.getTerms();
	}
}
