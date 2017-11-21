package com.freshdirect.storeapi.content;

import java.util.Comparator;

public class SortIntValueComparator<T extends ContentNodeModel> implements Comparator<FilteringSortingItem<T>> {
	private EnumSortingValue sortValue;

	public SortIntValueComparator(EnumSortingValue sortValue) {
		super();
		this.sortValue = sortValue;
	}

	@Override
	public int compare(FilteringSortingItem<T> o1, FilteringSortingItem<T> o2) {
		int iv1 = o1.getSortingValue(sortValue).intValue();
		int iv2 = o2.getSortingValue(sortValue).intValue();
		if (sortValue.isAscending())
			return iv1 - iv2;
		else
			return iv2 - iv1;
	}
}
