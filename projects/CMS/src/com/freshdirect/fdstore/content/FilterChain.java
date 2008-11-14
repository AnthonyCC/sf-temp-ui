package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

/**
 * Filter chain
 * 
 * @author segabor
 *
 */
public class FilterChain extends AbstractProductFilter {
	// List<AbstractProductFilter>
	List filters;

	public FilterChain(List filters) {
		this.filters = filters;
	}

	/**
	 * Convenience method
	 */
	public FilterChain(AbstractProductFilter f1, AbstractProductFilter f2) {
		this.filters = new ArrayList();
		if (f1 != null)
			this.filters.add(f1);
		if (f2 != null)
			this.filters.add(f2);
	}

	public boolean applyTest(ProductModel prod) throws FDResourceException {
		for (Iterator it=filters.iterator(); it.hasNext();) {
			AbstractProductFilter filter = (AbstractProductFilter) it.next();
			if (!filter.applyTest(prod))
				return false;
		}

		return true;
	}
}
