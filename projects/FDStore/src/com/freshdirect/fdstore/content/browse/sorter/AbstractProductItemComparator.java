package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductModel;

public abstract class AbstractProductItemComparator implements Comparator<FilteringProductItem> {

	@Override
	public int compare(FilteringProductItem o1, FilteringProductItem o2) {
		if (o1 != null) {
			if (o2 != null) {
				// at this point product models are considered to exist
				return compare(o1.getProductModel(), o2.getProductModel());
			} else {
				// o1 > NULL
				return -1;
			}
		} else {
			// NULL < o2
			return o2 != null ? 1 : 0;
		}
	}

	/**
	 * Implement this!
	 * @param p1
	 * @param p2
	 * @return
	 */
	protected abstract int compare(ProductModel p1, ProductModel p2);
}
