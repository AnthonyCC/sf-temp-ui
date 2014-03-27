package com.freshdirect.fdstore.content.browse.sorter;

import com.freshdirect.fdstore.content.FilteringProductItem;

public class NameComparator extends OptionalObjectComparator<FilteringProductItem, String> {

	@Override
	protected String getValue(FilteringProductItem obj) {
		return obj.getProductModel().getFullName();
	}

}
