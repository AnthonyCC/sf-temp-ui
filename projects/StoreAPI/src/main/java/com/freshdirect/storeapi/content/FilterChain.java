package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

/**
 * Filter chain
 * 
 * @author segabor
 * 
 */
public class FilterChain extends AbstractProductFilter {
	List<AbstractProductFilter> filters;

	public FilterChain(List<AbstractProductFilter> filters) {
		this.filters = filters;
	}

	/**
	 * Convenience method
	 */
	public FilterChain(AbstractProductFilter f1, AbstractProductFilter f2) {
		this.filters = new ArrayList<AbstractProductFilter>();
		if (f1 != null)
			this.filters.add(f1);
		if (f2 != null)
			this.filters.add(f2);
	}

	public void add(AbstractProductFilter a) {
		this.filters.add(a);
	}

	public boolean applyTest(ProductModel prod) throws FDResourceException {
		for (AbstractProductFilter filter : filters) {
			if (!filter.applyTest(prod))
				return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return filters.isEmpty();
	}
}
