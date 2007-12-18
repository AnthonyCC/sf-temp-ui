package com.freshdirect.framework.util;

import java.util.Comparator;

public class ReverseComparator implements Comparator {

	private final Comparator comp;

	public ReverseComparator(Comparator comp) {
		this.comp = comp;
	}

	public int compare(Object o1, Object o2) {
		return -comp.compare(o1, o2);
	}

}
