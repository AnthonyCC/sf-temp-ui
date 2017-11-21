package com.freshdirect.storeapi.content;

import java.util.Comparator;

public class SortLongValueComparator<T extends ContentNodeModel> implements Comparator<FilteringSortingItem<T>> {
	private EnumSortingValue sortValue;

	public SortLongValueComparator(EnumSortingValue sortValue) {
		super();
		this.sortValue = sortValue;
	}

	@Override
	public int compare(FilteringSortingItem<T> o1, FilteringSortingItem<T> o2) {
		long lv1 = o1.getSortingValue(sortValue).longValue();
		long lv2 = o2.getSortingValue(sortValue).longValue();
		if (sortValue.isAscending())
			return compare(lv1, lv2);
		else
			return compare(lv2, lv1);
	}

	private int compare(long v1, long v2) {
		long d = v1 - v2;
		if (d == 0)
			return 0;
		else if (d < 0l)
			return -1;
		else
			return 1;
	}
}
