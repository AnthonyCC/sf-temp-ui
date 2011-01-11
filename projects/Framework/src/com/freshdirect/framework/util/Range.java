package com.freshdirect.framework.util;

import java.io.Serializable;

/**
 * Generic range of Comparables. Start inclusive, end exclusive.
 */
public class Range<T extends Comparable<T>> implements Serializable {

	private static final long	serialVersionUID	= -2280698101305314135L;
	
	private final T start;
	private final T end;

	public Range(T start, T end) {
		if (lessThan(end, start)) {
			throw new IllegalArgumentException("End cannot be before start.");
		}
		this.start = start;
		this.end = end;
	}

	public T getStart() {
		return start;
	}

	public T getEnd() {
		return end;
	}

	public boolean contains(T c) {
		return !lessThan(c, start) && lessThan(c, end);
	}
	
	public boolean containsEx(T c) {
		return !lessThan(c, start) && !moreThan(c, end);
	}

	/**
	 * r1.contains(r2) implies r1.overlaps(r2).
	 * 
	 * @return true if the specified range is completely covered (or equal)
	 */
	public boolean contains(Range<T> range) {
		return (!lessThan(range.start, start) && !moreThan(range.end, end)) && !(range.start.equals(end) && range.end.equals(end));
	}

	/**
	 * r1.overlaps(r2) implies r2.overlaps(r1).
	 * 
	 * @return true if there's any overlap with the specified range
	 */
	public boolean overlaps(Range<T> range) {
		return this.contains(range.start) || range.contains(this.start);
	}

	private boolean lessThan(T o1, T o2) {
		return o1.compareTo(o2) < 0;
	}

	private boolean moreThan(T o1, T o2) {
		return o1.compareTo(o2) > 0;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Range<?>) {
			Range<?> r = (Range<?>) obj;
			return start.equals(r.start) && end.equals(r.end);
		}
		return false;
	}

	public int hashCode() {
		return start.hashCode() ^ end.hashCode();
	}

	public String toString() {
		return "From " + start + " to " + end;
	}

}