package com.freshdirect.storeapi.content;

import java.util.Comparator;

public class SortValueComparator<T extends ContentNodeModel> implements Comparator<FilteringSortingItem<T>> {
	private EnumSortingValue sortValue;

	public SortValueComparator(EnumSortingValue sortValue) {
		super();
		this.sortValue = sortValue;
	}

	@Override
	public int compare(FilteringSortingItem<T> o1, FilteringSortingItem<T> o2) {
		float fv1 = o1.getSortingValue(sortValue).floatValue();
		float fv2 = o2.getSortingValue(sortValue).floatValue();
		if (sortValue.isAscending())
			return Float.compare(fv1, fv2);
		else
			return Float.compare(fv2, fv1);
	}
}
