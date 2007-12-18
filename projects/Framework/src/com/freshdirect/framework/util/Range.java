package com.freshdirect.framework.util;

import java.io.Serializable;

/**
 * Generic range of Comparables. Start inclusive, end exclusive.
 */
public class Range implements Serializable {

	private final Comparable start;
	private final Comparable end;

	public Range(Comparable start, Comparable end) {
		if (lessThan(end, start)) {
			throw new IllegalArgumentException("End cannot be before start.");
		}
		this.start = start;
		this.end = end;
	}

	public Comparable getStart() {
		return start;
	}

	public Comparable getEnd() {
		return end;
	}

	public boolean contains(Comparable c) {
		return !lessThan(c, start) && lessThan(c, end);
	}

	/**
	 * r1.contains(r2) implies r1.overlaps(r2).
	 * 
	 * @return true if the specified range is completely covered (or equal)
	 */
	public boolean contains(Range range) {
		return (!lessThan(range.start, start) && !moreThan(range.end, end)) && !(range.start.equals(end) && range.end.equals(end));
	}

	/**
	 * r1.overlaps(r2) implies r2.overlaps(r1).
	 * 
	 * @return true if there's any overlap with the specified range
	 */
	public boolean overlaps(Range range) {
		// return !(lessThan(range.start, start) && !moreThan(range.end, start)) && !(!lessThan(range.start, end) && !lessThan(range.end, end));
		return this.contains(range.start) || range.contains(this.start);
	}

	private boolean lessThan(Comparable o1, Comparable o2) {
		return o1.compareTo(o2) < 0;
	}

	private boolean moreThan(Comparable o1, Comparable o2) {
		return o1.compareTo(o2) > 0;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Range) {
			Range r = (Range) obj;
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