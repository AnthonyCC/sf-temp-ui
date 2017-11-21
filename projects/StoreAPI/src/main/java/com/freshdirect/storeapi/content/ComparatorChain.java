package com.freshdirect.storeapi.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorChain<T> implements Comparator<T>, Serializable {
	private static final long serialVersionUID = 6452446405942321770L;

	public static <T> ComparatorChain<T> create(Comparator<T> comparator) {
		return new ComparatorChain<T>(comparator);
	}

	public static <T> ComparatorChain<T> create(Comparator<T>... comparators) {
		return new ComparatorChain<T>(comparators);
	}

	public static <T> ComparatorChain<T> create(List<Comparator<T>> comparators) {
		return new ComparatorChain<T>(comparators);
	}

	public static <T> ComparatorChain<T> reverseOrder(
			ComparatorChain<T> comparator) {
		return create(Collections.reverseOrder(comparator));
	};
	
	private List<Comparator<T>> comparators;
	
	private ComparatorChain(Comparator<T> comparator) {
		this.comparators = new ArrayList<Comparator<T>>();
		this.comparators.add(comparator);
	}

	private ComparatorChain(Comparator<T>... comparators) {
		this.comparators = new ArrayList<Comparator<T>>();
		for (Comparator<T> comparator : comparators)
			this.comparators.add(comparator);
	}

	private ComparatorChain(List<Comparator<T>> comparators) {
		this.comparators = new ArrayList<Comparator<T>>();
		this.comparators.addAll(comparators);
	}

	public ComparatorChain<T> chain(Comparator<T> comparator) {
		this.comparators.add(comparator);
		return this;
	}

	public ComparatorChain<T> chain(Comparator<T>... comparators) {
		for (Comparator<T> comparator : comparators)
			this.comparators.add(comparator);
		return this;
	}

	public ComparatorChain<T> chain(List<Comparator<T>> comparators) {
		this.comparators.addAll(comparators);
		return this;
	}

	public ComparatorChain<T> prepend(Comparator<T> comparator) {
		this.comparators.add(0, comparator);
		return this;
	}
	
	public int compare(T o1, T o2) {
		for (Comparator<T> comparator : comparators) {
			int d = comparator.compare(o1, o2);
			if (d != 0)
				return d;
		}
		return 0;
	}
}
