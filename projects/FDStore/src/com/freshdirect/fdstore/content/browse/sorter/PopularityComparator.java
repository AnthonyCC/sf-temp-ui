package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FilteringProductItem;

public class PopularityComparator implements Comparator<FilteringProductItem> {

	private static final Comparator<ContentNodeModel> POP_COMP = new com.freshdirect.fdstore.content.sort.PopularityComparator();
	
	@Override
	public int compare(FilteringProductItem o1, FilteringProductItem o2) {
		return POP_COMP.compare(o1.getProductModel(), o2.getProductModel());
	}
}
