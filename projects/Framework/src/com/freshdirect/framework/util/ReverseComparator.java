package com.freshdirect.framework.util;

import java.util.Comparator;

public class ReverseComparator<T> implements Comparator<T> {

	private final Comparator<T> comp;

	public ReverseComparator(Comparator<T> comp) {
		this.comp = comp;
	}

	public int compare(T o1, T o2) {
		return -comp.compare(o1, o2);
	}

}
